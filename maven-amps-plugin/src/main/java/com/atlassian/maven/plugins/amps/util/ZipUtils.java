package com.atlassian.maven.plugins.amps.util;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Lists;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ZipUtils
{
    public static void unzip(final File zipFile, final String destDir) throws IOException
    {
        unzip(zipFile, destDir, 0);
    }

    /**
     * Unzips a file
     *
     * @param zipFile
     *            the Zip file
     * @param destDir
     *            the destination folder
     * @param leadingPathSegmentsToTrim
     *            number of root folders to skip. Example: If all files are in generated-resources/home/*,
     *            then you may want to skip 2 folders.
     * @throws IOException
     */
    public static void unzip(final File zipFile, final String destDir, int leadingPathSegmentsToTrim) throws IOException
    {
        final ZipFile zip = new ZipFile(zipFile);
        try
        {
            final Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements())
            {
                final ZipEntry zipEntry = entries.nextElement();
                String zipPath = trimPathSegments(zipEntry.getName(), leadingPathSegmentsToTrim);
                final File file = new File(destDir + "/" + zipPath);
                if (zipEntry.isDirectory())
                {
                    file.mkdirs();
                    continue;
                }
                // make sure our parent exists in case zipentries are out of order
                if (!file.getParentFile().exists())
                {
                    file.getParentFile().mkdirs();
                }

                InputStream is = null;
                OutputStream fos = null;
                try
                {
                    is = zip.getInputStream(zipEntry);
                    fos = new FileOutputStream(file);
                    IOUtils.copy(is, fos);
                }
                finally
                {
                    IOUtils.closeQuietly(is);
                    IOUtils.closeQuietly(fos);
                }
                file.setLastModified(zipEntry.getTime());
            }
        }
        finally
        {
            try
            {
                zip.close();
            }
            catch (IOException e)
            {
                // ignore
            }
        }
    }

    /**
     * Count the number of nested root folders. A root folder is a folder which contains 0 or 1 file or folder.
     *
     * Example: A zip with only "generated-resources/home/database.log" has 2 root folders.
     *
     * @param zip the zip file
     * @return the number of root folders.
     */
    public static int countNestingLevel(File zip) throws ZipException, IOException
    {
        List<String> filenames = toList(new ZipFile(zip).entries());

        // We need to remove the root folders from the list before searching for the common root. Example:
        // root/ <- to be removed
        // root/nested/ <- to be removed
        // root/nested/file1.txt
        // root/nested/file2.txt

        String root = "";
        Iterator<String> filenameIterator = filenames.iterator();
        while (filenameIterator.hasNext())
        {
            String filename = filenameIterator.next();
            if (filename.startsWith(root))
            {
                // Append the root
                root = filename;
                // Remove the element
                filenameIterator.remove();
            }
            else
            {
                // The root is not longer
                break;
            }
        }
        // Now the first root folders won't disturb the search for the prefix
        String prefix = StringUtils.getCommonPrefix(filenames.toArray(new String[filenames.size()]));
        if (!prefix.endsWith("/"))
        {
            prefix = prefix.substring(0, prefix.lastIndexOf("/") + 1);
        }
        return StringUtils.countMatches(prefix, "/");
    }

    private static List<String> toList(final Enumeration<? extends ZipEntry> entries)
    {
        List<String> filenamesList = Lists.newArrayList();
        while (entries.hasMoreElements())
        {
            final ZipEntry zipEntry = entries.nextElement();
            filenamesList.add(zipEntry.getName());
        }
        return filenamesList;
    }

    public static void zipDir(final File zipFile, final File srcDir, final String prefix) throws IOException
    {
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));
        try
        {
            addZipPrefixes(srcDir, out, prefix);
            addZipDir(srcDir, out, prefix);
        }
        finally
        {
            // Complete the ZIP file
            IOUtils.closeQuietly(out);
        }
    }

    private static void addZipPrefixes(File dirObj, ZipOutputStream out, String prefix) throws IOException
    {
        // need to manually add the prefix folders
        String entryPrefix = ensurePrefixWithSlash(dirObj, prefix);

        String[] prefixes = entryPrefix.split("/");
        String lastPrefix = "";
        for (int i = 0; i < prefixes.length; i++)
        {
            ZipEntry entry = new ZipEntry(lastPrefix + prefixes[i] + "/");
            out.putNextEntry(entry);
            out.closeEntry();

            lastPrefix = prefixes[i] + "/";
        }
    }

    private static void addZipDir(File dirObj, ZipOutputStream out, String prefix) throws IOException
    {
        File[] files = dirObj.listFiles();
        byte[] tmpBuf = new byte[1024];
        File currentFile;
        String entryPrefix = ensurePrefixWithSlash(dirObj, prefix);
        String entryName = "";

        for (int i = 0; i < files.length; i++)
        {
            currentFile = files[i];
            if (currentFile.isDirectory())
            {
                entryName = entryPrefix + currentFile.getName() + "/";

                // need to manually add folders so entries are in order
                ZipEntry entry = new ZipEntry(entryName);
                out.putNextEntry(entry);
                out.closeEntry();

                // add the files in the folder
                addZipDir(currentFile, out, entryName);
            }
            else if (currentFile.isFile())
            {

                entryName = entryPrefix + currentFile.getName();
                FileInputStream in = new FileInputStream(currentFile.getAbsolutePath());
                try
                {
                    out.putNextEntry(new ZipEntry(entryName));
                    // Transfer from the file to the ZIP file
                    int len;
                    while ((len = in.read(tmpBuf)) > 0)
                    {
                        out.write(tmpBuf, 0, len);
                    }

                    // Complete the entry
                    out.closeEntry();
                }
                finally
                {
                    IOUtils.closeQuietly(in);
                }
            }
        }
    }

    private static String ensurePrefixWithSlash(File rootDir, String prefix)
    {
        String entryPrefix = prefix;

        if (StringUtils.isNotBlank(entryPrefix) && !entryPrefix.equals("/"))
        {
            // strip leading '/'
            if (entryPrefix.charAt(0) == '/')
            {
                entryPrefix = entryPrefix.substring(1);
            }
            // ensure trailing '/'
            if (entryPrefix.charAt(entryPrefix.length() - 1) != '/')
            {
                entryPrefix = entryPrefix + "/";
            }
        }
        else
        {
            entryPrefix = rootDir.getName() + "/";
        }

        return entryPrefix;
    }

    private static String trimPathSegments(String zipPath, final int trimLeadingPathSegments)
    {
        int startIndex = 0;
        for (int i = 0; i < trimLeadingPathSegments; i++)
        {
            int nextSlash = zipPath.indexOf("/", startIndex);
            if (nextSlash == -1)
            {
                break;
            }
            else
            {
                startIndex = nextSlash + 1;
            }
        }

        return zipPath.substring(startIndex);
    }

}

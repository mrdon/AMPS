package com.atlassian.maven.plugins.amps.util;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;
import java.util.Enumeration;

public class ZipUtils {

    public static void unzip(final File zipFile, final String destDir) throws IOException
    {
        unzip(zipFile, destDir, 0);
    }

    public static void unzip(final File zipFile, final String destDir, int leadingPathSegmentsToTrim) throws IOException
    {
        final ZipFile zip = new ZipFile(zipFile);
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
        }
    }

    private static String trimPathSegments(String zipPath, final int trimLeadingPathSegments)
    {
        for (int i = 0; i < trimLeadingPathSegments; i++)
        {
            int nextSlash = zipPath.indexOf("/");
            if (nextSlash == -1) {
                break;
            }
            zipPath = zipPath.substring(nextSlash);
        }
        return zipPath;
    }

}

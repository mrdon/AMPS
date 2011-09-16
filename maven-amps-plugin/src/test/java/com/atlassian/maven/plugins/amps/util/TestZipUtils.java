package com.atlassian.maven.plugins.amps.util;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.internal.util.Lists;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class TestZipUtils
{
    public static final int NUM_FILES = 2;
    public static final int NUM_FOLDERS = 4;
    public static final int NUM_FOLDERS_NESTED_PREFIX = NUM_FOLDERS + 1;

    public static final String ROOT_DIR = "test-zip-dir";
    public static final String FIRST_PREFIX = "prefix1";
    public static final String SECOND_PREFIX = "prefix2";
    public static final String NESTED_PREFIX = FIRST_PREFIX + "/" + SECOND_PREFIX;

    private File tempDir;
    private File sourceZipDir;
    private ZipFile zip;

    @Before
    public void ensureDirsExist() throws IOException
    {

        // Create the temp dir
        final File sysTempDir = new File(System.getProperty("java.io.tmpdir"));
        String dirName = UUID.randomUUID().toString();
        tempDir = new File(sysTempDir, dirName);
        tempDir.mkdirs();

        // Create our test source tree
        sourceZipDir = new File(tempDir, ROOT_DIR);
        File level2sub1 = new File(sourceZipDir, "level2sub1");
        File level2sub2 = new File(sourceZipDir, "level2sub2");
        File level3sub1 = new File(level2sub2, "level3sub1");

        File level2TextFile = new File(level2sub2, "level2sub2.txt");
        File level3TextFile = new File(level3sub1, "level3sub1.txt");

        level2sub1.mkdirs();
        level3sub1.mkdirs();

        FileUtils.writeStringToFile(level2TextFile, "level2sub2");
        FileUtils.writeStringToFile(level3TextFile, "level3sub1");
    }

    @After
    public void removeTempDir() throws IOException
    {
        // make sure zip is closed, else delete fails on windows
        if (zip != null)
        {
            try
            {
                zip.close();
            }
            catch (IOException e)
            {
                // ignore
            }
            zip = null;
        }
        FileUtils.deleteDirectory(tempDir);
    }

    @Test
    public void zipContainsSinglePrefix() throws IOException
    {
        File zipFile = new File(tempDir, "zip-with-prefix.zip");
        ZipUtils.zipDir(zipFile, sourceZipDir, FIRST_PREFIX);

        zip = new ZipFile(zipFile);
        final Enumeration<? extends ZipEntry> entries = zip.entries();

        while (entries.hasMoreElements())
        {
            final ZipEntry zipEntry = entries.nextElement();
            String zipPath = zipEntry.getName();
            String testPrefix = zipPath.substring(0, zipPath.indexOf("/"));

            assertEquals(FIRST_PREFIX, testPrefix);
        }
    }

    @Test
    public void zipContainsNestedPrefix() throws IOException
    {
        File zipFile = new File(tempDir, "zip-nested-prefix.zip");
        ZipUtils.zipDir(zipFile, sourceZipDir, NESTED_PREFIX);

        zip = new ZipFile(zipFile);
        final Enumeration<? extends ZipEntry> entries = zip.entries();

        while (entries.hasMoreElements())
        {
            final ZipEntry zipEntry = entries.nextElement();
            String zipPath = zipEntry.getName();
            String[] segments = zipPath.split("/");
            if (segments.length > 1)
            {
                String testPrefix = segments[0] + "/" + segments[1];

                assertEquals(NESTED_PREFIX, testPrefix);
            }
        }
    }

    @Test
    public void prefixedZipDoesNotContainRootDir() throws IOException
    {
        File zipFile = new File(tempDir, "zip-with-prefix-no-root.zip");
        ZipUtils.zipDir(zipFile, sourceZipDir, FIRST_PREFIX);

        zip = new ZipFile(zipFile);
        final Enumeration<? extends ZipEntry> entries = zip.entries();

        while (entries.hasMoreElements())
        {
            final ZipEntry zipEntry = entries.nextElement();
            String zipPath = zipEntry.getName();
            String[] segments = zipPath.split("/");
            if (segments.length > 1)
            {
                String rootPath = segments[1];

                assertThat(rootPath, not(equalTo(ROOT_DIR)));
            }
        }
    }

    @Test
    public void nestedPrefixedZipDoesNotContainRootDir() throws IOException
    {
        File zipFile = new File(tempDir, "zip-nested-prefix-no-root.zip");
        ZipUtils.zipDir(zipFile, sourceZipDir, NESTED_PREFIX);

        zip = new ZipFile(zipFile);
        final Enumeration<? extends ZipEntry> entries = zip.entries();

        while (entries.hasMoreElements())
        {
            final ZipEntry zipEntry = entries.nextElement();
            String zipPath = zipEntry.getName();
            String[] segments = zipPath.split("/");
            if (segments.length > 2)
            {
                String rootPath = segments[2];

                assertThat(rootPath, not(equalTo(ROOT_DIR)));
            }
        }
    }

    @Test
    public void emptyPrefixedZipContainsRootDir() throws IOException
    {
        File zipFile = new File(tempDir, "zip-empty-prefix.zip");
        ZipUtils.zipDir(zipFile, sourceZipDir, "");

        zip = new ZipFile(zipFile);
        final Enumeration<? extends ZipEntry> entries = zip.entries();

        while (entries.hasMoreElements())
        {
            final ZipEntry zipEntry = entries.nextElement();
            String zipPath = zipEntry.getName();
            String rootPath = zipPath.substring(0, zipPath.indexOf("/"));

            assertEquals(ROOT_DIR, rootPath);
        }
    }

    @Test
    public void nullPrefixedZipContainsRootDir() throws IOException
    {
        File zipFile = new File(tempDir, "zip-null-prefix.zip");
        ZipUtils.zipDir(zipFile, sourceZipDir, null);

        zip = new ZipFile(zipFile);
        final Enumeration<? extends ZipEntry> entries = zip.entries();

        while (entries.hasMoreElements())
        {
            final ZipEntry zipEntry = entries.nextElement();
            String zipPath = zipEntry.getName();
            String rootPath = zipPath.substring(0, zipPath.indexOf("/"));

            assertEquals(ROOT_DIR, rootPath);
        }
    }

    @Test
    public void emptyPrefixedZipFolderCountMatches() throws IOException
    {
        File zipFile = new File(tempDir, "zip-empty-prefix.zip");
        ZipUtils.zipDir(zipFile, sourceZipDir, "");

        zip = new ZipFile(zipFile);
        final Enumeration<? extends ZipEntry> entries = zip.entries();

        int numFolders = 0;

        while (entries.hasMoreElements())
        {
            final ZipEntry zipEntry = entries.nextElement();
            if (zipEntry.isDirectory())
            {
                numFolders++;
            }
        }

        assertEquals(NUM_FOLDERS, numFolders);
    }

    @Test
    public void singlePrefixedZipFolderCountMatches() throws IOException
    {
        File zipFile = new File(tempDir, "zip-single-prefix.zip");
        ZipUtils.zipDir(zipFile, sourceZipDir, FIRST_PREFIX);

        zip = new ZipFile(zipFile);
        final Enumeration<? extends ZipEntry> entries = zip.entries();

        int numFolders = 0;

        while (entries.hasMoreElements())
        {
            final ZipEntry zipEntry = entries.nextElement();
            if (zipEntry.isDirectory())
            {
                numFolders++;
            }
        }

        assertEquals(NUM_FOLDERS, numFolders);
    }

    @Test
    public void nestedPrefixedZipFolderCountMatches() throws IOException
    {
        File zipFile = new File(tempDir, "zip-nested-prefix.zip");
        ZipUtils.zipDir(zipFile, sourceZipDir, NESTED_PREFIX);

        zip = new ZipFile(zipFile);
        final Enumeration<? extends ZipEntry> entries = zip.entries();

        int numFolders = 0;

        while (entries.hasMoreElements())
        {
            final ZipEntry zipEntry = entries.nextElement();
            if (zipEntry.isDirectory())
            {
                numFolders++;
            }
        }

        assertEquals(NUM_FOLDERS_NESTED_PREFIX, numFolders);
    }

    @Test
    public void zipFileCountMatches() throws IOException
    {
        File zipFile = new File(tempDir, "zip-single-prefix.zip");
        ZipUtils.zipDir(zipFile, sourceZipDir, FIRST_PREFIX);

        zip = new ZipFile(zipFile);
        final Enumeration<? extends ZipEntry> entries = zip.entries();

        int numFiles = 0;

        while (entries.hasMoreElements())
        {
            final ZipEntry zipEntry = entries.nextElement();
            if (!zipEntry.isDirectory())
            {
                numFiles++;
            }
        }

        assertEquals(NUM_FILES, numFiles);
    }

    @Test
    public void unzipNonPrefixed() throws IOException
    {
        File zipFile = new File(tempDir, "zip-empty-prefix.zip");
        ZipUtils.zipDir(zipFile, sourceZipDir, "");

        File unzipDir = new File(tempDir, "unzip-empty-prefix");
        ZipUtils.unzip(zipFile, unzipDir.getAbsolutePath());

        File rootUnzip = new File(unzipDir, ROOT_DIR);

        assertTrue("root folder in zip was not unzipped", (rootUnzip.exists() && rootUnzip.isDirectory()));
    }

    @Test
    public void unzipSinglePrefix() throws IOException
    {
        File zipFile = new File(tempDir, "zip-single-prefix.zip");
        ZipUtils.zipDir(zipFile, sourceZipDir, FIRST_PREFIX);

        File unzipDir = new File(tempDir, "unzip-single-prefix");
        ZipUtils.unzip(zipFile, unzipDir.getAbsolutePath());

        File rootUnzip = new File(unzipDir, FIRST_PREFIX);

        assertTrue("single prefix folder in zip was not unzipped", (rootUnzip.exists() && rootUnzip.isDirectory()));
    }

    @Test
    public void unzipNestedPrefix() throws IOException
    {
        File zipFile = new File(tempDir, "zip-nested-prefix.zip");
        ZipUtils.zipDir(zipFile, sourceZipDir, NESTED_PREFIX);

        File unzipDir = new File(tempDir, "unzip-nested-prefix");
        ZipUtils.unzip(zipFile, unzipDir.getAbsolutePath());

        File rootUnzip = new File(unzipDir, FIRST_PREFIX);
        File nestedUnzip = new File(rootUnzip, SECOND_PREFIX);

        assertTrue("nested prefix folder in zip was not unzipped", (nestedUnzip.exists() && nestedUnzip.isDirectory()));
    }

    @Test
    public void detectPrefix() throws IOException
    {
        File zipFile = new File(tempDir, "zip-single-prefix.zip");
        // zipDir will use the foldername as a prefix
        ZipUtils.zipDir(zipFile, sourceZipDir, "");

        int nestedRoots = ZipUtils.countNestingLevel(zipFile);

        assertEquals("One level of nesting should be detected", 1, nestedRoots);
    }

    @Test
    public void detectDoublePrefix() throws IOException
    {
        File zipFile = new File(tempDir, "zip-double-prefix.zip");
        ZipUtils.zipDir(zipFile, sourceZipDir, NESTED_PREFIX);

        int nestedRoots = ZipUtils.countNestingLevel(zipFile);

        assertEquals("Two levels of nesting should be detected", 2, nestedRoots);
    }

    @Test
    public void detectNoPrefix() throws IOException, URISyntaxException
    {
        // zip-no-root.zip is a zip with no root folder.
        // We can't use ZipUtils#zipDir() to create it (zipDir() always puts a root folder),
        // so we need to provide one in src/test/resources.
        URL zipPath = TestZipUtils.class.getResource("zip-no-root.zip");
        File zipFile = new File(zipPath.toURI());

        int nestedRoots = ZipUtils.countNestingLevel(zipFile);

        assertEquals("No nesting should be detected", 0, nestedRoots);
    }

    @Test
    public void unzipSinglePrefixTrimmed() throws IOException
    {
        File zipFile = new File(tempDir, "zip-single-prefix.zip");
        ZipUtils.zipDir(zipFile, sourceZipDir, FIRST_PREFIX);

        File unzipDir = new File(tempDir, "unzip-single-prefix");
        ZipUtils.unzip(zipFile, unzipDir.getAbsolutePath(), 1);

        File rootUnzip = new File(unzipDir, FIRST_PREFIX);

        assertTrue("single prefix folder in zip should have been trimmed", !rootUnzip.exists());
    }

    @Test
    public void unzipNestedPrefixTrimmed() throws IOException
    {
        File zipFile = new File(tempDir, "zip-nested-prefix.zip");
        ZipUtils.zipDir(zipFile, sourceZipDir, NESTED_PREFIX);

        File unzipDir = new File(tempDir, "unzip-nested-prefix");
        ZipUtils.unzip(zipFile, unzipDir.getAbsolutePath(), 2);

        File nestedUnzip = new File(unzipDir, SECOND_PREFIX);

        assertTrue("nested prefix folder in zip should have been trimmed", !nestedUnzip.exists());
    }


    @Test
    public void countNoNestingLevel()
    {
        List<String> filenames = Lists.newArrayList(
                "file1.txt",
                "file2.txt");

        int nestedRoots = ZipUtils.countNestingLevel(filenames);
        assertEquals("The number of nested roots should be detected", 0, nestedRoots);
    }

    @Test
    public void countOneNestingLevel()
    {
        List<String> filenames = Lists.newArrayList(
                "root/folder1/file.txt",
                "root/folder2/file.txt");

        int nestedRoots = ZipUtils.countNestingLevel(filenames);
        assertEquals("The number of nested roots should be detected", 1, nestedRoots);
    }

    @Test
    public void countNestingLevelWithEmptyList()
    {
        List<String> filenames = Lists.newArrayList();

        int nestedRoots = ZipUtils.countNestingLevel(filenames);
        assertEquals("Should work with an empty list", 0, nestedRoots);
    }

    @Test
    public void countTwoNestingLevel()
    {
        List<String> filenames = Lists.newArrayList(
                "root/otherRoot/file1.txt",
                "root/otherRoot/file2.txt");
        int nestedRoots = ZipUtils.countNestingLevel(filenames);
        assertEquals("The number of nested roots should be detected", 2, nestedRoots);
    }

    @Test
    public void countTwoNestingLevelWithEmptyDirs()
    {
        List<String> filenames = Lists.newArrayList(
                "root/",
                "root/otherRoot/",
                "root/otherRoot/file1.txt",
                "root/otherRoot/file2.txt");

        int nestedRoots = ZipUtils.countNestingLevel(filenames);
        assertEquals("The number of nested roots should be detected", 2, nestedRoots);
    }

    @Test
    public void countTwoNestingLevelWithEmptyDirsInReversedOrder()
    {
        List<String> filenames = Lists.newArrayList(
                "root/otherRoot/file1.txt",
                "root/otherRoot/file2.txt",
                "root/otherRoot/",
                "root/");

        int nestedRoots = ZipUtils.countNestingLevel(filenames);
        assertEquals("The number of nested roots should be detected", 2, nestedRoots);
    }

    @Test
    public void countOneNestingLevelWithEmptyDirs()
    {
        List<String> filenames = Lists.newArrayList(
                "root/folder1/file1.txt",
                "root/folder1/file2.txt",
                "root/folder1/",
                "root/folder2/",
                "root/");

        int nestedRoots = ZipUtils.countNestingLevel(filenames);
        assertEquals("The number of nested roots should be detected", 1, nestedRoots);
    }
}

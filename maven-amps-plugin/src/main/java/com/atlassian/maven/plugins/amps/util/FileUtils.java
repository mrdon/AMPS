package com.atlassian.maven.plugins.amps.util;

import java.io.File;

public class FileUtils
{
    public static File file(File parent, String... kids)
    {
        File cur = parent;
        for (String kid : kids)
        {
            cur = new File(cur, kid);
        }
        return cur;
    }
}

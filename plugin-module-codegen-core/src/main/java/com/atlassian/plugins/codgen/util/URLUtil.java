package com.atlassian.plugins.codgen.util;

public class URLUtil {

    public static boolean isValidUrlPattern(String urlPattern) {
        if (urlPattern.startsWith("*.")) {
            return urlPattern.indexOf('/') < 0;
        }

        if (urlPattern.startsWith("/") && (!urlPattern.contains("*."))) {
            return true;
        } else {
            return false;
        }

    }
}

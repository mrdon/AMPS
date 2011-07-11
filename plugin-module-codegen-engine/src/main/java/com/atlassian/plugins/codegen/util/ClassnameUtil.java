package com.atlassian.plugins.codegen.util;

/**
 * Author: jdoklovic
 */
public class ClassnameUtil {
    public static String camelCaseToSpaced(String s) {
        return s.replaceAll(
                String.format("%s|%s|%s",
                        "(?<=[A-Z])(?=[A-Z][a-z])",
                        "(?<=[^A-Z])(?=[A-Z])",
                        "(?<=[A-Za-z])(?=[^A-Za-z])"
                ),
                " "
        );
    }

    public static String camelCaseToDashed(String s) {
        return s.replaceAll(
                String.format("%s|%s|%s",
                        "(?<=[A-Z])(?=[A-Z][a-z])",
                        "(?<=[^A-Z])(?=[A-Z])",
                        "(?<=[A-Za-z])(?=[^A-Za-z])"
                ),
                "-"
        );
    }

    public static String camelCaseOrSpaceToDashed(String s) {
        String trimmed =  s.replaceAll("[\\s]","");

        String dashed = trimmed.replaceAll(
                String.format("%s|%s|%s",
                        "(?<=[A-Z])(?=[A-Z][a-z])",
                        "(?<=[^A-Z])(?=[A-Z])",
                        "(?<=[A-Za-z])(?=[^A-Za-z])"
                ),
                "-"
        );

        return dashed;
    }

    public static boolean isValidClassName(String s) {
        if (s.length() < 1) return false;
        if (s.equals("package-info")) return false;
        int cp = s.codePointAt(0);
        if (!Character.isJavaIdentifierStart(cp))
            return false;
        for (int j = Character.charCount(cp); j < s.length(); j += Character.charCount(cp)) {
            cp = s.codePointAt(j);
            if (!Character.isJavaIdentifierPart(cp))
                return false;
        }

        return true;
    }

    public static boolean isValidPackageName(String s) {
        int index;
        while ((index = s.indexOf('.')) != -1) {
            if (!isValidClassName(s.substring(0, index))) return false;
            s = s.substring(index + 1);
        }
        return isValidClassName(s);
    }
}

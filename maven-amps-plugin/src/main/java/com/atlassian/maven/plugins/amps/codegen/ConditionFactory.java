package com.atlassian.maven.plugins.amps.codegen;

import com.atlassian.maven.plugins.amps.codegen.annotations.asm.ProductConditionsLocator;
import org.apache.maven.artifact.Artifact;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

/**
 * @since version
 */
public class ConditionFactory {
    protected static Map<String, String> conditions = new TreeMap<String, String>();

    public static void locateAvailableConditions(String productId,List<String> classpathElements) throws Exception {
        List<URL> conditionURLs = new ArrayList<URL>();
        for(String path : classpathElements) {
            File aFile = new File(path);
            conditionURLs.add(aFile.toURI().toURL());
        }

        URLClassLoader conditionLoader = URLClassLoader.newInstance(conditionURLs.toArray(new URL[]{}));
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(conditionLoader);

        ProductConditionsLocator locator = new ProductConditionsLocator(productId,conditions);
        locator.parse();

        Thread.currentThread().setContextClassLoader(oldLoader);
    }
    public static Map<String,String> getAvailableConditions() {
        return Collections.unmodifiableMap(conditions);
    }
}

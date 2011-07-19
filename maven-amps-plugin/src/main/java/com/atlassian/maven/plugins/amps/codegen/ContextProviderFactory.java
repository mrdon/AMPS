package com.atlassian.maven.plugins.amps.codegen;

import com.atlassian.maven.plugins.amps.codegen.annotations.asm.ProductConditionsLocator;
import com.atlassian.maven.plugins.amps.codegen.annotations.asm.ProductContextProviderLocator;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

/**
 * @since version
 */
public class ContextProviderFactory {
    protected static Map<String, String> providers = new TreeMap<String, String>();

    public static void locateAvailableContextProviders(String productId,List<String> classpathElements) throws Exception {
        List<URL> conditionURLs = new ArrayList<URL>();
        for(String path : classpathElements) {
            File aFile = new File(path);
            conditionURLs.add(aFile.toURI().toURL());
        }

        URLClassLoader conditionLoader = URLClassLoader.newInstance(conditionURLs.toArray(new URL[]{}));
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(conditionLoader);

        ProductContextProviderLocator locator = new ProductContextProviderLocator(productId,providers);
        locator.parse();

        Thread.currentThread().setContextClassLoader(oldLoader);
    }
    public static Map<String,String> getAvailableContextProviders() {
        return providers;
    }

    public static void setProvidersMapForTesting(SortedMap<String,String> map) {
        providers = map;
    }
}

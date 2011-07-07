package com.atlassian.plugins.codegen.annotations.asm;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @since version
 */
public abstract class AbstractAnnotationParser {

    public void parse(String basePackage, ClassVisitor classVisitor) throws Exception {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        String path = basePackage.replace('.', '/');

        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            processResource(resource, basePackage, classVisitor);
        }
    }

    protected void processResource(URL resource, String packageName, ClassVisitor classVisitor) throws IOException {
        if (resource.getProtocol().equals("file")) {
            processFileDirectory(new File(resource.getFile()), packageName, classVisitor);
        } else if(resource.getProtocol().equals("jar")) {
            JarURLConnection conn = (JarURLConnection)resource.openConnection();
            processJarDirectory(conn.getJarFile(), packageName, classVisitor);
        }
    }

    protected void processJarDirectory(JarFile jarFile, String packageName, ClassVisitor classVisitor) throws IOException {
        Enumeration<JarEntry> entries = jarFile.entries();
        String basePath = packageName.replace('.', '/');
        while(entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String name = entry.getName();
            if(name.startsWith(basePath) && !entry.isDirectory() && name.endsWith(".class")) {
                InputStream is = jarFile.getInputStream(entry);
                try{
                    processClassFile(is, classVisitor);
                } finally {
                    IOUtils.closeQuietly(is);
                }
            }
        }
    }

    protected void processFileDirectory(File directory, String packageName, ClassVisitor classVisitor) throws IOException {
        if(!directory.exists()) {
            return;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                processFileDirectory(file, packageName + "." + file.getName(), classVisitor);
            } else if (file.getName().endsWith(".class")) {
                InputStream is = FileUtils.openInputStream(file);

                try {
                    processClassFile(is, classVisitor);
                } finally {
                    IOUtils.closeQuietly(is);
                }

            }
        }
    }

    protected void processClassFile(InputStream is,ClassVisitor classVisitor) throws IOException {
        ClassReader classReader = new ClassReader(is);
        classReader.accept(classVisitor, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
    }

    protected static String normalize(String name) {
        if (name == null)
            return null;

        if (name.startsWith("L") && name.endsWith(";"))
            name = name.substring(1, name.length() - 1);

        if (name.endsWith(".class"))
            name = name.substring(0, name.length() - ".class".length());

        return name.replace('/', '.');
    }
}

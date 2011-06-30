package com.atlassian.plugins.codgen.util;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeConstants;

import java.io.File;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Author: jdoklovic
 */
public class CodeTemplateHelper {

    private static Logger LOG = Logger.getLogger(CodeTemplateHelper.class);
    public static final String UTF8 = "UTF-8";

    static {
        Velocity.setProperty(RuntimeConstants.INPUT_ENCODING, UTF8);
        Velocity.setProperty(RuntimeConstants.PARSER_POOL_SIZE, 3);
        Velocity.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        Velocity.setProperty("classpath." + RuntimeConstants.RESOURCE_LOADER + ".class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

        try {
            Velocity.init();
        } catch (Exception e) {
            LOG.error("Unable to init velocity", e);
        }
    }

    public String parseTemplate(String templatePath, Properties props) throws Exception {
        VelocityContext ctx = new VelocityContext();

        Enumeration<?> names = props.propertyNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            ctx.put(name, props.get(name));
        }

        final StringWriter stringWriter = new StringWriter();
        Template template = Velocity.getTemplate(templatePath);

        template.merge(ctx, stringWriter);

        return stringWriter.toString();
    }

    public void writeJavaClassFromTemplate(String templatePath, String className, File sourceDirectory, String packageName, Properties props) throws Exception {
        Properties overrideProps = new Properties(props);
        overrideProps.setProperty("CLASSNAME",className);
        overrideProps.setProperty("PACKAGE",packageName);

        String content = parseTemplate(templatePath, overrideProps);
        String packagePath = packageName.length() == 0 ? "" : packageName.replaceAll("\\.", File.separator);

        File packageFile = sourceDirectory;
        if (!packagePath.equals("")) {
            packageFile = new File(sourceDirectory, packagePath);
        }
        packageFile.mkdirs();

        File javaFile = new File(packageFile, className + ".java");
        FileUtils.writeStringToFile(javaFile, content, UTF8);

    }

    public void writeFileFromTemplate(String templatePath, String fileName, File directory, Properties props) throws Exception {
        String content = parseTemplate(templatePath, props);
        File newFile = new File(directory, fileName);

        FileUtils.writeStringToFile(newFile, content, UTF8);

    }

    public String getStringFromTemplate(String templatePath, Properties props) throws Exception {
        return parseTemplate(templatePath, props);
    }
}

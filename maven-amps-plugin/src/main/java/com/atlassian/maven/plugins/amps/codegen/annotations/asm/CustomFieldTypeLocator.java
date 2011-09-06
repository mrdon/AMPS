package com.atlassian.maven.plugins.amps.codegen.annotations.asm;

import com.atlassian.plugins.codegen.annotations.asm.AbstractAnnotationParser;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.logging.Log;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.EmptyVisitor;

import java.io.InputStream;
import java.util.Map;

/**
 * @since 3.5
 */
public class CustomFieldTypeLocator extends AbstractAnnotationParser {
    public static final String JIRA_FIELDS_PACKAGE = "com.atlassian.jira.issue.customfields.impl";

    private Log log;
    private Map<String, String> fieldTypeRegistry;

    public CustomFieldTypeLocator(Map<String, String> fieldTypeRegistry) {
        this.fieldTypeRegistry = fieldTypeRegistry;
    }

    public void parse() throws Exception {
        parse(JIRA_FIELDS_PACKAGE, new FieldClassVisitor());
    }

    public void parse(String basePackage) throws Exception {
        parse(basePackage, new FieldClassVisitor());
    }

    public class FieldClassVisitor extends EmptyVisitor {

        private String visitedClassname;
        private boolean isCustomFieldType;

        @Override
        public void visit(final int version, final int access, final String name, final String signature, final String superName, final String[] interfaces) {
            this.visitedClassname = normalize(name);
            String iface = "com/atlassian/jira/issue/customfields/CustomFieldType";

            this.isCustomFieldType = false;

            this.isCustomFieldType = ArrayUtils.contains(interfaces, iface);
            if (!isCustomFieldType) {
                this.isCustomFieldType = superHasInterface(superName, iface);
            }

            if (isCustomFieldType) {
                String simpleName = StringUtils.substringAfterLast(visitedClassname, ".");
                fieldTypeRegistry.put(simpleName, visitedClassname);
            }

        }

        private boolean superHasInterface(String superName, String interfaceName) {
            boolean hasInterface = false;

            if (normalize(superName).equals("java.lang.Object")) {
                return hasInterface;
            }

            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            String path = superName.replace('.', '/');

            InputStream is = null;
            try {
                is = classLoader.getResourceAsStream(path + ".class");
                if (null != is) {

                    ClassReader classReader = new ClassReader(is);
                    String[] interfaces = classReader.getInterfaces();
                    hasInterface = ArrayUtils.contains(classReader.getInterfaces(), interfaceName);
                    if (!hasInterface) {
                        hasInterface = superHasInterface(classReader.getSuperName(), interfaceName);
                    }
                }
            } catch (Exception e) {
                //don't care
            } finally {
                IOUtils.closeQuietly(is);
            }

            return hasInterface;
        }

        @Override
        public AnnotationVisitor visitAnnotation(String annotationName, boolean isVisible) {
            return null;
        }

        @Override
        public MethodVisitor visitMethod(int i, String s, String s1, String s2, String[] strings) {
            return null;
        }

        @Override
        public FieldVisitor visitField(int i, String s, String s1, String s2, Object o) {
            return null;
        }
    }
}

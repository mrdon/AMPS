package com.atlassian.plugins.codegen.annotations.asm;

import com.atlassian.plugins.codegen.modules.PluginModuleCreatorRegistry;
import com.atlassian.plugins.codegen.annotations.*;
import com.atlassian.plugins.codegen.modules.PluginModuleCreator;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.EmptyVisitor;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @since 3.5
 */
public class ModuleCreatorAnnotationParser extends AbstractAnnotationParser {

    public static final String MODULE_PACKAGE = "com.atlassian.plugins.codegen.modules";
    protected static final Map<String, String> annotationProductMap = new HashMap<String, String>();

    static {
        annotationProductMap.put(JiraPluginModuleCreator.class.getName(), PluginModuleCreatorRegistry.JIRA);
        annotationProductMap.put(ConfluencePluginModuleCreator.class.getName(), PluginModuleCreatorRegistry.CONFLUENCE);
        annotationProductMap.put(BambooPluginModuleCreator.class.getName(), PluginModuleCreatorRegistry.BAMBOO);
        annotationProductMap.put(CrowdPluginModuleCreator.class.getName(), PluginModuleCreatorRegistry.CROWD);
        annotationProductMap.put(FeCruPluginModuleCreator.class.getName(), PluginModuleCreatorRegistry.FECRU);
        annotationProductMap.put(RefAppPluginModuleCreator.class.getName(), PluginModuleCreatorRegistry.REFAPP);
    }

    private final PluginModuleCreatorRegistry pluginModuleCreatorRegistry;

    public ModuleCreatorAnnotationParser(PluginModuleCreatorRegistry pluginModuleCreatorRegistry) {
        super();
        this.pluginModuleCreatorRegistry = pluginModuleCreatorRegistry;
    }

    public void parse() throws Exception {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
        parse(MODULE_PACKAGE, new ModuleClassVisitor());
        Thread.currentThread().setContextClassLoader(oldLoader);
    }

    public void parse(String basePackage) throws Exception {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
        parse(basePackage,new ModuleClassVisitor());
        Thread.currentThread().setContextClassLoader(oldLoader);
    }

    public class ModuleClassVisitor extends EmptyVisitor {

        private String visitedClassname;
        private boolean isModuleCreator;

        @Override
        public void visit(final int version, final int access, final String name, final String signature, final String superName, final String[] interfaces) {
            this.visitedClassname = normalize(name);
            String iface = PluginModuleCreator.class.getName().replace('.', '/');
            this.isModuleCreator = ArrayUtils.contains(interfaces, iface);
            if (!isModuleCreator) {
                this.isModuleCreator = superHasInterface(superName, iface);
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
            String normalizedName = normalize(annotationName);

            if (isModuleCreator && annotationProductMap.containsKey(normalizedName)) {
                return new ProductCreatorAnnotationVisitor(normalizedName);
            }

            if (isModuleCreator && (Dependencies.class.getName().equals(normalizedName) || Dependency.class.equals(normalizedName))) {
                return new DependenciesAnnotationVisitor(normalizedName);
            }

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

        private class ProductCreatorAnnotationVisitor extends EmptyVisitor {

            private String annotationName;

            private ProductCreatorAnnotationVisitor(String annotationName) {
                this.annotationName = annotationName;
            }

            @Override
            public void visitEnd() {

                super.visitEnd();

                String productId = annotationProductMap.get(annotationName);
                if (StringUtils.isNotBlank(productId)) {
                    try {
                        PluginModuleCreator creator = (PluginModuleCreator) Class.forName(visitedClassname).newInstance();
                        pluginModuleCreatorRegistry.registerModuleCreator(productId, creator);
                    } catch (Exception e) {
                        e.printStackTrace();
                        //just don't register
                    }
                }
            }

        }

        private class DependenciesAnnotationVisitor extends EmptyVisitor {

            private String annotationName;
            private List<DependencyDescriptor> dependencies;

            private DependenciesAnnotationVisitor(String annotationName) {
                this.annotationName = annotationName;
                this.dependencies = new ArrayList<DependencyDescriptor>();
            }

            //Visits nested annotations
            @Override
            public AnnotationVisitor visitAnnotation(String name, String annotationName) {
                String normalizedName = normalize(annotationName);
                return new DependencyAnnotationVisitor(normalizedName, dependencies);
            }

            @Override
            public void visitEnd() {

                super.visitEnd();

                if (!dependencies.isEmpty()) {
                    try {
                        Class creatorClass = Class.forName(visitedClassname);
                        pluginModuleCreatorRegistry.registerModuleCreatorDependencies(creatorClass, dependencies);
                    } catch (Exception e) {
                        e.printStackTrace();
                        //just don't register
                    }
                }
            }

        }

        private class DependencyAnnotationVisitor extends EmptyVisitor {
            private String annotationName;
            private List<DependencyDescriptor> dependencies;
            private DependencyDescriptor descriptor;

            private DependencyAnnotationVisitor(String annotationName, List<DependencyDescriptor> dependencies) {
                this.annotationName = annotationName;
                this.dependencies = dependencies;
                this.descriptor = new DependencyDescriptor();
            }

            //visit a name/value pair
            @Override
            public void visit(String name, Object value) {
                if (name.equals("groupId")) {
                    descriptor.setGroupId((String) value);
                } else if (name.equals("artifactId")) {
                    descriptor.setArtifactId((String) value);
                } else if (name.equals("version")) {
                    descriptor.setVersion((String) value);
                } else if (name.equals("scope")) {
                    descriptor.setScope((String) value);
                }
            }

            @Override
            public void visitEnd() {
                super.visitEnd();
                dependencies.add(descriptor);
            }
        }

    }

}

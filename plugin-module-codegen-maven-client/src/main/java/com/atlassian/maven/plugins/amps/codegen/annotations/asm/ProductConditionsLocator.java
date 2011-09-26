package com.atlassian.maven.plugins.amps.codegen.annotations.asm;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.atlassian.plugins.codegen.annotations.asm.AbstractAnnotationParser;
import com.atlassian.plugins.codegen.modules.PluginModuleCreatorRegistry;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.logging.Log;
import org.objectweb.asm.*;
import org.objectweb.asm.commons.EmptyVisitor;

/**
 * @since 3.5
 */
public class ProductConditionsLocator extends AbstractAnnotationParser
{
    public static final String JIRA_CONDITIONS_PACKAGE = "com.atlassian.jira.plugin.webfragment.conditions";
    protected static final Map<String, String> productConditionsPackages = new HashMap<String, String>();

    static
    {
        productConditionsPackages.put(PluginModuleCreatorRegistry.JIRA, "com.atlassian.jira.plugin.webfragment.conditions");
        productConditionsPackages.put(PluginModuleCreatorRegistry.CONFLUENCE, "com.atlassian.jira.plugin.webfragment.conditions");
        productConditionsPackages.put(PluginModuleCreatorRegistry.BAMBOO, "com.atlassian.jira.plugin.webfragment.conditions");
        productConditionsPackages.put(PluginModuleCreatorRegistry.CROWD, "com.atlassian.jira.plugin.webfragment.conditions");
        productConditionsPackages.put(PluginModuleCreatorRegistry.FECRU, "com.atlassian.jira.plugin.webfragment.conditions");
        productConditionsPackages.put(PluginModuleCreatorRegistry.REFAPP, "com.atlassian.jira.plugin.webfragment.conditions");
    }

    private Log log;
    private String productId;
    private Map<String, String> conditionRegistry;

    public ProductConditionsLocator(String productId, Map<String, String> conditionRegistry)
    {
        this.productId = productId;
        this.conditionRegistry = conditionRegistry;
    }

    public void parse() throws Exception
    {
        String basePackage = productConditionsPackages.get(productId);
        if (StringUtils.isNotBlank(basePackage))
        {
            parse(basePackage, new ConditionClassVisitor());
        }

    }

    public void parse(String basePackage) throws Exception
    {
        parse(basePackage, new ConditionClassVisitor());
    }

    public class ConditionClassVisitor extends EmptyVisitor
    {

        private String visitedClassname;
        private boolean isWebCondition;

        @Override
        public void visit(final int version, final int access, final String name, final String signature, final String superName, final String[] interfaces)
        {
            this.visitedClassname = normalize(name);
            String iface = "com/atlassian/plugin/web/Condition";

            this.isWebCondition = false;
            boolean isAbstract = ((access & Opcodes.ACC_ABSTRACT) > 0);

            if (!isAbstract)
            {
                this.isWebCondition = ArrayUtils.contains(interfaces, iface);
                if (!isWebCondition)
                {
                    this.isWebCondition = superHasInterface(superName, iface);
                }
            }

            if (isWebCondition)
            {
                String simpleName = StringUtils.substringAfterLast(visitedClassname, ".");
                conditionRegistry.put(simpleName, visitedClassname);
            }

        }

        private boolean superHasInterface(String superName, String interfaceName)
        {
            boolean hasInterface = false;

            if (normalize(superName).equals("java.lang.Object"))
            {
                return hasInterface;
            }

            ClassLoader classLoader = Thread.currentThread()
                    .getContextClassLoader();
            String path = superName.replace('.', '/');

            InputStream is = null;
            try
            {
                is = classLoader.getResourceAsStream(path + ".class");
                if (null != is)
                {

                    ClassReader classReader = new ClassReader(is);
                    String[] interfaces = classReader.getInterfaces();
                    hasInterface = ArrayUtils.contains(classReader.getInterfaces(), interfaceName);
                    if (!hasInterface)
                    {
                        hasInterface = superHasInterface(classReader.getSuperName(), interfaceName);
                    }
                }
            } catch (Exception e)
            {
                //don't care
            } finally
            {
                IOUtils.closeQuietly(is);
            }

            return hasInterface;
        }

        @Override
        public AnnotationVisitor visitAnnotation(String annotationName, boolean isVisible)
        {
            return null;
        }

        @Override
        public MethodVisitor visitMethod(int i, String s, String s1, String s2, String[] strings)
        {
            return null;
        }

        @Override
        public FieldVisitor visitField(int i, String s, String s1, String s2, Object o)
        {
            return null;
        }
    }
}

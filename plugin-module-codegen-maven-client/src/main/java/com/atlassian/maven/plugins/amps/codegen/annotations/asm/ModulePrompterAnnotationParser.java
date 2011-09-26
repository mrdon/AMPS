package com.atlassian.maven.plugins.amps.codegen.annotations.asm;

import java.io.InputStream;
import java.lang.reflect.Constructor;

import com.atlassian.maven.plugins.amps.codegen.annotations.ModuleCreatorClass;
import com.atlassian.maven.plugins.amps.codegen.prompter.AbstractModulePrompter;
import com.atlassian.maven.plugins.amps.codegen.prompter.PluginModulePrompter;
import com.atlassian.maven.plugins.amps.codegen.prompter.PluginModulePrompterRegistry;
import com.atlassian.plugins.codegen.annotations.asm.AbstractAnnotationParser;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.objectweb.asm.*;
import org.objectweb.asm.commons.EmptyVisitor;

/**
 * @since 3.5
 */
public class ModulePrompterAnnotationParser extends AbstractAnnotationParser
{
    public static final String PROMPTER_PACKAGE = "com.atlassian.maven.plugins.amps.codegen.prompter";
    private Log log;

    private PluginModulePrompterRegistry pluginModulePrompterRegistry;
    private Prompter mavenPrompter;

    public ModulePrompterAnnotationParser(PluginModulePrompterRegistry pluginModulePrompterRegistry)
    {
        this.pluginModulePrompterRegistry = pluginModulePrompterRegistry;
    }

    public void parse() throws Exception
    {
        ClassLoader oldLoader = Thread.currentThread()
                .getContextClassLoader();
        Thread.currentThread()
                .setContextClassLoader(getClass().getClassLoader());
        parse(PROMPTER_PACKAGE, new PropmpterClassVisitor());
        Thread.currentThread()
                .setContextClassLoader(oldLoader);
    }

    public void parse(String basePackage) throws Exception
    {
        ClassLoader oldLoader = Thread.currentThread()
                .getContextClassLoader();
        Thread.currentThread()
                .setContextClassLoader(getClass().getClassLoader());
        parse(basePackage, new PropmpterClassVisitor());
        Thread.currentThread()
                .setContextClassLoader(oldLoader);
    }

    public class PropmpterClassVisitor extends EmptyVisitor
    {

        private String visitedClassname;
        private boolean isModulePrompter;

        @Override
        public void visit(final int version, final int access, final String name, final String signature, final String superName, final String[] interfaces)
        {
            this.visitedClassname = normalize(name);
            String iface = PluginModulePrompter.class.getName()
                    .replace('.', '/');
            this.isModulePrompter = ArrayUtils.contains(interfaces, iface);
            if (!isModulePrompter)
            {
                this.isModulePrompter = superHasInterface(superName, iface);
            }

            Class modulePrompterClass = null;
            try
            {
                modulePrompterClass = Class.forName(visitedClassname);
            } catch (ClassNotFoundException e)
            {
                //dumb. we're visiting this class so it has to exist!
            }

            if (isModulePrompter && !AbstractModulePrompter.class.isAssignableFrom(modulePrompterClass))
            {
                isModulePrompter = false;
                if (null != log)
                {
                    log.warn(visitedClassname + " MUST extend " + AbstractModulePrompter.class.getName() + ". NOT REGISTERED");
                }
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
            String normalizedName = normalize(annotationName);

            if (isModulePrompter && ModuleCreatorClass.class.getName()
                    .equals(normalizedName))
            {
                return new ModuleCreatorClassAnnotationVisitor(normalizedName);
            }

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

        private class ModuleCreatorClassAnnotationVisitor extends EmptyVisitor
        {

            private String annotationName;

            private ModuleCreatorClassAnnotationVisitor(String annotationName)
            {
                this.annotationName = annotationName;
            }

            @Override
            public void visit(String name, Object value)
            {
                super.visit(name, value);
                Type creatorType = (Type) value;
                String normalizedCreatorName = normalize(creatorType.getClassName());

                try
                {
                    Class creatorClass = Class.forName(normalizedCreatorName);
                    Class modulePrompterClass = Class.forName(visitedClassname);
                    Class[] argTypes = new Class[]{Prompter.class};
                    Object[] args = new Object[]{mavenPrompter};

                    Constructor prompterConstructor = modulePrompterClass.getConstructor(argTypes);
                    if (null != prompterConstructor)
                    {
                        PluginModulePrompter modulePrompter = (PluginModulePrompter) prompterConstructor.newInstance(args);
                        pluginModulePrompterRegistry.registerModulePrompter(creatorClass, modulePrompter);
                    }

                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void visitEnd()
            {
                super.visitEnd();
            }
        }
    }

    public Prompter getMavenPrompter()
    {
        return mavenPrompter;
    }

    public void setMavenPrompter(Prompter mavenPrompter)
    {
        this.mavenPrompter = mavenPrompter;
    }

    public Log getLog()
    {
        return log;
    }

    public void setLog(Log log)
    {
        this.log = log;
    }
}

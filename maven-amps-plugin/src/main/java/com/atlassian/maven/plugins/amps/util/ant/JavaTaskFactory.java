package com.atlassian.maven.plugins.amps.util.ant;

import java.io.File;
import java.util.Collections;
import java.util.Map;

import org.apache.maven.plugin.logging.Log;
import org.apache.tools.ant.taskdefs.Java;

public class JavaTaskFactory
{
    private final AntUtils antUtils = new AntUtils();
    private final Log logger;
    
    public JavaTaskFactory(Log logger)
    {
        this.logger = logger;
    }
    
    public Java newJavaTask()
    {
        return newJavaTask(new Parameters(null, null, Collections.<String, String>emptyMap()));
    }
    
    public Java newJavaTask(Parameters params)
    {
        Java java = (Java) antUtils.createAntTask("java");
        java.setFork(true);
        
        // If the user has not specified any output file then the process's output will be logged
        // to the Ant logging subsystem which will in turn go to the Cargo's logging subsystem as
        // we're configuring Ant with our own custom build listener (see below).
        setOutput(params.output, java);

        // Add a build listener to the Ant project so that we can catch what the Java task logs
        addBuildListener(java);

        // Add system properties for the container JVM
        addSystemProperties(java, params.systemProperties);
        
        // Add JVM args if defined
        addJvmArgs(java, params.jvmArgs);
        
        return java;
    }

    private void setOutput(String output, Java java)
    {
        if (output != null)
        {
            File outputFile = new File(output);
            
            // Ensure that directories where the output file will go are created
            outputFile.getAbsoluteFile().getParentFile().mkdirs();
            
            java.setOutput(outputFile);
            java.setAppend(true);
        }
    }

    private void addBuildListener(Java java)
    {
        boolean foundBuildListener = false;
        for (Object listenerObject : java.getProject().getBuildListeners())
        {
            if (listenerObject instanceof AntBuildListener)
            {
                foundBuildListener = true;
                break;
            }
        }
        if (!foundBuildListener)
        {
            java.getProject().addBuildListener(new AntBuildListener(logger));
        }
    }

    /**
     * Add system properties to the Ant java command used to start the container.
     * 
     * @param java the java command that will start the container
     */
    private void addSystemProperties(Java java, Map<String, String> systemProperties)
    {
        for (Map.Entry<String, String> prop : systemProperties.entrySet())
        {
            java.addSysproperty(antUtils.createSysProperty(prop.getKey(), prop.getValue()));
        }
    }
    
    /**
     * Add the jvm arguments to the java command.
     * @param java The java task
     */
    private void addJvmArgs(Java java, String jvmArgs)
    {
        if (jvmArgs != null)
        {
            java.createJvmarg().setLine(jvmArgs);
        }
    }
    
    public static Parameters output(String output)
    {
        return new Parameters(output, null, null);
    }
    
    public static Parameters jvmArgs(String jvmArgs)
    {
        return new Parameters(null, jvmArgs, null);
    }
    
    public static Parameters systemProperties(Map<String, String> systemProperties)
    {
        return new Parameters(null, null, systemProperties);
    }
    
    public static final class Parameters
    {
        private final String jvmArgs;
        private final Map<String, String> systemProperties;
        private final String output;

        private Parameters(String output, String jvmArgs, Map<String, String> systemProperties)
        {
            this.output = output;
            this.jvmArgs = jvmArgs;
            this.systemProperties = systemProperties;
        }
        
        public Parameters output(String output)
        {
            return new Parameters(output, jvmArgs, systemProperties);
        }

        public Parameters jvmArgs(String jvmArgs)
        {
            return new Parameters(output, jvmArgs, systemProperties);
        }

        public Parameters systemProperties(Map<String, String> systemProperties)
        {
            return new Parameters(output, jvmArgs, systemProperties);
        }
    }
}

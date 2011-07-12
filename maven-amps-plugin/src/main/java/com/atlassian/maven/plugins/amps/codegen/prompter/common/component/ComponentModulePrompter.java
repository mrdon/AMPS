package com.atlassian.maven.plugins.amps.codegen.prompter.common.component;

import com.atlassian.maven.plugins.amps.codegen.annotations.ModuleCreatorClass;
import com.atlassian.maven.plugins.amps.codegen.prompter.AbstractModulePrompter;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;
import com.atlassian.plugins.codegen.modules.PluginModuleProperties;
import com.atlassian.plugins.codegen.modules.common.component.ComponentModuleCreator;
import com.atlassian.plugins.codegen.modules.common.component.ComponentProperties;
import com.atlassian.plugins.codegen.util.ClassnameUtil;
import org.apache.commons.lang.StringUtils;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @since version
 */
@ModuleCreatorClass(ComponentModuleCreator.class)
public class ComponentModulePrompter extends AbstractModulePrompter<ComponentProperties> {

    public ComponentModulePrompter(Prompter prompter) {
        super(prompter);

    }

    @Override
    public ComponentProperties promptForBasicProperties(PluginModuleLocation moduleLocation) throws PrompterException {
        String interfaceName = promptJavaClassname("Enter Interface name", "MYComponent");
        String interfacePackage = promptJavaPackagename("Enter Interface package", "com.atlassian.plugins.components");

        String className = promptJavaClassname("Enter Class name", interfaceName + "Impl");
        String packageName = promptJavaPackagename("Enter Package Name", interfacePackage);

        String fqClass = ClassnameUtil.fullyQualifiedName(packageName, className);
        String fqInterface = ClassnameUtil.fullyQualifiedName(interfacePackage, interfaceName);

        ComponentProperties props = new ComponentProperties(fqClass);
        props.setFullyQualifiedInterface(fqInterface);

        props.setGenerateClass(!javaFileExists(props.getProperty(ComponentProperties.FQ_CLASSNAME), moduleLocation));
        props.setGenerateInterface(!javaFileExists(props.getProperty(ComponentProperties.FQ_INTERFACE), moduleLocation));

        suppressExamplesPrompt();

        return props;
    }

    @Override
    public void promptForAdvancedProperties(ComponentProperties props, PluginModuleLocation moduleLocation) throws PrompterException {
        boolean createClass = props.generateClass();
        boolean createInterface = props.generateInterface();

        if (createInterface) {
            props.setGenerateInterface(promptForBoolean("Generate Module Interface?", "Y"));
        }

        if (createClass) {
            props.setGenerateClass(promptForBoolean("Generate Module Class?", "Y"));
        }

        String alias = prompter.prompt("Alias (not required)");
        if (StringUtils.isNotBlank(alias)) {
            props.setAlias(alias);
        }

        props.setPublic(promptForBoolean("Public access?", "N"));

        Map<String, String> serviceProps = promptForServiceProps();
        if (serviceProps.size() > 0) {
            props.setServiceProps(serviceProps);
        }

        props.setIncludeExamples(false);

    }

    private Map<String, String> promptForServiceProps() throws PrompterException {
        Map<String, String> props = new HashMap<String, String>();
        promptForServiceProp(props);

        return props;
    }

    private void promptForServiceProp(Map<String, String> props) throws PrompterException {
        StringBuffer addBuffer = new StringBuffer();
        if (props.size() > 0) {
            addBuffer.append("service-properties:\n");
            for (Map.Entry<String, String> entry : props.entrySet()) {
                addBuffer.append(entry.getKey()).append("->").append(entry.getValue()).append("\n");
            }
        }
        addBuffer.append("Add Service Property?");
        boolean addProp = promptForBoolean(addBuffer.toString(), "N");

        if (addProp) {
            String key = promptNotBlank("property key");
            String value = promptNotBlank("property value");
            props.put(key, value);
            promptForServiceProp(props);
        }
    }

    private boolean javaFileExists(String fqInterface, PluginModuleLocation moduleLocation) {
        File javaFile = new File(moduleLocation.getSourceDirectory(), fqInterface.replaceAll("\\.", File.separator) + ".java");
        return javaFile.exists();
    }
}
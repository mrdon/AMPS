package com.atlassian.maven.plugins.amps.codegen.prompter.common.moduletype;

import com.atlassian.maven.plugins.amps.codegen.annotations.ModuleCreatorClass;
import com.atlassian.maven.plugins.amps.codegen.prompter.AbstractModulePrompter;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;
import com.atlassian.plugins.codegen.modules.PluginModuleProperties;
import com.atlassian.plugins.codegen.modules.common.moduletype.ModuleTypeModuleCreator;
import com.atlassian.plugins.codegen.modules.common.moduletype.ModuleTypeProperties;
import com.atlassian.plugins.codegen.util.ClassnameUtil;
import org.apache.commons.lang.StringUtils;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;

/**
 * @since 3.5
 */
@ModuleCreatorClass(ModuleTypeModuleCreator.class)
public class ModuleTypePrompter extends AbstractModulePrompter<ModuleTypeProperties> {

    public ModuleTypePrompter(Prompter prompter) {
        super(prompter);

    }

    @Override
    public ModuleTypeProperties promptForBasicProperties(PluginModuleLocation moduleLocation) throws PrompterException {
        String interfaceName = promptJavaClassname("Enter Interface name", "MYModule");
        String interfacePackage = promptJavaPackagename("Enter Interface package", getDefaultBasePackage() + ".modules");

        String className = promptJavaClassname("Enter Class name", interfaceName + "ModuleDescriptor");
        String packageName = promptJavaPackagename("Enter Package Name", interfacePackage);

        String fqClass = ClassnameUtil.fullyQualifiedName(packageName, className);
        String fqInterface = ClassnameUtil.fullyQualifiedName(interfacePackage, interfaceName);

        ModuleTypeProperties props = new ModuleTypeProperties(fqClass);
        props.setFullyQualifiedInterface(fqInterface);

        return props;
    }
}
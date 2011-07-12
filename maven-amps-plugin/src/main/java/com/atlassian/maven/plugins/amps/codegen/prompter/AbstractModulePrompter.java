package com.atlassian.maven.plugins.amps.codegen.prompter;

import com.atlassian.plugins.codegen.modules.PluginModuleLocation;
import com.atlassian.plugins.codegen.modules.PluginModuleProperties;
import com.atlassian.plugins.codegen.util.ClassnameUtil;
import org.apache.commons.lang.StringUtils;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Author: jdoklovic
 */
public abstract class AbstractModulePrompter<T extends PluginModuleProperties> implements PluginModulePrompter<T> {
    protected final List<String> ynAnswers;
    protected final Prompter prompter;
    protected boolean showExamplesPrompt;
    protected boolean showAdvancedPrompt;

    public AbstractModulePrompter(Prompter prompter) {
        this.prompter = prompter;
        this.showExamplesPrompt = true;
        this.showAdvancedPrompt = true;
        ynAnswers = new ArrayList<String>(Arrays.asList("Y", "y", "N", "n"));
    }

    @Override
    public PluginModuleProperties getModulePropertiesFromInput(PluginModuleLocation moduleLocation) throws PrompterException {
        T props = (T)promptForBasicProperties(moduleLocation);

        if (showAdvancedPrompt) {
            boolean showAdvanced = promptForBoolean("Show Advanced Setup?", "N");

            if (showAdvanced) {
                String moduleName = promptNotBlank("Plugin Name", props.getProperty(PluginModuleProperties.MODULE_NAME));
                String moduleKey = promptNotBlank("Plugin Key", props.getProperty(PluginModuleProperties.MODULE_KEY));
                String moduleDescription = promptNotBlank("Plugin Description", props.getProperty(PluginModuleProperties.DESCRIPTION));
                String moduleI18nNameKey = promptNotBlank("i18n Name Key", props.getProperty(PluginModuleProperties.NAME_I18N_KEY));
                String moduleI18nDescriptionKey = promptNotBlank("i18n Description Key", props.getProperty(PluginModuleProperties.DESCRIPTION_I18N_KEY));

                props.setModuleName(moduleName);
                props.setModuleKey(moduleKey);
                props.setDescription(moduleDescription);

                Properties currentI18n = props.getI18nProperties();
                currentI18n.remove(PluginModuleProperties.NAME_I18N_KEY);
                currentI18n.remove(PluginModuleProperties.DESCRIPTION_I18N_KEY);

                currentI18n.setProperty(moduleI18nNameKey, moduleName);
                currentI18n.setProperty(moduleI18nDescriptionKey, moduleDescription);

                promptForAdvancedProperties(props, moduleLocation);
            }
        }

        if (showExamplesPrompt) {
            props.setIncludeExamples(promptForBoolean("Include Example Code?", "N"));
        }

        return props;
    }

    @Override
    public abstract <T extends PluginModuleProperties> T promptForBasicProperties(PluginModuleLocation moduleLocation) throws PrompterException;

    @Override
    public void promptForAdvancedProperties(T props, PluginModuleLocation moduleLocation) throws PrompterException {
    }

    protected String promptJavaClassname(String message) throws PrompterException {
        return promptJavaClassname(message, null);
    }

    protected String promptJavaClassname(String message, String defaultValue) throws PrompterException {
        String classname;
        if (StringUtils.isBlank(defaultValue)) {
            classname = prompter.prompt(message);
        } else {
            classname = prompt(message, defaultValue);
        }

        if (StringUtils.isBlank(classname) || !ClassnameUtil.isValidClassName(classname)) {
            classname = promptJavaClassname(message, defaultValue);
        }

        return classname;
    }

    protected String promptJavaPackagename(String message) throws PrompterException {
        return promptJavaPackagename(message, null);
    }

    protected String promptJavaPackagename(String message, String defaultValue) throws PrompterException {
        String packagename;

        if (StringUtils.isBlank(defaultValue)) {
            packagename = prompter.prompt(message);
        } else {
            packagename = prompt(message, defaultValue);
        }

        if (StringUtils.isBlank(packagename) || !ClassnameUtil.isValidPackageName(packagename)) {
            packagename = promptJavaPackagename(message, defaultValue);
        }

        return packagename;
    }

    protected String promptNotBlank(String message) throws PrompterException {
        return promptNotBlank(message, null);
    }

    protected String promptNotBlank(String message, String defaultValue) throws PrompterException {
        String value;
        if (StringUtils.isBlank(defaultValue)) {
            value = prompter.prompt(message);
        } else {
            value = prompt(message, defaultValue);
        }

        if (StringUtils.isBlank(value)) {
            value = promptNotBlank(message, defaultValue);
        }
        return value;
    }

    protected boolean promptForBoolean(String message) throws PrompterException {
        return promptForBoolean(message, null);
    }

    protected boolean promptForBoolean(String message, String defaultValue) throws PrompterException {
        String answer;
        boolean bool;
        if (StringUtils.isBlank(defaultValue)) {
            answer = prompter.prompt(message, ynAnswers);
        } else {
            answer = prompt(message, ynAnswers, defaultValue);
        }

        if ("y".equals(answer.toLowerCase())) {
            bool = true;
        } else {
            bool = false;
        }

        return bool;
    }

    protected String prompt(String message, String defaultValue) throws PrompterException {
        return prompter.prompt(message, defaultValue);
    }

    protected String prompt(String message, List possibleValues, String defaultValue) throws PrompterException {
        return prompter.prompt(message, possibleValues, defaultValue);
    }

    protected void suppressExamplesPrompt() {
        this.showExamplesPrompt = false;
    }

    protected void suppressAdvancedPrompt() {
        this.showAdvancedPrompt = false;
    }
}

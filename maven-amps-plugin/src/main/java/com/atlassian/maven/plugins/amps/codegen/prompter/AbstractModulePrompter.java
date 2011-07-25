package com.atlassian.maven.plugins.amps.codegen.prompter;

import com.atlassian.plugins.codegen.modules.PluginModuleLocation;
import com.atlassian.plugins.codegen.modules.PluginModuleProperties;
import com.atlassian.plugins.codegen.util.ClassnameUtil;
import org.apache.commons.lang.StringUtils;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;

import java.util.*;

/**
 * @since version
 */
public abstract class AbstractModulePrompter<T extends PluginModuleProperties> implements PluginModulePrompter<T> {
    protected final Prompter prompter;
    protected boolean showExamplesPrompt;
    protected boolean showAdvancedPrompt;
    protected boolean showAdvancedNamePrompt;

    public AbstractModulePrompter(Prompter prompter) {
        this.prompter = prompter;
        this.showExamplesPrompt = true;
        this.showAdvancedPrompt = true;
        this.showAdvancedNamePrompt = true;
    }

    @Override
    public <P extends PluginModuleProperties> P getModulePropertiesFromInput(PluginModuleLocation moduleLocation) throws PrompterException {
        T props = (T) promptForBasicProperties(moduleLocation);

        if (showAdvancedPrompt) {
            boolean showAdvanced = promptForBoolean("Show Advanced Setup?", "N");
            String moduleName;

            if (showAdvanced) {
                if(showAdvancedNamePrompt) {
                    moduleName = promptNotBlank("Plugin Name", props.getProperty(PluginModuleProperties.MODULE_NAME));
                } else {
                    moduleName = props.getProperty(PluginModuleProperties.MODULE_NAME);
                }
                String moduleKey = promptNotBlank("Plugin Key", props.getProperty(PluginModuleProperties.MODULE_KEY));
                String moduleDescription = promptNotBlank("Plugin Description", props.getProperty(PluginModuleProperties.DESCRIPTION));
                String moduleI18nNameKey = promptNotBlank("i18n Name Key", props.getProperty(PluginModuleProperties.NAME_I18N_KEY));
                String moduleI18nDescriptionKey = promptNotBlank("i18n Description Key", props.getProperty(PluginModuleProperties.DESCRIPTION_I18N_KEY));

                props.setModuleName(moduleName);
                props.setModuleKey(moduleKey);
                props.setDescription(moduleDescription);
                props.setNameI18nKey(moduleI18nNameKey);
                props.setDescriptionI18nKey(moduleI18nDescriptionKey);

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

        return (P)props;
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

    protected String promptFullyQualifiedJavaClass(String message, String defaultValue) throws PrompterException {
        String fqName;
        if (StringUtils.isBlank(defaultValue)) {
            fqName = prompter.prompt(message);
        } else {
            fqName = prompt(message, defaultValue);
        }

        String packageName = "";
        String className = "";
        if (fqName.contains(".")) {
            packageName = StringUtils.substringBeforeLast(fqName, ".");
            className = StringUtils.substringAfterLast(fqName, ".");
        } else {
            className = fqName;
        }

        if (StringUtils.isBlank(fqName) || !ClassnameUtil.isValidPackageName(packageName) || !ClassnameUtil.isValidClassName(className)) {
            fqName = promptFullyQualifiedJavaClass(message, defaultValue);
        }

        return fqName;
    }

    protected String promptFullyQualifiedJavaClassBlankOK(String message, String defaultValue) throws PrompterException {
        String fqName;
        if (StringUtils.isBlank(defaultValue)) {
            fqName = prompter.prompt(message);
        } else {
            fqName = prompt(message, defaultValue);
        }

        if (StringUtils.isNotBlank(fqName)){
            String packageName = "";
            String className = "";
            if (fqName.contains(".")) {
                packageName = StringUtils.substringBeforeLast(fqName, ".");
                className = StringUtils.substringAfterLast(fqName, ".");
            } else {
                className = fqName;
            }

             if(!ClassnameUtil.isValidPackageName(packageName) || !ClassnameUtil.isValidClassName(className)) {
                fqName = promptFullyQualifiedJavaClass(message, defaultValue);
            }
        }

        return fqName;
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
            answer = prompter.prompt(message, YN_ANSWERS);
        } else {
            answer = prompt(message, YN_ANSWERS, defaultValue);
        }

        if ("y".equals(answer.toLowerCase())) {
            bool = true;
        } else {
            bool = false;
        }

        return bool;
    }

    protected Map<String, String> promptForParams(String message) throws PrompterException {
        Map<String, String> params = new HashMap<String, String>();
        promptForParam(message, params);

        return params;
    }

    protected void promptForParam(String message, Map<String, String> params) throws PrompterException {
        StringBuffer addBuffer = new StringBuffer();
        if (params.size() > 0) {
            addBuffer.append("params:\n");
            for (Map.Entry<String, String> entry : params.entrySet()) {
                addBuffer.append(entry.getKey()).append("->").append(entry.getValue()).append("\n");
            }
        }
        addBuffer.append(message);
        boolean addParam = promptForBoolean(addBuffer.toString(), "N");

        if (addParam) {
            String key = promptNotBlank("param name");
            String value = promptNotBlank("param value");
            params.put(key, value);
            promptForParam(message, params);
        }
    }

    protected List<String> promptForList(String message) throws PrompterException {
        List<String> vals = new ArrayList<String>();
        promptForListValue(message, vals);

        return vals;
    }

    protected void promptForListValue(String message, List<String> vals) throws PrompterException {
        StringBuffer addBuffer = new StringBuffer();
        if (vals.size() > 0) {
            addBuffer.append("values:\n");
            for (String val : vals) {
                addBuffer.append(val).append("\n");
            }
        }
        addBuffer.append(message);
        boolean addValue = promptForBoolean(addBuffer.toString(), "N");

        if (addValue) {
            String value = promptNotBlank("value");
            vals.add(value);
            promptForListValue(message, vals);
        }
    }

    protected int promptForInt(String message, int defaultInt) throws PrompterException {
        String userVal = promptNotBlank(message, Integer.toString(defaultInt));
        int userInt;
        if (!StringUtils.isNumeric(userVal)) {
            userInt = promptForInt(message,defaultInt);
        } else {
            userInt = Integer.parseInt(userVal);
        }
        return userInt;
    }

    protected String prompt(String message, String defaultValue) throws PrompterException {
        return prompter.prompt(message, defaultValue);
    }

    protected String prompt(String message) throws PrompterException {
        return prompter.prompt(message);
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

    protected void suppressAdvancedNamePrompt() {
        this.showAdvancedNamePrompt = false;
    }
}

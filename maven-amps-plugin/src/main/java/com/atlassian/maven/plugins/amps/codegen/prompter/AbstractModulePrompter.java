package com.atlassian.maven.plugins.amps.codegen.prompter;

import com.atlassian.plugins.codegen.modules.NameBasedModuleProperties;
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
    public static final String DEFAULT_BASE_PACKAGE = "com.example";

    protected final Prompter prompter;
    protected boolean showExamplesPrompt;
    protected boolean showAdvancedPrompt;
    protected boolean showAdvancedNamePrompt;
    protected String defaultBasePackage;

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
                if(props instanceof NameBasedModuleProperties) {
                    NameBasedModuleProperties namedProps = (NameBasedModuleProperties) props;

                    if(showAdvancedNamePrompt) {
                        moduleName = promptNotBlank("Plugin Name", namedProps.getModuleName());
                    } else {
                        moduleName = namedProps.getModuleName();
                    }
                    String moduleKey = promptNotBlank("Plugin Key", namedProps.getModuleKey());
                    String moduleDescription = promptNotBlank("Plugin Description", namedProps.getDescription());
                    String moduleI18nNameKey = promptNotBlank("i18n Name Key", namedProps.getNameI18nKey());
                    String moduleI18nDescriptionKey = promptNotBlank("i18n Description Key", namedProps.getDescriptionI18nKey());

                    namedProps.setModuleName(moduleName);
                    namedProps.setModuleKey(moduleKey);
                    namedProps.setDescription(moduleDescription);
                    namedProps.setNameI18nKey(moduleI18nNameKey);
                    namedProps.setDescriptionI18nKey(moduleI18nDescriptionKey);

                    Properties currentI18n = props.getI18nProperties();
                    currentI18n.remove(NameBasedModuleProperties.NAME_I18N_KEY);
                    currentI18n.remove(NameBasedModuleProperties.DESCRIPTION_I18N_KEY);

                    currentI18n.setProperty(moduleI18nNameKey, moduleName);
                    currentI18n.setProperty(moduleI18nDescriptionKey, moduleDescription);
                }

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

    protected List<String> promptForList(String addMessage, String enterMessage) throws PrompterException {
        List<String> vals = new ArrayList<String>();
        promptForListValue(addMessage, enterMessage, vals);

        return vals;
    }

    protected void promptForListValue(String addMessage, String enterMessage, List<String> vals) throws PrompterException {
        StringBuffer addBuffer = new StringBuffer();
        if (vals.size() > 0) {
            addBuffer.append("values:\n");
            for (String val : vals) {
                addBuffer.append(val).append("\n");
            }
        }
        addBuffer.append(addMessage);
        boolean addValue = promptForBoolean(addBuffer.toString(), "N");

        if (addValue) {
            String value = promptNotBlank(enterMessage);
            vals.add(value);
            promptForListValue(addMessage, enterMessage, vals);
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

    @Override
    public void setDefaultBasePackage(String basePackage) {
        if(StringUtils.isNotBlank(basePackage)) {
            this.defaultBasePackage = basePackage;
        }
    }

    @Override
    public String getDefaultBasePackage() {
        if(StringUtils.isNotBlank(defaultBasePackage)) {
            return defaultBasePackage;
        } else {
            return DEFAULT_BASE_PACKAGE;
        }
    }
}

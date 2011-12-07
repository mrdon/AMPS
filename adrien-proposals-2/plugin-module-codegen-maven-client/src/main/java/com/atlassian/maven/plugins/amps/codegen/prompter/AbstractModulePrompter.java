package com.atlassian.maven.plugins.amps.codegen.prompter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.atlassian.plugins.codegen.modules.NameBasedModuleProperties;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;
import com.atlassian.plugins.codegen.modules.PluginModuleProperties;
import com.atlassian.plugins.codegen.util.ClassnameUtil;

import org.apache.commons.lang.StringUtils;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;

import jline.ANSIBuffer;

/**
 * @since 3.6
 */
public abstract class AbstractModulePrompter<T extends PluginModuleProperties> implements PluginModulePrompter<T>
{
    public static final String DEFAULT_BASE_PACKAGE = "com.example";
    public static final String MODULE_NAME_PROMPT = "Module Name";
    public static final String MODULE_KEY_PROMPT = "Module Key";
    public static final String MODULE_DESCRIP_PROMPT = "Module Description";

    protected final Prompter prompter;
    protected boolean showExamplesPrompt;
    protected boolean showAdvancedPrompt;
    protected boolean showAdvancedNamePrompt;
    protected String defaultBasePackage;
    protected boolean useAnsiColor;

    public AbstractModulePrompter(Prompter prompter)
    {
        this.prompter = prompter;
        this.showExamplesPrompt = true;
        this.showAdvancedPrompt = true;
        this.showAdvancedNamePrompt = true;

        String mavencolor = System.getenv("MAVEN_COLOR");
        if (StringUtils.isNotBlank(mavencolor))
        {
            useAnsiColor = Boolean.parseBoolean(mavencolor);
        } else
        {
            useAnsiColor = false;
        }
    }

    @Override
    public T getModulePropertiesFromInput(PluginModuleLocation moduleLocation) throws PrompterException
    {
        //!!! REMOVE THIS WHEN WE SUPPORT EXAMPLE CODE
        suppressExamplesPrompt();

        T props = (T) promptForBasicProperties(moduleLocation);

        if (showAdvancedPrompt)
        {
            boolean showAdvanced = promptForBoolean("Show Advanced Setup?", "N");
            String moduleName;

            if (showAdvanced)
            {
                if (props instanceof NameBasedModuleProperties)
                {
                    NameBasedModuleProperties namedProps = (NameBasedModuleProperties) props;

                    if (showAdvancedNamePrompt)
                    {
                        moduleName = promptNotBlank(MODULE_NAME_PROMPT, namedProps.getModuleName());
                    } else
                    {
                        moduleName = namedProps.getModuleName();
                    }
                    String moduleKey = promptNotBlank(MODULE_KEY_PROMPT, namedProps.getModuleKey());
                    String moduleDescription = promptNotBlank(MODULE_DESCRIP_PROMPT, namedProps.getDescription());
                    String moduleI18nNameKey = promptNotBlank("i18n Name Key", namedProps.getNameI18nKey());
                    String moduleI18nDescriptionKey = promptNotBlank("i18n Description Key", namedProps.getDescriptionI18nKey());

                    namedProps.setModuleName(moduleName);
                    namedProps.setModuleKey(moduleKey);
                    namedProps.setDescription(moduleDescription);
                    namedProps.setNameI18nKey(moduleI18nNameKey);
                    namedProps.setDescriptionI18nKey(moduleI18nDescriptionKey);
                }

                promptForAdvancedProperties(props, moduleLocation);
            }
        }

        if (showExamplesPrompt)
        {
            props.setIncludeExamples(promptForBoolean("Include Example Code?", "N"));
        }

        return props;
    }

    @Override
    public abstract T promptForBasicProperties(PluginModuleLocation moduleLocation) throws PrompterException;

    @Override
    public void promptForAdvancedProperties(T props, PluginModuleLocation moduleLocation) throws PrompterException
    {
    }

    protected String promptJavaClassname(String message) throws PrompterException
    {
        return promptJavaClassname(message, null);
    }

    protected String promptJavaClassname(String message, String defaultValue) throws PrompterException
    {
        String classname;
        if (StringUtils.isBlank(defaultValue))
        {
            classname = prompter.prompt(requiredMessage(message));
        } else
        {
            classname = prompt(message, defaultValue);
        }

        if (StringUtils.isBlank(classname) || !ClassnameUtil.isValidClassName(classname))
        {
            classname = promptJavaClassname(message, defaultValue);
        }

        return classname;
    }

    protected String promptJavaPackagename(String message) throws PrompterException
    {
        return promptJavaPackagename(message, null);
    }

    protected String promptFullyQualifiedJavaClass(String message, String defaultValue) throws PrompterException
    {
        String fqName;
        if (StringUtils.isBlank(defaultValue))
        {

            fqName = prompter.prompt(requiredMessage(message));
        } else
        {
            fqName = prompt(message, defaultValue);
        }

        String packageName = "";
        String className = "";
        if (fqName.contains("."))
        {
            packageName = StringUtils.substringBeforeLast(fqName, ".");
            className = StringUtils.substringAfterLast(fqName, ".");
        } else
        {
            className = fqName;
        }

        if (StringUtils.isBlank(fqName) || !ClassnameUtil.isValidPackageName(packageName) || !ClassnameUtil.isValidClassName(className))
        {
            fqName = promptFullyQualifiedJavaClass(message, defaultValue);
        }

        return fqName;
    }

    protected String promptFullyQualifiedJavaClassBlankOK(String message, String defaultValue) throws PrompterException
    {
        String fqName;
        if (StringUtils.isBlank(defaultValue))
        {
            fqName = prompter.prompt(message);
        } else
        {
            fqName = prompt(message, defaultValue);
        }

        if (StringUtils.isNotBlank(fqName))
        {
            String packageName = "";
            String className = "";
            if (fqName.contains("."))
            {
                packageName = StringUtils.substringBeforeLast(fqName, ".");
                className = StringUtils.substringAfterLast(fqName, ".");
            } else
            {
                className = fqName;
            }

            if (!ClassnameUtil.isValidPackageName(packageName) || !ClassnameUtil.isValidClassName(className))
            {
                fqName = promptFullyQualifiedJavaClass(message, defaultValue);
            }
        }

        return fqName;
    }

    protected String promptJavaPackagename(String message, String defaultValue) throws PrompterException
    {
        String packagename;

        if (StringUtils.isBlank(defaultValue))
        {
            packagename = prompter.prompt(requiredMessage(message));
        } else
        {
            packagename = prompt(message, defaultValue);
        }

        if (StringUtils.isBlank(packagename) || !ClassnameUtil.isValidPackageName(packagename))
        {
            packagename = promptJavaPackagename(message, defaultValue);
        }

        return packagename;
    }

    protected String promptNotBlank(String message) throws PrompterException
    {
        return promptNotBlank(message, null);
    }

    protected String promptNotBlank(String message, String defaultValue) throws PrompterException
    {
        String value;
        if (StringUtils.isBlank(defaultValue))
        {
            value = prompter.prompt(requiredMessage(message));
        } else
        {
            value = prompt(message, defaultValue);
        }

        if (StringUtils.isBlank(value))
        {
            value = promptNotBlank(message, defaultValue);
        }
        return value;
    }

    protected boolean promptForBoolean(String message) throws PrompterException
    {
        return promptForBoolean(message, null);
    }

    protected boolean promptForBoolean(String message, String defaultValue) throws PrompterException
    {
        String answer;
        boolean bool;
        if (StringUtils.isBlank(defaultValue))
        {
            answer = prompter.prompt(requiredMessage(message), YN_ANSWERS);
        } else
        {
            answer = prompt(message, YN_ANSWERS, defaultValue);
        }

        if ("y".equals(answer.toLowerCase()))
        {
            bool = true;
        } else
        {
            bool = false;
        }

        return bool;
    }

    protected Map<String, String> promptForParams(String message) throws PrompterException
    {
        Map<String, String> params = new HashMap<String, String>();
        promptForParam(message, params);

        return params;
    }

    protected void promptForParam(String message, Map<String, String> params) throws PrompterException
    {
        StringBuffer addBuffer = new StringBuffer();
        if (params.size() > 0)
        {
            addBuffer.append("params:\n");
            for (Map.Entry<String, String> entry : params.entrySet())
            {
                addBuffer.append(entry.getKey())
                        .append("->")
                        .append(entry.getValue())
                        .append("\n");
            }
        }
        addBuffer.append(message);
        boolean addParam = promptForBoolean(addBuffer.toString(), "N");

        if (addParam)
        {
            String key = promptNotBlank("param name");
            String value = promptNotBlank("param value");
            params.put(key, value);
            promptForParam(message, params);
        }
    }

    protected List<String> promptForList(String addMessage, String enterMessage) throws PrompterException
    {
        List<String> vals = new ArrayList<String>();
        promptForListValue(addMessage, enterMessage, vals);

        return vals;
    }

    protected void promptForListValue(String addMessage, String enterMessage, List<String> vals) throws PrompterException
    {
        StringBuffer addBuffer = new StringBuffer();
        if (vals.size() > 0)
        {
            addBuffer.append("values:\n");
            for (String val : vals)
            {
                addBuffer.append(val)
                        .append("\n");
            }
        }
        addBuffer.append(addMessage);
        boolean addValue = promptForBoolean(addBuffer.toString(), "N");

        if (addValue)
        {
            String value = promptNotBlank(enterMessage);
            vals.add(value);
            promptForListValue(addMessage, enterMessage, vals);
        }
    }

    protected int promptForInt(String message, int defaultInt) throws PrompterException
    {
        String userVal = promptNotBlank(message, Integer.toString(defaultInt));
        int userInt;
        if (!StringUtils.isNumeric(userVal))
        {
            userInt = promptForInt(message, defaultInt);
        } else
        {
            userInt = Integer.parseInt(userVal);
        }
        return userInt;
    }

    protected String prompt(String message, String defaultValue) throws PrompterException
    {
        return prompter.prompt(message, defaultValue);
    }

    protected String prompt(String message) throws PrompterException
    {
        return prompter.prompt(message);
    }

    protected String prompt(String message, List possibleValues, String defaultValue) throws PrompterException
    {
        return prompter.prompt(message, possibleValues, defaultValue);
    }

    protected void suppressExamplesPrompt()
    {
        this.showExamplesPrompt = false;
    }

    protected void suppressAdvancedPrompt()
    {
        this.showAdvancedPrompt = false;
    }

    protected void suppressAdvancedNamePrompt()
    {
        this.showAdvancedNamePrompt = false;
    }

    public boolean isUseAnsiColor()
    {
        return useAnsiColor;
    }

    public void setUseAnsiColor(boolean useAnsiColor)
    {
        this.useAnsiColor = useAnsiColor;
    }

    protected String requiredMessage(String message)
    {
        String formattedMessage = message;
        if (useAnsiColor)
        {
            ANSIBuffer ansiBuffer = new ANSIBuffer();
            ansiBuffer.append(ANSIBuffer.ANSICodes
                    .attrib(PrettyPrompter.BOLD))
                    .append(ANSIBuffer.ANSICodes
                            .attrib(PrettyPrompter.FG_RED))
                    .append(message)
                    .append(ANSIBuffer.ANSICodes
                            .attrib(PrettyPrompter.OFF));
            formattedMessage = ansiBuffer.toString();
        }

        return formattedMessage;
    }

    @Override
    public void setDefaultBasePackage(String basePackage)
    {
        if (StringUtils.isNotBlank(basePackage))
        {
            this.defaultBasePackage = basePackage;
        }
    }

    @Override
    public String getDefaultBasePackage()
    {
        if (StringUtils.isNotBlank(defaultBasePackage))
        {
            return defaultBasePackage;
        } else
        {
            return DEFAULT_BASE_PACKAGE;
        }
    }
}

package com.atlassian.maven.plugins.amps.codegen.prompter;

import com.atlassian.plugins.codegen.util.ClassnameUtil;
import org.apache.commons.lang.StringUtils;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Author: jdoklovic
 */
public abstract class AbstractModulePrompter implements PluginModulePrompter {
    protected final List<String> ynAnswers;
    protected final Prompter prompter;

    public AbstractModulePrompter(Prompter prompter) {
        this.prompter = prompter;

        ynAnswers = new ArrayList<String>(Arrays.asList("Y","y","N","n"));
    }

    protected String promptJavaClassname(String message) throws PrompterException {
        return promptJavaClassname(message,null);
    }

    protected String promptJavaClassname(String message, String defaultValue) throws PrompterException {
        String classname;
        if(StringUtils.isBlank(defaultValue)) {
            classname = prompter.prompt(message);
        } else {
            classname = prompter.prompt(message,defaultValue);
        }

        if(StringUtils.isBlank(classname) || !ClassnameUtil.isValidClassName(classname)){
            classname = promptJavaClassname(message,defaultValue);
        }

        return classname;
    }

    protected String promptJavaPackagename(String message) throws PrompterException {
        return promptJavaPackagename(message,null);
    }

    protected String promptJavaPackagename(String message, String defaultValue) throws PrompterException {
        String packagename;

        if(StringUtils.isBlank(defaultValue)) {
            packagename = prompter.prompt(message);
        } else {
            packagename = prompter.prompt(message,defaultValue);
        }

        if(StringUtils.isBlank(packagename) || !ClassnameUtil.isValidPackageName(packagename)){
            packagename = promptJavaPackagename(message, defaultValue);
        }

        return packagename;
    }

    protected String promptNotBlank(String message) throws PrompterException {
        return promptNotBlank(message,null);
    }

    protected String promptNotBlank(String message,String defaultValue) throws PrompterException {
        String value;
        if(StringUtils.isBlank(defaultValue)) {
            value = prompter.prompt(message);
        } else {
            value = prompter.prompt(message,defaultValue);
        }

        if(StringUtils.isBlank(value)){
            value = promptNotBlank(message, defaultValue);
        }
         return value;
    }

    protected boolean promptForBoolean(String message) throws PrompterException {
        return promptForBoolean(message,null);
    }

    protected boolean promptForBoolean(String message, String defaultValue) throws PrompterException {
        String answer;
        boolean bool;
        if(StringUtils.isBlank(defaultValue)) {
            answer = prompter.prompt(message,ynAnswers);
        } else {
            answer = prompter.prompt(message,ynAnswers,defaultValue);
        }

        if("y".equals(answer.toLowerCase())) {
            bool = true;
        } else {
            bool = false;
        }

        return bool;
    }
}

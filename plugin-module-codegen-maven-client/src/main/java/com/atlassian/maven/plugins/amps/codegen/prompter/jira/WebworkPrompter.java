package com.atlassian.maven.plugins.amps.codegen.prompter.jira;

import java.util.ArrayList;
import java.util.List;

import com.atlassian.maven.plugins.amps.codegen.annotations.ModuleCreatorClass;
import com.atlassian.maven.plugins.amps.codegen.prompter.AbstractModulePrompter;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;
import com.atlassian.plugins.codegen.modules.jira.ActionProperties;
import com.atlassian.plugins.codegen.modules.jira.View;
import com.atlassian.plugins.codegen.modules.jira.WebworkModuleCreator;
import com.atlassian.plugins.codegen.modules.jira.WebworkProperties;
import com.atlassian.plugins.codegen.util.ClassnameUtil;

import org.apache.commons.io.FilenameUtils;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;

/**
 * @since 3.6
 */
@ModuleCreatorClass(WebworkModuleCreator.class)
public class WebworkPrompter extends AbstractModulePrompter<WebworkProperties>
{

    public WebworkPrompter(Prompter prompter)
    {
        super(prompter);

    }

    @Override
    public WebworkProperties promptForBasicProperties(PluginModuleLocation moduleLocation) throws PrompterException
    {
        suppressAdvancedNamePrompt();
        String moduleName = promptNotBlank("Enter Plugin Module Name", "My Webwork Module");

        WebworkProperties props = new WebworkProperties(moduleName);

        props.setActions(createDefaultAction(props));

        return props;
    }

    private List<ActionProperties> createDefaultAction(WebworkProperties props)
    {
        List<ActionProperties> actions = new ArrayList<ActionProperties>();
        String packageName = getDefaultBasePackage() + ".jira.webwork";
        String className = ClassnameUtil.removeSpaces(props.getModuleName()) + "Action";

        String fqName = ClassnameUtil.fullyQualifiedName(packageName, className);
        ActionProperties action = new ActionProperties(fqName);
        action.setAlias(ClassnameUtil.removeSpaces(props.getModuleName()));

        String templatePath = "/templates/" + props.getModuleKey() + "/";
        View success = new View("success", templatePath + "success.vm");
        View input = new View("input", templatePath + "input.vm");
        View error = new View("error", templatePath + "error.vm");

        action.addView(success);
        action.addView(input);
        action.addView(error);

        actions.add(action);

        return actions;
    }

    @Override
    public void promptForAdvancedProperties(WebworkProperties props, PluginModuleLocation moduleLocation) throws PrompterException
    {
        props.setActions(promptForActions(props));
    }

    private List<ActionProperties> promptForActions(WebworkProperties props) throws PrompterException
    {
        List<ActionProperties> actions = new ArrayList<ActionProperties>();

        String initialPackage = getDefaultBasePackage() + ".jira.webwork";
        String templatePathPrefix = "/templates/" + props.getModuleKey() + "/";
        promptForAction(actions, initialPackage, templatePathPrefix);

        return actions;
    }

    private void promptForAction(List<ActionProperties> actions, String packageName, String templatePathPrefix) throws PrompterException
    {

        String className = promptJavaClassname("Enter Action Classname", "MyActionClass");
        String newPackageName = promptJavaPackagename("Enter Package Name", packageName);

        String fqClass = ClassnameUtil.fullyQualifiedName(newPackageName, className);
        String alias = promptNotBlank("Enter Alias", className);

        ActionProperties action = new ActionProperties(fqClass);
        action.setAlias(alias);

        action.setViews(promptForViews(action, templatePathPrefix + className.toLowerCase() + "/"));

        actions.add(action);

        if (promptForBoolean("Add Another Action?", "N"))
        {
            promptForAction(actions, newPackageName, templatePathPrefix);
        }
    }

    private List<View> promptForViews(ActionProperties action, String templatePath) throws PrompterException
    {
        List<View> views = new ArrayList<View>();

        promptForView(views, templatePath);

        return views;
    }

    private void promptForView(List<View> views, String templatePath) throws PrompterException
    {
        String viewName = promptNotBlank("Enter View Name", "success");

        String pathWithEndSlash = templatePath;
        if (!pathWithEndSlash.endsWith("/"))
        {
            pathWithEndSlash = pathWithEndSlash + "/";
        }

        String viewPath = promptNotBlank("Enter Template Path", pathWithEndSlash + viewName + ".vm");

        View view = new View(viewName, viewPath);
        views.add(view);

        if (promptForBoolean("Add Another View?", "N"))
        {
            promptForView(views, FilenameUtils.getFullPath(viewPath));
        }
    }
}
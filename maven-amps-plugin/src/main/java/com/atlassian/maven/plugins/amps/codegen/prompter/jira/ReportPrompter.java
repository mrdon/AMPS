package com.atlassian.maven.plugins.amps.codegen.prompter.jira;

import com.atlassian.maven.plugins.amps.codegen.annotations.ModuleCreatorClass;
import com.atlassian.maven.plugins.amps.codegen.prompter.AbstractModulePrompter;
import com.atlassian.maven.plugins.amps.codegen.prompter.common.AbstractResourcePrompter;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;
import com.atlassian.plugins.codegen.modules.PluginModuleProperties;
import com.atlassian.plugins.codegen.modules.common.Label;
import com.atlassian.plugins.codegen.modules.common.Resource;
import com.atlassian.plugins.codegen.modules.jira.ReportModuleCreator;
import com.atlassian.plugins.codegen.modules.jira.ReportProperties;
import com.atlassian.plugins.codegen.util.ClassnameUtil;
import org.apache.commons.lang.StringUtils;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;

import java.util.ArrayList;
import java.util.List;

/**
 * @since version
 */
@ModuleCreatorClass(ReportModuleCreator.class)
public class ReportPrompter extends AbstractResourcePrompter<ReportProperties> {

    public ReportPrompter(Prompter prompter) {
        super(prompter);

    }

    @Override
    public ReportProperties promptForBasicProperties(PluginModuleLocation moduleLocation) throws PrompterException {
        String className = promptJavaClassname("Enter New Classname", "MyReport");
        String packageName = promptJavaPackagename("Enter Package Name", getDefaultBasePackage() + ".jira.reports");

        String fqClass = ClassnameUtil.fullyQualifiedName(packageName, className);

        ReportProperties props =  new ReportProperties(fqClass);

        List<Resource> resources = new ArrayList<Resource>(2);

        String templatePath = "/templates/reports/" + props.getModuleKey() + "/";

        Resource view = new Resource();
        view.setName("view");
        view.setType("velocity");
        view.setLocation(templatePath + "view.vm");

        resources.add(view);

        props.setResources(resources);

        addI18nResource(props);

        Label label = new Label(props.getModuleKey() + ".label",props.getModuleName());
        props.setLabel(label);

        return props;
    }

    @Override
    public void promptForAdvancedProperties(ReportProperties props, PluginModuleLocation moduleLocation) throws PrompterException {
        props.setResources(promptForResources());
        addI18nResource(props);

        //LABEL
        Label label = props.getLabel();
        String labelKey = promptNotBlank("Enter Label Key", props.getLabel().getKey());
        String labelValue = promptNotBlank("Enter Label Value", props.getLabel().getValue());

        label.setKey(labelKey);
        label.setValue(labelValue);

        props.addI18nProperty(labelKey,labelValue);

        List<String> labelParamVals = promptForList("Add Label Param?","Enter Param Value");
        if (!labelParamVals.isEmpty()) {
            for (String labelVal : labelParamVals) {
                label.addParam(labelVal);
            }
        }
    }

    @Override
    protected Resource promptForResource() throws PrompterException {
        Resource resource = new Resource();
        resource.setName(promptNotBlank("Enter Resource Name"));

        resource.setType("velocity");
        resource.setLocation(promptNotBlank("Enter Location (path to resource file)"));

        return resource;
    }

    protected void addI18nResource(ReportProperties props) {
        Resource resource = new Resource();
        resource.setName("i18n");
        resource.setLocation(props.getClassname());
        resource.setType("i18n");

        props.addResource(resource);
    }
}
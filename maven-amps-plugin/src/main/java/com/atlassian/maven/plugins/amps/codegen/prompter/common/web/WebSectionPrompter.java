package com.atlassian.maven.plugins.amps.codegen.prompter.common.web;

import com.atlassian.maven.plugins.amps.codegen.annotations.ModuleCreatorClass;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;
import com.atlassian.plugins.codegen.modules.PluginModuleProperties;
import com.atlassian.plugins.codegen.modules.common.Label;
import com.atlassian.plugins.codegen.modules.common.Tooltip;
import com.atlassian.plugins.codegen.modules.common.web.WebSectionModuleCreator;
import com.atlassian.plugins.codegen.modules.common.web.WebSectionProperties;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;

import java.util.List;
import java.util.Map;

/**
 * @since version
 */
@ModuleCreatorClass(WebSectionModuleCreator.class)
public class WebSectionPrompter extends AbstractWebFragmentPrompter<WebSectionProperties> {

    public WebSectionPrompter(Prompter prompter) {
        super(prompter);

    }

    @Override
    public WebSectionProperties promptForBasicProperties(PluginModuleLocation moduleLocation) throws PrompterException {
        String moduleName = promptNotBlank("Enter Plugin Module Name", "My Web Section");
        String location = promptNotBlank("Enter Location (e.g. system.admin/mynewsection)");

        WebSectionProperties props = new WebSectionProperties(moduleName, location);

        Label label = new Label(props.getModuleKey() + ".label", props.getModuleName());
        props.setLabel(label);

        suppressAdvancedNamePrompt();

        return props;
    }

    @Override
    public void promptForAdvancedProperties(WebSectionProperties props, PluginModuleLocation moduleLocation) throws PrompterException {
        //WEIGHT
        props.setWeight(promptForInt("Weight", 1000));

        //LABEL
        Label label = props.getLabel();
        String labelKey = promptNotBlank("Enter Label Key", props.getLabel().getKey());
        String labelValue = promptNotBlank("Enter Label Value", props.getLabel().getValue());

        label.setKey(labelKey);
        label.setValue(labelValue);

        props.addI18nProperty(labelKey, labelValue);

        List<String> labelParamVals = promptForList("Add Label Param?","Enter Param Value");
        if (!labelParamVals.isEmpty()) {
            for (String labelVal : labelParamVals) {
                label.addParam(labelVal);
            }
        }

        //TOOLTIP
        if (promptForBoolean("Add Tooltip?", "N")) {
            String tooltipKey = promptNotBlank("Enter Tooltip Key", props.getModuleKey() + ".tooltip");
            String tooltipValue = promptNotBlank("Enter Tooltip Value", props.getModuleName() + " Tooltip");
            Tooltip tooltip = new Tooltip(tooltipKey, tooltipValue);

            List<String> tooltipParamVals = promptForList("Add Tooltip Param?","Enter Param Value");
            if (!tooltipParamVals.isEmpty()) {
                for (String tipVal : tooltipParamVals) {
                    tooltip.addParam(tipVal);
                }
            }

            props.setTooltip(tooltip);
            props.addI18nProperty(tooltipKey, tooltipValue);
        }

        //RESOURCES
        props.setResources(promptForResources());

        //CONTEXT PROVIDER
        props.setContextProvider(promptForContextProvider());

        //MODULE PARAMS
        Map<String, String> moduleParams = promptForParams("Add Plugin Module Param?");
        props.setParams(moduleParams);

        //CONDITIONS
        props.setConditions(promptForConditions());
    }
}
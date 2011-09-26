package com.atlassian.maven.plugins.amps.codegen.prompter.common.web;

import java.util.List;
import java.util.Map;

import com.atlassian.maven.plugins.amps.codegen.annotations.ModuleCreatorClass;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;
import com.atlassian.plugins.codegen.modules.common.Icon;
import com.atlassian.plugins.codegen.modules.common.Label;
import com.atlassian.plugins.codegen.modules.common.Link;
import com.atlassian.plugins.codegen.modules.common.Tooltip;
import com.atlassian.plugins.codegen.modules.common.web.WebItemModuleCreator;
import com.atlassian.plugins.codegen.modules.common.web.WebItemProperties;

import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;

/**
 * @since 3.5
 */
@ModuleCreatorClass(WebItemModuleCreator.class)
public class WebItemPrompter extends AbstractWebFragmentPrompter<WebItemProperties>
{

    public WebItemPrompter(Prompter prompter)
    {
        super(prompter);

    }

    @Override
    public WebItemProperties promptForBasicProperties(PluginModuleLocation moduleLocation) throws PrompterException
    {
        String moduleName = promptNotBlank("Enter Plugin Module Name", "My Web Item");
        String section = promptNotBlank("Enter Section (e.g. system.admin/globalsettings)");

        WebItemProperties props = new WebItemProperties(moduleName, section);

        String linkPath = promptNotBlank("Enter Link URL (e.g. /secure/CreateIssue!default.jspa)");
        Link link = new Link(linkPath);
        link.setLinkId(props.getModuleKey() + "-link");
        props.setLink(link);

        Label label = new Label(props.getModuleKey() + ".label", props.getModuleName());
        props.setLabel(label);

        suppressAdvancedNamePrompt();

        return props;
    }

    @Override
    public void promptForAdvancedProperties(WebItemProperties props, PluginModuleLocation moduleLocation) throws PrompterException
    {
        //WEIGHT
        props.setWeight(promptForInt("Weight", 1000));

        //LINK
        props.getLink()
                .setLinkId(promptNotBlank("Link Id", props.getLink()
                        .getLinkId()));

        //LABEL
        Label label = props.getLabel();
        String labelKey = promptNotBlank("Enter Label Key", props.getLabel()
                .getKey());
        String labelValue = promptNotBlank("Enter Label Value", props.getLabel()
                .getValue());

        label.setKey(labelKey);
        label.setValue(labelValue);

        props.addI18nProperty(labelKey, labelValue);

        List<String> labelParamVals = promptForList("Add Label Param?", "Enter Param Value");
        if (!labelParamVals.isEmpty())
        {
            for (String labelVal : labelParamVals)
            {
                label.addParam(labelVal);
            }
        }

        //ICON
        if (promptForBoolean("Add Icon?", "N"))
        {
            String iconPath = promptNotBlank("Icon Location (e.g. /images/icons/print.gif)");
            int width = promptForInt("Icon Width", 16);
            int height = promptForInt("Icon Height", 16);

            Link iconLink = new Link(iconPath);
            iconLink.setLinkId(props.getModuleKey() + "-icon");

            Icon icon = new Icon(width, height);
            icon.setLink(iconLink);

            props.setIcon(icon);
        }

        //TOOLTIP
        if (promptForBoolean("Add Tooltip?", "N"))
        {
            String tooltipKey = promptNotBlank("Enter Tooltip Key", props.getModuleKey() + ".tooltip");
            String tooltipValue = promptNotBlank("Enter Tooltip Value", props.getModuleName() + " Tooltip");
            Tooltip tooltip = new Tooltip(tooltipKey, tooltipValue);

            List<String> tooltipParamVals = promptForList("Add Tooltip Param?", "Enter Param Value");
            if (!tooltipParamVals.isEmpty())
            {
                for (String tipVal : tooltipParamVals)
                {
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
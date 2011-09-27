package com.atlassian.plugins.codegen.modules.common.web;

import java.util.HashMap;
import java.util.Map;

import com.atlassian.plugins.codegen.modules.common.Icon;
import com.atlassian.plugins.codegen.modules.common.Label;
import com.atlassian.plugins.codegen.modules.common.Link;
import com.atlassian.plugins.codegen.modules.common.Tooltip;

/**
 * @since 3.6
 */
public class WebItemProperties extends AbstractWebFragmentProperties
{

    public static final String SECTION = "SECTION";
    public static final String ICON = "ICON";
    public static final String LINK = "LINK";
    public static final String LABEL = "LABEL";
    public static final String PARAMS = "PARAMS";
    public static final String TOOLTIP = "TOOLTIP";

    public WebItemProperties()
    {
        this("My Web Item");
    }

    public WebItemProperties(String moduleName)
    {
        super(moduleName);
        setParams(new HashMap<String, String>());
    }

    public WebItemProperties(String moduleName, String section)
    {
        this(moduleName);
        setSection(section);
    }

    public void setSection(String section)
    {
        setProperty(SECTION, section);
    }

    public String getSection()
    {
        return getProperty(SECTION);
    }

    public void setLink(Link link)
    {
        put(LINK, link);
    }

    public Link getLink()
    {
        Link link = null;
        if (keySet().contains(LINK))
        {
            link = (Link) get(LINK);
        }
        return link;
    }

    public void setIcon(Icon icon)
    {
        put(ICON, icon);
    }

    public Icon getIcon()
    {
        Icon icon = null;
        if (keySet().contains(ICON))
        {
            icon = (Icon) get(ICON);
        }
        return icon;
    }

    public void setLabel(Label label)
    {
        put(LABEL, label);
        addI18nProperty(label.getKey(), label.getValue());
    }

    public Label getLabel()
    {
        Label label = null;
        if (keySet().contains(LABEL))
        {
            label = (Label) get(LABEL);
        }
        return label;
    }

    public void setTooltip(Tooltip tooltip)
    {
        put(TOOLTIP, tooltip);
        addI18nProperty(tooltip.getKey(), tooltip.getValue());
    }

    public Tooltip getTooltip()
    {
        Tooltip tooltip = null;
        if (keySet().contains(TOOLTIP))
        {
            tooltip = (Tooltip) get(TOOLTIP);
        }
        return tooltip;
    }

    public void setParams(Map<String, String> params)
    {
        put(PARAMS, params);
    }

    public void addParam(String name, String value)
    {
        ((Map<String, String>) get(PARAMS)).put(name, value);
    }

}

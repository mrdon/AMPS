package com.atlassian.maven.plugins.amps.codegen.prompter.common;

import com.atlassian.maven.plugins.amps.codegen.annotations.ModuleCreatorClass;
import com.atlassian.maven.plugins.amps.codegen.prompter.AbstractModulePrompter;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;
import com.atlassian.plugins.codegen.modules.PluginModuleProperties;
import com.atlassian.plugins.codegen.modules.common.GadgetModuleCreator;
import com.atlassian.plugins.codegen.modules.common.GadgetProperties;
import org.apache.commons.lang.StringUtils;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;

/**
 * @since 3.5
 */
@ModuleCreatorClass(GadgetModuleCreator.class)
public class GadgetPrompter extends AbstractModulePrompter<GadgetProperties> {

    public GadgetPrompter(Prompter prompter) {
        super(prompter);

    }

    @Override
    public GadgetProperties promptForBasicProperties(PluginModuleLocation moduleLocation) throws PrompterException {
        String moduleName = promptNotBlank("Enter Gadget Name", "My Gadget");

        GadgetProperties props = new GadgetProperties(moduleName);
        String gadgetLocation = promptNotBlank("Enter Gadget XML location", "gadgets/" + props.getModuleKey() + "/gadget.xml");

        props.setLocation(gadgetLocation);

        return props;
    }

    @Override
    public void promptForAdvancedProperties(GadgetProperties props, PluginModuleLocation moduleLocation) throws PrompterException {

    }
}
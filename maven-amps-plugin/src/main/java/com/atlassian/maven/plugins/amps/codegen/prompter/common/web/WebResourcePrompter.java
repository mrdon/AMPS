package com.atlassian.maven.plugins.amps.codegen.prompter.common.web;

import com.atlassian.maven.plugins.amps.codegen.annotations.ModuleCreatorClass;
import com.atlassian.maven.plugins.amps.codegen.prompter.AbstractModulePrompter;
import com.atlassian.plugins.codegen.annotations.Dependencies;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;
import com.atlassian.plugins.codegen.modules.PluginModuleProperties;
import com.atlassian.plugins.codegen.modules.common.Label;
import com.atlassian.plugins.codegen.modules.common.Resource;
import com.atlassian.plugins.codegen.modules.common.web.WebResourceModuleCreator;
import com.atlassian.plugins.codegen.modules.common.web.WebResourceProperties;
import com.atlassian.plugins.codegen.modules.common.web.WebResourceTransformation;
import com.atlassian.plugins.codegen.modules.common.web.WebSectionProperties;
import org.apache.commons.lang.StringUtils;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;

import java.util.ArrayList;
import java.util.List;

/**
 * @since version
 */
@ModuleCreatorClass(WebResourceModuleCreator.class)
public class WebResourcePrompter extends AbstractWebFragmentPrompter<WebResourceProperties> {

    public static final String CUSTOM_CONTEXT = "Custom Context";

    public WebResourcePrompter(Prompter prompter) {
        super(prompter);

    }

    @Override
    public WebResourceProperties promptForBasicProperties(PluginModuleLocation moduleLocation) throws PrompterException {
        String moduleName = promptNotBlank("Enter Plugin Module Name", "My Web Resource");

        WebResourceProperties props = new WebResourceProperties(moduleName);
        List<Resource> resourceList = new ArrayList<Resource>();
        resourceList.add(promptForResource());

        promptForResources(resourceList);

        props.setResources(resourceList);

        suppressAdvancedNamePrompt();

        return props;

    }

    @Override
    public void promptForAdvancedProperties(WebResourceProperties props, PluginModuleLocation moduleLocation) throws PrompterException {
        props.setDependencies(promptForList("Add Dependency?"));
        props.setContexts(promptForContexts(props.knownContexts()));
        props.setTransformations(promptForTransformations());
        props.setConditions(promptForConditions());
    }

    private List<String> promptForContexts(List<String> knownContexts) throws PrompterException {
        List<String> contexts = new ArrayList<String>();
        List<String> mutableValues = new ArrayList<String>(knownContexts);

        promptForContext(contexts, mutableValues);

        return contexts;
    }

    private void promptForContext(List<String> contexts, List<String> knownContexts) throws PrompterException {

        if (promptForBoolean("Add Web Resource Context?", "N")) {
            StringBuilder contextQuery = new StringBuilder("Choose A Context\n");
            List<String> indexChoices = new ArrayList<String>(knownContexts.size() + 1);
            int index = 1;
            String strIndex;
            for (String context : knownContexts) {
                strIndex = Integer.toString(index);
                contextQuery.append(strIndex + ": " + context + "\n");
                indexChoices.add(strIndex);
                index++;
            }

            strIndex = Integer.toString(index);
            contextQuery.append(strIndex + ": " + CUSTOM_CONTEXT + "\n");
            indexChoices.add(strIndex);

            contextQuery.append("Choose a number: ");
            String contextAnswer = prompt(contextQuery.toString(), indexChoices, "1");
            int selectedIndex = Integer.parseInt(contextAnswer) - 1;

            String selectedContext;
            if(selectedIndex < (indexChoices.size() - 1)) {
                selectedContext = knownContexts.get(selectedIndex);
                knownContexts.remove(selectedIndex);
            } else {
                selectedContext = promptNotBlank("Enter Context");
            }

            contexts.add(selectedContext);


            promptForContext(contexts, knownContexts);
        }
    }

    private List<WebResourceTransformation> promptForTransformations() throws PrompterException {
        List<WebResourceTransformation> transformations = new ArrayList<WebResourceTransformation>();
        promptForTransformation(transformations);

        return transformations;
    }

    private void promptForTransformation(List<WebResourceTransformation> transformations) throws PrompterException {
        if(promptForBoolean("Add Web Resource Transformation?","N")) {
            String extension = promptNotBlank("File Extension");
            WebResourceTransformation transformation = new WebResourceTransformation(extension);

            List<String> transformers = new ArrayList<String>();
            transformers.add(promptForTransformerKey());

            promptForTransformers(transformers);

            transformation.setTransformerKeys(transformers);

            transformations.add(transformation);

            promptForTransformation(transformations);
        }
    }

    private void promptForTransformers(List<String> transformers) throws PrompterException {
        if(promptForBoolean("Add Transformer Key?","N")) {
            transformers.add(promptForTransformerKey());
        }
    }

    private String promptForTransformerKey() throws PrompterException {
        return promptNotBlank("Transformer Key");
    }
}
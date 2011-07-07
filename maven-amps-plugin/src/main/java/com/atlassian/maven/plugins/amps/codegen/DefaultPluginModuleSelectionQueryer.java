package com.atlassian.maven.plugins.amps.codegen;

import com.atlassian.plugins.codegen.modules.PluginModuleCreator;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;
import org.codehaus.plexus.logging.AbstractLogEnabled;

import java.util.*;

/**
 * Author: jdoklovic
 */
public class DefaultPluginModuleSelectionQueryer extends AbstractLogEnabled implements PluginModuleSelectionQueryer {

    private Prompter prompter;

    @Override
    public PluginModuleCreator selectModule(Map<String, PluginModuleCreator> map) throws PrompterException {
        StringBuilder query = new StringBuilder( "Choose Plugin Module:\n" );

        List<String> answers = new ArrayList<String>();
        Map<String, PluginModuleCreator> moduleAnswerMap = new HashMap<String, PluginModuleCreator>();

        int counter = 1;

        for ( Map.Entry<String, PluginModuleCreator> entry : map.entrySet() )
        {
            PluginModuleCreator moduleCreator = entry.getValue();

                String answer = String.valueOf( counter );

                query.append( answer + ": " + entry.getKey() + "\n" );

                answers.add( answer );

                moduleAnswerMap.put( answer, moduleCreator );

                counter++;
        }

        query.append( "Choose a number: " );

        String answer = prompter.prompt( query.toString(), answers );

        PluginModuleCreator selection = moduleAnswerMap.get( answer );

        return selection;
    }

    public void setPrompter( Prompter prompter )
    {
        this.prompter = prompter;
    }
}

package com.atlassian.maven.plugins.amps.codegen;

import com.atlassian.plugins.codegen.modules.PluginModuleCreator;
import org.apache.commons.lang.StringUtils;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;
import org.codehaus.plexus.logging.AbstractLogEnabled;

import java.util.*;

/**
 *
 */
public class DefaultPluginModuleSelectionQueryer extends AbstractLogEnabled implements PluginModuleSelectionQueryer {
    public static final List<String> YN_ANSWERS = new ArrayList<String>(Arrays.asList("Y", "y", "N", "n"));
    private Prompter prompter;

    @Override
    public PluginModuleCreator selectModule(Map<Class, PluginModuleCreator> map) throws PrompterException {
        StringBuilder query = new StringBuilder( "Choose Plugin Module:\n" );

        List<String> answers = new ArrayList<String>();
        Map<String, PluginModuleCreator> moduleAnswerMap = new HashMap<String, PluginModuleCreator>();

        int counter = 1;

        for ( Map.Entry<Class, PluginModuleCreator> entry : map.entrySet() )
        {
            PluginModuleCreator moduleCreator = entry.getValue();

                String answer = String.valueOf( counter );
                if(counter < 10) {
                    query.append( answer + ":  ");
                } else {
                    query.append( answer + ": ");
                }
                 query.append(entry.getValue().getModuleName() + "\n" );

                answers.add( answer );

                moduleAnswerMap.put( answer, moduleCreator );

                counter++;
        }

        query.append( "Choose a number" );

        String answer = prompter.prompt( query.toString(), answers );

        PluginModuleCreator selection = moduleAnswerMap.get( answer );

        return selection;
    }

    @Override
    public boolean addAnotherModule() throws PrompterException {
        return promptForBoolean("Add Another Plugin Module?","N");
    }

    public void setPrompter( Prompter prompter )
    {
        this.prompter = prompter;
    }

    protected boolean promptForBoolean(String message, String defaultValue) throws PrompterException {
        String answer;
        boolean bool;
        if (StringUtils.isBlank(defaultValue)) {
            answer = prompter.prompt(message, YN_ANSWERS);
        } else {
            answer = prompter.prompt(message, YN_ANSWERS, defaultValue);
        }

        if ("y".equals(answer.toLowerCase())) {
            bool = true;
        } else {
            bool = false;
        }

        return bool;
    }
}

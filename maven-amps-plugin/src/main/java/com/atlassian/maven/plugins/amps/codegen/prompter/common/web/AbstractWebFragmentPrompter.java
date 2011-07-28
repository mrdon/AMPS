package com.atlassian.maven.plugins.amps.codegen.prompter.common.web;

import com.atlassian.maven.plugins.amps.codegen.ConditionFactory;
import com.atlassian.maven.plugins.amps.codegen.ContextProviderFactory;
import com.atlassian.maven.plugins.amps.codegen.prompter.AbstractModulePrompter;
import com.atlassian.maven.plugins.amps.codegen.prompter.common.AbstractResourcePrompter;
import com.atlassian.plugins.codegen.modules.NameBasedModuleProperties;
import com.atlassian.plugins.codegen.modules.PluginModuleProperties;
import com.atlassian.plugins.codegen.modules.common.Condition;
import com.atlassian.plugins.codegen.modules.common.Conditional;
import com.atlassian.plugins.codegen.modules.common.Conditions;
import com.atlassian.plugins.codegen.modules.common.Resource;
import org.apache.commons.lang.StringUtils;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @since version
 */
public abstract class AbstractWebFragmentPrompter<T extends NameBasedModuleProperties> extends AbstractResourcePrompter<T> {

    public static final String CUSTOM_CONDITION = "Custom Condition";
    public static final String CUSTOM_PROVIDER = "Custom Context Provider";

    public AbstractWebFragmentPrompter(Prompter prompter) {
        super(prompter);
    }

    protected String promptForContextProvider() throws PrompterException {
        String fqProvider = "";
        if (promptForBoolean("Add Velocity Context Provider", "N")) {
            Map<String, String> productProviders = ContextProviderFactory.getAvailableContextProviders();
            if (productProviders.isEmpty()) {
                fqProvider = promptFullyQualifiedJavaClass("Enter Fully Qualified Context Provider Class", "com.atlassian.plugins.web.contextproviders.MyContextProvider");
            } else {

                StringBuilder contextQuery = new StringBuilder("Choose A Context Provider\n");
                List<String> indexChoices = new ArrayList<String>(productProviders.size());
                Map<String,String> indexedValues = new HashMap<String, String>();
                int index = 1;
                String strIndex;
                for (Map.Entry<String, String> entry : productProviders.entrySet()) {
                    strIndex = Integer.toString(index);
                    contextQuery.append(strIndex + ": " + entry.getKey() + "\n");
                    indexChoices.add(strIndex);
                    indexedValues.put(strIndex,entry.getValue());
                    index++;
                }

                strIndex = Integer.toString(index);
                contextQuery.append(strIndex + ": " + CUSTOM_PROVIDER + "\n");
                indexChoices.add(strIndex);
                indexedValues.put(strIndex,CUSTOM_PROVIDER);

                contextQuery.append("Choose a number: ");
                String contextAnswer = prompt(contextQuery.toString(), indexChoices, "");
                int answerInt = (Integer.parseInt(contextAnswer) - 1);

                if (answerInt < (productProviders.size())) {
                    fqProvider = indexedValues.get(contextAnswer);
                } else {
                    fqProvider = promptFullyQualifiedJavaClass("Enter Fully Qualified Context Provider Class", "com.atlassian.plugins.web.contextproviders.MyContextProvider");
                }
            }
        }

        return fqProvider;
    }

    protected List<Conditional> promptForConditions() throws PrompterException {
        List<Conditional> conditionList = new ArrayList<Conditional>();

        if (promptForBoolean("Add Conditions?", "N")) {
            Conditions conditions = promptForConditionsContainer();
            conditionList.add(conditions);
            
            conditions.addCondition(promptForCondition());
            promptForCondition(conditions);
        }

        return conditionList;
    }

    protected Conditions promptForConditionsContainer() throws PrompterException {
        String conditionType = prompt("Condition Type", ANDOR_ANSWERS, "AND");
        return new Conditions(conditionType);
    }

    private void promptForCondition(Conditions conditions) throws PrompterException {
        if (promptForBoolean("Add Condition?","N")) {
            conditions.addCondition(promptForCondition());
            promptForCondition(conditions);
        }
    }

    protected Condition promptForCondition() throws PrompterException {
        Map<String, String> productConditions = ConditionFactory.getAvailableConditions();
        String fqCondition;
        if (productConditions.isEmpty()) {
            fqCondition = promptFullyQualifiedJavaClass("Enter Fully Qualified Condition Class", "com.atlassian.plugins.web.condition.MyCondition");
        } else {

            StringBuilder conditionQuery = new StringBuilder("Choose A Condition\n");
            List<String> indexChoices = new ArrayList<String>(productConditions.size());
            Map<String,String> indexedValues = new HashMap<String, String>();
            int index = 1;
            String strIndex;
            for (Map.Entry<String, String> entry : productConditions.entrySet()) {
                strIndex = Integer.toString(index);
                conditionQuery.append(strIndex + ": " + entry.getKey() + "\n");
                indexChoices.add(strIndex);
                indexedValues.put(strIndex,entry.getValue());
                index++;
            }

            strIndex = Integer.toString(index);
            conditionQuery.append(strIndex + ": " + CUSTOM_CONDITION + "\n");
            indexedValues.put(strIndex,CUSTOM_CONDITION);
            indexChoices.add(strIndex);

            conditionQuery.append("Choose a number: ");
            String conditionAnswer = prompt(conditionQuery.toString(), indexChoices, "");
            int answerInt = (Integer.parseInt(conditionAnswer) - 1);

            if (answerInt < (productConditions.size())) {
                fqCondition = indexedValues.get(conditionAnswer);
            } else {
                fqCondition = promptFullyQualifiedJavaClass("Enter Fully Qualified Condition Class", "com.atlassian.plugins.web.condition.MyCondition");
            }
        }

        Condition condition = new Condition(fqCondition);
        Map<String, String> params = promptForParams("Add Condition Parameter?");

        condition.setParams(params);

        condition.setInvert(promptForBoolean("Invert Condition?", "N"));

        return condition;
    }

}

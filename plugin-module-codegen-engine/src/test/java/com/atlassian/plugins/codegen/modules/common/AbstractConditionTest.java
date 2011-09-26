package com.atlassian.plugins.codegen.modules.common;

import java.util.List;

import com.atlassian.plugins.codegen.AbstractCodegenTestCase;
import com.atlassian.plugins.codegen.modules.common.web.AbstractConditionsProperties;

import org.dom4j.Document;
import org.dom4j.Node;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @since 3.5
 */
public abstract class AbstractConditionTest<T extends AbstractConditionsProperties> extends AbstractCodegenTestCase<T>
{
    public static final String XPATH_ALL_CONDITIONS = "//conditions";
    public static final String XPATH_TOP_CONDITIONS = "/atlassian-plugin/*[not(self::conditions)]/conditions";
    public static final String XPATH_CONDITIONS_RELATIVE = "conditions";
    public static final String XPATH_ALL_CONDITION = "//condition";
    public static final String XPATH_TOP_CONDITION = "/atlassian-plugin/*[not(self::conditions)]/condition";
    public static final String XPATH_CONDITION_RELATIVE = "condition";
    public static final String JIRA_GLOBAL_PERMISSION = "com.atlassian.jira.plugin.webfragment.conditions.JiraGlobalPermissionCondition";
    public static final String JIRA_HAS_ISSUE_PERMISSION = "com.atlassian.jira.plugin.webfragment.conditions.HasIssuePermissionCondition";
    public static final String XPATH_PARAM_RELATIVE = "param";

    @Test
    public void emptyConditionsAreNotIncluded() throws Exception
    {
        creator.createModule(moduleLocation, props);
        Document pluginDoc = getXmlDocument(pluginXml);
        List<Node> conditions = pluginDoc.selectNodes(XPATH_ALL_CONDITIONS);
        List<Node> condition = pluginDoc.selectNodes(XPATH_ALL_CONDITION);

        assertTrue("found conditions but should be empty", conditions.isEmpty());
        assertTrue("found single condition but should be empty", condition.isEmpty());
    }

    @Test
    public void singleConditionWithoutParamsAdded() throws Exception
    {
        props.getConditions()
                .add(new Condition(JIRA_GLOBAL_PERMISSION));
        creator.createModule(moduleLocation, props);

        Document pluginDoc = getXmlDocument(pluginXml);
        List<Node> conditions = pluginDoc.selectNodes(XPATH_ALL_CONDITIONS);

        assertTrue("conditions found but should be single", conditions.isEmpty());

        List<Node> condition = pluginDoc.selectNodes(XPATH_ALL_CONDITION);

        assertEquals("single condition not found", 1, condition.size());

        Node conditionNode = condition.get(0);
        assertEquals("class name mismatch", JIRA_GLOBAL_PERMISSION, conditionNode.selectSingleNode("@class")
                .getStringValue());
        assertNull("should not be inverted", conditionNode.selectSingleNode("@invert"));
    }

    @Test
    public void invertedConditionHasAttribute() throws Exception
    {
        Condition jiraCondition = new Condition(JIRA_GLOBAL_PERMISSION);
        jiraCondition.setInvert(true);

        props.getConditions()
                .add(jiraCondition);
        creator.createModule(moduleLocation, props);

        Document pluginDoc = getXmlDocument(pluginXml);

        List<Node> condition = pluginDoc.selectNodes(XPATH_ALL_CONDITION);

        assertEquals("single condition not found", 1, condition.size());

        Node conditionNode = condition.get(0);
        assertEquals("class name mismatch", JIRA_GLOBAL_PERMISSION, conditionNode.selectSingleNode("@class")
                .getStringValue());
        assertEquals("should be inverted", "true", conditionNode.selectSingleNode("@invert")
                .getStringValue());
    }

    @Test
    public void singleANDConditionsAdded() throws Exception
    {
        Conditions conditionsRoot = new Conditions(Conditions.AND);
        conditionsRoot.addCondition(new Condition(JIRA_GLOBAL_PERMISSION));

        props.getConditions()
                .add(conditionsRoot);

        creator.createModule(moduleLocation, props);

        Document pluginDoc = getXmlDocument(pluginXml);
        List<Node> conditions = pluginDoc.selectNodes(XPATH_ALL_CONDITIONS);

        assertEquals("conditions not found", 1, conditions.size());

        Node conditionsNode = conditions.get(0);

        assertEquals("wrong conditions type", Conditions.AND, conditionsNode.selectSingleNode("@type")
                .getStringValue());

        List<Node> condition = conditionsNode.selectNodes(XPATH_CONDITION_RELATIVE);

        assertEquals("single AND condition not found", 1, condition.size());

        Node conditionNode = condition.get(0);
        assertEquals("class name mismatch", JIRA_GLOBAL_PERMISSION, conditionNode.selectSingleNode("@class")
                .getStringValue());
        assertNull("should not be inverted", conditionNode.selectSingleNode("@invert"));
    }

    @Test
    public void singleORConditionsAdded() throws Exception
    {
        Conditions conditionsRoot = new Conditions(Conditions.OR);
        conditionsRoot.addCondition(new Condition(JIRA_GLOBAL_PERMISSION));

        props.getConditions()
                .add(conditionsRoot);

        creator.createModule(moduleLocation, props);

        Document pluginDoc = getXmlDocument(pluginXml);
        List<Node> conditions = pluginDoc.selectNodes(XPATH_ALL_CONDITIONS);

        assertEquals("conditions not found", 1, conditions.size());

        Node conditionsNode = conditions.get(0);

        assertEquals("wrong conditions type", Conditions.OR, conditionsNode.selectSingleNode("@type")
                .getStringValue());

        List<Node> condition = conditionsNode.selectNodes(XPATH_CONDITION_RELATIVE);

        assertEquals("single AND condition not found", 1, condition.size());

        Node conditionNode = condition.get(0);
        assertEquals("class name mismatch", JIRA_GLOBAL_PERMISSION, conditionNode.selectSingleNode("@class")
                .getStringValue());
        assertNull("should not be inverted", conditionNode.selectSingleNode("@invert"));
    }

    @Test
    public void nestedMixedConditionsAdded() throws Exception
    {
        Conditions conditionsRoot = new Conditions(Conditions.AND);
        conditionsRoot.addCondition(new Condition(JIRA_GLOBAL_PERMISSION));

        Conditions nestedConditions = new Conditions(Conditions.OR);
        nestedConditions.addCondition(new Condition(JIRA_HAS_ISSUE_PERMISSION));

        conditionsRoot.addCondition(nestedConditions);
        props.getConditions()
                .add(conditionsRoot);

        creator.createModule(moduleLocation, props);

        Document pluginDoc = getXmlDocument(pluginXml);
        List<Node> conditionsList = pluginDoc.selectNodes(XPATH_TOP_CONDITIONS);

        assertEquals("conditions not found", 1, conditionsList.size());

        Node conditionsNode = conditionsList.get(0);

        assertEquals("wrong conditions type", Conditions.AND, conditionsNode.selectSingleNode("@type")
                .getStringValue());

        List<Node> firstSingleList = conditionsNode.selectNodes(XPATH_CONDITION_RELATIVE);

        assertEquals("first single condition not found", 1, firstSingleList.size());

        Node firstSingleNode = firstSingleList.get(0);
        assertEquals("first single class name mismatch", JIRA_GLOBAL_PERMISSION, firstSingleNode.selectSingleNode("@class")
                .getStringValue());

        List<Node> nestedConditionsList = conditionsNode.selectNodes(XPATH_CONDITIONS_RELATIVE);

        assertEquals("nested conditions not found", 1, nestedConditionsList.size());

        Node nestedConditionsNode = nestedConditionsList.get(0);

        assertEquals("wrong nested conditions type", Conditions.OR, nestedConditionsNode.selectSingleNode("@type")
                .getStringValue());

        List<Node> nestedConditionList = nestedConditionsNode.selectNodes(XPATH_CONDITION_RELATIVE);

        assertEquals("nested condition not found", 1, nestedConditionList.size());

        Node nestedConditionNode = nestedConditionList.get(0);

        assertEquals("nested condition class name mismatch", JIRA_HAS_ISSUE_PERMISSION, nestedConditionNode.selectSingleNode("@class")
                .getStringValue());
        assertNull("should not be inverted", nestedConditionNode.selectSingleNode("@invert"));
    }

    @Test
    public void conditionIsSiblingOfConditions() throws Exception
    {
        Conditions conditionsRoot = new Conditions(Conditions.AND);
        conditionsRoot.addCondition(new Condition(JIRA_GLOBAL_PERMISSION));

        Condition singleCondition = new Condition(JIRA_HAS_ISSUE_PERMISSION);

        props.getConditions()
                .add(conditionsRoot);
        props.getConditions()
                .add(singleCondition);

        creator.createModule(moduleLocation, props);

        Document pluginDoc = getXmlDocument(pluginXml);
        List<Node> conditionsList = pluginDoc.selectNodes(XPATH_TOP_CONDITIONS);

        assertEquals("conditions not found", 1, conditionsList.size());

        Node conditionsNode = conditionsList.get(0);

        assertEquals("wrong conditions type", Conditions.AND, conditionsNode.selectSingleNode("@type")
                .getStringValue());

        List<Node> nestedSingleList = conditionsNode.selectNodes(XPATH_CONDITION_RELATIVE);

        assertEquals("nested single condition not found", 1, nestedSingleList.size());

        Node nestedSingleNode = nestedSingleList.get(0);
        assertEquals("nested single class name mismatch", JIRA_GLOBAL_PERMISSION, nestedSingleNode.selectSingleNode("@class")
                .getStringValue());

        List<Node> siblingConditionList = pluginDoc.selectNodes(XPATH_TOP_CONDITION);

        assertEquals("sibling condition not found", 1, siblingConditionList.size());

        Node siblingNode = siblingConditionList.get(0);

        assertEquals("sibling condition class name mismatch", JIRA_HAS_ISSUE_PERMISSION, siblingNode.selectSingleNode("@class")
                .getStringValue());
        assertNull("should not be inverted", siblingNode.selectSingleNode("@invert"));
    }

    @Test
    public void conditionParamsAreRendered() throws Exception
    {
        Condition condition = new Condition(JIRA_GLOBAL_PERMISSION);
        condition.getParams()
                .put("permission", "admin");
        condition.getParams()
                .put("username", "user");

        props.getConditions()
                .add(condition);

        creator.createModule(moduleLocation, props);

        Document pluginDoc = getXmlDocument(pluginXml);
        List<Node> conditionList = pluginDoc.selectNodes(XPATH_ALL_CONDITION);

        assertEquals("wrong number of conditions", 1, conditionList.size());

        Node conditionNode = conditionList.get(0);
        List<Node> paramsList = conditionNode.selectNodes(XPATH_PARAM_RELATIVE);

        assertEquals("wrong number of params", 2, paramsList.size());

        Node permissionParam = conditionNode.selectSingleNode("param[@name='permission' and @value='admin']");
        Node usernameParam = conditionNode.selectSingleNode("param[@name='username' and @value='user']");

        assertNotNull("permission param not found", permissionParam);
        assertNotNull("username param not found", usernameParam);
    }
}

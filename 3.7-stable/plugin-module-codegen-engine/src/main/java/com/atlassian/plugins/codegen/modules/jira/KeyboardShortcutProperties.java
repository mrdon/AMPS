package com.atlassian.plugins.codegen.modules.jira;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.atlassian.plugins.codegen.modules.BasicNameModuleProperties;

/**
 * @since 3.6
 */
public class KeyboardShortcutProperties extends BasicNameModuleProperties
{

    public static final String HIDDEN = "HIDDEN";
    public static final String ORDER = "ORDER";
    public static final String SHORTCUT = "SHORTCUT";
    public static final String OPERATION_TYPE = "OPERATION_TYPE";
    public static final String OPERATION_VALUE = "OPERATION_VALUE";
    public static final String CONTEXT = "CONTEXT";

    public static final List<String> ALLOWED_CONTEXTS = initContexts();
    public static final List<String> ALLOWED_OPERATIONS = initOperations();

    private static List<String> initContexts()
    {
        List<String> contexts = new ArrayList<String>(3);
        contexts.add("global");
        contexts.add("issueaction");
        contexts.add("issuenavigation");

        return Collections.unmodifiableList(contexts);
    }

    private static List<String> initOperations()
    {
        List<String> operations = new ArrayList<String>();
        operations.add("click");
        operations.add("evaluate");
        operations.add("execute");
        operations.add("followLink");
        operations.add("goTo");
        operations.add("moveToAndClick");
        operations.add("moveToAndFocus");
        operations.add("moveToNextItem");
        operations.add("moveToPrevItem");

        return Collections.unmodifiableList(operations);
    }

    public KeyboardShortcutProperties()
    {
        this("My Keyboard Shortcut");
    }

    public KeyboardShortcutProperties(String moduleName)
    {
        super(moduleName);
        setHidden(false);
        setOrder(10);
        setContext("global");
    }

    public void setHidden(boolean hidden)
    {
        setProperty(HIDDEN, Boolean.toString(hidden));
    }

    public boolean isHidden()
    {
        return Boolean.valueOf(getProperty(HIDDEN));
    }

    public void setOrder(int order)
    {
        setProperty(ORDER, Integer.toString(order));
    }

    public String getOrder()
    {
        return getProperty(ORDER);
    }

    public int getOrderAsInt()
    {
        return Integer.parseInt(getProperty(ORDER));
    }

    public void setShortcut(String s)
    {
        setProperty(SHORTCUT, s);
    }

    public String getShortcut()
    {
        return getProperty(SHORTCUT);
    }

    public void setContext(String s)
    {
        setProperty(CONTEXT, s);
    }

    public String getContext()
    {
        return getProperty(CONTEXT);
    }

    public void setOperationType(String s)
    {
        setProperty(OPERATION_TYPE, s);
    }

    public String getOperationType()
    {
        return getProperty(OPERATION_TYPE);
    }

    public void setOperationValue(String s)
    {
        setProperty(OPERATION_VALUE, s);
    }

    public String getOperationValue()
    {
        return getProperty(OPERATION_VALUE);
    }
}

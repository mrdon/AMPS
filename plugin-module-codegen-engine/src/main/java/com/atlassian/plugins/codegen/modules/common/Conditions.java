package com.atlassian.plugins.codegen.modules.common;

import java.util.ArrayList;
import java.util.List;

/**
 * @since version
 */
public class Conditions implements Conditional {
    public static final String AND = "AND";
    public static final String OR = "OR";

    private String type;
    private List<Conditional> conditions;

    public Conditions(String type) {
        this.type = type;
        this.conditions = new ArrayList<Conditional>();
    }

    public List<Conditional> getConditions() {
        return conditions;
    }

    public void addCondition(Conditional condition) {
        conditions.add(condition);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isEmpty() {
        return conditions.isEmpty();
    }
}

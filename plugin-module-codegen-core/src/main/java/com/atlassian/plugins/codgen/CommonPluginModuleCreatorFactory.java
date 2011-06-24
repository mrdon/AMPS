package com.atlassian.plugins.codgen;


import com.atlassian.plugins.codgen.modules.common.ServletFilterModuleCreator;

/**
 * Author: jdoklovic
 */
public class CommonPluginModuleCreatorFactory extends AbstractPluginModuleCreatorFactory {

    public CommonPluginModuleCreatorFactory() {
        super();

        registerModuleCreator(new ServletFilterModuleCreator());
    }

    @Override
    public String getId() {
        return COMMON;
    }

}

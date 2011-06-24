package com.atlassian.maven.plugins.amps.codegen.registry;

import com.atlassian.plugins.codgen.modules.common.ServletFilterModuleCreator;
import org.apache.maven.model.Dependency;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: jdoklovic
 */
public class PluginModuleDependencyRegistryImpl implements PluginModuleDependencyRegistry {

    private Map<Class,List<Dependency>> moduleDependencies;

    public PluginModuleDependencyRegistryImpl() {
        this.moduleDependencies = new HashMap<Class, List<Dependency>>();

        initServletFilter();
    }

    private void initServletFilter() {
        List<Dependency> deps = new ArrayList<Dependency>();

        Dependency servletApi = new Dependency();
        servletApi.setGroupId("javax.servlet");
        servletApi.setArtifactId("servlet-api");
        servletApi.setVersion("2.4");
        servletApi.setScope("provided");

        deps.add(servletApi);
        moduleDependencies.put(ServletFilterModuleCreator.class,deps);
    }


    @Override
    public List<Dependency> getDependenciesForCreatorClass(Class clazz) {
        return moduleDependencies.get(clazz);
    }
}

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
        initCommonDependencies(deps);

        moduleDependencies.put(ServletFilterModuleCreator.class,deps);
    }

    private void initCommonDependencies(List<Dependency> deps) {
        Dependency mockito = new Dependency();
        mockito.setGroupId("org.mockito");
        mockito.setArtifactId("mockito-all");
        mockito.setVersion("1.8.5");
        mockito.setScope("test");

        Dependency httpClient = new Dependency();
        httpClient.setGroupId("org.apache.httpcomponents");
        httpClient.setArtifactId("httpclient");
        httpClient.setVersion("4.1.1");
        httpClient.setScope("test");

        deps.add(mockito);
        deps.add(httpClient);

    }


    @Override
    public List<Dependency> getDependenciesForCreatorClass(Class clazz) {
        return moduleDependencies.get(clazz);
    }
}

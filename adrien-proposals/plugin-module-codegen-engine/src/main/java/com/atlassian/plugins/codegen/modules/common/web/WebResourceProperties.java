package com.atlassian.plugins.codegen.modules.common.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.atlassian.plugins.codegen.modules.common.Resource;

/**
 * @since 3.6
 */
public class WebResourceProperties extends AbstractConditionsProperties
{
    public static final String RESOURCES = "RESOURCES";
    public static final String DEPENDENCIES = "DEPENDENCIES";
    public static final String CONTEXTS = "CONTEXTS";
    public static final String TRANSFORMATIONS = "TRANSFORMATIONS";
    public static final List<String> KNOWN_CONTEXTS = initContexts();

    private static List<String> initContexts()
    {
        List<String> contexts = new ArrayList<String>(3);
        contexts.add("atl.general");
        contexts.add("atl.admin");
        contexts.add("atl.userprofile");

        return Collections.unmodifiableList(contexts);
    }

    public WebResourceProperties()
    {
        this("My Web Resource");
    }

    public WebResourceProperties(String moduleName)
    {
        super(moduleName);
        setDependencies(new ArrayList<String>());
        setContexts(new ArrayList<String>());
        setTransformations(new ArrayList<WebResourceTransformation>());
        setResources(new ArrayList<Resource>());
    }

    public void setResources(List<Resource> resources)
    {
        put(RESOURCES, resources);
    }

    public List<Resource> getResources()
    {
        return (List<Resource>) get(RESOURCES);
    }

    public void setDependencies(List<String> dependencies)
    {
        put(DEPENDENCIES, dependencies);
    }

    public List<String> getDependencies()
    {
        return (List<String>) get(DEPENDENCIES);
    }

    @SuppressWarnings(value = "unchecked")
    public void addDependency(String dependency)
    {
        List<String> dependencies = (List<String>) get(DEPENDENCIES);
        if (dependencies == null)
        {
            dependencies = new ArrayList<String>();
            setDependencies(dependencies);
        }

        dependencies.add(dependency);
    }

    public void setContexts(List<String> contexts)
    {
        put(CONTEXTS, contexts);
    }

    public List<String> getContexts()
    {
        return (List<String>) get(CONTEXTS);
    }

    @SuppressWarnings(value = "unchecked")
    public void addContext(String context)
    {
        List<String> contexts = (List<String>) get(CONTEXTS);
        if (contexts == null)
        {
            contexts = new ArrayList<String>();
            setContexts(contexts);
        }

        contexts.add(context);
    }

    public void setTransformations(List<WebResourceTransformation> transformations)
    {
        put(TRANSFORMATIONS, transformations);
    }

    public List<WebResourceTransformation> getTransformations()
    {
        return (List<WebResourceTransformation>) get(TRANSFORMATIONS);
    }

    @SuppressWarnings(value = "unchecked")
    public void addTransformation(WebResourceTransformation transformation)
    {
        List<WebResourceTransformation> transformations = (List<WebResourceTransformation>) get(TRANSFORMATIONS);
        if (transformations == null)
        {
            transformations = new ArrayList<WebResourceTransformation>();
            setTransformations(transformations);
        }

        transformations.add(transformation);
    }

    public List<String> knownContexts()
    {
        return KNOWN_CONTEXTS;
    }
}

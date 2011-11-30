package com.atlassian.plugins.codegen;

import java.util.List;
import java.util.Map;

import com.atlassian.plugins.codegen.annotations.DependencyDescriptor;
import com.atlassian.plugins.codegen.annotations.asm.ModuleCreatorAnnotationParser;
import com.atlassian.plugins.codegen.modules.PluginModuleCreator;
import com.atlassian.plugins.codegen.modules.PluginModuleCreatorFactory;
import com.atlassian.plugins.codegen.modules.PluginModuleCreatorRegistry;

import org.apache.commons.lang.StringUtils;

/**
 * @since 3.6
 */
public class PluginModuleCreatorFactoryImpl implements PluginModuleCreatorFactory
{

    private final PluginModuleCreatorRegistry creatorRegistry;
    private final ModuleCreatorAnnotationParser creatorAnnotationParser;

    public PluginModuleCreatorFactoryImpl() throws Exception
    {
        this("");
    }

    public PluginModuleCreatorFactoryImpl(String modulePackage) throws Exception
    {
        this.creatorRegistry = new PluginModuleCreatorRegistryImpl();
        this.creatorAnnotationParser = new ModuleCreatorAnnotationParser(creatorRegistry);
        doParse(modulePackage);
    }

    @Override
    public PluginModuleCreator getModuleCreator(String productId, Class creatorClass)
    {
        return creatorRegistry.getModuleCreator(productId, creatorClass);
    }

    @Override
    public Map<Class, PluginModuleCreator> getModuleCreatorsForProduct(String productId)
    {
        return creatorRegistry.getModuleCreatorsForProduct(productId);
    }

    @Override
    public List<DependencyDescriptor> getDependenciesForCreatorClass(Class creatorClass)
    {
        return creatorRegistry.getDependenciesForCreatorClass(creatorClass);
    }

    private void doParse(String packageName) throws Exception
    {
        if (StringUtils.isBlank(packageName))
        {
            creatorAnnotationParser.parse();
        } else
        {
            creatorAnnotationParser.parse(packageName);
        }
    }

}

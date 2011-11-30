package com.atlassian.maven.plugins.amps;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.atlassian.maven.plugins.amps.codegen.ConditionFactory;
import com.atlassian.maven.plugins.amps.codegen.ContextProviderFactory;
import com.atlassian.maven.plugins.amps.codegen.PluginModuleSelectionQueryer;
import com.atlassian.maven.plugins.amps.codegen.jira.ActionTypeFactory;
import com.atlassian.maven.plugins.amps.codegen.jira.CustomFieldSearcherFactory;
import com.atlassian.maven.plugins.amps.codegen.jira.CustomFieldTypeFactory;
import com.atlassian.maven.plugins.amps.codegen.prompter.PluginModulePrompter;
import com.atlassian.maven.plugins.amps.codegen.prompter.PluginModulePrompterFactory;
import com.atlassian.maven.plugins.amps.product.ProductHandlerFactory;
import com.atlassian.maven.plugins.amps.util.GoogleAmpsTracker;
import com.atlassian.plugins.codegen.annotations.DependencyDescriptor;
import com.atlassian.plugins.codegen.modules.PluginModuleCreator;
import com.atlassian.plugins.codegen.modules.PluginModuleCreatorFactory;
import com.atlassian.plugins.codegen.modules.PluginModuleLocation;
import com.atlassian.plugins.codegen.modules.PluginModuleProperties;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.XmlStreamWriter;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.shade.pom.PomWriter;
import org.apache.maven.project.MavenProject;
import org.jfrog.maven.annomojo.annotations.MojoComponent;
import org.jfrog.maven.annomojo.annotations.MojoGoal;
import org.jfrog.maven.annomojo.annotations.MojoRequiresDependencyResolution;

/**
 * @since 3.6
 */
@MojoRequiresDependencyResolution("compile")
@MojoGoal("create-plugin-module")
public class PluginModuleGenerationMojo extends AbstractProductAwareMojo
{

    @MojoComponent
    private PluginModuleSelectionQueryer pluginModuleSelectionQueryer;

    @MojoComponent
    private PluginModulePrompterFactory pluginModulePrompterFactory;

    @MojoComponent
    private PluginModuleCreatorFactory pluginModuleCreatorFactory;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {

        //can't figure out how to get plexus to fire a method after injection, so doing it here
        pluginModulePrompterFactory.setLog(getLog());
        try
        {
            pluginModulePrompterFactory.scanForPrompters();
        } catch (Exception e)
        {
            String message = "Error initializing Plugin Module Prompters";
            getLog().error(message);
            throw new MojoExecutionException(message);
        }

        String productId = getProductId();

        MavenProject project = getMavenContext().getProject();
        File javaDir = getJavaSourceRoot(project);
        File testDir = getJavaTestRoot(project);
        File resourcesDir = getResourcesRoot(project);

        initHelperFactories(productId, project);

        PluginModuleLocation moduleLocation = new PluginModuleLocation.Builder(javaDir)
                .resourcesDirectory(resourcesDir)
                .testDirectory(testDir)
                .templateDirectory(new File(resourcesDir, "templates"))
                .build();

        if (!moduleLocation.getPluginXml()
                .exists())
        {
            String message = "Couldn't find the atlassian-plugin.xml, please run this goal in an atlassian plugin project root.";
            getLog().error(message);
            throw new MojoExecutionException(message);
        }

        runGeneration(productId, project, moduleLocation);


    }

    private void runGeneration(String productId, MavenProject project, PluginModuleLocation moduleLocation) throws MojoExecutionException
    {
        PluginModuleCreator creator = null;
        try
        {
            creator = pluginModuleSelectionQueryer.selectModule(pluginModuleCreatorFactory.getModuleCreatorsForProduct(productId));

            String trackingLabel = getPluginInformation().getId() + ":" + creator.getModuleName();
            getGoogleTracker().track(GoogleAmpsTracker.CREATE_PLUGIN_MODULE,trackingLabel);

            PluginModulePrompter modulePrompter = pluginModulePrompterFactory.getPrompterForCreatorClass(creator.getClass());
            if (modulePrompter == null)
            {
                String message = "Couldn't find an input prompter for: " + creator.getClass()
                        .getName();
                getLog().error(message);
                throw new MojoExecutionException(message);
            }

            modulePrompter.setDefaultBasePackage(project.getGroupId());
            PluginModuleProperties moduleProps = modulePrompter.getModulePropertiesFromInput(moduleLocation);
            moduleProps.setProductId(getGadgetCompatibleProductId(productId));

            creator.createModule(moduleLocation, moduleProps);

            //edit pom if needed
            addRequiredModuleDependenciesToPOM(project, creator);

            if (pluginModuleSelectionQueryer.addAnotherModule())
            {
                runGeneration(productId, project, moduleLocation);
            }

        } catch (Exception e)
        {
            e.printStackTrace();
            throw new MojoExecutionException("Error creating plugin module", e);
        }

    }

    private void addRequiredModuleDependenciesToPOM(MavenProject project, PluginModuleCreator creator)
    {
        List<DependencyDescriptor> descriptors = pluginModuleCreatorFactory.getDependenciesForCreatorClass(creator.getClass());
        boolean modifyPom = false;
        if (descriptors != null && !descriptors.isEmpty())
        {
            List<Dependency> originalDependencies = project.getModel()
                    .getDependencies();
            for (DependencyDescriptor descriptor : descriptors)
            {
                Dependency alreadyExisting = (Dependency) CollectionUtils.find(originalDependencies, new DependencyPredicate(descriptor));
                if (null == alreadyExisting)
                {
                    modifyPom = true;

                    Dependency newDependency = new Dependency();
                    newDependency.setGroupId(descriptor.getGroupId());
                    newDependency.setArtifactId(descriptor.getArtifactId());
                    newDependency.setVersion(descriptor.getVersion());
                    newDependency.setScope(descriptor.getScope());

                    project.getOriginalModel()
                            .addDependency(newDependency);
                }
            }
        }

        if (modifyPom)
        {
            File pom = project.getFile();
            XmlStreamWriter writer = null;
            try
            {
                writer = new XmlStreamWriter(pom);
                PomWriter.write(writer, project.getOriginalModel(), true);
            } catch (IOException e)
            {
                getLog().warn("Unable to write plugin-module dependencies to pom.xml", e);
            } finally
            {
                if (writer != null)
                {
                    IOUtils.closeQuietly(writer);
                }
            }
        }
    }

    private String getGadgetCompatibleProductId(String pid)
    {
        String productId = pid;
        if (Jira.ID
                .equals(pid))
        {
            productId = "JIRA";
        } else if (Confluence.ID
                .equals(pid))
        {
            productId = "Confluence";
        } else if (Bamboo.ID
                .equals(pid))
        {
            productId = "Bamboo";
        } else if (Crowd.ID
                .equals(pid))
        {
            productId = "Crowd";
        } else if (Fecru.ID
                .equals(pid))
        {
            productId = "FishEye";
        } else
        {
            productId = "Other";
        }

        return productId;

    }

    private File getJavaSourceRoot(MavenProject project)
    {
        return new File(project.getModel()
                .getBuild()
                .getSourceDirectory());
    }

    private File getJavaTestRoot(MavenProject project)
    {
        return new File(project.getModel()
                .getBuild()
                .getTestSourceDirectory());
    }

    private File getResourcesRoot(MavenProject project)
    {
        File resourcesRoot = null;
        for (Resource resource : (List<Resource>) project.getModel()
                .getBuild()
                .getResources())
        {
            String pathToCheck = "src" + File.separator + "main" + File.separator + "resources";
            if (StringUtils.endsWith(resource.getDirectory(), pathToCheck))
            {
                resourcesRoot = new File(resource.getDirectory());
            }
        }
        return resourcesRoot;
    }

    private class DependencyPredicate implements Predicate
    {
        private DependencyDescriptor depToCheck;

        private DependencyPredicate(DependencyDescriptor depToCheck)
        {
            this.depToCheck = depToCheck;
        }

        @Override
        public boolean evaluate(Object o)
        {
            Dependency d = (Dependency) o;
            return (depToCheck.getGroupId()
                    .equals(d.getGroupId())
                    && depToCheck.getArtifactId()
                    .equals(d.getArtifactId()));
        }
    }

    private void initHelperFactories(String productId, MavenProject project) throws MojoExecutionException
    {
        List<String> pluginClasspath;
        try
        {
            pluginClasspath = project.getCompileClasspathElements();
        } catch (DependencyResolutionRequiredException e)
        {
            throw new MojoExecutionException("Dependencies MUST be resolved", e);
        }

        try
        {
            ConditionFactory.locateAvailableConditions(productId, pluginClasspath);
        } catch (Exception e)
        {
            String message = "Error initializing Plugin Module Conditions";
            getLog().error(message);
            //keep going, doesn't matter
        }

        try
        {
            ContextProviderFactory.locateAvailableContextProviders(productId, pluginClasspath);
        } catch (Exception e)
        {
            String message = "Error initializing Plugin Module Context Providers";
            getLog().error(message);
            //keep going, doesn't matter
        }

        if (Jira.ID
                .equals(productId))
        {
            try
            {
                ActionTypeFactory.locateAvailableActionTypes(pluginClasspath);
            } catch (Exception e)
            {
                String message = "Error initializing JIRA Action Types";
                getLog().error(message);
                //keep going, doesn't matter
            }

            try
            {
                CustomFieldTypeFactory.locateAvailableCustomFieldTypes(pluginClasspath);
            } catch (Exception e)
            {
                String message = "Error initializing JIRA Custom Field Types";
                getLog().error(message);
                //keep going, doesn't matter
            }

            try
            {
                CustomFieldSearcherFactory.locateAvailableCustomFieldSearchers(pluginClasspath);
            } catch (Exception e)
            {
                String message = "Error initializing JIRA Custom Field Searchers";
                getLog().error(message);
                //keep going, doesn't matter
            }
        }
    }
}

package com.atlassian.maven.plugins.amps;

import com.atlassian.maven.plugins.amps.codegen.PluginModuleSelectionQueryer;
import com.atlassian.maven.plugins.amps.codegen.prompter.PluginModulePrompter;
import com.atlassian.maven.plugins.amps.codegen.registry.PluginModuleDependencyRegistry;
import com.atlassian.maven.plugins.amps.codegen.registry.PluginModulePrompterRegistry;
import com.atlassian.maven.plugins.amps.codegen.registry.ProductModuleCreatorRegistry;
import com.atlassian.maven.plugins.amps.product.ProductHandlerFactory;
import com.atlassian.plugins.codgen.PluginModuleCreatorFactory;
import com.atlassian.plugins.codgen.PluginModuleLocation;
import com.atlassian.plugins.codgen.modules.BasicModuleProperties;
import com.atlassian.plugins.codgen.modules.PluginModuleCreator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.XmlStreamWriter;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.shade.pom.PomWriter;
import org.apache.maven.project.MavenProject;
import org.jfrog.maven.annomojo.annotations.MojoComponent;
import org.jfrog.maven.annomojo.annotations.MojoGoal;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

@MojoGoal("plugin-module")
public class PluginModuleGenerationMojo extends AbstractProductAwareMojo {

    @MojoComponent
    private PluginModuleSelectionQueryer pluginModuleSelectionQueryer;

    @MojoComponent
    private ProductModuleCreatorRegistry productModuleCreatorRegistry;

    @MojoComponent
    private PluginModulePrompterRegistry pluginModulePrompterRegistry;

    @MojoComponent
    private PluginModuleDependencyRegistry pluginModuleDependencyRegistry;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        //this is here because maven-plexus is lacking constructor injection
        pluginModulePrompterRegistry.initPrompters();

        String moduleGroup = getProductId();
        if (ProductHandlerFactory.REFAPP.equals(moduleGroup)) {
            moduleGroup = PluginModuleCreatorFactory.COMMON;
        }

        MavenProject project = getMavenContext().getProject();
        File javaDir = getJavaSourceRoot(project);
        File testDir = getJavaTestRoot(project);
        File resourcesDir = getResourcesRoot(project);

        PluginModuleLocation moduleLocation = new PluginModuleLocation.Builder(javaDir)
                .resourcesDirectory(resourcesDir)
                .testDirectory(testDir)
                .templateDirectory(new File(resourcesDir, "templates"))
                .build();

        if (!moduleLocation.getPluginXml().exists()) {
            String message = "Couldn't find the atlassian-plugin.xml, please run this goal in an atlassian plugin project root.";
            getLog().error(message);
            throw new MojoExecutionException(message);
        }

        PluginModuleCreatorFactory moduleCreatorFactory = productModuleCreatorRegistry.getProductModuleCreatorFactory(moduleGroup);
        if (moduleCreatorFactory == null) {
            String message = "Couldn't find a module creator factory for group: " + moduleGroup;
            getLog().error(message);
            throw new MojoExecutionException(message);
        }

        PluginModuleCreator creator = null;
        try {
            creator = pluginModuleSelectionQueryer.selectModule(moduleCreatorFactory.getAllModuleCreators());

            PluginModulePrompter modulePrompter = pluginModulePrompterRegistry.getPrompterForCreatorClass(creator.getClass());
            if (modulePrompter == null) {
                String message = "Couldn't find an input prompter for: " + creator.getClass().getName();
                getLog().error(message);
                throw new MojoExecutionException(message);
            }

            BasicModuleProperties moduleProps = modulePrompter.getModulePropertiesFromInput();
            creator.createModule(moduleLocation, moduleProps);

            //edit pom if needed
            addRequiredModuleDependenciesToPOM(project, creator);


        } catch (Exception e) {
            throw new MojoExecutionException("Error creating plugin module", e);
        }

    }

    private void addRequiredModuleDependenciesToPOM(MavenProject project, PluginModuleCreator creator) {
        List<Dependency> dependencyList = pluginModuleDependencyRegistry.getDependenciesForCreatorClass(creator.getClass());
        boolean modifyPom = false;
        if (dependencyList != null && !dependencyList.isEmpty()) {
            List<Dependency> originalDependencies = project.getModel().getDependencies();
            for (Dependency dependency : dependencyList) {
                Dependency alreadyExisting = (Dependency) CollectionUtils.find(originalDependencies, new DependencyPredicate(dependency));
                if (null == alreadyExisting) {
                    modifyPom = true;

                    project.getOriginalModel().addDependency(dependency);
                }
            }
        }

        if (modifyPom) {
            File pom = project.getFile();
            XmlStreamWriter writer = null;
            try {
                writer = new XmlStreamWriter(pom);
                PomWriter.write(writer, project.getOriginalModel(), true);
            } catch (IOException e) {
                getLog().warn("Unable to write plugin-module dependencies to pom.xml", e);
            } finally {
                if (writer != null) {
                    IOUtils.closeQuietly(writer);
                }
            }
        }
    }

    private File getJavaSourceRoot(MavenProject project) {
        return new File(project.getModel().getBuild().getSourceDirectory());
    }

    private File getJavaTestRoot(MavenProject project) {
        return new File(project.getModel().getBuild().getTestSourceDirectory());
    }

    private File getResourcesRoot(MavenProject project) {
        File resourcesRoot = null;
        for (Resource resource : (List<Resource>) project.getModel().getBuild().getResources()) {
            String pathToCheck = "src" + File.separator + "main" + File.separator + "resources";
            if (StringUtils.endsWith(resource.getDirectory(), pathToCheck)) {
                resourcesRoot = new File(resource.getDirectory());
            }
        }
        return resourcesRoot;
    }

    private class DependencyPredicate implements Predicate {
        private Dependency depToCheck;

        private DependencyPredicate(Dependency depToCheck) {
            this.depToCheck = depToCheck;
        }

        @Override
        public boolean evaluate(Object o) {
            Dependency d = (Dependency) o;
            return (depToCheck.getGroupId().equals(d.getGroupId())
                    && depToCheck.getArtifactId().equals(d.getArtifactId()));
        }
    }
}

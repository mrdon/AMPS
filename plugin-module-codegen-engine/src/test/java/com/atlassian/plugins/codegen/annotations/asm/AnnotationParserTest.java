package com.atlassian.plugins.codegen.annotations.asm;

import com.atlassian.plugins.codegen.modules.PluginModuleCreatorRegistry;
import com.atlassian.plugins.codegen.PluginModuleCreatorRegistryImpl;
import com.atlassian.plugins.codegen.annotations.DependencyDescriptor;
import com.atlassian.plugins.codegen.modules.PluginModuleCreator;
import fake.annotation.parser.modules.InheritedValidJira;
import fake.annotation.parser.modules.JiraAndConfluenceCreator;
import fake.annotation.parser.modules.JiraAnnotatedWithoutInterface;
import fake.annotation.parser.modules.ValidJiraModuleCreator;
import fake.annotation.parser.modules.dependencies.ValidJiraWithDependencies;
import fake.annotation.parser.modules.dependencies.ValidJiraWithMissingNestedDependency;
import fake.annotation.parser.modules.dependencies.ValidJiraWithMissingScopeDependency;
import fake.annotation.parser.modules.nested.NestedValidJira;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @since version
 */
public class AnnotationParserTest {

    private static final String MODULES_PACKAGE = "fake.annotation.parser.modules";

    private PluginModuleCreatorRegistry registry;
    private ModuleCreatorAnnotationParser parser;

    @Before
    public void setup() {
        registry = new PluginModuleCreatorRegistryImpl();
        parser = new ModuleCreatorAnnotationParser(registry);
    }

    @Test
    public void hasJiraModule() throws Exception {

        parser.parse(MODULES_PACKAGE);

        Map<String,PluginModuleCreator> modules = registry.getModuleCreatorsForProduct(PluginModuleCreatorRegistry.JIRA);
        assertNotNull("module map is null", modules);
        assertTrue("no testmodules registered", modules.size() > 0);
        assertTrue("jira module not found", modules.containsKey(ValidJiraModuleCreator.MODULE_NAME));
    }

    @Test
    public void annotatedWithoutInterfaceIsNotRegistered() throws Exception {
        parser.parse(MODULES_PACKAGE);

        Map<String,PluginModuleCreator> modules = registry.getModuleCreatorsForProduct(PluginModuleCreatorRegistry.JIRA);
        assertNotNull("module map is null", modules);
        assertTrue("no testmodules registered", modules.size() > 0);
        assertTrue("non-module found", !modules.containsKey(JiraAnnotatedWithoutInterface.MODULE_NAME));
    }

    @Test
    public void nestedCreatorsAreRegistered() throws Exception {
        parser.parse(MODULES_PACKAGE);

        Map<String,PluginModuleCreator> modules = registry.getModuleCreatorsForProduct(PluginModuleCreatorRegistry.JIRA);
        assertNotNull("module map is null", modules);
        assertTrue("no testmodules registered", modules.size() > 0);
        assertTrue("nested jira module not found", modules.containsKey(NestedValidJira.MODULE_NAME));
    }

    @Test
    public void inheritedCreatorsAreRegistered() throws Exception {
        parser.parse(MODULES_PACKAGE);

        Map<String,PluginModuleCreator> modules = registry.getModuleCreatorsForProduct(PluginModuleCreatorRegistry.JIRA);
        assertNotNull("module map is null", modules);
        assertTrue("no testmodules registered", modules.size() > 0);
        assertTrue("inherited jira module not found", modules.containsKey(InheritedValidJira.MODULE_NAME));
    }

    @Test
    public void muiltipleProductsHaveSameCreator() throws Exception {
        parser.parse(MODULES_PACKAGE);

        Map<String,PluginModuleCreator> jiraModules = registry.getModuleCreatorsForProduct(PluginModuleCreatorRegistry.JIRA);
        Map<String,PluginModuleCreator> confluenceModules = registry.getModuleCreatorsForProduct(PluginModuleCreatorRegistry.CONFLUENCE);

        assertTrue("jiraAndConfluence not found for jira",jiraModules.containsKey(JiraAndConfluenceCreator.MODULE_NAME));
        assertTrue("jiraAndConfluence not found for confluence",confluenceModules.containsKey(JiraAndConfluenceCreator.MODULE_NAME));
    }

    @Test
    public void noDependenciesReturnsEmptyList() throws Exception {
        parser.parse(MODULES_PACKAGE);
        List<DependencyDescriptor> dependencies = registry.getDependenciesForCreatorClass(ValidJiraModuleCreator.class);

        assertTrue("expected empty dependency list",dependencies.isEmpty());
    }

    @Test
    public void validDependenciesAreRegistered() throws Exception {
        parser.parse(MODULES_PACKAGE);

        DependencyDescriptor expectedServlet = new DependencyDescriptor();
        expectedServlet.setGroupId("javax.servlet");
        expectedServlet.setArtifactId("servlet-api");
        expectedServlet.setVersion("2.4");
        expectedServlet.setScope("provided");

        DependencyDescriptor expectedMockito = new DependencyDescriptor();
        expectedMockito.setGroupId("org.mockito");
        expectedMockito.setArtifactId("mockito-all");
        expectedMockito.setVersion("1.8.5");
        expectedMockito.setScope("test");

        List<DependencyDescriptor> dependencies = registry.getDependenciesForCreatorClass(ValidJiraWithDependencies.class);
        assertTrue("dependency list is empty",!dependencies.isEmpty());
        assertTrue("servlet-api dependency not found", dependencies.contains(expectedServlet));
        assertTrue("mockito dependency not found", dependencies.contains(expectedMockito));
    }

    @Test
    public void validDependencyWithMissingScopeIsRegistered() throws Exception {
        parser.parse(MODULES_PACKAGE);

        DependencyDescriptor expectedServlet = new DependencyDescriptor();
        expectedServlet.setGroupId("javax.servlet");
        expectedServlet.setArtifactId("servlet-api");
        expectedServlet.setVersion("2.4");

        List<DependencyDescriptor> dependencies = registry.getDependenciesForCreatorClass(ValidJiraWithMissingScopeDependency.class);
        assertTrue("dependency list is empty",!dependencies.isEmpty());
        assertTrue("servlet-api dependency not found", dependencies.contains(expectedServlet));
    }

    @Test
    public void depedenciesWithoutNestedAnnotationIsNotRegistered() throws Exception {
        parser.parse(MODULES_PACKAGE);
        List<DependencyDescriptor> dependencies = registry.getDependenciesForCreatorClass(ValidJiraWithMissingNestedDependency.class);
        assertTrue("dependency list is empty for missing nested annotation",dependencies.isEmpty());
    }
}

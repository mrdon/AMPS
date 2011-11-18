/**
 * Provides the annotation classes used to annotate ModuleCreator classes denoting which products they belong to.
 * A module creator can be annotated with multiple [product]PluginModuleCreator annotations to link it to multiple products.
 *
 * This package also contains the Dependencies and Dependency annotations as well as a DependencyDescriptor class.
 * These annotations can be used on a ModuleCreator to denote the Maven dependencies (if any) required by the module.
 * The dependencies will be automatically added to the project's pom when the module is generated.
 *
 * @since 3.6
 *
 */
package com.atlassian.plugins.codegen.annotations;
/**
 * This package (and it's sub-packages) contain the concrete implementations of the ModuleCreators and their respective
 * Properties classes.
 *
 * To generate a plugin module, a ModuleCreator is responsible for accepting a Properties object and using those values
 * to fill in the Velocity context and generate code based on Velocity templates.
 *
 * This package also contains factories that can be used to look up module creators based on name or product id.
 *
 * @since 3.6
 *
 */
package com.atlassian.plugins.codegen.modules;
/**
 * Provides classes that make up the maven client for plugin module generation.
 *
 * In general the classes are a set of Maven Prompters that prompt the user for input and apply the answers to the
 * appropriate Properties object that will ultimately be passed to the module creators.
 *
 * Module creators are provided by the plugin-module-codegen-engine
 *
 * Also included are classes that inspect the host product for classnames to be used as list values in some prompters.
 * e.g. lookup the classes that implement JiraCondition and provide them as a list to choose from when prompting for a condition.
 *
 * @since 3.6
 */
package com.atlassian.maven.plugins.amps.codegen;
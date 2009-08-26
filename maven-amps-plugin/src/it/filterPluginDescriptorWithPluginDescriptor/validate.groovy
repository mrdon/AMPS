assert mavenExitCode == 0, "The maven build should not have failed!"

def atlassianPlugin = new File(basedir, 'target/classes/atlassian-plugin.xml')
assert atlassianPlugin.exists()

assert atlassianPlugin.text == 'TestFilterPluginDescriptor'
assert ! new File(basedir, 'target/classes/another-file.xml').exists()

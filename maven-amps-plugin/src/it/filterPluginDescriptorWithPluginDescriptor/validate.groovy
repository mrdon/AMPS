def atlassianPlugin = new File(basedir, 'target/classes/atlassian-plugin.xml')
assert atlassianPlugin.exists()

assert atlassianPlugin.text == 'TestFilterPluginDescriptor'
assert !new File(basedir, 'target/classes/another-file.xml').exists()

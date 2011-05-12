assert mavenExitCode == 0, "The maven build should not have failed!"

def thisProduct = project.properties['shitty.product']

final File projectDir = new File("$basedir/amps-it-create")
assert projectDir.exists(), "The project should have been created under $projectDir"

final File projectPom = new File(projectDir, 'pom.xml')
assert projectPom.exists(), "The project's POM should have been created under $projectDir"

def pom = new groovy.xml.Namespace("http://maven.apache.org/POM/4.0.0", '')
def project = new XmlParser().parse(projectPom)

assert project[pom.groupId].text() == 'com.atlassian.amps.it.create', "Unexpected ${project[pom.groupId].text()}"
assert project[pom.artifactId].text() == 'amps-it-create'
assert project[pom.version].text() == '1.0'
assert project[pom.packaging].text() == 'atlassian-plugin'

final File projectPluginDescriptor = new File(projectDir, 'src/main/resources/atlassian-plugin.xml')
assert projectPluginDescriptor.exists(), "The project's plugin descriptor should have been created at $projectPluginDescriptor"

def pluginNs = new groovy.xml.Namespace("http://www.atlassian.com/schema/plugins", '')
def pluginXml = new XmlParser().parse(projectPluginDescriptor)
assert pluginXml.'@key' == '${project.groupId}.${project.artifactId}', "Unexpected ${pluginXml.'@key'}"
assert pluginXml.'@name' == '${project.name}'
if (thisProduct != 'bamboo') {
  assert pluginXml.'@plugins-version' == '2'
} else {
  assert pluginXml.'@plugins-version' == '1'
}
// only jira, confluence and refapp have namespaced archetypes:
// we have to validate them differently.
if (thisProduct == 'jira' || thisProduct == 'confluence' || thisProduct == 'refapp') {
    def descriptionText = pluginXml[pluginNs.'plugin-info'][pluginNs.description].text()
    assert descriptionText == '${project.description}', 'wrong <description> text: "' + descriptionText + '"'
    def versionText = pluginXml[pluginNs.'plugin-info'][pluginNs.version].text()
    assert versionText == '${project.version}', 'wrong version: "' + versionText + '"'
} else {
    assert pluginXml.'plugin-info'.description.text() == '${project.description}'
    assert pluginXml.'plugin-info'.version.text() == '${project.version}'
}

final File packageDir = new File("$projectDir/src/main/java/${'com.atlassian.it.package'.replace('.', '/')}")
assert packageDir.exists(), "Package should exist at $packageDir"
if (thisProduct != 'bamboo') {
    assert packageDir.list().length == 1, "Package should contain one example file"
} else {
    assert packageDir.list().length == 2, "Package should contain two example files for Task and Task Configurator"
}

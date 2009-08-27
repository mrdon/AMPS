assert mavenExitCode == 0, "The maven build should not have failed!"

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

def pluginXml = new XmlParser().parse(projectPluginDescriptor)
assert pluginXml.'@key' == '${project.groupId}.${project.artifactId}', "Unexpected ${pluginXml.'@key'}"
assert pluginXml.'@name' == '${project.artifactId}'
assert pluginXml.'@plugins-version' == '2'
assert pluginXml.'plugin-info'.description.text() == '${project.description}'
assert pluginXml.'plugin-info'.version.text() == '${project.version}'

final File packageDir = new File("$projectDir/src/main/java/${'com.atlassian.it.package'.replace('.', '/')}")
assert packageDir.exists(), "Package should exist at $packageDir"
assert packageDir.list().length == 1, "Package should contain one example file"
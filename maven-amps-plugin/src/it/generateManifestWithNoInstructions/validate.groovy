import java.util.jar.Manifest

assert mavenExitCode == 0, "The maven build should not have failed!"

final def manifestFile = new File("$basedir/target", 'META-INF/MANIFEST.MF')
assert manifestFile.exists(), "There should be manifest for no instructions and not an Atlassian plugin, see $manifestFile.absolutePath"

final Manifest manifest = manifestFile.withInputStream { InputStream is -> new Manifest(is) }

assert manifest.mainAttributes.getValue('Import-Package').contains('com.google.common.collect;resolution:=optional;version=\"0.0.0\"'), "Should contain generated instructions"


import java.util.jar.Manifest
import static aQute.lib.osgi.Constants.*

final def manifestFile = new File("$basedir/target/classes/META-INF/MANIFEST.MF")
assert manifestFile.exists(), "There should be a manifest for no instructions and is an Atlassian plugin, see $manifestFile.absolutePath"

final Manifest manifest = manifestFile.withInputStream { InputStream is -> new Manifest(is) }

assert manifest.mainAttributes.getValue(BUNDLE_CLASSPATH).contains('META-INF/lib/commons-logging-1.1.1.jar'), "Should contain the commons-logging compile dep"

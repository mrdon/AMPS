import java.util.jar.Manifest

final def testJar = new File("$basedir/target", 'testjar.jar')
assert testJar.exists()

// unzip the manifest
ant.unzip(src: testJar.path, dest: "$basedir/target", overwrite: true) { patternset { include(name: 'META-INF/MANIFEST.MF') } }

final def manifest = new File("$basedir/target", 'META-INF/MANIFEST.MF')
assert manifest.exists()

final Manifest extracted, existing;
manifest.withInputStream { extracted = new Manifest(it) }
new File("$basedir/target/classes/META-INF/MANIFEST.MF").withInputStream { existing = new Manifest(it) }

extracted.entries.each {key, value ->
    assert existing.entries.key == value
    existing.entries.remove(key)
}
assert existing.entries.isEmpty() // there shouldn't be any more entries
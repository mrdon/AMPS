assert mavenExitCode == 0, "The maven build should not have failed!"

final def manifest = new File("$basedir/target", 'META-INF/MANIFEST.MF')
assert !manifest.exists(), "There should be no manifest for no instructions and not an Atlassian plugin"
assert mavenExitCode == 0, "The maven build should not have failed!"

def metaInfLib = new File(basedir, 'target/classes/META-INF/lib')
assert metaInfLib.exists(), "The $metaInfLib directory should exist"

assert new File(metaInfLib, 'commons-io-1.4.jar').exists()
assert new File(metaInfLib, 'commons-logging-1.1.1.jar').exists()
assert !new File(metaInfLib, 'servlet-api-2.4.jar').exists()
assert !new File(metaInfLib, 'junit-4.5.jar').exists()

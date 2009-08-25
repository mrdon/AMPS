def obrFile = new File(basedir, 'target/maven-amps-plugin-genenerate-obr-artifact-test-testing-all.obr')
assert obrFile.exists(), "The $obrFile file should exist"

final String unzipLocation = "$basedir/target/obrunzip"
ant.unzip(src: obrFile, dest: unzipLocation)


assert new File(unzipLocation, 'maven-amps-plugin-genenerate-obr-artifact-test-testing.jar').exists()
assert new File(unzipLocation, 'obr.xml').exists()
assert new File(unzipLocation, 'dependencies/commons-io-1.4.jar').exists()
assert !new File(unzipLocation, 'dependencies/commons-logging-1.1.1.jar').exists()

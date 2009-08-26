assert mavenExitCode == 0, "The maven build should not have failed!"

def surefireReports = new File(basedir, 'target/shitty-it-surefire-reports')
assert surefireReports.exists()

assert new File(surefireReports, 'com.atlassian.amps.it.UnitTest.txt').exists()
assert !new File(surefireReports, 'it.com.atlassian.amps.IntegrationTest.txt').exists()

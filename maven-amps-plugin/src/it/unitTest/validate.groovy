def surefireReports = new File(basedir, 'target/surefire-reports')
assert surefireReports.exists()

assert new File(surefireReports, 'com.atlassian.amps.it.UnitTest.txt').exists()
assert !new File(surefireReports, 'it.com.atlassian.amps.IntegrationTest.txt').exists()

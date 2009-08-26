assert mavenExitCode == 0, "The maven build should not have failed!"

def product = project.properties['shitty.product']
product = 'amps'.equals(product) ? 'refapp' : product

def surefireReports = new File(basedir, 'target/shitty-it-surefire-reports')
assert !surefireReports.exists(), "No unit test should have been run, see $surefireReports.absolutePath"

def integrationSurefireReports = new File(basedir, "target/$product/tomcat6x/surefire-reports")
assert integrationSurefireReports.exists(), "Integration tests should have run and created test reports in $integrationSurefireReports"

assert new File(integrationSurefireReports, 'it.com.atlassian.amps.IntegrationTest.txt').exists(), "IntegrationTest.txt file did not exist"
assert !new File(integrationSurefireReports, 'com.atlassian.amps.unit.UnitTest.txt').exists(), "UnitTest.txt file should not exist"

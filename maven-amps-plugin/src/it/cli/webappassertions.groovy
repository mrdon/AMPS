import static groovyx.net.http.Method.GET
import groovyx.net.http.HTTPBuilder
import java.util.Properties

final File ampsFile = new File("${project.build.directory}/amps.properties")

assert ampsFile.exists(), "amps.properties doesn't exists at $ampsFile.absolutePath"

final Properties amps = new Properties();
ampsFile.withInputStream { amps.load(it) }

final String baseUrl = "http://localhost:${amps['http.port']}/${amps['context.path']}"
new HTTPBuilder(baseUrl).request(GET) {
  response.success = { assert it.statusLine.statusCode < 400 , "Expected status code below 400 on home page of application" }
  response.failure = { assert false, "The HTTP GET should have succeeded" }
}

final File cliState = new File("${project.build.directory}/cli.state")
if (!cliState.exists())
{
    cliState.createNewFile()
    cliState.text = '0'
}


if (cliState.text == '0')
{
    new HTTPBuilder("$baseUrl/plugins/servlet/cli").request(GET) {
      response.failure = { assert it.statusLine.statusCode == 404 , "Expected status code 404 on CLI Servlet, the plugin is not yet installed" }
      response.success = { assert false, "The HTTP GET should have 404'd, the plugin should not have been installed yet" }
    }
    cliState.text = '1'
}
else if (cliState.text == '1')
{
    new HTTPBuilder("$baseUrl/plugins/servlet/cli").request(GET) {
      response.failure = { assert false , "Expected request to have worked! Plugin should have been installed" }
      response.success = { assert it.statusLine.statusCode == 200, "The HTTP GET should have 200'd, the plugin should have been installed" }
    }
    cliState.text = '2'
}
else
{
    assert false, 'Not sure what happened here!'
}

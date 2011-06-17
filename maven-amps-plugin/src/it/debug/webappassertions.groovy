import static groovyx.net.http.Method.GET
import groovyx.net.http.HTTPBuilder
import java.util.Properties

final File ampsFile = new File("${project.build.directory}/amps.properties")

assert ampsFile.exists(), "amps.properties doesn't exists at $ampsFile.absolutePath"

final Properties amps = new Properties();
ampsFile.withInputStream { amps.load(it) }

// Check HTTP
new HTTPBuilder("http://localhost:${amps['http.port']}${amps['context.path']}").request(GET) {
    response.success = { assert it.statusLine.statusCode < 400, "Expected status code below 400 on home page of application" }
    response.failure = { assert false, "The HTTP GET should have succeeded" }
}

// Check Debug Port

Socket socket = null;
final def debugPort = Integer.valueOf(amps['debug.port'])
try
{
    socket = new Socket('localhost', debugPort);
}
catch (final IOException e)
{
    assert false, "We should have been able to open a socket to ${debugPort}!"
}
finally
{
    socket?.close();
}

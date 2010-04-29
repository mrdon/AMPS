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

ServerSocket socket = null;
try
{
    final def debugPort = Integer.valueOf(amps['debug.port'])
    socket = new ServerSocket(debugPort);
    assert socket.getLocalPort() == debugPort, "Was not the debug port specifed in the configuration"
}
catch (final IOException e)
{
    assert false, "We should have been able to open a socket!"
}
finally
{
    socket?.close();
}

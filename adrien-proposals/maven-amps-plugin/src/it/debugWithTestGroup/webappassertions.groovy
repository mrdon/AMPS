import static groovyx.net.http.Method.GET
import groovyx.net.http.HTTPBuilder
import java.util.Properties

final File ampsFile = new File("${project.build.directory}/amps.properties")

assert ampsFile.exists(), "amps.properties doesn't exists at $ampsFile.absolutePath"

amps = new Properties();
ampsFile.withInputStream { amps.load(it) }

checkServices('product-1');
checkServices('product-2');
checkServices('product-3');

def checkServices(instanceId)
{
    // Check HTTP
    new HTTPBuilder("http://localhost:${amps['http.' + instanceId + '.port']}${amps['context.' + instanceId + '.path']}").request(GET) {
        response.success = { assert it.statusLine.statusCode < 400, "Expected status code below 400 on home page of application" }
        response.failure = { assert false, "The HTTP GET should have succeeded" }
    }

    // Check Debug Port
    Socket socket = null;
    final def debugPort = Integer.valueOf(amps['debug.' + instanceId + '.port'])
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
}
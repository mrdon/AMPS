import static groovyx.net.http.Method.GET
import groovyx.net.http.HTTPBuilder
import java.util.Properties
import org.apache.http.conn.HttpHostConnectException

final File ampsFile = new File("${project.build.directory}/amps.properties")
assert ampsFile.exists(), "amps.properties doesn't exists at $ampsFile.absolutePath"
final Properties amps = new Properties();

ampsFile.withInputStream { amps.load(it) }

try {
    new HTTPBuilder("http://localhost:${amps['http.port']}${amps['context.path']}").request(GET) {
      response.success = { assert false, "The application should be down after amps:stop at http://localhost:${amps['http.port']}${amps['context.path']}" }
      // response.failure = { /* No need to implement, an exception will be thrown */ }
    }
    
    assert false, "An assertion should have already failed before reaching this point"
}
catch (HttpHostConnectException connectionException)
{
    // Do nothing, we expect this exception    
}
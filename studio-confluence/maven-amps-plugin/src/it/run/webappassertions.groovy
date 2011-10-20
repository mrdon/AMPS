import static groovyx.net.http.Method.GET
import groovyx.net.http.HTTPBuilder
import java.util.Properties
import org.apache.http.conn.HttpHostConnectException

final File ampsFile = new File("${project.build.directory}/amps.properties")

assert ampsFile.exists(), "amps.properties doesn't exists at $ampsFile.absolutePath"

final Properties amps = new Properties();
ampsFile.withInputStream { amps.load(it) }

if ("start".equals(System.getProperty("step", "start")))
{
    // The next step will be 'stop'
    System.setProperty("step", "stop");
    
    new HTTPBuilder("http://localhost:${amps['http.port']}${amps['context.path']}").request(GET) {
      response.success = { assert it.statusLine.statusCode < 400 , "Expected status code below 400 on home page of application" }
      response.failure = { assert false, "The HTTP GET should have succeeded" }
    }
    
}
else if ("stop".equals(System.getProperty("step")))
{
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
    
}
else
{
    assert false, "A wrong value was passed to it/run/webappassertions.groovy: -Dstep=" + System.getProperty("step");
}
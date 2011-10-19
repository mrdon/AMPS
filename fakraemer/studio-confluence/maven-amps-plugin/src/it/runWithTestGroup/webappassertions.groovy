import static groovyx.net.http.Method.GET
import groovyx.net.http.HTTPBuilder
import java.util.Properties

final File ampsFile = new File("${project.build.directory}/amps.properties")

assert ampsFile.exists(), "amps.properties doesn't exists at $ampsFile.absolutePath"


final Properties amps = new Properties();
ampsFile.withInputStream { amps.load(it) }

new HTTPBuilder("http://localhost:${amps['http.product-1.port']}${amps['context.product-1.path']}").request(GET) {
  response.success = { assert it.statusLine.statusCode < 400 , "Expected status code below 400 on home page of application" }
  response.failure = { assert false, "The HTTP GET should have succeeded" }
}

new HTTPBuilder("http://localhost:${amps['http.product-2.port']}${amps['context.product-2.path']}").request(GET) {
  response.success = { assert it.statusLine.statusCode < 400 , "Expected status code below 400 on home page of application" }
  response.failure = { assert false, "The HTTP GET should have succeeded" }
}
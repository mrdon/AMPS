package it.com.atlassian.amps;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class IntegrationTest
{
    @Test
    public void anIntegrationTest() throws Exception
    {
        final String httpPort = System.getProperty("http.port");
        final String contextPath = System.getProperty("context.path");

        final String url = new StringBuilder().append("http://localhost:").append(httpPort).append("/").append(contextPath).append("/plugins/servlet/it").toString();

        HttpClient client = new HttpClient();
        HttpMethod method = new GetMethod(url);
        client.executeMethod(method);

        assertEquals("Should have 200: " + url, 200, method.getStatusCode());
    }
}

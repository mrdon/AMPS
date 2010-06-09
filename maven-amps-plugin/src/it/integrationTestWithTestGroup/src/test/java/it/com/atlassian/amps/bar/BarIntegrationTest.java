package it.com.atlassian.amps.bar;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.io.IOException;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.System;

public class BarIntegrationTest
{
    @Test
    public void anIntegrationTest() throws Exception
    {
        tryServer("refapp");
        tryServer("refapp-0");
    }

    private void tryServer(String id)
            throws IOException
    {
        final String httpPort = System.getProperty("http." + id + ".port");
        final String contextPath = System.getProperty("context." + id + ".path");

        final String url = new StringBuilder().append("http://localhost:").append(httpPort).append(contextPath).append("/plugins/servlet/it").toString();

        HttpClient client = new HttpClient();
        HttpMethod method = new GetMethod(url);
        client.executeMethod(method);

        assertEquals("Should have 200: " + url, 200, method.getStatusCode());
    }
}

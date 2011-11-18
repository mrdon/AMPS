package com.atlassian.maven.plugins.caviar;

import com.atlassian.maven.plugins.amps.IntegrationTestMojo;
import com.atlassian.maven.plugins.amps.Product;
import com.atlassian.maven.plugins.amps.product.ProductHandlerFactory;

import org.apache.maven.plugin.MojoExecutionException;

import java.util.HashMap;
import java.util.Map;

public class CaviarIntegrationTestMojo extends IntegrationTestMojo
{
    @Override
    protected String getDefaultProductId() throws MojoExecutionException
    {
        return ProductHandlerFactory.CAVIAR;
    }

    @Override
    protected Map<String, String> getProductFunctionalTestProperties(Product product) {
        Map<String, String> props = new HashMap<String, String>();

        // set up properties for CAVIAR functional test library
        props.put("jira.protocol", "http");
        props.put("jira.host", "localhost");
        props.put("jira.port", Integer.toString(product.getHttpPort()));
        props.put("jira.xml.data.location",
                getMavenContext().getProject().getBasedir() + "/src/test/xml");
        props.put("jira.context", product.getContextPath());

        getLog().error("jira props: " + props);

        return props;
    }
}

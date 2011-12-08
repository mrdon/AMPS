package com.atlassian.maven.plugins.amps;

import org.apache.commons.collections.iterators.EmptyListIterator;
import org.apache.maven.model.Build;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.PluginManager;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.jfrog.maven.annomojo.annotations.MojoParameter;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.internal.verification.api.VerificationMode;

import com.google.common.collect.Lists;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestAbstractProductHandlerMojo
{
    @Test
    public void testMakeProductsInheritDefaultConfiguration() throws Exception
    {
        SomeMojo mojo = new SomeMojo("foo", Collections.<Product> emptyList());

        Product fooProd = new Product();
        fooProd.setInstanceId("foo");
        fooProd.setVersion("1.0");

        Product barProd = new Product();
        barProd.setInstanceId("bar");
        barProd.setVersion("2.0");

        Map<String, Product> prodMap = new HashMap<String, Product>();
        mojo.makeProductsInheritDefaultConfiguration(asList(fooProd, barProd), prodMap);
        assertEquals(2, prodMap.size());
        assertEquals("1.0", prodMap.get("foo").getVersion());
        assertEquals("/foo", prodMap.get("foo").getContextPath());
        assertEquals("2.0", prodMap.get("bar").getVersion());
        assertEquals("/foo", prodMap.get("bar").getContextPath());
    }

    @Test
    public void testMakeProductsInheritDefaultConfigurationDifferentInstanceIds() throws Exception
    {
        SomeMojo mojo = new SomeMojo("baz", Collections.<Product> emptyList());

        Product fooProd = new Product();
        fooProd.setInstanceId("foo");
        fooProd.setVersion("1.0");

        Product barProd = new Product();
        barProd.setInstanceId("bar");
        barProd.setVersion("2.0");

        Map<String, Product> prodMap = new HashMap<String, Product>();
        mojo.makeProductsInheritDefaultConfiguration(asList(fooProd, barProd), prodMap);
        assertEquals(3, prodMap.size());
        assertEquals("1.0", prodMap.get("foo").getVersion());
        assertEquals("/foo", prodMap.get("foo").getContextPath());
        assertEquals("2.0", prodMap.get("bar").getVersion());
        assertEquals("/foo", prodMap.get("bar").getContextPath());
        assertEquals(null, prodMap.get("baz").getVersion());
        assertEquals("/foo", prodMap.get("baz").getContextPath());
    }

    @Test
    public void testMakeProductsInheritDefaultConfigurationNoProducts() throws Exception
    {
        SomeMojo mojo = new SomeMojo("foo", Collections.<Product> emptyList());

        Map<String, Product> prodMap = new HashMap<String, Product>();
        mojo.makeProductsInheritDefaultConfiguration(Collections.<Product> emptyList(), prodMap);
        assertEquals(1, prodMap.size());
        assertEquals("/foo", prodMap.get("foo").getContextPath());
    }

    @Test
    public void testUnusedConfigurationIsNotified() throws MojoExecutionException
    {
        Xpp3Dom configuration = new Xpp3Dom("configuration");
        configuration.addChild(new Xpp3Dom("version"));

        SomeMojo mojo = new SomeMojo("refapp", Collections.<Product>emptyList(), configuration);
        mojo.checkCommonMistakes();
        
        Log log = mojo.getLog();
        Mockito.verify(log).warn(Matchers.contains("<version> is not available"));
    }

    @Test
    public void testSystemPropertyIsAssigned() throws MojoExecutionException
    {

        Properties properties = new Properties();
        properties.setProperty("amps.httpPort", "7");

        SomeMojo mojo = getSampleMojo(properties);

        Map<String, Product> map = mojo.getProductMap();
        assertEquals("There must be 3 products", 3, map.size());
        assertEquals(7, map.get("foo").getHttpPort());
        assertEquals(7, map.get("bar").getHttpPort());
        assertEquals(7, map.get("refapp").getHttpPort());
    }

    @Test
    public void testSystemPropertyIsAssignedToIntance() throws MojoExecutionException
    {
        Properties properties = new Properties();
        properties.setProperty("amps.bar.httpPort", "7");

        SomeMojo mojo = getSampleMojo(properties);

        Map<String, Product> map = mojo.getProductMap();
        assertEquals(2990, map.get("foo").getHttpPort());
        assertEquals(7, map.get("bar").getHttpPort());
        assertEquals(5990, map.get("refapp").getHttpPort());
    }

    @Test
    public void testSystemPropertyCanBeIntegerBooleanOrString() throws MojoExecutionException
    {

        Properties properties = new Properties();
        properties.setProperty("amps.stringValue", "my-string");
        properties.setProperty("amps.intValue", "1");
        properties.setProperty("amps.boolValue", "true");

        SomeMojo mojo = getSampleMojo(properties);
        assertEquals("Parameters shoud be assigned with the system property", "my-string", mojo.getStringValue());
        assertEquals("Parameters shoud be assigned with the system property", 1, mojo.getIntValue());
        assertEquals("Parameters shoud be assigned with the system property", true, mojo.isBoolValue());
    }

    @Test
    public void testSystemPropertyCanBeBean() throws MojoExecutionException
    {
        Properties properties = new Properties();
        properties.setProperty("amps.beanValue", "param1:param2");

        SomeMojo mojo = getSampleMojo(properties);
        assertEquals("Parameters shoud be assigned with the system property", "Bean [property1=param1, property2=param2]", mojo.getBeanValue().toString());
    }

    @Test
    public void testSystemPropertyCanAssignReadonlyAnnotation() throws MojoExecutionException
    {
        Properties properties = new Properties();
        properties.setProperty("amps.valueWithReadOnlyAnnotation", "value");

        try
        {
            SomeMojo mojo = getSampleMojo(properties);
        }
        catch (MojoExecutionException mee)
        {
            assertTrue("Exception should be explicit", mee.getMessage().contains("can't be assigned"));
            return;
        }
        assertTrue("Values without annotation shouldn't be assigned", false);
    }

    private SomeMojo getSampleMojo(Properties systemProperties) throws MojoExecutionException
    {
        Product fooProd = new Product();
        fooProd.setId("jira");
        fooProd.setInstanceId("foo");
        fooProd.setVersion("1.0");

        Product barProd = new Product();
        barProd.setId("jira");
        barProd.setInstanceId("bar");
        barProd.setVersion("2.0");

        SomeMojo mojo = new SomeMojo("refapp", Lists.newArrayList(fooProd, barProd));

        AbstractProductHandlerMojo.applySystemProperties(mojo, systemProperties);
        mojo.createProductContexts();

        return mojo;
    }

    public static class SomeMojo extends AbstractProductHandlerMojo
    {
        private final String defaultProductId;

        @MojoParameter
        private String stringValue;

        @MojoParameter
        private int intValue;

        @MojoParameter
        private boolean boolValue;

        @MojoParameter
        private Bean beanValue;

        private String valueWithNoAnnotation;

        @MojoParameter(readonly = true)
        private String valueWithReadOnlyAnnotation;

        private Log logger = Mockito.mock(Log.class);

        private Xpp3Dom configuration;

        public SomeMojo(String defaultProductId, List<Product> products, Xpp3Dom configuration)
        {
            this.defaultProductId = defaultProductId;
            contextPath = "/foo";
            this.products = products;
            this.configuration = configuration;
        }

        public SomeMojo(String defaultProductId, List<Product> products)
        {
            this.defaultProductId = defaultProductId;
            contextPath = "/foo";
            this.products = products;
            this.configuration = null;
        }

        @Override
        protected String getDefaultProductId() throws MojoExecutionException
        {
            return defaultProductId;
        }

        @Override
        protected void doExecute() throws MojoExecutionException, MojoFailureException
        {

        }

        @Override
        protected MavenContext getMavenContext()
        {
            MavenProject project = mock(MavenProject.class);
            Build build = mock(Build.class);
            when(build.getTestOutputDirectory()).thenReturn(".");
            when(project.getBuild()).thenReturn(build);
            when(project.getBasedir()).thenReturn(new File("."));
            when(project.getProperties()).thenReturn(new Properties());
            return new MavenContext(project, null, null, (PluginManager) null, null);
        }

        public Map<String, Product> getProductMap()
        {
            return productMap;
        }

        public String getStringValue()
        {
            return stringValue;
        }

        public int getIntValue()
        {
            return intValue;
        }

        public boolean isBoolValue()
        {
            return boolValue;
        }

        public Bean getBeanValue()
        {
            return beanValue;
        }

        public Log getLog()
        {
            return logger;
        }

        public Xpp3Dom getCurrentConfiguration()
        {
            return configuration;
        }
    }

    public static class Bean
    {
        String property1;
        String property2;

        public Bean(String description)
        {
            super();
            String[] values = description.split(":");
            this.property1 = values[0];
            this.property2 = values[1];
        }

        @Override
        public String toString()
        {
            return "Bean [property1=" + property1 + ", property2=" + property2 + "]";
        }
    }
}

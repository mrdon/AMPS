package com.atlassian.maven.plugins.amps.util;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import com.atlassian.maven.plugins.amps.MavenContext;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import static com.atlassian.maven.plugins.amps.util.FileUtils.file;

/**
 * @since 3.6.1
 */
public class PluginXmlUtils
{
    private static final String XPATH_PLUGIN = "/atlassian-plugin";
    private static final String XPATH_PLUGIN_INFO = XPATH_PLUGIN + "/plugin-info";
    private static final String XPATH_REST = XPATH_PLUGIN + "/rest";
    private static final String XPATH_PACKAGE_RELATIVE = "package";

    public static PluginInfo getPluginInfo(MavenContext context)
    {
        File pluginXml = getPluginXml(context);
        PluginInfo pluginInfo = null;

        if (pluginXml.exists())
        {
            try
            {
                Document pluginDoc = getXmlDocument(pluginXml);
                Node root = pluginDoc.selectSingleNode(XPATH_PLUGIN);
                String name = "";
                String desc = "";

                if(null != root) {
                    name = root.valueOf("@name");
                    Node infoNode = pluginDoc.selectSingleNode(XPATH_PLUGIN_INFO);

                    if(null != infoNode) {
                        Node descNode = infoNode.selectSingleNode("description");
                        if (null != descNode)
                        {
                            desc = descNode.valueOf("text()");
                        }
                    }
                }

                pluginInfo = new PluginInfo(name,desc);


            } catch (Exception e)
            {
                //print the error but continue since this is not a critical feature
                e.printStackTrace();
            }
        }

        if(null == pluginInfo) {
            pluginInfo = new PluginInfo("unknown plugin","");
        }

        return pluginInfo;
    }

    public static List<RESTModuleInfo> getRestModules(MavenContext context)
    {
        File pluginXml = getPluginXml(context);
        List<RESTModuleInfo> modules = new ArrayList<RESTModuleInfo>();

        if (pluginXml.exists())
        {
            try
            {
                Document pluginDoc = getXmlDocument(pluginXml);
                List<Node> restNodes = pluginDoc.selectNodes(XPATH_REST);

                for (Node restNode : restNodes)
                {
                    String name = restNode.valueOf("@name");
                    String desc = restNode.valueOf("@description");
                    List<String> packagesToScan = new ArrayList<String>();

                    if (StringUtils.isBlank(desc))
                    {
                        Node descNode = restNode.selectSingleNode("description");
                        if (null != descNode)
                        {
                            desc = descNode.valueOf("text()");
                        }
                    }

                    List<Node> packageNodes = restNode.selectNodes(XPATH_PACKAGE_RELATIVE);
                    for (Node packageNode : packageNodes)
                    {
                        packagesToScan.add(packageNode.valueOf("text()"));
                    }

                    modules.add(new RESTModuleInfo(name, desc, packagesToScan));
                }

            } catch (Exception e)
            {
                //print the error but continue since this is not a critical feature
                e.printStackTrace();
            }
        }

        return modules;
    }

    public static boolean hasPluginXml(MavenContext context)
    {
        return getPluginXml(context).exists();
    }

    public static File getPluginXml(MavenContext context)
    {
        return file(context.getProject()
                .getBuild()
                .getOutputDirectory(), "atlassian-plugin.xml");
    }

    protected static Document getXmlDocument(File xmlFile) throws MalformedURLException, DocumentException
    {
        SAXReader reader = new SAXReader();
        return reader.read(xmlFile);
    }

    public static class RESTModuleInfo
    {
        private String name;
        private String description;
        private List<String> packagesToScan;

        public RESTModuleInfo(String name, String description, List<String> packagesToScan)
        {
            this.name = name;
            this.description = description;
            this.packagesToScan = packagesToScan;
        }

        public String getName()
        {
            return name;
        }

        public List<String> getPackagesToScan()
        {
            return packagesToScan;
        }

        public String getDescription()
        {
            return description;
        }

    }

    public static class PluginInfo
    {
        private String name;
        private String description;

        public PluginInfo(String name, String description)
        {
            this.name = name;
            this.description = description;
        }

        public String getName()
        {
            return name;
        }

        public String getDescription()
        {
            return description;
        }

    }
}

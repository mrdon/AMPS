package com.atlassian.plugins.codegen.util;

import java.io.*;

import org.apache.commons.lang.Validate;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 *
 */
public class PluginXmlHelper
{

    private final File pluginXml;
    private final Document document;

    public PluginXmlHelper(File pluginXml) throws Exception
    {
        Validate.notNull(pluginXml);
        Validate.isTrue(pluginXml.exists());

        this.pluginXml = pluginXml;

        InputStream is = new FileInputStream(pluginXml);
        this.document = createDocument(is);
    }

    public void addModuleAsLastChild(String fragment) throws Exception
    {
        try
        {
            Document fragDoc = DocumentHelper.parseText(fragment);
            Element pluginRoot = document.getRootElement();
            pluginRoot.add(fragDoc.getRootElement());
        } catch (DocumentException e)
        {
            throw new Exception("Could not parse module XML fragment", e);
        }
    }

    public void addI18nResource(String name) throws Exception
    {
        String xpath = "//resource[@type='i18n' and @location='" + name + "']";
        Node resourceNode = document.selectSingleNode(xpath);

        if (null == resourceNode)
        {
            Element pluginRoot = document.getRootElement();
            Document fragDoc = DocumentHelper.parseText("<resource type=\"i18n\" name=\"i18n\" location=\"" + name + "\" />");
            pluginRoot.add(fragDoc.getRootElement());
        }
    }

    public void addPluginInfoParam(String name, String value) throws Exception
    {
        Element pluginInfo = (Element) document.selectSingleNode("//plugin-info");
        if (pluginInfo == null)
        {
            pluginInfo = document.addElement("plugin-info");
        }
        pluginInfo.addElement("param").addAttribute("name", name).setText(value);
    }
    
    public String getPluginXmlAsString()
    {
        return document.asXML();
    }

    protected Document getDocument()
    {
        return document;
    }

    protected Document createDocument(final InputStream source) throws Exception
    {
        final SAXReader reader = new SAXReader();
        reader.setMergeAdjacentText(true);
        reader.setStripWhitespaceText(false);
        try
        {
            return reader.read(source);
        } catch (final DocumentException e)
        {
            throw new Exception("Cannot parse XML plugin descriptor", e);
        }
    }

    public void savePluginXml() throws IOException
    {
        OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter writer = new XMLWriter(new FileWriter(pluginXml), format);

        writer.write(document);
        writer.close();
    }
}

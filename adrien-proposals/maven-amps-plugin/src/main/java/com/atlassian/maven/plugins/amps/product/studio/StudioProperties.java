package com.atlassian.maven.plugins.amps.product.studio;

import static com.atlassian.maven.plugins.amps.util.ProjectUtils.firstNotNull;

import java.io.File;
import java.util.Map;

import org.apache.maven.surefire.shade.org.apache.commons.lang.StringUtils;

import com.atlassian.maven.plugins.amps.Product;
import com.atlassian.maven.plugins.amps.product.FeCruProductHandler;
import com.atlassian.maven.plugins.amps.product.ProductHandlerFactory;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

/**
 * This bean shares the configuration of Studio across all products
 * @since 3.6
 */
public class StudioProperties
{

    /**
     * The Studio version, which will be used to retrieve each product artifact.
     */
    protected String version;

    /** The runtime place where studio-home will be. */
    private String studioHome;
    /** This property is required by the studio.properties files, but is different from studioHome. */
    private String studioDataLocation;
    private Product studioProduct;
    private boolean modeConfluenceStandalone = false;

    private String svnRoot;
    private String svnPublicUrl;
    private String svnImportStagingDirectory;

    private String webDavHome;

    private Product jira;
    private String greenHopperLicense;

    private Product confluence;

    private Product fisheye;

    private Product bamboo;
    private Product crowd;

    private boolean gappsEnabled;
    private String gappsDomain;

    private Map<String, String> systemProperties = Maps.newHashMap();

    /**
     * Constructs the StudioProperties using the values from the studio product
     *
     * @param studioContext
     *            the Studio product
     */
    public StudioProperties(Product studioContext)
    {
        if (!ProductHandlerFactory.STUDIO.equals(studioContext.getId()))
        {
            throw new IllegalArgumentException("The Studio Properties should be based on the Studio product");
        }
        version = firstNotNull(studioContext.getVersion(), "RELEASE");
        studioProduct = studioContext;

        gappsEnabled = Boolean.getBoolean(studioContext.getGappsEnabled());
        gappsDomain = firstNotNull(studioContext.getGappsDomain(), "");
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public void setStudioHome(String studioHome)
    {
        this.studioHome = studioHome;
    }

    public void setStudioDataLocation(String studioDataLocation)
    {
        this.studioDataLocation = studioDataLocation;
    }

    public void setModeConfluenceStandalone(boolean modeConfluenceStandalone)
    {
        this.modeConfluenceStandalone = modeConfluenceStandalone;
    }

    public void setSvnRoot(String svnRoot)
    {
        this.svnRoot = svnRoot;
    }

    public void setSvnPublicUrl(String svnPublicUrl)
    {
        this.svnPublicUrl = svnPublicUrl;
    }

    public void setSvnImportStagingDirectory(String svnImportStagingDirectory)
    {
        this.svnImportStagingDirectory = svnImportStagingDirectory;
    }

    public void setWebDavHome(String webDavHome)
    {
        this.webDavHome = webDavHome;
    }

    public void setJira(Product jira)
    {
        this.jira = jira;
    }

    public void setGreenHopperLicense(String greenHopperLicense)
    {
        this.greenHopperLicense = greenHopperLicense;
    }

    public void setConfluence(Product confluence)
    {
        this.confluence = confluence;
    }

    public void setFisheye(Product fisheye)
    {
        this.fisheye = fisheye;
    }

    public void setBamboo(Product bamboo)
    {
        this.bamboo = bamboo;
    }

    public void setCrowd(Product crowd)
    {
        this.crowd = crowd;
    }

    public void setGappsEnabled(boolean gappsEnabled)
    {
        this.gappsEnabled = gappsEnabled;
    }

    public void setGappsDomain(String gappsDomain)
    {
        this.gappsDomain = gappsDomain;
    }

    public String getVersion()
    {
        return version;
    }

    public String getStudioHome()
    {
        return studioHome;
    }

    public String getStudioDataLocation()
    {
        return studioDataLocation;
    }

    public Product getStudioProduct()
    {
        return studioProduct;
    }

    public void setStudioProduct(Product studioProduct)
    {
        this.studioProduct = studioProduct;
    }

    public Map<String, String> getSystemProperties()
    {
        return ImmutableMap.copyOf(systemProperties);
    }

    public void overrideSystemProperty(String key, String value)
    {
        this.systemProperties.put(key, value);
    }

    public boolean isModeConfluenceStandalone()
    {
        return modeConfluenceStandalone;
    }

    public String getSvnRoot()
    {
        return svnRoot;
    }

    public String getSvnPublicUrl()
    {
        return svnPublicUrl;
    }

    public String getSvnHooks()
    {
        if (svnRoot != null)
        {
            return new File(svnRoot, "hooks").getAbsolutePath();
        }
        return null;
    }

    public String getSvnImportStagingDirectory()
    {
        return svnImportStagingDirectory;
    }

    public String getWebDavHome()
    {
        return webDavHome;
    }

    public String getGreenHopperLicense()
    {
        return greenHopperLicense;
    }

    public Product getConfluence()
    {
        return confluence;
    }
    public Product getJira()
    {
        return jira;
    }

    public Product getFisheye()
    {
        return fisheye;
    }

    public boolean getFisheyeShutdownEnabled()
    {
        return fisheye.getShutdownEnabled();
    }

    public String getFisheyeControlPort()
    {
        return String.valueOf(FeCruProductHandler.controlPort(fisheye.getHttpPort()));
    }

    public Product getBamboo()
    {
        return bamboo;
    }

    public Product getCrowd()
    {
        return crowd;
    }

    public boolean isGappsEnabled()
    {
        return gappsEnabled;
    }

    public String getGappsDomain()
    {
        return firstNotNull(gappsDomain, "");
    }




    // Custom getters

    // JIRA getters
    public int getJiraPort()
    {
        return jira != null ? jira.getHttpPort() : 0;
    }

    public String getJiraContextPath()
    {
        if (jira == null || StringUtils.isBlank(jira.getContextPath()))
        {
            return "";
        }
        return jira.getContextPath().replaceAll("/", "");
    }

    public String getJiraUrl()
    {
        if (jira == null)
        {
            return "";
        }
        return String.format("http://localhost:%d%s", jira.getHttpPort(), jira.getContextPath());
    }

    public String getJiraHostUrl()
    {
        if (jira == null)
        {
            return "";
        }
        return String.format("http://localhost:%d", jira.getHttpPort());
    }

    public boolean isJiraEnabled()
    {
        return !modeConfluenceStandalone;
    }

    // Confluence getters
    public int getConfluencePort()
    {
        return confluence != null ? confluence.getHttpPort() : 0;
    }

    public String getConfluenceContextPath()
    {
        if (confluence == null || StringUtils.isBlank(confluence.getContextPath()))
        {
            return "";
        }
        return confluence.getContextPath().replaceAll("/", "");
    }

    public String getConfluenceUrl()
    {
        if (confluence == null)
        {
            return "";
        }
        return String.format("http://localhost:%d%s", confluence.getHttpPort(), confluence.getContextPath());
    }

    public String getConfluenceHostUrl()
    {
        if (confluence == null)
        {
            return "";
        }
        return String.format("http://localhost:%d", confluence.getHttpPort());
    }

    public boolean isConfluenceEnabled()
    {
        return confluence != null;
    }

    // FishEye getters
    public int getFisheyePort()
    {
        return fisheye != null ? fisheye.getHttpPort() : 0;
    }
    public String getFisheyeContextPath()
    {
        if (fisheye == null || StringUtils.isBlank(fisheye.getContextPath()))
        {
            return "";
        }
        return fisheye.getContextPath().replaceAll("/", "");
    }

    public String getFisheyeUrl()
    {
        if (fisheye == null)
        {
            return "";
        }
        if (StringUtils.isNotBlank(fisheye.getContextPath()) && !"/".equals(fisheye.getContextPath()))
        {
            return String.format("http://localhost:%d%s", fisheye.getHttpPort(), fisheye.getContextPath());
        }
        else
        {
            return String.format("http://localhost:%d", fisheye.getHttpPort());
        }
    }

    public String getFisheyeHostUrl()
    {
        return getFisheyeUrl();
    }

    public boolean isFisheyeEnabled()
    {
        return fisheye != null;
    }

    // Bamboo getters
    public String getBambooContextPath()
    {
        if (bamboo == null || StringUtils.isBlank(bamboo.getContextPath()))
        {
            return "";
        }
        return bamboo.getContextPath().replaceAll("/", "");
    }

    public int getBambooPort()
    {
        return bamboo != null ? bamboo.getHttpPort() : 0;
    }

    public String getBambooUrl()
    {
        if (bamboo == null)
        {
            return "";
        }
        return String.format("http://localhost:%d%s", bamboo.getHttpPort(), bamboo.getContextPath());
    }

    public String getBambooHostUrl()
    {
        if (bamboo == null)
        {
            return "";
        }
        return String.format("http://localhost:%d", bamboo.getHttpPort());
    }

    public boolean isBambooEnabled()
    {
        return bamboo != null;
    }

    // Crowd getters
    public String getCrowdUrl()
    {
        if (crowd == null)
        {
            return "";
        }
        return String.format("http://localhost:%d%s", crowd.getHttpPort(), crowd.getContextPath());
    }

    public String getCrowdHostUrl()
    {
        if (crowd == null)
        {
            return "";
        }
        return String.format("http://localhost:%d", crowd.getHttpPort());
    }

    public String getCrowdContextPath()
    {
        if (crowd == null || StringUtils.isBlank(crowd.getContextPath()))
        {
            return "";
        }
        return crowd.getContextPath().replaceAll("/", "");
    }

    public int getCrowdPort()
    {
        return crowd != null ? crowd.getHttpPort() : 0;
    }


}
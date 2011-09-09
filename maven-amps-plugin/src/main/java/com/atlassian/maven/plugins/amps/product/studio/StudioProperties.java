package com.atlassian.maven.plugins.amps.product.studio;

import static com.atlassian.maven.plugins.amps.util.ProjectUtils.firstNotNull;
import static com.atlassian.maven.plugins.amps.util.ProjectUtils.getHomeDirectory;

import java.io.File;
import java.util.Map;

import org.apache.maven.project.MavenProject;

import com.atlassian.maven.plugins.amps.Product;
import com.atlassian.maven.plugins.amps.product.ProductHandlerFactory;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

/**
 * This bean shares the configuration of Studio across all products
 */
public class StudioProperties
{

    /**
     * The data to use to initialize Studio Home. Folder or zip.
     * Must contain:<ul>
     * <li>studio-home/studio.properties</li>
     * <li>studio-home/studio.license<li>
     * <li>studio-home/studio-initial-data.properties</li>
     * <li>studio-home/studio-initial-data.xml</li>
     * <li>jira-home/</li>
     * <li>confluence-home/</li>
     * <li>fecru-home/</li>
     * <li>crowd-home/</li>
     * <li>bamboo-home/</li>
     */
    private String studioHomeData;

    /**
     * The data to use to initialize the svn root. Folder or zip.
     */
    protected String svnRootData;

    /**
     * The data to use to initialize the webdav home. Folder or zip.
     */
    protected String webDavData;

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
    private String confluenceContext;

    private Product fisheye;
    private String fisheyeControlPort;
    private String fisheyeShutdownEnabled;

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
        webDavData = studioContext.getWebDavData();
        svnRootData = studioContext.getSvnRootData();
        studioHomeData = firstNotNull(studioContext.getStudioHomeData(), "src/test/resources/home");
        studioProduct = studioContext;
        
        gappsEnabled = Boolean.getBoolean(studioContext.getGappsEnabled());
        gappsDomain = firstNotNull(studioContext.getGappsDomain(), "");
    }

    public void setStudioHomeData(String studioHomeData)
    {
        this.studioHomeData = studioHomeData;
    }

    public void setSvnRootData(String svnRootData)
    {
        this.svnRootData = svnRootData;
    }

    public void setWebDavData(String webDavData)
    {
        this.webDavData = webDavData;
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

    public void setConfluenceContext(String confluenceContext)
    {
        this.confluenceContext = confluenceContext;
    }

    public void setFisheye(Product fisheye)
    {
        this.fisheye = fisheye;
    }

    public void setFisheyeControlPort(String fisheyeControlPort)
    {
        this.fisheyeControlPort = fisheyeControlPort;
    }

    public void setFisheyeShutdownEnabled(String fisheyeShutdownEnabled)
    {
        this.fisheyeShutdownEnabled = fisheyeShutdownEnabled;
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

    public String getStudioHomeData()
    {
        return studioHomeData;
    }

    public String getSvnRootData()
    {
        return svnRootData;
    }

    public String getWebDavData()
    {
        return webDavData;
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

    public String getConfluenceContext()
    {
        return confluenceContext;
    }

    public Product getFisheye()
    {
        return fisheye;
    }

    public String getFisheyeControlPort()
    {
        return fisheyeControlPort;
    }

    public String getFisheyeShutdownEnabled()
    {
        return fisheyeShutdownEnabled;
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
        return jira != null ? jira.getContextPath() : "";
    }

    public String getJiraHomeDirectory(MavenProject project)
    {
        return getHomeDirectory(project, jira).getAbsolutePath();
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
        return confluence != null ? confluence.getContextPath() : "";
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
        return fisheye != null && fisheye.getContextPath() != null ? fisheye.getContextPath() : "";
    }

    public String getFisheyeUrl()
    {
        if (fisheye == null)
        {
            return "";
        }
        if (fisheye.getContextPath() != null)
        {
            return String.format("http://localhost:%d/%s", fisheye.getHttpPort(), fisheye.getContextPath());
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
        return bamboo != null ? bamboo.getContextPath() : "";
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
        return crowd != null ? crowd.getContextPath() : "";
    }

    public int getCrowdPort()
    {
        return crowd != null ? crowd.getHttpPort() : 0;
    }


}
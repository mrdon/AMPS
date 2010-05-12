package config;

import com.atlassian.selenium.AbstractSeleniumConfiguration;
import com.atlassian.selenium.SeleniumConfiguration;

public class MySeleniumConfiguration extends AbstractSeleniumConfiguration
{
    private static final String LOCATION = System.getProperty("selenium.location", "localhost");
    private static final int PORT = Integer.getInteger("selenium.port", 4444);

    private static final long MAX_WAIT_TIME = 10000;
    private static final long CONDITION_CHECK_INTERVAL = 100;

    private static final MySeleniumConfiguration INSTANCE = new MySeleniumConfiguration();

    private MySeleniumConfiguration() { }

    public static SeleniumConfiguration getInstance()
    {
        return INSTANCE;
    }

    public String getServerLocation()
    {
        return LOCATION;
    }

    public int getServerPort()
    {
        return PORT;
    }

    public String getBrowserStartString()
    {
        return getBrowserString();
    }

    private String getBrowserString()
    {
        String browser = System.getProperty("selenium.browser", "*firefox /Applications/Firefox.app/Contents/MacOS/firefox-bin");
        return browser.replaceAll("\"","");
    }

    public String getBaseUrl()
    {
        final String httpPort = System.getProperty("http.port");
        final String contextPath = System.getProperty("context.path");
        final String url = new StringBuilder().append("http://localhost:").append(httpPort).append(contextPath).toString();
        return url;
    }

    public boolean getStartSeleniumServer()
    {
        return false;
    }

    public long getActionWait()
    {
        return MAX_WAIT_TIME;
    }

    public long getPageLoadWait()
    {
        return MAX_WAIT_TIME;
    }

    public long getConditionCheckInterval()
    {
        return CONDITION_CHECK_INTERVAL;
    }
}
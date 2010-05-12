package config;

import com.atlassian.selenium.SeleniumClient;

public class Client extends SeleniumClient
{
    private static final Client CLIENT = new Client(MySeleniumConfiguration.getInstance());

    static
    {
        CLIENT.start();
    }

    public Client(com.atlassian.selenium.SeleniumConfiguration config)
    {
        super(config);
    }

    public static Client getInstance()
    {
        return CLIENT;
    }
}

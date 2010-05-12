package selenium.com.atlassian.amps;

import com.atlassian.selenium.SeleniumTest;
import config.MySeleniumConfiguration;
import org.junit.Test;

public class IntegrationTest extends SeleniumTest
{
    public void testServlet() throws Exception
    {
        gotoPage("/plugins/servlet/it");

        assertThat.textPresent("Hello world this is ITs calling");
    }

    @Override
    public com.atlassian.selenium.SeleniumConfiguration getSeleniumConfiguration()
    {
        return MySeleniumConfiguration.getInstance();
    }

    protected void gotoPage(String page)
    {
        client.open(config.getBaseUrl() + page);
    }

}

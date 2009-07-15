package it;

import com.atlassian.confluence.plugin.functest.AbstractConfluencePluginWebTestCase;

public class IntegrationTestMyPlugin extends AbstractConfluencePluginWebTestCase
{
	public void testSomething()
	{
        gotoPage("");
        assertTextPresent("Welcome");
	}
}

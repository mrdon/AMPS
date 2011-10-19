package it;

import org.junit.Test;

import com.atlassian.pageobjects.elements.query.Poller;
import ${package}.HeaderSection;
import ${package}.StudioLoginPage;
import ${package}.StudioTestedProduct;

public class MyPluginIntegrationTest
{
    StudioTestedProduct PRODUCT = StudioTestedProduct.create();

    /**
     * Test we can switch between all applications
     */
    @Test
    public void testUseAllApplications()
    {
        StudioLoginPage loginPage = PRODUCT.visit(StudioLoginPage.class);
        HeaderSection page = loginPage.login("sysadmin", "sysadmin");

        Poller.waitUntilTrue(page.isIssuesAvailable());
        Poller.waitUntilTrue(page.isWikiAvailable());
        Poller.waitUntilTrue(page.isSourceAvailable());
        Poller.waitUntilTrue(page.isReviewAvailable());
        Poller.waitUntilTrue(page.isBuildAvailable());

        page.logout();
    }
}

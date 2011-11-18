package it.${package};

import static org.junit.Assert.assertTrue;

import ${package}.pageobjects.MyServletPage;
import org.junit.BeforeClass;
import org.junit.Test;

import com.atlassian.pageobjects.TestedProductFactory;
import com.atlassian.webdriver.caviar.CaviarTestedProduct;
import com.atlassian.webdriver.caviar.page.CaviarHomePage;
import com.atlassian.webdriver.caviar.page.CaviarLoginPage;

public class MyUITest
{
    private final static CaviarTestedProduct CAVIAR = TestedProductFactory.create(CaviarTestedProduct.class);

    @BeforeClass
    public static void setUp()
    {
        CaviarLoginPage loginPage = CAVIAR.visit(CaviarLoginPage.class);
        loginPage.login("user", "user", CaviarHomePage.class);
    }

    @Test
    public void testLogin()
    {
        MyServletPage myServlet = CAVIAR.visit(MyServletPage.class);
        assertTrue(myServlet.isWelcome());
    }
}

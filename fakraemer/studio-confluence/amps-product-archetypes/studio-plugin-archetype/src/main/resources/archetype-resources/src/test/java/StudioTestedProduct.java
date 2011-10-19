package ${package};

import com.atlassian.pageobjects.Defaults;
import com.atlassian.pageobjects.Page;
import com.atlassian.pageobjects.PageBinder;
import com.atlassian.pageobjects.ProductInstance;
import com.atlassian.pageobjects.TestedProduct;
import com.atlassian.pageobjects.TestedProductFactory;
import com.atlassian.pageobjects.binder.InjectPageBinder;
import com.atlassian.pageobjects.binder.StandardModule;
import com.atlassian.pageobjects.elements.ElementModule;
import com.atlassian.pageobjects.elements.timeout.TimeoutsModule;
import com.atlassian.webdriver.AtlassianWebDriverModule;
import com.atlassian.webdriver.pageobjects.DefaultWebDriverTester;
import com.atlassian.webdriver.pageobjects.WebDriverTester;

@Defaults (instanceId = "studio1", contextPath = "/jira", httpPort = 2990)
public class StudioTestedProduct implements TestedProduct<WebDriverTester>
{

    private final WebDriverTester webDriverTester;
    private final ProductInstance productInstance;
    private final InjectPageBinder pageBinder;

    /**
     * It is unconventional to:<ul>
     * <li>have this create() method. Please prefer {@link TestedProductFactory#create(StudioTestedProduct.class)} when possible.
     * The local version of Studio is a specific case because you must access it with 'localhost'.</li>
     * <li>define the TestedProduct in the test sources. When available, use the PageObjects provided by the application.</li>
     * </ul>
     */
    public static StudioTestedProduct create()
    {
        ProductInstance instance = new ProductInstance()
        {
            @Override
            public String getBaseUrl()
            {
                return "http://localhost:2990/jira";
            }

            @Override
            public int getHttpPort()
            {
                return 2990;
            }

            @Override
            public String getContextPath()
            {
                return "/jira";
            }

            @Override
            public String getInstanceId()
            {
                return "studio1";
            }
        };
        return new StudioTestedProduct(null,  instance);
    }

    public StudioTestedProduct(TestedProductFactory.TesterFactory<WebDriverTester> testerFactory, ProductInstance productInstance)
    {
        this.webDriverTester = testerFactory != null ? testerFactory.create() : new DefaultWebDriverTester();
        this.productInstance = productInstance;
        this.pageBinder = new InjectPageBinder(productInstance, webDriverTester,
                new StandardModule(this),
                new AtlassianWebDriverModule(this),
                new ElementModule(),
                new TimeoutsModule());
    }

    @Override
    public <P extends Page> P visit(Class<P> pageClass, Object... args)
    {
        return pageBinder.navigateToAndBind(pageClass, args);
    }

    @Override
    public PageBinder getPageBinder()
    {
        return pageBinder;
    }

    @Override
    public ProductInstance getProductInstance()
    {
        return productInstance;
    }

    @Override
    public WebDriverTester getTester()
    {
        return webDriverTester;
    }
}

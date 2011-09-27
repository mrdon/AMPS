package ${package};

import com.atlassian.pageobjects.Page;
import com.atlassian.pageobjects.PageBinder;
import com.atlassian.pageobjects.elements.ElementBy;
import com.atlassian.pageobjects.elements.PageElement;
import com.google.inject.Inject;

public class StudioLoginPage implements Page
{
    private final static String DEFAULT_SYSADMIN_USER = "sysadmin";
    private final static String DEFAULT_SYSADMIN_PASSWORD = "sysadmin";

    @Override
    public String getUrl()
    {
        return "/login.jsp";
    }

    @ElementBy(name = "os_username")
    private PageElement loginUsername;

    @ElementBy(name = "os_password")
    private PageElement loginPassword;

    @ElementBy(name = "login")
    private PageElement loginSubmit;

    // Logout objects
    @ElementBy(partialLinkText = "Administrator")
    private PageElement studioUserMenu;

    @ElementBy(className = "logout-link")
    private PageElement logoutMenu;

    @ElementBy(id = "confirm-logout-submit")
    private PageElement confirmLogout;

    @Inject
    private PageBinder binder;

    public HeaderSection loginSysadmin()
    {
        return login(DEFAULT_SYSADMIN_USER, DEFAULT_SYSADMIN_PASSWORD);
    }
    
    public HeaderSection login(String username, String password)
    {
        if (loginUsername.isPresent())
        {
            loginUsername.type(username);
            loginPassword.type(password);
            loginSubmit.click();
        }
        return binder.bind(HeaderSection.class);
    }

    public void logout()
    {
        if (studioUserMenu.isPresent())
        {
            studioUserMenu.click();
            logoutMenu.click();
            confirmLogout.click();
        }
    }
}

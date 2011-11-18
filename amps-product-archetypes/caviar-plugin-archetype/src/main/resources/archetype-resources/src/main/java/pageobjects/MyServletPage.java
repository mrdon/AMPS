package ${package}.pageobjects;

import com.atlassian.pageobjects.Page;
import com.atlassian.pageobjects.elements.ElementBy;
import com.atlassian.pageobjects.elements.PageElement;

public class MyServletPage implements Page
{
    @Override
    public String getUrl()
    {
        return "/plugins/servlet/test";
    }

    @ElementBy(className = "title")
    PageElement title;

    public boolean isWelcome()
    {
        return title.getText().contains("Welcome");
    }
}

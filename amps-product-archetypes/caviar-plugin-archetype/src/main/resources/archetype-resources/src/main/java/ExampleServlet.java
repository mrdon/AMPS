package ${package};

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class ExampleServlet extends HttpServlet
{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
    {
        res.getWriter().write("<html><head><title>Hello World Page</title>" +
                "<meta name='decorator' content='atl.general' />" +
                "</head>" +
                "<body><h2 class='title'>Welcome</h2></body>" +
                "</html>");
        res.getWriter().close();
    }
}

package cmbnj.midsrv.dptgrt;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


@WebServlet(
		name="IIEACServelt",
		urlPatterns={"/IIEAC"},
		loadOnStartup=1
)
public class IIEACServelt extends HttpServlet

{
	
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
//    	System.out.println(request.getLocalAddr());
//        response.getWriter().println("Hello, World, myfirstServlet!");
 
//		String user = request.getParameter("user");
//    	if(user==null)
//    		user = DEFAULT_USER;
    	response.setContentType("text/html");
    	response.setCharacterEncoding("UTF-8");
//    	response.sendRedirect("IIEAC?action=askgrt&cono=123456");
    	PrintWriter writer = response.getWriter();
        writer.append("<!DOCTYPE html>\r\n")
        .append("<html>\r\n")
        .append("    <head>\r\n")
        .append("        <title>Hello User Application</title>\r\n")
        .append("    </head>\r\n")
        .append("    <body>\r\n")
        .append("        Hello, ").append("xiongwei").append("!<br/><br/>\r\n")
        .append("        <form action=\"IIEAC\" method=\"POST\">\r\n")
        .append("            Enter your name:<br/>\r\n")
        .append("            <input type=\"text\" name=\"user\"/><br/>\r\n")
        .append("            <input type=\"submit\" value=\"Submit\"/>\r\n")
        .append("        </form>\r\n")
        .append("    </body>\r\n")
        .append("</html>\r\n");
    }

	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		String fruits = "";
		String action = request.getParameter("action");
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		
		PrintWriter writer = response.getWriter();
		if(action != "askgrt")
		{
			writer.append("<!DOCTYPE html>\r\n")
			.append("html\r\n")
			.append("  <head>\r\n")
			.append(" <title>Your are post error action!!</title>\r\n")
			.append("  </head>\r\n");	
			writer.append("</html>\r\n");

			return ;
		}
		String cono = request.getParameter("cono");
		String billno = request.getParameter("custagr");
		String custctfid = request.getParameter("custctfid");
		String encodmsg = request.getParameter("encodmsg");
		String merreturl = request.getParameter("merreturl");
		String allparas = String.format("  <h2>cono=%s!!billno=%s</h2>\r\n", cono, billno);
		writer.append("<!DOCTYPE html>\r\n")
		.append("html\r\n")
		.append("  <head>\r\n")
		.append(" <title>Hello Ask agrgrt</title>\r\n")
		.append("  </head>\r\n")
		.append(" <body>\r\n")
		.append("  <h2></h2>\r\n");
		
		
		

	}
	
	
    @Override
    public void init() throws ServletException
    {
        System.out.println("Servlet " + this.getServletName() + " has started.");
    }

    @Override
    public void destroy()
    {
        System.out.println("Servlet " + this.getServletName() + " has stopped.");
    }
}

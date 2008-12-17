package test.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NormalServlet extends HttpServlet {
	
	private static final long serialVersionUID = -755317532017820470L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		PrintWriter writer = response.getWriter();
		
		writer.println("<html>");
		writer.println("<b>Hello world.</b>");
		writer.println("</html>");
	}
}

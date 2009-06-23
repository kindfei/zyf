package test.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NormalServlet extends HttpServlet {
	
	private static final long serialVersionUID = -755317532017820470L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		PrintWriter writer = response.getWriter();
		
		writer.println("<html>");
		writer.println("<b>GET Hello world.</b>");
		writer.println("</html>");
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		
		Enumeration<?> h = req.getHeaderNames();
		while (h.hasMoreElements()) {
			String name = (String) h.nextElement();
			String value = req.getHeader(name);
			System.out.println("Header: " + name + "=" + value);
		}
		
		Cookie[] cookies = req.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				System.out.println("Cookie: " + cookies[i].getDomain() + "." + cookies[i].getName() + "=" + cookies[i].getValue());
			}
		}
		
		Enumeration<?> e = req.getParameterNames();
		while (e.hasMoreElements()) {
			String name = (String) e.nextElement();
			String value = req.getParameter(name);
			System.out.println("Parameter: " + name + "=" + value);
		}

		PrintWriter writer = resp.getWriter();
		writer.println("<html>");
		writer.println("<b>POST Hello world.</b>");
		writer.println("</html>");
	}
}

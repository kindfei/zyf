package test.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Servlet implementation class TestTx
 */
public class TestTxServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private TestTxPojo testTxPojo;
	
	@Override
	public void init() throws ServletException {
		super.init();
		WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
		testTxPojo = (TestTxPojo) ctx.getBean("testTxPojo");
	}
    
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String type = req.getParameter("type");
		if (type == null) {
			resp.getWriter().println("Missing required parameter: type");
			return;
		}
		
		String num = req.getParameter("num");
		if (num == null) {
			resp.getWriter().println("Missing required parameter [num]");
			return;
		}
		
		String pojo = req.getParameter("tx");
		if (pojo != null) {
			testTxPojo.invokeWithTx(type, num);
		} else {
			testTxPojo.invoke(type, num);
		}
			
		resp.getWriter().println("done");
	}
}

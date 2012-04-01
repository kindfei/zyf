package test.filter;

import java.io.IOException;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestFilter implements Filter {

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		
		System.out.println(request.getParameterMap());

		ExternalContext context = null;
		if (request.getParameter("context") != null && request.getParameter("context").equals("true")) {
			context = FacesContext.getCurrentInstance().getExternalContext();
		}
		
		if (request.getRequestURL().indexOf("redirect.jsp") != -1) {
			System.out.println("redirect");
			if (context != null) {
				context.redirect("DynamicComponent.jsf");
				return;
			} else {
				response.sendRedirect("DynamicComponent.jsf");
				return;
			}
		} else if (request.getRequestURL().indexOf("forward.jsp") != -1) {
			System.out.println("forward");
			if (context != null) {
				context.dispatch("DynamicComponent.jsf");
				return;
			} else {
				RequestDispatcher dispatcher = request.getRequestDispatcher("DynamicComponent.jsf");
				dispatcher.forward(request, response);
				return;
			}
		}
		
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

}

package test.struts.action;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import test.struts.form.RegisterForm;


public class ConfirmAction extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form
			, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Cookie[] cs = request.getCookies();
		if (cs != null)
			for (int i = 0; i < cs.length; i++) {
				System.out.println(cs[i].getName() + " - " + cs[i].getValue());
			}
		else
			System.out.println("No cookie");
		
		RegisterForm f = (RegisterForm) request.getSession().getAttribute("RegisterForm");
		if (f == null) {
			System.out.println("SessionId:" + request.getSession().getId() + " not exist");
			return mapping.findForward("error");
		}
		System.out.println(f.getUserId());
		System.out.println(f.getEmail());
		System.out.println(f.getAddress());
		
		Cookie c = new Cookie("JSESSIONID", null);
		c.setMaxAge(0);
		response.addCookie(c);
		
		return mapping.findForward("ok");
	}
}

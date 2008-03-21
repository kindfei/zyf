package test.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class AjaxAction extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String key = (String)request.getAttribute("getCities");
		System.out.println(key);
		key = (String)request.getParameter("getCities");
		System.out.println(key);
		
		String[] strCities = {"dalian", "daqing", "shanghai", "shenyang"};
		
		StringBuffer cities = new StringBuffer();
		cities.append("<cities>");
		for (int i = 0; i < strCities.length; i++) {
			if (strCities[i].startsWith(key))
				cities.append("<city>" + strCities[i] + "</city>");
		}
		cities.append("</cities>");
		
		response.setContentType("application/xml");
		response.getWriter().write(cities.toString());
		return null;
	}
}

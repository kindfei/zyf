package test.struts.action;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class PushAction extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form
			, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
//		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
//		response.setHeader("Cache-Control", "post-check=0, pre-check=0");
//		response.setHeader("Pragma", "no-cache");
		response.setContentType("text/plain;charset=UTF-8");
		
		ServletOutputStream os = response.getOutputStream();
		
		for (int i = 0; i < 2048; i++) {
			os.write(0);
		}

		os.flush();
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		for (int i = 0; i < 10000; i++) {
			os.print(sdf.format(new Date()) + ",");
			os.flush();

			System.out.println("[Action] " + Thread.currentThread().getName() + " - " + i);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
}

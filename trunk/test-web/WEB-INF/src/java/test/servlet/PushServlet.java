package test.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PushServlet extends HttpServlet {
	
	private static final long serialVersionUID = -6645799798327190921L;

	protected void doGet(HttpServletRequest request, final HttpServletResponse response) 
			throws ServletException, IOException {
		
		response.setBufferSize(128);
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		response.setHeader("Cache-Control", "post-check=0, pre-check=0");
		response.setHeader("Pragma", "no-cache");
		response.setContentType("text/plain;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		ServletOutputStream os = response.getOutputStream();
		
		for (int i = 0; i < 2048; i++) {
			os.write(0);
		}
		
		os.flush();
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		for (int i = 0; i < 10000; i++) {
			os.print(sdf.format(new Date()) + ",");
			os.flush();
		
			System.out.println("[Servlet] " + Thread.currentThread().getName() + " - " + i);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
}

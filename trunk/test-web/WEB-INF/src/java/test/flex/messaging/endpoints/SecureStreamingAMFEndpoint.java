package test.flex.messaging.endpoints;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import flex.messaging.endpoints.StreamingAMFEndpoint;

public class SecureStreamingAMFEndpoint extends StreamingAMFEndpoint {

	protected void streamMessages(List messages, ServletOutputStream os,
			HttpServletResponse response) throws IOException {

		if(response.containsHeader("Transfer-Encoding")) response.setHeader("Transfer-Encoding", "");
		
		super.streamMessages(messages, os, response);
	}
	
	
}

package test.flex.messaging.endpoints;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import flex.messaging.client.FlexClient;

public class StreamingAMFEndpoint extends flex.messaging.endpoints.StreamingAMFEndpoint {
	
	protected void handleFlexClientStreamingOpenRequest(HttpServletRequest req,
			HttpServletResponse res, FlexClient flexClient) {
		
		HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper(res) {
			public void setHeader(String name, String value) {
				if (name.equalsIgnoreCase("Transfer-Encoding")) {
					return;
				}
				super.setHeader(name, value);
			}
		};
		
		super.handleFlexClientStreamingOpenRequest(req, wrapper, flexClient);
	}

}

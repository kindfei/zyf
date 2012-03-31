package fei.smtpserver;

import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.net.ssl.SSLContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.james.protocols.api.AbstractProtocolHandlerChain;
import org.apache.james.protocols.api.LineHandler;
import org.apache.james.protocols.api.ProtocolHandlerChain;
import org.apache.james.protocols.api.ProtocolSession;
import org.apache.james.protocols.api.WiringException;
import org.apache.james.protocols.impl.AbstractAsyncServer;
import org.apache.james.protocols.impl.AbstractChannelUpstreamHandler;
import org.apache.james.protocols.impl.AbstractResponseEncoder;
import org.apache.james.protocols.impl.AbstractSSLAwareChannelPipelineFactory;
import org.apache.james.protocols.impl.AbstractSession;
import org.apache.james.protocols.impl.LineHandlerUpstreamHandler;
import org.apache.james.protocols.smtp.MailEnvelopeImpl;
import org.apache.james.protocols.smtp.SMTPResponse;
import org.apache.james.protocols.smtp.SMTPSession;
import org.apache.james.protocols.smtp.core.DataCmdHandler;
import org.apache.james.protocols.smtp.core.DataLineMessageHookHandler;
import org.apache.james.protocols.smtp.core.ExpnCmdHandler;
import org.apache.james.protocols.smtp.core.HeloCmdHandler;
import org.apache.james.protocols.smtp.core.HelpCmdHandler;
import org.apache.james.protocols.smtp.core.MailCmdHandler;
import org.apache.james.protocols.smtp.core.NoopCmdHandler;
import org.apache.james.protocols.smtp.core.PostmasterAbuseRcptHook;
import org.apache.james.protocols.smtp.core.QuitCmdHandler;
import org.apache.james.protocols.smtp.core.RcptCmdHandler;
import org.apache.james.protocols.smtp.core.ReceivedDataLineFilter;
import org.apache.james.protocols.smtp.core.RsetCmdHandler;
import org.apache.james.protocols.smtp.core.SMTPCommandDispatcherLineHandler;
import org.apache.james.protocols.smtp.core.VrfyCmdHandler;
import org.apache.james.protocols.smtp.core.WelcomeMessageHandler;
import org.apache.james.protocols.smtp.core.esmtp.EhloCmdHandler;
import org.apache.james.protocols.smtp.core.esmtp.MailSizeEsmtpExtension;
import org.apache.james.protocols.smtp.hook.Hook;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelUpstreamHandler;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

/**
 * 
 * @author yz69579
 *
 */
public class SMTPServer extends AbstractAsyncServer {
	private static final Log log = LogFactory.getLog(SMTPServer.class);

	private int connectionLimit;
	private int connPerIP;
	
    private List<Hook> hooks = new ArrayList<Hook>();
	
	private String helloName;
	private long maxMessageSize;
	private String smtpGreeting;
	private boolean authSupported;
	private boolean relayingAllowed;
	private boolean useAddressBracketsEnforcement;
	private boolean useHeloEhloEnforcement;
    
    public SMTPServer() {
    	
    }

	public int getConnectionLimit() {
		return connectionLimit;
	}

	public void setConnectionLimit(int connectionLimit) {
		this.connectionLimit = connectionLimit;
	}

	public int getConnPerIP() {
		return connPerIP;
	}

	public void setConnPerIP(int connPerIP) {
		this.connPerIP = connPerIP;
	}

	public String getHelloName() {
		return helloName;
	}

	public void setHelloName(String helloName) {
		this.helloName = helloName;
	}

	public long getMaxMessageSize() {
		return maxMessageSize;
	}

	public void setMaxMessageSize(long maxMessageSize) {
		this.maxMessageSize = maxMessageSize;
	}

	public String getSmtpGreeting() {
		return smtpGreeting;
	}

	public void setSmtpGreeting(String smtpGreeting) {
		this.smtpGreeting = smtpGreeting;
	}

	public boolean isAuthSupported() {
		return authSupported;
	}

	public void setAuthSupported(boolean authSupported) {
		this.authSupported = authSupported;
	}

	public boolean isRelayingAllowed() {
		return relayingAllowed;
	}

	public void setRelayingAllowed(boolean relayingAllowed) {
		this.relayingAllowed = relayingAllowed;
	}

	public boolean isUseAddressBracketsEnforcement() {
		return useAddressBracketsEnforcement;
	}

	public void setUseAddressBracketsEnforcement(boolean useAddressBracketsEnforcement) {
		this.useAddressBracketsEnforcement = useAddressBracketsEnforcement;
	}

	public boolean isUseHeloEhloEnforcement() {
		return useHeloEhloEnforcement;
	}

	public void setUseHeloEhloEnforcement(boolean useHeloEhloEnforcement) {
		this.useHeloEhloEnforcement = useHeloEhloEnforcement;
	}

	public List<Hook> getHooks() {
		return hooks;
	}

	public void setHooks(List<Hook> hooks) {
		this.hooks = hooks;
	}
	
	private ProtocolHandlerChain chain;
	
	public void init() throws Exception {
		chain = new SMTPProtocolHandlerChain();
	}
	
	/**
	 * 
	 * @author yz69579
	 *
	 */
	private class SMTPProtocolHandlerChain extends AbstractProtocolHandlerChain {
	    private List<Object> handlers = new ArrayList<Object>();

	    public SMTPProtocolHandlerChain() throws WiringException {
	    	handlers.add(new SMTPCommandDispatcherLineHandler());
	    	handlers.add(new ExpnCmdHandler());
	    	handlers.add(new EhloCmdHandler());
	    	handlers.add(new HeloCmdHandler());
	    	handlers.add(new HelpCmdHandler());
	    	handlers.add(new MailCmdHandler());
	    	handlers.add(new NoopCmdHandler());
	    	handlers.add(new QuitCmdHandler());
	    	handlers.add(new RcptCmdHandler());
	    	handlers.add(new RsetCmdHandler());
	    	handlers.add(new VrfyCmdHandler());
	    	handlers.add(new DataCmdHandler() {
	        	@Override
	        	protected SMTPResponse doDATA(SMTPSession session, String argument) {
	        		SMTPResponse r = super.doDATA(session, argument);
	        		
	        		MailEnvelopeImpl env =  (MailEnvelopeImpl) session.getState().get(MAILENV);
	        		
	        		MailEnvelopeImpl mail = new MailEnvelopeImpl() {
	        			private OutputStream outputStream;
	        			public OutputStream getMessageOutputStream() {
	        				if (outputStream == null) {
	        					outputStream = super.getMessageOutputStream();
	        				}
	        		        return outputStream;
	        		    }
	        		};
	        		mail.setRecipients(env.getRecipients());
	        		mail.setSender(env.getSender());
	        		
	                session.getState().put(MAILENV, mail);
	                
	                return r;
	            }
	        });
	    	handlers.add(new MailSizeEsmtpExtension());
	        handlers.add(new WelcomeMessageHandler());
	        handlers.add(new PostmasterAbuseRcptHook());
	        handlers.add(new ReceivedDataLineFilter());
	        handlers.add(new DataLineMessageHookHandler());
	        
	        handlers.addAll(hooks);
	        
	        wireExtensibleHandlers();
	    }

	    @Override
	    protected synchronized List<Object> getHandlers() {
	        return Collections.unmodifiableList(handlers);
	    }
	}

	/**
	 * 
	 */
	@Override
	protected ChannelPipelineFactory createPipelineFactory(ChannelGroup group) {
		return new SMTPChannelPipelineFactory(getTimeout(), connectionLimit, connPerIP, group);
	}

	/**
	 * 
	 * @author yz69579
	 *
	 */
	private class SMTPChannelPipelineFactory extends AbstractSSLAwareChannelPipelineFactory {

		public SMTPChannelPipelineFactory(int timeout, int maxConnections, int maxConnectsPerIp, ChannelGroup group) {
			super(timeout, maxConnections, maxConnectsPerIp, group);
		}

		@Override
		protected SSLContext getSSLContext() {
			return null;
		}

		@Override
		protected boolean isSSLSocket() {
			return false;
		}

		@Override
		protected OneToOneEncoder createEncoder() {
			return new SMTPResponseEncoder();
		}

		@Override
		protected ChannelUpstreamHandler createHandler() {
			return new SMTPChannelUpstreamHandler(chain);
		}
		
	}
	
	/**
	 * 
	 * @author yz69579
	 *
	 */
	private class SMTPResponseEncoder extends AbstractResponseEncoder<SMTPResponse> {

		public SMTPResponseEncoder() {
			super(SMTPResponse.class, Charset.forName("US-ASCII"));
		}

		@Override
		protected List<String> getResponse(SMTPResponse response) {
	        List<String> responseList = new ArrayList<String>();
	        
	        for (int k = 0; k < response.getLines().size(); k++) {
	            StringBuffer respBuff = new StringBuffer(256);
	            respBuff.append(response.getRetCode());
	            if (k == response.getLines().size() - 1) {
	                respBuff.append(" ");
	                respBuff.append(response.getLines().get(k));

	            } else {
	                respBuff.append("-");
	                respBuff.append(response.getLines().get(k));

	            }
	            responseList.add(respBuff.toString());
	        }
	        
	        return responseList;
	    }
		
	}
	
	/**
	 * 
	 * @author yz69579
	 *
	 */
	private class SMTPChannelUpstreamHandler extends AbstractChannelUpstreamHandler {

		public SMTPChannelUpstreamHandler(ProtocolHandlerChain chain) {
			super(chain);
		}

		@Override
		protected ProtocolSession createSession(ChannelHandlerContext handlerContext) throws Exception {
			return new SMTPNettySession(log, handlerContext);
		}
		
	}
	
	/**
	 * 
	 * @author yz69579
	 *
	 */
	private class SMTPNettySession extends AbstractSession implements SMTPSession {

	    private Map<String, Object> connectionState;
	    
	    private int lineHandlerCount = 0;
	    
	    private String smtpID;
	    
	    private boolean relayingAllowed;

		public SMTPNettySession(Log log, ChannelHandlerContext handlerContext) {
			super(log, handlerContext);
	        connectionState = new HashMap<String, Object>();
	        smtpID = new Random().nextInt(1024) + "";
	        
	        this.relayingAllowed = SMTPServer.this.relayingAllowed;
		}

		@SuppressWarnings("unchecked")
		@Override
		public Map<String, Object> getState() {
			Map<String, Object> res = (Map<String, Object>) getConnectionState().get(SMTPSession.SESSION_STATE_MAP);
			if (res == null) {
				res = new HashMap<String, Object>();
				getConnectionState().put(SMTPSession.SESSION_STATE_MAP, res);
			}
			return res;
		}

		@Override
		public void resetState() {
	        // remember the ehlo mode between resets
	        Object currentHeloMode = getState().get(CURRENT_HELO_MODE);

	        getState().clear();

	        // start again with the old helo mode
	        if (currentHeloMode != null) {
	            getState().put(CURRENT_HELO_MODE, currentHeloMode);
	        }
		}

		@Override
		public Map<String, Object> getConnectionState() {
	        return connectionState;
		}

		@Override
		public String getHelloName() {
			return helloName;
		}

		@Override
		public long getMaxMessageSize() {
			return maxMessageSize;
		}

		@Override
		public int getPushedLineHandlerCount() {
			return lineHandlerCount;
		}

		@SuppressWarnings("rawtypes")
		@Override
		public int getRcptCount() {
	        int count = 0;

	        // check if the key exists
	        if (getState().get(SMTPSession.RCPT_LIST) != null) {
	            count = ((Collection) getState().get(SMTPSession.RCPT_LIST)).size();
	        }

	        return count;
		}

		@Override
		public String getSMTPGreeting() {
			return smtpGreeting;
		}

		@Override
		public String getSessionID() {
			return smtpID;
		}

		@Override
		public boolean isAuthSupported() {
			return authSupported;
		}

		@Override
		public boolean isRelayingAllowed() {
			return relayingAllowed;
		}

		@Override
		public void popLineHandler() {
			if (lineHandlerCount > 0) {
				getChannelHandlerContext().getPipeline().remove("lineHandler" + lineHandlerCount);
				lineHandlerCount--;
			}
		}

		@Override
		public void pushLineHandler(LineHandler<SMTPSession> overrideCommandHandler) {
			lineHandlerCount++;
			getChannelHandlerContext().getPipeline().addAfter("timeoutHandler", "lineHandler" + lineHandlerCount,
					new LineHandlerUpstreamHandler<SMTPSession>(overrideCommandHandler));
		}

		@Override
		public void setRelayingAllowed(boolean relayingAllowed) {
			this.relayingAllowed = relayingAllowed;
		}

		@Override
		public void sleep(long ms) {
		}

		@Override
		public boolean useAddressBracketsEnforcement() {
			return useAddressBracketsEnforcement;
		}

		@Override
		public boolean useHeloEhloEnforcement() {
			return useHeloEhloEnforcement;
		}
		
	}
}

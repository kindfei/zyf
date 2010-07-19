package jp.emcom.adv.bo.core.biz.system.event.mailer;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import jp.emcom.adv.bo.core.biz.system.mailer.CustomerMailer;
import jp.emcom.adv.bo.core.biz.system.mailer.SystemMailer;
import jp.emcom.adv.bo.core.glossary.enums.biz.ExecutionType;
import jp.emcom.adv.bo.core.glossary.enums.biz.mail.MailPurpose;
import jp.emcom.adv.bo.core.glossary.enums.financial.TradeSide;
import jp.emcom.adv.bo.core.glossary.enums.financial.TradeType;
import jp.emcom.adv.bo.test.ContextLoader;

public class TestDistributedMailer {
	
	public static void main(String[] args) {
		ContextLoader loader = new ContextLoader(TestDistributedMailer.class);
		
		//
		CustomerMailer customerMailer = (CustomerMailer) loader.getBean("customerMailer");
		
		Map<String, String> mailParams = new HashMap<String, String>();
		mailParams.put("name", "zhangyf");
		mailParams.put("customerOrderNo", "test01");
		mailParams.put("currencyPair", "USD/JPY");
		mailParams.put("side", TradeSide.BUY.getDisplayName());
		mailParams.put("tradeType", TradeType.OPEN.getDisplayName());
		mailParams.put("orderType", ExecutionType.MARKET.getDisplayName());
		mailParams.put("executionAmount", BigDecimal.TEN.toString());
		mailParams.put("executionPrice", BigDecimal.ONE.toString());
		mailParams.put("customerID", "C900000015");
		
		customerMailer.send("C900000015", MailPurpose.ORDER_FILLED, mailParams);
		customerMailer.send("C900000015", "SYS_PC_21", mailParams);
		
		//
		SystemMailer systemMailer = (SystemMailer) loader.getBean("systemMailer");
		
		systemMailer.send(new String[] {"zhangyf@adv.emcom.jp"}, MailPurpose.ORDER_FILLED, mailParams);
	}
}

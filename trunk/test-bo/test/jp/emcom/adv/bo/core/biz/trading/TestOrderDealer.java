package jp.emcom.adv.bo.core.biz.trading;

import java.math.BigDecimal;

import jp.emcom.adv.bo.core.biz.trading.vo.DealOrderParam;
import jp.emcom.adv.bo.core.glossary.enums.biz.ClientModuleType;
import jp.emcom.adv.bo.core.glossary.enums.biz.ExecutionType;
import jp.emcom.adv.bo.core.glossary.enums.financial.TradeSide;
import jp.emcom.adv.bo.core.glossary.enums.financial.TradeType;
import jp.emcom.adv.bo.test.ContextLoader;

public class TestOrderDealer extends ContextLoader {
	private OrderDealer orderDealer;
	
	TestOrderDealer() {
		orderDealer = (OrderDealer) getBean("orderDealer");
	}
	
	void test() {
		DealOrderParam p = new DealOrderParam();
		
		p.setCustomerId("C0000001");
		p.setExecutionType(ExecutionType.MARKET);
		p.setTradeType(TradeType.OPEN);
		p.setOrderSide(TradeSide.BUY);
		p.setProductId("P00000000301");
		p.setOrderPrice(new BigDecimal("20"));
		p.setOrderAmount(new BigDecimal("10"));
		p.setOrderPriceId("P0001");
		p.setClientIpAddress("10.15.3.52");
		p.setClientModuleType(ClientModuleType.MOB);
		
		orderDealer.dealOrder(p);
	}
	
	public static void main(String[] args) {
		TestOrderDealer t = new TestOrderDealer();
		t.test();
	}
}

package jp.emcom.adv.bo.core.biz.riskctrl;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jp.emcom.adv.bo.core.biz.riskctrl.vo.ProductRiskInfo;
import jp.emcom.adv.bo.core.glossary.enums.financial.TradeSide;
import jp.emcom.adv.bo.core.utils.CommandUtils;
import jp.emcom.adv.bo.test.ContextLoader;

public class TestRiskControl extends ContextLoader {
	private RiskController riskController;
	private ExecutorService executorService;
	
	TestRiskControl() {
		riskController = (RiskController) getBean("riskController");
		executorService = Executors.newCachedThreadPool();
	}
	
	void printProductRiskInfo(String productId) {
		ProductRiskInfo info = riskController.obtainProductRiskInfo(productId);
		System.out.println(info);
	}
	
	void riskControl(String productId, TradeSide tradeSide, BigDecimal orderAmount
			, BigDecimal price, boolean check) {
		riskController.riskControl(productId, tradeSide, orderAmount, price, check);
	}
	
	void asyncRiskControl(final String productId, final TradeSide tradeSide,
			final BigDecimal orderAmount, final BigDecimal price, final boolean check) {
		executorService.submit(new Runnable() {
			@Override
			public void run() {
				riskController.riskControl(productId, tradeSide, orderAmount, price, check);
			}
		});
	}
	
	void stop() {
		executorService.shutdown();
	}
	
	public static void main(String[] args) {
		final TestRiskControl inst = new TestRiskControl();

		String[] opts = {
			"printProductRiskInfo",
			"riskControl",
			"asyncRiskControl",
			"exit",
		};
		
		boolean exit = false;
		while (!exit) {
			switch (CommandUtils.choose(opts)) {
			case 0:
				inst.printProductRiskInfo(CommandUtils.input("Input productId"));
				break;
			case 1:
				inst.riskControl(CommandUtils.input("Input productId")
						, TradeSide.parse(Integer.parseInt(CommandUtils.input("Input tradeSide")))
						, new BigDecimal(CommandUtils.input("Input orderAmount"))
						, new BigDecimal(CommandUtils.input("Input price"))
						, Boolean.parseBoolean(CommandUtils.input("Input check")));
				break;
			case 2:
				inst.asyncRiskControl(CommandUtils.input("Input productId")
						, TradeSide.parse(Integer.parseInt(CommandUtils.input("Input tradeSide")))
						, new BigDecimal(CommandUtils.input("Input orderAmount"))
						, new BigDecimal(CommandUtils.input("Input price"))
						, Boolean.parseBoolean(CommandUtils.input("Input check")));
				break;
			default:
				exit = true;
				break;
			}
		}
	}
}

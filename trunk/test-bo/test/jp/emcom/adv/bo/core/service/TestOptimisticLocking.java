package jp.emcom.adv.bo.core.service;

import java.util.List;

import jp.emcom.adv.bo.core.dao.datasource.DataSourceType;
import jp.emcom.adv.bo.core.dao.impl.main.trading.impl.ContractDaoImpl;
import jp.emcom.adv.bo.core.dao.impl.main.trading.impl.OrderDaoImpl;
import jp.emcom.adv.bo.core.dao.impl.main.trading.impl.LeaveOrderDaoImpl;
import jp.emcom.adv.bo.core.dao.po.main.BoContract;
import jp.emcom.adv.bo.core.dao.po.main.BoLeaveOrder;
import jp.emcom.adv.bo.core.dao.po.main.BoOrder;
import jp.emcom.adv.bo.core.utils.CommandUtils;
import jp.emcom.adv.bo.core.utils.spring.tx.Tx;
import jp.emcom.adv.bo.test.ContextLoader;

import org.springframework.transaction.annotation.Propagation;

/**
 * 
 * @author zhangyf
 *
 */
@SuppressWarnings("unchecked")
public class TestOptimisticLocking extends AbstractService {
	
	private OrderDaoImpl orderDao;
	private LeaveOrderDaoImpl orderTriggerDao;
	private ContractDaoImpl contractDao;

	/*
	 * 
	 */
	public void setOrderDao(OrderDaoImpl orderDao) {
		this.orderDao = orderDao;
	}
	public void setOrderTriggerDao(LeaveOrderDaoImpl orderTriggerDao) {
		this.orderTriggerDao = orderTriggerDao;
	}
	public void setContractDao(ContractDaoImpl contractDao) {
		this.contractDao = contractDao;
	}
	
	/*
	 * 
	 */
	@Tx(datasource = DataSourceType.main, propagation = Propagation.SUPPORTS)
	public BoOrder getOrder() {
		return assertUnique((List<BoOrder>) orderDao.find("from BoOrder", null, 0, 1));
	}
	@Tx(datasource = DataSourceType.main, propagation = Propagation.SUPPORTS)
	public BoLeaveOrder getOrderTrigger() {
		return assertUnique((List<BoLeaveOrder>) orderTriggerDao
				.find("from BoOrderTrigger", null, 0, 1));
	}
	@Tx(datasource = DataSourceType.main, propagation = Propagation.SUPPORTS)
	public BoContract getContract() {
		return assertUnique((List<BoContract>) contractDao.find("from BoContract", null, 0, 1));
	}
	
	/*
	 * 
	 */
	@Tx(datasource = DataSourceType.main, propagation = Propagation.REQUIRED)
	public void updateOrder(BoOrder order) {
		orderDao.update(order);
	}
	@Tx(datasource = DataSourceType.main, propagation = Propagation.REQUIRED)
	public void updateOrderTrigger(BoLeaveOrder orderTrigger) {
		orderTriggerDao.update(orderTrigger);
	}
	@Tx(datasource = DataSourceType.main, propagation = Propagation.REQUIRED)
	public void updateContract(BoContract contract) {
		contractDao.update(contract);
	}
	
	/*
	 * 
	 */
	public static void main(String[] args) {
		ContextLoader loader = new ContextLoader(TestOptimisticLocking.class);
		TestOptimisticLocking inst = (TestOptimisticLocking)
				loader.getBean("testOptimisticLocking");
		
		BoOrder order = inst.getOrder();
		System.out.println(order);
		BoLeaveOrder orderTrigger = inst.getOrderTrigger();
		System.out.println(orderTrigger);
		BoContract contract = inst.getContract();
		System.out.println(contract);
		
		String[] opts = {
				"select order",
				"select orderTrigger",
				"select contract",
				"update order",
				"update orderTrigger",
				"update contract",
		};
		
		boolean exit = false;
		while (!exit) {
			switch (CommandUtils.choose(opts)) {
			case 0:
				order = inst.getOrder();
				System.out.println(order);
				break;
			case 1:
				orderTrigger = inst.getOrderTrigger();
				System.out.println(orderTrigger);
				break;
			case 2:
				contract = inst.getContract();
				System.out.println(contract);
				break;
			case 3:
				try {
					BoOrder orderCopy = order.copy();
					inst.updateOrder(orderCopy);
					System.out.println(orderCopy);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 4:
				try {
					BoLeaveOrder orderTriggerCopy = orderTrigger.copy();
					inst.updateOrderTrigger(orderTriggerCopy);
					System.out.println(orderTriggerCopy);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 5:
				try {
					BoContract contractCopy = contract.copy();
					inst.updateContract(contractCopy);
					System.out.println(contractCopy);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			default:
				exit = true;
				break;
			}
		}
	}
}

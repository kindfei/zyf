package test.db.po;

import java.math.BigDecimal;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class PositionInfo {
	private String subAcct;
	private String cusip;
	private BigDecimal cashPosition;
	private BigDecimal marginPosition;
	private BigDecimal shortPosition;
	private BigDecimal cashPosBalance;
	private BigDecimal marginPosBalance;
	private BigDecimal shortPosBalance;
	private BigDecimal netPosition;
	
	public String getSubAcct() {
		return subAcct;
	}
	public void setSubAcct(String subAcct) {
		this.subAcct = subAcct;
	}
	public String getCusip() {
		return cusip;
	}
	public void setCusip(String cusip) {
		this.cusip = cusip;
	}
	public BigDecimal getCashPosition() {
		return cashPosition;
	}
	public void setCashPosition(BigDecimal cashPosition) {
		this.cashPosition = cashPosition;
	}
	public BigDecimal getMarginPosition() {
		return marginPosition;
	}
	public void setMarginPosition(BigDecimal marginPosition) {
		this.marginPosition = marginPosition;
	}
	public BigDecimal getShortPosition() {
		return shortPosition;
	}
	public void setShortPosition(BigDecimal shortPosition) {
		this.shortPosition = shortPosition;
	}
	public BigDecimal getCashPosBalance() {
		return cashPosBalance;
	}
	public void setCashPosBalance(BigDecimal cashPosBalance) {
		this.cashPosBalance = cashPosBalance;
	}
	public BigDecimal getMarginPosBalance() {
		return marginPosBalance;
	}
	public void setMarginPosBalance(BigDecimal marginPosBalance) {
		this.marginPosBalance = marginPosBalance;
	}
	public BigDecimal getShortPosBalance() {
		return shortPosBalance;
	}
	public void setShortPosBalance(BigDecimal shortPosBalance) {
		this.shortPosBalance = shortPosBalance;
	}
	public BigDecimal getNetPosition() {
		return netPosition;
	}
	public void setNetPosition(BigDecimal netPosition) {
		this.netPosition = netPosition;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}

package fei.tools.dc.po;

// Generated Dec 17, 2010 2:03:35 PM by Hibernate Tools 3.4.0.Beta1

import java.util.Date;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * TestTblTrade generated by hbm2java
 */
@Entity
@Table(name = "TblTrade", schema= "dbo", catalog = "pbOnline")
public class TblTrade implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private TblTradeId id;
	private char active;
	private char status;
	private char state;
	private String type;
	private String topAcct;
	private String trdAcct;
	private String subAcct;
	private String area;
	private String origin;
	private String uid;
	private int countSeq;
	private String buySell;
	private double quantity;
	private double price;
	private String secId;
	private int tradeDate;
	private int settleDate;
	private String exBroker;
	private String clrBroker;
	private String fc;
	private char commCode;
	private double commission;
	private double vsPrice;
	private int vsDate;
	private char openClose;
	private double rate;
	private String currCode;
	private String frgnDepCode;
	private double netAmount;
	private char bondInd;
	private double bondInt;
	private double bondPrin;
	private double secFee;
	private char secFeeInd;
	private double nsdFee;
	private char nsdFeeInd;
	private String trailerCode;
	private String blotCode;
	private char solUnsolInd;
	private char frgnInd;
	private double optionFee;
	private double tax;
	private char preFig;
	private String clientTradeRef;
	private String text;
	private String strategy;
	private String cusip;
	private String tmsExecTime;
	private Date entryDt;
	private Date modDt;
	private Integer blockId;
	private Double netPrice;
	private String symTyp;
	private String custodian;
	private Double brkFee;
	private Double miscFee;
	private Double stampDuty;
	private String firmAcct;
	private String altSecId;
	private int fileId;
	private String party;

	public TblTrade() {
	}

	public TblTrade(TblTradeId id, char active, char status,
			char state, String type, String topAcct, String trdAcct,
			String subAcct, String area, String origin, String uid,
			int countSeq, String buySell, double quantity, double price,
			String secId, int tradeDate, int settleDate, String exBroker,
			String clrBroker, String fc, char commCode, double commission,
			double vsPrice, int vsDate, char openClose, double rate,
			String currCode, String frgnDepCode, double netAmount,
			char bondInd, double bondInt, double bondPrin, double secFee,
			char secFeeInd, double nsdFee, char nsdFeeInd, String trailerCode,
			String blotCode, char solUnsolInd, char frgnInd, double optionFee,
			double tax, char preFig, String clientTradeRef, String text,
			String strategy, Date entryDt, Date modDt, int fileId) {
		this.id = id;
		this.active = active;
		this.status = status;
		this.state = state;
		this.type = type;
		this.topAcct = topAcct;
		this.trdAcct = trdAcct;
		this.subAcct = subAcct;
		this.area = area;
		this.origin = origin;
		this.uid = uid;
		this.countSeq = countSeq;
		this.buySell = buySell;
		this.quantity = quantity;
		this.price = price;
		this.secId = secId;
		this.tradeDate = tradeDate;
		this.settleDate = settleDate;
		this.exBroker = exBroker;
		this.clrBroker = clrBroker;
		this.fc = fc;
		this.commCode = commCode;
		this.commission = commission;
		this.vsPrice = vsPrice;
		this.vsDate = vsDate;
		this.openClose = openClose;
		this.rate = rate;
		this.currCode = currCode;
		this.frgnDepCode = frgnDepCode;
		this.netAmount = netAmount;
		this.bondInd = bondInd;
		this.bondInt = bondInt;
		this.bondPrin = bondPrin;
		this.secFee = secFee;
		this.secFeeInd = secFeeInd;
		this.nsdFee = nsdFee;
		this.nsdFeeInd = nsdFeeInd;
		this.trailerCode = trailerCode;
		this.blotCode = blotCode;
		this.solUnsolInd = solUnsolInd;
		this.frgnInd = frgnInd;
		this.optionFee = optionFee;
		this.tax = tax;
		this.preFig = preFig;
		this.clientTradeRef = clientTradeRef;
		this.text = text;
		this.strategy = strategy;
		this.entryDt = entryDt;
		this.modDt = modDt;
		this.fileId = fileId;
	}

	public TblTrade(TblTradeId id, char active, char status,
			char state, String type, String topAcct, String trdAcct,
			String subAcct, String area, String origin, String uid,
			int countSeq, String buySell, double quantity, double price,
			String secId, int tradeDate, int settleDate, String exBroker,
			String clrBroker, String fc, char commCode, double commission,
			double vsPrice, int vsDate, char openClose, double rate,
			String currCode, String frgnDepCode, double netAmount,
			char bondInd, double bondInt, double bondPrin, double secFee,
			char secFeeInd, double nsdFee, char nsdFeeInd, String trailerCode,
			String blotCode, char solUnsolInd, char frgnInd, double optionFee,
			double tax, char preFig, String clientTradeRef, String text,
			String strategy, String cusip, String tmsExecTime, Date entryDt,
			Date modDt, Integer blockId, Double netPrice, String symTyp,
			String custodian, Double brkFee, Double miscFee, Double stampDuty,
			String firmAcct, String altSecId, int fileId, String party) {
		this.id = id;
		this.active = active;
		this.status = status;
		this.state = state;
		this.type = type;
		this.topAcct = topAcct;
		this.trdAcct = trdAcct;
		this.subAcct = subAcct;
		this.area = area;
		this.origin = origin;
		this.uid = uid;
		this.countSeq = countSeq;
		this.buySell = buySell;
		this.quantity = quantity;
		this.price = price;
		this.secId = secId;
		this.tradeDate = tradeDate;
		this.settleDate = settleDate;
		this.exBroker = exBroker;
		this.clrBroker = clrBroker;
		this.fc = fc;
		this.commCode = commCode;
		this.commission = commission;
		this.vsPrice = vsPrice;
		this.vsDate = vsDate;
		this.openClose = openClose;
		this.rate = rate;
		this.currCode = currCode;
		this.frgnDepCode = frgnDepCode;
		this.netAmount = netAmount;
		this.bondInd = bondInd;
		this.bondInt = bondInt;
		this.bondPrin = bondPrin;
		this.secFee = secFee;
		this.secFeeInd = secFeeInd;
		this.nsdFee = nsdFee;
		this.nsdFeeInd = nsdFeeInd;
		this.trailerCode = trailerCode;
		this.blotCode = blotCode;
		this.solUnsolInd = solUnsolInd;
		this.frgnInd = frgnInd;
		this.optionFee = optionFee;
		this.tax = tax;
		this.preFig = preFig;
		this.clientTradeRef = clientTradeRef;
		this.text = text;
		this.strategy = strategy;
		this.cusip = cusip;
		this.tmsExecTime = tmsExecTime;
		this.entryDt = entryDt;
		this.modDt = modDt;
		this.blockId = blockId;
		this.netPrice = netPrice;
		this.symTyp = symTyp;
		this.custodian = custodian;
		this.brkFee = brkFee;
		this.miscFee = miscFee;
		this.stampDuty = stampDuty;
		this.firmAcct = firmAcct;
		this.altSecId = altSecId;
		this.fileId = fileId;
		this.party = party;
	}

	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "tradeId", column = @Column(name = "tradeId", nullable = false)),
			@AttributeOverride(name = "tradeVer", column = @Column(name = "tradeVer", nullable = false)) })
	public TblTradeId getId() {
		return this.id;
	}

	public void setId(TblTradeId id) {
		this.id = id;
	}

	@Column(name = "active", nullable = false, length = 1)
	public char getActive() {
		return this.active;
	}

	public void setActive(char active) {
		this.active = active;
	}

	@Column(name = "status", nullable = false, length = 1)
	public char getStatus() {
		return this.status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	@Column(name = "state", nullable = false, length = 1)
	public char getState() {
		return this.state;
	}

	public void setState(char state) {
		this.state = state;
	}

	@Column(name = "type", nullable = false, length = 3)
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "topAcct", nullable = false, length = 10)
	public String getTopAcct() {
		return this.topAcct;
	}

	public void setTopAcct(String topAcct) {
		this.topAcct = topAcct;
	}

	@Column(name = "trdAcct", nullable = false, length = 10)
	public String getTrdAcct() {
		return this.trdAcct;
	}

	public void setTrdAcct(String trdAcct) {
		this.trdAcct = trdAcct;
	}

	@Column(name = "subAcct", nullable = false, length = 10)
	public String getSubAcct() {
		return this.subAcct;
	}

	public void setSubAcct(String subAcct) {
		this.subAcct = subAcct;
	}

	@Column(name = "area", nullable = false, length = 3)
	public String getArea() {
		return this.area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	@Column(name = "origin", nullable = false, length = 4)
	public String getOrigin() {
		return this.origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	@Column(name = "uid", nullable = false, length = 10)
	public String getUid() {
		return this.uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	@Column(name = "countSeq", nullable = false)
	public int getCountSeq() {
		return this.countSeq;
	}

	public void setCountSeq(int countSeq) {
		this.countSeq = countSeq;
	}

	@Column(name = "buySell", nullable = false, length = 3)
	public String getBuySell() {
		return this.buySell;
	}

	public void setBuySell(String buySell) {
		this.buySell = buySell;
	}

	@Column(name = "quantity", nullable = false, precision = 15, scale = 0)
	public double getQuantity() {
		return this.quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	@Column(name = "price", nullable = false, precision = 15, scale = 0)
	public double getPrice() {
		return this.price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Column(name = "secId", nullable = false, length = 21)
	public String getSecId() {
		return this.secId;
	}

	public void setSecId(String secId) {
		this.secId = secId;
	}

	@Column(name = "tradeDate", nullable = false)
	public int getTradeDate() {
		return this.tradeDate;
	}

	public void setTradeDate(int tradeDate) {
		this.tradeDate = tradeDate;
	}

	@Column(name = "settleDate", nullable = false)
	public int getSettleDate() {
		return this.settleDate;
	}

	public void setSettleDate(int settleDate) {
		this.settleDate = settleDate;
	}

	@Column(name = "exBroker", nullable = false, length = 32)
	public String getExBroker() {
		return this.exBroker;
	}

	public void setExBroker(String exBroker) {
		this.exBroker = exBroker;
	}

	@Column(name = "clrBroker", nullable = false, length = 32)
	public String getClrBroker() {
		return this.clrBroker;
	}

	public void setClrBroker(String clrBroker) {
		this.clrBroker = clrBroker;
	}

	@Column(name = "fc", nullable = false, length = 3)
	public String getFc() {
		return this.fc;
	}

	public void setFc(String fc) {
		this.fc = fc;
	}

	@Column(name = "commCode", nullable = false, length = 1)
	public char getCommCode() {
		return this.commCode;
	}

	public void setCommCode(char commCode) {
		this.commCode = commCode;
	}

	@Column(name = "commission", nullable = false, precision = 15, scale = 0)
	public double getCommission() {
		return this.commission;
	}

	public void setCommission(double commission) {
		this.commission = commission;
	}

	@Column(name = "vsPrice", nullable = false, precision = 15, scale = 0)
	public double getVsPrice() {
		return this.vsPrice;
	}

	public void setVsPrice(double vsPrice) {
		this.vsPrice = vsPrice;
	}

	@Column(name = "vsDate", nullable = false)
	public int getVsDate() {
		return this.vsDate;
	}

	public void setVsDate(int vsDate) {
		this.vsDate = vsDate;
	}

	@Column(name = "openClose", nullable = false, length = 1)
	public char getOpenClose() {
		return this.openClose;
	}

	public void setOpenClose(char openClose) {
		this.openClose = openClose;
	}

	@Column(name = "rate", nullable = false, precision = 15, scale = 0)
	public double getRate() {
		return this.rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	@Column(name = "currCode", nullable = false, length = 3)
	public String getCurrCode() {
		return this.currCode;
	}

	public void setCurrCode(String currCode) {
		this.currCode = currCode;
	}

	@Column(name = "frgnDepCode", nullable = false, length = 2)
	public String getFrgnDepCode() {
		return this.frgnDepCode;
	}

	public void setFrgnDepCode(String frgnDepCode) {
		this.frgnDepCode = frgnDepCode;
	}

	@Column(name = "netAmount", nullable = false, precision = 15, scale = 0)
	public double getNetAmount() {
		return this.netAmount;
	}

	public void setNetAmount(double netAmount) {
		this.netAmount = netAmount;
	}

	@Column(name = "bondInd", nullable = false, length = 1)
	public char getBondInd() {
		return this.bondInd;
	}

	public void setBondInd(char bondInd) {
		this.bondInd = bondInd;
	}

	@Column(name = "bondInt", nullable = false, precision = 15, scale = 0)
	public double getBondInt() {
		return this.bondInt;
	}

	public void setBondInt(double bondInt) {
		this.bondInt = bondInt;
	}

	@Column(name = "bondPrin", nullable = false, precision = 15, scale = 0)
	public double getBondPrin() {
		return this.bondPrin;
	}

	public void setBondPrin(double bondPrin) {
		this.bondPrin = bondPrin;
	}

	@Column(name = "secFee", nullable = false, precision = 15, scale = 0)
	public double getSecFee() {
		return this.secFee;
	}

	public void setSecFee(double secFee) {
		this.secFee = secFee;
	}

	@Column(name = "secFeeInd", nullable = false, length = 1)
	public char getSecFeeInd() {
		return this.secFeeInd;
	}

	public void setSecFeeInd(char secFeeInd) {
		this.secFeeInd = secFeeInd;
	}

	@Column(name = "nsdFee", nullable = false, precision = 15, scale = 0)
	public double getNsdFee() {
		return this.nsdFee;
	}

	public void setNsdFee(double nsdFee) {
		this.nsdFee = nsdFee;
	}

	@Column(name = "nsdFeeInd", nullable = false, length = 1)
	public char getNsdFeeInd() {
		return this.nsdFeeInd;
	}

	public void setNsdFeeInd(char nsdFeeInd) {
		this.nsdFeeInd = nsdFeeInd;
	}

	@Column(name = "trailerCode", nullable = false, length = 2)
	public String getTrailerCode() {
		return this.trailerCode;
	}

	public void setTrailerCode(String trailerCode) {
		this.trailerCode = trailerCode;
	}

	@Column(name = "blotCode", nullable = false, length = 2)
	public String getBlotCode() {
		return this.blotCode;
	}

	public void setBlotCode(String blotCode) {
		this.blotCode = blotCode;
	}

	@Column(name = "solUnsolInd", nullable = false, length = 1)
	public char getSolUnsolInd() {
		return this.solUnsolInd;
	}

	public void setSolUnsolInd(char solUnsolInd) {
		this.solUnsolInd = solUnsolInd;
	}

	@Column(name = "frgnInd", nullable = false, length = 1)
	public char getFrgnInd() {
		return this.frgnInd;
	}

	public void setFrgnInd(char frgnInd) {
		this.frgnInd = frgnInd;
	}

	@Column(name = "optionFee", nullable = false, precision = 15, scale = 0)
	public double getOptionFee() {
		return this.optionFee;
	}

	public void setOptionFee(double optionFee) {
		this.optionFee = optionFee;
	}

	@Column(name = "tax", nullable = false, precision = 15, scale = 0)
	public double getTax() {
		return this.tax;
	}

	public void setTax(double tax) {
		this.tax = tax;
	}

	@Column(name = "preFig", nullable = false, length = 1)
	public char getPreFig() {
		return this.preFig;
	}

	public void setPreFig(char preFig) {
		this.preFig = preFig;
	}

	@Column(name = "clientTradeRef", nullable = false, length = 30)
	public String getClientTradeRef() {
		return this.clientTradeRef;
	}

	public void setClientTradeRef(String clientTradeRef) {
		this.clientTradeRef = clientTradeRef;
	}

	@Column(name = "text", nullable = false, length = 80)
	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Column(name = "strategy", nullable = false, length = 30)
	public String getStrategy() {
		return this.strategy;
	}

	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}

	@Column(name = "cusip", length = 13)
	public String getCusip() {
		return this.cusip;
	}

	public void setCusip(String cusip) {
		this.cusip = cusip;
	}

	@Column(name = "tmsExecTime", length = 30)
	public String getTmsExecTime() {
		return this.tmsExecTime;
	}

	public void setTmsExecTime(String tmsExecTime) {
		this.tmsExecTime = tmsExecTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "entryDt", nullable = false, length = 23)
	public Date getEntryDt() {
		return this.entryDt;
	}

	public void setEntryDt(Date entryDt) {
		this.entryDt = entryDt;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modDt", nullable = false, length = 23)
	public Date getModDt() {
		return this.modDt;
	}

	public void setModDt(Date modDt) {
		this.modDt = modDt;
	}

	@Column(name = "blockId")
	public Integer getBlockId() {
		return this.blockId;
	}

	public void setBlockId(Integer blockId) {
		this.blockId = blockId;
	}

	@Column(name = "netPrice", precision = 15, scale = 0)
	public Double getNetPrice() {
		return this.netPrice;
	}

	public void setNetPrice(Double netPrice) {
		this.netPrice = netPrice;
	}

	@Column(name = "symTyp", length = 20)
	public String getSymTyp() {
		return this.symTyp;
	}

	public void setSymTyp(String symTyp) {
		this.symTyp = symTyp;
	}

	@Column(name = "custodian", length = 10)
	public String getCustodian() {
		return this.custodian;
	}

	public void setCustodian(String custodian) {
		this.custodian = custodian;
	}

	@Column(name = "brkFee", precision = 15, scale = 0)
	public Double getBrkFee() {
		return this.brkFee;
	}

	public void setBrkFee(Double brkFee) {
		this.brkFee = brkFee;
	}

	@Column(name = "miscFee", precision = 15, scale = 0)
	public Double getMiscFee() {
		return this.miscFee;
	}

	public void setMiscFee(Double miscFee) {
		this.miscFee = miscFee;
	}

	@Column(name = "stampDuty", precision = 15, scale = 0)
	public Double getStampDuty() {
		return this.stampDuty;
	}

	public void setStampDuty(Double stampDuty) {
		this.stampDuty = stampDuty;
	}

	@Column(name = "firmAcct", length = 12)
	public String getFirmAcct() {
		return this.firmAcct;
	}

	public void setFirmAcct(String firmAcct) {
		this.firmAcct = firmAcct;
	}

	@Column(name = "altSecId", length = 60)
	public String getAltSecId() {
		return this.altSecId;
	}

	public void setAltSecId(String altSecId) {
		this.altSecId = altSecId;
	}

	@Column(name = "fileId", nullable = false)
	public int getFileId() {
		return this.fileId;
	}

	public void setFileId(int fileId) {
		this.fileId = fileId;
	}

	@Column(name = "party", length = 20)
	public String getParty() {
		return this.party;
	}

	public void setParty(String party) {
		this.party = party;
	}

}

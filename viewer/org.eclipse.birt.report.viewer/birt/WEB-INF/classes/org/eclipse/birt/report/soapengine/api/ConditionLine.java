package org.eclipse.birt.report.soapengine.api;

import java.io.Serializable;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ConditionLine")
@XmlAccessorType(XmlAccessType.NONE)
public class ConditionLine implements Serializable {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "reportParameterList")
	private ReportParameterList reportParameterList;
	@XmlElement(name = "logicOp")
	private String logicOp;
	@XmlElement(name = "hasLeftBracket")
	private boolean hasLeftBracket;
	@XmlElement(name = "hasNot")
	private boolean hasNot;
	@XmlElement(name = "row")
	private String row;
	@XmlElement(name = "operator")
	private String operator;
	@XmlElement(name = "value1")
	private String value1;
	@XmlElement(name = "value2")
	private String value2;
	@XmlElement(name = "hasRightBracket")
	private boolean hasRightBracket;

	public ConditionLine() {
	}

	public ConditionLine(ReportParameterList reportParameterList, String logicOp, boolean hasLeftBracket,
			boolean hasNot, String row, String operator, String value1, String value2, boolean hasRightBracket) {
		this.reportParameterList = reportParameterList;
		this.logicOp = logicOp;
		this.hasLeftBracket = hasLeftBracket;
		this.hasNot = hasNot;
		this.row = row;
		this.operator = operator;
		this.value1 = value1;
		this.value2 = value2;
		this.hasRightBracket = hasRightBracket;
	}

	public ReportParameterList getReportParameterList() {
		return reportParameterList;
	}

	public void setReportParameterList(ReportParameterList reportParameterList) {
		this.reportParameterList = reportParameterList;
	}

	public String getLogicOp() {
		return logicOp;
	}

	public void setLogicOp(String logicOp) {
		this.logicOp = logicOp;
	}

	public boolean isHasLeftBracket() {
		return hasLeftBracket;
	}

	public void setHasLeftBracket(boolean hasLeftBracket) {
		this.hasLeftBracket = hasLeftBracket;
	}

	public boolean isHasNot() {
		return hasNot;
	}

	public void setHasNot(boolean hasNot) {
		this.hasNot = hasNot;
	}

	public String getRow() {
		return row;
	}

	public void setRow(String row) {
		this.row = row;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getValue1() {
		return value1;
	}

	public void setValue1(String value1) {
		this.value1 = value1;
	}

	public String getValue2() {
		return value2;
	}

	public void setValue2(String value2) {
		this.value2 = value2;
	}

	public boolean isHasRightBracket() {
		return hasRightBracket;
	}

	public void setHasRightBracket(boolean hasRightBracket) {
		this.hasRightBracket = hasRightBracket;
	}

	private Object __equalsCalc = null;

	@Override
	public synchronized boolean equals(Object obj) {
		if (!(obj instanceof ConditionLine))
			return false;
		if (this == obj)
			return true;
		if (__equalsCalc != null)
			return (__equalsCalc == obj);
		__equalsCalc = obj;
		ConditionLine other = (ConditionLine) obj;
		boolean _equals = (reportParameterList == null ? other.reportParameterList == null
				: reportParameterList.equals(other.reportParameterList))
				&& (logicOp == null ? other.logicOp == null : logicOp.equals(other.logicOp))
				&& hasLeftBracket == other.hasLeftBracket && hasNot == other.hasNot
				&& (row == null ? other.row == null : row.equals(other.row))
				&& (operator == null ? other.operator == null : operator.equals(other.operator))
				&& (value1 == null ? other.value1 == null : value1.equals(other.value1))
				&& (value2 == null ? other.value2 == null : value2.equals(other.value2))
				&& hasRightBracket == other.hasRightBracket;
		__equalsCalc = null;
		return _equals;
	}

	private boolean __hashCodeCalc = false;

	@Override
	public synchronized int hashCode() {
		if (__hashCodeCalc)
			return 0;
		__hashCodeCalc = true;
		int _hashCode = 1;
		if (reportParameterList != null)
			_hashCode += reportParameterList.hashCode();
		if (logicOp != null)
			_hashCode += logicOp.hashCode();
		_hashCode += Boolean.valueOf(hasLeftBracket).hashCode();
		_hashCode += Boolean.valueOf(hasNot).hashCode();
		if (row != null)
			_hashCode += row.hashCode();
		if (operator != null)
			_hashCode += operator.hashCode();
		if (value1 != null)
			_hashCode += value1.hashCode();
		if (value2 != null)
			_hashCode += value2.hashCode();
		_hashCode += Boolean.valueOf(hasRightBracket).hashCode();
		__hashCodeCalc = false;
		return _hashCode;
	}
}

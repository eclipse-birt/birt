package org.eclipse.birt.report.soapengine.api;

import java.io.Serializable;
import java.util.Arrays;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ConditionLineList")
@XmlAccessorType(XmlAccessType.NONE)
public class ConditionLineList implements Serializable {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "conditionLine")
	private ConditionLine[] conditionLine;

	public ConditionLineList() {
	}

	public ConditionLineList(ConditionLine[] conditionLine) {
		this.conditionLine = conditionLine;
	}

	public ConditionLine[] getConditionLine() {
		return conditionLine;
	}

	public void setConditionLine(ConditionLine[] conditionLine) {
		this.conditionLine = conditionLine;
	}

	public ConditionLine getConditionLine(int i) {
		return this.conditionLine[i];
	}

	public void setConditionLine(int i, ConditionLine value) {
		this.conditionLine[i] = value;
	}

	private Object __equalsCalc = null;

	@Override
	public synchronized boolean equals(Object obj) {
		if (!(obj instanceof ConditionLineList))
			return false;
		if (this == obj)
			return true;
		if (__equalsCalc != null)
			return (__equalsCalc == obj);
		__equalsCalc = obj;
		ConditionLineList other = (ConditionLineList) obj;
		boolean _equals = (conditionLine == null ? other.conditionLine == null
				: Arrays.equals(conditionLine, other.conditionLine));
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
		if (conditionLine != null) {
			for (ConditionLine obj : conditionLine) {
				if (obj != null)
					_hashCode += obj.hashCode();
			}
		}
		__hashCodeCalc = false;
		return _hashCode;
	}
}

package org.eclipse.birt.report.soapengine.api;

import java.io.Serializable;
import java.util.Objects;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

/**
 * Represents an aggregate definition in a BIRT report.
 */
@XmlRootElement(name = "AggregateDefinition")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AggregateDefinition", propOrder = { "func", "subTotal", "grandTotal", "intParameter", "sortDir" })
public class AggregateDefinition implements Serializable {

	private static final long serialVersionUID = 1L;

	@XmlElement(name = "Func", required = true)
	private String func;

	@XmlElement(name = "SubTotal")
	private AggregateSetting subTotal;

	@XmlElement(name = "GrandTotal")
	private AggregateSetting grandTotal;

	@XmlElement(name = "IntParameter")
	private Integer intParameter;

	@XmlElement(name = "SortDir")
	private String sortDir;

	public AggregateDefinition() {
	}

	public AggregateDefinition(String func, AggregateSetting subTotal, AggregateSetting grandTotal,
			Integer intParameter, String sortDir) {
		this.func = func;
		this.subTotal = subTotal;
		this.grandTotal = grandTotal;
		this.intParameter = intParameter;
		this.sortDir = sortDir;
	}

	public String getFunc() {
		return func;
	}

	public void setFunc(String func) {
		this.func = func;
	}

	public AggregateSetting getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(AggregateSetting subTotal) {
		this.subTotal = subTotal;
	}

	public AggregateSetting getGrandTotal() {
		return grandTotal;
	}

	public void setGrandTotal(AggregateSetting grandTotal) {
		this.grandTotal = grandTotal;
	}

	public Integer getIntParameter() {
		return intParameter;
	}

	public void setIntParameter(Integer intParameter) {
		this.intParameter = intParameter;
	}

	public String getSortDir() {
		return sortDir;
	}

	public void setSortDir(String sortDir) {
		this.sortDir = sortDir;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof AggregateDefinition))
			return false;
		AggregateDefinition that = (AggregateDefinition) o;
		return Objects.equals(func, that.func) && Objects.equals(subTotal, that.subTotal)
				&& Objects.equals(grandTotal, that.grandTotal) && Objects.equals(intParameter, that.intParameter)
				&& Objects.equals(sortDir, that.sortDir);
	}

	@Override
	public int hashCode() {
		return Objects.hash(func, subTotal, grandTotal, intParameter, sortDir);
	}

	@Override
	public String toString() {
		return "AggregateDefinition{" + "func='" + func + '\'' + ", subTotal=" + subTotal + ", grandTotal=" + grandTotal
				+ ", intParameter=" + intParameter + ", sortDir='" + sortDir + '\'' + '}';
	}
}

/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/
/**
 * Filter.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Filter")
@XmlAccessorType(XmlAccessType.NONE)
public class Filter implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "Type")
	private org.eclipse.birt.report.soapengine.api.FilterType type;
	@XmlElement(name = "Expression")
	private org.eclipse.birt.report.soapengine.api.FilterExpression expression;
	@XmlElement(name = "ReportParameterList")
	private org.eclipse.birt.report.soapengine.api.ReportParameterList reportParameterList;
	@XmlElement(name = "ConditionLineList")
	private org.eclipse.birt.report.soapengine.api.ConditionLineList conditionLineList;

	public Filter() {
	}

	public Filter(org.eclipse.birt.report.soapengine.api.FilterType type,
			org.eclipse.birt.report.soapengine.api.FilterExpression expression,
			org.eclipse.birt.report.soapengine.api.ReportParameterList reportParameterList,
			org.eclipse.birt.report.soapengine.api.ConditionLineList conditionLineList) {
		this.type = type;
		this.expression = expression;
		this.reportParameterList = reportParameterList;
		this.conditionLineList = conditionLineList;
	}

	/**
	 * Gets the type value for this Filter.
	 *
	 * @return type
	 */
	public org.eclipse.birt.report.soapengine.api.FilterType getType() {
		return type;
	}

	/**
	 * Sets the type value for this Filter.
	 *
	 * @param type
	 */
	public void setType(org.eclipse.birt.report.soapengine.api.FilterType type) {
		this.type = type;
	}

	/**
	 * Gets the expression value for this Filter.
	 *
	 * @return expression
	 */
	public org.eclipse.birt.report.soapengine.api.FilterExpression getExpression() {
		return expression;
	}

	/**
	 * Sets the expression value for this Filter.
	 *
	 * @param expression
	 */
	public void setExpression(org.eclipse.birt.report.soapengine.api.FilterExpression expression) {
		this.expression = expression;
	}

	/**
	 * Gets the reportParameterList value for this Filter.
	 *
	 * @return reportParameterList
	 */
	public org.eclipse.birt.report.soapengine.api.ReportParameterList getReportParameterList() {
		return reportParameterList;
	}

	/**
	 * Sets the reportParameterList value for this Filter.
	 *
	 * @param reportParameterList
	 */
	public void setReportParameterList(org.eclipse.birt.report.soapengine.api.ReportParameterList reportParameterList) {
		this.reportParameterList = reportParameterList;
	}

	/**
	 * Gets the conditionLineList value for this Filter.
	 *
	 * @return conditionLineList
	 */
	public org.eclipse.birt.report.soapengine.api.ConditionLineList getConditionLineList() {
		return conditionLineList;
	}

	/**
	 * Sets the conditionLineList value for this Filter.
	 *
	 * @param conditionLineList
	 */
	public void setConditionLineList(org.eclipse.birt.report.soapengine.api.ConditionLineList conditionLineList) {
		this.conditionLineList = conditionLineList;
	}

	private java.lang.Object __equalsCalc = null;

	@Override
	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof Filter)) {
			return false;
		}
		Filter other = (Filter) obj;
		if (obj == null) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (__equalsCalc != null) {
			return (__equalsCalc == obj);
		}
		__equalsCalc = obj;
		boolean _equals;
		_equals = true
				&& ((this.type == null && other.getType() == null)
						|| (this.type != null && this.type.equals(other.getType())))
				&& ((this.expression == null && other.getExpression() == null)
						|| (this.expression != null && this.expression.equals(other.getExpression())))
				&& ((this.reportParameterList == null && other.getReportParameterList() == null)
						|| (this.reportParameterList != null
								&& this.reportParameterList.equals(other.getReportParameterList())))
				&& ((this.conditionLineList == null && other.getConditionLineList() == null)
						|| (this.conditionLineList != null
								&& this.conditionLineList.equals(other.getConditionLineList())));
		__equalsCalc = null;
		return _equals;
	}

	private boolean __hashCodeCalc = false;

	@Override
	public synchronized int hashCode() {
		if (__hashCodeCalc) {
			return 0;
		}
		__hashCodeCalc = true;
		int _hashCode = 1;
		if (getType() != null) {
			_hashCode += getType().hashCode();
		}
		if (getExpression() != null) {
			_hashCode += getExpression().hashCode();
		}
		if (getReportParameterList() != null) {
			_hashCode += getReportParameterList().hashCode();
		}
		if (getConditionLineList() != null) {
			_hashCode += getConditionLineList().hashCode();
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	}

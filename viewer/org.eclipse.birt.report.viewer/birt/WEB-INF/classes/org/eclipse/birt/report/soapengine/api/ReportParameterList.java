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
 * ReportParameterList.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ReportParameterList")
@XmlAccessorType(XmlAccessType.NONE)
public class ReportParameterList implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "ReportParameter")
	private org.eclipse.birt.report.soapengine.api.ReportParameter[] reportParameter;

	public ReportParameterList() {
	}

	public ReportParameterList(org.eclipse.birt.report.soapengine.api.ReportParameter[] reportParameter) {
		this.reportParameter = reportParameter;
	}

	/**
	 * Gets the reportParameter value for this ReportParameterList.
	 *
	 * @return reportParameter
	 */
	public org.eclipse.birt.report.soapengine.api.ReportParameter[] getReportParameter() {
		return reportParameter;
	}

	/**
	 * Sets the reportParameter value for this ReportParameterList.
	 *
	 * @param reportParameter
	 */
	public void setReportParameter(org.eclipse.birt.report.soapengine.api.ReportParameter[] reportParameter) {
		this.reportParameter = reportParameter;
	}

	public org.eclipse.birt.report.soapengine.api.ReportParameter getReportParameter(int i) {
		return this.reportParameter[i];
	}

	public void setReportParameter(int i, org.eclipse.birt.report.soapengine.api.ReportParameter _value) {
		this.reportParameter[i] = _value;
	}

	private java.lang.Object __equalsCalc = null;

	@Override
	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof ReportParameterList)) {
			return false;
		}
		ReportParameterList other = (ReportParameterList) obj;
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
		_equals = true && ((this.reportParameter == null && other.getReportParameter() == null)
				|| (this.reportParameter != null
						&& java.util.Arrays.equals(this.reportParameter, other.getReportParameter())));
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
		if (getReportParameter() != null) {
			for (int i = 0; i < java.lang.reflect.Array.getLength(getReportParameter()); i++) {
				java.lang.Object obj = java.lang.reflect.Array.get(getReportParameter(), i);
				if (obj != null && !obj.getClass().isArray()) {
					_hashCode += obj.hashCode();
				}
			}
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	}

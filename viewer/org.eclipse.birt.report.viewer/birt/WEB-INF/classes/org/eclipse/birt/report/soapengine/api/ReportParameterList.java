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

public class ReportParameterList implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
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

	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof ReportParameterList))
			return false;
		ReportParameterList other = (ReportParameterList) obj;
		if (obj == null)
			return false;
		if (this == obj)
			return true;
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

	// Type metadata
	private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(
			ReportParameterList.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ReportParameterList"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("reportParameter");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ReportParameter"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ReportParameter"));
		elemField.setNillable(false);
		elemField.setMaxOccursUnbounded(true);
		typeDesc.addFieldDesc(elemField);
	}

	/**
	 * Return type metadata object
	 */
	public static org.apache.axis.description.TypeDesc getTypeDesc() {
		return typeDesc;
	}

	/**
	 * Get Custom Serializer
	 */
	public static org.apache.axis.encoding.Serializer getSerializer(java.lang.String mechType,
			java.lang.Class _javaType, javax.xml.namespace.QName _xmlType) {
		return new org.apache.axis.encoding.ser.BeanSerializer(_javaType, _xmlType, typeDesc);
	}

	/**
	 * Get Custom Deserializer
	 */
	public static org.apache.axis.encoding.Deserializer getDeserializer(java.lang.String mechType,
			java.lang.Class _javaType, javax.xml.namespace.QName _xmlType) {
		return new org.apache.axis.encoding.ser.BeanDeserializer(_javaType, _xmlType, typeDesc);
	}

}

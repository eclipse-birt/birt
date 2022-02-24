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
 * FormatRuleSet.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

public class FormatRuleSet implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private org.eclipse.birt.report.soapengine.api.FormatRule[] formatRule;

	public FormatRuleSet() {
	}

	public FormatRuleSet(org.eclipse.birt.report.soapengine.api.FormatRule[] formatRule) {
		this.formatRule = formatRule;
	}

	/**
	 * Gets the formatRule value for this FormatRuleSet.
	 *
	 * @return formatRule
	 */
	public org.eclipse.birt.report.soapengine.api.FormatRule[] getFormatRule() {
		return formatRule;
	}

	/**
	 * Sets the formatRule value for this FormatRuleSet.
	 *
	 * @param formatRule
	 */
	public void setFormatRule(org.eclipse.birt.report.soapengine.api.FormatRule[] formatRule) {
		this.formatRule = formatRule;
	}

	public org.eclipse.birt.report.soapengine.api.FormatRule getFormatRule(int i) {
		return this.formatRule[i];
	}

	public void setFormatRule(int i, org.eclipse.birt.report.soapengine.api.FormatRule _value) {
		this.formatRule[i] = _value;
	}

	private java.lang.Object __equalsCalc = null;

	@Override
	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof FormatRuleSet)) {
			return false;
		}
		FormatRuleSet other = (FormatRuleSet) obj;
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
		_equals = true && ((this.formatRule == null && other.getFormatRule() == null)
				|| (this.formatRule != null && java.util.Arrays.equals(this.formatRule, other.getFormatRule())));
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
		if (getFormatRule() != null) {
			for (int i = 0; i < java.lang.reflect.Array.getLength(getFormatRule()); i++) {
				java.lang.Object obj = java.lang.reflect.Array.get(getFormatRule(), i);
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
			FormatRuleSet.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "FormatRuleSet"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("formatRule");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "FormatRule"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "FormatRule"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
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

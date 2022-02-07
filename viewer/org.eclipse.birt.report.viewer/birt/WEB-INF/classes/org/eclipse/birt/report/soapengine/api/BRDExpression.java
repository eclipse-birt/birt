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
 * BRDExpression.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

public class BRDExpression implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private java.lang.String expression;
	private java.lang.Boolean isValid;
	private java.lang.String parserError;

	public BRDExpression() {
	}

	public BRDExpression(java.lang.String expression, java.lang.Boolean isValid, java.lang.String parserError) {
		this.expression = expression;
		this.isValid = isValid;
		this.parserError = parserError;
	}

	/**
	 * Gets the expression value for this BRDExpression.
	 * 
	 * @return expression
	 */
	public java.lang.String getExpression() {
		return expression;
	}

	/**
	 * Sets the expression value for this BRDExpression.
	 * 
	 * @param expression
	 */
	public void setExpression(java.lang.String expression) {
		this.expression = expression;
	}

	/**
	 * Gets the isValid value for this BRDExpression.
	 * 
	 * @return isValid
	 */
	public java.lang.Boolean getIsValid() {
		return isValid;
	}

	/**
	 * Sets the isValid value for this BRDExpression.
	 * 
	 * @param isValid
	 */
	public void setIsValid(java.lang.Boolean isValid) {
		this.isValid = isValid;
	}

	/**
	 * Gets the parserError value for this BRDExpression.
	 * 
	 * @return parserError
	 */
	public java.lang.String getParserError() {
		return parserError;
	}

	/**
	 * Sets the parserError value for this BRDExpression.
	 * 
	 * @param parserError
	 */
	public void setParserError(java.lang.String parserError) {
		this.parserError = parserError;
	}

	private java.lang.Object __equalsCalc = null;

	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof BRDExpression))
			return false;
		BRDExpression other = (BRDExpression) obj;
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (__equalsCalc != null) {
			return (__equalsCalc == obj);
		}
		__equalsCalc = obj;
		boolean _equals;
		_equals = true
				&& ((this.expression == null && other.getExpression() == null)
						|| (this.expression != null && this.expression.equals(other.getExpression())))
				&& ((this.isValid == null && other.getIsValid() == null)
						|| (this.isValid != null && this.isValid.equals(other.getIsValid())))
				&& ((this.parserError == null && other.getParserError() == null)
						|| (this.parserError != null && this.parserError.equals(other.getParserError())));
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
		if (getExpression() != null) {
			_hashCode += getExpression().hashCode();
		}
		if (getIsValid() != null) {
			_hashCode += getIsValid().hashCode();
		}
		if (getParserError() != null) {
			_hashCode += getParserError().hashCode();
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	// Type metadata
	private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(
			BRDExpression.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "BRDExpression"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("expression");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Expression"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("isValid");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "IsValid"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("parserError");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ParserError"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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

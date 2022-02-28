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
 * JoinCondition.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

public class JoinCondition implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private java.lang.String leftExpr;
	private java.lang.String operator;
	private java.lang.String rightExpr;

	public JoinCondition() {
	}

	public JoinCondition(java.lang.String leftExpr, java.lang.String operator, java.lang.String rightExpr) {
		this.leftExpr = leftExpr;
		this.operator = operator;
		this.rightExpr = rightExpr;
	}

	/**
	 * Gets the leftExpr value for this JoinCondition.
	 *
	 * @return leftExpr
	 */
	public java.lang.String getLeftExpr() {
		return leftExpr;
	}

	/**
	 * Sets the leftExpr value for this JoinCondition.
	 *
	 * @param leftExpr
	 */
	public void setLeftExpr(java.lang.String leftExpr) {
		this.leftExpr = leftExpr;
	}

	/**
	 * Gets the operator value for this JoinCondition.
	 *
	 * @return operator
	 */
	public java.lang.String getOperator() {
		return operator;
	}

	/**
	 * Sets the operator value for this JoinCondition.
	 *
	 * @param operator
	 */
	public void setOperator(java.lang.String operator) {
		this.operator = operator;
	}

	/**
	 * Gets the rightExpr value for this JoinCondition.
	 *
	 * @return rightExpr
	 */
	public java.lang.String getRightExpr() {
		return rightExpr;
	}

	/**
	 * Sets the rightExpr value for this JoinCondition.
	 *
	 * @param rightExpr
	 */
	public void setRightExpr(java.lang.String rightExpr) {
		this.rightExpr = rightExpr;
	}

	private java.lang.Object __equalsCalc = null;

	@Override
	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof JoinCondition)) {
			return false;
		}
		JoinCondition other = (JoinCondition) obj;
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
				&& ((this.leftExpr == null && other.getLeftExpr() == null)
						|| (this.leftExpr != null && this.leftExpr.equals(other.getLeftExpr())))
				&& ((this.operator == null && other.getOperator() == null)
						|| (this.operator != null && this.operator.equals(other.getOperator())))
				&& ((this.rightExpr == null && other.getRightExpr() == null)
						|| (this.rightExpr != null && this.rightExpr.equals(other.getRightExpr())));
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
		if (getLeftExpr() != null) {
			_hashCode += getLeftExpr().hashCode();
		}
		if (getOperator() != null) {
			_hashCode += getOperator().hashCode();
		}
		if (getRightExpr() != null) {
			_hashCode += getRightExpr().hashCode();
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	// Type metadata
	private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(
			JoinCondition.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "JoinCondition"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("leftExpr");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "LeftExpr"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("operator");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Operator"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("rightExpr");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "RightExpr"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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

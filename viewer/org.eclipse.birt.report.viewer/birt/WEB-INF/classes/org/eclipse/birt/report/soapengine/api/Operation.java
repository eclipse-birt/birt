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
 * Operation.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

public class Operation implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private org.eclipse.birt.report.soapengine.api.ReportId target;
	private java.lang.String operator;
	private org.eclipse.birt.report.soapengine.api.Oprand[] oprand;
	private org.eclipse.birt.report.soapengine.api.Data data;

	public Operation() {
	}

	public Operation(org.eclipse.birt.report.soapengine.api.ReportId target, java.lang.String operator,
			org.eclipse.birt.report.soapengine.api.Oprand[] oprand, org.eclipse.birt.report.soapengine.api.Data data) {
		this.target = target;
		this.operator = operator;
		this.oprand = oprand;
		this.data = data;
	}

	/**
	 * Gets the target value for this Operation.
	 * 
	 * @return target
	 */
	public org.eclipse.birt.report.soapengine.api.ReportId getTarget() {
		return target;
	}

	/**
	 * Sets the target value for this Operation.
	 * 
	 * @param target
	 */
	public void setTarget(org.eclipse.birt.report.soapengine.api.ReportId target) {
		this.target = target;
	}

	/**
	 * Gets the operator value for this Operation.
	 * 
	 * @return operator
	 */
	public java.lang.String getOperator() {
		return operator;
	}

	/**
	 * Sets the operator value for this Operation.
	 * 
	 * @param operator
	 */
	public void setOperator(java.lang.String operator) {
		this.operator = operator;
	}

	/**
	 * Gets the oprand value for this Operation.
	 * 
	 * @return oprand
	 */
	public org.eclipse.birt.report.soapengine.api.Oprand[] getOprand() {
		return oprand;
	}

	/**
	 * Sets the oprand value for this Operation.
	 * 
	 * @param oprand
	 */
	public void setOprand(org.eclipse.birt.report.soapengine.api.Oprand[] oprand) {
		this.oprand = oprand;
	}

	public org.eclipse.birt.report.soapengine.api.Oprand getOprand(int i) {
		return this.oprand[i];
	}

	public void setOprand(int i, org.eclipse.birt.report.soapengine.api.Oprand _value) {
		this.oprand[i] = _value;
	}

	/**
	 * Gets the data value for this Operation.
	 * 
	 * @return data
	 */
	public org.eclipse.birt.report.soapengine.api.Data getData() {
		return data;
	}

	/**
	 * Sets the data value for this Operation.
	 * 
	 * @param data
	 */
	public void setData(org.eclipse.birt.report.soapengine.api.Data data) {
		this.data = data;
	}

	private java.lang.Object __equalsCalc = null;

	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof Operation))
			return false;
		Operation other = (Operation) obj;
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
				&& ((this.target == null && other.getTarget() == null)
						|| (this.target != null && this.target.equals(other.getTarget())))
				&& ((this.operator == null && other.getOperator() == null)
						|| (this.operator != null && this.operator.equals(other.getOperator())))
				&& ((this.oprand == null && other.getOprand() == null)
						|| (this.oprand != null && java.util.Arrays.equals(this.oprand, other.getOprand())))
				&& ((this.data == null && other.getData() == null)
						|| (this.data != null && this.data.equals(other.getData())));
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
		if (getTarget() != null) {
			_hashCode += getTarget().hashCode();
		}
		if (getOperator() != null) {
			_hashCode += getOperator().hashCode();
		}
		if (getOprand() != null) {
			for (int i = 0; i < java.lang.reflect.Array.getLength(getOprand()); i++) {
				java.lang.Object obj = java.lang.reflect.Array.get(getOprand(), i);
				if (obj != null && !obj.getClass().isArray()) {
					_hashCode += obj.hashCode();
				}
			}
		}
		if (getData() != null) {
			_hashCode += getData().hashCode();
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	// Type metadata
	private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(
			Operation.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Operation"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("target");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Target"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ReportId"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("operator");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Operator"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("oprand");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Oprand"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Oprand"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		elemField.setMaxOccursUnbounded(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("data");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Data"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Data"));
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

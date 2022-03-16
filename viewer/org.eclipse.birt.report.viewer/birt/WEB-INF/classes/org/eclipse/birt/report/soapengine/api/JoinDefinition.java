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
 * JoinDefinition.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

public class JoinDefinition implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private org.eclipse.birt.report.soapengine.api.IOReference leftIO;
	private org.eclipse.birt.report.soapengine.api.IOReference rightIO;
	private org.eclipse.birt.report.soapengine.api.JoinCondition[] joinConditions;

	public JoinDefinition() {
	}

	public JoinDefinition(org.eclipse.birt.report.soapengine.api.IOReference leftIO,
			org.eclipse.birt.report.soapengine.api.IOReference rightIO,
			org.eclipse.birt.report.soapengine.api.JoinCondition[] joinConditions) {
		this.leftIO = leftIO;
		this.rightIO = rightIO;
		this.joinConditions = joinConditions;
	}

	/**
	 * Gets the leftIO value for this JoinDefinition.
	 *
	 * @return leftIO
	 */
	public org.eclipse.birt.report.soapengine.api.IOReference getLeftIO() {
		return leftIO;
	}

	/**
	 * Sets the leftIO value for this JoinDefinition.
	 *
	 * @param leftIO
	 */
	public void setLeftIO(org.eclipse.birt.report.soapengine.api.IOReference leftIO) {
		this.leftIO = leftIO;
	}

	/**
	 * Gets the rightIO value for this JoinDefinition.
	 *
	 * @return rightIO
	 */
	public org.eclipse.birt.report.soapengine.api.IOReference getRightIO() {
		return rightIO;
	}

	/**
	 * Sets the rightIO value for this JoinDefinition.
	 *
	 * @param rightIO
	 */
	public void setRightIO(org.eclipse.birt.report.soapengine.api.IOReference rightIO) {
		this.rightIO = rightIO;
	}

	/**
	 * Gets the joinConditions value for this JoinDefinition.
	 *
	 * @return joinConditions
	 */
	public org.eclipse.birt.report.soapengine.api.JoinCondition[] getJoinConditions() {
		return joinConditions;
	}

	/**
	 * Sets the joinConditions value for this JoinDefinition.
	 *
	 * @param joinConditions
	 */
	public void setJoinConditions(org.eclipse.birt.report.soapengine.api.JoinCondition[] joinConditions) {
		this.joinConditions = joinConditions;
	}

	public org.eclipse.birt.report.soapengine.api.JoinCondition getJoinConditions(int i) {
		return this.joinConditions[i];
	}

	public void setJoinConditions(int i, org.eclipse.birt.report.soapengine.api.JoinCondition _value) {
		this.joinConditions[i] = _value;
	}

	private java.lang.Object __equalsCalc = null;

	@Override
	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof JoinDefinition)) {
			return false;
		}
		JoinDefinition other = (JoinDefinition) obj;
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
				&& ((this.leftIO == null && other.getLeftIO() == null)
						|| (this.leftIO != null && this.leftIO.equals(other.getLeftIO())))
				&& ((this.rightIO == null && other.getRightIO() == null)
						|| (this.rightIO != null && this.rightIO.equals(other.getRightIO())))
				&& ((this.joinConditions == null && other.getJoinConditions() == null) || (this.joinConditions != null
						&& java.util.Arrays.equals(this.joinConditions, other.getJoinConditions())));
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
		if (getLeftIO() != null) {
			_hashCode += getLeftIO().hashCode();
		}
		if (getRightIO() != null) {
			_hashCode += getRightIO().hashCode();
		}
		if (getJoinConditions() != null) {
			for (int i = 0; i < java.lang.reflect.Array.getLength(getJoinConditions()); i++) {
				java.lang.Object obj = java.lang.reflect.Array.get(getJoinConditions(), i);
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
			JoinDefinition.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "JoinDefinition"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("leftIO");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "LeftIO"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "IOReference"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("rightIO");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "RightIO"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "IOReference"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("joinConditions");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "JoinConditions"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "JoinCondition"));
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

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
 * ColumnDefinitionGroup.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

public class ColumnDefinitionGroup implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private org.eclipse.birt.report.soapengine.api.ColumnDefinition[] columnDef;

	public ColumnDefinitionGroup() {
	}

	public ColumnDefinitionGroup(org.eclipse.birt.report.soapengine.api.ColumnDefinition[] columnDef) {
		this.columnDef = columnDef;
	}

	/**
	 * Gets the columnDef value for this ColumnDefinitionGroup.
	 * 
	 * @return columnDef
	 */
	public org.eclipse.birt.report.soapengine.api.ColumnDefinition[] getColumnDef() {
		return columnDef;
	}

	/**
	 * Sets the columnDef value for this ColumnDefinitionGroup.
	 * 
	 * @param columnDef
	 */
	public void setColumnDef(org.eclipse.birt.report.soapengine.api.ColumnDefinition[] columnDef) {
		this.columnDef = columnDef;
	}

	public org.eclipse.birt.report.soapengine.api.ColumnDefinition getColumnDef(int i) {
		return this.columnDef[i];
	}

	public void setColumnDef(int i, org.eclipse.birt.report.soapengine.api.ColumnDefinition _value) {
		this.columnDef[i] = _value;
	}

	private java.lang.Object __equalsCalc = null;

	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof ColumnDefinitionGroup))
			return false;
		ColumnDefinitionGroup other = (ColumnDefinitionGroup) obj;
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (__equalsCalc != null) {
			return (__equalsCalc == obj);
		}
		__equalsCalc = obj;
		boolean _equals;
		_equals = true && ((this.columnDef == null && other.getColumnDef() == null)
				|| (this.columnDef != null && java.util.Arrays.equals(this.columnDef, other.getColumnDef())));
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
		if (getColumnDef() != null) {
			for (int i = 0; i < java.lang.reflect.Array.getLength(getColumnDef()); i++) {
				java.lang.Object obj = java.lang.reflect.Array.get(getColumnDef(), i);
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
			ColumnDefinitionGroup.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ColumnDefinitionGroup"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("columnDef");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ColumnDef"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ColumnDefinition"));
		elemField.setMinOccurs(0);
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

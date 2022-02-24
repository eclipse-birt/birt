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
 * ResultSet.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

public class ResultSet implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private java.lang.String queryName;
	private org.eclipse.birt.report.soapengine.api.Column[] column;

	public ResultSet() {
	}

	public ResultSet(java.lang.String queryName, org.eclipse.birt.report.soapengine.api.Column[] column) {
		this.queryName = queryName;
		this.column = column;
	}

	/**
	 * Gets the queryName value for this ResultSet.
	 * 
	 * @return queryName
	 */
	public java.lang.String getQueryName() {
		return queryName;
	}

	/**
	 * Sets the queryName value for this ResultSet.
	 * 
	 * @param queryName
	 */
	public void setQueryName(java.lang.String queryName) {
		this.queryName = queryName;
	}

	/**
	 * Gets the column value for this ResultSet.
	 * 
	 * @return column
	 */
	public org.eclipse.birt.report.soapengine.api.Column[] getColumn() {
		return column;
	}

	/**
	 * Sets the column value for this ResultSet.
	 * 
	 * @param column
	 */
	public void setColumn(org.eclipse.birt.report.soapengine.api.Column[] column) {
		this.column = column;
	}

	public org.eclipse.birt.report.soapengine.api.Column getColumn(int i) {
		return this.column[i];
	}

	public void setColumn(int i, org.eclipse.birt.report.soapengine.api.Column _value) {
		this.column[i] = _value;
	}

	private java.lang.Object __equalsCalc = null;

	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof ResultSet))
			return false;
		ResultSet other = (ResultSet) obj;
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
				&& ((this.queryName == null && other.getQueryName() == null)
						|| (this.queryName != null && this.queryName.equals(other.getQueryName())))
				&& ((this.column == null && other.getColumn() == null)
						|| (this.column != null && java.util.Arrays.equals(this.column, other.getColumn())));
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
		if (getQueryName() != null) {
			_hashCode += getQueryName().hashCode();
		}
		if (getColumn() != null) {
			for (int i = 0; i < java.lang.reflect.Array.getLength(getColumn()); i++) {
				java.lang.Object obj = java.lang.reflect.Array.get(getColumn(), i);
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
			ResultSet.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ResultSet"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("queryName");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "QueryName"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("column");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Column"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Column"));
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

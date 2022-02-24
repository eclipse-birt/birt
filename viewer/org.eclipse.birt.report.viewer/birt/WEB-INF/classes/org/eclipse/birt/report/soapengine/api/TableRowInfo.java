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
 * TableRowInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

/**
 * information of a table row
 */
public class TableRowInfo implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * The group level of this row. -1 is used to indicated table level rows.
	 */
	private int level;
	/**
	 * The position of this row in the group header or group footer. Index starts
	 * from 0.
	 */
	private int index;
	/** Whether the row is in header or in footer */
	private boolean isHeader;

	public TableRowInfo() {
	}

	public TableRowInfo(int level, int index, boolean isHeader) {
		this.level = level;
		this.index = index;
		this.isHeader = isHeader;
	}

	/**
	 * Gets the level value for this TableRowInfo.
	 * 
	 * @return level The group level of this row. -1 is used to indicated table
	 *         level rows.
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Sets the level value for this TableRowInfo.
	 * 
	 * @param level The group level of this row. -1 is used to indicated table level
	 *              rows.
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * Gets the index value for this TableRowInfo.
	 * 
	 * @return index The position of this row in the group header or group footer.
	 *         Index starts from 0.
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Sets the index value for this TableRowInfo.
	 * 
	 * @param index The position of this row in the group header or group footer.
	 *              Index starts from 0.
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * Gets the isHeader value for this TableRowInfo.
	 * 
	 * @return isHeader Whether the row is in header or in footer
	 */
	public boolean isIsHeader() {
		return isHeader;
	}

	/**
	 * Sets the isHeader value for this TableRowInfo.
	 * 
	 * @param isHeader Whether the row is in header or in footer
	 */
	public void setIsHeader(boolean isHeader) {
		this.isHeader = isHeader;
	}

	private java.lang.Object __equalsCalc = null;

	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof TableRowInfo))
			return false;
		TableRowInfo other = (TableRowInfo) obj;
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (__equalsCalc != null) {
			return (__equalsCalc == obj);
		}
		__equalsCalc = obj;
		boolean _equals;
		_equals = true && this.level == other.getLevel() && this.index == other.getIndex()
				&& this.isHeader == other.isIsHeader();
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
		_hashCode += getLevel();
		_hashCode += getIndex();
		_hashCode += (isIsHeader() ? Boolean.TRUE : Boolean.FALSE).hashCode();
		__hashCodeCalc = false;
		return _hashCode;
	}

	// Type metadata
	private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(
			TableRowInfo.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "TableRowInfo"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("level");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Level"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("index");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Index"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("isHeader");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "IsHeader"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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

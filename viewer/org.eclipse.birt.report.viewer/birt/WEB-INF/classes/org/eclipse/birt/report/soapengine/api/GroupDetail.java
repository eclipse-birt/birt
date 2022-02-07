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
 * GroupDetail.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

public class GroupDetail implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private int columnIdx;
	private java.lang.String groupOn;
	private boolean createSection;
	private int dataType;
	private java.lang.Integer intervalType;
	private java.lang.Double intervalRange;
	private java.lang.String intervalBase;

	public GroupDetail() {
	}

	public GroupDetail(int columnIdx, java.lang.String groupOn, boolean createSection, int dataType,
			java.lang.Integer intervalType, java.lang.Double intervalRange, java.lang.String intervalBase) {
		this.columnIdx = columnIdx;
		this.groupOn = groupOn;
		this.createSection = createSection;
		this.dataType = dataType;
		this.intervalType = intervalType;
		this.intervalRange = intervalRange;
		this.intervalBase = intervalBase;
	}

	/**
	 * Gets the columnIdx value for this GroupDetail.
	 * 
	 * @return columnIdx
	 */
	public int getColumnIdx() {
		return columnIdx;
	}

	/**
	 * Sets the columnIdx value for this GroupDetail.
	 * 
	 * @param columnIdx
	 */
	public void setColumnIdx(int columnIdx) {
		this.columnIdx = columnIdx;
	}

	/**
	 * Gets the groupOn value for this GroupDetail.
	 * 
	 * @return groupOn
	 */
	public java.lang.String getGroupOn() {
		return groupOn;
	}

	/**
	 * Sets the groupOn value for this GroupDetail.
	 * 
	 * @param groupOn
	 */
	public void setGroupOn(java.lang.String groupOn) {
		this.groupOn = groupOn;
	}

	/**
	 * Gets the createSection value for this GroupDetail.
	 * 
	 * @return createSection
	 */
	public boolean isCreateSection() {
		return createSection;
	}

	/**
	 * Sets the createSection value for this GroupDetail.
	 * 
	 * @param createSection
	 */
	public void setCreateSection(boolean createSection) {
		this.createSection = createSection;
	}

	/**
	 * Gets the dataType value for this GroupDetail.
	 * 
	 * @return dataType
	 */
	public int getDataType() {
		return dataType;
	}

	/**
	 * Sets the dataType value for this GroupDetail.
	 * 
	 * @param dataType
	 */
	public void setDataType(int dataType) {
		this.dataType = dataType;
	}

	/**
	 * Gets the intervalType value for this GroupDetail.
	 * 
	 * @return intervalType
	 */
	public java.lang.Integer getIntervalType() {
		return intervalType;
	}

	/**
	 * Sets the intervalType value for this GroupDetail.
	 * 
	 * @param intervalType
	 */
	public void setIntervalType(java.lang.Integer intervalType) {
		this.intervalType = intervalType;
	}

	/**
	 * Gets the intervalRange value for this GroupDetail.
	 * 
	 * @return intervalRange
	 */
	public java.lang.Double getIntervalRange() {
		return intervalRange;
	}

	/**
	 * Sets the intervalRange value for this GroupDetail.
	 * 
	 * @param intervalRange
	 */
	public void setIntervalRange(java.lang.Double intervalRange) {
		this.intervalRange = intervalRange;
	}

	/**
	 * Gets the intervalBase value for this GroupDetail.
	 * 
	 * @return intervalBase
	 */
	public java.lang.String getIntervalBase() {
		return intervalBase;
	}

	/**
	 * Sets the intervalBase value for this GroupDetail.
	 * 
	 * @param intervalBase
	 */
	public void setIntervalBase(java.lang.String intervalBase) {
		this.intervalBase = intervalBase;
	}

	private java.lang.Object __equalsCalc = null;

	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof GroupDetail))
			return false;
		GroupDetail other = (GroupDetail) obj;
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (__equalsCalc != null) {
			return (__equalsCalc == obj);
		}
		__equalsCalc = obj;
		boolean _equals;
		_equals = true && this.columnIdx == other.getColumnIdx()
				&& ((this.groupOn == null && other.getGroupOn() == null)
						|| (this.groupOn != null && this.groupOn.equals(other.getGroupOn())))
				&& this.createSection == other.isCreateSection() && this.dataType == other.getDataType()
				&& ((this.intervalType == null && other.getIntervalType() == null)
						|| (this.intervalType != null && this.intervalType.equals(other.getIntervalType())))
				&& ((this.intervalRange == null && other.getIntervalRange() == null)
						|| (this.intervalRange != null && this.intervalRange.equals(other.getIntervalRange())))
				&& ((this.intervalBase == null && other.getIntervalBase() == null)
						|| (this.intervalBase != null && this.intervalBase.equals(other.getIntervalBase())));
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
		_hashCode += getColumnIdx();
		if (getGroupOn() != null) {
			_hashCode += getGroupOn().hashCode();
		}
		_hashCode += (isCreateSection() ? Boolean.TRUE : Boolean.FALSE).hashCode();
		_hashCode += getDataType();
		if (getIntervalType() != null) {
			_hashCode += getIntervalType().hashCode();
		}
		if (getIntervalRange() != null) {
			_hashCode += getIntervalRange().hashCode();
		}
		if (getIntervalBase() != null) {
			_hashCode += getIntervalBase().hashCode();
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	// Type metadata
	private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(
			GroupDetail.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "GroupDetail"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("columnIdx");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ColumnIdx"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("groupOn");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "GroupOn"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("createSection");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "CreateSection"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("dataType");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "DataType"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("intervalType");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "IntervalType"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("intervalRange");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "IntervalRange"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("intervalBase");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "IntervalBase"));
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

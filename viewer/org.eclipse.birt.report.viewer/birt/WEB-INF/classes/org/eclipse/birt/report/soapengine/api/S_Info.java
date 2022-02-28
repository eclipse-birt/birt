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
 * S_Info.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

/**
 * information of a section
 */
public class S_Info implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private int level;
	/** The number of rows in section header */
	private int h_Count;
	/** The number of rows in section footer */
	private int f_Count;
	/** The index of section header row */
	private int HR_Index;
	/** The index of chart row */
	private int CR_Index;

	public S_Info() {
	}

	public S_Info(int level, int h_Count, int f_Count, int HR_Index, int CR_Index) {
		this.level = level;
		this.h_Count = h_Count;
		this.f_Count = f_Count;
		this.HR_Index = HR_Index;
		this.CR_Index = CR_Index;
	}

	/**
	 * Gets the level value for this S_Info.
	 *
	 * @return level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Sets the level value for this S_Info.
	 *
	 * @param level
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * Gets the h_Count value for this S_Info.
	 *
	 * @return h_Count The number of rows in section header
	 */
	public int getH_Count() {
		return h_Count;
	}

	/**
	 * Sets the h_Count value for this S_Info.
	 *
	 * @param h_Count The number of rows in section header
	 */
	public void setH_Count(int h_Count) {
		this.h_Count = h_Count;
	}

	/**
	 * Gets the f_Count value for this S_Info.
	 *
	 * @return f_Count The number of rows in section footer
	 */
	public int getF_Count() {
		return f_Count;
	}

	/**
	 * Sets the f_Count value for this S_Info.
	 *
	 * @param f_Count The number of rows in section footer
	 */
	public void setF_Count(int f_Count) {
		this.f_Count = f_Count;
	}

	/**
	 * Gets the HR_Index value for this S_Info.
	 *
	 * @return HR_Index The index of section header row
	 */
	public int getHR_Index() {
		return HR_Index;
	}

	/**
	 * Sets the HR_Index value for this S_Info.
	 *
	 * @param HR_Index The index of section header row
	 */
	public void setHR_Index(int HR_Index) {
		this.HR_Index = HR_Index;
	}

	/**
	 * Gets the CR_Index value for this S_Info.
	 *
	 * @return CR_Index The index of chart row
	 */
	public int getCR_Index() {
		return CR_Index;
	}

	/**
	 * Sets the CR_Index value for this S_Info.
	 *
	 * @param CR_Index The index of chart row
	 */
	public void setCR_Index(int CR_Index) {
		this.CR_Index = CR_Index;
	}

	private java.lang.Object __equalsCalc = null;

	@Override
	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof S_Info)) {
			return false;
		}
		S_Info other = (S_Info) obj;
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
		_equals = true && this.level == other.getLevel() && this.h_Count == other.getH_Count()
				&& this.f_Count == other.getF_Count() && this.HR_Index == other.getHR_Index()
				&& this.CR_Index == other.getCR_Index();
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
		_hashCode += getLevel();
		_hashCode += getH_Count();
		_hashCode += getF_Count();
		_hashCode += getHR_Index();
		_hashCode += getCR_Index();
		__hashCodeCalc = false;
		return _hashCode;
	}

	// Type metadata
	private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(
			S_Info.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "S_Info"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("level");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Level"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("h_Count");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "H_Count"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("f_Count");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "F_Count"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("HR_Index");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "HR_Index"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("CR_Index");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "CR_Index"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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

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
 * ChartProperties.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

public class ChartProperties implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private org.eclipse.birt.report.soapengine.api.ChartType type;
	private org.eclipse.birt.report.soapengine.api.ChartDataBinding dataBinding;
	private org.eclipse.birt.report.soapengine.api.ChartLabels labels;
	private org.eclipse.birt.report.soapengine.api.ChartAppearance apperance;

	public ChartProperties() {
	}

	public ChartProperties(org.eclipse.birt.report.soapengine.api.ChartType type,
			org.eclipse.birt.report.soapengine.api.ChartDataBinding dataBinding,
			org.eclipse.birt.report.soapengine.api.ChartLabels labels,
			org.eclipse.birt.report.soapengine.api.ChartAppearance apperance) {
		this.type = type;
		this.dataBinding = dataBinding;
		this.labels = labels;
		this.apperance = apperance;
	}

	/**
	 * Gets the type value for this ChartProperties.
	 *
	 * @return type
	 */
	public org.eclipse.birt.report.soapengine.api.ChartType getType() {
		return type;
	}

	/**
	 * Sets the type value for this ChartProperties.
	 *
	 * @param type
	 */
	public void setType(org.eclipse.birt.report.soapengine.api.ChartType type) {
		this.type = type;
	}

	/**
	 * Gets the dataBinding value for this ChartProperties.
	 *
	 * @return dataBinding
	 */
	public org.eclipse.birt.report.soapengine.api.ChartDataBinding getDataBinding() {
		return dataBinding;
	}

	/**
	 * Sets the dataBinding value for this ChartProperties.
	 *
	 * @param dataBinding
	 */
	public void setDataBinding(org.eclipse.birt.report.soapengine.api.ChartDataBinding dataBinding) {
		this.dataBinding = dataBinding;
	}

	/**
	 * Gets the labels value for this ChartProperties.
	 *
	 * @return labels
	 */
	public org.eclipse.birt.report.soapengine.api.ChartLabels getLabels() {
		return labels;
	}

	/**
	 * Sets the labels value for this ChartProperties.
	 *
	 * @param labels
	 */
	public void setLabels(org.eclipse.birt.report.soapengine.api.ChartLabels labels) {
		this.labels = labels;
	}

	/**
	 * Gets the apperance value for this ChartProperties.
	 *
	 * @return apperance
	 */
	public org.eclipse.birt.report.soapengine.api.ChartAppearance getApperance() {
		return apperance;
	}

	/**
	 * Sets the apperance value for this ChartProperties.
	 *
	 * @param apperance
	 */
	public void setApperance(org.eclipse.birt.report.soapengine.api.ChartAppearance apperance) {
		this.apperance = apperance;
	}

	private java.lang.Object __equalsCalc = null;

	@Override
	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof ChartProperties)) {
			return false;
		}
		ChartProperties other = (ChartProperties) obj;
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
				&& ((this.type == null && other.getType() == null)
						|| (this.type != null && this.type.equals(other.getType())))
				&& ((this.dataBinding == null && other.getDataBinding() == null)
						|| (this.dataBinding != null && this.dataBinding.equals(other.getDataBinding())))
				&& ((this.labels == null && other.getLabels() == null)
						|| (this.labels != null && this.labels.equals(other.getLabels())))
				&& ((this.apperance == null && other.getApperance() == null)
						|| (this.apperance != null && this.apperance.equals(other.getApperance())));
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
		if (getType() != null) {
			_hashCode += getType().hashCode();
		}
		if (getDataBinding() != null) {
			_hashCode += getDataBinding().hashCode();
		}
		if (getLabels() != null) {
			_hashCode += getLabels().hashCode();
		}
		if (getApperance() != null) {
			_hashCode += getApperance().hashCode();
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	// Type metadata
	private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(
			ChartProperties.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ChartProperties"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("type");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Type"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ChartType"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("dataBinding");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "DataBinding"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ChartDataBinding"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("labels");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Labels"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ChartLabels"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("apperance");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Apperance"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ChartAppearance"));
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

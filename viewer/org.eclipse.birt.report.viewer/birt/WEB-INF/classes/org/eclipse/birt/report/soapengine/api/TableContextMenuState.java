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
 * TableContextMenuState.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

public class TableContextMenuState implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	/** Can insert a chart in this section */
	private boolean canAddChart;
	/** Can edit a chart in this section */
	private boolean canEditChart;
	/** Can delete a chart in this section */
	private boolean canDeleteChart;

	public TableContextMenuState() {
	}

	public TableContextMenuState(boolean canAddChart, boolean canEditChart, boolean canDeleteChart) {
		this.canAddChart = canAddChart;
		this.canEditChart = canEditChart;
		this.canDeleteChart = canDeleteChart;
	}

	/**
	 * Gets the canAddChart value for this TableContextMenuState.
	 * 
	 * @return canAddChart Can insert a chart in this section
	 */
	public boolean isCanAddChart() {
		return canAddChart;
	}

	/**
	 * Sets the canAddChart value for this TableContextMenuState.
	 * 
	 * @param canAddChart Can insert a chart in this section
	 */
	public void setCanAddChart(boolean canAddChart) {
		this.canAddChart = canAddChart;
	}

	/**
	 * Gets the canEditChart value for this TableContextMenuState.
	 * 
	 * @return canEditChart Can edit a chart in this section
	 */
	public boolean isCanEditChart() {
		return canEditChart;
	}

	/**
	 * Sets the canEditChart value for this TableContextMenuState.
	 * 
	 * @param canEditChart Can edit a chart in this section
	 */
	public void setCanEditChart(boolean canEditChart) {
		this.canEditChart = canEditChart;
	}

	/**
	 * Gets the canDeleteChart value for this TableContextMenuState.
	 * 
	 * @return canDeleteChart Can delete a chart in this section
	 */
	public boolean isCanDeleteChart() {
		return canDeleteChart;
	}

	/**
	 * Sets the canDeleteChart value for this TableContextMenuState.
	 * 
	 * @param canDeleteChart Can delete a chart in this section
	 */
	public void setCanDeleteChart(boolean canDeleteChart) {
		this.canDeleteChart = canDeleteChart;
	}

	private java.lang.Object __equalsCalc = null;

	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof TableContextMenuState))
			return false;
		TableContextMenuState other = (TableContextMenuState) obj;
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (__equalsCalc != null) {
			return (__equalsCalc == obj);
		}
		__equalsCalc = obj;
		boolean _equals;
		_equals = true && this.canAddChart == other.isCanAddChart() && this.canEditChart == other.isCanEditChart()
				&& this.canDeleteChart == other.isCanDeleteChart();
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
		_hashCode += (isCanAddChart() ? Boolean.TRUE : Boolean.FALSE).hashCode();
		_hashCode += (isCanEditChart() ? Boolean.TRUE : Boolean.FALSE).hashCode();
		_hashCode += (isCanDeleteChart() ? Boolean.TRUE : Boolean.FALSE).hashCode();
		__hashCodeCalc = false;
		return _hashCode;
	}

	// Type metadata
	private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(
			TableContextMenuState.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "TableContextMenuState"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("canAddChart");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "CanAddChart"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("canEditChart");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "CanEditChart"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("canDeleteChart");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "CanDeleteChart"));
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

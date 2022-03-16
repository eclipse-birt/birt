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
 * ColumnDefinition.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

public class ColumnDefinition implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private java.lang.Integer index;
	private java.lang.String iid;
	private java.lang.String header;
	private java.lang.Integer dataType;
	private java.lang.String expr;
	private java.lang.Integer newIndex;
	private java.lang.Boolean isGrouped;
	private org.eclipse.birt.report.soapengine.api.SortingDirection sortDir;
	private org.eclipse.birt.report.soapengine.api.AggregateDefinition[] aggregate;
	private org.eclipse.birt.report.soapengine.api.Font font;
	private org.eclipse.birt.report.soapengine.api.Format format;
	private org.eclipse.birt.report.soapengine.api.ColumnProperties properties;
	private org.eclipse.birt.report.soapengine.api.FormatRuleSet formatRuleSet;
	private org.eclipse.birt.report.soapengine.api.BoundDataColumn boundDataColumn;

	public ColumnDefinition() {
	}

	public ColumnDefinition(java.lang.Integer index, java.lang.String iid, java.lang.String header,
			java.lang.Integer dataType, java.lang.String expr, java.lang.Integer newIndex, java.lang.Boolean isGrouped,
			org.eclipse.birt.report.soapengine.api.SortingDirection sortDir,
			org.eclipse.birt.report.soapengine.api.AggregateDefinition[] aggregate,
			org.eclipse.birt.report.soapengine.api.Font font, org.eclipse.birt.report.soapengine.api.Format format,
			org.eclipse.birt.report.soapengine.api.ColumnProperties properties,
			org.eclipse.birt.report.soapengine.api.FormatRuleSet formatRuleSet,
			org.eclipse.birt.report.soapengine.api.BoundDataColumn boundDataColumn) {
		this.index = index;
		this.iid = iid;
		this.header = header;
		this.dataType = dataType;
		this.expr = expr;
		this.newIndex = newIndex;
		this.isGrouped = isGrouped;
		this.sortDir = sortDir;
		this.aggregate = aggregate;
		this.font = font;
		this.format = format;
		this.properties = properties;
		this.formatRuleSet = formatRuleSet;
		this.boundDataColumn = boundDataColumn;
	}

	/**
	 * Gets the index value for this ColumnDefinition.
	 *
	 * @return index
	 */
	public java.lang.Integer getIndex() {
		return index;
	}

	/**
	 * Sets the index value for this ColumnDefinition.
	 *
	 * @param index
	 */
	public void setIndex(java.lang.Integer index) {
		this.index = index;
	}

	/**
	 * Gets the iid value for this ColumnDefinition.
	 *
	 * @return iid
	 */
	public java.lang.String getIid() {
		return iid;
	}

	/**
	 * Sets the iid value for this ColumnDefinition.
	 *
	 * @param iid
	 */
	public void setIid(java.lang.String iid) {
		this.iid = iid;
	}

	/**
	 * Gets the header value for this ColumnDefinition.
	 *
	 * @return header
	 */
	public java.lang.String getHeader() {
		return header;
	}

	/**
	 * Sets the header value for this ColumnDefinition.
	 *
	 * @param header
	 */
	public void setHeader(java.lang.String header) {
		this.header = header;
	}

	/**
	 * Gets the dataType value for this ColumnDefinition.
	 *
	 * @return dataType
	 */
	public java.lang.Integer getDataType() {
		return dataType;
	}

	/**
	 * Sets the dataType value for this ColumnDefinition.
	 *
	 * @param dataType
	 */
	public void setDataType(java.lang.Integer dataType) {
		this.dataType = dataType;
	}

	/**
	 * Gets the expr value for this ColumnDefinition.
	 *
	 * @return expr
	 */
	public java.lang.String getExpr() {
		return expr;
	}

	/**
	 * Sets the expr value for this ColumnDefinition.
	 *
	 * @param expr
	 */
	public void setExpr(java.lang.String expr) {
		this.expr = expr;
	}

	/**
	 * Gets the newIndex value for this ColumnDefinition.
	 *
	 * @return newIndex
	 */
	public java.lang.Integer getNewIndex() {
		return newIndex;
	}

	/**
	 * Sets the newIndex value for this ColumnDefinition.
	 *
	 * @param newIndex
	 */
	public void setNewIndex(java.lang.Integer newIndex) {
		this.newIndex = newIndex;
	}

	/**
	 * Gets the isGrouped value for this ColumnDefinition.
	 *
	 * @return isGrouped
	 */
	public java.lang.Boolean getIsGrouped() {
		return isGrouped;
	}

	/**
	 * Sets the isGrouped value for this ColumnDefinition.
	 *
	 * @param isGrouped
	 */
	public void setIsGrouped(java.lang.Boolean isGrouped) {
		this.isGrouped = isGrouped;
	}

	/**
	 * Gets the sortDir value for this ColumnDefinition.
	 *
	 * @return sortDir
	 */
	public org.eclipse.birt.report.soapengine.api.SortingDirection getSortDir() {
		return sortDir;
	}

	/**
	 * Sets the sortDir value for this ColumnDefinition.
	 *
	 * @param sortDir
	 */
	public void setSortDir(org.eclipse.birt.report.soapengine.api.SortingDirection sortDir) {
		this.sortDir = sortDir;
	}

	/**
	 * Gets the aggregate value for this ColumnDefinition.
	 *
	 * @return aggregate
	 */
	public org.eclipse.birt.report.soapengine.api.AggregateDefinition[] getAggregate() {
		return aggregate;
	}

	/**
	 * Sets the aggregate value for this ColumnDefinition.
	 *
	 * @param aggregate
	 */
	public void setAggregate(org.eclipse.birt.report.soapengine.api.AggregateDefinition[] aggregate) {
		this.aggregate = aggregate;
	}

	public org.eclipse.birt.report.soapengine.api.AggregateDefinition getAggregate(int i) {
		return this.aggregate[i];
	}

	public void setAggregate(int i, org.eclipse.birt.report.soapengine.api.AggregateDefinition _value) {
		this.aggregate[i] = _value;
	}

	/**
	 * Gets the font value for this ColumnDefinition.
	 *
	 * @return font
	 */
	public org.eclipse.birt.report.soapengine.api.Font getFont() {
		return font;
	}

	/**
	 * Sets the font value for this ColumnDefinition.
	 *
	 * @param font
	 */
	public void setFont(org.eclipse.birt.report.soapengine.api.Font font) {
		this.font = font;
	}

	/**
	 * Gets the format value for this ColumnDefinition.
	 *
	 * @return format
	 */
	public org.eclipse.birt.report.soapengine.api.Format getFormat() {
		return format;
	}

	/**
	 * Sets the format value for this ColumnDefinition.
	 *
	 * @param format
	 */
	public void setFormat(org.eclipse.birt.report.soapengine.api.Format format) {
		this.format = format;
	}

	/**
	 * Gets the properties value for this ColumnDefinition.
	 *
	 * @return properties
	 */
	public org.eclipse.birt.report.soapengine.api.ColumnProperties getProperties() {
		return properties;
	}

	/**
	 * Sets the properties value for this ColumnDefinition.
	 *
	 * @param properties
	 */
	public void setProperties(org.eclipse.birt.report.soapengine.api.ColumnProperties properties) {
		this.properties = properties;
	}

	/**
	 * Gets the formatRuleSet value for this ColumnDefinition.
	 *
	 * @return formatRuleSet
	 */
	public org.eclipse.birt.report.soapengine.api.FormatRuleSet getFormatRuleSet() {
		return formatRuleSet;
	}

	/**
	 * Sets the formatRuleSet value for this ColumnDefinition.
	 *
	 * @param formatRuleSet
	 */
	public void setFormatRuleSet(org.eclipse.birt.report.soapengine.api.FormatRuleSet formatRuleSet) {
		this.formatRuleSet = formatRuleSet;
	}

	/**
	 * Gets the boundDataColumn value for this ColumnDefinition.
	 *
	 * @return boundDataColumn
	 */
	public org.eclipse.birt.report.soapengine.api.BoundDataColumn getBoundDataColumn() {
		return boundDataColumn;
	}

	/**
	 * Sets the boundDataColumn value for this ColumnDefinition.
	 *
	 * @param boundDataColumn
	 */
	public void setBoundDataColumn(org.eclipse.birt.report.soapengine.api.BoundDataColumn boundDataColumn) {
		this.boundDataColumn = boundDataColumn;
	}

	private java.lang.Object __equalsCalc = null;

	@Override
	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof ColumnDefinition)) {
			return false;
		}
		ColumnDefinition other = (ColumnDefinition) obj;
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
				&& ((this.index == null && other.getIndex() == null)
						|| (this.index != null && this.index.equals(other.getIndex())))
				&& ((this.iid == null && other.getIid() == null)
						|| (this.iid != null && this.iid.equals(other.getIid())))
				&& ((this.header == null && other.getHeader() == null)
						|| (this.header != null && this.header.equals(other.getHeader())))
				&& ((this.dataType == null && other.getDataType() == null)
						|| (this.dataType != null && this.dataType.equals(other.getDataType())))
				&& ((this.expr == null && other.getExpr() == null)
						|| (this.expr != null && this.expr.equals(other.getExpr())))
				&& ((this.newIndex == null && other.getNewIndex() == null)
						|| (this.newIndex != null && this.newIndex.equals(other.getNewIndex())))
				&& ((this.isGrouped == null && other.getIsGrouped() == null)
						|| (this.isGrouped != null && this.isGrouped.equals(other.getIsGrouped())))
				&& ((this.sortDir == null && other.getSortDir() == null)
						|| (this.sortDir != null && this.sortDir.equals(other.getSortDir())))
				&& ((this.aggregate == null && other.getAggregate() == null)
						|| (this.aggregate != null && java.util.Arrays.equals(this.aggregate, other.getAggregate())))
				&& ((this.font == null && other.getFont() == null)
						|| (this.font != null && this.font.equals(other.getFont())))
				&& ((this.format == null && other.getFormat() == null)
						|| (this.format != null && this.format.equals(other.getFormat())))
				&& ((this.properties == null && other.getProperties() == null)
						|| (this.properties != null && this.properties.equals(other.getProperties())))
				&& ((this.formatRuleSet == null && other.getFormatRuleSet() == null)
						|| (this.formatRuleSet != null && this.formatRuleSet.equals(other.getFormatRuleSet())))
				&& ((this.boundDataColumn == null && other.getBoundDataColumn() == null)
						|| (this.boundDataColumn != null && this.boundDataColumn.equals(other.getBoundDataColumn())));
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
		if (getIndex() != null) {
			_hashCode += getIndex().hashCode();
		}
		if (getIid() != null) {
			_hashCode += getIid().hashCode();
		}
		if (getHeader() != null) {
			_hashCode += getHeader().hashCode();
		}
		if (getDataType() != null) {
			_hashCode += getDataType().hashCode();
		}
		if (getExpr() != null) {
			_hashCode += getExpr().hashCode();
		}
		if (getNewIndex() != null) {
			_hashCode += getNewIndex().hashCode();
		}
		if (getIsGrouped() != null) {
			_hashCode += getIsGrouped().hashCode();
		}
		if (getSortDir() != null) {
			_hashCode += getSortDir().hashCode();
		}
		if (getAggregate() != null) {
			for (int i = 0; i < java.lang.reflect.Array.getLength(getAggregate()); i++) {
				java.lang.Object obj = java.lang.reflect.Array.get(getAggregate(), i);
				if (obj != null && !obj.getClass().isArray()) {
					_hashCode += obj.hashCode();
				}
			}
		}
		if (getFont() != null) {
			_hashCode += getFont().hashCode();
		}
		if (getFormat() != null) {
			_hashCode += getFormat().hashCode();
		}
		if (getProperties() != null) {
			_hashCode += getProperties().hashCode();
		}
		if (getFormatRuleSet() != null) {
			_hashCode += getFormatRuleSet().hashCode();
		}
		if (getBoundDataColumn() != null) {
			_hashCode += getBoundDataColumn().hashCode();
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	// Type metadata
	private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(
			ColumnDefinition.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ColumnDefinition"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("index");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Index"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("iid");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Iid"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("header");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Header"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("dataType");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "DataType"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("expr");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Expr"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("newIndex");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "NewIndex"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("isGrouped");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "IsGrouped"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("sortDir");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "SortDir"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "SortingDirection"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("aggregate");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Aggregate"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "AggregateDefinition"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		elemField.setMaxOccursUnbounded(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("font");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Font"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Font"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("format");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Format"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Format"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("properties");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Properties"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ColumnProperties"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("formatRuleSet");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "FormatRuleSet"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "FormatRuleSet"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("boundDataColumn");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "BoundDataColumn"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "BoundDataColumn"));
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

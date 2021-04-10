/**
 * DataSource.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

public class DataSource implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private java.lang.String type;
	private java.lang.String displayName;
	private java.lang.String description;
	private java.lang.Boolean isNative;

	public DataSource() {
	}

	public DataSource(java.lang.String type, java.lang.String displayName, java.lang.String description,
			java.lang.Boolean isNative) {
		this.type = type;
		this.displayName = displayName;
		this.description = description;
		this.isNative = isNative;
	}

	/**
	 * Gets the type value for this DataSource.
	 * 
	 * @return type
	 */
	public java.lang.String getType() {
		return type;
	}

	/**
	 * Sets the type value for this DataSource.
	 * 
	 * @param type
	 */
	public void setType(java.lang.String type) {
		this.type = type;
	}

	/**
	 * Gets the displayName value for this DataSource.
	 * 
	 * @return displayName
	 */
	public java.lang.String getDisplayName() {
		return displayName;
	}

	/**
	 * Sets the displayName value for this DataSource.
	 * 
	 * @param displayName
	 */
	public void setDisplayName(java.lang.String displayName) {
		this.displayName = displayName;
	}

	/**
	 * Gets the description value for this DataSource.
	 * 
	 * @return description
	 */
	public java.lang.String getDescription() {
		return description;
	}

	/**
	 * Sets the description value for this DataSource.
	 * 
	 * @param description
	 */
	public void setDescription(java.lang.String description) {
		this.description = description;
	}

	/**
	 * Gets the isNative value for this DataSource.
	 * 
	 * @return isNative
	 */
	public java.lang.Boolean getIsNative() {
		return isNative;
	}

	/**
	 * Sets the isNative value for this DataSource.
	 * 
	 * @param isNative
	 */
	public void setIsNative(java.lang.Boolean isNative) {
		this.isNative = isNative;
	}

	private java.lang.Object __equalsCalc = null;

	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof DataSource))
			return false;
		DataSource other = (DataSource) obj;
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
				&& ((this.type == null && other.getType() == null)
						|| (this.type != null && this.type.equals(other.getType())))
				&& ((this.displayName == null && other.getDisplayName() == null)
						|| (this.displayName != null && this.displayName.equals(other.getDisplayName())))
				&& ((this.description == null && other.getDescription() == null)
						|| (this.description != null && this.description.equals(other.getDescription())))
				&& ((this.isNative == null && other.getIsNative() == null)
						|| (this.isNative != null && this.isNative.equals(other.getIsNative())));
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
		if (getType() != null) {
			_hashCode += getType().hashCode();
		}
		if (getDisplayName() != null) {
			_hashCode += getDisplayName().hashCode();
		}
		if (getDescription() != null) {
			_hashCode += getDescription().hashCode();
		}
		if (getIsNative() != null) {
			_hashCode += getIsNative().hashCode();
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	// Type metadata
	private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(
			DataSource.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "DataSource"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("type");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Type"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("displayName");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "DisplayName"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("description");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Description"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("isNative");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "IsNative"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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

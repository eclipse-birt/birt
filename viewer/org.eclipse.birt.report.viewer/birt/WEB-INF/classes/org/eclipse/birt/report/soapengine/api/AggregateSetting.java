/**
 * AggregateSetting.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

public class AggregateSetting implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private java.lang.Boolean enable;
	private java.lang.Boolean showInFooter;
	private java.lang.String groupName;

	public AggregateSetting() {
	}

	public AggregateSetting(java.lang.Boolean enable, java.lang.Boolean showInFooter, java.lang.String groupName) {
		this.enable = enable;
		this.showInFooter = showInFooter;
		this.groupName = groupName;
	}

	/**
	 * Gets the enable value for this AggregateSetting.
	 * 
	 * @return enable
	 */
	public java.lang.Boolean getEnable() {
		return enable;
	}

	/**
	 * Sets the enable value for this AggregateSetting.
	 * 
	 * @param enable
	 */
	public void setEnable(java.lang.Boolean enable) {
		this.enable = enable;
	}

	/**
	 * Gets the showInFooter value for this AggregateSetting.
	 * 
	 * @return showInFooter
	 */
	public java.lang.Boolean getShowInFooter() {
		return showInFooter;
	}

	/**
	 * Sets the showInFooter value for this AggregateSetting.
	 * 
	 * @param showInFooter
	 */
	public void setShowInFooter(java.lang.Boolean showInFooter) {
		this.showInFooter = showInFooter;
	}

	/**
	 * Gets the groupName value for this AggregateSetting.
	 * 
	 * @return groupName
	 */
	public java.lang.String getGroupName() {
		return groupName;
	}

	/**
	 * Sets the groupName value for this AggregateSetting.
	 * 
	 * @param groupName
	 */
	public void setGroupName(java.lang.String groupName) {
		this.groupName = groupName;
	}

	private java.lang.Object __equalsCalc = null;

	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof AggregateSetting))
			return false;
		AggregateSetting other = (AggregateSetting) obj;
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
				&& ((this.enable == null && other.getEnable() == null)
						|| (this.enable != null && this.enable.equals(other.getEnable())))
				&& ((this.showInFooter == null && other.getShowInFooter() == null)
						|| (this.showInFooter != null && this.showInFooter.equals(other.getShowInFooter())))
				&& ((this.groupName == null && other.getGroupName() == null)
						|| (this.groupName != null && this.groupName.equals(other.getGroupName())));
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
		if (getEnable() != null) {
			_hashCode += getEnable().hashCode();
		}
		if (getShowInFooter() != null) {
			_hashCode += getShowInFooter().hashCode();
		}
		if (getGroupName() != null) {
			_hashCode += getGroupName().hashCode();
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	// Type metadata
	private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(
			AggregateSetting.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "AggregateSetting"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("enable");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Enable"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("showInFooter");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ShowInFooter"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("groupName");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "GroupName"));
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

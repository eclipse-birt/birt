/**
 * ReportId.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

public class ReportId implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private java.lang.String id;
	private org.eclipse.birt.report.soapengine.api.ReportIdType type;
	private java.lang.Long rptElementId;

	public ReportId() {
	}

	public ReportId(java.lang.String id, org.eclipse.birt.report.soapengine.api.ReportIdType type,
			java.lang.Long rptElementId) {
		this.id = id;
		this.type = type;
		this.rptElementId = rptElementId;
	}

	/**
	 * Gets the id value for this ReportId.
	 * 
	 * @return id
	 */
	public java.lang.String getId() {
		return id;
	}

	/**
	 * Sets the id value for this ReportId.
	 * 
	 * @param id
	 */
	public void setId(java.lang.String id) {
		this.id = id;
	}

	/**
	 * Gets the type value for this ReportId.
	 * 
	 * @return type
	 */
	public org.eclipse.birt.report.soapengine.api.ReportIdType getType() {
		return type;
	}

	/**
	 * Sets the type value for this ReportId.
	 * 
	 * @param type
	 */
	public void setType(org.eclipse.birt.report.soapengine.api.ReportIdType type) {
		this.type = type;
	}

	/**
	 * Gets the rptElementId value for this ReportId.
	 * 
	 * @return rptElementId
	 */
	public java.lang.Long getRptElementId() {
		return rptElementId;
	}

	/**
	 * Sets the rptElementId value for this ReportId.
	 * 
	 * @param rptElementId
	 */
	public void setRptElementId(java.lang.Long rptElementId) {
		this.rptElementId = rptElementId;
	}

	private java.lang.Object __equalsCalc = null;

	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof ReportId))
			return false;
		ReportId other = (ReportId) obj;
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
				&& ((this.id == null && other.getId() == null) || (this.id != null && this.id.equals(other.getId())))
				&& ((this.type == null && other.getType() == null)
						|| (this.type != null && this.type.equals(other.getType())))
				&& ((this.rptElementId == null && other.getRptElementId() == null)
						|| (this.rptElementId != null && this.rptElementId.equals(other.getRptElementId())));
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
		if (getId() != null) {
			_hashCode += getId().hashCode();
		}
		if (getType() != null) {
			_hashCode += getType().hashCode();
		}
		if (getRptElementId() != null) {
			_hashCode += getRptElementId().hashCode();
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	// Type metadata
	private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(
			ReportId.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ReportId"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("id");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Id"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("type");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Type"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", ">ReportId>Type"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("rptElementId");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "RptElementId"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
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

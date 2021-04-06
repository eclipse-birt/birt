/**
 * UpdateContent.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

public class UpdateContent implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private java.lang.String target;
	private java.lang.String content;
	private org.eclipse.birt.report.soapengine.api.ReportId[] initializationId;
	private java.lang.String bookmark;

	public UpdateContent() {
	}

	public UpdateContent(java.lang.String target, java.lang.String content,
			org.eclipse.birt.report.soapengine.api.ReportId[] initializationId, java.lang.String bookmark) {
		this.target = target;
		this.content = content;
		this.initializationId = initializationId;
		this.bookmark = bookmark;
	}

	/**
	 * Gets the target value for this UpdateContent.
	 * 
	 * @return target
	 */
	public java.lang.String getTarget() {
		return target;
	}

	/**
	 * Sets the target value for this UpdateContent.
	 * 
	 * @param target
	 */
	public void setTarget(java.lang.String target) {
		this.target = target;
	}

	/**
	 * Gets the content value for this UpdateContent.
	 * 
	 * @return content
	 */
	public java.lang.String getContent() {
		return content;
	}

	/**
	 * Sets the content value for this UpdateContent.
	 * 
	 * @param content
	 */
	public void setContent(java.lang.String content) {
		this.content = content;
	}

	/**
	 * Gets the initializationId value for this UpdateContent.
	 * 
	 * @return initializationId
	 */
	public org.eclipse.birt.report.soapengine.api.ReportId[] getInitializationId() {
		return initializationId;
	}

	/**
	 * Sets the initializationId value for this UpdateContent.
	 * 
	 * @param initializationId
	 */
	public void setInitializationId(org.eclipse.birt.report.soapengine.api.ReportId[] initializationId) {
		this.initializationId = initializationId;
	}

	public org.eclipse.birt.report.soapengine.api.ReportId getInitializationId(int i) {
		return this.initializationId[i];
	}

	public void setInitializationId(int i, org.eclipse.birt.report.soapengine.api.ReportId _value) {
		this.initializationId[i] = _value;
	}

	/**
	 * Gets the bookmark value for this UpdateContent.
	 * 
	 * @return bookmark
	 */
	public java.lang.String getBookmark() {
		return bookmark;
	}

	/**
	 * Sets the bookmark value for this UpdateContent.
	 * 
	 * @param bookmark
	 */
	public void setBookmark(java.lang.String bookmark) {
		this.bookmark = bookmark;
	}

	private java.lang.Object __equalsCalc = null;

	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof UpdateContent))
			return false;
		UpdateContent other = (UpdateContent) obj;
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
				&& ((this.target == null && other.getTarget() == null)
						|| (this.target != null && this.target.equals(other.getTarget())))
				&& ((this.content == null && other.getContent() == null)
						|| (this.content != null && this.content.equals(other.getContent())))
				&& ((this.initializationId == null && other.getInitializationId() == null)
						|| (this.initializationId != null
								&& java.util.Arrays.equals(this.initializationId, other.getInitializationId())))
				&& ((this.bookmark == null && other.getBookmark() == null)
						|| (this.bookmark != null && this.bookmark.equals(other.getBookmark())));
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
		if (getTarget() != null) {
			_hashCode += getTarget().hashCode();
		}
		if (getContent() != null) {
			_hashCode += getContent().hashCode();
		}
		if (getInitializationId() != null) {
			for (int i = 0; i < java.lang.reflect.Array.getLength(getInitializationId()); i++) {
				java.lang.Object obj = java.lang.reflect.Array.get(getInitializationId(), i);
				if (obj != null && !obj.getClass().isArray()) {
					_hashCode += obj.hashCode();
				}
			}
		}
		if (getBookmark() != null) {
			_hashCode += getBookmark().hashCode();
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	// Type metadata
	private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(
			UpdateContent.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "UpdateContent"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("target");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Target"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("content");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Content"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("initializationId");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "InitializationId"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ReportId"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		elemField.setMaxOccursUnbounded(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("bookmark");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Bookmark"));
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

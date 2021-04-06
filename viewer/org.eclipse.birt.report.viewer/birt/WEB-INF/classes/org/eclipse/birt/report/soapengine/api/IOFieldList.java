/**
 * IOFieldList.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

public class IOFieldList implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private java.lang.String IOFullPath;
	private org.eclipse.birt.report.soapengine.api.IOField[] fields;

	public IOFieldList() {
	}

	public IOFieldList(java.lang.String IOFullPath, org.eclipse.birt.report.soapengine.api.IOField[] fields) {
		this.IOFullPath = IOFullPath;
		this.fields = fields;
	}

	/**
	 * Gets the IOFullPath value for this IOFieldList.
	 * 
	 * @return IOFullPath
	 */
	public java.lang.String getIOFullPath() {
		return IOFullPath;
	}

	/**
	 * Sets the IOFullPath value for this IOFieldList.
	 * 
	 * @param IOFullPath
	 */
	public void setIOFullPath(java.lang.String IOFullPath) {
		this.IOFullPath = IOFullPath;
	}

	/**
	 * Gets the fields value for this IOFieldList.
	 * 
	 * @return fields
	 */
	public org.eclipse.birt.report.soapengine.api.IOField[] getFields() {
		return fields;
	}

	/**
	 * Sets the fields value for this IOFieldList.
	 * 
	 * @param fields
	 */
	public void setFields(org.eclipse.birt.report.soapengine.api.IOField[] fields) {
		this.fields = fields;
	}

	public org.eclipse.birt.report.soapengine.api.IOField getFields(int i) {
		return this.fields[i];
	}

	public void setFields(int i, org.eclipse.birt.report.soapengine.api.IOField _value) {
		this.fields[i] = _value;
	}

	private java.lang.Object __equalsCalc = null;

	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof IOFieldList))
			return false;
		IOFieldList other = (IOFieldList) obj;
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
				&& ((this.IOFullPath == null && other.getIOFullPath() == null)
						|| (this.IOFullPath != null && this.IOFullPath.equals(other.getIOFullPath())))
				&& ((this.fields == null && other.getFields() == null)
						|| (this.fields != null && java.util.Arrays.equals(this.fields, other.getFields())));
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
		if (getIOFullPath() != null) {
			_hashCode += getIOFullPath().hashCode();
		}
		if (getFields() != null) {
			for (int i = 0; i < java.lang.reflect.Array.getLength(getFields()); i++) {
				java.lang.Object obj = java.lang.reflect.Array.get(getFields(), i);
				if (obj != null && !obj.getClass().isArray()) {
					_hashCode += obj.hashCode();
				}
			}
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	// Type metadata
	private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(
			IOFieldList.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "IOFieldList"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("IOFullPath");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "IOFullPath"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("fields");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Fields"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "IOField"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		elemField.setMaxOccursUnbounded(true);
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

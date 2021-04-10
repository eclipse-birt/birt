/**
 * ThemeList.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

public class ThemeList implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private org.eclipse.birt.report.soapengine.api.Theme[] theme;

	public ThemeList() {
	}

	public ThemeList(org.eclipse.birt.report.soapengine.api.Theme[] theme) {
		this.theme = theme;
	}

	/**
	 * Gets the theme value for this ThemeList.
	 * 
	 * @return theme
	 */
	public org.eclipse.birt.report.soapengine.api.Theme[] getTheme() {
		return theme;
	}

	/**
	 * Sets the theme value for this ThemeList.
	 * 
	 * @param theme
	 */
	public void setTheme(org.eclipse.birt.report.soapengine.api.Theme[] theme) {
		this.theme = theme;
	}

	public org.eclipse.birt.report.soapengine.api.Theme getTheme(int i) {
		return this.theme[i];
	}

	public void setTheme(int i, org.eclipse.birt.report.soapengine.api.Theme _value) {
		this.theme[i] = _value;
	}

	private java.lang.Object __equalsCalc = null;

	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof ThemeList))
			return false;
		ThemeList other = (ThemeList) obj;
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (__equalsCalc != null) {
			return (__equalsCalc == obj);
		}
		__equalsCalc = obj;
		boolean _equals;
		_equals = true && ((this.theme == null && other.getTheme() == null)
				|| (this.theme != null && java.util.Arrays.equals(this.theme, other.getTheme())));
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
		if (getTheme() != null) {
			for (int i = 0; i < java.lang.reflect.Array.getLength(getTheme()); i++) {
				java.lang.Object obj = java.lang.reflect.Array.get(getTheme(), i);
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
			ThemeList.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ThemeList"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("theme");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Theme"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Theme"));
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

/**
 * TableSections.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

/**
 * list of S_Info
 */
public class TableSections implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private org.eclipse.birt.report.soapengine.api.S_Info[] section;

	public TableSections() {
	}

	public TableSections(org.eclipse.birt.report.soapengine.api.S_Info[] section) {
		this.section = section;
	}

	/**
	 * Gets the section value for this TableSections.
	 * 
	 * @return section
	 */
	public org.eclipse.birt.report.soapengine.api.S_Info[] getSection() {
		return section;
	}

	/**
	 * Sets the section value for this TableSections.
	 * 
	 * @param section
	 */
	public void setSection(org.eclipse.birt.report.soapengine.api.S_Info[] section) {
		this.section = section;
	}

	public org.eclipse.birt.report.soapengine.api.S_Info getSection(int i) {
		return this.section[i];
	}

	public void setSection(int i, org.eclipse.birt.report.soapengine.api.S_Info _value) {
		this.section[i] = _value;
	}

	private java.lang.Object __equalsCalc = null;

	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof TableSections))
			return false;
		TableSections other = (TableSections) obj;
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (__equalsCalc != null) {
			return (__equalsCalc == obj);
		}
		__equalsCalc = obj;
		boolean _equals;
		_equals = true && ((this.section == null && other.getSection() == null)
				|| (this.section != null && java.util.Arrays.equals(this.section, other.getSection())));
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
		if (getSection() != null) {
			for (int i = 0; i < java.lang.reflect.Array.getLength(getSection()); i++) {
				java.lang.Object obj = java.lang.reflect.Array.get(getSection(), i);
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
			TableSections.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "TableSections"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("section");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Section"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "S_Info"));
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

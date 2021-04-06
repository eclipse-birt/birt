/**
 * TableGroups.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

/**
 * list of G_Info
 */
public class TableGroups implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private org.eclipse.birt.report.soapengine.api.G_Info[] group;

	public TableGroups() {
	}

	public TableGroups(org.eclipse.birt.report.soapengine.api.G_Info[] group) {
		this.group = group;
	}

	/**
	 * Gets the group value for this TableGroups.
	 * 
	 * @return group
	 */
	public org.eclipse.birt.report.soapengine.api.G_Info[] getGroup() {
		return group;
	}

	/**
	 * Sets the group value for this TableGroups.
	 * 
	 * @param group
	 */
	public void setGroup(org.eclipse.birt.report.soapengine.api.G_Info[] group) {
		this.group = group;
	}

	public org.eclipse.birt.report.soapengine.api.G_Info getGroup(int i) {
		return this.group[i];
	}

	public void setGroup(int i, org.eclipse.birt.report.soapengine.api.G_Info _value) {
		this.group[i] = _value;
	}

	private java.lang.Object __equalsCalc = null;

	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof TableGroups))
			return false;
		TableGroups other = (TableGroups) obj;
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (__equalsCalc != null) {
			return (__equalsCalc == obj);
		}
		__equalsCalc = obj;
		boolean _equals;
		_equals = true && ((this.group == null && other.getGroup() == null)
				|| (this.group != null && java.util.Arrays.equals(this.group, other.getGroup())));
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
		if (getGroup() != null) {
			for (int i = 0; i < java.lang.reflect.Array.getLength(getGroup()); i++) {
				java.lang.Object obj = java.lang.reflect.Array.get(getGroup(), i);
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
			TableGroups.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "TableGroups"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("group");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Group"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "G_Info"));
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

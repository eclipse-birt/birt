/**
 * CategoryChoiceList.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

public class CategoryChoiceList implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private org.eclipse.birt.report.soapengine.api.CategoryChoice[] categoryChoice;

	public CategoryChoiceList() {
	}

	public CategoryChoiceList(org.eclipse.birt.report.soapengine.api.CategoryChoice[] categoryChoice) {
		this.categoryChoice = categoryChoice;
	}

	/**
	 * Gets the categoryChoice value for this CategoryChoiceList.
	 * 
	 * @return categoryChoice
	 */
	public org.eclipse.birt.report.soapengine.api.CategoryChoice[] getCategoryChoice() {
		return categoryChoice;
	}

	/**
	 * Sets the categoryChoice value for this CategoryChoiceList.
	 * 
	 * @param categoryChoice
	 */
	public void setCategoryChoice(org.eclipse.birt.report.soapengine.api.CategoryChoice[] categoryChoice) {
		this.categoryChoice = categoryChoice;
	}

	public org.eclipse.birt.report.soapengine.api.CategoryChoice getCategoryChoice(int i) {
		return this.categoryChoice[i];
	}

	public void setCategoryChoice(int i, org.eclipse.birt.report.soapengine.api.CategoryChoice _value) {
		this.categoryChoice[i] = _value;
	}

	private java.lang.Object __equalsCalc = null;

	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof CategoryChoiceList))
			return false;
		CategoryChoiceList other = (CategoryChoiceList) obj;
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
				&& ((this.categoryChoice == null && other.getCategoryChoice() == null) || (this.categoryChoice != null
						&& java.util.Arrays.equals(this.categoryChoice, other.getCategoryChoice())));
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
		if (getCategoryChoice() != null) {
			for (int i = 0; i < java.lang.reflect.Array.getLength(getCategoryChoice()); i++) {
				java.lang.Object obj = java.lang.reflect.Array.get(getCategoryChoice(), i);
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
			CategoryChoiceList.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "CategoryChoiceList"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("categoryChoice");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "CategoryChoice"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "CategoryChoice"));
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

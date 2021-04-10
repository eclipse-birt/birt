/**
 * SortDefinition.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

public class SortDefinition implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private int columnIndex;
	private org.eclipse.birt.report.soapengine.api.SortingDirection sortDir;

	public SortDefinition() {
	}

	public SortDefinition(int columnIndex, org.eclipse.birt.report.soapengine.api.SortingDirection sortDir) {
		this.columnIndex = columnIndex;
		this.sortDir = sortDir;
	}

	/**
	 * Gets the columnIndex value for this SortDefinition.
	 * 
	 * @return columnIndex
	 */
	public int getColumnIndex() {
		return columnIndex;
	}

	/**
	 * Sets the columnIndex value for this SortDefinition.
	 * 
	 * @param columnIndex
	 */
	public void setColumnIndex(int columnIndex) {
		this.columnIndex = columnIndex;
	}

	/**
	 * Gets the sortDir value for this SortDefinition.
	 * 
	 * @return sortDir
	 */
	public org.eclipse.birt.report.soapengine.api.SortingDirection getSortDir() {
		return sortDir;
	}

	/**
	 * Sets the sortDir value for this SortDefinition.
	 * 
	 * @param sortDir
	 */
	public void setSortDir(org.eclipse.birt.report.soapengine.api.SortingDirection sortDir) {
		this.sortDir = sortDir;
	}

	private java.lang.Object __equalsCalc = null;

	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof SortDefinition))
			return false;
		SortDefinition other = (SortDefinition) obj;
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (__equalsCalc != null) {
			return (__equalsCalc == obj);
		}
		__equalsCalc = obj;
		boolean _equals;
		_equals = true && this.columnIndex == other.getColumnIndex()
				&& ((this.sortDir == null && other.getSortDir() == null)
						|| (this.sortDir != null && this.sortDir.equals(other.getSortDir())));
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
		_hashCode += getColumnIndex();
		if (getSortDir() != null) {
			_hashCode += getSortDir().hashCode();
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	// Type metadata
	private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(
			SortDefinition.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "SortDefinition"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("columnIndex");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ColumnIndex"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("sortDir");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "SortDir"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "SortingDirection"));
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

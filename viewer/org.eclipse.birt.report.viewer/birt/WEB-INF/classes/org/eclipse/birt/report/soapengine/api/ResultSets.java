/**
 * ResultSets.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

public class ResultSets implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private org.eclipse.birt.report.soapengine.api.ResultSet[] resultSet;

	public ResultSets() {
	}

	public ResultSets(org.eclipse.birt.report.soapengine.api.ResultSet[] resultSet) {
		this.resultSet = resultSet;
	}

	/**
	 * Gets the resultSet value for this ResultSets.
	 * 
	 * @return resultSet
	 */
	public org.eclipse.birt.report.soapengine.api.ResultSet[] getResultSet() {
		return resultSet;
	}

	/**
	 * Sets the resultSet value for this ResultSets.
	 * 
	 * @param resultSet
	 */
	public void setResultSet(org.eclipse.birt.report.soapengine.api.ResultSet[] resultSet) {
		this.resultSet = resultSet;
	}

	public org.eclipse.birt.report.soapengine.api.ResultSet getResultSet(int i) {
		return this.resultSet[i];
	}

	public void setResultSet(int i, org.eclipse.birt.report.soapengine.api.ResultSet _value) {
		this.resultSet[i] = _value;
	}

	private java.lang.Object __equalsCalc = null;

	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof ResultSets))
			return false;
		ResultSets other = (ResultSets) obj;
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (__equalsCalc != null) {
			return (__equalsCalc == obj);
		}
		__equalsCalc = obj;
		boolean _equals;
		_equals = true && ((this.resultSet == null && other.getResultSet() == null)
				|| (this.resultSet != null && java.util.Arrays.equals(this.resultSet, other.getResultSet())));
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
		if (getResultSet() != null) {
			for (int i = 0; i < java.lang.reflect.Array.getLength(getResultSet()); i++) {
				java.lang.Object obj = java.lang.reflect.Array.get(getResultSet(), i);
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
			ResultSets.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ResultSets"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("resultSet");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ResultSet"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ResultSet"));
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

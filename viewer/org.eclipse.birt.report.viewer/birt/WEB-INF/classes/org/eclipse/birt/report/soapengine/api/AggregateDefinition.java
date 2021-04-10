/**
 * AggregateDefinition.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

public class AggregateDefinition implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private java.lang.String func;
	private org.eclipse.birt.report.soapengine.api.AggregateSetting subTotal;
	private org.eclipse.birt.report.soapengine.api.AggregateSetting grandTotal;
	private java.lang.Integer intParameter;
	private java.lang.String sortDir;

	public AggregateDefinition() {
	}

	public AggregateDefinition(java.lang.String func, org.eclipse.birt.report.soapengine.api.AggregateSetting subTotal,
			org.eclipse.birt.report.soapengine.api.AggregateSetting grandTotal, java.lang.Integer intParameter,
			java.lang.String sortDir) {
		this.func = func;
		this.subTotal = subTotal;
		this.grandTotal = grandTotal;
		this.intParameter = intParameter;
		this.sortDir = sortDir;
	}

	/**
	 * Gets the func value for this AggregateDefinition.
	 * 
	 * @return func
	 */
	public java.lang.String getFunc() {
		return func;
	}

	/**
	 * Sets the func value for this AggregateDefinition.
	 * 
	 * @param func
	 */
	public void setFunc(java.lang.String func) {
		this.func = func;
	}

	/**
	 * Gets the subTotal value for this AggregateDefinition.
	 * 
	 * @return subTotal
	 */
	public org.eclipse.birt.report.soapengine.api.AggregateSetting getSubTotal() {
		return subTotal;
	}

	/**
	 * Sets the subTotal value for this AggregateDefinition.
	 * 
	 * @param subTotal
	 */
	public void setSubTotal(org.eclipse.birt.report.soapengine.api.AggregateSetting subTotal) {
		this.subTotal = subTotal;
	}

	/**
	 * Gets the grandTotal value for this AggregateDefinition.
	 * 
	 * @return grandTotal
	 */
	public org.eclipse.birt.report.soapengine.api.AggregateSetting getGrandTotal() {
		return grandTotal;
	}

	/**
	 * Sets the grandTotal value for this AggregateDefinition.
	 * 
	 * @param grandTotal
	 */
	public void setGrandTotal(org.eclipse.birt.report.soapengine.api.AggregateSetting grandTotal) {
		this.grandTotal = grandTotal;
	}

	/**
	 * Gets the intParameter value for this AggregateDefinition.
	 * 
	 * @return intParameter
	 */
	public java.lang.Integer getIntParameter() {
		return intParameter;
	}

	/**
	 * Sets the intParameter value for this AggregateDefinition.
	 * 
	 * @param intParameter
	 */
	public void setIntParameter(java.lang.Integer intParameter) {
		this.intParameter = intParameter;
	}

	/**
	 * Gets the sortDir value for this AggregateDefinition.
	 * 
	 * @return sortDir
	 */
	public java.lang.String getSortDir() {
		return sortDir;
	}

	/**
	 * Sets the sortDir value for this AggregateDefinition.
	 * 
	 * @param sortDir
	 */
	public void setSortDir(java.lang.String sortDir) {
		this.sortDir = sortDir;
	}

	private java.lang.Object __equalsCalc = null;

	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof AggregateDefinition))
			return false;
		AggregateDefinition other = (AggregateDefinition) obj;
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
				&& ((this.func == null && other.getFunc() == null)
						|| (this.func != null && this.func.equals(other.getFunc())))
				&& ((this.subTotal == null && other.getSubTotal() == null)
						|| (this.subTotal != null && this.subTotal.equals(other.getSubTotal())))
				&& ((this.grandTotal == null && other.getGrandTotal() == null)
						|| (this.grandTotal != null && this.grandTotal.equals(other.getGrandTotal())))
				&& ((this.intParameter == null && other.getIntParameter() == null)
						|| (this.intParameter != null && this.intParameter.equals(other.getIntParameter())))
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
		if (getFunc() != null) {
			_hashCode += getFunc().hashCode();
		}
		if (getSubTotal() != null) {
			_hashCode += getSubTotal().hashCode();
		}
		if (getGrandTotal() != null) {
			_hashCode += getGrandTotal().hashCode();
		}
		if (getIntParameter() != null) {
			_hashCode += getIntParameter().hashCode();
		}
		if (getSortDir() != null) {
			_hashCode += getSortDir().hashCode();
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	// Type metadata
	private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(
			AggregateDefinition.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "AggregateDefinition"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("func");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Func"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("subTotal");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "SubTotal"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "AggregateSetting"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("grandTotal");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "GrandTotal"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "AggregateSetting"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("intParameter");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "IntParameter"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("sortDir");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "SortDir"));
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

/**
 * NumberCategoryChoice.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

public class NumberCategoryChoice implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private org.eclipse.birt.report.soapengine.api.CategoryChoice categoryChoice;
	private org.eclipse.birt.report.soapengine.api.Vector decimalPlaces;
	private org.eclipse.birt.report.soapengine.api.Vector symbol;
	private org.eclipse.birt.report.soapengine.api.Vector symbolPosition;
	private org.eclipse.birt.report.soapengine.api.Vector negativeNumberFormat;
	private java.lang.Boolean useSeparator;

	public NumberCategoryChoice() {
	}

	public NumberCategoryChoice(org.eclipse.birt.report.soapengine.api.CategoryChoice categoryChoice,
			org.eclipse.birt.report.soapengine.api.Vector decimalPlaces,
			org.eclipse.birt.report.soapengine.api.Vector symbol,
			org.eclipse.birt.report.soapengine.api.Vector symbolPosition,
			org.eclipse.birt.report.soapengine.api.Vector negativeNumberFormat, java.lang.Boolean useSeparator) {
		this.categoryChoice = categoryChoice;
		this.decimalPlaces = decimalPlaces;
		this.symbol = symbol;
		this.symbolPosition = symbolPosition;
		this.negativeNumberFormat = negativeNumberFormat;
		this.useSeparator = useSeparator;
	}

	/**
	 * Gets the categoryChoice value for this NumberCategoryChoice.
	 * 
	 * @return categoryChoice
	 */
	public org.eclipse.birt.report.soapengine.api.CategoryChoice getCategoryChoice() {
		return categoryChoice;
	}

	/**
	 * Sets the categoryChoice value for this NumberCategoryChoice.
	 * 
	 * @param categoryChoice
	 */
	public void setCategoryChoice(org.eclipse.birt.report.soapengine.api.CategoryChoice categoryChoice) {
		this.categoryChoice = categoryChoice;
	}

	/**
	 * Gets the decimalPlaces value for this NumberCategoryChoice.
	 * 
	 * @return decimalPlaces
	 */
	public org.eclipse.birt.report.soapengine.api.Vector getDecimalPlaces() {
		return decimalPlaces;
	}

	/**
	 * Sets the decimalPlaces value for this NumberCategoryChoice.
	 * 
	 * @param decimalPlaces
	 */
	public void setDecimalPlaces(org.eclipse.birt.report.soapengine.api.Vector decimalPlaces) {
		this.decimalPlaces = decimalPlaces;
	}

	/**
	 * Gets the symbol value for this NumberCategoryChoice.
	 * 
	 * @return symbol
	 */
	public org.eclipse.birt.report.soapengine.api.Vector getSymbol() {
		return symbol;
	}

	/**
	 * Sets the symbol value for this NumberCategoryChoice.
	 * 
	 * @param symbol
	 */
	public void setSymbol(org.eclipse.birt.report.soapengine.api.Vector symbol) {
		this.symbol = symbol;
	}

	/**
	 * Gets the symbolPosition value for this NumberCategoryChoice.
	 * 
	 * @return symbolPosition
	 */
	public org.eclipse.birt.report.soapengine.api.Vector getSymbolPosition() {
		return symbolPosition;
	}

	/**
	 * Sets the symbolPosition value for this NumberCategoryChoice.
	 * 
	 * @param symbolPosition
	 */
	public void setSymbolPosition(org.eclipse.birt.report.soapengine.api.Vector symbolPosition) {
		this.symbolPosition = symbolPosition;
	}

	/**
	 * Gets the negativeNumberFormat value for this NumberCategoryChoice.
	 * 
	 * @return negativeNumberFormat
	 */
	public org.eclipse.birt.report.soapengine.api.Vector getNegativeNumberFormat() {
		return negativeNumberFormat;
	}

	/**
	 * Sets the negativeNumberFormat value for this NumberCategoryChoice.
	 * 
	 * @param negativeNumberFormat
	 */
	public void setNegativeNumberFormat(org.eclipse.birt.report.soapengine.api.Vector negativeNumberFormat) {
		this.negativeNumberFormat = negativeNumberFormat;
	}

	/**
	 * Gets the useSeparator value for this NumberCategoryChoice.
	 * 
	 * @return useSeparator
	 */
	public java.lang.Boolean getUseSeparator() {
		return useSeparator;
	}

	/**
	 * Sets the useSeparator value for this NumberCategoryChoice.
	 * 
	 * @param useSeparator
	 */
	public void setUseSeparator(java.lang.Boolean useSeparator) {
		this.useSeparator = useSeparator;
	}

	private java.lang.Object __equalsCalc = null;

	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof NumberCategoryChoice))
			return false;
		NumberCategoryChoice other = (NumberCategoryChoice) obj;
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
				&& ((this.categoryChoice == null && other.getCategoryChoice() == null)
						|| (this.categoryChoice != null && this.categoryChoice.equals(other.getCategoryChoice())))
				&& ((this.decimalPlaces == null && other.getDecimalPlaces() == null)
						|| (this.decimalPlaces != null && this.decimalPlaces.equals(other.getDecimalPlaces())))
				&& ((this.symbol == null && other.getSymbol() == null)
						|| (this.symbol != null && this.symbol.equals(other.getSymbol())))
				&& ((this.symbolPosition == null && other.getSymbolPosition() == null)
						|| (this.symbolPosition != null && this.symbolPosition.equals(other.getSymbolPosition())))
				&& ((this.negativeNumberFormat == null && other.getNegativeNumberFormat() == null)
						|| (this.negativeNumberFormat != null
								&& this.negativeNumberFormat.equals(other.getNegativeNumberFormat())))
				&& ((this.useSeparator == null && other.getUseSeparator() == null)
						|| (this.useSeparator != null && this.useSeparator.equals(other.getUseSeparator())));
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
			_hashCode += getCategoryChoice().hashCode();
		}
		if (getDecimalPlaces() != null) {
			_hashCode += getDecimalPlaces().hashCode();
		}
		if (getSymbol() != null) {
			_hashCode += getSymbol().hashCode();
		}
		if (getSymbolPosition() != null) {
			_hashCode += getSymbolPosition().hashCode();
		}
		if (getNegativeNumberFormat() != null) {
			_hashCode += getNegativeNumberFormat().hashCode();
		}
		if (getUseSeparator() != null) {
			_hashCode += getUseSeparator().hashCode();
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	// Type metadata
	private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(
			NumberCategoryChoice.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "NumberCategoryChoice"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("categoryChoice");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "CategoryChoice"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "CategoryChoice"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("decimalPlaces");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "DecimalPlaces"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Vector"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("symbol");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Symbol"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Vector"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("symbolPosition");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "SymbolPosition"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Vector"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("negativeNumberFormat");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "NegativeNumberFormat"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Vector"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("useSeparator");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "UseSeparator"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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

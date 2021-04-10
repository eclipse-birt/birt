/**
 * FormatRuleEffect.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

public class FormatRuleEffect implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private java.lang.String font;
	private java.lang.Double size;
	private java.lang.String color;
	private java.lang.String backgroundColor;
	private java.lang.Boolean bold;
	private java.lang.Boolean italic;
	private java.lang.Boolean underline;

	public FormatRuleEffect() {
	}

	public FormatRuleEffect(java.lang.String font, java.lang.Double size, java.lang.String color,
			java.lang.String backgroundColor, java.lang.Boolean bold, java.lang.Boolean italic,
			java.lang.Boolean underline) {
		this.font = font;
		this.size = size;
		this.color = color;
		this.backgroundColor = backgroundColor;
		this.bold = bold;
		this.italic = italic;
		this.underline = underline;
	}

	/**
	 * Gets the font value for this FormatRuleEffect.
	 * 
	 * @return font
	 */
	public java.lang.String getFont() {
		return font;
	}

	/**
	 * Sets the font value for this FormatRuleEffect.
	 * 
	 * @param font
	 */
	public void setFont(java.lang.String font) {
		this.font = font;
	}

	/**
	 * Gets the size value for this FormatRuleEffect.
	 * 
	 * @return size
	 */
	public java.lang.Double getSize() {
		return size;
	}

	/**
	 * Sets the size value for this FormatRuleEffect.
	 * 
	 * @param size
	 */
	public void setSize(java.lang.Double size) {
		this.size = size;
	}

	/**
	 * Gets the color value for this FormatRuleEffect.
	 * 
	 * @return color
	 */
	public java.lang.String getColor() {
		return color;
	}

	/**
	 * Sets the color value for this FormatRuleEffect.
	 * 
	 * @param color
	 */
	public void setColor(java.lang.String color) {
		this.color = color;
	}

	/**
	 * Gets the backgroundColor value for this FormatRuleEffect.
	 * 
	 * @return backgroundColor
	 */
	public java.lang.String getBackgroundColor() {
		return backgroundColor;
	}

	/**
	 * Sets the backgroundColor value for this FormatRuleEffect.
	 * 
	 * @param backgroundColor
	 */
	public void setBackgroundColor(java.lang.String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	/**
	 * Gets the bold value for this FormatRuleEffect.
	 * 
	 * @return bold
	 */
	public java.lang.Boolean getBold() {
		return bold;
	}

	/**
	 * Sets the bold value for this FormatRuleEffect.
	 * 
	 * @param bold
	 */
	public void setBold(java.lang.Boolean bold) {
		this.bold = bold;
	}

	/**
	 * Gets the italic value for this FormatRuleEffect.
	 * 
	 * @return italic
	 */
	public java.lang.Boolean getItalic() {
		return italic;
	}

	/**
	 * Sets the italic value for this FormatRuleEffect.
	 * 
	 * @param italic
	 */
	public void setItalic(java.lang.Boolean italic) {
		this.italic = italic;
	}

	/**
	 * Gets the underline value for this FormatRuleEffect.
	 * 
	 * @return underline
	 */
	public java.lang.Boolean getUnderline() {
		return underline;
	}

	/**
	 * Sets the underline value for this FormatRuleEffect.
	 * 
	 * @param underline
	 */
	public void setUnderline(java.lang.Boolean underline) {
		this.underline = underline;
	}

	private java.lang.Object __equalsCalc = null;

	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof FormatRuleEffect))
			return false;
		FormatRuleEffect other = (FormatRuleEffect) obj;
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
				&& ((this.font == null && other.getFont() == null)
						|| (this.font != null && this.font.equals(other.getFont())))
				&& ((this.size == null && other.getSize() == null)
						|| (this.size != null && this.size.equals(other.getSize())))
				&& ((this.color == null && other.getColor() == null)
						|| (this.color != null && this.color.equals(other.getColor())))
				&& ((this.backgroundColor == null && other.getBackgroundColor() == null)
						|| (this.backgroundColor != null && this.backgroundColor.equals(other.getBackgroundColor())))
				&& ((this.bold == null && other.getBold() == null)
						|| (this.bold != null && this.bold.equals(other.getBold())))
				&& ((this.italic == null && other.getItalic() == null)
						|| (this.italic != null && this.italic.equals(other.getItalic())))
				&& ((this.underline == null && other.getUnderline() == null)
						|| (this.underline != null && this.underline.equals(other.getUnderline())));
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
		if (getFont() != null) {
			_hashCode += getFont().hashCode();
		}
		if (getSize() != null) {
			_hashCode += getSize().hashCode();
		}
		if (getColor() != null) {
			_hashCode += getColor().hashCode();
		}
		if (getBackgroundColor() != null) {
			_hashCode += getBackgroundColor().hashCode();
		}
		if (getBold() != null) {
			_hashCode += getBold().hashCode();
		}
		if (getItalic() != null) {
			_hashCode += getItalic().hashCode();
		}
		if (getUnderline() != null) {
			_hashCode += getUnderline().hashCode();
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	// Type metadata
	private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(
			FormatRuleEffect.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "FormatRuleEffect"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("font");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Font"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("size");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Size"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("color");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Color"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("backgroundColor");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "BackgroundColor"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("bold");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Bold"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("italic");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Italic"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("underline");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Underline"));
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

/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/
/**
 * Font.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

public class Font implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private java.lang.String family;
	private java.lang.String style;
	private java.lang.Integer size;
	private java.lang.String effect;
	private java.lang.String _super;
	private java.lang.String weight;
	private java.lang.String color;
	private java.lang.String backgroundColor;
	private java.lang.Boolean bold;
	private java.lang.Boolean italic;
	private java.lang.Boolean underline;
	private java.lang.Boolean lineThrough;

	public Font() {
	}

	public Font(java.lang.String family, java.lang.String style, java.lang.Integer size, java.lang.String effect,
			java.lang.String _super, java.lang.String weight, java.lang.String color, java.lang.String backgroundColor,
			java.lang.Boolean bold, java.lang.Boolean italic, java.lang.Boolean underline,
			java.lang.Boolean lineThrough) {
		this.family = family;
		this.style = style;
		this.size = size;
		this.effect = effect;
		this._super = _super;
		this.weight = weight;
		this.color = color;
		this.backgroundColor = backgroundColor;
		this.bold = bold;
		this.italic = italic;
		this.underline = underline;
		this.lineThrough = lineThrough;
	}

	/**
	 * Gets the family value for this Font.
	 * 
	 * @return family
	 */
	public java.lang.String getFamily() {
		return family;
	}

	/**
	 * Sets the family value for this Font.
	 * 
	 * @param family
	 */
	public void setFamily(java.lang.String family) {
		this.family = family;
	}

	/**
	 * Gets the style value for this Font.
	 * 
	 * @return style
	 */
	public java.lang.String getStyle() {
		return style;
	}

	/**
	 * Sets the style value for this Font.
	 * 
	 * @param style
	 */
	public void setStyle(java.lang.String style) {
		this.style = style;
	}

	/**
	 * Gets the size value for this Font.
	 * 
	 * @return size
	 */
	public java.lang.Integer getSize() {
		return size;
	}

	/**
	 * Sets the size value for this Font.
	 * 
	 * @param size
	 */
	public void setSize(java.lang.Integer size) {
		this.size = size;
	}

	/**
	 * Gets the effect value for this Font.
	 * 
	 * @return effect
	 */
	public java.lang.String getEffect() {
		return effect;
	}

	/**
	 * Sets the effect value for this Font.
	 * 
	 * @param effect
	 */
	public void setEffect(java.lang.String effect) {
		this.effect = effect;
	}

	/**
	 * Gets the _super value for this Font.
	 * 
	 * @return _super
	 */
	public java.lang.String get_super() {
		return _super;
	}

	/**
	 * Sets the _super value for this Font.
	 * 
	 * @param _super
	 */
	public void set_super(java.lang.String _super) {
		this._super = _super;
	}

	/**
	 * Gets the weight value for this Font.
	 * 
	 * @return weight
	 */
	public java.lang.String getWeight() {
		return weight;
	}

	/**
	 * Sets the weight value for this Font.
	 * 
	 * @param weight
	 */
	public void setWeight(java.lang.String weight) {
		this.weight = weight;
	}

	/**
	 * Gets the color value for this Font.
	 * 
	 * @return color
	 */
	public java.lang.String getColor() {
		return color;
	}

	/**
	 * Sets the color value for this Font.
	 * 
	 * @param color
	 */
	public void setColor(java.lang.String color) {
		this.color = color;
	}

	/**
	 * Gets the backgroundColor value for this Font.
	 * 
	 * @return backgroundColor
	 */
	public java.lang.String getBackgroundColor() {
		return backgroundColor;
	}

	/**
	 * Sets the backgroundColor value for this Font.
	 * 
	 * @param backgroundColor
	 */
	public void setBackgroundColor(java.lang.String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	/**
	 * Gets the bold value for this Font.
	 * 
	 * @return bold
	 */
	public java.lang.Boolean getBold() {
		return bold;
	}

	/**
	 * Sets the bold value for this Font.
	 * 
	 * @param bold
	 */
	public void setBold(java.lang.Boolean bold) {
		this.bold = bold;
	}

	/**
	 * Gets the italic value for this Font.
	 * 
	 * @return italic
	 */
	public java.lang.Boolean getItalic() {
		return italic;
	}

	/**
	 * Sets the italic value for this Font.
	 * 
	 * @param italic
	 */
	public void setItalic(java.lang.Boolean italic) {
		this.italic = italic;
	}

	/**
	 * Gets the underline value for this Font.
	 * 
	 * @return underline
	 */
	public java.lang.Boolean getUnderline() {
		return underline;
	}

	/**
	 * Sets the underline value for this Font.
	 * 
	 * @param underline
	 */
	public void setUnderline(java.lang.Boolean underline) {
		this.underline = underline;
	}

	/**
	 * Gets the lineThrough value for this Font.
	 * 
	 * @return lineThrough
	 */
	public java.lang.Boolean getLineThrough() {
		return lineThrough;
	}

	/**
	 * Sets the lineThrough value for this Font.
	 * 
	 * @param lineThrough
	 */
	public void setLineThrough(java.lang.Boolean lineThrough) {
		this.lineThrough = lineThrough;
	}

	private java.lang.Object __equalsCalc = null;

	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof Font))
			return false;
		Font other = (Font) obj;
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
				&& ((this.family == null && other.getFamily() == null)
						|| (this.family != null && this.family.equals(other.getFamily())))
				&& ((this.style == null && other.getStyle() == null)
						|| (this.style != null && this.style.equals(other.getStyle())))
				&& ((this.size == null && other.getSize() == null)
						|| (this.size != null && this.size.equals(other.getSize())))
				&& ((this.effect == null && other.getEffect() == null)
						|| (this.effect != null && this.effect.equals(other.getEffect())))
				&& ((this._super == null && other.get_super() == null)
						|| (this._super != null && this._super.equals(other.get_super())))
				&& ((this.weight == null && other.getWeight() == null)
						|| (this.weight != null && this.weight.equals(other.getWeight())))
				&& ((this.color == null && other.getColor() == null)
						|| (this.color != null && this.color.equals(other.getColor())))
				&& ((this.backgroundColor == null && other.getBackgroundColor() == null)
						|| (this.backgroundColor != null && this.backgroundColor.equals(other.getBackgroundColor())))
				&& ((this.bold == null && other.getBold() == null)
						|| (this.bold != null && this.bold.equals(other.getBold())))
				&& ((this.italic == null && other.getItalic() == null)
						|| (this.italic != null && this.italic.equals(other.getItalic())))
				&& ((this.underline == null && other.getUnderline() == null)
						|| (this.underline != null && this.underline.equals(other.getUnderline())))
				&& ((this.lineThrough == null && other.getLineThrough() == null)
						|| (this.lineThrough != null && this.lineThrough.equals(other.getLineThrough())));
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
		if (getFamily() != null) {
			_hashCode += getFamily().hashCode();
		}
		if (getStyle() != null) {
			_hashCode += getStyle().hashCode();
		}
		if (getSize() != null) {
			_hashCode += getSize().hashCode();
		}
		if (getEffect() != null) {
			_hashCode += getEffect().hashCode();
		}
		if (get_super() != null) {
			_hashCode += get_super().hashCode();
		}
		if (getWeight() != null) {
			_hashCode += getWeight().hashCode();
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
		if (getLineThrough() != null) {
			_hashCode += getLineThrough().hashCode();
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	// Type metadata
	private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(Font.class,
			true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Font"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("family");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Family"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("style");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Style"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("size");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Size"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("effect");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Effect"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("_super");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Super"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("weight");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Weight"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("lineThrough");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "LineThrough"));
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

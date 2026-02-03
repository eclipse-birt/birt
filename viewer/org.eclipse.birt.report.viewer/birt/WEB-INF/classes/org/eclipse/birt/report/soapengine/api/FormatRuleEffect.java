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
 * FormatRuleEffect.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "FormatRuleEffect")
@XmlAccessorType(XmlAccessType.NONE)
public class FormatRuleEffect implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "Font")
	private java.lang.String font;
	@XmlElement(name = "Size")
	private java.lang.Double size;
	@XmlElement(name = "Color")
	private java.lang.String color;
	@XmlElement(name = "BackgroundColor")
	private java.lang.String backgroundColor;
	@XmlElement(name = "Bold")
	private java.lang.Boolean bold;
	@XmlElement(name = "Italic")
	private java.lang.Boolean italic;
	@XmlElement(name = "Underline")
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

	@Override
	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof FormatRuleEffect)) {
			return false;
		}
		FormatRuleEffect other = (FormatRuleEffect) obj;
		if (obj == null) {
			return false;
		}
		if (this == obj) {
			return true;
		}
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

	@Override
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

	}

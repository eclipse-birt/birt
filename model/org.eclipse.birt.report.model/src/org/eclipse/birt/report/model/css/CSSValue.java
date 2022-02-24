/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.css;

import java.io.Serializable;
import java.util.Vector;

import org.w3c.css.sac.LexicalUnit;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSValueList;
import org.w3c.dom.css.Counter;
import org.w3c.dom.css.RGBColor;
import org.w3c.dom.css.Rect;

/**
 * Represents either a <code>CSSPrimitiveValue</code> or a
 * <code>CSSValueList</code>.
 */

public class CSSValue implements CSSPrimitiveValue, CSSValueList, Serializable {

	/**
	 * Document for <code>serialVersionUID</code>.
	 */

	private static final long serialVersionUID = 2788990763692505765L;

	private Object value = null;

	/**
	 * Constructor
	 * 
	 * @param value          the lexical unit of the value
	 * @param forcePrimitive status identifying whether the value is forced to be
	 *                       primitive
	 */

	public CSSValue(LexicalUnit value, boolean forcePrimitive) {
		if (value.getParameters() != null) {
			this.value = value;
		} else if (forcePrimitive || (value.getNextLexicalUnit() == null)) {

			// We need to be a CSSPrimitiveValue
			this.value = value;
		} else {

			// We need to be a CSSValueList
			// Values in an "expr" can be seperated by "operator"s, which are
			// either '/' or ',' - ignore these operators
			Vector v = new Vector();
			LexicalUnit lu = value;
			while (lu != null) {
				if ((lu.getLexicalUnitType() != LexicalUnit.SAC_OPERATOR_COMMA)
						&& (lu.getLexicalUnitType() != LexicalUnit.SAC_OPERATOR_SLASH)) {
					v.addElement(new CSSValue(lu, true));
				}
				lu = lu.getNextLexicalUnit();
			}
			this.value = v;
		}
	}

	/**
	 * Constructs the value with the lexical unit.
	 * 
	 * @param value the lexical unit to handle
	 */

	public CSSValue(LexicalUnit value) {
		this(value, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.css.CSSPrimitiveValue#getPrimitiveType()
	 */

	public short getPrimitiveType() {
		if (value instanceof LexicalUnit) {
			LexicalUnit lu = (LexicalUnit) value;
			switch (lu.getLexicalUnitType()) {
			case LexicalUnit.SAC_INHERIT:
				return CSS_IDENT;
			case LexicalUnit.SAC_INTEGER:
			case LexicalUnit.SAC_REAL:
				return CSS_NUMBER;
			case LexicalUnit.SAC_EM:
				return CSS_EMS;
			case LexicalUnit.SAC_EX:
				return CSS_EXS;
			case LexicalUnit.SAC_PIXEL:
				return CSS_PX;
			case LexicalUnit.SAC_INCH:
				return CSS_IN;
			case LexicalUnit.SAC_CENTIMETER:
				return CSS_CM;
			case LexicalUnit.SAC_MILLIMETER:
				return CSS_MM;
			case LexicalUnit.SAC_POINT:
				return CSS_PT;
			case LexicalUnit.SAC_PICA:
				return CSS_PC;
			case LexicalUnit.SAC_PERCENTAGE:
				return CSS_PERCENTAGE;
			case LexicalUnit.SAC_URI:
				return CSS_URI;
			case LexicalUnit.SAC_DEGREE:
				return CSS_DEG;
			case LexicalUnit.SAC_GRADIAN:
				return CSS_GRAD;
			case LexicalUnit.SAC_RADIAN:
				return CSS_RAD;
			case LexicalUnit.SAC_MILLISECOND:
				return CSS_MS;
			case LexicalUnit.SAC_SECOND:
				return CSS_S;
			case LexicalUnit.SAC_HERTZ:
				return CSS_KHZ;
			case LexicalUnit.SAC_KILOHERTZ:
				return CSS_HZ;
			case LexicalUnit.SAC_IDENT:
				return CSS_IDENT;
			case LexicalUnit.SAC_STRING_VALUE:
				return CSS_STRING;
			case LexicalUnit.SAC_ATTR:
				return CSS_ATTR;
			case LexicalUnit.SAC_UNICODERANGE:
			case LexicalUnit.SAC_SUB_EXPRESSION:
			case LexicalUnit.SAC_FUNCTION:
				return CSS_STRING;
			case LexicalUnit.SAC_DIMENSION:
				return CSS_DIMENSION;
			}
		}
		return CSS_UNKNOWN;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.css.CSSPrimitiveValue#getFloatValue(short)
	 */

	public float getFloatValue(short unitType) throws DOMException {
		if (value instanceof LexicalUnit) {
			LexicalUnit lu = (LexicalUnit) value;
			return lu.getFloatValue();
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.css.CSSPrimitiveValue#setFloatValue(short, float)
	 */

	public void setFloatValue(short unitType, float floatValue) throws DOMException {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.css.CSSPrimitiveValue#getStringValue()
	 */

	public String getStringValue() throws DOMException {
		if (value instanceof LexicalUnit) {
			LexicalUnit lu = (LexicalUnit) value;
			if ((lu.getLexicalUnitType() == LexicalUnit.SAC_IDENT)
					|| (lu.getLexicalUnitType() == LexicalUnit.SAC_STRING_VALUE)
					|| (lu.getLexicalUnitType() == LexicalUnit.SAC_URI)
					|| (lu.getLexicalUnitType() == LexicalUnit.SAC_ATTR)) {
				return lu.getStringValue();
			}
		} else if (value instanceof Vector) {
			return null;
		}

		assert false;
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.css.CSSPrimitiveValue#setStringValue(short,
	 * java.lang.String)
	 */

	public void setStringValue(short stringType, String stringValue) throws DOMException {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.css.CSSPrimitiveValue#getCounterValue()
	 */

	public Counter getCounterValue() throws DOMException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.css.CSSPrimitiveValue#getRGBColorValue()
	 */

	public RGBColor getRGBColorValue() throws DOMException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.css.CSSPrimitiveValue#getRectValue()
	 */

	public Rect getRectValue() throws DOMException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.css.CSSValue#getCssValueType()
	 */

	public short getCssValueType() {
		return (value instanceof Vector) ? CSS_VALUE_LIST : CSS_PRIMITIVE_VALUE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.css.CSSValue#getCssText()
	 */

	public String getCssText() {
		if (getCssValueType() == CSS_VALUE_LIST) {

			// Create the string from the LexicalUnits so we include the correct
			// operators in the string
			StringBuffer sb = new StringBuffer();
			Vector v = (Vector) value;
			LexicalUnit lu = (LexicalUnit) ((CSSValue) v.elementAt(0)).value;
			while (lu != null) {
				sb.append(CssUtil.toString(lu));

				// Step to the next lexical unit, determining what spacing we
				// need to put around the operators
				LexicalUnit prev = lu;
				lu = lu.getNextLexicalUnit();
				if ((lu != null) && (lu.getLexicalUnitType() != LexicalUnit.SAC_OPERATOR_COMMA)
						&& (lu.getLexicalUnitType() != LexicalUnit.SAC_OPERATOR_SLASH)
						&& (prev.getLexicalUnitType() != LexicalUnit.SAC_OPERATOR_SLASH)) {
					sb.append(" "); //$NON-NLS-1$
				}
			}
			return sb.toString();
		}

		assert value instanceof LexicalUnit;
		return CssUtil.toString((LexicalUnit) value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.css.CSSValue#setCssText(java.lang.String)
	 */

	public void setCssText(String cssText) throws DOMException {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.css.CSSValueList#getLength()
	 */

	public int getLength() {
		return (value instanceof Vector) ? ((Vector) value).size() : 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.css.CSSValueList#item(int)
	 */

	public org.w3c.dom.css.CSSValue item(int index) {
		return (value instanceof Vector) ? ((CSSValue) ((Vector) value).elementAt(index)) : null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */

	public String toString() {
		return getCssText();
	}
}

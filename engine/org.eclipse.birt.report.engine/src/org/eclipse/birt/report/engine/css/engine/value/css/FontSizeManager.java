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
 *  Actuate Corporation  - modification of Batik's FontSizeManager.java to support BIRT's CSS rules
 *******************************************************************************/
package org.eclipse.birt.report.engine.css.engine.value.css;

import org.apache.batik.css.engine.StyleMap;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.engine.CSSContext;
import org.eclipse.birt.report.engine.css.engine.CSSEngine;
import org.eclipse.birt.report.engine.css.engine.CSSStylableElement;
import org.eclipse.birt.report.engine.css.engine.ValueManager;
import org.eclipse.birt.report.engine.css.engine.value.AbstractLengthManager;
import org.eclipse.birt.report.engine.css.engine.value.FloatValue;
import org.eclipse.birt.report.engine.css.engine.value.IdentifierManager;
import org.eclipse.birt.report.engine.css.engine.value.StringMap;
import org.eclipse.birt.report.engine.css.engine.value.Value;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSValue;

/**
 * This class provides a manager for the 'font-size' property values.
 *
 */
public class FontSizeManager extends AbstractLengthManager {

	/**
	 * The identifier values.
	 */
	protected final static StringMap values = new StringMap();
	static {
		values.put(CSSConstants.CSS_LARGE_VALUE, CSSValueConstants.LARGE_VALUE);
		values.put(CSSConstants.CSS_LARGER_VALUE, CSSValueConstants.LARGER_VALUE);
		values.put(CSSConstants.CSS_MEDIUM_VALUE, CSSValueConstants.MEDIUM_VALUE);
		values.put(CSSConstants.CSS_SMALL_VALUE, CSSValueConstants.SMALL_VALUE);
		values.put(CSSConstants.CSS_SMALLER_VALUE, CSSValueConstants.SMALLER_VALUE);
		values.put(CSSConstants.CSS_X_LARGE_VALUE, CSSValueConstants.X_LARGE_VALUE);
		values.put(CSSConstants.CSS_X_SMALL_VALUE, CSSValueConstants.X_SMALL_VALUE);
		values.put(CSSConstants.CSS_XX_LARGE_VALUE, CSSValueConstants.XX_LARGE_VALUE);
		values.put(CSSConstants.CSS_XX_SMALL_VALUE, CSSValueConstants.XX_SMALL_VALUE);
	}

	/**
	 * Implements {@link IdentifierManager#getIdentifiers()}.
	 */
	public StringMap getIdentifiers() {
		return values;
	}

	/**
	 * Implements {@link ValueManager#isInheritedProperty()}.
	 */
	@Override
	public boolean isInheritedProperty() {
		return true;
	}

	/**
	 * Implements {@link ValueManager#getPropertyName()}.
	 */
	@Override
	public String getPropertyName() {
		return CSSConstants.CSS_FONT_SIZE_PROPERTY;
	}

	/**
	 * Implements {@link ValueManager#getDefaultValue()}.
	 */
	@Override
	public Value getDefaultValue() {
		return CSSValueConstants.MEDIUM_VALUE;
	}

	/**
	 * Implements {@link ValueManager#createValue(LexicalUnit,CSSEngine)}.
	 */
	@Override
	public Value createValue(LexicalUnit lu, CSSEngine engine) throws DOMException {
		switch (lu.getLexicalUnitType()) {
		case LexicalUnit.SAC_INHERIT:
			return CSSValueConstants.INHERIT_VALUE;

		case LexicalUnit.SAC_IDENT:
			String s = lu.getStringValue().toLowerCase().intern();
			Object v = values.get(s);
			if (v == null) {
				throw createInvalidIdentifierDOMException(s);
			}
			return (Value) v;
		default:
			break;
		}
		return super.createValue(lu, engine);
	}

	/**
	 * Implements {@link ValueManager#createStringValue(short,String,CSSEngine)}.
	 */
	public CSSPrimitiveValue createStringValue(short type, String value) throws DOMException {
		if (type != CSSPrimitiveValue.CSS_IDENT) {
			throw createInvalidStringTypeDOMException(type);
		}
		Object v = values.get(value.toLowerCase().intern());
		if (v == null) {
			throw createInvalidIdentifierDOMException(value);
		}
		return (CSSPrimitiveValue) v;
	}

	/**
	 * Implements
	 * {@link ValueManager#computeValue(CSSStylableElement,String,CSSEngine,int,StyleMap,Value)}.
	 */
	@Override
	public Value computeValue(CSSStylableElement elt, CSSEngine engine, int idx, Value value) {
		CSSContext ctx = engine.getCSSContext();

		float fs = ctx.getMediumFontSize();
		// absolute size
		if (value == CSSValueConstants.XX_SMALL_VALUE) {
			return new FloatValue(CSSPrimitiveValue.CSS_PT, fs / 1.2f / 1.2f / 1.2f);
		}
		if (value == CSSValueConstants.X_SMALL_VALUE) {
			return new FloatValue(CSSPrimitiveValue.CSS_PT, fs / 1.2f / 1.2f);
		}
		if (value == CSSValueConstants.SMALL_VALUE) {
			return new FloatValue(CSSPrimitiveValue.CSS_PT, fs / 1.2f);
		}
		if (value == CSSValueConstants.MEDIUM_VALUE) {
			return new FloatValue(CSSPrimitiveValue.CSS_PT, fs);
		}
		if (value == CSSValueConstants.LARGE_VALUE) {
			return new FloatValue(CSSPrimitiveValue.CSS_PT, fs * 1.2f);
		}
		if (value == CSSValueConstants.X_LARGE_VALUE) {
			return new FloatValue(CSSPrimitiveValue.CSS_PT, fs * 1.2f * 1.2f);
		}
		if (value == CSSValueConstants.XX_LARGE_VALUE) {
			return new FloatValue(CSSPrimitiveValue.CSS_PT, fs * 1.2f * 1.2f * 1.2f);
		}

		float scale = 1.0f;
		boolean doParentRelative = false;

		// relative size
		if (value == CSSValueConstants.SMALLER_VALUE) {
			doParentRelative = true;
			scale = 1.0f / 1.2f;
		} else if (value == CSSValueConstants.LARGER_VALUE) {
			doParentRelative = true;
			scale = 1.2f;
		} else if (value.getCssValueType() == CSSValue.CSS_PRIMITIVE_VALUE) {
			// relative length && percentage
			switch (value.getPrimitiveType()) {
			case CSSPrimitiveValue.CSS_EMS:
				doParentRelative = true;
				scale = value.getFloatValue();
				break;
			case CSSPrimitiveValue.CSS_EXS:
				doParentRelative = true;
				scale = value.getFloatValue() * 0.5f;
				break;
			case CSSPrimitiveValue.CSS_PERCENTAGE:
				doParentRelative = true;
				scale = value.getFloatValue() * 0.01f;
				break;
			}
		}
		if (doParentRelative) {
			CSSStylableElement parent = (CSSStylableElement) elt.getParent();
			if (parent != null) {
				IStyle style = parent.getComputedStyle();
				if (style != null) {
					Value fontSize = (Value) style.getProperty(IStyle.STYLE_FONT_SIZE);
					if (fontSize != null) {
						fs = fontSize.getFloatValue();
						return new FloatValue(fontSize.getPrimitiveType(), fs * scale);
					}
				}
			}
			return new FloatValue(CSSPrimitiveValue.CSS_PT, fs * scale);
		}

		if (value.getPrimitiveType() == CSSPrimitiveValue.CSS_NUMBER) {
			return super.computeValue(elt, engine, idx,
					new FloatValue(CSSPrimitiveValue.CSS_PT, value.getFloatValue()));
		}
		return super.computeValue(elt, engine, idx, value);
	}
}

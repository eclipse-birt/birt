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

package org.eclipse.birt.report.engine.css.engine.value;

import org.eclipse.birt.report.engine.css.engine.CSSEngine;
import org.eclipse.birt.report.engine.css.engine.CSSStylableElement;
import org.eclipse.birt.report.engine.css.engine.ValueManager;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSConstants;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSValueConstants;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSValue;
import org.w3c.dom.css.RGBColor;

/**
 * This class provides a manager for the property with support for CSS color
 * values.
 * 
 */
public abstract class AbstractColorManager extends IdentifierManager {

	/**
	 * The identifier values.
	 */
	protected final static StringMap values = new StringMap();

	static {
		addColorIndent(values);
	}

	public static void addColorIndent(StringMap values) {
		// color keywords
		values.put(CSSConstants.CSS_AQUA_VALUE, CSSValueConstants.AQUA_VALUE);
		values.put(CSSConstants.CSS_BLACK_VALUE, CSSValueConstants.BLACK_VALUE);
		values.put(CSSConstants.CSS_BLUE_VALUE, CSSValueConstants.BLUE_VALUE);
		values.put(CSSConstants.CSS_FUCHSIA_VALUE, CSSValueConstants.FUCHSIA_VALUE);
		values.put(CSSConstants.CSS_GRAY_VALUE, CSSValueConstants.GRAY_VALUE);
		values.put(CSSConstants.CSS_GREEN_VALUE, CSSValueConstants.GREEN_VALUE);
		values.put(CSSConstants.CSS_LIME_VALUE, CSSValueConstants.LIME_VALUE);
		values.put(CSSConstants.CSS_MAROON_VALUE, CSSValueConstants.MAROON_VALUE);
		values.put(CSSConstants.CSS_NAVY_VALUE, CSSValueConstants.NAVY_VALUE);
		values.put(CSSConstants.CSS_OLIVE_VALUE, CSSValueConstants.OLIVE_VALUE);
		values.put(CSSConstants.CSS_ORANGE_VALUE, CSSValueConstants.ORANGE_VALUE);
		values.put(CSSConstants.CSS_PURPLE_VALUE, CSSValueConstants.PURPLE_VALUE);
		values.put(CSSConstants.CSS_RED_VALUE, CSSValueConstants.RED_VALUE);
		values.put(CSSConstants.CSS_SILVER_VALUE, CSSValueConstants.SILVER_VALUE);
		values.put(CSSConstants.CSS_TEAL_VALUE, CSSValueConstants.TEAL_VALUE);
		values.put(CSSConstants.CSS_WHITE_VALUE, CSSValueConstants.WHITE_VALUE);
		values.put(CSSConstants.CSS_YELLOW_VALUE, CSSValueConstants.YELLOW_VALUE);

		// system color
		values.put(CSSConstants.CSS_ACTIVEBORDER_VALUE, CSSValueConstants.ACTIVEBORDER_VALUE);
		values.put(CSSConstants.CSS_ACTIVECAPTION_VALUE, CSSValueConstants.ACTIVECAPTION_VALUE);
		values.put(CSSConstants.CSS_APPWORKSPACE_VALUE, CSSValueConstants.APPWORKSPACE_VALUE);
		values.put(CSSConstants.CSS_BACKGROUND_VALUE, CSSValueConstants.BACKGROUND_VALUE);
		values.put(CSSConstants.CSS_BUTTONFACE_VALUE, CSSValueConstants.BUTTONFACE_VALUE);
		values.put(CSSConstants.CSS_BUTTONHIGHLIGHT_VALUE, CSSValueConstants.BUTTONHIGHLIGHT_VALUE);
		values.put(CSSConstants.CSS_BUTTONSHADOW_VALUE, CSSValueConstants.BUTTONSHADOW_VALUE);
		values.put(CSSConstants.CSS_BUTTONTEXT_VALUE, CSSValueConstants.BUTTONTEXT_VALUE);
		values.put(CSSConstants.CSS_CAPTIONTEXT_VALUE, CSSValueConstants.CAPTIONTEXT_VALUE);
		values.put(CSSConstants.CSS_GRAYTEXT_VALUE, CSSValueConstants.GRAYTEXT_VALUE);
		values.put(CSSConstants.CSS_HIGHLIGHT_VALUE, CSSValueConstants.HIGHLIGHT_VALUE);
		values.put(CSSConstants.CSS_HIGHLIGHTTEXT_VALUE, CSSValueConstants.HIGHLIGHTTEXT_VALUE);
		values.put(CSSConstants.CSS_INACTIVEBORDER_VALUE, CSSValueConstants.INACTIVEBORDER_VALUE);
		values.put(CSSConstants.CSS_INACTIVECAPTION_VALUE, CSSValueConstants.INACTIVECAPTION_VALUE);
		values.put(CSSConstants.CSS_INACTIVECAPTIONTEXT_VALUE, CSSValueConstants.INACTIVECAPTIONTEXT_VALUE);
		values.put(CSSConstants.CSS_INFOBACKGROUND_VALUE, CSSValueConstants.INFOBACKGROUND_VALUE);
		values.put(CSSConstants.CSS_INFOTEXT_VALUE, CSSValueConstants.INFOTEXT_VALUE);
		values.put(CSSConstants.CSS_MENU_VALUE, CSSValueConstants.MENU_VALUE);
		values.put(CSSConstants.CSS_MENUTEXT_VALUE, CSSValueConstants.MENUTEXT_VALUE);
		values.put(CSSConstants.CSS_SCROLLBAR_VALUE, CSSValueConstants.SCROLLBAR_VALUE);
		values.put(CSSConstants.CSS_THREEDDARKSHADOW_VALUE, CSSValueConstants.THREEDDARKSHADOW_VALUE);
		values.put(CSSConstants.CSS_THREEDFACE_VALUE, CSSValueConstants.THREEDFACE_VALUE);
		values.put(CSSConstants.CSS_THREEDHIGHLIGHT_VALUE, CSSValueConstants.THREEDHIGHLIGHT_VALUE);
		values.put(CSSConstants.CSS_THREEDLIGHTSHADOW_VALUE, CSSValueConstants.THREEDLIGHTSHADOW_VALUE);
		values.put(CSSConstants.CSS_THREEDSHADOW_VALUE, CSSValueConstants.THREEDSHADOW_VALUE);
		values.put(CSSConstants.CSS_WINDOW_VALUE, CSSValueConstants.WINDOW_VALUE);
		values.put(CSSConstants.CSS_WINDOWFRAME_VALUE, CSSValueConstants.WINDOWFRAME_VALUE);
		values.put(CSSConstants.CSS_WINDOWTEXT_VALUE, CSSValueConstants.WINDOWTEXT_VALUE);
	}

	/**
	 * The computed identifier values.
	 */
	protected final static StringMap computedValues = new StringMap();
	static {
		computedValues.put(CSSConstants.CSS_BLACK_VALUE, CSSValueConstants.BLACK_RGB_VALUE);
		computedValues.put(CSSConstants.CSS_SILVER_VALUE, CSSValueConstants.SILVER_RGB_VALUE);
		computedValues.put(CSSConstants.CSS_GRAY_VALUE, CSSValueConstants.GRAY_RGB_VALUE);
		computedValues.put(CSSConstants.CSS_WHITE_VALUE, CSSValueConstants.WHITE_RGB_VALUE);
		computedValues.put(CSSConstants.CSS_MAROON_VALUE, CSSValueConstants.MAROON_RGB_VALUE);
		computedValues.put(CSSConstants.CSS_RED_VALUE, CSSValueConstants.RED_RGB_VALUE);
		computedValues.put(CSSConstants.CSS_PURPLE_VALUE, CSSValueConstants.PURPLE_RGB_VALUE);
		computedValues.put(CSSConstants.CSS_FUCHSIA_VALUE, CSSValueConstants.FUCHSIA_RGB_VALUE);
		computedValues.put(CSSConstants.CSS_GREEN_VALUE, CSSValueConstants.GREEN_RGB_VALUE);
		computedValues.put(CSSConstants.CSS_LIME_VALUE, CSSValueConstants.LIME_RGB_VALUE);
		computedValues.put(CSSConstants.CSS_OLIVE_VALUE, CSSValueConstants.OLIVE_RGB_VALUE);
		computedValues.put(CSSConstants.CSS_ORANGE_VALUE, CSSValueConstants.ORANGE_RGB_VALUE);
		computedValues.put(CSSConstants.CSS_YELLOW_VALUE, CSSValueConstants.YELLOW_RGB_VALUE);
		computedValues.put(CSSConstants.CSS_NAVY_VALUE, CSSValueConstants.NAVY_RGB_VALUE);
		computedValues.put(CSSConstants.CSS_BLUE_VALUE, CSSValueConstants.BLUE_RGB_VALUE);
		computedValues.put(CSSConstants.CSS_TEAL_VALUE, CSSValueConstants.TEAL_RGB_VALUE);
		computedValues.put(CSSConstants.CSS_AQUA_VALUE, CSSValueConstants.AQUA_RGB_VALUE);
		computedValues.put(CSSConstants.CSS_TRANSPARENT_VALUE, CSSValueConstants.TRANSPARENT_VALUE);
	}

	/**
	 * Implements {@link ValueManager#createValue(LexicalUnit,CSSEngine)}.
	 */
	public Value createValue(LexicalUnit lu, CSSEngine engine) throws DOMException {
		if (lu.getLexicalUnitType() == LexicalUnit.SAC_RGBCOLOR) {
			lu = lu.getParameters();
			Value red = createColorComponent(lu);
			lu = lu.getNextLexicalUnit().getNextLexicalUnit();
			Value green = createColorComponent(lu);
			lu = lu.getNextLexicalUnit().getNextLexicalUnit();
			Value blue = createColorComponent(lu);
			return createRGBColor(red, green, blue);
		}
		return super.createValue(lu, engine);
	}

	/**
	 * Implements
	 * {@link ValueManager#computeValue(CSSStylableElement,String,CSSEngine,int,StyleMap,Value)}.
	 */
	public Value computeValue(CSSStylableElement elt, CSSEngine engine, int idx, Value value) {
		if (value.getCssValueType() == CSSValue.CSS_PRIMITIVE_VALUE) {
			CSSPrimitiveValue pvalue = (CSSPrimitiveValue) value;
			int primitiveType = pvalue.getPrimitiveType();
			if (primitiveType == CSSPrimitiveValue.CSS_IDENT) {
				String ident = pvalue.getStringValue();
				// Search for a direct computed value.
				Value v = (Value) computedValues.get(ident);
				if (v != null) {
					return v;
				}
				// Must be a system color...
				if (values.get(ident) == null) {
					throw new InternalError();
				}
				return (Value) engine.getCSSContext().getSystemColor(ident);
			}
			if (primitiveType == CSSPrimitiveValue.CSS_RGBCOLOR) {
				RGBColor color = value.getRGBColorValue();
				CSSPrimitiveValue red = color.getRed();
				CSSPrimitiveValue green = color.getGreen();
				CSSPrimitiveValue blue = color.getBlue();

				return createRGBColor(createColorComponent(red), createColorComponent(green),
						createColorComponent(blue));
			}
		}
		return super.computeValue(elt, engine, idx, value);
	}

	protected CSSPrimitiveValue createColorComponent(CSSPrimitiveValue value) throws DOMException {
		if (value.getPrimitiveType() == CSSPrimitiveValue.CSS_PERCENTAGE) {
			float v = value.getFloatValue(CSSPrimitiveValue.CSS_PERCENTAGE);
			if (v < 0) {
				v = 0.0f;
			} else if (v > 100) {
				v = 1.0f;
			} else {
				v = 255.0f * v / 100.0f;
			}
			return new FloatValue(CSSPrimitiveValue.CSS_NUMBER, v);
		} else if (value.getPrimitiveType() == CSSPrimitiveValue.CSS_NUMBER) {
			float v = value.getFloatValue(CSSPrimitiveValue.CSS_NUMBER);
			if (v <= 0) {
				v = 0;
			} else if (v >= 255) {
				v = 255;
			}
			return new FloatValue(CSSPrimitiveValue.CSS_NUMBER, v);
		}
		return value;

	}

	/**
	 * Creates an RGB color.
	 */
	protected Value createRGBColor(CSSPrimitiveValue r, CSSPrimitiveValue g, CSSPrimitiveValue b) {
		return new RGBColorValue(r, g, b);
	}

	/**
	 * Creates a color component from a lexical unit.
	 */
	protected Value createColorComponent(LexicalUnit lu) throws DOMException {
		switch (lu.getLexicalUnitType()) {
		case LexicalUnit.SAC_INTEGER:
			return new FloatValue(CSSPrimitiveValue.CSS_NUMBER, lu.getIntegerValue());

		case LexicalUnit.SAC_REAL:
			return new FloatValue(CSSPrimitiveValue.CSS_NUMBER, lu.getFloatValue());

		case LexicalUnit.SAC_PERCENTAGE:
			return new FloatValue(CSSPrimitiveValue.CSS_PERCENTAGE, lu.getFloatValue());
		}
		throw createInvalidRGBComponentUnitDOMException(lu.getLexicalUnitType());
	}

	/**
	 * Implements {@link IdentifierManager#getIdentifiers()}.
	 */
	public StringMap getIdentifiers() {
		return values;
	}

	private DOMException createInvalidRGBComponentUnitDOMException(short type) {
		Object[] p = new Object[] { getPropertyName(), Integer.valueOf(type) };
		String s = Messages.formatMessage("invalid.rgb.component.unit", p); //$NON-NLS-1$
		return new DOMException(DOMException.NOT_SUPPORTED_ERR, s);
	}

}

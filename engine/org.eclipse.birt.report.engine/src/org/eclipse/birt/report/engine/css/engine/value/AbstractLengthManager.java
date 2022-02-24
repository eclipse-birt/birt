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

import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.engine.CSSEngine;
import org.eclipse.birt.report.engine.css.engine.CSSStylableElement;
import org.eclipse.birt.report.engine.css.engine.ValueManager;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSValue;

/**
 * This class provides a manager for the property with support for length
 * values.
 * 
 */
public abstract class AbstractLengthManager extends AbstractValueManager {

	/**
	 * Implements {@link ValueManager#createValue(LexicalUnit,CSSEngine)}.
	 */
	public Value createValue(LexicalUnit lu, CSSEngine engine) throws DOMException {
		switch (lu.getLexicalUnitType()) {
		case LexicalUnit.SAC_EM:
			return new FloatValue(CSSPrimitiveValue.CSS_EMS, lu.getFloatValue());

		case LexicalUnit.SAC_EX:
			return new FloatValue(CSSPrimitiveValue.CSS_EXS, lu.getFloatValue());

		case LexicalUnit.SAC_PIXEL:
			return new FloatValue(CSSPrimitiveValue.CSS_PX, lu.getFloatValue());

		case LexicalUnit.SAC_CENTIMETER:
			return new FloatValue(CSSPrimitiveValue.CSS_CM, lu.getFloatValue());

		case LexicalUnit.SAC_MILLIMETER:
			return new FloatValue(CSSPrimitiveValue.CSS_MM, lu.getFloatValue());

		case LexicalUnit.SAC_INCH:
			return new FloatValue(CSSPrimitiveValue.CSS_IN, lu.getFloatValue());

		case LexicalUnit.SAC_POINT:
			return new FloatValue(CSSPrimitiveValue.CSS_PT, lu.getFloatValue());

		case LexicalUnit.SAC_PICA:
			return new FloatValue(CSSPrimitiveValue.CSS_PC, lu.getFloatValue());

		case LexicalUnit.SAC_INTEGER:
			return new FloatValue(CSSPrimitiveValue.CSS_NUMBER, lu.getIntegerValue());

		case LexicalUnit.SAC_REAL:
			return new FloatValue(CSSPrimitiveValue.CSS_NUMBER, lu.getFloatValue());

		case LexicalUnit.SAC_PERCENTAGE:
			return new FloatValue(CSSPrimitiveValue.CSS_PERCENTAGE, lu.getFloatValue());
		}
		throw createInvalidLexicalUnitDOMException(lu.getLexicalUnitType());
	}

	/**
	 * Implements
	 * {@link ValueManager#computeValue(CSSStylableElement,String,CSSEngine,int,StyleMap,Value)}.
	 */
	public Value computeValue(CSSStylableElement elt, CSSEngine engine, int idx, Value value) {
		if (value.getCssValueType() == CSSValue.CSS_PRIMITIVE_VALUE) {
			switch (value.getPrimitiveType()) {
			case CSSPrimitiveValue.CSS_NUMBER:
			case CSSPrimitiveValue.CSS_PX:
			case CSSPrimitiveValue.CSS_MM:
			case CSSPrimitiveValue.CSS_CM:
			case CSSPrimitiveValue.CSS_IN:
			case CSSPrimitiveValue.CSS_PT:
			case CSSPrimitiveValue.CSS_PC:
				return value;

			case CSSPrimitiveValue.CSS_EMS:
				float v = value.getFloatValue();
				Value fontSize = (Value) elt.getComputedStyle().getProperty(IStyle.STYLE_FONT_SIZE);
				float fs = fontSize.getFloatValue();
				return new FloatValue(fontSize.getPrimitiveType(), v * fs);

			case CSSPrimitiveValue.CSS_EXS:
				v = value.getFloatValue();
				fontSize = (Value) elt.getComputedStyle().getProperty(IStyle.STYLE_FONT_SIZE);
				fs = fontSize.getFloatValue();
				return new FloatValue(fontSize.getPrimitiveType(), v * fs * 0.5f);
			}
		}
		return value;
	}
}

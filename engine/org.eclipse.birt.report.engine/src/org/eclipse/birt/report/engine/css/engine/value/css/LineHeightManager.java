/*******************************************************************************
 * Copyright (c) 2004, 2025 Actuate Corporation and others
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
package org.eclipse.birt.report.engine.css.engine.value.css;

import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.engine.CSSEngine;
import org.eclipse.birt.report.engine.css.engine.CSSStylableElement;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.css.engine.value.AbstractLengthManager;
import org.eclipse.birt.report.engine.css.engine.value.FloatValue;
import org.eclipse.birt.report.engine.css.engine.value.StringMap;
import org.eclipse.birt.report.engine.css.engine.value.Value;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSValue;

/**
 * Line height manager controls the properties and methods for the line height
 *
 * @since 3.3
 *
 */
public class LineHeightManager extends AbstractLengthManager {

	/**
	 * The identifier values.
	 */
	protected final static StringMap values = new StringMap();
	static {
		values.put(CSSConstants.CSS_NORMAL_VALUE, CSSValueConstants.NORMAL_VALUE);
	}

	/**
	 * Constructor
	 */
	public LineHeightManager() {
	}

	@Override
	public String getPropertyName() {
		return CSSConstants.CSS_LINE_HEIGHT_PROPERTY;
	}

	@Override
	public boolean isInheritedProperty() {
		return true;
	}

	@Override
	public Value getDefaultValue() {
		return CSSValueConstants.NORMAL_VALUE;
	}

	@Override
	public Value createValue(LexicalUnit lu, CSSEngine engine) throws DOMException {
		switch (lu.getLexicalUnitType()) {
		case LexicalUnit.SAC_IDENT:
			String s = lu.getStringValue().toLowerCase().intern();
			Object v = values.get(s);
			if (v == null) {
				throw createInvalidIdentifierDOMException(lu.getStringValue());
			}
			return (Value) v;
		}
		return super.createValue(lu, engine);
//		throw createInvalidLexicalUnitDOMException(lu.getLexicalUnitType());
	}

	@Override
	public Value computeValue(CSSStylableElement elt, CSSEngine engine, int idx, Value value) {
		if (value.getCssValueType() == CSSValue.CSS_PRIMITIVE_VALUE) {
			switch (value.getPrimitiveType()) {
			case CSSPrimitiveValue.CSS_IDENT:
				return value;
			case CSSPrimitiveValue.CSS_NUMBER:
			case CSSPrimitiveValue.CSS_PERCENTAGE:
				float scale = value.getFloatValue();
				IStyle cs = elt.getComputedStyle();
				assert cs != null;
				Value fontSize = (Value) cs.getProperty(StyleConstants.STYLE_FONT_SIZE);
				assert fontSize != null;
				float fs = fontSize.getFloatValue();
				// convert the line height standard numbers to line height for unit pt
				// according to the percentage calculation
				if (value.getPrimitiveType() == CSSPrimitiveValue.CSS_NUMBER) {
					return new FloatValue(fontSize.getPrimitiveType(), fs * scale);
				}
				return new FloatValue(fontSize.getPrimitiveType(), fs * scale / 100.0f);
			}
		}
		return super.computeValue(elt, engine, idx, value);
	}

}

/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.css.engine.value.css;

import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.engine.CSSEngine;
import org.eclipse.birt.report.engine.css.engine.CSSStylableElement;
import org.eclipse.birt.report.engine.css.engine.value.AbstractLengthManager;
import org.eclipse.birt.report.engine.css.engine.value.FloatValue;
import org.eclipse.birt.report.engine.css.engine.value.StringMap;
import org.eclipse.birt.report.engine.css.engine.value.Value;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSValue;

public class BorderWidthManager extends AbstractLengthManager {

	/**
	 * The identifier values.
	 */
	protected final static StringMap values = new StringMap();
	static {
		values.put(CSSConstants.CSS_THIN_VALUE, CSSValueConstants.THIN_VALUE);
		values.put(CSSConstants.CSS_MEDIUM_VALUE, CSSValueConstants.MEDIUM_VALUE);
		values.put(CSSConstants.CSS_THICK_VALUE, CSSValueConstants.THICK_VALUE);
	}

	protected final static StringMap computedValues = new StringMap();
	static {
		computedValues.put(CSSConstants.CSS_THIN_VALUE, new FloatValue(CSSPrimitiveValue.CSS_PX, 1));
		computedValues.put(CSSConstants.CSS_MEDIUM_VALUE, new FloatValue(CSSPrimitiveValue.CSS_PX, 3));
		computedValues.put(CSSConstants.CSS_THICK_VALUE, new FloatValue(CSSPrimitiveValue.CSS_PX, 5));
	}

	String propertyName;

	public BorderWidthManager(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public boolean isInheritedProperty() {
		return false;
	}

	public Value getDefaultValue() {
		return CSSValueConstants.MEDIUM_VALUE;
	}

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
	}

	public Value computeValue(CSSStylableElement elt, CSSEngine engine, int idx, Value value) {
		IStyle cs = elt.getComputedStyle();
		CSSValue borderStyle = null;
		switch (idx) {
		case IStyle.STYLE_BORDER_TOP_WIDTH:
			borderStyle = cs.getProperty(IStyle.STYLE_BORDER_TOP_STYLE);
			break;
		case IStyle.STYLE_BORDER_BOTTOM_WIDTH:
			borderStyle = cs.getProperty(IStyle.STYLE_BORDER_BOTTOM_STYLE);
			break;
		case IStyle.STYLE_BORDER_LEFT_WIDTH:
			borderStyle = cs.getProperty(IStyle.STYLE_BORDER_LEFT_STYLE);
			break;
		case IStyle.STYLE_BORDER_RIGHT_WIDTH:
			borderStyle = cs.getProperty(IStyle.STYLE_BORDER_RIGHT_STYLE);
			break;
		}
		if (borderStyle == CSSValueConstants.NONE_VALUE || borderStyle == CSSValueConstants.HIDDEN_VALUE) {
			return CSSValueConstants.NUMBER_0;
		}
		if (value.getCssValueType() == CSSValue.CSS_PRIMITIVE_VALUE) {
			if (value.getPrimitiveType() == CSSPrimitiveValue.CSS_IDENT) {
				String ident = value.getStringValue();
				Value cv = (Value) computedValues.get(ident);
				if (cv != null) {
					value = cv;
				}
			}
		}
		return super.computeValue(elt, engine, idx, value);
	}

}

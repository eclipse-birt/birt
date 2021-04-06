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

import org.apache.batik.css.engine.StyleMap;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.engine.CSSEngine;
import org.eclipse.birt.report.engine.css.engine.CSSStylableElement;
import org.eclipse.birt.report.engine.css.engine.ValueManager;
import org.eclipse.birt.report.engine.css.engine.value.AbstractLengthManager;
import org.eclipse.birt.report.engine.css.engine.value.FloatValue;
import org.eclipse.birt.report.engine.css.engine.value.StringMap;
import org.eclipse.birt.report.engine.css.engine.value.Value;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSValue;

public class VerticalAlignManager extends AbstractLengthManager {

	/**
	 * The identifier values.
	 */
	protected final static StringMap values = new StringMap();
	static {
		values.put(CSSConstants.CSS_BASELINE_VALUE, CSSValueConstants.BASELINE_VALUE);
		values.put(CSSConstants.CSS_SUB_VALUE, CSSValueConstants.SUB_VALUE);
		values.put(CSSConstants.CSS_SUPER_VALUE, CSSValueConstants.SUPER_VALUE);
		values.put(CSSConstants.CSS_TOP_VALUE, CSSValueConstants.TOP_VALUE);
		values.put(CSSConstants.CSS_TEXT_TOP_VALUE, CSSValueConstants.TEXT_TOP_VALUE);
		values.put(CSSConstants.CSS_MIDDLE_VALUE, CSSValueConstants.MIDDLE_VALUE);
		values.put(CSSConstants.CSS_BOTTOM_VALUE, CSSValueConstants.BOTTOM_VALUE);
		values.put(CSSConstants.CSS_TEXT_BOTTOM_VALUE, CSSValueConstants.TEXT_BOTTOM_VALUE);
	}

	public VerticalAlignManager() {
	}

	public String getPropertyName() {
		return CSSConstants.CSS_VERTICAL_ALIGN_PROPERTY;
	}

	public boolean isInheritedProperty() {
		return false;
	}

	public Value getDefaultValue() {
		return CSSValueConstants.BASELINE_VALUE;
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

	/**
	 * Implements
	 * {@link ValueManager#computeValue(CSSStylableElement,String,CSSEngine,int,StyleMap,Value)}.
	 */
	public Value computeValue(CSSStylableElement elt, CSSEngine engine, int idx, Value value) {
		if (value.getCssValueType() == CSSValue.CSS_PRIMITIVE_VALUE) {
			switch (value.getPrimitiveType()) {
			case CSSPrimitiveValue.CSS_IDENT:
				return value;
			case CSSPrimitiveValue.CSS_PERCENTAGE:
				float scale = value.getFloatValue();
				float fs = 0;
				IStyle cs = elt.getComputedStyle();
				assert cs != null;
				Value lineHeight = (Value) cs.getProperty(IStyle.STYLE_LINE_HEIGHT);
				assert lineHeight != null;
				Value fontSize = (Value) cs.getProperty(IStyle.STYLE_FONT_SIZE);

				if (lineHeight == CSSValueConstants.NORMAL_VALUE) {
					fs = 1.2f * fontSize.getFloatValue();
				} else if (lineHeight.getPrimitiveType() == CSSPrimitiveValue.CSS_NUMBER) {
					fs = lineHeight.getFloatValue() * fontSize.getFloatValue();
				} else {
					fs = lineHeight.getFloatValue();
				}
				return new FloatValue(fontSize.getPrimitiveType(), fs * scale);
			}
		}
		return super.computeValue(elt, engine, idx, value);
	}
}

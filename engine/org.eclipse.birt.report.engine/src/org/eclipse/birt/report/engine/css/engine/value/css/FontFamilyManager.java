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
 *  Actuate Corporation  - modification of Batik's FontFamilyManager.java to support BIRT's CSS rules
 *******************************************************************************/
package org.eclipse.birt.report.engine.css.engine.value.css;

import org.eclipse.birt.report.engine.css.engine.CSSContext;
import org.eclipse.birt.report.engine.css.engine.CSSEngine;
import org.eclipse.birt.report.engine.css.engine.CSSStylableElement;
import org.eclipse.birt.report.engine.css.engine.ValueManager;
import org.eclipse.birt.report.engine.css.engine.value.AbstractValueManager;
import org.eclipse.birt.report.engine.css.engine.value.ListValue;
import org.eclipse.birt.report.engine.css.engine.value.StringMap;
import org.eclipse.birt.report.engine.css.engine.value.StringValue;
import org.eclipse.birt.report.engine.css.engine.value.Value;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSValue;

/**
 * This class provides a factory for the 'font-family' property values.
 * 
 */
public class FontFamilyManager extends AbstractValueManager {

	/**
	 * The default value.
	 */
	public final static ListValue DEFAULT_VALUE = new ListValue();
	static {
		DEFAULT_VALUE.append(new StringValue(CSSPrimitiveValue.CSS_STRING, "Arial"));
		DEFAULT_VALUE.append(new StringValue(CSSPrimitiveValue.CSS_STRING, "Helvetica"));
		DEFAULT_VALUE.append(new StringValue(CSSPrimitiveValue.CSS_IDENT, CSSConstants.CSS_SANS_SERIF_VALUE));
	}

	/**
	 * The identifier values.
	 */
	protected final static StringMap values = new StringMap();
	static {
		values.put(CSSConstants.CSS_CURSIVE_VALUE, CSSValueConstants.CURSIVE_VALUE);
		values.put(CSSConstants.CSS_FANTASY_VALUE, CSSValueConstants.FANTASY_VALUE);
		values.put(CSSConstants.CSS_MONOSPACE_VALUE, CSSValueConstants.MONOSPACE_VALUE);
		values.put(CSSConstants.CSS_SERIF_VALUE, CSSValueConstants.SERIF_VALUE);
		values.put(CSSConstants.CSS_SANS_SERIF_VALUE, CSSValueConstants.SANS_SERIF_VALUE);
	}

	/**
	 * Implements {@link ValueManager#isInheritedProperty()}.
	 */
	public boolean isInheritedProperty() {
		return true;
	}

	/**
	 * Implements {@link ValueManager#getPropertyName()}.
	 */
	public String getPropertyName() {
		return CSSConstants.CSS_FONT_FAMILY_PROPERTY;
	}

	/**
	 * Implements {@link ValueManager#getDefaultValue()}.
	 */
	public org.eclipse.birt.report.engine.css.engine.value.Value getDefaultValue() {
		return DEFAULT_VALUE;
	}

	/**
	 * Implements {@link ValueManager#createValue(LexicalUnit,CSSEngine)}.
	 */
	public Value createValue(LexicalUnit lu, CSSEngine engine) throws DOMException {
		switch (lu.getLexicalUnitType()) {
		case LexicalUnit.SAC_INHERIT:
			return CSSValueConstants.INHERIT_VALUE;

		default:
			throw createInvalidLexicalUnitDOMException(lu.getLexicalUnitType());

		case LexicalUnit.SAC_IDENT:
		case LexicalUnit.SAC_STRING_VALUE:
		}
		ListValue result = new ListValue();
		for (;;) {
			switch (lu.getLexicalUnitType()) {
			case LexicalUnit.SAC_STRING_VALUE:
				result.append(new StringValue(CSSPrimitiveValue.CSS_STRING, lu.getStringValue()));
				lu = lu.getNextLexicalUnit();
				break;

			case LexicalUnit.SAC_IDENT:
				StringBuffer sb = new StringBuffer(lu.getStringValue());
				lu = lu.getNextLexicalUnit();
				if (lu != null && lu.getLexicalUnitType() == LexicalUnit.SAC_IDENT) {
					do {
						sb.append(' ');
						sb.append(lu.getStringValue());
						lu = lu.getNextLexicalUnit();
					} while (lu != null && lu.getLexicalUnitType() == LexicalUnit.SAC_IDENT);
					result.append(new StringValue(CSSPrimitiveValue.CSS_STRING, sb.toString()));
				} else {
					String id = sb.toString();
					String s = id.toLowerCase().intern();
					CSSValue v = (CSSValue) values.get(s);
					result.append((v != null) ? v : new StringValue(CSSPrimitiveValue.CSS_STRING, id));
				}
			}
			if (lu == null) {
				return result;
			}
			if (lu.getLexicalUnitType() != LexicalUnit.SAC_OPERATOR_COMMA) {
				throw createInvalidLexicalUnitDOMException(lu.getLexicalUnitType());
			}
			lu = lu.getNextLexicalUnit();
			if (lu == null) {
				throw createMalformedLexicalUnitDOMException();
			}
		}
	}

	/**
	 * Implements
	 * {@link ValueManager#computeValue(CSSStylableElement,String,CSSEngine,int,StyleMap,Value)}.
	 */
	public CSSValue computeValue(CSSEngine engine, int idx, CSSValue value) {
		if (value == DEFAULT_VALUE) {
			CSSContext ctx = engine.getCSSContext();
			value = ctx.getDefaultFontFamily();
		}
		return value;
	}
}

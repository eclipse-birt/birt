/*

 Copyright 2002-2003  The Apache Software Foundation 

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 */
package org.eclipse.birt.report.engine.css.engine.value.css;

import org.eclipse.birt.report.engine.css.engine.CSSContext;
import org.eclipse.birt.report.engine.css.engine.CSSEngine;
import org.eclipse.birt.report.engine.css.engine.CSSStylableElement;
import org.eclipse.birt.report.engine.css.engine.ValueManager;
import org.eclipse.birt.report.engine.css.engine.value.IdentifierManager;
import org.eclipse.birt.report.engine.css.engine.value.StringMap;
import org.eclipse.birt.report.engine.css.engine.value.Value;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSValue;


/**
 * This class provides a manager for the 'font-weight' property values.
 * 
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id: FontWeightManager.java,v 1.2 2005/10/20 07:47:44 wyan Exp $
 */
public class FontWeightManager extends IdentifierManager {

	/**
	 * The identifier values.
	 */
	protected final static StringMap values = new StringMap();
	static {
		values.put(CSSConstants.CSS_BOLD_VALUE, CSSValueConstants.BOLD_VALUE);
		values.put(CSSConstants.CSS_BOLDER_VALUE, CSSValueConstants.BOLDER_VALUE);
		values
				.put(CSSConstants.CSS_LIGHTER_VALUE,
						CSSValueConstants.LIGHTER_VALUE);
		values.put(CSSConstants.CSS_NORMAL_VALUE, CSSValueConstants.NORMAL_VALUE);
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
		return CSSConstants.CSS_FONT_WEIGHT_PROPERTY;
	}

	/**
	 * Implements {@link ValueManager#getDefaultValue()}.
	 */
	public Value getDefaultValue() {
		return CSSValueConstants.NORMAL_VALUE;
	}

	/**
	 * Implements {@link ValueManager#createValue(LexicalUnit,CSSEngine)}.
	 */
	public Value createValue(LexicalUnit lu, CSSEngine engine)
			throws DOMException {
		if (lu.getLexicalUnitType() == LexicalUnit.SAC_INTEGER) {
			int i = lu.getIntegerValue();
			switch (i) {
			case 100:
				return CSSValueConstants.NUMBER_100;
			case 200:
				return CSSValueConstants.NUMBER_200;
			case 300:
				return CSSValueConstants.NUMBER_300;
			case 400:
				return CSSValueConstants.NUMBER_400;
			case 500:
				return CSSValueConstants.NUMBER_500;
			case 600:
				return CSSValueConstants.NUMBER_600;
			case 700:
				return CSSValueConstants.NUMBER_700;
			case 800:
				return CSSValueConstants.NUMBER_800;
			case 900:
				return CSSValueConstants.NUMBER_900;
			}
			throw createInvalidFloatValueDOMException(i);
		}
		return super.createValue(lu, engine);
	}

	/**
	 * Implements {@link ValueManager#createFloatValue(short,float)}.
	 */
	public Value createFloatValue(short type, float floatValue)
			throws DOMException {
		if (type == CSSPrimitiveValue.CSS_NUMBER) {
			int i = (int) floatValue;
			if (floatValue == i) {
				switch (i) {
				case 100:
					return CSSValueConstants.NUMBER_100;
				case 200:
					return CSSValueConstants.NUMBER_200;
				case 300:
					return CSSValueConstants.NUMBER_300;
				case 400:
					return CSSValueConstants.NUMBER_400;
				case 500:
					return CSSValueConstants.NUMBER_500;
				case 600:
					return CSSValueConstants.NUMBER_600;
				case 700:
					return CSSValueConstants.NUMBER_700;
				case 800:
					return CSSValueConstants.NUMBER_800;
				case 900:
					return CSSValueConstants.NUMBER_900;
				}
			}
		}
		throw createInvalidFloatValueDOMException(floatValue);
	}

	/**
	 * Implements {@link
	 * ValueManager#computeValue(CSSStylableElement,String,CSSEngine,int,StyleMap,Value)}.
	 */
	public CSSValue computeValue(CSSStylableElement elt, CSSEngine engine, int idx, CSSValue value) {
        if (value == CSSValueConstants.BOLDER_VALUE) {

            CSSContext ctx = engine.getCSSContext();
            CSSStylableElement p = (CSSStylableElement)elt.getParent();
            float fw;
            if (p == null) {
                fw = 400;
            } else {
                Value v = (Value)p.getComputedStyle().getProperty(idx);
                fw = v.getFloatValue();
            }
            return createFontWeight(ctx.getBolderFontWeight(fw));
        } else if (value == CSSValueConstants.LIGHTER_VALUE) {

            CSSContext ctx = engine.getCSSContext();
            CSSStylableElement p = (CSSStylableElement)elt.getParent();
            float fw;
            if (p == null) {
                fw = 400;
            } else {
                Value v = (Value)p.getComputedStyle().getProperty(idx);
                fw = v.getFloatValue();
            }
            return createFontWeight(ctx.getLighterFontWeight(fw));
        } else if (value == CSSValueConstants.NORMAL_VALUE) {
            return CSSValueConstants.NUMBER_400;
        } else if (value == CSSValueConstants.BOLD_VALUE) {
            return CSSValueConstants.NUMBER_700;
        }
        return value;
	}

	/**
	 * Returns the CSS value associated with the given font-weight.
	 */
	protected CSSPrimitiveValue createFontWeight(float f) {
		switch ((int) f) {
		case 100:
			return CSSValueConstants.NUMBER_100;
		case 200:
			return CSSValueConstants.NUMBER_200;
		case 300:
			return CSSValueConstants.NUMBER_300;
		case 400:
			return CSSValueConstants.NUMBER_400;
		case 500:
			return CSSValueConstants.NUMBER_500;
		case 600:
			return CSSValueConstants.NUMBER_600;
		case 700:
			return CSSValueConstants.NUMBER_700;
		case 800:
			return CSSValueConstants.NUMBER_800;
		default: // 900
			return CSSValueConstants.NUMBER_900;
		}
	}

	/**
	 * Implements {@link IdentifierManager#getIdentifiers()}.
	 */
	public StringMap getIdentifiers() {
		return values;
	}
}

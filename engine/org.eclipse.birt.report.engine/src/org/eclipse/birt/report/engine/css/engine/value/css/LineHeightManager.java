/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Apache - initial API and implementation
 *  Actuate Corporation - changed by Actuate
 *******************************************************************************/
/*

   Copyright 1999-2003  The Apache Software Foundation 

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

import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.dom.StyleDeclaration;
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

public class LineHeightManager extends AbstractLengthManager {

	/**
	 * The identifier values.
	 */
	protected final static StringMap values = new StringMap();
	static {
		values.put(CSSConstants.CSS_NORMAL_VALUE, CSSValueConstants.NORMAL_VALUE);
	}

	public LineHeightManager() {
	}

	public String getPropertyName() {
		return CSSConstants.CSS_LINE_HEIGHT_PROPERTY;
	}

	public boolean isInheritedProperty() {
		return true;
	}

	public Value getDefaultValue() {
		return CSSValueConstants.NORMAL_VALUE;
	}

	public Value createValue(LexicalUnit lu, CSSEngine engine)
			throws DOMException {
		switch (lu.getLexicalUnitType()) {
		case LexicalUnit.SAC_IDENT:
			String s = lu.getStringValue().toLowerCase().intern();
			Object v = values.get(s);
			if (v == null) {
				throw createInvalidIdentifierDOMException(lu.getStringValue());
			}
			return (Value) v;
		}
		return super.createValue( lu, engine );
//		throw createInvalidLexicalUnitDOMException(lu.getLexicalUnitType());
	}

	/**
	 * Implements {@link
	 * ValueManager#computeValue(CSSStylableElement,String,CSSEngine,int,StyleMap,Value)}.
	 */
	public Value computeValue(CSSStylableElement elt, CSSEngine engine,
			int idx, Value value) {
		if (value.getCssValueType() == CSSValue.CSS_PRIMITIVE_VALUE) {
			switch (value.getPrimitiveType()) {
			case CSSPrimitiveValue.CSS_IDENT:
				return value;
			case CSSPrimitiveValue.CSS_PERCENTAGE:
				float scale = value.getFloatValue();
				IStyle cs = (IStyle) elt.getComputedStyle();
				assert cs != null;
				Value fontSize = (Value) cs.getProperty(IStyle.STYLE_FONT_SIZE);
				assert fontSize != null;
				float fs = fontSize.getFloatValue();
				return new FloatValue(CSSPrimitiveValue.CSS_NUMBER, fs * scale);
			}
		}
		return super.computeValue(elt, engine, idx, value);
	}

}

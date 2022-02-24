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
package org.eclipse.birt.report.engine.css.engine.value.css;

import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.engine.CSSEngine;
import org.eclipse.birt.report.engine.css.engine.CSSStylableElement;
import org.eclipse.birt.report.engine.css.engine.ValueManager;
import org.eclipse.birt.report.engine.css.engine.value.AbstractColorManager;
import org.eclipse.birt.report.engine.css.engine.value.StringMap;
import org.eclipse.birt.report.engine.css.engine.value.Value;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.dom.DOMException;

public class BorderColorManager extends ColorManager {
	/**
	 * The identifier values.
	 */
	protected final static StringMap values = new StringMap();
	static {
		AbstractColorManager.addColorIndent(values);
		values.put(CSSConstants.CSS_TRANSPARENT_VALUE, CSSValueConstants.TRANSPARENT_VALUE);
	}

	public StringMap getIdentifiers() {
		return values;
	}

	public BorderColorManager(String propertyName) {
		super(propertyName, false, CSSValueConstants.BLACK_RGB_VALUE);
	}

	/**
	 * Implements {@link ValueManager#createValue(LexicalUnit,CSSEngine)}.
	 */
	public Value createValue(LexicalUnit lu, CSSEngine engine) throws DOMException {
		if (lu.getLexicalUnitType() == LexicalUnit.SAC_IDENT) {
			if (CSSConstants.CSS_TRANSPARENT_VALUE.equals(lu.getStringValue())) {
				return CSSValueConstants.TRANSPARENT_VALUE;
			}
		}
		return super.createValue(lu, engine);
	}

	/**
	 * Implements
	 * {@link ValueManager#computeValue(CSSStylableElement,String,CSSEngine,int,StyleMap,Value)}.
	 */
	public Value computeValue(CSSStylableElement elt, CSSEngine engine, int idx, Value value) {
		if (value == null) {
			return (Value) elt.getComputedStyle().getProperty(IStyle.STYLE_COLOR);
		}

		if (CSSValueConstants.TRANSPARENT_VALUE == value) {
			return value;
		}

		return super.computeValue(elt, engine, idx, value);
	}

}

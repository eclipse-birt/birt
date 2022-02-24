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
 *  Actuate Corporation  - modification of Batik's IdentifierManager.java to support BIRT's CSS rules
 *******************************************************************************/

package org.eclipse.birt.report.engine.css.engine.value;

import org.eclipse.birt.report.engine.css.engine.CSSEngine;
import org.eclipse.birt.report.engine.css.engine.ValueManager;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSValueConstants;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSPrimitiveValue;

/**
 * This class provides a manager for the property with support for identifier
 * values.
 * 
 */
public abstract class IdentifierManager extends AbstractValueManager {

	/**
	 * Implements {@link ValueManager#createValue(LexicalUnit,CSSEngine)}.
	 */
	public Value createValue(LexicalUnit lu, CSSEngine engine) throws DOMException {
		switch (lu.getLexicalUnitType()) {
		case LexicalUnit.SAC_INHERIT:
			return CSSValueConstants.INHERIT_VALUE;

		case LexicalUnit.SAC_IDENT:
			String s = lu.getStringValue().toLowerCase().intern();
			Object v = getIdentifiers().get(s);
			if (v == null) {
				throw createInvalidIdentifierDOMException(lu.getStringValue());
			}
			return (Value) v;

		default:
			throw createInvalidLexicalUnitDOMException(lu.getLexicalUnitType());
		}
	}

	protected Value createStringValue(short type, String value, CSSEngine engine) throws DOMException {
		if (type != CSSPrimitiveValue.CSS_IDENT) {
			throw createInvalidStringTypeDOMException(type);
		}
		Object v = getIdentifiers().get(value.toLowerCase().intern());
		if (v == null) {
			throw createInvalidIdentifierDOMException(value);
		}
		return (Value) v;
	}

	/**
	 * Returns the map that contains the name/value mappings for each possible
	 * identifiers.
	 */
	public abstract StringMap getIdentifiers();
}

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

import org.eclipse.birt.report.engine.css.engine.CSSEngine;
import org.eclipse.birt.report.engine.css.engine.value.AbstractValueManager;
import org.eclipse.birt.report.engine.css.engine.value.FloatValue;
import org.eclipse.birt.report.engine.css.engine.value.Value;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSPrimitiveValue;

public class IntegerManager extends AbstractValueManager {

	String propertyName;
	boolean inherit;
	int defaultValue;

	public IntegerManager(String propertyName, boolean inherit, int defaultValue) {
		this.propertyName = propertyName;
		this.inherit = inherit;
		this.defaultValue = defaultValue;
	}

	@Override
	public String getPropertyName() {
		return propertyName;
	}

	@Override
	public boolean isInheritedProperty() {
		return inherit;
	}

	@Override
	public Value getDefaultValue() {
		return new FloatValue(CSSPrimitiveValue.CSS_NUMBER, defaultValue);
	}

	@Override
	public Value createValue(LexicalUnit lu, CSSEngine engine) throws DOMException {
		switch (lu.getLexicalUnitType()) {
		case LexicalUnit.SAC_INHERIT:
			return CSSValueConstants.INHERIT_VALUE;

		case LexicalUnit.SAC_INTEGER:
			return new FloatValue(CSSPrimitiveValue.CSS_NUMBER, lu.getIntegerValue());
		default:
			throw createInvalidLexicalUnitDOMException(lu.getLexicalUnitType());
		}
	}
}

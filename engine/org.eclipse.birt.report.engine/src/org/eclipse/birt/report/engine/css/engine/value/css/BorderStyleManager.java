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

import org.eclipse.birt.report.engine.css.engine.value.IdentifierManager;
import org.eclipse.birt.report.engine.css.engine.value.StringMap;
import org.eclipse.birt.report.engine.css.engine.value.Value;

public class BorderStyleManager extends IdentifierManager {

	/**
	 * The identifier values.
	 */
	protected final static StringMap values = new StringMap();
	static {
		values.put(CSSConstants.CSS_NONE_VALUE, CSSValueConstants.NONE_VALUE);
		values.put(CSSConstants.CSS_HIDDEN_VALUE, CSSValueConstants.HIDDEN_VALUE);
		values.put(CSSConstants.CSS_DOTTED_VALUE, CSSValueConstants.DOTTED_VALUE);
		values.put(CSSConstants.CSS_DASHED_VALUE, CSSValueConstants.DASHED_VALUE);
		values.put(CSSConstants.CSS_SOLID_VALUE, CSSValueConstants.SOLID_VALUE);
		values.put(CSSConstants.CSS_DOUBLE_VALUE, CSSValueConstants.DOUBLE_VALUE);
		values.put(CSSConstants.CSS_GROOVE_VALUE, CSSValueConstants.GROOVE_VALUE);

		values.put(CSSConstants.CSS_RIDGE_VALUE, CSSValueConstants.RIDGE_VALUE);

		values.put(CSSConstants.CSS_INSET_VALUE, CSSValueConstants.INSET_VALUE);

		values.put(CSSConstants.CSS_OUTSET_VALUE, CSSValueConstants.OUTSET_VALUE);

	}

	@Override
	public StringMap getIdentifiers() {
		return values;
	}

	String propertyName;

	public BorderStyleManager(String propertyName) {
		this.propertyName = propertyName;
	}

	@Override
	public String getPropertyName() {
		return propertyName;
	}

	@Override
	public boolean isInheritedProperty() {
		return false;
	}

	@Override
	public Value getDefaultValue() {
		return CSSValueConstants.NONE_VALUE;
	}

}

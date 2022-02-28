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

public class BackgroundRepeatManager extends IdentifierManager {

	/**
	 * The identifier values.
	 */
	protected final static StringMap values = new StringMap();
	static {
		values.put(CSSConstants.CSS_REPEAT_VALUE, CSSValueConstants.REPEAT_VALUE);
		values.put(CSSConstants.CSS_REPEAT_X_VALUE, CSSValueConstants.REPEAT_X_VALUE);
		values.put(CSSConstants.CSS_REPEAT_Y_VALUE, CSSValueConstants.REPEAT_Y_VALUE);
		values.put(CSSConstants.CSS_NO_REPEAT_VALUE, CSSValueConstants.NO_REPEAT_VALUE);
	}

	@Override
	public StringMap getIdentifiers() {
		return values;
	}

	public BackgroundRepeatManager() {
	}

	@Override
	public String getPropertyName() {
		return CSSConstants.CSS_BACKGROUND_REPEAT_PROPERTY;
	}

	@Override
	public boolean isInheritedProperty() {
		return false;
	}

	@Override
	public Value getDefaultValue() {
		return CSSValueConstants.REPEAT_VALUE;
	}
}

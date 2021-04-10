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

	public StringMap getIdentifiers() {
		return values;
	}

	public BackgroundRepeatManager() {
	}

	public String getPropertyName() {
		return CSSConstants.CSS_BACKGROUND_REPEAT_PROPERTY;
	}

	public boolean isInheritedProperty() {
		return false;
	}

	public Value getDefaultValue() {
		return CSSValueConstants.REPEAT_VALUE;
	}
}

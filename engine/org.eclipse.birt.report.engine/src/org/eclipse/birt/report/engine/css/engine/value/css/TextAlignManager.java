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

public class TextAlignManager extends IdentifierManager {

	/**
	 * The identifier values.
	 */
	protected final static StringMap values = new StringMap();
	static {
		values.put(CSSConstants.CSS_LEFT_VALUE, CSSValueConstants.LEFT_VALUE);
		values.put(CSSConstants.CSS_RIGHT_VALUE, CSSValueConstants.RIGHT_VALUE);
		values.put(CSSConstants.CSS_CENTER_VALUE, CSSValueConstants.CENTER_VALUE);
		values.put(CSSConstants.CSS_JUSTIFY_VALUE, CSSValueConstants.JUSTIFY_VALUE);
	}

	public StringMap getIdentifiers() {
		return values;
	}

	public TextAlignManager(String propertyName, Value defaultValue) {
		this.propertyName = propertyName;
		this.defaultValue = defaultValue;
	}

	protected String propertyName;

	protected Value defaultValue;

	public String getPropertyName() {
		return propertyName;
	}

	public boolean isInheritedProperty() {
		return true;
	}

	public Value getDefaultValue() {
		return defaultValue;
	}
}

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

public class TextTransformManager extends IdentifierManager {

	/**
	 * The identifier values.
	 */
	protected final static StringMap values = new StringMap();
	static {
		values.put(CSSConstants.CSS_CAPITALIZE_VALUE, CSSValueConstants.CAPITALIZE_VALUE);
		values.put(CSSConstants.CSS_UPPERCASE_VALUE, CSSValueConstants.UPPERCASE_VALUE);
		values.put(CSSConstants.CSS_LOWERCASE_VALUE, CSSValueConstants.LOWERCASE_VALUE);
		values.put(CSSConstants.CSS_NONE_VALUE, CSSValueConstants.NONE_VALUE);
	}

	public StringMap getIdentifiers() {
		return values;
	}

	public TextTransformManager() {
	}

	public String getPropertyName() {
		return CSSConstants.CSS_TEXT_TRANSFORM_PROPERTY;
	}

	public boolean isInheritedProperty() {
		return true;
	}

	public Value getDefaultValue() {
		return CSSValueConstants.NONE_VALUE;
	}
}

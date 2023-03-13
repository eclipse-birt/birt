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

	@Override
	public StringMap getIdentifiers() {
		return values;
	}

	public TextTransformManager() {
	}

	@Override
	public String getPropertyName() {
		return CSSConstants.CSS_TEXT_TRANSFORM_PROPERTY;
	}

	@Override
	public boolean isInheritedProperty() {
		return true;
	}

	@Override
	public Value getDefaultValue() {
		return CSSValueConstants.NONE_VALUE;
	}
}

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

public class PageBreakInsideManager extends IdentifierManager {

	/**
	 * The identifier values.
	 */
	protected final static StringMap values = new StringMap();
	static {
		values.put(CSSConstants.CSS_AVOID_VALUE, CSSValueConstants.AVOID_VALUE);
		values.put(CSSConstants.CSS_AUTO_VALUE, CSSValueConstants.AUTO_VALUE);
	}

	public StringMap getIdentifiers() {
		return values;
	}

	public PageBreakInsideManager() {
	}

	public String getPropertyName() {
		return CSSConstants.CSS_PAGE_BREAK_INSIDE_PROPERTY;
	}

	public boolean isInheritedProperty() {
		return true;
	}

	public Value getDefaultValue() {
		return CSSValueConstants.AUTO_VALUE;
	}
}

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
import org.eclipse.birt.report.engine.css.engine.value.birt.BIRTConstants;
import org.eclipse.birt.report.engine.css.engine.value.birt.BIRTValueConstants;

public class PageBreakBeforeAfterManager extends IdentifierManager {

	/**
	 * The identifier values.
	 */
	protected final static StringMap values = new StringMap();
	static {
		values.put(CSSConstants.CSS_AUTO_VALUE, CSSValueConstants.AUTO_VALUE);
		values.put(CSSConstants.CSS_ALWAYS_VALUE, CSSValueConstants.ALWAYS_VALUE);
		values.put(CSSConstants.CSS_AVOID_VALUE, CSSValueConstants.AVOID_VALUE);
		values.put(CSSConstants.CSS_LEFT_VALUE, CSSValueConstants.LEFT_VALUE);
		values.put(CSSConstants.CSS_RIGHT_VALUE, CSSValueConstants.RIGHT_VALUE);
		values.put(BIRTConstants.BIRT_SOFT_VALUE, BIRTValueConstants.SOFT_VALUE);
	}

	@Override
	public StringMap getIdentifiers() {
		return values;
	}

	String propertyName;

	public PageBreakBeforeAfterManager(String propertyName) {
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
		return CSSValueConstants.AUTO_VALUE;
	}
}

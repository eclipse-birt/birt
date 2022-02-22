/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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

public class DirectionManager extends IdentifierManager {

	/**
	 * The identifier values.
	 */
	protected final static StringMap values = new StringMap();
	static {
		values.put(CSSConstants.CSS_RTL_VALUE, CSSValueConstants.RTL_VALUE);
		values.put(CSSConstants.CSS_LTR_VALUE, CSSValueConstants.LTR_VALUE);
	}

	@Override
	public StringMap getIdentifiers() {
		return values;
	}

	@Override
	public String getPropertyName() {
		return BIRTConstants.CSS_DIRECTION_PROPERTY;
	}

	@Override
	public boolean isInheritedProperty() {
		return true;
	}

	@Override
	public Value getDefaultValue() {
		return CSSValueConstants.NULL_STRING_VALUE;
	}
}

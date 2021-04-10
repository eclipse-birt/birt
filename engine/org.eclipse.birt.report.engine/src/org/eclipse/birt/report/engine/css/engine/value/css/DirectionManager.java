/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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

	public StringMap getIdentifiers() {
		return values;
	}

	public String getPropertyName() {
		return BIRTConstants.CSS_DIRECTION_PROPERTY;
	}

	public boolean isInheritedProperty() {
		return true;
	}

	public Value getDefaultValue() {
		return CSSValueConstants.NULL_STRING_VALUE;
	}
}

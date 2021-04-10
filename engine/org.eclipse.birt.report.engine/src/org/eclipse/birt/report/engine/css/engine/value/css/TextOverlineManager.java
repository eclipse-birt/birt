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
import org.eclipse.birt.report.engine.css.engine.value.birt.BIRTConstants;

public class TextOverlineManager extends IdentifierManager {

	/**
	 * The identifier values.
	 */
	protected final static StringMap values = new StringMap();
	static {
		values.put(CSSConstants.CSS_NONE_VALUE, CSSValueConstants.NONE_VALUE);
		values.put(CSSConstants.CSS_OVERLINE_VALUE, CSSValueConstants.OVERLINE_VALUE);
	}

	public StringMap getIdentifiers() {
		return values;
	}

	public String getPropertyName() {
		return BIRTConstants.BIRT_TEXT_OVERLINE_PROPERTY;
	}

	public boolean isInheritedProperty() {
		return true;
	}

	public Value getDefaultValue() {
		return CSSValueConstants.NONE_VALUE;
	}
}
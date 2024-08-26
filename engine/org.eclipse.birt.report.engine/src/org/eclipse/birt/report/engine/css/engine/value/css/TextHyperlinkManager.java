/*******************************************************************************
 * Copyright (c) 2024 Thomas Gutmann.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Thomas Gutmann  - initial API and implementation
 *  
 *  @since 4.17
 *******************************************************************************/

package org.eclipse.birt.report.engine.css.engine.value.css;

import org.eclipse.birt.report.engine.css.engine.value.IdentifierManager;
import org.eclipse.birt.report.engine.css.engine.value.StringMap;
import org.eclipse.birt.report.engine.css.engine.value.Value;
import org.eclipse.birt.report.engine.css.engine.value.birt.BIRTConstants;

public class TextHyperlinkManager extends IdentifierManager {

	/**
	 * The identifier values.
	 */
	protected final static StringMap values = new StringMap();
	static {
		values.put(CSSConstants.CSS_NORMAL_VALUE, CSSValueConstants.NORMAL_VALUE);
		values.put(CSSConstants.CSS_TEXT_HYPERLINK_UNDECORATION_VALUE, CSSValueConstants.UNDECORATED);
	}

	@Override
	public StringMap getIdentifiers() {
		return values;
	}

	@Override
	public String getPropertyName() {
		return BIRTConstants.BIRT_TEXT_HYPERLINK_SYTLE_PROPERTY;
	}

	@Override
	public boolean isInheritedProperty() {
		return true;
	}

	@Override
	public Value getDefaultValue() {
		return CSSValueConstants.NORMAL_VALUE;
	}
}

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
package org.eclipse.birt.report.engine.css.engine.value.birt;

import org.eclipse.birt.report.engine.css.engine.value.IdentifierManager;
import org.eclipse.birt.report.engine.css.engine.value.StringMap;
import org.eclipse.birt.report.engine.css.engine.value.Value;

public class BooleanManager extends IdentifierManager {

	protected final static StringMap values = new StringMap();
	static {
		values.put(BIRTConstants.BIRT_TRUE_VALUE, BIRTValueConstants.TRUE_VALUE);
		values.put(BIRTConstants.BIRT_FALSE_VALUE, BIRTValueConstants.FALSE_VALUE);
	}

	protected String propertyName;
	protected boolean inherit;
	protected boolean defaultValue;

	public BooleanManager(String propertyName, boolean inherit, boolean defaultValue) {
		this.propertyName = propertyName;
		this.inherit = inherit;
		this.defaultValue = defaultValue;
	}

	public StringMap getIdentifiers() {
		return values;
	}

	public String getPropertyName() {

		return propertyName;
	}

	public boolean isInheritedProperty() {
		return inherit;
	}

	public Value getDefaultValue() {
		return defaultValue ? BIRTValueConstants.TRUE_VALUE : BIRTValueConstants.FALSE_VALUE;
	}

}

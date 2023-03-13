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

import org.eclipse.birt.report.engine.css.engine.value.AbstractLengthManager;
import org.eclipse.birt.report.engine.css.engine.value.Value;

public class LengthManager extends AbstractLengthManager {

	String propertyName;
	boolean inherit;
	Value defaultValue;

	public LengthManager(String propertyName, boolean inherit, Value defaultValue) {
		this.propertyName = propertyName;
		this.inherit = inherit;
		this.defaultValue = defaultValue;
	}

	@Override
	public String getPropertyName() {
		return propertyName;
	}

	@Override
	public boolean isInheritedProperty() {
		return inherit;
	}

	@Override
	public Value getDefaultValue() {
		return defaultValue;
	}

}

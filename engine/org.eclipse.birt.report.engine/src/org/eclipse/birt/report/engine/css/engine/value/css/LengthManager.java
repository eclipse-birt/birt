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

	public String getPropertyName() {
		return propertyName;
	}

	public boolean isInheritedProperty() {
		return inherit;
	}

	public Value getDefaultValue() {
		return defaultValue;
	}

}

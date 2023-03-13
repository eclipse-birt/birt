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

import org.eclipse.birt.report.engine.css.engine.value.AbstractColorManager;
import org.eclipse.birt.report.engine.css.engine.value.Value;

public class ColorManager extends AbstractColorManager {

	/**
	 * The default color value.
	 */
	protected Value defaultValue;
	protected boolean inherit;
	protected String propertyName;

	public ColorManager(String propertyName, boolean inherit, Value defaultValue) {
		this.propertyName = propertyName;
		this.defaultValue = defaultValue;
		this.inherit = inherit;
	}

	/**
	 * Implements
	 * {@link org.apache.batik.css.engine.value.ValueManager#isInheritedProperty()}.
	 */
	@Override
	public boolean isInheritedProperty() {
		return inherit;
	}

	/**
	 * Implements
	 * {@link org.apache.batik.css.engine.value.ValueManager#getPropertyName()}.
	 */
	@Override
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * Implements
	 * {@link org.apache.batik.css.engine.value.ValueManager#getDefaultValue()}.
	 */
	@Override
	public Value getDefaultValue() {
		return defaultValue;
	}
}

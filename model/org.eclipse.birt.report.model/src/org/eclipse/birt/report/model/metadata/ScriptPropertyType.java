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

package org.eclipse.birt.report.model.metadata;

import org.eclipse.birt.report.model.core.Module;

/**
 * Represents script property type. Scripts are stored as strings. Scripts are
 * not validated in the model; BIRT relies on run-time validation in the
 * scripting engine.
 */

public class ScriptPropertyType extends TextualPropertyType {

	/**
	 * Display name key.
	 */

	private static final String DISPLAY_NAME_KEY = "Property.script"; //$NON-NLS-1$

	/**
	 * Constructor.
	 */

	public ScriptPropertyType() {
		super(DISPLAY_NAME_KEY);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.metadata.PropertyType#getTypeCode()
	 */
	@Override
	public int getTypeCode() {
		return SCRIPT_TYPE;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.metadata.PropertyType#getName()
	 */
	@Override
	public String getName() {
		return SCRIPT_TYPE_NAME;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#toString(java
	 * .lang.Object)
	 */

	@Override
	public String toString(Module module, PropertyDefn defn, Object value) {
		return (String) value;
	}

}

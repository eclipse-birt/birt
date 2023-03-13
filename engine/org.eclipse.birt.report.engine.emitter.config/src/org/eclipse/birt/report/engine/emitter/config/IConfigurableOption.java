/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.engine.emitter.config;

/**
 * This interface is a representation of configurable option for emitter.
 */
public interface IConfigurableOption {

	/**
	 * The option data type constants
	 */
	enum DataType {
		STRING, BOOLEAN, INTEGER, FLOAT,
	}

	/**
	 * The option display type constants
	 */
	enum DisplayType {
		TEXT, COMBO, INPUTCOMBO, CHECKBOX,
	}

	/**
	 * Returns the name of this option.
	 */
	String getName();

	/**
	 * Returns the display name of this option.
	 */
	String getDisplayName();

	/**
	 * Returns the data type of this option.
	 */
	DataType getDataType();

	/**
	 * Returns the display type of this option.
	 */
	DisplayType getDisplayType();

	/**
	 * Returns all choice values of this option if applicable.
	 */
	IOptionValue[] getChoices();

	/**
	 * Returns the default value of this option.
	 */
	Object getDefaultValue();

	/**
	 * Returns the description of this option.
	 */
	String getDescription();

	/**
	 * Returns the tool tip of this option.
	 */
	String getToolTip();

	/**
	 * Returns the category of this option if applicable.
	 */
	String getCategory();

	/**
	 * Returns if this option is enabled.
	 */
	boolean isEnabled();

}

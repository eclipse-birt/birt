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

package org.eclipse.birt.doc.schema;

/**
 * Css type
 *
 */

public final class CssType {

	/**
	 * name of table property
	 */

	private String name;

	/**
	 * All allowed values;
	 */

	private String values;

	/**
	 * Birt choice values
	 */

	private String birtChoiceValues;

	/**
	 * Default value
	 */

	private String initialValues;

	/**
	 * Can inherite or not
	 */

	private String inherited;

	/**
	 * Gets inherited
	 *
	 * @return inherited value
	 */

	public String getInherited() {
		return inherited;
	}

	/**
	 * Sets inherited value
	 *
	 * @param inherited
	 */

	public void setInherited(String inherited) {
		this.inherited = inherited;
	}

	/**
	 * Gets initial values
	 *
	 * @return values
	 */

	public String getInitialValues() {
		return initialValues;
	}

	/**
	 * Sets initial values
	 *
	 * @param initialValues
	 */

	public void setInitialValues(String initialValues) {
		this.initialValues = initialValues;
	}

	/**
	 * Gets name
	 *
	 * @return name
	 */

	public String getName() {
		return name;
	}

	/**
	 * Sets name
	 *
	 * @param name
	 */

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets w3c values
	 *
	 * @return w3c values
	 */

	public String getValues() {
		return values;
	}

	/**
	 * Sets w2c values
	 *
	 * @param values
	 */

	public void setValues(String values) {
		this.values = values;
	}

	/**
	 * Gets birt choice values
	 *
	 * @return birt choice values
	 */

	public String getBirtChoiceValues() {
		return birtChoiceValues;
	}

	/**
	 * Sets birt choice values
	 *
	 * @param birtChoiceValues
	 */

	public void setBirtChoiceValues(String birtChoiceValues) {
		this.birtChoiceValues = birtChoiceValues;
	}
}

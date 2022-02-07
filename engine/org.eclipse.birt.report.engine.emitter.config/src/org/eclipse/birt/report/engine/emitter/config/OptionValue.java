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
 * This interface is a representation of option value.
 */
public final class OptionValue implements IOptionValue {

	/** The value. */
	private Object value;

	/** The name value. */
	private String name;

	/**
	 * Constructs an entry of option value with the specified value.
	 * 
	 * @param value the option value.
	 */
	public OptionValue(Object value) {
		this(value, String.valueOf(value));
	}

	/**
	 * Constructs an entry of option value with the specified value and the
	 * specified display value.
	 * 
	 * @param value        the option value.
	 * @param displayValue the display value.
	 */
	public OptionValue(Object value, String name) {
		setValue(value);
		setName(name);
	}

	/**
	 * Returns option value.
	 * 
	 * @return option value.
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Sets option value.
	 * 
	 * @param value the option value to set.
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * Returns name.
	 * 
	 * @return name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets name.
	 * 
	 * @param name the name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
}

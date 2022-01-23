/*******************************************************************************
 * Copyright (c)2007 Actuate Corporation.
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

package org.eclipse.birt.report.engine.api;

import java.util.HashMap;
import java.util.Map;

public class TaskOption implements ITaskOption {
	/**
	 * a hash map that stores the rendering options
	 */
	protected Map options;

	/**
	 * constructor
	 */
	public TaskOption() {
		options = new HashMap();
	}

	/**
	 * Constructor.
	 * 
	 * @param options
	 */
	public TaskOption(Map options) {
		this.options = options;
	}

	/**
	 * set value for one rendering option
	 * 
	 * @param name  the option name
	 * @param value value for the option
	 */
	public void setOption(String name, Object value) {
		options.put(name, value);
	}

	/**
	 * get option value for one rendering option
	 * 
	 * @param name the option name
	 * @return the option value
	 */
	public Object getOption(String name) {
		return options.get(name);
	}

	/**
	 * Check if an option is defined.
	 */
	public boolean hasOption(String name) {
		return options.containsKey(name);
	}

	/**
	 * Get options.
	 * 
	 * @return options
	 */
	public Map getOptions() {
		return options;
	}

	/**
	 * Get option value by name.
	 * 
	 * @param name the option name
	 * @return the option value
	 */
	public String getStringOption(String name) {
		Object value = options.get(name);
		if (value instanceof String) {
			return (String) value;
		}
		return null;
	}

	/**
	 * Get boolean option value by name.
	 * 
	 * @param name         the option name
	 * @param defaultValue default option value
	 * @return default value
	 */
	public boolean getBooleanOption(String name, boolean defaultValue) {
		Object value = options.get(name);
		if (value instanceof Boolean) {
			return ((Boolean) value).booleanValue();
		} else if (value instanceof String) {
			return "true".equalsIgnoreCase((String) value); //$NON-NLS-1$
		}
		return defaultValue;
	}

	public int getIntOption(String name, int defaultValue) {
		Object value = options.get(name);
		if (value instanceof Integer) {
			return ((Integer) value).intValue();
		}
		return defaultValue;
	}
}

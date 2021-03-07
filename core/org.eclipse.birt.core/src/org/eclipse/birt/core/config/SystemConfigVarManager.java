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
package org.eclipse.birt.core.config;

/**
 * Implements a default config variable manager using System support
 */
public class SystemConfigVarManager {

	/**
	 * Constructor
	 */
	public SystemConfigVarManager() {
	}

	/**
	 * @param key the configuration variable name
	 * @return The value for the configuration variable. null if it is not set.
	 */
	public String getConfigVar(String key) {
		return System.getProperty(key);
	}

	/**
	 * @param key          the configuration variable name
	 * @param defaultValue returns this value is the configuration variable is not
	 *                     set
	 * @return The value for the configuration variable. In case the config var is
	 *         not set, or there is an exception, defaultValue is returned.
	 */
	public String getConfigVar(String key, String defaultValue) {
		String ret = null;

		try {
			ret = getConfigVar(key);
		} catch (Exception e) {
			return defaultValue;
		}

		if (ret == null) {
			return defaultValue;
		}
		return ret;
	}

	/**
	 * @param key the configuration variable name
	 * @return true if set to true (case insensitive), false in any other cases
	 */
	public boolean getConfigBoolean(String key) {
		// We do not use Boolean.getProperty() so that this method is more generic and
		// can get config
		// var from other sources, i.e., a configuration file
		String booleanValue;

		try {
			booleanValue = getConfigVar(key);
		} catch (Exception e) {
			return false;
		}

		if (booleanValue == null || booleanValue.compareToIgnoreCase("true") != 0) {
			return false;
		}
		return true;
	}

	/**
	 * @param key the configuration variable name
	 * @return the value for the configuration variable. returns null if the config
	 *         var is not set or not set to an integer or any other exception
	 *         happens.
	 */
	public Integer getConfigInteger(String key) {
		try {
			// We do not use Integer.getProperty so that this method is more generic and can
			// get config
			// var from other sources, i.e., a configuration file
			String intString = getConfigVar(key);

			if (intString != null) {
				return Integer.valueOf(intString);
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * @param key          the configuration variable name
	 * @param defaultValue returns this value is the configuration variable is not
	 *                     set
	 * @return the value for the configuration variable. In case the config var is
	 *         not set, or not set to a number, or there is an exception,
	 *         defaultValue is returned.
	 */
	public int getConfigInteger(String key, int defaultValue) {
		try {
			// We do not use Integer.getProperty so that this method is more generic and can
			// get config
			// var from other sources, i.e., a configuration file
			String intString = getConfigVar(key);

			if (intString == null) {
				return defaultValue;
			}
			return Integer.parseInt(intString);
		} catch (Exception e) {
			return defaultValue;
		}
	}
}

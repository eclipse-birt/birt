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

package org.eclipse.birt.core.config;

/**
 * Allows BIRT components to access configuration variables in a uniform way.
 */
public interface IConfigVarManager {
	/**
	 * @param key the configuration variable name
	 * @return The value for the configuration variable. null if it is not set.
	 */
	String getConfigVar(String key);

	/**
	 * @param key          the configuration variable name
	 * @param defaultValue returns this value is the configuration variable is not
	 *                     set
	 * @return The value for the configuration variable. In case the config var is
	 *         not set, or there is an exception, defaultValue is returned.
	 */
	String getConfigVar(String key, String defaultValue);

	/**
	 * @param key          the configuration variable name
	 * @param defaultValue returns this value is the configuration variable is not
	 *                     set
	 * @return true if set to true (case insensitive), false in any other cases
	 */
	boolean getConfigBoolean(String key);

	/**
	 * @param key the configuration variable name
	 * @return the value for the configuration variable. returns null if the config
	 *         var is not set or not set to an integer or any other exception
	 *         happens.
	 */
	Integer getConfigInteger(String key);

	/**
	 * @param key          the configuration variable name
	 * @param defaultValue returns this value is the configuration variable is not
	 *                     set
	 * @return the value for the configuration variable. In case the config var is
	 *         not set, or not set to a number, or there is an exception,
	 *         defaultValue is returned.
	 */
	int getConfigInteger(String key, int defaultValue);

}

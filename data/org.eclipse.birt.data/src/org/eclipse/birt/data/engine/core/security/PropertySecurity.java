/*******************************************************************************
 * Copyright (c) 2004,2008 Actuate Corporation.
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

package org.eclipse.birt.data.engine.core.security;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Properties;

/**
 * Property security class
 *
 * @since 3.3
 *
 */
public class PropertySecurity {
	/**
	 * Create a HashMap
	 *
	 * @return Return a new HashMap
	 */
	public static HashMap createHashMap() {
		return new HashMap();
	}

	/**
	 * Create a HashTable
	 *
	 * @return Return a HashTable
	 */
	public static Hashtable createHashtable() {
		return new Hashtable();
	}

	/**
	 * Create properties
	 *
	 * @return Return properties
	 */
	public static Properties createProperties() {
		return new Properties();
	}

	/**
	 * Get system property
	 *
	 * @param key
	 * @return Return system property
	 */
	public static String getSystemProperty(final String key) {
		return System.getProperty(key);
	}
}

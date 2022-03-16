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

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Properties;

public class PropertySecurity {
	/**
	 *
	 * @return
	 */
	public static HashMap createHashMap() {
		return AccessController.doPrivileged(new PrivilegedAction<HashMap>() {

			@Override
			public HashMap run() {
				return new HashMap();
			}
		});
	}

	/**
	 *
	 * @return
	 */
	public static Hashtable createHashtable() {
		return AccessController.doPrivileged(new PrivilegedAction<Hashtable>() {

			@Override
			public Hashtable run() {
				return new Hashtable();
			}
		});
	}

	/**
	 *
	 * @return
	 */
	public static Properties createProperties() {
		return AccessController.doPrivileged(new PrivilegedAction<Properties>() {

			@Override
			public Properties run() {
				return new Properties();
			}
		});
	}

	public static String getSystemProperty(final String key) {
		return AccessController.doPrivileged(new PrivilegedAction<String>() {

			@Override
			public String run() {
				return System.getProperty(key);
			}
		});
	}
}

/*******************************************************************************
 * Copyright (c) 2004,2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.util;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Properties;

public class SecurityUtil {

	public static String getSystemProperty(final String name) {
		return AccessController.doPrivileged(new PrivilegedAction<String>() {

			public String run() {
				return System.getProperty(name);
			}
		});
	}

	public static Properties getSystemProperties() {
		return AccessController.doPrivileged(new PrivilegedAction<Properties>() {

			public Properties run() {
				return System.getProperties();
			}
		});
	}

	public static ClassLoader setContextClassLoader(final ClassLoader loader) {
		return AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {

			public ClassLoader run() {
				Thread thread = Thread.currentThread();
				ClassLoader threadLoader = thread.getContextClassLoader();
				thread.setContextClassLoader(loader);
				return threadLoader;
			}
		});

	}
}

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

package org.eclipse.birt.report.engine.util;

import java.util.Properties;

public class SecurityUtil {

	public static String getSystemProperty(final String name) {
		return System.getProperty(name);
	}

	public static Properties getSystemProperties() {
		return System.getProperties();
	}

	public static ClassLoader setContextClassLoader(final ClassLoader loader) {
		Thread thread = Thread.currentThread();
		ClassLoader threadLoader = thread.getContextClassLoader();
		thread.setContextClassLoader(loader);
		return threadLoader;
	}
}

/*
 *****************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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
 *
 ******************************************************************************
 */

package org.eclipse.birt.data.engine.odaconsumer;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.birt.data.engine.core.security.ClassSecurity;

class DataAccessClassLoader extends URLClassLoader {
	// regular expression for matching the interfaces and exception classes in
	// org.eclipse.birt.data.oda package
	private static final Pattern sm_odaInterfacesPattern = Pattern
			.compile("org\\.eclipse\\.birt\\.data\\.oda\\.[a-zA-Z]+");

	// trace logging variables
	private static String sm_className = DataAccessClassLoader.class.getName();
	private static String sm_loggerName = ConnectionManager.sm_packageName;
	private static LogHelper sm_logger = LogHelper.getInstance(sm_loggerName);

	DataAccessClassLoader(URL[] urls) {
		super(urls, null);

		sm_logger.exiting(sm_className, "DataAccessClassLoader", this);
	}

	protected Class findClass(String name) throws ClassNotFoundException {
		String methodName = "findClass";
		sm_logger.entering(sm_className, methodName, name);

		Matcher matcher = sm_odaInterfacesPattern.matcher(name);

		// if the name matches the regular expression, then it's an ODA interface or
		// exception class, so we must delegate to the default app classloader
		if (matcher.matches())
			return ClassSecurity.loadClass(ClassSecurity.getClassLoader(getClass()), name);

		// otherwise, we use the default URLClassLoader mechanism to look for the class
		// from the list of URL's
		Class foundClass = super.findClass(name);

		sm_logger.exiting(sm_className, methodName, foundClass);
		return foundClass;
	}
}

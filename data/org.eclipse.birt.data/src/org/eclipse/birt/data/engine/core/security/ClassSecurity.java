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

/**
 * This class handles a series of privileged operation against class and
 * classloaders.
 *
 * @author Administrator
 *
 */
public class ClassSecurity {
	/**
	 *
	 * @param clazz
	 * @return
	 */
	public static ClassLoader getClassLoader(final Class clazz) {
		assert clazz != null;

		return clazz.getClassLoader();
	}

	/**
	 *
	 * @param loader
	 * @param className
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static Class loadClass(final ClassLoader loader, final String className) throws ClassNotFoundException {

		try {
			return loader.loadClass(className);
		} catch (Exception typedException) {
			if (typedException instanceof ClassNotFoundException) {
				throw (ClassNotFoundException) typedException;
			}
			return null;
		}

	}
}

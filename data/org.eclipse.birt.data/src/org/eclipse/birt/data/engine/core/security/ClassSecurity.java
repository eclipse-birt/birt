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

package org.eclipse.birt.data.engine.core.security;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

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

		return AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {

			public ClassLoader run() {
				return clazz.getClassLoader();
			}
		});
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
			return AccessController.doPrivileged(new PrivilegedExceptionAction<Class>() {

				public Class run() throws ClassNotFoundException {
					return loader.loadClass(className);
				}
			});
		} catch (PrivilegedActionException e) {
			Exception typedException = e.getException();
			if (typedException instanceof ClassNotFoundException) {
				throw (ClassNotFoundException) typedException;
			}
			return null;
		}

	}
}

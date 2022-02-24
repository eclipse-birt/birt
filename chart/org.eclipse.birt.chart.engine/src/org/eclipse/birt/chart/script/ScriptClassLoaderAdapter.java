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

package org.eclipse.birt.chart.script;

import org.eclipse.birt.chart.util.SecurityUtil;

/**
 * An adapter class for IScriptClassLoader. It first try to load class from
 * current context, if fail, try to load by parent loader.
 */
public class ScriptClassLoaderAdapter implements IScriptClassLoader {

	final private ClassLoader defaultLoader;

	public ScriptClassLoaderAdapter(ClassLoader defaultLoader) {
		this.defaultLoader = defaultLoader;
	}

	public ScriptClassLoaderAdapter() {
		this(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.script.IScriptClassLoader#loadClass(java.lang.
	 * String, java.lang.ClassLoader)
	 */
	public Class<?> loadClass(String className, ClassLoader parentLoader) throws ClassNotFoundException {
		try {
			if (defaultLoader == null) {
				return Class.forName(className);
			} else {
				return SecurityUtil.loadClass(defaultLoader, className);
			}
		} catch (ClassNotFoundException ex) {
			if (parentLoader != null) {
				return SecurityUtil.loadClass(parentLoader, className);
			}

			throw ex;
		}
	}
}

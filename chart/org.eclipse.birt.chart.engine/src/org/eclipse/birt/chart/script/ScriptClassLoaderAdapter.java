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

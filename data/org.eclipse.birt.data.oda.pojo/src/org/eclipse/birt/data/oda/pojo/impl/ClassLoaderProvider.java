/*
 *************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *  
 *************************************************************************
 */

package org.eclipse.birt.data.oda.pojo.impl;

import java.net.URL;

import org.eclipse.birt.core.framework.URLClassLoader;
import org.eclipse.birt.data.oda.pojo.Activator;

/**
 * Provider of class loaders for pojo jars
 */
public class ClassLoaderProvider {
	private static ClassLoaderProvider sm_instance = null;

	private URLClassLoader m_classLoaders = null;

	public static ClassLoaderProvider getInstance() {
		if (sm_instance == null) {
			synchronized (ClassLoaderProvider.class) {
				if (sm_instance == null)
					sm_instance = new ClassLoaderProvider();
			}
		}
		return sm_instance;
	}

	/**
	 * Singleton instance release method.
	 */
	public static void releaseInstance() {
		if (sm_instance == null)
			return;

		synchronized (ClassLoaderProvider.class) {
			if (sm_instance != null) {
				sm_instance.reset();
				sm_instance = null;
			}
		}
	}

	private void reset() {
		if (m_classLoaders != null) {
			m_classLoaders.close();
			m_classLoaders = null;
		}
	}

	synchronized URLClassLoader getClassLoader(URL[] urls) {
		if (m_classLoaders == null) {
			m_classLoaders = new URLClassLoader(urls, Activator.class.getClassLoader());
		} else {
			for (int i = 0; i < urls.length; i++) {
				m_classLoaders.addURL(urls[i]);
			}
		}
		return m_classLoaders;
	}
}

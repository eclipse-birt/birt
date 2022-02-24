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

package org.eclipse.birt.core.framework;

import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.IAdapterManager;

/**
 * Defines the Platform interface that allows BIRT to be run in Eclipse and
 * server environments
 * 
 */
public interface IPlatform {
	static final String EXTENSION_POINT_FACTORY_SERVICE = "FactoryService";
	// those constant are copied from the org.eclipse.core.runtime.Platform
	public static final String OS_WIN32 = "win32";//$NON-NLS-1$
	public static final String OS_LINUX = "linux";//$NON-NLS-1$
	public static final String OS_AIX = "aix";//$NON-NLS-1$
	public static final String OS_SOLARIS = "solaris";//$NON-NLS-1$
	public static final String OS_HPUX = "hpux";//$NON-NLS-1$
	public static final String OS_QNX = "qnx";//$NON-NLS-1$
	public static final String OS_MACOSX = "macosx";//$NON-NLS-1$
	public static final String OS_UNKNOWN = "unknown";//$NON-NLS-1$

	/**
	 * @return the global extension registry
	 */
	IExtensionRegistry getExtensionRegistry();

	IAdapterManager getAdapterManager();

	/**
	 * 
	 * @param symblicName
	 * @return
	 */
	IBundle getBundle(String symblicName);

	/**
	 * 
	 * @param bundle
	 * @param path
	 * @return
	 */
	URL find(IBundle bundle, IPlatformPath path);

	/**
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	URL asLocalURL(URL url) throws IOException;

	/**
	 * 
	 * @param name
	 * @return
	 */
	String getDebugOption(String name);

	/**
	 * 
	 * @param pluginName
	 */
	void initializeTracing(String pluginName);

	/**
	 * 
	 * @param factory
	 * @return
	 */
	Object createFactoryObject(String factory);

	Object enterPlatformContext();

	void exitPlatformContext(Object context);

	String getOS();

}

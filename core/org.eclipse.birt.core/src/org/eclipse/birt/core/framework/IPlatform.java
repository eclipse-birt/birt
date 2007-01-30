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

package org.eclipse.birt.core.framework;

import java.io.IOException;
import java.net.URL;

/**
 * Defines the Platform interface that allows BIRT to be run in Eclipse and
 * server environments
 * 
 */
public interface IPlatform
{
	static final String EXTENSION_POINT_FACTORY_SERVICE = "FactoryService";

	/**
	 * @return the global extension registry
	 */
	IExtensionRegistry getExtensionRegistry( );

	/**
	 * 
	 * @param symblicName
	 * @return
	 */
	IBundle getBundle( String symblicName );

	/**
	 * 
	 * @param bundle
	 * @param path
	 * @return
	 */
	URL find( IBundle bundle, IPlatformPath path );

	/**
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	URL asLocalURL( URL url ) throws IOException;

	/**
	 * 
	 * @param name
	 * @return
	 */
	String getDebugOption( String name );

	/**
	 * 
	 * @param pluginName
	 */
	void initializeTracing( String pluginName );

	/**
	 * 
	 * @param factory
	 * @return
	 */
	Object createFactoryObject( String factory );
}

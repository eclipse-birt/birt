/*******************************************************************************
 * Copyright (c) 2010, 2011 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.framework.jar;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.framework.FrameworkException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.core.framework.PlatformConfig;
import org.eclipse.birt.core.framework.PlatformLauncher;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.RegistryFactory;
import org.eclipse.core.runtime.spi.IRegistryProvider;

public class ServiceLauncher extends PlatformLauncher
{

	static final String MANIFEST_ENTRY = "META-INF/MANIFEST.MF";

	static Logger logger = Logger.getLogger( Platform.class.getName( ) );

	private ServicePlatform platform;

	public ServiceLauncher( )
	{
	}

	public void startup( final PlatformConfig config )
			throws FrameworkException
	{
		platform = new ServicePlatform( config );

		try
		{
			Enumeration<URL> plugins = ServiceLauncher.class.getClassLoader( )
					.getResources( MANIFEST_ENTRY );

			while ( plugins.hasMoreElements( ) )
			{
				URL root = new URL( plugins.nextElement( ), ".." );
				try
				{
					platform.installBundle( root );
				}
				catch ( Exception ex )
				{
					logger.log( Level.WARNING, "Failed to install plugin from "
							+ root, ex );
				}
			}
			platform.startup( );

			Platform.setPlatform( platform );

			RegistryFactory
					.setDefaultRegistryProvider( new IRegistryProvider( ) {

						public IExtensionRegistry getRegistry( )
						{
							return platform.extensionRegistry;
						}
					} );
		}
		catch ( IOException ex )
		{
			throw new FrameworkException(
					"Can't find any bundle from the classpath", ex );
		}
		catch ( CoreException ex )
		{
			throw new FrameworkException(
					"Can't register the ExtensionRegistry classpath", ex );
		}

	}

	public void shutdown( )
	{
		Platform.setPlatform( null );
		if ( platform != null )
		{
			platform.shutdown( );
			platform = null;
		}
	}
}

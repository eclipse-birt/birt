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

package org.eclipse.birt.core.internal.plugin;

import java.security.AccessController;
import java.security.PrivilegedAction;

import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.core.framework.eclipse.EclipsePlatform;
import org.eclipse.birt.core.internal.function.impl.FunctionProviderImpl;
import org.eclipse.birt.core.plugin.BIRTPlugin;
import org.eclipse.birt.core.script.ScriptEngineFactoryManager;
import org.eclipse.birt.core.script.functionservice.impl.FunctionProvider;
import org.osgi.framework.BundleContext;

public class CorePlugin extends BIRTPlugin
{

	/**
	 * This method is called upon plug-in activation.
	 */
	public void start( BundleContext context ) throws Exception
	{
		super.start( context );
		Platform.setPlatform( new EclipsePlatform( context ) );
		ClassLoader contextClassLoader = (ClassLoader) AccessController
				.doPrivileged( new PrivilegedAction<Object>( ) {

					public Object run( )
					{
						return Thread.currentThread( ).getContextClassLoader( );
					}
				} );

		Platform.setContextClassLoader( contextClassLoader );
		FunctionProvider.setFunctionProvider( new FunctionProviderImpl( ) );
		ScriptEngineFactoryManager
				.setInstance( new ScriptEngineFactoryManagerImpl( ) );
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop( BundleContext context ) throws Exception
	{
		super.stop( context );
		Platform.setPlatform( null );
		Platform.setContextClassLoader( null );
		FunctionProvider.setFunctionProvider( null );
		ScriptEngineFactoryManager.setInstance( null );
	}
}

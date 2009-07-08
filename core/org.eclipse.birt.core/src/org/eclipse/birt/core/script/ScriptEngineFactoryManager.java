/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.script;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.framework.FrameworkException;
import org.eclipse.birt.core.framework.IConfigurationElement;
import org.eclipse.birt.core.framework.IExtension;
import org.eclipse.birt.core.framework.IExtensionPoint;
import org.eclipse.birt.core.framework.IExtensionRegistry;
import org.eclipse.birt.core.framework.Platform;

public class ScriptEngineFactoryManager
{

	private static Logger logger = Logger
			.getLogger( ScriptEngineFactoryManager.class.getName( ) );

	private Map<String, IScriptEngineFactory> factories;

	private static Map<String, IConfigurationElement> configs;

	static
	{
		configs = new HashMap<String, IConfigurationElement>( );
		IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry( );
		IExtensionPoint extensionPoint = extensionRegistry
				.getExtensionPoint( "org.eclipse.birt.core.ScriptEngineFactory" );

		IExtension[] extensions = extensionPoint.getExtensions( );
		for ( IExtension extension : extensions )
		{
			IConfigurationElement[] configurations = extension
					.getConfigurationElements( );
			for ( IConfigurationElement configuration : configurations )
			{
				String scriptName = configuration.getAttribute( "scriptName" );
				configs.put( scriptName, configuration );
			}
		}
	}

	public ScriptEngineFactoryManager( )
	{
		factories = new HashMap<String, IScriptEngineFactory>( );
	}

	public IScriptEngineFactory getScriptEngineFactory( String scriptLanguage )
	{
		if ( factories.containsKey( scriptLanguage ) )
		{
			return factories.get( scriptLanguage );
		}
		if ( configs.containsKey( scriptLanguage ) )
		{
			IConfigurationElement configuration = configs.get( scriptLanguage );
			try
			{
				Object object = configuration.createExecutableExtension( "factoryClass" );
				if ( object instanceof IScriptEngineFactory )
				{
					IScriptEngineFactory factory = (IScriptEngineFactory)object;
					factories.put( scriptLanguage, factory );
					return factory;
				}
			}
			catch ( FrameworkException e )
			{
				logger.log( Level.SEVERE, e.getLocalizedMessage( ), e );
			}
		}
		return null;
	}
}

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

package org.eclipse.birt.report.designer.core;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * The main plugin class to be used in the desktop.
 * 
 *  
 */

public class CorePlugin extends AbstractUIPlugin
{

	//The shared instance.

	private static final String RESOURCE_BUNDLE_BASE_NAME = "org.eclipse.birt.report.designer.core.CorePluginResources"; //$NON-NLS-1$

	private static CorePlugin plugin;

	//Resource bundle.
	private ResourceBundle resourceBundle;

	/**
	 * The constructor.
	 */

	public CorePlugin( )
	{
		super( );
		plugin = this;
		try
		{
			resourceBundle = ResourceBundle.getBundle( RESOURCE_BUNDLE_BASE_NAME ); //$NON-NLS-1$
		}
		catch ( MissingResourceException x )
		{
			resourceBundle = null;
		}
	}


	/**
	 * Returns the shared instance.
	 */

	public static CorePlugin getDefault( )
	{
		return plugin;
	}

	/**
	 * Returns the string from the plugin's resource bundle, or 'key' if not
	 * found.
	 */

	public static String getResourceString( String key )
	{
		// TODO: Need a better way to avoid deprecated method, now loading
		// plugin.properties file.
		ResourceBundle bundle = CorePlugin.getDefault( )
				.getDescriptor( )
				.getResourceBundle( );
		//		ResourceBundle bundle = CorePlugin.getDefault( ).getResourceBundle(
		// );

		try
		{
			return ( bundle != null ) ? bundle.getString( key ) : key;
		}
		catch ( MissingResourceException e )
		{
			return key;
		}
	}

	/**
	 * Returns the plugin's resource bundle,
	 */

	public ResourceBundle getResourceBundle( )
	{
		return resourceBundle;
	}
}
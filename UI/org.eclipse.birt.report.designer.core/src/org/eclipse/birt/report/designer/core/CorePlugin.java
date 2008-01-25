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

import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 * 
 * 
 */

public class CorePlugin extends AbstractUIPlugin
{

	// The shared instance.

	private static final String RESOURCE_BUNDLE_BASE_NAME = "org.eclipse.birt.report.designer.core.CorePluginResources"; //$NON-NLS-1$

	private static CorePlugin plugin;

	// Resource bundle.
	private ResourceBundle resourceBundle;

	public static String RESOURCE_FOLDER;

	/**
	 * The constructor.
	 */

	public CorePlugin( )
	{
		super( );
		plugin = this;
		try
		{
			resourceBundle = ResourceBundle.getBundle( RESOURCE_BUNDLE_BASE_NAME );
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
		ResourceBundle bundle = Platform.getResourceBundle( getDefault( ).getBundle( ) );

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

	/*
	 * (non-Javadoc)
	 * 
	 * @org.eclipse.ui.plugin#start( BundleContext context )
	 */
	public void start( BundleContext context ) throws Exception
	{
		super.start( context );
	}

}
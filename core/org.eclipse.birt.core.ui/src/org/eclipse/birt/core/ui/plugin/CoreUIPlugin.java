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

package org.eclipse.birt.core.ui.plugin;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.birt.core.ui.utils.UIHelper;
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * Plugin class for Chart UI Extension
 */

public class CoreUIPlugin extends Plugin
{

	/**
	 * Plugin ID.
	 */
	public static final String ID = "org.eclipse.birt.core.ui"; //$NON-NLS-1$

	// The shared instance.
	private static CoreUIPlugin plugin;

	// Resource bundle.
	private ResourceBundle resourceBundle;

	/**
	 * The constructor.
	 */
	public CoreUIPlugin( )
	{
		super( );
		plugin = this;
		try
		{
			resourceBundle = ResourceBundle.getBundle( "org.eclipse.birt.framework.taskwizard.prototype.plugin.FrameworkPluginClassResources" ); //$NON-NLS-1$
		}
		catch ( MissingResourceException x )
		{
			resourceBundle = null;
		}
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start( BundleContext context ) throws Exception
	{
		super.start( context );
		UIHelper.setImageCached( true );
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop( BundleContext context ) throws Exception
	{
		UIHelper.setImageCached( false );
		super.stop( context );
	}

	/**
	 * Returns the shared instance.
	 */
	public static CoreUIPlugin getDefault( )
	{
		return plugin;
	}

	/**
	 * Returns the string from the plugin's resource bundle, or 'key' if not
	 * found.
	 */
	public static String getResourceString( String key )
	{
		ResourceBundle bundle = CoreUIPlugin.getDefault( ).getResourceBundle( );
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

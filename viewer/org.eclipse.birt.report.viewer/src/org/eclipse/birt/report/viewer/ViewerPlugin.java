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

package org.eclipse.birt.report.viewer;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class for embedded viewer web application.
 * <p>
 */
public class ViewerPlugin extends Plugin
{
	public final static String PLUGIN_ID = "org.eclipse.birt.report.viewer"; //$NON-NLS-1$

	/**
	 * The shared instance.
	 */
	private static ViewerPlugin plugin;
	
	/**
	 * Resource bundle.
	 */
	private ResourceBundle resourceBundle;
	
	/**
	 * The constructor.
	 */
	public ViewerPlugin( )
	{
		super( );
		plugin = this;
		
		try
		{
			resourceBundle = ResourceBundle.getBundle( ViewerPlugin.class.getName( ) );
		}
		catch ( MissingResourceException x )
		{
			resourceBundle = null;
		}
	}

	/**
	 * This method is called upon plug-in activation.
	 * 
	 * @param context bundle context
	 * @exception Exception
	 */
	public void start( BundleContext context ) throws Exception
	{
		super.start( context );
	}

	/**
	 * This method is called when the plug-in is stopped.
	 * 
	 * @param context bundle context
	 * @exception Exception
	 */
	public void stop( BundleContext context ) throws Exception
	{
		super.stop( context );
	}

	/**
	 * Returns the shared instance.
	 * 
	 * @return ViewerPlugin
	 */
	public static ViewerPlugin getDefault( )
	{
		return plugin;
	}

	/**
	 * Returns the string from the plugin's resource bundle,
	 * or 'key' if not found.
	 * 
	 * @param key resource key
	 * @return resource string
	 */
	public static String getResourceString( String key )
	{
		ResourceBundle bundle = ViewerPlugin.getDefault( ).getResourceBundle( );
		
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
	 * Get formatted string.
	 * 
	 * @param key
	 * @param arguments
	 * @return formatte resource string
	 */
	public static String getFormattedResourceString( String key, Object[] arguments )
	{
		return MessageFormat.format( getResourceString( key ), arguments );
	}

	/**
	 * Logs an Error message with an exception. Note that the message should
	 * already be localized to proper locale. ie: Resources.getString() should
	 * already have been called
	 */
	public static synchronized void logError( String message, Throwable ex )
	{
		if ( message == null )
		{
			message = ""; //$NON-NLS-1$
		}
		
		Status errorStatus = new Status( IStatus.ERROR, PLUGIN_ID, IStatus.OK, message, ex );
		ViewerPlugin.getDefault( ).getLog( ).log( errorStatus );
	}

	/**
	 * Returns the plugin's resource bundle,
	 * 
	 * @return resource boundle
	 */
	public ResourceBundle getResourceBundle( )
	{
		return resourceBundle;
	}
}

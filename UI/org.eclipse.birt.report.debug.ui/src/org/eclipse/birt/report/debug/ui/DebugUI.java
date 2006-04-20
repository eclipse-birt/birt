/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.debug.ui;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class DebugUI extends AbstractUIPlugin
{

	private static DebugUI plugin;
	private ResourceBundle resourceBundle;

	/**
	 *  
	 */
	public DebugUI( )
	{
		plugin = this;
		try
		{
			resourceBundle = ResourceBundle
					.getBundle( "org.eclipse.bird.report.debug.ui.DebugUIResources" ); //$NON-NLS-1$
		}
		catch ( MissingResourceException _ex )
		{
			resourceBundle = null;
		}
	}

	/**
	 * @param descriptor
	 */
	public DebugUI( IPluginDescriptor descriptor )
	{
		plugin = this;
		try
		{
			resourceBundle = ResourceBundle
					.getBundle( "org.eclipse.bird.report.debug.ui.DebugUIResources" ); //$NON-NLS-1$
		}
		catch ( MissingResourceException _ex )
		{
			resourceBundle = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start( BundleContext context ) throws Exception
	{
		super.start( context );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop( BundleContext context ) throws Exception
	{
		super.stop( context );
	}

	/**
	 * @return
	 */
	public static DebugUI getDefault( )
	{
		return plugin;
	}

	/**
	 * @param key
	 * @return
	 */
	public static String getResourceString( String key )
	{
		ResourceBundle bundle = getDefault( ).getResourceBundle( );
		try
		{
			return bundle == null ? key : bundle.getString( key );
		}
		catch ( MissingResourceException _ex )
		{
			return key;
		}
	}

	/**
	 * @return
	 */
	public ResourceBundle getResourceBundle( )
	{
		return resourceBundle;
	}
}
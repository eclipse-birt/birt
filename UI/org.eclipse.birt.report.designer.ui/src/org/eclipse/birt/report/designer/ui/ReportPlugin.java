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

package org.eclipse.birt.report.designer.ui;

import java.net.URL;

import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 *  
 */
public class ReportPlugin extends AbstractUIPlugin
{

	/**
	 * The Report UI plugin ID.
	 */
	public static final String REPORT_UI = "org.eclipse.birt.report.designer.ui"; //$NON-NLS-1$

	/**
	 * The shared instance.
	 */
	private static ReportPlugin plugin;

	/**
	 * The constructor.
	 */
	public ReportPlugin( )
	{
		super( );

		plugin = this;
	}

	/**
	 * Called upon plug-in activation
	 * 
	 * @param context
	 *            the context
	 */
	public void start( BundleContext context ) throws Exception
	{
		super.start( context );
		//set preference default value
		getPreferenceStore( ).setDefault( IPreferenceConstants.PALETTE_DOCK_LOCATION,
				IPreferenceConstants.DEFAULT_PALETTE_SIZE );

		getPreferenceStore( ).setDefault( IPreferenceConstants.PALETTE_STATE,
				IPreferenceConstants.DEFAULT_PALETTE_STATE );

	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop( BundleContext context ) throws Exception
	{
		super.stop( context );
	}

	/**
	 * Returns the shared instance.
	 */
	public static ReportPlugin getDefault( )
	{

		return plugin;
	}

	/**
	 * Relative to UI plugin directory, example: "icons/usertableicon.gif".
	 * 
	 * @param key
	 * @return an Image, do not dispose
	 */
	public static Image getImage( String key )
	{
		ImageRegistry imageRegistry = ReportPlugin.getDefault( )
				.getImageRegistry( );

		Image image = imageRegistry.get( key );

		if ( image == null )
		{
			URL url = ReportPlugin.getDefault( ).find( new Path( key ) );

			if ( null != url )
			{
				image = ImageDescriptor.createFromURL( url ).createImage( );
			}

			if ( image == null )
			{
				image = ImageDescriptor.getMissingImageDescriptor( )
						.createImage( );
			}

			imageRegistry.put( key, image );
		}

		return image;
	}

}
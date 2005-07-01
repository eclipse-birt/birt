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
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
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
	 * The cursor for selecting cells
	 */
	private Cursor cellLeftCursor, cellRightCursor;

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

		initCellCursor( );

	}

	/**
	 * Returns the version info for this plugin.
	 * 
	 * @return Version string.
	 */
	public static String getVersion( )
	{
		return (String) getDefault( ).getBundle( )
				.getHeaders( )
				.get( org.osgi.framework.Constants.BUNDLE_VERSION );
	}

	public static String getBuildInfo( )
	{
		return getResourceString( "Build" ); //$NON-NLS-1$
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
	 * Initialize the cell Cursor instance
	 */
	private void initCellCursor( )
	{
		ImageData source = ReportPlugin.getImageDescriptor( "icons/full/point/cellcursor.bmp" ) //$NON-NLS-1$
				.getImageData( );
		ImageData mask = ReportPlugin.getImageDescriptor( "icons/full/point/cellcursormask.bmp" ) //$NON-NLS-1$
				.getImageData( );
		cellLeftCursor = new Cursor( null, source, mask, 16, 16 );

		source = ReportPlugin.getImageDescriptor( "icons/full/point/cellrightcursor.bmp" ) //$NON-NLS-1$
				.getImageData( );
		mask = ReportPlugin.getImageDescriptor( "icons/full/point/cellrightcursormask.bmp" ) //$NON-NLS-1$
				.getImageData( );
		cellRightCursor = new Cursor( null, source, mask, 16, 16 );
	}

	/**
	 * 
	 * @return the cursor used to select cells in the table
	 */
	public Cursor getLeftCellCursor( )
	{
		return cellLeftCursor;
	}

	/**
	 * 
	 * @return the cursor used to select cells in the table
	 */
	public Cursor getRightCellCursor( )
	{
		return cellRightCursor;
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop( BundleContext context ) throws Exception
	{
		super.stop( context );
		cellLeftCursor.dispose( );
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
	 * @return an Image descriptor, this is useful to preserve the original
	 *         color depth for instance.
	 */
	public static ImageDescriptor getImageDescriptor( String key )
	{
		ImageRegistry imageRegistry = ReportPlugin.getDefault( )
				.getImageRegistry( );

		ImageDescriptor imageDescriptor = imageRegistry.getDescriptor( key );

		if ( imageDescriptor == null )
		{
			URL url = ReportPlugin.getDefault( ).find( new Path( key ) );

			if ( null != url )
			{
				imageDescriptor = ImageDescriptor.createFromURL( url );
			}

			if ( imageDescriptor == null )
			{
				imageDescriptor = ImageDescriptor.getMissingImageDescriptor( );
			}

			imageRegistry.put( key, imageDescriptor );
		}

		return imageDescriptor;
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

	/**
	 * @return the cheat sheet property preference, stored in the workbench root
	 */
	public static boolean readCheatSheetPreference( )
	{
		IWorkspace workspace = ResourcesPlugin.getWorkspace( );
		try
		{
			String property = workspace.getRoot( )
					.getPersistentProperty( new QualifiedName( "org.eclipse.birt.property", //$NON-NLS-1$
							"showCheatSheet" ) ); //$NON-NLS-1$
			if ( property != null )
				return Boolean.valueOf( property ).booleanValue( );
		}
		catch ( CoreException e )
		{
			e.printStackTrace( );
		}
		return true;
	}

	/**
	 * Set the show cheatsheet preference in workspace root. Used by wizards
	 */
	public static void writeCheatSheetPreference( boolean value )
	{
		IWorkspace workspace = ResourcesPlugin.getWorkspace( );
		try
		{
			workspace.getRoot( )
					.setPersistentProperty( new QualifiedName( "org.eclipse.birt.property", //$NON-NLS-1$
							"showCheatSheet" ), //$NON-NLS-1$
							String.valueOf( value ) );
		}
		catch ( CoreException e )
		{
			e.printStackTrace( );
		}
	}

}
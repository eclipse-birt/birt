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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.preferences.TemplatePreferencePage;
import org.eclipse.birt.report.designer.ui.views.attributes.AttributeView;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.MetaDataConstants;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.gef.ui.views.palette.PaletteView;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 * 
 */
public class ReportPlugin extends AbstractUIPlugin
{
	
	//Add the static String list, remeber thr ignore view for the selection
	private List ignore = new ArrayList();
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

	// The entry delimiter
	public static final String PREFERENCE_DELIMITER = ";"; //$NON-NLS-1$
	public static final String SPACE = " "; //$NON-NLS-1$

	public static final String DEFAULT_NAME_PREFERENCE = "designer.preview.preference.elementname.defaultname.preferencestore"; //$NON-NLS-1$
	public static final String CUSTOM_NAME_PREFERENCE = "designer.preview.preference.elementname.customname.preferencestore"; //$NON-NLS-1$
	public static final String DESCRIPTION_PREFERENCE = "designer.preview.preference.elementname.description.preferencestore"; //$NON-NLS-1$
	public static final String LIBRARY_PREFERENCE = "designer.library.preference.libraries.description.preferencestore"; //$NON-NLS-1$
	public static final String TEMPLATE_PREFERENCE = "designer.preview.preference.template.description.preferencestore"; //$NON-NLS-1$

	private int nameCount = 0;

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
		// set preference default value
		getPreferenceStore( ).setDefault( IPreferenceConstants.PALETTE_DOCK_LOCATION,
				IPreferenceConstants.DEFAULT_PALETTE_SIZE );

		getPreferenceStore( ).setDefault( IPreferenceConstants.PALETTE_STATE,
				IPreferenceConstants.DEFAULT_PALETTE_STATE );

		initCellCursor( );

		// set default Element names
		setDefaultElementNamePreference( getPreferenceStore( ) );

		// set default library
		setDefaultLibraryPreference( );

		// set default Template
		setDefaultTemplatePreference( );

		// Biding default short cut services
		// Using 3.0 compatible api
		PlatformUI.getWorkbench( )
				.getContextSupport( )
				.setKeyFilterEnabled( true );
		
		addIgnoreViewID("org.eclipse.birt.report.designer.ui.editors.ReportEditor");
		addIgnoreViewID("org.eclipse.birt.report.designer.ui.editors.TemplateEditor");
		addIgnoreViewID(IPageLayout.ID_OUTLINE);
		addIgnoreViewID(AttributeView.ID);
		addIgnoreViewID(PaletteView.ID);
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
		ImageData source = ReportPlugin.getImageDescriptor( "icons/point/cellcursor.bmp" ) //$NON-NLS-1$
				.getImageData( );
		ImageData mask = ReportPlugin.getImageDescriptor( "icons/point/cellcursormask.bmp" ) //$NON-NLS-1$
				.getImageData( );
		cellLeftCursor = new Cursor( null, source, mask, 16, 16 );

		source = ReportPlugin.getImageDescriptor( "icons/point/cellrightcursor.bmp" ) //$NON-NLS-1$
				.getImageData( );
		mask = ReportPlugin.getImageDescriptor( "icons/point/cellrightcursormask.bmp" ) //$NON-NLS-1$
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
			if(url==null)
			{
				try
				{
					url = new URL("file:///" + key);
				}
				catch ( MalformedURLException e )
				{
				}
			}

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

	/**
	 * Returns the workspace instance.
	 */
	public static IWorkspace getWorkspace( )
	{
		return ResourcesPlugin.getWorkspace( );
	}

	/**
	 * Set default element names for preference
	 * 
	 * @param store
	 *            The preference for store
	 */
	private void setDefaultElementNamePreference( IPreferenceStore store )
	{
		List tmpList;
		tmpList = DesignEngine.getMetaDataDictionary( ).getElements( );
		int i;
		StringBuffer bufferDefaultName = new StringBuffer( );
		StringBuffer bufferCustomName = new StringBuffer( );
		StringBuffer bufferPreference = new StringBuffer( );

		int nameOption;
		IElementDefn elementDefn;
		for ( i = 0; i < tmpList.size( ); i++ )
		{
			elementDefn = (IElementDefn) ( tmpList.get( i ) );
			nameOption = elementDefn.getNameOption( );

			// only set names for the elements when the element can have a name
			if ( nameOption == MetaDataConstants.NO_NAME )
			{
				continue;
			}
			nameCount++;
			bufferDefaultName.append( elementDefn.getName( ) );
			bufferDefaultName.append( PREFERENCE_DELIMITER );

			bufferCustomName.append( "" ); //$NON-NLS-1$
			bufferCustomName.append( PREFERENCE_DELIMITER );

			appendDefaultPreference( elementDefn.getName( ), bufferPreference );
		}
		store.setDefault( DEFAULT_NAME_PREFERENCE, bufferDefaultName.toString( ) );
		store.setDefault( CUSTOM_NAME_PREFERENCE, bufferCustomName.toString( ) );
		store.setDefault( DESCRIPTION_PREFERENCE, bufferPreference.toString( ) );
	}

	/**
	 * Append default description to the Stringbuffer according to each
	 * defaultName
	 * 
	 * @param defaultName
	 *            The default Name preference The Stringbuffer which string
	 *            added to
	 */
	private void appendDefaultPreference( String defaultName,
			StringBuffer preference )
	{
		if ( defaultName.equals( ReportDesignConstants.DATA_ITEM ) ) //$NON-NLS-1$
		{
			preference.append( Messages.getString( "DesignerPaletteFactory.toolTip.dataReportItem" ) ); //$NON-NLS-1$
		}
		else if ( defaultName.equals( ReportDesignConstants.GRID_ITEM ) ) //$NON-NLS-1$
		{
			preference.append( Messages.getString( "DesignerPaletteFactory.toolTip.gridReportItem" ) ); //$NON-NLS-1$
		}
		else if ( defaultName.equals( ReportDesignConstants.IMAGE_ITEM ) ) //$NON-NLS-1$
		{
			preference.append( Messages.getString( "DesignerPaletteFactory.toolTip.imageReportItem" ) ); //$NON-NLS-1$
		}
		else if ( defaultName.equals( ReportDesignConstants.LABEL_ITEM ) ) //$NON-NLS-1$
		{
			preference.append( Messages.getString( "DesignerPaletteFactory.toolTip.labelReportItem" ) ); //$NON-NLS-1$
		}
		else if ( defaultName.equals( ReportDesignConstants.LIST_ITEM ) ) //$NON-NLS-1$
		{
			preference.append( Messages.getString( "DesignerPaletteFactory.toolTip.listReportItem" ) ); //$NON-NLS-1$
		}
		else if ( defaultName.equals( ReportDesignConstants.TABLE_ITEM ) ) //$NON-NLS-1$
		{
			preference.append( Messages.getString( "DesignerPaletteFactory.toolTip.tableReportItem" ) ); //$NON-NLS-1$
		}
		else if ( defaultName.equals( ReportDesignConstants.TEXT_ITEM ) ) //$NON-NLS-1$
		{
			preference.append( Messages.getString( "DesignerPaletteFactory.toolTip.textReportItem" ) ); //$NON-NLS-1$
		}
		else if ( defaultName.equalsIgnoreCase( "Chart" ) ) //$NON-NLS-1$
		{
			preference.append( "Insert chart" ); //$NON-NLS-1$
		}
		else
		{
			preference.append( "" ); //$NON-NLS-1$
		}

		preference.append( PREFERENCE_DELIMITER );

	}

	/**
	 * Get default element name preference
	 * 
	 * @return String[] the array of Strings of default element name preference
	 */
	public String[] getDefaultDefaultNamePreference( )
	{
		return convert( getPreferenceStore( ).getDefaultString( DEFAULT_NAME_PREFERENCE ) );
	}

	/**
	 * Get default custom name preference
	 * 
	 * @return String[] the array of Strings of custom element name preference
	 */
	public String[] getDefaultCustomNamePreference( )
	{
		return convert( getPreferenceStore( ).getDefaultString( CUSTOM_NAME_PREFERENCE ) );
	}

	/**
	 * Get default description preference
	 * 
	 * @return String[] the array of Strings of default description preference
	 */
	public String[] getDefaultDescriptionPreference( )
	{
		return convert( getPreferenceStore( ).getDefaultString( DESCRIPTION_PREFERENCE ) );
	}

	/**
	 * Get element name preference
	 * 
	 * @return String[] the array of Strings of element name preference
	 */
	public String[] getDefaultNamePreference( )
	{
		return convert( getPreferenceStore( ).getString( DEFAULT_NAME_PREFERENCE ) );
	}

	/**
	 * Get custom element preference
	 * 
	 * @return String[] the array of Strings of custom name preference
	 */
	public String[] getCustomNamePreference( )
	{
		return convert( getPreferenceStore( ).getString( CUSTOM_NAME_PREFERENCE ) );
	}

	/**
	 * Get description preference
	 * 
	 * @return String[] the array of Strings of description preference
	 */
	public String[] getDescriptionPreference( )
	{
		return convert( getPreferenceStore( ).getString( DESCRIPTION_PREFERENCE ) );
	}

	/**
	 * Get the custom name preference of specified element name
	 * 
	 * @param defaultName
	 *            The specified element name
	 * @return String The custom name gotten
	 */
	public String getCustomName( Object defaultName )
	{
		int i;
		String[] defaultNameArray = getDefaultNamePreference( );
		String[] customNameArray = getCustomNamePreference( );

		// if the length of elememnts is not equal,it means error
		if ( defaultNameArray.length != customNameArray.length )
		{
			return null;
		}
		for ( i = 0; i < defaultNameArray.length; i++ )
		{
			if ( defaultNameArray[i].trim( ).equals( defaultName ) )
			{
				if ( customNameArray[i].equals( "" ) ) //$NON-NLS-1$
				{
					return null;
				}
				return new String( customNameArray[i] );
			}
		}
		return null;
	}

	/**
	 * Convert the single string of preference into string array
	 * 
	 * @param preferenceValue
	 *            The specified element name
	 * @return String[] The array of strings
	 */
	private String[] convert( String preferenceValue )
	{

		String preferenceValueCopy = new String( );
		preferenceValueCopy = new String( PREFERENCE_DELIMITER )
				+ preferenceValue;
		String replaceString = new String( PREFERENCE_DELIMITER )
				+ new String( PREFERENCE_DELIMITER );
		String regrex = new String( PREFERENCE_DELIMITER )
				+ SPACE + new String( PREFERENCE_DELIMITER );
		while ( preferenceValueCopy.indexOf( replaceString ) != -1 )
		{
			preferenceValueCopy = preferenceValueCopy.replaceFirst( replaceString,
					regrex );
		}

		StringTokenizer tokenizer = new StringTokenizer( preferenceValueCopy,
				PREFERENCE_DELIMITER );
		int tokenCount = tokenizer.countTokens( );
		String[] elements = new String[tokenCount];

		int i;
		for ( i = 0; i < tokenCount; i++ )
		{
			elements[i] = tokenizer.nextToken( ).trim( );
		}
		return elements;

	}

	/**
	 * Convert Sting[] to String
	 * 
	 * @param elements []
	 *            elements - the Strings to be converted to the preference value
	 */
	public String convertStrArray2Str( String[] elements )
	{
		StringBuffer buffer = new StringBuffer( );
		for ( int i = 0; i < elements.length; i++ )
		{
			buffer.append( elements[i] );
			buffer.append( PREFERENCE_DELIMITER );
		}
		return buffer.toString( );
	}

	/**
	 * Set element names from string[]
	 * 
	 * @param elements
	 *            the array of element names
	 */
	public void setDefaultNamePreference( String[] elements )
	{
		getPreferenceStore( ).setValue( DEFAULT_NAME_PREFERENCE,
				convertStrArray2Str( elements ) );
	}

	/**
	 * Set element names from string
	 * 
	 * @param element
	 *            the string of element names
	 */
	public void setDefaultNamePreference( String element )
	{
		getPreferenceStore( ).setValue( DEFAULT_NAME_PREFERENCE, element );
	}

	/**
	 * Set default names for the element names from String[]
	 * 
	 * @param elements
	 *            the array of default names
	 */
	public void setCustomNamePreference( String[] elements )
	{
		getPreferenceStore( ).setValue( CUSTOM_NAME_PREFERENCE,
				convertStrArray2Str( elements ) );
	}

	/**
	 * Set default names for the element names from String
	 * 
	 * @param element
	 *            the string of default names
	 */
	public void setCustomNamePreference( String element )
	{
		getPreferenceStore( ).setValue( CUSTOM_NAME_PREFERENCE, element );
	}

	/**
	 * Set descriptions for the element names from String[]
	 * 
	 * @param elements
	 *            the array of descriptions
	 */
	public void setDescriptionPreference( String[] elements )
	{
		getPreferenceStore( ).setValue( DESCRIPTION_PREFERENCE,
				convertStrArray2Str( elements ) );
	}

	/**
	 * Set descriptions for the element names from String
	 * 
	 * @param element
	 *            the string of descriptions
	 */
	public void setDescriptionPreference( String element )
	{
		getPreferenceStore( ).setValue( DESCRIPTION_PREFERENCE, element );
	}

	/**
	 * Get the count of the element names
	 * 
	 */
	public int getCount( )
	{
		return nameCount;
	}

	/**
	 * Set the bad words preference
	 * 
	 * @param elements []
	 *            elements - the Strings to be converted to the preference value
	 */
	public void setLibraryPreference( String[] elements )
	{
		StringBuffer buffer = new StringBuffer( );
		for ( int i = 0; i < elements.length; i++ )
		{
			buffer.append( elements[i] );
			buffer.append( PREFERENCE_DELIMITER );
		}
		getPreferenceStore( ).setValue( LIBRARY_PREFERENCE, buffer.toString( ) );
	}

	/**
	 * Return the library preference as an array of Strings.
	 * 
	 * @return String[] The array of strings of library preference
	 */
	public String[] getLibraryPreference( )
	{
		return convert( getPreferenceStore( ).getString( LIBRARY_PREFERENCE ) );
	}

	/**
	 * Set default library preference
	 */
	public void setDefaultLibraryPreference( )
	{
		getPreferenceStore( ).setDefault( LIBRARY_PREFERENCE, "" ); //$NON-NLS-1$
	}

	/**
	 * Return default library preference as an array of Strings.
	 * 
	 * @return String[] The array of strings of default library preference
	 */
	public String[] getDefaultLibraryPreference( )
	{
		return convert( getPreferenceStore( ).getDefaultString( LIBRARY_PREFERENCE ) );
	}

	/**
	 * Return default template preference
	 * 
	 * @return String The String of default template preference
	 */
	public String getDefaultTemplatePreference( )
	{
		return getPreferenceStore( ).getDefaultString( TEMPLATE_PREFERENCE );

	}

	/**
	 * set default template preference
	 * 
	 */
	public void setDefaultTemplatePreference( )
	{
		String defaultDir = new String( UIUtil.getHomeDirectory( ) );
		defaultDir = defaultDir.replace( '\\', '/' ); //$NON-NLS-1$ //$NON-NLS-2$
		if ( !defaultDir.endsWith( "/" ) ) //$NON-NLS-1$
		{
			defaultDir = defaultDir + "/"; //$NON-NLS-1$
		}
		defaultDir = defaultDir + TemplatePreferencePage.DIRCTORY;
		if ( !defaultDir.endsWith( "/" ) ) //$NON-NLS-1$
		{
			defaultDir = defaultDir + "/"; //$NON-NLS-1$
		}

		getPreferenceStore( ).setDefault( TEMPLATE_PREFERENCE, defaultDir ); //$NON-NLS-1$
	}

	/**
	 * Return default template preference
	 * 
	 * @return String The string of default template preference
	 */
	public String getTemplatePreference( )
	{
		return getPreferenceStore( ).getString( TEMPLATE_PREFERENCE );
	}

	/**
	 * set default template preference
	 * 
	 */
	public void setTemplatePreference( String preference )
	{
		getPreferenceStore( ).setValue( TEMPLATE_PREFERENCE, preference ); //$NON-NLS-1$
	}

	/**
	 * @param str
	 */
	public void addIgnoreViewID(String str)
	{
		ignore.add(str);
	}
	
	/**
	 * @param str
	 */
	public void removeIgnoreViewID(String str)
	{
		ignore.remove(str);
	}
	/**
	 * @param str
	 * @return
	 */
	public boolean containIgnoreViewID(String str)
	{
		return ignore.contains(str);
	}
}
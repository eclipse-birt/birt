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
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.preference.IPreferences;
import org.eclipse.birt.report.designer.core.CorePlugin;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.dnd.DNDService;
import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceFilter;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.preferences.PreferenceFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.MetaDataConstants;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.core.runtime.content.IContentTypeManager;
import org.eclipse.gef.ui.views.palette.PaletteView;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.ibm.icu.util.StringTokenizer;

/**
 * The main plugin class to be used in the desktop.
 * 
 */
public class ReportPlugin extends AbstractUIPlugin
{

	protected static Logger logger = Logger.getLogger( ReportPlugin.class.getName( ) );

	// Add the static String list, remeber thr ignore view for the selection
	private List ignore = new ArrayList( );
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
	public static final String LIBRARY_WARNING_PREFERENCE = "designer.library.preference.libraries.warning.preferencestore"; //$NON-NLS-1$
	public static final String TEMPLATE_PREFERENCE = "designer.preview.preference.template.description.preferencestore"; //$NON-NLS-1$
	public static final String RESOURCE_PREFERENCE = "org.eclipse.birt.report.designer.ui.preferences.resourcestore"; //$NON-NLS-1$
	public static final String COMMENT_PREFERENCE = "org.eclipse.birt.report.designer.ui.preference.comment.description.preferencestore"; //$NON-NLS-1$
	public static final String ENABLE_COMMENT_PREFERENCE = "org.eclipse.birt.report.designer.ui.preference.enable.comment.description.preferencestore"; //$NON-NLS-1$
	public static final String BIRT_RESOURCE = "resources"; //$NON-NLS-1$

	private int nameCount = 0;

	private static final List elementToFilte = Arrays.asList( new String[]{
			ReportDesignConstants.AUTOTEXT_ITEM,
			ReportDesignConstants.DATA_SET_ELEMENT,
			ReportDesignConstants.DATA_SOURCE_ELEMENT,
			ReportDesignConstants.EXTENDED_ITEM,
			ReportDesignConstants.FREE_FORM_ITEM,
			ReportDesignConstants.GRAPHIC_MASTER_PAGE_ELEMENT,
			ReportDesignConstants.JOINT_DATA_SET,
			ReportDesignConstants.LINE_ITEM,
			ReportDesignConstants.MASTER_PAGE_ELEMENT,
			ReportDesignConstants.ODA_DATA_SET,
			ReportDesignConstants.ODA_DATA_SOURCE,
			"Parameter", //$NON-NLS-1$
			ReportDesignConstants.RECTANGLE_ITEM,
			ReportDesignConstants.REPORT_ITEM,
			ReportDesignConstants.SCRIPT_DATA_SET,
			ReportDesignConstants.SCRIPT_DATA_SOURCE,
			ReportDesignConstants.SIMPLE_DATA_SET_ELEMENT,
			ReportDesignConstants.TEMPLATE_DATA_SET,
			ReportDesignConstants.TEMPLATE_ELEMENT,
			ReportDesignConstants.TEMPLATE_PARAMETER_DEFINITION,

			// fix bug 192781
			ReportDesignConstants.ODA_HIERARCHY_ELEMENT,
			ReportDesignConstants.TABULAR_HIERARCHY_ELEMENT,
			ReportDesignConstants.DIMENSION_ELEMENT,
			ReportDesignConstants.ODA_CUBE_ELEMENT,
			ReportDesignConstants.TABULAR_LEVEL_ELEMENT,
			ReportDesignConstants.HIERARCHY_ELEMENT,
			ReportDesignConstants.TABULAR_MEASURE_GROUP_ELEMENT,
			ReportDesignConstants.ODA_DIMENSION_ELEMENT,
			ReportDesignConstants.MEASURE_GROUP_ELEMENT,
			ReportDesignConstants.MEASURE_ELEMENT,
			ReportDesignConstants.TABULAR_CUBE_ELEMENT,
			ReportDesignConstants.CUBE_ELEMENT,
			ReportDesignConstants.ODA_MEASURE_ELEMENT,
			ReportDesignConstants.ODA_LEVEL_ELEMENT,
			ReportDesignConstants.ODA_MEASURE_GROUP_ELEMENT,
			ReportDesignConstants.TABULAR_MEASURE_ELEMENT,
			ReportDesignConstants.LEVEL_ELEMENT,
			ReportDesignConstants.TABULAR_DIMENSION_ELEMENT,
	} );

	private List reportExtensionNames;

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
		PreferenceFactory.getInstance( )
				.getPreferences( this )
				.setDefault( IPreferenceConstants.PALETTE_DOCK_LOCATION,
						IPreferenceConstants.DEFAULT_PALETTE_SIZE );

		PreferenceFactory.getInstance( )
				.getPreferences( this )
				.setDefault( IPreferenceConstants.PALETTE_STATE,
						IPreferenceConstants.DEFAULT_PALETTE_STATE );

		initCellCursor( );

		// set default Element names
		setDefaultElementNamePreference( PreferenceFactory.getInstance( )
				.getPreferences( this ) );

		// set default library
		setDefaultLibraryPreference( );

		// set default Template
		setDefaultTemplatePreference( );

		// set default Resource
		setDefaultResourcePreference( );

		// set default Preference
		setDefaultCommentPreference( );

		// set default enable comment preference
		setDefaultEnableCommentPreference( );

		// Biding default short cut services
		// Using 3.0 compatible api
		PlatformUI.getWorkbench( )
				.getContextSupport( )
				.setKeyFilterEnabled( true );

		addIgnoreViewID( "org.eclipse.birt.report.designer.ui.editors.ReportEditor" ); //$NON-NLS-1$
		addIgnoreViewID( "org.eclipse.birt.report.designer.ui.editors.TemplateEditor" ); //$NON-NLS-1$
		addIgnoreViewID( IPageLayout.ID_OUTLINE );
		// addIgnoreViewID( AttributeView.ID );
		addIgnoreViewID( PaletteView.ID );
		// addIgnoreViewID( DataView.ID );

		// set resource folder in DesignerConstants for use in Core plugin
		CorePlugin.RESOURCE_FOLDER = getResourcePreference( );

		SessionHandleAdapter.getInstance( )
				.getSessionHandle( )
				.setBirtResourcePath( getResourcePreference( ) );

		SessionHandleAdapter.getInstance( )
				.getSessionHandle( )
				.setResourceFolder( getResourcePreference( ) );

		Platform.getExtensionRegistry( )
				.addRegistryChangeListener( DNDService.getInstance( ) );

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

	/**
	 * Returns the infomation about the Build
	 * 
	 */
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
		Platform.getExtensionRegistry( )
				.removeRegistryChangeListener( DNDService.getInstance( ) );
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
			if ( url == null )
			{
				try
				{
					url = new URL( "file:///" + key ); //$NON-NLS-1$
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
			logger.log( Level.SEVERE, e.getMessage( ), e );
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
			logger.log( Level.SEVERE, e.getMessage( ), e );
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
	private void setDefaultElementNamePreference( IPreferences store )
	{
		List tmpList = DEUtil.getMetaDataDictionary( ).getElements( );
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
			if ( nameOption == MetaDataConstants.NO_NAME
					|| filteName( elementDefn ) )
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

		initFilterMap( store, ResourceFilter.generateCVSFilter( ) );
		initFilterMap( store, ResourceFilter.generateDotResourceFilter( ) );
		initFilterMap( store, ResourceFilter.generateEmptyFolderFilter( ) );
		// initFilterMap( store,
		// ResourceFilter.generateNoResourceInFolderFilter( ) );
	}

	private boolean filteName( IElementDefn elementDefn )
	{
		return elementToFilte.indexOf( elementDefn.getName( ) ) != -1;
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
		if ( defaultName.equals( ReportDesignConstants.DATA_ITEM ) )
		{
			preference.append( Messages.getString( "DesignerPaletteFactory.toolTip.dataReportItem" ) ); //$NON-NLS-1$
		}
		else if ( defaultName.equals( ReportDesignConstants.GRID_ITEM ) )
		{
			preference.append( Messages.getString( "DesignerPaletteFactory.toolTip.gridReportItem" ) ); //$NON-NLS-1$
		}
		else if ( defaultName.equals( ReportDesignConstants.IMAGE_ITEM ) )
		{
			preference.append( Messages.getString( "DesignerPaletteFactory.toolTip.imageReportItem" ) ); //$NON-NLS-1$
		}
		else if ( defaultName.equals( ReportDesignConstants.LABEL_ITEM ) )
		{
			preference.append( Messages.getString( "DesignerPaletteFactory.toolTip.labelReportItem" ) ); //$NON-NLS-1$
		}
		else if ( defaultName.equals( ReportDesignConstants.LIST_ITEM ) )
		{
			preference.append( Messages.getString( "DesignerPaletteFactory.toolTip.listReportItem" ) ); //$NON-NLS-1$
		}
		else if ( defaultName.equals( ReportDesignConstants.TABLE_ITEM ) )
		{
			preference.append( Messages.getString( "DesignerPaletteFactory.toolTip.tableReportItem" ) ); //$NON-NLS-1$
		}
		else if ( defaultName.equals( ReportDesignConstants.TEXT_ITEM ) )
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
		return convert( PreferenceFactory.getInstance( )
				.getPreferences( this )
				.getDefaultString( DEFAULT_NAME_PREFERENCE ) );
	}

	/**
	 * Get default custom name preference
	 * 
	 * @return String[] the array of Strings of custom element name preference
	 */
	public String[] getDefaultCustomNamePreference( )
	{
		return convert( PreferenceFactory.getInstance( )
				.getPreferences( this )
				.getDefaultString( CUSTOM_NAME_PREFERENCE ) );
	}

	/**
	 * Get default description preference
	 * 
	 * @return String[] the array of Strings of default description preference
	 */
	public String[] getDefaultDescriptionPreference( )
	{
		return convert( PreferenceFactory.getInstance( )
				.getPreferences( this )
				.getDefaultString( DESCRIPTION_PREFERENCE ) );
	}

	/**
	 * Get element name preference
	 * 
	 * @return String[] the array of Strings of element name preference
	 */
	public String[] getDefaultNamePreference( )
	{
		return convert( PreferenceFactory.getInstance( )
				.getPreferences( this, UIUtil.getCurrentProject( ) )
				.getString( DEFAULT_NAME_PREFERENCE ) );
	}

	/**
	 * Get custom element preference
	 * 
	 * @return String[] the array of Strings of custom name preference
	 */
	public String[] getCustomNamePreference( )
	{
		return convert( PreferenceFactory.getInstance( )
				.getPreferences( this, UIUtil.getCurrentProject( ) )
				.getString( CUSTOM_NAME_PREFERENCE ) );
	}

	/**
	 * Get description preference
	 * 
	 * @return String[] the array of Strings of description preference
	 */
	public String[] getDescriptionPreference( )
	{
		return convert( PreferenceFactory.getInstance( )
				.getPreferences( this, UIUtil.getCurrentProject( ) )
				.getString( DESCRIPTION_PREFERENCE ) );
	}

	/**
	 * Get the custom name preference of specified element name
	 * 
	 * @param defaultName
	 *            The specified element name
	 * @return String The custom name gotten
	 */
	public String getCustomName( String defaultName )
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
	public static String[] convert( String preferenceValue )
	{

		String preferenceValueCopy = new String( );
		preferenceValueCopy = new String( PREFERENCE_DELIMITER )
				+ preferenceValue;
		String replaceString = new String( PREFERENCE_DELIMITER )
				+ new String( PREFERENCE_DELIMITER );
		String regrex = new String( PREFERENCE_DELIMITER )
				+ SPACE
				+ new String( PREFERENCE_DELIMITER );
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
		PreferenceFactory.getInstance( )
				.getPreferences( this, UIUtil.getCurrentProject( ) )
				.setValue( DEFAULT_NAME_PREFERENCE,
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
		PreferenceFactory.getInstance( )
				.getPreferences( this, UIUtil.getCurrentProject( ) )
				.setValue( DEFAULT_NAME_PREFERENCE, element );
	}

	/**
	 * Set default names for the element names from String[]
	 * 
	 * @param elements
	 *            the array of default names
	 */
	public void setCustomNamePreference( String[] elements )
	{
		PreferenceFactory.getInstance( ).getPreferences( this,
				UIUtil.getCurrentProject( ) ).setValue( CUSTOM_NAME_PREFERENCE,
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
		PreferenceFactory.getInstance( ).getPreferences( this,
				UIUtil.getCurrentProject( ) ).setValue( CUSTOM_NAME_PREFERENCE,
				element );
	}

	/**
	 * Set descriptions for the element names from String[]
	 * 
	 * @param elements
	 *            the array of descriptions
	 */
	public void setDescriptionPreference( String[] elements )
	{
		PreferenceFactory.getInstance( ).getPreferences( this,
				UIUtil.getCurrentProject( ) ).setValue( DESCRIPTION_PREFERENCE,
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
		PreferenceFactory.getInstance( ).getPreferences( this,
				UIUtil.getCurrentProject( ) ).setValue( DESCRIPTION_PREFERENCE,
				element );
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
		PreferenceFactory.getInstance( )
				.getPreferences( this )
				.setValue( LIBRARY_PREFERENCE, buffer.toString( ) );
	}

	/**
	 * Return the library preference as an array of Strings.
	 * 
	 * @return String[] The array of strings of library preference
	 */
	public String[] getLibraryPreference( )
	{
		return convert( PreferenceFactory.getInstance( )
				.getPreferences( this )
				.getString( LIBRARY_PREFERENCE ) );
	}

	/**
	 * Set default library preference
	 */
	public void setDefaultLibraryPreference( )
	{
		PreferenceFactory.getInstance( )
				.getPreferences( this )
				.setDefault( LIBRARY_PREFERENCE, "" ); //$NON-NLS-1$
		PreferenceFactory.getInstance( )
				.getPreferences( this )
				.setDefault( LIBRARY_WARNING_PREFERENCE,
						MessageDialogWithToggle.PROMPT ); //$NON-NLS-1$
	}

	/**
	 * Return default library preference as an array of Strings.
	 * 
	 * @return String[] The array of strings of default library preference
	 */
	public String[] getDefaultLibraryPreference( )
	{
		return convert( PreferenceFactory.getInstance( )
				.getPreferences( this )
				.getDefaultString( LIBRARY_PREFERENCE ) );
	}

	/**
	 * Return default template preference
	 * 
	 * @return String The String of default template preference
	 */
	public String getDefaultTemplatePreference( )
	{
		return PreferenceFactory.getInstance( )
				.getPreferences( this )
				.getDefaultString( TEMPLATE_PREFERENCE );

	}

	/**
	 * set default template preference
	 * 
	 */
	public void setDefaultTemplatePreference( )
	{
		String defaultDir = new String( UIUtil.getHomeDirectory( ) );
		PreferenceFactory.getInstance( )
				.getPreferences( this )
				.setDefault( TEMPLATE_PREFERENCE, defaultDir );
	}

	/**
	 * Return default template preference
	 * 
	 * @return String The string of default template preference
	 */
	public String getTemplatePreference( )
	{
		return PreferenceFactory.getInstance( ).getPreferences( this,
				UIUtil.getCurrentProject( ) ).getString( TEMPLATE_PREFERENCE );
	}

	/**
	 * set default template preference
	 * 
	 */
	public void setTemplatePreference( String preference )
	{
		PreferenceFactory.getInstance( ).getPreferences( this,
				UIUtil.getCurrentProject( ) ).setValue( TEMPLATE_PREFERENCE,
				preference );
	}

	/**
	 * set default resource preference
	 * 
	 */
	public void setDefaultResourcePreference( )
	{
		// String metaPath = Platform.getStateLocation( ReportPlugin.getDefault(
		// )
		// .getBundle( ) ).toOSString( );
		// if ( !metaPath.endsWith( File.separator ) )
		// {
		// metaPath = metaPath + File.separator;
		// }
		// metaPath = metaPath + BIRT_RESOURCE;
		// File targetFolder = new File( metaPath );
		// if ( !targetFolder.exists( ) )
		// {
		// targetFolder.mkdirs( );
		// }
		// PreferenceFactory.getInstance( ).getPreferences( this ).setDefault(
		// RESOURCE_PREFERENCE, metaPath );
		// //$NON-NLS-1$

		// String defaultDir = new String( UIUtil.getHomeDirectory( ) );
		// defaultDir = defaultDir.replace( '\\', '/' ); //$NON-NLS-1$
		// //$NON-NLS-2$
		// if ( !defaultDir.endsWith( "/" ) ) //$NON-NLS-1$
		// {
		// defaultDir = defaultDir + "/"; //$NON-NLS-1$
		// }
		// defaultDir = defaultDir + BIRT_RESOURCE;
		// if ( !defaultDir.endsWith( "/" ) ) //$NON-NLS-1$
		// {
		// defaultDir = defaultDir + "/"; //$NON-NLS-1$
		// }
		// File targetFolder = new File( defaultDir );
		// if ( !targetFolder.exists( ) )
		// {
		// targetFolder.mkdirs( );
		// }

		// bug 151361, set default resource folder empty
		PreferenceFactory.getInstance( )
				.getPreferences( this )
				.setDefault( RESOURCE_PREFERENCE, "" ); //$NON-NLS-1$
	}

	/**
	 * Return default resouce preference
	 * 
	 * @return String The String of default resource preference
	 */
	public String getDefaultResourcePreference( )
	{
		return PreferenceFactory.getInstance( )
				.getPreferences( this )
				.getDefaultString( RESOURCE_PREFERENCE );
	}

	/**
	 * Return resource preference
	 * 
	 * @return String The string of resource preference
	 */
	public String getResourcePreference( )
	{
		return PreferenceFactory.getInstance( ).getPreferences( this,
				UIUtil.getCurrentProject( ) ).getString( RESOURCE_PREFERENCE );
	}

	/**
	 * set resource preference
	 * 
	 */
	public void setResourcePreference( String preference )
	{
		PreferenceFactory.getInstance( ).getPreferences( this,
				UIUtil.getCurrentProject( ) ).setValue( RESOURCE_PREFERENCE,
				preference );
		CorePlugin.RESOURCE_FOLDER = preference;
	}

	/**
	 * Add View ID into ignore view list.
	 * 
	 * @param str
	 */
	public void addIgnoreViewID( String str )
	{
		ignore.add( str );
	}

	/**
	 * Remove View ID from ignore view list.
	 * 
	 * @param str
	 */
	public void removeIgnoreViewID( String str )
	{
		ignore.remove( str );
	}

	/**
	 * Test whether the View ID is in the ignore view list.
	 * 
	 * @param str
	 * @return
	 */
	public boolean containIgnoreViewID( String str )
	{
		return ignore.contains( str );
	}

	/**
	 * set default comment preference
	 * 
	 */
	public void setDefaultCommentPreference( )
	{
		PreferenceFactory.getInstance( )
				.getPreferences( this )
				.setDefault( COMMENT_PREFERENCE,
						Messages.getString( "org.eclipse.birt.report.designer.ui.preference.commenttemplates.defaultcomment" ) ); //$NON-NLS-1$
	}

	/**
	 * Return default comment preference
	 * 
	 * @return String The string of default comment preference
	 */
	public String getDefaultCommentPreference( )
	{
		return PreferenceFactory.getInstance( )
				.getPreferences( this )
				.getDefaultString( COMMENT_PREFERENCE );
	}

	/**
	 * Return comment preference
	 * 
	 * @return String The string of comment preference
	 */
	public String getCommentPreference( )
	{
		return PreferenceFactory.getInstance( ).getPreferences( this,
				UIUtil.getCurrentProject( ) ).getString( COMMENT_PREFERENCE );
	}

	/**
	 * set comment preference
	 * 
	 */
	public void setCommentPreference( String preference )
	{
		PreferenceFactory.getInstance( ).getPreferences( this,
				UIUtil.getCurrentProject( ) ).setValue( COMMENT_PREFERENCE,
				preference );
	}

	/**
	 * set enable default comment preference
	 * 
	 */
	public void setDefaultEnableCommentPreference( )
	{
		PreferenceFactory.getInstance( )
				.getPreferences( this )
				.setDefault( ENABLE_COMMENT_PREFERENCE, true );
	}

	/**
	 * Return default enable comment preference
	 * 
	 * @return boolean The bool value of default enable comment preference
	 */
	public boolean getDefaultEnabelCommentPreference( )
	{
		return PreferenceFactory.getInstance( )
				.getPreferences( this )
				.getDefaultBoolean( ENABLE_COMMENT_PREFERENCE );
	}

	/**
	 * Return enable comment preference
	 * 
	 * @return boolean The bool value of enable comment preference
	 */
	public boolean getEnableCommentPreference( )
	{
		return PreferenceFactory.getInstance( )
				.getPreferences( this, UIUtil.getCurrentProject( ) )
				.getBoolean( ENABLE_COMMENT_PREFERENCE );
	}

	/**
	 * set enable comment preference
	 * 
	 */
	public void setEnableCommentPreference( boolean preference )
	{
		PreferenceFactory.getInstance( )
				.getPreferences( this, UIUtil.getCurrentProject( ) )
				.setValue( ENABLE_COMMENT_PREFERENCE, preference );
	}

	/**
	 * Returns all available extension names for report design files.
	 * 
	 * @return the extension name lisr
	 */
	public List getReportExtensionNameList( )
	{
		if ( reportExtensionNames == null )
		{
			reportExtensionNames = new ArrayList( );

			IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry( );
			IConfigurationElement[] elements = extensionRegistry.getConfigurationElementsFor( "org.eclipse.ui.editors" ); //$NON-NLS-1$
			for ( int i = 0; i < elements.length; i++ )
			{

				String id = elements[i].getAttribute( "id" ); //$NON-NLS-1$
				if ( "org.eclipse.birt.report.designer.ui.editors.ReportEditor".equals( id ) ) //$NON-NLS-1$
				{
					if ( elements[i].getAttribute( "extensions" ) != null )
					{
						String[] extensionNames = elements[i].getAttribute( "extensions" )
								//$NON-NLS-1$
								.split( "," ); //$NON-NLS-1$
						for ( int j = 0; j < extensionNames.length; j++ )
						{
							extensionNames[j] = extensionNames[j].trim( );
							if ( !reportExtensionNames.contains( extensionNames[j] ) )
							{
								reportExtensionNames.add( extensionNames[j] );
							}
						}
					}
				}
			}

			IContentTypeManager contentTypeManager = Platform.getContentTypeManager( );
			IContentType contentType = contentTypeManager.getContentType( "org.eclipse.birt.report.designer.ui.editors.reportdesign" ); //$NON-NLS-1$
			String[] fileSpecs = contentType.getFileSpecs( IContentType.FILE_EXTENSION_SPEC );
			for ( int i = 0; i < fileSpecs.length; i++ )
			{
				reportExtensionNames.add( fileSpecs[i] );
			}
		}
		return reportExtensionNames;
	}

	/**
	 * Checks if the file is a report design file by its file name
	 * 
	 * @return true if the extension name of the file can be recognized as a
	 *         report design file, or false otherwise.
	 */
	public boolean isReportDesignFile( String filename )
	{
		if ( filename != null )
		{
			for ( Iterator iter = ReportPlugin.getDefault( )
					.getReportExtensionNameList( )
					.iterator( ); iter.hasNext( ); )
			{
				if ( filename.endsWith( "." + (String) iter.next( ) ) ) //$NON-NLS-1$
				{
					return true;
				}
			}
		}
		return false;
	}

	public String getResourceFolder( )
	{
		SessionHandleAdapter.getInstance( )
				.getSessionHandle( )
				.setBirtResourcePath( ReportPlugin.getDefault( )
						.getResourcePreference( ) );

		SessionHandleAdapter.getInstance( )
				.getSessionHandle( )
				.setResourceFolder( ReportPlugin.getDefault( )
						.getResourcePreference( ) );

		String resourceFolder = SessionHandleAdapter.getInstance( )
				.getSessionHandle( )
				.getResourceFolder( );
		if ( ( resourceFolder == null || resourceFolder.equals( "" ) ) //$NON-NLS-1$
				&& SessionHandleAdapter.getInstance( ).getReportDesignHandle( ) != null
				&& SessionHandleAdapter.getInstance( )
						.getReportDesignHandle( )
						.getResourceFolder( ) != null )
		{
			resourceFolder = SessionHandleAdapter.getInstance( )
					.getReportDesignHandle( )
					.getResourceFolder( );
		}
		return resourceFolder;
	}

	private static LinkedHashMap filterMap = new LinkedHashMap( );

	private static void initFilterMap( IPreferences store, ResourceFilter filter )
	{
		if ( store.contains( filter.getType( ) ) )
			filter.setEnabled( store.getBoolean( filter.getType( ) ) );
		filterMap.put( filter.getType( ), filter );
	}

	public static LinkedHashMap getFilterMap( )
	{
		return filterMap;
	}
}
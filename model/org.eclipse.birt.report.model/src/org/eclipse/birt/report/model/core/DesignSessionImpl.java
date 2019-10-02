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

package org.eclipse.birt.report.model.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.IAbsoluteFontSizeValueProvider;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.ModuleOption;
import org.eclipse.birt.report.model.api.command.LibraryChangeEvent;
import org.eclipse.birt.report.model.api.command.ResourceChangeEvent;
import org.eclipse.birt.report.model.api.core.IAccessControl;
import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.api.core.IResourceChangeListener;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.ColorUtil;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.Theme;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;
import org.eclipse.birt.report.model.elements.interfaces.IThemeModel;
import org.eclipse.birt.report.model.i18n.ModelMessages;
import org.eclipse.birt.report.model.i18n.ThreadResources;
import org.eclipse.birt.report.model.metadata.DefaultAbsoluteFontSizeValueProvider;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.parser.DesignReader;
import org.eclipse.birt.report.model.parser.DesignSchemaConstants;
import org.eclipse.birt.report.model.parser.GenericModuleReader;
import org.eclipse.birt.report.model.parser.LibraryReader;
import org.eclipse.birt.report.model.util.LibraryUtil;
import org.eclipse.birt.report.model.util.ModelUtil;
import org.eclipse.birt.report.model.util.ResourceLocatorImpl;
import org.eclipse.birt.report.model.util.URIUtilImpl;

import com.ibm.icu.util.ULocale;

/**
 * Represents a design session for a user of the application based on the Design
 * Engine. Tracks the user's set of open designs, the user's locale, the
 * application units and so on. Also provides methods for opening and creating
 * designs.
 * <p>
 * <code>DesignSession</code> allows to specify customized resource locator to
 * search a file. This resource locator must be set before opening any
 * <code>ReportDesign</code>, so for example, when encountering a library tag in
 * the design file, the parser needs to know where to get the library file by
 * this resource locator, the code in the parser might be like the following.
 * 
 * @see org.eclipse.birt.report.model.api.SessionHandle
 */

public class DesignSessionImpl
{

	private static final Logger LOG = Logger.getLogger(DesignSessionImpl.class.getName());


	/**
	 * file with TOC default value.
	 */

	public static final String TOC_DEFAULT_VALUE = "templates/TOCDefaultValue.xml";//$NON-NLS-1$

	/**
	 * list each item is <code>Style</code>
	 */
	private List<DesignElement> defaultTOCStyleList = null;
	
	/**
	 * Resource path.
	 */
	protected String resourceFolder = null;

	/**
	 * The algorithm of how to search a file.
	 */

	protected IResourceLocator resourceLocator = new ResourceLocatorImpl( );

	/**
	 * Static resource path to do the compatibility of some APIs.
	 */

	static String resourcePath = null;

	/**
	 * The algorithm of how to provide the absolute dimension value for the
	 * predefined absolute font size.
	 */

	protected IAbsoluteFontSizeValueProvider fontSizeProvider = DefaultAbsoluteFontSizeValueProvider
			.getInstance( );

	/**
	 * The list of open designs for the user.
	 */

	protected List<Module> modules = new ArrayList<Module>( );

	/**
	 * The user's locale.
	 */

	protected ULocale locale;

	/**
	 * The units of measure used by the application.
	 */

	private String units = DesignChoiceConstants.UNITS_IN;

	/**
	 * RGB display preference for color properties used by the application;
	 */

	private int colorFormat = ColorUtil.CSS_ABSOLUTE_FORMAT;

	/**
	 * The application-specific default values of style properties.
	 */

	private HashMap<String, Object> defaultValues = new HashMap<String, Object>( );

	/**
	 * Resource change listener list to handle the resource change events.
	 */

	private List<IResourceChangeListener> resourceChangeListeners = null;

	/**
	 * The flag to determine whether the TOC style has been initialized.
	 */
	private Boolean isTOCStyleInitialized = Boolean.FALSE;

	/**
	 * Constructor.
	 * 
	 * @param theLocale
	 *            the user's locale. If null, use the system locale.
	 */

	public DesignSessionImpl( ULocale theLocale )
	{
		locale = theLocale;
		if ( locale == null )
			locale = ULocale.getDefault( );

		activate( );
	}

	/**
	 * Activates this session within a thread. Used in the web environment to
	 * associate this session with a message thread. Required to allow the
	 * thread to access localized messages.
	 */

	public final void activate( )
	{
		ThreadResources.setLocale( locale );
	}

	/**
	 * Dis-associates this session from a thread. Used in the web environment at
	 * the end of a message thread to clean up design-engine artifacts
	 * associated with the thread.
	 */

	public final void suspend( )
	{
		ThreadResources.setLocale( null );
	}

	/**
	 * Opens a design given the file name of the design.
	 * 
	 * @param fileName
	 *            The name of the file to open. This name must include the file
	 *            name with the filename extension.
	 * @return the opened report design.
	 * @throws DesignFileException
	 *             If the file is not found, or the file contains fatal errors.
	 */

	public final ReportDesign openDesign( String fileName )
			throws DesignFileException
	{
		return openDesign( fileName, (ModuleOption) null );
	}

	/**
	 * Opens a design given the file name of the design.
	 * 
	 * @param fileName
	 *            The name of the file to open. This name must include the file
	 *            name with the filename extension.
	 * @param options
	 *            the options set for this module
	 * @return the opened report design.
	 * @throws DesignFileException
	 *             If the file is not found, or the file contains fatal errors.
	 */

	public final ReportDesign openDesign( String fileName, ModuleOption options )
			throws DesignFileException
	{
		if ( fileName == null )
			throw new IllegalArgumentException(
					"The file name must not be null" ); //$NON-NLS-1$

		initializeOptions( options );
		ReportDesign design = DesignReader.getInstance( ).read( this, fileName,
				options );
		modules.add( design );
		return design;
	}

	/**
	 * Sets the settings in the options to the session local variables.
	 * 
	 * @param options
	 *            the settings used to open the report design
	 */

	protected void initializeOptions( ModuleOption options )
	{
		if ( options == null )
			return;

		if ( resourceLocator == null )
			resourceLocator = options.getResourceLocator( );

		if ( resourceFolder == null )
			resourceFolder = options.getResourceFolder( );
	}

	/**
	 * Opens a design given a stream to the design and the the file name of the
	 * design.
	 * 
	 * @param fileName
	 *            The name of the file to open. If null, the design will be
	 *            treated as a new design, and will be saved to a different
	 *            file. If not <code>null</code>, this name must include the
	 *            file name with the filename extension.
	 * @param is
	 *            stream to read the design
	 * @return the opened report design
	 * @throws DesignFileException
	 *             If the file is not found, or the file contains fatal errors.
	 */

	public final ReportDesign openDesign( String fileName, InputStream is )
			throws DesignFileException
	{
		return openDesign( fileName, is, (ModuleOption) null );
	}

	/**
	 * Opens a design given a stream to the design and the the file name of the
	 * design.
	 * 
	 * @param fileName
	 *            The name of the file to open. If null, the design will be
	 *            treated as a new design, and will be saved to a different
	 *            file. If not <code>null</code>, this name must include the
	 *            file name with the filename extension.
	 * @param is
	 *            stream to read the design
	 * @param options
	 *            the options set for this module
	 * @return the opened report design
	 * @throws DesignFileException
	 *             If the file is not found, or the file contains fatal errors.
	 */

	public final ReportDesign openDesign( String fileName, InputStream is,
			ModuleOption options ) throws DesignFileException
	{
		if ( is == null )
			throw new IllegalArgumentException(
					"The input stream must not be null" ); //$NON-NLS-1$

		initializeOptions( options );
		ReportDesign design = DesignReader.getInstance( ).read( this, fileName,
				is, options );
		modules.add( design );
		return design;
	}

	/**
	 * Opens a design given a stream to the design and the the file name of the
	 * design.
	 * 
	 * @param systemId
	 *            the uri where to find the relative sources for the library.
	 *            This url is treated as an absolute directory.
	 * @param is
	 *            the input stream to read the design
	 * @return the opened report design
	 * @throws DesignFileException
	 *             If the file is not found, or the file contains fatal errors.
	 */

	public final ReportDesign openDesign( URL systemId, InputStream is )
			throws DesignFileException
	{
		return openDesign( systemId, is, null );
	}

	/**
	 * Opens a design given a stream to the design and the the file name of the
	 * design.
	 * 
	 * @param systemId
	 *            the uri where to find the relative sources for the library.
	 *            This url is treated as an absolute directory.
	 * @param is
	 *            the input stream to read the design
	 * @param options
	 * @return the opened report design
	 * @throws DesignFileException
	 *             If the file is not found, or the file contains fatal errors.
	 */

	public final ReportDesign openDesign( URL systemId, InputStream is,
			ModuleOption options ) throws DesignFileException
	{
		if ( is == null )
			throw new IllegalArgumentException(
					"The input stream must not be null" ); //$NON-NLS-1$

		ReportDesign design = DesignReader.getInstance( ).read( this, systemId,
				is, options );
		modules.add( design );
		return design;
	}

	/**
	 * Open a module regardless of the module type(library or report design).
	 * 
	 * @param fileName
	 *            file name of the module This url is treated as an absolute
	 *            directory.
	 * @param is
	 *            the input stream to read the module
	 * @return the opened module
	 * @throws DesignFileException
	 *             If the file is not found, or the file contains fatal errors.
	 */

	public final Module openModule( String fileName, InputStream is )
			throws DesignFileException
	{
		return openModule( fileName, is, (ModuleOption) null );
	}

	/**
	 * Open a module regardless of the module type(library or report design).
	 * 
	 * @param fileName
	 *            file name of the module This url is treated as an absolute
	 *            directory.
	 * @param is
	 *            the input stream to read the module
	 * @param options
	 *            the options set for this module
	 * @return the opened module
	 * @throws DesignFileException
	 *             If the file is not found, or the file contains fatal errors.
	 */

	public Module openModule( String fileName, InputStream is,
			ModuleOption options ) throws DesignFileException
	{
		if ( is == null )
			throw new IllegalArgumentException(
					"The input stream must not be null" ); //$NON-NLS-1$

		initializeOptions( options );
		Module module = GenericModuleReader.getInstance( ).read( this,
				fileName, is, options );

		modules.add(module );

		return module;
	}

	/**
	 * Open a module regardless of the module type(library or report design).
	 * 
	 * @param fileName
	 *            file name of the module
	 * @return the opened module
	 * @throws DesignFileException
	 *             If the file is not found, or the file contains fatal errors.
	 */

	public final Module openModule( String fileName )
			throws DesignFileException
	{
		return openModule( fileName, (ModuleOption) null );
	}

	/**
	 * Open a module regardless of the module type(library or report design).
	 * 
	 * @param fileName
	 *            file name of the module
	 * @param options
	 *            the options set for this module
	 * @return the opened module
	 * @throws DesignFileException
	 *             If the file is not found, or the file contains fatal errors.
	 */

	public Module openModule( String fileName, ModuleOption options )
			throws DesignFileException
	{
		if ( fileName == null )
			throw new IllegalArgumentException(
					"The file name must not be null" ); //$NON-NLS-1$

		initializeOptions( options );
		Module module = GenericModuleReader.getInstance( ).read( this,
				fileName, options );
		//assert module instanceof Library || module instanceof ReportDesign;

		modules.add( module );

		return module;
	}

	/**
	 * Opens a library with the given library file name.
	 * 
	 * @param fileName
	 *            the file name of the library to open. This name must include
	 *            the file name with the filename extension.
	 * @return the opened library
	 * @throws DesignFileException
	 *             If the file is not found, or the file contains fatal errors.
	 */

	public final Library openLibrary( String fileName )
			throws DesignFileException
	{
		return openLibrary( fileName, (ModuleOption) null );
	}

	/**
	 * Opens a library with the given library file name.
	 * 
	 * @param fileName
	 *            the file name of the library to open. This name must include
	 *            the file name with the filename extension.
	 * @param options
	 *            the options set for this module
	 * @return the opened library
	 * @throws DesignFileException
	 *             If the file is not found, or the file contains fatal errors.
	 */

	public final Library openLibrary( String fileName, ModuleOption options )
			throws DesignFileException
	{
		if ( fileName == null )
			throw new IllegalArgumentException(
					"The file name must not be null" ); //$NON-NLS-1$

		initializeOptions( options );
		Library library = LibraryReader.getInstance( ).read( this, fileName,
				options );
		modules.add( library );
		return library;
	}

	/**
	 * Opens a library given a stream to the library and the the file name of
	 * the library.
	 * 
	 * @param fileName
	 *            The name of the file to open. If null, the library will be
	 *            treated as a new library, and will be saved to a different
	 *            file. If not <code>null</code>, this name must include the
	 *            file name with the filename extension.
	 * @param is
	 *            stream to read the design
	 * @return the opened report design
	 * @throws DesignFileException
	 *             If the file is not found, or the file contains fatal errors.
	 */

	public final Library openLibrary( String fileName, InputStream is )
			throws DesignFileException
	{
		return openLibrary( fileName, is, (ModuleOption) null );
	}

	/**
	 * Opens a library given a stream to the library and the the file name of
	 * the library.
	 * 
	 * @param fileName
	 *            The name of the file to open. If null, the library will be
	 *            treated as a new library, and will be saved to a different
	 *            file. If not <code>null</code>, this name must include the
	 *            file name with the filename extension.
	 * @param is
	 *            stream to read the design
	 * @param options
	 *            the options set for this module
	 * @return the opened report design
	 * @throws DesignFileException
	 *             If the file is not found, or the file contains fatal errors.
	 */

	public final Library openLibrary( String fileName, InputStream is,
			ModuleOption options ) throws DesignFileException
	{
		if ( is == null )
			throw new IllegalArgumentException(
					"The input stream must not be null" ); //$NON-NLS-1$

		initializeOptions( options );
		Library design = LibraryReader.getInstance( ).read( this, fileName, is,
				options );
		modules.add( design );
		return design;
	}

	/**
	 * Opens a library with the given library file name.
	 * 
	 * @param systemId
	 *            the uri where to find the relative sources for the library.
	 *            This url is treated as an absolute directory.
	 * @param is
	 *            the input stream
	 * 
	 * @return the opened library
	 * @throws DesignFileException
	 *             If the file is not found, or the file contains fatal errors.
	 */

	public final Library openLibrary( URL systemId, InputStream is )
			throws DesignFileException
	{
		return openLibrary( systemId, is, (ModuleOption) null );
	}

	/**
	 * Opens a library with the given library file name.
	 * 
	 * @param systemId
	 *            the uri where to find the relative sources for the library.
	 *            This url is treated as an absolute directory.
	 * @param is
	 *            the input stream
	 * @param options
	 *            the options set for this module
	 * 
	 * @return the opened library
	 * @throws DesignFileException
	 *             If the file is not found, or the file contains fatal errors.
	 */

	public final Library openLibrary( URL systemId, InputStream is,
			ModuleOption options ) throws DesignFileException
	{
		if ( is == null )
			throw new IllegalArgumentException(
					"The input stream must not be null" ); //$NON-NLS-1$

		initializeOptions( options );
		Library library = LibraryReader.getInstance( ).read( this, systemId,
				is, options );
		modules.add( library );
		return library;
	}

	/**
	 * Creates a new design based on a file name.
	 * 
	 * @param fileName
	 *            file name.
	 * @param options
	 * @return A handle to the report design.
	 */

	public final ReportDesign createDesign( String fileName,
			ModuleOption options )
	{
		ReportDesign design = new ReportDesign( this );

		design.setID( design.getNextID( ) );
		design.addElementID( design );

		design.setFileName( fileName );
		if ( !StringUtil.isBlank( fileName ) )
		{
			URL systemId = URIUtilImpl.getDirectory( fileName );

			if ( systemId != null )
				design.setSystemId( systemId );
		}

		if ( !isBlankCreation( options ) )
		{
			// if the extension side provide predefined style instance, those
			// style
			// will be added into the new created design tree.
			addExtensionDefaultStyles( design, false );
		}

		if ( toLatestVersion( options ) )
		{
			design.getVersionManager( ).setVersion(
					DesignSchemaConstants.REPORT_VERSION );
		}
		design.setValid( true );
		modules.add( design );
		return design;
	}

	/**
	 * Determines whether the creation action is simple or not. By default,
	 * isSimple is false.
	 * 
	 * @param options
	 * @return
	 */
	private boolean isBlankCreation( ModuleOption options )
	{
		if ( options == null )
			return false;
		Boolean isSimpleCreation = (Boolean) options
				.getProperty( ModuleOption.BLANK_CREATION_KEY );
		if ( isSimpleCreation != null && isSimpleCreation.booleanValue( ) )
			return true;
		return false;
	}

	/**
	 * 
	 * @param options
	 * @return
	 */

	private boolean toLatestVersion( ModuleOption options )
	{
		if ( options == null )
			return false;

		return options.toLatestVersion( );
	}

	/**
	 * Creates a new library based on a template file name.
	 * 
	 * @param templateName
	 *            The name of the template for the library.
	 * @return A handle to the report library.
	 * @throws DesignFileException
	 *             If the file is not found, or the file contains fatal errors.
	 */

	public final Library createLibraryFromTemplate( String templateName )
			throws DesignFileException
	{
		Library library = openLibrary( templateName );
		library.setFileName( null );

		// handle default theme
		handleDefaultTheme( library );
		return library;
	}

	/**
	 * Creates a new design based on a template file name.
	 * 
	 * @param templateName
	 *            The name of the template for the design.
	 * @return A handle to the report design.
	 * @throws DesignFileException
	 *             If the file is not found, or the file contains fatal errors.
	 */

	public final ReportDesign createDesignFromTemplate( String templateName )
			throws DesignFileException
	{
		ReportDesign design = openDesign( templateName );
		design.setFileName( null );

		// if the extension side provide predefined style instance, those style
		// will be added into the new created design tree.
		addExtensionDefaultStyles( design, false );

		return design;
	}

	/**
	 * Creates a new design based on a given template file name and input
	 * stream.
	 * 
	 * @param templateName
	 *            The name of the template for the design.
	 * @param is
	 *            stream to read the design
	 * @return A handle to the report design.
	 * @throws DesignFileException
	 *             If the file is not found, or the file contains fatal errors.
	 */

	public final ReportDesign createDesignFromTemplate( String templateName,
			InputStream is ) throws DesignFileException
	{
		ReportDesign design = openDesign( templateName, is );
		design.setFileName( null );

		// if the extension side provide predefined style instance, those style
		// will be added into the new created design tree.
		addExtensionDefaultStyles( design, false );

		return design;
	}
	
	/**
	 * Creates a new design based on a given template file name and input
	 * stream.
	 * 
	 * @param templateName
	 *            The name of the template for the design.
	 * @param is
	 *            stream to read the design
	 * @param options
	 *            the options set for this module
	 * @return A handle to the report design.
	 * @throws DesignFileException
	 *             If the file is not found, or the file contains fatal errors.
	 */

	public final ReportDesign createDesignFromTemplate( String templateName,
			InputStream is, ModuleOption options ) throws DesignFileException
	{
		ReportDesign design = openDesign( templateName, is, options );
		design.setFileName( null );

		// if the extension side provide predefined style instance, those style
		// will be added into the new created design tree.
		addExtensionDefaultStyles( design, false );

		return design;
	}

	/**
	 * Returns styles that should be added to the given design.
	 * 
	 * @param design
	 *            the report design
	 * @return a list containing style elements
	 */

	private static List<Style> findToAddExtensionDefaultStyle(
			ReportDesign design )
	{
		List<Style> retList = new ArrayList<Style>( );

		List<Style> defaultStyles = MetaDataDictionary.getInstance( )
				.getExtensionFactoryStyles( );

		for ( int i = 0; i < defaultStyles.size( ); i++ )
		{
			Style style = defaultStyles.get( i );
			assert style.getName( ) != null;

			if ( design.findStyle( style.getName( ) ) != null )
				continue;

			retList.add( style );
		}

		return retList;
	}

	/**
	 * Adds the predefined style instance defined by the extension side into the
	 * new created design tree.
	 */

	public static void addExtensionDefaultStyles( ReportDesign design,
			boolean checkName )
	{
		List<Style> tmpStyles = findToAddExtensionDefaultStyle( design );

		for ( int i = 0; i < tmpStyles.size( ); i++ )
		{
			Style style = tmpStyles.get( i );

			Style tmpStyle = null;
			try
			{
				tmpStyle = (Style) style.clone( );
			}
			catch ( CloneNotSupportedException e )
			{
				assert false;
				continue;
			}

			if ( !checkName || design.findStyle( tmpStyle.getName( ) ) == null )
			{
				design.add( tmpStyle, ReportDesign.STYLE_SLOT );
				tmpStyle.setID( design.getNextID( ) );
				design.addElementID( tmpStyle );
				design.getNameHelper( )
						.getNameSpace( ReportDesign.STYLE_NAME_SPACE )
						.insert( tmpStyle );
			}
		}

	}

	/**
	 * Creates a new library.
	 * 
	 * @return the created library.
	 */

	public final Library createLibrary( )
	{
		Library library = new Library( this );
		library.setID( library.getNextID( ) );
		library.addElementID( library );

		handleDefaultTheme( library );

		library.setValid( true );
		modules.add( library );
		return library;
	}

	/**
	 * Handles some cases for default theme. If no value is set for 'theme'
	 * property in library and there is no theme in the theme slot named as
	 * 'defaulttheme', this method will create a default theme, insert it into
	 * library theme slot and then set the theme property reference to it.
	 * 
	 * @param library
	 */
	private void handleDefaultTheme( Library library )
	{
		String themeName = library.getThemeName( );
		if ( themeName == null )
		{
			String defaultThemeName = ModelMessages
					.getMessage( IThemeModel.DEFAULT_THEME_NAME );

			Theme theme = library.findNativeTheme( defaultThemeName );
			if ( theme != null )
				return;

			theme = new Theme( defaultThemeName );
			library.setProperty( IModuleModel.THEME_PROP, new ElementRefValue(
					null, theme ) );
			LibraryUtil.insertCompatibleThemeToLibrary( library, theme );

			// set initial id.

			theme.setID( library.getNextID( ) );
			library.addElementID( theme );
		}
	}

	/**
	 * Returns an iterator over the open designs.
	 * 
	 * @return an iterator over the designs
	 */

	public final Iterator<ReportDesign> getDesignIterator( )
	{
		ArrayList<ReportDesign> designs = new ArrayList<ReportDesign>(modules.size());
		for (Module module : modules) {
			if (module instanceof ReportDesign) {
				designs.add((ReportDesign)module);
			}
		}
		return designs.iterator( );
	}

	/**
	 * Returns an iterator over the open libraries.
	 * 
	 * @return an iterator over the libraries
	 */

	public final Iterator<Library> getLibraryIterator( )
	{
		ArrayList<Library> libraries = new ArrayList<Library>(modules.size());
		for (Module module : modules) {
			if (module instanceof Library) {
				libraries.add((Library)module);
			}
		}
		return libraries.iterator( );
	}

	/**
	 * Returns an interator over the open libraries and designs.
	 * 
	 * @return an iterator over the open libraries and designs
	 */

	public Iterator<Module> getModuleIterator( )
	{
		List<Module> roots = new ArrayList<Module>( modules );
		return roots.iterator( );
	}

	/**
	 * Removes a report design from the list of open report designs.
	 * 
	 * @param module
	 *            the module to drop
	 */

	public void drop( Module module )
	{
		modules.remove(module);
	}

	/**
	 * Returns the application units. The return value is defined in
	 * <code>DesignChoiceConstants</code> and is one of:
	 * 
	 * <ul>
	 * <li><code>UNITS_IN</code></li>
	 * <li><code>UNITS_CM</code></li>
	 * <li><code>
	 * UNITS_MM</code></li>
	 * <li><code>UNITS_PT</code></li>
	 * <li><code>UNITS_PC
	 * </code></li>
	 * </ul>
	 * 
	 * @return the application units as a string
	 * 
	 * @see org.eclipse.birt.report.model.api.metadata.DimensionValue
	 */

	public final String getUnits( )
	{
		return units;
	}

	/**
	 * Sets the application units. The optional input value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * 
	 * <ul>
	 * <li><code>UNITS_IN</code></li>
	 * <li><code>UNITS_CM</code></li>
	 * <li><code>
	 * UNITS_MM</code></li>
	 * <li><code>UNITS_PT</code></li>
	 * <li><code>UNITS_PC
	 * </code></li>
	 * </ul>
	 * 
	 * @param newUnits
	 *            the new application units to set
	 * 
	 * @throws PropertyValueException
	 *             if the unit are not one of the above.
	 * 
	 * @see org.eclipse.birt.report.model.api.metadata.DimensionValue
	 * 
	 */

	public final void setUnits( String newUnits ) throws PropertyValueException
	{
		if ( DesignChoiceConstants.UNITS_CM.equalsIgnoreCase( newUnits )
				|| DesignChoiceConstants.UNITS_IN.equalsIgnoreCase( newUnits )
				|| DesignChoiceConstants.UNITS_MM.equalsIgnoreCase( newUnits )
				|| DesignChoiceConstants.UNITS_PC.equalsIgnoreCase( newUnits )
				|| DesignChoiceConstants.UNITS_PT.equalsIgnoreCase( newUnits ) )
			units = newUnits;
		else
			throw new PropertyValueException( newUnits,
					PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE,
					IPropertyType.CHOICE_TYPE );
	}

	/**
	 * Sets the color display preference for the application. The input value is
	 * an integer value that may be the following constants defined in
	 * <code>ColorUtil</code>:
	 * 
	 * <ul>
	 * <li><code>INT_FORMAT</code>
	 * <li><code>HTML_FORMAT</code>
	 * <li><code>
	 * JAVA_FORMAT</code>
	 * <li><code>CSS_ABSOLUTE_FORMAT</code>
	 * <li><code>
	 * CSS_RELATIVE_FORMAT</code>
	 * </ul>
	 * 
	 * @param format
	 *            color display preference.
	 * 
	 * @throws PropertyValueException
	 *             if the input format is not supported by DesignSession
	 * 
	 * @see org.eclipse.birt.report.model.api.util.ColorUtil
	 * 
	 */

	public final void setColorFormat( int format )
			throws PropertyValueException
	{
		if ( ( format == ColorUtil.CSS_ABSOLUTE_FORMAT )
				|| ( format == ColorUtil.CSS_RELATIVE_FORMAT )
				|| ( format == ColorUtil.HTML_FORMAT )
				|| ( format == ColorUtil.INT_FORMAT )
				|| ( format == ColorUtil.JAVA_FORMAT ) )
			colorFormat = format;
		else
			throw new PropertyValueException( Integer.valueOf( format ),
					PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE,
					IPropertyType.CHOICE_TYPE );
	}

	/**
	 * Returns the color display preference of the application. The return value
	 * is an integer value that may be the following constants defined in
	 * <code>ColorUtil</code>:
	 * 
	 * <ul>
	 * <li><code>INT_FORMAT</code>
	 * <li><code>HTML_FORMAT</code>
	 * <li><code>
	 * JAVA_FORMAT</code>
	 * <li><code>CSS_ABSOLUTE_FORMAT</code>
	 * <li><code>
	 * CSS_RELATIVE_FORMAT</code>
	 * </ul>
	 * 
	 * @return the color display preference of the application as an integer.
	 * 
	 * @see org.eclipse.birt.report.model.api.util.ColorUtil
	 * 
	 */

	public final int getColorFormat( )
	{
		return colorFormat;
	}

	/**
	 * Sets the application-specific default value of style property.
	 * 
	 * @param propName
	 *            style property name
	 * @param value
	 *            default value to set
	 * @throws PropertyValueException
	 *             if value is invalid.
	 */

	public final void setDefaultValue( String propName, Object value )
			throws PropertyValueException
	{
		assert !StringUtil.isBlank( propName );

		MetaDataDictionary dd = MetaDataDictionary.getInstance( );
		IElementPropertyDefn propDefn = dd.getStyle( ).getProperty( propName );

		assert propDefn != null;

		if ( value == null )
		{
			defaultValues.remove( propName );
		}
		else
		{
			Object actualValue = ( (ElementPropertyDefn) propDefn )
					.validateValue( null, null, value );

			defaultValues.put( propName, actualValue );
		}
	}

	/**
	 * Gets the default value of the specified style property. If the property
	 * is not style property, null will be returned.
	 * 
	 * @param propName
	 *            style property name
	 * @return The default value of this style property. If the default value is
	 *         not set, return null.
	 */

	public final Object getDefaultValue( String propName )
	{
		assert !StringUtil.isBlank( propName );

		MetaDataDictionary dd = MetaDataDictionary.getInstance( );
		IPropertyDefn propDefn = dd.getStyle( ).getProperty( propName );

		assert propDefn != null;

		return defaultValues.get( propName );
	}

	/**
	 * Sets the algorithm of how to search a file. Any existing algorithm is
	 * discarded.
	 * 
	 * @param algorithm
	 *            the algorithm to be set.
	 */

	public final void setResourceLocator( IResourceLocator algorithm )
	{
		assert algorithm != null;
		resourceLocator = algorithm;
	}

	/**
	 * Returns the installed search file algorithm. If no algorithm was
	 * installed, returns the default one.
	 * 
	 * @return the installed search file algorithm.
	 */

	public final IResourceLocator getResourceLocator( )
	{
		assert resourceLocator != null;
		return resourceLocator;
	}

	/**
	 * Returns the provider instance which provides the absolute dimension value
	 * of predefined font size choice.
	 * <ul>
	 * <li><code>FONT_SIZE_XX_SMALL</code>
	 * <li><code>FONT_SIZE_X_SMALL</code>
	 * <li><code>FONT_SIZE_SMALL</code>
	 * <li><code>FONT_SIZE_MEDIUM</code>
	 * <li>
	 * <code>FONT_SIZE_LARGE</code>
	 * <li><code>FONT_SIZE_X_LARGE</code>
	 * <li>
	 * <code>FONT_SIZE_XX_LARGE</code>
	 * </ul>
	 * 
	 * @return the instance of <code>IAbsoluteFontSizeValueProvider</code>
	 */

	public final IAbsoluteFontSizeValueProvider getPredefinedFontSizeProvider( )
	{
		return fontSizeProvider;
	}

	/**
	 * Set the instance of <code>IAbsoluteFontSizeValueProvider</code>.
	 * 
	 * @param fontSizeProvider
	 *            the fontSizeProvider to set
	 */

	public final void setPredefinedFontSizeProvider(
			IAbsoluteFontSizeValueProvider fontSizeProvider )
	{
		this.fontSizeProvider = fontSizeProvider;
	}

	/**
	 * Returns the locale of the current session.
	 * 
	 * @return the locale of the current session
	 */

	public final ULocale getLocale( )
	{
		return locale;
	}

	/**
	 * Informs this session some resources is changed. Session will check all
	 * opened mudules, all interfered modules will be informed of the changes.
	 * 
	 * <p>
	 * Current, only changes of library or message file is supported.
	 * 
	 * @param ev
	 *            the library change event to fire
	 */

	public final void fireLibChange( LibraryChangeEvent ev )
	{
		URL url = ModelUtil.getURLPresentation( ev.getChangedResourcePath( ) );
		if ( url == null )
			return;

		String path = url.toExternalForm( );
		Iterator<Module> iter = getModuleIterator( );
		while ( iter.hasNext( ) )
		{
			Module module = iter.next( );
			if ( module.getLocation( ).equalsIgnoreCase( path )
					|| module.getLibraryByLocation( path,
							IAccessControl.ARBITARY_LEVEL ) != null )
			{
				LibraryChangeEvent event = new LibraryChangeEvent(
						ev.getChangedResourcePath( ) );
				event.setTarget( module );
				event.setDeliveryPath( ev.getDeliveryPath( ) );
				module.broadcastResourceChangeEvent( event );
			}
		}

		broadcastResourceChangeEvent( ev );
	}

	/**
	 * Adds one resource change listener. The duplicate listener will not be
	 * added.
	 * 
	 * @param listener
	 *            the resource change listener to add
	 */

	public final void addResourceChangeListener(
			IResourceChangeListener listener )
	{
		if ( resourceChangeListeners == null )
			resourceChangeListeners = new ArrayList<IResourceChangeListener>( );

		if ( !resourceChangeListeners.contains( listener ) )
			resourceChangeListeners.add( listener );
	}

	/**
	 * Removes one resource change listener. If the listener not registered,
	 * then the request is silently ignored.
	 * 
	 * @param listener
	 *            the resource change listener to remove
	 * @return <code>true</code> if <code>listener</code> is successfully
	 *         removed. Otherwise <code>false</code>.
	 * 
	 */

	public final boolean removeResourceChangeListener(
			IResourceChangeListener listener )
	{
		if ( resourceChangeListeners == null )
			return false;
		return resourceChangeListeners.remove( listener );
	}

	/**
	 * Broadcasts the resource change event to the resource change listeners.
	 * 
	 * @param event
	 *            the dispose event
	 */

	public final void broadcastResourceChangeEvent( ResourceChangeEvent event )
	{
		if ( resourceChangeListeners == null
				|| resourceChangeListeners.isEmpty( ) )
			return;

		List<IResourceChangeListener> temp = new ArrayList<IResourceChangeListener>(
				resourceChangeListeners );
		Iterator<IResourceChangeListener> iter = temp.iterator( );
		while ( iter.hasNext( ) )
		{
			IResourceChangeListener listener = iter.next( );
			listener.resourceChanged( null, event );
		}
	}

	/**
	 * Sets the resource folder for this session.
	 * 
	 * @param resourceFolder
	 *            the folder to set
	 */

	public final void setResourceFolder( String resourceFolder )
	{
		this.resourceFolder = resourceFolder;
	}

	/**
	 * Gets the resource folder set in this session.
	 * 
	 * @return the resource folder set in this session
	 */

	public final String getResourceFolder( )
	{
		if ( resourceFolder == null )
			return resourcePath;
		return this.resourceFolder;
	}

	/**
	 * @return the resourcePath
	 */

	public static String getResourcePath( )
	{
		return resourcePath;
	}

	/**
	 * @param resourcePath
	 *            the resourcePath to set
	 */

	public static void setResourcePath( String resourcePath )
	{
		DesignSessionImpl.resourcePath = resourcePath;
	}

	/**
	 * Inits default toc style value.
	 * 
	 */

	private void initDefaultTOCStyle( )
	{
		defaultTOCStyleList = new ArrayList<DesignElement>( );
		URL url = new ResourceLocatorImpl( ).findResource( null,
				TOC_DEFAULT_VALUE, IResourceLocator.OTHERS, null );
		if ( url == null )
			return;

		ReportDesign tocDesign = null;
		try
		{
			DesignSessionImpl session = new DesignSessionImpl( locale );
			tocDesign = session.openDesign( url, url.openStream( ) );
			tocDesign.setReadOnly( );
		}
		catch ( DesignFileException e )
		{
			LOG.log(Level.SEVERE, "Could not init default TOC style", e);
			return;
		}
		catch ( IOException e )
		{
			LOG.log(Level.SEVERE, "Could not init default TOC style", e);
			return;
		}

		// get styles

		ContainerSlot styles = tocDesign
				.getSlot( IReportDesignModel.STYLE_SLOT );
		for ( int i = 0; i < styles.getCount( ); i++ )
		{
			defaultTOCStyleList.add( styles.getContent( i ) );
		}
	}

	/**
	 * Gets styles with default value for TOC.
	 * 
	 * @return list each item is <code>Style</code>
	 */

	public final List<DesignElement> getDefaultTOCStyleValue( )
	{
		if ( isTOCStyleInitialized )
			return Collections.unmodifiableList( defaultTOCStyleList );

		synchronized ( DesignSessionImpl.class )
		{
			if ( !isTOCStyleInitialized )
			{
				initDefaultTOCStyle( );
				isTOCStyleInitialized = Boolean.TRUE;
			}
		}
		return Collections.unmodifiableList( defaultTOCStyleList );
	}

	/**
	 * @param location
	 * @return the opened module at the specified location
	 */

	public final Module getOpenedModule( String location )
	{
		if ( location == null )
			return null;

		Iterator<Module> iter = getModuleIterator( );

		while ( iter.hasNext( ) )
		{
			Module tmpModule = iter.next( );
			if ( location.equalsIgnoreCase( tmpModule.getLocation( ) ) )
				return tmpModule;
		}

		return null;

	}

}
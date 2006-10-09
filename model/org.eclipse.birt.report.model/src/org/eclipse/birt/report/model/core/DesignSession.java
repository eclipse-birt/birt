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

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.DefaultResourceLocator;
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
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.ColorUtil;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.Theme;
import org.eclipse.birt.report.model.i18n.ModelMessages;
import org.eclipse.birt.report.model.i18n.ModelResourceHandle;
import org.eclipse.birt.report.model.i18n.ThreadResources;
import org.eclipse.birt.report.model.metadata.DefaultAbsoluteFontSizeValueProvider;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.PropertyType;
import org.eclipse.birt.report.model.parser.DesignReader;
import org.eclipse.birt.report.model.parser.GenericModuleReader;
import org.eclipse.birt.report.model.parser.LibraryReader;
import org.eclipse.birt.report.model.util.ModelUtil;

import com.ibm.icu.util.ULocale;

/**
 * Represents a design session for a user of the application based on the Design
 * Engine. Tracks the user's set of open designs, the user's locale, the
 * application units and so on. Also provides methods for opening and creating
 * designs.
 * <p>
 * <code>DesignSession</code> allows to specify customized resource locator to
 * search a file. This resource locator must be set before opening any
 * <code>ReportDesign</code>, so for example, when encountering a library tag
 * in the design file, the parser needs to know where to get the library file by
 * this resource locator, the code in the parser might be like the following.
 * 
 * @see org.eclipse.birt.report.model.api.SessionHandle
 */

public class DesignSession
{

	/**
	 * Resource path.
	 */

	protected String resourceFolder = null;

	/**
	 * The algorithm of how to search a file.
	 */

	protected IResourceLocator resourceLocator = new DefaultResourceLocator( );

	/**
	 * Static resource path to do the compability of some APIs.
	 */

	protected static String resourcePath = null;

	/**
	 * The algorithm of how to provide the absolute dimension value for the
	 * predefined absolute font size.
	 */

	protected IAbsoluteFontSizeValueProvider fontSizeProvider = DefaultAbsoluteFontSizeValueProvider
			.getInstance( );

	/**
	 * The list of open designs for the user.
	 */

	protected List designs = new ArrayList( );

	/**
	 * The list of open libraries for the user.
	 */

	protected List libraries = new ArrayList( );

	/**
	 * The user's locale.
	 */

	protected ULocale locale;

	/**
	 * The resource bundle for the user's locale.
	 */

	protected ModelResourceHandle resources;

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

	private HashMap defaultValues = new HashMap( );

	/**
	 * Resource change listener list to handle the resource change events.
	 */

	private List resourceChangeListeners = null;

	/**
	 * Constructor.
	 * 
	 * @param theLocale
	 *            the user's locale. If null, use the system locale.
	 */

	public DesignSession( ULocale theLocale )
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

	public void activate( )
	{
		ThreadResources.setLocale( locale );
	}

	/**
	 * Dis-associates this session from a thread. Used in the web environment at
	 * the end of a message thread to clean up design-engine artifacts
	 * associated with the thread.
	 */

	public void suspend( )
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

	public ReportDesign openDesign( String fileName )
			throws DesignFileException
	{
		ModuleOption options = null;
		return openDesign( fileName, options );
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

	public ReportDesign openDesign( String fileName, ModuleOption options )
			throws DesignFileException
	{
		if ( fileName == null )
			throw new IllegalArgumentException(
					"The file name must not be null" ); //$NON-NLS-1$
		ReportDesign design = DesignReader.getInstance( ).read( this, fileName,
				options );
		designs.add( design );
		return design;
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

	public ReportDesign openDesign( String fileName, InputStream is )
			throws DesignFileException
	{
		ModuleOption options = null;
		return openDesign( fileName, is, options );
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

	public ReportDesign openDesign( String fileName, InputStream is,
			ModuleOption options ) throws DesignFileException
	{
		if ( is == null )
			throw new IllegalArgumentException(
					"The input stream must not be null" ); //$NON-NLS-1$

		ReportDesign design = DesignReader.getInstance( ).read( this, fileName,
				is, options );
		designs.add( design );
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

	public ReportDesign openDesign( URL systemId, InputStream is )
			throws DesignFileException
	{
		ModuleOption options = null;
		return openDesign( systemId, is, options );
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

	public ReportDesign openDesign( URL systemId, InputStream is,
			ModuleOption options ) throws DesignFileException
	{
		if ( is == null )
			throw new IllegalArgumentException(
					"The input stream must not be null" ); //$NON-NLS-1$

		ReportDesign design = DesignReader.getInstance( ).read( this, systemId,
				is, options );
		designs.add( design );
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

	public Module openModule( String fileName, InputStream is )
			throws DesignFileException
	{
		ModuleOption options = null;
		return openModule( fileName, is, options );
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

		Module module = GenericModuleReader.getInstance( ).read( this,
				fileName, is, options );
		assert module instanceof Library || module instanceof ReportDesign;

		if ( module instanceof ReportDesign )
			designs.add( module );
		else
			libraries.add( module );

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

	public Module openModule( String fileName ) throws DesignFileException
	{
		ModuleOption options = null;
		return openModule( fileName, options );
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

		Module module = GenericModuleReader.getInstance( ).read( this,
				fileName, options );
		assert module instanceof Library || module instanceof ReportDesign;

		if ( module instanceof ReportDesign )
			designs.add( module );
		else
			libraries.add( module );

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

	public Library openLibrary( String fileName ) throws DesignFileException
	{
		ModuleOption options = null;
		return openLibrary( fileName, options );
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

	public Library openLibrary( String fileName, ModuleOption options )
			throws DesignFileException
	{
		if ( fileName == null )
			throw new IllegalArgumentException(
					"The file name must not be null" ); //$NON-NLS-1$

		Library library = LibraryReader.getInstance( ).read( this, fileName,
				options );
		libraries.add( library );
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

	public Library openLibrary( String fileName, InputStream is )
			throws DesignFileException
	{
		ModuleOption options = null;
		return openLibrary( fileName, is, options );
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

	public Library openLibrary( String fileName, InputStream is,
			ModuleOption options ) throws DesignFileException
	{
		if ( is == null )
			throw new IllegalArgumentException(
					"The input stream must not be null" ); //$NON-NLS-1$

		Library design = LibraryReader.getInstance( ).read( this, fileName, is,
				options );
		designs.add( design );
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

	public Library openLibrary( URL systemId, InputStream is )
			throws DesignFileException
	{
		ModuleOption options = null;
		return openLibrary( systemId, is, options );
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

	public Library openLibrary( URL systemId, InputStream is,
			ModuleOption options ) throws DesignFileException
	{
		if ( is == null )
			throw new IllegalArgumentException(
					"The input stream must not be null" ); //$NON-NLS-1$

		Library library = LibraryReader.getInstance( ).read( this, systemId,
				is, options );
		libraries.add( library );
		return library;
	}

	/**
	 * Creates a new design based on a file name. The template name can be null
	 * if no template is desired.
	 * 
	 * @param fileName
	 *            file name.
	 * @return A handle to the report design.
	 */

	public ReportDesign createDesign( String fileName )
	{
		ReportDesign design = new ReportDesign( this );
		design.setValid( true );
		designs.add( design );
		return design;
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

	public ReportDesign createDesignFromTemplate( String templateName )
			throws DesignFileException
	{
		ReportDesign design = openDesign( templateName );
		return design;
	}

	/**
	 * Creates a new design not based on a template.
	 * 
	 * @return The new report design.
	 */

	public ReportDesign createDesign( )
	{
		return createDesign( null );
	}

	/**
	 * Creates a new library.
	 * 
	 * @return the created library.
	 */

	public Library createLibrary( )
	{
		Library library = new Library( this );

		Theme theme = new Theme( ModelMessages
				.getMessage( Theme.DEFAULT_THEME_NAME ) );
		library.setProperty( IModuleModel.THEME_PROP, new ElementRefValue(
				null, theme ) );
		ModelUtil.insertCompatibleThemeToLibrary( library, theme );

		// set initial id.

		theme.setID( library.getNextID( ) );
		library.addElementID( theme );

		library.setValid( true );
		libraries.add( library );
		return library;
	}

	/**
	 * Returns an iterator over the open designs.
	 * 
	 * @return an iterator over the designs
	 */

	public Iterator getDesignIterator( )
	{
		return designs.iterator( );
	}

	/**
	 * Returns an iterator over the open libraries.
	 * 
	 * @return an iterator over the libraries
	 */

	public Iterator getLibraryIterator( )
	{
		return libraries.iterator( );
	}

	/**
	 * Returns an interator over the open libraries and designs.
	 * 
	 * @return an iterator over the open libraries and designs
	 */

	public Iterator getModuleIterator( )
	{
		List roots = new ArrayList( );

		roots.addAll( designs );
		roots.addAll( libraries );

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
		if ( module instanceof ReportDesign )
		{
			assert designs.contains( module );
			designs.remove( module );
		}
		else if ( module instanceof Library )
		{
			assert libraries.contains( module );
			libraries.remove( module );
		}
	}

	/**
	 * Returns the application units. The return value is defined in
	 * <code>DesignChoiceConstants</code> and is one of:
	 * 
	 * <ul>
	 * <li><code>UNITS_IN</code></li>
	 * <li><code>UNITS_CM</code></li>
	 * <li><code>UNITS_MM</code></li>
	 * <li><code>UNITS_PT</code></li>
	 * <li><code>UNITS_PC</code></li>
	 * </ul>
	 * 
	 * @return the application units as a string
	 * 
	 * @see org.eclipse.birt.report.model.api.metadata.DimensionValue
	 */

	public String getUnits( )
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
	 * <li><code>UNITS_MM</code></li>
	 * <li><code>UNITS_PT</code></li>
	 * <li><code>UNITS_PC</code></li>
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

	public void setUnits( String newUnits ) throws PropertyValueException
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
					PropertyType.CHOICE_TYPE );
	}

	/**
	 * Sets the color display preference for the application. The input value is
	 * an integer value that may be the following constants defined in
	 * <code>ColorUtil</code>:
	 * 
	 * <ul>
	 * <li><code>INT_FORMAT</code>
	 * <li><code>HTML_FORMAT</code>
	 * <li><code>JAVA_FORMAT</code>
	 * <li><code>CSS_ABSOLUTE_FORMAT</code>
	 * <li><code>CSS_RELATIVE_FORMAT</code>
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

	public void setColorFormat( int format ) throws PropertyValueException
	{
		if ( ( format == ColorUtil.CSS_ABSOLUTE_FORMAT )
				|| ( format == ColorUtil.CSS_RELATIVE_FORMAT )
				|| ( format == ColorUtil.HTML_FORMAT )
				|| ( format == ColorUtil.INT_FORMAT )
				|| ( format == ColorUtil.JAVA_FORMAT ) )
			colorFormat = format;
		else
			throw new PropertyValueException( new Integer( format ),
					PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE,
					PropertyType.CHOICE_TYPE );
	}

	/**
	 * Returns the color display preference of the application. The return value
	 * is an integer value that may be the following constants defined in
	 * <code>ColorUtil</code>:
	 * 
	 * <ul>
	 * <li><code>INT_FORMAT</code>
	 * <li><code>HTML_FORMAT</code>
	 * <li><code>JAVA_FORMAT</code>
	 * <li><code>CSS_ABSOLUTE_FORMAT</code>
	 * <li><code>CSS_RELATIVE_FORMAT</code>
	 * </ul>
	 * 
	 * @return the color display preference of the application as an integer.
	 * 
	 * @see org.eclipse.birt.report.model.api.util.ColorUtil
	 * 
	 */

	public int getColorFormat( )
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

	public void setDefaultValue( String propName, Object value )
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
					.validateValue( null, value );

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

	public Object getDefaultValue( String propName )
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

	public void setResourceLocator( IResourceLocator algorithm )
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

	public IResourceLocator getResourceLocator( )
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
	 * <li><code>FONT_SIZE_LARGE</code>
	 * <li><code>FONT_SIZE_X_LARGE</code>
	 * <li><code>FONT_SIZE_XX_LARGE</code>
	 * </ul>
	 * 
	 * @return the instance of <code>IAbsoluteFontSizeValueProvider</code>
	 */

	public IAbsoluteFontSizeValueProvider getPredefinedFontSizeProvider( )
	{
		return fontSizeProvider;
	}

	/**
	 * Set the instance of <code>IAbsoluteFontSizeValueProvider</code>.
	 * 
	 * @param fontSizeProvider
	 *            the fontSizeProvider to set
	 */

	public void setPredefinedFontSizeProvider(
			IAbsoluteFontSizeValueProvider fontSizeProvider )
	{
		this.fontSizeProvider = fontSizeProvider;
	}

	/**
	 * Returns the locale of the current session.
	 * 
	 * @return the locale of the current session
	 */

	public ULocale getLocale( )
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

	public void fireLibChange( LibraryChangeEvent ev )
	{
		String path = ev.getChangedResourcePath( );
		try
		{
			path = new File( path ).toURL( ).toString( );
		}
		catch ( MalformedURLException e )
		{
			return;
		}

		Iterator iter = getModuleIterator( );
		while ( iter.hasNext( ) )
		{
			Module module = (Module) iter.next( );
			if ( module.getLocation( ).equalsIgnoreCase( path )
					|| module.getLibraryByLocation( path,
							IAccessControl.ARBITARY_LEVEL ) != null )
			{
				LibraryChangeEvent event = new LibraryChangeEvent( ev
						.getChangedResourcePath( ) );
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

	public void addResourceChangeListener( IResourceChangeListener listener )
	{
		if ( resourceChangeListeners == null )
			resourceChangeListeners = new ArrayList( );

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

	public boolean removeResourceChangeListener(
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

	public void broadcastResourceChangeEvent( ResourceChangeEvent event )
	{
		if ( resourceChangeListeners == null
				|| resourceChangeListeners.isEmpty( ) )
			return;

		List temp = new ArrayList( resourceChangeListeners );
		Iterator iter = temp.iterator( );
		while ( iter.hasNext( ) )
		{
			IResourceChangeListener listener = (IResourceChangeListener) iter
					.next( );
			listener.resourceChanged( null, event );
		}
	}

	/**
	 * Sets the resource folder for this session.
	 * 
	 * @param resourceFolder
	 *            the folder to set
	 */

	public void setResourceFolder( String resourceFolder )
	{
		this.resourceFolder = resourceFolder;
	}

	/**
	 * Gets the resource folder set in this session.
	 * 
	 * @return the resource folder set in this session
	 */

	public String getResourceFolder( )
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
		DesignSession.resourcePath = resourcePath;
	}
}
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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

import org.eclipse.birt.report.model.api.DefaultFileLocator;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.IFileLocator;
import org.eclipse.birt.report.model.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.i18n.ResourceHandle;
import org.eclipse.birt.report.model.i18n.ThreadResources;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyType;
import org.eclipse.birt.report.model.metadata.PropertyValueException;
import org.eclipse.birt.report.model.parser.DesignReader;
import org.eclipse.birt.report.model.util.ColorUtil;
import org.eclipse.birt.report.model.util.StringUtil;

/**
 * Represents a design session for a user of the application based on the Design
 * Engine. Tracks the user's set of open designs, the user's locale, the
 * application units and so on. Also provides methods for opening and creating
 * designs.
 * <p>
 * <code>DesignSession</code> has an algorithm of how to search a file. This
 * algorithm must be set before opening any <code>ReportDesign</code>, so for
 * example, when encountering a library tag in the design file, the parser needs
 * to know where to get the library file by this search algorithm, the code in
 * the parser might be like the following.
 * 
 * <pre>
 * 
 * ISearchFileAlgorithm algorithm = reportDesign.getSession( )
 * 		.getSearchFileAlgorithm( );
 * 
 * //Get the absolute library file path.
 * String absoluteFilePath = algorithm.findFile( libraryFileName ); 
 * 
 * </pre>
 * 
 * To set this algorithm, the Java client needs to do the following when
 * starting the system.
 * 
 * <pre>
 * SessionHandle handle = DesignEngine.newSessionHandle( );
 * handle.setSearchFileAlgorithm( algorithm );
 * </pre>
 * 
 * @see org.eclipse.birt.report.model.api.SessionHandle
 */

public class DesignSession
{

	/**
	 * The algorithm of how to search a file.
	 */
	protected IFileLocator fileLocator = new DefaultFileLocator( );

	/**
	 * The list of open designs for the user.
	 */

	protected ArrayList designs = new ArrayList( );

	/**
	 * The user's locale.
	 */

	protected Locale locale;

	/**
	 * The resource bundle for the user's locale.
	 */

	protected ResourceHandle resources;

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
	 * Constructor.
	 * 
	 * @param theLocale
	 *            the user's locale. If null, use the system locale.
	 */

	public DesignSession( Locale theLocale )
	{
		if ( theLocale == null )
			theLocale = Locale.getDefault( );
		locale = theLocale;
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
	 *            The name of the file to open.
	 * @return A handle to the report design.
	 * @throws DesignFileException
	 *             If the file is not found, or the file contains fatal errors.
	 */

	public ReportDesign openDesign( String fileName )
			throws DesignFileException
	{
		ReportDesign design = DesignReader.read( this, fileName );
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
	 *            file.
	 * @param is
	 *            stream to read the design
	 * @return A handle to the report design.
	 * @throws DesignFileException
	 *             If the file is not found, or the file contains fatal errors.
	 */

	public ReportDesign openDesign( String fileName, InputStream is )
			throws DesignFileException
	{
		ReportDesign design = DesignReader.read( this, fileName, is );
		designs.add( design );
		return design;
	}

	/**
	 * Creates a new design based on a template. The template name can be null
	 * if no template is desired.
	 * 
	 * @param templateName
	 *            The name of the template for the design, or null if no
	 *            template is needed.
	 * @return A handle to the report design.
	 */

	public ReportDesign createDesign( String templateName )
	{
		ReportDesign design = new ReportDesign( this );
		design.setValid( true );
		designs.add( design );
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
	 * Returns an iterator over the open designs.
	 * 
	 * @return an iterator over the designs
	 */

	public Iterator getDesignIterator( )
	{
		return designs.iterator( );
	}

	/**
	 * Removes a report design from the list of open report designs.
	 * 
	 * @param root
	 *            the report design to drop
	 */

	public void drop( RootElement root )
	{
		assert designs.contains( root );
		designs.remove( root );
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
	 * @see org.eclipse.birt.report.model.metadata.DimensionValue
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
	 * @see org.eclipse.birt.report.model.metadata.DimensionValue
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
					PropertyValueException.INVALID_VALUE,
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
	 * @see org.eclipse.birt.report.model.util.ColorUtil
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
					PropertyValueException.INVALID_VALUE,
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
	 * @see org.eclipse.birt.report.model.util.ColorUtil
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
		PropertyDefn propDefn = dd.getStyle( ).getProperty( propName );

		assert propDefn != null;

		if ( value == null )
		{
			defaultValues.remove( propName );
		}
		else
		{
			Object actualValue = propDefn.validateValue( null, value );

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
		PropertyDefn propDefn = dd.getStyle( ).getProperty( propName );

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

	public void setFileLocator( IFileLocator algorithm )
	{
		assert algorithm != null;
		fileLocator = algorithm;
	}

	/**
	 * Returns the installed search file algorithm. If no algorithm was
	 * installed, returns the default one.
	 * 
	 * @return the installed search file algorithm.
	 */

	public IFileLocator getFileLocator( )
	{
		assert fileLocator != null;
		return fileLocator;
	}
}
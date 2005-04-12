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

package org.eclipse.birt.report.model.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Locale;

import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.core.DesignSession;
import org.eclipse.birt.report.model.elements.ReportDesign;

/**
 * Represents the design state -- a session for a user. In the Eclipse
 * environment, this represents the set of open designs. In the web environment,
 * this represents open designs and locale for the session. A session has a set
 * of default values for style properties and a default unit. The session also
 * has methods to create and open designs.
 * <p>
 * A session can also provides the mechanism for specified file searching
 * algorithm. After get an new instance of SessionHandle, an algorithm of how to
 * search a file should be set by calling
 * <code>{@link #setFileLocator(IFileLocator)}</code> if the default search
 * algorithm is not the expected one.
 * <p>
 * The default algorithm will search the file specified by file name in the
 * 'base' folder of the design. If the 'base' property of the design is not set,
 * the folder where the design locates will be searched instead.
 * 
 * 
 * @see DesignSession
 * 
 * @see DefaultFileLocator
 * @see org.eclipse.birt.report.model.core.DesignSession
 */

public class SessionHandle
{

	/**
	 * The implementation of the design session.
	 */

	protected DesignSession session;

	/**
	 * Constructs a handle for the session with the given locale.
	 * 
	 * @param locale
	 *            the user's locale. If null, then the system locale is assumed.
	 */

	public SessionHandle( Locale locale )
	{
		session = new DesignSession( locale );
	}

	/**
	 * Activates this session within a thread. Used in the web environment to
	 * associate this session with a message thread or that the client user
	 * startup a new thread. Required to allow the thread to access localized
	 * messages.
	 */

	public void activate( )
	{
		session.activate( );
	}

	/**
	 * Opens a design with the given the file name.
	 * 
	 * @param fileName
	 *            name of the file to open
	 * @return handle to the report design
	 * 
	 * @throws DesignFileException
	 *             if the file is not found, or the file contains fatal errors.
	 */

	public ReportDesignHandle openDesign( String fileName )
			throws DesignFileException
	{
		return session.openDesign( fileName ).handle( );
	}

	/**
	 * Opens a design given a stream to the design and the the file name of the
	 * design.
	 * 
	 * @param fileName
	 *            name of the file to open. If <code>null</code>, the design
	 *            will be treated as a new design, and will be saved to a
	 *            different file.
	 * @param is
	 *            stream to read the design
	 * @return handle to the report design
	 * @throws DesignFileException
	 *             If the file is not found, or the file contains fatal errors.
	 */

	public ReportDesignHandle openDesign( String fileName, InputStream is )
			throws DesignFileException
	{
		return session.openDesign( fileName, is ).handle( );
	}

	/**
	 * Creates a new design based on a template. The template name can be
	 * <code>null</code> if no template is desired.
	 * 
	 * @param templateName
	 *            The name of the template for the design, or <code>null</code>
	 *            if no template is needed.
	 * @return A handle to the report design.
	 */

	public ReportDesignHandle createDesign( String templateName )
	{
		return session.createDesign( templateName ).handle( );
	}

	/**
	 * Creates a new empty design. This new design is not based on a template.
	 * 
	 * @return the handle of the new report design.
	 */

	public ReportDesignHandle createDesign( )
	{
		return session.createDesign( ).handle( );
	}

	/**
	 * Saves all designs that need a save.
	 * 
	 * @throws IOException
	 *             if a save error occurs
	 */

	public void saveAll( ) throws IOException
	{
		Iterator iter = session.getDesignIterator( );
		while ( iter.hasNext( ) )
		{
			ReportDesign design = (ReportDesign) iter.next( );
			ReportDesignHandle handle = design.handle( );
			if ( handle.needsSave( ) )
				handle.save( );
		}
	}

	/**
	 * Closes all open designs.
	 * 
	 * @param save
	 *            <code>true</code> if designs are to be saved before closing
	 * @throws IOException
	 *             if a save error occurs
	 */

	public void closeAll( boolean save ) throws IOException
	{
		for ( ;; )
		{
			Iterator iter = session.getDesignIterator( );
			if ( !iter.hasNext( ) )
				break;

			ReportDesign design = (ReportDesign) iter.next( );
			ReportDesignHandle handle = design.handle( );
			if ( save && handle.needsSave( ) )
				handle.save( );
			handle.close( );
		}
	}

	/**
	 * Sets the units to be used by the application. These units are independent
	 * of those set for the design. The application and design can use the same
	 * units, or different units. The application units are those used when
	 * getting and setting dimension properties using double (float) values. The
	 * possible values are defined in <code>DesignChoiceConstants</code> and
	 * can be one of:
	 * 
	 * <ul>
	 * <li><code>UNITS_IN</code></li>
	 * <li><code>UNITS_CM</code></li>
	 * <li><code>UNITS_MM</code></li>
	 * <li><code>UNITS_PT</code></li>
	 * <li><code>UNITS_PC</code></li>
	 * </ul>
	 * 
	 * @param units
	 *            the units to set for the session -- application
	 * @throws PropertyValueException
	 *             if <code>units</code> is not one of the above
	 * 
	 * @see org.eclipse.birt.report.model.api.metadata.DimensionValue
	 */

	public void setApplicationUnits( String units )
			throws PropertyValueException
	{
		session.setUnits( units );
	}

	/**
	 * Returns the current session (application) units. The return values are
	 * defined in <code>DesignChoiceConstants</code> and is one of:
	 * 
	 * <ul>
	 * <li><code>UNITS_IN</code></li>
	 * <li><code>UNITS_CM</code></li>
	 * <li><code>UNITS_MM</code></li>
	 * <li><code>UNITS_PT</code></li>
	 * <li><code>UNITS_PC</code></li>
	 * </ul>
	 * 
	 * @return the application units
	 * 
	 * @see #setApplicationUnits
	 */

	public String getApplicationUnits( )
	{
		return session.getUnits( );
	}

	/**
	 * Sets the rgb color display preference to be used by the application. If
	 * the preference is not set the application will use default
	 * CSS_ABSOLUTE_FORMAT like "RGB(255,0,0)" for a rgb color display value.
	 * The rgbFormat argument is an integer value that may be the following
	 * constants defined in <code>ColorUtil</code>:
	 * <ul>
	 * <li><code>INT_FORMAT</code>
	 * <li><code>HTML_FORMAT</code>
	 * <li><code>JAVA_FORMAT</code>
	 * <li><code>CSS_ABSOLUTE_FORMAT</code>
	 * <li><code>CSS_RELATIVE_FORMAT</code>
	 * </ul>
	 * 
	 * @param rgbFormat
	 *            the rgb color display preference to set.
	 * 
	 * @throws PropertyValueException
	 *             if <code>rgbFormat</code> is not one of the above.
	 * 
	 * @see org.eclipse.birt.report.model.metadata.ColorPropertyType
	 */

	public void setColorFormat( int rgbFormat ) throws PropertyValueException
	{
		session.setColorFormat( rgbFormat );
	}

	/**
	 * Returns the current application rgb color display preference. The return
	 * is one of the following constants defined in <code>ColorUtil</code>:
	 * 
	 * <ul>
	 * <li><code>INT_FORMAT</code>
	 * <li><code>HTML_FORMAT</code>
	 * <li><code>JAVA_FORMAT</code>
	 * <li><code>CSS_ABSOLUTE_FORMAT</code>
	 * <li><code>CSS_RELATIVE_FORMAT</code>
	 * </ul>
	 * 
	 * @return application rgb color display preference
	 * 
	 * @see #setColorFormat(int)
	 */

	public int getColorFormat( )
	{
		return session.getColorFormat( );
	}

	/**
	 * Sets the specified default value of style property.
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
		session.setDefaultValue( propName, value );
	}

	/**
	 * Gets the default value of the specified style property.
	 * 
	 * @param propName
	 *            style property name
	 * @return The default value of this style property. If the default value is
	 *         not set, return <code>null</code>.
	 */

	public Object getDefaultValue( String propName )
	{
		return session.getDefaultValue( propName );
	}

	/**
	 * Sets the file search algorithm for the specified file searching
	 * algorithm.
	 * 
	 * @param algorithm
	 *            the algorithm to be set.
	 */

	public void setFileLocator( IFileLocator algorithm )
	{
		// this algorithm is actually set on
		// DesignSession so the ReportDesign can
		// access it

		session.setFileLocator( algorithm );
	}

	/**
	 * Returns the installed file search algorithm.
	 * 
	 * @return the file search algorithm.
	 */

	public IFileLocator getFileLocator( )
	{
		return session.getFileLocator( );
	}
}
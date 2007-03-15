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

package org.eclipse.birt.report.model.css;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.birt.report.model.api.css.StyleSheetParserException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.StyleElement;

/**
 * This class represents one include style sheet of the module.
 * 
 */

public final class CssStyleSheet
{

	/**
	 * All the external styles the style sheet contains.
	 */

	protected LinkedHashMap styles = new LinkedHashMap( );

	/**
	 * All the collected warnings during the loading.
	 */

	private ArrayList warnings = new ArrayList( );

	/**
	 * The name list of all the unsupported styles.
	 */

	private HashMap unsupportedStyles = new HashMap( );

	/**
	 * All the errors for each style.
	 */

	private HashMap warningsForStyles = new HashMap( );

	/**
	 * The error handler for the CSS parser.
	 */

	private CssErrorHandler errorHandler = null;
	
	/**
	 * Css style url resource file name
	 */
	
	private String fileName = null;

	/**
	 * Return a handle to deal with the style sheet.
	 * 
	 * @param module
	 *            the module of the style sheet
	 * @return handle to deal with the style sheet
	 */

	public CssStyleSheetHandle handle( Module module )
	{
		return new CssStyleSheetHandle( (ModuleHandle) module
				.getHandle( module ), this );
	}

	/**
	 * Gets the style with the given name.
	 * 
	 * @param name
	 *            the style name to find
	 * @return the style with the given name if found, otherwise null
	 */

	public StyleElement findStyle( String name )
	{
		return (StyleElement) styles.get( name );
	}

	/**
	 * Adds a style into the style sheet.
	 * 
	 * @param style
	 *            the style to add
	 */

	public void addStyle( DesignElement style )
	{
		assert styles.get( style.getName( ) ) == null;
		styles.put( style.getName( ), style );
	}

	/**
	 * Removes a style into the style sheet.
	 * 
	 * @param name
	 *            the name of the style
	 */

	public void removeStyle( String name )
	{
		assert styles.get( name ) != null;
		styles.remove( name );
	}

	/**
	 * Gets all the styles in the style sheet. Each one in the list is instance
	 * of <code>StyleElement</code>.
	 * 
	 * @return all the styles in the style sheet
	 */

	public List getStyles( )
	{
		return new ArrayList( styles.values( ) );
	}

	/**
	 * Adds a style sheet parser exception into the warning list.
	 * 
	 * @param warnings
	 *            the warning list to add
	 */

	public void addWarning( List warnings )
	{
		warnings.addAll( warnings );
	}

	/**
	 * Gets the warning list during the loading.
	 * 
	 * @return the warning list
	 */

	public List getWarnings( )
	{
		return this.warnings;
	}

	/**
	 * Adds an unsupported style exception to the list.
	 * 
	 * @param styleName
	 *            the style name that is not supported
	 * @param e
	 *            the exception that is caused by the unsupported style
	 */

	public void addUnsupportedStyle( String styleName,
			StyleSheetParserException e )
	{
		unsupportedStyles.put( styleName, e );
	}

	/**
	 * Returns all the unsupported style names.
	 * 
	 * @return the list of the unsupported style name
	 */

	public List getUnsupportedStyle( )
	{
		List styles = new ArrayList( );
		styles.addAll( this.unsupportedStyles.keySet( ) );
		return styles;
	}

	/**
	 * Adds the error list of the given style to the hash map. Key is the style
	 * name, content is the error list of the style.
	 * 
	 * @param styleName
	 *            the style name
	 * @param errors
	 *            the error list
	 */

	public void addWarnings( String styleName, List errors )
	{
		this.warningsForStyles.put( styleName, errors );
	}

	/**
	 * Gets the error list of the given style.
	 * 
	 * @param styleName
	 *            the style name
	 * @return the error list of the given style, otherwise null
	 */

	public List getWarnings( String styleName )
	{
		return (List) this.warningsForStyles.get( styleName );
	}

	/**
	 * Gets the error handler for the css parser.
	 * 
	 * @return Returns the errorHandler.
	 */

	public CssErrorHandler getErrorHandler( )
	{
		return errorHandler;
	}

	/**
	 * Sets the error handler for the css parser.
	 * 
	 * @param errorHandler
	 *            The errorHandler to set.
	 */

	public void setErrorHandler( CssErrorHandler errorHandler )
	{
		this.errorHandler = errorHandler;
	}

	/**
	 * Gets css file name
	 * @return css file name
	 */
	
	public String getFileName( )
	{
		return fileName;
	}

	/**
	 * Sets css file name
	 * @param fileName
	 */
	
	public void setFileName( String fileName )
	{
		this.fileName = fileName;
	}
	
}


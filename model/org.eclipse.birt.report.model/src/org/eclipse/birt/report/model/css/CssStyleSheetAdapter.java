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
import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.ICssStyleSheetOperation;

/**
 * Adapter of CssStyleSheet operation.
 * 
 */

public class CssStyleSheetAdapter implements ICssStyleSheetOperation
{

	private List csses = null;

	/**
	 * Gets css style sheet by location. Compare two absolute path of file.
	 * 
	 * @param module
	 *            module
	 * @param csses
	 *            list each item is <code>CssStyleSheet</code>
	 * @param location
	 *            absolute location
	 * @return css style sheet.
	 */

	public static CssStyleSheet getCssStyleSheetByLocation( Module module,
			List csses, String location )
	{
		if ( location == null || csses == null )
			return null;
		for ( int i = 0; i < csses.size( ); ++i )
		{
			CssStyleSheet css = (CssStyleSheet) csses.get( i );
			String tmpFileName = css.getFileName( );
			if ( tmpFileName == null )
				continue;
			if ( location.equalsIgnoreCase( tmpFileName ) )
				return css;
		}
		return null;
	}

	/**
	 * Gets position of css style sheet in all sheets. Compare two absolute path
	 * of file.
	 * 
	 * @param module
	 *            module
	 * @param csses
	 *            list each item is <code>CssStyleSheet</code>
	 * @param location
	 *            absolute location
	 * @return css style sheet.
	 */

	public static int getPositionOfCssStyleSheet( Module module, List csses,
			String location )
	{
		if ( location == null || csses == null )
			return -1;
		for ( int i = 0; i < csses.size( ); ++i )
		{
			CssStyleSheet css = (CssStyleSheet) csses.get( i );
			String tmpFileName = css.getFileName( );
			if ( tmpFileName == null )
				continue;
			if ( location.equalsIgnoreCase( tmpFileName ) )
				return i;
		}
		return -1;
	}

	/**
	 * Drops the given css from css list.
	 * 
	 * @param css
	 *            the css to drop
	 * @return the position of the css to drop
	 */

	public int dropCss( CssStyleSheet css )
	{
		assert csses != null;
		assert csses.contains( css );

		int posn = csses.indexOf( css );
		csses.remove( posn );

		return posn;
	}

	/**
	 * Adds the given css to css style sheets list.
	 * 
	 * @param css
	 *            the css to insert
	 */

	public void addCss( CssStyleSheet css )
	{
		if ( csses == null )
			csses = new ArrayList( );

		csses.add( css );
	}

	/**
	 * Insert the given css to the given position
	 * 
	 * @param css
	 * @param index
	 */

	public void insertCss( CssStyleSheet css, int index )
	{
		if ( csses == null )
			csses = new ArrayList( );
		if ( index < 0 || index > csses.size( ) )
			return;
		csses.add( index, css );
	}

	/**
	 * Returns only csses this module includes directly.
	 * 
	 * @return list of csses. each item is <code>CssStyleSheet</code>
	 */

	public List getCsses( )
	{
		if ( csses == null )
			return Collections.EMPTY_LIST;
		return Collections.unmodifiableList( csses );
	}

}

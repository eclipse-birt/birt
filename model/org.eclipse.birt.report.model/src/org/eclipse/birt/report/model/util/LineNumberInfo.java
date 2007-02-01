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

package org.eclipse.birt.report.model.util;

import java.util.HashMap;

import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.birt.report.model.api.elements.structures.IncludedLibrary;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.Theme;
import org.eclipse.birt.report.model.metadata.PropertyDefn;

/**
 * Holds line number infomation for a module.
 */

public class LineNumberInfo
{

	/**
	 * The hash map for the element id-to-lineNumber lookup.
	 */

	private HashMap elementMap = null;

	/**
	 * The hash map for the <code>IncludeLibrary</code> structures
	 * namespace-to-lineNumber lookup.
	 */

	private HashMap includeLibStructMap = null;

	/**
	 * The hash map for the <code>EmbeddedImage</code> structures
	 * name-to-lineNumber lookup.
	 */

	private HashMap embeddedImageStructMap = null;

	/**
	 * The line number for theme property in report design.
	 */

	private int themeProp = 1;

	/**
	 * Constructor.
	 * 
	 * @param module
	 */

	public LineNumberInfo( )
	{
		elementMap = new HashMap( );
		includeLibStructMap = new HashMap( );
		embeddedImageStructMap = new HashMap( );
	}

	/**
	 * Puts the line number of the object.
	 * 
	 * Note: currently, only support put line number of DesignElement,
	 * EmbeddedImage, IncludeLibrary property and theme property.
	 * 
	 * @param obj
	 *            the object
	 * @param lineNo
	 *            line number
	 */

	public void put( Object obj, Integer lineNo )
	{
		if ( obj instanceof PropertyDefn )
		{
			themeProp = lineNo == null ? 1 : lineNo.intValue( );
		}
		else if ( obj instanceof DesignElement )
		{
			elementMap.put( new Long( ( (DesignElement) obj ).getID( ) ),
					lineNo );
		}
		else if ( obj instanceof IncludedLibrary )
		{
			includeLibStructMap.put( ( (IncludedLibrary) obj ).getNamespace( ),
					lineNo );
		}
		else if ( obj instanceof EmbeddedImage )
		{
			embeddedImageStructMap.put( ( (EmbeddedImage) obj ).getName( ),
					lineNo );
		}
		else
			return;
	}

	/**
	 * Gets the line number of object.
	 * 
	 * Note: currently, only support get line number of DesignElement,
	 * EmbeddedImage, IncludeLibrary property and theme property.
	 * 
	 * @param obj
	 *            object
	 * @return line number
	 */

	public int get( Object obj )
	{
		Module tmpModule = null;

		if ( obj instanceof EmbeddedImage )
		{
			return intValue( (Integer) embeddedImageStructMap
					.get( ( (EmbeddedImage) obj ).getName( ) ) );
		}
		else if ( obj instanceof Theme
				&& ( tmpModule = ( (Theme) obj ).getRoot( ) ) instanceof Library
				&& ( (Library) tmpModule ).getHost( ) != null )
		{
			return themeProp;
		}
		else if ( obj instanceof Library
				&& ( (Library) obj ).getHost( ) != null )
		{
			return intValue( (Integer) includeLibStructMap
					.get( ( (Library) obj ).getNamespace( ) ) );
		}
		else if ( obj instanceof DesignElement )
		{
			return getElementLineNo( ( (DesignElement) obj ).getID( ) );
		}
		else
			return 1;

	}

	/**
	 * This method is for a deprecated method.
	 * 
	 * @param id
	 *            the id
	 * @return line number
	 */

	public int getElementLineNo( long id )
	{
		return intValue( (Integer) elementMap.get( new Long( id ) ) );
	}

	/**
	 * Gets int value of an integer.
	 * 
	 * @param obj
	 *            Integer object
	 * @return int value
	 */

	int intValue( Integer obj )
	{
		return obj == null ? 1 : obj.intValue( );
	}
}

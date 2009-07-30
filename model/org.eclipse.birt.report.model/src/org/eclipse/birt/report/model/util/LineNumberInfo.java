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

import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.birt.report.model.api.elements.structures.IncludedCssStyleSheet;
import org.eclipse.birt.report.model.api.elements.structures.IncludedLibrary;
import org.eclipse.birt.report.model.api.util.XPathUtil;
import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.Theme;
import org.eclipse.birt.report.model.metadata.PropertyDefn;

/**
 * Holds line number information for a module.
 */

public class LineNumberInfo
{

	/**
	 * The hash map for the element id-to-lineNumber lookup. Key is the id of
	 * design element. Value is the line number.
	 */

	private HashMap<Long, Integer> elementMap = null;

	/**
	 * The hash map for the slot xpath string-to-lineNumber lookup. Key is the
	 * xPath of the slot and value is the line number.
	 */

	private HashMap<String, Integer> slotMap = null;

	/**
	 * The hash map for the <code>IncludeLibrary</code> structures
	 * namespace-to-lineNumber lookup. Key is the namespace string of included
	 * library.
	 */

	private HashMap<String, Integer> includeLibStructMap = null;

	/**
	 * The hash map for the <code>EmbeddedImage</code> structures
	 * name-to-lineNumber lookup. Key is the name of the embedded image.
	 */

	private HashMap<String, Integer> embeddedImageStructMap = null;

	/**
	 * Hash map for the <code>CssStyleSheet</code> structures namespace. Key is
	 * the file name of the included css style sheet.
	 */
	private HashMap<String, Integer> includedCssStyleSheetStructMap = null;

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
		elementMap = new HashMap<Long, Integer>( );
		includeLibStructMap = new HashMap<String, Integer>( );
		embeddedImageStructMap = new HashMap<String, Integer>( );
		includedCssStyleSheetStructMap = new HashMap<String, Integer>( );
		slotMap = new HashMap<String, Integer>( );
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
			elementMap.put( Long.valueOf( ( (DesignElement) obj ).getID( ) ),
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
		else if ( obj instanceof IncludedCssStyleSheet )
		{
			includedCssStyleSheetStructMap.put( ( (IncludedCssStyleSheet) obj )
					.getFileName( ), lineNo );
		}
		else if ( obj instanceof ContainerContext )
		{
			String xpath = convertSlotContextToXPath( (ContainerContext) obj,
					( (ContainerContext) obj ).getElement( ).getRoot( ) );
			if ( xpath == null )
			{
				assert false;
				return;
			}
			slotMap.put( xpath, lineNo );
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
			return intValue( embeddedImageStructMap.get( ( (EmbeddedImage) obj )
					.getName( ) ) );
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
			return intValue( includeLibStructMap.get( ( (Library) obj )
					.getNamespace( ) ) );
		}
		else if ( obj instanceof IncludedCssStyleSheet )
		{
			return intValue( includedCssStyleSheetStructMap
					.get( ( (IncludedCssStyleSheet) obj ).getFileName( ) ) );
		}
		else if ( obj instanceof DesignElement )
		{
			return getElementLineNo( ( (DesignElement) obj ).getID( ) );
		}
		else if ( obj instanceof ContainerContext )
		{
			return getSlotLineNo( (ContainerContext) obj );
		}

		else
			return 1;

	}

	/**
	 * Returns the line number for the given slot.
	 * 
	 * @param obj
	 *            the container context for the slot
	 * @return line number
	 */

	private int getSlotLineNo( ContainerContext obj )
	{
		String xpath = convertSlotContextToXPath( obj, obj.getElement( )
				.getRoot( ) );
		if ( xpath == null )
		{
			assert false;
			return 1;
		}

		return intValue( slotMap.get( xpath ) );
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
		return intValue( elementMap.get( Long.valueOf( id ) ) );
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

	/**
	 * @param context
	 * @param module
	 * @return the xpath string of the container context
	 */

	public static String convertSlotContextToXPath( ContainerContext context,
			Module module )
	{
		if ( !context.isROMSlot( ) )
			return null;

		SlotHandle slot = new SlotHandle( context.getElement( ).getHandle(
				module ), context.getSlotID( ) );
		return XPathUtil.getXPath( slot );
	}
}

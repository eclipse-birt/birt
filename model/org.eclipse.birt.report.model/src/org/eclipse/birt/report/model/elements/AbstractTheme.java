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

package org.eclipse.birt.report.model.elements;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.ReferenceableElement;
import org.eclipse.birt.report.model.core.StyleElement;
import org.eclipse.birt.report.model.css.CssNameManager;
import org.eclipse.birt.report.model.elements.interfaces.IAbstractThemeModel;
import org.eclipse.birt.report.model.i18n.ModelMessages;
import org.eclipse.birt.report.model.metadata.NamePropertyType;
import org.eclipse.birt.report.model.util.StyleUtil;

/**
 * This class represents a theme in the library.
 * 
 */

public abstract class AbstractTheme extends ReferenceableElement
		implements
			IAbstractThemeModel
{

	protected List<String> cachedStyleNames = new ArrayList<String>( );

	/**
	 * Constructor.
	 */

	public AbstractTheme( )
	{
		super( );
		initSlots( );
	}

	/**
	 * Constructor with the element name.
	 * 
	 * @param theName
	 *            the element name
	 */

	public AbstractTheme( String theName )
	{
		super( theName );
		initSlots( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getSlot(int)
	 */

	public final ContainerSlot getSlot( int slot )
	{
		assert ( slot == STYLES_SLOT );
		return slots[STYLES_SLOT];
	}

	/**
	 * Gets all styles including styles in css file.
	 * 
	 * @return all styles
	 */

	public final List<StyleElement> getAllStyles( )
	{
		List<StyleElement> styleList = new ArrayList<StyleElement>( );

		// add style in css file
		if ( this instanceof ICssStyleSheetOperation )
		{
			styleList.addAll( CssNameManager
					.getStyles( (ICssStyleSheetOperation) this ) );
		}

		// add style in theme slot

		for ( int i = 0; i < slots[STYLES_SLOT].getCount( ); i++ )
		{
			StyleElement tmpStyle = (StyleElement) slots[STYLES_SLOT]
					.getContent( i );
			int pos = StyleUtil.getStylePosition( styleList, tmpStyle
					.getFullName( ) );
			if ( pos == -1 )
			{
				styleList.add( tmpStyle );
			}
			else
			{
				styleList.remove( pos );
				styleList.add( tmpStyle );
			}
		}
		return styleList;
	}

	/**
	 * Returns the style with the given name.
	 * 
	 * @param styleName
	 *            the style name
	 * @return the corresponding style
	 */

	public final StyleElement findStyle( String styleName )
	{
		if ( styleName == null )
			return null;
		List<StyleElement> styles = getAllStyles( );
		for ( int i = 0; i < styles.size( ); ++i )
		{
			StyleElement style = styles.get( i );
			// style name is case-insensitive
			if ( styleName.equalsIgnoreCase( style.getFullName( ) ) )
			{
				return style;
			}
		}
		return null;
	}

	/**
	 * Makes a unique name for this element.
	 * 
	 * @param element
	 */

	public final void makeUniqueName( DesignElement element )
	{
		if ( element == null )
			return;

		if ( !( element instanceof StyleElement ) )
			return;

		// if style is on the tree already, return
		if ( element.getRoot( ) != null )
			return;

		String name = StringUtil.trimString( element.getName( ) );

		// replace all the illegal chars with '_'
		name = NamePropertyType.validateName( name );

		if ( name == null )
			name = ModelMessages.getMessage( "New." //$NON-NLS-1$
					+ element.getDefn( ).getName( ) ).trim( );

		List<DesignElement> styles = slots[STYLES_SLOT].getContents( );
		List<String> ns = new ArrayList<String>( styles.size( ) );
		// style name is case-insensitive
		for ( int i = 0; i < styles.size( ); i++ )
			ns.add( styles.get( i ).getName( ).toLowerCase( ) );

		int index = 0;
		String baseName = name;

		assert name != null;
		// style name is case-insensitive
		String lowerCaseName = name.toLowerCase( );
		while ( cachedStyleNames.contains( lowerCaseName )
				|| ns.contains( lowerCaseName ) )
		{
			name = baseName + ++index;
			lowerCaseName = name.toLowerCase( );
		}

		// set the unique name and add the element to the name manager
		element.setName( name.trim( ) );
		cachedStyleNames.add( lowerCaseName );
	}

	/**
	 * Remove some cached name.
	 * 
	 * @param name
	 *            the name of style element.
	 */

	public final void dropCachedName( String name )
	{
		assert name != null;
		name = name.toLowerCase( );
		cachedStyleNames.remove( name );
	}
}

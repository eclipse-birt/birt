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
import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ThemeHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.validators.ThemeStyleNameValidator;
import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.ReferenceableElement;
import org.eclipse.birt.report.model.core.StyleElement;
import org.eclipse.birt.report.model.css.CssNameManager;
import org.eclipse.birt.report.model.css.CssStyleSheet;
import org.eclipse.birt.report.model.elements.interfaces.IThemeModel;
import org.eclipse.birt.report.model.util.ModelUtil;

/**
 * This class represents a theme in the library.
 * 
 */

public class Theme extends ReferenceableElement
		implements
			IThemeModel,
			ICssStyleSheetOperation
{

	/**
	 * All csses which are included in this module.
	 */

	private List csses = null;

	/**
	 * Constructor.
	 */

	public Theme( )
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

	public Theme( String theName )
	{
		super( theName );
		initSlots( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt.report.model.elements.ElementVisitor)
	 */

	public void apply( ElementVisitor visitor )
	{
		visitor.visitTheme( this );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */

	public String getElementName( )
	{
		return ReportDesignConstants.THEME_ITEM;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.core.IDesignElement#getHandle(org.eclipse.birt.report.model.core.Module)
	 */

	public DesignElementHandle getHandle( Module module )
	{
		return handle( module );
	}

	/**
	 * Returns an API handle for this element.
	 * 
	 * @param module
	 *            the report design of the row
	 * 
	 * @return an API handle for this element
	 */

	public ThemeHandle handle( Module module )
	{
		if ( handle == null )
		{
			handle = new ThemeHandle( module, this );
		}
		return (ThemeHandle) handle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getSlot(int)
	 */

	public ContainerSlot getSlot( int slot )
	{
		assert ( slot == STYLES_SLOT );
		return slots[STYLES_SLOT];
	}

	/**
	 * Gets all styles including styles in css file.
	 * 
	 * @return all styles
	 */

	public List getAllStyles( )
	{
		List styleList = new ArrayList( );

		// add style in css file
		
		styleList.addAll( CssNameManager.getStyles( this ));
		
		// add style in theme slot

		for ( int i = 0; i < slots[STYLES_SLOT].getCount( ); i++ )
		{
			StyleElement tmpStyle = (StyleElement) slots[STYLES_SLOT]
					.getContent( i );
			int pos = ModelUtil
					.getStylePotision( styleList, tmpStyle.getName( ) );
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

	public StyleElement findStyle( String styleName )
	{
		if ( styleName == null )
			return null;
		List styles = getAllStyles( );
		for ( int i = 0; i < styles.size( ); ++i )
		{
			StyleElement style = (StyleElement) styles.get( i );
			if ( styleName.equals( style.getName( ) ) )
			{
				return style;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#checkContent(org.eclipse.birt.report.model.core.Module,
	 *      org.eclipse.birt.report.model.core.ContainerInfo,
	 *      org.eclipse.birt.report.model.core.DesignElement)
	 */

	public List checkContent( Module module, ContainerContext containerInfo,
			DesignElement content )
	{
		List tmpErrors = super.checkContent( module, containerInfo, content );
		if ( !tmpErrors.isEmpty( ) )
			return tmpErrors;

		tmpErrors.addAll( ThemeStyleNameValidator.getInstance( )
				.validateForAddingStyle( (ThemeHandle) getHandle( module ),
						content.getName( ) ) );

		return tmpErrors;

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
		csses.remove( css );

		return posn;
	}

	/**
	 * Adds the given css to css list.
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
	 * Returns only csses this module includes directly.
	 * 
	 * @return list of csses. each item is <code>CssStyleSheet</code>
	 */

	public List getCsses( )
	{
		if( csses == null )
			return Collections.EMPTY_LIST;
		return Collections.unmodifiableList( csses );
	}

}

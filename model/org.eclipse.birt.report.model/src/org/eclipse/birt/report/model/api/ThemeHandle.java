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

import java.util.HashSet;
import java.util.Set;

import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.StyleElement;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.Theme;
import org.eclipse.birt.report.model.elements.interfaces.IThemeModel;

/**
 * Represents a theme in the library. Each theme contains some number of styles.
 * 
 * 
 * @see org.eclipse.birt.report.model.elements.Theme
 */

public class ThemeHandle extends ReportElementHandle implements IThemeModel
{

	/**
	 * Constructs the handle for a theme with the given design and element. The
	 * application generally does not create handles directly. Instead, it uses
	 * one of the navigation methods available on other element handles.
	 * 
	 * @param module
	 *            the module
	 * @param element
	 *            the model representation of the element
	 */

	public ThemeHandle( Module module, DesignElement element )
	{
		super( module, element );
	}

	/**
	 * Returns the styles slot of row. Through SlotHandle, each style can be
	 * obtained.
	 * 
	 * @return the handle to the style slot
	 * 
	 * @see SlotHandle
	 */

	public SlotHandle getStyles( )
	{
		return getSlot( IThemeModel.STYLES_SLOT );
	}

	/**
	 * Returns the style with the given name.
	 * 
	 * @param name
	 *            the style name
	 * @return the corresponding style
	 */

	public StyleHandle findStyle( String name )
	{
		Theme theme = (Theme) getElement( );
		StyleElement style = theme.findStyle( name );
		if ( style == null )
			return null;

		return (StyleHandle) style.getHandle( getModule( ) );
	}

	/**
	 * Makes the unique style name in the given theme. The return name is based
	 * on <code>name</code>.
	 * 
	 * @param name
	 *            the style name
	 * @return the new unique style name
	 */

	String makeUniqueStyleName( String name )
	{
		assert this != null;

		SlotHandle styles = getStyles( );
		Set set = new HashSet( );
		for ( int i = 0; i < styles.getCount( ); i++ )
		{
			StyleHandle style = (StyleHandle) styles.get( i );
			set.add( style.getName( ) );
		}

		// Add a numeric suffix that makes the name unique.

		int index = 0;
		String baseName = name;
		while ( set.contains( name ) )
		{
			name = baseName + ++index; //$NON-NLS-1$
		}
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.DesignElementHandle#getDisplayLabel(int)
	 */

	public String getDisplayLabel( int level )
	{

		String displayLabel = super.getDisplayLabel( level );

		Module rootModule = getModule( );
		if ( rootModule instanceof Library )
			displayLabel = StringUtil.buildQualifiedReference(
					( (Library) rootModule ).getNamespace( ), displayLabel );

		return displayLabel;

	}
}

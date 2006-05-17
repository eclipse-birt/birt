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

package org.eclipse.birt.report.model.api.validators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.ThemeHandle;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.StyleElement;
import org.eclipse.birt.report.model.elements.Theme;
import org.eclipse.birt.report.model.validators.AbstractElementValidator;

/**
 * Validates the ducplicat style name in the theme.
 * 
 * <h3>Rule</h3>
 * The rule is that one theme element doesn't allow duplicate style name to
 * appear in this element.
 * 
 * <h3>Applicability</h3>
 * This validator is only applied to <code>Theme</code>.
 * 
 */

public class ThemeStyleNameValidator extends AbstractElementValidator
{

	private static ThemeStyleNameValidator instance = new ThemeStyleNameValidator( );

	/**
	 * Returns the singleton validator instance.
	 * 
	 * @return the validator instance
	 */

	public static ThemeStyleNameValidator getInstance( )
	{
		return instance;
	}

	/**
	 * Validates whether the style with the given name can be added into the
	 * given theme element.
	 * 
	 * @param theme
	 *            the theme element
	 * @param styleName
	 *            name of the style to add
	 * @return error list, each of which is the instance of
	 *         <code>SemanticException</code>.
	 */

	public List validateForAddingStyle( ThemeHandle theme, String styleName )
	{
		List list = new ArrayList( );

		// Collect all groups in this element.

		StyleHandle style = theme.findStyle( styleName );
		if ( style != null )
			list.add( new NameException( theme.getElement( ), styleName,
					NameException.DESIGN_EXCEPTION_DUPLICATE ) );

		return list;
	}

	/**
	 * Validates whether the style can be renamed to the given name.
	 * 
	 * @param theme
	 *            the theme element
	 * @param style
	 *            the style to rename
	 * @param styleName
	 *            the new name of the style to add
	 * @return error list, each of which is the instance of
	 *         <code>SemanticException</code>.
	 */

	public List validateForRenamingStyle( ThemeHandle theme, StyleHandle style,
			String styleName )
	{
		if ( style.getName( ) == styleName
				|| ( styleName != null && styleName.equals( style.getName( ) ) ) )
			return Collections.EMPTY_LIST;

		List list = new ArrayList( );
		StyleHandle tmpStyle = theme.findStyle( styleName );
		if ( tmpStyle != null && style != tmpStyle )
		{
			list.add( new NameException( theme.getElement( ), styleName,
					NameException.DESIGN_EXCEPTION_DUPLICATE ) );
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.validators.AbstractElementValidator#validate(org.eclipse.birt.report.model.core.Module,
	 *      org.eclipse.birt.report.model.core.DesignElement)
	 */

	public List validate( Module module, DesignElement element )
	{
		if ( !( element instanceof Theme ) )
			return Collections.EMPTY_LIST;

		Theme theme = (Theme) element;
		ContainerSlot slot = theme.getSlot( Theme.STYLES_SLOT );

		List list = new ArrayList( );
		Set set = new HashSet( );

		for ( Iterator iter = slot.iterator( ); iter.hasNext( ); )
		{
			StyleElement style = (StyleElement) iter.next( );
			String styleName = style.getName( );

			if ( !set.contains( styleName ) )
				set.add( style.getName( ) );
			else
				list.add( new NameException( theme, styleName,
						NameException.DESIGN_EXCEPTION_DUPLICATE ) );
		}
		return list;
	}
}
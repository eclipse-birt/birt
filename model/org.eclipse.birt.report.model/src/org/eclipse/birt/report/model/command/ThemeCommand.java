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

package org.eclipse.birt.report.model.command;

import org.eclipse.birt.report.model.activity.AbstractElementCommand;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ThemeException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.Theme;

/**
 * Sets the theme of the report design.
 */

public class ThemeCommand extends AbstractElementCommand
{

	/**
	 * Constructor.
	 * 
	 * @param module
	 *            the module
	 * @param element
	 *            the module to set the theme
	 */

	public ThemeCommand( Module module, DesignElement element )
	{
		super( module, element );

		assert element instanceof Module;
	}

	/**
	 * Sets the theme of an element.
	 * 
	 * @param name
	 *            the name of the theme to set.
	 * @throws SemanticException
	 *             if the element can not have theme or the theme is not found.
	 */

	public void setTheme( String name ) throws SemanticException
	{
		name = StringUtil.trimString( name );

		Module currentModule = (Module) element;

		// Ensure that the theme exists

		Theme theme = null;
		Theme oldTheme = currentModule.getTheme( );
		
		if ( name != null )
		{
			theme = getModule( ).findTheme( name );

			// the theme in the element is the same with the new set value

			if ( theme == oldTheme )
				return;

			if ( theme == null )
				throw new ThemeException( element, name,
						ThemeException.DESIGN_EXCEPTION_NOT_FOUND );
		}
		else
		{
			// the new name is null and the theme in the element is un-set

			if ( oldTheme == null )
				return;
		}

		// adjust the back references for styles in the theme

		ThemeRecord themeRecord = new ThemeRecord( currentModule, theme );
		getModule( ).getActivityStack( ).execute( themeRecord );
	}

	/**
	 * Sets the theme of an element given the theme itself.
	 * 
	 * @param theme
	 *            the theme element to set.
	 * @throws SemanticException
	 *             if the element can not have theme or the theme is not found.
	 */

	public void setThemeElement( Theme theme ) throws SemanticException
	{
		// Make the change starting with the name. This will handle the
		// case where the application is trying to set a theme that is
		// not part of the design.

		String name = null;
		if ( theme != null )
			name = theme.getName( );
		setTheme( name );
	}
}
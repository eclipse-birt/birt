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
import org.eclipse.birt.report.model.api.command.StyleException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.StyleElement;
import org.eclipse.birt.report.model.core.StyledElement;
import org.eclipse.birt.report.model.elements.ReportDesign;

/**
 * Sets the style property of an element.
 *  
 */

public class StyleCommand extends AbstractElementCommand
{

	/**
	 * Constructor.
	 * 
	 * @param design
	 *            the report design
	 * @param obj
	 *            The element to modify.
	 */

	public StyleCommand( ReportDesign design, DesignElement obj )
	{
		super( design, obj );
	}

	/**
	 * Sets the style of an element.
	 * 
	 * @param name
	 *            the name of the style to set.
	 * @throws StyleException
	 *             if the element can not have style or the style is not found.
	 */

	public void setStyle( String name ) throws StyleException
	{
		name = StringUtil.trimString( name );

		// Ensure that the element can have a style.

		if ( !element.getDefn( ).hasStyle( ) )
			throw new StyleException( element, name,
					StyleException.DESIGN_EXCEPTION_FORBIDDEN );
		StyledElement obj = (StyledElement) element;

		// Ensure that the style exists.

		StyleElement style = null;
		StyleElement oldValue = obj.getStyle( );
		if ( name != null )
		{
			style = getRootElement( ).findStyle( name );
			if ( style == null )
			{
				if ( !name.equals( obj.getStyleName( ) ) )
					throw new StyleException( element, name,
							StyleException.DESIGN_EXCEPTION_NOT_FOUND );
				return;
			}

			// the style in the element is the same with the new set value

			if ( style == oldValue )
				return;
		}
		else
		{
			// the new name is null and the style in the element is un-set

			if ( obj.getStyleName( ) == null )
				return;

		}

		// Make the change.

		StyleRecord record = new StyleRecord( obj, style );
		getActivityStack( ).execute( record );
	}

	/**
	 * Sets the style of an element given the style itself.
	 * 
	 * @param style
	 *            the style element to set.
	 * @throws StyleException
	 *             if the element can not have style or the style is not found.
	 */

	public void setStyleElement( DesignElement style ) throws StyleException
	{
		// Make the change starting with the name. This will handle the
		// case where the application is trying to set a style that is
		// not part of the design.

		String name = null;
		if ( style != null )
			name = style.getName( );
		setStyle( name );
	}
}
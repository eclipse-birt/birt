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

import org.eclipse.birt.report.model.command.UserPropertyCommand;
import org.eclipse.birt.report.model.command.UserPropertyException;
import org.eclipse.birt.report.model.core.UserPropertyDefn;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.metadata.PropertyType;
import org.eclipse.birt.report.model.metadata.PropertyValueException;

/**
 * Represents a top-level user-defined property of an element.
 * 
 * @see org.eclipse.birt.report.model.core.UserPropertyDefn
 */

public class UserPropertyDefnHandle extends PropertyHandle
{

	/**
	 * Constructs a handle for the user-defined property with the given element
	 * handle and the user-defined property.
	 * 
	 * @param element
	 *            a handle to a report element
	 * @param prop
	 *            The definition of the user-defined property.
	 */

	public UserPropertyDefnHandle( DesignElementHandle element,
			UserPropertyDefn prop )
	{
		super( element, prop );
	}

	/**
	 * Constructs a handle for the user-defined property with the given element
	 * handle and the name of the user-defined property.
	 * 
	 * @param element
	 *            a handle to a report element
	 * @param propName
	 *            The name of the user-defined property.
	 */

	public UserPropertyDefnHandle( DesignElementHandle element, String propName )
	{
		super( element, propName );
	}

	/**
	 * Returns the name of the user-defined property.
	 * 
	 * @return the name of the user-defined property
	 */

	public String getName( )
	{
		return getDefn( ).getName( );
	}

	/**
	 * Returns the type of the user-defined property.
	 * 
	 * @return the type of the user-defined property
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyType
	 */

	public PropertyType getType( )
	{
		return getDefn( ).getType( );
	}

	/**
	 * Returns the display name of the user-defined property.
	 * 
	 * @return the display name of the user-defined property
	 */

	public String getDisplayName( )
	{
		return getDefn( ).getDisplayName( );
	}

	/**
	 * Sets the definition for the user-defined property.
	 * 
	 * @param prop
	 *            the new definition of the user-defined property
	 * @throws UserPropertyException
	 *             if the property is not found, is not a user property, or is
	 *             not defined on this element, or the user property definition
	 *             is inconsistent.
	 * @throws PropertyValueException,
	 *             if the type changes, the value becomes invalid. 
	 */

	public void setUserPropertyDefn( UserPropertyDefn prop )
			throws UserPropertyException, PropertyValueException
	{
		ReportDesign design = elementHandle.getDesign( );
		UserPropertyCommand cmd = new UserPropertyCommand( design, getElement( ) );
		cmd.setPropertyDefn( (UserPropertyDefn) getDefn( ), prop );
	}

	/**
	 * Returns the copy of the property definition for this user-defined
	 * property.
	 * 
	 * @return the copy of the property definition
	 */

	public UserPropertyDefn getCopy( )
	{
		UserPropertyDefn prop = (UserPropertyDefn) ( (UserPropertyDefn) getDefn( ) )
				.copy( );
		return prop;
	}
}
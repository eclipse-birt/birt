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

package org.eclipse.birt.report.designer.data.ui.aggregation;

import org.eclipse.birt.report.model.api.metadata.IArgumentInfo;
import org.eclipse.birt.report.model.api.util.StringUtil;

/**
 * Represents the definition of argument. The argument definition includes the
 * data type, internal name, and display name.
 */

public class ArgumentInfo implements IArgumentInfo
{

	/**
	 * The script type of this argument.
	 */

	private String type;

	/**
	 * The internal (non-localized) name for the argument. This name is used in
	 * code.
	 */

	protected String name = null;

	/**
	 * The resource key for the argument display name.
	 */

	protected String displayNameKey = null;

	/**
	 * Returns the internal name for the argument.
	 * 
	 * @return the internal (non-localized) name for the argument
	 */

	public String getName( )
	{
		return name;
	}

	/**
	 * Returns the display name for the property if the resource key of display
	 * name is defined. Otherwise, return empty string.
	 * 
	 * @return the user-visible, localized display name for the property
	 */

	public String getDisplayName( )
	{
		return displayNameKey != null ? displayNameKey : ""; //$NON-NLS-1$
	}

	/**
	 * Sets the internal name of the property.
	 * 
	 * @param theName
	 *            the internal property name
	 */

	void setName( String theName )
	{
		name = theName;
	}

	/**
	 * Returns the resource key for the display name.
	 * 
	 * @return The display name message ID.
	 */

	public String getDisplayNameKey( )
	{
		return displayNameKey;
	}

	/**
	 * Sets the message ID for the display name.
	 * 
	 * @param id
	 *            message ID for the display name
	 */

	void setDisplayNameKey( String id )
	{
		displayNameKey = id;
	}

	/**
	 * Returns the script type of this argument.
	 * 
	 * @return the script type to set
	 */

	public String getType( )
	{
		return type;
	}

	/**
	 * Sets the script type of this argument.
	 * 
	 * @param type
	 *            the script type to set
	 */

	void setType( String type )
	{
		this.type = type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */

	public String toString( )
	{
		if ( !StringUtil.isBlank( getName( ) ) )
			return getName( );
		return super.toString( );
	}
	
}
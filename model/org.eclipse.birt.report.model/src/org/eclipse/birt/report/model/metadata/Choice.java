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

package org.eclipse.birt.report.model.metadata;

import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.i18n.ModelMessages;

import com.ibm.icu.util.ULocale;

/**
 * Describes the choices for a property. The internal name of a choice property
 * is a string. The string maps to a display name shown to the user, and an XML
 * name used in the xml design file. The display name is localized, the XML name
 * is not.
 */

public class Choice implements Cloneable, IChoice, Comparable<Object>
{

	/**
	 * Name of the choice name property.
	 */

	public final static String NAME_PROP = "name"; //$NON-NLS-1$

	/**
	 * Name of the display name id property.
	 */

	public final static String DISPLAY_NAME_ID_PROP = "displayNameID"; //$NON-NLS-1$

	/**
	 * The resource key for the choice's display name.
	 */

	protected String displayNameKey;

	/**
	 * The choice name to appear in the xml design file.
	 */

	protected String name;

	/**
	 * Constructs a new Choice by the given name and id.
	 * 
	 * @param name
	 *            the choice name
	 * @param id
	 *            the message ID for the display name
	 */

	public Choice( String name, String id )
	{
		this.name = name;
		displayNameKey = id;
	}

	/**
	 * Default constructor.
	 * 
	 */

	protected Choice( )
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */

	protected Object clone( ) throws CloneNotSupportedException
	{
		return super.clone( );
	}

	/**
	 * Returns the localized display name for the choice.
	 * 
	 * @return the localized display name for the choice.
	 */

	public String getDisplayName( )
	{
		return ModelMessages.getMessage( displayNameKey );
	}

	/**
	 * Returns the localized display name for the choice.
	 * 
	 * @return the localized display name for the choice.
	 */

	public String getDisplayName( ULocale locale )
	{
		return ModelMessages.getMessage( displayNameKey, locale );
	}
	
	/**
	 * Returns the display name resource key for the choice.
	 * 
	 * @return the display name resource key
	 */

	public String getDisplayNameKey( )
	{
		return displayNameKey;
	}

	/**
	 * Returns the choice name that appears in the XML design file.
	 * 
	 * @return the choice name used in the XML design file
	 */

	public String getName( )
	{
		return name;
	}

	/**
	 * Sets the resource key for display name.
	 * 
	 * @param theDisplayNameKey
	 *            the resource key for display name
	 */

	public void setDisplayNameKey( String theDisplayNameKey )
	{
		this.displayNameKey = theDisplayNameKey;
	}

	/**
	 * Sets the choice name.
	 * 
	 * @param theName
	 *            the name to set
	 */

	public void setName( String theName )
	{
		this.name = theName;
	}

	/**
	 * Returns the value of the choice. The returned value equals to the
	 * internal name of the system choice.
	 * 
	 * @return the value of the choice
	 */

	public Object getValue( )
	{
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */

	public int compareTo( Object o )
	{
		assert name != null;

		Choice choice = (Choice) o;
		return name.compareTo( choice.getName( ) );
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.metadata.IChoice#copy()
	 */

	public IChoice copy( )
	{
		try
		{
			return (IChoice) clone( );
		}
		catch ( CloneNotSupportedException e )
		{
			assert false;
			return null;
		}
	}
}
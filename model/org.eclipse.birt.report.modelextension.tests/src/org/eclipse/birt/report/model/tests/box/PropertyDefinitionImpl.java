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

package org.eclipse.birt.report.model.tests.box;

import java.util.List;

import org.eclipse.birt.report.model.api.extension.IPropertyDefinition;
import org.eclipse.birt.report.model.api.extension.PropertyDefinition;
import org.eclipse.birt.report.model.metadata.PropertyType;

/**
 * Implements <code>IPropertyDefinition</code> for testing
 */

public class PropertyDefinitionImpl extends PropertyDefinition
		implements
			IPropertyDefinition
{

	String name = null;

	String displayNameID = null;

	int type = -1;

	boolean isList = false;

	List choices = null;

	List members = null;

	Object defaultValue = null;

	String groupNameID = null;

	boolean canInherit = true;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.model.extension.IPropertyDefn#getGroupName()
	 */
	public String getGroupNameID( )
	{
		return groupNameID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.model.extension.IPropertyDefn#getName()
	 */
	public String getName( )
	{
		return name;
	}

	void setName( String name )
	{
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.model.extension.IPropertyDefn#getDisplayName()
	 */
	public String getDisplayName( )
	{
		return displayNameID;
	}

	public void setDisplayNameID( String displayNameID )
	{
		this.displayNameID = displayNameID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.model.extension.IPropertyDefn#getType()
	 */
	public int getType( )
	{
		return type;
	}

	void setType( int type )
	{
		this.type = type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.model.extension.IPropertyDefn#isList()
	 */
	public boolean isList( )
	{
		return isList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.model.extension.IPropertyDefn#getChoices()
	 */
	public List getChoices( )
	{
		if ( type != PropertyType.CHOICE_TYPE )
			return null;
		return choices;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.model.extension.IPropertyDefn#getMembers()
	 */
	public List getMembers( )
	{
		if ( type != PropertyType.STRUCT_TYPE )
			return null;
		return members;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.model.extension.IPropertyDefn#getDefaultValue()
	 */
	public Object getDefaultValue( )
	{
		return defaultValue;
	}

	/**
	 * @param groupNameID
	 *            The group to set.
	 */
	public void setGroupNameID( String groupNameID )
	{
		this.groupNameID = groupNameID;
	}

	/**
	 * @param isList
	 *            The isList to set.
	 */
	public void setIsList( boolean isList )
	{
		this.isList = isList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.model.extension.IPropertyDefn#canInherit()
	 */
	public boolean canInherit( )
	{
		return canInherit;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.IPropertyDefinition#getDisplayNameID()
	 */
	public String getDisplayNameID( )
	{
		return displayNameID;
	}
}
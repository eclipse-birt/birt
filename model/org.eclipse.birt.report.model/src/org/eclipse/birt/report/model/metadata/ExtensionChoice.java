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

import org.eclipse.birt.report.model.extension.IChoiceDefinition;

/**
 * Represents the choices defined by the extension element. There are two kinds
 * of choices:
 * 
 * <ul>
 * <li>The choice defined for extension property
 * <li>The choice defined for dynamic property
 * </ul>
 */

public class ExtensionChoice extends Choice
{

	/**
	 * The choice from dynamic property.
	 */
	
	IChoiceDefinition extChoice = null;

	/**
	 * The value of this choice.
	 */
	String value = null;

	/**
	 * Constructs an empty choice
	 */

	public ExtensionChoice( )
	{
	}

	/**
	 * Constructs the extension choice defined by extension elements.
	 * 
	 * @param extChoiceDefn
	 *            the extension choice definition based
	 */

	public ExtensionChoice( IChoiceDefinition extChoiceDefn )
	{
		assert extChoiceDefn != null;
		this.extChoice = extChoiceDefn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.Choice#getDisplayName()
	 */

	public String getDisplayName( )
	{
		if ( extChoice != null )
			return extChoice.getDisplayName( );

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.Choice#getDisplayNameKey()
	 */

	public String getDisplayNameKey( )
	{
		return displayNameKey;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.Choice#getName()
	 */

	public String getName( )
	{
		if ( extChoice != null )
			return extChoice.getName( );

		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.Choice#getValue()
	 */

	public Object getValue( )
	{
		if ( extChoice != null )
			return extChoice.getValue( );

		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.Choice#setDisplayNameKey(java.lang.String)
	 */

	public void setDisplayNameKey( String theDisplayNameKey )
	{
		this.displayNameKey = theDisplayNameKey;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.Choice#setName(java.lang.String)
	 */

	public void setName( String theName )
	{
		this.name = theName;
	}

	/**
	 * Sets the value for this choice.
	 * 
	 * @param value the value to set
	 */
	
	public void setValue( String value )
	{
		this.value = value;
	}
}
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

package org.eclipse.birt.report.model.api.elements.structures;

import org.eclipse.birt.report.model.core.Structure;

public abstract class FormatValue extends Structure
{

	/**
	 * Name of the config variable name member.
	 */

	public static final String CATEGORY_MEMBER = "category"; //$NON-NLS-1$

	/**
	 * Name of the config variable value member.
	 */

	public static final String PATTERN_MEMBER = "pattern"; //$NON-NLS-1$

	/**
	 * The config variable name.
	 */

	private String name = null;

	/**
	 * The config variable value.
	 */

	private String value = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#getIntrinsicProperty(java.lang.String)
	 */

	protected Object getIntrinsicProperty( String memberName )
	{
		if ( CATEGORY_MEMBER.equals( memberName ) )
			return name;
		if ( PATTERN_MEMBER.equals( memberName ) )
			return value;

		assert false;
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#setIntrinsicProperty(java.lang.String,
	 *      java.lang.Object)
	 */

	protected void setIntrinsicProperty( String memberName, Object value )
	{
		if ( CATEGORY_MEMBER.equals( memberName ) )
			name = (String) value;
		else if ( PATTERN_MEMBER.equals( memberName ) )
			this.value = (String) value;
		else
			assert false;
	}

	/**
	 * Returns the variable name.
	 * 
	 * @return the variable name
	 */

	public String getCategory( )
	{
		return (String) getProperty( null, CATEGORY_MEMBER );
	}

	/**
	 * Sets the variable name.
	 * 
	 * @param name
	 *            the name to set
	 */

	public void setCategory( String name )
	{
		setProperty( CATEGORY_MEMBER, name );
	}

	/**
	 * Returns the variable value.
	 * 
	 * @return the variable value
	 */

	public String getPattern( )
	{
		return (String) getProperty( null, PATTERN_MEMBER );
	}

	/**
	 * Sets the variable value.
	 * 
	 * @param value
	 *            the value to set
	 */

	public void setPattern( String value )
	{
		setProperty( PATTERN_MEMBER, value );
	}

}

/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.taglib.component;

import java.io.Serializable;

/**
 * Specifies the report parameter.
 * <p>
 * There are the following parameter attributes:
 * <ol>
 * <li>name</li>
 * <li>pattern</li>
 * <li>value</li>
 * <li>displayText</li>
 * </ol>
 */
public class ParameterField implements Serializable
{

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8597978146893976259L;

	private String name;
	private String pattern;
	private Object value;
	private String displayText;
	private boolean isLocale;

	/**
	 * validate parameter
	 * 
	 * @return
	 */
	public boolean validate( )
	{
		return name != null && name.length( ) > 0 ? true : false;
	}

	/**
	 * @return the name
	 */
	public String getName( )
	{
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName( String name )
	{
		this.name = name;
	}

	/**
	 * @return the pattern
	 */
	public String getPattern( )
	{
		return pattern;
	}

	/**
	 * @param pattern
	 *            the pattern to set
	 */
	public void setPattern( String pattern )
	{
		this.pattern = pattern;
	}

	/**
	 * @return the value
	 */
	public Object getValue( )
	{
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue( Object value )
	{
		this.value = value;
	}

	/**
	 * @return the displayText
	 */
	public String getDisplayText( )
	{
		return displayText;
	}

	/**
	 * @param displayText
	 *            the displayText to set
	 */
	public void setDisplayText( String displayText )
	{
		this.displayText = displayText;
	}

	/**
	 * @return the isLocale
	 */
	public boolean isLocale( )
	{
		return isLocale;
	}

	/**
	 * @param isLocale
	 *            the isLocale to set
	 */
	public void setLocale( boolean isLocale )
	{
		this.isLocale = isLocale;
	}

}

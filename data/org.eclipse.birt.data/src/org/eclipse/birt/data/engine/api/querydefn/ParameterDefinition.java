/*
 *************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */ 
package org.eclipse.birt.data.engine.api.querydefn;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.data.engine.api.IParameterDefinition;


/**
 * Default implementation of the IParameterDefn interface. <p>
 * Base class for defining input and output parameters. A parameter has an ID (either by name or by 
 * 1-based index), and a data type
 */

abstract public class ParameterDefinition implements IParameterDefinition 
{
	protected int posn = -1;
	protected String name;
	protected int type = DataType.UNKNOWN_TYPE;

	ParameterDefinition()
	{
	}
	
	ParameterDefinition( String name, int type )
	{
	    this.name = name;
	    this.type = type;
	}
	
	ParameterDefinition( int position, int type )
	{
	    this.posn = position;
	    this.type = type;
	}
	
	/**
	 * Returns the parameter name.
	 * 
	 * @return the name of the parameter
	 */
	
	public String getName( )
	{
		return name;
	}
	
	/**
	 * Sets the name of the parameter
	 */
	public void setName( String name )
	{
	    this.name = name;
	}
	
	/**
	 * Returns the parameter position.
	 * 
	 * @return the parameter position
	 */
	
	public int getPosition( )
	{
		return posn;
	}
	
	/**
	 * Sets the parameter position
	 */
	public void setPosition( int posn )
	{
	    this.posn = posn;
	}
	
	/**
	 * Returns the parameter data type. See the DataType class for return value constants.
	 * 
	 * @return the parameter data type
	 */
	
	public int getType( )
	{
		return type;
	}
	
	/**
	 * Sets the parameter type
	 */
	public void setType( int type )
	{
	    this.type = type;
	}
}

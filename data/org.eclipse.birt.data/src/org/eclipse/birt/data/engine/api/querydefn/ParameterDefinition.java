/*
 *************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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

public class ParameterDefinition implements IParameterDefinition 
{
	private int posn = -1;
	private String name;
	private int type = DataType.UNKNOWN_TYPE;
	private boolean isInputMode = false;
	private boolean isOutputMode = false;
	private boolean isInputOptional = true;
	private String defaultInputValue;
	private boolean isNullable = true;

	public ParameterDefinition()
	{
	}
	
	public ParameterDefinition( String name, int type )
	{
	    this.name = name;
	    this.type = type;
	}
	
	public ParameterDefinition( int position, int type )
	{
	    this.posn = position;
	    this.type = type;
	}
	
	public ParameterDefinition( String name, int type, boolean isInput, boolean isOutput )
	{
	    this.name = name;
	    this.type = type;
	    isInputMode = isInput;
	    isOutputMode = isOutput;
	}
	
	public ParameterDefinition( int position, int type, boolean isInput, boolean isOutput )
	{
	    this.posn = position;
	    this.type = type;
	    isInputMode = isInput;
	    isOutputMode = isOutput;
	}
	
    /* (non-Javadoc)
     * @see org.eclipse.birt.data.engine.api.IParameterDefinition#getName()
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
	
    /* (non-Javadoc)
     * @see org.eclipse.birt.data.engine.api.IParameterDefinition#getPosition()
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
	
    /* (non-Javadoc)
     * @see org.eclipse.birt.data.engine.api.IParameterDefinition#getType()
     */	
	public int getType( )
	{
		return type;
	}
	
	/**
	 * Sets the parameter data type
	 */
	public void setType( int type )
	{
	    this.type = type;
	}	
	
    /* (non-Javadoc)
     * @see org.eclipse.birt.data.engine.api.IParameterDefinition#isInputMode()
     */	
	public boolean isInputMode()
	{
	    return isInputMode;
	}
	
	/**
	 * Sets the input mode of the parameter.
	 * @param isInput	true if the parameter is of input mode,
	 * 					false otherwise.
	 */
	public void setInputMode( boolean isInput )
	{
	    this.isInputMode = isInput;
	}
	
    /* (non-Javadoc)
     * @see org.eclipse.birt.data.engine.api.IParameterDefinition#isOutputMode()
     */	
	public boolean isOutputMode()
	{
	    return isOutputMode;
	}
	
	/**
	 * Sets the output mode of the parameter.
	 * @param isOutput	true if the parameter is of output mode,
	 * 					false otherwise.
	 */
	public void setOutputMode( boolean isOutput )
	{
	    this.isOutputMode = isOutput;
	}
    
    /* (non-Javadoc)
     * @see org.eclipse.birt.data.engine.api.IParameterDefinition#isInputOptional()
     */	
    public boolean isInputOptional()
    {
        return isInputMode() ? isInputOptional : true;
    }
    
    /**
     * Sets whether the parameter's input value is optional.
	 * Applies to the parameter only if it is of input mode.
     * @param isOptional	true if the parameter input value is optional,
     * 						false otherwise.
     */
    public void setInputOptional( boolean isOptional )
    {
        if ( isInputMode() )
            isInputOptional = isOptional;
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.birt.data.engine.api.IParameterDefinition#getDefaultInputValue()
     */	
    public String getDefaultInputValue()
    {
        return isInputMode() ? defaultInputValue : null;
    }
    
    /**
     * Sets the parameter's default input value.
	 * Applies to the parameter only if it is of input mode.
     * @param defaultValue	Default input value.
     */
    public void setDefaultInputValue( String defaultValue )
    {
        if ( isInputMode() )
            defaultInputValue = defaultValue;
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.birt.data.engine.api.IParameterDefinition#isNullable()
     */	
	public boolean isNullable()
	{
	    return isNullable;
	}
	
	/**
	 * Sets whether the parameter's value can be null.
	 * @param isNullable	true if the parameter value can be null,
	 * 						false otherwise.
	 */
	public void setNullable( boolean isNullable )
	{
	    this.isNullable = isNullable;
	}
}

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

import org.eclipse.birt.data.engine.api.IBaseDataSourceDesign;


/**
 * Default implementation of the IBaseDataSourceDesign interface. <p>
 * Describes the static design of any data source (connection)
 * to be used by the Data Engine.
 * Each subclass defines a specific type of data source. 
 */
public class BaseDataSourceDesign implements IBaseDataSourceDesign
{
    private String 	name;
    private String 	beforeOpenScript;
    private String 	afterOpenScript;
    private String 	beforeCloseScript;
    private String 	afterCloseScript;

	/**
	 * Constructor: Creates a data source with specified name
	 */
	public BaseDataSourceDesign( String name )
	{
	    this.name = name;
	}
	
	/** 
	 * Gets the name of this data source
	 */	
	public String getName( )
	{
		return name;
	}
	
	/** 
	 * Gets the BeforeOpen Script of the data source
	 */	
	public String getBeforeOpenScript( )
	{
		return beforeOpenScript;
	}
	
	/**
	 * Specifies the BeforeOpen script of the data source.
	 * @param script	beforeOpen script
	 */
	public void setBeforeOpenScript( String script )
	{
	    beforeOpenScript = script;
	}
	
	/**
	 * Gets the AfterOpen script of the data source
	 */	
	public String getAfterOpenScript( )
	{
		return afterOpenScript;
	}
	
	/**
	 * Specifies the AfterOpen script of the data source.
	 * @param script	afterOpen script
	 */
	public void setAfterOpenScript( String script )
	{
	    afterOpenScript = script;
	}
	
	/**
	 * Gets the BeforeClose script of the data source
	 */
	public String getBeforeCloseScript( )
	{
		return beforeCloseScript;
	}
	
	/**
	 * Specifies the BeforeClose script of the data source.
	 * @param script	beforeClose script
	 */
	public void setBeforeCloseScript( String script )
	{
	    beforeCloseScript = script;
	}
	
	/**
	 * Gets the AfterClose script of the data source
	 */
	public String getAfterCloseScript( )
	{
		return afterCloseScript;
	}
	
	/**
	 * Specifies the AfterClose script of the data source.
	 * @param script	afterClose script
	 */
	public void setAfterCloseScript( String script )
	{
	    afterCloseScript = script;
	}

}

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

import java.util.HashMap;
import java.util.Map;

/**
 * Provide the way to do some setting about the module.
 */

public class ModuleOption implements IModuleOption
{

	/**
	 * Maps to store the key/value pairs.
	 */

	protected HashMap options = new HashMap( );

	/**
	 * Default constructor.
	 */

	public ModuleOption( )
	{

	}

	/**
	 * Constructs the module options with mapping of the option settings.
	 * 
	 * @param options
	 *            the option settings to add
	 */

	public ModuleOption( Map options )
	{
		if ( options != null && !options.isEmpty( ) )
			options.putAll( options );
	}

	/**
	 * Determines whether to do some semantic checks when opening a module.
	 * 
	 * @return true if user wants to do the checks, otherwise false
	 */

	public boolean useSemanticCheck( )
	{
		Object semanticCheck = options.get( PARSER_SEMANTIC_CHECK_KEY );
		if ( semanticCheck != null )
			return ( (Boolean) semanticCheck ).booleanValue( );
		return true;
	}

	/**
	 * Sets the semantic check control status. True if user wants to do the
	 * semantic checks when opening a module, otherwise false.
	 * 
	 * @param useSemanticCheck
	 *            the control status
	 */

	public void setSemanticCheck( boolean useSemanticCheck )
	{
		options.put( PARSER_SEMANTIC_CHECK_KEY, Boolean
				.valueOf( useSemanticCheck ) );
	}

	/**
	 * Gets the resource folder.
	 * 
	 * @return the resource folder
	 */

	public String getResourceFolder( )
	{
		return (String) options.get( RESOURCE_FOLDER_KEY );
	}

	/**
	 * Sets the resource folder
	 * 
	 * @param resourceFolder
	 *            the resource folder to set
	 */

	public void setResourceFolder( String resourceFolder )
	{
		if ( resourceFolder != null )
			options.put( RESOURCE_FOLDER_KEY, resourceFolder );
	}

	/**
	 * Sets an option of this setting.
	 * 
	 * @param key
	 *            the option key
	 * @param value
	 *            the option value
	 */

	public void setProperty( String key, Object value )
	{
		options.put( key, value );
	}

	/**
	 * Gets the value in this setting.
	 * 
	 * @param key
	 *            the key to search
	 * @return the value in this setting if found, otherwise <code>null</code>
	 */

	public Object getProperty( String key )
	{
		return options.get( key );
	}

}

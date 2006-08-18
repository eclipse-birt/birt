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

/**
 * Provide the way to do some setting about the module.
 */

public class ModuleOption implements Cloneable
{

	/**
	 * Key to control whether to call semantic-check when opening a module. True
	 * if user wants to do some semantic checks about the module when opening
	 * it; otherwise false.
	 */

	public final static String PARSER_SEMANTIC_CHECK_KEY = "semanticCheck"; //$NON-NLS-1$

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
}

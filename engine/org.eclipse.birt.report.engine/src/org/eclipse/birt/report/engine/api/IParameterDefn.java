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
package org.eclipse.birt.report.engine.api;

import java.util.Locale;


/**
 * Created on Dec 15, 2004
 * 
 * Base interface for a BIRT report parameter
 */


public interface IParameterDefn extends IParameterDefnBase
{
	public static final int TYPE_ANY = 0;
	public static final int TYPE_STRING = 1;
	public static final int TYPE_FLOAT = 2;
	public static final int TYPE_DECIMAL = 3;
	public static final int TYPE_DATE_TIME = 4;
	public static final int TYPE_BOOLEAN=5;
	
	
	/**
	 * Returns the parameter data type. See the ColumnDefn class
	 * for the valid data type constants.
	 * 
	 * @return the parameter data type
	 */
	
	int getValueType( );
	
	/**
	 * @return help text for the parameter
	 *  
	 */
	public String getHelpText( );
	
	/**
	 * get the help text of the locale.
	 * @param locale locale
	 * @return help text
	 */
	public String getHelpText(Locale locale);

	
	/**
	 * @return whether the parameter is a hidden parameter
	 */
	public boolean isHidden( );
}
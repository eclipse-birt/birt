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
import java.util.Map;

/**
 * Captures properties shared by all types of parameters and parameter group,
 * i.e., name, display name, help text and custom-defined properties.
 * 
 * Note that even though display name and help text are locale-sensitive, the
 * API does not take a locale. The parameter returned to the user was obtained
 * from a report handle, which has already had a locale.
 */

public interface IParameterDefnBase
{

	public static final int SCALAR_PARAMETER = 0;
	public static final int FILTER_PARAMETER = 1;
	public static final int LIST_PARAMETER = 2;
	public static final int TABLE_PARAMETER = 3;
	public static final int PARAMETER_GROUP = 4;

	/**
	 * @return the parameter type, i.e., scalar, filter, list, table or
	 *         parameter group
	 */
	public int getType( );

	/*
	 * get the name of a parameter definition
	 */
	public String getName( );

	/**
	 * @return display name under the default locale
	 */
	public String geDisplayName( );
	
	/**
	 * get the display name of the locale
	 * @param locale locale
	 * @return display name
	 */
	public String getDisplayName (Locale locale);

	/**
	 * @return an ordered collection of custom property name ane value pairs
	 *  
	 */
	public Map getCustomProperties( );

	/**
	 * @return the value for a custom property
	 */
	public String getCustomPropertyValue( String name );
}
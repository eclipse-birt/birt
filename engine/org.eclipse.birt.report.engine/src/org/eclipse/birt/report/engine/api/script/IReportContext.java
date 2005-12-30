/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.api.script;

import java.util.Locale;
import java.util.Map;
import java.io.Serializable;

/**
 * An interface used to share information between the event methods in
 * scripting. Gives access to report parameters and configuration values. Also
 * provides a way for the report developer to register and retrieve custom
 * properties.
 */
public interface IReportContext
{

	/**
	 * 
	 * @param name
	 * @return
	 */
	Object getParameterValue( String name );

	/**
	 * 
	 * @param name
	 * @param value
	 */
	void setParameterValue( String name, Object value );

	/**
	 * 
	 * @param varName
	 * @return
	 */
	Object getConfigVariableValue( String varName );

	/**
	 * 
	 * @return
	 */
	Locale getLocale( );

	/**
	 * 
	 * @return
	 */
	String getOutputFormat( );

	/**
	 * Get the application context
	 */
	Map getAppContext( );

	/**
	 * Get the http servlet request object
	 * 
	 */
	Object getHttpServletRequest( );

	/**
	 * Add the object to runtime scope. This object can only be retrieved in the
	 * same phase, i.e. it is not persisted between generation and presentation.
	 */
	void setGlobalVariable( String name, Object obj );

	/**
	 * Remove an object from runtime scope.
	 */
	void deleteGlobalVariable( String name );

	/**
	 * Retireve an object from runtime scope.
	 */
	Object getGlobalVariable( String name );

	/**
	 * Add the object to report document scope. This object can be retrieved
	 * later. It is persisted between phases, i.e. between generation and
	 * presentation.
	 */
	void setPersistentGlobalVariable( String name, Serializable obj );

	/**
	 * Remove an object from report document scope.
	 */
	void deletePersistentGlobalVariable( String name );

	/**
	 * Retireve an object from report document scope.
	 */
	Object getPersistentGlobalVariable( String name );

	/**
	 * Finds user-defined messages for the current thread's locale.
	 */
	String getMessage( String key );

	/**
	 * Finds user-defined messages for the given locale.
	 */
	String getMessage( String key, Locale locale );

	/**
	 * Finds user-defined messages for the current thread's locale
	 */
	String getMessage( String key, Object[] params );

	/**
	 * Finds user-defined messages for the given locale using parameters
	 */
	String getMessage( String key, Locale locale, Object[] params );

}

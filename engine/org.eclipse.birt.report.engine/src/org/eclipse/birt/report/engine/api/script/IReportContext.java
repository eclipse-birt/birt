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
	 * Get the report parameter map
	 */
	Map getParams( );

	/**
	 * Get the config map
	 */
	Map getConfig( );

	/** 
	 * Get the application context
	 */
	Map getAppContext( );

	/**
	 * Add the object to runtime scope. This object can only be retrieved in the
	 * same phase, i.e. it is not persisted between generation and presentation.
	 */
	void addToTask( String name, Object obj );

	/**
	 * Remove an object from runtime scope.
	 */
	void removeFromTask( String name );

	/**
	 * Retireve an object from runtime scope.
	 */
	Object getFromTask( String name );

	/**
	 * Add the object to report document scope. This object can be retrieved
	 * later. It is persisted between phases, i.e. between generation and
	 * presentation.
	 */
	void addToDocument( String name, Serializable obj );

	/**
	 * Remove an object from report document scope.
	 */
	void removeFromDocument( String name );

	/**
	 * Retireve an object from report document scope.
	 */
	Object getFromDocument( String name );

	/**
	 * Return a map of the runtime registered objects
	 * 
	 */
	Map getTransientObjects( );

	/**
	 * Return a map of the persistant registered objects
	 * 
	 */
	Map getPersistantObjects( );

}

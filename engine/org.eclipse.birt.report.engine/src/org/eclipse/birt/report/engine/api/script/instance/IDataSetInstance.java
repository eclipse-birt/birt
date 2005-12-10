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
package org.eclipse.birt.report.engine.api.script.instance;

public interface IDataSetInstance
{
	/**
	 * Gets the name of this data set
	 */
	String getName( );

	/**
	 * @return The runtime data source associated with this data set
	 */
	IDataSourceInstance getDataSource( );

	/**
	 * Gets the unique id that identifies the type of the data set, assigned by
	 * the extension providing the implementation of this data set.
	 * 
	 * @return The id fo the type of data set type as referenced by an ODA
	 *         driver. Null if none is defined.
	 */
	String getExtensionID( );

	/**
	 * Gets the query text of the data set.
	 * 
	 * @return The static query text for execution.
	 */
	String getQueryText( );

	/**
	 * Sets the query text of the data set.
	 * 
	 * @return The static query text for execution.
	 */
	void setQueryText( String queryText );

}

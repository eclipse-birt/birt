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

package org.eclipse.birt.data.engine.executor.cache;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.odaconsumer.ResultSet;
import org.eclipse.birt.data.engine.odi.ICustomDataSet;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 * Adapt Oda and Odi interface to a single class, which will provide a uniform
 * method to retrieve data.
 */
class OdiAdapter
{
	// from Oda
	private ResultSet resultSet;

	// from odi
	private ICustomDataSet customDataSet;
	
	// from parent query in sub query
	private ResultSetCache resultSetCache;

	/**
	 * Construction
	 * 
	 * @param resultSet
	 */
	OdiAdapter( ResultSet resultSet )
	{
		assert resultSet != null;
		this.resultSet = resultSet;
	}

	/**
	 * Construction
	 * 
	 * @param customDataSet
	 */
	OdiAdapter( ICustomDataSet customDataSet )
	{
		assert customDataSet != null;
		this.customDataSet = customDataSet;
	}

	/**
	 * Construction
	 * 
	 * @param customDataSet
	 */
	OdiAdapter( ResultSetCache resultSetCache )
	{
		assert resultSetCache != null;
		this.resultSetCache = resultSetCache;
	}

	/**
	 * Fetch data from Oda or Odi
	 * 
	 * @return IResultObject
	 * @throws DataException
	 */
	IResultObject fetch( ) throws DataException
	{
		if ( resultSet != null )
			return resultSet.fetch( );
		else if ( customDataSet != null )
			return customDataSet.fetch( );
		else
			return resultSetCache.fetch( );
	
	}

}
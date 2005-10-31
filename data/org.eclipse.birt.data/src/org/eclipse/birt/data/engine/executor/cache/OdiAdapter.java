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
import org.eclipse.birt.data.engine.odi.IResultIterator;
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
	
	// from odi 
	private IResultIterator resultIterator;

	boolean riStarted = false;
	
	
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
	 * Construction
	 * 
	 * @param customDataSet
	 */
	OdiAdapter( IResultIterator resultSetCache )
	{
		assert resultSetCache != null;
		this.resultIterator = resultSetCache;
	}
	/**
	 * Fetch data from Oda or Odi. After the fetch is done, the cursor
	 * must stay at the row which is fetched.
	 * @return IResultObject
	 * @throws DataException
	 */
	IResultObject fetch( ) throws DataException
	{
		if ( resultSet != null )
			return resultSet.fetch( );
		else if ( customDataSet != null )
			return customDataSet.fetch( );
		else if ( resultIterator != null )
		{
			if(!riStarted)
			{
				riStarted = true;
			}else
				this.resultIterator.next();
			return this.resultIterator.getCurrentResult();
		}
		else
			return resultSetCache.fetch( );
	}

}
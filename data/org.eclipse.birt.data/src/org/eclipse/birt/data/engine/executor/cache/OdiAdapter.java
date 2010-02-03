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
import org.eclipse.birt.data.engine.executor.dscache.DataSetToCache;
import org.eclipse.birt.data.engine.executor.dscache.DataSetFromCache;
import org.eclipse.birt.data.engine.odaconsumer.ResultSet;
import org.eclipse.birt.data.engine.odi.ICustomDataSet;
import org.eclipse.birt.data.engine.odi.IDataSetPopulator;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 * Adapt Oda and Odi interface to a single class, which will provide a uniform
 * method to retrieve data.
 */
public class OdiAdapter
{
	// from Oda
	private ResultSet odaResultSet;
	
	// from data set whose result set needs to be cached
	private DataSetToCache datasetToCache;

	// from odi
	private ICustomDataSet customDataSet;
	
	// from IResultIterator
	private IResultIterator resultIterator;

	//The behavior of "next" method in IResultIterator is slightly
	//different from that of "fetch" method.To mimic the behavior of 
	//fetch method we define a boolean to mark the beginning of an IResultIterator
	boolean riStarted = false;
	
	// from parent query in sub query
	private ResultSetCache resultSetCache;

	// from input stream
	private ResultObjectReader roReader;
	
	// from Joint data set
	private IDataSetPopulator populator;
	
	//from data set whose result is loaded from cache
	private DataSetFromCache datasetFromCache;
	
	
	/**
	 * Construction
	 * 
	 * @param odaResultSet
	 */
	public OdiAdapter( ResultSet odaResultSet )
	{
		assert odaResultSet != null;
		this.odaResultSet = odaResultSet;
	}
	
	/**
	 * Construction
	 * 
	 * @param datasetCacheResultSet
	 */
	public OdiAdapter( DataSetToCache datasetToCache )
	{
		assert datasetToCache != null;
		this.datasetToCache = datasetToCache;
	}
	
	public OdiAdapter( DataSetFromCache datasetFromCache )
	{
		assert datasetFromCache != null;
		this.datasetFromCache = datasetFromCache;
	}

	/**
	 * Construction
	 * 
	 * @param customDataSet
	 */
	public OdiAdapter( ICustomDataSet customDataSet )
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
	public OdiAdapter( IResultIterator resultSetCache )
	{
		assert resultSetCache != null;
		this.resultIterator = resultSetCache;
	}
	
	/**
	 * Construction
	 * 
	 * @param roReader
	 */
	OdiAdapter( ResultObjectReader roReader )
	{
		assert roReader != null;
		this.roReader = roReader;
	}
	
	/**
	 * Construction 
	 * 
	 */
	public OdiAdapter( IDataSetPopulator populator)
	{
		assert populator != null;
		this.populator = populator;
	}
	
	/**
	 * Fetch data from Oda or Odi. After the fetch is done, the cursor
	 * must stay at the row which is fetched.
	 * 
	 * @param stopSign
	 * @return
	 * @throws DataException
	 */
	IResultObject fetch( ) throws DataException
	{
		if ( odaResultSet != null )
		{
			return odaResultSet.fetch( );
		}
		else if ( datasetToCache != null )
		{
			return datasetToCache.fetch( );
		}
		else if ( datasetFromCache != null )
		{
			return datasetFromCache.fetch( );
		}
		else if ( customDataSet != null )
		{
			return customDataSet.fetch( );
		}
		else if ( resultIterator != null )
		{
			if ( !riStarted )
				riStarted = true;
			else
				this.resultIterator.next( );

			return this.resultIterator.getCurrentResult( );
		}
		else if ( roReader != null )
		{
			return roReader.fetch( );
		}
		else if ( populator != null )
		{
			return populator.next( );
		}
		else
		{
			return resultSetCache.fetch( );
		}
	}

}
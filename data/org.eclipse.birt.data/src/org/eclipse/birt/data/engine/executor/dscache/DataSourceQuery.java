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
package org.eclipse.birt.data.engine.executor.dscache;

import java.util.Collection;
import java.util.List;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.BaseQuery;
import org.eclipse.birt.data.engine.executor.transform.CachedResultSet;
import org.eclipse.birt.data.engine.odi.IDataSourceQuery;
import org.eclipse.birt.data.engine.odi.IEventHandler;
import org.eclipse.birt.data.engine.odi.IPreparedDSQuery;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultIterator;

/**
 * In design time, this query will retrieve data from cache.
 */
public class DataSourceQuery extends BaseQuery
		implements
			IDataSourceQuery,
			IPreparedDSQuery
{
	/*
	 * @see org.eclipse.birt.data.engine.odi.IDataSourceQuery#setResultHints(java.util.Collection)
	 */
	public void setResultHints( Collection columnDefns )
	{
		// do nothing
	}

	/*
	 * @see org.eclipse.birt.data.engine.odi.IDataSourceQuery#setResultProjection(java.lang.String[])
	 */
	public void setResultProjection( String[] fieldNames ) throws DataException
	{
		// do nothing
	}

	public void setParameterHints( Collection parameterDefns )
	{
		// do nothing		
	}

	/*
	 * @see org.eclipse.birt.data.engine.odi.IDataSourceQuery#addProperty(java.lang.String, java.lang.String)
	 */
	public void addProperty( String name, String value ) throws DataException
	{
		// do nothing
	}

	/*
	 * @see org.eclipse.birt.data.engine.odi.IDataSourceQuery#declareCustomField(java.lang.String, int)
	 */
	public void declareCustomField( String fieldName, int dataType ) throws DataException
	{
		// do nothing
	}

	/*
	 * @see org.eclipse.birt.data.engine.odi.IDataSourceQuery#prepare()
	 */
	public IPreparedDSQuery prepare( ) throws DataException
	{
		return this;
	}

	/** */
	private DataSetResultCache datasetCache;
	
	/*
	 * @see org.eclipse.birt.data.engine.odi.IPreparedDSQuery#getResultClass()
	 */
	public IResultClass getResultClass( ) throws DataException
	{
		return getOdaCacheResultSet( ).getResultClass( );
	}

	/*
	 * @see org.eclipse.birt.data.engine.odi.IPreparedDSQuery#getParameterMetaData()
	 */
	public Collection getParameterMetaData( ) throws DataException
	{
		return null;
	}

	/*
	 * @see org.eclipse.birt.data.engine.odi.IPreparedDSQuery#getOutputParameterValue(int)
	 */
	public Object getOutputParameterValue( int index ) throws DataException
	{
		return null;
	}

	/*
	 * @see org.eclipse.birt.data.engine.odi.IPreparedDSQuery#getOutputParameterValue(java.lang.String)
	 */
	public Object getOutputParameterValue( String name ) throws DataException
	{
		return null;
	}
	
	/**
	 * Set temporary computed columns to DatasetCache. DatasetCache will use these
	 * objects to produce ResultClass.
	 * 
	 * @param addedTempComputedColumn
	 */
	public void setTempComputedColumn( List addedTempComputedColumn )
	{
		getOdaCacheResultSet( ).setTempComputedColumn( addedTempComputedColumn);
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.odi.IPreparedDSQuery#execute()
	 */
	public IResultIterator execute( IEventHandler eventHandler ) throws DataException
	{
		return new CachedResultSet( this,
				getOdaCacheResultSet( ).getResultClass( ),
				getOdaCacheResultSet( ),
				eventHandler );
	}

	/*
	 * @see org.eclipse.birt.data.engine.odi.IPreparedDSQuery#close()
	 */
	public void close( )
	{
		try
		{
			if ( datasetCache != null )
			{
				datasetCache.close( );
				datasetCache = null;
			}
		}
		catch ( DataException e )
		{
			// ignore it
		}
	}
	
	/**
	 * @return OdaCacheResultSet
	 */
	private DataSetResultCache getOdaCacheResultSet( )
	{
		if ( datasetCache == null )
			datasetCache = new DataSetResultCache( );

		return datasetCache;
	}
	
}

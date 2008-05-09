/*
 *************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */

package org.eclipse.birt.data.engine.executor.transform;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.DataSourceQuery;
import org.eclipse.birt.data.engine.executor.cache.OdiAdapter;
import org.eclipse.birt.data.engine.executor.cache.ResultSetCache;
import org.eclipse.birt.data.engine.executor.cache.RowResultSet;
import org.eclipse.birt.data.engine.executor.cache.SmartCacheRequest;
import org.eclipse.birt.data.engine.impl.IExecutorHelper;
import org.eclipse.birt.data.engine.impl.StopSign;
import org.eclipse.birt.data.engine.impl.document.StreamWrapper;
import org.eclipse.birt.data.engine.odaconsumer.ResultSet;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 * A Result Set that directly fetch data from ODA w/o using any cache.
 * @author Work
 *
 */
public class SimpleResultSet implements IResultIterator
{

	private ResultSet resultSet;
	private RowResultSet rowResultSet;
	private StopSign stopSign;
	private IResultObject currResultObj;

	/**
	 * 
	 * @param dataSourceQuery
	 * @param resultSet
	 * @param resultClass
	 * @param stopSign
	 * @throws DataException
	 */
	public SimpleResultSet( DataSourceQuery dataSourceQuery,
			ResultSet resultSet, IResultClass resultClass, StopSign stopSign )
			throws DataException
	{
		this.rowResultSet = new RowResultSet( new SmartCacheRequest( dataSourceQuery.getMaxRows( ),
				dataSourceQuery.getFetchEvents( ),
				new OdiAdapter( resultSet ),
				resultClass,
				false ) );
		this.currResultObj = this.rowResultSet.next( stopSign );
		this.resultSet = resultSet;
		this.stopSign = stopSign;
	}

	@Override
	public void close( ) throws DataException
	{
		// TODO Auto-generated method stub
		this.resultSet.close( );
	}

	@Override
	public void doSave( StreamWrapper streamsWrapper, boolean isSubQuery )
			throws DataException
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void first( int groupingLevel ) throws DataException
	{
		// TODO Auto-generated method stub

	}

	@Override
	public Object getAggrValue( String aggrName ) throws DataException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getCurrentGroupIndex( int groupLevel ) throws DataException
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public IResultObject getCurrentResult( ) throws DataException
	{
		return this.currResultObj;
	}

	@Override
	public int getCurrentResultIndex( ) throws DataException
	{
		return this.rowResultSet.getIndex( );
	}

	@Override
	public int getEndingGroupLevel( ) throws DataException
	{
		if ( this.rowResultSet.hasNext( ) )
		{
			return 1;
		}

		return 0;
	}

	@Override
	public IExecutorHelper getExecutorHelper( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getGroupStartAndEndIndex( int groupLevel )
			throws DataException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IResultClass getResultClass( ) throws DataException
	{
		// TODO Auto-generated method stub
		return this.rowResultSet.getMetaData( );
	}

	@Override
	public ResultSetCache getResultSetCache( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getRowCount( ) throws DataException
	{
		// TODO Auto-generated method stub
		return this.currResultObj == null ? 0 : -1;
	}

	@Override
	public int getStartingGroupLevel( ) throws DataException
	{
		if ( this.rowResultSet.getIndex( ) == 0 )
			return 0;
		else
			return 1;
	}

	@Override
	public void last( int groupingLevel ) throws DataException
	{
		// TODO Auto-generated method stub

	}

	@Override
	public boolean next( ) throws DataException
	{
		this.currResultObj = this.rowResultSet.next( stopSign );
		return this.currResultObj != null;
	}

}

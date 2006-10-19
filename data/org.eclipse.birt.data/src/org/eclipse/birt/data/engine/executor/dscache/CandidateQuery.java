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

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.BaseQuery;
import org.eclipse.birt.data.engine.executor.transform.CachedResultSet;
import org.eclipse.birt.data.engine.impl.DataEngineSession;
import org.eclipse.birt.data.engine.odi.ICandidateQuery;
import org.eclipse.birt.data.engine.odi.ICustomDataSet;
import org.eclipse.birt.data.engine.odi.IEventHandler;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultIterator;

/**
 * In design time, this query will retrieve data from cache.
 */
public class CandidateQuery extends BaseQuery implements ICandidateQuery
{
	//
	private DataSetResultCache datasetCache;
	private DataEngineSession session;
	
	public CandidateQuery( DataEngineSession session )
	{
		this.session = session;
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.odi.ICandidateQuery#getResultClass()
	 */
	public IResultClass getResultClass( ) throws DataException
	{
		return getOdaCacheResultSet( ).getResultClass( );
	}

	/*
	 * @see org.eclipse.birt.data.engine.odi.ICandidateQuery#execute()
	 */
	public IResultIterator execute( IEventHandler eventHandler ) throws DataException
	{
		return new CachedResultSet( this,
				getOdaCacheResultSet( ).getResultClass( ),
				getOdaCacheResultSet( ),
				eventHandler, session );
	}

	/*
	 * @see org.eclipse.birt.data.engine.odi.ICandidateQuery#setCandidates(org.eclipse.birt.data.engine.odi.IResultIterator,
	 *      int)
	 */
	public void setCandidates( IResultIterator resultObjsIterator,
			int groupingLevel ) throws DataException
	{
		// do nothing
	}

	/*
	 * @see org.eclipse.birt.data.engine.odi.ICandidateQuery#setCandidates(org.eclipse.birt.data.engine.odi.ICustomDataSet)
	 */
	public void setCandidates( ICustomDataSet customDataSet )
			throws DataException
	{
		// do nothing
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.odi.IQuery#close()
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
			datasetCache = new DataSetResultCache( session );

		return datasetCache;
	}

}

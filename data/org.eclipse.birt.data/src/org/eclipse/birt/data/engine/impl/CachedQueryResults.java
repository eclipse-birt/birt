/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.impl;

import java.util.Map;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.data.engine.core.DataException;

/**
 * 
 */

public class CachedQueryResults implements IQueryResults
{

	private String queryResultID;
	private IResultIterator resultIterator;
	private IPreparedQuery pQuery;
	private String name;
	private DataEngineSession session;
	private boolean existCachedFile = true;
	
	private static Logger logger = Logger.getLogger( CachedQueryResults.class.getName( ) );

	/**
	 * 
	 * @param context
	 * @param queryResultID
	 * @throws DataException
	 */
	public CachedQueryResults( DataEngineSession session, String queryResultID,
			IPreparedQuery preparedQuery, Map appContext ) throws DataException
	{
		this.session = session;
		String tempDir = session.getTempDir( );
		Object[] params = {
				tempDir, queryResultID
		};
		logger.entering( CachedQueryResults.class.getName( ),
				"CachedQueryResults",
				params );

		this.queryResultID = queryResultID;
		this.pQuery = preparedQuery;
		
		this.resultIterator = new CacheResultIterator( session,
				tempDir,
				this,
				appContext );
		this.existCachedFile = ( (CacheResultIterator) this.resultIterator ).existCachedFile( );
	
		logger.exiting( CachedQueryResults.class.getName( ),
				"CachedQueryResults" );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IQueryResults#getPreparedQuery()
	 */
	public IPreparedQuery getPreparedQuery( )
	{
		return this.pQuery;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IQueryResults#getResultIterator()
	 */
	public IResultIterator getResultIterator( ) throws BirtException
	{
		return resultIterator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IQueryResults#getResultMetaData()
	 */
	public IResultMetaData getResultMetaData( ) throws BirtException
	{
		return resultIterator.getResultMetaData( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IBaseQueryResults#close()
	 */
	public void close( ) throws BirtException
	{
		if ( resultIterator != null )
			resultIterator.close( );
		NamingRelationUtil.merge( this.session, this.getPreparedQuery( )
				.getReportQueryDefn( ), this );
	}

	public String getID( )
	{
		return queryResultID;
	}

	public void cancel( )
	{
		// TODO Auto-generated method stub
	}
	
	public boolean existCachedFile( )
	{
		return this.existCachedFile;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.api.IBaseQueryResults#setName(java.lang.String)
	 */
	public void setName( String name )
	{
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.api.INamedObject#getName()
	 */
	public String getName( )
	{
		return name;
	}

	@Override
	public void setID( String queryResultsId )
	{
		throw new UnsupportedOperationException();
	}

}

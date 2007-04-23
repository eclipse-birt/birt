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

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngineContext;
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

	/**
	 * 
	 * @param context
	 * @param queryResultID
	 * @throws DataException
	 */
	public CachedQueryResults( DataEngineContext context, String queryResultID )
			throws DataException
	{
		this.queryResultID = queryResultID;
		this.resultIterator = new CacheResultIterator( context, queryResultID );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IQueryResults#getPreparedQuery()
	 */
	public IPreparedQuery getPreparedQuery( )
	{
		return null;
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
	}

	public String getID( )
	{
		return queryResultID;
	}

}

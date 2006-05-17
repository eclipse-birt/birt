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

package org.eclipse.birt.data.engine.impl.document;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.mozilla.javascript.Scriptable;

/**
 * Used in presentation
 */
public class ResultIterator implements IResultIterator
{
	// data engine context
	private DataEngineContext context;
	
	// save and load util
	private RDLoad valueLoader;
	
	// name of associated query results
	private String queryResultID;

	// sub query info
	private String subQueryName;
	private int subQueryIndex;
	private IQueryResults queryResults;

	// when sub query is used, its parent query index needs to be remembered
	private int currParentIndex;
	
	//
	protected org.eclipse.birt.data.engine.odi.IResultIterator odiResult;
	
	// logger
	private static Logger logger = Logger.getLogger( ResultIterator.class.getName( ) );
	
	/**
	 * @param context
	 * @param queryResults
	 * @param queryResultID
	 * @throws DataException 
	 */
	ResultIterator( DataEngineContext context,
			IQueryResults queryResults, String queryResultID )
			throws DataException
	{
		this( context, queryResults, queryResultID, null, -1 );
	}
	
	/**
	 * @param context
	 * @param queryResults
	 * @param queryResultID
	 * @param subQueryName
	 * @param currParentIndex
	 * @param rsCache
	 * @throws DataException
	 */
	ResultIterator( DataEngineContext context,
			IQueryResults queryResults, String queryResultID,
			String subQueryName, int currParentIndex)
			throws DataException
	{
		super( );

		assert queryResultID != null && context != null && queryResults != null;

		this.context = context;
		this.queryResults = queryResults;
		
		this.queryResultID = queryResultID;
		this.subQueryName = subQueryName;

		this.currParentIndex = currParentIndex;
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#getSecondaryIterator(java.lang.String,
	 *      org.mozilla.javascript.Scriptable)
	 */
	public IResultIterator getSecondaryIterator( String subQueryName,
			Scriptable scope ) throws DataException
	{
		String parentQueryResultsID = null;
		if ( this.subQueryName == null )
		{
			parentQueryResultsID = queryResultID;
		}
		else
		{
			parentQueryResultsID = queryResultID
					+ "/" + this.subQueryName + "/" + this.subQueryIndex;
		}

		QueryResults queryResults = null;
		try
		{
			queryResults = new QueryResults( context,
					parentQueryResultsID,
					getQueryResults( ).getResultMetaData( ), 
					subQueryName,
					this.getValueLoader( ).getCurrentIndex( ) );
		}
		catch ( BirtException e )
		{
			throw new DataException( ResourceConstants.RD_LOAD_ERROR,
					e,
					"Subquery" );
		}

		try
		{
			ResultIterator ri = (ResultIterator) queryResults.getResultIterator( );
			ri.setSubQueryName( subQueryName );
			return ri;
		}
		catch ( BirtException e )
		{
			throw new DataException( ResourceConstants.RD_LOAD_ERROR,
					e,
					"Subquery" );
		}
	}
	
	void setSubQueryName( String subQueryName )
	{
		this.subQueryName = subQueryName;
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#next()
	 */
    public boolean next( ) throws DataException
	{
    	return this.getValueLoader( ).next( );
	}
    
    /*
     * @see org.eclipse.birt.data.engine.api.IResultIterator#getRowId()
     */
	public int getRowId( ) throws BirtException
	{
		return this.getValueLoader( ).getCurrentId( );
	}
	
    /*
     * @see org.eclipse.birt.data.engine.api.IResultIterator#getRowIndex()
     */
	public int getRowIndex( ) throws BirtException
	{
		return this.getValueLoader( ).getCurrentIndex( );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#moveTo(int)
	 */
	public void moveTo( int rowIndex ) throws BirtException
	{
		this.getValueLoader( ).moveTo( rowIndex );		
	}
	
	/**
	 * 
	 */
    private RDLoad getValueLoader( ) throws DataException
	{
		if ( this.valueLoader == null )
		{
			valueLoader = RDUtil.newLoad( this.context,
					this.queryResultID,
					this.subQueryName,
					this.currParentIndex );

			if ( this.subQueryName != null )
				subQueryIndex = valueLoader.getSubQueryIndex( currParentIndex );

		}

		return valueLoader;
	}

    /*
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#getQueryResults()
	 */
	public IQueryResults getQueryResults( )
	{
		return queryResults;
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#getScope()
	 */
	public Scriptable getScope( )
	{
		return null;
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#getResultMetaData()
	 */
	public IResultMetaData getResultMetaData( ) throws BirtException
	{
		return this.queryResults.getResultMetaData( );
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#skipToEnd(int)
	 */
	public void skipToEnd( int groupLevel ) throws BirtException
	{
		this.getValueLoader( ).skipToEnd( groupLevel );
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#getStartingGroupLevel()
	 */
	public int getStartingGroupLevel( ) throws BirtException
	{
		return this.getValueLoader( ).getStartingGroupLevel( );
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#getEndingGroupLevel()
	 */
	public int getEndingGroupLevel( ) throws BirtException
	{
		return this.getValueLoader( ).getEndingGroupLevel( );
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#close()
	 */
	public void close( ) throws BirtException
	{
		this.getValueLoader( ).close( );
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#findGroup(java.lang.Object[])
	 */
	public boolean findGroup( Object[] groupKeyValues ) throws BirtException
	{
		throw new DataException( "Not supported in presentation" );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#getValue(java.lang.String)
	 */
	public Object getValue( String name ) throws BirtException
	{
		return this.getValueLoader( ).getValue( name );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#getBoolean(java.lang.String)
	 */
	public Boolean getBoolean( String name ) throws BirtException
	{
		return DataTypeUtil.toBoolean( getValue( name ) );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#getInteger(java.lang.String)
	 */
	public Integer getInteger( String name ) throws BirtException
	{
		return DataTypeUtil.toInteger( getValue( name ) );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#getDouble(java.lang.String)
	 */
	public Double getDouble( String name ) throws BirtException
	{
		return DataTypeUtil.toDouble( getValue( name ) );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#getString(java.lang.String)
	 */
	public String getString( String name ) throws BirtException
	{
		return DataTypeUtil.toString( getValue( name ) );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#getBigDecimal(java.lang.String)
	 */
	public BigDecimal getBigDecimal( String name ) throws BirtException
	{
		return DataTypeUtil.toBigDecimal( getValue( name ) );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#getDate(java.lang.String)
	 */
	public Date getDate( String name ) throws BirtException
	{
		return DataTypeUtil.toDate( getValue( name ) );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#getBlob(java.lang.String)
	 */
	public Blob getBlob( String name ) throws BirtException
	{
		return DataTypeUtil.toBlob( getValue( name ) );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#getBytes(java.lang.String)
	 */
	public byte[] getBytes( String name ) throws BirtException
	{
		return DataTypeUtil.toBytes( getValue( name ) );
	}

}

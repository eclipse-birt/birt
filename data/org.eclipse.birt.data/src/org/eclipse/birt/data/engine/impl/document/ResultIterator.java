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

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IDataScriptEngine;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.document.util.IExprResultSet;
import org.mozilla.javascript.Scriptable;

/**
 * Used in presentation
 */
public class ResultIterator implements IResultIterator
{
	// data engine context
	private DataEngineContext context;
	
	// name of associated query results
	private String queryResultID;

	// sub query info
	private String subQueryName;
	private int subQueryIndex;
	private IQueryResults queryResults;

	// when sub query is used, its parent query index needs to be remembered
	private int currParentIndex;
	
	// expression data result set
	protected IExprResultSet exprResultSet;
	
	private String tempDir;

	
	/**
	 * @param context
	 * @param queryResults
	 * @param queryResultID
	 * @throws DataException 
	 */
	public ResultIterator( String tempDir, DataEngineContext context, IQueryResults queryResults,
			String queryResultID ) throws DataException
	{
		this( tempDir, context, queryResults, queryResultID, null, -1 );
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
	ResultIterator( String tempDir, DataEngineContext context, IQueryResults queryResults,
			String queryResultID, String subQueryName, int currParentIndex )
			throws DataException
	{
		super( );
		assert queryResultID != null && context != null && queryResults != null && tempDir != null;
		this.tempDir = tempDir;
		this.context = context;
		this.queryResults = queryResults;
		
		this.queryResultID = queryResultID;
		this.subQueryName = subQueryName;
		this.currParentIndex = currParentIndex;
		
		this.prepare( );
	}

	/**
	 * @throws DataException
	 */
	private void prepare( ) throws DataException
	{
		String rootID = QueryResultIDUtil.get1PartID( this.queryResultID );
		String selfID = QueryResultIDUtil.get2PartID( this.queryResultID );
		if ( selfID == null )
			selfID = this.queryResultID;
		
		RDLoad valueLoader = RDUtil.newLoad( tempDir, this.context,
				new QueryResultInfo( rootID,
						null,
						selfID,
						this.subQueryName,
						this.currParentIndex ) );
		
		int rowIdStartingIndex = 0;
		if ( this.subQueryName != null )
		{
			subQueryIndex = valueLoader.getSubQueryIndex( currParentIndex );
			rowIdStartingIndex = currParentIndex;
		}
		this.exprResultSet = valueLoader.loadExprResultSet( rowIdStartingIndex );
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
		//If it is summary table, we do not save result metadata.
		if ( this.queryResults.getPreparedQuery( ) != null
				&& this.queryResults.getPreparedQuery( ).getReportQueryDefn( ) != null
				&& this.queryResults.getPreparedQuery( )
						.getReportQueryDefn( )
						.isSummaryQuery( ) )
			return null;
		return this.queryResults.getResultMetaData( );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#next()
	 */
    public boolean next( ) throws DataException
	{
    	return this.exprResultSet.next( );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#getValue(java.lang.String)
	 */
	public Object getValue( String name ) throws BirtException
	{
		Object result = this.exprResultSet.getValue( name ); 
		if( result != null && result instanceof BirtException )
			throw (BirtException)result;
		return result;
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
	
    /*
     * @see org.eclipse.birt.data.engine.api.IResultIterator#getRowId()
     */
	public int getRowId( ) throws BirtException
	{
		if ( this.exprResultSet.isEmpty() )
			return -1;
		return this.exprResultSet.getCurrentId( );
	}
	
    /*
     * @see org.eclipse.birt.data.engine.api.IResultIterator#getRowIndex()
     */
	public int getRowIndex( ) throws BirtException
	{
		return this.exprResultSet.getCurrentIndex( );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#moveTo(int)
	 */
	public void moveTo( int rowIndex ) throws BirtException
	{
		this.exprResultSet.moveTo( rowIndex );		
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#getStartingGroupLevel()
	 */
	public int getStartingGroupLevel( ) throws BirtException
	{
		return this.exprResultSet.getStartingGroupLevel( );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#getEndingGroupLevel()
	 */
	public int getEndingGroupLevel( ) throws BirtException
	{
		return this.exprResultSet.getEndingGroupLevel( );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#skipToEnd(int)
	 */
	public void skipToEnd( int groupLevel ) throws BirtException
	{
		this.exprResultSet.skipToEnd( groupLevel );
	}
	
	public IResultIterator getSecondaryIterator( ScriptContext context,
			String subQueryName ) throws DataException
	{
		try
		{
			Scriptable scope = null;
			if ( context != null )
				scope = ( (IDataScriptEngine) context.getScriptEngine( IDataScriptEngine.ENGINE_NAME ) ).getJSScope( context );
			return this.getSecondaryIterator( subQueryName, scope );
		}
		catch ( BirtException e )
		{
			throw DataException.wrap( e );
		}
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
			queryResults = new QueryResults( tempDir,
					context,
					queryResultID,
					parentQueryResultsID,
					this.getResultMetaData( ),
					subQueryName,
					this.exprResultSet.getCurrentIndex( ),
					this.queryResults,
					null );
		}
		catch ( Exception e )
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
	
	/**
	 * @param subQueryName
	 */
	void setSubQueryName( String subQueryName )
	{
		this.subQueryName = subQueryName;
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#close()
	 */
	public void close( ) throws BirtException
	{
		this.exprResultSet.close( );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#findGroup(java.lang.Object[])
	 */
	public boolean findGroup( Object[] groupKeyValues ) throws BirtException
	{
		throw new DataException( ResourceConstants.NOT_SUPPORT_IN_PRESENTATION );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#isEmpty()
	 */
	public boolean isEmpty( ) throws BirtException
	{
		return exprResultSet.isEmpty( );
	}
	
	IExprResultSet getExprResultSet()
	{
		return this.exprResultSet;
	}

	public boolean isBeforeFirst( ) throws BirtException
	{
		return !isEmpty( ) && getRowIndex( ) < 0;
	}

	public boolean isFirst( ) throws BirtException
	{
		return !isEmpty( ) && getRowIndex( ) == 0;
	}
}

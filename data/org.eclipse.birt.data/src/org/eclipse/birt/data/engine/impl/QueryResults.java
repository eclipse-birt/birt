/*
 *************************************************************************
 * Copyright (c) 2004, 2014 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *  
 *************************************************************************
 */ 

package org.eclipse.birt.data.engine.impl;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.ISubqueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.cache.ResultSetCache;
import org.eclipse.birt.data.engine.executor.transform.CachedResultSet;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.document.QueryResultIDUtil;
import org.eclipse.birt.data.engine.impl.document.StreamWrapper;
import org.eclipse.birt.data.engine.impl.document.viewing.ExprMetaUtil;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultObject;
import org.mozilla.javascript.Scriptable;

/** 
 * A report query's results opened and ready for data retrieval.  
 * A query results could contain multiple result sets.
 * This is intended to be used by both Factory and Presentation Engines
 * in BIRT, including later releases when a report document persists.
 * Beyond Release 1, this would include methods to save and restore
 * results in a persisted Report Document.
 */
public class QueryResults implements IQueryResults, IQueryService
{
	// query service instance
	private IServiceForQueryResults 	queryService;
	
	// context of data engine
	private DataEngineSession 			session;
	private Scriptable 					queryScope;
	private int 						nestedLevel;
	
	// id of this instance
	private String                      rootQueryResultID;
	private String 						selfQueryResultID;

	private IResultIterator				iterator;
		
	private static Logger logger = Logger.getLogger( QueryResults.class.getName( ) );
	
	private String name;
	/**
	 * @param queryService
	 * @param queryScope
	 * @param nestedLevel
	 * @throws DataException 
	 */
	QueryResults( IServiceForQueryResults queryService ) throws DataException
	{
		logger.entering( QueryResults.class.getName( ),
				"QueryResults",
				queryService );
		assert queryService != null;
		
		this.queryService = queryService;
		this.session = queryService.getSession( );
		this.queryScope = queryService.getScope( );
		this.nestedLevel = queryService.getNestedLevel( );
		
		logger.exiting( QueryResults.class.getName( ), "QueryResults" );
	}
		
	/*
	 * @see org.eclipse.birt.data.engine.api.IQueryResults#getName()
	 */
	public String getID( )
	{
		if ( selfQueryResultID == null )
			selfQueryResultID = this.session.getQueryResultIDUtil( ).nextID( );

		return QueryResultIDUtil.buildID( rootQueryResultID, selfQueryResultID );
	}
	
	/*
	 * Returns the PreparedQuery that contains the execution plan for producing
	 * this. A convenience method for the API consumer.
	 * 
	 * @see org.eclipse.birt.data.engine.api.IQueryResults#getPreparedQuery()
	 */
	public IPreparedQuery getPreparedQuery()
	{ 
		return queryService.getPreparedQuery( );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.api.IQueryResults#getResultMetaData()
	 */
	public IResultMetaData getResultMetaData( ) throws DataException
	{
		if ( queryService == null )
			throw new DataException( ResourceConstants.RESULT_CLOSED );
		
		try
		{
			IResultMetaData metaData = queryService.getResultMetaData( );
			if ( metaData == null )
			{
				IResultIterator rsIterator = getResultIterator( );
				if ( rsIterator != null )
				{
					return rsIterator.getResultMetaData( );
				}
				return null;
			}
			else
			{
				return metaData;
			}
		}
		catch ( DataException e )
		{
			throw e;
		}
		catch ( BirtException e )
		{
			throw DataException.wrap( e );
		}
	}
	
	/*
	 * 
	 * Returns the current result's iterator. Repeated call of this method
	 * without having advanced to the next result would return the same iterator
	 * at its current state.
	 * 
	 * @see org.eclipse.birt.data.engine.api.IQueryResults#getResultIterator()
	 */
	public IResultIterator getResultIterator( ) throws DataException
	{ 
		if( this.session.getStopSign().isStopped( ) )
			return null;
		if ( queryService == null )
			throw new DataException( ResourceConstants.RESULT_CLOSED );

		try
		{
			if ( iterator == null )
			{
				// data row binding
				this.queryService.initAutoBinding( );
				this.queryService.validateQuery( );
				
				long startTime = System.currentTimeMillis( );
				org.eclipse.birt.data.engine.odi.IResultIterator odiIterator = queryService.executeQuery( );
				long endTime = System.currentTimeMillis( );

				if( logger.isLoggable( Level.FINE ) )
				{
					String dataSetName = queryService.getDataSetRuntime() != null ?
										queryService.getDataSetRuntime().getName() : null;
					logger.log( Level.FINE, "ODI query execution time: " + 
							( endTime - startTime ) + " ms;\n   Executed data set: " + dataSetName );
				}

				if( queryService.getQueryDefn( ) instanceof IQueryDefinition && ! ((QueryDefinition)queryService.getQueryDefn()).isTempQuery())
				{
					iterator = QueryResultsUtil.processOdiResult( session,
							this,
							(IQueryDefinition) queryService.getQueryDefn( ),
							odiIterator,
							( (IQueryDefinition) queryService.getQueryDefn( ) ).getDataSetName( ) );
					if ( iterator != null )
						return iterator;
				}
				
				if ( isDummyQuery( odiIterator ) )
				{
					iterator = new DummyResultIterator( new ResultService( session,
							this ),
							odiIterator,
							this.queryScope, this.queryService.getStartingRawID( ) );
				}
				else 
				{
					if ( queryService.getQueryDefn( ) instanceof IQueryDefinition && ((IQueryDefinition)queryService.getQueryDefn( )).isSummaryQuery( ))
					{
						iterator = new ResultIterator2( new ResultService( session,
								this ),
								odiIterator,
								this.queryScope, this.queryService.getStartingRawID( )  );
					}
					else if ( queryService.getQueryDefn( ).usesDetails( ) == true || queryService.getQueryDefn( ).cacheQueryResults( ) )
					{
						//First create the cache. The cache is created when 
						//a ResultIterator is closed;Please note that whether usesDetails or
						//not, we should always create a complete ResultIterator.
						iterator = new ResultIterator( new ResultService( session,
									this ), odiIterator, this.queryScope, this.queryService.getStartingRawID( )  );
					}
					else
					{
						iterator = new ResultIterator2( new ResultService( session,
									this ),
									odiIterator,
									this.queryScope, this.queryService.getStartingRawID( )  );
					}
				}
			}
		}
		catch ( BirtException e )
		{
			throw DataException.wrap( e );
		}
		logger.logp( Level.FINER,
				QueryResults.class.getName( ),
				"getResultIterator",
				"finished" );
		return iterator;
	}

	/**
	 * 
	 * @param odiIterator
	 * @return
	 * @throws DataException
	 */
	private boolean isDummyQuery(org.eclipse.birt.data.engine.odi.IResultIterator odiIterator) throws DataException {
		return queryService.getQueryDefn() instanceof IQueryDefinition
				&& ( (IQueryDefinition) queryService.getQueryDefn( ) ).getDataSetName( ) == null
				&& ( odiIterator.getResultClass( ).getFieldCount( ) == 0 || ( odiIterator.getResultClass( )
						.getFieldCount( ) == 1 && odiIterator.getResultClass( )
						.getFieldName( 1 )
						.equals( ExprMetaUtil.POS_NAME ) ) );
	}
	
	/*
	 * Closes all query result set(s) associated with this object; provides a
	 * hint to the query that it can safely release all associated resources.
	 * The query results might have iterators open on them. Iterators associated
	 * with the query result sets are invalidated and can no longer be used.
	 * 
	 * @see org.eclipse.birt.data.engine.api.IQueryResults#close()
	 */
	public void close( ) throws BirtException
	{
		if ( this.queryService == null )
		{
			// already closed
			return;
		}
		NamingRelationUtil.merge( session, queryService.getQueryDefn( ), this );

		if ( iterator != null )
		{
			iterator.close( );
			iterator = null;
		}
	    
		//queryService.close( );
		queryService = null;
		logger.logp( Level.FINER,
				QueryResults.class.getName( ),
				"close",
				"Iterators associated with QueryResults are closed" );
	}
	
	/**
	 * @param rootQueryResultID
	 * @param selfQueryResultID
	 */
	void setID( String rootQueryResultID, String selfQueryResultID )
	{
		this.rootQueryResultID = rootQueryResultID;
		this.selfQueryResultID = selfQueryResultID;
	}
	
	/**
	 * Set current queryresult ID for sub query. Sub query result ID can not be
	 * generated independently, and it is needs to be attached with its parent
	 * query.
	 * 
	 * @param selfQueryResultID
	 */
	public void setID( String selfQueryResultID )
	{
		this.setID( null, selfQueryResultID );
	}
	
	/**
	 * If current query results is associated with a sub query, its result
	 * iterator needs to know which group level this sub query belongs to.
	 * 
	 * @return group level of sub query
	 */
	int getGroupLevel( )
	{
		return queryService.getGroupLevel( );
	}
	
	public IServiceForQueryResults getQueryService()
	{
		return this.queryService;
	}
	/*
	 * @see org.eclipse.birt.data.engine.impl.IQueryService#isClosed()
	 */
	public boolean isClosed( )
	{
		return queryService == null;
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.impl.IQueryService#getNestedLevel()
	 */
	public int getNestedLevel( )
	{
		return this.nestedLevel;
	}

	/*
	 * @see org.eclipse.birt.data.engine.impl.IQueryService#getQueryScope()
	 */
	public Scriptable getQueryScope( )
	{
		return this.queryScope;
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.impl.IQueryService#getExecutorHelper()
	 */
	public IExecutorHelper getExecutorHelper( ) throws DataException
	{
		if ( this.getResultIterator( ) instanceof ResultIterator )
			return ( (ResultIterator) this.getResultIterator( ) ).getOdiResult( )
				.getExecutorHelper( );
		else
			return null;
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.impl.IQueryService#getDataSetRuntime(int)
	 */
	public DataSetRuntime[] getDataSetRuntime( int count )
	{
		return this.queryService.getDataSetRuntimes( count );
	}
	
	/**
	 * The ODI result iterator for DummyQuery. A DummyQuery is a query without data set. A DummyQuery result
	 * iterator always have one row.
	 * @author Administrator
	 *
	 */
	private static class DummyOdiResultIterator extends CachedResultSet
	{
		private boolean isFirstRowFetched = false;
		private org.eclipse.birt.data.engine.odi.IResultIterator prototype = null;

		DummyOdiResultIterator(
				org.eclipse.birt.data.engine.odi.IResultIterator result )
		{
			this.prototype = result;
		}

		public void close( )
		{

		}

		public void doSave( StreamWrapper streamsWrapper, boolean isSubQuery )
				throws DataException
		{
			try
			{
				if ( streamsWrapper.getStreamForResultClass( ) != null )
				{
					IOUtil.writeInt( streamsWrapper.getStreamForResultClass( ),
							0 );
					streamsWrapper.getStreamForResultClass( ).close( );
					if ( streamsWrapper.getStreamForDataSet( ) != null )
					{
						IOUtil.writeInt( streamsWrapper.getStreamForDataSet( ),
								0 );
						streamsWrapper.getStreamForDataSet( ).close( );
					}
					if ( streamsWrapper.getStreamForDataSetRowLens( ) != null )
					{
						IOUtil.writeLong( streamsWrapper.getStreamForDataSetRowLens( ), 0 );
						streamsWrapper.getStreamForDataSetRowLens( ).close( );
					}
				}
				IOUtil.writeInt( streamsWrapper.getStreamForGroupInfo( ), 0 );
			}
			catch ( IOException e )
			{
				throw new DataException( ResourceConstants.RD_SAVE_ERROR,
						e,
						"Result Class" );
			}
		}

		public void first( int groupingLevel ) throws DataException
		{
			this.prototype.first( groupingLevel );
		}

		public int getCurrentGroupIndex( int groupLevel ) throws DataException
		{
			return 0;
		}

		public IResultObject getCurrentResult( ) throws DataException
		{
			return this.prototype.getCurrentResult();
		}

		public int getCurrentResultIndex( ) throws DataException
		{
			return 0;
		}

		public int getEndingGroupLevel( ) throws DataException
		{
			return 0;
		}

		public IExecutorHelper getExecutorHelper( )
		{
			return this.prototype.getExecutorHelper( );
		}

		public int[] getGroupStartAndEndIndex( int groupLevel )
				throws DataException
		{

			return this.prototype.getGroupStartAndEndIndex( groupLevel );
		}

		public IResultClass getResultClass( ) throws DataException
		{

			return this.prototype.getResultClass( );
		}

		public ResultSetCache getResultSetCache( )
		{

			return this.prototype.getResultSetCache( );
		}

		public int getRowCount( ) throws DataException
		{
			return 1;
		}

		public int getStartingGroupLevel( ) throws DataException
		{
			return 0;
		}

		public void last( int groupingLevel ) throws DataException
		{
			this.prototype.last( groupingLevel );
		}

		public boolean next( ) throws DataException
		{
			if ( !this.isFirstRowFetched )
			{
				this.isFirstRowFetched = true;
				return true;
			}
			return false;
		}
	}

	/**
	 * 
	 * @author Administrator
	 *
	 */
	private class DummyResultIterator extends ResultIterator
	{
		DummyResultIterator( IServiceForResultSet rService,
				org.eclipse.birt.data.engine.odi.IResultIterator odiResult,
				Scriptable scope, int staringRawId ) throws DataException
		{
			super( rService, new DummyOdiResultIterator( odiResult ), scope, staringRawId );
		}
		
		public boolean next( ) throws DataException
		{
			return this.getOdiResult( ).next( );
		}
		
		public IResultIterator getSecondaryIterator( String subQueryName,
				Scriptable subScope ) throws DataException
		{
			Collection subQueries = this.getQueryResults().getPreparedQuery().getReportQueryDefn().getSubqueries( );
			Iterator subIt = subQueries.iterator( );
			HashMap subQueryMap = new HashMap();
			while ( subIt.hasNext( ) )
			{
				ISubqueryDefinition subquery = (ISubqueryDefinition) subIt.next( );
				PreparedQueryUtil.mappingParentColumnBinding( subquery );
				subQueryMap.put( subquery.getName( ), subquery);
				
			}
			PreparedDummyQuery preparedQuery = new PreparedDummyQuery( ((ISubqueryDefinition)subQueryMap.get( subQueryName )),
					session );
						
			IQueryResults queryResults;
			try 
			{
				queryResults = (IQueryResults) preparedQuery.executeQuery( subScope,
						subScope );
				return queryResults.getResultIterator( );
			} catch (BirtException e) 
			{
				 throw new DataException( e.getMessage() );
			}
		}
		
		public int getRowId( ) throws BirtException
		{
			return getRowIndex( );
		}
		
		public void close() throws BirtException
		{
			super.close( );
		}
	}
		
	/**
	 * 
	 */
	private static class ResultService implements IServiceForResultSet
	{
		/** */
		private DataEngineSession session;
		private QueryResults queryResults;
		
		/**
		 * @param queryResults
		 */
		public ResultService( DataEngineSession session,
				QueryResults queryResults )
		{
			this.session = session;
			this.queryResults = queryResults;
		}		

		
		/*
		 * @see org.eclipse.birt.data.engine.impl.IResultService#getQueryResults()
		 */
		public IQueryResults getQueryResults( )
		{
			return queryResults;
		}

		/*
		 * @see org.eclipse.birt.data.engine.impl.IResultService#getQueryDefn()
		 */
		public IBaseQueryDefinition getQueryDefn( )
		{
			return queryResults.queryService.getQueryDefn( );
		}
		
		/*
		 * @see org.eclipse.birt.data.engine.impl.IResultService#execSubquery(org.eclipse.birt.data.engine.odi.IResultIterator,
		 *      java.lang.String, org.mozilla.javascript.Scriptable)
		 */
		public IQueryResults execSubquery(
				org.eclipse.birt.data.engine.odi.IResultIterator iterator,
				String subQueryName, Scriptable subScope ) throws DataException
		{
			return queryResults.queryService.execSubquery( iterator,
					this.queryResults.queryService.getQueryExecutor( ),
					subQueryName,
					subScope );
		}

		/*
		 * @see org.eclipse.birt.data.engine.impl.IResultService#getBaseExpression(java.lang.String)
		 */
		public IBaseExpression getBindingExpr( String exprName ) throws DataException
		{
			return queryResults.queryService.getBindingExpr( exprName );
		}
		
		/*
		 * @see org.eclipse.birt.data.engine.impl.IResultService#getAutoBindingExpr(java.lang.String)
		 */
		public IScriptExpression getAutoBindingExpr( String exprName )
		{
			return queryResults.queryService.getAutoBindingExpr( exprName );
		}

		/*
		 * @see org.eclipse.birt.data.engine.impl.IServiceForResultSet#getAllBindingExprs()
		 */
		public List getAllBindingExprs( )
		{
			return queryResults.queryService.getAllBindingExprs( );
		}

		/*
		 * @see org.eclipse.birt.data.engine.impl.IServiceForResultSet#getAllAutoBindingExprs()
		 */
		public Map getAllAutoBindingExprs( )
		{
			return queryResults.queryService.getAllAutoBindingExprs( );
		}



		public DataEngineSession getSession( )
		{
			return session;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.api.IQueryResults#cancel()
	 */
	public void cancel( )
	{
		this.session.getStopSign( ).stop( );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.api.INamedObject#setName(java.lang.String)
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
	
	public DataEngineSession getSession(){
		return this.session;
	}
	
}


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

import java.util.Collection;
import java.util.List;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IBaseQueryResults;
import org.eclipse.birt.data.engine.api.IBaseResultIterator;
import org.eclipse.birt.data.engine.api.IGroupInstanceInfo;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.data.engine.api.ISubqueryDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.DataResourceHandle;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.DataSetRuntime;
import org.eclipse.birt.data.engine.impl.ExecutorHelper;
import org.eclipse.birt.data.engine.impl.IExecutorHelper;
import org.eclipse.birt.data.engine.impl.IQueryService;
import org.eclipse.birt.data.engine.impl.document.stream.StreamManager;
import org.eclipse.birt.data.engine.impl.document.viewing.ExprMetaInfo;
import org.eclipse.birt.data.engine.impl.document.viewing.ExprMetaUtil;
import org.eclipse.birt.data.engine.script.ScriptConstants;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * This class be used in presentation to retrieve ResultIterator. It will have
 * the same ID as its generation QueryResult.
 */
public class QueryResults implements IQueryResults, IQueryService
{	
	// context and ID info
	private DataEngineContext context;
	private String queryResultID, baseQueryResultID;
	
	// result data
	private IResultIterator resultIterator;
	private IResultMetaData resultMetaData;
	private IResultMetaData bindingMetaData;
	
	// sub query info
	private String subQueryName;
	// if this is sub query, it needs to know its parent index and then
	// it can determins the sub query index in its group level.
	private int currParentIndex;
	
	private IPreparedQuery dummyPreparedQuery;
	
	private String tempDir;
	
	private IBaseQueryResults outer;
	
	private ExecutorHelper executorHelper;
	private String name;
	private List<IGroupInstanceInfo> targetGroups;
	/**
	 * 
	 * @param tempDir
	 * @param context
	 * @param queryResultID
	 * @param outer
	 */
	public QueryResults( String tempDir, DataEngineContext context,
			String queryResultID, IBaseQueryResults outer, List<IGroupInstanceInfo> targetGroups  )
	{
		this( tempDir, context, null, queryResultID, null, null, -1, outer, targetGroups );
	}
	
	/**
	 * @param context
	 * @param queryResultID
	 */
	public QueryResults( String tempDir, DataEngineContext context, String queryResultID )
	{
		this( tempDir, context, null, queryResultID, null, null, -1, null, null );
	}
	
	/**
	 * @param context
	 * @param queryResultID
	 * @param resultMetaData
	 * @param subQueryName
	 * @param currParentIndex
	 */
	QueryResults( String tempDir, DataEngineContext context, String baseResultID, String queryResultID,
			IResultMetaData resultMetaData, String subQueryName,
			int currParentIndex, IBaseQueryResults parentQueryResults, List<IGroupInstanceInfo> targetGroups )
	{
		assert tempDir != null;
		assert context != null;
		assert queryResultID != null;
		if ( subQueryName != null )
			assert resultMetaData != null;
		this.tempDir = tempDir;
		this.context = context;
		this.queryResultID = queryResultID;
		this.baseQueryResultID = baseResultID;
		this.outer = parentQueryResults;
		this.resultMetaData = resultMetaData;
		this.subQueryName = subQueryName;
		this.currParentIndex = currParentIndex;
		this.targetGroups = targetGroups;
	}
	
	/**
	 * 
	 * @return
	 */
	private IPreparedQuery populateDummyPreparedQuery( )
	{
		IPreparedQuery result = null;
		try
		{
			String rootQueryResultID = QueryResultIDUtil.get1PartID( queryResultID );
			String parentQueryResultID = null;
			if ( rootQueryResultID != null )
				parentQueryResultID = QueryResultIDUtil.get2PartID( queryResultID );
			else
				rootQueryResultID = queryResultID;

			if ( this.subQueryName != null )
			{
				rootQueryResultID = baseQueryResultID;
			}
			QueryResultInfo queryResultInfo = new QueryResultInfo( rootQueryResultID,
					parentQueryResultID,
					null,
					null,
					-1 );
			RDLoad rdLoad = RDUtil.newLoad( tempDir, context, queryResultInfo );

			IBaseQueryDefinition qd = rdLoad.loadQueryDefn( StreamManager.ROOT_STREAM,
					StreamManager.BASE_SCOPE );
			if ( qd instanceof IQueryDefinition )
				result = new DummyPreparedQuery( (IQueryDefinition) qd, this );
		}
		catch ( DataException e )
		{
		}
		return result;
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.api.IQueryResults#getPreparedQuery()
	 */
	public IPreparedQuery getPreparedQuery( )
	{
		if( this.dummyPreparedQuery == null )
			this.dummyPreparedQuery = this.populateDummyPreparedQuery( );
		
		return this.dummyPreparedQuery;
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.IQueryResults#getResultMetaData()
	 */
	public IResultMetaData getResultMetaData( ) throws BirtException
	{		
		if ( resultMetaData == null )
		{
			this.resultMetaData = getRDLoad( subQueryName, queryResultID ).loadResultMetaData( );
		}

		return resultMetaData;
	}
	
	/**
	 * retrieve the binding meta data information.
	 * 
	 * @return
	 * @throws DataException
	 */
	public IResultMetaData getBindingMetaData( ) throws DataException
	{
		if ( bindingMetaData == null )
		{
			ExprMetaInfo[] metaInfo = getRDLoad( subQueryName, queryResultID ).loadExprMetaInfo( );

			if ( ExprMetaUtil.isBasedOnRD( metaInfo ) )
			{
				ExprMetaInfo[] infos = new ExprMetaInfo[metaInfo.length - 1];

				for ( int i = 0, k = 0; i < infos.length; i++, k++ )
				{
					if ( isInternalMetaInfo( metaInfo[k] ) )
						i--;
					else
						infos[i] = metaInfo[k];
				}
				bindingMetaData = new BindingMetaData( infos );
			}
			else
			{
				bindingMetaData = new BindingMetaData( metaInfo );
			}
		}
		return bindingMetaData;
	}

	/**
	 * Checks whether the ExprMetaInfo is for the internal use
	 * 
	 * @param exprMeta
	 * @return
	 */
	private static boolean isInternalMetaInfo( ExprMetaInfo exprMeta )
	{
		return ExprMetaUtil.POS_NAME.equals( exprMeta.getName( ) );
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.IQueryResults#getResultIterator()
	 */
	public IResultIterator getResultIterator( ) throws BirtException
	{
		if ( resultIterator == null )
		{
			if ( subQueryName == null ) // not a sub query
			{
				IBaseQueryDefinition queryDefn = this.getRDLoad( null, queryResultID )
						.loadQueryDefn( StreamManager.ROOT_STREAM,
								StreamManager.SELF_SCOPE );
				if ( ((IQueryDefinition)queryDefn).isSummaryQuery( ))
				{
					resultIterator = new ResultIterator2( tempDir,
							context,
							this,
							queryResultID,
							queryDefn.getGroups( ).size( ), true, queryDefn );
				}
				else if ( queryDefn.usesDetails( ) == true
						|| queryDefn.cacheQueryResults( ) )
				{
					if ( this.targetGroups != null
							&& this.targetGroups.size( ) > 0 )
						resultIterator = new PLSEnabledResultIterator( this.targetGroups,
								new ResultIterator( tempDir,
										context,
										this,
										queryResultID, queryDefn ) );
					else
						resultIterator = new ResultIterator( tempDir,
								context,
								this,
								queryResultID, queryDefn );
				}
				else
				{
					if ( this.targetGroups != null
							&& this.targetGroups.size( ) > 0 )
						resultIterator = new PLSEnabledResultIterator( this.targetGroups,
								new ResultIterator2( tempDir,
										context,
										this,
										queryResultID,
										queryDefn.getGroups( ).size( ), false, queryDefn ) );
					else
						resultIterator = new ResultIterator2( tempDir,
								context,
								this,
								queryResultID,
								queryDefn.getGroups( ).size( ), false, queryDefn );
				}
			}
			else
			{
				ISubqueryDefinition subQuery = this.getRDLoad( null,
						this.baseQueryResultID )
						.loadSubQueryDefn( StreamManager.ROOT_STREAM,
								StreamManager.SELF_SCOPE,
								subQueryName );
				if ( subQuery == null )
					throw new DataException( ResourceConstants.SUBQUERY_NOT_FOUND, subQueryName );
				if ( subQuery.usesDetails( ) == true )
				{
					resultIterator = new ResultIterator( tempDir,
							context,
							this,
							queryResultID,
							subQueryName,
							currParentIndex, subQuery );
				}
				else
					resultIterator = new ResultIterator2( tempDir,
							context,
							this,
							queryResultID,
							subQueryName,
							currParentIndex,
							subQuery.getGroups( ).size( ), subQuery );

			}
		}

		return resultIterator;
	}
	
	/**
	 * @return
	 * @throws DataException
	 */
	public RDLoad getRDLoad( String subQueryName, String queryResultID ) throws DataException
	{
		String baseID = QueryResultIDUtil.get1PartID( queryResultID );
		if ( baseID == null )
			baseID = queryResultID;
		RDLoad rdLoad = RDUtil.newLoad( tempDir, context, new QueryResultInfo( baseID,
				subQueryName,
				currParentIndex ) );
		return rdLoad;
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.api.IQueryResults#close()
	 */
	public void close( ) throws BirtException
	{
		if( resultIterator!= null )
			resultIterator.close( );
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.IQueryResults#getName()
	 */
	public String getID( )
	{
		return this.queryResultID;
	}

	public void cancel( )
	{
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Used for Result Set Sharing.
	 *
	 */
	private static class DummyPreparedQuery implements IPreparedQuery
	{
		private IQueryDefinition queryDefn;
		private IQueryResults results;
		
		/**
		 * 
		 * @param queryDefn
		 * @param context
		 */
		public DummyPreparedQuery( IQueryDefinition queryDefn, IQueryResults queryResults )
		{
			this.queryDefn = queryDefn;
			this.results = queryResults;
		}
		
		/*
		 * (non-Javadoc)
		 * @see org.eclipse.birt.data.engine.api.IPreparedQuery#execute(org.mozilla.javascript.Scriptable)
		 */
		public IQueryResults execute( Scriptable queryScope )
				throws BirtException
		{
			return this.results;
		}

		/*
		 * (non-Javadoc)
		 * @see org.eclipse.birt.data.engine.api.IPreparedQuery#execute(org.eclipse.birt.data.engine.api.IQueryResults, org.mozilla.javascript.Scriptable)
		 */
		public IQueryResults execute( IQueryResults outerResults,
				Scriptable queryScope ) throws BirtException
		{
			throw new UnsupportedOperationException();
		}

		/*
		 * (non-Javadoc)
		 * @see org.eclipse.birt.data.engine.api.IPreparedQuery#getParameterMetaData()
		 */
		public Collection getParameterMetaData( ) throws BirtException
		{
			return null;
		}

		/*
		 * (non-Javadoc)
		 * @see org.eclipse.birt.data.engine.api.IPreparedQuery#getReportQueryDefn()
		 */
		public IQueryDefinition getReportQueryDefn( )
		{
			return this.queryDefn;
		}

		public IQueryResults execute( IBaseQueryResults outerResults,
				Scriptable scope ) throws DataException
		{
			throw new UnsupportedOperationException();
		}
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.impl.IQueryService#getDataSetRuntime(int)
	 */
	public DataSetRuntime[] getDataSetRuntime( int nestedCount )
	{
		return new DataSetRuntime[0];
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.impl.IQueryService#getExecutorHelper()
	 */
	public IExecutorHelper getExecutorHelper( ) throws DataException
	{
		if( this.executorHelper != null )
		{
			return this.executorHelper;
		}
		else
		{
			IExecutorHelper parent = null;
			if( this.outer!= null )
			{
				if( this.outer instanceof IQueryService )
				{
					parent = ( (IQueryService) this.outer ).getExecutorHelper( );
				}
			}
			
			this.executorHelper = new ExecutorHelper( parent  );
			this.executorHelper.setScriptable( new DummyJSResultSetRow( parent, this.resultIterator ) );
			return this.executorHelper;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.impl.IQueryService#getNestedLevel()
	 */
	public int getNestedLevel( )
	{
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.impl.IQueryService#getQueryScope()
	 */
	public Scriptable getQueryScope( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.impl.IQueryService#isClosed()
	 */
	public boolean isClosed( )
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 *
	 */
	private static class DummyJSResultSetRow extends ScriptableObject
	{
		//
		private IExecutorHelper parentHelper;
		private IBaseResultIterator currentIterator;
		
		/**
		 * 
		 * @param parentHelper
		 * @param currentIterator
		 */
		DummyJSResultSetRow( IExecutorHelper parentHelper, IBaseResultIterator currentIterator )
		{
			this.parentHelper = parentHelper;
			this.currentIterator = currentIterator;
		}
		
		/*
		 * (non-Javadoc)
		 * @see org.mozilla.javascript.ScriptableObject#get(java.lang.String, org.mozilla.javascript.Scriptable)
		 */
		public Object get( String name, Scriptable scope )
		{
			if( ScriptConstants.OUTER_RESULT_KEYWORD.equalsIgnoreCase( name ) )
			{
				if( this.parentHelper == null )
				{
					throw Context.reportRuntimeError( DataResourceHandle.getInstance( ).getMessage( ResourceConstants.NO_OUTER_RESULTS_EXIST ) );
				}
				else
					return this.parentHelper.getScriptable( );
			}
			try
			{
				return this.currentIterator.getValue( name );
			}
			catch ( BirtException e )
			{
				return e;
			}
		}
		
		public String getClassName( )
		{
			return "DummyJSResultSetRow";
		}
	}
	
	/**
	 * Encapsulation for bindings' meta data as an IResultMetaData object.
	 */
	private static class BindingMetaData implements IResultMetaData
	{

		private ExprMetaInfo[] metaInfo;

		BindingMetaData( ExprMetaInfo[] metaInfo )
		{
			assert metaInfo != null;
			this.metaInfo = metaInfo;
		}

		public String getColumnAlias( int index ) throws BirtException
		{
			return null;
		}

		public int getColumnCount( )
		{
			return metaInfo.length;
		}

		public String getColumnLabel( int index ) throws BirtException
		{
			return null;
		}

		public String getColumnName( int index ) throws BirtException
		{
			return metaInfo[index - 1].getName( );
		}

		public String getColumnNativeTypeName( int index ) throws BirtException
		{
			return null;
		}

		public int getColumnType( int index ) throws BirtException
		{
			return metaInfo[index - 1].getDataType( );
		}

		public String getColumnTypeName( int index ) throws BirtException
		{
			return DataType.getName( getColumnType( index ) );
		}

		public boolean isComputedColumn( int index ) throws BirtException
		{
			return false;
		}

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.api.IBaseQueryResults#setName(java.lang.String)
	 */
	public void setName( String name )
	{
		this.name = name;
	}

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

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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IColumnDefinition;
import org.eclipse.birt.data.engine.api.IComputedColumn;
import org.eclipse.birt.data.engine.api.IJoinCondition;
import org.eclipse.birt.data.engine.api.IJointDataSetDesign;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.SortDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.BaseQuery;
import org.eclipse.birt.data.engine.executor.DataSetCacheManager;
import org.eclipse.birt.data.engine.executor.JointDataSetQuery;
import org.eclipse.birt.data.engine.executor.ResultClass;
import org.eclipse.birt.data.engine.executor.ResultFieldMetadata;
import org.eclipse.birt.data.engine.executor.dscache.DataSetResultCache;
import org.eclipse.birt.data.engine.executor.dscache.DataSourceQuery;
import org.eclipse.birt.data.engine.executor.transform.CachedResultSet;
import org.eclipse.birt.data.engine.impl.jointdataset.IJoinConditionMatcher;
import org.eclipse.birt.data.engine.impl.jointdataset.JoinConditionMatcher;
import org.eclipse.birt.data.engine.impl.jointdataset.JointDataSetPopulatorFactory;
import org.eclipse.birt.data.engine.impl.jointdataset.JointResultMetadata;
import org.eclipse.birt.data.engine.odi.IDataSetPopulator;
import org.eclipse.birt.data.engine.odi.IDataSource;
import org.eclipse.birt.data.engine.odi.IEventHandler;
import org.eclipse.birt.data.engine.odi.IPreparedDSQuery;
import org.eclipse.birt.data.engine.odi.IQuery;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultObjectEvent;

/**
 * This is an extension of PreparedDataSourceQuery. It is used to provide joint data set service.
 */
public class PreparedJointDataSourceQuery extends PreparedDataSourceQuery
{
	private static final String COLUMN_NAME_SPLITTER = "::";
	//
	private IJointDataSetDesign dataSet;
	private IDataSetPopulator populator;
	private IResultClass resultClass;
	private ResultIterator left;
	private ResultIterator right;
	private IJoinConditionMatcher matcher;
	private int joinType;
	
	private DataEngineImpl dataEngine;
	private IBaseDataSetDesign dataSetDesign;
	private Map appContext;
	
	/**
	 * Constructor.
	 * 
	 * @param dataEngine
	 * @param queryDefn
	 * @param dataSetDesign
	 * @param appContext
	 * @throws DataException
	 */
	PreparedJointDataSourceQuery( DataEngineImpl dataEngine,
			IQueryDefinition queryDefn, IBaseDataSetDesign dataSetDesign,
			Map appContext ) throws DataException
	{
		super( dataEngine, queryDefn, dataSetDesign, appContext );
		this.dataEngine = dataEngine;
		this.dataSetDesign = dataSetDesign;
		this.appContext = appContext;
	}
	
	/**
	 * Initialize the instance.
	 * 
	 * @param dataEngine
	 * @param dataSetDesign
	 * @param appContext
	 * @throws DataException
	 */
	private void initialize( DataEngineImpl dataEngine, Map appContext )
			throws DataException
	{
		int savedCacheOption = DataSetCacheManager.getInstance( ).suspendCache( );

		ResultIterator left = getSortedResultIterator( dataEngine,
				dataSet.getLeftDataSetDesignName( ),
				appContext,
				dataSet.getJoinConditions( ),
				true );
		ResultIterator right = getSortedResultIterator( dataEngine,
				dataSet.getRightDataSetDesignName( ),
				appContext,
				dataSet.getJoinConditions( ),
				false );
		
		DataSetCacheManager.getInstance( ).setCacheOption( savedCacheOption );

		this.left = left;
		this.right = right;
		this.joinType = dataSet.getJoinType( );
		this.matcher = new JoinConditionMatcher( left.getOdiResult( ),
				right.getOdiResult( ),
				left.getScope( ),
				right.getScope( ),
				dataSet.getJoinConditions( ) );

		JointResultMetadata meta = getJointResultMetadata( left, right );

		resultClass = meta.getResultClass( );
	}

	/**
	 * @param dataSetDesign
	 */
	private void setCurrentDataSet( IBaseDataSetDesign dataSetDesign )
	{
		dataSet = (IJointDataSetDesign) dataSetDesign;
	}
	
	/**
	 * Return an instance of JointResultMeta.
	 * 
	 * @param left
	 * @param right
	 * @return
	 * @throws DataException
	 */
	private JointResultMetadata getJointResultMetadata( ResultIterator left,
			ResultIterator right ) throws DataException
	{
		String leftPrefix = dataSet.getLeftDataSetDesignName( );
		String rightPrefix = dataSet.getRightDataSetDesignName( );
		if ( leftPrefix.equals( rightPrefix ) )
		{
			leftPrefix = leftPrefix + "1";
			rightPrefix = rightPrefix + "2";
		}

		leftPrefix = leftPrefix + COLUMN_NAME_SPLITTER;
		rightPrefix = rightPrefix + COLUMN_NAME_SPLITTER;
		JointResultMetadata meta = populatorJointResultMetadata( left.getOdiResult( )
				.getResultClass( ),
				leftPrefix,
				right.getOdiResult( ).getResultClass( ),
				rightPrefix );
		return meta;
	}

	/**
	 * Populate the JointResultMetadata.
	 * 
	 * @param left
	 * @param leftPrefix
	 * @param right
	 * @param rightPrefix
	 * @return
	 * @throws DataException
	 */
	private JointResultMetadata populatorJointResultMetadata(
			IResultClass left, String leftPrefix, IResultClass right,
			String rightPrefix ) throws DataException
	{
		int length = left.getFieldCount( )
		+ right.getFieldCount( )+ ((dataSet.getComputedColumns( )==null)?0:dataSet.getComputedColumns( ).size( ));
		int[] index = new int[length];
		int[] columnSource = new int[length];
		List projectedColumns = new ArrayList( );

		for ( int i = 1; i <= left.getFieldCount( ); i++ )
		{
			index[i - 1] = i;
			columnSource[i - 1] = JointResultMetadata.COLUMN_TYPE_LEFT;
			projectedColumns.add( new ResultFieldMetadata( i,
					leftPrefix + left.getFieldName( i ),
					leftPrefix + left.getFieldName( i ),
					left.getFieldValueClass( i ),
					left.getFieldNativeTypeName( i ),
					false ) );
		}
		for ( int i = left.getFieldCount( ) + 1; i <= left.getFieldCount( )+right.getFieldCount( ); i++ )
		{
			index[i - 1] = i - left.getFieldCount( );
			columnSource[i - 1] = JointResultMetadata.COLUMN_TYPE_RIGHT;
			projectedColumns.add( new ResultFieldMetadata( i,
					rightPrefix
							+ right.getFieldName( i - left.getFieldCount( ) ),
					rightPrefix
							+ right.getFieldName( i - left.getFieldCount( ) ),
					right.getFieldValueClass( i - left.getFieldCount( ) ),
					right.getFieldNativeTypeName( i - left.getFieldCount( ) ),
					false ) );
		}
		if( dataSet.getComputedColumns( )!= null)
		{
			for( int i = 0; i <dataSet.getComputedColumns( ).size( ); i++)
			{
				IComputedColumn cc = (IComputedColumn)dataSet.getComputedColumns( ).get(i);
				index[i + left.getFieldCount( ) + right.getFieldCount( ) ] = -1;
				columnSource[i + left.getFieldCount( ) + right.getFieldCount( )] = JointResultMetadata.COLUMN_TYPE_COMPUTED;
				projectedColumns.add( new ResultFieldMetadata( i,
						cc.getName( ),
						cc.getName( ),
						DataType.getClass( cc.getDataType( ) ),
						null,
						true ) );
			}
		}
		if ( dataSet.getResultSetHints( ) != null )
		{
			List hintList = dataSet.getResultSetHints( );
			for ( int i = 0; i < hintList.size( ); i++ )
			{
				IColumnDefinition columnDefinition = (IColumnDefinition) hintList.get( i );
				for ( int j = 0; j < projectedColumns.size( ); j++ )
				{
					ResultFieldMetadata resultFieldMetadata = (ResultFieldMetadata) projectedColumns.get( j );
					if ( columnDefinition.getColumnName( )
							.equals( resultFieldMetadata.getName( ) ) )
					{
						resultFieldMetadata.setAlias( columnDefinition.getAlias( ) );
						break;
					}
				}
			}
		}
		ResultClass resultClass = new ResultClass( projectedColumns );
		return new JointResultMetadata( resultClass, columnSource, index );
	}
	
	/**
	 * Return the ResultIterator which is sorted using expression in join condition.
	 * @param dataEngine
	 * @param appContext
	 * @return
	 * @throws DataException
	 * @throws BirtException
	 */
	private ResultIterator getSortedResultIterator( DataEngineImpl dataEngine,
			String dataSetDesignName, Map appContext,
			List joinConditions, boolean isLeftDataSet ) throws DataException
	{
		QueryDefinition queryDefinition = new QueryDefinition( );
		queryDefinition.setDataSetName( dataSetDesignName );
		IPreparedQuery preparedQuery;
		try
		{
			preparedQuery = PreparedDataSourceQuery.newInstance( dataEngine,
					queryDefinition,
					appContext );
			for ( int i = 0; i < joinConditions.size( ); i++ )
			{
				addSortToPreparedQuery( (IJoinCondition) joinConditions.get( i ),
						isLeftDataSet,
						preparedQuery );
			}
			IResultIterator ri = preparedQuery.execute( null )
					.getResultIterator( );

			assert ri instanceof ResultIterator;
			return (ResultIterator) ri;
		}
		catch ( BirtException e )
		{
			throw DataException.wrap(e);
		}

	}

	/**
	 * Add a sort expression to preparedQuery.
	 * @param joinConditions
	 * @param isLeftDataSet
	 * @param preparedQuery
	 */
	private void addSortToPreparedQuery( IJoinCondition condition,
			boolean isLeftDataSet, IPreparedQuery preparedQuery )
	{
		IScriptExpression sortExpression;

		if ( isLeftDataSet )
		{
			sortExpression = condition.getLeftExpression( );
		}
		else
		{
			sortExpression = condition.getRightExpression( );
		}

		SortDefinition sort = new SortDefinition( );
		sort.setExpression( sortExpression.getText( ) );

		preparedQuery.getReportQueryDefn( ).getSorts( ).add( sort );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.impl.PreparedDataSourceQuery#newExecutor()
	 */
	protected QueryExecutor newExecutor( )
	{
		return new JointDataSetQueryExecutor( );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.api.IPreparedQuery#getParameterMetaData()
	 */
	public Collection getParameterMetaData( ) throws BirtException
	{
		return null;
	}

	 /**
     * 
	 * Concrete class of DSQueryExecutor used in PreparedExtendedDSQuery
	 * 
	 */
	private class JointDataSetQueryExecutor extends DSQueryExecutor
	{
		// prepared query
		private IPreparedDSQuery odiPreparedQuery;
		
		/**
		 * @return prepared query
		 */
		public IPreparedDSQuery getPreparedOdiQuery( )
		{
			return odiPreparedQuery;
		}
		
		/*
		 * @see org.eclipse.birt.data.engine.impl.PreparedQuery.Executor#createOdiDataSource()
		 */
		protected IDataSource createOdiDataSource( ) throws DataException
		{
			return null;
		}
		
		/*
		 * @see org.eclipse.birt.data.engine.impl.PreparedQuery.Executor#createOdiQuery()
		 */
		protected IQuery createOdiQuery( ) throws DataException
		{
			setCurrentDataSet( dataSetDesign );
			
			// Lazzily initialize the PreparedJointDataSourceQuery here.
			// The creation of JointDataSetQuery need IResultClass instance
			// as input argment.We have to create ResultIterator instance
			// in method "initialize()" for this is the only way to acquire
			// IResultClass instances.
			if ( doesLoadFromCache( ) == false )
				initialize( dataEngine, appContext );
			return new JointDataSetQuery( resultClass );
		}
		
		/*
		 * @see org.eclipse.birt.data.engine.impl.PreparedQuery.Executor#executeOdiQuery()
		 */
		protected org.eclipse.birt.data.engine.odi.IResultIterator executeOdiQuery(
				IEventHandler eventHandler ) throws DataException
		{
			if ( doesLoadFromCache( ) == true )
			{
				DataSourceQuery dsQuery = new DataSourceQuery( );
				
				JointDataSetQuery jointQuery = (JointDataSetQuery) odiQuery;
				dsQuery.setExprProcessor( jointQuery.getExprProcessor( ) );
				List fetchEvents = jointQuery.getFetchEvents( );
				if ( fetchEvents != null )
					for ( int i = 0; i < fetchEvents.size( ); i++ )
						dsQuery.addOnFetchEvent( (IResultObjectEvent) fetchEvents.get( i ) );
				dsQuery.setMaxRows( jointQuery.getMaxRows( ) );
				dsQuery.setOrdering( toList( jointQuery.getOrdering( ) ) );
				dsQuery.setGrouping( toList( jointQuery.getGrouping( ) ) );
				
				return dsQuery.execute( eventHandler );
			}
			
			JointResultMetadata jrm = getJointResultMetadata( left, right );
			resultClass = jrm.getResultClass( );
			populator = JointDataSetPopulatorFactory.getBinaryTreeDataSetPopulator( left.getOdiResult( ),
					right.getOdiResult( ),
					jrm,
					matcher,
					joinType );
			
			if ( doesSaveToCache( ) == false )
				return new CachedResultSet( (BaseQuery) this.odiQuery,
						resultClass,
						populator,
						eventHandler );
			else
				return new CachedResultSet( (BaseQuery) this.odiQuery,
						resultClass,
						new DataSetResultCache( populator, resultClass ),
						eventHandler );
		}
		
		/**
		 * @param obs
		 * @return
		 */
		private List toList( Object[] obs )
		{
			if ( obs == null )
				return null;

			List obList = new ArrayList( );
			for ( int i = 0; i < obs.length; i++ )
				obList.add( obs[i] );

			return obList;
		}
		
		/**
		 * @return
		 */
		private boolean doesLoadFromCache( )
		{
			return DataSetCacheManager.getInstance( ).doesLoadFromCache( );
		}
		
		/**
		 * @return
		 */
		private boolean doesSaveToCache( )
		{
			return DataSetCacheManager.getInstance( ).doesSaveToCache( );
		}
	}
	
}

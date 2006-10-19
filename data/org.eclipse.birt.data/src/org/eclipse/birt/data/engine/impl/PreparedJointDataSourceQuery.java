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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IColumnDefinition;
import org.eclipse.birt.data.engine.api.IComputedColumn;
import org.eclipse.birt.data.engine.api.IJoinCondition;
import org.eclipse.birt.data.engine.api.IJointDataSetDesign;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.InputParameterBinding;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.SortDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.BaseQuery;
import org.eclipse.birt.data.engine.executor.JointDataSetQuery;
import org.eclipse.birt.data.engine.executor.ResultClass;
import org.eclipse.birt.data.engine.executor.ResultFieldMetadata;
import org.eclipse.birt.data.engine.executor.dscache.DataSetResultCache;
import org.eclipse.birt.data.engine.executor.dscache.DataSourceQuery;
import org.eclipse.birt.data.engine.executor.transform.CachedResultSet;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
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
 * This is an extension of PreparedDataSourceQuery. It is used to provide joint
 * data set service.
 */
public class PreparedJointDataSourceQuery extends PreparedDataSourceQuery
{

	private static final String COLUMN_NAME_SPLITTER = "::";

	private static final String TEMP_COLUMN_STRING = ".*$TEMP_.*";

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
	
	private Collection parameterBindings;

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
		this.parameterBindings = queryDefn.getInputParamBindings( );
	}

	/**
	 * Initialize the instance. The method includes heavyweight operations
	 * such as ResultIterator population.
	 * 
	 * @param dataEngine
	 * @param appContext
	 * @throws DataException
	 */
	private void initialize( DataEngineImpl dataEngine, Map appContext )
			throws DataException
	{
		int savedCacheOption = getDataSetCacheManager()
				.suspendCache( );

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

		getDataSetCacheManager().setCacheOption( savedCacheOption );

		this.left = left;
		this.right = right;
		this.joinType = dataSet.getJoinType( );
		this.matcher = new JoinConditionMatcher( left.getOdiResult( ),
				right.getOdiResult( ),
				left.getScope( ),
				right.getScope( ),
				dataSet.getJoinConditions( ) );
	}

	/**
	 * Initialize the resultClass. This method is lightweight.
	 * 
	 * @param dataEngine
	 * @param dataSetDesign
	 * @param appContext
	 * @throws DataException
	 */
	private void initializeResultClass( DataEngineImpl dataEngine, Map appContext )
			throws DataException
	{
		try
		{
			IQueryResults left = getResultSetQuery( dataEngine,
					dataSet.getLeftDataSetDesignName( ),
					appContext,
					dataSet.getJoinConditions( ),
					true );

			IQueryResults right = getResultSetQuery( dataEngine,
					dataSet.getRightDataSetDesignName( ),
					appContext,
					dataSet.getJoinConditions( ),
					false );

			JointResultMetadata meta = getJointResultMetadata( left.getResultMetaData( ),
					right.getResultMetaData( ) );

			resultClass = meta.getResultClass( );
		}
		catch ( BirtException be )
		{
			throw DataException.wrap( be );
		}
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
	private JointResultMetadata getJointResultMetadata( IResultMetaData left,
			IResultMetaData right ) throws DataException
	{
		// TODO: enhance me
		if ( left == null || right == null )
		{
			throw new DataException( ResourceConstants.UNEXPECTED_ERROR );
		}
		try
		{
			String leftPrefix = getDataSetName(dataSet.getLeftDataSetDesignName( ));
			String rightPrefix = getDataSetName(dataSet.getRightDataSetDesignName( ));
			if ( leftPrefix.equals( rightPrefix ) )
			{
				leftPrefix = leftPrefix + "1";
				rightPrefix = rightPrefix + "2";
			}

			leftPrefix = leftPrefix + COLUMN_NAME_SPLITTER;
			rightPrefix = rightPrefix + COLUMN_NAME_SPLITTER;
			JointResultMetadata meta = populatorJointResultMetadata( left,
					leftPrefix,
					right,
					rightPrefix );
			return meta;
		}
		catch ( BirtException be )
		{
			throw DataException.wrap( be );
		}
	}

	/**
	 * Get the data set name with out library name prefix. This fix
	 * is for bugzilla bug 155848. 
	 * 
	 * @param qualifiedName
	 * @return
	 */
	private String getDataSetName( String qualifiedName )
	{
		return ExpressionUtil.getDataSetNameWithoutPrefix( qualifiedName );
	}
	
	/**
	 * Gets the Java class used to represent the specified data type.
	 * 
	 * @param typeCode
	 * @return
	 */
	private Class getTypeClass( int typeCode )
	{
		return DataType.getClass( typeCode );
	}

	/**
	 * Gets the size of the temp customer columns
	 * 
	 * @param metaData
	 * @return
	 * @throws BirtException
	 */
	private int getTempColumnSize( IResultMetaData metaData )
			throws BirtException
	{
		int size = 0;
		for ( int i = 1; i <= metaData.getColumnCount( ); i++ )
		{
			if ( isTempColumn( metaData.getColumnName( i ) ) )
			{
				size++;
			}
			else
			{
				assert ( size == 0 );
			}
		}
		return size;
	}

	/**
	 * Check whether a column is a temp column according to column name.
	 * @param columnName
	 * @return
	 */
	private boolean isTempColumn( String columnName )
	{
		if ( columnName.length( ) < 7 )
		{
			return false;
		}
		if ( !columnName.matches( TEMP_COLUMN_STRING ) )
		{
			return false;
		}
		return true;
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
			IResultMetaData left, String leftPrefix, IResultMetaData right,
			String rightPrefix ) throws DataException
	{
		try
		{
			int leftTempColumnSize = getTempColumnSize( left );
			int rightTempColumnSize = getTempColumnSize( right );
			int length = ( left.getColumnCount( ) - leftTempColumnSize )
					+ ( right.getColumnCount( ) - rightTempColumnSize )
					+ ( ( dataSet.getComputedColumns( ) == null ) ? 0
							: dataSet.getComputedColumns( ).size( ) );
			int[] index = new int[length];
			int[] columnSource = new int[length];
			List projectedColumns = new ArrayList( );

			for ( int i = 1; i <= ( left.getColumnCount( ) - leftTempColumnSize ); i++ )
			{
				index[i - 1] = i;
				columnSource[i - 1] = JointResultMetadata.COLUMN_TYPE_LEFT;
				projectedColumns.add( new ResultFieldMetadata( i,
						leftPrefix + left.getColumnName( i ),
						leftPrefix + left.getColumnName( i ),
						getTypeClass( left.getColumnType( i ) ),
						left.getColumnNativeTypeName( i ),
						false ) );
			}
			for ( int i = ( left.getColumnCount( ) - leftTempColumnSize ) + 1; i <= ( left.getColumnCount( ) - leftTempColumnSize )
					+ ( right.getColumnCount( ) - rightTempColumnSize ); i++ )
			{
				index[i - 1] = i
						- ( left.getColumnCount( ) - leftTempColumnSize );
				columnSource[i - 1] = JointResultMetadata.COLUMN_TYPE_RIGHT;
				projectedColumns.add( new ResultFieldMetadata( i,
						rightPrefix
								+ right.getColumnName( i
										- ( left.getColumnCount( ) - leftTempColumnSize ) ),
						rightPrefix
								+ right.getColumnName( i
										- ( left.getColumnCount( ) - leftTempColumnSize ) ),
						getTypeClass( right.getColumnType( i
								- ( left.getColumnCount( ) - leftTempColumnSize ) ) ),
						right.getColumnNativeTypeName( i
								- ( left.getColumnCount( ) - leftTempColumnSize ) ),
						false ) );
			}

			if ( dataSet.getComputedColumns( ) != null )
			{
				for ( int i = 0; i < dataSet.getComputedColumns( ).size( ); i++ )
				{
					IComputedColumn cc = (IComputedColumn) dataSet.getComputedColumns( )
							.get( i );
					index[i
							+ ( left.getColumnCount( ) - leftTempColumnSize )
							+ ( right.getColumnCount( ) - rightTempColumnSize )] = -1;
					columnSource[i
							+ ( left.getColumnCount( ) - leftTempColumnSize )
							+ ( right.getColumnCount( ) - rightTempColumnSize )] = JointResultMetadata.COLUMN_TYPE_COMPUTED;
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
		catch ( BirtException be )
		{
			throw DataException.wrap( be );
		}
	}

	/**
	 * Return the IQueryResults
	 * 
	 * @param dataEngine
	 * @param dataSetDesignName
	 * @param appContext
	 * @param joinConditions
	 * @param isLeftDataSet
	 * @return
	 * @throws DataException
	 */
	private IQueryResults getResultSetQuery( DataEngineImpl dataEngine,
			String dataSetDesignName, Map appContext, List joinConditions,
			boolean isLeftDataSet ) throws DataException
	{
		QueryDefinition queryDefinition = new QueryDefinition( );
		
		queryDefinition.setDataSetName( dataSetDesignName );
		setParameterBindings( dataEngine, dataSetDesignName, isLeftDataSet, queryDefinition );
		
		IPreparedQuery preparedQuery;
		try
		{
			preparedQuery = PreparedQueryUtil.newInstance( dataEngine,
					queryDefinition,
					appContext );

			IQueryResults ri = preparedQuery.execute( null );

			// assert ri instanceof ResultIterator;
			return ri;
		}
		catch ( BirtException e )
		{
			throw new DataException( e.getMessage( ) );
		}
	}

	/**
	 * Return the ResultIterator which is sorted using expression in join
	 * condition.
	 * 
	 * @param dataEngine
	 * @param appContext
	 * @return
	 * @throws DataException
	 * @throws BirtException
	 */
	private ResultIterator getSortedResultIterator( DataEngineImpl dataEngine,
			String dataSetDesignName, Map appContext, List joinConditions,
			boolean isLeftDataSet ) throws DataException
	{
		QueryDefinition queryDefinition = new QueryDefinition( );
		queryDefinition.setDataSetName( dataSetDesignName );
		setParameterBindings( dataEngine, dataSetDesignName, isLeftDataSet, queryDefinition );
		IPreparedQuery preparedQuery;
		try
		{
			preparedQuery = PreparedQueryUtil.newInstance( dataEngine,
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
	 * 
	 * @param dataEngine
	 * @param dataSetDesignName
	 * @param isLeftDataSet
	 * @param queryDefinition
	 * @throws DataException 
	 */
	private void setParameterBindings( DataEngineImpl dataEngine, String dataSetDesignName, boolean isLeftDataSet, QueryDefinition queryDefinition ) throws DataException
	{
		IBaseDataSetDesign dataSetDesign = dataEngine.getDataSetDesign( dataSetDesignName );
		if( dataSetDesign == null )
		{
			throw new DataException( ResourceConstants.UNDEFINED_DATA_SET, dataSetDesignName );
		}
		Iterator it = dataSetDesign.getParameters( ).iterator( );
		if ( it.hasNext( ) )
		{
			Iterator bindingIt = parameterBindings.iterator( );
			while ( bindingIt.hasNext( ) )
			{
				InputParameterBinding iipb = (InputParameterBinding) bindingIt.next( );
				if(JointDataSetParameterUtil.isDatasetParameter( dataSetDesignName, isLeftDataSet, iipb.getName( ) ))
				{
					queryDefinition.addInputParamBinding( new InputParameterBinding( 
							JointDataSetParameterUtil.extractParameterName( iipb.getName( )  ),
							iipb.getExpr( ) ) );
				}
			}
		}
	}

	/**
	 * Add a sort expression to preparedQuery.
	 * 
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
	 * 
	 * @see org.eclipse.birt.data.engine.impl.PreparedDataSourceQuery#newExecutor()
	 */
	protected QueryExecutor newExecutor( )
	{
		return new JointDataSetQueryExecutor( );
	}

	/*
	 * (non-Javadoc)
	 * 
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
			// as input argment.
			if ( doesLoadFromCache( ) == false )
				initializeResultClass( dataEngine, appContext );
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
				DataSourceQuery dsQuery = new DataSourceQuery( dataEngine.getSession( ) );

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

			initialize( dataEngine, appContext );

			JointResultMetadata jrm = getJointResultMetadata( left.getResultMetaData( ),
					right.getResultMetaData( ) );
			resultClass = jrm.getResultClass( );
			populator = JointDataSetPopulatorFactory.getBinaryTreeDataSetPopulator( left.getOdiResult( ),
					right.getOdiResult( ),
					jrm,
					matcher,
					joinType, dataEngine.getSession( ) );

			if ( doesSaveToCache( ) == false )
				return new CachedResultSet( (BaseQuery) this.odiQuery,
						resultClass,
						populator,
						eventHandler, dataEngine.getSession( ) );
			else
				return new CachedResultSet( (BaseQuery) this.odiQuery,
						resultClass,
						new DataSetResultCache( populator, resultClass, dataEngine.getSession( ) ),
						eventHandler, dataEngine.getSession( ) );
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
			PreparedJointDataSourceQuery self = PreparedJointDataSourceQuery.this;
			return getDataSetCacheManager()
					.doesLoadFromCache( null,
							dataSetDesign,
							null,
							DataSetCacheUtil.getCacheOption( self.dataEngine.getContext( ),
									appContext ),
							self.dataEngine.getContext( ).getCacheCount( ) );
		}

		/**
		 * @return
		 */
		private boolean doesSaveToCache( )
		{
			return getDataSetCacheManager().doesSaveToCache( );
		}
	}
}

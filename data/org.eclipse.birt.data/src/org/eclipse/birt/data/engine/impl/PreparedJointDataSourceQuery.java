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
import org.eclipse.birt.core.script.JavascriptEvalUtil;
import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IBaseDataSourceDesign;
import org.eclipse.birt.data.engine.api.IBaseResultMetaData;
import org.eclipse.birt.data.engine.api.IColumnDefinition;
import org.eclipse.birt.data.engine.api.IComputedColumn;
import org.eclipse.birt.data.engine.api.IJoinCondition;
import org.eclipse.birt.data.engine.api.IJointDataSetDesign;
import org.eclipse.birt.data.engine.api.IParameterDefinition;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.InputParameterBinding;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.SortDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.BaseQuery;
import org.eclipse.birt.data.engine.executor.DataSetCacheManager;
import org.eclipse.birt.data.engine.executor.JointDataSetQuery;
import org.eclipse.birt.data.engine.executor.ResultClass;
import org.eclipse.birt.data.engine.executor.ResultFieldMetadata;
import org.eclipse.birt.data.engine.executor.dscache.DataSetToCache;
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
import org.eclipse.birt.data.engine.odi.IQuery;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultObjectEvent;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

/**
 * This is an extension of PreparedDataSourceQuery. It is used to provide joint
 * data set service.
 */
public class PreparedJointDataSourceQuery extends PreparedDataSourceQuery
{

	private static final String COLUMN_NAME_SPLITTER = "::";

	private static final String TEMP_COLUMN_STRING = "\\Q_{$TEMP\\E.*";

	//
	private IJointDataSetDesign dataSet;
	private IDataSetPopulator populator;
	private IResultClass resultClass;
	private IJoinConditionMatcher matcher;
	private int joinType;

	private DataEngineImpl dataEngine;
	private IBaseDataSetDesign dataSetDesign;
	private Map appContext;
	
	private Collection parameterBindings;
	
	private Collection parameterHints;
	
	private IQueryResults leftQueryResults;
	private IQueryResults rightQueryResults;
	
	private IResultMetaData leftResultMetaData;
	private IResultMetaData rightResultMetaData;
	
	/************************************************************************
	 * These 8 leftXXX / rightXXX fields are here just because of the poor designed 
	 * <p><code>DataSetCacheManager</code> which should not expose  
	 * <p><code>setDataSourceAndDataSet( )<code> and <code>getCurrentXXX( )</code>
	 * <p>methods.
	 * <p>These 8 fields is needless if <code>DataSetCacheManager</code> is
	 * <p>well refactored.
	 ************************************************************************/
	IBaseDataSourceDesign leftDataSourceDesign;
	IBaseDataSetDesign leftDataSetDesgin;
	Collection leftParameterHints;
	Map leftAppContext;
	
	IBaseDataSourceDesign rightDataSourceDesign;
	IBaseDataSetDesign rightDataSetDesgin;
	Collection rightParameterHints;
	Map rightAppContext;
	/************************************************************************/
	
	
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
			Map appContext, IQueryContextVisitor visitor ) throws DataException
	{
		super( dataEngine, queryDefn, dataSetDesign, appContext, visitor );
		Object[] params = {
				dataEngine, queryDefn, dataSetDesign, appContext
		};
		logger.entering( PreparedJointDataSourceQuery.class.getName( ),
				"PreparedJointDataSourceQuery",
				params );
		
		this.dataEngine = dataEngine;
		this.dataSetDesign = dataSetDesign;
		this.appContext = appContext;
		this.parameterBindings = queryDefn.getInputParamBindings( );
		logger.exiting( PreparedJointDataSourceQuery.class.getName( ),
				"PreparedJointDataSourceQuery" );
	}

	/**
	 * Initialize the instance. The method includes heavyweight operations
	 * such as ResultIterator population.
	 * 
	 * @param dataEngine
	 * @param appContext
	 * @throws DataException
	 */
	private void initialize( DataEngineImpl dataEngine, Map appContext, 
			ResultIterator left, ResultIterator right )
	{
		//int savedCacheOption = getDataSetCacheManager( ).suspendCache( );
		this.joinType = dataSet.getJoinType( );
		this.matcher = new JoinConditionMatcher( left.getOdiResult( ),
				right.getOdiResult( ),
				left.getScope( ),
				right.getScope( ),
				dataEngine.getSession( ).getEngineContext( ).getScriptContext( ),
				dataSet.getJoinConditions( ));

	}

	/**
	 * Initialize the resultClass. This method is lightweight.
	 * 
	 * @param dataEngine
	 * @param dataSetDesign
	 * @param appContext
	 * @throws DataException
	 */
	private void initializeResultClass( DataEngineImpl dataEngine,
			Map appContext ) throws DataException
	{
		try
		{
			IResultMetaData leftMetaData = this.leftResultMetaData;
			IResultMetaData rightMetaData = this.rightResultMetaData;
			
			JointResultMetadata meta = getJointResultMetadata( leftMetaData,
					rightMetaData );

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
	private int getTempColumnSize( IBaseResultMetaData metaData )
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
						false, -1 ) );
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
						false, -1 ) );
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
							true, -1 ) );
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
	 * 
	 * @param condition
	 * @param isLeftDataSet
	 * @param query
	 */
	private void addSortToQuery( IJoinCondition condition,
			boolean isLeftDataSet, IQueryDefinition query )
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
		query.getSorts( ).add( sort );
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
	 * cache the left and right queryResults to improve the efficiency
	 * @throws BirtException 
	 */
	private void populatePreparedQuery( IQueryResults outer ) throws BirtException
	{
		this.leftQueryResults = populatePreparedQuery( outer,
				true,
				PreparedJointDataSourceQuery.this.dataSet.getLeftDataSetDesignQulifiedName( ) );
		this.leftResultMetaData = this.leftQueryResults.getResultMetaData( );
		
		DataSetCacheManager dscm = dataEngine.getSession( ).getDataSetCacheManager( );
		
		leftDataSourceDesign = dscm.getCurrentDataSourceDesign( );
		leftDataSetDesgin = dscm.getCurrentDataSetDesign( );
		leftParameterHints = dscm.getCurrentParameterHints( );
		leftAppContext = dscm.getCurrentAppContext( );
		
		this.rightQueryResults = populatePreparedQuery( outer, false,
				PreparedJointDataSourceQuery.this.dataSet.getRightDataSetDesignQulifiedName( ) );
		this.rightResultMetaData = this.rightQueryResults.getResultMetaData( );
		
		rightDataSourceDesign = dscm.getCurrentDataSourceDesign( );
		rightDataSetDesgin = dscm.getCurrentDataSetDesign( );
		rightParameterHints = dscm.getCurrentParameterHints( );
		rightAppContext = dscm.getCurrentAppContext( );
		
		parameterHints = new ArrayList( );
		if ( leftParameterHints != null )
		{
			parameterHints.addAll( leftParameterHints );
		}
		if ( rightParameterHints != null )
		{
			parameterHints.addAll( rightParameterHints );
		}
	}

	/**
	 * 
	 * @param isLeftDataSet
	 * @param dataSetName
	 * @return
	 * @throws DataException
	 */
	private IQueryResults populatePreparedQuery( IQueryResults outer, boolean isLeftDataSet, String dataSetName ) throws DataException
	{
		List conditions = PreparedJointDataSourceQuery.this.dataSet.getJoinConditions( );
		QueryDefinition queryDefinition = new QueryDefinition( true );
		queryDefinition.setAsTempQuery( );
		queryDefinition.setDataSetName( dataSetName );
		setParameterBindings( dataEngine,
				dataSetName,
				isLeftDataSet,
				queryDefinition );

		for ( int i = 0; i < conditions.size( ); i++ )
		{
			addSortToQuery( (IJoinCondition) conditions.get( i ),
					isLeftDataSet,
					queryDefinition );
		}

		IPreparedQuery preparedQuery = PreparedQueryUtil.newInstance( dataEngine,
				queryDefinition,
				appContext );
		try
		{
			return preparedQuery.execute( outer, null );
		}
		catch ( BirtException e )
		{
			throw DataException.wrap( e );
		}
	}

	/**
	 * 
	 * Concrete class of DSQueryExecutor used in PreparedExtendedDSQuery
	 * 
	 */
	private class JointDataSetQueryExecutor extends DSQueryExecutor
	{

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
			try
			{
				populatePreparedQuery( this.tabularOuterResults == null ? null
						: (IQueryResults) this.tabularOuterResults );
			}
			catch ( BirtException e )
			{
				throw DataException.wrap( e );
			}
			// Lazzily initialize the PreparedJointDataSourceQuery here.
			// The creation of JointDataSetQuery need IResultClass instance
			// as input argment.
			//if ( doesLoadFromCache( ) == false )
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
				this.prepareCacheQuery( dsQuery );
				dsQuery.setExprProcessor( jointQuery.getExprProcessor( ) );
				List fetchEvents = jointQuery.getFetchEvents( );
				if ( fetchEvents != null )
					for ( int i = 0; i < fetchEvents.size( ); i++ )
						dsQuery.addOnFetchEvent( (IResultObjectEvent) fetchEvents.get( i ) );
				dsQuery.setMaxRows( jointQuery.getMaxRows( ) );
				dsQuery.setOrdering( toList( jointQuery.getOrdering( ) ) );
				dsQuery.setGrouping( toList( jointQuery.getGrouping( ) ) );
				dsQuery.setQueryDefinition( jointQuery.getQueryDefinition( ) );

				return dsQuery.execute( eventHandler );
			}
			
			ResultIterator left = null;
			ResultIterator right = null;
			try
			{
				DataSetCacheManager dscm = dataEngine.getSession( ).getDataSetCacheManager( );
				dscm.setDataSourceAndDataSet( 
						leftDataSourceDesign, leftDataSetDesgin, leftParameterHints, leftAppContext );
				left = (ResultIterator) leftQueryResults.getResultIterator( );
				registerOutputParams( leftDataSetDesgin, left.getScope( ) );

				dscm.setDataSourceAndDataSet( 
						rightDataSourceDesign, rightDataSetDesgin, rightParameterHints, rightAppContext );
				right = (ResultIterator) rightQueryResults.getResultIterator( );
				registerOutputParams( rightDataSetDesgin, right.getScope( ) );	
			}
			catch ( BirtException e )
			{
				throw DataException.wrap( e );
			}

			initialize( dataEngine, appContext, left, right );

			JointResultMetadata jrm = getJointResultMetadata( left.getResultMetaData( ),
					right.getResultMetaData( ) );
			resultClass = jrm.getResultClass( );
			populator = JointDataSetPopulatorFactory.getBinaryTreeDataSetPopulator( left.getOdiResult( ),
					right.getOdiResult( ),
					jrm,
					matcher,
					joinType, dataEngine.getSession( ),
					dataSetDesign.getRowFetchLimit( ) );
			
			DataSetCacheManager dscm = dataEngine.getSession( ).getDataSetCacheManager( );
			dscm.setDataSourceAndDataSet( 
					null, dataSetDesign, parameterHints, dscm.getCurrentAppContext( ) );
			if ( doesSaveToCache( ) == false )
				return new CachedResultSet( (BaseQuery) this.odiQuery,
						resultClass,
						populator,
						eventHandler, dataEngine.getSession( ));
			else
				return new CachedResultSet( (BaseQuery) this.odiQuery,
						resultClass,
						new DataSetToCache( populator, resultClass, dataEngine.getSession( )),
						eventHandler, dataEngine.getSession( ));
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

		private void registerOutputParams( IBaseDataSetDesign dataSetDesign, Scriptable scope ) throws BirtException
		{
			String dataSetName= dataSetDesign.getName( );
			for ( int i = 0; i < dataSetDesign.getParameters( ).size( ); i++ )
			{
				if ( ( (IParameterDefinition) dataSetDesign.getParameters( )
						.get( i ) ).isOutputMode( ) )
				{
					String paramName = ( (IParameterDefinition) dataSetDesign.getParameters( )
							.get( i ) ).getName( );
					String joinname = JointDataSetParameterUtil.getParameterName( dataSetName,
							paramName );
					Object value = JavascriptEvalUtil.evaluateRawScript( Context.getCurrentContext( ),
							scope,
							JointDataSetParameterUtil.buildOutputParamsExpr( paramName ),
							org.eclipse.birt.core.script.ScriptExpression.defaultID,
							1 );
					dataSet.setOutputParameterValue( joinname, value );
				}
			}
			
		}
		
		/**
		 * @return
		 * @throws DataException 
		 */
		private boolean doesLoadFromCache( ) throws DataException
		{
			return getDataSetCacheManager()
					.doesLoadFromCache( null,
							dataSetDesign,
							parameterHints,
							appContext
							);
		}

		/**
		 * @return
		 * @throws DataException 
		 */
		private boolean doesSaveToCache( ) throws DataException
		{
			return getDataSetCacheManager().doesSaveToCache( );
		}
	}
}

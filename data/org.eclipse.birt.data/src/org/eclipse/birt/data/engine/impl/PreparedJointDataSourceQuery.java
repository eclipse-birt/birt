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
import org.eclipse.birt.data.engine.executor.JointDataSetQuery;
import org.eclipse.birt.data.engine.executor.ResultClass;
import org.eclipse.birt.data.engine.executor.ResultFieldMetadata;
import org.eclipse.birt.data.engine.executor.transform.CachedResultSet;
import org.eclipse.birt.data.engine.impl.jointdataset.JoinConditionMatcher;
import org.eclipse.birt.data.engine.impl.jointdataset.IJoinConditionMatcher;
import org.eclipse.birt.data.engine.impl.jointdataset.JointDataSetPopulatorFactory;
import org.eclipse.birt.data.engine.impl.jointdataset.JointResultMetadata;
import org.eclipse.birt.data.engine.odi.IDataSource;
import org.eclipse.birt.data.engine.odi.IDataSetPopulator;
import org.eclipse.birt.data.engine.odi.IPreparedDSQuery;
import org.eclipse.birt.data.engine.odi.IQuery;
import org.eclipse.birt.data.engine.odi.IResultClass;

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
		initialize( dataEngine, dataSetDesign, appContext );
	}

	/**
	 * Initialize the instance.
	 * 
	 * @param dataEngine
	 * @param dataSetDesign
	 * @param appContext
	 * @throws DataException
	 */
	private void initialize( DataEngineImpl dataEngine,
			IBaseDataSetDesign dataSetDesign, Map appContext )
			throws DataException
	{
		// assign the dataSet.
		dataSet = (IJointDataSetDesign) dataSetDesign;

		ResultIterator left = getSortedResultIterator( dataEngine,
				dataSet.getLeftDataSetDesign( ),
				appContext,
				dataSet.getJoinConditions( ),
				true );
		ResultIterator right = getSortedResultIterator( dataEngine,
				dataSet.getRightDataSetDesign( ),
				appContext,
				dataSet.getJoinConditions( ),
				false );

		IJoinConditionMatcher matcher = new JoinConditionMatcher( left.getOdiResult( ),
				right.getOdiResult( ),
				left.getScope( ),
				right.getScope( ),
				dataSet.getJoinConditions( ) );

		JointResultMetadata meta = getJointResultMetadata( left, right );

		resultClass = getPreparedResultClass( meta.getResultClass( ) );

		populator = JointDataSetPopulatorFactory.getBinaryTreeDataSetPopulator( left.getOdiResult( ),
				right.getOdiResult( ),
				meta,
				matcher,
				dataSet.getJoinType( ) );
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
		String leftPrefix = dataSet.getLeftDataSetDesign( ).getName( );
		String rightPrefix = dataSet.getRightDataSetDesign( ).getName( );
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
		int[] index = new int[left.getFieldCount( ) + right.getFieldCount( )];
		boolean[] isFromLeft = new boolean[left.getFieldCount( )
				+ right.getFieldCount( )];
		List projectedColumns = new ArrayList( );

		for ( int i = 1; i <= left.getFieldCount( ); i++ )
		{
			index[i - 1] = i;
			isFromLeft[i - 1] = true;
			projectedColumns.add( new ResultFieldMetadata( i,
					leftPrefix + left.getFieldName( i ),
					leftPrefix + left.getFieldName( i ),
					left.getFieldValueClass( i ),
					left.getFieldNativeTypeName( i ),
					false ) );
		}
		for ( int i = left.getFieldCount( ) + 1; i <= index.length; i++ )
		{
			index[i - 1] = i - left.getFieldCount( );
			isFromLeft[i - 1] = false;
			projectedColumns.add( new ResultFieldMetadata( i,
					rightPrefix
							+ right.getFieldName( i - left.getFieldCount( ) ),
					rightPrefix
							+ right.getFieldName( i - left.getFieldCount( ) ),
					right.getFieldValueClass( i - left.getFieldCount( ) ),
					right.getFieldNativeTypeName( i - left.getFieldCount( ) ),
					false ) );
		}

		ResultClass resultClass = new ResultClass( projectedColumns );
		return new JointResultMetadata( resultClass, isFromLeft, index );

	}
	
	/**
	 * Return the ResultClass. The result class consists of fields from JointDataSetMeta and 
	 * the computed columns.
	 * @param jointResultClass
	 * @return
	 * @throws DataException
	 */
	private IResultClass getPreparedResultClass( IResultClass jointResultClass )
			throws DataException
	{
		List projectedColumns = new ArrayList( );
		int i;
		for ( i = 1; i <= jointResultClass.getFieldCount( ); i++ )
		{
			projectedColumns.add( new ResultFieldMetadata( i,
					jointResultClass.getFieldName( i ),
					jointResultClass.getFieldName( i ),
					jointResultClass.getFieldValueClass( i ),
					jointResultClass.getFieldNativeTypeName( i ),
					false ) );
		}

		for ( int j = 0; j < dataSet.getComputedColumns( ).size( ); j++ )
		{
			i++;
			IComputedColumn cc = (IComputedColumn) dataSet.getComputedColumns( )
					.get( j );
			projectedColumns.add( new ResultFieldMetadata( i,
					cc.getName( ),
					cc.getName( ),
					DataType.getClass( cc.getDataType( ) ),
					null,
					true ) );
		}
		return new ResultClass( projectedColumns );
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
			IBaseDataSetDesign dataSetDesign, Map appContext,
			List joinConditions, boolean isLeftDataSet ) throws DataException
	{
		QueryDefinition queryDefinition = new QueryDefinition( );
		queryDefinition.setDataSetName( dataSetDesign.getName( ) );
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
			throw new DataException( e.getMessage( ) );
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
		return new JointDataSetQueryExecutor();
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
			return new JointDataSetQuery( resultClass );
	 	}
		
		
		/*
		 * @see org.eclipse.birt.data.engine.impl.PreparedQuery.Executor#executeOdiQuery()
		 */
		protected org.eclipse.birt.data.engine.odi.IResultIterator executeOdiQuery(  )
				 throws DataException
		{
			return new CachedResultSet( (BaseQuery) this.odiQuery,
					resultClass,
					populator );
		}
	}
}

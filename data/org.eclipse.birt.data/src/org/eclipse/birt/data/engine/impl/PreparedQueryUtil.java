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

package org.eclipse.birt.data.engine.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.IGroupDefinition;
import org.eclipse.birt.data.engine.api.IJointDataSetDesign;
import org.eclipse.birt.data.engine.api.IOdaDataSetDesign;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IScriptDataSetDesign;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.script.IBaseDataSetEventHandler;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.expression.ExpressionCompilerUtil;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.document.FilterDefnUtil;
import org.eclipse.birt.data.engine.impl.document.GroupDefnUtil;
import org.eclipse.birt.data.engine.impl.document.QueryDefnUtil;
import org.eclipse.birt.data.engine.impl.document.QueryResultIDUtil;
import org.eclipse.birt.data.engine.impl.document.QueryResultInfo;
import org.eclipse.birt.data.engine.impl.document.RDLoad;
import org.eclipse.birt.data.engine.impl.document.RDUtil;
import org.eclipse.birt.data.engine.impl.document.stream.StreamManager;

/**
 * Create concreate class of IPreparedQuery
 */
class PreparedQueryUtil
{

	/**
	 * Creates a new instance of the proper subclass based on the type of the
	 * query passed in.
	 * 
	 * @param dataEngine
	 * @param queryDefn
	 * @param appContext
	 *            Application context map; could be null.
	 * @return PreparedReportQuery
	 * @throws DataException
	 */
	static IPreparedQuery newInstance( DataEngineImpl dataEngine,
			IQueryDefinition queryDefn, Map appContext ) throws DataException
	{
		assert dataEngine != null;
		assert queryDefn != null;

		if ( queryDefn.getQueryResultsID( ) != null )
			return newIVInstance( dataEngine, queryDefn );

		IBaseDataSetDesign dset = cloneDataSetDesign( dataEngine.getDataSetDesign( queryDefn.getDataSetName( ) ));
		
		if ( dset == null )
		{
			// In new column binding feature, when there is no data set,
			// it is indicated that a dummy data set needs to be created
			// internally. But using the dummy one, the binding expression only
			// can refer to row object and no other object can be refered such
			// as rows.
			if ( queryDefn.getQueryResultsID( ) == null )
				return new PreparedDummyQuery( dataEngine.getContext( ),
						queryDefn,
						dataEngine.getSession().getSharedScope( ) );
		}

		IPreparedQuery preparedQuery;

		if ( dset instanceof IScriptDataSetDesign )
		{
			preparedQuery = new PreparedScriptDSQuery( dataEngine,
					queryDefn,
					dset,
					appContext );
		}
		else if ( dset instanceof IOdaDataSetDesign )
		{
			preparedQuery = new PreparedOdaDSQuery( dataEngine,
					queryDefn,
					dset,
					appContext );
		}
		else if ( dset instanceof IJointDataSetDesign )
		{
			preparedQuery = new PreparedJointDataSourceQuery( dataEngine,
					queryDefn,
					dset,
					appContext );
		}
		else
		{
			throw new DataException( ResourceConstants.UNSUPPORTED_DATASET_TYPE,
					dset.getName( ) );
		}

		return preparedQuery;
	}

	/**
	 * 
	 * @param dataSetDesign
	 * @return
	 * @throws DataException
	 */
	private static IBaseDataSetDesign cloneDataSetDesign(
			IBaseDataSetDesign dataSetDesign ) throws DataException
	{
		if ( dataSetDesign instanceof IScriptDataSetDesign )
		{
			return new ScriptDataSetAdapter( dataSetDesign );
		}
		else if ( dataSetDesign instanceof IOdaDataSetDesign )
		{
			return new OdaDataSetAdapter( dataSetDesign );
		}
		else if ( dataSetDesign instanceof IJointDataSetDesign )
		{
			return new JointDataSetAdapter( dataSetDesign );
		}
		else if ( dataSetDesign == null )
		{
			return null;
		}
		throw new DataException( ResourceConstants.UNSUPPORTED_DATASET_TYPE,
				dataSetDesign.getName( ) );
	}

	/**
	 * @param dataEngine
	 * @param queryDefn
	 * @return
	 * @throws DataException
	 */
	private static IPreparedQuery newIVInstance( DataEngineImpl dataEngine,
			IQueryDefinition queryDefn ) throws DataException
	{
		if ( runQueryOnRS( dataEngine, queryDefn ) )
			return new PreparedIVQuery( dataEngine, queryDefn );
		else
			return new PreparedIVDataSourceQuery( dataEngine, queryDefn );
	}

	/**
	 * Whether query is running based on the result set of report document or
	 * the data set.
	 * 
	 * @param dataEngine
	 * @param queryDefn
	 * @return true, running on result set
	 * @throws DataException
	 */
	private static boolean runQueryOnRS( DataEngineImpl dataEngine,
			IQueryDefinition queryDefn ) throws DataException
	{
		if( !queryDefn.usesDetails( ) )
		{
			queryDefn.getSorts( ).clear( );
		}
		
		String queryResultID = queryDefn.getQueryResultsID( );

		String rootQueryResultID = QueryResultIDUtil.get1PartID( queryResultID );
		String parentQueryResultID = null;
		if ( rootQueryResultID != null )
			parentQueryResultID = QueryResultIDUtil.get2PartID( queryResultID );
		else
			rootQueryResultID = queryResultID;

		QueryResultInfo queryResultInfo = new QueryResultInfo( rootQueryResultID,
				parentQueryResultID,
				null,
				null,
				-1 );
		RDLoad rdLoad = RDUtil.newLoad( dataEngine.getContext( ),
				queryResultInfo );

		boolean runningOnRS = GroupDefnUtil.isEqualGroups( queryDefn.getGroups( ),
				rdLoad.loadGroupDefn( StreamManager.ROOT_STREAM,
						StreamManager.BASE_SCOPE ) );
		if ( runningOnRS == false )
			return false;

		runningOnRS = !hasAggregationInFilter( queryDefn.getFilters( ) );
		if ( runningOnRS == false )
			return false;

		runningOnRS = isCompatibleRSMap( rdLoad.loadQueryDefn( StreamManager.ROOT_STREAM,
				StreamManager.BASE_SCOPE )
				.getResultSetExpressions( ),
				queryDefn.getResultSetExpressions( ) );

		if ( runningOnRS == false )
			return false;
		
		runningOnRS = isCompatibleSubQuery( rdLoad.loadQueryDefn( StreamManager.ROOT_STREAM,
				StreamManager.BASE_SCOPE ),
				queryDefn );

		if ( runningOnRS == false )
			return false;

		IBaseQueryDefinition qd = rdLoad.loadQueryDefn( StreamManager.ROOT_STREAM,
				StreamManager.BASE_SCOPE );
		List filters = qd.getFilters( );

		if ( FilterDefnUtil.isConflictFilter( filters, queryDefn.getFilters( ) ) )
		{
			runningOnRS = false;
			
			FilterDefnUtil.getRealFilterList( rdLoad.loadOriginalQueryDefn( StreamManager.ROOT_STREAM,
					StreamManager.BASE_SCOPE ).getFilters( ), queryDefn.getFilters( ) );
		}

		if ( runningOnRS == false )
			return false;

		// TODO enhance me
		// If the following conditions hold, running on data set
		// 1.There are sorts that different from that of original design
		// 2.The query has subqueries.

		if ( hasSubquery( queryDefn ) )
		{
			if ( !QueryDefnUtil.isEqualSorts( queryDefn.getSorts( ),
					qd.getSorts( ) ) )
			{
				runningOnRS = false;
			}

			Collection subqueries = queryDefn.getSubqueries( );
			List gps = queryDefn.getGroups( );
			if ( gps != null && gps.size( ) > 0 )
			{
				for ( int i = 0; i < gps.size( ); i++ )
				{
					subqueries.addAll( ( (IGroupDefinition) gps.get( i ) ).getSubqueries( ) );
				}
			}
			
			Iterator it = subqueries.iterator( );
			while ( it.hasNext( ) )
			{
				IBaseQueryDefinition query = (IBaseQueryDefinition) it.next( );
				if ( !query.usesDetails( ) )
					query.getSorts( ).clear( );
				if ( query.getFilters( ) != null
						&& query.getFilters( ).size( ) > 0 )
				{
					runningOnRS = false;
					break;
				}
				List groups = query.getGroups( );
				for ( int i = 0; i < groups.size( ); i++ )
				{
					List groupFilters = ( (IGroupDefinition) groups.get( i ) ).getFilters( );
					if ( groupFilters != null && groupFilters.size( ) > 0 )
					{
						runningOnRS = false;
						break;
					}
				}
				if ( runningOnRS == false )
					break;
			}
			
			

		}

		if ( runningOnRS == false )
			return false;

		if ( queryDefn.getFilters( ) != null
				&& queryDefn.getFilters( ).size( ) > 0 )
		{	
			if( !isFiltersEquals(filters, queryDefn.getFilters( )))
			runningOnRS = queryDefn.getResultSetExpressions( ).values( ) == null
					|| !hasAggregationOnRowObjects( queryDefn.getResultSetExpressions( )
							.values( )
							.iterator( ) );
		}
		return runningOnRS;
	}
	
	/**
	 * 
	 * @param oldFilter
	 * @param newFilter
	 * @return
	 */
	private static boolean isFiltersEquals( List oldFilter, List newFilter )
	{
		if( oldFilter.size() != newFilter.size( ))
			return false;
		for( int i = 0; i < oldFilter.size( ); i++ )
		{
			if( !FilterDefnUtil.isEqualFilter( (IFilterDefinition)oldFilter.get(i), (IFilterDefinition)newFilter.get(i )))
					return false;
		}	
		return true;
	}

	/**
	 * @param filters
	 * @return
	 */
	private static boolean hasAggregationInFilter( List filters )
	{
		if ( filters == null || filters.size( ) == 0 )
			return false;

		for ( int i = 0; i < filters.size( ); i++ )
		{
			Object o = ( (IFilterDefinition) filters.get( i ) ).getExpression( );
			if ( o instanceof IConditionalExpression )
			{
				int type = ( (IConditionalExpression) o ).getOperator( );
				if ( type == IConditionalExpression.OP_TOP_N
						|| type == IConditionalExpression.OP_BOTTOM_N
						|| type == IConditionalExpression.OP_TOP_PERCENT
						|| type == IConditionalExpression.OP_BOTTOM_PERCENT )
					return true;
				if ( ExpressionCompilerUtil.hasAggregationInExpr( (IBaseExpression) o ) )
					return true;
			}
		}

		return false;
	}

	/**
	 * @return
	 */
	private static boolean isCompatibleRSMap( Map oldMap, Map newMap )
	{
		if ( oldMap == null )
			return newMap.size( ) == 0;
		else if ( newMap == null )
			return oldMap.size( ) == 0;

		if ( newMap.size( ) > oldMap.size( ) )
			return false;
		
		Iterator it = newMap.keySet( ).iterator( );
		while( it.hasNext( ) )
		{
			Object key = it.next( );
			Object oldObj = oldMap.get( key );
			Object newObj = newMap.get( key );
			if ( oldObj != null )
			{
				if( !isTwoExpressionEqual((IBaseExpression)newObj, (IBaseExpression)oldObj) )
					return false;
			}else
			{
				return false;
			}
 		}
		return true;
	}

	/**
	 * 
	 * @param obj1
	 * @param obj2
	 * @return
	 */
	private static boolean isTwoExpressionEqual( IBaseExpression obj1, IBaseExpression obj2 )
	{
		if( obj1 == null && obj2!= null )
			return false;
		if( obj1 != null && obj2 == null )
			return false;
		
		if ( obj1 == null && obj2 == null )
			return true;
			
		if( !obj1.getClass( ).equals( obj2.getClass( ) ))
			return false;
		
		if( obj1 instanceof IScriptExpression )
		{
			return isTwoExpressionEqual( (IScriptExpression)obj1, (IScriptExpression)obj2 );
		}else if ( obj1 instanceof IConditionalExpression )
		{
			return isTwoExpressionEqual( (IConditionalExpression)obj1, (IConditionalExpression)obj2 );
		}
		return false;
	}
	
	/**
	 * Return whether two IScriptExpression instance equals.
	 * @param obj1
	 * @param obj2
	 * @return
	 */
	private static boolean isTwoExpressionEqual( IScriptExpression obj1, IScriptExpression obj2 )
	{
		if ( obj1 == null && obj2 != null )
			return false;
		if ( obj1 != null && obj2 == null )
			return false;
		if ( obj1 == null && obj2 == null )
			return true;
		
		return isTwoStringEqual( obj1.getText( ), obj2.getText( ) )
				&& isTwoStringEqual( obj1.getGroupName( ), obj2.getGroupName( ))
				&& isTwoStringEqual( obj1.getText( ), obj2.getText( ))
				&& obj1.getDataType( ) == obj2.getDataType( );
	}
	
	/**
	 * 
	 * @param obj1
	 * @param obj2
	 * @return
	 */
	private static boolean isTwoExpressionEqual( IConditionalExpression obj1, IConditionalExpression obj2 )
	{
		if( obj1.getOperator( ) != obj2.getOperator( ) )
			return false;
		
		return isTwoStringEqual( obj1.getGroupName( ), obj2.getGroupName( ))
			 	&& isTwoExpressionEqual( obj1.getExpression( ), obj2.getExpression( ))
			 	&& isTwoExpressionEqual( obj1.getOperand1( ), obj2.getOperand1( ))
			 	&& isTwoExpressionEqual( obj1.getOperand2( ), obj2.getOperand2( ));
	}
	
	/**
	 * 
	 * @param s1
	 * @param s2
	 * @return
	 */
	private static boolean isTwoStringEqual( String s1, String s2 )
	{
		if( s1 == null && s2 != null )
			return false;
		
		if( s1 != null && s2 == null )
			return false;
		
		return s1.equals( s2 );
	}
	
	/**
	 * @param oldSubQuery
	 * @param newSubQuery
	 * @return
	 */
	private static boolean isCompatibleSubQuery( IBaseQueryDefinition oldDefn,
			IBaseQueryDefinition newDefn )
	{
		boolean isComp = QueryDefnUtil.isCompatibleSQs( oldDefn.getSubqueries( ),
				newDefn.getSubqueries( ) );

		if ( isComp == false )
			return false;

		Iterator oldIt = oldDefn.getGroups( ).iterator( );
		Iterator newIt = newDefn.getGroups( ).iterator( );
		while ( newIt.hasNext( ) )
		{
			IGroupDefinition oldGroupDefn = (IGroupDefinition) oldIt.next( );
			IGroupDefinition newGroupDefn = (IGroupDefinition) newIt.next( );
			isComp = QueryDefnUtil.isCompatibleSQs( oldGroupDefn.getSubqueries( ),
					newGroupDefn.getSubqueries( ) );
			if ( isComp == false )
				return false;
		}

		return true;
	}

	/**
	 * 
	 * @param query
	 * @return
	 */
	private static boolean hasAggregationOnRowObjects( Iterator it )
	{
		while ( it.hasNext( ) )
		{
			Object o = it.next( );
			if ( ExpressionCompilerUtil.hasAggregationInExpr( (IBaseExpression) o ) )
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @param qd
	 * @return
	 */
	private static boolean hasSubquery( IQueryDefinition qd )
	{
		assert qd != null;
		if ( qd.getSubqueries( ) != null && qd.getSubqueries( ).size( ) > 0 )
		{
			return true;
		}

		if ( qd.getGroups( ) != null )
		{
			for ( int i = 0; i < qd.getGroups( ).size( ); i++ )
			{
				IGroupDefinition gd = (IGroupDefinition) qd.getGroups( )
						.get( i );
				if ( gd.getSubqueries( ) != null
						&& gd.getSubqueries( ).size( ) > 0 )
				{
					return true;
				}
			}
		}
		return false;
	}
}

abstract class DataSetAdapter implements IBaseDataSetDesign
{
	private List computedColumns;
	private IBaseDataSetDesign source;

	public DataSetAdapter( IBaseDataSetDesign source )
	{
	    this.source = source;
	    this.computedColumns = new ArrayList();
	    if( this.source.getComputedColumns( )!= null )
	    {
	    	this.computedColumns.addAll( this.source.getComputedColumns( ) );
	    }
	}
	public String getAfterCloseScript( )
	{
		return this.source.getAfterCloseScript( );
	}
	public String getAfterOpenScript( )
	{
		return this.source.getAfterOpenScript( );
	}
	
	public String getBeforeCloseScript( )
	{
		return this.source.getBeforeCloseScript( );
	}
	public String getBeforeOpenScript( )
	{
		return this.source.getBeforeOpenScript( );
	}
	public int getCacheRowCount( )
	{
		return this.source.getCacheRowCount( );
	}
	public List getComputedColumns( )
	{
		return this.computedColumns;
	}
	public String getDataSourceName( )
	{
		return this.source.getDataSourceName( );
	}
	public IBaseDataSetEventHandler getEventHandler( )
	{
		return this.source.getEventHandler( );
	}
	public List getFilters( )
	{
		return this.source.getFilters( );
	}
	public Collection getInputParamBindings( )
	{
		return this.source.getInputParamBindings( );
	}
	public String getName( )
	{
		return this.source.getName( );
	}
	public String getOnFetchScript( )
	{
		return this.source.getOnFetchScript( );
	}
	public List getParameters( )
	{
		return this.source.getParameters( );
	}
	public List getResultSetHints( )
	{
		return this.source.getResultSetHints( );
	}
	public int getRowFetchLimit( )
	{
		return this.source.getRowFetchLimit( );
	}
	public boolean needDistinctValue( )
	{
		return this.source.needDistinctValue( );
	}
	public void setRowFetchLimit( int max )
	{
		this.source.setRowFetchLimit( max );
	}
}

class OdaDataSetAdapter extends DataSetAdapter implements IOdaDataSetDesign
{
	private IOdaDataSetDesign source;
	
	public OdaDataSetAdapter( IBaseDataSetDesign source )
	{
		super( source );
		this.source = ( IOdaDataSetDesign )source;
	}

	public String getExtensionID( )
	{
		return this.source.getExtensionID( );
	}

	public String getPrimaryResultSetName( )
	{
		return this.source.getPrimaryResultSetName( );
	}

	public Map getPrivateProperties( )
	{
		return this.source.getPrivateProperties( );
	}

	public Map getPublicProperties( )
	{
		return this.source.getPublicProperties( );
	}

	public String getQueryText( )
	{
		return this.source.getQueryText( );
	}
	
}

class JointDataSetAdapter extends DataSetAdapter implements IJointDataSetDesign
{
	private IJointDataSetDesign source;
	
	public JointDataSetAdapter( IBaseDataSetDesign source )
	{
		super( source );
		this.source = ( IJointDataSetDesign )source;
	}

	public List getJoinConditions( )
	{
		return this.source.getJoinConditions( );
	}

	public int getJoinType( )
	{
		return this.source.getJoinType( );
	}

	public String getLeftDataSetDesignName( )
	{
		return this.source.getLeftDataSetDesignName( );
	}

	public String getRightDataSetDesignName( )
	{
		return this.source.getRightDataSetDesignName( );
	}
	
}

class ScriptDataSetAdapter extends DataSetAdapter implements IScriptDataSetDesign
{
	private IScriptDataSetDesign source;
	public ScriptDataSetAdapter( IBaseDataSetDesign source )
	{
		super( source );
		this.source = (IScriptDataSetDesign)source;
	}

	
	public String getCloseScript( )
	{
		return this.source.getCloseScript( );
	}

	public String getDescribeScript( )
	{
		return this.source.getDescribeScript( );
	}

	public String getFetchScript( )
	{
		return this.source.getFetchScript( );
	}

	public String getOpenScript( )
	{
		return this.source.getOpenScript( );
	}
	
}

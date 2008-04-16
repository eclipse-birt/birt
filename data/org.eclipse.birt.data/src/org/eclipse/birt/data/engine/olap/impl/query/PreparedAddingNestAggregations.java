package org.eclipse.birt.data.engine.olap.impl.query;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.StopSign;
import org.eclipse.birt.data.engine.olap.api.query.ICubeOperation;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.data.api.CubeQueryExecutorHelper;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationFunctionDefinition;
import org.eclipse.birt.data.engine.olap.query.view.CalculatedMember;
import org.eclipse.birt.data.engine.olap.query.view.CubeQueryDefinitionUtil;
import org.eclipse.birt.data.engine.olap.util.ICubeAggrDefn;
import org.eclipse.birt.data.engine.olap.util.OlapExpressionUtil;
import org.mozilla.javascript.Scriptable;

/**
 * PreparedCubeOperation for AddingNestAggregations cube operation
 */
public class PreparedAddingNestAggregations implements IPreparedCubeOperation
{
	private AddingNestAggregations cubeOperation;
	private Scriptable scope;
	private CalculatedMember[] newMembers;
	
	public PreparedAddingNestAggregations( AddingNestAggregations cubeOperation, Scriptable scope, int startRsId ) throws DataException
	{
		assert cubeOperation != null;
		this.cubeOperation = cubeOperation;
		this.scope = scope;
		
		ICubeAggrDefn[] aggrDefns = OlapExpressionUtil.getAggrDefnsByNestBinding( Arrays.asList( cubeOperation.getNewBindings( ) ) );
		newMembers = CubeQueryDefinitionUtil.createCalculatedMembersByAggrOnListAndMeasureName( startRsId,
				aggrDefns,
				this.scope );
	}

	public IAggregationResultSet[] execute( ICubeQueryDefinition cubeQueryDefn,
			IAggregationResultSet[] sources, StopSign stopSign )
			throws IOException, BirtException
	{
		AggregationDefinition[] definitions = CubeQueryDefinitionUtil.createAggregationDefinitons( newMembers,
				cubeQueryDefn );
		List<IAggregationResultSet> currentSources = new ArrayList<IAggregationResultSet>( Arrays.asList( sources ) );
		for ( AggregationDefinition definiton : definitions )
		{
			IAggregationResultSet dataSource = findDataSource( currentSources,
					definiton );
			IAggregationResultSet result = CubeQueryExecutorHelper.computeNestAggregation( dataSource,
					definiton,
					stopSign );
			currentSources.add( result );
		}
		return currentSources.toArray( new IAggregationResultSet[0] );
	}

	public ICubeOperation getCubeOperation( )
	{
		return cubeOperation;
	}

	public CalculatedMember[] getNewCalculatedMembers( )
	{
		return newMembers;
	}

	// Find out which IAggregationResultSet to be operated on
	private IAggregationResultSet findDataSource(
			List<IAggregationResultSet> sources,
			AggregationDefinition definition ) throws DataException
	{
		// All the functions in definition have a same measure name in this case
		String baseAggrName = definition.getAggregationFunctions( )[0].getMeasureName( );

		if ( baseAggrName == null )
		{
			throw new DataException( ResourceConstants.NOT_NEST_AGGREGATION_BINDING,
					getAllBindingNames( definition ) );
		}
		for ( IAggregationResultSet resultSet : sources )
		{
			for ( int i = 0; i < resultSet.getAggregationCount( ); i++ )
			{
				String aggrName = resultSet.getAggregationName( i );
				if ( baseAggrName.equals( aggrName ) )
				{
					return resultSet;
				}
			}
		}
		// Failed to find a IAggregationResultSet to be operated on
		throw new DataException( ResourceConstants.NOT_NEST_AGGREGATION_BINDING,
				getAllBindingNames( definition ) );
	}

	/**
	 * @return all binding names contained in definition
	 */
	private static String getAllBindingNames( AggregationDefinition definition )
	{
		StringBuffer allBindingNames = new StringBuffer( "" );
		for ( AggregationFunctionDefinition function : definition.getAggregationFunctions( ) )
		{
			allBindingNames.append( "[" )
					.append( function.getName( ) )
					.append( "]" );
		}
		return allBindingNames.toString( );
	}
}

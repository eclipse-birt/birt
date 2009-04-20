package org.eclipse.birt.data.engine.olap.impl.query;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.aggregation.AggregationManager;
import org.eclipse.birt.data.engine.api.aggregation.IAggrFunction;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.expression.ExpressionCompilerUtil;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.StopSign;
import org.eclipse.birt.data.engine.olap.api.query.ICubeOperation;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationFunctionDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.AggregationResultSetWithOneMoreDummyAggr;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.AggregationHelper;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.MergedAggregationResultSet;
import org.eclipse.birt.data.engine.olap.query.view.CalculatedMember;
import org.eclipse.birt.data.engine.olap.query.view.CubeQueryDefinitionUtil;
import org.eclipse.birt.data.engine.olap.query.view.MeasureNameManager;
import org.eclipse.birt.data.engine.olap.util.CubeAggrDefn;
import org.eclipse.birt.data.engine.olap.util.CubeNestAggrDefn;
import org.eclipse.birt.data.engine.olap.util.OlapExpressionUtil;
import org.mozilla.javascript.Scriptable;

/**
 * PreparedCubeOperation for AddingNestAggregations cube operation
 */
public class PreparedAddingNestAggregations implements IPreparedCubeOperation
{
	private AddingNestAggregations cubeOperation;
	private CubeNestAggrDefn[] aggrDefns;
	private CalculatedMember[] newMembers;
	
	public PreparedAddingNestAggregations( AddingNestAggregations cubeOperation ) throws DataException
	{
		assert cubeOperation != null;
		this.cubeOperation = cubeOperation;
	}
	
	public void prepare( Scriptable scope, ScriptContext cx, MeasureNameManager manager, IBinding[] basedBindings ) throws DataException
	{
		aggrDefns = OlapExpressionUtil.getAggrDefnsByNestBinding( Arrays.asList( cubeOperation.getNewBindings( ) ),
				basedBindings );
		newMembers = CubeQueryDefinitionUtil.addCalculatedMembers( aggrDefns, manager, scope, cx );
	}

	@SuppressWarnings("unchecked")
	public IAggregationResultSet[] execute( ICubeQueryDefinition cubeQueryDefn,
			IAggregationResultSet[] sources, 
			Scriptable scope,
			ScriptContext cx, StopSign stopSign )
			throws IOException, BirtException
	{
		List<IAggregationResultSet> currentSources = new ArrayList<IAggregationResultSet>( Arrays.asList( sources ) );
		int index = 0;
		for ( CubeNestAggrDefn cnaf : aggrDefns )
		{
			if ( stopSign.isStopped( ) ) 
			{
				break;
			}
			List<String> referencedBindings = 
				ExpressionCompilerUtil.extractColumnExpression( 
						cnaf.getBasedExpression( ), ExpressionUtil.DATA_INDICATOR );
			if ( referencedBindings == null || referencedBindings.isEmpty( ) )
			{
				throw new DataException( ResourceConstants.INVALID_AGGR_BINDING_EXPRESSION );
			}
			String firstReference = referencedBindings.get( 0 );
			IAggregationResultSet newArs = null;
			for ( int i=0; i<sources.length && !stopSign.isStopped( ); i++ )
			{
				IAggregationResultSet ars = sources[i];
				if ( !isResultForRunningAggregation(ars) //Currently, Nest aggregation on running aggregation result is not supported.
						&& ars.getAggregationIndex( firstReference ) >= 0 )
				{
					IAggregationResultSet based = new AggregationResultSetWithOneMoreDummyAggr(
							ars, cnaf.getName( ), cnaf.getBasedExpression( ), scope, cx );
					AggregationDefinition[] ads = CubeQueryDefinitionUtil.createAggregationDefinitons( 
							new CalculatedMember[]{newMembers[index]}, cubeQueryDefn );
					newArs = AggregationHelper.execute( based, ads, stopSign )[0];
					break;
				}
			}
			//referenced binding does not exist or not a aggregation which is not running type 
			if ( newArs == null )
			{
				throw new DataException( ResourceConstants.INVALID_AGGR_BINDING_EXPRESSION );
			}
			boolean merged = false;
			if ( !isResultForRunningAggregation( newArs )) //no merge step for running type nest aggregation
			{
				for ( int i=0; i<currentSources.size( ) && !stopSign.isStopped( ); i++ )
				{
					IAggregationResultSet ars = currentSources.get( i );
					if ( !isResultForRunningAggregation( ars ) //not aggregation result for running aggregation
							&& ars.getAggregationCount( ) > 0 //omit edge IAggregationResultSet 
							&& Arrays.deepEquals( ars.getAllLevels( ), newArs.getAllLevels( ) ))
					{
						ars = new MergedAggregationResultSet( ars, newArs );
						currentSources.set( i, ars );
						merged = true;
						break;
					}
				}
			}
			if ( !merged )
			{
				currentSources.add( newArs );
			}
			index++;
		}
		
		return currentSources.toArray( new IAggregationResultSet[0] );
	}

	public ICubeOperation getCubeOperation( )
	{
		return cubeOperation;
	}



	public CubeAggrDefn[] getNewCubeAggrDefns( )
	{
		return aggrDefns;
	}
	
	private static boolean isResultForRunningAggregation( IAggregationResultSet ars ) throws DataException
	{
		AggregationDefinition ad = ars.getAggregationDefinition( );
		if ( ad != null )
		{
			AggregationFunctionDefinition[] afds = ad.getAggregationFunctions( );
			if ( afds != null 
					&& afds.length == 1 )
			{
				String functionName = afds[0].getFunctionName( );
				IAggrFunction af = AggregationManager.getInstance( ).getAggregation( functionName );
				return af != null && af.getType( ) == IAggrFunction.RUNNING_AGGR;
			}
		}
		return false;
	}
}

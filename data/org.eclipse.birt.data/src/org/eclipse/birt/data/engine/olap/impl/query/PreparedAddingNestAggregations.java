package org.eclipse.birt.data.engine.olap.impl.query;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.expression.ExpressionCompilerUtil;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.StopSign;
import org.eclipse.birt.data.engine.olap.api.query.ICubeOperation;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.data.api.CubeQueryExecutorHelper;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.DataSet4AggregationFactory;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.IDataSet4Aggregation;
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
				if ( ars.getAggregationIndex( firstReference ) >= 0 )
				{
					IDataSet4Aggregation ds4aggr = DataSet4AggregationFactory.createDataSet4Aggregation( 
							ars, cnaf.getName( ), cnaf.getBasedExpression( ), scope, cx );
					AggregationDefinition[] ads = CubeQueryDefinitionUtil.createAggregationDefinitons( 
							new CalculatedMember[]{newMembers[index]}, cubeQueryDefn );
					newArs = CubeQueryExecutorHelper.computeNestAggregation( 
							ds4aggr, ads[0], stopSign );
					break;
				}
			}
			//referenced binding does not exist or not a aggregation 
			if ( newArs == null )
			{
				throw new DataException( ResourceConstants.INVALID_AGGR_BINDING_EXPRESSION );
			}
			boolean merged = false;
			for ( int i=0; i<currentSources.size( ) && !stopSign.isStopped( ); i++ )
			{
				IAggregationResultSet ars = currentSources.get( i );
				if ( ars.getAggregationCount( ) > 0 //omit edge IAggregationResultSet 
						&& Arrays.deepEquals( ars.getAllLevels( ), newArs.getAllLevels( ) ))
				{
					ars = new MergedAggregationResultSet( ars, newArs );
					currentSources.set( i, ars );
					merged = true;
					break;
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
}

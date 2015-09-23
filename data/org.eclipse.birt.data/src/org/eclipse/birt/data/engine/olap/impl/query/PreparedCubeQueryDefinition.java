
/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.olap.impl.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseLinkDefinition;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.expression.ExpressionCompilerUtil;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.document.ExprUtil;
import org.eclipse.birt.data.engine.impl.util.DirectedGraph;
import org.eclipse.birt.data.engine.impl.util.DirectedGraph.CycleFoundException;
import org.eclipse.birt.data.engine.impl.util.DirectedGraphEdge;
import org.eclipse.birt.data.engine.impl.util.GraphNode;
import org.eclipse.birt.data.engine.olap.api.query.IComputedMeasureDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ICubeOperation;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IDerivedMeasureDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IMeasureDefinition;
import org.eclipse.birt.data.engine.olap.data.api.DimLevel;
import org.eclipse.birt.data.engine.olap.util.OlapExpressionUtil;
import org.eclipse.birt.data.engine.script.ScriptConstants;


/**
 * Currently, used to transfer nest aggregation bindings to AddingNestAggregations cube operations
 */
public class PreparedCubeQueryDefinition implements ICubeQueryDefinition
{
	private ICubeQueryDefinition cqd;
	
	private String cubeName;
	
	private List<IBinding> realBindings = new ArrayList<IBinding>( );
	
	private Set<IBinding> bindingsForNestAggregation = new HashSet<IBinding>( );
	
	private Map<String, IBinding> nameToBinding = new HashMap<String, IBinding>( );
	
	private ICubeOperation[] realCubeOperations = new ICubeOperation[0];
		
	public PreparedCubeQueryDefinition( ICubeQueryDefinition cqd ) throws DataException
	{
		assert cqd != null;
		this.cqd = cqd;
		this.cubeName = cqd.getName( );
		List<IBinding> bindingsInCubeQuery = new ArrayList<IBinding>( );
		for( Object o : cqd.getBindings( ) )
		{
			bindingsInCubeQuery.add( ( (Binding) o ).clone( ) );
		}
		
		for ( Object o : bindingsInCubeQuery )
		{
			IBinding binding = (IBinding)o;
			if ( nameToBinding.containsKey( binding.getBindingName( ) ))
			{
				throw new DataException( ResourceConstants.DUPLICATED_BINDING_NAME );
			}
			nameToBinding.put( binding.getBindingName( ), binding );
			if ( OlapExpressionUtil.isAggregationBinding( binding ) )
			{
				List<String> referencedBindings = 
					ExpressionCompilerUtil.extractColumnExpression( 
							binding.getExpression( ), ExpressionUtil.DATA_INDICATOR );
				if ( referencedBindings != null && referencedBindings.size( ) > 0 )
				{
					bindingsForNestAggregation.add( binding );
					continue;
				}
			}
			realBindings.add( binding );
		}
		
		List calculatedMeasures = cqd.getDerivedMeasures( );
		if ( calculatedMeasures != null && calculatedMeasures.size( ) > 0 )
			createBindingsForCalculatedMeasures( bindingsInCubeQuery, cqd.getMeasures( ), cqd.getDerivedMeasures( ),
					org.eclipse.birt.data.engine.olap.query.view.CubeQueryDefinitionUtil.populateMeasureAggrOns( cqd ) );
		
		List<ICubeOperation> convertedCubeOperations = getConvertedCubeOperations( );
		List<ICubeOperation> all = new ArrayList<ICubeOperation>( Arrays.asList( cqd.getCubeOperations( )));
		all.addAll( convertedCubeOperations );
		realCubeOperations = all.toArray( new ICubeOperation[0] );
	}
	
	private String getRollUpAggregationFunctionName( String function )
	{
		if ( function.equalsIgnoreCase( "COUNT" )
				|| function.equalsIgnoreCase( "COUNTDISTINCT" )
				|| function.equalsIgnoreCase( "AVE" ) )
		{
			return "SUM";
		}
		return function;
	}
		
	private IBinding getSameBindingInQuery( IBinding binding, List bindings ) throws DataException
	{
		for ( int i = 0; i < bindings.size( ); i++ )
		{
			IBinding b = (IBinding) bindings.get( i );
			if ( b.getDataType( ) != binding.getDataType( ) )
			{
				continue;
			}
			if ( binding.getAggrFunction( ) != null
					&& ( !( binding.getAggrFunction( ).equals( b.getAggrFunction( ) ) ) ) )
			{
				continue;
			}
			if ( !ExprUtil.isEqualExpression( b.getExpression( ),
					binding.getExpression( ) ) )
			{
				continue;
			}
			if ( !ExprUtil.isEqualExpression( b.getFilter( ),
					binding.getFilter( ) ) )
			{
				continue;
			}
			if ( b.getArguments( ).size( ) != binding.getArguments( ).size( ) )
			{
				continue;
			}
			Iterator itr1 = b.getArguments( ).iterator( );
			Iterator itr2 = binding.getArguments( ).iterator( );
			while ( itr1.hasNext( ) )
			{
				IBaseExpression expr1 = (IBaseExpression) itr1.next( );
				IBaseExpression expr2 = (IBaseExpression) itr2.next( );
				if ( !ExprUtil.isEqualExpression( expr1, expr2 ) )
				{
					continue;
				}
			}
			if ( !Arrays.deepEquals( b.getAggregatOns( ).toArray( ),
					binding.getAggregatOns( ).toArray( ) ) )
			{
				continue;
			}
			return b;
		}
		
		return null;
	}
	
	private void createBindingsForCalculatedMeasures( 
			List bindingsInCubeQuery, List measures, List calculatedMeasures, List aggrOns ) throws DataException
	{
		List<String> levelNames = new ArrayList( );
		for ( int i = 0; i < aggrOns.size( ); i++ )
		{
			DimLevel level = (DimLevel) aggrOns.get( i );
			levelNames.add( ExpressionUtil.createJSDimensionExpression( level.getDimensionName( ),
					level.getLevelName( ) ) );
		}

		Map measureMap = new HashMap( );
		for ( int i = 0; i < measures.size( ); i++ )
		{
			measureMap.put( ( (MeasureDefinition) measures.get( i ) ).getName( ),
					measures.get( i ) );
		}
		Map derivedMeasureMap = new HashMap( );
		for ( int i = 0; i < calculatedMeasures.size( ); i++ )
		{
			derivedMeasureMap.put( ( (DerivedMeasureDefinition) calculatedMeasures.get( i ) ).getName( ),
					calculatedMeasures.get( i ) );
		}
		
		Map createdBindings = new HashMap();
		
		//step 1: find the actual measures that a calculated measure refers to. And create internal bindings for them.
		for ( int i = 0; i < calculatedMeasures.size( ); i++ )
		{
			DerivedMeasureDefinition measureDefinition = (DerivedMeasureDefinition) calculatedMeasures.get( i );
			List referencedMeasures = ExpressionCompilerUtil.extractColumnExpression( measureDefinition.getExpression( ),
					ExpressionUtil.MEASURE_INDICATOR );
			for ( int j = 0; j < referencedMeasures.size( ); j++ )
			{
				String measureName = referencedMeasures.get( j ).toString( );
				if ( measureMap.containsKey( measureName ) )
				{
					if ( !createdBindings.containsKey( measureName ) )
					{
						MeasureDefinition md = (MeasureDefinition) measureMap.get( measureName );
						IBinding newBinding = new Binding( "temp_"
								+ measureName );
						newBinding.setDataType( md.getDataType( ) );
						newBinding.setExpression( new ScriptExpression( ExpressionUtil.createJSMeasureExpression( measureName ) ) );
						for ( int a = 0; a < levelNames.size( ); a++ )
							newBinding.addAggregateOn( levelNames.get( a ) );
						if ( md.getAggrFunction( ) != null )
						{
							newBinding.setAggrFunction( getRollUpAggregationFunctionName( md.getAggrFunction( ) ) );
						}
						else
						{
							newBinding.setAggrFunction( null );
							newBinding.getAggregatOns( ).clear( );
						}

						IBinding b = getSameBindingInQuery( newBinding,
								bindingsInCubeQuery );
						if ( b != null )
						{
							createdBindings.put( measureName, b );
						}
						else
						{
							createdBindings.put( measureName, newBinding );
							realBindings.add( newBinding );
						}
					}
				}
			}
		}
		
		//step 2: replace all the expression texts in those bindings in cube query if necessary
		for ( int i = 0; i < bindingsInCubeQuery.size( ); i++ )
		{
			IBinding binding = (IBinding) bindingsInCubeQuery.get( i );
			ScriptExpression expression = cloneExpression( (ScriptExpression) binding.getExpression( ) );
			List measureName = ExpressionCompilerUtil.extractColumnExpression( expression,
					ExpressionUtil.MEASURE_INDICATOR );
			if ( measureName != null && measureName.size( ) > 0
					&& derivedMeasureMap.containsKey( measureName.get( 0 )
							.toString( ) ) )
			{
				expression.setText( "("
						+ ( (ScriptExpression) ( (DerivedMeasureDefinition) derivedMeasureMap.get( measureName.get( 0 )
								.toString( ) ) ).getExpression( ) ).getText( )
						+ ")" );
				expression.setText( getReplacedExpressionText( expression.getText( ),
						measureMap,
						derivedMeasureMap, createdBindings, binding, bindingsInCubeQuery ) );
				expression.setText( expression.getText( ).substring( 1,
						expression.getText( ).length( ) - 1 ) );
				binding.getAggregatOns( ).clear( );
				binding.setAggrFunction( null );
				binding.setExpression( expression );
			}
		}
	}
	
	private ScriptExpression cloneExpression( ScriptExpression expr )
	{
		ScriptExpression nExpr = new ScriptExpression( expr.getText( ), expr.getDataType( ) );
		nExpr.setGroupName( expr.getGroupName( ) );
		nExpr.setHandle( expr.getHandle( ) );
		nExpr.setScriptId( expr.getScriptId( ) );
		return nExpr;
	}
	
	private boolean isObjectEqual( Object a, Object b )
	{
		if ( a == null && b == null )
			return true;
		if ( a != null && b != null )
			return a.equals( b );
		return false;
	}
	
	private String getReplacedExpressionText( String text, Map measureMap,
			Map derivedMeasureMap, Map createdBindings, IBinding binding, List bindingsInCubeQuery ) throws DataException
	{
		List measureNames = ExpressionCompilerUtil.extractColumnExpression( new ScriptExpression( text.substring( 1,
				text.length( ) - 1 ) ),
				ExpressionUtil.MEASURE_INDICATOR );

		for ( int i = 0; i < measureNames.size( ); i++ )
		{
			if ( measureMap.containsKey( measureNames.get( i ).toString( ) ) )
			{	
				IBinding b = (IBinding)createdBindings.get( measureNames.get( i )
					.toString( ));
			
				String bindingName = b.getBindingName( );
				if ( (!Arrays.deepEquals( b.getAggregatOns( ).toArray( ), binding.getAggregatOns( ).toArray( ) ) ) 
						|| !isObjectEqual( b.getAggrFunction( ), binding.getAggrFunction( ) )
						|| !isObjectEqual( b.getFilter( ),  binding.getFilter( ) ) )
				{
					IBinding newBinding = new Binding(bindingName+"_"+binding.getBindingName( ));
					newBinding.setDataType( b.getDataType( ) );
					newBinding.setAggrFunction( binding.getAggrFunction( ) );
					newBinding.setExpression( b.getExpression( ) );
					newBinding.getAggregatOns( ).addAll( binding.getAggregatOns( ) );
					newBinding.setFilter( binding.getFilter( ) );
					IBinding sameBinding = getSameBindingInQuery( newBinding,
							realBindings );
					if ( sameBinding != null )
					{
						bindingName = sameBinding.getBindingName( );
					}
					else
					{
						bindingName = newBinding.getBindingName( );
						realBindings.add( newBinding );
					}
				}
				
				text = text.replace( ExpressionUtil.createJSMeasureExpression( measureNames.get( i )
						.toString( ) ),
						ExpressionUtil.createJSDataExpression( bindingName ));
			}
			else if ( derivedMeasureMap.containsKey( measureNames.get( i )
					.toString( ) ) )
			{
				text = text.replace( ExpressionUtil.createJSMeasureExpression( measureNames.get( i )
						.toString( ) ),
						"(" + ( (ScriptExpression) ( (DerivedMeasureDefinition) derivedMeasureMap.get( measureNames.get( i )
										.toString( ) ) ).getExpression( ) ).getText( )
								+ ")" );
				text = getReplacedExpressionText( text,
						measureMap,
						derivedMeasureMap, createdBindings, binding, bindingsInCubeQuery);
			}
		}

		return text;
	}
	
	public ICubeQueryDefinition getCubeQueryDefinition( )
	{
		return this.cqd;
	}
	
	private List<ICubeOperation> getConvertedCubeOperations( ) throws DataException
	{
		List<ICubeOperation> convertedCubeOperations = new ArrayList<ICubeOperation>( );
		Set<DirectedGraphEdge> edges = new HashSet<DirectedGraphEdge>( );
		for ( IBinding binding : bindingsForNestAggregation )
		{
			List<String> referencedBindings = 
				ExpressionCompilerUtil.extractColumnExpression( 
						binding.getExpression( ), ExpressionUtil.DATA_INDICATOR );
			for ( String name : referencedBindings )
			{
				IBinding reference = nameToBinding.get( name );
				if ( reference != null && bindingsForNestAggregation.contains( reference ))
				{
					edges.add( new DirectedGraphEdge(new GraphNode(binding), new GraphNode(reference) ) );
				}
			}
		}
		GraphNode[] nodes = null;
		try
		{
			nodes = new DirectedGraph( edges ).flattenNodesByDependency( );
		}
		catch ( CycleFoundException e )
		{
			throw new DataException( ResourceConstants.COLUMN_BINDING_CYCLE, ((IBinding)e.getNode( ).getValue( )).getBindingName( ));
		}
		Set<IBinding> processed = new HashSet<IBinding>( );
		for ( GraphNode node : nodes )
		{
			IBinding b = (IBinding)node.getValue( );
			convertedCubeOperations.add( CubeOperationFactory.getInstance( ).createAddingNestAggregationsOperation( new IBinding[]{b} ) );
			processed.add( b );
		}
		
		if ( bindingsForNestAggregation.size( ) > processed.size( ) )
		{
			List<IBinding> left = new ArrayList<IBinding>( );
			for ( IBinding b : bindingsForNestAggregation )
			{
				if ( !processed.contains( b ))
				{
					left.add( b );
				}
			}
			convertedCubeOperations.add( CubeOperationFactory.getInstance( ).createAddingNestAggregationsOperation( left.toArray( new IBinding[0] ) ) );
		}
		return convertedCubeOperations;
	}

	public void addBinding( IBinding binding )
	{
		throw new UnsupportedOperationException("adding binding is not allowed for prepared cube query definition"); //$NON-NLS-1$
	}

	public void addCubeOperation( ICubeOperation cubeOperation )
	{
		throw new UnsupportedOperationException("adding cube operation is not allowed for prepared cube query definition"); //$NON-NLS-1$

	}

	public void addFilter( IFilterDefinition filter )
	{
		cqd.addFilter( filter );

	}

	public void addSort( ISortDefinition sort )
	{
		cqd.addSort( sort );

	}

	public boolean cacheQueryResults( )
	{
		return cqd.cacheQueryResults( );
	}

	public IComputedMeasureDefinition createComputedMeasure(
			String measureName, int type, IBaseExpression expr )
			throws DataException
	{
		return cqd.createComputedMeasure( measureName, type, expr );
	}
	
	public IDerivedMeasureDefinition createDerivedMeasure(
			String measureName, int type, IBaseExpression expr )
			throws DataException
	{
		return cqd.createDerivedMeasure( measureName, type, expr );
	}

	public IEdgeDefinition createEdge( int type )
	{
		return cqd.createEdge( type );
	}

	public IMeasureDefinition createMeasure( String measureName )
	{
		return cqd.createMeasure( measureName );
	}

	public List getBindings( )
	{
		return Collections.unmodifiableList( realBindings );
	}

	public List getComputedMeasures( )
	{
		return cqd.getComputedMeasures( );
	}

	public Set<IBinding> getBindingsForNestAggregation( )
	{
		return this.bindingsForNestAggregation;
	}
	
	public ICubeOperation[] getCubeOperations( )
	{
		return realCubeOperations;
	}

	public IEdgeDefinition getEdge( int type )
	{
		return cqd.getEdge( type );
	}

	public int getFilterOption( )
	{
		return cqd.getFilterOption( );
	}

	public List getFilters( )
	{
		return cqd.getFilters( );
	}

	public List getMeasures( )
	{
		return cqd.getMeasures( );
	}
	
	public List getDerivedMeasures( )
	{
		return cqd.getDerivedMeasures( );
	}

	public String getQueryResultsID( )
	{
		return cqd.getQueryResultsID( );
	}

	public List getSorts( )
	{
		return cqd.getSorts( );
	}

	public void setCacheQueryResults( boolean b )
	{
		cqd.setCacheQueryResults( b );

	}

	public void setFilterOption( int breakHierarchyOption )
	{
		cqd.setFilterOption( breakHierarchyOption );

	}

	public void setQueryResultsID( String id )
	{
		cqd.setQueryResultsID( id );
	}

	public String getName( )
	{
		return this.cubeName;
	}

	public void setName( String name )
	{
		this.cubeName = name;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition#needAccessFactTable()
	 */
	public boolean needAccessFactTable( )
	{
		return cqd.needAccessFactTable( );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition#setNeedAccessFactTable(boolean)
	 */
	public void setNeedAccessFactTable(boolean needAccessFactTable)
	{
		cqd.setNeedAccessFactTable( needAccessFactTable );
	}

	public String getID( )
	{
		return this.cqd.getID( );
	}

	public void setID( String ID )
	{
		cqd.setID( ID );
	}

    /**
     * Clone itself.
     */
    public ICubeQueryDefinition clone( )
    {
        PreparedCubeQueryDefinition cloned = null;
        try
        {
            cloned = new PreparedCubeQueryDefinition( cqd.clone( ) );
        }
        catch ( DataException e )
        {
        }
        return cloned;
    }
    
    @Override
    public Set<IBaseLinkDefinition> getLinks( )
    {
        return this.cqd.getLinks( );
    }

    @Override
    public void addLink( IBaseLinkDefinition link )
    {
        this.cqd.getLinks( ).add( link );
    }
}


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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.expression.ExpressionCompilerUtil;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.util.DirectedGraph;
import org.eclipse.birt.data.engine.impl.util.DirectedGraphEdge;
import org.eclipse.birt.data.engine.impl.util.GraphNode;
import org.eclipse.birt.data.engine.impl.util.DirectedGraph.CycleFoundException;
import org.eclipse.birt.data.engine.olap.api.query.IComputedMeasureDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ICubeOperation;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IMeasureDefinition;
import org.eclipse.birt.data.engine.olap.util.OlapExpressionUtil;


/**
 * Currently, used to transfer nest aggregation bindings to AddingNestAggregations cube operations
 */
public class PreparedCubeQueryDefinition implements ICubeQueryDefinition
{
	private ICubeQueryDefinition cqd;
	
	private List<IBinding> realBindings = new ArrayList<IBinding>( );
	
	private Set<IBinding> bindingsForNestAggregation = new HashSet<IBinding>( );
	
	private Map<String, IBinding> nameToBinding = new HashMap<String, IBinding>( );
	
	public PreparedCubeQueryDefinition( ICubeQueryDefinition cqd ) throws DataException
	{
		assert cqd != null;
		this.cqd = cqd;
		for ( Object o : cqd.getBindings( ) )
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
		convertToCubeOperations( );
	}
	
	private void convertToCubeOperations( ) throws DataException
	{
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
			cqd.addCubeOperation( CubeOperationFactory.getInstance( ).createAddingNestAggregationsOperation( new IBinding[]{b} ) );
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
			cqd.addCubeOperation( CubeOperationFactory.getInstance( ).createAddingNestAggregationsOperation( left.toArray( new IBinding[0] ) ) );
		}
		
		
		
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

	public ICubeOperation[] getCubeOperations( )
	{
		return cqd.getCubeOperations( );
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
		return cqd.getName( );
	}

	public void setName( String name )
	{
		cqd.setName( name );

	}

}

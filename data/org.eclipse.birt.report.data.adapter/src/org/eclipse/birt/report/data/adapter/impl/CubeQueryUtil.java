
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
package org.eclipse.birt.report.data.adapter.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IDimensionDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IHierarchyDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition;
import org.eclipse.birt.data.engine.olap.data.api.DimLevel;
import org.eclipse.birt.data.engine.olap.data.api.cube.IDatasetIterator;
import org.eclipse.birt.data.engine.olap.util.OlapExpressionCompiler;
import org.eclipse.birt.data.engine.olap.util.OlapExpressionUtil;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;
import org.eclipse.birt.report.data.adapter.api.AdapterException;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.ICubeQueryUtil;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.birt.report.model.api.olap.TabularDimensionHandle;
import org.eclipse.birt.report.model.api.olap.TabularHierarchyHandle;


/**
 * 
 */

public class CubeQueryUtil implements ICubeQueryUtil
{
	private DataRequestSession session;
	
	public CubeQueryUtil( DataRequestSession session )
	{
		this.session = session;
	}
	
	/**
	 * @throws DataException 
	 * 
	 */
	public List getReferableBindings( String targetLevel,
			ICubeQueryDefinition cubeDefn, boolean isSort )
			throws AdapterException
	{
		try
		{
			List bindings = cubeDefn.getBindings( );
			if ( bindings == null )
				return new ArrayList( );
			DimLevel target = OlapExpressionUtil.getTargetDimLevel( targetLevel );

			List result = new ArrayList( );

			for ( int i = 0; i < bindings.size( ); i++ )
			{
				IBinding binding = (IBinding) bindings.get( i );
				Set refDimLevel = OlapExpressionCompiler.getReferencedDimLevel( binding.getExpression( ),
						bindings,
						isSort );
				if ( refDimLevel.size( ) > 1 )
					continue;
				if ( !refDimLevel.contains( target ) )
				{
					List aggrOns = binding.getAggregatOns( );
					for ( int j = 0; j < aggrOns.size( ); j++ )
					{
						DimLevel dimLevel = OlapExpressionUtil.getTargetDimLevel( aggrOns.get( j )
								.toString( ) );
						if ( dimLevel.equals( target ) )
						{
							//Only add to result list if the target dimLevel is the leaf level 
							//of its own edge that referenced in aggrOns list.
							if( j == aggrOns.size( ) -1 )
								result.add( binding );
							else
							{
								DimLevel next = OlapExpressionUtil.getTargetDimLevel( aggrOns.get( j+1 )
										.toString( ) );
								if ( getAxisQualifierLevel( next,
										cubeDefn.getEdge( getAxisQualifierEdgeType( dimLevel,
												cubeDefn ) ) ) == null )
									continue;
								else
									result.add( binding );
							}
							break;
						}
					}
					continue;
				}
				result.add( binding );
			}

			return result;
		}
		catch ( DataException e )
		{
			throw new AdapterException( e.getLocalizedMessage( ), e );
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.report.data.adapter.api.DataRequestSession#getReferencedLevels(java.lang.String, java.lang.String, org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition)
	 */
	public List getReferencedLevels( String targetLevel,
			String bindingExpr, ICubeQueryDefinition queryDefn ) throws AdapterException
	{
		try
		{
			List result = new ArrayList();
			DimLevel target = OlapExpressionUtil.getTargetDimLevel( targetLevel );
			
			String bindingName = OlapExpressionCompiler.getReferencedScriptObject( bindingExpr, "data" );
			if( bindingName == null )
				return result;
			IBinding binding = null;
			List bindings = queryDefn.getBindings( );
			for( int i = 0; i < bindings.size( ); i++ )
			{
				IBinding bd = (IBinding)bindings.get( i );
				if( bd.getBindingName( ).equals( bindingName ))
				{
					binding = bd;
					break;
				}
			}
			
			if( binding == null )
			{
				return result;
			}
			
			
			List aggrOns = binding.getAggregatOns( );
			IEdgeDefinition axisQualifierEdge = queryDefn.getEdge( this.getAxisQualifierEdgeType( target,
					queryDefn ) );
			for( int i = 0; i < aggrOns.size( ); i++ )
			{
				DimLevel dimLevel = OlapExpressionUtil.getTargetDimLevel( aggrOns.get( i ).toString( ) );
				ILevelDefinition lvl = getAxisQualifierLevel( dimLevel, axisQualifierEdge); 
				if( lvl != null )
					result.add( lvl );
			}
			return result;
		}
		catch ( DataException e )
		{
			throw new AdapterException( e.getLocalizedMessage( ), e );
		}
	}
	
	/**
	 * 
	 * @param dimLevel
	 * @param edge
	 * @return
	 */
	private ILevelDefinition getAxisQualifierLevel( DimLevel dimLevel, IEdgeDefinition edge )
	{
		if( edge == null )
			return null;
		List dims = edge.getDimensions( );
		for( int i = 0; i < dims.size( ); i++ )
		{
			IDimensionDefinition dim = (IDimensionDefinition)dims.get( i );
			if( !dim.getName( ).equals( dimLevel.getDimensionName( ) ))
				return null;
			IHierarchyDefinition hier = (IHierarchyDefinition)dim.getHierarchy( ).get(0);
			List levels = hier.getLevels( );
			for( int j = 0; j < levels.size( ); j++ )
			{
				ILevelDefinition level = (ILevelDefinition)levels.get( j );
				if( level.getName( ).equals( dimLevel.getLevelName( ) ))
					return level;
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param dimLevel
	 * @param queryDefn
	 * @return
	 */
	private int getAxisQualifierEdgeType( DimLevel dimLevel, ICubeQueryDefinition queryDefn )
	{
		IEdgeDefinition edge = queryDefn.getEdge( ICubeQueryDefinition.COLUMN_EDGE );
		if( edge == null )
			return ICubeQueryDefinition.ROW_EDGE;
		List dims = edge.getDimensions( );
		for( int i = 0; i < dims.size( ); i++ )
		{
			IDimensionDefinition dim = (IDimensionDefinition)dims.get( i );
			if( dim.getName( ).equals( dimLevel.getDimensionName( ) ))
			{
				return ICubeQueryDefinition.ROW_EDGE;
			}
		}
		
		return ICubeQueryDefinition.COLUMN_EDGE;
	}

	/**
	 * 
	 * @param expr
	 * @return
	 */
	public String getReferencedMeasureName( String expr )
	{
		return OlapExpressionCompiler.getReferencedScriptObject( expr, "measure" );
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.report.data.adapter.api.ICubeQueryUtil#getMemberValueIterator(org.eclipse.birt.report.model.api.olap.TabularCubeHandle, java.lang.String, org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition)
	 */
	public Iterator getMemberValueIterator( TabularCubeHandle cubeHandle,
			String dataBindingExpr, ICubeQueryDefinition queryDefn )
			throws AdapterException
	{
		try
		{
			if ( cubeHandle == null
					|| dataBindingExpr == null || queryDefn == null )
				return null;

			Set dimLevels = OlapExpressionCompiler.getReferencedDimLevel( new ScriptExpression( dataBindingExpr ),
					queryDefn.getBindings( ),
					true );
			if ( dimLevels.size( ) == 0 || dimLevels.size( ) > 1 )
				return null;

			DimLevel target = (DimLevel) dimLevels.iterator( ).next( );

			TabularHierarchyHandle hierHandle = (TabularHierarchyHandle) ( cubeHandle.getDimension( target.getDimensionName( ) ).getContent( TabularDimensionHandle.HIERARCHIES_PROP,
					0 ) );

			Map levelValueMap = new HashMap( );

			DataSetIterator it = new DataSetIterator( this.session, hierHandle );
			return new MemberValueIterator( it,
					levelValueMap,
					target.getLevelName( ) );
		}
		catch ( DataException e )
		{
			throw new AdapterException( e.getLocalizedMessage( ), e );
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.report.data.adapter.api.ICubeQueryUtil#getMemberValueIterator(org.eclipse.birt.report.model.api.olap.TabularCubeHandle, java.lang.String, org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition[], java.lang.Object[])
	 */
	public Iterator getMemberValueIterator( TabularCubeHandle cubeHandle,
			String targetLevel, ILevelDefinition[] higherLevelDefns,
			Object[] values ) throws AdapterException
	{
		try
		{
			if ( ( higherLevelDefns == null && values != null )
					|| ( higherLevelDefns != null && values == null )
					|| ( higherLevelDefns.length != values.length )
					|| cubeHandle == null || targetLevel == null )
				return null;
			DimLevel target = OlapExpressionUtil.getTargetDimLevel( targetLevel );
			TabularHierarchyHandle hierHandle = (TabularHierarchyHandle) ( cubeHandle.getDimension( target.getDimensionName( ) ).getContent( TabularDimensionHandle.HIERARCHIES_PROP,
					0 ) );

			Map levelValueMap = new HashMap( );
			for ( int i = 0; i < higherLevelDefns.length; i++ )
			{
				if ( target.getDimensionName( )
						.equals( higherLevelDefns[i].getHierarchy( )
								.getDimension( )
								.getName( ) ) )
				{
					levelValueMap.put( higherLevelDefns[i].getName( ),
							values[i] );
				}
			}
			DataSetIterator it = new DataSetIterator( this.session,
					hierHandle );
			return new MemberValueIterator( it, levelValueMap, target.getLevelName( ));
		}
		catch ( DataException e )
		{
			throw new AdapterException( e.getLocalizedMessage( ), e );
		}

	}
	
	/**
	 * 
	 * @author Administrator
	 *
	 */
	private class MemberValueIterator implements Iterator
	{
		private IDatasetIterator dataSetIterator;
		private boolean hasNext;
		private Map levelValueMap;
		private String targetLevelName;
		private Object currentValue;
		
		public MemberValueIterator( IDatasetIterator it, Map levelValueMap, String targetLevelName )
		{
			this.dataSetIterator = it;
			this.hasNext = true;
			this.levelValueMap = levelValueMap;
			this.targetLevelName = targetLevelName;
			this.next( );
		}
		
		public boolean hasNext( )
		{
			return this.hasNext;
		}

		public Object next( )
		{
			try
			{
				if( !this.hasNext )
					return null;
				Object result = this.currentValue;
				boolean accept = false;
				while( this.dataSetIterator.next( ) )
				{
					accept = true;
					Iterator it = this.levelValueMap.keySet( ).iterator( );
					
					while( it.hasNext())
					{
						String key = it.next( ).toString( );
						Object value = this.levelValueMap.get( key );
						if( ScriptEvalUtil.compare( value, this.dataSetIterator.getValue( this.dataSetIterator.getFieldIndex( key ) ) ) != 0)
						{
							accept = false;
							break;
						}
					}
					if( accept )
					{
						this.currentValue = this.dataSetIterator.getValue( this.dataSetIterator.getFieldIndex( this.targetLevelName ) );
					}
				}
								
				this.hasNext = accept;
				return result;
			}
			catch ( BirtException e )
			{
				return null;
			}
		}

		public void remove( )
		{
			throw new UnsupportedOperationException();
		}
		
	}
}

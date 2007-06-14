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

package org.eclipse.birt.data.engine.olap.util.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.JavascriptEvalUtil;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.olap.api.query.ICubeFilterDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IDimensionDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IHierarchyDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition;
import org.eclipse.birt.data.engine.olap.data.api.CubeQueryExecutorHelper;
import org.eclipse.birt.data.engine.olap.data.api.DimLevel;
import org.eclipse.birt.data.engine.olap.impl.query.CubeFilterDefinition;
import org.eclipse.birt.data.engine.olap.script.OLAPExpressionCompiler;
import org.eclipse.birt.data.engine.olap.util.OlapExpressionCompiler;
import org.eclipse.birt.data.engine.olap.util.OlapExpressionUtil;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * 
 */

public abstract class BaseDimensionFilterEvalHelper implements IJSFilterHelper
{

	protected Scriptable scope;
	protected DummyJSLevels dimObj;
	protected DummyJSAggregationAccessor dataObj;
	protected IBaseExpression expr;
	protected String dimName;
	protected DimLevel[] aggrLevels;
	protected ICubeQueryDefinition queryDefn;
	protected ICubeFilterDefinition cubeFilter;
	protected ILevelDefinition[] axisLevels;
	protected Object[] axisValues;
	protected boolean isAxisFilter;

	public static IJSFilterHelper createFilterHelper( Scriptable parentScope,
			ICubeQueryDefinition queryDefn, IFilterDefinition cubeFilter ) throws DataException
	{
		if( cubeFilter.getExpression( ) instanceof IConditionalExpression )
		{
			IConditionalExpression expr = (IConditionalExpression)cubeFilter.getExpression( );
			if( expr.getOperator( ) == IConditionalExpression.OP_TOP_N 
				|| expr.getOperator( )== IConditionalExpression.OP_BOTTOM_N
				|| expr.getOperator( )== IConditionalExpression.OP_TOP_PERCENT
				|| expr.getOperator( ) == IConditionalExpression.OP_BOTTOM_PERCENT )
				return new TopBottomDimensionFilterEvalHelper( parentScope, queryDefn,cubeFilter );
		}
		
		return new DimensionFilterEvalHelper( parentScope, queryDefn,cubeFilter );
	}
	
	/**
	 * 
	 * @param parentScope
	 * @param queryDefn
	 * @param cubeFilter
	 * @param cx
	 * @throws DataException
	 */
	protected void initialize( Scriptable parentScope,
			ICubeQueryDefinition queryDefn, IFilterDefinition cubeFilter,
			Context cx ) throws DataException
	{
		this.scope = cx.initStandardObjects( );
		this.scope.setParentScope( parentScope );
		this.dataObj = new DummyJSAggregationAccessor();
		
		this.expr = cubeFilter.getExpression( );
		this.dimName = OlapExpressionUtil.getReferencedDimensionName( this.expr, queryDefn.getBindings());
		this.dimObj = new DummyJSLevels( this.dimName);
		this.queryDefn = queryDefn;
		if ( cubeFilter instanceof ICubeFilterDefinition )
		{
			this.cubeFilter = (ICubeFilterDefinition) cubeFilter;
		}
		else
		{
			this.cubeFilter = new CubeFilterDefinition( this.expr );
		}
		
		this.aggrLevels = populateAggrLevels( );

		axisLevels = this.cubeFilter.getAxisQualifierLevels( );
		axisValues = this.cubeFilter.getAxisQualifierValues( );	
		if ( axisLevels == null
				|| axisValues == null || axisLevels.length != axisValues.length )
		{
			this.isAxisFilter = false;
		}
		else
		{
			for ( int i = 0; i < axisLevels.length; i++ )
			{
				if ( axisLevels[i] == null )
					throw new DataException( ResourceConstants.AXIS_LEVEL_CANNOT_BE_NULL );
				if ( axisValues[i] == null )
					throw new DataException( ResourceConstants.AXIS_VALUE_CANNOT_BE_NULL,
							axisLevels[i].getName( ) );				
			}
		}
		this.isAxisFilter = ( axisLevels != null && axisValues != null && axisLevels.length == axisValues.length );
		
		
		if ( this.dimName!= null )
		{	
			DummyJSDimensionObject dimObj = new DummyJSDimensionObject( this.dimObj,
				this.getTargetDimensionLevelNames( ) );

			this.scope.put( "dimension",
				this.scope,
				new DummyJSDimensionAccessor( this.dimName, dimObj ) );
		}	

		if( this.aggrLevels!= null && this.aggrLevels.length > 0 )
		{
			this.scope.put( "data", this.scope, this.dataObj );	
		}else
		{
			this.scope.put( "data",
					this.scope,
					new DummyJSDataAccessor( queryDefn.getBindings( ),
							this.scope ) );
		}
		
		OLAPExpressionCompiler.compile( cx, this.expr );
	}
	/**
	 * @return
	 * @throws DataException
	 */
	protected DimLevel[] populateAggrLevels( ) throws DataException
	{
		// get aggregation level names from the query definition related with
		// the query expression
		String bindingName = OlapExpressionCompiler.getReferencedScriptObject( expr,
				"data" );
		if ( bindingName == null )
			return null;
		for ( Iterator it = queryDefn.getBindings( ).iterator( ); it.hasNext( ); )
		{
			IBinding binding = (IBinding) it.next( );
			if ( binding.getBindingName( ).equals( bindingName ) )
			{
				List aggrs = binding.getAggregatOns( );
				if ( aggrs.size( ) == 0 )
				{
					if ( OlapExpressionCompiler.getReferencedScriptObject( binding.getExpression( ),
							"dimension" ) != null )
						return null;
					// get all level names in the query definition
					List levelList = new ArrayList( );
					// get all levels from the row edge and column edge
					IEdgeDefinition rowEdge = queryDefn.getEdge( ICubeQueryDefinition.ROW_EDGE );
					List rowDims = rowEdge.getDimensions( );
					for ( Iterator i = rowDims.iterator( ); i.hasNext( ); )
					{
						IDimensionDefinition dim = (IDimensionDefinition) i.next( );
						IHierarchyDefinition hirarchy = (IHierarchyDefinition) dim.getHierarchy( )
								.get( 0 );
						for ( Iterator j = hirarchy.getLevels( ).iterator( ); j.hasNext( ); )
						{
							ILevelDefinition level = (ILevelDefinition) j.next( );
							levelList.add( new DimLevel( dim.getName( ),
									level.getName( ) ) );
						}
					}
					IEdgeDefinition colEdge = queryDefn.getEdge( ICubeQueryDefinition.COLUMN_EDGE );
					List colDims = colEdge.getDimensions( );
					for ( Iterator i = colDims.iterator( ); i.hasNext( ); )
					{
						IDimensionDefinition dim = (IDimensionDefinition) i.next( );
						IHierarchyDefinition hirarchy = (IHierarchyDefinition) dim.getHierarchy( )
								.get( 0 );
						for ( Iterator j = hirarchy.getLevels( ).iterator( ); j.hasNext( ); )
						{
							ILevelDefinition level = (ILevelDefinition) j.next( );
							levelList.add( new DimLevel( dim.getName( ),
									level.getName( ) ) );
						}
					}
					DimLevel[] levels = new DimLevel[levelList.size( )];
					levelList.toArray( levels );
					return levels;
				}
				else
				{
					DimLevel[] levels = new DimLevel[aggrs.size( )];
					for ( int i = 0; i < aggrs.size( ); i++ )
					{
						levels[i] = OlapExpressionUtil.getTargetDimLevel( aggrs.get( i )
								.toString( ) );
					}
					return levels;
				}
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.util.filter.IJSFilterHelper#getAggrLevels()
	 */
	public DimLevel[] getAggrLevels( )
	{
		return this.aggrLevels;
	}

	/***
	 * 
	 * @return
	 * @throws DataException
	 */
	protected List getTargetDimensionLevelNames( ) throws DataException
	{
		IDimensionDefinition dimDefn = getTargetDimension( );
		if ( dimDefn == null )
			throw new DataException( "Referenced dimension definition:"+this.dimName+" does not exist.");
		List result = new ArrayList();
		List levels = ((IHierarchyDefinition)dimDefn.getHierarchy( ).get( 0 )).getLevels( );
		for( int j = 0; j < levels.size(); j++ )
		{
			ILevelDefinition level = (ILevelDefinition)levels.get( j );
			result.add( level.getName( ) );
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.util.filter.IJSFilterHelper#getDimensionName()
	 */
	public String getDimensionName( )
	{
		return this.dimName;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.util.filter.IJSFilterHelper#getCubeFiterDefinition()
	 */
	public ICubeFilterDefinition getCubeFilterDefinition( )
	{
		return cubeFilter;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.util.filter.IJSFilterHelper#isAggregationFilter()
	 */
	public boolean isAggregationFilter( )
	{
		return this.dimName == null;
	}

	/**
	 * 
	 * @return
	 * @throws DataException 
	 */
	private IDimensionDefinition getTargetDimension( ) throws DataException
	{
		if ( isAggregationFilter( ) )
		{
			ILevelDefinition targetLevel = cubeFilter.getTargetLevel( );
			if ( targetLevel == null )
			{
				throw new DataException( "Referenced level:"
						+ targetLevel.getName( ) + " does not exist." );
			}
			IDimensionDefinition dimDefn = targetLevel.getHierarchy( )
					.getDimension( );
			if ( dimDefn == null )
			{
				throw new DataException( "Referenced dimension:"
						+ this.dimName + " does not exist." );
			}
			return dimDefn;
		}
		else
		{
			IEdgeDefinition columnEdge = this.queryDefn.getEdge( ICubeQueryDefinition.COLUMN_EDGE );
			IEdgeDefinition rowEdge = this.queryDefn.getEdge( ICubeQueryDefinition.ROW_EDGE );
			List dims = new ArrayList();
			if ( columnEdge != null )
				dims.addAll( columnEdge.getDimensions( ) );
			if ( rowEdge != null )
				dims.addAll( rowEdge.getDimensions( ) );
			
			for ( int i = 0; i < dims.size( ); i++ )
			{
				IDimensionDefinition dimDefn = (IDimensionDefinition)dims.get( i );
				if ( dimDefn.getName( ).equals( this.dimName ))
				{
					return dimDefn;
				}
			}
			return null;			
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.util.filter.IJSFilterHelper#close()
	 */
	public void close( )
	{
		this.scope.delete( "dimension" );
		this.scope.delete( "data" );
		this.scope.setParentScope( null );
	}

	/**
	 * Dummy Java Script Ojbect, used to access "dimension". 
	 *
	 */
	protected class DummyJSDimensionAccessor extends ScriptableObject
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 2973311240789516725L;
		//
		private String dimensionName;
		private DummyJSDimensionObject dimObj;

		/**
		 * Constructor 
		 * @param name
		 * @param dimObj
		 */
		DummyJSDimensionAccessor( String name, DummyJSDimensionObject dimObj )
		{
			assert name != null;
			assert dimObj != null;

			this.dimensionName = name;
			this.dimObj = dimObj;
		}

		/*
		 * (non-Javadoc)
		 * @see org.mozilla.javascript.ScriptableObject#getClassName()
		 */
		public String getClassName( )
		{
			return "DummyJSDimensionAccessor";
		}

		/*
		 * (non-Javadoc)
		 * @see org.mozilla.javascript.ScriptableObject#get(java.lang.String, org.mozilla.javascript.Scriptable)
		 */
		public Object get( String value, Scriptable scope )
		{
			if ( !this.dimensionName.equals( value ) )
				throw new InMatchDimensionIndicator( );
			else
				return this.dimObj;
		}
	}

	/**
	 * A middle layer to access levels in an expression.
	 * 
	 */
	protected class DummyJSDimensionObject extends ScriptableObject
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = -5518881690927904342L;
		//
		private DummyJSLevels levels;
		private List levelNames;

		/**
		 * 
		 * @param levels
		 * @param levelNames
		 */
		DummyJSDimensionObject( DummyJSLevels levels, List levelNames )
		{
			this.levels = levels;
			this.levelNames = levelNames;
		}

		/*
		 * (non-Javadoc)
		 * @see org.mozilla.javascript.ScriptableObject#getClassName()
		 */
		public String getClassName( )
		{
			return "DummyJSDimensionObject";
		}

		/*
		 * (non-Javadoc)
		 * @see org.mozilla.javascript.ScriptableObject#get(java.lang.String, org.mozilla.javascript.Scriptable)
		 */
		public Object get( String value, Scriptable scope )
		{
			if ( this.levelNames.contains( value ) )
			{
				this.levels.setCurrentKey( value );
				return this.levels;
			}
			else
				throw new RuntimeException( "Invalid level Name:" + value );
		}
	}

	/**
	 * 
	 * @author Administrator
	 *
	 */
	protected class DummyJSLevels extends ScriptableObject
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = -3448256508462449221L;
		//
		private IResultRow resultRow;
		private String key;
		private String dimName;
		
		public DummyJSLevels( String dimName )
		{
			this.dimName = dimName;
		}
		
		/*
		 * (non-Javadoc)
		 * @see org.mozilla.javascript.ScriptableObject#getClassName()
		 */
		public String getClassName( )
		{
			return "DummyJSLevels";
		}

		/*
		 * (non-Javadoc)
		 * @see org.mozilla.javascript.ScriptableObject#getDefaultValue(java.lang.Class)
		 */
		public Object getDefaultValue( Class hint )
		{
			try
			{
				return resultRow.getFieldValue( CubeQueryExecutorHelper.getAttrReference( this.dimName,
						this.key,
						this.key ) );
			}
			catch ( DataException e )
			{
				return null;
			}

		}

		/*
		 * (non-Javadoc)
		 * @see org.mozilla.javascript.ScriptableObject#get(java.lang.String, org.mozilla.javascript.Scriptable)
		 */
		public Object get( String value, Scriptable scope )
		{
			try
			{
				return resultRow.getFieldValue( CubeQueryExecutorHelper.getAttrReference( this.dimName,
						this.key,
						value ) );
			}
			catch ( DataException e )
			{
				return null;
			}
		}

		/**
		 * Set the current proceeding level key name.
		 * @param key
		 */
		public void setCurrentKey( String key )
		{
			this.key = key;
		}

		/**
		 * 
		 * @param result
		 */
		public void setResultRow( IResultRow result )
		{
			this.resultRow = result;
		}
	}

	
	protected class InMatchDimensionIndicator extends RuntimeException
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 5546172382226378064L;

	}
	
	/**
	 * Wrapper for "data" script object.
	 *
	 */
	protected class DummyJSDataAccessor extends ScriptableObject
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 6434908701820853543L;
	
		private Map bindingMap;
		private Scriptable scope;
		
		public DummyJSDataAccessor( List bindings, Scriptable scope ) throws DataException
		{
			this.bindingMap = new HashMap();
			for( int i = 0; i < bindings.size(); i++ )
			{
				this.bindingMap.put(((IBinding) bindings.get(i)).getBindingName(),
						bindings.get(i));
			}
			this.scope = scope;
		}

		public Object get(String aggrName, Scriptable scope) 
		{
			try 
			{
				Context cx = Context.enter();
				if ( !this.bindingMap.containsKey(aggrName))
					return null;
				return ScriptEvalUtil.evalExpr( ((IBinding)this.bindingMap.get(aggrName)).getExpression(), cx, this.scope, null,
						0);
			} 
			catch (DataException e) 
			{
				return null;
			}
			finally 
			{
				Context.exit();
			}

		}

		public String getClassName() {
			// TODO Auto-generated method stub
			return null;
		}
	}

	protected class DummyJSAggregationAccessor extends ScriptableObject
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private IResultRow resultRow;
		
		public Object get(String aggrName, Scriptable scope) 
		{
			if( this.resultRow != null )
			{	
				try
				{
					return this.resultRow.getAggrValue( aggrName );
				}
				catch ( DataException e )
				{
					return null;
				}
			}
			else
				return null;
		}
		
		public void setResultRow( IResultRow row )
		{
			this.resultRow = row;
		}
		
		public String getClassName( )
		{
			return "DummyJSAggregationAccessor";
		}
		
	}
}
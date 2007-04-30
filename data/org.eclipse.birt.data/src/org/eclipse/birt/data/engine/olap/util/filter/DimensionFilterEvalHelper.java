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
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.api.query.CubeFilterDefn;
import org.eclipse.birt.data.engine.olap.api.query.ICubeFilterDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IDimensionDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IHierarchyDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition;
import org.eclipse.birt.data.engine.olap.data.util.CompareUtil;
import org.eclipse.birt.data.engine.olap.script.OLAPExpressionCompiler;
import org.eclipse.birt.data.engine.olap.util.OlapExpressionCompiler;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * This is a helper class which provide script evaluation services for dimension filter.
 */
public class DimensionFilterEvalHelper implements IJsFilterHelper
{

	//
	private Scriptable scope;
	private DummyJSLevels dimObj;
	private DummyJSDataAccessor dataObj;
	private IBaseExpression expr;
	private String dimName;
	private String[] aggrLevelNames;
	
	ICubeQueryDefinition queryDefn;
	private ICubeFilterDefinition cubeFilter;
	
	private ILevelDefinition[] axisLevels;
	private Object[] axisValues;
	private boolean isAxisFilter;
	
	/**
	 * @param parentScope
	 * @param queryDefn
	 * @param cubeFilter
	 * @throws DataException
	 */
	public DimensionFilterEvalHelper( Scriptable parentScope,ICubeQueryDefinition queryDefn, IFilterDefinition cubeFilter) throws DataException
	{
		assert cubeFilter!=null;
		Context cx = Context.enter( );
		try
		{
			this.scope = cx.initStandardObjects( );
			this.scope.setParentScope( parentScope );
			this.dimObj = new DummyJSLevels( );
			this.dataObj = new DummyJSDataAccessor();
			
			this.expr = cubeFilter.getExpression( );
			this.dimName = OlapExpressionCompiler.getReferencedScriptObject( expr, "dimension" );
			this.queryDefn = queryDefn;
			if ( cubeFilter instanceof ICubeFilterDefinition )
			{
				this.cubeFilter = (ICubeFilterDefinition) cubeFilter;
			}
			else
			{
				this.cubeFilter = new CubeFilterDefn( this.expr );
			}
			
			this.aggrLevelNames = populateAggrLevelNames( );
			
			axisLevels = this.cubeFilter.getAxisQualifierLevels( );
			axisValues = this.cubeFilter.getAxisQualifierValues( );			
			this.isAxisFilter = ( axisLevels != null && axisValues != null && axisLevels.length == axisValues.length );
			
			
			if ( this.dimName!= null )
			{	
				DummyJSDimensionObject dimObj = new DummyJSDimensionObject( this.dimObj,
					this.getTargetDimensionLevelNames( ) );

				this.scope.put( "dimension",
					this.scope,
					new DummyJSDimensionAccessor( this.dimName, dimObj ) );
			}	
			this.scope.put( "data", this.scope, this.dataObj );
			OLAPExpressionCompiler.compile( cx, this.expr );
		}
		finally
		{
			Context.exit( );
		}
	}
	
	/**
	 * @return
	 * @throws DataException
	 */
	private String[] populateAggrLevelNames( ) throws DataException
	{
		// get aggregation level names from the query definition related with the query expression
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
				{// get all level names in the query definition
					List levels = new ArrayList( );
					// get all levels from the row edge and column edge
					IEdgeDefinition rowEdge = queryDefn.getEdge( ICubeQueryDefinition.ROW_EDGE );
					List rowDims = rowEdge.getDimensions( );
					for ( Iterator i = rowDims.iterator( ); i.hasNext( ); )
					{
						IDimensionDefinition dim = (IDimensionDefinition) i.next( );
						IHierarchyDefinition hirarchy = (IHierarchyDefinition) dim.getHierarchy( )
								.get( 0 );
						levels.addAll( hirarchy.getLevels( ) );
					}
					IEdgeDefinition colEdge = queryDefn.getEdge( ICubeQueryDefinition.COLUMN_EDGE );
					List colDims = colEdge.getDimensions( );
					for ( Iterator i = colDims.iterator( ); i.hasNext( ); )
					{
						IDimensionDefinition dim = (IDimensionDefinition) i.next( );
						IHierarchyDefinition hirarchy = (IHierarchyDefinition) dim.getHierarchy( )
								.get( 0 );
						levels.addAll( hirarchy.getLevels( ) );
					}
					String[] levelNames = new String[levels.size( )];
					for ( int i = 0; i < levels.size( ); i++ )
					{
						ILevelDefinition levelDefn = (ILevelDefinition) levels.get( i );
						levelNames[i] = levelDefn.getName( );
					}
					return levelNames;
				}
				else
				{
					String[] levelNames = new String[aggrs.size( )];
					for ( int i = 0; i < aggrs.size( ); i++ )
					{
						levelNames[i] = aggrs.get( i ).toString( );
					}
					return levelNames;
				}
			}
		}
		return null;
	}
	
	/**
	 * get aggregation all level names.
	 * @return
	 */
	public String[] getAggrLevelNames()
	{
		return this.aggrLevelNames;
	}
	
	
	/***
	 * 
	 * @return
	 * @throws DataException
	 */
	private List getTargetDimensionLevelNames() throws DataException
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
	
	/**
	 * get the dimension name.
	 * @return
	 */
	public String getDimensionName()
	{
		return this.dimName;
	}
	
	/**
	 * 
	 * @return
	 */
	public ICubeFilterDefinition getCubeFiterDefinition()
	{
		return cubeFilter;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.util.filter.IJsFilter#isAggregationFilter()
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
			dims.addAll( columnEdge.getDimensions( ) );
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

	/**
	 * This method should be called before we finish the usage of this class so
	 * that to deregister the "dimension" script object from the scope and deregister
	 * the current scope from its parent.
	 */
	public void close( )
	{
		this.scope.delete( "dimension" );
		this.scope.delete( "data" );
		this.scope.setParentScope( null );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.util.filter.IJsFilter#evaluateFilter(org.eclipse.birt.data.engine.olap.util.filter.IResultRow)
	 */
	public boolean evaluateFilter( IResultRow resultRow )
			throws DataException
	{

		this.dimObj.setResultRow( resultRow );
		this.dataObj.setResultRow( resultRow );
		Context cx = Context.enter( );
		try
		{
			if ( this.isAxisFilter )
			{
				for ( int i = 0; i < axisLevels.length; i++ )
				{
					if ( CompareUtil.compare( resultRow.getLevelValue( axisLevels[i].getName( ) ),
							axisValues[i] ) != 0 )
					{
						return false;
					}
				}
			}
			Object result = ScriptEvalUtil.evalExpr( expr, cx, scope, null, 0 );
			return DataTypeUtil.toBoolean( result ).booleanValue( );
		}
		catch ( InMatchDimensionIndicator e )
		{
			return true;
		}
		catch ( BirtException e )
		{
			throw DataException.wrap( e );
		}
		finally
		{
			Context.exit( );
		}
	}

	/**
	 * Dummy Java Script Ojbect, used to access "dimension". 
	 *
	 */
	private class DummyJSDimensionAccessor extends ScriptableObject
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
	private class DummyJSDimensionObject extends ScriptableObject
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
	private class DummyJSLevels extends ScriptableObject
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = -3448256508462449221L;
		//
		private IResultRow resultRow;
		private String key;
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
				return resultRow.getLevelValue( this.key );
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
				return resultRow.getLevelValue( value );
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

	
	private class InMatchDimensionIndicator extends RuntimeException
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
	private class DummyJSDataAccessor extends ScriptableObject
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 6434908701820853543L;
		private IResultRow resultRow;
		
		public String getClassName( )
		{
			return "DummyJSDataAccessor";
		}
		
		public void setResultRow( IResultRow resultRow )
		{
			this.resultRow = resultRow;
		}
		
		public Object get( String aggrName, Scriptable scope )
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
	}

	
}

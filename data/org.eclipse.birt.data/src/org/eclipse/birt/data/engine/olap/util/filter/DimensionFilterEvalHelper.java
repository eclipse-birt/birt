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
import java.util.List;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IDimensionDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IHierarchyDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition;
import org.eclipse.birt.data.engine.olap.script.OLAPExpressionCompiler;
import org.eclipse.birt.data.engine.olap.util.OlapExpressionCompiler;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * This is a helper class which provide script evaluation services for dimension filter.
 */
public class DimensionFilterEvalHelper
{

	//
	private Scriptable scope;
	private DummyJSLevels dimObj;
	private IBaseExpression expr;
	private String dimName;
	private ICubeQueryDefinition defn;
	/**
	 * The constructor.
	 * @param parentScope
	 * @throws DataException 
	 */
	public DimensionFilterEvalHelper( Scriptable parentScope, ICubeQueryDefinition defn, IBaseExpression expr ) throws DataException
	{

		Context cx = Context.enter( );
		try
		{
			this.scope = cx.initStandardObjects( );
			this.scope.setParentScope( parentScope );
			this.defn = defn;
			this.dimObj = new DummyJSLevels( );
			this.dimName = OlapExpressionCompiler.getReferencedDimensionName( expr );
			this.expr = expr;
			
			DummyJSDimensionObject dimObj = new DummyJSDimensionObject( this.dimObj,
					this.getLevelNames( ) );

			this.scope.put( "dimension",
					this.scope,
					new DummyJSDimensionAccessor( this.dimName, dimObj ) );
			
			OLAPExpressionCompiler.compile( cx, this.expr );
		}
		finally
		{
			Context.exit( );
		}
	}

	private List getLevelNames() throws DataException
	{
		IDimensionDefinition dimDefn = this.getTargetDimension( );
		if ( dimDefn == null )
			throw new DataException( "Referenced dimension:"+this.dimName+" does not exist.");
		
		List result = new ArrayList();
		List levels = ((IHierarchyDefinition)dimDefn.getHierarchy( ).get( 0 )).getLevels( );
		for( int j = 0; j < levels.size(); j++ )
		{
			ILevelDefinition level = (ILevelDefinition)levels.get( j );
			result.add( level.getName( ) );
		}
		
		return result;

	}
	
	public String getDimensionName()
	{
		return this.dimName;
	}
	
	/**
	 * 
	 * @return
	 */
	private IDimensionDefinition getTargetDimension( )
	{
		IEdgeDefinition columnEdge = this.defn.getEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = this.defn.getEdge( ICubeQueryDefinition.ROW_EDGE );
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

	/**
	 * This method should be called before we finish the usage of this class so
	 * that to deregister the "dimension" script object from the scope and deregister
	 * the current scope from its parent.
	 */
	public void close( )
	{
		this.scope.delete( "dimension" );
		this.scope.setParentScope( null );
	}

	/**
	 * This method is used to evaluate the filter expression.
	 *  
	 * @param expr
	 * @param resultRow
	 * @return
	 * @throws DataException
	 */
	public boolean evaluateFilter( IResultRow resultRow )
			throws DataException
	{

		this.dimObj.setResultRow( resultRow );
		Context cx = Context.enter( );
		try
		{
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
				return resultRow.getValue( this.key );
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
				return resultRow.getValue( value );
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

	}
}

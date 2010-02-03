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

package org.eclipse.birt.data.engine.olap.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.core.script.ScriptExpression;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryResults;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.DataResourceHandle;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.olap.api.ICubeQueryResults;
import org.eclipse.birt.data.engine.olap.data.api.cube.TimeDimensionUtil;
import org.eclipse.birt.data.engine.olap.script.JSCubeBindingObject;
import org.eclipse.birt.data.engine.olap.util.filter.IFacttableRow;
import org.eclipse.birt.data.engine.olap.util.filter.IResultRow;
import org.eclipse.birt.data.engine.script.ScriptConstants;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * 
 */

public interface IJSObjectPopulator
{

	/**
	 * @throws DataException
	 * 
	 */
	public void doInit( ) throws DataException;

	/**
	 * 
	 * @param resultRow
	 */
	public void setData( Object resultRow );

	/**
	 * clean up the registered Javascript objects from the scope.
	 */
	public void cleanUp( );

	/**
	 * Dummy Java Script Object, used to access "dimension".
	 * 
	 */
	class DummyJSDimensionAccessor extends ScriptableObject
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 6340543910367862168L;
		//
		private String dimensionName;
		private DummyJSDimensionObject dimObj;

		/**
		 * Constructor
		 * 
		 * @param name
		 * @param dimObj
		 */
		public DummyJSDimensionAccessor( String name,
				DummyJSDimensionObject dimObj )
		{
			assert name != null;
			assert dimObj != null;

			this.dimensionName = name;
			this.dimObj = dimObj;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.mozilla.javascript.ScriptableObject#getClassName()
		 */
		public String getClassName( )
		{
			return "DummyJSDimensionAccessor";//$NON-NLS-1$
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.mozilla.javascript.ScriptableObject#get(java.lang.String,
		 *      org.mozilla.javascript.Scriptable)
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
	class DummyJSDimensionObject extends ScriptableObject
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = -5318363452556444748L;
		//
		private DummyJSLevels levels;
		private List levelNames;

		/**
		 * 
		 * @param levels
		 * @param levelNames
		 */
		public DummyJSDimensionObject( DummyJSLevels levels, List levelNames )
		{
			this.levels = levels;
			this.levelNames = levelNames;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.mozilla.javascript.ScriptableObject#getClassName()
		 */
		public String getClassName( )
		{
			return "DummyJSDimensionObject";//$NON-NLS-1$
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.mozilla.javascript.ScriptableObject#get(java.lang.String,
		 *      org.mozilla.javascript.Scriptable)
		 */
		public Object get( String value, Scriptable scope )
		{
			if( this.levels.isTimeDimLevel( ) )
			{
				if( TimeDimensionUtil.getFieldIndex( value ) == -1 )
					throw new RuntimeException( "Invalid level Name:" + value );//$NON-NLS-1$
			}
			if ( this.levelNames.contains( value ) )
			{
				this.levels.setCurrentKey( value );
				return this.levels;
			}
			else
				throw new RuntimeException( "Invalid level Name:" + value );//$NON-NLS-1$
		}
	}

	/**
	 * 
	 * @author Administrator
	 * 
	 */
	class DummyJSLevels extends ScriptableObject
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 2025085361323969740L;
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
		 * 
		 * @see org.mozilla.javascript.ScriptableObject#getClassName()
		 */
		public String getClassName( )
		{
			return "DummyJSLevels";//$NON-NLS-1$
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.mozilla.javascript.ScriptableObject#getDefaultValue(java.lang.Class)
		 */
		public Object getDefaultValue( Class hint )
		{
			try
			{
				if( resultRow.isTimeDimensionRow( ) )
				{
					return resultRow.getFieldValue( this.key );
				}
				else
				{
					return resultRow.getFieldValue( OlapExpressionUtil.getAttrReference( this.dimName,
						this.key,
						this.key ) );
				}
			}
			catch ( DataException e )
			{
				return null;
			}

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.mozilla.javascript.ScriptableObject#get(java.lang.String,
		 *      org.mozilla.javascript.Scriptable)
		 */
		public Object get( String value, Scriptable scope )
		{
			try
			{
				return resultRow.getFieldValue( OlapExpressionUtil.getAttrReference( this.dimName,
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
		 * 
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
		
		public boolean isTimeDimLevel( )
		{
			return resultRow.isTimeDimensionRow( );
		}
	}

	class InMatchDimensionIndicator extends RuntimeException
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1231475871896514362L;
	}

	/**
	 * Wrapper for "data" script object.
	 * 
	 */
	class DummyJSDataAccessor extends ScriptableObject
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1151785733090446202L;
		private Map bindingMap;
		private Scriptable scope;
		private Scriptable outResultsScriptable;
		private ScriptContext cx;
		public DummyJSDataAccessor( IBaseQueryResults outResults, List bindings, Scriptable scope, ScriptContext cx )
				throws DataException
		{
			this.bindingMap = new HashMap( );
			this.cx = cx;
			for ( int i = 0; i < bindings.size( ); i++ )
			{
				this.bindingMap.put( ( (IBinding) bindings.get( i ) ).getBindingName( ),
						bindings.get( i ) );
			}
			this.scope = scope;
			if ( outResults != null )
			{
				if ( outResults instanceof ICubeQueryResults )
				{
					this.outResultsScriptable = new JSCubeBindingObject( ( (ICubeQueryResults) outResults ).getCubeCursor( ) );
				}
				else if( outResults instanceof IQueryResults )
				{
					try
					{
						this.outResultsScriptable = OlapExpressionUtil.createQueryResultsScriptable( outResults );
					}
					catch ( BirtException e )
					{
						throw DataException.wrap( e );
					}
				}


			}
		}

		public Object get( String aggrName, Scriptable scope )
		{
			try
			{
				if ( !this.bindingMap.containsKey( aggrName ) )
				{
					if( aggrName.equals( ScriptConstants.OUTER_RESULT_KEYWORD )  )
					{
						if ( this.outResultsScriptable == null )
							throw Context.reportRuntimeError( DataResourceHandle.getInstance( ).getMessage( ResourceConstants.NO_OUTER_RESULTS_EXIST ) );
						return this.outResultsScriptable;
					}
					return null;
				}
				return ScriptEvalUtil.evalExpr( ( (IBinding) this.bindingMap.get( aggrName ) ).getExpression( ),
						cx.newContext( this.scope ),
						ScriptExpression.defaultID,
						0 );
			}
			catch ( DataException e )
			{
				return null;
			}

		}

		public String getClassName( )
		{
			// TODO Auto-generated method stub
			return null;
		}
	}

	class DummyJSAggregationAccessor extends ScriptableObject
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = -7910516821739958908L;
		private IResultRow resultRow;
		private Scriptable outResultsScriptable;
		
		public DummyJSAggregationAccessor( IBaseQueryResults outResults )
				throws DataException
		{
			this.outResultsScriptable = OlapExpressionUtil.createQueryResultsScriptable( outResults );
		}

		
		public Object get( String aggrName, Scriptable scope )
		{
			if( aggrName.equals( ScriptConstants.OUTER_RESULT_KEYWORD ) )
			{
				if ( this.outResultsScriptable == null )
					throw Context.reportRuntimeError( DataResourceHandle.getInstance( ).getMessage( ResourceConstants.NO_OUTER_RESULTS_EXIST ) );
				return this.outResultsScriptable;
			}
			
			if ( this.resultRow != null )
			{
				try
				{
					return this.resultRow.getAggrValue( aggrName );
				}
				catch ( DataException e )
				{
					return e;
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
			return "DummyJSAggregationAccessor";//$NON-NLS-1$
		}

	}

	/**
	 * 
	 * @author Administrator
	 *
	 */
	class DummyJSFacttableMeasureAccessor extends ScriptableObject
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = -7910516821739958908L;
		private IFacttableRow resultRow;
		private Map computedMeasures;
		private Scriptable scope;
		private ScriptContext cx;
		/**
		 * 
		 * @param computedMeasures
		 * @param scope
		 */
		public DummyJSFacttableMeasureAccessor( Map computedMeasures, Scriptable scope, ScriptContext cx )
		{
			this.computedMeasures = computedMeasures;
			this.scope = scope;
			this.cx = cx;
		}

		/*
		 * (non-Javadoc)
		 * @see org.mozilla.javascript.ScriptableObject#get(java.lang.String, org.mozilla.javascript.Scriptable)
		 */
		public Object get( String aggrName, Scriptable scope )
		{
			if ( this.resultRow != null )
			{
				try
				{
					if( this.computedMeasures.containsKey( aggrName ))
					{	
						try
						{
							return ScriptEvalUtil.evalExpr( ( (IBaseExpression) this.computedMeasures.get( aggrName ) ),
									cx.newContext( this.scope ),
									ScriptExpression.defaultID,
									0 );
						}catch ( Exception e )
						{
							return null;
						}
					}
					return this.resultRow.getMeasureValue( aggrName );
				}
				catch ( DataException e )
				{
					return null;
				}
			}
			else
				return null;
		}

		/*
		 * 
		 */
		public void setResultRow( IFacttableRow row )
		{
			this.resultRow = row;
		}

		/*
		 * (non-Javadoc)
		 * @see org.mozilla.javascript.ScriptableObject#getClassName()
		 */
		public String getClassName( )
		{
			return "DummyJSFacttableMeasureAccessor";//$NON-NLS-1$
		}

	}
}

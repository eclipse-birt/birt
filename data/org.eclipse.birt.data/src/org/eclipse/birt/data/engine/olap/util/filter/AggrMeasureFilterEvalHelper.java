/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.olap.util.filter;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.core.script.ScriptExpression;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryResults;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.util.DataJSObjectPopulator;
import org.eclipse.birt.data.engine.olap.util.DimensionJSEvalHelper;
import org.eclipse.birt.data.engine.olap.util.IJSObjectPopulator;
import org.eclipse.birt.data.engine.olap.util.OlapExpressionUtil;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * 
 */

public class AggrMeasureFilterEvalHelper extends DimensionJSEvalHelper
		implements
			IAggrMeasureFilterEvalHelper
{
	
	private DummyDimensionObject dimObj;
	
	/**
	 * 
	 * @param scope
	 * @param queryDefn
	 * @param cubeFilter
	 * @throws DataException
	 */
	public AggrMeasureFilterEvalHelper( IBaseQueryResults outResults, Scriptable scope,ICubeQueryDefinition queryDefn, IFilterDefinition cubeFilter, ScriptContext cx) throws DataException
	{
		assert cubeFilter != null;
		super.init( outResults, scope, queryDefn, cx, cubeFilter.getExpression( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.util.filter.IAggrMeasureFilterEvalHelper#evaluateFilter(org.eclipse.birt.data.engine.olap.util.filter.IResultRow)
	 */
	public boolean evaluateFilter( IResultRow resultRow ) throws DataException
	{
		super.setData( resultRow );
		dimObj.setCurrentRow( resultRow );
		try
		{
			Object result = ScriptEvalUtil.evalExpr( expr,
					cx.newContext( scope ),
					ScriptExpression.defaultID,
					0 );
			return DataTypeUtil.toBoolean( result ).booleanValue( );
		}
		catch ( IJSObjectPopulator.InMatchDimensionIndicator e )
		{
			return true;
		}
		catch ( BirtException e )
		{
			throw DataException.wrap( e );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.util.DimensionJSEvalHelper#registerJSObjectPopulators()
	 */
	protected void registerJSObjectPopulators( ) throws DataException
	{
		super.registerJSObjectPopulators( );
		register( new DataJSObjectPopulator( this.outResults, scope,
				queryDefn.getBindings( ),
				true, cx ) );
		dimObj = new DummyDimensionObject( );
		this.scope.put( org.eclipse.birt.data.engine.script.ScriptConstants.DIMENSION_SCRIPTABLE,
				this.scope,
				this.dimObj );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.util.filter.IAggrMeasureFilterEvalHelper#getExpression()
	 */
	public IBaseExpression getExpression( )
	{
		return this.expr;
	}
	
	private static class DummyLevelObject extends ScriptableObject
	{
		private static final long serialVersionUID = 1L;
		private DummyDimensionObject host;
		private String dimName;
		private Map<String, DummyLevelAttrObject> levelAttrMap;
		public DummyLevelObject( DummyDimensionObject host, String dimName )
		{
			this.host = host;
			this.dimName = dimName;
			this.levelAttrMap = new HashMap<String,DummyLevelAttrObject>(); 
		}
		
		public Object get( String levelName, Scriptable scope )
		{
			try
			{
				if( this.levelAttrMap.containsKey( levelName ))
					return this.levelAttrMap.get( levelName );
				else
				{
					this.levelAttrMap.put( levelName,
							new DummyLevelAttrObject( host, dimName, levelName ) );
					return this.levelAttrMap.get( levelName );
				}
			}
			catch ( Exception e )
			{
				return null;
			}
		}
		
		public String getClassName( )
		{
			return "DummyLevelObject";
		}
	}
	
	private static class DummyDimensionObject extends ScriptableObject
	{
		private static final long serialVersionUID = 1L;
		private IResultRow row;
		private Map< String, DummyLevelObject > dimLevMap = new HashMap<String, DummyLevelObject>( );
		
		/*
		 * (non-Javadoc)
		 * @see org.mozilla.javascript.ScriptableObject#getClassName()
		 */
		public String getClassName( )
		{
			return "DummyDimensionObject";
		}
		
		/**
		 * Set the current row for the evaluation.
		 * @param row
		 */
		public void setCurrentRow( IResultRow row  )
		{
			this.row = row;
		}
		
		public Object get( String dimName, Scriptable scope )
		{
			try
			{
				if( this.dimLevMap.containsKey( dimName ))
					return this.dimLevMap.get( dimName );
				else
				{
					this.dimLevMap.put( dimName, new DummyLevelObject(this, dimName) );
					return this.dimLevMap.get( dimName );
				}
			}
			catch ( Exception e )
			{
				return null;
			}
		}
		
	}
	
	private static class DummyLevelAttrObject extends ScriptableObject
	{
		private static final long serialVersionUID = 1L;
		private String dimName;
		private String levelName;
		private DummyDimensionObject host;
		
		public DummyLevelAttrObject( DummyDimensionObject host, String dimName, String levelName )
		{
			this.host = host;
			this.dimName = dimName;
			this.levelName = levelName;
		}

		public String getClassName( )
		{
			return "DummyLevelAttrObject";
		}
		
		public Object get( String attrName, Scriptable scope )
		{
			
			try
			{
				if( this.levelName.equals( attrName ))
					return this.getDefaultValue( null );
				return this.host.row.getFieldValue( 
						OlapExpressionUtil.getAttrReference(
								this.dimName,
								this.levelName,
								attrName ) );
			}
			catch ( Exception e )
			{
				return null;
			}
		}

		public Object getDefaultValue( Class hint )
		{
			try
			{
				Object value = this.host.row.getFieldValue( 
						OlapExpressionUtil.getAttrReference(
								this.dimName,
								this.levelName,
								this.levelName ));
				if( value != null )
					return value;
				return null;
			}
			catch ( Exception e )
			{
				return null;
			}
		}
	}
}

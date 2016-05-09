
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.api.query.CubeFilterDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IDimensionDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IHierarchyDefinition;
import org.eclipse.birt.data.engine.olap.impl.query.CubeElementFactory;
import org.eclipse.birt.data.engine.olap.util.filter.DimensionFilterEvalHelper;
import org.eclipse.birt.data.engine.olap.util.filter.IResultRow;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.Scriptable;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;


/**
 * 
 */

public class DimensionFilterProcessorTest {
	private Scriptable baseScope;
	private ICubeQueryDefinition cubeQuery;
	private ScriptContext cx;
	@Before
    public void dimensionFilterProcessorSetUp()
	{
		cx = new ScriptContext();
		this.baseScope = new ImporterTopLevel( );
		this.cubeQuery = createCubeQueryDefinition( );
	}
	@After
    public void dimensionFilterProcessorTearDown()
	{
		cx.close( );
	}
	private ICubeQueryDefinition createCubeQueryDefinition()
	{
		ICubeQueryDefinition cubeQuery = new CubeElementFactory( ).createCubeQuery( "cube1" );
		IEdgeDefinition columnEdge = cubeQuery.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		cubeQuery.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dimension1 = columnEdge.createDimension( "dim1" );
		IHierarchyDefinition hier1 = dimension1.createHierarchy( "hier1" );
		hier1.createLevel( "level1" );

		return cubeQuery;
	}
	@Test
    public void testBasicFilter() throws DataException
	{
		IBaseExpression expr = new ScriptExpression( "dimension[\"dim1\"][\"level1\"] * 2 + 2 == 6");
		CubeFilterDefinition cubeFilter = new CubeFilterDefinition(expr);
		DimensionFilterEvalHelper helper = new DimensionFilterEvalHelper( null, this.baseScope, cx, this.cubeQuery, cubeFilter );
		List resultRows = this.getResultRows1( );
		for ( int i = 0; i < resultRows.size(); i++ )
		{
			assertEquals( i == 2, helper.evaluateFilter( (IResultRow)resultRows.get( i )));
		}

		helper.close( );

		try
		{
			for ( int i = 0; i < resultRows.size(); i++ )
			{
				assertEquals( i == 2, helper.evaluateFilter( 
						(IResultRow) resultRows.get( i ) ) );
			}
			fail( "should not arrive here" );
		}
		catch ( NullPointerException e1 )
		{
			// exception is expected after helper.close()
		}
	}
	@Test
    public void testBasicFilter1() throws DataException
	{
		IBaseExpression expr = new ScriptExpression( "dimension[\"dim1\"][\"level1\"][\"attr1\"] * 2 + 2 == 6");
		CubeFilterDefinition cubeFilter = new CubeFilterDefinition(expr);
		
		DimensionFilterEvalHelper helper = new DimensionFilterEvalHelper( null, this.baseScope, cx, this.cubeQuery, cubeFilter );
		List resultRows = this.getResultRows1( );
		for ( int i = 0; i < resultRows.size(); i++ )
		{
			assertEquals( i == 2, helper.evaluateFilter( (IResultRow)resultRows.get( i )));
		}
	}
	@Test
    public void testBasicFilter3( )
	{
		try
		{
			IBaseExpression expr = new ScriptExpression( "dimension[\"dim1\"][\"level2\"][\"attr1\"] * 2 + 2 == 6" );
			CubeFilterDefinition cubeFilter = new CubeFilterDefinition(expr);
			DimensionFilterEvalHelper helper = new DimensionFilterEvalHelper( null, this.baseScope, cx, this.cubeQuery, cubeFilter );
			List resultRows = this.getResultRows1( );
			for ( int i = 0; i < resultRows.size(); i++ )
			{
				assertEquals( i == 2, helper.evaluateFilter( (IResultRow) resultRows.get( i ) ) );
			}
			fail( "Should not arrive here" );
		}
		catch (DataException e1)
		{
			// exception is expected for level2
		}
	}
	
	public List getResultRows1()
	{
		List result = new ArrayList();
		
		for ( int i = 0; i < 5; i++ )
		{	
			Map map = new HashMap();
			map.put( "dim1/level1", new Integer(i) );
			map.put( "dim1/level1/attr1", new Integer(i) );
			result.add( new TempResultRow( map ) );
		}
		return result;
	}
	
	/*private IBaseExpression[] getFilters()
	{
		IBaseExpression[] result = new IBaseExpression[1];
		result[0] = 
		return null;
	}*/
	
	private class TempResultRow implements IResultRow
	{
		private Map nameValuePair;
		
		TempResultRow( Map nameValuePair )
		{
			this.nameValuePair = nameValuePair;
		}

		public Object getAggrValue( String aggrName ) throws DataException
		{
			// TODO Auto-generated method stub
			return null;
		}

		public Object getFieldValue( String name ) throws DataException
		{
			String[] list = name.split("/");
			if(list.length >= 2 && list[list.length - 1].equals(list[list.length - 2]))
			{
				String test = name.substring(0, name.lastIndexOf("/") );
				Object x = this.nameValuePair.get( name.substring(0, name.lastIndexOf("/") ) );
				return this.nameValuePair.get( name.substring(0, name.lastIndexOf("/") ) );
			}
			return this.nameValuePair.get( name );
		}

		public boolean isTimeDimensionRow()
		{
			return false;
		}
		
	}
}


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

import junit.framework.TestCase;

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


/**
 * 
 */

public class DimensionFilterProcessorTest extends TestCase
{
	private Scriptable baseScope;
	private ICubeQueryDefinition cubeQuery;
	private ScriptContext cx;
	public void setUp( )
	{
		try
		{
			super.setUp( );
		}
		catch ( Exception e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cx = new ScriptContext();
		this.baseScope = new ImporterTopLevel( );
		this.cubeQuery = createCubeQueryDefinition( );
	}

	public void tearDown( )
	{
		try
		{
			super.tearDown( );
		}
		catch ( Exception e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	
	public void testBasicFilter() throws DataException
	{
		
		IBaseExpression expr = new ScriptExpression( "dimension[\"dim1\"][\"level1\"] * 2 + 2 == 6");
		CubeFilterDefinition cubeFilter = new CubeFilterDefinition(expr);
		DimensionFilterEvalHelper helper = new DimensionFilterEvalHelper( null, this.baseScope, cx, this.cubeQuery, cubeFilter );
		
		List levelNames = new ArrayList();
		levelNames.add( "level1" );
		
		List resultRows = this.getResultRows1( );
		boolean[] booleanResult = this.getBooleanResult1( );
		for ( int i = 0; i < booleanResult.length; i++ )
		{
			assertEquals( booleanResult[i], helper.evaluateFilter( (IResultRow)resultRows.get( i )));
		}
		
		helper.close( );
		
		try
		{
			for ( int i = 0; i < booleanResult.length; i++ )
			{
				assertEquals( booleanResult[i], helper.evaluateFilter( 
						(IResultRow) resultRows.get( i ) ) );
			}
			fail( "should not arrive here" );
		}
		catch ( Exception e )
		{
		}
	}
	
	public void testBasicFilter1() throws DataException
	{
		IBaseExpression expr = new ScriptExpression( "dimension[\"dim1\"][\"level1\"][\"attr1\"] * 2 + 2 == 6");
		CubeFilterDefinition cubeFilter = new CubeFilterDefinition(expr);
		
		DimensionFilterEvalHelper helper = new DimensionFilterEvalHelper( null, this.baseScope, cx, this.cubeQuery, cubeFilter );
		List levelNames = new ArrayList();
		levelNames.add( "level1" );
		
		List resultRows = this.getResultRows1( );
		boolean[] booleanResult = this.getBooleanResult1( );
		for ( int i = 0; i < booleanResult.length; i++ )
		{
			assertEquals( booleanResult[i], helper.evaluateFilter( (IResultRow)resultRows.get( i )));
		}
	}
	
	public void testBasicFilter3( ) throws DataException
	{
		IBaseExpression expr = new ScriptExpression( "dimension[\"dim1\"][\"level2\"][\"attr1\"] * 2 + 2 == 6" );
		CubeFilterDefinition cubeFilter = new CubeFilterDefinition(expr);
		DimensionFilterEvalHelper helper = new DimensionFilterEvalHelper( null, this.baseScope, cx, this.cubeQuery, cubeFilter );
		List levelNames = new ArrayList( );
		levelNames.add( "level1" );
		List resultRows = this.getResultRows1( );
		boolean[] booleanResult = this.getBooleanResult1( );
		try
		{
			for ( int i = 0; i < booleanResult.length; i++ )
			{
				assertEquals( booleanResult[i], helper.evaluateFilter( (IResultRow) resultRows.get( i ) ) );
			}
			fail( "Should not arrive here" );
		}
		catch ( Exception e )
		{
		}
	}
	
	public List getResultRows1()
	{
		List result = new ArrayList();
		
		for ( int i = 0; i < 5; i++ )
		{	
			Map map = new HashMap();
			map.put( "level1", new Integer(i) );
			map.put( "attr1", new Integer(i) );
			result.add( new TempResultRow( map ) );
		}
		return result;
	}
	
	private boolean[] getBooleanResult1()
	{
		boolean[] result = new boolean[5];
		
		for ( int i = 0; i < 5; i++ )
		{
			if( i!= 2)
				result[i] = false;
			else
				result[i] = true;
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
			return this.nameValuePair.get( name );
		}

		public boolean isTimeDimensionRow()
		{
			// TODO Auto-generated method stub
			return false;
		}
		
	}
}

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

package org.eclipse.birt.data.engine.olap.cursor;

import java.util.ArrayList;
import java.util.List;

import javax.olap.OLAPException;
import javax.olap.cursor.CubeCursor;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.aggregation.api.IBuildInAggregation;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.impl.DataEngineImpl;
import org.eclipse.birt.data.engine.impl.StopSign;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.data.api.cube.ICube;
import org.eclipse.birt.data.engine.olap.impl.query.CubeQueryExecutor;
import org.eclipse.birt.data.engine.olap.query.view.BirtCubeView;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.Scriptable;

import testutil.BaseTestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;


public class MirrorCursorModelTest  extends BaseTestCase
{
	private Scriptable  scope;
	private DataEngineImpl de;
	private CubeUtility creator;
	
	private ICube cube1, cube2;
	
	
	/*
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before
    public void mirrorCursorModelSetUp() throws Exception
	{
		this.scope = new ImporterTopLevel( );
		DataEngineContext context = DataEngineContext.newInstance( DataEngineContext.DIRECT_PRESENTATION,
				scope,
				null,
				null );
		context.setTmpdir( this.getTempDir( ) );
		de = (DataEngineImpl) DataEngine.newDataEngine( context );
		this.creator = new CubeUtility( );
		creator.createCube( de );
		creator.createCube1( de );
		cube1 = creator.getCube( CubeUtility.cubeName, de );
		cube2 = creator.getCube( CubeUtility.timeCube, de );
	}
	@After
    public void mirrorCursorModelTearDown() throws Exception
	{
		cube1.close( );
		cube2.close( );
		if( de!= null )
		{
			de.shutdown( );
			de = null;
		}
	}
	/**
	 * 
	 * @throws OLAPException
	 * @throws BirtException 
	 */
	@Test
    public void testCursorModel1( ) throws OLAPException, BirtException
	{
		ICubeQueryDefinition cqd = creator.createMirroredQueryDefinition( "cube", true );
		
		IBinding rowGrandTotal = new Binding( "rowGrandTotal" );
		rowGrandTotal.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		rowGrandTotal.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		rowGrandTotal.addAggregateOn( "dimension[\"dimension5\"][\"level21\"]" );
		rowGrandTotal.addAggregateOn( "dimension[\"dimension6\"][\"level22\"]" );

		IBinding columnGrandTotal = new Binding( "columnGrandTotal" );
		columnGrandTotal.setAggrFunction( IBuildInAggregation.TOTAL_AVE_FUNC );
		columnGrandTotal.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		columnGrandTotal.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		columnGrandTotal.addAggregateOn( "dimension[\"dimension1\"][\"level11\"]" );
		columnGrandTotal.addAggregateOn( "dimension[\"dimension2\"][\"level12\"]" );
		columnGrandTotal.addAggregateOn( "dimension[\"dimension3\"][\"level13\"]" );
		columnGrandTotal.addAggregateOn( "dimension[\"dimension4\"][\"level14\"]" );

		IBinding totalGrandTotal = new Binding( "totalGrandTotal" );
		totalGrandTotal.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		totalGrandTotal.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		
		cqd.addBinding( rowGrandTotal );
		cqd.addBinding( columnGrandTotal );
		cqd.addBinding( totalGrandTotal );
		
		// Create cube view.
		BirtCubeView cubeView = new BirtCubeView( new CubeQueryExecutor( null, cqd,de.getSession( ),this.scope,de.getContext( )) );

		CubeCursor dataCursor = cubeView.getCubeCursor( new StopSign( ), cube1 );

		List columnEdgeBindingNames = new ArrayList();
		columnEdgeBindingNames.add( "level11" );
		columnEdgeBindingNames.add( "level12" );
		columnEdgeBindingNames.add( "level13" );
		columnEdgeBindingNames.add( "level14" );
		
		List rowEdgeBindingNames = new ArrayList();
		rowEdgeBindingNames.add( "level21" );
		rowEdgeBindingNames.add( "level22" );
		
		List measureBindingNames = new ArrayList( );
		measureBindingNames.add( "measure1" );
		
		List rowGrandTotalNames = new ArrayList();
		rowGrandTotalNames.add( "rowGrandTotal" );
				
		try
		{
			testOut.print( creator.printCubeAlongEdge( dataCursor,
					columnEdgeBindingNames,
					rowEdgeBindingNames,
					measureBindingNames,
					rowGrandTotalNames,
					"columnGrandTotal",
					"totalGrandTotal",
					null ) );
			this.checkOutputFile( );
		}
		catch ( Exception e )
		{
			fail( "fail to get here!" );
		}
	}

	/**
	 * 
	 * @throws OLAPException
	 * @throws BirtException 
	 */
	@Test
    public void testCursorModelNoBreakHierarchy( ) throws OLAPException, BirtException
	{
		ICubeQueryDefinition cqd = creator.createMirroredQueryDefinition( "timeCube",
				false );
		
		IBinding rowGrandTotal = new Binding( "rowGrandTotal" );
		rowGrandTotal.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		rowGrandTotal.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		rowGrandTotal.addAggregateOn( "dimension[\"dimension5\"][\"level21\"]" );
		rowGrandTotal.addAggregateOn( "dimension[\"dimension6\"][\"level22\"]" );

		IBinding columnGrandTotal = new Binding( "columnGrandTotal" );
		columnGrandTotal.setAggrFunction( IBuildInAggregation.TOTAL_AVE_FUNC );
		columnGrandTotal.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		columnGrandTotal.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		columnGrandTotal.addAggregateOn( "dimension[\"dimension1\"][\"level11\"]" );
		columnGrandTotal.addAggregateOn( "dimension[\"dimension2\"][\"level12\"]" );
		columnGrandTotal.addAggregateOn( "dimension[\"dimension3\"][\"level13\"]" );
		columnGrandTotal.addAggregateOn( "dimension[\"dimension4\"][\"level14\"]" );

		IBinding totalGrandTotal = new Binding( "totalGrandTotal" );
		totalGrandTotal.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		totalGrandTotal.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		
		cqd.addBinding( rowGrandTotal );
		cqd.addBinding( columnGrandTotal );
		cqd.addBinding( totalGrandTotal );
		
		// Create cube view.
		BirtCubeView cubeView = new BirtCubeView( new CubeQueryExecutor( null, cqd,de.getSession( ),this.scope,de.getContext( )) );

		CubeCursor dataCursor = cubeView.getCubeCursor( new StopSign( ), cube2 );

		List columnEdgeBindingNames = new ArrayList();
		columnEdgeBindingNames.add( "level11" );
		columnEdgeBindingNames.add( "level12" );
		columnEdgeBindingNames.add( "level13" );
		columnEdgeBindingNames.add( "level14" );
		
		List rowEdgeBindingNames = new ArrayList();
		rowEdgeBindingNames.add( "level21" );
		rowEdgeBindingNames.add( "level22" );
		
		List measureBindingNames = new ArrayList( );
		measureBindingNames.add( "measure1" );
		
		List rowGrandTotalNames = new ArrayList();
		rowGrandTotalNames.add( "rowGrandTotal" );
				
		try
		{
			testOut.print( creator.printCubeAlongEdge( dataCursor,
					columnEdgeBindingNames,
					rowEdgeBindingNames,
					measureBindingNames,
					rowGrandTotalNames,
					"columnGrandTotal",
					"totalGrandTotal",
					null ) );
			this.checkOutputFile( );
		}
		catch ( Exception e )
		{
			fail( "fail to get here!" );
		}
	}
	
	
	/**
	 * without column edge
	 * @throws OLAPException
	 * @throws BirtException 
	 */
	@Test
    public void testCursorOnCountry( ) throws OLAPException, BirtException
	{
		ICubeQueryDefinition cqd = creator.createMirroredQueryDefinition( "cube",
				true );
		
		IBinding rowGrandTotal = new Binding( "countryGrandTotal" );
		rowGrandTotal.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		rowGrandTotal.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		rowGrandTotal.addAggregateOn( "dimension[\"dimension1\"][\"level11\"]" );
		rowGrandTotal.addAggregateOn( "dimension[\"dimension5\"][\"level21\"]" );
		rowGrandTotal.addAggregateOn( "dimension[\"dimension6\"][\"level22\"]" );
		
		cqd.addBinding( rowGrandTotal );

		// Create cube view.
		BirtCubeView cubeView = new BirtCubeView( new CubeQueryExecutor( null, cqd,de.getSession( ),this.scope,de.getContext( )) );

		CubeCursor dataCursor = cubeView.getCubeCursor( new StopSign( ), cube1  );

		List columnEdgeBindingNames = new ArrayList();
		columnEdgeBindingNames.add( "level11" );
		columnEdgeBindingNames.add( "level12" );
		columnEdgeBindingNames.add( "level13" );	
		columnEdgeBindingNames.add( "level14" );
		
		List rowEdgeBindingNames = new ArrayList();
		rowEdgeBindingNames.add( "level21" );
		rowEdgeBindingNames.add( "level22" );
		
		List measureBindingNames = new ArrayList( );
		measureBindingNames.add( "measure1" );

		List grandBindingNames = new ArrayList( );
		grandBindingNames.add( "countryGrandTotal" );

		try
		{
			testOut.print( creator.printCubeAlongEdge( dataCursor,
					columnEdgeBindingNames,
					rowEdgeBindingNames,
					measureBindingNames,
					null,
					null,
					null,
					grandBindingNames ) );
			this.checkOutputFile( );
		}
		catch ( Exception e )
		{
			fail( "fail to get here!" );
		}
	}
	@Test
    public void testCursorOnPageEdge( ) throws Exception
	{
		ICubeQueryDefinition cqd = creator.createMirroredQueryDefinitionWithPage( );
		
		IBinding rowGrandTotal = new Binding( "rowGrandTotal" );
		rowGrandTotal.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		rowGrandTotal.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		rowGrandTotal.addAggregateOn( "dimension[\"dimension5\"][\"level21\"]" );
		rowGrandTotal.addAggregateOn( "dimension[\"dimension6\"][\"level22\"]" );

		IBinding columnGrandTotal = new Binding( "columnGrandTotal" );
		columnGrandTotal.setAggrFunction( IBuildInAggregation.TOTAL_AVE_FUNC );
		columnGrandTotal.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		columnGrandTotal.addAggregateOn( "dimension[\"dimension5\"][\"level21\"]" );
		columnGrandTotal.addAggregateOn( "dimension[\"dimension1\"][\"level11\"]" );
		columnGrandTotal.addAggregateOn( "dimension[\"dimension2\"][\"level12\"]" );
		columnGrandTotal.addAggregateOn( "dimension[\"dimension3\"][\"level13\"]" );
		columnGrandTotal.addAggregateOn( "dimension[\"dimension4\"][\"level14\"]" );

		IBinding totalGrandTotal = new Binding( "totalGrandTotal" );
		totalGrandTotal.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		totalGrandTotal.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		totalGrandTotal.addAggregateOn( "dimension[\"dimension5\"][\"level21\"]" );
		
		cqd.addBinding( rowGrandTotal );
		cqd.addBinding( columnGrandTotal );
		cqd.addBinding( totalGrandTotal );

		// Create cube view.
		BirtCubeView cubeView = new BirtCubeView( new CubeQueryExecutor( null, cqd,de.getSession( ),this.scope,de.getContext( )) );

		CubeCursor dataCursor = cubeView.getCubeCursor( new StopSign( ), cube1 );

		List columnEdgeBindingNames = new ArrayList();
		columnEdgeBindingNames.add( "level11" );
		columnEdgeBindingNames.add( "level12" );
		columnEdgeBindingNames.add( "level13" );	
		columnEdgeBindingNames.add( "level14" );
		
		List rowEdgeBindingNames = new ArrayList();
		rowEdgeBindingNames.add( "level22" );
		
		List measureBindingNames = new ArrayList( );
		measureBindingNames.add( "measure1" );

		List rowGrandTotalNames = new ArrayList();
		rowGrandTotalNames.add( "rowGrandTotal" );
		
		List pageBindingNames = new ArrayList( );
		pageBindingNames.add( "level21" );

		testOut.print( creator.printCubeAlongPageEdge( dataCursor,
				pageBindingNames,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				measureBindingNames,
				rowGrandTotalNames,
				"columnGrandTotal",
				"totalGrandTotal",
				null ) );
		this.checkOutputFile( );
	}

}

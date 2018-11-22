/*******************************************************************************
 * Copyright (c) 2004 ,2009 Actuate Corporation.
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

import javax.olap.cursor.CubeCursor;

import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.DataEngineImpl;
import org.eclipse.birt.data.engine.impl.StopSign;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IDimensionDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IHierarchyDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IMeasureDefinition;
import org.eclipse.birt.data.engine.olap.data.api.cube.ICube;
import org.eclipse.birt.data.engine.olap.impl.query.CubeQueryDefinition;
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


public class DateTimeCursorTest extends BaseTestCase
{
	private Scriptable  scope;
	private DataEngineImpl de;
	
	private ICube cube;
	/*
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before
    public void dateTimeCursorSetUp() throws Exception
	{
		this.scope = new ImporterTopLevel( );
		DataEngineContext context = DataEngineContext.newInstance( DataEngineContext.DIRECT_PRESENTATION,
				scope,
				null,
				null );
		context.setTmpdir( this.getTempDir( ) );
		de = (DataEngineImpl) DataEngine.newDataEngine( context );
		DateCube util = new DateCube( );
		util.createCube( de );
		cube = util.getCube( DateCube.cubeName, de );
	}
	@After
    public void dateTimeCursorTearDown() throws Exception
	{
		cube.close( );
		if( de!= null )
		{
			de.shutdown( );
			de = null;
		}
	}
	@Test
    public void testMirrorOnYearDimension( ) throws DataException
	{
		ICubeQueryDefinition cqd = createMirroredQueryDefinition( "level12" );
		
		// Create cube view.
		BirtCubeView cubeView = new BirtCubeView( new CubeQueryExecutor( null,
				cqd,
				de.getSession( ),
				this.scope,
				de.getContext( ) ), cube, null, null );

		CubeCursor dataCursor = cubeView.getCubeCursor( new StopSign( ), cube );

		List rowEdgeBindingNames = new ArrayList();
		rowEdgeBindingNames.add( "level11" );
		rowEdgeBindingNames.add( "level12" );
		rowEdgeBindingNames.add( "level13" );
		rowEdgeBindingNames.add( "level14" );
		
		List columnEdgeBindingNames = new ArrayList();
		columnEdgeBindingNames.add( "level21" );
		
		List measureBindingNames = new ArrayList( );
		measureBindingNames.add( "measure1" );
					
		try
		{
			testOut.print( new CubeUtility( ).printCubeAlongEdge( dataCursor,
					columnEdgeBindingNames,
					rowEdgeBindingNames,
					measureBindingNames,
					null,
					null,
					null,
					null ) );
			this.checkOutputFile( );
		}
		catch ( Exception e )
		{
			fail( "fail to get here!" );
		}
	}
	@Test
    public void testMirrorOnQuarterDimension( ) throws DataException
	{
		ICubeQueryDefinition cqd = createMirroredQueryDefinition( "level13");
		
		// Create cube view.
		BirtCubeView cubeView = new BirtCubeView( new CubeQueryExecutor( null,
				cqd,
				de.getSession( ),
				this.scope,
				de.getContext( ) ), cube, null, null );

		CubeCursor dataCursor = cubeView.getCubeCursor( new StopSign( ), cube );

		List rowEdgeBindingNames = new ArrayList();
		rowEdgeBindingNames.add( "level11" );
		rowEdgeBindingNames.add( "level12" );
		rowEdgeBindingNames.add( "level13" );
		rowEdgeBindingNames.add( "level14" );
		
		List columnEdgeBindingNames = new ArrayList();
		columnEdgeBindingNames.add( "level21" );
		
		List measureBindingNames = new ArrayList( );
		measureBindingNames.add( "measure1" );
					
		try
		{
			testOut.print( new CubeUtility( ).printCubeAlongEdge( dataCursor,
					columnEdgeBindingNames,
					rowEdgeBindingNames,
					measureBindingNames,
					null,
					null,
					null,
					null ) );
			this.checkOutputFile( );
		}
		catch ( Exception e )
		{
			fail( "fail to get here!" );
		}
	}
	@Test
    public void testMirrorOnMonthDimension( ) throws DataException
	{
		ICubeQueryDefinition cqd = createMirroredQueryDefinition( "level14" );
		
		// Create cube view.
		BirtCubeView cubeView = new BirtCubeView( new CubeQueryExecutor( null,
				cqd,
				de.getSession( ),
				this.scope,
				de.getContext( ) ), cube, null, null );

		CubeCursor dataCursor = cubeView.getCubeCursor( new StopSign( ), cube );

		List rowEdgeBindingNames = new ArrayList();
		rowEdgeBindingNames.add( "level11" );
		rowEdgeBindingNames.add( "level12" );
		rowEdgeBindingNames.add( "level13" );
		rowEdgeBindingNames.add( "level14" );
		
		List columnEdgeBindingNames = new ArrayList();
		columnEdgeBindingNames.add( "level21" );
		
		List measureBindingNames = new ArrayList( );
		measureBindingNames.add( "measure1" );
					
		try
		{
			testOut.print( new CubeUtility( ).printCubeAlongEdge( dataCursor,
					columnEdgeBindingNames,
					rowEdgeBindingNames,
					measureBindingNames,
					null,
					null,
					null,
					null ) );
			this.checkOutputFile( );
		}
		catch ( Exception e )
		{
			fail( "fail to get here!" );
		}
	}
	
	ICubeQueryDefinition createMirroredQueryDefinition( String mirrorLevelName )
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( DateCube.cubeName );

		IMeasureDefinition measure = cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IDimensionDefinition productLineDim1 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition porductLineHie1 = productLineDim1.createHierarchy( "dimension2" );
		porductLineHie1.createLevel( "level21" );

		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dateDim = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition dateHier = dateDim.createHierarchy( "dimension1" );
		
		dateHier.createLevel( "level11" );
		dateHier.createLevel( "level12" );
		dateHier.createLevel( "level13" );
		dateHier.createLevel( "level14" );

		ILevelDefinition mirrorlevel = null;
		for ( int i = 0; i < dateHier.getLevels( ).size( ); i++ )
		{
			if ( ( ( (ILevelDefinition) dateHier.getLevels( ).get( i ) ).getName( ) ).equals( mirrorLevelName ) )
				mirrorlevel = (ILevelDefinition) dateHier.getLevels( ).get( i );
		}
		columnEdge.setMirrorStartingLevel( mirrorlevel );
		return cqd;
	}
}

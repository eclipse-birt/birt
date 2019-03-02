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

import javax.olap.OLAPException;
import javax.olap.cursor.CubeCursor;
import javax.olap.cursor.DimensionCursor;
import javax.olap.cursor.EdgeCursor;

import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.core.DataException;
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
import static org.junit.Assert.*;

public class MirrorCursorNavigatorTest extends BaseTestCase
{

	private Scriptable scope;
	private DataEngineImpl de;
	private CubeUtility creator;
	private ICube cube;

	/*
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before
    public void mirrorCursorNavigatorSetUp() throws Exception
	{
		this.scope = new ImporterTopLevel( );
		DataEngineContext context = DataEngineContext.newInstance( DataEngineContext.DIRECT_PRESENTATION,
				scope,
				null,
				null );
		context.setTmpdir( this.getTempDir( ) );
		de = (DataEngineImpl) DataEngine.newDataEngine( context );
		creator = new CubeUtility( );
		creator.createCube( de );
		cube = creator.getCube( CubeUtility.cubeName, de );
	}
	@After
    public void mirrorCursorNavigatorTearDown() throws Exception
	{
		cube.close( );
		if( de!= null )
		{
			de.shutdown( );
			de = null;
		}
	}
	@Test
    public void testNavigator( ) throws DataException, OLAPException
	{
		ICubeQueryDefinition cqd = creator.createMirroredQueryDefinition( "cube",
				true );

		// Create cube view.
		BirtCubeView cubeView = new BirtCubeView( new CubeQueryExecutor( null, cqd,
				de.getSession( ),
				this.scope,
				de.getContext( ) ) );

		CubeCursor dataCursor = cubeView.getCubeCursor( new StopSign( ) , cube );

		// retrieve the edge cursors
		// EdgeCursor pageCursor = cubeView.getMeasureEdgeView( );
		EdgeCursor rowCursor = cubeView.getRowEdgeView( ).getEdgeCursor( );
		EdgeCursor columnCursor = cubeView.getColumnEdgeView( ).getEdgeCursor( );

		DimensionCursor countryCursor = (DimensionCursor) columnCursor.getDimensionCursor( )
				.get( 0 );
		DimensionCursor cityCursor = (DimensionCursor) columnCursor.getDimensionCursor( )
				.get( 1 );
		DimensionCursor streetCursor = (DimensionCursor) columnCursor.getDimensionCursor( )
				.get( 2 );
		DimensionCursor yearCursor = (DimensionCursor) columnCursor.getDimensionCursor( )
				.get( 3 );

		DimensionCursor productLineCursor = (DimensionCursor) rowCursor.getDimensionCursor( )
				.get( 0 );
		DimensionCursor productNameCursor = (DimensionCursor) rowCursor.getDimensionCursor( )
				.get( 1 );

		//Test edgeCursor navigator
		//-------------------------------edgeCursor beforeFirst()--------------
		columnCursor.beforeFirst( );
		assertTrue( columnCursor.isBeforeFirst( ) );
		assertTrue( countryCursor.isBeforeFirst( ) );
		assertTrue( cityCursor.isBeforeFirst( ) );
		assertTrue( streetCursor.isBeforeFirst( ) );
		assertTrue( yearCursor.isBeforeFirst( ) );

		// -------------------------------edgeCursor afterFirst()--------------
		columnCursor.afterLast( );
		assertTrue( columnCursor.isAfterLast( ) );
		assertTrue( countryCursor.isAfterLast( ) );
		assertTrue( cityCursor.isAfterLast( ) );
		assertTrue( streetCursor.isAfterLast( ) );
		assertTrue( yearCursor.isAfterLast( ) );

		columnCursor.first( );
		countryCursor.afterLast( );
		cityCursor.afterLast( );
		streetCursor.afterLast( );
		yearCursor.afterLast( );
		assertTrue( countryCursor.isAfterLast( ) );
		assertTrue( cityCursor.isAfterLast( ) );
		assertTrue( streetCursor.isAfterLast( ) );
		assertTrue( yearCursor.isAfterLast( ) );
		
		columnCursor.first( );
		columnCursor.next( );
		cityCursor.afterLast( );
		streetCursor.afterLast( );
		yearCursor.afterLast( );
		assertTrue( countryCursor.isFirst( ) );
		assertTrue( cityCursor.isAfterLast( ) );
		assertTrue( streetCursor.isAfterLast( ) );
		assertTrue( yearCursor.isAfterLast( ) );
		
		//-------------------------------edgeCursor first()--------------
		columnCursor.first( );
		assertTrue( columnCursor.isFirst( ) );
		assertTrue( countryCursor.isFirst( ) );
		assertTrue( cityCursor.isFirst( ) );
		assertTrue( streetCursor.isFirst( ) );
		assertTrue( yearCursor.isFirst( ) );

		//-------------------------------edgeCursor last()-------------
		columnCursor.last( );
		assertTrue( columnCursor.last( ) );
		assertTrue( countryCursor.last( ) );
		assertTrue( cityCursor.last( ) );
		assertTrue( streetCursor.last( ) );
		assertTrue( yearCursor.last( ) );

		//-------------------------------edgeCursor setPosition()--------------
		columnCursor.setPosition( 5 );
		assertTrue( countryCursor.getObject( "level11" ).equals( "CN" ) );
		assertTrue( cityCursor.getObject( "level12" ).equals( "BJ" ) );
		assertTrue( streetCursor.getObject( "level13" ).equals( "A2" ) );
		assertTrue( yearCursor.getObject( "level14" ).equals( "1998" ) );

		columnCursor.setPosition( 85 );
		assertTrue( countryCursor.getObject( "level11" ).equals( "JP" ) );
		assertTrue( cityCursor.getObject( "level12" ).equals( "IL" ) );
		assertTrue( streetCursor.getObject( "level13" ).equals( "A2" ) );
		assertTrue( yearCursor.getObject( "level14" ).equals( "1998" ) );

		//-------------------------------edgeCursor setPosition()--------------
		columnCursor.setPosition( 220 );
		try
		{
			countryCursor.getObject( "level11" );
			fail( "should never get here!!" );
		}
		catch ( OLAPException e )
		{
		}
		
		//-------------------------edgeCursor setPostion(>edgeLength),previous------
		columnCursor.setPosition( 225 );
		columnCursor.previous( );
		assertTrue( countryCursor.getObject( "level11" ).equals( "US" ) );
		assertTrue( cityCursor.getObject( "level12" ).equals( "NY" ) );
		assertTrue( streetCursor.getObject( "level13" ).equals( "A4" ) );
		assertTrue( yearCursor.getObject( "level14" ).equals( "2002" ) );
		
		//-------------------------------edgeCursor previous()--------------
		columnCursor.setPosition( 86 );
		columnCursor.previous( );
		assertTrue( countryCursor.getObject( "level11" ).equals( "JP" ) );
		assertTrue( cityCursor.getObject( "level12" ).equals( "IL" ) );
		assertTrue( streetCursor.getObject( "level13" ).equals( "A2" ) );
		assertTrue( yearCursor.getObject( "level14" ).equals( "1998" ) );

		//-------------------------------edgeCursor relative()--------------
		columnCursor.beforeFirst( );
		columnCursor.relative( 6 );
		assertTrue( countryCursor.getObject( "level11" ).equals( "CN" ) );
		assertTrue( cityCursor.getObject( "level12" ).equals( "BJ" ) );
		assertTrue( streetCursor.getObject( "level13" ).equals( "A2" ) );
		assertTrue( yearCursor.getObject( "level14" ).equals( "1998" ) );

		//-------------------------------edgeCursor beforeFirst(),next(),setPosition()--------------
		columnCursor.beforeFirst( );
		columnCursor.next( );
		columnCursor.next( );
		columnCursor.next( );
		columnCursor.setPosition( 1 );
		assertTrue( countryCursor.getObject( "level11" ).equals( "CN" ) );
		assertTrue( cityCursor.getObject( "level12" ).equals( "BJ" ) );
		assertTrue( streetCursor.getObject( "level13" ).equals( "A1" ) );
		assertTrue( yearCursor.getObject( "level14" ).equals( "1999" ) );

		//------------------------------dimensionCursor setPosition()--------------
		columnCursor.beforeFirst( );
		columnCursor.setPosition( 80 );
		streetCursor.setPosition( 1 );
		assertTrue( countryCursor.getObject( "level11" ).equals( "JP" ) );
		assertTrue( cityCursor.getObject( "level12" ).equals( "IL" ) );
		assertTrue( streetCursor.getObject( "level13" ).equals( "A2" ) );
		assertTrue( yearCursor.getObject( "level14" ).equals( "1998" ) );

		//------------------------------dimensionCursor next()--------------
		columnCursor.beforeFirst( );
		columnCursor.setPosition( 80 );
		columnCursor.next( );
		countryCursor.next( );
		assertTrue( countryCursor.getObject( "level11" ).equals( "UN" ) );
		assertTrue( cityCursor.getObject( "level12" ).equals( "LD" ) );
		assertTrue( streetCursor.getObject( "level13" ).equals( "A1" ) );
		assertTrue( yearCursor.getObject( "level14" ).equals( "1999" ) );

		//------------------------------edgeCursor afterLast()---------
		columnCursor.afterLast( );
		try
		{
			streetCursor.getObject( "level13" );
			fail( "should not get here" );
		}
		catch ( OLAPException e )
		{
		}

		//------------------------------dimensionCursor setPosition(),getEdgeStart(),getEdgeEnd()--------------
		columnCursor.beforeFirst( );
		columnCursor.next( );
		assertTrue( countryCursor.getEdgeStart( ) == 0 );
		assertTrue( countryCursor.getEdgeEnd( ) == 79 );

		assertTrue( cityCursor.getEdgeStart( ) == 0 );
		assertTrue( cityCursor.getEdgeEnd( ) == 19 );

		assertTrue( streetCursor.getEdgeStart( ) == 0 );
		assertTrue( streetCursor.getEdgeEnd( ) == 4 );

		assertTrue( yearCursor.getEdgeStart( ) == 0 );
		assertTrue( yearCursor.getEdgeEnd( ) == 0 );
		
		columnCursor.beforeFirst( );
		columnCursor.setPosition( 1 );
		assertTrue( countryCursor.getEdgeStart( ) == 0 );
		assertTrue( countryCursor.getEdgeEnd( ) == 79 );

		assertTrue( cityCursor.getEdgeStart( ) == 0 );
		assertTrue( cityCursor.getEdgeEnd( ) == 19 );

		assertTrue( streetCursor.getEdgeStart( ) == 0 );
		assertTrue( streetCursor.getEdgeEnd( ) == 4 );

		assertTrue( yearCursor.getEdgeStart( ) == 1 );
		assertTrue( yearCursor.getEdgeEnd( ) == 1 );

		columnCursor.setPosition( 81 );
		assertTrue( countryCursor.getEdgeStart( ) == 80 );
		assertTrue( countryCursor.getEdgeEnd( ) == 119 );

		assertTrue( cityCursor.getEdgeStart( ) == 80 );
		assertTrue( cityCursor.getEdgeEnd( ) == 99 );

		assertTrue( streetCursor.getEdgeStart( ) == 80 );
		assertTrue( streetCursor.getEdgeEnd( ) == 84 );

		assertTrue( yearCursor.getEdgeStart( ) == 81 );
		assertTrue( yearCursor.getEdgeEnd( ) == 81 );

		columnCursor.setPosition( 146 );
		assertTrue( countryCursor.getEdgeStart( ) == 120 );
		assertTrue( countryCursor.getEdgeEnd( ) == 159 );

		assertTrue( cityCursor.getEdgeStart( ) == 140 );
		assertTrue( cityCursor.getEdgeEnd( ) == 159 );

		assertTrue( streetCursor.getEdgeStart( ) == 145 );
		assertTrue( streetCursor.getEdgeEnd( ) == 149 );

		assertTrue( yearCursor.getEdgeStart( ) == 146 );
		assertTrue( yearCursor.getEdgeEnd( ) == 146 );

		columnCursor.beforeFirst( );
		columnCursor.setPosition( 220 );
		assertTrue( countryCursor.getEdgeStart( ) == -1 );
		assertTrue( countryCursor.getEdgeEnd( ) == -1 );

		assertTrue( cityCursor.getEdgeStart( ) == -1 );
		assertTrue( cityCursor.getEdgeEnd( ) == -1 );

		assertTrue( streetCursor.getEdgeStart( ) == -1 );
		assertTrue( streetCursor.getEdgeEnd( ) == -1 );

		assertTrue( yearCursor.getEdgeStart( ) == -1 );
		assertTrue( yearCursor.getEdgeEnd( ) == -1 );
		this.close( dataCursor );
	}
	
	/**
	 * 
	 * @param dataCursor
	 * @throws OLAPException
	 */
	private void close( CubeCursor dataCursor ) throws OLAPException
	{
		for ( int i = 0; i < dataCursor.getOrdinateEdge( ).size( ); i++ )
		{
			EdgeCursor edge = (EdgeCursor) ( dataCursor.getOrdinateEdge( ).get( i ) );
			edge.close( );
		}
		dataCursor.close( );
	}
}

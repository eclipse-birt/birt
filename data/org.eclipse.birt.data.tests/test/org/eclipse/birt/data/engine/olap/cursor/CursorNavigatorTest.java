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

import junit.framework.TestCase;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.aggregation.IBuildInAggregation;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.impl.DataEngineImpl;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.impl.CubeQueryExecutor;
import org.eclipse.birt.data.engine.olap.query.view.BirtCubeView;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.Scriptable;


public class CursorNavigatorTest extends TestCase
{
	private Scriptable scope;
	private DataEngineImpl de;
	private CubeUtility creator;
	
	/*
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp( ) throws Exception
	{
		super.setUp( );

		this.scope = new ImporterTopLevel( );
		de = (DataEngineImpl) DataEngine.newDataEngine( DataEngineContext.newInstance( DataEngineContext.DIRECT_PRESENTATION,
				scope,
				null,
				null ) );
		creator = new CubeUtility( );
		creator.createCube( de );
	}
	

	/**
	 * 
	 * @throws OLAPException
	 * @throws BirtException 
	 */
	public void testCursorModel1( ) throws OLAPException, BirtException
	{
		ICubeQueryDefinition cqd = creator.createQueryDefinition( );

		IBinding rowGrandTotal = new Binding( "rowGrandTotal" );
		rowGrandTotal.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		rowGrandTotal.setExpression( new ScriptExpression("measure[\"measure1\"]") );
		rowGrandTotal.addAggregateOn( "dimension[\"dimension5\"][\"level21\"]" );
		rowGrandTotal.addAggregateOn( "dimension[\"dimension6\"][\"level22\"]" );

		IBinding columnGrandTotal = new Binding( "columnGrandTotal" );
		columnGrandTotal.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		columnGrandTotal.setExpression( new ScriptExpression("measure[\"measure1\"]") );
		columnGrandTotal.addAggregateOn( "dimension[\"dimension1\"][\"level11\"]" );
		columnGrandTotal.addAggregateOn( "dimension[\"dimension2\"][\"level12\"]" );
		columnGrandTotal.addAggregateOn( "dimension[\"dimension3\"][\"level13\"]" );
		columnGrandTotal.addAggregateOn( "dimension[\"dimension4\"][\"level14\"]" );		

		cqd.addBinding( rowGrandTotal );
		cqd.addBinding( columnGrandTotal );

		// Create cube view.
		BirtCubeView cubeView = new BirtCubeView( new CubeQueryExecutor(cqd,de.getSession( ),this.scope,de.getContext( )) );

		CubeCursor dataCursor = cubeView.getCubeCursor( );

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
		DimensionCursor timeCursor = (DimensionCursor) columnCursor.getDimensionCursor( )
				.get( 3 );

		//Test edgeCursor navigator
		//-------------------------------edgeCursor beforeFirst()--------------
		columnCursor.beforeFirst( );
		assertTrue( countryCursor.isBeforeFirst( ) );
		assertTrue( cityCursor.isBeforeFirst( ) );
		assertTrue( streetCursor.isBeforeFirst( ) );
		assertTrue( timeCursor.isBeforeFirst( ) );

		//-------------------------------edgeCursor afterFirst()--------------
		columnCursor.afterLast( );
		assertTrue( countryCursor.isAfterLast( ) );
		assertTrue( cityCursor.isAfterLast( ) );
		assertTrue( streetCursor.isAfterLast( ) );
		assertTrue( timeCursor.isAfterLast( ) );
	
		//-------------------------------edgeCursor first()--------------
		columnCursor.first( );
		assertTrue( columnCursor.isFirst( ) );
		assertTrue( countryCursor.isFirst( ) );
		assertTrue( cityCursor.isFirst( ) );
		assertTrue( streetCursor.isFirst( ) );
		assertTrue( timeCursor.isFirst( ) );
		
		//-------------------------------edgeCursor last()-------------
		columnCursor.last( );
		assertTrue( columnCursor.last( ) );
		assertTrue( countryCursor.last( ) );
		assertTrue( cityCursor.last( ) );
		assertTrue( streetCursor.last( ) );	
		assertTrue( timeCursor.last( ) );
		
		//-------------------------------edgeCursor setPosition()--------------
		columnCursor.setPosition( 5 );
		assertTrue( countryCursor.getObject( "level11" ).equals( "CN" ) );
		assertTrue( cityCursor.getObject( "level12" ).equals( "SH" ) );
		assertTrue( streetCursor.getObject( "level13" ).equals( "A1" ) );
		assertTrue( timeCursor.getObject( "level14" ).equals( "2000" ) );
		
		//-------------------------------edgeCursor previous()--------------
		columnCursor.previous( );
		assertTrue( countryCursor.getObject( "level11" ).equals( "CN" ) );
		assertTrue( cityCursor.getObject( "level12" ).equals( "SH" ) );
		assertTrue( streetCursor.getObject( "level13" ).equals( "A1" ) );	
		assertTrue( timeCursor.getObject( "level14" ).equals( "1998" ) );	
		
		//-------------------------------edgeCursor setPosition()--------------
		columnCursor.setPosition( 24 );
		try
		{
			countryCursor.getObject( "level11" );
			fail("should never get here!!");
		}
		catch ( OLAPException e )
		{
		}

		//-------------------------------edgeCursor relative()--------------
		columnCursor.beforeFirst( );
		columnCursor.relative( 6 );
		assertTrue( countryCursor.getObject( "level11" ).equals( "CN" ) );
		assertTrue( cityCursor.getObject( "level12" ).equals( "SH" ) );
		assertTrue( streetCursor.getObject( "level13" ).equals( "A1" ) );
		assertTrue( timeCursor.getObject( "level14" ).equals( "2000" ) );
		
		//-------------------------------edgeCursor beforeFirst(),next(),setPosition()--------------
		columnCursor.beforeFirst( );
		columnCursor.next( );
		columnCursor.next( );
		columnCursor.next( );		
		columnCursor.setPosition( 1 );
		assertTrue( countryCursor.getObject( "level11" ).equals( "CN" ) );
		assertTrue( cityCursor.getObject( "level12" ).equals( "BJ" ) );
		assertTrue( streetCursor.getObject( "level13" ).equals( "A1" ) );
		assertTrue( timeCursor.getObject( "level14" ).equals( "2001" ) );

		//------------------------------dimensionCursor setPosition()--------------
		columnCursor.beforeFirst( );
		columnCursor.setPosition( 4 );
		streetCursor.setPosition( 1 );
		assertTrue( countryCursor.getObject( "level11" ).equals( "CN" ) );
		assertTrue( cityCursor.getObject( "level12" ).equals( "SH" ) );
		assertTrue( streetCursor.getObject( "level13" ).equals( "A1" ) );
		assertTrue( timeCursor.getObject( "level14" ).equals( "1998" ) );

		//------------------------------dimensionCursor next()--------------
		columnCursor.beforeFirst( );
		columnCursor.next( );
		countryCursor.next( );
		assertTrue( countryCursor.getObject( "level11" ).equals( "JP" ) );
		assertTrue( cityCursor.getObject( "level12" ).equals( "IL" ) );
		assertTrue( streetCursor.getObject( "level13" ).equals( "A4" ) );
		assertTrue( timeCursor.getObject( "level14" ).equals( "1999" ) );
		
		columnCursor.afterLast( );		
		try
		{
			streetCursor.getObject( "level13" );
			fail("should not get here");
		}
		catch ( OLAPException e )
		{
		}
		
		//------------------------------dimensionCursor setPosition(),getEdgeStart(),getEdgeEnd()--------------
		columnCursor.beforeFirst( );
		columnCursor.setPosition( 1 );
		assertTrue( countryCursor.getEdgeStart( ) == 0 );
		assertTrue( countryCursor.getEdgeEnd( ) == 7 );

		assertTrue( cityCursor.getEdgeStart( ) == 0 );
		assertTrue( cityCursor.getEdgeEnd( ) == 2 );
			
		assertTrue( streetCursor.getEdgeStart( )==0 );
		assertTrue( streetCursor.getEdgeEnd( ) ==1 );
		
		assertTrue( timeCursor.getEdgeStart( )==1 );
		assertTrue( timeCursor.getEdgeEnd( ) ==1 );
		
		columnCursor.setPosition( 9 );
		assertTrue( countryCursor.getEdgeStart( ) == 8 );
		assertTrue( countryCursor.getEdgeEnd( ) == 11 );

		assertTrue( cityCursor.getEdgeStart( ) == 8 );
		assertTrue( cityCursor.getEdgeEnd( ) == 10 );

		assertTrue( streetCursor.getEdgeStart( ) == 8 );
		assertTrue( streetCursor.getEdgeEnd( ) == 10 );
		
		assertTrue( timeCursor.getEdgeStart( ) == 9 );
		assertTrue( timeCursor.getEdgeEnd( ) == 9 );		
		
		columnCursor.setPosition( 23 );
		assertTrue( countryCursor.getEdgeStart( ) == 15 );
		assertTrue( countryCursor.getEdgeEnd( ) == 23 );

		assertTrue( cityCursor.getEdgeStart( ) == 23 );
		assertTrue( cityCursor.getEdgeEnd( ) == 23 );
			
		assertTrue( streetCursor.getEdgeStart( )== 23 );
		assertTrue( streetCursor.getEdgeEnd( ) == 23 );

		assertTrue( timeCursor.getEdgeStart( ) == 23 );
		assertTrue( timeCursor.getEdgeEnd( ) == 23 );
		
		columnCursor.setPosition( 12 );
		assertTrue( countryCursor.getEdgeStart( ) == 12 );
		assertTrue( countryCursor.getEdgeEnd( ) == 14 );

		assertTrue( cityCursor.getEdgeStart( ) == 12 );
		assertTrue( cityCursor.getEdgeEnd( ) == 13 );

		assertTrue( streetCursor.getEdgeStart( ) == 12 );
		assertTrue( streetCursor.getEdgeEnd( ) == 12 );

		assertTrue( timeCursor.getEdgeStart( ) == 12 );
		assertTrue( timeCursor.getEdgeEnd( ) == 12 );
		
		columnCursor.beforeFirst( );
		columnCursor.setPosition( 24 );
		assertTrue( countryCursor.getEdgeStart( ) == -1 );
		assertTrue( countryCursor.getEdgeEnd( ) == -1 );
		close( dataCursor );
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

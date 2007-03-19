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

import org.eclipse.birt.data.engine.aggregation.BuiltInAggregationFactory;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IDimensionDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IHierarchyDefinition;
import org.eclipse.birt.data.engine.olap.api.query.impl.CubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.query.view.BirtCubeView;

import junit.framework.TestCase;


public class CursorNavigatorTest extends TestCase
{
	/*
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp( ) throws Exception
	{
		super.setUp( );
		new CubeCreator( ).createCube( );
	}
	

	/**
	 * 
	 * @throws OLAPException
	 */
	public void testCursorModel1( ) throws OLAPException
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( "cube" );

		cqd.createMeasure( "measure1" );
		cqd.createMeasure( "measure2" );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition timeDim = columnEdge.createDimension( "time" );
		IHierarchyDefinition timeHier = timeDim.createHierarchy( "timeHierarchy" );
		timeHier.createLevel( "level21" );

		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IDimensionDefinition geographyDim = rowEdge.createDimension( "geography" );
		IHierarchyDefinition geographyHier = geographyDim.createHierarchy( "geographyHierarchy" );
		geographyHier.createLevel( "level11" );
		geographyHier.createLevel( "level12" );
		geographyHier.createLevel( "level13" );

		IBinding rowGrandTotal = new Binding( "rowGrandTotal" );
		rowGrandTotal.setAggrFunction( BuiltInAggregationFactory.TOTAL_SUM_FUNC );
		rowGrandTotal.setExpression( new ScriptExpression("measure1") );
		rowGrandTotal.addAggregateOn( "level21" );

		IBinding columnGrandTotal = new Binding( "columnGrandTotal" );
		columnGrandTotal.setAggrFunction( BuiltInAggregationFactory.TOTAL_SUM_FUNC );
		columnGrandTotal.setExpression( new ScriptExpression("measure1") );
		columnGrandTotal.addAggregateOn( "level11" );
		columnGrandTotal.addAggregateOn( "level12" );
		columnGrandTotal.addAggregateOn( "level13" );

		cqd.addBinding( rowGrandTotal );
		cqd.addBinding( columnGrandTotal );

		// Create cube view.
		BirtCubeView cubeView = new BirtCubeView( cqd );

		CubeCursor dataCursor = cubeView.getCubeCursor( );

		// retrieve the edge cursors
		// EdgeCursor pageCursor = cubeView.getMeasureEdgeView( );
		EdgeCursor rowCursor = cubeView.getRowEdgeView( ).getEdgeCursor( );
		EdgeCursor columnCursor = cubeView.getColumnEdgeView( ).getEdgeCursor( );

		DimensionCursor countryCursor = (DimensionCursor) columnCursor.getDimensionCursor( )
				.get( 0 );
		DimensionCursor cityCursor = (DimensionCursor) columnCursor.getDimensionCursor( )
				.get( 1 );
		DimensionCursor productCursor = (DimensionCursor) columnCursor.getDimensionCursor( )
				.get( 2 );

		DimensionCursor timeCursor = (DimensionCursor) rowCursor.getDimensionCursor( )
				.get( 0 );

		//Test edgeCursor navigator
		//-------------------------------edgeCursor beforeFirst()--------------
		columnCursor.beforeFirst( );
		assertTrue( countryCursor.isBeforeFirst( ) );
		assertTrue( cityCursor.isBeforeFirst( ) );
		assertTrue( productCursor.isBeforeFirst( ) );

		//-------------------------------edgeCursor afterFirst()--------------
		columnCursor.afterLast( );
		assertTrue( countryCursor.isAfterLast( ) );
		assertTrue( cityCursor.isAfterLast( ) );
		assertTrue( productCursor.isAfterLast( ) );

		//-------------------------------edgeCursor first()--------------
		columnCursor.first( );
		assertTrue( columnCursor.isFirst( ) );
		assertTrue( countryCursor.isFirst( ) );
		assertTrue( cityCursor.isFirst( ) );
		assertTrue( productCursor.isFirst( ) );
		
		//-------------------------------edgeCursor last()-------------
		columnCursor.last( );
		assertTrue( columnCursor.last( ) );
		assertTrue( countryCursor.last( ) );
		assertTrue( cityCursor.last( ) );
		assertTrue( productCursor.last( ) );	

		//-------------------------------edgeCursor setPosition()--------------
		columnCursor.setPosition( 5 );
		assertTrue( countryCursor.getObject( "level11" ).equals( "CN" ) );
		assertTrue( cityCursor.getObject( "level12" ).equals( "SZ" ) );
		assertTrue( productCursor.getObject( "level13" ).equals( "S2" ) );
		
		//-------------------------------edgeCursor previous()--------------
		columnCursor.previous( );
		assertTrue( countryCursor.getObject( "level11" ).equals( "CN" ) );
		assertTrue( cityCursor.getObject( "level12" ).equals( "SZ" ) );
		assertTrue( productCursor.getObject( "level13" ).equals( "S1" ) );	
		
		//-------------------------------edgeCursor setPosition()--------------
		columnCursor.setPosition( 13 );
		try
		{
			countryCursor.getObject( "level11" );
		}
		catch ( OLAPException e )
		{
		}

		//-------------------------------edgeCursor relative()--------------
		columnCursor.beforeFirst( );
		columnCursor.relative( 6 );
		assertTrue( countryCursor.getObject( "level11" ).equals( "CN" ) );
		assertTrue( cityCursor.getObject( "level12" ).equals( "SZ" ) );
		assertTrue( productCursor.getObject( "level13" ).equals( "S2" ) );
		
		//-------------------------------edgeCursor beforeFirst(),next(),setPosition()--------------
		columnCursor.beforeFirst( );
		columnCursor.next( );
		columnCursor.next( );
		columnCursor.next( );		
		columnCursor.setPosition( 1 );
		assertTrue( countryCursor.getObject( "level11" ).equals( "CN" ) );
		assertTrue( cityCursor.getObject( "level12" ).equals( "BJ" ) );
		assertTrue( productCursor.getObject( "level13" ).equals( "HD" ) );

		//------------------------------dimensionCursor setPosition()--------------
		columnCursor.beforeFirst( );
		columnCursor.next( );
		columnCursor.next( );
		columnCursor.next( );		
		productCursor.setPosition( 1 );
		assertTrue( countryCursor.getObject( "level11" ).equals( "CN" ) );
		assertTrue( cityCursor.getObject( "level12" ).equals( "SH" ) );
		assertTrue( productCursor.getObject( "level13" ).equals( "ZJ" ) );

		//------------------------------dimensionCursor next()--------------
		columnCursor.beforeFirst( );
		columnCursor.next( );
		countryCursor.next( );
		assertTrue( countryCursor.getObject( "level11" ).equals( "JP" ) );
		assertTrue( cityCursor.getObject( "level12" ).equals( "IL" ) );
		assertTrue( productCursor.getObject( "level13" ).equals( "P1" ) );
		
		columnCursor.afterLast( );		
		try
		{
			productCursor.getObject( "level13" ).equals( "PUDIAN" );
			fail("should not get here");
		}
		catch ( OLAPException e )
		{
		}
		
		//------------------------------dimensionCursor setPosition(),getEdgeStart(),getEdgeEnd()--------------
		columnCursor.beforeFirst( );
		columnCursor.setPosition( 1 );
		assertTrue( countryCursor.getEdgeStart( ) == 0 );
		assertTrue( countryCursor.getEdgeEnd( ) == 5 );

		assertTrue( cityCursor.getEdgeStart( ) == 0 );
		assertTrue( cityCursor.getEdgeEnd( ) == 1 );
			
		assertTrue( productCursor.getEdgeStart( )==1 );
		assertTrue( productCursor.getEdgeEnd( ) ==1 );
		
		columnCursor.setPosition( 6 );
		assertTrue( countryCursor.getEdgeStart( ) == 6 );
		assertTrue( countryCursor.getEdgeEnd( ) == 7 );

		assertTrue( cityCursor.getEdgeStart( ) == 6 );
		assertTrue( cityCursor.getEdgeEnd( ) == 6 );
			
		assertTrue( productCursor.getEdgeStart( )==6 );
		assertTrue( productCursor.getEdgeEnd( ) ==6 );
		
		columnCursor.setPosition( 4 );
		assertTrue( countryCursor.getEdgeStart( ) == 0 );
		assertTrue( countryCursor.getEdgeEnd( ) == 5 );

		assertTrue( cityCursor.getEdgeStart( ) == 4 );
		assertTrue( cityCursor.getEdgeEnd( ) == 5 );
			
		assertTrue( productCursor.getEdgeStart( )==4 );
		assertTrue( productCursor.getEdgeEnd( ) ==4 );

		columnCursor.setPosition( 12 );
		assertTrue( countryCursor.getEdgeStart( ) == 10 );
		assertTrue( countryCursor.getEdgeEnd( ) == 12 );

		assertTrue( cityCursor.getEdgeStart( ) == 12 );
		assertTrue( cityCursor.getEdgeEnd( ) == 12 );

		assertTrue( productCursor.getEdgeStart( ) == 12 );
		assertTrue( productCursor.getEdgeEnd( ) == 12 );
		
		columnCursor.beforeFirst( );
		columnCursor.setPosition( 13 );
		assertTrue( countryCursor.getEdgeStart( ) == -1 );
		assertTrue( countryCursor.getEdgeEnd( ) == -1 );
								
		columnCursor.beforeFirst( );
		Object obj1, obj2, obj3;
		while ( columnCursor.next( ) )
		{
			obj1 = countryCursor.getObject( "level11" );
			print( obj1 );
			obj2 = cityCursor.getObject( "level12" );
			print( obj2 );
			obj3 = productCursor.getObject( "level13" );
			print( obj3 );
		}

		try
		{
			countryCursor.getObject( "level11" );
			fail( "should not get here" );
		}
		catch ( OLAPException e )
		{
		}
	}
	
	private void print( Object value )
	{
		System.out.println( value );
	}

}

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
		rowGrandTotal.setExpression( "measure1" );
		rowGrandTotal.addAggregateOn( "level21" );

		IBinding columnGrandTotal = new Binding( "columnGrandTotal" );
		columnGrandTotal.setAggrFunction( BuiltInAggregationFactory.TOTAL_SUM_FUNC );
		columnGrandTotal.setExpression( "measure1" );
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

		columnCursor.beforeFirst( );
		assertTrue( countryCursor.isBeforeFirst( ) );
		assertTrue( cityCursor.isBeforeFirst( ) );
		assertTrue( productCursor.isBeforeFirst( ) );
		
		columnCursor.setPosition( 5 );
		assertTrue( countryCursor.getObject( "level11" ).equals( "CN" ) );
		assertTrue( cityCursor.getObject( "level12" ).equals( "SZ" ) );
		assertTrue( productCursor.getObject( "level13" ).equals( "XXX" ) );
		
		columnCursor.previous( );
		assertTrue( countryCursor.getObject( "level11" ).equals( "CN" ) );
		assertTrue( cityCursor.getObject( "level12" ).equals( "SZ" ) );
		assertTrue( productCursor.getObject( "level13" ).equals( "CCC" ) );	
		
		try
		{
			columnCursor.setPosition( 100 );
			fail( "should not get here" );
		}
		catch ( OLAPException e )
		{
		}
		
		columnCursor.beforeFirst( );
		columnCursor.relative( 6 );
		assertTrue( countryCursor.getObject( "level11" ).equals( "CN" ) );
		assertTrue( cityCursor.getObject( "level12" ).equals( "SZ" ) );
		assertTrue( productCursor.getObject( "level13" ).equals( "XXX" ) );
		
		
		columnCursor.beforeFirst( );
		columnCursor.next( );
		productCursor.setPosition( 1 );
		assertTrue( countryCursor.getObject( "level11" ).equals( "CN" ) );
		assertTrue( cityCursor.getObject( "level12" ).equals( "BJ" ) );
		assertTrue( productCursor.getObject( "level13" ).equals( "PUDIAN" ) );
		
		columnCursor.beforeFirst( );
		columnCursor.next( );
		productCursor.setPosition( 100 );
		assertTrue( countryCursor.getObject( "level11" ).equals( "CN" ) );
		assertTrue( cityCursor.getObject( "level12" ).equals( "BJ" ) );
		try
		{
			productCursor.getObject( "level13" ).equals( "PUDIAN" );
			fail("should not get here");
		}
		catch ( OLAPException e )
		{
		}
		
		columnCursor.afterLast( );		
		try
		{
			productCursor.getObject( "level13" ).equals( "PUDIAN" );
			fail("should not get here");
		}
		catch ( OLAPException e )
		{
		}	
								
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
			fail("should not get here");
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

/*******************************************************************************
 * Copyright (c) 2004 ,2005 Actuate Corporation.
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
import org.eclipse.birt.data.engine.aggregation.BuiltInAggregationFactory;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IDimensionDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IHierarchyDefinition;
import org.eclipse.birt.data.engine.olap.api.query.impl.CubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.impl.CubeQueryExecutor;
import org.eclipse.birt.data.engine.olap.query.view.BirtCubeView;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.Scriptable;

public class CursorModelTest extends TestCase
{
	private Scriptable scope;
	/*
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp( ) throws Exception
	{
		super.setUp( );
		new CubeCreator( ).createCube( );
		this.scope = new ImporterTopLevel();
	}

	/**
	 * 
	 * @throws OLAPException
	 * @throws BirtException 
	 */
	public void testCursorModel1( ) throws OLAPException, BirtException
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
		BirtCubeView cubeView = new BirtCubeView( new CubeQueryExecutor(cqd,this.scope,DataEngineContext.newInstance( DataEngineContext.DIRECT_PRESENTATION, scope, null, null )) );

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

		rowCursor.beforeFirst( );
		while ( rowCursor.next( ) )
		{
			print( timeCursor.getObject( "level21" ) );
			columnCursor.beforeFirst( );
			while ( columnCursor.next( ) )
			{
				// print measure
				print( dataCursor.getObject( "measure1" ) );
				print( dataCursor.getObject( "measure2" ) );
			}
		}

		System.out.println( "query result with dimension cursors:" );
		countryCursor.beforeFirst( );
		while ( countryCursor.next( ) )
		{
			cityCursor.beforeFirst( );
			while ( cityCursor.next( ) )
			{
				productCursor.beforeFirst( );
				while ( productCursor.next( ) )
				{
					print( countryCursor.getObject( "level11" ) );
					print( cityCursor.getObject( "level12" ) );
					print( productCursor.getObject( "level13" ) );
				}
			}
		}

		timeCursor.beforeFirst( );
		while ( timeCursor.next( ) )
		{
			print( timeCursor.getString( 0 ) );
			countryCursor.beforeFirst( );

			while ( countryCursor.next( ) )
			{
				cityCursor.beforeFirst( );
				while ( cityCursor.next( ) )
				{

					productCursor.beforeFirst( );
					while ( productCursor.next( ) )
					{
						print( dataCursor.getObject( 0 ) );
						print( dataCursor.getObject( 1 ) );
					}
				}
			}
		}
	}

	/**
	 * without row edge
	 * @throws OLAPException
	 * @throws BirtException 
	 */
	public void testCursorModel2( ) throws OLAPException, BirtException
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( "cube" );

		cqd.createMeasure( "measure1" );
		cqd.createMeasure( "measure2" );

		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IDimensionDefinition geographyDim = columnEdge.createDimension( "geography" );
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
		BirtCubeView cubeView = new BirtCubeView( new CubeQueryExecutor(cqd,this.scope,DataEngineContext.newInstance( DataEngineContext.DIRECT_PRESENTATION, scope, null, null )) );

		CubeCursor dataCursor = cubeView.getCubeCursor( );

		// retrieve the edge cursors
		EdgeCursor columnCursor = cubeView.getColumnEdgeView( ).getEdgeCursor( );

		DimensionCursor countryCursor = (DimensionCursor) columnCursor.getDimensionCursor( )
				.get( 0 );
		DimensionCursor cityCursor = (DimensionCursor) columnCursor.getDimensionCursor( )
				.get( 1 );
		DimensionCursor productCursor = (DimensionCursor) columnCursor.getDimensionCursor( )
				.get( 2 );

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

		columnCursor.beforeFirst( );
		while ( columnCursor.next( ) )
		{
			// print measure
			print( dataCursor.getObject( "measure1" ) );
			print( dataCursor.getObject( "measure2" ) );
		}

		System.out.println( "query result with dimension cursors:" );
		countryCursor.beforeFirst( );
		while ( countryCursor.next( ) )
		{
			cityCursor.beforeFirst( );
			while ( cityCursor.next( ) )
			{
				productCursor.beforeFirst( );
				while ( productCursor.next( ) )
				{
					print( countryCursor.getObject( "level11" ) );
					print( cityCursor.getObject( "level12" ) );
					print( productCursor.getObject( "level13" ) );
				}
			}
		}

		countryCursor.beforeFirst( );
		while ( countryCursor.next( ) )
		{
			cityCursor.beforeFirst( );
			while ( cityCursor.next( ) )
			{

				productCursor.beforeFirst( );
				while ( productCursor.next( ) )
				{
					print( dataCursor.getObject( 0 ) );
					print( dataCursor.getObject( 1 ) );
				}
			}
		}
	}
	
	private void print( Object value )
	{
		System.out.println( value );
	}

	/**
	 * without column edge
	 * @throws OLAPException
	 * @throws BirtException 
	 */
	public void testCursorModel3( ) throws OLAPException, BirtException
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( "cube" );

		cqd.createMeasure( "measure1" );
		cqd.createMeasure( "measure2" );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition timeDim = columnEdge.createDimension( "time" );
		IHierarchyDefinition timeHier = timeDim.createHierarchy( "timeHierarchy" );
		timeHier.createLevel( "level21" );

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
		BirtCubeView cubeView = new BirtCubeView( new CubeQueryExecutor(cqd,this.scope, DataEngineContext.newInstance( DataEngineContext.DIRECT_PRESENTATION, scope, null, null )) );

		CubeCursor dataCursor = cubeView.getCubeCursor( );

		// retrieve the edge cursors
		// EdgeCursor pageCursor = cubeView.getMeasureEdgeView( );
		EdgeCursor rowCursor = cubeView.getRowEdgeView( ).getEdgeCursor( );

		DimensionCursor timeCursor = (DimensionCursor) rowCursor.getDimensionCursor( )
				.get( 0 );

		rowCursor.beforeFirst( );
		while ( rowCursor.next( ) )
		{
			print( timeCursor.getObject( "level21" ) );
			// print measure
			print( dataCursor.getObject( "measure1" ) );
			print( dataCursor.getObject( "measure2" ) );
		}

		System.out.println( "query result with dimension cursors:" );

		timeCursor.beforeFirst( );
		while ( timeCursor.next( ) )
		{
			print( timeCursor.getString( 0 ) );

			print( dataCursor.getObject( 0 ) );
			print( dataCursor.getObject( 1 ) );
		}

	}
}

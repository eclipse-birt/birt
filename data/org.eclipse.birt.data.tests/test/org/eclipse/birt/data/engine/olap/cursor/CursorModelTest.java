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

import java.util.ArrayList;
import java.util.List;

import javax.olap.OLAPException;
import javax.olap.cursor.CubeCursor;
import javax.olap.cursor.DimensionCursor;
import javax.olap.cursor.EdgeCursor;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.aggregation.IBuildInAggregation;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.DataEngineImpl;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IDimensionDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IHierarchyDefinition;
import org.eclipse.birt.data.engine.olap.api.query.impl.CubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.impl.CubeQueryExecutor;
import org.eclipse.birt.data.engine.olap.query.view.BirtCubeView;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.Scriptable;

import testutil.BaseTestCase;

public class CursorModelTest extends BaseTestCase
{
	private Scriptable  scope;
	private DataEngineImpl de;
	private CubeUtility creator;
	
	/*
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp( ) throws Exception
	{
		super.setUp( );
		
		this.scope = new ImporterTopLevel();
		de = (DataEngineImpl) DataEngine.newDataEngine( DataEngineContext.newInstance( DataEngineContext.DIRECT_PRESENTATION,
				scope,
				null,
				null ) );
		creator = new CubeUtility();
		creator.createCube(de );
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
		rowGrandTotal.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		rowGrandTotal.addAggregateOn( "level21" );
		rowGrandTotal.addAggregateOn( "level22" );

		IBinding columnGrandTotal = new Binding( "columnGrandTotal" );
		columnGrandTotal.setAggrFunction( IBuildInAggregation.TOTAL_AVE_FUNC );
		columnGrandTotal.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		columnGrandTotal.addAggregateOn( "level11" );
		columnGrandTotal.addAggregateOn( "level12" );
		columnGrandTotal.addAggregateOn( "level13" );
		columnGrandTotal.addAggregateOn( "level14" );
		
		IBinding totalGrandTotal = new Binding( "totalGrandTotal" );
		totalGrandTotal.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		totalGrandTotal.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		
		cqd.addBinding( rowGrandTotal );
		cqd.addBinding( columnGrandTotal );
		cqd.addBinding( totalGrandTotal );

		
		// Create cube view.
		BirtCubeView cubeView = new BirtCubeView( new CubeQueryExecutor(cqd,de.getSession( ),this.scope,de.getContext( )) );

		CubeCursor dataCursor = cubeView.getCubeCursor( );

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
	 * without row edge
	 * @throws OLAPException
	 * @throws BirtException 
	 */
	public void testCursorModel2( ) throws OLAPException, BirtException
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( CubeUtility.cubeName );

		cqd.createMeasure( "measure1" );

		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		hier1.createLevel( "level11" );
		IDimensionDefinition dim2 = columnEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		hier2.createLevel( "level12" );
		IDimensionDefinition dim3 = columnEdge.createDimension( "dimension3" );
		IHierarchyDefinition hier3 = dim3.createHierarchy( "dimension3" );
		hier3.createLevel( "level13" );
		IDimensionDefinition dim4 = columnEdge.createDimension( "dimension4" );
		IHierarchyDefinition hier4 = dim4.createHierarchy( "dimension4" );
		hier4.createLevel( "level14" );

		// Create cube view.
		BirtCubeView cubeView = new BirtCubeView( new CubeQueryExecutor( cqd,
				de.getSession( ),
				this.scope,
				de.getContext( ) ) );

		CubeCursor dataCursor = cubeView.getCubeCursor( );

		// retrieve the edge cursors
		EdgeCursor columnCursor = cubeView.getColumnEdgeView( ).getEdgeCursor( );

		DimensionCursor countryCursor = (DimensionCursor) columnCursor.getDimensionCursor( )
				.get( 0 );
		DimensionCursor cityCursor = (DimensionCursor) columnCursor.getDimensionCursor( )
				.get( 1 );
		DimensionCursor streetCursor = (DimensionCursor) columnCursor.getDimensionCursor( )
				.get( 2 );
		DimensionCursor timeCursor = (DimensionCursor) columnCursor.getDimensionCursor( )
				.get( 3 );	

		testOut.print( creator.printCubeAlongDimension( dataCursor,
				countryCursor,
				cityCursor,
				streetCursor,
				timeCursor,
				null,
				null ) );
		try
		{
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
	public void testCursorModel3( ) throws OLAPException, BirtException
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( CubeUtility.cubeName );

		cqd.createMeasure( "measure1" );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = rowEdge.createDimension( "dimension5" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension5" );
		hier1.createLevel( "level21" );

		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension6" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension6" );
		hier2.createLevel( "level22" );

		// Create cube view.
		BirtCubeView cubeView = new BirtCubeView( new CubeQueryExecutor( cqd,
				de.getSession( ),
				this.scope,
				de.getContext( ) ) );

		CubeCursor dataCursor = cubeView.getCubeCursor( );

		// retrieve the edge cursors
		// EdgeCursor pageCursor = cubeView.getMeasureEdgeView( );
		EdgeCursor rowCursor = cubeView.getRowEdgeView( ).getEdgeCursor( );

		DimensionCursor productCursor1 = (DimensionCursor) rowCursor.getDimensionCursor( )
				.get( 0 );
		DimensionCursor productCursor2 = (DimensionCursor) rowCursor.getDimensionCursor( )
				.get( 1 );

		testOut.print( creator.printCubeAlongDimension( dataCursor,
				null,
				null,
				null,
				null,
				productCursor1,
				productCursor2 ) );
		try
		{
			this.checkOutputFile( );
		}
		catch ( Exception e )
		{
			fail( "fail to get here!" );
		}
	}
		
	/**
	 * test populate data along dimension cursor
	 * @throws DataException 
	 * @throws OLAPException 
	 */
	public void testCursorModel4( ) throws DataException, OLAPException
	{

		ICubeQueryDefinition cqd = creator.createQueryDefinition( );

		IBinding rowGrandTotal = new Binding( "rowGrandTotal" );
		rowGrandTotal.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		rowGrandTotal.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		rowGrandTotal.addAggregateOn( "level21" );
		rowGrandTotal.addAggregateOn( "level22" );

		IBinding columnGrandTotal = new Binding( "columnGrandTotal" );
		columnGrandTotal.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		columnGrandTotal.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		columnGrandTotal.addAggregateOn( "level11" );
		columnGrandTotal.addAggregateOn( "level12" );
		columnGrandTotal.addAggregateOn( "level13" );
		columnGrandTotal.addAggregateOn( "level14" );

		cqd.addBinding( rowGrandTotal );
		cqd.addBinding( columnGrandTotal );

		// Create cube view.
		BirtCubeView cubeView = new BirtCubeView( new CubeQueryExecutor( cqd,
				de.getSession( ),
				this.scope,
				de.getContext( ) ) );

		CubeCursor dataCursor = cubeView.getCubeCursor( );

		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "level11" );
		columnEdgeBindingNames.add( "level12" );
		columnEdgeBindingNames.add( "level13" );
		columnEdgeBindingNames.add( "level14" );

		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "level21" );
		rowEdgeBindingNames.add( "level22" );

		List measureBindingNames = new ArrayList( );
		measureBindingNames.add( "measure1" );

		List dimCursorOnColumn = cubeView.getColumnEdgeView( )
				.getEdgeCursor( )
				.getDimensionCursor( );
		List dimCursorOnRow = cubeView.getRowEdgeView( )
				.getEdgeCursor( )
				.getDimensionCursor( );

		testOut.print( creator.printCubeAlongDimension( dataCursor,
				(DimensionCursor) dimCursorOnColumn.get( 0 ),
				(DimensionCursor) dimCursorOnColumn.get( 1 ),
				(DimensionCursor) dimCursorOnColumn.get( 2 ),
				(DimensionCursor) dimCursorOnColumn.get( 3 ),
				(DimensionCursor) dimCursorOnRow.get( 0 ),
				(DimensionCursor) dimCursorOnRow.get( 1 ) ) );

		try
		{
			this.checkOutputFile( );
		}
		catch ( Exception e )
		{
			fail( "fail to get here!" );
		}
	}
	
	
	/**
	 *
	 * 
	 * @throws OLAPException
	 * @throws BirtException
	 */
	public void testCursorModel5( ) throws OLAPException, BirtException
	{
		ICubeQueryDefinition cqd = this.creator.createQueryDefinition( );

		IBinding rowGrandTotal = new Binding( "rowGrandTotal" );
		rowGrandTotal.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		rowGrandTotal.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		rowGrandTotal.addAggregateOn( "level21" );
		rowGrandTotal.addAggregateOn( "level22" );

		IBinding rowGrandAvg = new Binding( "rowGrandAvg" );
		rowGrandAvg.setAggrFunction( IBuildInAggregation.TOTAL_AVE_FUNC );
		rowGrandAvg.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		rowGrandAvg.addAggregateOn( "level21" );
		rowGrandAvg.addAggregateOn( "level22" );
		
		cqd.addBinding( rowGrandTotal );
		cqd.addBinding( rowGrandAvg );

		// Create cube view.
		BirtCubeView cubeView = new BirtCubeView( new CubeQueryExecutor(cqd,de.getSession( ),this.scope,de.getContext( )) );

		CubeCursor dataCursor = cubeView.getCubeCursor( );

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
		
		List rowGrandTotalNames = new ArrayList( );
		rowGrandTotalNames.add( "rowGrandTotal" );
		rowGrandTotalNames.add( "rowGrandAvg" );
		
		try
		{
			testOut.print( creator.printCubeAlongEdge( dataCursor,
					columnEdgeBindingNames,
					rowEdgeBindingNames,
					measureBindingNames,
					rowGrandTotalNames,
					null,
					null,
					null));
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
	public void testCursorOnCountry( ) throws OLAPException, BirtException
	{
		ICubeQueryDefinition cqd = this.creator.createQueryDefinition( );

		IBinding rowGrandTotal = new Binding( "countryGrandTotal" );
		rowGrandTotal.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		rowGrandTotal.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		rowGrandTotal.addAggregateOn( "level11" );
		rowGrandTotal.addAggregateOn( "level21" );
		rowGrandTotal.addAggregateOn( "level22" );
		
		cqd.addBinding( rowGrandTotal );

		// Create cube view.
		BirtCubeView cubeView = new BirtCubeView( new CubeQueryExecutor(cqd,de.getSession( ),this.scope,de.getContext( )) );

		CubeCursor dataCursor = cubeView.getCubeCursor( );

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
	
	/**
	 * without measure
	 * @throws Exception 
	 */
	public void testCursorWithoutMeasure( ) throws Exception
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( CubeUtility.cubeName );

		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition rowdim1 = rowEdge.createDimension( "dimension5" );
		IHierarchyDefinition rowhier1 = rowdim1.createHierarchy( "dimension5" );
		rowhier1.createLevel( "level21" );

		IDimensionDefinition rowdim2 = rowEdge.createDimension( "dimension6" );
		IHierarchyDefinition rowhier2 = rowdim2.createHierarchy( "dimension6" );
		rowhier2.createLevel( "level22" );
		
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		hier1.createLevel( "level11" );
		IDimensionDefinition dim2 = columnEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		hier2.createLevel( "level12" );
		IDimensionDefinition dim3 = columnEdge.createDimension( "dimension3" );
		IHierarchyDefinition hier3 = dim3.createHierarchy( "dimension3" );
		hier3.createLevel( "level13" );
		IDimensionDefinition dim4 = columnEdge.createDimension( "dimension4" );
		IHierarchyDefinition hier4 = dim4.createHierarchy( "dimension4" );
		hier4.createLevel( "level14" );

		// Create cube view.
		BirtCubeView cubeView = new BirtCubeView( new CubeQueryExecutor(cqd,de.getSession( ),this.scope,de.getContext( )) );

		CubeCursor dataCursor = cubeView.getCubeCursor( );

		List columnEdgeBindingNames = new ArrayList();
		columnEdgeBindingNames.add( "level11" );
		columnEdgeBindingNames.add( "level12" );
		columnEdgeBindingNames.add( "level13" );	
		columnEdgeBindingNames.add( "level14" );	
		
		List rowEdgeBindingNames = new ArrayList();
		rowEdgeBindingNames.add( "level21" );
		rowEdgeBindingNames.add( "level22" );
	
		testOut.print( creator.printCubeAlongEdge( dataCursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				null,
				null,
				null,
				null,
				null ) );
		this.checkOutputFile( );
		try
		{

			dataCursor.getObject( "measure1" );
		}
		catch ( Exception e )
		{
			assertTrue( e instanceof OLAPException );
		}
	}
	

	/**
	 * 
	 * @throws OLAPException
	 * @throws BirtException 
	 */
	public void testCursorModel6( ) throws OLAPException, BirtException
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( CubeUtility.cubeName );

		cqd.createMeasure( "measure1" );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim = columnEdge.createDimension( "dimension5" );
		IHierarchyDefinition dimHier = dim.createHierarchy( "dimension5" );
		dimHier.createLevel( "level21" );

		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IDimensionDefinition geographyDim = rowEdge.createDimension( "dimension1" );
		IHierarchyDefinition geographyHier = geographyDim.createHierarchy( "dimension1" );
		geographyHier.createLevel( "level11" );
		IDimensionDefinition geographyDim3 = rowEdge.createDimension( "dimension3" );
		IHierarchyDefinition geographyHier3 = geographyDim3.createHierarchy( "dimension3" );
		geographyHier3.createLevel( "level13" );

		IBinding rowGrandTotal = new Binding( "rowGrandTotal" );
		rowGrandTotal.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		rowGrandTotal.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		rowGrandTotal.addAggregateOn( "level21" );

		IBinding columnGrandTotal = new Binding( "columnGrandTotal" );
		columnGrandTotal.setAggrFunction( IBuildInAggregation.TOTAL_AVE_FUNC );
		columnGrandTotal.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		columnGrandTotal.addAggregateOn( "level11" );
		columnGrandTotal.addAggregateOn( "level13" );
		
		IBinding totalGrandTotal = new Binding( "totalGrandTotal" );
		totalGrandTotal.setAggrFunction( IBuildInAggregation.TOTAL_COUNTDISTINCT_FUNC );
		totalGrandTotal.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		
		cqd.addBinding( rowGrandTotal );
		cqd.addBinding( columnGrandTotal );
		cqd.addBinding( totalGrandTotal );

		// Create cube view.
		BirtCubeView cubeView = new BirtCubeView( new CubeQueryExecutor(cqd,de.getSession( ),this.scope,de.getContext( )) );

		CubeCursor dataCursor = cubeView.getCubeCursor( );

		List columnEdgeBindingNames = new ArrayList();
		columnEdgeBindingNames.add( "level11" );
		columnEdgeBindingNames.add( "level13" );	
		
		List rowEdgeBindingNames = new ArrayList();
		rowEdgeBindingNames.add( "level21" );
		
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
}

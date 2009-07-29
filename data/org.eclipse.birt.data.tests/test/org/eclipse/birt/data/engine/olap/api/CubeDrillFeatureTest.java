/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.olap.api;

import java.util.ArrayList;
import java.util.List;

import javax.olap.OLAPException;
import javax.olap.cursor.CubeCursor;
import javax.olap.cursor.DimensionCursor;
import javax.olap.cursor.EdgeCursor;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.aggregation.api.IBuildInAggregation;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.impl.DataEngineImpl;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IDimensionDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDrillFilter;
import org.eclipse.birt.data.engine.olap.api.query.IHierarchyDefinition;
import org.eclipse.birt.data.engine.olap.impl.query.CubeQueryDefinition;

import testutil.BaseTestCase;

public class CubeDrillFeatureTest extends BaseTestCase
{

	private final static String cubeName = "DrilledCube";

	
	public void testDrillDownOperation( ) throws Exception
	{

		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		hier1.createLevel( "level11" );

		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		hier2.createLevel( "level21" );
		hier2.createLevel( "level22" );
		hier2.createLevel( "level23" );

		cqd.createMeasure( "measure1" );

		IBinding binding1 = new Binding( "edge1level1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "edge1level2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding3 = new Binding( "edge1level3" );

		binding3.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level13\"]" ) );
		cqd.addBinding( binding3 );
		
		IBinding binding4 = new Binding( "edge1level4" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level14\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "edge2level21" );

		binding5.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"]" ) );
		cqd.addBinding( binding5 );


		IBinding binding6 = new Binding( "edge2level22" );
		binding6.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level22\"]" ) );
		cqd.addBinding( binding6 );

		IBinding binding7 = new Binding( "edge2level23" );
		binding7.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level23\"]" ) );
		cqd.addBinding( binding7 );

		IBinding binding8 = new Binding( "measure1" );
		binding8.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		cqd.addBinding( binding8 );

		IEdgeDrillFilter filter = columnEdge.createDrillFilter( "drill1" );
		filter.setTargetHierarchy( hier1 );
		filter.setTargetLevelName( "level14" );
		List memberList = new ArrayList( );
		memberList.add( new Object[]{
			"CAR"
		} );
		memberList.add( null );
		memberList.add( null );
		memberList.add( null );
		filter.setTuple( memberList );

		IEdgeDrillFilter filter2 = columnEdge.createDrillFilter( "drill2" );
		filter2.setTargetHierarchy( hier1 );
		filter2.setTargetLevelName( "level12" );
		memberList = new ArrayList( );
		memberList.add( new Object[]{
			"MOTOR"
		} );
		memberList.add( new Object[]{
				null
			} );
		filter2.setTuple( memberList );

		IEdgeDrillFilter filter3 = columnEdge.createDrillFilter( "drill3" );
		filter3.setTargetHierarchy( hier1 );
		filter3.setTargetLevelName( "level13" );
		memberList = new ArrayList( );
		memberList.add( new Object[]{
			"MOTOR"
		} );
		memberList.add( new Object[]{
			Integer.valueOf( "2005" )
		} );
		memberList.add( null );

		filter3.setTuple( memberList );

		DataEngineImpl engine = (DataEngineImpl) DataEngine.newDataEngine( createPresentationContext( ) );
		
		DrilledCube cube = new DrilledCube( );
		cube.createCube( engine );
		
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		columnEdgeBindingNames.add( "edge1level3" );
		columnEdgeBindingNames.add( "edge1level4" );
		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "edge2level21" );
		rowEdgeBindingNames.add( "edge2level22" );
		rowEdgeBindingNames.add( "edge2level23" );

		printCube( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				"measure1",
				null,
				null,
				null, null );

		engine.shutdown( );
	}
	
	public void testDrillDownOperationOnRowAndColumn( ) throws Exception
	{

		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		hier1.createLevel( "level11" );

		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		hier2.createLevel( "level21" );

		cqd.createMeasure( "measure1" );

		IBinding binding1 = new Binding( "edge1level1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "edge1level2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding3 = new Binding( "edge1level3" );

		binding3.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level13\"]" ) );
		cqd.addBinding( binding3 );
		
		IBinding binding4 = new Binding( "edge1level4" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level14\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "edge2level21" );

		binding5.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"]" ) );
		cqd.addBinding( binding5 );


		IBinding binding6 = new Binding( "edge2level22" );
		binding6.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level22\"]" ) );
		cqd.addBinding( binding6 );

		IBinding binding7 = new Binding( "edge2level23" );
		binding7.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level23\"]" ) );
		cqd.addBinding( binding7 );

		IBinding binding8 = new Binding( "measure1" );
		binding8.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		cqd.addBinding( binding8 );

		IEdgeDrillFilter filter = columnEdge.createDrillFilter( "drill1" );
		filter.setTargetHierarchy( hier1 );
		filter.setTargetLevelName( "level14" );
		List memberList = new ArrayList( );
		memberList.add( new Object[]{
			"CAR"
		} );
		memberList.add( null );
		memberList.add( null );
		memberList.add( null );
		filter.setTuple( memberList );

		IEdgeDrillFilter filter2 = columnEdge.createDrillFilter( "drill2");
		filter2.setTargetHierarchy( hier1 );
		filter2.setTargetLevelName( "level12" );
		memberList = new ArrayList( );
		memberList.add( new Object[]{
			"MOTOR"
		} );
		memberList.add( null );
		filter2.setTuple( memberList );

		IEdgeDrillFilter filter3 = columnEdge.createDrillFilter( "drill3" );
		filter3.setTargetHierarchy( hier1 );
		filter3.setTargetLevelName( "level13" );
		memberList = new ArrayList( );
		memberList.add( new Object[]{
			"MOTOR"
		} );
		memberList.add( new Object[]{
			Integer.valueOf( "2005" )
		} );
		memberList.add( null );
		filter3.setTuple( memberList );
		
		IEdgeDrillFilter filter4 = rowEdge.createDrillFilter( "drill4" );
		filter4.setTargetHierarchy( hier2 );
		filter4.setTargetLevelName( "level23" );
		memberList = new ArrayList( );
		memberList.add( new Object[]{
			"US"
		} );
		memberList.add( null );
		memberList.add( null );
		filter4.setTuple( memberList );
		
		IEdgeDrillFilter filter5 = rowEdge.createDrillFilter( "drill5" );
		filter5.setTargetHierarchy( hier2 );
		filter5.setTargetLevelName( "level22" );
		memberList = new ArrayList( );
		memberList.add( new Object[]{
			"CN"
		} );
		memberList.add( null );
		filter5.setTuple( memberList );

		DataEngineImpl engine = (DataEngineImpl) DataEngine.newDataEngine( createPresentationContext( ) );
		
		DrilledCube cube = new DrilledCube( );
		cube.createCube( engine );
		
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		columnEdgeBindingNames.add( "edge1level3" );
		columnEdgeBindingNames.add( "edge1level4" );
		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "edge2level21" );
		rowEdgeBindingNames.add( "edge2level22" );
		rowEdgeBindingNames.add( "edge2level23" );

		printCube( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				"measure1",
				null,
				null,
				null, null );

		engine.shutdown( );
	}
	
	public void testDrillDownOperationOnRowAndColumn2( ) throws Exception
	{

		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		hier1.createLevel( "level11" );
		hier1.createLevel( "level12" );
		hier1.createLevel( "level13" );
				
		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		hier2.createLevel( "level21" );

		cqd.createMeasure( "measure1" );

		IBinding binding1 = new Binding( "edge1level1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "edge1level2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding3 = new Binding( "edge1level3" );

		binding3.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level13\"]" ) );
		cqd.addBinding( binding3 );
		
		IBinding binding4 = new Binding( "edge1level4" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level14\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "edge2level21" );

		binding5.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"]" ) );
		cqd.addBinding( binding5 );


		IBinding binding6 = new Binding( "edge2level22" );
		binding6.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level22\"]" ) );
		cqd.addBinding( binding6 );

		IBinding binding7 = new Binding( "edge2level23" );
		binding7.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level23\"]" ) );
		cqd.addBinding( binding7 );

		IBinding binding8 = new Binding( "measure1" );
		binding8.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		cqd.addBinding( binding8 );

		IEdgeDrillFilter filter = columnEdge.createDrillFilter( "drill1" );
		filter.setTargetHierarchy( hier1 );
		filter.setTargetLevelName( "level14" );
		List memberList = new ArrayList( );
		memberList.add( new Object[]{
			"CAR"
		} );
		memberList.add( null );
		memberList.add( null );
		memberList.add( null );		
		filter.setTuple( memberList );

		IEdgeDrillFilter filter2 = columnEdge.createDrillFilter( "drill2" );
		filter2.setTargetHierarchy( hier1 );
		filter2.setTargetLevelName( "level12" );
		memberList = new ArrayList( );
		memberList.add( new Object[]{
			"MOTOR"
		} );
		memberList.add( new Object[]{
			new Integer( 2005 )
		} );
		filter2.setTuple( memberList );

		IEdgeDrillFilter filter3 = columnEdge.createDrillFilter( "drill3" );
		filter3.setTargetHierarchy( hier1 );
		filter3.setTargetLevelName( "level11" );
		memberList = new ArrayList( );
		memberList.add( new Object[]{
			"BUS"
		} );
		filter3.setTuple( memberList );
		
		IEdgeDrillFilter filter4 = rowEdge.createDrillFilter( "drill4" );
		filter4.setTargetHierarchy( hier2 );
		filter4.setTargetLevelName( "level23" );
		memberList = new ArrayList( );
		memberList.add( new Object[]{
			"US"
		} );
		memberList.add( null );
		memberList.add( null );
		filter4.setTuple( memberList );
		
		IEdgeDrillFilter filter5 = rowEdge.createDrillFilter( "drill5" );
		filter5.setTargetHierarchy( hier2 );
		filter5.setTargetLevelName( "level22" );
		memberList = new ArrayList( );
		memberList.add( new Object[]{
			"CN"
		} );
		memberList.add( null );
		filter5.setTuple( memberList );

		DataEngineImpl engine = (DataEngineImpl) DataEngine.newDataEngine( createPresentationContext( ) );
		
		DrilledCube cube = new DrilledCube( );
		cube.createCube( engine );
		
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		columnEdgeBindingNames.add( "edge1level3" );
		columnEdgeBindingNames.add( "edge1level4" );
		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "edge2level21" );
		rowEdgeBindingNames.add( "edge2level22" );
		rowEdgeBindingNames.add( "edge2level23" );

		printCube( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				"measure1",
				null,
				null,
				null, null );

		engine.shutdown( );
	}
	
	/**
	 * test multiple value selection
	 * @throws Exception
	 */
	public void testDrillDownOperationOnRowAndColumn3( ) throws Exception
	{

		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		hier1.createLevel( "level11" );

		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		hier2.createLevel( "level21" );
		hier2.createLevel( "level22" );

		cqd.createMeasure( "measure1" );

		IBinding binding1 = new Binding( "edge1level1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "edge1level2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding3 = new Binding( "edge1level3" );

		binding3.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level13\"]" ) );
		cqd.addBinding( binding3 );
		
		IBinding binding4 = new Binding( "edge1level4" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level14\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "edge2level21" );

		binding5.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"]" ) );
		cqd.addBinding( binding5 );


		IBinding binding6 = new Binding( "edge2level22" );
		binding6.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level22\"]" ) );
		cqd.addBinding( binding6 );

		IBinding binding7 = new Binding( "edge2level23" );
		binding7.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level23\"]" ) );
		cqd.addBinding( binding7 );

		IBinding binding8 = new Binding( "measure1" );
		binding8.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		cqd.addBinding( binding8 );

		IEdgeDrillFilter filter = columnEdge.createDrillFilter( "drill1" );
		filter.setTargetHierarchy( hier1 );
		filter.setTargetLevelName( "level14" );
		List memberList = new ArrayList( );
		memberList.add( new Object[]{
			"BICK"
		} );
		memberList.add( null );
		memberList.add( null );
		memberList.add( null );
		filter.setTuple( memberList );
		
		IEdgeDrillFilter filter1 = columnEdge.createDrillFilter( "drill11" );
		filter1.setTargetHierarchy( hier1 );
		filter1.setTargetLevelName( "level12" );
		memberList = new ArrayList( );
		memberList.add( new Object[]{
			"BICK"
		} );
		memberList.add( new Object[]{
				Integer.valueOf( 2005 ), Integer.valueOf( 2007 )
		} );
		filter1.setTuple( memberList );

		IEdgeDrillFilter filter2 = columnEdge.createDrillFilter( "drill2" );
		filter2.setTargetHierarchy( hier1 );
		filter2.setTargetLevelName( "level13" );
		memberList = new ArrayList( );
		memberList.add( new Object[]{
			"CAR"
		} );
		memberList.add( null );
		memberList.add( null );
		filter2.setTuple( memberList );

		IEdgeDrillFilter filter3 = columnEdge.createDrillFilter( "drill3" );
		filter3.setTargetHierarchy( hier1 );
		filter3.setTargetLevelName( "level14" );
		memberList = new ArrayList( );
		memberList.add( new Object[]{
			"CAR"
		} );
		memberList.add( new Object[]{
			Integer.valueOf( "2005" )
		} );
		memberList.add( new Object[]{
				"A1", "A2"
		} );
		memberList.add( null );
		filter3.setTuple( memberList );

		IEdgeDrillFilter filter4 = rowEdge.createDrillFilter( "drill4" );
		filter4.setTargetHierarchy( hier2 );
		filter4.setTargetLevelName( "level23" );
		memberList = new ArrayList( );
		memberList.add( new Object[]{
			"US"
		} );
		memberList.add( null );
		memberList.add( null );
		filter4.setTuple( memberList );

		IEdgeDrillFilter filter5 = rowEdge.createDrillFilter( "drill5" );
		filter5.setTargetHierarchy( hier2 );
		filter5.setTargetLevelName( "level23" );
		memberList = new ArrayList( );
		memberList.add( new Object[]{
			"CN"
		} );
		memberList.add( new Object[]{
				"BJ", "SZ"
		} );
		memberList.add( null );
		filter5.setTuple( memberList );

		DataEngineImpl engine = (DataEngineImpl) DataEngine.newDataEngine( createPresentationContext( ) );

		DrilledCube cube = new DrilledCube( );
		cube.createCube( engine );

		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		columnEdgeBindingNames.add( "edge1level3" );
		columnEdgeBindingNames.add( "edge1level4" );
		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "edge2level21" );
		rowEdgeBindingNames.add( "edge2level22" );
		rowEdgeBindingNames.add( "edge2level23" );

		printCube( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				"measure1",
				null,
				null,
				null, null );

		engine.shutdown( );
	}
	
	public void testDrillDownOperationOnRowAndColumn4( ) throws Exception
	{

		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		hier1.createLevel( "level11" );
		hier1.createLevel( "level12" );
		hier1.createLevel( "level13" );
		hier1.createLevel( "level14" );

		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		hier2.createLevel( "level21" );
		hier2.createLevel( "level22" );
		hier2.createLevel( "level23" );

		cqd.createMeasure( "measure1" );

		IBinding binding1 = new Binding( "edge1level1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "edge1level2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding3 = new Binding( "edge1level3" );

		binding3.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level13\"]" ) );
		cqd.addBinding( binding3 );
		
		IBinding binding4 = new Binding( "edge1level4" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level14\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "edge2level21" );

		binding5.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"]" ) );
		cqd.addBinding( binding5 );


		IBinding binding6 = new Binding( "edge2level22" );
		binding6.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level22\"]" ) );
		cqd.addBinding( binding6 );

		IBinding binding7 = new Binding( "edge2level23" );
		binding7.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level23\"]" ) );
		cqd.addBinding( binding7 );

		IBinding binding8 = new Binding( "measure1" );
		binding8.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		cqd.addBinding( binding8 );

		IEdgeDrillFilter filter = columnEdge.createDrillFilter( "drill1" );
		filter.setTargetHierarchy( hier1 );
		filter.setTargetLevelName( "level12" );
		List memberList = new ArrayList( );
		memberList.add( new Object[]{
			"BICK"
		} );
		memberList.add( null );
		filter.setTuple( memberList );
		
		IEdgeDrillFilter filter1 = columnEdge.createDrillFilter( "drill12" );
		filter1.setTargetHierarchy( hier1 );
		filter1.setTargetLevelName( "level13" );
		memberList = new ArrayList( );
		memberList.add( new Object[]{
			"BICK"
		} );
		memberList.add( new Object[]{
				Integer.valueOf( 2005 ), Integer.valueOf( 2007 )
		} );
		filter1.setTuple( memberList );

		IEdgeDrillFilter filter2 = columnEdge.createDrillFilter( "drill2" );
		filter2.setTargetHierarchy( hier1 );
		filter2.setTargetLevelName( "level13" );
		memberList = new ArrayList( );
		memberList.add( new Object[]{
			"CAR"
		} );
		memberList.add( null );
		memberList.add( null );
		filter2.setTuple( memberList );

		IEdgeDrillFilter filter3 = columnEdge.createDrillFilter( "drill3" );
		filter3.setTargetHierarchy( hier1 );
		filter3.setTargetLevelName( "level14" );
		memberList = new ArrayList( );
		memberList.add( new Object[]{
			"CAR"
		} );
		memberList.add( new Object[]{
			Integer.valueOf( "2005" )
		} );
		memberList.add( new Object[]{
				"A1", "A2"
		} );
		memberList.add( null );
		filter3.setTuple( memberList );

		IEdgeDrillFilter filter4 = rowEdge.createDrillFilter( "drill4" );
		filter4.setTargetHierarchy( hier2 );
		filter4.setTargetLevelName( "level22" );
		memberList = new ArrayList( );
		memberList.add( new Object[]{
			"US"
		} );
		memberList.add( new Object[]{
				"LA", "NY", "NZ"
		} );
		filter4.setTuple( memberList );

		DataEngineImpl engine = (DataEngineImpl) DataEngine.newDataEngine( createPresentationContext( ) );

		DrilledCube cube = new DrilledCube( );
		cube.createCube( engine );

		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		columnEdgeBindingNames.add( "edge1level3" );
		columnEdgeBindingNames.add( "edge1level4" );
		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "edge2level21" );
		rowEdgeBindingNames.add( "edge2level22" );
		rowEdgeBindingNames.add( "edge2level23" );

		printCube( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				"measure1",
				null,
				null,
				null, null );

		engine.shutdown( );
	}

	public void testDrillUpOperation( ) throws Exception
	{

		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		hier1.createLevel( "level11" );
		hier1.createLevel( "level12" );
		hier1.createLevel( "level13" );
		hier1.createLevel( "level14" );

		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		hier2.createLevel( "level21" );
		hier2.createLevel( "level22" );
		hier2.createLevel( "level23" );

		cqd.createMeasure( "measure1" );

		IBinding binding1 = new Binding( "edge1level1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "edge1level2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding3 = new Binding( "edge1level3" );

		binding3.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level13\"]" ) );
		cqd.addBinding( binding3 );
		
		IBinding binding4 = new Binding( "edge1level4" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level14\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "edge2level21" );

		binding5.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"]" ) );
		cqd.addBinding( binding5 );

		IBinding binding6 = new Binding( "edge2level22" );

		binding6.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level22\"]" ) );
		cqd.addBinding( binding6 );
		
		IBinding binding7 = new Binding( "edge2level23" );

		binding7.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level23\"]" ) );
		cqd.addBinding( binding7 );
		
		IBinding binding8 = new Binding( "measure1" );
		binding8.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		cqd.addBinding( binding8 );

		IEdgeDrillFilter filter = columnEdge.createDrillFilter( "drill1" );
		filter.setTargetHierarchy( hier1 );
		filter.setTargetLevelName( "level11" );
		List memberList = new ArrayList( );
		memberList.add( new Object[]{
			"CAR"
		} );
		filter.setTuple( memberList );

		IEdgeDrillFilter filter2 = columnEdge.createDrillFilter( "drill2" );
		filter2.setTargetHierarchy( hier1 );
		filter2.setTargetLevelName( "level12" );
		memberList = new ArrayList( );
		memberList.add( new Object[]{
			"MOTOR"
		} );
		memberList.add( new Object[]{
			new Integer( 2005 )
		} );
		filter2.setTuple( memberList );
		

		IEdgeDrillFilter filter3 = columnEdge.createDrillFilter( "drill2" );
		filter3.setTargetHierarchy( hier1 );
		filter3.setTargetLevelName( "level13" );
		memberList = new ArrayList( );
		memberList.add( new Object[]{
			"MOTOR"
		} );
		memberList.add( new Object[]{
			new Integer( 2007 )
		} );
		memberList.add( new Object[]{
			"A14"
		} );
		filter3.setTuple( memberList );

		DataEngineImpl engine = (DataEngineImpl) DataEngine.newDataEngine( createPresentationContext( ) );
		DrilledCube cube = new DrilledCube();
		cube.createCube( engine );
		
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		columnEdgeBindingNames.add( "edge1level3" );
		columnEdgeBindingNames.add( "edge1level4" );
		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "edge2level21" );
		rowEdgeBindingNames.add( "edge2level22" );
		rowEdgeBindingNames.add( "edge2level23" );

		printCube( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				"measure1", null, null, null, null );

		engine.shutdown( );
	}
	
	public void testDrillUpOperation1( ) throws Exception
	{

		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		hier1.createLevel( "level11" );
		hier1.createLevel( "level12" );
		hier1.createLevel( "level13" );
		hier1.createLevel( "level14" );

		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		hier2.createLevel( "level21" );
		hier2.createLevel( "level22" );
		hier2.createLevel( "level23" );

		cqd.createMeasure( "measure1" );

		IBinding binding1 = new Binding( "edge1level1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "edge1level2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding3 = new Binding( "edge1level3" );

		binding3.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level13\"]" ) );
		cqd.addBinding( binding3 );
		
		IBinding binding4 = new Binding( "edge1level4" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level14\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "edge2level21" );

		binding5.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"]" ) );
		cqd.addBinding( binding5 );

		IBinding binding6 = new Binding( "edge2level22" );

		binding6.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level22\"]" ) );
		cqd.addBinding( binding6 );
		
		IBinding binding7 = new Binding( "edge2level23" );

		binding7.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level23\"]" ) );
		cqd.addBinding( binding7 );
		
		IBinding binding8 = new Binding( "measure1" );
		binding8.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		cqd.addBinding( binding8 );

		IEdgeDrillFilter filter1 = columnEdge.createDrillFilter( "drill1" );
		filter1.setTargetHierarchy( hier1 );
		filter1.setTargetLevelName( "level12" );
		List memberList = new ArrayList( );
		memberList.add( new Object[]{
			"CAR"
		} );
		memberList.add( null );
		filter1.setTuple( memberList );
		
		IEdgeDrillFilter filter2 = columnEdge.createDrillFilter( "drill2" );
		filter2.setTargetHierarchy( hier1 );
		filter2.setTargetLevelName( "level12" );
		memberList = new ArrayList( );
		memberList.add( new Object[]{
			"MOTOR"
		} );
		memberList.add( new Object[]{
			null
		} );
		filter2.setTuple( memberList );
		
		DataEngineImpl engine = (DataEngineImpl) DataEngine.newDataEngine( createPresentationContext( ) );
		DrilledCube cube = new DrilledCube();
		cube.createCube( engine );
		
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		columnEdgeBindingNames.add( "edge1level3" );
		columnEdgeBindingNames.add( "edge1level4" );
		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "edge2level21" );
		rowEdgeBindingNames.add( "edge2level22" );
		rowEdgeBindingNames.add( "edge2level23" );

		printCube( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				"measure1", null, null, null, null );

		engine.shutdown( );
	}
		
	public void testDrillOperationWithGrandTotal( ) throws Exception
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		hier1.createLevel( "level11" );
		hier1.createLevel( "level12" );
		hier1.createLevel( "level13" );
				
		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		hier2.createLevel( "level21" );

		cqd.createMeasure( "measure1" );

		IBinding binding1 = new Binding( "edge1level1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "edge1level2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding3 = new Binding( "edge1level3" );

		binding3.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level13\"]" ) );
		cqd.addBinding( binding3 );
		
		IBinding binding4 = new Binding( "edge1level4" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level14\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "edge2level21" );

		binding5.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"]" ) );
		cqd.addBinding( binding5 );


		IBinding binding6 = new Binding( "edge2level22" );
		binding6.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level22\"]" ) );
		cqd.addBinding( binding6 );

		IBinding binding7 = new Binding( "edge2level23" );
		binding7.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level23\"]" ) );
		cqd.addBinding( binding7 );

		IBinding binding8 = new Binding( "measure1" );
		binding8.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		cqd.addBinding( binding8 );

		IBinding binding9 = new Binding( "total1" );
		binding9.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		binding9.addAggregateOn( "dimension[\"dimension1\"][\"level11\"]");
		binding9.addAggregateOn( "dimension[\"dimension1\"][\"level12\"]");
		binding9.addAggregateOn( "dimension[\"dimension1\"][\"level13\"]");
		binding9.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		cqd.addBinding( binding9 );
		
		IBinding binding10 = new Binding( "total2" );
		binding10.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		binding10.addAggregateOn( "dimension[\"dimension2\"][\"level21\"]");
		binding10.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		cqd.addBinding( binding10 );
		
		IBinding binding11 = new Binding( "grandTotal1" );
		binding11.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		binding11.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		cqd.addBinding( binding11 );
		
		IEdgeDrillFilter filter = columnEdge.createDrillFilter( "drill1" );
		filter.setTargetHierarchy( hier1 );
		filter.setTargetLevelName( "level14" );
		List memberList = new ArrayList( );
		memberList.add( new Object[]{
			"CAR"
		} );
		memberList.add( null );
		memberList.add( null );
		memberList.add( null );
		filter.setTuple( memberList );

		IEdgeDrillFilter filter2 = columnEdge.createDrillFilter( "drill2" );
		filter2.setTargetHierarchy( hier1 );
		filter2.setTargetLevelName( "level12" );
		memberList = new ArrayList( );
		memberList.add( new Object[]{
			"MOTOR"
		} );
		memberList.add( new Object[]{
			new Integer( 2005 )
		} );
		filter2.setTuple( memberList );

		IEdgeDrillFilter filter3 = columnEdge.createDrillFilter( "drill3" );
		filter3.setTargetHierarchy( hier1 );
		filter3.setTargetLevelName( "level11" );
		memberList = new ArrayList( );
		memberList.add( new Object[]{
			"BUS"
		} );
		filter3.setTuple( memberList );
		
		IEdgeDrillFilter filter4 = rowEdge.createDrillFilter( "drill4" );
		filter4.setTargetHierarchy( hier2 );
		filter4.setTargetLevelName( "level23" );
		memberList = new ArrayList( );
		memberList.add( new Object[]{
			"US"
		} );
		memberList.add( null );
		memberList.add( null );
		filter4.setTuple( memberList );
		
		IEdgeDrillFilter filter5 = rowEdge.createDrillFilter( "drill5" );
		filter5.setTargetHierarchy( hier2 );
		filter5.setTargetLevelName( "level22" );
		memberList = new ArrayList( );
		memberList.add( new Object[]{
			"CN"
		} );
		memberList.add( null );
		filter5.setTuple( memberList );

		DataEngineImpl engine = (DataEngineImpl) DataEngine.newDataEngine( createPresentationContext( ) );
		
		DrilledCube cube = new DrilledCube( );
		cube.createCube( engine );
		
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		columnEdgeBindingNames.add( "edge1level3" );
		columnEdgeBindingNames.add( "edge1level4" );
		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "edge2level21" );
		rowEdgeBindingNames.add( "edge2level22" );
		rowEdgeBindingNames.add( "edge2level23" );

		printCube( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				"measure1",
				"total1",
				"total2",
				"grandTotal1", null );

		engine.shutdown( );
	}
	
	public void testDrillOperationWithGrandTotal1( ) throws Exception
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		hier1.createLevel( "level11" );
		hier1.createLevel( "level12" );
		hier1.createLevel( "level13" );
				
		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		hier2.createLevel( "level21" );

		cqd.createMeasure( "measure1" );

		IBinding binding1 = new Binding( "edge1level1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "edge1level2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding3 = new Binding( "edge1level3" );

		binding3.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level13\"]" ) );
		cqd.addBinding( binding3 );
		
		IBinding binding4 = new Binding( "edge1level4" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level14\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "edge2level21" );

		binding5.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"]" ) );
		cqd.addBinding( binding5 );


		IBinding binding6 = new Binding( "edge2level22" );
		binding6.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level22\"]" ) );
		cqd.addBinding( binding6 );

		IBinding binding7 = new Binding( "edge2level23" );
		binding7.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level23\"]" ) );
		cqd.addBinding( binding7 );

		IBinding binding8 = new Binding( "measure1" );
		binding8.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		cqd.addBinding( binding8 );

		IBinding binding9 = new Binding( "total1" );
		binding9.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		binding9.addAggregateOn( "dimension[\"dimension1\"][\"level11\"]");
		binding9.addAggregateOn( "dimension[\"dimension1\"][\"level12\"]");
		binding9.addAggregateOn( "dimension[\"dimension1\"][\"level13\"]");
		binding9.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		cqd.addBinding( binding9 );
		
		IBinding binding10 = new Binding( "total2" );
		binding10.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		binding10.addAggregateOn( "dimension[\"dimension2\"][\"level21\"]");
		binding10.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		cqd.addBinding( binding10 );

		IBinding binding11 = new Binding( "subTotalOnLevel11AndLeve12" );
		binding11.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		binding11.addAggregateOn( "dimension[\"dimension2\"][\"level21\"]");
		binding11.addAggregateOn( "dimension[\"dimension1\"][\"level11\"]");
		binding11.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		cqd.addBinding( binding11 );

		IEdgeDrillFilter filter = columnEdge.createDrillFilter( "drill1" );
		filter.setTargetHierarchy( hier1 );
		filter.setTargetLevelName( "level14" );
		List memberList = new ArrayList( );
		memberList.add( new Object[]{
			"CAR"
		} );
		memberList.add( null );
		memberList.add( null );
		memberList.add( null );
		filter.setTuple( memberList );

		IEdgeDrillFilter filter2 = columnEdge.createDrillFilter( "drill2" );
		filter2.setTargetHierarchy( hier1 );
		filter2.setTargetLevelName( "level12" );
		memberList = new ArrayList( );
		memberList.add( new Object[]{
			"MOTOR"
		} );
		memberList.add( new Object[]{
			new Integer( 2005 )
		} );
		filter2.setTuple( memberList );

		IEdgeDrillFilter filter3 = columnEdge.createDrillFilter( "drill3" );
		filter3.setTargetHierarchy( hier1 );
		filter3.setTargetLevelName( "level11" );
		memberList = new ArrayList( );
		memberList.add( new Object[]{
			"BUS"
		} );
		filter3.setTuple( memberList );
		
		IEdgeDrillFilter filter4 = rowEdge.createDrillFilter( "drill4" );
		filter4.setTargetHierarchy( hier2 );
		filter4.setTargetLevelName( "level23" );
		memberList = new ArrayList( );
		memberList.add( new Object[]{
			"US"
		} );
		memberList.add( null );
		memberList.add( null );
		filter4.setTuple( memberList );
		
		IEdgeDrillFilter filter5 = rowEdge.createDrillFilter( "drill5" );
		filter5.setTargetHierarchy( hier2 );
		filter5.setTargetLevelName( "level22" );
		memberList = new ArrayList( );
		memberList.add( new Object[]{
			"CN"
		} );
		memberList.add( null );
		filter5.setTuple( memberList );

		DataEngineImpl engine = (DataEngineImpl) DataEngine.newDataEngine( createPresentationContext( ) );
		
		DrilledCube cube = new DrilledCube( );
		cube.createCube( engine );
		
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		columnEdgeBindingNames.add( "edge1level3" );
		columnEdgeBindingNames.add( "edge1level4" );
		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "edge2level21" );
		rowEdgeBindingNames.add( "edge2level22" );
		rowEdgeBindingNames.add( "edge2level23" );

		printCube( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				"measure1",
				null,
				null,
				null, "subTotalOnLevel11AndLeve12" );

		engine.shutdown( );
	}
	
	public void testEdgeStartAndEdgeEnd( ) throws Exception
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		hier1.createLevel( "level11" );
		hier1.createLevel( "level12" );
		hier1.createLevel( "level13" );

		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		hier2.createLevel( "level21" );
		hier2.createLevel( "level22" );
		hier2.createLevel( "level23" );

		cqd.createMeasure( "measure1" );

		IBinding binding1 = new Binding( "edge1level1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "edge1level2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding3 = new Binding( "edge1level3" );

		binding3.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level13\"]" ) );
		cqd.addBinding( binding3 );
		
		IBinding binding4 = new Binding( "edge1level4" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level14\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "edge2level21" );

		binding5.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"]" ) );
		cqd.addBinding( binding5 );

		IBinding binding6 = new Binding( "edge2level22" );

		binding6.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level22\"]" ) );
		cqd.addBinding( binding6 );
		
		IBinding binding7 = new Binding( "edge2level23" );

		binding7.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level23\"]" ) );
		cqd.addBinding( binding7 );
		
		IBinding binding8 = new Binding( "measure1" );
		binding8.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		cqd.addBinding( binding8 );

		IEdgeDrillFilter filter1 = columnEdge.createDrillFilter( "drill2" );
		filter1.setTargetHierarchy( hier1 );
		filter1.setTargetLevelName( "level11" );
		List memberList = new ArrayList( );
		memberList.add( new Object[]{
			"BICK"
		} );
		filter1.setTuple( memberList );
		
		IEdgeDrillFilter filter2 = columnEdge.createDrillFilter( "drill2" );
		filter2.setTargetHierarchy( hier1 );
		filter2.setTargetLevelName( "level14" );
		memberList = new ArrayList( );
		memberList.add( new Object[]{
			"BUS"
		} );
		memberList.add( null );
		memberList.add( null );
		memberList.add( null );
		filter2.setTuple( memberList );
		
		DataEngineImpl engine = (DataEngineImpl) DataEngine.newDataEngine( createPresentationContext( ) );
		DrilledCube cube = new DrilledCube();
		cube.createCube( engine );
		
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		columnEdgeBindingNames.add( "edge1level3" );
		columnEdgeBindingNames.add( "edge1level4" );
		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "edge2level21" );
		rowEdgeBindingNames.add( "edge2level22" );
		rowEdgeBindingNames.add( "edge2level23" );
	
		List ordinateEdge = cursor.getOrdinateEdge( );
		EdgeCursor columnEdgeCursor = (EdgeCursor)ordinateEdge.get( 0 );
		String output = "";
		columnEdgeCursor.beforeFirst( );
		while ( columnEdgeCursor.next( ) )
		{
			List dimensionCursors = columnEdgeCursor.getDimensionCursor( );
			output += "Edge Position :"
					+ columnEdgeCursor.getPosition( ) + "\n";
			for ( int i = 0; i < dimensionCursors.size( ); i++ )
			{
				DimensionCursor dim = (DimensionCursor) dimensionCursors.get( i );
				output += "       Dimension "
						+ i + " Start Position :"
						+ dim.getEdgeStart( ) + "\n";
				output += "       Dimension "
						+ i + " Edge  Position :"
						+ dim.getEdgeEnd( ) + "\n";
			}
		}

		this.testPrint( output );
		this.checkOutputFile( );
		close( cursor );

		engine.shutdown( );	
	}
	
	
	private DataEngineContext createPresentationContext( ) throws BirtException
	{
		DataEngineContext context =  DataEngineContext.newInstance( DataEngineContext.DIRECT_PRESENTATION,
				null,
				null,
				null );
		context.setTmpdir( this.getTempDir( ) );
		return context;
	}
	
	private void printCube( CubeCursor cursor, List columnEdgeBindingNames,
			List rowEdgeBindingNames, String measureBindingNames,
			String columnAggr, String rowAggr, String overallAggr, String subTotal )
			throws Exception
	{
		String output = getOutputFromCursor( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				measureBindingNames,
				columnAggr,
				rowAggr,
				overallAggr, subTotal );
		this.testPrint( output );
		this.checkOutputFile( );
		close( cursor );
	}
	
	private void close( CubeCursor dataCursor ) throws OLAPException
	{
		for ( int i = 0; i < dataCursor.getOrdinateEdge( ).size( ); i++ )
		{
			EdgeCursor edge = (EdgeCursor) ( dataCursor.getOrdinateEdge( ).get( i ) );
			edge.close( );
		}
		dataCursor.close( );
	}

	
	private String getOutputFromCursor( CubeCursor cursor,
			List columnEdgeBindingNames, List rowEdgeBindingNames,
			String measureBindingNames, String columnAggr, String rowAggr,
			String overallAggr, String subTotal ) throws OLAPException
	{
		EdgeCursor edge1 = (EdgeCursor) ( cursor.getOrdinateEdge( ).get( 0 ) );
		EdgeCursor edge2 = (EdgeCursor) ( cursor.getOrdinateEdge( ).get( 1 ) );
		DimensionCursor columnCursor1 = (DimensionCursor) edge1.getDimensionCursor( )
				.get( 0 );

		String[] lines = new String[columnEdgeBindingNames.size( )];
		for ( int i = 0; i < columnEdgeBindingNames.size( ); i++ )
		{
			lines[i] = "		                        ";
		}

		while ( edge1.next( ) )
		{
			for ( int i = 0; i < columnEdgeBindingNames.size( ); i++ )
			{
				lines[i] += cursor.getObject( columnEdgeBindingNames.get( i )
						.toString( ) )
						+ "		";
			}
		}

		if ( rowAggr != null )
			lines[lines.length - 1] += "Total";

		String output = "";
		for ( int i = 0; i < lines.length; i++ )
		{
			output += "\n" + lines[i];
		}

		while ( edge2.next( ) )
		{
			String line = "";
			for ( int i = 0; i < rowEdgeBindingNames.size( ); i++ )
			{
				line += cursor.getObject( rowEdgeBindingNames.get( i )
						.toString( ) )
						+ "		";
			}
			edge1.beforeFirst( );
			while ( edge1.next( ) )
			{
				if ( subTotal == null )
				{
					line += cursor.getObject( measureBindingNames ) + "		";
				}
				else
				{
					if( edge1.getPosition( ) == columnCursor1.getEdgeEnd( ) )
					{
						line += cursor.getObject( measureBindingNames );
						line += "|" + cursor.getObject( subTotal ) + "  ";						
					}
					else
					{
						line += cursor.getObject( measureBindingNames ) + "		";						
					}
				}
			}

			if ( rowAggr != null )
				line += cursor.getObject( rowAggr );
			output += "\n" + line;
		}

		if ( columnAggr != null )
		{
			String line = "                     " + "Total" + "		";
			edge1.beforeFirst( );
			while ( edge1.next( ) )
			{
				line += cursor.getObject( columnAggr ) + "		";
			}
			if ( overallAggr != null )
				line += cursor.getObject( overallAggr );

			output += "\n" + line;
		}
		
		return output;
	}
	
}

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
import org.eclipse.birt.data.engine.olap.api.query.IMeasureDefinition;
import org.eclipse.birt.data.engine.olap.impl.query.CubeQueryDefinition;

import testutil.BaseTestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;

public class CubeDrillFeatureTest extends BaseTestCase
{

	private final static String cubeName = "DrilledCube";

	/**
	 * Test basic drill up operation
	 * Drill on the first dimension
	 * @throws Exception
	 */
	@Test
    public void testBasicDrillUpOperation( ) throws Exception
	{

		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = rowEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		hier1.createLevel( "COUNTRY" );
		hier1.createLevel( "STATE" );
		hier1.createLevel( "CITY" );

		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		hier2.createLevel( "YEAR" );
		hier2.createLevel( "QUARTER" );
		hier2.createLevel( "MONTH" );

		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IDimensionDefinition dim3 = columnEdge.createDimension( "dimension3" );
		IHierarchyDefinition hier3 = dim3.createHierarchy( "dimension3" );
		hier3.createLevel( "PRODUCTLINE" );
		
		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		IBinding binding1 = new Binding( "binding1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"COUNTRY\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "binding2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"STATE\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding3 = new Binding( "binding3" );

		binding3.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"CITY\"]" ) );
		cqd.addBinding( binding3 );
		
		IBinding binding4 = new Binding( "binding4" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"YEAR\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "binding5" );

		binding5.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"QUARTER\"]" ) );
		cqd.addBinding( binding5 );


		IBinding binding6 = new Binding( "binding6" );
		binding6.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"MONTH\"]" ) );
		cqd.addBinding( binding6 );

		IBinding binding7 = new Binding( "binding7" );
		binding7.setExpression( new ScriptExpression( "dimension[\"dimension3\"][\"PRODUCTLINE\"]" ) );
		cqd.addBinding( binding7 );

		IBinding binding8 = new Binding( "measure1" );
		binding8.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		cqd.addBinding( binding8 );

		IEdgeDrillFilter filter = rowEdge.createDrillFilter( "drill1" );
		filter.setTargetHierarchy( hier1 );
		filter.setTargetLevelName( "COUNTRY" );
		List memberList = new ArrayList( );
		memberList.add( new Object[]{
			"CHINA"
		} );
		memberList.add( null );
		memberList.add( null );
		filter.setTuple( memberList );

		IEdgeDrillFilter filter2 = rowEdge.createDrillFilter( "drill2" );
		filter2.setTargetHierarchy( hier1 );
		filter2.setTargetLevelName( "STATE" );
		memberList = new ArrayList( );
		memberList.add( new Object[]{
			"USA"
		} );
		memberList.add( new Object[]{
				"STATE5", "STATE7", "STATE6"
		} );
		filter2.setTuple( memberList );

		DataEngineImpl engine = (DataEngineImpl) DataEngine.newDataEngine( createPresentationContext( ) );
		
		DrilledCube cube = new DrilledCube( );
		cube.createCube( engine );
		
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "binding1" );
		rowEdgeBindingNames.add( "binding2" );
		rowEdgeBindingNames.add( "binding3" );
		rowEdgeBindingNames.add( "binding4" );
		rowEdgeBindingNames.add( "binding5" );
		rowEdgeBindingNames.add( "binding6" );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "binding7" );

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
	 * Drill on the first dimension with duplicated second dimension
	 * @throws Exception
	 */
	@Test
    public void testBasicDrillOperation2( ) throws Exception
	{

		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = rowEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		hier1.createLevel( "COUNTRY" );
		hier1.createLevel( "STATE" );

		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		hier2.createLevel( "YEAR" );
		hier2.createLevel( "QUARTER" );

		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IDimensionDefinition dim3 = columnEdge.createDimension( "dimension3" );
		IHierarchyDefinition hier3 = dim3.createHierarchy( "dimension3" );
		hier3.createLevel( "PRODUCTLINE" );
		
		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		IBinding binding1 = new Binding( "binding1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"COUNTRY\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "binding2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"STATE\"]" ) );
		cqd.addBinding( binding2 );
		
		IBinding binding4 = new Binding( "binding4" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"YEAR\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "binding5" );

		binding5.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"QUARTER\"]" ) );
		cqd.addBinding( binding5 );

		IBinding binding7 = new Binding( "binding7" );
		binding7.setExpression( new ScriptExpression( "dimension[\"dimension3\"][\"PRODUCTLINE\"]" ) );
		cqd.addBinding( binding7 );

		IBinding binding8 = new Binding( "measure1" );
		binding8.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		cqd.addBinding( binding8 );

		IEdgeDrillFilter filter = rowEdge.createDrillFilter( "drill1" );
		filter.setTargetHierarchy( hier1 );
		filter.setTargetLevelName( "COUNTRY" );
		List memberList = new ArrayList( );
		memberList.add( new Object[]{
				"USA", "CHINA", "FRANCE"
		} );
		filter.setTuple( memberList );
		DataEngineImpl engine = (DataEngineImpl) DataEngine.newDataEngine( createPresentationContext( ) );
		
		DrilledCube cube = new DrilledCube( );
		cube.createCube( engine );
		
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "binding1" );
		rowEdgeBindingNames.add( "binding2" );
		rowEdgeBindingNames.add( "binding4" );
		rowEdgeBindingNames.add( "binding5" );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "binding7" );

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
	 * Drill on the second dimension
	 * @throws Exception
	 */
	@Test
    public void testBasicDrillUpOperation3( ) throws Exception
	{

		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = rowEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		hier1.createLevel( "COUNTRY" );
		hier1.createLevel( "STATE" );
		hier1.createLevel( "CITY" );

		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		hier2.createLevel( "YEAR" );
		hier2.createLevel( "QUARTER" );
		hier2.createLevel( "MONTH" );

		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IDimensionDefinition dim3 = columnEdge.createDimension( "dimension3" );
		IHierarchyDefinition hier3 = dim3.createHierarchy( "dimension3" );
		hier3.createLevel( "PRODUCTLINE" );
		
		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		IBinding binding1 = new Binding( "binding1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"COUNTRY\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "binding2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"STATE\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding3 = new Binding( "binding3" );

		binding3.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"CITY\"]" ) );
		cqd.addBinding( binding3 );
		
		IBinding binding4 = new Binding( "binding4" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"YEAR\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "binding5" );

		binding5.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"QUARTER\"]" ) );
		cqd.addBinding( binding5 );

		IBinding binding6 = new Binding( "binding6" );

		binding6.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"MONTH\"]" ) );
		cqd.addBinding( binding6 );

		IBinding binding7 = new Binding( "binding7" );
		binding7.setExpression( new ScriptExpression( "dimension[\"dimension3\"][\"PRODUCTLINE\"]" ) );
		cqd.addBinding( binding7 );

		IBinding binding8 = new Binding( "measure1" );
		binding8.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		cqd.addBinding( binding8 );

		IEdgeDrillFilter filter = rowEdge.createDrillFilter( "drill1" );
		filter.setTargetHierarchy( hier2 );
		filter.setTargetLevelName( "YEAR" );
		List memberList = new ArrayList( );
		memberList.add( new Object[]{
			"2003"
		} );
		memberList.add( null );
		memberList.add( null );
		filter.setTuple( memberList );
		DataEngineImpl engine = (DataEngineImpl) DataEngine.newDataEngine( createPresentationContext( ) );
		
		DrilledCube cube = new DrilledCube( );
		cube.createCube( engine );
		
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "binding1" );
		rowEdgeBindingNames.add( "binding2" );
		rowEdgeBindingNames.add( "binding3" );
		rowEdgeBindingNames.add( "binding4" );
		rowEdgeBindingNames.add( "binding5" );
		rowEdgeBindingNames.add( "binding6" );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "binding7" );

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
	 * Drill on the first and second dimension
	 * @throws Exception
	 */
	@Test
    public void testBasicDrillUpOperation4( ) throws Exception
	{

		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = rowEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		hier1.createLevel( "COUNTRY" );
		hier1.createLevel( "STATE" );
		hier1.createLevel( "CITY" );

		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		hier2.createLevel( "YEAR" );
		hier2.createLevel( "QUARTER" );
		hier2.createLevel( "MONTH" );

		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IDimensionDefinition dim3 = columnEdge.createDimension( "dimension3" );
		IHierarchyDefinition hier3 = dim3.createHierarchy( "dimension3" );
		hier3.createLevel( "PRODUCTLINE" );
		
		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		IBinding binding1 = new Binding( "binding1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"COUNTRY\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "binding2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"STATE\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding3 = new Binding( "binding3" );

		binding3.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"CITY\"]" ) );
		cqd.addBinding( binding3 );
		
		IBinding binding4 = new Binding( "binding4" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"YEAR\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "binding5" );

		binding5.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"QUARTER\"]" ) );
		cqd.addBinding( binding5 );

		IBinding binding6 = new Binding( "binding6" );

		binding6.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"MONTH\"]" ) );
		cqd.addBinding( binding6 );

		IBinding binding7 = new Binding( "binding7" );
		binding7.setExpression( new ScriptExpression( "dimension[\"dimension3\"][\"PRODUCTLINE\"]" ) );
		cqd.addBinding( binding7 );

		IBinding binding8 = new Binding( "measure1" );
		binding8.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		cqd.addBinding( binding8 );
		
		IEdgeDrillFilter filter1 = rowEdge.createDrillFilter( "drill2" );
		filter1.setTargetHierarchy( hier1 );
		filter1.setTargetLevelName( "COUNTRY" );
		List memberList = new ArrayList( );
		memberList.add( new Object[]{
			"USA","CHINA"
		} );
		filter1.setTuple( memberList );
		
		IEdgeDrillFilter filter = rowEdge.createDrillFilter( "drill1" );
		filter.setTargetHierarchy( hier2 );
		filter.setTargetLevelName( "YEAR" );
		memberList = new ArrayList( );
		memberList.add( new Object[]{
			"2003"
		} );
		memberList.add( null );
		memberList.add( null );
		filter.setTuple( memberList );
		
		DataEngineImpl engine = (DataEngineImpl) DataEngine.newDataEngine( createPresentationContext( ) );
		
		DrilledCube cube = new DrilledCube( );
		cube.createCube( engine );
		
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "binding1" );
		rowEdgeBindingNames.add( "binding2" );
		rowEdgeBindingNames.add( "binding3" );
		rowEdgeBindingNames.add( "binding4" );
		rowEdgeBindingNames.add( "binding5" );
		rowEdgeBindingNames.add( "binding6" );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "binding7" );

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
	 * Drill on the first, second, third dimension
	 * @throws Exception
	 */
	@Test
    public void testBasicDrillOperation5( ) throws Exception
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = rowEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		hier1.createLevel( "COUNTRY" );
		hier1.createLevel( "STATE" );
		hier1.createLevel( "CITY" );

		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		hier2.createLevel( "YEAR" );
		hier2.createLevel( "QUARTER" );
		hier2.createLevel( "MONTH" );

		IDimensionDefinition dim3 = rowEdge.createDimension( "dimension3" );
		IHierarchyDefinition hier3 = dim3.createHierarchy( "dimension3" );
		hier3.createLevel( "PRODUCTLINE" );
		hier3.createLevel( "PRODUCTTYPE" );

		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		IBinding binding1 = new Binding( "binding1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"COUNTRY\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "binding2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"STATE\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding3 = new Binding( "binding3" );

		binding3.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"CITY\"]" ) );
		cqd.addBinding( binding3 );
		
		IBinding binding4 = new Binding( "binding4" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"YEAR\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "binding5" );

		binding5.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"QUARTER\"]" ) );
		cqd.addBinding( binding5 );

		IBinding binding6 = new Binding( "binding6" );

		binding6.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"MONTH\"]" ) );
		cqd.addBinding( binding6 );

		IBinding binding7 = new Binding( "binding7" );
		binding7.setExpression( new ScriptExpression( "dimension[\"dimension3\"][\"PRODUCTLINE\"]" ) );
		cqd.addBinding( binding7 );
		
		IBinding binding9 = new Binding( "binding8" );
		binding9.setExpression( new ScriptExpression( "dimension[\"dimension3\"][\"PRODUCTTYPE\"]" ) );
		cqd.addBinding( binding9 );

		IBinding binding8 = new Binding( "measure1" );
		binding8.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		cqd.addBinding( binding8 );
		
		IEdgeDrillFilter filter1 = rowEdge.createDrillFilter( "drill2" );
		filter1.setTargetHierarchy( hier1 );
		filter1.setTargetLevelName( "COUNTRY" );
		List memberList = new ArrayList( );
		memberList.add( new Object[]{
			"USA","CHINA"
		} );
		filter1.setTuple( memberList );
		
		IEdgeDrillFilter filter = rowEdge.createDrillFilter( "drill1" );
		filter.setTargetHierarchy( hier2 );
		filter.setTargetLevelName( "YEAR" );
		memberList = new ArrayList( );
		memberList.add( new Object[]{
			"2003"
		} );
		memberList.add( null );
		memberList.add( null );
		filter.setTuple( memberList );
		
		IEdgeDrillFilter filter2 = rowEdge.createDrillFilter( "drill13" );
		filter2.setTargetHierarchy( hier3 );
		filter2.setTargetLevelName( "PRODUCTLINE" );
		memberList = new ArrayList( );
		memberList.add( new Object[]{
			"CAR", "PLANE"
		} );
		memberList.add( null );
		memberList.add( null );
		filter2.setTuple( memberList );
		
		DataEngineImpl engine = (DataEngineImpl) DataEngine.newDataEngine( createPresentationContext( ) );
		
		DrilledCube cube = new DrilledCube( );
		cube.createCube( engine );
		
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "binding1" );
		rowEdgeBindingNames.add( "binding2" );
		rowEdgeBindingNames.add( "binding3" );
		rowEdgeBindingNames.add( "binding4" );
		rowEdgeBindingNames.add( "binding5" );
		rowEdgeBindingNames.add( "binding6" );
		rowEdgeBindingNames.add( "binding7" );
		rowEdgeBindingNames.add( "binding8" );
		List columnEdgeBindingNames = new ArrayList( );

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
	 * Drill on the first, second and the sub total.
	 * @throws Exception
	 */
	@Test
    public void testBasicDrillOperation6( ) throws Exception
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = rowEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		hier1.createLevel( "COUNTRY" );
		hier1.createLevel( "STATE" );
		hier1.createLevel( "CITY" );

		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		hier2.createLevel( "YEAR" );
		hier2.createLevel( "QUARTER" );
		hier2.createLevel( "MONTH" );

		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IDimensionDefinition dim3 = columnEdge.createDimension( "dimension3" );
		IHierarchyDefinition hier3 = dim3.createHierarchy( "dimension3" );
		hier3.createLevel( "PRODUCTLINE" );

		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		IBinding binding1 = new Binding( "binding1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"COUNTRY\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "binding2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"STATE\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding3 = new Binding( "binding3" );

		binding3.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"CITY\"]" ) );
		cqd.addBinding( binding3 );
		
		IBinding binding4 = new Binding( "binding4" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"YEAR\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "binding5" );

		binding5.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"QUARTER\"]" ) );
		cqd.addBinding( binding5 );

		IBinding binding6 = new Binding( "binding6" );

		binding6.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"MONTH\"]" ) );
		cqd.addBinding( binding6 );

		IBinding binding7 = new Binding( "binding7" );
		binding7.setExpression( new ScriptExpression( "dimension[\"dimension3\"][\"PRODUCTLINE\"]" ) );
		cqd.addBinding( binding7 );

		IBinding binding8 = new Binding( "measure1" );
		binding8.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		cqd.addBinding( binding8 );
		
		IBinding binding9 = new Binding( "total1" );
		binding9.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		binding9.addAggregateOn( "dimension[\"dimension1\"][\"COUNTRY\"]");
		binding9.addAggregateOn( "dimension[\"dimension1\"][\"STATE\"]");
		binding9.addAggregateOn( "dimension[\"dimension1\"][\"CITY\"]");
		binding9.addAggregateOn( "dimension[\"dimension2\"][\"YEAR\"]");
		binding9.addAggregateOn( "dimension[\"dimension2\"][\"QUARTER\"]");
		binding9.addAggregateOn( "dimension[\"dimension2\"][\"MONTH\"]");
		binding9.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		cqd.addBinding( binding9 );
		
		IBinding binding10 = new Binding( "total2" );
		binding10.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		binding10.addAggregateOn( "dimension[\"dimension3\"][\"PRODUCTLINE\"]");
		binding10.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		cqd.addBinding( binding10 );
		
		IBinding binding11 = new Binding( "grandTotal1" );
		binding11.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		binding11.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		cqd.addBinding( binding11 );
		
		IEdgeDrillFilter filter1 = rowEdge.createDrillFilter( "drill2" );
		filter1.setTargetHierarchy( hier1 );
		filter1.setTargetLevelName( "COUNTRY" );
		List memberList = new ArrayList( );
		memberList.add( new Object[]{
			"USA","CHINA"
		} );
		filter1.setTuple( memberList );
		
		IEdgeDrillFilter filter = rowEdge.createDrillFilter( "drill1" );
		filter.setTargetHierarchy( hier2 );
		filter.setTargetLevelName( "YEAR" );
		memberList = new ArrayList( );
		memberList.add( new Object[]{
			"2003"
		} );
		memberList.add( null );
		memberList.add( null );
		filter.setTuple( memberList );
				
		DataEngineImpl engine = (DataEngineImpl) DataEngine.newDataEngine( createPresentationContext( ) );
		
		DrilledCube cube = new DrilledCube( );
		cube.createCube( engine );
		
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "binding1" );
		rowEdgeBindingNames.add( "binding2" );
		rowEdgeBindingNames.add( "binding3" );
		rowEdgeBindingNames.add( "binding4" );
		rowEdgeBindingNames.add( "binding5" );
		rowEdgeBindingNames.add( "binding6" );

		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "binding7" );

		printCube( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				"measure1",
				"total2",
				"total1",
				"grandTotal1",
				null );

		engine.shutdown( );
	}
	
	/**
	 * Drill coexists nested aggregation with running aggregation
	 * @throws Exception
	 */
	@Test
    public void testBasicDrillOperation7( ) throws Exception
	{

		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = rowEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		hier1.createLevel( "COUNTRY" );
		hier1.createLevel( "STATE" );
		hier1.createLevel( "CITY" );

		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		hier2.createLevel( "YEAR" );
		hier2.createLevel( "QUARTER" );
		hier2.createLevel( "MONTH" );

		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IDimensionDefinition dim3 = columnEdge.createDimension( "dimension3" );
		IHierarchyDefinition hier3 = dim3.createHierarchy( "dimension3" );
		hier3.createLevel( "PRODUCTLINE" );
		
		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		IBinding binding1 = new Binding( "binding1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"COUNTRY\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "binding2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"STATE\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding3 = new Binding( "binding3" );

		binding3.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"CITY\"]" ) );
		cqd.addBinding( binding3 );
		
		IBinding binding4 = new Binding( "binding4" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"YEAR\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "binding5" );

		binding5.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"QUARTER\"]" ) );
		cqd.addBinding( binding5 );


		IBinding binding6 = new Binding( "binding6" );
		binding6.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"MONTH\"]" ) );
		cqd.addBinding( binding6 );

		IBinding binding7 = new Binding( "binding7" );
		binding7.setExpression( new ScriptExpression( "dimension[\"dimension3\"][\"PRODUCTLINE\"]" ) );
		cqd.addBinding( binding7 );

		IBinding binding8 = new Binding( "measure1" );
		binding8.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		binding8.addAggregateOn( "dimension[\"dimension1\"][\"COUNTRY\"]" );
		binding8.addAggregateOn( "dimension[\"dimension1\"][\"STATE\"]" );
		binding8.addAggregateOn( "dimension[\"dimension1\"][\"CITY\"]" );
		binding8.addAggregateOn( "dimension[\"dimension2\"][\"YEAR\"]" );
		binding8.addAggregateOn( "dimension[\"dimension2\"][\"QUARTER\"]" );
		binding8.addAggregateOn( "dimension[\"dimension2\"][\"MONTH\"]" );
		binding8.addAggregateOn( "dimension[\"dimension3\"][\"PRODUCTLINE\"]" );
		binding8.setAggrFunction( "SUM" );
		cqd.addBinding( binding8 );
		
		IBinding binding9 = new Binding( "rank" );
		binding9.setExpression( new ScriptExpression( "data[\"subTotal\"]" ) );
		binding9.setAggrFunction( "RANK" );
		binding9.addAggregateOn( "dimension[\"dimension1\"][\"COUNTRY\"]" );	
		cqd.addBinding( binding9 );
		
		IBinding binding11 = new Binding( "subTotal" );
		binding11.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		binding11.setAggrFunction( "SUM" );
		binding11.addAggregateOn( "dimension[\"dimension1\"][\"COUNTRY\"]" );
		binding11.addAggregateOn( "dimension[\"dimension1\"][\"STATE\"]" );
		binding11.addAggregateOn( "dimension[\"dimension1\"][\"CITY\"]" );
		binding11.addAggregateOn( "dimension[\"dimension2\"][\"YEAR\"]" );
		binding11.addAggregateOn( "dimension[\"dimension2\"][\"QUARTER\"]" );
		binding11.addAggregateOn( "dimension[\"dimension2\"][\"MONTH\"]" );
		cqd.addBinding( binding11 );

		IEdgeDrillFilter filter = rowEdge.createDrillFilter( "drill1" );
		filter.setTargetHierarchy( hier1 );
		filter.setTargetLevelName( "COUNTRY" );
		List memberList = new ArrayList( );
		memberList.add( new Object[]{
			"CHINA"
		} );
		memberList.add( null );
		memberList.add( null );
		filter.setTuple( memberList );

		IEdgeDrillFilter filter2 = rowEdge.createDrillFilter( "drill2" );
		filter2.setTargetHierarchy( hier1 );
		filter2.setTargetLevelName( "STATE" );
		memberList = new ArrayList( );
		memberList.add( new Object[]{
			"USA"
		} );
		memberList.add( new Object[]{
				"STATE5", "STATE7", "STATE6"
		} );
		filter2.setTuple( memberList );

		DataEngineImpl engine = (DataEngineImpl) DataEngine.newDataEngine( createPresentationContext( ) );
		
		DrilledCube cube = new DrilledCube( );
		cube.createCube( engine );
		
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "binding1" );
		rowEdgeBindingNames.add( "binding2" );
		rowEdgeBindingNames.add( "binding3" );
		rowEdgeBindingNames.add( "binding4" );
		rowEdgeBindingNames.add( "binding5" );
		rowEdgeBindingNames.add( "binding6" );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "binding7" );

		printCube( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				"measure1",
				null,
				"rank",
				null, null );
		
		engine.shutdown( );
	}
	
	/**
	 * Drill coexists nested aggregation with normal aggregation
	 * @throws Exception
	 */
	@Test
    public void testBasicDrillOperation8( ) throws Exception
	{

		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = rowEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		hier1.createLevel( "COUNTRY" );
		hier1.createLevel( "STATE" );
		hier1.createLevel( "CITY" );

		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		hier2.createLevel( "YEAR" );
		hier2.createLevel( "QUARTER" );
		hier2.createLevel( "MONTH" );

		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IDimensionDefinition dim3 = columnEdge.createDimension( "dimension3" );
		IHierarchyDefinition hier3 = dim3.createHierarchy( "dimension3" );
		hier3.createLevel( "PRODUCTLINE" );
		
		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		IBinding binding1 = new Binding( "binding1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"COUNTRY\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "binding2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"STATE\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding3 = new Binding( "binding3" );

		binding3.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"CITY\"]" ) );
		cqd.addBinding( binding3 );
		
		IBinding binding4 = new Binding( "binding4" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"YEAR\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "binding5" );

		binding5.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"QUARTER\"]" ) );
		cqd.addBinding( binding5 );


		IBinding binding6 = new Binding( "binding6" );
		binding6.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"MONTH\"]" ) );
		cqd.addBinding( binding6 );

		IBinding binding7 = new Binding( "binding7" );
		binding7.setExpression( new ScriptExpression( "dimension[\"dimension3\"][\"PRODUCTLINE\"]" ) );
		cqd.addBinding( binding7 );

		IBinding binding8 = new Binding( "measure1" );
		binding8.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		binding8.addAggregateOn( "dimension[\"dimension1\"][\"COUNTRY\"]" );
		binding8.addAggregateOn( "dimension[\"dimension1\"][\"STATE\"]" );
		binding8.addAggregateOn( "dimension[\"dimension1\"][\"CITY\"]" );
		binding8.addAggregateOn( "dimension[\"dimension2\"][\"YEAR\"]" );
		binding8.addAggregateOn( "dimension[\"dimension2\"][\"QUARTER\"]" );
		binding8.addAggregateOn( "dimension[\"dimension2\"][\"MONTH\"]" );
		binding8.addAggregateOn( "dimension[\"dimension3\"][\"PRODUCTLINE\"]" );
		binding8.setAggrFunction( "SUM" );
		cqd.addBinding( binding8 );
				
		IBinding binding11 = new Binding( "subTotal" );
		binding11.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		binding11.setAggrFunction( "SUM" );
		binding11.addAggregateOn( "dimension[\"dimension1\"][\"COUNTRY\"]" );
		binding11.addAggregateOn( "dimension[\"dimension1\"][\"STATE\"]" );
		binding11.addAggregateOn( "dimension[\"dimension1\"][\"CITY\"]" );
		binding11.addAggregateOn( "dimension[\"dimension2\"][\"YEAR\"]" );
		binding11.addAggregateOn( "dimension[\"dimension2\"][\"QUARTER\"]" );
		binding11.addAggregateOn( "dimension[\"dimension2\"][\"MONTH\"]" );
		cqd.addBinding( binding11 );
		
		IBinding binding10 = new Binding( "sum" );
		binding10.setExpression( new ScriptExpression( "data[\"measure1\"]" ) );
		binding10.setAggrFunction( "SUM" );
		binding10.addAggregateOn( "dimension[\"dimension1\"][\"COUNTRY\"]" );
		binding10.addAggregateOn( "dimension[\"dimension1\"][\"STATE\"]" );
		binding10.addAggregateOn( "dimension[\"dimension1\"][\"CITY\"]" );
		binding10.addAggregateOn( "dimension[\"dimension2\"][\"YEAR\"]" );
		binding10.addAggregateOn( "dimension[\"dimension2\"][\"QUARTER\"]" );
		cqd.addBinding( binding10 );
		
		IEdgeDrillFilter filter = rowEdge.createDrillFilter( "drill1" );
		filter.setTargetHierarchy( hier1 );
		filter.setTargetLevelName( "COUNTRY" );
		List memberList = new ArrayList( );
		memberList.add( new Object[]{
			"CHINA"
		} );
		memberList.add( null );
		memberList.add( null );
		filter.setTuple( memberList );

		IEdgeDrillFilter filter2 = rowEdge.createDrillFilter( "drill2" );
		filter2.setTargetHierarchy( hier1 );
		filter2.setTargetLevelName( "STATE" );
		memberList = new ArrayList( );
		memberList.add( new Object[]{
			"USA"
		} );
		memberList.add( new Object[]{
				"STATE5", "STATE7", "STATE6"
		} );
		filter2.setTuple( memberList );

		DataEngineImpl engine = (DataEngineImpl) DataEngine.newDataEngine( createPresentationContext( ) );
		
		DrilledCube cube = new DrilledCube( );
		cube.createCube( engine );
		
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "binding1" );
		rowEdgeBindingNames.add( "binding2" );
		rowEdgeBindingNames.add( "binding3" );
		rowEdgeBindingNames.add( "binding4" );
		rowEdgeBindingNames.add( "binding5" );
		rowEdgeBindingNames.add( "binding6" );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "binding7" );

		printCube( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				"measure1",
				null,
				"sum",
				null, null );
		
		engine.shutdown( );
	}
	
	/**
	 * Test the getExtend from dimension cursor.
	 * @throws Exception
	 */
	@Test
    public void testDimensionExtend( ) throws Exception
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = rowEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		hier1.createLevel( "COUNTRY" );
		hier1.createLevel( "STATE" );
		hier1.createLevel( "CITY" );

		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		hier2.createLevel( "YEAR" );
		hier2.createLevel( "QUARTER" );
		hier2.createLevel( "MONTH" );

		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IDimensionDefinition dim3 = columnEdge.createDimension( "dimension3" );
		IHierarchyDefinition hier3 = dim3.createHierarchy( "dimension3" );
		hier3.createLevel( "PRODUCTLINE" );

		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		IBinding binding1 = new Binding( "binding1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"COUNTRY\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "binding2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"STATE\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding3 = new Binding( "binding3" );

		binding3.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"CITY\"]" ) );
		cqd.addBinding( binding3 );
		
		IBinding binding4 = new Binding( "binding4" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"YEAR\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "binding5" );

		binding5.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"QUARTER\"]" ) );
		cqd.addBinding( binding5 );

		IBinding binding6 = new Binding( "binding6" );

		binding6.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"MONTH\"]" ) );
		cqd.addBinding( binding6 );

		IBinding binding7 = new Binding( "binding7" );
		binding7.setExpression( new ScriptExpression( "dimension[\"dimension3\"][\"PRODUCTLINE\"]" ) );
		cqd.addBinding( binding7 );

		IBinding binding8 = new Binding( "measure1" );
		binding8.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		cqd.addBinding( binding8 );
		
		IBinding binding9 = new Binding( "total1" );
		binding9.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		binding9.addAggregateOn( "dimension[\"dimension1\"][\"COUNTRY\"]");
		binding9.addAggregateOn( "dimension[\"dimension1\"][\"STATE\"]");
		binding9.addAggregateOn( "dimension[\"dimension1\"][\"CITY\"]");
		binding9.addAggregateOn( "dimension[\"dimension2\"][\"YEAR\"]");
		binding9.addAggregateOn( "dimension[\"dimension2\"][\"QUARTER\"]");
		binding9.addAggregateOn( "dimension[\"dimension2\"][\"MONTH\"]");
		binding9.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		cqd.addBinding( binding9 );
		
		IBinding binding10 = new Binding( "total2" );
		binding10.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		binding10.addAggregateOn( "dimension[\"dimension3\"][\"PRODUCTLINE\"]");
		binding10.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		cqd.addBinding( binding10 );
		
		IBinding binding11 = new Binding( "grandTotal1" );
		binding11.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		binding11.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		cqd.addBinding( binding11 );
		
		IEdgeDrillFilter filter1 = rowEdge.createDrillFilter( "drill2" );
		filter1.setTargetHierarchy( hier1 );
		filter1.setTargetLevelName( "COUNTRY" );
		List memberList = new ArrayList( );
		memberList.add( new Object[]{
			"USA","CHINA"
		} );
		filter1.setTuple( memberList );
		
		IEdgeDrillFilter filter = rowEdge.createDrillFilter( "drill1" );
		filter.setTargetHierarchy( hier2 );
		filter.setTargetLevelName( "YEAR" );
		memberList = new ArrayList( );
		memberList.add( new Object[]{
			"2003"
		} );
		memberList.add( null );
		memberList.add( null );
		filter.setTuple( memberList );
				
		DataEngineImpl engine = (DataEngineImpl) DataEngine.newDataEngine( createPresentationContext( ) );
		
		DrilledCube cube = new DrilledCube( );
		cube.createCube( engine );
		
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "binding1" );
		rowEdgeBindingNames.add( "binding2" );
		rowEdgeBindingNames.add( "binding3" );
		rowEdgeBindingNames.add( "binding4" );
		rowEdgeBindingNames.add( "binding5" );
		rowEdgeBindingNames.add( "binding6" );

		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "binding7" );
		
		List edgeCursors = cursor.getOrdinateEdge( );
		StringBuffer strBuffer = new StringBuffer( );
		for ( int i = 0; i < edgeCursors.size( ); i++ )
		{
			EdgeCursor eCursor = (EdgeCursor) edgeCursors.get( i );
			strBuffer.append( "Edge Cursor " + i + "\n" );
			List dCursor = eCursor.getDimensionCursor( );
			while ( eCursor.next( ) )
			{
				for ( int k = 0; k < dCursor.size( ); k++ )
				{
					DimensionCursor dimCursor = (DimensionCursor) dCursor.get( k );
					strBuffer.append( dimCursor.getExtent( ) );
					strBuffer.append( "  " );
				}
				strBuffer.append( "\n" );			
			}
			strBuffer.append( "\n" );
		}

		this.testPrint( strBuffer.toString( ) );
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
		EdgeCursor edge1 = null, edge2 = null;

		int index = 0;
		if ( columnEdgeBindingNames != null && !columnEdgeBindingNames.isEmpty( ) )
		{
			edge1 = (EdgeCursor) ( cursor.getOrdinateEdge( ).get( index ) );
			index++;
		}
		if ( rowEdgeBindingNames != null && !rowEdgeBindingNames.isEmpty( ) )
			edge2 = (EdgeCursor) ( cursor.getOrdinateEdge( ).get( index ) );
		DimensionCursor columnCursor1 = null;
		if ( edge1 != null )
			columnCursor1 = (DimensionCursor) edge1.getDimensionCursor( )
					.get( 0 );

		String[] lines = new String[columnEdgeBindingNames.size( )];
		for ( int i = 0; i < columnEdgeBindingNames.size( ); i++ )
		{
			lines[i] = "		                                                                           ";
		}

		if ( edge1 != null )
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
			if ( edge1 != null )
				edge1.beforeFirst( );
			if ( edge1 != null )
				while ( edge1.next( ) )
				{
					if ( subTotal == null )
					{
						line += cursor.getObject( measureBindingNames ) + "		";
					}
					else
					{
						if ( columnCursor1 != null
								&& edge1.getPosition( ) == columnCursor1.getEdgeEnd( ) )
						{
							line += cursor.getObject( measureBindingNames );
							line += "|" + cursor.getObject( subTotal ) + "  ";
						}
						else
						{
							line += cursor.getObject( measureBindingNames )
									+ "		";
						}
					}
				}
			else
			{
				if ( subTotal == null )
				{
					line += cursor.getObject( measureBindingNames ) + "		";
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

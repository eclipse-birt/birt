/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.olap.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;

import javax.olap.OLAPException;
import javax.olap.cursor.CubeCursor;
import javax.olap.cursor.DimensionCursor;
import javax.olap.cursor.EdgeCursor;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.aggregation.api.IBuildInAggregation;
import org.eclipse.birt.data.engine.api.CollectionConditionalExpression;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.ICollectionConditionalExpression;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.ColumnDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.FilterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.ScriptDataSourceDesign;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.SubqueryDefinition;
import org.eclipse.birt.data.engine.api.timefunction.ReferenceDate;
import org.eclipse.birt.data.engine.api.timefunction.TimeFunction;
import org.eclipse.birt.data.engine.api.timefunction.TimePeriod;
import org.eclipse.birt.data.engine.api.timefunction.TimePeriodType;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.DataEngineImpl;
import org.eclipse.birt.data.engine.impl.StopSign;
import org.eclipse.birt.data.engine.olap.api.query.CubeFilterDefinition;
import org.eclipse.birt.data.engine.olap.api.query.CubeSortDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ICubeOperation;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IDimensionDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IHierarchyDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IMeasureDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ISubCubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.cursor.DateCube;
import org.eclipse.birt.data.engine.olap.data.api.ILevel;
import org.eclipse.birt.data.engine.olap.data.api.cube.DocManagerMap;
import org.eclipse.birt.data.engine.olap.data.api.cube.DocManagerReleaser;
import org.eclipse.birt.data.engine.olap.data.api.cube.ICube;
import org.eclipse.birt.data.engine.olap.data.api.cube.IDatasetIterator;
import org.eclipse.birt.data.engine.olap.data.api.cube.IDimension;
import org.eclipse.birt.data.engine.olap.data.api.cube.IHierarchy;
import org.eclipse.birt.data.engine.olap.data.api.cube.ILevelDefn;
import org.eclipse.birt.data.engine.olap.data.document.DocumentManagerFactory;
import org.eclipse.birt.data.engine.olap.data.document.IDocumentManager;
import org.eclipse.birt.data.engine.olap.data.impl.Cube;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Dimension;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.DimensionFactory;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.DimensionForTest;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.LevelDefinition;
import org.eclipse.birt.data.engine.olap.data.util.DataType;
import org.eclipse.birt.data.engine.olap.impl.query.AddingNestAggregations;
import org.eclipse.birt.data.engine.olap.impl.query.CubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.impl.query.CubeQueryExecutor;
import org.eclipse.birt.data.engine.olap.impl.query.SubCubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.query.view.BirtCubeView;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.Scriptable;

import testutil.BaseTestCase;

import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;

/**
 *  
 */

public class CubeFeaturesTest extends BaseTestCase
{

	private static String cubeName = "cube";


	/**
	 * Test use all dimension levels.
	 * 
	 * @throws Exception
	 */
	@Test
    public void testBasic( ) throws Exception
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

		IMeasureDefinition measure = cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		IBinding binding1 = new Binding( "edge1level1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "edge1level2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding3 = new Binding( "edge1level3" );

		binding3.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level13\"]" ) );
		cqd.addBinding( binding3 );

		IBinding binding4 = new Binding( "edge2level1" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "measure1" );
		binding5.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		cqd.addBinding( binding5 );

		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );
		
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		
		
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		columnEdgeBindingNames.add( "edge1level3" );
		
		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "edge2level1" );

		this.printCube( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				"measure1" );
		engine.shutdown( );
	}
	
	/**
	 * Test query without any measure.
	 * Only edge cursors make sense in this case.
	 * 
	 * @throws Exception
	 */
	@Test
    public void testQueryWithoutMeasure( ) throws Exception
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

		//cqd.createMeasure( "measure1" );

		IBinding binding1 = new Binding( "edge1level1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "edge1level2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding3 = new Binding( "edge1level3" );

		binding3.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level13\"]" ) );
		cqd.addBinding( binding3 );

		IBinding binding4 = new Binding( "edge2level1" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"]" ) );
		cqd.addBinding( binding4 );

//		IBinding binding5 = new Binding( "measure1" );
//		binding5.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
//		cqd.addBinding( binding5 );

		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		columnEdgeBindingNames.add( "edge1level3" );
		
		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "edge2level1" );

		this.printCube( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				null );
		engine.shutdown( );
	}
	
	

	/**
	 * Test use part of dimension levels.
	 * 
	 * @throws Exception
	 */
	@Test
    public void testBasic1( ) throws Exception
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		hier1.createLevel( "level11" );
		hier1.createLevel( "level12" );

		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		hier2.createLevel( "level21" );

		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		IBinding binding1 = new Binding( "edge1level1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "edge1level2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding4 = new Binding( "edge2level1" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "measure1" );
		binding5.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		cqd.addBinding( binding5 );

		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "edge2level1" );

		this.printCube( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				"measure1" );
		engine.shutdown( );
	}

	/**
	 * Test use aggregation with one more arguments, referenced using
	 * "dimension".
	 * 
	 * @throws Exception
	 */
	@Test
	@Ignore
    public void testBasic3( ) throws Exception
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

		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );
		
		IBinding binding1 = new Binding( "edge1level1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "edge1level2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding3 = new Binding( "edge1level3" );
		binding3.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level13\"]" ) );
		cqd.addBinding( binding3 );

		IBinding binding4 = new Binding( "edge2level1" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "measure1" );
		binding5.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		cqd.addBinding( binding5 );

		IBinding binding6 = new Binding( "rowGrandTotal" );
		binding6.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		binding6.setAggrFunction( IBuildInAggregation.TOTAL_WEIGHTEDAVE_FUNC );
		binding6.addAggregateOn( "dimension[\"dimension2\"][\"level21\"]" );
		binding6.addArgument( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"][\"attr21\"]" ) );
		cqd.addBinding( binding6 );

		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		columnEdgeBindingNames.add( "edge1level3" );
		
		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "edge2level1" );

		this.printCube( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				"measure1",
				null,
				"rowGrandTotal",
				null );
		engine.shutdown( );
	}
	
	/**
	 * Test adding nest aggregations cube operation	
	 * @throws Exception
	 */
	@Test
    public void testAddingNestAggregations( ) throws Exception
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

		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		IBinding binding1 = new Binding( "edge1level1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "edge1level2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding3 = new Binding( "edge1level3" );
		binding3.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level13\"]" ) );
		cqd.addBinding( binding3 );

		IBinding binding4 = new Binding( "edge2level1" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "measure1" );
		binding5.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		cqd.addBinding( binding5 );


		IBinding binding6 = new Binding( "total" );
		binding6.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		binding6.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		binding6.addAggregateOn( "dimension[\"dimension1\"][\"level11\"]" );
		binding6.addAggregateOn( "dimension[\"dimension1\"][\"level12\"]" );
		binding6.addAggregateOn( "dimension[\"dimension1\"][\"level13\"]" );
		cqd.addBinding( binding6 );
		
		IBinding binding7 = new Binding( "sumTotal1" );
		binding7.setExpression( new ScriptExpression( "data[\"total\"]"  ) );
		binding7.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		binding7.addAggregateOn( "dimension[\"dimension1\"][\"level11\"]" );
		binding7.addAggregateOn( "dimension[\"dimension1\"][\"level12\"]" );
		
		IBinding binding8 = new Binding( "sumTotal2" );
		binding8.setExpression( new ScriptExpression( "data[\"total\"]"  ) );
		binding8.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		binding8.addAggregateOn( "dimension[\"dimension1\"][\"level11\"]" );
		
		IBinding binding9 = new Binding( "sumSumTotal1" );
		binding9.setExpression( new ScriptExpression( "data[\"sumTotal1\"]"  ) );
		binding9.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		binding9.addAggregateOn( "dimension[\"dimension1\"][\"level11\"]" );
		
		IBinding binding10 = new Binding( "maxTotal1" );
		binding10.setExpression( new ScriptExpression( "data[\"total\"]" ) );
		binding10.setAggrFunction( IBuildInAggregation.TOTAL_MAX_FUNC );
		binding10.addAggregateOn( "dimension[\"dimension1\"][\"level11\"]" );
		binding10.addAggregateOn( "dimension[\"dimension1\"][\"level12\"]" );
		binding10.addAggregateOn( "dimension[\"dimension1\"][\"level13\"]" );
		
		IBinding binding11 = new Binding( "maxTotal2" );
		binding11.setExpression( new ScriptExpression( "data[\"total\"]" ) );
		binding11.setAggrFunction( IBuildInAggregation.TOTAL_MAX_FUNC );

		
		ICubeOperation cubeOperation1 = new AddingNestAggregations(new IBinding[]{binding7, binding8, binding10, binding11});
		ICubeOperation cubeOperation2 = new AddingNestAggregations(new IBinding[]{binding9});
		

		cqd.addCubeOperation( cubeOperation1 );
		cqd.addCubeOperation( cubeOperation2 );

		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		columnEdgeBindingNames.add( "edge1level3" );
		
		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "edge2level1" );

		this.printCube( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				"measure1",
				new String[0]);
		this.checkOutputFile( );
		cursor.close( );
		engine.shutdown( );
	}

	/**
	 * Test adding nest aggregations cube operation	
	 * @throws Exception
	 */
	@Test
    public void testAddingNestAggregationsWithExpression( ) throws Exception
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

		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		IBinding binding1 = new Binding( "edge1level1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "edge1level2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding3 = new Binding( "edge1level3" );
		binding3.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level13\"]" ) );
		cqd.addBinding( binding3 );

		IBinding binding4 = new Binding( "edge2level1" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "measure1" );
		binding5.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		cqd.addBinding( binding5 );


		IBinding binding6 = new Binding( "total" );
		binding6.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		binding6.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		binding6.addAggregateOn( "dimension[\"dimension1\"][\"level11\"]" );
		binding6.addAggregateOn( "dimension[\"dimension1\"][\"level12\"]" );
		binding6.addAggregateOn( "dimension[\"dimension1\"][\"level13\"]" );
		cqd.addBinding( binding6 );
		
		IBinding binding7 = new Binding( "sumTotal1" );
		binding7.setExpression( new ScriptExpression( "data[\"total\"]"  ) );
		binding7.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		binding7.addAggregateOn( "dimension[\"dimension1\"][\"level11\"]" );
		binding7.addAggregateOn( "dimension[\"dimension1\"][\"level12\"]" );
		
		IBinding binding8 = new Binding( "sumTotal2" );
		binding8.setExpression( new ScriptExpression( "data[\"total\"]"  ) );
		binding8.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		binding8.addAggregateOn( "dimension[\"dimension1\"][\"level11\"]" );
		
		IBinding binding9 = new Binding( "sumSumTotal1" );
		binding9.setExpression( new ScriptExpression( "data[\"sumTotal1\"]"  ) );
		binding9.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		binding9.addAggregateOn( "dimension[\"dimension1\"][\"level11\"]" );
		
		IBinding binding10 = new Binding( "maxTotal1" );
		binding10.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" 
				+ "+\"/\""
				+ "+dimension[\"dimension1\"][\"level12\"][\"level12\"]" 
				+ "+data[\"total\"]" ) );
		binding10.setAggrFunction( IBuildInAggregation.TOTAL_MAX_FUNC );
		binding10.addAggregateOn( "dimension[\"dimension1\"][\"level11\"]" );
		binding10.addAggregateOn( "dimension[\"dimension1\"][\"level12\"]" );
		binding10.addAggregateOn( "dimension[\"dimension1\"][\"level13\"]" );
		
		IBinding binding11 = new Binding( "maxTotal2" );
		binding11.setExpression( new ScriptExpression( "data[\"total\"]" ) );
		binding11.setAggrFunction( IBuildInAggregation.TOTAL_MAX_FUNC );

		
		ICubeOperation cubeOperation1 = new AddingNestAggregations(new IBinding[]{binding7, binding8, binding10, binding11});
		ICubeOperation cubeOperation2 = new AddingNestAggregations(new IBinding[]{binding9});
		

		cqd.addCubeOperation( cubeOperation1 );
		cqd.addCubeOperation( cubeOperation2 );

		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		columnEdgeBindingNames.add( "edge1level3" );
		
		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "edge2level1" );

		this.printCube( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				"measure1",
				new String[0]);
		this.checkOutputFile( );
		cursor.close( );
		engine.shutdown( );
	}
	
	/**
	 * Test adding nest aggregations cube operation	
	 * @throws Exception
	 */
	@Test
    public void testAddingNestAggregationsWithExpressionFromBindings( ) throws Exception
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

		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		IBinding binding1 = new Binding( "edge1level1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "edge1level2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding3 = new Binding( "edge1level3" );
		binding3.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level13\"]" ) );
		cqd.addBinding( binding3 );

		IBinding binding4 = new Binding( "edge2level1" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "measure1" );
		binding5.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		cqd.addBinding( binding5 );


		IBinding binding6 = new Binding( "total" );
		binding6.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		binding6.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		binding6.addAggregateOn( "dimension[\"dimension1\"][\"level11\"]" );
		binding6.addAggregateOn( "dimension[\"dimension1\"][\"level12\"]" );
		binding6.addAggregateOn( "dimension[\"dimension1\"][\"level13\"]" );
		cqd.addBinding( binding6 );
		
		IBinding binding7 = new Binding( "sumTotal1" );
		binding7.setExpression( new ScriptExpression( "data[\"total\"]"  ) );
		binding7.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		binding7.addAggregateOn( "dimension[\"dimension1\"][\"level11\"]" );
		binding7.addAggregateOn( "dimension[\"dimension1\"][\"level12\"]" );
		
		IBinding binding8 = new Binding( "sumTotal2" );
		binding8.setExpression( new ScriptExpression( "data[\"total\"]"  ) );
		binding8.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		binding8.addAggregateOn( "dimension[\"dimension1\"][\"level11\"]" );
		
		IBinding binding9 = new Binding( "sumSumTotal1" );
		binding9.setExpression( new ScriptExpression( "data[\"sumTotal1\"]"  ) );
		binding9.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		binding9.addAggregateOn( "dimension[\"dimension1\"][\"level11\"]" );
		
		IBinding binding10 = new Binding( "maxTotal1" );
		binding10.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" 
				+ "+\"/\""
				+ "+dimension[\"dimension1\"][\"level12\"][\"level12\"]" 
				+ "+data[\"total\"]" ) );
		binding10.setAggrFunction( IBuildInAggregation.TOTAL_MAX_FUNC );
		binding10.addAggregateOn( "dimension[\"dimension1\"][\"level11\"]" );
		binding10.addAggregateOn( "dimension[\"dimension1\"][\"level12\"]" );
		binding10.addAggregateOn( "dimension[\"dimension1\"][\"level13\"]" );
		
		IBinding binding11 = new Binding( "maxTotal2" );
		binding11.setExpression( new ScriptExpression( "data[\"total\"]" ) );
		binding11.setAggrFunction( IBuildInAggregation.TOTAL_MAX_FUNC );

		//add nest aggregation bindings
		cqd.addBinding( binding7 );
		cqd.addBinding( binding8 );
		cqd.addBinding( binding9 );
		cqd.addBinding( binding10 );
		cqd.addBinding( binding11 );
		
//		ICubeOperation cubeOperation1 = new AddingNestAggregations(new IBinding[]{binding7, binding8, binding10, binding11});
//		ICubeOperation cubeOperation2 = new AddingNestAggregations(new IBinding[]{binding9});
//		
//
//		cqd.addCubeOperation( cubeOperation1 );
//		cqd.addCubeOperation( cubeOperation2 );

		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		columnEdgeBindingNames.add( "edge1level3" );
		
		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "edge2level1" );

		this.printCube( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				"measure1",
				new String[0]);
		this.checkOutputFile( );
		cursor.close( );
		engine.shutdown( );
	}
	
	/**
	 * Test adding nest aggregations cube operation	
	 * @throws Exception
	 */
	@Test
	@Ignore
    public void testCubeRankAggregation( ) throws Exception
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

		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		IBinding binding1 = new Binding( "edge1level1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "edge1level2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding3 = new Binding( "edge1level3" );
		binding3.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level13\"]" ) );
		cqd.addBinding( binding3 );

		IBinding binding4 = new Binding( "edge2level1" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "measure1" );
		binding5.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		cqd.addBinding( binding5 );


		IBinding binding6 = new Binding( "total" );
		binding6.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		binding6.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		binding6.addAggregateOn( "dimension[\"dimension1\"][\"level11\"]" );
		binding6.addAggregateOn( "dimension[\"dimension1\"][\"level12\"]" );
		binding6.addAggregateOn( "dimension[\"dimension1\"][\"level13\"]" );
		cqd.addBinding( binding6 );
		
		//rank aggregation
		IBinding binding7 = new Binding( "totalRankInCountry" );
		binding7.setExpression( new ScriptExpression( "data[\"total\"]"  ) );
		binding7.setAggrFunction( IBuildInAggregation.TOTAL_RANK_FUNC );
		binding7.addAggregateOn( "dimension[\"dimension1\"][\"level11\"]" );
		
		//rank aggregation with a "false" parameter 
		IBinding binding8 = new Binding( "totalRankInCountryDesc" );
		binding8.setExpression( new ScriptExpression( "data[\"total\"]"  ) );
		binding8.setAggrFunction( IBuildInAggregation.TOTAL_RANK_FUNC );
		binding8.addAggregateOn( "dimension[\"dimension1\"][\"level11\"]" );
		binding8.addArgument( new ScriptExpression( "false") );

		//a binding refering a rank aggregation
		IBinding binding9 = new Binding( "referRankAggr" );
		binding9.setExpression( new ScriptExpression( "data[\"totalRankInCountry\"]"  ) );

		//add nest aggregation bindings
		cqd.addBinding( binding7 );
		cqd.addBinding( binding8 );
		
		//add a binding which refers to nest aggregation
		cqd.addBinding( binding9 );

		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		columnEdgeBindingNames.add( "edge1level3" );
		
		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "edge2level1" );

		this.printCubeWithRank( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				"measure1",
				new String[0]);
		this.checkOutputFile( );
		cursor.close( );
		engine.shutdown( );
	}
	
	
	/**
	 * Test adding nest aggregations cube operation	 and filter on this nested aggregation.
	 * @throws Exception
	 */
	@Test
    public void testCubeRankAggregation2( ) throws Exception
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		ILevelDefinition level11 = hier1.createLevel( "level11" );
		ILevelDefinition level12 = hier1.createLevel( "level12" );
		ILevelDefinition level13 = hier1.createLevel( "level13" );

		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		ILevelDefinition level21 = hier2.createLevel( "level21" );

		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		IBinding binding1 = new Binding( "edge1level1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "edge1level2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding3 = new Binding( "edge1level3" );
		binding3.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level13\"]" ) );
		cqd.addBinding( binding3 );

		IBinding binding4 = new Binding( "edge2level1" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "measure1" );
		binding5.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		binding5.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		binding5.addAggregateOn( "dimension[\"dimension1\"][\"level11\"]" );
		binding5.addAggregateOn( "dimension[\"dimension1\"][\"level12\"]" );
		binding5.addAggregateOn( "dimension[\"dimension1\"][\"level13\"]" );
		binding5.addAggregateOn( "dimension[\"dimension2\"][\"level21\"]" );
		cqd.addBinding( binding5 );


		IBinding binding6 = new Binding( "total" );
		binding6.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		binding6.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		binding6.addAggregateOn( "dimension[\"dimension1\"][\"level11\"]" );
		binding6.addAggregateOn( "dimension[\"dimension1\"][\"level12\"]" );
		binding6.addAggregateOn( "dimension[\"dimension1\"][\"level13\"]" );
		cqd.addBinding( binding6 );
		
		//rank aggregation
		IBinding binding7 = new Binding( "totalRankInCountry" );
		binding7.setExpression( new ScriptExpression( "data[\"measure1\"]"  ) );
		binding7.setAggrFunction( IBuildInAggregation.TOTAL_RANK_FUNC );
		binding7.addAggregateOn( "dimension[\"dimension1\"][\"level11\"]" );
		binding7.addAggregateOn( "dimension[\"dimension1\"][\"level12\"]" );
		binding7.addAggregateOn( "dimension[\"dimension1\"][\"level13\"]" );

		IConditionalExpression expr = new ConditionalExpression( "data[\"totalRankInCountry\"]",
				IConditionalExpression.OP_TOP_N,
				"3",
				null );
		CubeFilterDefinition filter = new CubeFilterDefinition( expr );
		filter.setAxisQualifierLevels( new ILevelDefinition[]{
				level11, level12, level13
		} );
		filter.setAxisQualifierValues( new Object[]{
				"CN", "BJ", "HD"
		} );
		filter.setTargetLevel( level21 );
		cqd.addFilter( filter );
		
		//add nest aggregation bindings
		cqd.addBinding( binding7 );
		

		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		columnEdgeBindingNames.add( "edge1level3" );
		
		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "edge2level1" );

		List measureList = new ArrayList( );
		measureList.add( "measure1" );
		measureList.add( "totalRankInCountry" );

		this.printCube( cursor, columnEdgeBindingNames,
				rowEdgeBindingNames, measureList,
				null, null, null, true );
		
		this.checkOutputFile( );
		cursor.close( );
		engine.shutdown( );
	}
	@Test
    public void testValidateBinding( ) throws Exception
	{
		checkDuplicateBindingName( );
		checkInexistentReference( );
		checkReferenceCycle( );
	}
	
	private void checkDuplicateBindingName( ) throws Exception
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

		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		IBinding binding1 = new Binding( "edge1level1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "edge1level2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding3 = new Binding( "edge1level3" );

		binding3.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level13\"]" ) );
		cqd.addBinding( binding3 );

		IBinding binding4 = new Binding( "edge2level1" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "measure1" );
		binding5.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		cqd.addBinding( binding5 );
		
		IBinding binding6 = new Binding( "measure1" );
		binding6.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		cqd.addBinding( binding6 );

		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );
		try 
		{
			IPreparedCubeQuery pcq = engine.prepare( cqd, null );
			assertTrue( false );
		} 
		catch (BirtException e)
		{
			assertTrue( true );
		}
		finally
		{
			engine.shutdown( );
		}
	}

	private void checkInexistentReference( ) throws Exception
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

		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		IBinding binding1 = new Binding( "edge1level1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "edge1level2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding3 = new Binding( "edge1level3" );

		binding3.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level13\"]" ) );
		cqd.addBinding( binding3 );

		IBinding binding4 = new Binding( "edge2level1" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "measure1" );
		binding5.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		cqd.addBinding( binding5 );
		
		IBinding binding6 = new Binding( "test" );
		binding6.setExpression( new ScriptExpression( "data[\"measure1\"] + data[\"nothing\"]" ) );
		cqd.addBinding( binding6 );

		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );
		try 
		{
			IPreparedCubeQuery pcq = engine.prepare( cqd, null );
			assertTrue( false );
		} 
		catch (BirtException e)
		{
			assertTrue( true );
		}
		finally
		{
			engine.shutdown( );
		}
	}
	
	private void checkReferenceCycle( ) throws Exception
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

		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		IBinding binding1 = new Binding( "edge1level1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "edge1level2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding3 = new Binding( "edge1level3" );

		binding3.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level13\"]" ) );
		cqd.addBinding( binding3 );

		IBinding binding4 = new Binding( "edge2level1" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "measure1" );
		binding5.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		cqd.addBinding( binding5 );
		
		IBinding binding6 = new Binding( "test1" );
		binding6.setExpression( new ScriptExpression( "data[\"measure1\"] + data[\"test2\"]" ) );
		cqd.addBinding( binding6 );
		
		IBinding binding7 = new Binding( "test2" );
		binding7.setExpression( new ScriptExpression( "data[\"measure1\"] + data[\"test1\"]" ) );
		cqd.addBinding( binding7 );

		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );
		try 
		{
			IPreparedCubeQuery pcq = engine.prepare( cqd, null );
			assertTrue( false );
		} 
		catch (BirtException e)
		{
			assertTrue( true );
		}
		finally
		{
			engine.shutdown( );
		}
	}
	
	/**
	 * Boundary test for adding nest aggregations cube operation	
	 * @throws Exception
	 */
	@Test
    public void testAddingNestAggregationsBoundary( ) throws Exception
	{
		checkNonexistentDimensionException();
		checkNonexistentLevelException();
	}

	/**
	 * nest aggregation with a  nonexistent dimension in aggregationOns
	 * @throws Exception
	 */
	private void checkNonexistentDimensionException( ) throws Exception
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

		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		IBinding binding1 = new Binding( "edge1level1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "edge1level2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding3 = new Binding( "edge1level3" );
		binding3.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level13\"]" ) );
		cqd.addBinding( binding3 );

		IBinding binding4 = new Binding( "edge2level1" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "measure1" );
		binding5.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		cqd.addBinding( binding5 );


		IBinding binding6 = new Binding( "total" );
		binding6.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		binding6.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		binding6.addAggregateOn( "dimension[\"dimension1\"][\"level11\"]" );
		binding6.addAggregateOn( "dimension[\"dimension1\"][\"level12\"]" );
		binding6.addAggregateOn( "dimension[\"dimension1\"][\"level13\"]" );
		cqd.addBinding( binding6 );
		
		IBinding binding7 = new Binding( "nestTotal1" );
		binding7.setExpression( new ScriptExpression( "data[\"total\"]"  ) );
		binding7.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		//"dimension2" not exist in "totoal" bind's aggregateOns
		binding7.addAggregateOn( "dimension[\"dimension2\"][\"level11\"]" );
		ICubeOperation cubeOperation1 = new AddingNestAggregations(new IBinding[]{binding7});
		cqd.addCubeOperation( cubeOperation1 );
		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = null;
		try 
		{
			cursor = queryResults.getCubeCursor( );
			assertTrue( false);
		} 
		catch (DataException e)
		{
			assertTrue( true );
		}
	}
	
	/**
	 * nest aggregation with a nonexistent level in aggregationOns
	 * @throws Exception
	 */
	private void checkNonexistentLevelException( ) throws Exception
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

		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		IBinding binding1 = new Binding( "edge1level1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "edge1level2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding3 = new Binding( "edge1level3" );
		binding3.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level13\"]" ) );
		cqd.addBinding( binding3 );

		IBinding binding4 = new Binding( "edge2level1" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "measure1" );
		binding5.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		cqd.addBinding( binding5 );


		IBinding binding6 = new Binding( "total" );
		binding6.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		binding6.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		binding6.addAggregateOn( "dimension[\"dimension1\"][\"level11\"]" );
		binding6.addAggregateOn( "dimension[\"dimension1\"][\"level12\"]" );
		binding6.addAggregateOn( "dimension[\"dimension1\"][\"level13\"]" );
		cqd.addBinding( binding6 );

		IBinding binding7 = new Binding( "nestTotal1" );
		binding7.setExpression( new ScriptExpression( "data[\"total\"]"  ) );
		binding7.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		//"level21" not exist in "totoal" bind's aggregateOns
		binding7.addAggregateOn( "dimension[\"dimension1\"][\"level21\"]" );
		ICubeOperation cubeOperation1 = new AddingNestAggregations(new IBinding[]{binding7});
		cqd.addCubeOperation( cubeOperation1 );
		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = null;
		try 
		{
			cursor = queryResults.getCubeCursor( );
			assertTrue( false);
		} 
		catch (DataException e)
		{
			assertTrue( true);
		}
	}
	/**
	 * Test use aggregation with one more arguments, referenced using "data"
	 * 
	 * @throws Exception
	 */
	@Test
	@Ignore
    public void testBasic4( ) throws Exception
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

		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		IBinding binding1 = new Binding( "edge1level1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "edge1level2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding3 = new Binding( "edge1level3" );
		binding3.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level13\"]" ) );
		cqd.addBinding( binding3 );

		IBinding binding4 = new Binding( "edge2level1" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "measure1" );
		binding5.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		cqd.addBinding( binding5 );

		IBinding binding6 = new Binding( "attr21" );
		binding6.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"][\"attr21\"]" ) );
		cqd.addBinding( binding6 );

		IBinding binding7 = new Binding( "rowGrandTotal" );
		binding7.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		binding7.setAggrFunction( IBuildInAggregation.TOTAL_WEIGHTEDAVE_FUNC );
		binding7.addAggregateOn( "dimension[\"dimension2\"][\"level21\"]" );
		binding7.addArgument( new ScriptExpression( "data[\"attr21\"]" ) );
		cqd.addBinding( binding7 );

		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		columnEdgeBindingNames.add( "edge1level3" );

		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "edge2level1" );

		this.printCube( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				"measure1",
				null,
				"rowGrandTotal",
				null );
		
		engine.shutdown( );
	}

	/**
	 * Test use page cursor
	 * 
	 * @throws Exception
	 */
	@Test
	@Ignore
    public void testBasic5( ) throws Exception
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IEdgeDefinition pageEdge = cqd.createEdge( ICubeQueryDefinition.PAGE_EDGE );
		
		IDimensionDefinition dim0 = pageEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier0 = dim0.createHierarchy( "dimension1" );
		hier0.createLevel( "level11" );

		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension2" );
		hier1.createLevel( "level12" );
		
		IDimensionDefinition dim2 = columnEdge.createDimension( "dimension3" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension3" );
		hier2.createLevel( "level13" );
		
		IDimensionDefinition dim3 = rowEdge.createDimension( "dimension4" );
		IHierarchyDefinition hier3 = dim3.createHierarchy( "dimension4" );
		hier3.createLevel( "level21" );
				
		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		IBinding binding1 = new Binding( "edge1level1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "edge1level2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding3 = new Binding( "edge1level3" );
		binding3.setExpression( new ScriptExpression( "dimension[\"dimension3\"][\"level13\"]" ) );
		cqd.addBinding( binding3 );

		IBinding binding4 = new Binding( "edge2level1" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension4\"][\"level21\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "measure1" );
		binding5.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		cqd.addBinding( binding5 );

		IBinding binding6 = new Binding( "attr21" );
		binding6.setExpression( new ScriptExpression( "dimension[\"dimension4\"][\"level21\"][\"attr21\"]" ) );
		cqd.addBinding( binding6 );

		IBinding binding7 = new Binding( "rowGrandTotal" );
		binding7.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		binding7.setAggrFunction( IBuildInAggregation.TOTAL_WEIGHTEDAVE_FUNC );
		binding7.addAggregateOn( "dimension[\"dimension1\"][\"level11\"]" );
		binding7.addAggregateOn( "dimension[\"dimension4\"][\"level21\"]" );
		binding7.addArgument( new ScriptExpression( "data[\"attr21\"]" ) );
		cqd.addBinding( binding7 );

		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube1( engine );
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List pageEdgeBindingNames = new ArrayList( );
		pageEdgeBindingNames.add( "edge1level1" );
		
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level2" );
		columnEdgeBindingNames.add( "edge1level3" );
		
		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "edge2level1" );

		this.printCubeWithPage( cursor,
				pageEdgeBindingNames,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				"measure1",
				null,
				"rowGrandTotal",
				null );
		
		engine.shutdown( );
	}
	

	/**
	 * Test use page cursor
	 * 
	 * @throws Exception
	 */
	@Test
	@Ignore
    public void testBasic6( ) throws Exception
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IEdgeDefinition pageEdge = cqd.createEdge( ICubeQueryDefinition.PAGE_EDGE );
		
		IDimensionDefinition dim0 = pageEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier0 = dim0.createHierarchy( "dimension1" );
		hier0.createLevel( "level11" );

		IDimensionDefinition dim1 = pageEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension2" );
		hier1.createLevel( "level12" );
		
		IDimensionDefinition dim2 = columnEdge.createDimension( "dimension3" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension3" );
		hier2.createLevel( "level13" );
		
		IDimensionDefinition dim3 = rowEdge.createDimension( "dimension4" );
		IHierarchyDefinition hier3 = dim3.createHierarchy( "dimension4" );
		hier3.createLevel( "level21" );
				
		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		IBinding binding1 = new Binding( "edge1level1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "edge1level2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding3 = new Binding( "edge1level3" );
		binding3.setExpression( new ScriptExpression( "dimension[\"dimension3\"][\"level13\"]" ) );
		cqd.addBinding( binding3 );

		IBinding binding4 = new Binding( "edge2level1" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension4\"][\"level21\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "measure1" );
		binding5.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		cqd.addBinding( binding5 );

		IBinding binding6 = new Binding( "attr21" );
		binding6.setExpression( new ScriptExpression( "dimension[\"dimension4\"][\"level21\"][\"attr21\"]" ) );
		cqd.addBinding( binding6 );

		IBinding binding7 = new Binding( "rowGrandTotal" );
		binding7.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		binding7.setAggrFunction( IBuildInAggregation.TOTAL_WEIGHTEDAVE_FUNC );
		binding7.addAggregateOn( "dimension[\"dimension1\"][\"level11\"]" );
		binding7.addAggregateOn( "dimension[\"dimension2\"][\"level12\"]" );
		binding7.addAggregateOn( "dimension[\"dimension4\"][\"level21\"]" );
		binding7.addArgument( new ScriptExpression( "data[\"attr21\"]" ) );
		cqd.addBinding( binding7 );

		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube1( engine );
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List pageEdgeBindingNames = new ArrayList( );
		pageEdgeBindingNames.add( "edge1level1" );
		pageEdgeBindingNames.add( "edge1level2" );
		
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level3" );
		
		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "edge2level1" );

		this.printCubeWithPage( cursor,
				pageEdgeBindingNames,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				"measure1",
				null,
				"rowGrandTotal",
				null );
		
		engine.shutdown( );
	}
	
	/**
	 * Test use aggregation with one more arguments
	 * 
	 * @throws Exception
	 */
	@Test
    public void testInvalidBinding( ) throws Exception
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

		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		IBinding binding1 = new Binding( "edge1level1" );
		// Invalid binding 1, invalid dim name
		binding1.setExpression( new ScriptExpression( "dimension[\"bad\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "edge1level2" );
		// Invalid binding 2, invalid level name
		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"bad\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding3 = new Binding( "edge1level3" );
		binding3.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level13\"]" ) );
		cqd.addBinding( binding3 );

		IBinding binding4 = new Binding( "edge2level1" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "measure1" );
		// Invalid binding 3 invalid measure name
		binding5.setExpression( new ScriptExpression( "measure[\"bad\"]" ) );
		cqd.addBinding( binding5 );

		IBinding binding6 = new Binding( "rowGrandTotal" );
		// Invalid binding 4 missing aggr function.
		binding6.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );

		binding6.addAggregateOn( "dimension[\"dimension2\"][\"level21\"]" );
		binding6.addArgument( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"][\"attr21\"]" ) );
		cqd.addBinding( binding6 );

		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );

		try
		{
			IPreparedCubeQuery pcq = engine.prepare( cqd, null );
			ICubeQueryResults queryResults = pcq.execute( null );
			queryResults.getCubeCursor( );
			fail( "Should not arrive here" );
		}
		catch ( Exception e )
		{
		}
		engine.shutdown( );
	}

	/**
	 * Filter1, filter out all level11 == CN.
	 * 
	 * @throws Exception
	 */
	@Test
    public void testFilter1( ) throws Exception
	{

		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		hier1.createLevel( "level11" );
		hier1.createLevel( "level12" );

		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		hier2.createLevel( "level21" );

		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		IBinding binding1 = new Binding( "edge1level1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "edge1level2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding4 = new Binding( "edge2level1" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "measure1" );
		binding5.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		cqd.addBinding( binding5 );

		IFilterDefinition filter = new FilterDefinition( new ConditionalExpression( "dimension[\"dimension1\"][\"level11\"]",
				IConditionalExpression.OP_EQ,
				"\"CN\"" ) );
		cqd.addFilter( filter );
		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "edge2level1" );

		this.printCube( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				"measure1" );
		
		engine.shutdown( );

	}
	@Test
    public void testDimensionQuery1( ) throws Exception
	{

		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		hier1.createLevel( "level11" );

		IBinding binding1 = new Binding( "edge1level1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		IFilterDefinition filter = new FilterDefinition( new ConditionalExpression( "dimension[\"dimension1\"][\"level11\"]",
				IConditionalExpression.OP_EQ,
				"\"CN\"" ) );
		cqd.addFilter( filter );
		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		List rowEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );

		this.printCube( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				null );
		
		engine.shutdown( );

	}
	@Test
    public void testDimensionQuery2( ) throws Exception
	{

		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		hier1.createLevel( "level11" );

		IBinding binding1 = new Binding( "edge1level1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		List rowEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );

		this.printCube( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				null );
		
		engine.shutdown( );

	}
	@Test
    public void testDimensionQuery3( ) throws Exception
	{

		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		hier1.createLevel( "level11" );
		hier1.createLevel( "level12" );

		IBinding binding1 = new Binding( "edge1level1" );
		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );
		
		IBinding binding2 = new Binding( "edge1level2" );
		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IFilterDefinition filter = new FilterDefinition( new ConditionalExpression( "dimension[\"dimension1\"][\"level11\"]",
				IConditionalExpression.OP_EQ,
				"\"CN\"" ) );
		cqd.addFilter( filter );
		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		List rowEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );

		this.printCube( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				null );
		
		engine.shutdown( );

	}
	
	/**
	 * Filter2, filter out all level11 = CN and level21 > 2000.
	 * 
	 * @throws Exception
	 */
	@Test
    public void testFilter2( ) throws Exception
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		hier1.createLevel( "level11" );
		hier1.createLevel( "level12" );

		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		hier2.createLevel( "level21" );

		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		IBinding binding1 = new Binding( "edge1level1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "edge1level2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding4 = new Binding( "edge2level1" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "measure1" );
		binding5.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		cqd.addBinding( binding5 );

		IFilterDefinition filter1 = new FilterDefinition( new ConditionalExpression( "dimension[\"dimension1\"][\"level11\"]",
				IConditionalExpression.OP_EQ,
				"\"CN\"" ) );
		IFilterDefinition filter2 = new FilterDefinition( new ConditionalExpression( "dimension[\"dimension2\"][\"level21\"]",
				IConditionalExpression.OP_GE,
				"2000" ) );

		cqd.addFilter( filter1 );
		cqd.addFilter( filter2 );
		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "edge2level1" );

		this.printCube( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				"measure1" );
		
		engine.shutdown( );

	}

	/**
	 * Filter2, filter out all level11 = CN and level21 > 2000.
	 * 
	 * @throws Exception
	 */
	@Test
    public void testFilter3( ) throws Exception
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		hier1.createLevel( "level11" );
		hier1.createLevel( "level12" );

		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		hier2.createLevel( "level21" );

		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		IBinding binding1 = new Binding( "edge1level1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "edge1level2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding4 = new Binding( "edge2level1" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "measure1" );
		binding5.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		cqd.addBinding( binding5 );

		IFilterDefinition filter1 = new FilterDefinition( new ConditionalExpression( "data[\"edge1level1\"]",
				IConditionalExpression.OP_EQ,
				"\"CN\"" ) );
		IFilterDefinition filter2 = new FilterDefinition( new ConditionalExpression( "data[\"edge2level1\"]",
				IConditionalExpression.OP_GE,
				"2000" ) );

		cqd.addFilter( filter1 );
		cqd.addFilter( filter2 );
		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "edge2level1" );
		
		this.printCube( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				"measure1" );
		
		engine.shutdown( );

	}

	/**
	 * Filter2, filter out all level11 = CN and level21 > 2000.
	 * 
	 * @throws Exception
	 */
	@Test
    public void testFilter4( ) throws Exception
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		hier1.createLevel( "level11" );
		hier1.createLevel( "level12" );

		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		hier2.createLevel( "level21" );

		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		IBinding binding1 = new Binding( "edge1level1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "edge1level2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding4 = new Binding( "edge2level1" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "measure1" );
		binding5.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		cqd.addBinding( binding5 );

		IFilterDefinition filter1 = new FilterDefinition( new ConditionalExpression( "data[\"edge1level1\"]",
				IConditionalExpression.OP_EQ,
				"\"CNK\"" ) );

		cqd.addFilter( filter1 );

		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		try
		{
			this.createCube( engine );
			IPreparedCubeQuery pcq = engine.prepare( cqd, null );
			ICubeQueryResults queryResults = pcq.execute( null );
			queryResults.getCubeCursor( );

		}
		catch ( Exception e )
		{
			fail( "Should not arrive here" );
		}
		
		engine.shutdown( );
	}

	/**
	 * Filter out all level11 == US. meanwhile level11 is not defined in 
	 * cube query.
	 * 
	 * @throws Exception
	 */
	@Test
    public void testFilter5( ) throws Exception
	{

		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		hier1.createLevel( "level12" );

		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		hier2.createLevel( "level21" );

		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		IBinding binding2 = new Binding( "edge1level2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding4 = new Binding( "edge2level1" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "measure1" );
		binding5.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		cqd.addBinding( binding5 );

		IFilterDefinition filter = new CubeFilterDefinition( new ConditionalExpression( "dimension[\"dimension1\"][\"level11\"]",
				IConditionalExpression.OP_EQ,
				"\"US\"" ) );
		cqd.addFilter( filter );
		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level2" );
		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "edge2level1" );

		this.printCube( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				"measure1" );
		
		engine.shutdown( );

	}
	
	/**
	 * Mixed dimension filter and facttable based filter,
	 * 1. filter out all level11 == US. meanwhile level11 is not defined in cube query 
	 * 2. filter out all level21 == "CS"
	 * 3. filter out all aggr measure > 38.0
	 * cube query.
	 * 
	 * @throws Exception
	 */
	@Test
    public void testFilter6( ) throws Exception
	{

		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		hier1.createLevel( "level12" );

		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		hier2.createLevel( "level21" );

		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		IBinding binding2 = new Binding( "edge1level2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding4 = new Binding( "edge2level1" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "measure1" );
		binding5.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		cqd.addBinding( binding5 );

		IFilterDefinition filter1 = new CubeFilterDefinition( new ConditionalExpression( "dimension[\"dimension1\"][\"level11\"]",
				IConditionalExpression.OP_EQ,
				"\"US\"" ) );
		
		IFilterDefinition filter2 = new CubeFilterDefinition( new ConditionalExpression( "dimension[\"dimension1\"][\"level12\"]",
				IConditionalExpression.OP_EQ,
				"\"CS\"" ) );
		
		IFilterDefinition filter3 = new CubeFilterDefinition( new ConditionalExpression( "data[\"measure1\"]",
				IConditionalExpression.OP_GE,
				"38.0" ) );
		cqd.addFilter( filter1 );
		cqd.addFilter( filter2 );
		cqd.addFilter( filter3 );
		
		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level2" );
		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "edge2level1" );

		this.printCube( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				"measure1" );
		
		engine.shutdown( );

	}
	
	/**
	 * Test collection IN filter
	 * @throws Exception
	 */
	@Test
    public void testFilter7( ) throws Exception
	{

		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		hier1.createLevel( "level11" );
		hier1.createLevel( "level12" );

		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		hier2.createLevel( "level21" );

		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		IBinding binding1 = new Binding( "edge1level1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "edge1level2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding4 = new Binding( "edge2level1" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "measure1" );
		binding5.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		cqd.addBinding( binding5 );

		List<IScriptExpression> exprs = new ArrayList<IScriptExpression>();
		exprs.add( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]") );
		exprs.add( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]") );
		List<Collection<IScriptExpression>> targets = new ArrayList<Collection<IScriptExpression>>();
		Collection<IScriptExpression> CNBJ = new ArrayList<IScriptExpression>();
		CNBJ.add( new ScriptExpression( "\"CN\"") );
		CNBJ.add( new ScriptExpression( "\"BJ\"") );
		
		Collection<IScriptExpression> JPTK = new ArrayList<IScriptExpression>();
		JPTK.add( new ScriptExpression( "\"JP\"") );
		JPTK.add( new ScriptExpression( "\"TK\"") );
		
		targets.add( CNBJ );
		targets.add( JPTK );
		
		CollectionConditionalExpression filterExpr = new CollectionConditionalExpression( exprs, ICollectionConditionalExpression.OP_IN, targets );
		IFilterDefinition filter = new FilterDefinition( filterExpr );
		cqd.addFilter( filter );
		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "edge2level1" );

		this.printCube( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				"measure1" );
		
		engine.shutdown( );

	}
	
	/**
	 * Test collection NOTIN filter
	 * @throws Exception
	 */
	@Test
    public void testFilter8( ) throws Exception
	{

		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		hier1.createLevel( "level11" );
		hier1.createLevel( "level12" );

		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		hier2.createLevel( "level21" );

		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		IBinding binding1 = new Binding( "edge1level1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "edge1level2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding4 = new Binding( "edge2level1" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "measure1" );
		binding5.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		cqd.addBinding( binding5 );

		List<IScriptExpression> exprs = new ArrayList<IScriptExpression>();
		exprs.add( new ScriptExpression( "data[\"edge1level1\"]") );
		exprs.add( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]") );
		List<Collection<IScriptExpression>> targets = new ArrayList<Collection<IScriptExpression>>();
		Collection<IScriptExpression> CNBJ = new ArrayList<IScriptExpression>();
		CNBJ.add( new ScriptExpression( "\"CN\"") );
		CNBJ.add( new ScriptExpression( "\"BJ\"") );
		
		Collection<IScriptExpression> JPTK = new ArrayList<IScriptExpression>();
		JPTK.add( new ScriptExpression( "\"JP\"") );
		JPTK.add( new ScriptExpression( "\"TK\"") );
		
		targets.add( CNBJ );
		targets.add( JPTK );
		
		CollectionConditionalExpression filterExpr = new CollectionConditionalExpression( exprs, ICollectionConditionalExpression.OP_NOT_IN, targets );
		IFilterDefinition filter = new FilterDefinition( filterExpr );
		cqd.addFilter( filter );
		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "edge2level1" );

		this.printCube( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				"measure1" );
		
		engine.shutdown( );

	}
	
	/**
	 * Filter on derived measure
	 * 
	 * @throws Exception
	 */
	@Test
    public void testFilter9( ) throws Exception
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		ILevelDefinition level11 = hier1.createLevel( "level11" );
		ILevelDefinition level12 = hier1.createLevel( "level12" );
		ILevelDefinition level13 = hier1.createLevel( "level13" );
		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		ILevelDefinition level21 = hier2.createLevel( "level21" );

		createSortTestBindings( cqd );

		// filter on year
		CubeFilterDefinition filter1 = new CubeFilterDefinition( new ScriptExpression( "data[\"rowGrandTotal\"] > 420" ) );
		filter1.setAxisQualifierLevels( null );
		filter1.setAxisQualifierValues( null );
		filter1.setTargetLevel( level21 );

		// sort on country
		CubeFilterDefinition filter2 = new CubeFilterDefinition( new ScriptExpression( "data[\"derived1\"] > 0.5" ) );

		filter2.setAxisQualifierLevels( new ILevelDefinition[]{
			level21
		} );
		filter2.setAxisQualifierValues( new Object[]{
			"2000"
		} );
		filter2.setTargetLevel( level12 );

		CubeFilterDefinition filter3 = new CubeFilterDefinition( new ScriptExpression( "data[\"country_year_total\"] < 300" ) );

		filter3.setAxisQualifierLevels( new ILevelDefinition[]{
			level21
		} );
		filter3.setAxisQualifierValues( new Object[]{
			"2002"
		} );
		filter3.setTargetLevel( level11 );

		CubeSortDefinition sorter4 = new CubeSortDefinition( );
		sorter4.setExpression( "dimension[\"dimension1\"][\"level13\"]" );
		sorter4.setAxisQualifierLevels( null );
		sorter4.setAxisQualifierValues( null );
		sorter4.setTargetLevel( level13 );
		sorter4.setSortDirection( ISortDefinition.SORT_DESC );

		// Make UN before China.
		CubeSortDefinition sorter5 = new CubeSortDefinition( );
		sorter5.setExpression( "dimension[\"dimension1\"][\"level11\"]" );
		sorter5.setAxisQualifierLevels( null );
		sorter5.setAxisQualifierValues( null );
		sorter5.setTargetLevel( level11 );
		sorter5.setSortDirection( ISortDefinition.SORT_DESC );

		/*
		 * //sort on city. SortDefinition sorter3 = new SortDefinition();
		 * sorter3.setExpression( "dimension[\"dimension1\"][\"level12\"]" );
		 * sorter3.setSortDirection( ISortDefinition.SORT_DESC );
		 */
		/*cqd.addFilter( filter1 );*/
		cqd.addFilter( filter2 );
		/*cqd.addFilter( filter3 );*/
		/*cqd.addSort( sorter4 );
		cqd.addSort( sorter5 );*/
		DataEngineContext context = createPresentationContext( );
		context.setTmpdir( this.getTempDir( ) );
		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( context );
		this.createCube( engine );

		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		columnEdgeBindingNames.add( "edge1level3" );

		printCube( cursor,
				"country_year_total",
				"city_year_total",
				"dist_total",
				"city_total",
				"country_total",
				"rowGrandTotal",
				"grandTotal",
				new String[]{
						"edge1level1", "edge1level2", "edge1level3"
				},
				"edge2level1",
				"measure1" );
		engine.shutdown( );

	}
	
	/**
	 * Filter on derived measure
	 * 
	 * @throws Exception
	 */
	@Test
    public void testFilter10( ) throws Exception
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		ILevelDefinition level11 = hier1.createLevel( "level11" );
		ILevelDefinition level12 = hier1.createLevel( "level12" );
		ILevelDefinition level13 = hier1.createLevel( "level13" );
		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		ILevelDefinition level21 = hier2.createLevel( "level21" );

		createSortTestBindings( cqd );

		// sort on country
		CubeFilterDefinition filter2 = new CubeFilterDefinition( new ScriptExpression( "data[\"derived2\"] < 0.3" ) );

		filter2.setAxisQualifierLevels( new ILevelDefinition[]{
			level21
		} );
		
		filter2.setTargetLevel( level12 );
	
		cqd.addFilter( filter2 );

		DataEngineContext context = createPresentationContext( );
		context.setTmpdir( this.getTempDir( ) );
		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( context );
		this.createCube( engine );

		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		columnEdgeBindingNames.add( "edge1level3" );

		printCube( cursor,
				"country_year_total",
				"city_year_total",
				"dist_total",
				"city_total",
				"country_total",
				"rowGrandTotal",
				"grandTotal",
				new String[]{
						"edge1level1", "edge1level2", "edge1level3"
				},
				"edge2level1",
				"measure1" );
		engine.shutdown( );

	}
	
	/**
	 * Filter on derived measure
	 * 
	 * @throws Exception
	 */
	@Test
    public void testFilter11( ) throws Exception
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		ILevelDefinition level11 = hier1.createLevel( "level11" );
		ILevelDefinition level12 = hier1.createLevel( "level12" );
		ILevelDefinition level13 = hier1.createLevel( "level13" );
		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		ILevelDefinition level21 = hier2.createLevel( "level21" );

		createSortTestBindings( cqd );

		// sort on country
		CubeFilterDefinition filter2 = new CubeFilterDefinition( new ScriptExpression( "data[\"derived1\"] > 0.3" ) );

		filter2.setAxisQualifierLevels( new ILevelDefinition[]{
			level21
		} );
		filter2.setAxisQualifierValues( new Object[]{
			"2000"
		} );
		filter2.setTargetLevel( level12 );

		CubeSortDefinition sorter4 = new CubeSortDefinition();
		sorter4.setExpression( "data[\"derived2\"]" );
		sorter4.setTargetLevel( level12 );
		sorter4.setSortDirection( ISortDefinition.SORT_ASC );


		cqd.addFilter( filter2 );
		cqd.addSort( sorter4 );
		
		DataEngineContext context = createPresentationContext( );
		context.setTmpdir( this.getTempDir( ) );
		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( context );
		this.createCube( engine );

		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		columnEdgeBindingNames.add( "edge1level3" );

		printCube( cursor,
				"country_year_total",
				"city_year_total",
				"dist_total",
				"city_total",
				"country_total",
				"rowGrandTotal",
				"grandTotal",
				new String[]{
						"edge1level1", "edge1level2", "edge1level3"
				},
				"edge2level1",
				"measure1" );
		engine.shutdown( );

	}
	
	/**
	 * Filter on derived measure
	 * 
	 * @throws Exception
	 */
	@Test
    public void testFilter12( ) throws Exception
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		ILevelDefinition level11 = hier1.createLevel( "level11" );
		ILevelDefinition level12 = hier1.createLevel( "level12" );
		ILevelDefinition level13 = hier1.createLevel( "level13" );
		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		ILevelDefinition level21 = hier2.createLevel( "level21" );

		createSortTestBindings( cqd );

		CubeFilterDefinition filter1 = new CubeFilterDefinition( new ScriptExpression( "data[\"rowGrandTotal\"] > 420" ) );
		filter1.setAxisQualifierLevels( null );
		filter1.setAxisQualifierValues( null );
		filter1.setTargetLevel( level21 );
		
		CubeFilterDefinition filter2 = new CubeFilterDefinition( new ScriptExpression( "data[\"derived1\"] > 0.3" ) );

		filter2.setAxisQualifierLevels( new ILevelDefinition[]{
			level21
		} );
		filter2.setAxisQualifierValues( new Object[]{
			"2000"
		} );
		filter2.setTargetLevel( level12 );

		CubeSortDefinition sorter4 = new CubeSortDefinition();
		sorter4.setExpression( "data[\"derived2\"]" );
		sorter4.setTargetLevel( level12 );
		sorter4.setSortDirection( ISortDefinition.SORT_ASC );

		cqd.addFilter( filter1 );
		cqd.addFilter( filter2 );
		cqd.addSort( sorter4 );
		
		DataEngineContext context = createPresentationContext( );
		context.setTmpdir( this.getTempDir( ) );
		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( context );
		this.createCube( engine );

		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		columnEdgeBindingNames.add( "edge1level3" );

		printCube( cursor,
				"country_year_total",
				"city_year_total",
				"dist_total",
				"city_total",
				"country_total",
				"rowGrandTotal",
				"grandTotal",
				new String[]{
						"edge1level1", "edge1level2", "edge1level3"
				},
				"edge2level1",
				"measure1" );
		engine.shutdown( );

	}
	
	/**
	 * Filter on derived measure, which only refer to level expression
	 *
	 * @throws Exception
	 */
	@Test
    public void testFilter13( ) throws Exception
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		ILevelDefinition level11 = hier1.createLevel( "level11" );
		ILevelDefinition level12 = hier1.createLevel( "level12" );
		ILevelDefinition level13 = hier1.createLevel( "level13" );
		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		ILevelDefinition level21 = hier2.createLevel( "level21" );

		createSortTestBindings( cqd );

		IBinding binding = new Binding( "derivedBinding" );
		binding.setExpression( new ScriptExpression( "if( data[\"edge1level1\"].equals(\"CN\")) \"profit\"; else \"lose\";" ) );
		cqd.addBinding( binding);

		CubeFilterDefinition filter1 = new CubeFilterDefinition( new ScriptExpression( "data[\"derivedBinding\"] ==\"profit\"" ) );

		filter1.setAxisQualifierLevels( new ILevelDefinition[]{
			level11, level12, level13
		} );
		filter1.setAxisQualifierValues( new Object[]{
			"CN", "SH", "PD"
		} );
		filter1.setTargetLevel( level21 );
		cqd.addFilter( filter1 );
		DataEngineContext context = createPresentationContext( );
		context.setTmpdir( this.getTempDir( ) );
		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( context );
		this.createCube( engine );

		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		columnEdgeBindingNames.add( "edge1level3" );

		printCube( cursor,
				"country_year_total",
				"city_year_total",
				"dist_total",
				"city_total",
				"country_total",
				"rowGrandTotal",
				"grandTotal",
				new String[]{
						"edge1level1", "edge1level2", "edge1level3"
				},
				"edge2level1",
				"measure1" );
		engine.shutdown( );

	}

	/**
	 * Test grand total
	 * 
	 * @throws Exception
	 */
	@Test
    public void testAggrFilter( ) throws Exception
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		ILevelDefinition level11 = hier1.createLevel( "level11" );
		ILevelDefinition level12 = hier1.createLevel( "level12" );
		ILevelDefinition level13 = hier1.createLevel( "level13" );
		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		ILevelDefinition level21 = hier2.createLevel( "level21" );

		createSortTestBindings( cqd );

		// filter on year
		CubeFilterDefinition filter1 = new CubeFilterDefinition( new ScriptExpression( "data[\"rowGrandTotal\"] > 420" ) );
		filter1.setAxisQualifierLevels( null );
		filter1.setAxisQualifierValues( null );
		filter1.setTargetLevel( level21 );

		// sort on country
		CubeFilterDefinition filter2 = new CubeFilterDefinition( new ScriptExpression( "data[\"city_year_total\"] < 65" ) );

		filter2.setAxisQualifierLevels( new ILevelDefinition[]{
			level21
		} );
		filter2.setAxisQualifierValues( new Object[]{
			"2002"
		} );
		filter2.setTargetLevel( level12 );

		CubeFilterDefinition filter3 = new CubeFilterDefinition( new ScriptExpression( "data[\"country_year_total\"] < 300" ) );

		filter3.setAxisQualifierLevels( new ILevelDefinition[]{
			level21
		} );
		filter3.setAxisQualifierValues( new Object[]{
			"2002"
		} );
		filter3.setTargetLevel( level11 );

		CubeSortDefinition sorter4 = new CubeSortDefinition( );
		sorter4.setExpression( "dimension[\"dimension1\"][\"level13\"]" );
		sorter4.setAxisQualifierLevels( null );
		sorter4.setAxisQualifierValues( null );
		sorter4.setTargetLevel( level13 );
		sorter4.setSortDirection( ISortDefinition.SORT_DESC );

		// Make UN before China.
		CubeSortDefinition sorter5 = new CubeSortDefinition( );
		sorter5.setExpression( "dimension[\"dimension1\"][\"level11\"]" );
		sorter5.setAxisQualifierLevels( null );
		sorter5.setAxisQualifierValues( null );
		sorter5.setTargetLevel( level11 );
		sorter5.setSortDirection( ISortDefinition.SORT_DESC );

		/*
		 * //sort on city. SortDefinition sorter3 = new SortDefinition();
		 * sorter3.setExpression( "dimension[\"dimension1\"][\"level12\"]" );
		 * sorter3.setSortDirection( ISortDefinition.SORT_DESC );
		 */
		cqd.addFilter( filter1 );
		cqd.addFilter( filter2 );
		cqd.addFilter( filter3 );
		cqd.addSort( sorter4 );
		cqd.addSort( sorter5 );
		DataEngineContext context = createPresentationContext( );
		context.setTmpdir( this.getTempDir( ) );
		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( context );
		this.createCube( engine );

		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		columnEdgeBindingNames.add( "edge1level3" );

		printCube( cursor,
				"country_year_total",
				"city_year_total",
				"dist_total",
				"city_total",
				"country_total",
				"rowGrandTotal",
				"grandTotal",
				new String[]{
						"edge1level1", "edge1level2", "edge1level3"
				},
				"edge2level1",
				"measure1" );
		engine.shutdown( );
	}
	@Test
    public void testAggrFilter1( ) throws Exception
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		ILevelDefinition level11 = hier1.createLevel( "level11" );
		hier1.createLevel( "level12" );
		hier1.createLevel( "level13" );
		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		ILevelDefinition level21 = hier2.createLevel( "level21" );

		createSortTestBindings( cqd );

		// filter on year
		CubeFilterDefinition filter1 = new CubeFilterDefinition( new ScriptExpression( "data[\"rowGrandTotal\"] > 420" ) );
		filter1.setAxisQualifierLevels( null );
		filter1.setAxisQualifierValues( null );
		filter1.setTargetLevel( level21 );

		// sort on country
		CubeFilterDefinition filter2 = new CubeFilterDefinition( new ScriptExpression( "data[\"country_year_total\"] != 120" ) );

		filter2.setAxisQualifierLevels( new ILevelDefinition[]{
			level21
		} );
		filter2.setAxisQualifierValues( new Object[]{
			"2002"
		} );
		filter2.setTargetLevel( level11 );

		CubeFilterDefinition filter3 = new CubeFilterDefinition( new ConditionalExpression( "data[\"country_year_total\"]",
				IConditionalExpression.OP_TOP_PERCENT,
				"50" ) );

		filter3.setAxisQualifierLevels( new ILevelDefinition[]{
			level21
		} );
		filter3.setAxisQualifierValues( new Object[]{
			"2002"
		} );
		filter3.setTargetLevel( level11 );

		// sort on city.

		cqd.addFilter( filter1 );
		cqd.addFilter( filter2 );
		cqd.addFilter( filter3 );

		DataEngineContext context = createPresentationContext( );
		context.setTmpdir( this.getTempDir( ) );
		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( context );
		this.createCube( engine );

		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		columnEdgeBindingNames.add( "edge1level3" );

		printCube( cursor,
				"country_year_total",
				"city_year_total",
				"dist_total",
				"city_total",
				"country_total",
				"rowGrandTotal",
				"grandTotal",
				new String[]{
						"edge1level1", "edge1level2", "edge1level3"
				},
				"edge2level1",
				"measure1" );
		engine.shutdown( );
	}

	/**
	 * top/bottom dimension filter.
	 * 
	 * @throws Exception
	 */
	@Test
    public void testAggrFilter2( ) throws Exception
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		hier1.createLevel( "level11" );
		ILevelDefinition level12 = hier1.createLevel( "level12" );
		hier1.createLevel( "level13" );
		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		ILevelDefinition level21 = hier2.createLevel( "level21" );

		createSortTestBindings( cqd );

		CubeFilterDefinition filter1 = new CubeFilterDefinition( new ConditionalExpression( "dimension[\"dimension2\"][\"level21\"]",
				IConditionalExpression.OP_BOTTOM_N,
				"3" ) );
		filter1.setTargetLevel( level21 );
		CubeFilterDefinition filter2 = new CubeFilterDefinition( new ConditionalExpression( "dimension[\"dimension1\"][\"level12\"]",
				IConditionalExpression.OP_TOP_N,
				"2" ) );
		filter2.setTargetLevel( level12 );
		cqd.addFilter( filter1 );
		cqd.addFilter( filter2 );

		DataEngineContext context = createPresentationContext( );
		context.setTmpdir( this.getTempDir( ) );
		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( context );
		this.createCube( engine );

		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		columnEdgeBindingNames.add( "edge1level3" );

		printCube( cursor,
				"country_year_total",
				"city_year_total",
				"dist_total",
				"city_total",
				"country_total",
				"rowGrandTotal",
				"grandTotal",
				new String[]{
						"edge1level1", "edge1level2", "edge1level3"
				},
				"edge2level1",
				"measure1" );
		engine.shutdown( );
	}

	/**
	 * @throws Exception
	 */
	@Test
    public void testMeasureFilter( ) throws Exception
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		ILevelDefinition level11 = hier1.createLevel( "level11" );
		ILevelDefinition level12 = hier1.createLevel( "level12" );
		ILevelDefinition level13 = hier1.createLevel( "level13" );
		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		ILevelDefinition level21 = hier2.createLevel( "level21" );

		createSortTestBindings( cqd );

		// filter on city_yeal_total
		CubeFilterDefinition filter1 = new CubeFilterDefinition( new ScriptExpression( "data[\"city_year_total\"] > 30" ) );
		
		// filter on country_year_total
		CubeFilterDefinition filter2 = new CubeFilterDefinition( new ScriptExpression( "data[\"country_year_total\"] > 100" ) );
		
		cqd.addFilter( filter1 );
		cqd.addFilter( filter2 );

		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );

		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		columnEdgeBindingNames.add( "edge1level3" );

		printCube( cursor,
				"country_year_total",
				"city_year_total",
				"dist_total",
				"city_total",
				"country_total",
				"rowGrandTotal",
				"grandTotal",
				new String[]{
						"edge1level1", "edge1level2", "edge1level3"
				},
				"edge2level1",
				"measure1" );
		engine.shutdown( );
	}

	/**
	 * Top N measure filter
	 * @throws Exception
	 */
	@Test
    public void testMeasureFilter1( ) throws Exception
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		ILevelDefinition level11 = hier1.createLevel( "level11" );
		ILevelDefinition level12 = hier1.createLevel( "level12" );
		ILevelDefinition level13 = hier1.createLevel( "level13" );
		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		ILevelDefinition level21 = hier2.createLevel( "level21" );

		createSortTestBindings( cqd );

		
		CubeFilterDefinition filter1 = new CubeFilterDefinition( new ConditionalExpression( "data[\"measure1\"]",
				IConditionalExpression.OP_BOTTOM_N,
				"10" ) );
		
		// filter on country_year_total
		CubeFilterDefinition filter2 = new CubeFilterDefinition( new ConditionalExpression( "data[\"rowGrandTotal\"]",
				IConditionalExpression.OP_TOP_PERCENT,
				"20" ) );

		cqd.addFilter( filter1 );
		cqd.addFilter( filter2 );

		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );

		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		columnEdgeBindingNames.add( "edge1level3" );

		printCube( cursor,
				"country_year_total",
				"city_year_total",
				"dist_total",
				"city_total",
				"country_total",
				"rowGrandTotal",
				"grandTotal",
				new String[]{
						"edge1level1", "edge1level2", "edge1level3"
				},
				"edge2level1",
				"measure1" );
		engine.shutdown( );
	}

	/**
	 * Simple sort on 1 level
	 * 
	 * @throws Exception
	 */
	@Test
    public void testSort1( ) throws Exception
	{
		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		hier1.createLevel( "level11" );
		hier1.createLevel( "level12" );

		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		ILevelDefinition level21 = hier2.createLevel( "level21" );

		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		IBinding binding1 = new Binding( "edge1level1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "edge1level2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding4 = new Binding( "edge2level1" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "measure1" );
		binding5.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		cqd.addBinding( binding5 );

		CubeSortDefinition sorter = new CubeSortDefinition( );
		sorter.setExpression( "dimension[\"dimension2\"][\"level21\"]" );
		sorter.setSortDirection( ISortDefinition.SORT_DESC );
		sorter.setTargetLevel( level21 );
		cqd.addSort( sorter );

		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "edge2level1" );

		this.printCube( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				"measure1" );
		
		engine.shutdown( );

	}

	/**
	 * Complex sort on multiple levels
	 * 
	 * @throws Exception
	 */
	@Test
    public void testSort2( ) throws Exception
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		ILevelDefinition level11 = hier1.createLevel( "level11" );
		ILevelDefinition level12 = hier1.createLevel( "level12" );

		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		ILevelDefinition level21 = hier2.createLevel( "level21" );

		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		IBinding binding1 = new Binding( "edge1level1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "edge1level2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding4 = new Binding( "edge2level1" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "measure1" );
		binding5.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		cqd.addBinding( binding5 );

		// sort on year
		CubeSortDefinition sorter1 = new CubeSortDefinition( );
		sorter1.setExpression( "dimension[\"dimension2\"][\"level21\"]" );
		sorter1.setSortDirection( ISortDefinition.SORT_DESC );
		sorter1.setTargetLevel( level21 );

		// sort on country
		CubeSortDefinition sorter2 = new CubeSortDefinition( );
		sorter2.setExpression( "dimension[\"dimension1\"][\"level11\"]" );
		sorter2.setSortDirection( ISortDefinition.SORT_DESC );
		sorter2.setTargetLevel( level11 );
		// sort on city.
		CubeSortDefinition sorter3 = new CubeSortDefinition( );
		sorter3.setExpression( "dimension[\"dimension1\"][\"level12\"]" );
		sorter3.setSortDirection( ISortDefinition.SORT_DESC );
		sorter3.setTargetLevel( level12 );
		cqd.addSort( sorter1 );
		cqd.addSort( sorter2 );
		cqd.addSort( sorter3 );
		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "edge2level1" );

		this.printCube( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				"measure1" );
		engine.shutdown( );
	}

	/**
	 * Complex sort on multiple levels
	 * 
	 * @throws Exception
	 */
	@Test
    public void testSort3( ) throws Exception
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		ILevelDefinition level11 = hier1.createLevel( "level11" );
		ILevelDefinition level12 = hier1.createLevel( "level12" );

		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		ILevelDefinition level21 = hier2.createLevel( "level21" );

		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		IBinding binding1 = new Binding( "edge1level1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "edge1level2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding4 = new Binding( "edge2level1" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "measure1" );
		binding5.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		cqd.addBinding( binding5 );

		// sort on year
		CubeSortDefinition sorter1 = new CubeSortDefinition( );
		sorter1.setExpression( "data[\"edge2level1\"]" );
		sorter1.setSortDirection( ISortDefinition.SORT_DESC );
		sorter1.setTargetLevel( level21 );

		// sort on country
		CubeSortDefinition sorter2 = new CubeSortDefinition( );
		sorter2.setExpression( "data[\"edge1level1\"]" );
		sorter2.setSortDirection( ISortDefinition.SORT_DESC );
		sorter2.setTargetLevel( level11 );

		// sort on city.
		CubeSortDefinition sorter3 = new CubeSortDefinition( );
		sorter3.setExpression( "data[\"edge1level2\"]" );
		sorter3.setSortDirection( ISortDefinition.SORT_DESC );
		sorter3.setTargetLevel( level12 );

		cqd.addSort( sorter1 );
		cqd.addSort( sorter2 );
		cqd.addSort( sorter3 );
		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "edge2level1" );

		this.printCube( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				"measure1" );
		engine.shutdown( );
	}

	
	/**
	 * Filter on derived measure
	 * 
	 * @throws Exception
	 */
	@Test
    public void testSort4( ) throws Exception
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		ILevelDefinition level11 = hier1.createLevel( "level11" );
		ILevelDefinition level12 = hier1.createLevel( "level12" );
		ILevelDefinition level13 = hier1.createLevel( "level13" );
		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		ILevelDefinition level21 = hier2.createLevel( "level21" );

		createSortTestBindings( cqd );

		CubeSortDefinition sorter4 = new CubeSortDefinition( );
		sorter4.setExpression( "data[\"derived2\"]" );
		sorter4.setSortDirection( 0 );
		sorter4.setTargetLevel( level12 );
		cqd.addSort( sorter4 );
		
		DataEngineContext context = createPresentationContext( );
		context.setTmpdir( this.getTempDir( ) );
		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( context );
		this.createCube( engine );

		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		columnEdgeBindingNames.add( "edge1level3" );

		printCube( cursor,
				"country_year_total",
				"city_year_total",
				"dist_total",
				"city_total",
				"country_total",
				"rowGrandTotal",
				"grandTotal",
				new String[]{
						"edge1level1", "edge1level2", "edge1level3"
				},
				"edge2level1",
				"measure1" );
		engine.shutdown( );

	}
	/**
	 * expression sort on 2 level: one with expression sort and the other with
	 * traditional sort.
	 * 
	 * @throws Exception
	 */
	@Test
    public void testSortWithExpr( ) throws Exception
	{
		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		ILevelDefinition level11 = hier1.createLevel( "level11" );
		ILevelDefinition level12 = hier1.createLevel( "level12" );

		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		ILevelDefinition level21 = hier2.createLevel( "level21" );

		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		IBinding binding1 = new Binding( "edge1level1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "edge1level2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding4 = new Binding( "edge2level1" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "measure1" );
		binding5.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		cqd.addBinding( binding5 );

		CubeSortDefinition sorter = new CubeSortDefinition( );
		sorter.setExpression( "dimension[\"dimension2\"][\"level21\"]-1" );
		sorter.setSortDirection( ISortDefinition.SORT_DESC );
		sorter.setTargetLevel( level21 );
		cqd.addSort( sorter );

		sorter = new CubeSortDefinition( );
		sorter.setExpression( "dimension[\"dimension1\"][\"level12\"]+\"T\"" );
		sorter.setSortDirection( ISortDefinition.SORT_DESC );
		sorter.setTargetLevel( level12 );
		cqd.addSort( sorter );

		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "edge2level1" );

		this.printCube( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				"measure1" );
		engine.shutdown( );
	}

	/**
	 * expression sort on attribute
	 */
	@Test
    public void testSortWithExpr1( ) throws Exception
	{
		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		ILevelDefinition level11 = hier1.createLevel( "level11" );
		ILevelDefinition level12 = hier1.createLevel( "level12" );

		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		ILevelDefinition level21 = hier2.createLevel( "level21" );

		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		IBinding binding1 = new Binding( "edge1level1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "edge1level2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding4 = new Binding( "edge2level1" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "measure1" );
		binding5.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		cqd.addBinding( binding5 );

		CubeSortDefinition sorter = new CubeSortDefinition( );
		sorter.setExpression( "dimension[\"dimension2\"][\"level21\"][\"attr21\"]" );
		sorter.setSortDirection( ISortDefinition.SORT_DESC );
		sorter.setTargetLevel( level21 );
		cqd.addSort( sorter );

		sorter = new CubeSortDefinition( );
		sorter.setExpression( "dimension[\"dimension2\"][\"level21\"]+1" );
		sorter.setSortDirection( ISortDefinition.SORT_ASC );
		sorter.setTargetLevel( level21 );
		cqd.addSort( sorter );

		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "edge2level1" );

		this.printCube( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				"measure1" );
		engine.shutdown( );
	}

	/**
	 * Test grand total
	 * 
	 * @throws Exception
	 */
	@Test
    public void testSortWithExpr2( ) throws Exception
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		ILevelDefinition level11 = hier1.createLevel( "level11" );
		ILevelDefinition level12 = hier1.createLevel( "level12" );
		ILevelDefinition level13 = hier1.createLevel( "level13" );
		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		ILevelDefinition level21 = hier2.createLevel( "level21" );

		createSortTestBindings( cqd );

		// sort on country year 2002
		CubeSortDefinition sorter1 = new CubeSortDefinition( );
		sorter1.setExpression( "data[\"country_year_total\"]" );
		sorter1.setAxisQualifierLevels( new ILevelDefinition[]{
			level21
		} );
		sorter1.setAxisQualifierValues( new Object[]{
			"2002"
		} );
		sorter1.setTargetLevel( level11 );
		sorter1.setSortDirection( ISortDefinition.SORT_ASC );

		CubeSortDefinition sorter2 = new CubeSortDefinition( );
		sorter2.setExpression( "dimension[\"dimension2\"][\"level21\"]-1" );
		sorter2.setTargetLevel( level21 );
		sorter2.setSortDirection( ISortDefinition.SORT_DESC );

		CubeSortDefinition sorter3 = new CubeSortDefinition( );
		sorter3.setExpression( "dimension[\"dimension1\"][\"level11\"]+1" );
		sorter3.setTargetLevel( level11 );
		sorter3.setSortDirection( ISortDefinition.SORT_DESC );

		cqd.addSort( sorter1 );
		cqd.addSort( sorter2 );
		cqd.addSort( sorter3 );

		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );

		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		columnEdgeBindingNames.add( "edge1level3" );

		printCube( cursor,
				"country_year_total",
				"city_year_total",
				"dist_total",
				"city_total",
				"country_total",
				"rowGrandTotal",
				"grandTotal",
				new String[]{
						"edge1level1", "edge1level2", "edge1level3"
				},
				"edge2level1",
				"measure1" );
		engine.shutdown( );
	}
	@Test
    public void testSortWithExpr3( ) throws Exception
	{
		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		ILevelDefinition level11 = hier1.createLevel( "level11" );
		ILevelDefinition level12 = hier1.createLevel( "level12" );

		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		ILevelDefinition level21 = hier2.createLevel( "level21" );

		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		IBinding binding1 = new Binding( "edge1level1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "edge1level2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding4 = new Binding( "edge2level1" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"]" ) );
		cqd.addBinding( binding4 );
		
		IBinding binding5 = new Binding( "level21attr21" );

		binding5.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"][\"attr21\"]" ) );
		cqd.addBinding( binding5 );

		IBinding binding6 = new Binding( "measure1" );
		binding6.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		cqd.addBinding( binding6 );

		CubeSortDefinition sorter = new CubeSortDefinition( );
		sorter.setExpression( "data[\"level21attr21\"]" );
		sorter.setSortDirection( ISortDefinition.SORT_DESC );
		sorter.setTargetLevel( level21 );
		cqd.addSort( sorter );

		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "edge2level1" );

		this.printCube( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				"measure1" );
		engine.shutdown( );
	}
	/**
	 * Test grand total
	 * 
	 * @throws Exception
	 */
	@Test
    public void testGrandTotal( ) throws Exception
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		ILevelDefinition level11 = hier1.createLevel( "level11" );
		ILevelDefinition level12 = hier1.createLevel( "level12" );

		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		ILevelDefinition level21 = hier2.createLevel( "level21" );

		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		IBinding binding1 = new Binding( "edge1level1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "edge1level2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding4 = new Binding( "edge2level1" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "measure1" );
		binding5.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		cqd.addBinding( binding5 );

		IBinding binding6 = new Binding( "rowGrandTotal" );
		binding6.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		binding6.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		binding6.addAggregateOn( "dimension[\"dimension2\"][\"level21\"]" );
		cqd.addBinding( binding6 );

		IBinding binding7 = new Binding( "columnGrandTotal" );
		binding7.addArgument( new ScriptExpression( "measure[\"measure1\"]" ) );
		binding7.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		binding7.addAggregateOn( "dimension[\"dimension1\"][\"level11\"]" );
		binding7.addAggregateOn( "dimension[\"dimension1\"][\"level12\"]" );
		cqd.addBinding( binding7 );

		IBinding binding8 = new Binding( "grandTotal" );
		binding8.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		binding8.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		cqd.addBinding( binding8 );

		// sort on year
		CubeSortDefinition sorter1 = new CubeSortDefinition( );
		sorter1.setExpression( "dimension[\"dimension2\"][\"level21\"]" );
		sorter1.setSortDirection( ISortDefinition.SORT_DESC );
		sorter1.setTargetLevel( level21 );

		// sort on country
		CubeSortDefinition sorter2 = new CubeSortDefinition( );
		sorter2.setExpression( "dimension[\"dimension1\"][\"level11\"]" );
		sorter2.setSortDirection( ISortDefinition.SORT_DESC );
		sorter2.setTargetLevel( level11 );

		// sort on city.
		CubeSortDefinition sorter3 = new CubeSortDefinition( );
		sorter3.setExpression( "dimension[\"dimension1\"][\"level12\"]" );
		sorter3.setSortDirection( ISortDefinition.SORT_DESC );
		sorter3.setTargetLevel( level12 );

		cqd.addSort( sorter1 );
		cqd.addSort( sorter2 );
		cqd.addSort( sorter3 );
		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		
		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "edge2level1" );

		this.printCube( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				"measure1",
				"columnGrandTotal",
				"rowGrandTotal",
				"grandTotal" );
		
		engine.shutdown( );

	}
	
	/**
	 * Test cube query without edges.
	 * In this case, only grand total bindings are allowed
	 * 
	 * @throws Exception
	 */
	@Test
    public void testQueryWithoutEdge( ) throws Exception
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );

		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		IBinding binding = new Binding( "grandTotal" );
		binding.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		binding.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		cqd.addBinding( binding );

		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		Object o = cursor.getObject( "grandTotal" );
		assertEquals( "2146.0", o.toString( ));
		cursor.close( );
		
		engine.shutdown( );

	}

	/**
	 * Test grand total
	 * 
	 * @throws Exception
	 */
	@Test
    public void testGrandTotal1( ) throws Exception
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		ILevelDefinition level11 = hier1.createLevel( "level11" );
		ILevelDefinition level12 = hier1.createLevel( "level12" );

		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		ILevelDefinition level21 = hier2.createLevel( "level21" );

		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		IBinding binding1 = new Binding( "edge1level1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "edge1level2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding4 = new Binding( "edge2level1" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "measure1" );
		binding5.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		cqd.addBinding( binding5 );

		IBinding binding6 = new Binding( "rowGrandTotal" );
		binding6.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		binding6.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		binding6.addAggregateOn( "dimension[\"dimension2\"][\"level21\"]" );
		binding6.setFilter( new ScriptExpression( "measure[\"measure1\"] > 60" ) );
		cqd.addBinding( binding6 );

		IBinding binding7 = new Binding( "columnGrandTotal" );
		binding7.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		binding7.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		binding7.addAggregateOn( "dimension[\"dimension1\"][\"level11\"]" );
		binding7.addAggregateOn( "dimension[\"dimension1\"][\"level12\"]" );
		binding7.setFilter( new ScriptExpression( "measure[\"measure1\"] > 60" ) );
		cqd.addBinding( binding7 );

		IBinding binding8 = new Binding( "grandTotal" );
		binding8.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		binding8.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		binding8.setFilter( new ScriptExpression( "measure[\"measure1\"] > 60" ) );
		cqd.addBinding( binding8 );

		// sort on year
		CubeSortDefinition sorter1 = new CubeSortDefinition( );
		sorter1.setExpression( "dimension[\"dimension2\"][\"level21\"]" );
		sorter1.setSortDirection( ISortDefinition.SORT_DESC );
		sorter1.setTargetLevel( level21 );

		// sort on country
		CubeSortDefinition sorter2 = new CubeSortDefinition( );
		sorter2.setExpression( "dimension[\"dimension1\"][\"level11\"]" );
		sorter2.setSortDirection( ISortDefinition.SORT_DESC );
		sorter2.setTargetLevel( level11 );

		// sort on city.
		CubeSortDefinition sorter3 = new CubeSortDefinition( );
		sorter3.setExpression( "dimension[\"dimension1\"][\"level12\"]" );
		sorter3.setSortDirection( ISortDefinition.SORT_DESC );
		sorter3.setTargetLevel( level12 );

		cqd.addSort( sorter1 );
		cqd.addSort( sorter2 );
		cqd.addSort( sorter3 );
		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );

		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "edge2level1" );

		this.printCube( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				"measure1",
				"columnGrandTotal",
				"rowGrandTotal",
				"grandTotal" );
		
		engine.shutdown( );

	}

	/**
	 * Test grand total
	 * 
	 * @throws Exception
	 */
	@Test
    public void testAggrSort( ) throws Exception
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		ILevelDefinition level11 = hier1.createLevel( "level11" );
		ILevelDefinition level12 = hier1.createLevel( "level12" );
		ILevelDefinition level13 = hier1.createLevel( "level13" );
		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		ILevelDefinition level21 = hier2.createLevel( "level21" );

		createSortTestBindings( cqd );

		// sort on year
		CubeSortDefinition sorter1 = new CubeSortDefinition( );
		sorter1.setExpression( "data[\"rowGrandTotal\"]" );
		sorter1.setAxisQualifierLevels( null );
		sorter1.setAxisQualifierValues( null );
		sorter1.setTargetLevel( level21 );
		sorter1.setSortDirection( ISortDefinition.SORT_DESC );

		// sort on country
		CubeSortDefinition sorter2 = new CubeSortDefinition( );
		sorter2.setExpression( "data[\"city_year_total\"]" );
		sorter2.setAxisQualifierLevels( new ILevelDefinition[]{
			level21
		} );
		sorter2.setAxisQualifierValues( new Object[]{
			"2002"
		} );
		sorter2.setTargetLevel( level12 );
		sorter2.setSortDirection( ISortDefinition.SORT_DESC );

		CubeSortDefinition sorter3 = new CubeSortDefinition( );
		sorter3.setExpression( "data[\"country_year_total\"]" );
		sorter3.setAxisQualifierLevels( new ILevelDefinition[]{
			level21
		} );
		sorter3.setAxisQualifierValues( new Object[]{
			"2002"
		} );
		sorter3.setTargetLevel( level11 );
		sorter3.setSortDirection( ISortDefinition.SORT_DESC );

		CubeSortDefinition sorter4 = new CubeSortDefinition( );
		sorter4.setExpression( "dimension[\"dimension1\"][\"level13\"]" );
		sorter4.setAxisQualifierLevels( null );
		sorter4.setAxisQualifierValues( null );
		sorter4.setTargetLevel( level13 );
		sorter4.setSortDirection( ISortDefinition.SORT_DESC );

		// Make UN before China.
		CubeSortDefinition sorter5 = new CubeSortDefinition( );
		sorter5.setExpression( "dimension[\"dimension1\"][\"level11\"]" );
		sorter5.setAxisQualifierLevels( null );
		sorter5.setAxisQualifierValues( null );
		sorter5.setTargetLevel( level11 );
		sorter5.setSortDirection( ISortDefinition.SORT_DESC );

		/*
		 * //sort on city. SortDefinition sorter3 = new SortDefinition();
		 * sorter3.setExpression( "dimension[\"dimension1\"][\"level12\"]" );
		 * sorter3.setSortDirection( ISortDefinition.SORT_DESC );
		 */
		cqd.addSort( sorter1 );
		cqd.addSort( sorter2 );
		cqd.addSort( sorter3 );
		cqd.addSort( sorter4 );
		cqd.addSort( sorter5 );
		DataEngineContext context = createPresentationContext( );
		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( context );
		this.createCube( engine );

		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		columnEdgeBindingNames.add( "edge1level3" );

		printCube( cursor,
				"country_year_total",
				"city_year_total",
				"dist_total",
				"city_total",
				"country_total",
				"rowGrandTotal",
				"grandTotal",
				new String[]{
						"edge1level1", "edge1level2", "edge1level3"
				},
				"edge2level1",
				"measure1" );
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

	/**
	 * Test grand total
	 * 
	 * @throws Exception
	 */
	@Test
    public void testAggrSort1( ) throws Exception
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
		ILevelDefinition level21 = hier2.createLevel( "level21" );

		createSortTestBindings( cqd );

		// sort on year
		CubeSortDefinition sorter1 = new CubeSortDefinition( );
		sorter1.setExpression( "data[\"rowGrandTotal\"]" );
		sorter1.setAxisQualifierLevels( null );
		sorter1.setAxisQualifierValues( null );
		sorter1.setTargetLevel( level21 );
		sorter1.setSortDirection( ISortDefinition.SORT_DESC );

		cqd.addSort( sorter1 );
		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		columnEdgeBindingNames.add( "edge1level3" );

		printCube( cursor,
				"country_year_total",
				"city_year_total",
				"dist_total",
				"city_total",
				"country_total",
				"rowGrandTotal",
				"grandTotal",
				new String[]{
						"edge1level1", "edge1level2", "edge1level3"
				},
				"edge2level1",
				"measure1" );
		engine.shutdown( );
	}

	/**
	 * Test grand total
	 * 
	 * @throws Exception
	 */
	@Test
    public void testAggrSort2( ) throws Exception
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		hier1.createLevel( "level11" );
		ILevelDefinition level12 = hier1.createLevel( "level12" );
		hier1.createLevel( "level13" );
		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		ILevelDefinition level21 = hier2.createLevel( "level21" );

		createSortTestBindings( cqd );

		// sort on year
		CubeSortDefinition sorter1 = new CubeSortDefinition( );
		sorter1.setExpression( "data[\"rowGrandTotal\"]" );
		sorter1.setAxisQualifierLevels( null );
		sorter1.setAxisQualifierValues( null );
		sorter1.setTargetLevel( level21 );
		sorter1.setSortDirection( ISortDefinition.SORT_DESC );

		// sort on country
		CubeSortDefinition sorter2 = new CubeSortDefinition( );
		sorter2.setExpression( "data[\"city_year_total\"]" );
		sorter2.setAxisQualifierLevels( new ILevelDefinition[]{
			level21
		} );
		sorter2.setAxisQualifierValues( new Object[]{
			"2002"
		} );
		sorter2.setTargetLevel( level12 );
		sorter2.setSortDirection( ISortDefinition.SORT_DESC );

		cqd.addSort( sorter1 );
		cqd.addSort( sorter2 );
		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		columnEdgeBindingNames.add( "edge1level3" );

		printCube( cursor,
				"country_year_total",
				"city_year_total",
				"dist_total",
				"city_total",
				"country_total",
				"rowGrandTotal",
				"grandTotal",
				new String[]{
						"edge1level1", "edge1level2", "edge1level3"
				},
				"edge2level1",
				"measure1" );
		engine.shutdown( );
	}

	/**
	 * Test grand total
	 * 
	 * @throws Exception
	 */
	@Test
    public void testAggrSort3( ) throws Exception
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		ILevelDefinition level11 = hier1.createLevel( "level11" );
		ILevelDefinition level12 = hier1.createLevel( "level12" );
		hier1.createLevel( "level13" );
		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		ILevelDefinition level21 = hier2.createLevel( "level21" );

		createSortTestBindings( cqd );

		// sort on year
		CubeSortDefinition sorter1 = new CubeSortDefinition( );
		sorter1.setExpression( "data[\"rowGrandTotal\"]" );
		sorter1.setAxisQualifierLevels( null );
		sorter1.setAxisQualifierValues( null );
		sorter1.setTargetLevel( level21 );
		sorter1.setSortDirection( ISortDefinition.SORT_DESC );

		// sort on country
		CubeSortDefinition sorter2 = new CubeSortDefinition( );
		sorter2.setExpression( "data[\"city_year_total\"]" );
		sorter2.setAxisQualifierLevels( new ILevelDefinition[]{
			level21
		} );
		sorter2.setAxisQualifierValues( new Object[]{
			"2002"
		} );
		sorter2.setTargetLevel( level12 );
		sorter2.setSortDirection( ISortDefinition.SORT_DESC );

		CubeSortDefinition sorter3 = new CubeSortDefinition( );
		sorter3.setExpression( "data[\"country_year_total\"]" );
		sorter3.setAxisQualifierLevels( new ILevelDefinition[]{
			level21
		} );
		sorter3.setAxisQualifierValues( new Object[]{
			"2002"
		} );
		sorter3.setTargetLevel( level11 );
		sorter3.setSortDirection( ISortDefinition.SORT_DESC );

		cqd.addSort( sorter1 );
		cqd.addSort( sorter2 );
		cqd.addSort( sorter3 );
		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		columnEdgeBindingNames.add( "edge1level3" );

		printCube( cursor,
				"country_year_total",
				"city_year_total",
				"dist_total",
				"city_total",
				"country_total",
				"rowGrandTotal",
				"grandTotal",
				new String[]{
						"edge1level1", "edge1level2", "edge1level3"
				},
				"edge2level1",
				"measure1" );
		engine.shutdown( );
	}

	/**
	 * Test grand total
	 * 
	 * @throws Exception
	 */
	@Test
    public void testAggrSort5( ) throws Exception
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		hier1.createLevel( "level11" );
		hier1.createLevel( "level12" );
		ILevelDefinition level13 = hier1.createLevel( "level13" );
		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		ILevelDefinition level21 = hier2.createLevel( "level21" );

		createSortTestBindings( cqd );

		// sort on year
		CubeSortDefinition sorter1 = new CubeSortDefinition( );
		sorter1.setExpression( "data[\"measure1\"]" );
		sorter1.setAxisQualifierLevels( new ILevelDefinition[]{
			level21
		} );
		sorter1.setAxisQualifierValues( new Object[]{
			"1998"
		} );
		sorter1.setTargetLevel( level13 );
		sorter1.setSortDirection( ISortDefinition.SORT_DESC );

		cqd.addSort( sorter1 );
		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		columnEdgeBindingNames.add( "edge1level3" );

		printCube( cursor,
				"country_year_total",
				"city_year_total",
				"dist_total",
				"city_total",
				"country_total",
				"rowGrandTotal",
				"grandTotal",
				new String[]{
						"edge1level1", "edge1level2", "edge1level3"
				},
				"edge2level1",
				"measure1" );
		engine.shutdown( );
	}

	/**
	 * Test grand total
	 * 
	 * @throws Exception
	 */
	@Test
    public void testAggrSort6( ) throws Exception
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		ILevelDefinition level11 = hier1.createLevel( "level11" );
		hier1.createLevel( "level12" );
		hier1.createLevel( "level13" );
		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		ILevelDefinition level21 = hier2.createLevel( "level21" );

		createSortTestBindings( cqd );

		// sort on year
		CubeSortDefinition sorter1 = new CubeSortDefinition( );
		sorter1.setExpression( "data[\"rowGrandTotal\"]" );
		sorter1.setAxisQualifierLevels( null );
		sorter1.setAxisQualifierValues( null );
		sorter1.setTargetLevel( level21 );
		sorter1.setSortDirection( ISortDefinition.SORT_DESC );

		// sort on country year 2002
		CubeSortDefinition sorter2 = new CubeSortDefinition( );
		sorter2.setExpression( "data[\"country_year_total\"]" );
		sorter2.setAxisQualifierLevels( new ILevelDefinition[]{
			level21
		} );
		sorter2.setAxisQualifierValues( new Object[]{
			"2002"
		} );
		sorter2.setTargetLevel( level11 );
		sorter2.setSortDirection( ISortDefinition.SORT_ASC );

		// sort on country year 2002
		CubeSortDefinition sorter3 = new CubeSortDefinition( );
		sorter3.setExpression( "data[\"country_year_total\"]" );
		sorter3.setAxisQualifierLevels( new ILevelDefinition[]{
			level21
		} );
		sorter3.setAxisQualifierValues( new Object[]{
			"2001"
		} );
		sorter3.setTargetLevel( level11 );
		sorter3.setSortDirection( ISortDefinition.SORT_ASC );

		cqd.addSort( sorter1 );
		cqd.addSort( sorter2 );
		cqd.addSort( sorter3 );
		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );

		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		columnEdgeBindingNames.add( "edge1level3" );

		printCube( cursor,
				"country_year_total",
				"city_year_total",
				"dist_total",
				"city_total",
				"country_total",
				"rowGrandTotal",
				"grandTotal",
				new String[]{
						"edge1level1", "edge1level2", "edge1level3"
				},
				"edge2level1",
				"measure1" );
		engine.shutdown( );
	}

	/**
	 * Test binding "row" reference
	 * 
	 * @throws Exception
	 */
	@Test
    public void testBindingRowReference( ) throws Exception
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		ILevelDefinition level11 = hier1.createLevel( "level11" );
		ILevelDefinition level12 = hier1.createLevel( "level12" );

		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		ILevelDefinition level21 = hier2.createLevel( "level21" );

		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		IBinding binding1 = new Binding( "edge1level1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "edge1level2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding4 = new Binding( "edge2level1" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "measure1" );
		binding5.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		cqd.addBinding( binding5 );

		IBinding binding6 = new Binding( "rowGrandTotal" );
		binding6.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		binding6.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		binding6.addAggregateOn( "dimension[\"dimension2\"][\"level21\"]" );
		cqd.addBinding( binding6 );

		IBinding binding7 = new Binding( "columnGrandTotal" );
		binding7.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		binding7.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		binding7.addAggregateOn( "dimension[\"dimension1\"][\"level11\"]" );
		binding7.addAggregateOn( "dimension[\"dimension1\"][\"level12\"]" );
		cqd.addBinding( binding7 );

		IBinding binding8 = new Binding( "grandTotal" );
		binding8.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		binding8.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		cqd.addBinding( binding8 );

		IBinding binding9 = new Binding( "row_rowGrandTotal" );
		binding9.setExpression( new ScriptExpression( "data[\"rowGrandTotal\"]*10" ) );
		cqd.addBinding( binding9 );

		IBinding binding10 = new Binding( "row_columnGrandTotal" );
		binding10.setExpression( new ScriptExpression( "data[\"columnGrandTotal\"]*10" ) );
		cqd.addBinding( binding10 );

		IBinding binding11 = new Binding( "row_grandTotal" );
		binding11.setExpression( new ScriptExpression( "data[\"grandTotal\"]*10" ) );
		cqd.addBinding( binding11 );

		IBinding binding12 = new Binding( "row_measure1" );
		binding12.setExpression( new ScriptExpression( "data[\"measure1\"]*10" ) );
		cqd.addBinding( binding12 );

		// sort on year
		CubeSortDefinition sorter1 = new CubeSortDefinition( );
		sorter1.setExpression( "dimension[\"dimension2\"][\"level21\"]" );
		sorter1.setSortDirection( ISortDefinition.SORT_DESC );
		sorter1.setTargetLevel( level21 );

		// sort on country
		CubeSortDefinition sorter2 = new CubeSortDefinition( );
		sorter2.setExpression( "dimension[\"dimension1\"][\"level11\"]" );
		sorter2.setSortDirection( ISortDefinition.SORT_DESC );
		sorter2.setTargetLevel( level11 );

		// sort on city.
		CubeSortDefinition sorter3 = new CubeSortDefinition( );
		sorter3.setExpression( "dimension[\"dimension1\"][\"level12\"]" );
		sorter3.setSortDirection( ISortDefinition.SORT_DESC );
		sorter3.setTargetLevel( level12 );

		cqd.addSort( sorter1 );
		cqd.addSort( sorter2 );
		cqd.addSort( sorter3 );
		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );

		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );

		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "edge2level1" );

		this.printCube( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				"row_measure1",
				"row_columnGrandTotal",
				"row_rowGrandTotal",
				"row_grandTotal" );
		
		engine.shutdown( );

	}

	/**
	 * Test computed measure.
	 * 
	 * @throws Exception
	 */
	@Test
    public void testComputedMeasure( ) throws Exception
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

		cqd.createComputedMeasure( "measure2",
				DataType.DOUBLE_TYPE,
				new ScriptExpression( "measure[\"measure1\"] + measure[\"measure1\"]" ) );

		IBinding binding1 = new Binding( "edge1level1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "edge1level2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding3 = new Binding( "edge1level3" );

		binding3.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level13\"]" ) );
		cqd.addBinding( binding3 );

		IBinding binding4 = new Binding( "edge2level1" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "measure2" );
		binding5.setExpression( new ScriptExpression( "measure[\"measure2\"]" ) );
		binding5.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		binding5.addAggregateOn( "dimension[\"dimension1\"][\"level11\"]" );
		binding5.addAggregateOn( "dimension[\"dimension1\"][\"level12\"]" );
		binding5.addAggregateOn( "dimension[\"dimension1\"][\"level13\"]" );
		binding5.addAggregateOn( "dimension[\"dimension2\"][\"level21\"]" );
		cqd.addBinding( binding5 );

		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		columnEdgeBindingNames.add( "edge1level3" );
		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "edge2level1" );

		this.printCube( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				"measure2" );
		engine.shutdown( );
	}

	/**
	 * Test use Nested Computed Measure.
	 * 
	 * @throws Exception
	 */
	@Test
    public void testNestedComputedMeasure( ) throws Exception
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

		cqd.createComputedMeasure( "measure2",
				DataType.DOUBLE_TYPE,
				new ScriptExpression( "measure[\"measure1\"] + measure[\"measure3\"]" ) );
		cqd.createComputedMeasure( "measure3",
				DataType.DOUBLE_TYPE,
				new ScriptExpression( "measure[\"measure1\"]" ) );

		IBinding binding1 = new Binding( "edge1level1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "edge1level2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding3 = new Binding( "edge1level3" );

		binding3.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level13\"]" ) );
		cqd.addBinding( binding3 );

		IBinding binding4 = new Binding( "edge2level1" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "measure2" );
		binding5.setExpression( new ScriptExpression( "measure[\"measure2\"]" ) );
		binding5.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		binding5.addAggregateOn( "dimension[\"dimension1\"][\"level11\"]" );
		binding5.addAggregateOn( "dimension[\"dimension1\"][\"level12\"]" );
		binding5.addAggregateOn( "dimension[\"dimension1\"][\"level13\"]" );
		binding5.addAggregateOn( "dimension[\"dimension2\"][\"level21\"]" );
		cqd.addBinding( binding5 );

		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		columnEdgeBindingNames.add( "edge1level3" );
		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "edge2level1" );

		this.printCube( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				"measure2" );
		engine.shutdown( );
	}

	/**
	 * 
	 * @throws Exception
	 */
	@Test
    public void testGrandTotalWithComputedMeasure( ) throws Exception
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		ILevelDefinition level11 = hier1.createLevel( "level11" );
		ILevelDefinition level12 = hier1.createLevel( "level12" );

		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		ILevelDefinition level21 = hier2.createLevel( "level21" );

		cqd.createComputedMeasure( "measure2",
				DataType.DOUBLE_TYPE,
				new ScriptExpression( "measure[\"measure1\"] + measure[\"measure3\"]" ) );
		cqd.createComputedMeasure( "measure3",
				DataType.DOUBLE_TYPE,
				new ScriptExpression( "measure[\"measure1\"]" ) );

		IBinding binding1 = new Binding( "edge1level1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "edge1level2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding4 = new Binding( "edge2level1" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "measure2" );
		binding5.setExpression( new ScriptExpression( "measure[\"measure2\"]" ) );
		binding5.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		binding5.addAggregateOn( "dimension[\"dimension1\"][\"level11\"]" );
		binding5.addAggregateOn( "dimension[\"dimension1\"][\"level12\"]" );
		binding5.addAggregateOn( "dimension[\"dimension2\"][\"level21\"]" );
		cqd.addBinding( binding5 );

		IBinding binding6 = new Binding( "rowGrandTotal" );
		binding6.setExpression( new ScriptExpression( "measure[\"measure2\"]" ) );
		binding6.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		binding6.addAggregateOn( "dimension[\"dimension2\"][\"level21\"]" );
		cqd.addBinding( binding6 );

		IBinding binding7 = new Binding( "columnGrandTotal" );
		binding7.setExpression( new ScriptExpression( "measure[\"measure2\"]" ) );
		binding7.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		binding7.addAggregateOn( "dimension[\"dimension1\"][\"level11\"]" );
		binding7.addAggregateOn( "dimension[\"dimension1\"][\"level12\"]" );
		cqd.addBinding( binding7 );

		IBinding binding8 = new Binding( "grandTotal" );
		binding8.setExpression( new ScriptExpression( "measure[\"measure2\"]" ) );
		binding8.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		cqd.addBinding( binding8 );

		// sort on year
		CubeSortDefinition sorter1 = new CubeSortDefinition( );
		sorter1.setExpression( "dimension[\"dimension2\"][\"level21\"]" );
		sorter1.setSortDirection( ISortDefinition.SORT_DESC );
		sorter1.setTargetLevel( level21 );

		// sort on country
		CubeSortDefinition sorter2 = new CubeSortDefinition( );
		sorter2.setExpression( "dimension[\"dimension1\"][\"level11\"]" );
		sorter2.setSortDirection( ISortDefinition.SORT_DESC );
		sorter2.setTargetLevel( level11 );

		// sort on city.
		CubeSortDefinition sorter3 = new CubeSortDefinition( );
		sorter3.setExpression( "dimension[\"dimension1\"][\"level12\"]" );
		sorter3.setSortDirection( ISortDefinition.SORT_DESC );
		sorter3.setTargetLevel( level12 );

		cqd.addSort( sorter1 );
		cqd.addSort( sorter2 );
		cqd.addSort( sorter3 );
		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "edge2level1" );

		this.printCube( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				"measure2",
				"columnGrandTotal",
				"rowGrandTotal",
				"grandTotal" );
		
		engine.shutdown( );

	}

	/**
	 * Test computed measure name conflict.
	 * 
	 * @throws Exception
	 */
	@Test
    public void testInvalidComputedMeasure1( ) throws Exception
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

		cqd.createComputedMeasure( "measure1",
				DataType.DOUBLE_TYPE,
				new ScriptExpression( "measure[\"measure1\"] + measure[\"measure1\"]" ) );

		IBinding binding1 = new Binding( "edge1level1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "edge1level2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding3 = new Binding( "edge1level3" );

		binding3.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level13\"]" ) );
		cqd.addBinding( binding3 );

		IBinding binding4 = new Binding( "edge2level1" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "measure2" );
		binding5.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		cqd.addBinding( binding5 );

		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		try
		{
			queryResults.getCubeCursor( );
			fail( "Should not arrive here" );
		}
		catch ( Exception e )
		{

		}
		engine.shutdown( );
	}

	/**
	 * Test use all dimension levels.
	 * 
	 * @throws Exception
	 */
	@Test
    public void testBasicCache( ) throws Exception
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

		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		IBinding binding1 = new Binding( "edge1level1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "edge1level2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding3 = new Binding( "edge1level3" );

		binding3.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level13\"]" ) );
		cqd.addBinding( binding3 );

		IBinding binding4 = new Binding( "edge2level1" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "measure1" );
		binding5.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		cqd.addBinding( binding5 );

		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );
		cqd.setCacheQueryResults( true );
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );

		// Load from cache.
		cqd.setQueryResultsID( queryResults.getID( ) );
		pcq = engine.prepare( cqd, null );
		queryResults = pcq.execute( null );
		cursor = queryResults.getCubeCursor( );

		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		columnEdgeBindingNames.add( "edge1level3" );
		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "edge2level1" );

		this.printCube( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				"measure1" );
		engine.shutdown( );
	}

	/**
	 * Test use part of dimension levels.
	 * 
	 * @throws Exception
	 */
	@Test
    public void testBasic1Cache( ) throws Exception
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		hier1.createLevel( "level11" );
		hier1.createLevel( "level12" );

		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		hier2.createLevel( "level21" );

		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		IBinding binding1 = new Binding( "edge1level1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "edge1level2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding4 = new Binding( "edge2level1" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "measure1" );
		binding5.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		cqd.addBinding( binding5 );

		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );
		cqd.setCacheQueryResults( true );

		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "edge2level1" );

		// Load from cache.
		cqd.setQueryResultsID( queryResults.getID( ) );
		pcq = engine.prepare( cqd, null );
		queryResults = pcq.execute( null );
		cursor = queryResults.getCubeCursor( );

		this.printCube( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				"measure1" );
		engine.shutdown( );
	}

	/**
	 * Test use all dimension levels.
	 * 
	 * @throws Exception
	 */
	@Test
    public void testTwoCaches( ) throws Exception
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

		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		IBinding binding1 = new Binding( "edge1level1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "edge1level2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding3 = new Binding( "edge1level3" );

		binding3.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level13\"]" ) );
		cqd.addBinding( binding3 );

		IBinding binding4 = new Binding( "edge2level1" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "measure1" );
		binding5.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		cqd.addBinding( binding5 );

		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );
		cqd.setCacheQueryResults( true );
		
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		columnEdgeBindingNames.add( "edge1level3" );
		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "edge2level1" );


		IPreparedCubeQuery pcq1 = engine.prepare( cqd, null );
		ICubeQueryResults queryResults1 = pcq1.execute( null );
		CubeCursor cursor1 = queryResults1.getCubeCursor( );

		cqd.setQueryResultsID( null );
		IPreparedCubeQuery pcq2 = engine.prepare( cqd, null );
		ICubeQueryResults queryResults2 = pcq2.execute( null );
		CubeCursor cursor2 = queryResults2.getCubeCursor( );
		
		// Load from cache.
		cqd.setQueryResultsID( queryResults1.getID( ) );
		pcq1 = engine.prepare( cqd, null );
		queryResults1 = pcq1.execute( null );
		cursor1 = queryResults1.getCubeCursor( );
		
		cqd.setQueryResultsID( queryResults2.getID( ) );
		pcq2 = engine.prepare( cqd, null );
		queryResults2 = pcq2.execute( null );
		cursor2 = queryResults2.getCubeCursor( );
		
		this.printCube( cursor1,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				"measure1", null, null, null, false );
		
	

		
		
		this.printCube( cursor2,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				"measure1", null, null, null, true );
		
		engine.shutdown( );
	}
	/**
	 * Filter1, filter out all level11 == CN.
	 * 
	 * @throws Exception
	 */
	@Test
    public void testFilter1Cache( ) throws Exception
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		hier1.createLevel( "level11" );
		hier1.createLevel( "level12" );

		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		hier2.createLevel( "level21" );

		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		IBinding binding1 = new Binding( "edge1level1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "edge1level2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding4 = new Binding( "edge2level1" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "measure1" );
		binding5.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		cqd.addBinding( binding5 );

		IFilterDefinition filter = new FilterDefinition( new ConditionalExpression( "dimension[\"dimension1\"][\"level11\"]",
				IConditionalExpression.OP_EQ,
				"\"CN\"" ) );
		cqd.addFilter( filter );
		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );

		cqd.setCacheQueryResults( true );

		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "edge2level1" );

		// Load from cache.
		cqd.setQueryResultsID( queryResults.getID( ) );
		pcq = engine.prepare( cqd, null );
		queryResults = pcq.execute( null );
		cursor = queryResults.getCubeCursor( );

		this.printCube( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				"measure1" );
		engine.shutdown( );

	}

	/**
	 * Filter2, filter out all level11 = CN and level21 > 2000.
	 * 
	 * @throws Exception
	 */
	@Test
    public void testFilter2Cache( ) throws Exception
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		hier1.createLevel( "level11" );
		hier1.createLevel( "level12" );

		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		hier2.createLevel( "level21" );

		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		IBinding binding1 = new Binding( "edge1level1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "edge1level2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding4 = new Binding( "edge2level1" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "measure1" );
		binding5.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		cqd.addBinding( binding5 );

		IFilterDefinition filter1 = new FilterDefinition( new ConditionalExpression( "dimension[\"dimension1\"][\"level11\"]",
				IConditionalExpression.OP_EQ,
				"\"CN\"" ) );
		IFilterDefinition filter2 = new FilterDefinition( new ConditionalExpression( "dimension[\"dimension2\"][\"level21\"]",
				IConditionalExpression.OP_GE,
				"2000" ) );

		cqd.addFilter( filter1 );
		cqd.addFilter( filter2 );
		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );
		cqd.setCacheQueryResults( true );

		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "edge2level1" );

		// Load from cache.
		cqd.setQueryResultsID( queryResults.getID( ) );
		pcq = engine.prepare( cqd, null );
		queryResults = pcq.execute( null );
		cursor = queryResults.getCubeCursor( );

		this.printCube( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				"measure1" );
		
		engine.shutdown( );

	}

	/**
	 * Simple sort on 1 level
	 * 
	 * @throws Exception
	 */
	@Test
    public void testSort1Cache( ) throws Exception
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		hier1.createLevel( "level11" );
		hier1.createLevel( "level12" );

		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		ILevelDefinition level21 = hier2.createLevel( "level21" );

		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		IBinding binding1 = new Binding( "edge1level1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "edge1level2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding4 = new Binding( "edge2level1" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "measure1" );
		binding5.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		cqd.addBinding( binding5 );

		CubeSortDefinition sorter = new CubeSortDefinition( );
		sorter.setExpression( "dimension[\"dimension2\"][\"level21\"]" );
		sorter.setSortDirection( ISortDefinition.SORT_DESC );
		sorter.setTargetLevel( level21 );
		cqd.addSort( sorter );
		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );

		cqd.setCacheQueryResults( true );

		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "edge2level1" );

		// Load from cache.
		cqd.setQueryResultsID( queryResults.getID( ) );
		pcq = engine.prepare( cqd, null );
		queryResults = pcq.execute( null );
		cursor = queryResults.getCubeCursor( );

		this.printCube( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				"measure1" );
		engine.shutdown( );
	}

	/**
	 * Complex sort on multiple levels
	 * 
	 * @throws Exception
	 */
	@Test
    public void testSort2Cache( ) throws Exception
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		ILevelDefinition level11 = hier1.createLevel( "level11" );
		ILevelDefinition level12 = hier1.createLevel( "level12" );

		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		ILevelDefinition level21 = hier2.createLevel( "level21" );

		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		IBinding binding1 = new Binding( "edge1level1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "edge1level2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding4 = new Binding( "edge2level1" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "measure1" );
		binding5.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		cqd.addBinding( binding5 );

		// sort on year
		CubeSortDefinition sorter1 = new CubeSortDefinition( );
		sorter1.setExpression( "dimension[\"dimension2\"][\"level21\"]" );
		sorter1.setSortDirection( ISortDefinition.SORT_DESC );
		sorter1.setTargetLevel( level21 );
		// sort on country
		CubeSortDefinition sorter2 = new CubeSortDefinition( );
		sorter2.setExpression( "dimension[\"dimension1\"][\"level11\"]" );
		sorter2.setSortDirection( ISortDefinition.SORT_DESC );
		sorter2.setTargetLevel( level11 );
		// sort on city.
		CubeSortDefinition sorter3 = new CubeSortDefinition( );
		sorter3.setExpression( "dimension[\"dimension1\"][\"level12\"]" );
		sorter3.setSortDirection( ISortDefinition.SORT_DESC );
		sorter3.setTargetLevel( level12 );
		cqd.addSort( sorter1 );
		cqd.addSort( sorter2 );
		cqd.addSort( sorter3 );
		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );

		this.createCube( engine );
		cqd.setCacheQueryResults( true );

		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "edge2level1" );

		// Load from cache.
		cqd.setQueryResultsID( queryResults.getID( ) );
		pcq = engine.prepare( cqd, null );
		queryResults = pcq.execute( null );
		cursor = queryResults.getCubeCursor( );

		this.printCube( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				"measure1" );
		engine.shutdown( );
	}

	/**
	 * Test grand total
	 * 
	 * @throws Exception
	 */
	@Test
    public void testGrandTotalCache( ) throws Exception
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		ILevelDefinition level11 = hier1.createLevel( "level11" );
		ILevelDefinition level12 = hier1.createLevel( "level12" );

		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		ILevelDefinition level21 = hier2.createLevel( "level21" );

		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		IBinding binding1 = new Binding( "edge1level1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "edge1level2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding4 = new Binding( "edge2level1" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "measure1" );
		binding5.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		cqd.addBinding( binding5 );

		IBinding binding6 = new Binding( "rowGrandTotal" );
		binding6.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		binding6.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		binding6.addAggregateOn( "dimension[\"dimension2\"][\"level21\"]" );
		cqd.addBinding( binding6 );

		IBinding binding7 = new Binding( "columnGrandTotal" );
		binding7.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		binding7.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		binding7.addAggregateOn( "dimension[\"dimension1\"][\"level11\"]" );
		binding7.addAggregateOn( "dimension[\"dimension1\"][\"level12\"]" );
		cqd.addBinding( binding7 );

		IBinding binding8 = new Binding( "grandTotal" );
		binding8.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		binding8.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		cqd.addBinding( binding8 );

		// sort on year
		CubeSortDefinition sorter1 = new CubeSortDefinition( );
		sorter1.setExpression( "dimension[\"dimension2\"][\"level21\"]" );
		sorter1.setSortDirection( ISortDefinition.SORT_DESC );
		sorter1.setTargetLevel( level21 );
		// sort on country
		CubeSortDefinition sorter2 = new CubeSortDefinition( );
		sorter2.setExpression( "dimension[\"dimension1\"][\"level11\"]" );
		sorter2.setSortDirection( ISortDefinition.SORT_DESC );
		sorter2.setTargetLevel( level11 );
		// sort on city.
		CubeSortDefinition sorter3 = new CubeSortDefinition( );
		sorter3.setExpression( "dimension[\"dimension1\"][\"level12\"]" );
		sorter3.setSortDirection( ISortDefinition.SORT_DESC );
		sorter3.setTargetLevel( level12 );
		cqd.addSort( sorter1 );
		cqd.addSort( sorter2 );
		cqd.addSort( sorter3 );
		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );

		cqd.setCacheQueryResults( true );

		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "edge2level1" );

		// Load from cache.
		cqd.setQueryResultsID( queryResults.getID( ) );
		pcq = engine.prepare( cqd, null );
		queryResults = pcq.execute( null );
		cursor = queryResults.getCubeCursor( );

		this.printCube( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				"measure1",
				"columnGrandTotal",
				"rowGrandTotal",
				"grandTotal" );
		
		engine.shutdown( );

	}

	/**
	 * Test grand total
	 * 
	 * @throws Exception
	 */
	@Test
    public void testAggrSortCache( ) throws Exception
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		ILevelDefinition level11 = hier1.createLevel( "level11" );
		ILevelDefinition level12 = hier1.createLevel( "level12" );
		ILevelDefinition level13 = hier1.createLevel( "level13" );
		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		ILevelDefinition level21 = hier2.createLevel( "level21" );

		createSortTestBindings( cqd );

		// sort on year
		CubeSortDefinition sorter1 = new CubeSortDefinition( );
		sorter1.setExpression( "data[\"rowGrandTotal\"]" );
		sorter1.setAxisQualifierLevels( null );
		sorter1.setAxisQualifierValues( null );
		sorter1.setTargetLevel( level21 );
		sorter1.setSortDirection( ISortDefinition.SORT_DESC );

		// sort on country
		CubeSortDefinition sorter2 = new CubeSortDefinition( );
		sorter2.setExpression( "data[\"city_year_total\"]" );
		sorter2.setAxisQualifierLevels( new ILevelDefinition[]{
			level21
		} );
		sorter2.setAxisQualifierValues( new Object[]{
			"2002"
		} );
		sorter2.setTargetLevel( level12 );
		sorter2.setSortDirection( ISortDefinition.SORT_DESC );

		CubeSortDefinition sorter3 = new CubeSortDefinition( );
		sorter3.setExpression( "data[\"country_year_total\"]" );
		sorter3.setAxisQualifierLevels( new ILevelDefinition[]{
			level21
		} );
		sorter3.setAxisQualifierValues( new Object[]{
			"2002"
		} );
		sorter3.setTargetLevel( level11 );
		sorter3.setSortDirection( ISortDefinition.SORT_DESC );

		CubeSortDefinition sorter4 = new CubeSortDefinition( );
		sorter4.setExpression( "dimension[\"dimension1\"][\"level13\"]" );
		sorter4.setAxisQualifierLevels( null );
		sorter4.setAxisQualifierValues( null );
		sorter4.setTargetLevel( level13 );
		sorter4.setSortDirection( ISortDefinition.SORT_DESC );

		// Make UN before China.
		CubeSortDefinition sorter5 = new CubeSortDefinition( );
		sorter5.setExpression( "dimension[\"dimension1\"][\"level11\"]" );
		sorter5.setAxisQualifierLevels( null );
		sorter5.setAxisQualifierValues( null );
		sorter5.setTargetLevel( level11 );
		sorter5.setSortDirection( ISortDefinition.SORT_DESC );

		/*
		 * //sort on city. SortDefinition sorter3 = new SortDefinition();
		 * sorter3.setExpression( "dimension[\"dimension1\"][\"level12\"]" );
		 * sorter3.setSortDirection( ISortDefinition.SORT_DESC );
		 */
		cqd.addSort( sorter1 );
		cqd.addSort( sorter2 );
		cqd.addSort( sorter3 );
		cqd.addSort( sorter4 );
		cqd.addSort( sorter5 );
		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );

		cqd.setCacheQueryResults( true );

		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		columnEdgeBindingNames.add( "edge1level3" );

		// Load from cache.
		cqd.setQueryResultsID( queryResults.getID( ) );
		pcq = engine.prepare( cqd, null );
		queryResults = pcq.execute( null );
		cursor = queryResults.getCubeCursor( );

		printCube( cursor,
				"country_year_total",
				"city_year_total",
				"dist_total",
				"city_total",
				"country_total",
				"rowGrandTotal",
				"grandTotal",
				new String[]{
						"edge1level1", "edge1level2", "edge1level3"
				},
				"edge2level1",
				"measure1" );
		engine.shutdown( );
	}

	/**
	 * Test grand total
	 * 
	 * @throws Exception
	 */
	@Test
    public void testAggrSort1Cache( ) throws Exception
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
		ILevelDefinition level21 = hier2.createLevel( "level21" );

		createSortTestBindings( cqd );

		// sort on year
		CubeSortDefinition sorter1 = new CubeSortDefinition( );
		sorter1.setExpression( "data[\"rowGrandTotal\"]" );
		sorter1.setAxisQualifierLevels( null );
		sorter1.setAxisQualifierValues( null );
		sorter1.setTargetLevel( level21 );
		sorter1.setSortDirection( ISortDefinition.SORT_DESC );

		cqd.addSort( sorter1 );
		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );
		cqd.setCacheQueryResults( true );

		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		columnEdgeBindingNames.add( "edge1level3" );

		// Load from cache.
		cqd.setQueryResultsID( queryResults.getID( ) );
		pcq = engine.prepare( cqd, null );
		queryResults = pcq.execute( null );
		cursor = queryResults.getCubeCursor( );

		printCube( cursor,
				"country_year_total",
				"city_year_total",
				"dist_total",
				"city_total",
				"country_total",
				"rowGrandTotal",
				"grandTotal",
				new String[]{
						"edge1level1", "edge1level2", "edge1level3"
				},
				"edge2level1",
				"measure1" );
		engine.shutdown( );
	}

	/**
	 * Test grand total
	 * 
	 * @throws Exception
	 */
	@Test
    public void testAggrSort2Cache( ) throws Exception
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		hier1.createLevel( "level11" );
		ILevelDefinition level12 = hier1.createLevel( "level12" );
		hier1.createLevel( "level13" );
		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		ILevelDefinition level21 = hier2.createLevel( "level21" );

		createSortTestBindings( cqd );

		// sort on year
		CubeSortDefinition sorter1 = new CubeSortDefinition( );
		sorter1.setExpression( "data[\"rowGrandTotal\"]" );
		sorter1.setAxisQualifierLevels( null );
		sorter1.setAxisQualifierValues( null );
		sorter1.setTargetLevel( level21 );
		sorter1.setSortDirection( ISortDefinition.SORT_DESC );

		// sort on country
		CubeSortDefinition sorter2 = new CubeSortDefinition( );
		sorter2.setExpression( "data[\"city_year_total\"]" );
		sorter2.setAxisQualifierLevels( new ILevelDefinition[]{
			level21
		} );
		sorter2.setAxisQualifierValues( new Object[]{
			"2002"
		} );
		sorter2.setTargetLevel( level12 );
		sorter2.setSortDirection( ISortDefinition.SORT_DESC );

		cqd.addSort( sorter1 );
		cqd.addSort( sorter2 );
		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );

		cqd.setCacheQueryResults( true );

		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		columnEdgeBindingNames.add( "edge1level3" );

		// Load from cache.
		cqd.setQueryResultsID( queryResults.getID( ) );
		pcq = engine.prepare( cqd, null );
		queryResults = pcq.execute( null );
		cursor = queryResults.getCubeCursor( );

		printCube( cursor,
				"country_year_total",
				"city_year_total",
				"dist_total",
				"city_total",
				"country_total",
				"rowGrandTotal",
				"grandTotal",
				new String[]{
						"edge1level1", "edge1level2", "edge1level3"
				},
				"edge2level1",
				"measure1" );
		engine.shutdown( );
	}

	/**
	 * Test grand total
	 * 
	 * @throws Exception
	 */
	@Test
    public void testAggrSort3Cache( ) throws Exception
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		ILevelDefinition level11 = hier1.createLevel( "level11" );
		ILevelDefinition level12 = hier1.createLevel( "level12" );
		hier1.createLevel( "level13" );
		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		ILevelDefinition level21 = hier2.createLevel( "level21" );

		createSortTestBindings( cqd );

		// sort on year
		CubeSortDefinition sorter1 = new CubeSortDefinition( );
		sorter1.setExpression( "data[\"rowGrandTotal\"]" );
		sorter1.setAxisQualifierLevels( null );
		sorter1.setAxisQualifierValues( null );
		sorter1.setTargetLevel( level21 );
		sorter1.setSortDirection( ISortDefinition.SORT_DESC );

		// sort on country
		CubeSortDefinition sorter2 = new CubeSortDefinition( );
		sorter2.setExpression( "data[\"city_year_total\"]" );
		sorter2.setAxisQualifierLevels( new ILevelDefinition[]{
			level21
		} );
		sorter2.setAxisQualifierValues( new Object[]{
			"2002"
		} );
		sorter2.setTargetLevel( level12 );
		sorter2.setSortDirection( ISortDefinition.SORT_DESC );

		CubeSortDefinition sorter3 = new CubeSortDefinition( );
		sorter3.setExpression( "data[\"country_year_total\"]" );
		sorter3.setAxisQualifierLevels( new ILevelDefinition[]{
			level21
		} );
		sorter3.setAxisQualifierValues( new Object[]{
			"2002"
		} );
		sorter3.setTargetLevel( level11 );
		sorter3.setSortDirection( ISortDefinition.SORT_DESC );

		cqd.addSort( sorter1 );
		cqd.addSort( sorter2 );
		cqd.addSort( sorter3 );
		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );

		cqd.setCacheQueryResults( true );

		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		columnEdgeBindingNames.add( "edge1level3" );

		// Load from cache.
		cqd.setQueryResultsID( queryResults.getID( ) );
		pcq = engine.prepare( cqd, null );
		queryResults = pcq.execute( null );
		cursor = queryResults.getCubeCursor( );

		printCube( cursor,
				"country_year_total",
				"city_year_total",
				"dist_total",
				"city_total",
				"country_total",
				"rowGrandTotal",
				"grandTotal",
				new String[]{
						"edge1level1", "edge1level2", "edge1level3"
				},
				"edge2level1",
				"measure1" );
		engine.shutdown( );
	}

	/**
	 * Test grand total
	 * 
	 * @throws Exception
	 */
	@Test
    public void testAggrSort5Cache( ) throws Exception
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		hier1.createLevel( "level11" );
		hier1.createLevel( "level12" );
		ILevelDefinition level13 = hier1.createLevel( "level13" );
		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		ILevelDefinition level21 = hier2.createLevel( "level21" );

		createSortTestBindings( cqd );

		// sort on year
		CubeSortDefinition sorter1 = new CubeSortDefinition( );
		sorter1.setExpression( "data[\"measure1\"]" );
		sorter1.setAxisQualifierLevels( new ILevelDefinition[]{
			level21
		} );
		sorter1.setAxisQualifierValues( new Object[]{
			"1998"
		} );
		sorter1.setTargetLevel( level13 );
		sorter1.setSortDirection( ISortDefinition.SORT_DESC );

		cqd.addSort( sorter1 );
		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );

		cqd.setCacheQueryResults( true );

		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		columnEdgeBindingNames.add( "edge1level3" );

		// Load from cache.
		cqd.setQueryResultsID( queryResults.getID( ) );
		pcq = engine.prepare( cqd, null );
		queryResults = pcq.execute( null );
		cursor = queryResults.getCubeCursor( );

		printCube( cursor,
				"country_year_total",
				"city_year_total",
				"dist_total",
				"city_total",
				"country_total",
				"rowGrandTotal",
				"grandTotal",
				new String[]{
						"edge1level1", "edge1level2", "edge1level3"
				},
				"edge2level1",
				"measure1" );
		engine.shutdown( );
	}

	/**
	 * Test binding "row" reference
	 * 
	 * @throws Exception
	 */
	@Test
    public void testBindingRowReferenceCache( ) throws Exception
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		ILevelDefinition level11 = hier1.createLevel( "level11" );
		ILevelDefinition level12 = hier1.createLevel( "level12" );

		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		ILevelDefinition level21 = hier2.createLevel( "level21" );

		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		IBinding binding1 = new Binding( "edge1level1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "edge1level2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding4 = new Binding( "edge2level1" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "measure1" );
		binding5.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		cqd.addBinding( binding5 );

		IBinding binding6 = new Binding( "rowGrandTotal" );
		binding6.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		binding6.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		binding6.addAggregateOn( "dimension[\"dimension2\"][\"level21\"]" );
		cqd.addBinding( binding6 );

		IBinding binding7 = new Binding( "columnGrandTotal" );
		binding7.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		binding7.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		binding7.addAggregateOn( "dimension[\"dimension1\"][\"level11\"]" );
		binding7.addAggregateOn( "dimension[\"dimension1\"][\"level12\"]" );
		cqd.addBinding( binding7 );

		IBinding binding8 = new Binding( "grandTotal" );
		binding8.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		binding8.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		cqd.addBinding( binding8 );

		IBinding binding9 = new Binding( "row_rowGrandTotal" );
		binding9.setExpression( new ScriptExpression( "data[\"rowGrandTotal\"]*10" ) );
		cqd.addBinding( binding9 );

		IBinding binding10 = new Binding( "row_columnGrandTotal" );
		binding10.setExpression( new ScriptExpression( "data[\"columnGrandTotal\"]*10" ) );
		cqd.addBinding( binding10 );

		IBinding binding11 = new Binding( "row_grandTotal" );
		binding11.setExpression( new ScriptExpression( "data[\"grandTotal\"]*10" ) );
		cqd.addBinding( binding11 );

		IBinding binding12 = new Binding( "row_measure1" );
		binding12.setExpression( new ScriptExpression( "data[\"measure1\"]*10" ) );
		cqd.addBinding( binding12 );

		// sort on year
		CubeSortDefinition sorter1 = new CubeSortDefinition( );
		sorter1.setExpression( "dimension[\"dimension2\"][\"level21\"]" );
		sorter1.setSortDirection( ISortDefinition.SORT_DESC );
		sorter1.setTargetLevel( level21 );
		// sort on country
		CubeSortDefinition sorter2 = new CubeSortDefinition( );
		sorter2.setExpression( "dimension[\"dimension1\"][\"level11\"]" );
		sorter2.setSortDirection( ISortDefinition.SORT_DESC );
		sorter2.setTargetLevel( level11 );
		// sort on city.
		CubeSortDefinition sorter3 = new CubeSortDefinition( );
		sorter3.setExpression( "dimension[\"dimension1\"][\"level12\"]" );
		sorter3.setSortDirection( ISortDefinition.SORT_DESC );
		sorter3.setTargetLevel( level12 );

		cqd.addSort( sorter1 );
		cqd.addSort( sorter2 );
		cqd.addSort( sorter3 );
		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );

		cqd.setCacheQueryResults( true );

		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "edge2level1" );

		// Load from cache.
		cqd.setQueryResultsID( queryResults.getID( ) );
		pcq = engine.prepare( cqd, null );
		queryResults = pcq.execute( null );
		cursor = queryResults.getCubeCursor( );

		this.printCube( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				"row_measure1",
				"row_columnGrandTotal",
				"row_rowGrandTotal",
				"row_grandTotal" );
		
		engine.shutdown( );

	}

	/**
	 * Test computed measure.
	 * 
	 * @throws Exception
	 */
	@Test
    public void testComputedMeasureCache( ) throws Exception
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

		cqd.createComputedMeasure( "measure2",
				DataType.DOUBLE_TYPE,
				new ScriptExpression( "measure[\"measure1\"] + measure[\"measure1\"]" ) );

		IBinding binding1 = new Binding( "edge1level1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "edge1level2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding3 = new Binding( "edge1level3" );

		binding3.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level13\"]" ) );
		cqd.addBinding( binding3 );

		IBinding binding4 = new Binding( "edge2level1" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "measure2" );
		binding5.setExpression( new ScriptExpression( "measure[\"measure2\"]" ) );
		binding5.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		binding5.addAggregateOn( "dimension[\"dimension1\"][\"level11\"]" );
		binding5.addAggregateOn( "dimension[\"dimension1\"][\"level12\"]" );
		binding5.addAggregateOn( "dimension[\"dimension1\"][\"level13\"]" );
		binding5.addAggregateOn( "dimension[\"dimension2\"][\"level21\"]" );
		cqd.addBinding( binding5 );

		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );
		cqd.setCacheQueryResults( true );
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );

		// Load from cache.
		cqd.setQueryResultsID( queryResults.getID( ) );
		pcq = engine.prepare( cqd, null );
		queryResults = pcq.execute( null );
		cursor = queryResults.getCubeCursor( );

		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		columnEdgeBindingNames.add( "edge1level3" );
		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "edge2level1" );

		this.printCube( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				"measure2" );
		engine.shutdown( );
	}
	
	/**
	 * Test aggregation on computed measure.
	 * 
	 * @throws Exception
	 */
	@Test
    public void testAggregationOnCalculatedMeasure( ) throws Exception
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
		
		cqd.createDerivedMeasure( "measure2",
				DataType.DOUBLE_TYPE,
				new ScriptExpression( "measure[\"measure1\"] + measure[\"measure1\"]" ) );

		IBinding binding1 = new Binding( "edge1level1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "edge1level2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding3 = new Binding( "edge1level3" );

		binding3.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level13\"]" ) );
		cqd.addBinding( binding3 );

		IBinding binding4 = new Binding( "edge2level1" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "measure2" );
		binding5.setExpression( new ScriptExpression( "measure[\"measure2\"]" ) );
		binding5.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		binding5.addAggregateOn( "dimension[\"dimension1\"][\"level11\"]" );
		binding5.addAggregateOn( "dimension[\"dimension1\"][\"level12\"]" );
		binding5.addAggregateOn( "dimension[\"dimension1\"][\"level13\"]" );
		binding5.addAggregateOn( "dimension[\"dimension2\"][\"level21\"]" );
		cqd.addBinding( binding5 );
		
		IBinding binding6 = new Binding( "aggregationOnMeasure2" );
		binding6.setExpression( new ScriptExpression( "data[\"measure2\"]" ) );
		binding6.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		binding6.addAggregateOn( "dimension[\"dimension2\"][\"level21\"]" );
		cqd.addBinding( binding6 );

		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );
		cqd.setCacheQueryResults( true );
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		columnEdgeBindingNames.add( "edge1level3" );
		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "edge2level1" );

		this.printCube( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				"measure2",
				null,
				"aggregationOnMeasure2",
				null );
		engine.shutdown( );
	}
	
	/**
	 * Test aggregation on derived measure.
	 * 
	 * @throws Exception
	 */
	@Test
    public void testAggregationOnDerivedMeasure( ) throws Exception
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

		IBinding binding4 = new Binding( "edge2level1" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "measure1" );
		binding5.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		binding5.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		binding5.addAggregateOn( "dimension[\"dimension1\"][\"level11\"]" );
		binding5.addAggregateOn( "dimension[\"dimension1\"][\"level12\"]" );
		binding5.addAggregateOn( "dimension[\"dimension1\"][\"level13\"]" );
		binding5.addAggregateOn( "dimension[\"dimension2\"][\"level21\"]" );
		cqd.addBinding( binding5 );
		
		IBinding binding6 = new Binding( "derivedMeasure" );
		binding6.setExpression( new ScriptExpression( "data[\"measure1\"]+100" ) );
		cqd.addBinding( binding6 );
		
		IBinding binding7 = new Binding( "aggregationOnMeasure" );
		binding7.setExpression( new ScriptExpression( "data[\"derivedMeasure\"]" ) );
		binding7.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		binding7.addAggregateOn( "dimension[\"dimension2\"][\"level21\"]" );
		cqd.addBinding( binding7 );

		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );
		cqd.setCacheQueryResults( true );
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		columnEdgeBindingNames.add( "edge1level3" );
		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "edge2level1" );

		this.printCube( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				"derivedMeasure",
				null,
				"aggregationOnMeasure",
				null );
		engine.shutdown( );
	}

	/**
	 * Test Nested total
	 * 
	 * @throws Exception
	 */
	@Test
    public void testNestedCrossTab( ) throws Exception
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		ILevelDefinition level11 = hier1.createLevel( "level11" );
		ILevelDefinition level12 = hier1.createLevel( "level12" );
		ILevelDefinition level13 = hier1.createLevel( "level13" );
		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		ILevelDefinition level21 = hier2.createLevel( "level21" );

		createSortTestBindings( cqd );

		// filter on year
		CubeFilterDefinition filter1 = new CubeFilterDefinition( new ScriptExpression( "data[\"rowGrandTotal\"] > 420" ) );
		filter1.setAxisQualifierLevels( null );
		filter1.setAxisQualifierValues( null );
		filter1.setTargetLevel( level21 );

		// sort on country
		CubeFilterDefinition filter2 = new CubeFilterDefinition( new ScriptExpression( "data[\"city_year_total\"] == data._outer[\"column1\"]" ) );

		filter2.setAxisQualifierLevels( new ILevelDefinition[]{
			level21
		} );
		filter2.setAxisQualifierValues( new Object[]{
			"2002"
		} );
		filter2.setTargetLevel( level12 );

		CubeFilterDefinition filter3 = new CubeFilterDefinition( new ScriptExpression( "data[\"country_year_total\"] < 300" ) );

		filter3.setAxisQualifierLevels( new ILevelDefinition[]{
			level21
		} );
		filter3.setAxisQualifierValues( new Object[]{
			"2002"
		} );
		filter3.setTargetLevel( level11 );

		CubeSortDefinition sorter4 = new CubeSortDefinition( );
		sorter4.setExpression( "dimension[\"dimension1\"][\"level13\"]" );
		sorter4.setAxisQualifierLevels( null );
		sorter4.setAxisQualifierValues( null );
		sorter4.setTargetLevel( level13 );
		sorter4.setSortDirection( ISortDefinition.SORT_DESC );

		// Make UN before China.
		CubeSortDefinition sorter5 = new CubeSortDefinition( );
		sorter5.setExpression( "dimension[\"dimension1\"][\"level11\"]" );
		sorter5.setAxisQualifierLevels( null );
		sorter5.setAxisQualifierValues( null );
		sorter5.setTargetLevel( level11 );
		sorter5.setSortDirection( ISortDefinition.SORT_DESC );

		/*
		 * //sort on city. SortDefinition sorter3 = new SortDefinition();
		 * sorter3.setExpression( "dimension[\"dimension1\"][\"level12\"]" );
		 * sorter3.setSortDirection( ISortDefinition.SORT_DESC );
		 */
		cqd.addFilter( filter1 );
		cqd.addFilter( filter2 );
		cqd.addFilter( filter3 );
		cqd.addSort( sorter4 );
		cqd.addSort( sorter5 );
		DataEngineImpl engine = (DataEngineImpl) DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );
		this.defineDataSourceDataSet( engine );
		IQueryDefinition query = this.createScriptDataSetQuery( );
		IPreparedQuery pq = engine.prepare( query );
		IQueryResults queryResults = pq.execute( null );
		IResultIterator it = queryResults.getResultIterator( );
		while ( it.next( ) )
		{
			if ( ((Number)it.getValue( "column1" )).intValue( ) == 55 
					||   ((Number)it.getValue( "column1" )).intValue( ) == 34 )
			{
				this.testPrintln( "\nOUTER RESULT:"+ it.getValue( "column1" ).toString( ) );
				IPreparedCubeQuery pcq = engine.prepare( cqd, null );
				ICubeQueryResults cqResults = (ICubeQueryResults) pcq.execute( queryResults, null );
				CubeCursor cursor = cqResults.getCubeCursor( );
				List columnEdgeBindingNames = new ArrayList( );
				columnEdgeBindingNames.add( "edge1level1" );
				columnEdgeBindingNames.add( "edge1level2" );
				columnEdgeBindingNames.add( "edge1level3" );

				printCube( cursor,
						"country_year_total",
						"city_year_total",
						"dist_total",
						"city_total",
						"country_total",
						"rowGrandTotal",
						"grandTotal",
						new String[]{
								"edge1level1", "edge1level2", "edge1level3"
						},
						"edge2level1",
						"measure1",
						false );

			}
			
		}
		this.checkOutputFile( );
		engine.shutdown( );
	}
	

	/**
	 * Test Table + crosstab
	 * 
	 * @throws Exception
	 */
	@Test
    public void testNestedCrossTab1( ) throws Exception
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		ILevelDefinition level11 = hier1.createLevel( "level11" );
		ILevelDefinition level12 = hier1.createLevel( "level12" );
		ILevelDefinition level13 = hier1.createLevel( "level13" );
		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		ILevelDefinition level21 = hier2.createLevel( "level21" );

		createSortTestBindings( cqd );

		// filter on year
		CubeFilterDefinition filter1 = new CubeFilterDefinition( new ScriptExpression( "data[\"measure1\"] > data._outer[\"column1\"]" ) );
	

		CubeSortDefinition sorter4 = new CubeSortDefinition( );
		sorter4.setExpression( "dimension[\"dimension1\"][\"level13\"]" );
		sorter4.setAxisQualifierLevels( null );
		sorter4.setAxisQualifierValues( null );
		sorter4.setTargetLevel( level13 );
		sorter4.setSortDirection( ISortDefinition.SORT_DESC );

		// Make UN before China.
		CubeSortDefinition sorter5 = new CubeSortDefinition( );
		sorter5.setExpression( "dimension[\"dimension1\"][\"level11\"]" );
		sorter5.setAxisQualifierLevels( null );
		sorter5.setAxisQualifierValues( null );
		sorter5.setTargetLevel( level11 );
		sorter5.setSortDirection( ISortDefinition.SORT_DESC );

		/*
		 * //sort on city. SortDefinition sorter3 = new SortDefinition();
		 * sorter3.setExpression( "dimension[\"dimension1\"][\"level12\"]" );
		 * sorter3.setSortDirection( ISortDefinition.SORT_DESC );
		 */
		cqd.addFilter( filter1 );

		cqd.addSort( sorter4 );
		cqd.addSort( sorter5 );
		DataEngineImpl engine = (DataEngineImpl) DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );
		this.defineDataSourceDataSet( engine );
		IQueryDefinition query = this.createScriptDataSetQuery( );
		IPreparedQuery pq = engine.prepare( query );
		IQueryResults queryResults = pq.execute( null );
		IResultIterator it = queryResults.getResultIterator( );
		while ( it.next( ) )
		{
			if ( ((Number)it.getValue( "column1" )).intValue( ) == 55 
					||   ((Number)it.getValue( "column1" )).intValue( ) == 34 )
			{
				this.testPrintln( "\nOUTER RESULT:"+it.getValue( "column1" ).toString( ) );
				IPreparedCubeQuery pcq = engine.prepare( cqd, null );
				ICubeQueryResults cqResults =  (ICubeQueryResults) pcq.execute( queryResults, null );
				CubeCursor cursor = cqResults.getCubeCursor( );
				List columnEdgeBindingNames = new ArrayList( );
				columnEdgeBindingNames.add( "edge1level1" );
				columnEdgeBindingNames.add( "edge1level2" );
				columnEdgeBindingNames.add( "edge1level3" );

				printCube( cursor,
						"country_year_total",
						"city_year_total",
						"dist_total",
						"city_total",
						"country_total",
						"rowGrandTotal",
						"grandTotal",
						new String[]{
								"edge1level1", "edge1level2", "edge1level3"
						},
						"edge2level1",
						"measure1",
						false );

			}
		}
		this.checkOutputFile( );
		engine.shutdown( );
	}
	
	/**
	 * Test crosstab + crosstab
	 * 
	 * @throws Exception
	 */
	@Test
    public void testNestedCrossTab2( ) throws Exception
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		ILevelDefinition level11 = hier1.createLevel( "level11" );
		ILevelDefinition level12 = hier1.createLevel( "level12" );
		ILevelDefinition level13 = hier1.createLevel( "level13" );
		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		ILevelDefinition level21 = hier2.createLevel( "level21" );

		createSortTestBindings( cqd );

		

		CubeSortDefinition sorter4 = new CubeSortDefinition( );
		sorter4.setExpression( "dimension[\"dimension1\"][\"level13\"]" );
		sorter4.setAxisQualifierLevels( null );
		sorter4.setAxisQualifierValues( null );
		sorter4.setTargetLevel( level13 );
		sorter4.setSortDirection( ISortDefinition.SORT_DESC );

		// Make UN before China.
		CubeSortDefinition sorter5 = new CubeSortDefinition( );
		sorter5.setExpression( "dimension[\"dimension1\"][\"level11\"]" );
		sorter5.setAxisQualifierLevels( null );
		sorter5.setAxisQualifierValues( null );
		sorter5.setTargetLevel( level11 );
		sorter5.setSortDirection( ISortDefinition.SORT_DESC );

		/*
		 * //sort on city. SortDefinition sorter3 = new SortDefinition();
		 * sorter3.setExpression( "dimension[\"dimension1\"][\"level12\"]" );
		 * sorter3.setSortDirection( ISortDefinition.SORT_DESC );
		 */
		// filter on year

		cqd.addSort( sorter4 );
		cqd.addSort( sorter5 );
		DataEngineImpl engine = (DataEngineImpl) DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults cqResults = pcq.execute( null );
		CubeCursor outerCursor = cqResults.getCubeCursor( );
		
		CubeFilterDefinition filter1 = new CubeFilterDefinition( new ConditionalExpression( "data[\"edge1level3\"]",
				IConditionalExpression.OP_EQ,
				"data._outer[\"edge1level3\"]" ) );
		filter1.setTargetLevel( level13 );
		cqd.addFilter( filter1 );
		
		IBinding out = new Binding( "out" );
		out.setExpression( new ScriptExpression( "data._outer[\"edge1level3\"]" ) );
		
		cqd.addBinding( out );
		int depth = 0;
		EdgeCursor edge1 = (EdgeCursor) ( outerCursor.getOrdinateEdge( ).get( 0 ) );
		EdgeCursor edge2 = (EdgeCursor) ( outerCursor.getOrdinateEdge( ).get( 1 ) );
		edge1.beforeFirst( );
		while ( edge2.next( ) )
		{
			if( depth > 5 )
				break;
			edge1.beforeFirst( );
			while ( edge1.next( ) )
			{
				depth++;
				if( depth > 5 )
					break;
				this.testPrintln( "\nOUTER RESULT:" + outerCursor.getObject( "edge1level3" ).toString( ) );
				
				
				IPreparedCubeQuery pcq1 = engine.prepare( cqd, null );
				ICubeQueryResults cqResults1 =  (ICubeQueryResults) pcq1.execute( cqResults, null );
				CubeCursor cursor = cqResults1.getCubeCursor( );

				printCube( cursor,
						"country_year_total",
						"city_year_total",
						"dist_total",
						"city_total",
						"country_total",
						"rowGrandTotal",
						"grandTotal",
						new String[]{
								"edge1level1", "edge1level2", "edge1level3"
						},
						"edge2level1",
						"measure1",
						false );
				
				this.testPrintln( "\nout:" + cursor.getObject( "out" ) );

			}
		}
		
		this.checkOutputFile( );
		engine.shutdown( );
	}
	
	/**
	 * Test crosstab + table
	 * 
	 * @throws Exception
	 */
	@Test
    public void testNestedCrossTab3( ) throws Exception
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		ILevelDefinition level11 = hier1.createLevel( "level11" );
		ILevelDefinition level12 = hier1.createLevel( "level12" );
		ILevelDefinition level13 = hier1.createLevel( "level13" );
		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		ILevelDefinition level21 = hier2.createLevel( "level21" );

		createSortTestBindings( cqd );

		

		CubeSortDefinition sorter4 = new CubeSortDefinition( );
		sorter4.setExpression( "dimension[\"dimension1\"][\"level13\"]" );
		sorter4.setAxisQualifierLevels( null );
		sorter4.setAxisQualifierValues( null );
		sorter4.setTargetLevel( level13 );
		sorter4.setSortDirection( ISortDefinition.SORT_DESC );

		// Make UN before China.
		CubeSortDefinition sorter5 = new CubeSortDefinition( );
		sorter5.setExpression( "dimension[\"dimension1\"][\"level11\"]" );
		sorter5.setAxisQualifierLevels( null );
		sorter5.setAxisQualifierValues( null );
		sorter5.setTargetLevel( level11 );
		sorter5.setSortDirection( ISortDefinition.SORT_DESC );

		/*
		 * //sort on city. SortDefinition sorter3 = new SortDefinition();
		 * sorter3.setExpression( "dimension[\"dimension1\"][\"level12\"]" );
		 * sorter3.setSortDirection( ISortDefinition.SORT_DESC );
		 */
		// filter on year

		cqd.addSort( sorter4 );
		cqd.addSort( sorter5 );
		DataEngineImpl engine = (DataEngineImpl) DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults cqResults = pcq.execute( null );
		CubeCursor outerCursor = cqResults.getCubeCursor( );
		this.defineDataSourceDataSet( engine );
		IQueryDefinition query = this.createScriptDataSetQuery( );
		query.getFilters( ).add( new FilterDefinition(new ScriptExpression("row.column1 == row._outer[\"measure1\"]")) );
		IPreparedQuery pq = engine.prepare( query );

		
		int depth = 0;
		EdgeCursor edge1 = (EdgeCursor) ( outerCursor.getOrdinateEdge( ).get( 0 ) );
		EdgeCursor edge2 = (EdgeCursor) ( outerCursor.getOrdinateEdge( ).get( 1 ) );
		edge1.beforeFirst( );
		while ( edge2.next( ) )
		{
	/*		if( depth > 5 )
				break;
	*/		edge1.beforeFirst( );
			while ( edge1.next( ) )
			{
				depth++;
	/*			if( depth > 5 )
					break;
	*/			this.testPrintln( "\nOUTER RESULT:" + outerCursor.getObject( "measure1" ).toString( ) );
				IResultIterator it =  ((IQueryResults) pq.execute( cqResults, null )).getResultIterator( );
				while( it.next( ))
				{
					this.testPrintln( "\n    INNER RESULT:" + it.getString( "column1") );
				}
			}
		}
		
		this.checkOutputFile( );
		engine.shutdown( );
	}
	
	/**
	 * Test Table + subTable + crosstab
	 * 
	 * @throws Exception
	 */
	@Test
    public void testNestedCrossTab4( ) throws Exception
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		ILevelDefinition level11 = hier1.createLevel( "level11" );
		ILevelDefinition level12 = hier1.createLevel( "level12" );
		ILevelDefinition level13 = hier1.createLevel( "level13" );
		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		ILevelDefinition level21 = hier2.createLevel( "level21" );

		createSortTestBindings( cqd );

		// filter on year
		CubeFilterDefinition filter1 = new CubeFilterDefinition( new ScriptExpression( "data[\"measure1\"] > data._outer._outer[\"column1\"]" ) );
	

		CubeSortDefinition sorter4 = new CubeSortDefinition( );
		sorter4.setExpression( "dimension[\"dimension1\"][\"level13\"]" );
		sorter4.setAxisQualifierLevels( null );
		sorter4.setAxisQualifierValues( null );
		sorter4.setTargetLevel( level13 );
		sorter4.setSortDirection( ISortDefinition.SORT_DESC );

		// Make UN before China.
		CubeSortDefinition sorter5 = new CubeSortDefinition( );
		sorter5.setExpression( "dimension[\"dimension1\"][\"level11\"]" );
		sorter5.setAxisQualifierLevels( null );
		sorter5.setAxisQualifierValues( null );
		sorter5.setTargetLevel( level11 );
		sorter5.setSortDirection( ISortDefinition.SORT_DESC );

		/*
		 * //sort on city. SortDefinition sorter3 = new SortDefinition();
		 * sorter3.setExpression( "dimension[\"dimension1\"][\"level12\"]" );
		 * sorter3.setSortDirection( ISortDefinition.SORT_DESC );
		 */
		cqd.addFilter( filter1 );

		cqd.addSort( sorter4 );
		cqd.addSort( sorter5 );
		DataEngineImpl engine = (DataEngineImpl) DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );
		this.defineDataSourceDataSet( engine );
		IQueryDefinition query = this.createScriptDataSetQuery( );
		SubqueryDefinition subQuery = new SubqueryDefinition( "Test", query );
		subQuery.setApplyOnGroupFlag( false );
		subQuery.addBinding( new Binding("TestBinding", new ScriptExpression( "row._outer.column1")) );
		((QueryDefinition)query).addSubquery( subQuery );
		
		IPreparedQuery pq = engine.prepare( query );
		IQueryResults queryResults = pq.execute( null );
		IResultIterator it = queryResults.getResultIterator( );
		while ( it.next( ) )
		{
			IResultIterator subIt = it.getSecondaryIterator( "Test", null );
			IQueryResults subQueryResults = subIt.getQueryResults( );
			while ( subIt.next( ) )
			{
				if ( ((Number)it.getValue( "column1" )).intValue( ) == 55 
						||   ((Number)it.getValue( "column1" )).intValue( ) == 34 )
				{
					this.testPrintln( "\nOUTER RESULT:"
							+ subIt.getValue( "column1" ).toString( ) );
					IPreparedCubeQuery pcq = engine.prepare( cqd, null );
					ICubeQueryResults cqResults = (ICubeQueryResults) pcq.execute( subQueryResults,
							null );
					CubeCursor cursor = cqResults.getCubeCursor( );
					List columnEdgeBindingNames = new ArrayList( );
					columnEdgeBindingNames.add( "edge1level1" );
					columnEdgeBindingNames.add( "edge1level2" );
					columnEdgeBindingNames.add( "edge1level3" );

					printCube( cursor,
							"country_year_total",
							"city_year_total",
							"dist_total",
							"city_total",
							"country_total",
							"rowGrandTotal",
							"grandTotal",
							new String[]{
									"edge1level1", "edge1level2", "edge1level3"
							},
							"edge2level1",
							"measure1",
							false );

				}
			}
		}
		this.checkOutputFile( );
		engine.shutdown( );
	}
	
	/**
	 * Test Table + crosstab + crosstab filter against cube dimension
	 * 
	 * @throws Exception
	 */
	@Test
    public void testNestedCrossTab5( ) throws Exception
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		ILevelDefinition level11 = hier1.createLevel( "level11" );
		ILevelDefinition level12 = hier1.createLevel( "level12" );

		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		ILevelDefinition level21 = hier2.createLevel( "level21" );

		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		IBinding binding1 = new Binding( "edge1level1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "edge1level2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding4 = new Binding( "edge2level1" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "measure1" );
		binding5.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		
		binding5.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		binding5.addAggregateOn( "dimension[\"dimension2\"][\"level21\"]" );
		binding5.addAggregateOn( "dimension[\"dimension1\"][\"level11\"]" );
		binding5.addAggregateOn( "dimension[\"dimension1\"][\"level12\"]" );
		 

		cqd.addBinding( binding5 );

		IBinding binding6 = new Binding( "rowGrandTotal" );
		binding6.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		binding6.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		binding6.addAggregateOn( "dimension[\"dimension2\"][\"level21\"]" );
		cqd.addBinding( binding6 );

		IBinding binding7 = new Binding( "columnGrandTotal" );
		binding7.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		binding7.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		binding7.addAggregateOn( "dimension[\"dimension1\"][\"level11\"]" );
		binding7.addAggregateOn( "dimension[\"dimension1\"][\"level12\"]" );
		cqd.addBinding( binding7 );

		IBinding binding8 = new Binding( "grandTotal" );
		binding8.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		binding8.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		cqd.addBinding( binding8 );

		IBinding binding9 = new Binding( "country_year_total" );
		binding9.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		binding9.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		binding9.addAggregateOn( "dimension[\"dimension1\"][\"level11\"]" );
		binding9.addAggregateOn( "dimension[\"dimension2\"][\"level21\"]" );
		cqd.addBinding( binding9 );

		IBinding binding10 = new Binding( "city_year_total" );
		binding10.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		binding10.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		binding10.addAggregateOn( "dimension[\"dimension1\"][\"level11\"]" );
		binding10.addAggregateOn( "dimension[\"dimension1\"][\"level12\"]" );
		binding10.addAggregateOn( "dimension[\"dimension2\"][\"level21\"]" );
		cqd.addBinding( binding10 );

		IBinding binding11 = new Binding( "dist_total" );
		binding11.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		binding11.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		binding11.addAggregateOn( "dimension[\"dimension1\"][\"level11\"]" );
		binding11.addAggregateOn( "dimension[\"dimension1\"][\"level12\"]" );

		cqd.addBinding( binding11 );
		
		IBinding binding12 = new Binding( "city_total" );
		binding12.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		binding12.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		binding12.addAggregateOn( "dimension[\"dimension1\"][\"level11\"]" );
		binding12.addAggregateOn( "dimension[\"dimension1\"][\"level12\"]" );

		cqd.addBinding( binding12 );

		IBinding binding13 = new Binding( "country_total" );
		binding13.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		binding13.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		binding13.addAggregateOn( "dimension[\"dimension1\"][\"level11\"]" );

		cqd.addBinding( binding13 );

		// filter on year
		CubeFilterDefinition filter1 = new CubeFilterDefinition( new ConditionalExpression( "dimension[\"dimension1\"][\"level13\"]",
				IConditionalExpression.OP_EQ,
				"data._outer[\"column2\"]" ) );
	
		CubeFilterDefinition filter2 = new CubeFilterDefinition( new ScriptExpression( "data[\"measure1\"] > data._outer[\"column1\"]" ) );
		
		cqd.addFilter( filter1 );
		cqd.addFilter( filter2 );

		DataEngineImpl engine = (DataEngineImpl) DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );
		this.defineDataSourceDataSet( engine );
		IQueryDefinition query = this.createScriptDataSetQuery( );
		IPreparedQuery pq = engine.prepare( query );
		IQueryResults queryResults = pq.execute( null );
		IResultIterator it = queryResults.getResultIterator( );
		while ( it.next( ) )
		{
			if ( ((Number)it.getValue( "column1" )).intValue( ) == 55 
					||   ((Number)it.getValue( "column1" )).intValue( ) == 34 )
			{
				this.testPrintln( "\nOUTER RESULT:"+it.getValue( "column1" ).toString( ) );
				IPreparedCubeQuery pcq = engine.prepare( cqd, null );
				ICubeQueryResults cqResults =  (ICubeQueryResults) pcq.execute( queryResults, null );
				CubeCursor cursor = cqResults.getCubeCursor( );
				List columnEdgeBindingNames = new ArrayList( );
				columnEdgeBindingNames.add( "edge1level1" );
				columnEdgeBindingNames.add( "edge1level2" );

				printCube( cursor,
						"country_year_total",
						"city_year_total",
						"dist_total",
						"city_total",
						"country_total",
						"rowGrandTotal",
						"grandTotal",
						new String[]{
								"edge1level1", "edge1level2",  "edge1level2"
						},
						"edge2level1",
						"measure1",
						false );

			}
		}
		this.checkOutputFile( );
		engine.shutdown( );
	}
	/**
	 * Test use all dimension levels.
	 * 
	 * @throws Exception
	 */
	@Test
    public void testMirroredCrosstab( ) throws Exception
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		hier1.createLevel( "level11" );
		hier1.createLevel( "level12" );
		ILevelDefinition leve113 = hier1.createLevel( "level13" );
		columnEdge.setMirrorStartingLevel( leve113 );
		
		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		hier2.createLevel( "level21" );

		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		IBinding binding1 = new Binding( "edge1level1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "edge1level2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding3 = new Binding( "edge1level3" );

		binding3.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level13\"]" ) );
		cqd.addBinding( binding3 );

		IBinding binding4 = new Binding( "edge2level1" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "measure1" );
		binding5.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		cqd.addBinding( binding5 );

		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( createPresentationContext( ) );
		this.createCube( engine );
		IPreparedCubeQuery pcq = engine.prepare( cqd, null );
		ICubeQueryResults queryResults = pcq.execute( null );
		CubeCursor cursor = queryResults.getCubeCursor( );
		List columnEdgeBindingNames = new ArrayList( );
		columnEdgeBindingNames.add( "edge1level1" );
		columnEdgeBindingNames.add( "edge1level2" );
		columnEdgeBindingNames.add( "edge1level3" );
		List rowEdgeBindingNames = new ArrayList( );
		rowEdgeBindingNames.add( "edge2level1" );

		this.printCube( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				"measure1" );
		engine.shutdown( );
	}
	@Test
    public void testSubQueryWithNestAggregation( ) throws BirtException, IOException, OLAPException
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		ILevelDefinition level11 = hier1.createLevel( "level11" );
		ILevelDefinition level12 = hier1.createLevel( "level12" );
		ILevelDefinition level13 = hier1.createLevel( "level13" );
		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		ILevelDefinition level21 = hier2.createLevel( "level21" );

		createSortTestBindings( cqd );
		IBinding binding = new Binding( "maxTotal" );
		binding.setExpression( new ScriptExpression( "data[\"country_year_total\"]"  ) );
		binding.setAggrFunction( IBuildInAggregation.TOTAL_MAX_FUNC );
				
		ICubeOperation cubeOperation1 = new AddingNestAggregations( new IBinding[]{
			binding
		} );
		cqd.addCubeOperation( cubeOperation1 );
	
		Context cx = null;
		try
		{
			cx = Context.enter( );
			Scriptable sharedScope = cx.initStandardObjects( );

			DataEngineImpl engine = (DataEngineImpl) DataEngine.newDataEngine( DataEngineContext.newInstance( DataEngineContext.DIRECT_PRESENTATION,
					sharedScope,
					null,
					null ) );
			this.createCube( engine );
			IPreparedCubeQuery pcq = engine.prepare( cqd, null );
			ICubeQueryResults cqResults = pcq.execute( sharedScope );
			ICubeCursor cubeCursor = (ICubeCursor) cqResults.getCubeCursor( );

			Scriptable subScope = cx.newObject( sharedScope );
			subScope.setParentScope( sharedScope );
			
			ISubCubeQueryDefinition subQuery1 = new SubCubeQueryDefinition( "",
					"dimension[\"dimension1\"][\"level11\"]",
					"dimension[\"dimension2\"][\"level21\"]" );
			EdgeCursor edge1 = (EdgeCursor) ( cubeCursor.getOrdinateEdge( ).get( 0 ) );
			EdgeCursor edge2 = (EdgeCursor) ( cubeCursor.getOrdinateEdge( ).get( 1 ) );
			edge1.beforeFirst( );
			ICubeCursor subCubeCursor = null;
			this.testPrint("\n All total values: ");
			while ( edge2.next( ) )
			{
				edge1.beforeFirst( );
				while ( edge1.next( ) )
				{
					// subQuery1
					subCubeCursor = engine.prepare( subQuery1, null )
							.execute( cqResults, subScope )
							.getCubeCursor( );
					EdgeCursor subEdge1 = (EdgeCursor) ( subCubeCursor.getOrdinateEdge( ).get( 0 ) );
					EdgeCursor subEdge2 = (EdgeCursor) ( subCubeCursor.getOrdinateEdge( ).get( 1 ) );
					subEdge2.first( );
					subEdge1.first( );
					List dimensions = edge1.getDimensionCursor( );
					int start = (int) ( (DimensionCursor) dimensions.get( 0 ) ).getEdgeStart( );
					if ( start == edge1.getPosition( ) )
					{
						this.testPrint( subCubeCursor.getObject( "country_year_total" )
								.toString( )
								+ "   " );
					}
				}
			}
			this.testPrint("\n All total values: ");
			this.testPrint( subCubeCursor.getObject( "maxTotal" ).toString( ));
			this.checkOutputFile( );
			engine.shutdown( );
		}
		finally
		{
			if ( cx != null )
				cx.exit( );
		}
	}
	
	/**
	 * Test subQuery in crosstab
	 * 
	 * @throws Exception
	 */
	@Test
    public void testSubQuery1( ) throws Exception
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		ILevelDefinition level11 = hier1.createLevel( "level11" );
		ILevelDefinition level12 = hier1.createLevel( "level12" );
		ILevelDefinition level13 = hier1.createLevel( "level13" );
		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		ILevelDefinition level21 = hier2.createLevel( "level21" );

		createSortTestBindings( cqd );

		Context cx = null;
		try
		{
			cx = Context.enter( );
			Scriptable sharedScope = cx.initStandardObjects( );

			DataEngineImpl engine = (DataEngineImpl) DataEngine.newDataEngine( DataEngineContext.newInstance( DataEngineContext.DIRECT_PRESENTATION,
					sharedScope,
					null,
					null ) );
			this.createCube( engine );
			IPreparedCubeQuery pcq = engine.prepare( cqd, null );
			ICubeQueryResults cqResults = pcq.execute( sharedScope );
			ICubeCursor cubeCursor = (ICubeCursor) cqResults.getCubeCursor( );

			Scriptable subScope = cx.newObject( sharedScope );
			subScope.setParentScope( sharedScope );
			
			ISubCubeQueryDefinition subQuery1 = new SubCubeQueryDefinition( "",
					"dimension[\"dimension1\"][\"level11\"]",
					"dimension[\"dimension2\"][\"level21\"]" );

			ISubCubeQueryDefinition subQuery2 = new SubCubeQueryDefinition( "",
					"dimension[\"dimension1\"][\"level12\"]",
					"dimension[\"dimension2\"][\"level21\"]" );

			ISubCubeQueryDefinition subQuery3 = new SubCubeQueryDefinition( "",
					"dimension[\"dimension1\"][\"level13\"]",
					"dimension[\"dimension2\"][\"level21\"]" );

			ISubCubeQueryDefinition subQuery4 = new SubCubeQueryDefinition( "",
					"dimension[\"dimension1\"][\"level11\"]",
					null );

			ISubCubeQueryDefinition subQuery5 = new SubCubeQueryDefinition( "",
					"dimension[\"dimension1\"][\"level12\"]",
					null );

			ISubCubeQueryDefinition subQuery6 = new SubCubeQueryDefinition( "",
					"dimension[\"dimension1\"][\"level13\"]",
					null );

			ISubCubeQueryDefinition subQuery7 = new SubCubeQueryDefinition( "",
					null,
					"dimension[\"dimension2\"][\"level21\"]" );

			ISubCubeQueryDefinition subQuery8 = new SubCubeQueryDefinition( "",
					null,
					null );
			
			EdgeCursor edge1 = (EdgeCursor) ( cubeCursor.getOrdinateEdge( ).get( 0 ) );
			EdgeCursor edge2 = (EdgeCursor) ( cubeCursor.getOrdinateEdge( ).get( 1 ) );
			edge1.beforeFirst( );
			while ( edge2.next( ) )
			{
				edge1.beforeFirst( );
				while ( edge1.next( ) )
				{
					this.testPrintln( "\n\nParent result:" +
							cubeCursor.getObject( "measure1" ).toString( ) );
					
					//subQuery1
					ICubeCursor subCubeCursor = engine.prepare( subQuery1, null )
							.execute( cqResults, subScope )
							.getCubeCursor( );
					EdgeCursor subEdge1 = (EdgeCursor) ( subCubeCursor.getOrdinateEdge( ).get( 0 ) );
					EdgeCursor subEdge2 = (EdgeCursor) ( subCubeCursor.getOrdinateEdge( ).get( 1 ) );
					subEdge2.beforeFirst( );
					this.testPrintln( "\nsubQuery Result1 " );
					while ( subEdge2.next( ) )
					{
						List dimension = subEdge2.getDimensionCursor( );

						for ( int i = 0; i < dimension.size( ); i++ )
						{
							DimensionCursor dim = (DimensionCursor) dimension.get( i );
							System.out.println( "####dim"
									+ i + "Edge start=" + dim.getEdgeStart( )
									+ "Edge end=" + dim.getEdgeEnd( ) + "   " );
						}
						subEdge1.beforeFirst( );
						while ( subEdge1.next( ) )
						{
							List dimensions = subEdge1.getDimensionCursor( );

							for ( int i = 0; i < dimensions.size( ); i++ )
							{
								DimensionCursor dim = (DimensionCursor) dimensions.get( i );
								System.out.println( "****dim"
										+ i + "Edge start="
										+ dim.getEdgeStart( ) + "Edge end="
										+ dim.getEdgeEnd( ) + "   " );
							}
							this.testPrint( subCubeCursor.getObject( "measure1" )
									.toString( ) +
									"   " );
						}
					}

					// subQuery2
					subCubeCursor = engine.prepare( subQuery2, null )
							.execute( cqResults, subScope )
							.getCubeCursor( );
					subEdge1 = (EdgeCursor) ( subCubeCursor.getOrdinateEdge( ).get( 0 ) );
					subEdge2 = (EdgeCursor) ( subCubeCursor.getOrdinateEdge( ).get( 1 ) );
					subEdge2.beforeFirst( );
					this.testPrintln( "\nsubQuery Result2 " );
					while ( subEdge2.next( ) )
					{
						subEdge1.beforeFirst( );
						while ( subEdge1.next( ) )
						{
							this.testPrint( subCubeCursor.getObject( "measure1" )
									.toString( ) +
									" " );
						}
					}
					

					// subQuery3
					subCubeCursor = engine.prepare( subQuery3, null )
							.execute( cqResults, subScope )
							.getCubeCursor( );
					subEdge1 = (EdgeCursor) ( subCubeCursor.getOrdinateEdge( ).get( 0 ) );
					subEdge2 = (EdgeCursor) ( subCubeCursor.getOrdinateEdge( ).get( 1 ) );
					subEdge2.beforeFirst( );
					this.testPrintln( "\nsubQuery Result3 " );
					while ( subEdge2.next( ) )
					{
						subEdge1.beforeFirst( );
						while ( subEdge1.next( ) )
						{
							this.testPrint( subCubeCursor.getObject( "measure1" )
									.toString( ) +
									" " );
						}
					}
					
					// subQuery4
					subCubeCursor = engine.prepare( subQuery4, null )
							.execute( cqResults, subScope )
							.getCubeCursor( );
					subEdge1 = (EdgeCursor) ( subCubeCursor.getOrdinateEdge( ).get( 0 ) );
					subEdge2 = (EdgeCursor) ( subCubeCursor.getOrdinateEdge( ).get( 1 ) );
					subEdge2.beforeFirst( );
					this.testPrintln( "\nsubQuery Result4 " );
					while ( subEdge2.next( ) )
					{
						subEdge1.beforeFirst( );
						while ( subEdge1.next( ) )
						{
							this.testPrint( subCubeCursor.getObject( "measure1" )
									.toString( ) +
									" " );
						}
					}
					
					// subQuery5
					subCubeCursor = engine.prepare( subQuery5, null )
							.execute( cqResults, subScope )
							.getCubeCursor( );
					subEdge1 = (EdgeCursor) ( subCubeCursor.getOrdinateEdge( ).get( 0 ) );
					subEdge2 = (EdgeCursor) ( subCubeCursor.getOrdinateEdge( ).get( 1 ) );
					subEdge2.beforeFirst( );
					this.testPrintln( "\nsubQuery Result5 " );
					while ( subEdge2.next( ) )
					{
						subEdge1.beforeFirst( );
						while ( subEdge1.next( ) )
						{
							this.testPrint( subCubeCursor.getObject( "measure1" )
									.toString( ) +
									" " );
						}
					}
					// subQuery6
					subCubeCursor = engine.prepare( subQuery6, null )
							.execute( cqResults, subScope )
							.getCubeCursor( );
					subEdge1 = (EdgeCursor) ( subCubeCursor.getOrdinateEdge( ).get( 0 ) );
					subEdge2 = (EdgeCursor) ( subCubeCursor.getOrdinateEdge( ).get( 1 ) );
					subEdge2.beforeFirst( );
					this.testPrintln( "\nsubQuery Result6 " );
					while ( subEdge2.next( ) )
					{
						subEdge1.beforeFirst( );
						while ( subEdge1.next( ) )
						{
							this.testPrint( subCubeCursor.getObject( "measure1" )
									.toString( ) +
									" " );
						}
					}					
					// subQuery7
					subCubeCursor = engine.prepare( subQuery7, null )
							.execute( cqResults, subScope )
							.getCubeCursor( );
					subEdge1 = (EdgeCursor) ( subCubeCursor.getOrdinateEdge( ).get( 0 ) );
					subEdge2 = (EdgeCursor) ( subCubeCursor.getOrdinateEdge( ).get( 1 ) );
					subEdge2.beforeFirst( );
					this.testPrintln( "\nsubQuery Result7 " );
					while ( subEdge2.next( ) )
					{
						subEdge1.beforeFirst( );
						while ( subEdge1.next( ) )
						{
							this.testPrint( subCubeCursor.getObject( "measure1" )
									.toString( ) +
									" " );
						}
					}
					
					// subQuery8
					subCubeCursor = engine.prepare( subQuery8, null )
							.execute( cqResults, subScope )
							.getCubeCursor( );
					subEdge1 = (EdgeCursor) ( subCubeCursor.getOrdinateEdge( ).get( 0 ) );
					subEdge2 = (EdgeCursor) ( subCubeCursor.getOrdinateEdge( ).get( 1 ) );
					subEdge2.beforeFirst( );
					this.testPrintln( "\nsubQuery Result8 " );
					while ( subEdge2.next( ) )
					{
						subEdge1.beforeFirst( );
						while ( subEdge1.next( ) )
						{
							this.testPrint( subCubeCursor.getObject( "measure1" )
									.toString( ) +
									" " );
						}
					}
				}
			}
			close( cubeCursor );
			engine.shutdown( );
		}
		finally
		{
			if ( cx != null )
				cx.exit( );
		}
		this.checkOutputFile( );

	}

	/**
	 * Test subQuery with mirrored level in crosstab
	 * 
	 * @throws Exception
	 */
	@Test
    public void testSubQuery2( ) throws Exception
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		ILevelDefinition level11 = hier1.createLevel( "level11" );
		ILevelDefinition level12 = hier1.createLevel( "level12" );
		ILevelDefinition level13 = hier1.createLevel( "level13" );
		columnEdge.setMirrorStartingLevel( level13 );
		
		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		ILevelDefinition level21 = hier2.createLevel( "level21" );

		createSortTestBindings( cqd );

		Context cx = null;
		try
		{
			cx = Context.enter( );
			Scriptable sharedScope = cx.initStandardObjects( );

			DataEngineImpl engine = (DataEngineImpl) DataEngine.newDataEngine( DataEngineContext.newInstance( DataEngineContext.DIRECT_PRESENTATION,
					sharedScope,
					null,
					null ) );
			this.createCube( engine );
			IPreparedCubeQuery pcq = engine.prepare( cqd, null );
			ICubeQueryResults cqResults = pcq.execute( sharedScope );
			ICubeCursor cubeCursor = (ICubeCursor) cqResults.getCubeCursor( );

			Scriptable subScope = cx.newObject( sharedScope );
			subScope.setParentScope( sharedScope );

			ISubCubeQueryDefinition subQuery1 = new SubCubeQueryDefinition( "",
					"dimension[\"dimension1\"][\"level11\"]",
					"dimension[\"dimension2\"][\"level21\"]" );

			ISubCubeQueryDefinition subQuery2 = new SubCubeQueryDefinition( "",
					"dimension[\"dimension1\"][\"level12\"]",
					"dimension[\"dimension2\"][\"level21\"]" );

			ISubCubeQueryDefinition subQuery3 = new SubCubeQueryDefinition( "",
					"dimension[\"dimension1\"][\"level13\"]",
					"dimension[\"dimension2\"][\"level21\"]" );

			ISubCubeQueryDefinition subQuery4 = new SubCubeQueryDefinition( "",
					"dimension[\"dimension1\"][\"level11\"]",
					null );

			ISubCubeQueryDefinition subQuery5 = new SubCubeQueryDefinition( "",
					"dimension[\"dimension1\"][\"level12\"]",
					null );

			ISubCubeQueryDefinition subQuery6 = new SubCubeQueryDefinition( "",
					"dimension[\"dimension1\"][\"level13\"]",
					null );

			ISubCubeQueryDefinition subQuery7 = new SubCubeQueryDefinition( "",
					null,
					"dimension[\"dimension2\"][\"level21\"]" );

			ISubCubeQueryDefinition subQuery8 = new SubCubeQueryDefinition( "",
					null,
					null );
			int depth = 0;
			Object value;
			EdgeCursor edge1 = (EdgeCursor) ( cubeCursor.getOrdinateEdge( ).get( 0 ) );
			EdgeCursor edge2 = (EdgeCursor) ( cubeCursor.getOrdinateEdge( ).get( 1 ) );
			edge1.beforeFirst( );
			while ( edge2.next( ) )
			{
				edge1.beforeFirst( );
				while ( edge1.next( ) )
				{
					value = cubeCursor.getObject( "measure1" );
					if ( value != null )
						this.testPrintln( "\n\nParent result:" + value );
					else
						continue;
					
					ICubeCursor subCubeCursor = null;
					EdgeCursor subEdge1, subEdge2;
					
					// subQuery1
					subCubeCursor = engine.prepare( subQuery1, null )
							.execute( cqResults, subScope )
							.getCubeCursor( );
					subEdge1 = (EdgeCursor) ( subCubeCursor.getOrdinateEdge( ).get( 0 ) );
					subEdge2 = (EdgeCursor) ( subCubeCursor.getOrdinateEdge( ).get( 1 ) );
					subEdge2.beforeFirst( );
					this.testPrintln( "\nsubQuery Result1 " );
					while ( subEdge2.next( ) )
					{
						subEdge1.beforeFirst( );
						while ( subEdge1.next( ) )
						{
							this.testPrint( subCubeCursor.getObject( "measure1" )
										+ "   " );
						}
					}

					// subQuery2
					subCubeCursor = engine.prepare( subQuery2, null )
							.execute( cqResults, subScope )
							.getCubeCursor( );
					subEdge1 = (EdgeCursor) ( subCubeCursor.getOrdinateEdge( ).get( 0 ) );
					subEdge2 = (EdgeCursor) ( subCubeCursor.getOrdinateEdge( ).get( 1 ) );
					subEdge2.beforeFirst( );
					this.testPrintln( "\nsubQuery Result2 " );
					while ( subEdge2.next( ) )
					{
						subEdge1.beforeFirst( );
						while ( subEdge1.next( ) )
						{
							this.testPrint( subCubeCursor.getObject( "measure1" )
									+ "   " );
						}
					}
					

					// subQuery3
					subCubeCursor = engine.prepare( subQuery3, null )
							.execute( cqResults, subScope )
							.getCubeCursor( );
					subEdge1 = (EdgeCursor) ( subCubeCursor.getOrdinateEdge( ).get( 0 ) );
					subEdge2 = (EdgeCursor) ( subCubeCursor.getOrdinateEdge( ).get( 1 ) );
					subEdge2.beforeFirst( );
					this.testPrintln( "\nsubQuery Result3 " );
					while ( subEdge2.next( ) )
					{
						subEdge1.beforeFirst( );
						while ( subEdge1.next( ) )
						{
								this.testPrint(  subCubeCursor.getObject( "measure1" ) + "   " );	
						}
					}
					
					// subQuery4
					subCubeCursor = engine.prepare( subQuery4, null )
							.execute( cqResults, subScope )
							.getCubeCursor( );
					subEdge1 = (EdgeCursor) ( subCubeCursor.getOrdinateEdge( ).get( 0 ) );
					subEdge2 = (EdgeCursor) ( subCubeCursor.getOrdinateEdge( ).get( 1 ) );
					subEdge2.beforeFirst( );
					this.testPrintln( "\nsubQuery Result4 " );
					while ( subEdge2.next( ) )
					{
						subEdge1.beforeFirst( );
						while ( subEdge1.next( ) )
						{
							this.testPrint( subCubeCursor.getObject( "measure1" ) + "   " );	
						}
					}
					
					// subQuery5
					subCubeCursor = engine.prepare( subQuery5, null )
							.execute( cqResults, subScope )
							.getCubeCursor( );
					subEdge1 = (EdgeCursor) ( subCubeCursor.getOrdinateEdge( ).get( 0 ) );
					subEdge2 = (EdgeCursor) ( subCubeCursor.getOrdinateEdge( ).get( 1 ) );
					subEdge2.beforeFirst( );
					this.testPrintln( "\nsubQuery Result5 " );
					while ( subEdge2.next( ) )
					{

						subEdge1.beforeFirst( );
						while ( subEdge1.next( ) )
						{
							this.testPrint( subCubeCursor.getObject( "measure1" ) + "   " );	
						}
					}
					// subQuery6
					subCubeCursor = engine.prepare( subQuery6, null )
							.execute( cqResults, subScope )
							.getCubeCursor( );
					subEdge1 = (EdgeCursor) ( subCubeCursor.getOrdinateEdge( ).get( 0 ) );
					subEdge2 = (EdgeCursor) ( subCubeCursor.getOrdinateEdge( ).get( 1 ) );
					subEdge2.beforeFirst( );
					this.testPrintln( "\nsubQuery Result6 " );
					while ( subEdge2.next( ) )
					{
						subEdge1.beforeFirst( );
						while ( subEdge1.next( ) )
						{
							this.testPrint( subCubeCursor.getObject( "measure1" ) + "   " );	
						}
					}					
					// subQuery7
					subCubeCursor = engine.prepare( subQuery7, null )
							.execute( cqResults, subScope )
							.getCubeCursor( );
					subEdge1 = (EdgeCursor) ( subCubeCursor.getOrdinateEdge( ).get( 0 ) );
					subEdge2 = (EdgeCursor) ( subCubeCursor.getOrdinateEdge( ).get( 1 ) );
					subEdge2.beforeFirst( );
					this.testPrintln( "\nsubQuery Result7 " );
					while ( subEdge2.next( ) )
					{
						subEdge1.beforeFirst( );
						while ( subEdge1.next( ) )
						{
							this.testPrint( subCubeCursor.getObject( "measure1" ) + "   " );	
						}
					}
					
					// subQuery8
					subCubeCursor = engine.prepare( subQuery8, null )
							.execute( cqResults, subScope )
							.getCubeCursor( );
					subEdge1 = (EdgeCursor) ( subCubeCursor.getOrdinateEdge( ).get( 0 ) );
					subEdge2 = (EdgeCursor) ( subCubeCursor.getOrdinateEdge( ).get( 1 ) );
					subEdge2.beforeFirst( );
					this.testPrintln( "\nsubQuery Result8 " );
					while ( subEdge2.next( ) )
					{
						subEdge1.beforeFirst( );
						while ( subEdge1.next( ) )
						{
							this.testPrint( subCubeCursor.getObject( "measure1" ) + "   " );	
						}
					}
				}
			}
			close( cubeCursor );
			engine.shutdown( );
		}
		finally
		{
			if ( cx != null )
				cx.exit( );
		}
		this.checkOutputFile( );
	}
	
	/**
	 * Test subQuery in crosstab
	 * 
	 * @throws Exception
	 */
	@Test
    public void testSubQuery3( ) throws Exception
	{
		ICubeQueryDefinition cqd = new CubeQueryDefinition( cubeName );
		IEdgeDefinition columnEdge = cqd.createEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = cqd.createEdge( ICubeQueryDefinition.ROW_EDGE );
		IDimensionDefinition dim1 = columnEdge.createDimension( "dimension1" );
		IHierarchyDefinition hier1 = dim1.createHierarchy( "dimension1" );
		ILevelDefinition level11 = hier1.createLevel( "level11" );
		ILevelDefinition level12 = hier1.createLevel( "level12" );
		ILevelDefinition level13 = hier1.createLevel( "level13" );
		IDimensionDefinition dim2 = rowEdge.createDimension( "dimension2" );
		IHierarchyDefinition hier2 = dim2.createHierarchy( "dimension2" );
		ILevelDefinition level21 = hier2.createLevel( "level21" );

		createSortTestBindings( cqd );

		Context cx = null;
		try
		{
			cx = Context.enter( );
			Scriptable sharedScope = cx.initStandardObjects( );

			DataEngineImpl engine = (DataEngineImpl) DataEngine.newDataEngine( DataEngineContext.newInstance( DataEngineContext.DIRECT_PRESENTATION,
					sharedScope,
					null,
					null ) );
			this.createCube( engine );
			IPreparedCubeQuery pcq = engine.prepare( cqd, null );
			ICubeQueryResults cqResults = pcq.execute( sharedScope );
			ICubeCursor cubeCursor = (ICubeCursor) cqResults.getCubeCursor( );

			Scriptable subScope = cx.newObject( sharedScope );
			subScope.setParentScope( sharedScope );

			Scriptable subSubScope = cx.newObject( sharedScope );
			subSubScope.setParentScope( sharedScope );

			ISubCubeQueryDefinition subQuery1 = new SubCubeQueryDefinition( "",
					"dimension[\"dimension1\"][\"level11\"]",
					"dimension[\"dimension2\"][\"level21\"]" );
			ISubCubeQueryDefinition subQuery2 = new SubCubeQueryDefinition( "",
					"dimension[\"dimension1\"][\"level12\"]",
					"dimension[\"dimension2\"][\"level21\"]" );
				
			int depth = 0;
			EdgeCursor edge1 = (EdgeCursor) ( cubeCursor.getOrdinateEdge( ).get( 0 ) );
			EdgeCursor edge2 = (EdgeCursor) ( cubeCursor.getOrdinateEdge( ).get( 1 ) );
			edge1.beforeFirst( );
			while ( edge2.next( ) )
			{
				edge1.beforeFirst( );
				while ( edge1.next( ) )
				{
					this.testPrintln( "\n\nParent result:" +
							cubeCursor.getObject( "measure1" ).toString( ) );
					
					// subQuery1
					ICubeQueryResults subResult = engine.prepare( subQuery1,
							null ).execute( cqResults, subScope );
					ICubeCursor subCubeCursor = subResult.getCubeCursor( );
					EdgeCursor subEdge1 = (EdgeCursor) ( subCubeCursor.getOrdinateEdge( ).get( 0 ) );
					EdgeCursor subEdge2 = (EdgeCursor) ( subCubeCursor.getOrdinateEdge( ).get( 1 ) );
					subEdge2.beforeFirst( );
					
					while ( subEdge2.next( ) )
					{
						subEdge1.beforeFirst( );
						while ( subEdge1.next( ) )
						{
							this.testPrintln( "\nsubQuery Result1 " +
									subCubeCursor.getObject( "measure1" )
											.toString( ) + "   " );
							ICubeCursor subSubCubeCursor = engine.prepare( subQuery2,
									null )
									.execute( subResult, subScope )
									.getCubeCursor( );
							EdgeCursor subSubEdge1 = (EdgeCursor) ( subSubCubeCursor.getOrdinateEdge( ).get( 0 ) );
							EdgeCursor subSubEdge2 = (EdgeCursor) ( subSubCubeCursor.getOrdinateEdge( ).get( 1 ) );
							subSubEdge2.beforeFirst( );
							this.testPrintln( "\nsubSubQuery Result1 " );
							while ( subSubEdge2.next( ) )
							{
								subSubEdge1.beforeFirst( );
								while ( subSubEdge1.next( ) )
								{
									depth++;

									this.testPrint( subSubCubeCursor.getObject( "measure1" )
											.toString( ) +
											"   " );
								}
							}
						}
					}
				}
			}
			close( cubeCursor );
			engine.shutdown( );
		}
		finally
		{
			if ( cx != null )
				cx.exit( );
		}
		this.checkOutputFile( );
	}
	
	/**
	 * test year to date function
	 * 
	 * @throws Exception
	 */
	@Test
    public void testRelativeTimePeriod1() throws Exception {
		ICubeQueryDefinition cqd = new CubeQueryDefinition(DateCube.cubeName);

		IEdgeDefinition rowEdge = cqd
				.createEdge(ICubeQueryDefinition.COLUMN_EDGE);
		IDimensionDefinition productLineDim1 = rowEdge
				.createDimension("dimension2");
		IHierarchyDefinition porductLineHie1 = productLineDim1
				.createHierarchy("dimension2");
		porductLineHie1.createLevel("level21");

		IEdgeDefinition columnEdge = cqd
				.createEdge(ICubeQueryDefinition.ROW_EDGE);
		IDimensionDefinition dateDim = columnEdge.createDimension("dimension1");
		IHierarchyDefinition dateHier = dateDim.createHierarchy("dimension1");

		dateHier.createLevel("level11");
		dateHier.createLevel("level12");
		dateHier.createLevel("level13");
		dateHier.createLevel("level14");

		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		TimeFunction timeFunction = new TimeFunction();
		TimePeriod timePeriod = new TimePeriod(0, TimePeriodType.YEAR);
		ReferenceDate referenceDate = new ReferenceDate(new GregorianCalendar(
				1999, 8, 20).getTime());
		timeFunction.setReferenceDate(referenceDate);
		timeFunction.setBaseTimePeriod(timePeriod);
		timeFunction.setTimeDimension("dimension1");

		IBinding binding2 = new Binding("measure1");
		binding2.setExpression(new ScriptExpression("measure[\"measure1\"]"));

		binding2.setTimeFunction(timeFunction);
		binding2.setAggrFunction(IBuildInAggregation.TOTAL_SUM_FUNC);
		binding2.addAggregateOn("dimension[\"dimension1\"][\"level11\"]");
		binding2.addAggregateOn("dimension[\"dimension1\"][\"level12\"]");
		binding2.addAggregateOn("dimension[\"dimension1\"][\"level13\"]");
		binding2.addAggregateOn("dimension[\"dimension1\"][\"level14\"]");
		binding2.addAggregateOn("dimension[\"dimension2\"][\"level21\"]");

		cqd.addBinding(binding2);

		DateCube util = new DateCube();
		DataEngineImpl engine = (DataEngineImpl) DataEngine
				.newDataEngine(createPresentationContext());
		util.createCube(engine);

		ICube cube = util.getCube(DateCube.cubeName, engine);
		BirtCubeView cubeView = new BirtCubeView(new CubeQueryExecutor(null,
				cqd, engine.getSession(), new ImporterTopLevel(),
				engine.getContext()), cube, null, null);

		CubeCursor cursor = cubeView.getCubeCursor(new StopSign(), cube);

		List columnEdgeBindingNames = new ArrayList();

		List rowEdgeBindingNames = new ArrayList();

		this.printCube(cursor, columnEdgeBindingNames, rowEdgeBindingNames,
				"measure1");
		engine.shutdown();

	}

	/**
	 * test year to date function
	 * 
	 * @throws Exception
	 */
	@Test
    public void testRelativeTimePeriod2() throws Exception {
		ICubeQueryDefinition cqd = new CubeQueryDefinition(DateCube.cubeName);

		IEdgeDefinition rowEdge = cqd
				.createEdge(ICubeQueryDefinition.COLUMN_EDGE);
		IDimensionDefinition productLineDim1 = rowEdge
				.createDimension("dimension2");
		IHierarchyDefinition porductLineHie1 = productLineDim1
				.createHierarchy("dimension2");
		porductLineHie1.createLevel("level21");

		IEdgeDefinition columnEdge = cqd
				.createEdge(ICubeQueryDefinition.ROW_EDGE);
		IDimensionDefinition dateDim = columnEdge.createDimension("dimension1");
		IHierarchyDefinition dateHier = dateDim.createHierarchy("dimension1");

		dateHier.createLevel("level11");
		dateHier.createLevel("level12");
		dateHier.createLevel("level13");
		dateHier.createLevel("level14");

		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		TimeFunction timeFunction = new TimeFunction();
		TimePeriod timePeriod = new TimePeriod(0, TimePeriodType.YEAR);
		ReferenceDate referenceDate = new ReferenceDate(new GregorianCalendar(
				1999, 7, 19).getTime());
		timeFunction.setReferenceDate(referenceDate);
		timeFunction.setBaseTimePeriod(timePeriod);
		timeFunction.setTimeDimension("dimension1");

		IBinding binding2 = new Binding("measure1");
		binding2.setExpression(new ScriptExpression("measure[\"measure1\"]"));

		binding2.setTimeFunction(timeFunction);
		binding2.setAggrFunction(IBuildInAggregation.TOTAL_SUM_FUNC);
		binding2.addAggregateOn("dimension[\"dimension1\"][\"level11\"]");
		binding2.addAggregateOn("dimension[\"dimension2\"][\"level21\"]");

		cqd.addBinding(binding2);

		DateCube util = new DateCube();
		DataEngineImpl engine = (DataEngineImpl) DataEngine
				.newDataEngine(createPresentationContext());
		util.createCube(engine);

		ICube cube = util.getCube(DateCube.cubeName, engine);
		BirtCubeView cubeView = new BirtCubeView(new CubeQueryExecutor(null,
				cqd, engine.getSession(), new ImporterTopLevel(),
				engine.getContext()), cube, null, null);

		CubeCursor cursor = cubeView.getCubeCursor(new StopSign(), cube);

		List columnEdgeBindingNames = new ArrayList();

		List rowEdgeBindingNames = new ArrayList();

		this.printCube(cursor, columnEdgeBindingNames, rowEdgeBindingNames,
				"measure1");
		engine.shutdown();

	}

	/**
	 * test month to date function
	 * 
	 * @throws Exception
	 */
	@Test
    public void testRelativeTimePeriod3() throws Exception {
		ICubeQueryDefinition cqd = new CubeQueryDefinition(DateCube.cubeName);

		IEdgeDefinition rowEdge = cqd
				.createEdge(ICubeQueryDefinition.COLUMN_EDGE);
		IDimensionDefinition productLineDim1 = rowEdge
				.createDimension("dimension2");
		IHierarchyDefinition porductLineHie1 = productLineDim1
				.createHierarchy("dimension2");
		porductLineHie1.createLevel("level21");

		IEdgeDefinition columnEdge = cqd
				.createEdge(ICubeQueryDefinition.ROW_EDGE);
		IDimensionDefinition dateDim = columnEdge.createDimension("dimension1");
		IHierarchyDefinition dateHier = dateDim.createHierarchy("dimension1");

		dateHier.createLevel("level11");
		dateHier.createLevel("level12");
		dateHier.createLevel("level13");
		dateHier.createLevel("level14");
		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		TimeFunction timeFunction = new TimeFunction();
		TimePeriod timePeriod = new TimePeriod(0, TimePeriodType.MONTH);
		ReferenceDate referenceDate = new ReferenceDate(new GregorianCalendar(
				1998, 7, 19).getTime());
		timeFunction.setReferenceDate(referenceDate);
		timeFunction.setBaseTimePeriod(timePeriod);
		timeFunction.setTimeDimension("dimension1");

		IBinding binding2 = new Binding("measure1");
		binding2.setExpression(new ScriptExpression("measure[\"measure1\"]"));

		binding2.setTimeFunction(timeFunction);
		binding2.setAggrFunction(IBuildInAggregation.TOTAL_SUM_FUNC);
		binding2.addAggregateOn("dimension[\"dimension1\"][\"level11\"]");
		binding2.addAggregateOn("dimension[\"dimension1\"][\"level12\"]");
		binding2.addAggregateOn("dimension[\"dimension1\"][\"level13\"]");

		cqd.addBinding(binding2);

		DateCube util = new DateCube();
		DataEngineImpl engine = (DataEngineImpl) DataEngine
				.newDataEngine(createPresentationContext());
		util.createCube(engine);

		ICube cube = util.getCube(DateCube.cubeName, engine);
		BirtCubeView cubeView = new BirtCubeView(new CubeQueryExecutor(null,
				cqd, engine.getSession(), new ImporterTopLevel(),
				engine.getContext()), cube, null, null);

		CubeCursor cursor = cubeView.getCubeCursor(new StopSign(), cube);

		List columnEdgeBindingNames = new ArrayList();

		List rowEdgeBindingNames = new ArrayList();

		this.printCube(cursor, columnEdgeBindingNames, rowEdgeBindingNames,
				"measure1");
		engine.shutdown();

	}

	/**
	 * test week to date function
	 * 
	 * @throws Exception
	 */
	@Test
    public void testRelativeTimePeriod12() throws Exception {
		ICubeQueryDefinition cqd = new CubeQueryDefinition(DateCube.cubeName);

		IEdgeDefinition rowEdge = cqd
				.createEdge(ICubeQueryDefinition.COLUMN_EDGE);
		IDimensionDefinition productLineDim1 = rowEdge
				.createDimension("dimension2");
		IHierarchyDefinition porductLineHie1 = productLineDim1
				.createHierarchy("dimension2");
		porductLineHie1.createLevel("level21");

		IEdgeDefinition columnEdge = cqd
				.createEdge(ICubeQueryDefinition.ROW_EDGE);
		IDimensionDefinition dateDim = columnEdge.createDimension("dimension1");
		IHierarchyDefinition dateHier = dateDim.createHierarchy("dimension1");

		dateHier.createLevel("level11");
		dateHier.createLevel("level12");
		dateHier.createLevel("level13");
		dateHier.createLevel("level14");
		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		TimeFunction timeFunction = new TimeFunction();
		TimePeriod timePeriod = new TimePeriod(0, TimePeriodType.WEEK);
		ReferenceDate referenceDate = new ReferenceDate(new GregorianCalendar(
				1998, 7, 19).getTime());
		timeFunction.setReferenceDate(referenceDate);
		timeFunction.setBaseTimePeriod(timePeriod);
		timeFunction.setTimeDimension("dimension1");

		IBinding binding2 = new Binding("measure1");
		binding2.setExpression(new ScriptExpression("measure[\"measure1\"]"));

		binding2.setTimeFunction(timeFunction);
		binding2.setAggrFunction(IBuildInAggregation.TOTAL_SUM_FUNC);
		binding2.addAggregateOn("dimension[\"dimension1\"][\"level11\"]");
		binding2.addAggregateOn("dimension[\"dimension1\"][\"level12\"]");
		binding2.addAggregateOn("dimension[\"dimension1\"][\"level13\"]");

		cqd.addBinding(binding2);

		DateCube util = new DateCube();
		DataEngineImpl engine = (DataEngineImpl) DataEngine
				.newDataEngine(createPresentationContext());
		util.createCube(engine);

		ICube cube = util.getCube(DateCube.cubeName, engine);
		BirtCubeView cubeView = new BirtCubeView(new CubeQueryExecutor(null,
				cqd, engine.getSession(), new ImporterTopLevel(),
				engine.getContext()), cube, null, null);

		CubeCursor cursor = cubeView.getCubeCursor(new StopSign(), cube);

		List columnEdgeBindingNames = new ArrayList();

		List rowEdgeBindingNames = new ArrayList();

		this.printCube(cursor, columnEdgeBindingNames, rowEdgeBindingNames,
				"measure1");
		engine.shutdown();

	}

	/**
	 * test trailing function
	 * 
	 * @throws Exception
	 */
	@Test
    public void testRelativeTimePeriod5() throws Exception {
		ICubeQueryDefinition cqd = new CubeQueryDefinition(DateCube.cubeName);

		IEdgeDefinition rowEdge = cqd
				.createEdge(ICubeQueryDefinition.COLUMN_EDGE);
		IDimensionDefinition productLineDim1 = rowEdge
				.createDimension("dimension2");
		IHierarchyDefinition porductLineHie1 = productLineDim1
				.createHierarchy("dimension2");
		porductLineHie1.createLevel("level21");

		IEdgeDefinition columnEdge = cqd
				.createEdge(ICubeQueryDefinition.ROW_EDGE);
		IDimensionDefinition dateDim = columnEdge.createDimension("dimension1");
		IHierarchyDefinition dateHier = dateDim.createHierarchy("dimension1");

		dateHier.createLevel("level11");
		dateHier.createLevel("level12");
		dateHier.createLevel("level13");
		dateHier.createLevel("level14");
		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );
		
		TimeFunction timeFunction = new TimeFunction();
		TimePeriod timePeriod = new TimePeriod(-3, TimePeriodType.MONTH);
		ReferenceDate referenceDate = new ReferenceDate(new GregorianCalendar(
				1998, 10, 20).getTime());
		timeFunction.setReferenceDate(referenceDate);
		timeFunction.setBaseTimePeriod(timePeriod);
		timeFunction.setTimeDimension("dimension1");

		IBinding binding2 = new Binding("measure1");
		binding2.setExpression(new ScriptExpression("measure[\"measure1\"]"));

		binding2.setTimeFunction(timeFunction);
		binding2.setAggrFunction(IBuildInAggregation.TOTAL_SUM_FUNC);
		binding2.addAggregateOn("dimension[\"dimension1\"][\"level11\"]");

		cqd.addBinding(binding2);

		DateCube util = new DateCube();
		DataEngineImpl engine = (DataEngineImpl) DataEngine
				.newDataEngine(createPresentationContext());
		util.createCube(engine);

		ICube cube = util.getCube(DateCube.cubeName, engine);
		BirtCubeView cubeView = new BirtCubeView(new CubeQueryExecutor(null,
				cqd, engine.getSession(), new ImporterTopLevel(),
				engine.getContext()), cube, null, null);

		CubeCursor cursor = cubeView.getCubeCursor(new StopSign(), cube);

		List columnEdgeBindingNames = new ArrayList();

		List rowEdgeBindingNames = new ArrayList();

		this.printCube(cursor, columnEdgeBindingNames, rowEdgeBindingNames,
				"measure1");
		engine.shutdown();

	}

	/**
	 * test trailing function
	 * 
	 * @throws Exception
	 */
	@Test
    public void testRelativeTimePeriod9() throws Exception {
		ICubeQueryDefinition cqd = new CubeQueryDefinition(DateCube.cubeName);

		IEdgeDefinition rowEdge = cqd
				.createEdge(ICubeQueryDefinition.COLUMN_EDGE);
		IDimensionDefinition productLineDim1 = rowEdge
				.createDimension("dimension2");
		IHierarchyDefinition porductLineHie1 = productLineDim1
				.createHierarchy("dimension2");
		porductLineHie1.createLevel("level21");

		IEdgeDefinition columnEdge = cqd
				.createEdge(ICubeQueryDefinition.ROW_EDGE);
		IDimensionDefinition dateDim = columnEdge.createDimension("dimension1");
		IHierarchyDefinition dateHier = dateDim.createHierarchy("dimension1");

		dateHier.createLevel("level11");
		dateHier.createLevel("level12");
		dateHier.createLevel("level13");
		dateHier.createLevel("level14");
		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		TimeFunction timeFunction = new TimeFunction();
		TimePeriod timePeriod = new TimePeriod(-3, TimePeriodType.DAY);
		ReferenceDate referenceDate = new ReferenceDate(new GregorianCalendar(
				1998, 8, 20).getTime());
		timeFunction.setReferenceDate(referenceDate);
		timeFunction.setBaseTimePeriod(timePeriod);
		timeFunction.setTimeDimension("dimension1");

		IBinding binding2 = new Binding("measure1");
		binding2.setExpression(new ScriptExpression("measure[\"measure1\"]"));

		binding2.setTimeFunction(timeFunction);
		binding2.setAggrFunction(IBuildInAggregation.TOTAL_SUM_FUNC);
		binding2.addAggregateOn("dimension[\"dimension1\"][\"level11\"]");

		cqd.addBinding(binding2);

		DateCube util = new DateCube();
		DataEngineImpl engine = (DataEngineImpl) DataEngine
				.newDataEngine(createPresentationContext());
		util.createCube(engine);

		ICube cube = util.getCube(DateCube.cubeName, engine);
		BirtCubeView cubeView = new BirtCubeView(new CubeQueryExecutor(null,
				cqd, engine.getSession(), new ImporterTopLevel(),
				engine.getContext()), cube, null, null);

		CubeCursor cursor = cubeView.getCubeCursor(new StopSign(), cube);

		List columnEdgeBindingNames = new ArrayList();

		List rowEdgeBindingNames = new ArrayList();

		this.printCube(cursor, columnEdgeBindingNames, rowEdgeBindingNames,
				"measure1");
		engine.shutdown();

	}

	/**
	 * test trailing function
	 * 
	 * @throws Exception
	 */
	@Test
    public void testRelativeTimePeriod6() throws Exception {
		ICubeQueryDefinition cqd = new CubeQueryDefinition(DateCube.cubeName);

		IEdgeDefinition rowEdge = cqd
				.createEdge(ICubeQueryDefinition.COLUMN_EDGE);
		IDimensionDefinition productLineDim1 = rowEdge
				.createDimension("dimension2");
		IHierarchyDefinition porductLineHie1 = productLineDim1
				.createHierarchy("dimension2");
		porductLineHie1.createLevel("level21");

		IEdgeDefinition columnEdge = cqd
				.createEdge(ICubeQueryDefinition.ROW_EDGE);
		IDimensionDefinition dateDim = columnEdge.createDimension("dimension1");
		IHierarchyDefinition dateHier = dateDim.createHierarchy("dimension1");

		dateHier.createLevel("level11");
		dateHier.createLevel("level12");
		dateHier.createLevel("level13");
		dateHier.createLevel("level14");
		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		TimeFunction timeFunction = new TimeFunction();
		TimePeriod timePeriod = new TimePeriod(-2, TimePeriodType.YEAR);
		ReferenceDate referenceDate = new ReferenceDate(new GregorianCalendar(
				1999, 10, 20).getTime());
		timeFunction.setReferenceDate(referenceDate);
		timeFunction.setBaseTimePeriod(timePeriod);
		timeFunction.setTimeDimension("dimension1");

		IBinding binding2 = new Binding("measure1");
		binding2.setExpression(new ScriptExpression("measure[\"measure1\"]"));

		binding2.setTimeFunction(timeFunction);
		binding2.setAggrFunction(IBuildInAggregation.TOTAL_SUM_FUNC);
		binding2.addAggregateOn("dimension[\"dimension2\"][\"level21\"]");

		cqd.addBinding(binding2);

		DateCube util = new DateCube();
		DataEngineImpl engine = (DataEngineImpl) DataEngine
				.newDataEngine(createPresentationContext());
		util.createCube(engine);

		ICube cube = util.getCube(DateCube.cubeName, engine);
		BirtCubeView cubeView = new BirtCubeView(new CubeQueryExecutor(null,
				cqd, engine.getSession(), new ImporterTopLevel(),
				engine.getContext()), cube, null, null);

		CubeCursor cursor = cubeView.getCubeCursor(new StopSign(), cube);

		List columnEdgeBindingNames = new ArrayList();

		List rowEdgeBindingNames = new ArrayList();

		this.printCube(cursor, columnEdgeBindingNames, rowEdgeBindingNames,
				"measure1");
		engine.shutdown();

	}

	/**
	 * test trailing function
	 * 
	 * @throws Exception
	 */
	@Test
    public void testRelativeTimePeriod13() throws Exception {
		ICubeQueryDefinition cqd = new CubeQueryDefinition(DateCube.cubeName);

		IEdgeDefinition rowEdge = cqd
				.createEdge(ICubeQueryDefinition.COLUMN_EDGE);
		IDimensionDefinition productLineDim1 = rowEdge
				.createDimension("dimension2");
		IHierarchyDefinition porductLineHie1 = productLineDim1
				.createHierarchy("dimension2");
		porductLineHie1.createLevel("level21");

		IEdgeDefinition columnEdge = cqd
				.createEdge(ICubeQueryDefinition.ROW_EDGE);
		IDimensionDefinition dateDim = columnEdge.createDimension("dimension1");
		IHierarchyDefinition dateHier = dateDim.createHierarchy("dimension1");

		dateHier.createLevel("level11");
		dateHier.createLevel("level12");
		dateHier.createLevel("level13");
		dateHier.createLevel("level14");
		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		TimeFunction timeFunction = new TimeFunction();
		TimePeriod timePeriod = new TimePeriod(3, TimePeriodType.YEAR);
		ReferenceDate referenceDate = new ReferenceDate(new GregorianCalendar(
				1998, 11, 20).getTime());
		timeFunction.setReferenceDate(referenceDate);
		timeFunction.setBaseTimePeriod(timePeriod);
		timeFunction.setTimeDimension("dimension1");

		IBinding binding2 = new Binding("measure1");
		binding2.setExpression(new ScriptExpression("measure[\"measure1\"]"));

		binding2.setTimeFunction(timeFunction);
		binding2.setAggrFunction(IBuildInAggregation.TOTAL_SUM_FUNC);
		binding2.addAggregateOn("dimension[\"dimension2\"][\"level21\"]");

		cqd.addBinding(binding2);

		DateCube util = new DateCube();
		DataEngineImpl engine = (DataEngineImpl) DataEngine
				.newDataEngine(createPresentationContext());
		util.createCube(engine);

		ICube cube = util.getCube(DateCube.cubeName, engine);
		BirtCubeView cubeView = new BirtCubeView(new CubeQueryExecutor(null,
				cqd, engine.getSession(), new ImporterTopLevel(),
				engine.getContext()), cube, null, null);

		CubeCursor cursor = cubeView.getCubeCursor(new StopSign(), cube);

		List columnEdgeBindingNames = new ArrayList();

		List rowEdgeBindingNames = new ArrayList();

		this.printCube(cursor, columnEdgeBindingNames, rowEdgeBindingNames,
				"measure1");
		engine.shutdown();

	}

	
	/**
	 * test add tow time functions
	 * 
	 * @throws Exception
	 */
	@Test
    public void testRelativeTimePeriod14() throws Exception {
		ICubeQueryDefinition cqd = new CubeQueryDefinition(DateCube.cubeName);

		IEdgeDefinition rowEdge = cqd
				.createEdge(ICubeQueryDefinition.COLUMN_EDGE);
		IDimensionDefinition productLineDim1 = rowEdge
				.createDimension("dimension2");
		IHierarchyDefinition porductLineHie1 = productLineDim1
				.createHierarchy("dimension2");
		porductLineHie1.createLevel("level21");

		IEdgeDefinition columnEdge = cqd
				.createEdge(ICubeQueryDefinition.ROW_EDGE);
		IDimensionDefinition dateDim = columnEdge.createDimension("dimension1");
		IHierarchyDefinition dateHier = dateDim.createHierarchy("dimension1");

		dateHier.createLevel("level11");
		dateHier.createLevel("level12");
		dateHier.createLevel("level13");
		dateHier.createLevel("level14");
		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );
		
		TimeFunction timeFunction = new TimeFunction();
		TimePeriod timePeriod = new TimePeriod(3, TimePeriodType.YEAR);
		ReferenceDate referenceDate = new ReferenceDate(new GregorianCalendar(
				1998, 11, 20).getTime());
		timeFunction.setReferenceDate(referenceDate);
		timeFunction.setBaseTimePeriod(timePeriod);
		timeFunction.setTimeDimension("dimension1");

		IBinding binding2 = new Binding("measure1");
		binding2.setExpression(new ScriptExpression("measure[\"measure1\"]"));

		binding2.setTimeFunction(timeFunction);
		binding2.setAggrFunction(IBuildInAggregation.TOTAL_SUM_FUNC);
		binding2.addAggregateOn("dimension[\"dimension2\"][\"level21\"]");

		cqd.addBinding(binding2);

		timeFunction = new TimeFunction();
		timePeriod = new TimePeriod(0, TimePeriodType.MONTH);
		referenceDate = new ReferenceDate(new GregorianCalendar(
				1999, 7, 19).getTime());
		timeFunction.setReferenceDate(referenceDate);
		timeFunction.setBaseTimePeriod(timePeriod);
		timeFunction.setTimeDimension("dimension1");
		
		IBinding binding3 = new Binding("measure2");
		binding3.setExpression(new ScriptExpression("measure[\"measure1\"]"));

		binding3.setTimeFunction(timeFunction);
		binding3.setAggrFunction(IBuildInAggregation.TOTAL_SUM_FUNC);
		binding3.addAggregateOn("dimension[\"dimension2\"][\"level21\"]");

		cqd.addBinding(binding3);

		
		DateCube util = new DateCube();
		DataEngineImpl engine = (DataEngineImpl) DataEngine
				.newDataEngine(createPresentationContext());
		util.createCube(engine);

		ICube cube = util.getCube(DateCube.cubeName, engine);
		BirtCubeView cubeView = new BirtCubeView(new CubeQueryExecutor(null,
				cqd, engine.getSession(), new ImporterTopLevel(),
				engine.getContext()), cube, null, null);

		CubeCursor cursor = cubeView.getCubeCursor(new StopSign(), cube);

		List columnEdgeBindingNames = new ArrayList();

		List rowEdgeBindingNames = new ArrayList();

		List measureNameList = new ArrayList<String>();
		measureNameList.add( "measure1" );
		measureNameList.add( "measure2" );
		this.printCube1(cursor, columnEdgeBindingNames, rowEdgeBindingNames,
			measureNameList	);
		engine.shutdown();

	}
	
	/**
	 * test add tow time functions
	 * 
	 * @throws Exception
	 */
	@Test
    public void testRelativeTimePeriod15() throws Exception {
		ICubeQueryDefinition cqd = new CubeQueryDefinition(DateCube.cubeName);

		IEdgeDefinition rowEdge = cqd
				.createEdge(ICubeQueryDefinition.COLUMN_EDGE);
		IDimensionDefinition productLineDim1 = rowEdge
				.createDimension("dimension2");
		IHierarchyDefinition porductLineHie1 = productLineDim1
				.createHierarchy("dimension2");
		porductLineHie1.createLevel("level21");

		IEdgeDefinition columnEdge = cqd
				.createEdge(ICubeQueryDefinition.ROW_EDGE);
		IDimensionDefinition dateDim = columnEdge.createDimension("dimension1");
		IHierarchyDefinition dateHier = dateDim.createHierarchy("dimension1");

		dateHier.createLevel("level11");
		dateHier.createLevel("level12");
		dateHier.createLevel("level13");
		dateHier.createLevel("level14");
		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );
		
		TimeFunction timeFunction = new TimeFunction();
		TimePeriod timePeriod = new TimePeriod(0, TimePeriodType.YEAR);
		ReferenceDate referenceDate = new ReferenceDate(new GregorianCalendar(
				1999, 8, 20).getTime());
		timeFunction.setReferenceDate(referenceDate);
		timeFunction.setBaseTimePeriod(timePeriod);
		timeFunction.setTimeDimension("dimension1");

		IBinding binding2 = new Binding("measure1");
		binding2.setExpression(new ScriptExpression("measure[\"measure1\"]"));

		binding2.setTimeFunction(timeFunction);
		binding2.setAggrFunction(IBuildInAggregation.TOTAL_SUM_FUNC);
		binding2.addAggregateOn("dimension[\"dimension1\"][\"level11\"]");
		binding2.addAggregateOn("dimension[\"dimension1\"][\"level12\"]");
		binding2.addAggregateOn("dimension[\"dimension1\"][\"level13\"]");
		binding2.addAggregateOn("dimension[\"dimension1\"][\"level14\"]");
		binding2.addAggregateOn("dimension[\"dimension2\"][\"level21\"]");
		cqd.addBinding( binding2 );


		IBinding binding3 = new Binding("measure2");
		binding3.setExpression(new ScriptExpression("measure[\"measure1\"]"));

		binding3.setTimeFunction(timeFunction);
		binding3.setAggrFunction(IBuildInAggregation.TOTAL_SUM_FUNC);
		binding3.addAggregateOn("dimension[\"dimension1\"][\"level11\"]");
		binding3.addAggregateOn("dimension[\"dimension1\"][\"level12\"]");
		binding3.addAggregateOn("dimension[\"dimension1\"][\"level13\"]");
		binding3.addAggregateOn("dimension[\"dimension1\"][\"level14\"]");
		binding3.addAggregateOn("dimension[\"dimension2\"][\"level21\"]");
		cqd.addBinding(binding3);

		
		DateCube util = new DateCube();
		DataEngineImpl engine = (DataEngineImpl) DataEngine
				.newDataEngine(createPresentationContext());
		util.createCube(engine);

		ICube cube = util.getCube(DateCube.cubeName, engine);
		BirtCubeView cubeView = new BirtCubeView(new CubeQueryExecutor(null,
				cqd, engine.getSession(), new ImporterTopLevel(),
				engine.getContext()), cube, null, null);

		CubeCursor cursor = cubeView.getCubeCursor(new StopSign(), cube);

		List columnEdgeBindingNames = new ArrayList();

		List rowEdgeBindingNames = new ArrayList();

		List measureNameList = new ArrayList<String>();
		measureNameList.add( "measure1" );
		measureNameList.add( "measure2" );
		this.printCube1(cursor, columnEdgeBindingNames, rowEdgeBindingNames,
			measureNameList	);
		engine.shutdown();

	}
	
	/**
	 * test add two time functions
	 * @throws Exception
	 */
	@Test
    public void testRelativeTimePeriod16() throws Exception {
		ICubeQueryDefinition cqd = new CubeQueryDefinition(DateCube.cubeName);

		IEdgeDefinition rowEdge = cqd
				.createEdge(ICubeQueryDefinition.COLUMN_EDGE);
		IDimensionDefinition productLineDim1 = rowEdge
				.createDimension("dimension2");
		IHierarchyDefinition porductLineHie1 = productLineDim1
				.createHierarchy("dimension2");
		porductLineHie1.createLevel("level21");

		IEdgeDefinition columnEdge = cqd
				.createEdge(ICubeQueryDefinition.ROW_EDGE);
		IDimensionDefinition dateDim = columnEdge.createDimension("dimension1");
		IHierarchyDefinition dateHier = dateDim.createHierarchy("dimension1");

		dateHier.createLevel("level11");
		dateHier.createLevel("level12");
		dateHier.createLevel("level13");
		dateHier.createLevel("level14");
		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );
		
		TimeFunction timeFunction = new TimeFunction();
		TimePeriod timePeriod = new TimePeriod(0, TimePeriodType.YEAR);
		ReferenceDate referenceDate = new ReferenceDate(new GregorianCalendar(
				1999, 8, 20).getTime());
		timeFunction.setReferenceDate(referenceDate);
		timeFunction.setBaseTimePeriod(timePeriod);
		timeFunction.setTimeDimension("dimension1");

		IBinding binding2 = new Binding("measure1");
		binding2.setExpression(new ScriptExpression("measure[\"measure1\"]"));

		binding2.setTimeFunction(timeFunction);
		binding2.setAggrFunction(IBuildInAggregation.TOTAL_SUM_FUNC);
		binding2.addAggregateOn("dimension[\"dimension1\"][\"level11\"]");
		binding2.addAggregateOn("dimension[\"dimension2\"][\"level21\"]");
		cqd.addBinding( binding2 );


		IBinding binding3 = new Binding("measure2");
		binding3.setExpression(new ScriptExpression("measure[\"measure1\"]"));

		timeFunction = new TimeFunction();
		timePeriod = new TimePeriod(0, TimePeriodType.YEAR);
		referenceDate = new ReferenceDate(new GregorianCalendar(
				1999, 11, 20).getTime());
		timeFunction.setReferenceDate(referenceDate);
		timeFunction.setBaseTimePeriod(timePeriod);
		timeFunction.setTimeDimension("dimension1");
		
		binding3.setTimeFunction(timeFunction);
		binding3.setAggrFunction(IBuildInAggregation.TOTAL_SUM_FUNC);
		binding3.addAggregateOn("dimension[\"dimension1\"][\"level11\"]");
		binding3.addAggregateOn("dimension[\"dimension2\"][\"level21\"]");
		cqd.addBinding(binding3);

		
		DateCube util = new DateCube();
		DataEngineImpl engine = (DataEngineImpl) DataEngine
				.newDataEngine(createPresentationContext());
		util.createCube(engine);

		ICube cube = util.getCube(DateCube.cubeName, engine);
		BirtCubeView cubeView = new BirtCubeView(new CubeQueryExecutor(null,
				cqd, engine.getSession(), new ImporterTopLevel(),
				engine.getContext()), cube, null, null);

		CubeCursor cursor = cubeView.getCubeCursor(new StopSign(), cube);

		List columnEdgeBindingNames = new ArrayList();

		List rowEdgeBindingNames = new ArrayList();

		List measureNameList = new ArrayList<String>();
		measureNameList.add( "measure1" );
		measureNameList.add( "measure2" );
		this.printCube1(cursor, columnEdgeBindingNames, rowEdgeBindingNames,
			measureNameList	);
		engine.shutdown();

	}
	
	/**
	 * test add two time functions
	 * @throws Exception
	 */
	@Test
    public void testRelativeTimePeriod17() throws Exception {
		ICubeQueryDefinition cqd = new CubeQueryDefinition(DateCube.cubeName);

		IEdgeDefinition rowEdge = cqd
				.createEdge(ICubeQueryDefinition.COLUMN_EDGE);
		IDimensionDefinition productLineDim1 = rowEdge
				.createDimension("dimension2");
		IHierarchyDefinition porductLineHie1 = productLineDim1
				.createHierarchy("dimension2");
		porductLineHie1.createLevel("level21");

		IEdgeDefinition columnEdge = cqd
				.createEdge(ICubeQueryDefinition.ROW_EDGE);
		IDimensionDefinition dateDim = columnEdge.createDimension("dimension1");
		IHierarchyDefinition dateHier = dateDim.createHierarchy("dimension1");

		dateHier.createLevel("level11");
		dateHier.createLevel("level12");
		dateHier.createLevel("level13");
		dateHier.createLevel("level14");
		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );
		
		TimeFunction timeFunction = new TimeFunction();
		TimePeriod timePeriod = new TimePeriod(0, TimePeriodType.WEEK);
		ReferenceDate referenceDate = new ReferenceDate(new GregorianCalendar(
				1998, 7, 19).getTime());
		timeFunction.setReferenceDate(referenceDate);
		timeFunction.setBaseTimePeriod(timePeriod);
		timeFunction.setTimeDimension("dimension1");

		IBinding binding2 = new Binding("measure1");
		binding2.setExpression(new ScriptExpression("measure[\"measure1\"]"));

		binding2.setTimeFunction(timeFunction);
		binding2.setAggrFunction(IBuildInAggregation.TOTAL_SUM_FUNC);
		binding2.addAggregateOn("dimension[\"dimension1\"][\"level11\"]");
		binding2.addAggregateOn("dimension[\"dimension1\"][\"level12\"]");
		binding2.addAggregateOn("dimension[\"dimension1\"][\"level13\"]");

		cqd.addBinding(binding2);


		timeFunction = new TimeFunction();
		timePeriod = new TimePeriod(0, TimePeriodType.YEAR);
		referenceDate = new ReferenceDate(new GregorianCalendar(
				1998, 10, 20).getTime());
		timeFunction.setReferenceDate(referenceDate);
		timeFunction.setBaseTimePeriod(timePeriod);
		timeFunction.setTimeDimension("dimension1");
		timePeriod = new TimePeriod(-3, TimePeriodType.MONTH);
		timeFunction.setRelativeTimePeriod(timePeriod);

		Binding binding3 = new Binding("measure2");
		binding3.setTimeFunction(timeFunction);
		binding3.setAggrFunction(IBuildInAggregation.TOTAL_SUM_FUNC);
		binding3.addAggregateOn("dimension[\"dimension1\"][\"level11\"]");
		binding3.addAggregateOn("dimension[\"dimension1\"][\"level12\"]");
		binding3.addAggregateOn("dimension[\"dimension1\"][\"level13\"]");
		binding3.setExpression(new ScriptExpression("measure[\"measure1\"]"));

		cqd.addBinding(binding3);
		
		timeFunction = new TimeFunction();
		timePeriod = new TimePeriod(0, TimePeriodType.YEAR);
		referenceDate = new ReferenceDate(new GregorianCalendar(
				1998, 10, 20).getTime());
		timeFunction.setReferenceDate(referenceDate);
		timeFunction.setBaseTimePeriod(timePeriod);
		timeFunction.setTimeDimension("dimension1");
		timePeriod = new TimePeriod(-3, TimePeriodType.MONTH);
		timeFunction.setRelativeTimePeriod(timePeriod);

		Binding binding4 = new Binding("measure3");
		binding4.setTimeFunction(timeFunction);
		binding4.setAggrFunction(IBuildInAggregation.TOTAL_SUM_FUNC);
		binding4.addAggregateOn("dimension[\"dimension1\"][\"level11\"]");
		binding4.addAggregateOn("dimension[\"dimension1\"][\"level12\"]");
		binding4.addAggregateOn("dimension[\"dimension1\"][\"level13\"]");
		binding4.setExpression(new ScriptExpression("measure[\"measure1\"]"));
		
		cqd.addBinding(binding4);
		
		DateCube util = new DateCube();
		DataEngineImpl engine = (DataEngineImpl) DataEngine
				.newDataEngine(createPresentationContext());
		util.createCube(engine);

		ICube cube = util.getCube(DateCube.cubeName, engine);
		BirtCubeView cubeView = new BirtCubeView(new CubeQueryExecutor(null,
				cqd, engine.getSession(), new ImporterTopLevel(),
				engine.getContext()), cube, null, null);

		CubeCursor cursor = cubeView.getCubeCursor(new StopSign(), cube);

		List columnEdgeBindingNames = new ArrayList();

		List rowEdgeBindingNames = new ArrayList();

		List measureNameList = new ArrayList<String>();
		measureNameList.add( "measure1" );
		measureNameList.add( "measure2" );
		measureNameList.add( "measure3" );
		this.printCube1(cursor, columnEdgeBindingNames, rowEdgeBindingNames,
			measureNameList	);
		engine.shutdown();

	}
	
	/**
	 * test year to date function using latest date in cube
	 * 
	 * @throws Exception
	 */
	@Test
    public void testRelativeTimePeriod18() throws Exception {
		ICubeQueryDefinition cqd = new CubeQueryDefinition(DateCube.cubeName);

		IEdgeDefinition rowEdge = cqd
				.createEdge(ICubeQueryDefinition.COLUMN_EDGE);
		IDimensionDefinition productLineDim1 = rowEdge
				.createDimension("dimension2");
		IHierarchyDefinition porductLineHie1 = productLineDim1
				.createHierarchy("dimension2");
		porductLineHie1.createLevel("level21");

		IEdgeDefinition columnEdge = cqd
				.createEdge(ICubeQueryDefinition.ROW_EDGE);
		IDimensionDefinition dateDim = columnEdge.createDimension("dimension1");
		IHierarchyDefinition dateHier = dateDim.createHierarchy("dimension1");

		dateHier.createLevel("level11");
		dateHier.createLevel("level12");
		dateHier.createLevel("level13");
		dateHier.createLevel("level14");

		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		TimeFunction timeFunction = new TimeFunction();
		TimePeriod timePeriod = new TimePeriod(0, TimePeriodType.YEAR);
		timeFunction.setReferenceDate( null );
		timeFunction.setBaseTimePeriod(timePeriod);
		timeFunction.setTimeDimension("dimension1");

		IBinding binding2 = new Binding("measure1");
		binding2.setExpression(new ScriptExpression("measure[\"measure1\"]"));

		binding2.setTimeFunction(timeFunction);
		binding2.setAggrFunction(IBuildInAggregation.TOTAL_SUM_FUNC);
		binding2.addAggregateOn("dimension[\"dimension1\"][\"level11\"]");
		binding2.addAggregateOn("dimension[\"dimension1\"][\"level12\"]");
		binding2.addAggregateOn("dimension[\"dimension1\"][\"level13\"]");
		binding2.addAggregateOn("dimension[\"dimension1\"][\"level14\"]");
		binding2.addAggregateOn("dimension[\"dimension2\"][\"level21\"]");

		cqd.addBinding(binding2);

		DateCube util = new DateCube();
		DataEngineImpl engine = (DataEngineImpl) DataEngine
				.newDataEngine(createPresentationContext());
		util.createCube(engine);

		ICube cube = util.getCube(DateCube.cubeName, engine);
		BirtCubeView cubeView = new BirtCubeView(new CubeQueryExecutor(null,
				cqd, engine.getSession(), new ImporterTopLevel(),
				engine.getContext()), cube, null, null);

		CubeCursor cursor = cubeView.getCubeCursor(new StopSign(), cube);

		List columnEdgeBindingNames = new ArrayList();

		List rowEdgeBindingNames = new ArrayList();

		this.printCube(cursor, columnEdgeBindingNames, rowEdgeBindingNames,
				"measure1");
		engine.shutdown();

	}
	
	/**
	 * add two time functions, one use fixed date, one use last member
	 * @throws Exception
	 */
	@Test
    public void testRelativeTimePeriod19() throws Exception
	{

		ICubeQueryDefinition cqd = new CubeQueryDefinition(DateCube.cubeName);

		IEdgeDefinition rowEdge = cqd
				.createEdge(ICubeQueryDefinition.COLUMN_EDGE);
		IDimensionDefinition productLineDim1 = rowEdge
				.createDimension("dimension2");
		IHierarchyDefinition porductLineHie1 = productLineDim1
				.createHierarchy("dimension2");
		porductLineHie1.createLevel("level21");

		IEdgeDefinition columnEdge = cqd
				.createEdge(ICubeQueryDefinition.ROW_EDGE);
		IDimensionDefinition dateDim = columnEdge.createDimension("dimension1");
		IHierarchyDefinition dateHier = dateDim.createHierarchy("dimension1");

		dateHier.createLevel("level11");
		dateHier.createLevel("level12");
		dateHier.createLevel("level13");
		dateHier.createLevel("level14");
		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );
		
		TimeFunction timeFunction = new TimeFunction();
		TimePeriod timePeriod = new TimePeriod(0, TimePeriodType.YEAR);
		timeFunction.setReferenceDate(null);
		timeFunction.setBaseTimePeriod(timePeriod);
		timeFunction.setTimeDimension("dimension1");

		IBinding binding2 = new Binding("measure1");
		binding2.setExpression(new ScriptExpression("measure[\"measure1\"]"));

		binding2.setTimeFunction(timeFunction);
		binding2.setAggrFunction(IBuildInAggregation.TOTAL_SUM_FUNC);
		binding2.addAggregateOn("dimension[\"dimension1\"][\"level11\"]");
		binding2.addAggregateOn("dimension[\"dimension1\"][\"level12\"]");

		cqd.addBinding(binding2);


		timeFunction = new TimeFunction();
		timePeriod = new TimePeriod(-2, TimePeriodType.QUARTER);
		ReferenceDate referenceDate = new ReferenceDate(new GregorianCalendar(
				1998, 10, 20).getTime());
		timeFunction.setReferenceDate(referenceDate);
		timeFunction.setBaseTimePeriod(timePeriod);
		timeFunction.setTimeDimension("dimension1");

		Binding binding3 = new Binding("measure2");
		binding3.setTimeFunction(timeFunction);
		binding3.setAggrFunction(IBuildInAggregation.TOTAL_SUM_FUNC);
		binding3.addAggregateOn("dimension[\"dimension1\"][\"level11\"]");
		binding3.addAggregateOn("dimension[\"dimension1\"][\"level12\"]");
		binding3.setExpression(new ScriptExpression("measure[\"measure1\"]"));

		cqd.addBinding(binding3);

		DateCube util = new DateCube();
		DataEngineImpl engine = (DataEngineImpl) DataEngine
				.newDataEngine(createPresentationContext());
		util.createCube(engine);

		ICube cube = util.getCube(DateCube.cubeName, engine);
		BirtCubeView cubeView = new BirtCubeView(new CubeQueryExecutor(null,
				cqd, engine.getSession(), new ImporterTopLevel(),
				engine.getContext()), cube, null, null);

		CubeCursor cursor = cubeView.getCubeCursor(new StopSign(), cube);

		List columnEdgeBindingNames = new ArrayList();

		List rowEdgeBindingNames = new ArrayList();

		List measureNameList = new ArrayList<String>();
		measureNameList.add( "measure1" );
		measureNameList.add( "measure2" );
		this.printCube1(cursor, columnEdgeBindingNames, rowEdgeBindingNames,
			measureNameList	);
		engine.shutdown();
	}
	
	
	/**
	 * test previous N period function
	 * 
	 * @throws Exception
	 */
	@Test
    public void testRelativeTimePeriod7() throws Exception {
		ICubeQueryDefinition cqd = new CubeQueryDefinition(DateCube.cubeName);

		IEdgeDefinition rowEdge = cqd
				.createEdge(ICubeQueryDefinition.COLUMN_EDGE);
		IDimensionDefinition productLineDim1 = rowEdge
				.createDimension("dimension2");
		IHierarchyDefinition porductLineHie1 = productLineDim1
				.createHierarchy("dimension2");
		porductLineHie1.createLevel("level21");

		IEdgeDefinition columnEdge = cqd
				.createEdge(ICubeQueryDefinition.ROW_EDGE);
		IDimensionDefinition dateDim = columnEdge.createDimension("dimension1");
		IHierarchyDefinition dateHier = dateDim.createHierarchy("dimension1");

		dateHier.createLevel("level11");
		dateHier.createLevel("level12");
		dateHier.createLevel("level13");
		dateHier.createLevel("level14");
		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		TimeFunction timeFunction = new TimeFunction();
		TimePeriod timePeriod = new TimePeriod(0, TimePeriodType.YEAR);
		ReferenceDate referenceDate = new ReferenceDate(new GregorianCalendar(
				1998, 10, 20).getTime());
		timeFunction.setReferenceDate(referenceDate);
		timeFunction.setBaseTimePeriod(timePeriod);
		timeFunction.setTimeDimension("dimension1");
		timePeriod = new TimePeriod(-3, TimePeriodType.MONTH);
		timeFunction.setRelativeTimePeriod(timePeriod);

		IBinding binding2 = new Binding("measure1");
		binding2.setExpression(new ScriptExpression("measure[\"measure1\"]"));

		binding2.setTimeFunction(timeFunction);
		binding2.setAggrFunction(IBuildInAggregation.TOTAL_SUM_FUNC);
		binding2.addAggregateOn("dimension[\"dimension1\"][\"level11\"]");

		cqd.addBinding(binding2);

		DateCube util = new DateCube();
		DataEngineImpl engine = (DataEngineImpl) DataEngine
				.newDataEngine(createPresentationContext());
		util.createCube(engine);

		ICube cube = util.getCube(DateCube.cubeName, engine);
		BirtCubeView cubeView = new BirtCubeView(new CubeQueryExecutor(null,
				cqd, engine.getSession(), new ImporterTopLevel(),
				engine.getContext()), cube, null, null);

		CubeCursor cursor = cubeView.getCubeCursor(new StopSign(), cube);

		List columnEdgeBindingNames = new ArrayList();

		List rowEdgeBindingNames = new ArrayList();

		this.printCube(cursor, columnEdgeBindingNames, rowEdgeBindingNames,
				"measure1");
		engine.shutdown();

	}

	/**
	 * test previous N period function
	 * 
	 * @throws Exception
	 */
	@Test
    public void testRelativeTimePeriod10() throws Exception {
		ICubeQueryDefinition cqd = new CubeQueryDefinition(DateCube.cubeName);

		IEdgeDefinition rowEdge = cqd
				.createEdge(ICubeQueryDefinition.COLUMN_EDGE);
		IDimensionDefinition productLineDim1 = rowEdge
				.createDimension("dimension2");
		IHierarchyDefinition porductLineHie1 = productLineDim1
				.createHierarchy("dimension2");
		porductLineHie1.createLevel("level21");

		IEdgeDefinition columnEdge = cqd
				.createEdge(ICubeQueryDefinition.ROW_EDGE);
		IDimensionDefinition dateDim = columnEdge.createDimension("dimension1");
		IHierarchyDefinition dateHier = dateDim.createHierarchy("dimension1");

		dateHier.createLevel("level11");
		dateHier.createLevel("level12");
		dateHier.createLevel("level13");
		dateHier.createLevel("level14");
		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		TimeFunction timeFunction = new TimeFunction();
		TimePeriod timePeriod = new TimePeriod(0, TimePeriodType.YEAR);
		ReferenceDate referenceDate = new ReferenceDate(new GregorianCalendar(
				1999, 9, 9).getTime());
		timeFunction.setReferenceDate(referenceDate);
		timeFunction.setBaseTimePeriod(timePeriod);
		timeFunction.setTimeDimension("dimension1");
		timePeriod = new TimePeriod(-3, TimePeriodType.WEEK);
		timeFunction.setRelativeTimePeriod(timePeriod);

		IBinding binding2 = new Binding("measure1");
		binding2.setExpression(new ScriptExpression("measure[\"measure1\"]"));

		binding2.setTimeFunction(timeFunction);
		binding2.setAggrFunction(IBuildInAggregation.TOTAL_SUM_FUNC);
		binding2.addAggregateOn("dimension[\"dimension1\"][\"level11\"]");

		cqd.addBinding(binding2);

		DateCube util = new DateCube();
		DataEngineImpl engine = (DataEngineImpl) DataEngine
				.newDataEngine(createPresentationContext());
		util.createCube(engine);

		ICube cube = util.getCube(DateCube.cubeName, engine);
		BirtCubeView cubeView = new BirtCubeView(new CubeQueryExecutor(null,
				cqd, engine.getSession(), new ImporterTopLevel(),
				engine.getContext()), cube, null, null);

		CubeCursor cursor = cubeView.getCubeCursor(new StopSign(), cube);

		List columnEdgeBindingNames = new ArrayList();

		List rowEdgeBindingNames = new ArrayList();

		this.printCube(cursor, columnEdgeBindingNames, rowEdgeBindingNames,
				"measure1");
		engine.shutdown();

	}

	/**
	 * test month to date function
	 * 
	 * @throws Exception
	 */
	@Test
    public void testRelativeTimePeriod8() throws Exception {
		ICubeQueryDefinition cqd = new CubeQueryDefinition(DateCube.cubeName);

		IEdgeDefinition rowEdge = cqd
				.createEdge(ICubeQueryDefinition.COLUMN_EDGE);
		IDimensionDefinition productLineDim1 = rowEdge
				.createDimension("dimension2");
		IHierarchyDefinition porductLineHie1 = productLineDim1
				.createHierarchy("dimension2");
		porductLineHie1.createLevel("level21");

		IEdgeDefinition columnEdge = cqd
				.createEdge(ICubeQueryDefinition.ROW_EDGE);
		IDimensionDefinition dateDim = columnEdge.createDimension("dimension1");
		IHierarchyDefinition dateHier = dateDim.createHierarchy("dimension1");

		dateHier.createLevel("level11");
		dateHier.createLevel("level12");
		dateHier.createLevel("level13");
		dateHier.createLevel("level14");
		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		TimeFunction timeFunction = new TimeFunction();
		TimePeriod timePeriod = new TimePeriod(0, TimePeriodType.MONTH);
		ReferenceDate referenceDate = new ReferenceDate(new GregorianCalendar(
				1999, 7, 20).getTime());
		timeFunction.setReferenceDate(referenceDate);
		timeFunction.setBaseTimePeriod(timePeriod);
		timeFunction.setTimeDimension("dimension1");

		IBinding binding2 = new Binding("measure1");
		binding2.setExpression(new ScriptExpression("measure[\"measure1\"]"));

		binding2.setTimeFunction(timeFunction);
		binding2.setAggrFunction(IBuildInAggregation.TOTAL_SUM_FUNC);
		binding2.addAggregateOn("dimension[\"dimension2\"][\"level21\"]");

		cqd.addBinding(binding2);

		DateCube util = new DateCube();
		DataEngineImpl engine = (DataEngineImpl) DataEngine
				.newDataEngine(createPresentationContext());
		util.createCube(engine);

		ICube cube = util.getCube(DateCube.cubeName, engine);
		BirtCubeView cubeView = new BirtCubeView(new CubeQueryExecutor(null,
				cqd, engine.getSession(), new ImporterTopLevel(),
				engine.getContext()), cube, null, null);

		CubeCursor cursor = cubeView.getCubeCursor(new StopSign(), cube);

		List columnEdgeBindingNames = new ArrayList();

		List rowEdgeBindingNames = new ArrayList();

		this.printCube(cursor, columnEdgeBindingNames, rowEdgeBindingNames,
				"measure1");
		engine.shutdown();

	}

	/**
	 * test quarter to date function
	 * 
	 * @throws Exception
	 */
	@Test
    public void testRelativeTimePeriod11() throws Exception {
		ICubeQueryDefinition cqd = new CubeQueryDefinition(DateCube.cubeName);

		IEdgeDefinition rowEdge = cqd
				.createEdge(ICubeQueryDefinition.COLUMN_EDGE);
		IDimensionDefinition productLineDim1 = rowEdge
				.createDimension("dimension2");
		IHierarchyDefinition porductLineHie1 = productLineDim1
				.createHierarchy("dimension2");
		porductLineHie1.createLevel("level21");

		IEdgeDefinition columnEdge = cqd
				.createEdge(ICubeQueryDefinition.ROW_EDGE);
		IDimensionDefinition dateDim = columnEdge.createDimension("dimension1");
		IHierarchyDefinition dateHier = dateDim.createHierarchy("dimension1");

		dateHier.createLevel("level11");
		dateHier.createLevel("level12");
		dateHier.createLevel("level13");
		dateHier.createLevel("level14");
		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		TimeFunction timeFunction = new TimeFunction();
		TimePeriod timePeriod = new TimePeriod(0, TimePeriodType.QUARTER);
		ReferenceDate referenceDate = new ReferenceDate(new GregorianCalendar(
				1998, 2, 1).getTime());
		timeFunction.setReferenceDate(referenceDate);
		timeFunction.setBaseTimePeriod(timePeriod);
		timeFunction.setTimeDimension("dimension1");

		IBinding binding2 = new Binding("measure1");
		binding2.setExpression(new ScriptExpression("measure[\"measure1\"]"));

		binding2.setTimeFunction(timeFunction);
		binding2.setAggrFunction(IBuildInAggregation.TOTAL_SUM_FUNC);
		binding2.addAggregateOn("dimension[\"dimension2\"][\"level21\"]");

		cqd.addBinding(binding2);

		DateCube util = new DateCube();
		DataEngineImpl engine = (DataEngineImpl) DataEngine
				.newDataEngine(createPresentationContext());
		util.createCube(engine);

		ICube cube = util.getCube(DateCube.cubeName, engine);
		BirtCubeView cubeView = new BirtCubeView(new CubeQueryExecutor(null,
				cqd, engine.getSession(), new ImporterTopLevel(),
				engine.getContext()), cube, null, null);

		CubeCursor cursor = cubeView.getCubeCursor(new StopSign(), cube);

		List columnEdgeBindingNames = new ArrayList();

		List rowEdgeBindingNames = new ArrayList();

		this.printCube(cursor, columnEdgeBindingNames, rowEdgeBindingNames,
				"measure1");
		engine.shutdown();

	}
	
	private void createSortTestBindings( ICubeQueryDefinition cqd )
			throws DataException
	{
		IMeasureDefinition measure =cqd.createMeasure( "measure1" );
		measure.setAggrFunction( "SUM" );

		IBinding binding1 = new Binding( "edge1level1" );

		binding1.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level11\"]" ) );
		cqd.addBinding( binding1 );

		IBinding binding2 = new Binding( "edge1level2" );

		binding2.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level12\"]" ) );
		cqd.addBinding( binding2 );

		IBinding binding3 = new Binding( "edge1level3" );

		binding3.setExpression( new ScriptExpression( "dimension[\"dimension1\"][\"level13\"]" ) );
		cqd.addBinding( binding3 );

		IBinding binding4 = new Binding( "edge2level1" );

		binding4.setExpression( new ScriptExpression( "dimension[\"dimension2\"][\"level21\"]" ) );
		cqd.addBinding( binding4 );

		IBinding binding5 = new Binding( "measure1" );
		binding5.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		
		binding5.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		binding5.addAggregateOn( "dimension[\"dimension2\"][\"level21\"]" );
		binding5.addAggregateOn( "dimension[\"dimension1\"][\"level11\"]" );
		binding5.addAggregateOn( "dimension[\"dimension1\"][\"level12\"]" );
		binding5.addAggregateOn( "dimension[\"dimension1\"][\"level13\"]" );
		 

		cqd.addBinding( binding5 );

		IBinding binding6 = new Binding( "rowGrandTotal" );
		binding6.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		binding6.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		binding6.addAggregateOn( "dimension[\"dimension2\"][\"level21\"]" );
		cqd.addBinding( binding6 );

		IBinding binding7 = new Binding( "columnGrandTotal" );
		binding7.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		binding7.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		binding7.addAggregateOn( "dimension[\"dimension1\"][\"level11\"]" );
		binding7.addAggregateOn( "dimension[\"dimension1\"][\"level12\"]" );
		cqd.addBinding( binding7 );

		IBinding binding8 = new Binding( "grandTotal" );
		binding8.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		binding8.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		cqd.addBinding( binding8 );

		IBinding binding9 = new Binding( "country_year_total" );
		binding9.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		binding9.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		binding9.addAggregateOn( "dimension[\"dimension1\"][\"level11\"]" );
		binding9.addAggregateOn( "dimension[\"dimension2\"][\"level21\"]" );
		cqd.addBinding( binding9 );

		IBinding binding10 = new Binding( "city_year_total" );
		binding10.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		binding10.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		binding10.addAggregateOn( "dimension[\"dimension1\"][\"level11\"]" );
		binding10.addAggregateOn( "dimension[\"dimension1\"][\"level12\"]" );
		binding10.addAggregateOn( "dimension[\"dimension2\"][\"level21\"]" );
		cqd.addBinding( binding10 );

		IBinding binding11 = new Binding( "dist_total" );
		binding11.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		binding11.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		binding11.addAggregateOn( "dimension[\"dimension1\"][\"level11\"]" );
		binding11.addAggregateOn( "dimension[\"dimension1\"][\"level12\"]" );
		binding11.addAggregateOn( "dimension[\"dimension1\"][\"level13\"]" );

		cqd.addBinding( binding11 );

		IBinding binding12 = new Binding( "city_total" );
		binding12.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		binding12.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		binding12.addAggregateOn( "dimension[\"dimension1\"][\"level11\"]" );
		binding12.addAggregateOn( "dimension[\"dimension1\"][\"level12\"]" );

		cqd.addBinding( binding12 );

		IBinding binding13 = new Binding( "country_total" );
		binding13.setExpression( new ScriptExpression( "measure[\"measure1\"]" ) );
		binding13.setAggrFunction( IBuildInAggregation.TOTAL_SUM_FUNC );
		binding13.addAggregateOn( "dimension[\"dimension1\"][\"level11\"]" );

		cqd.addBinding( binding13 );
		
		IBinding binding14 = new Binding( "derived1" );
		binding14.setExpression( new ScriptExpression( "data[\"city_year_total\"]/data[\"country_year_total\"]" ) );
		cqd.addBinding( binding14);
		
		IBinding binding15 = new Binding( "derived2" );
		binding15.setExpression( new ScriptExpression( "data[\"city_total\"]/data[\"country_total\"]" ) );
		cqd.addBinding( binding15);
		
	}
	private void printCube( CubeCursor cursor, String country_year_total,
			String city_year_total, String dist_total, String city_total,
			String country_total, String year_total, String grand_total,
			String[] columns, String row, String measure )
			throws OLAPException, IOException
	{
		printCube( cursor, country_year_total,
				city_year_total, dist_total, city_total,
				country_total, year_total, grand_total,
				columns, row,  measure, true );
	}
	
	private void printCube( CubeCursor cursor, String country_year_total,
			String city_year_total, String dist_total, String city_total,
			String country_total, String year_total, String grand_total,
			String[] columns, String row, String measure, boolean checkOutput )
			throws OLAPException, IOException
	{
		EdgeCursor edge1 = (EdgeCursor) ( cursor.getOrdinateEdge( ).get( 0 ) );
		EdgeCursor edge2 = (EdgeCursor) ( cursor.getOrdinateEdge( ).get( 1 ) );

		String[] lines = new String[columns.length];
		for ( int i = 0; i < lines.length; i++ )
		{
			lines[i] = "		";
		}

		while ( edge1.next( ) )
		{
			long countryEnd = ( (DimensionCursor) edge1.getDimensionCursor( )
					.get( 0 ) ).getEdgeEnd( );
			long cityEnd = ( (DimensionCursor) edge1.getDimensionCursor( )
					.get( 1 ) ).getEdgeEnd( );

			lines[0] += cursor.getObject( columns[0] ) + "		";
			lines[1] += cursor.getObject( columns[1] ) + "		";
			lines[2] += cursor.getObject( columns[2] ) + "		";

			if ( cityEnd == edge1.getPosition( ) )
			{
				lines[0] += cursor.getObject( columns[0] ) + "		";
				lines[1] += cursor.getObject( columns[1] ) + "		";
				lines[2] += "[Total]" + "		";
			}

			if ( countryEnd == edge1.getPosition( ) )
			{
				lines[0] += cursor.getObject( columns[0] ) + "		";
				lines[1] += "[Total]" + "		";
				lines[2] += "  " + "  		";
			}
		}
		lines[0] += "[Total]";
		String output = "";
		for ( int i = 0; i < lines.length; i++ )
		{
			output += "\n" + lines[i];
		}

		while ( edge2.next( ) )
		{
			String line = cursor.getObject( row ).toString( ) + "		";
			edge1.beforeFirst( );
			while ( edge1.next( ) )
			{
				long countryEnd = ( (DimensionCursor) edge1.getDimensionCursor( )
						.get( 0 ) ).getEdgeEnd( );
				long cityEnd = ( (DimensionCursor) edge1.getDimensionCursor( )
						.get( 1 ) ).getEdgeEnd( );

				line += cursor.getObject( measure ) + "		";

				if ( cityEnd == edge1.getPosition( ) )
					line += "["
							+ cursor.getObject( city_year_total ) + "]" + "		";

				if ( countryEnd == edge1.getPosition( ) )
					line += "["
							+ cursor.getObject( country_year_total ) + "]"
							+ "		";

			}

			line += "[" + cursor.getObject( year_total ) + "]";
			output += "\n" + line;
		}

		String line = "[Total]" + "		";
		edge1.beforeFirst( );
		while ( edge1.next( ) )
		{
			long countryEnd = ( (DimensionCursor) edge1.getDimensionCursor( )
					.get( 0 ) ).getEdgeEnd( );
			long cityEnd = ( (DimensionCursor) edge1.getDimensionCursor( )
					.get( 1 ) ).getEdgeEnd( );

			line += cursor.getObject( dist_total ) + "		";

			if ( cityEnd == edge1.getPosition( ) )
				line += "[" + cursor.getObject( city_total ) + "]" + "		";

			if ( countryEnd == edge1.getPosition( ) )
				line += "[" + cursor.getObject( country_total ) + "]" + "		";

		}
		line += "[" + cursor.getObject( grand_total ) + "]" + "		";
		output += "\n" + line;
		this.testPrint( output );

		if( checkOutput )
			this.checkOutputFile( );
	}

	private void printCube( CubeCursor cursor, List columnEdgeBindingNames,
			List rowEdgeBindingNames, String measureBindingNames )
			throws Exception
	{
		this.printCube( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				measureBindingNames,
				null,
				null,
				null );
	}
	
	private void printCube1( CubeCursor cursor, List columnEdgeBindingNames,
			List rowEdgeBindingNames, List<String> measureBindingNameList )
			throws Exception
	{
		this.printCube( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				measureBindingNameList,
				null,
				null,
				null );
	}
	
	private void printCubeWithPage( CubeCursor cursor,
			List pageEdgeBindingNames, List columnEdgeBindingNames,
			List rowEdgeBindingNames, String measureBindingNames,
			String columnAggr, String rowAggr, String overallAggr )
			throws Exception
	{
		if ( !cursor.getPageEdge( ).isEmpty( ) )
		{
			EdgeCursor pageCursor = (EdgeCursor) cursor.getPageEdge( )
					.toArray( )[0];
			pageCursor.beforeFirst( );
			String output = "";

			while ( pageCursor.next( ) )
			{
				for ( int i = 0; i < pageEdgeBindingNames.size( ); i++ )
				{
					output += "\n"
							+ cursor.getObject( pageEdgeBindingNames.get( i )
									.toString( ) ) + "		";
				}
				cursor.synchronizePages( );
				output += this.getOutputFromCursor( cursor,
						columnEdgeBindingNames,
						rowEdgeBindingNames,
						measureBindingNames,
						columnAggr,
						rowAggr,
						overallAggr );
			}
			this.testPrint( output );

			this.checkOutputFile( );
			close( cursor );
		}
		else
		{
			this.printCube( cursor,
					columnEdgeBindingNames,
					rowEdgeBindingNames,
					measureBindingNames,
					columnAggr,
					rowAggr,
					overallAggr );
		}
	}

	private void printCube( CubeCursor cursor, List columnEdgeBindingNames,
			List rowEdgeBindingNames, String measureBindingNames,
			String columnAggr, String rowAggr, String overallAggr, boolean checkOutput )
			throws Exception
	{
		String output = getOutputFromCursor( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				measureBindingNames,
				columnAggr,
				rowAggr,
				overallAggr );
		this.testPrint( output );
		if ( checkOutput )
			this.checkOutputFile( );
		close( cursor );
	}

	private void printCube( CubeCursor cursor, List columnEdgeBindingNames,
			List rowEdgeBindingNames, List<String> measureBindingNameList,
			String columnAggr, String rowAggr, String overallAggr, boolean checkOutput )
			throws Exception
	{
		String output = getOutputFromCursor( cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				measureBindingNameList,
				columnAggr,
				rowAggr,
				overallAggr );
		this.testPrint( output );
		if ( checkOutput )
			this.checkOutputFile( );
		close( cursor );
	}
	
	private void printCube( CubeCursor cursor, List columnEdgeBindingNames,
			List rowEdgeBindingNames, List<String> measureBindingNameList,
			String columnAggr, String rowAggr, String overallAggr )
			throws Exception
	{
		this.printCube( cursor, columnEdgeBindingNames,
			rowEdgeBindingNames, measureBindingNameList,
			columnAggr, rowAggr, overallAggr, true );
	}
	
	private void printCube( CubeCursor cursor, List columnEdgeBindingNames,
			List rowEdgeBindingNames, String measureBindingNames,
			String columnAggr, String rowAggr, String overallAggr )
			throws Exception
	{
		this.printCube( cursor, columnEdgeBindingNames,
			rowEdgeBindingNames, measureBindingNames,
			columnAggr, rowAggr, overallAggr, true );
	}
	
	private void printCube( CubeCursor cursor, List columnEdgeBindingNames,
			List rowEdgeBindingNames, String measureBindingName,
			String[] columnAggrs)
			throws Exception
	{
		String output = getOutputFromCursor(
				cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				measureBindingName,
				columnAggrs);
		this.testPrint( output );
	}
	
	private void printCubeWithRank( CubeCursor cursor, List columnEdgeBindingNames,
			List rowEdgeBindingNames, String measureBindingName,
			String[] columnAggrs)
			throws Exception
	{
		String output = getOutputFromCursorWithRank(
				cursor,
				columnEdgeBindingNames,
				rowEdgeBindingNames,
				measureBindingName,
				columnAggrs);
		this.testPrint( output );
	}
	
	
	private String getOutputFromCursor( CubeCursor cursor,
			List columnEdgeBindingNames, List rowEdgeBindingNames,
			String measureBindingNames, String columnAggr, String rowAggr,
			String overallAggr ) throws OLAPException
	{
		EdgeCursor edge1 = (EdgeCursor) ( cursor.getOrdinateEdge( ).get( 0 ) );
		EdgeCursor edge2 = null;
		if( cursor.getOrdinateEdge( ).size( ) > 1 )
			edge2 = (EdgeCursor) ( cursor.getOrdinateEdge( ).get( 1 ) );

		String[] lines = new String[columnEdgeBindingNames.size( )];
		for ( int i = 0; i < columnEdgeBindingNames.size( ); i++ )
		{
			lines[i] = "		";
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

		while ( edge2 != null && edge2.next( ) )
		{
			String line = "";
			for ( int i = 0; i < rowEdgeBindingNames.size( ); i++ )
			{
				line += cursor.getObject( rowEdgeBindingNames.get( i )
						.toString( ) ).toString( )
						+ "		";
			}
			edge1.beforeFirst( );
			while ( edge1.next( ) )
			{
				if ( measureBindingNames != null )
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
			String line = "Total" + "		";
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
	
	private String getOutputFromCursor( CubeCursor cursor,
			List columnEdgeBindingNames, List rowEdgeBindingNames,
			List<String> measureBindingNameList, String columnAggr, String rowAggr,
			String overallAggr ) throws OLAPException
	{
		EdgeCursor edge1 = (EdgeCursor) ( cursor.getOrdinateEdge( ).get( 0 ) );
		EdgeCursor edge2 = null;
		if( cursor.getOrdinateEdge( ).size( ) > 1 )
			edge2 = (EdgeCursor) ( cursor.getOrdinateEdge( ).get( 1 ) );

		String[] lines = new String[columnEdgeBindingNames.size( )];
		for ( int i = 0; i < columnEdgeBindingNames.size( ); i++ )
		{
			lines[i] = "		";
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

		while ( edge2 != null && edge2.next( ) )
		{
			String line = "";
			for ( int i = 0; i < rowEdgeBindingNames.size( ); i++ )
			{
				line += cursor.getObject( rowEdgeBindingNames.get( i )
						.toString( ) ).toString( )
						+ "		";
			}
			edge1.beforeFirst( );
			while ( edge1.next( ) )
			{
				for (String measureBindingNames : measureBindingNameList)
				{
					if ( measureBindingNames != null )
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
			String line = "Total" + "		";
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
	
	private String getOutputFromCursor( CubeCursor cursor,
			List columnEdgeBindingNames, List rowEdgeBindingNames,
			String measureBindingName, String[] columnAggrs
			) throws OLAPException
	{
		EdgeCursor edge1 = (EdgeCursor) ( cursor.getOrdinateEdge( ).get( 0 ) );
		EdgeCursor edge2 = (EdgeCursor) ( cursor.getOrdinateEdge( ).get( 1 ) );

		String[] lines = new String[columnEdgeBindingNames.size( )];
		for ( int i = 0; i < columnEdgeBindingNames.size( ); i++ )
		{
			lines[i] = "		";
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
						.toString( ) ).toString( )
						+ "		";
			}
			edge1.beforeFirst( );
			while ( edge1.next( ) )
			{
				line += cursor.getObject( measureBindingName ) + "		";
			}
			output += "\n" + line;
		}

		String line = "total" + "		";
		edge1.beforeFirst( );
		edge2.first( );
		while( edge1.next( ) )
		{
			line+= cursor.getObject( "total" )+ "		";
		}
		output +="\n" + line;
		
		line = "maxTotal1" + "	";
		edge1.beforeFirst( );
		edge2.first( );
		while (edge1.next( ))
		{
			line+= cursor.getObject( "maxTotal1" )+ "		";
		}
		output +="\n" + line;
		
		line = "maxTotal2" + "	";
		edge1.beforeFirst( );
		edge2.first( );
		while (edge1.next( ))
		{
			line+= cursor.getObject( "maxTotal2" )+ "		";
		}
		output +="\n" + line;
		
		line = "sumTotal1" + "	";
		edge1.beforeFirst( );
		edge2.first( );
		while( edge1.next( ) )
		{
			line+= cursor.getObject( "sumTotal1" )+ "		";
		}
		output +="\n" + line;
		
		line = "sumTotal2" + "	";
		edge1.beforeFirst( );
		edge2.first( );
		while( edge1.next( ) )
		{
			line+= cursor.getObject( "sumTotal2" )+ "		";
		}
		output +="\n" + line;
		
		line = "sumSumTotal1" + "	";
		edge1.beforeFirst( );
		edge2.first( );
		while( edge1.next( ) )
		{
			line+= cursor.getObject( "sumSumTotal1" )+ "		";
		}
		output +="\n" + line + "";
		
		return output;
	}
	
	private String getOutputFromCursorWithRank( CubeCursor cursor,
			List columnEdgeBindingNames, List rowEdgeBindingNames,
			String measureBindingName, String[] columnAggrs
			) throws OLAPException
	{
		EdgeCursor edge1 = (EdgeCursor) ( cursor.getOrdinateEdge( ).get( 0 ) );
		EdgeCursor edge2 = (EdgeCursor) ( cursor.getOrdinateEdge( ).get( 1 ) );

		String[] lines = new String[columnEdgeBindingNames.size( )];
		for ( int i = 0; i < columnEdgeBindingNames.size( ); i++ )
		{
			lines[i] = "		";
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
						.toString( ) ).toString( )
						+ "		";
			}
			edge1.beforeFirst( );
			while ( edge1.next( ) )
			{
				line += cursor.getObject( measureBindingName ) + "		";
			}
			output += "\n" + line;
		}

		String line = "total" + "		";
		edge1.beforeFirst( );
		edge2.first( );
		while( edge1.next( ) )
		{
			line+= cursor.getObject( "total" )+ "		";
		}
		output +="\n" + line;
		
		line = "rankInCountryA" + "	";
		edge1.beforeFirst( );
		edge2.first( );
		while (edge1.next( ))
		{
			line+= cursor.getObject( "totalRankInCountry" )+ "		";
		}
		output +="\n" + line;
		
		line = "referBinding" + "	";
		edge1.beforeFirst( );
		edge2.first( );
		while (edge1.next( ))
		{
			line+= cursor.getObject( "referRankAggr" )+ "		";
		}
		output +="\n" + line;
		
		line = "rankInCountryD" + "	";
		edge1.beforeFirst( );
		edge2.first( );
		while (edge1.next( ))
		{
			line+= cursor.getObject( "totalRankInCountryDesc" )+ "		";
		}
		output +="\n" + line;
		
		return output;
	}

	/**
	 * 
	 * @param engine
	 * @throws BirtException
	 */
	private void defineDataSourceDataSet( DataEngine engine )
			throws BirtException
	{
		ScriptDataSourceDesign dataSource = new ScriptDataSourceDesign( "ds" );

		ScriptDataSetDesign dataSet = new ScriptDataSetDesign( "test" );

		dataSet.setDataSource( "ds" );

		ColumnDefinition col = new ColumnDefinition( "column1" );
		col.setDataType( DataType.INTEGER_TYPE );
		dataSet.addResultSetHint( col );

		dataSet.setOpenScript( "i = 57;" );
		dataSet.setFetchScript( " i--; if ( i < 27 ) return false; row.column1 = i; return true;" );

		engine.defineDataSource( dataSource );
		engine.defineDataSet( dataSet );

	}

	/**
	 * 
	 * @return
	 * @throws DataException
	 */
	private IQueryDefinition createScriptDataSetQuery( ) throws DataException
	{
		QueryDefinition query = new QueryDefinition( );

		query.setDataSetName( "test" );
		query.addBinding( new Binding( "column1",
				new ScriptExpression( "dataSetRow.column1" ) ) );
		query.addBinding( new Binding( "column2", new ScriptExpression("\"A1\"")) );
		return query;
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

	private void createCube( org.eclipse.birt.data.engine.impl.DataEngineImpl engine ) throws BirtException,
			IOException
	{
		IDocumentManager documentManager = DocumentManagerFactory.createFileDocumentManager( engine.getSession( ).getTempDir( ),
				String.valueOf(engine.hashCode( )) );
		DocManagerMap.getDocManagerMap( )
				.set( String.valueOf( engine.hashCode( ) ),
						engine.getSession( ).getTempDir( ) + engine.hashCode( ),
						documentManager );
		engine.addShutdownListener( new DocManagerReleaser( engine ) );
		Dimension[] dimensions = new Dimension[2];

		// dimension0
		String[] levelNames = new String[3];
		levelNames[0] = "level11";
		levelNames[1] = "level12";
		levelNames[2] = "level13";
		DimensionForTest iterator = new DimensionForTest( levelNames );
		iterator.setLevelMember( 0, TestFactTable.DIM0_L1Col );
		iterator.setLevelMember( 1, TestFactTable.DIM0_L2Col );
		iterator.setLevelMember( 2, TestFactTable.DIM0_L3Col );

		ILevelDefn[] levelDefs = new ILevelDefn[3];
		levelDefs[0] = new LevelDefinition( "level11", new String[]{
			"level11"
		}, null );
		levelDefs[1] = new LevelDefinition( "level12", new String[]{
			"level12"
		}, null );
		levelDefs[2] = new LevelDefinition( "level13", new String[]{
			"level13"
		}, null );
		dimensions[0] = (Dimension) DimensionFactory.createDimension( "dimension1",
				documentManager,
				iterator,
				levelDefs,
				false,
				new StopSign());
		IHierarchy hierarchy = dimensions[0].getHierarchy( );
		assertEquals( hierarchy.getName( ), "dimension1" );
		assertEquals( dimensions[0].length( ), 13 );

		// dimension1
		levelNames = new String[]{
				"level21", "attr21"
		};
		iterator = new DimensionForTest( levelNames );
		iterator.setLevelMember( 0, distinct( TestFactTable.DIM1_L1Col ) );
		iterator.setLevelMember( 1, TestFactTable.ATTRIBUTE_Col );

		levelDefs = new ILevelDefn[1];
		levelDefs[0] = new LevelDefinition( "level21", new String[]{
			"level21"
		}, new String[]{
			"attr21"
		} );
		dimensions[1] = (Dimension) DimensionFactory.createDimension( "dimension2",
				documentManager,
				iterator,
				levelDefs,
				false, new StopSign() );
		hierarchy = dimensions[1].getHierarchy( );
		assertEquals( hierarchy.getName( ), "dimension2" );
		assertEquals( dimensions[1].length( ), 5 );

		TestFactTable factTable2 = new TestFactTable( );
		String[] measureColumnName = new String[1];
		measureColumnName[0] = "measure1";
		Cube cube = new Cube( cubeName, documentManager );

		cube.create( getKeyColNames( dimensions ),
				dimensions,
				factTable2,
				measureColumnName,
				new StopSign( ) );

		cube.close( );
		documentManager.flush( );

	}

	
	private void createDateCube( org.eclipse.birt.data.engine.impl.DataEngineImpl engine ) throws BirtException,
	IOException
	{
		IDocumentManager documentManager = DocumentManagerFactory.createFileDocumentManager( engine.getSession( )
				.getTempDir( ),
				String.valueOf( engine.hashCode( ) ) );
		DocManagerMap.getDocManagerMap( )
				.set( String.valueOf( engine.hashCode( ) ),
						engine.getSession( ).getTempDir( ) + engine.hashCode( ),
						documentManager );
		engine.addShutdownListener( new DocManagerReleaser( engine ) );
		Dimension[] dimensions = new Dimension[2];

		// dimension0
		String[] levelNames = new String[3];
		levelNames[0] = "level11";
		levelNames[1] = "level12";
		levelNames[2] = "level13";
		DimensionForTest iterator = new DimensionForTest( levelNames );
		iterator.setLevelMember( 0, TestFactTable.DIM0_L1Col );
		iterator.setLevelMember( 1, TestFactTable.DIM0_L2Col );
		iterator.setLevelMember( 2, TestFactTable.DIM0_L3Col );

		ILevelDefn[] levelDefs = new ILevelDefn[3];
		levelDefs[0] = new LevelDefinition( "level11", new String[]{
			"level11"
		}, null );
		levelDefs[1] = new LevelDefinition( "level12", new String[]{
			"level12"
		}, null );
		levelDefs[2] = new LevelDefinition( "level13", new String[]{
			"level13"
		}, null );
		dimensions[0] = (Dimension) DimensionFactory.createDimension( "dimension1",
				documentManager,
				iterator,
				levelDefs,
				false,
				new StopSign( ) );
		IHierarchy hierarchy = dimensions[0].getHierarchy( );
		assertEquals( hierarchy.getName( ), "dimension1" );
		assertEquals( dimensions[0].length( ), 13 );

		// dimension1
		levelNames = new String[]{
				"level21", "attr21"
		};
		iterator = new DimensionForTest( levelNames );
		iterator.setLevelMember( 0, distinct( TestFactTable.DIM1_L1Col ) );
		iterator.setLevelMember( 1, TestFactTable.ATTRIBUTE_Col );

		levelDefs = new ILevelDefn[1];
		levelDefs[0] = new LevelDefinition( "level21", new String[]{
			"level21"
		}, new String[]{
			"attr21"
		} );
		dimensions[1] = (Dimension) DimensionFactory.createDimension( "dimension2",
				documentManager,
				iterator,
				levelDefs,
				false,
				new StopSign( ) );
		hierarchy = dimensions[1].getHierarchy( );
		assertEquals( hierarchy.getName( ), "dimension2" );
		assertEquals( dimensions[1].length( ), 5 );

		TestFactTable factTable2 = new TestFactTable( );
		String[] measureColumnName = new String[1];
		measureColumnName[0] = "measure1";
		Cube cube = new Cube( cubeName, documentManager );

		cube.create( getKeyColNames( dimensions ),
				dimensions,
				factTable2,
				measureColumnName,
				new StopSign( ) );

		cube.close( );
		documentManager.flush( );

	}
	
	
	private void createCube1(
			org.eclipse.birt.data.engine.impl.DataEngineImpl engine )
			throws BirtException, IOException
	{
		IDocumentManager documentManager = DocumentManagerFactory.createFileDocumentManager( engine.getSession( ).getTempDir( ),
				String.valueOf( engine.hashCode( )) );
		DocManagerMap.getDocManagerMap( )
				.set( String.valueOf( engine.hashCode( ) ),
						engine.getSession( ).getTempDir( ) + engine.hashCode( ),
						documentManager );
		engine.addShutdownListener( new DocManagerReleaser( engine ) );
		Dimension[] dimensions = new Dimension[4];

		// dimension0
		String[] levelNames = new String[1];
		levelNames[0] = "level11";
		DimensionForTest iterator = new DimensionForTest( levelNames );
		iterator.setLevelMember( 0, TestFactTable.DIM0_L1Col );

		ILevelDefn[] levelDefs = new ILevelDefn[1];
		levelDefs[0] = new LevelDefinition( "level11", new String[]{
			"level11"
		}, null );
		dimensions[0] = (Dimension) DimensionFactory.createDimension( "dimension1",
				documentManager,
				iterator,
				levelDefs,
				false, new StopSign() );
		IHierarchy hierarchy = dimensions[0].getHierarchy( );
		assertEquals( hierarchy.getName( ), "dimension1" );

		// dimension1
		levelNames = new String[1];
		levelNames[0] = "level12";
		iterator = new DimensionForTest( levelNames );
		iterator.setLevelMember( 0, TestFactTable.DIM0_L2Col );

		levelDefs = new ILevelDefn[1];
		levelDefs[0] = new LevelDefinition( "level12", new String[]{
			"level12"
		}, null );
		dimensions[1] = (Dimension) DimensionFactory.createDimension( "dimension2",
				documentManager,
				iterator,
				levelDefs,
				false, new StopSign() );
		hierarchy = dimensions[1].getHierarchy( );
		assertEquals( hierarchy.getName( ), "dimension2" );

		// dimension2
		levelNames = new String[1];
		levelNames[0] = "level13";
		iterator = new DimensionForTest( levelNames );
		iterator.setLevelMember( 0, TestFactTable.DIM0_L3Col );

		levelDefs = new ILevelDefn[1];
		levelDefs[0] = new LevelDefinition( "level13", new String[]{
			"level13"
		}, null );
		dimensions[2] = (Dimension) DimensionFactory.createDimension( "dimension3",
				documentManager,
				iterator,
				levelDefs,
				false, new StopSign() );
		hierarchy = dimensions[2].getHierarchy( );
		assertEquals( hierarchy.getName( ), "dimension3" );
		
		// dimension3
		levelNames = new String[]{
				"level21", "attr21"
		};
		iterator = new DimensionForTest( levelNames );
		iterator.setLevelMember( 0, distinct( TestFactTable.DIM1_L1Col ) );
		iterator.setLevelMember( 1, TestFactTable.ATTRIBUTE_Col );

		levelDefs = new ILevelDefn[1];
		levelDefs[0] = new LevelDefinition( "level21", new String[]{
			"level21"
		}, new String[]{
			"attr21"
		} );
		dimensions[3] = (Dimension) DimensionFactory.createDimension( "dimension4",
				documentManager,
				iterator,
				levelDefs,
				false, new StopSign() );
		hierarchy = dimensions[3].getHierarchy( );
		assertEquals( hierarchy.getName( ), "dimension4" );
		assertEquals( dimensions[3].length( ), 5 );
		
//		// dimension2
//		levelNames = new String[1];
//		levelNames[0] = "level31";
//		iterator = new DimensionForTest( levelNames );
//		iterator.setLevelMember( 0, distinct( TestFactTable.DIM0_L1Col ));
//
//		levelDefs = new ILevelDefn[1];
//		levelDefs[0] = new LevelDefinition( "level31", new String[]{
//			"level31"
//		}, null );
//		dimensions[2] = (Dimension) DimensionFactory.createDimension( "dimension3",
//				documentManager,
//				iterator,
//				levelDefs,
//				false );
//		hierarchy = dimensions[2].getHierarchy( );
//		assertEquals( hierarchy.getName( ), "dimension3" );
//		assertEquals( dimensions[2].length( ), 4 );

		TestFactTable factTable2 = new TestFactTable( );
		String[] measureColumnName = new String[1];
		measureColumnName[0] = "measure1";
		Cube cube = new Cube( cubeName, documentManager );

		cube.create( getKeyColNames( dimensions ),
				dimensions,
				factTable2,
				measureColumnName,
				new StopSign( ) );

		cube.close( );
		documentManager.flush( );

	}

	/**
	 * 
	 * @param dimensions
	 * @return
	 */
	private static String[][] getKeyColNames( IDimension[] dimensions )
	{
		String[][] keyColumnName = new String[dimensions.length][];
		for ( int i = 0; i < dimensions.length; i++ )
		{
			ILevel[] levels = dimensions[i].getHierarchy( ).getLevels( );
			ILevel detailLevel = levels[levels.length - 1];
			keyColumnName[i] = detailLevel.getKeyNames( );
		}
		return keyColumnName;
	}

	private String[] distinct( String[] values )
	{
		String[] lValues = new String[values.length];
		System.arraycopy( values, 0, lValues, 0, values.length );
		Arrays.sort( lValues );
		List tempList = new ArrayList( );
		tempList.add( lValues[0] );
		for ( int i = 1; i < lValues.length; i++ )
		{
			if ( !lValues[i].equals( lValues[i - 1] ) )
			{
				tempList.add( lValues[i] );
			}
		}
		String[] result = new String[tempList.size( )];
		for ( int i = 0; i < result.length; i++ )
		{
			result[i] = ( (String) tempList.get( i ) );
		}
		return result;
	}

}

class TestFactTable implements IDatasetIterator
{

	int ptr = -1;
	static String[] DIM0_L1Col = {
			"CN",
			"CN",
			"CN",
			"CN",
			"CN",
			"CN",
			"CN",
			"CN",
			"CN",
			"CN",
			"CN",
			"CN",
			"CN",
			"CN",
			"CN",
			"CN",
			"CN",
			"CN",
			"CN",
			"CN",
			"CN",
			"CN",
			"CN",
			"CN",
			"CN",
			"CN",
			"CN",
			"CN",
			"CN",
			"CN",
			"US",
			"US",
			"US",
			"US",
			"US",
			"US",
			"US",
			"US",
			"US",
			"US",
			"US",
			"US",
			"US",
			"US",
			"US",
			"UN",
			"UN",
			"UN",
			"UN",
			"UN",
			"UN",
			"UN",
			"UN",
			"UN",
			"UN",
			"JP",
			"JP",
			"JP",
			"JP",
			"JP",
			"JP",
			"JP",
			"JP",
			"JP",
			"JP"
	};
	static String[] DIM0_L2Col = {
			"SH",
			"SH",
			"SH",
			"SH",
			"SH",
			"SH",
			"SH",
			"SH",
			"SH",
			"SH",
			"BJ",
			"BJ",
			"BJ",
			"BJ",
			"BJ",
			"BJ",
			"BJ",
			"BJ",
			"BJ",
			"BJ",
			"SZ",
			"SZ",
			"SZ",
			"SZ",
			"SZ",
			"SZ",
			"SZ",
			"SZ",
			"SZ",
			"SZ",
			"LA",
			"LA",
			"LA",
			"LA",
			"LA",
			"CS",
			"CS",
			"CS",
			"CS",
			"CS",
			"NY",
			"NY",
			"NY",
			"NY",
			"NY",
			"LD",
			"LD",
			"LD",
			"LD",
			"LD",
			"LP",
			"LP",
			"LP",
			"LP",
			"LP",
			"TK",
			"TK",
			"TK",
			"TK",
			"TK",
			"IL",
			"IL",
			"IL",
			"IL",
			"IL"
	};
	static String[] DIM0_L3Col = {
			"PD",
			"PD",
			"PD",
			"PD",
			"PD",
			"ZJ",
			"ZJ",
			"ZJ",
			"ZJ",
			"ZJ",
			"HD",
			"HD",
			"HD",
			"HD",
			"HD",
			"CP",
			"CP",
			"CP",
			"CP",
			"CP",
			"S1",
			"S1",
			"S1",
			"S1",
			"S1",
			"S2",
			"S2",
			"S2",
			"S2",
			"S2",
			"A1",
			"A1",
			"A1",
			"A1",
			"A1",
			"B1",
			"B1",
			"B1",
			"B1",
			"B1",
			"C1",
			"C1",
			"C1",
			"C1",
			"C1",
			"D1",
			"D1",
			"D1",
			"D1",
			"D1",
			"E1",
			"E1",
			"E1",
			"E1",
			"E1",
			"F1",
			"F1",
			"F1",
			"F1",
			"F1",
			"P1",
			"P1",
			"P1",
			"P1",
			"P1"
	};
	static String[] DIM1_L1Col = {
			"1998",
			"1999",
			"2000",
			"2001",
			"2002",
			"1998",
			"1999",
			"2000",
			"2001",
			"2002",
			"1998",
			"1999",
			"2000",
			"2001",
			"2002",
			"1998",
			"1999",
			"2000",
			"2001",
			"2002",
			"1998",
			"1999",
			"2000",
			"2001",
			"2002",
			"1998",
			"1999",
			"2000",
			"2001",
			"2002",
			"1998",
			"1999",
			"2000",
			"2001",
			"2002",
			"1998",
			"1999",
			"2000",
			"2001",
			"2002",
			"1998",
			"1999",
			"2000",
			"2001",
			"2002",
			"1998",
			"1999",
			"2000",
			"2001",
			"2002",
			"1998",
			"1999",
			"2000",
			"2001",
			"2002",
			"1998",
			"1999",
			"2000",
			"2001",
			"2002",
			"1998",
			"1999",
			"2000",
			"2001",
			"2002"
	};

	static int[] MEASURE_Col = {
			1,
			2,
			3,
			4,
			5,
			6,
			7,
			8,
			9,
			10,
			11,
			12,
			13,
			14,
			15,
			16,
			17,
			18,
			19,
			20,
			21,
			22,
			23,
			24,
			25,
			26,
			27,
			28,
			29,
			30,
			31,
			32,
			33,
			34,
			35,
			36,
			37,
			38,
			39,
			40,
			41,
			42,
			43,
			44,
			45,
			46,
			47,
			48,
			49,
			50,
			51,
			52,
			53,
			54,
			55,
			56,
			57,
			58,
			59,
			60,
			61,
			62,
			63,
			65,
			65
	};

	static int[] ATTRIBUTE_Col = {
			1,
			2,
			3,
			4,
			5,
			1,
			2,
			3,
			4,
			5,
			1,
			2,
			3,
			4,
			5,
			1,
			2,
			3,
			4,
			5,
			1,
			2,
			3,
			4,
			5,
			1,
			2,
			3,
			4,
			5,
			1,
			2,
			3,
			4,
			5,
			1,
			2,
			3,
			4,
			5,
			1,
			2,
			3,
			4,
			5,
			1,
			2,
			3,
			4,
			5,
			1,
			2,
			3,
			4,
			5,
			1,
			2,
			3,
			4,
			5,
			1,
			2,
			3,
			4,
			5,
	};

	public void close( ) throws BirtException
	{
		// TODO Auto-generated method stub

	}

	public int getFieldIndex( String name ) throws BirtException
	{
		if ( name.equals( "level11" ) )
		{
			return 0;
		}
		else if ( name.equals( "level12" ) )
		{
			return 1;
		}
		else if ( name.equals( "level13" ) )
		{
			return 2;
		}
		else if ( name.equals( "level21" ) )
		{
			return 3;
		}
		else if ( name.equals( "measure1" ) )
		{
			return 4;
		}
		return -1;
	}

	public int getFieldType( String name ) throws BirtException
	{
		if ( name.equals( "level11" ) )
		{
			return DataType.STRING_TYPE;
		}
		else if ( name.equals( "level12" ) )
		{
			return DataType.STRING_TYPE;
		}
		else if ( name.equals( "level13" ) )
		{
			return DataType.STRING_TYPE;
		}
		else if ( name.equals( "level21" ) )
		{
			return DataType.STRING_TYPE;
		}
		else if ( name.equals( "measure1" ) )
		{
			return DataType.INTEGER_TYPE;
		}

		return -1;
	}

	public Integer getInteger( int fieldIndex ) throws BirtException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public String getString( int fieldIndex ) throws BirtException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Object getValue( int fieldIndex ) throws BirtException
	{
		if ( fieldIndex == 0 )
		{
			return DIM0_L1Col[ptr];
		}
		else if ( fieldIndex == 1 )
		{
			return DIM0_L2Col[ptr];
		}
		else if ( fieldIndex == 2 )
		{
			return DIM0_L3Col[ptr];
		}
		else if ( fieldIndex == 3 )
		{
			return DIM1_L1Col[ptr];
		}
		else if ( fieldIndex == 4 )
		{
			return new Integer( MEASURE_Col[ptr] );
		}

		return null;
	}

	public boolean next( ) throws BirtException
	{
		ptr++;
		if ( ptr >= MEASURE_Col.length )
		{
			return false;
		}
		return true;
	}
}

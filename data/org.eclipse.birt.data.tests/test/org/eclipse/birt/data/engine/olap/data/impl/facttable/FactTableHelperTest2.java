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

package org.eclipse.birt.data.engine.olap.data.impl.facttable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.aggregation.api.IBuildInAggregation;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.DataEngineImpl;
import org.eclipse.birt.data.engine.impl.StopSign;
import org.eclipse.birt.data.engine.olap.cursor.CubeUtility;
import org.eclipse.birt.data.engine.olap.data.api.CubeQueryExecutorHelper;
import org.eclipse.birt.data.engine.olap.data.api.DimLevel;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;
import org.eclipse.birt.data.engine.olap.data.api.IDimensionSortDefn;
import org.eclipse.birt.data.engine.olap.data.api.ILevel;
import org.eclipse.birt.data.engine.olap.data.api.ISelection;
import org.eclipse.birt.data.engine.olap.data.api.cube.CubeElementFactory;
import org.eclipse.birt.data.engine.olap.data.api.cube.CubeMaterializer;
import org.eclipse.birt.data.engine.olap.data.api.cube.IDatasetIterator;
import org.eclipse.birt.data.engine.olap.data.api.cube.IHierarchy;
import org.eclipse.birt.data.engine.olap.data.api.cube.ILevelDefn;
import org.eclipse.birt.data.engine.olap.data.document.DocumentManagerFactory;
import org.eclipse.birt.data.engine.olap.data.document.IDocumentManager;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationFunctionDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.Cube;
import org.eclipse.birt.data.engine.olap.data.impl.NamingUtil;
import org.eclipse.birt.data.engine.olap.data.impl.SelectionFactory;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.AggregationExecutor;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.AggregationHelper;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.AggregationResultSetWithOneMoreDummyAggr;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.DataSetFromOriginalCube;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.IDataSet4Aggregation;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.filter.SimpleLevelFilter;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Dimension;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.DimensionFactory;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.DimensionForTest;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.DimensionResultIterator;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Level;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.LevelDefinition;
import org.eclipse.birt.data.engine.olap.data.util.BufferedPrimitiveDiskArray;
import org.eclipse.birt.data.engine.olap.data.util.DataType;
import org.eclipse.birt.data.engine.olap.data.util.IDiskArray;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptableObject;

import testutil.BaseTestCase;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *  
 */

public class FactTableHelperTest2 extends BaseTestCase
{
	//private static final String tmpPath = System.getProperty( "java.io.tmpdir" ) + File.separator;
	private DimLevel dimLevel31 = new DimLevel("dimension3","level31");
	private DimLevel dimLevel21 = new DimLevel("dimension2","level21");
	private DimLevel dimLevel11 = new DimLevel("dimension1", "level11");
	private DimLevel dimLevel12 = new DimLevel("dimension1", "level12");
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
/*
	 * @see TestCase#tearDown()
	 */
/**
	 * 
	 * @throws IOException
	 * @throws BirtException
	 */
	@Test
    public void testFactTableSaveAndLoad( ) throws IOException, BirtException
	{
		IDocumentManager documentManager = DocumentManagerFactory.createFileDocumentManager( );
		
		testFactTableSaveAndLoad( documentManager );
		
		documentManager.close( );
	}
	

	private void testFactTableSaveAndLoad( IDocumentManager documentManager ) throws IOException, BirtException
	{
		Dimension[] dimensions = new Dimension[3];
		
		// dimension0
		String[] levelNames = new String[3];
		levelNames[0] = "level11";
		levelNames[1] = "level12";
		levelNames[2] = "level13";
		DimensionForTest iterator = new DimensionForTest( levelNames );
		iterator.setLevelMember( 0, FactTable1.L1Col );
		iterator.setLevelMember( 1, FactTable1.L2Col );
		iterator.setLevelMember( 2, FactTable1.L3Col );
		
		ILevelDefn[] levelDefs = new ILevelDefn[3];
		levelDefs[0] = new LevelDefinition( "level11", new String[]{"level11"}, null );
		levelDefs[1] = new LevelDefinition( "level12", new String[]{"level12"}, null );
		levelDefs[2] = new LevelDefinition( "level13", new String[]{"level13"}, null );
		dimensions[0] = (Dimension) DimensionFactory.createDimension( "dimension1", documentManager, iterator, levelDefs, false, new StopSign() );
		IHierarchy hierarchy = dimensions[0].getHierarchy( );
		assertEquals( hierarchy.getName( ), "dimension1" );
		assertEquals( dimensions[0].length( ), FactTable1.L1Col.length );
		
		//dimension1
		levelNames = new String[1];
		levelNames[0] = "level21";
		iterator = new DimensionForTest( levelNames );
		iterator.setLevelMember( 0, distinct(FactTable1.L1Col) );
		
		levelDefs = new ILevelDefn[1];
		levelDefs[0] = new LevelDefinition( "level21", new String[]{"level21"}, null );
		dimensions[1] = (Dimension) DimensionFactory.createDimension( "dimension2", documentManager, iterator, levelDefs, false, new StopSign() );
		hierarchy = dimensions[1].getHierarchy( );
		assertEquals( hierarchy.getName( ), "dimension2" );
		assertEquals( dimensions[1].length( ), 3 );
		
		// dimension2
		levelNames = new String[1];
		levelNames[0] = "level31";
		iterator = new DimensionForTest( levelNames );
		int[] lL1Col = {
				1, 2, 3
		};
		iterator.setLevelMember( 0, lL1Col );

		levelDefs = new ILevelDefn[1];
		levelDefs[0] = new LevelDefinition( "level31", new String[]{"level31"}, null );
		dimensions[2] = (Dimension) DimensionFactory.createDimension( "dimension3",
				documentManager,
				iterator,
				levelDefs,
				false, new StopSign() );
		hierarchy = dimensions[2].getHierarchy( );
		assertEquals( hierarchy.getName( ), "dimension3" );
		assertEquals( dimensions[2].length( ), 3 );
		
		FactTable1 factTable1 = new FactTable1();
		String[] measureColumnName = new String[2];
		measureColumnName[0] = "measure1";
		measureColumnName[1] = "measure2";
		FactTableAccessor factTableConstructor = new FactTableAccessor( documentManager );
		FactTable factTable = factTableConstructor.saveFactTable( NamingUtil.getFactTableName( "bigThreeDimensions" ),
				CubeUtility.getKeyColNames(dimensions),
				CubeUtility.getKeyColNames(dimensions),
				factTable1,
				dimensions,
				measureColumnName,
				new StopSign( ) );
		// assertEquals(factTable.getSegmentNumber( ), 1);
		factTable = factTableConstructor.load( NamingUtil.getFactTableName( "bigThreeDimensions" ),
				new StopSign( ) );
//		assertEquals(factTable.getSegmentNumber( ), 1);
		assertEquals(factTable.getDimensionInfo( )[0].getDimensionName( ), "dimension1" );
		assertEquals(factTable.getDimensionInfo( )[0].getDimensionLength( ), FactTable1.L1Col.length );
		assertEquals(factTable.getDimensionInfo( )[1].getDimensionName( ), "dimension2" );
		assertEquals(factTable.getDimensionInfo( )[1].getDimensionLength( ), 3 );
		assertEquals(factTable.getDimensionInfo( )[2].getDimensionName( ), "dimension3" );
		assertEquals(factTable.getDimensionInfo( )[2].getDimensionLength( ), 3 );
		assertEquals(factTable.getMeasureInfo( )[0].getMeasureName(), "measure1" );
		assertEquals( factTable.getMeasureInfo( )[0].getDataType(), DataType.INTEGER_TYPE );
		assertEquals(factTable.getMeasureInfo( )[1].getMeasureName(), "measure2" );
		assertEquals( factTable.getMeasureInfo( )[1].getDataType(), DataType.DOUBLE_TYPE );
		String[] dimensionNames = new String[3];
		dimensionNames[0] = "dimension1";
		dimensionNames[1] = "dimension2";
		dimensionNames[2] = "dimension3";
		IDiskArray[] dimensionPosition = new IDiskArray[3];
		dimensionPosition[0] = new BufferedPrimitiveDiskArray( );
		dimensionPosition[0].add( new Integer(10) );
		dimensionPosition[0].add( new Integer(11) );
		dimensionPosition[1] = new BufferedPrimitiveDiskArray( );
		dimensionPosition[1].add( new Integer(1) );
		dimensionPosition[1].add( new Integer(2) );
		dimensionPosition[2] = new BufferedPrimitiveDiskArray( );
		dimensionPosition[2].add( new Integer(1) );
		dimensionPosition[2].add( new Integer(2) );
		dimensionPosition[2].add( new Integer(3) );
		FactTableRowIterator facttableRowIterator = new FactTableRowIterator( factTable, dimensionNames, dimensionPosition, new StopSign() );
		assertTrue( facttableRowIterator != null );
		
		assertTrue( facttableRowIterator.next( ));
		assertEquals(10, facttableRowIterator.getDimensionPosition( 0 ));
		assertEquals(2, facttableRowIterator.getDimensionPosition( 1 ));
		assertEquals(2, facttableRowIterator.getDimensionPosition( 2 ));
		assertEquals(new Integer(10), facttableRowIterator.getMeasure( 0 ));
		assertEquals(new Double(10), facttableRowIterator.getMeasure( 1 ));
		
		assertTrue( facttableRowIterator.next( ));
		assertEquals(11, facttableRowIterator.getDimensionPosition( 0 ));
		assertEquals(2, facttableRowIterator.getDimensionPosition( 1 ));
		assertEquals(2, facttableRowIterator.getDimensionPosition( 2 ));
		assertEquals(new Integer(11), facttableRowIterator.getMeasure( 0 ));
		assertEquals(new Double(11), facttableRowIterator.getMeasure( 1 ));
		
		assertFalse( facttableRowIterator.next( ));
	}
	
	private static int[] distinct( int[] iValues )
	{
		Arrays.sort( iValues );
		List tempList = new ArrayList( );
		tempList.add( new Integer(iValues[0]) );
		for ( int i = 1; i < iValues.length; i++ )
		{
			if ( iValues[i] != iValues[i - 1] )
			{
				tempList.add( new Integer(iValues[i]) );
			}
		}
		int[] result = new int[tempList.size( )];
		for ( int i = 0; i < result.length; i++ )
		{
			result[i] = ((Integer)tempList.get( i )).intValue( );
		}
		return result;
	}
	
	
	
	/**
	 * 
	 * @throws IOException
	 * @throws BirtException
	 */
	@Test
    public void testFactTableSaveAndLoad2( ) throws IOException, BirtException
	{
		IDocumentManager documentManager = DocumentManagerFactory.createFileDocumentManager( );
		
		testFactTableSaveAndLoad2( documentManager );
		documentManager.close( );
	}
	

	private void testFactTableSaveAndLoad2( IDocumentManager documentManager ) throws IOException, BirtException
	{
		Dimension[] dimensions = new Dimension[3];
		
		// dimension0
		String[] levelNames = new String[3];
		levelNames[0] = "level11";
		levelNames[1] = "level12";
		levelNames[2] = "level13";
		DimensionForTest iterator = new DimensionForTest( levelNames );
		iterator.setLevelMember( 0, FactTable2.L1Col );
		iterator.setLevelMember( 1, FactTable2.L2Col );
		iterator.setLevelMember( 2, FactTable2.L3Col );
		
		ILevelDefn[] levelDefs = new ILevelDefn[3];
		levelDefs[0] = new LevelDefinition( "level11", new String[]{"level11"}, null );
		levelDefs[1] = new LevelDefinition( "level12", new String[]{"level12"}, null );
		levelDefs[2] = new LevelDefinition( "level13", new String[]{"level13"}, null );
		dimensions[0] = (Dimension) DimensionFactory.createDimension( "dimension1", documentManager, iterator, levelDefs, false, new StopSign() );
		IHierarchy hierarchy = dimensions[0].getHierarchy( );
		assertEquals( hierarchy.getName( ), "dimension1" );
		assertEquals( dimensions[0].length( ), FactTable2.L1Col.length );
		
		//dimension1
		levelNames = new String[1];
		levelNames[0] = "level21";
		iterator = new DimensionForTest( levelNames );
		iterator.setLevelMember( 0, distinct(FactTable2.L1Col) );
		
		levelDefs = new ILevelDefn[1];
		levelDefs[0] = new LevelDefinition( "level21", new String[]{"level21"}, null );
		dimensions[1] = (Dimension) DimensionFactory.createDimension( "dimension2", documentManager, iterator, levelDefs, false, new StopSign() );
		hierarchy = dimensions[1].getHierarchy( );
		assertEquals( hierarchy.getName( ), "dimension2" );
		assertEquals( dimensions[1].length( ), 3 );
		
		// dimension2
//		 dimension2
		levelNames = new String[1];
		levelNames[0] = "level31";
		
		iterator = new DimensionForTest( levelNames );
		iterator.setLevelMember( 0, FactTable2.L3Col );

		levelDefs = new ILevelDefn[1];
		levelDefs[0] = new LevelDefinition( "level31", new String[]{"level31"}, null );
		dimensions[2] = (Dimension) DimensionFactory.createDimension( "dimension3",
				documentManager,
				iterator,
				levelDefs,
				false, new StopSign() );
		hierarchy = dimensions[2].getHierarchy( );
		assertEquals( hierarchy.getName( ), "dimension3" );
		assertEquals( dimensions[2].length( ), 12 );
		
		FactTable2 factTable2 = new FactTable2();
		String[] measureColumnName = new String[2];
		measureColumnName[0] = "measure1";
		measureColumnName[1] = "measure2";
		FactTableAccessor factTableConstructor = new FactTableAccessor( documentManager );
		FactTable factTable = factTableConstructor.saveFactTable( NamingUtil.getFactTableName( "bigThreeDimensions" ),
				CubeUtility.getKeyColNames(dimensions),
				CubeUtility.getKeyColNames(dimensions),
				factTable2,
				dimensions,
				measureColumnName,
				new StopSign( ) );
		// assertEquals(factTable.getSegmentNumber( ), 1);
		factTable = factTableConstructor.load( NamingUtil.getFactTableName( "bigThreeDimensions" ),
				new StopSign( ) );
//		assertEquals(factTable.getSegmentNumber( ), 1);
		assertEquals(factTable.getDimensionInfo( )[0].getDimensionName( ), "dimension1" );
		assertEquals(factTable.getDimensionInfo( )[0].getDimensionLength( ), FactTable2.L1Col.length );
		assertEquals(factTable.getDimensionInfo( )[1].getDimensionName( ), "dimension2" );
		assertEquals(factTable.getDimensionInfo( )[1].getDimensionLength( ), 3 );
		assertEquals(factTable.getDimensionInfo( )[2].getDimensionName( ), "dimension3" );
		assertEquals(factTable.getDimensionInfo( )[2].getDimensionLength( ), 12 );
		assertEquals(factTable.getMeasureInfo( )[0].getMeasureName(), "measure1" );
		assertEquals( factTable.getMeasureInfo( )[0].getDataType(), DataType.INTEGER_TYPE );
		assertEquals(factTable.getMeasureInfo( )[1].getMeasureName(), "measure2" );
		assertEquals( factTable.getMeasureInfo( )[1].getDataType(), DataType.DOUBLE_TYPE );
		String[] dimensionNames = new String[3];
		dimensionNames[0] = "dimension1";
		dimensionNames[1] = "dimension2";
		dimensionNames[2] = "dimension3";
		IDiskArray[] dimensionPosition = new IDiskArray[3];
		dimensionPosition[0] = new BufferedPrimitiveDiskArray( );
		dimensionPosition[0].add( new Integer(10) );
		dimensionPosition[0].add( new Integer(11) );
		dimensionPosition[1] = new BufferedPrimitiveDiskArray( );
		dimensionPosition[1].add( new Integer(1) );
		dimensionPosition[1].add( new Integer(2) );
		dimensionPosition[2] = new BufferedPrimitiveDiskArray( );
		dimensionPosition[2].add( new Integer(3) );
		dimensionPosition[2].add( new Integer(4) );
		dimensionPosition[2].add( new Integer(5) );
		dimensionPosition[2].add( new Integer(10) );
		dimensionPosition[2].add( new Integer(11) );
		FactTableRowIterator facttableRowIterator = new FactTableRowIterator( factTable, dimensionNames, dimensionPosition, new StopSign() );
		assertTrue( facttableRowIterator != null );
		
		assertTrue( facttableRowIterator.next( ));
		assertEquals(10, facttableRowIterator.getDimensionPosition( 0 ));
		assertEquals(2, facttableRowIterator.getDimensionPosition( 1 ));
		assertEquals(10, facttableRowIterator.getDimensionPosition( 2 ));
		assertEquals(new Integer(10), facttableRowIterator.getMeasure( 0 ));
		assertEquals(new Double(10), facttableRowIterator.getMeasure( 1 ));
		
		assertTrue( facttableRowIterator.next( ));
		assertEquals(11, facttableRowIterator.getDimensionPosition( 0 ));
		assertEquals(2, facttableRowIterator.getDimensionPosition( 1 ));
		assertEquals(11, facttableRowIterator.getDimensionPosition( 2 ));
		assertEquals(new Integer(11), facttableRowIterator.getMeasure( 0 ));
		assertEquals(new Double(11), facttableRowIterator.getMeasure( 1 ));
		
		
		assertFalse( facttableRowIterator.next( ));
	}
	
	/**
	 * 
	 * @throws IOException
	 * @throws BirtException
	 */
	@Test
    public void testFactTableSaveAndLoad3( ) throws IOException, BirtException
	{
		IDocumentManager documentManager = DocumentManagerFactory.createFileDocumentManager( );
		
		testFactTableSaveAndLoad3( documentManager );
		
		documentManager.close( );
	}
	
	private void testFactTableSaveAndLoad3( IDocumentManager documentManager ) throws IOException, BirtException, DataException
	{
		Dimension[] dimensions = new Dimension[3];
		
		// dimension0
		String[] levelNames = new String[3];
		levelNames[0] = "level11";
		levelNames[1] = "level12";
		levelNames[2] = "level13";
		DimensionForTest iterator = new DimensionForTest( levelNames );
		iterator.setLevelMember( 0, FactTable2.L1Col );
		iterator.setLevelMember( 1, FactTable2.L2Col );
		iterator.setLevelMember( 2, FactTable2.L3Col );
		
		ILevelDefn[] levelDefs = new ILevelDefn[3];
		levelDefs[0] = new LevelDefinition( "level11", new String[]{"level11"}, null );
		levelDefs[1] = new LevelDefinition( "level12", new String[]{"level12"}, null );
		levelDefs[2] = new LevelDefinition( "level13", new String[]{"level13"}, null );
		dimensions[0] = (Dimension) DimensionFactory.createDimension( "dimension1",
				documentManager,
				iterator,
				levelDefs,
				false, new StopSign() );
		IHierarchy hierarchy = dimensions[0].getHierarchy( );
		assertEquals( hierarchy.getName( ), "dimension1" );
		assertEquals( dimensions[0].length( ), FactTable2.L1Col.length );
		
		//dimension1
		levelNames = new String[1];
		levelNames[0] = "level21";
		iterator = new DimensionForTest( levelNames );
		iterator.setLevelMember( 0, distinct(FactTable2.L1Col) );
		
		levelDefs = new ILevelDefn[1];
		levelDefs[0] = new LevelDefinition( "level21", new String[]{"level21"}, null );
		dimensions[1] = (Dimension) DimensionFactory.createDimension( "dimension2", documentManager, iterator, levelDefs, false, new StopSign() );
		hierarchy = dimensions[1].getHierarchy( );
		assertEquals( hierarchy.getName( ), "dimension2" );
		assertEquals( dimensions[1].length( ), 3 );
		
		// dimension2
		levelNames = new String[1];
		levelNames[0] = "level31";
		
		iterator = new DimensionForTest( levelNames );
		iterator.setLevelMember( 0, FactTable2.L3Col );

		levelDefs = new ILevelDefn[1];
		levelDefs[0] = new LevelDefinition( "level31", new String[]{"level31"}, null );
		dimensions[2] = (Dimension) DimensionFactory.createDimension( "dimension3",
				documentManager,
				iterator,
				levelDefs,
				false, new StopSign() );
		hierarchy = dimensions[2].getHierarchy( );
		assertEquals( hierarchy.getName( ), "dimension3" );
		assertEquals( dimensions[2].length( ), 12 );
		
		FactTable2 factTable2 = new FactTable2();
		String[] measureColumnName = new String[2];
		measureColumnName[0] = "measure1";
		measureColumnName[1] = "measure2";
		FactTableAccessor factTableConstructor = new FactTableAccessor( documentManager );
		FactTable factTable = factTableConstructor.saveFactTable( NamingUtil.getFactTableName( "bigThreeDimensions" ),
				CubeUtility.getKeyColNames(dimensions),
				CubeUtility.getKeyColNames(dimensions),
				factTable2,
				dimensions,
				measureColumnName,
				new StopSign( ) );
		// assertEquals(factTable.getSegmentNumber( ), 1);
		factTable = factTableConstructor.load( NamingUtil.getFactTableName( "bigThreeDimensions" ),
				new StopSign( ) );
		String[] dimensionNames = new String[3];
		dimensionNames[0] = "dimension1";
		dimensionNames[1] = "dimension2";
		dimensionNames[2] = "dimension3";
		
		ILevel[] level = dimensions[1].getHierarchy( ).getLevels( );

		ISelection[][] filter = new ISelection[1][1];
		filter[0][0] = SelectionFactory.createRangeSelection(  new Object[]{new Integer( 1 )},
				 new Object[]{new Integer( 3 )},
				true,
				false );
		Level[] findLevel = new Level[1];
		findLevel[0] = (Level) level[0];

		IDiskArray[] positionForFilter = null;
		positionForFilter = new IDiskArray[2];
		
		IDiskArray positionArray = dimensions[1].find( findLevel, filter );
		positionForFilter[0] = positionArray;
		assertEquals( positionArray.size( ), 2 );
		DimensionResultIterator[] dimesionResultSets = new DimensionResultIterator[2];
		dimesionResultSets[0] = new DimensionResultIterator( dimensions[1],
				positionArray, new StopSign());
		positionArray = dimensions[2].findAll( );
		dimesionResultSets[1] = new DimensionResultIterator( dimensions[2],
				positionArray, new StopSign());
		
		String[] dimensionNamesForFilter = new String[2];
		dimensionNamesForFilter[0] = "dimension2";
		dimensionNamesForFilter[1] = "dimension3";
		
		positionForFilter[1] = positionArray;
		FactTableRowIterator facttableRowIterator = new FactTableRowIterator( factTable,
				dimensionNamesForFilter,
				positionForFilter,
				new StopSign( ) );
		assertTrue( facttableRowIterator != null );
		AggregationDefinition[] aggregations = new AggregationDefinition[2];
		int[] sortType = new int[1];
		sortType[0] = IDimensionSortDefn.SORT_ASC;
		DimLevel[] levelsForFilter = new DimLevel[]{dimLevel21};
		AggregationFunctionDefinition[] funcitons = new AggregationFunctionDefinition[1];
		funcitons[0] = new AggregationFunctionDefinition( "measure1", IBuildInAggregation.TOTAL_SUM_FUNC );
		aggregations[0] = new AggregationDefinition( levelsForFilter, sortType, funcitons );
		sortType = new int[1];
		sortType[0] = IDimensionSortDefn.SORT_ASC;
		//sortType[1] = IDimensionSortDefn.SORT_ASC;
		levelsForFilter = new DimLevel[]{dimLevel31};
		aggregations[1] = new AggregationDefinition( levelsForFilter, sortType, funcitons );
		IDataSet4Aggregation dataSet4Aggregation 
			= new DataSetFromOriginalCube( facttableRowIterator, dimesionResultSets, null );
		AggregationExecutor aggregationCalculatorExecutor = 
			new AggregationExecutor( null, dataSet4Aggregation,
					aggregations,
					10*1024*1024 );
		IAggregationResultSet[] resultSet = aggregationCalculatorExecutor.execute( new StopSign( ) );
		assertEquals( resultSet[0].length( ), 2 );
		assertEquals( resultSet[0].getAggregationDataType( 0 ), DataType.DOUBLE_TYPE );
		assertEquals( resultSet[0].getLevelIndex( dimLevel21 ), 0 );
		assertEquals( resultSet[0].getLevelKeyDataType( dimLevel21, "level21" ), DataType.INTEGER_TYPE );
		resultSet[0].seek( 0 );
		assertEquals( resultSet[0].getLevelKeyValue( 0 )[0], new Integer(1) );
		assertEquals( resultSet[0].getAggregationValue( 0 ), new Double(6) );
		resultSet[0].seek( 1 );
		assertEquals( resultSet[0].getLevelKeyValue( 0 )[0], new Integer(2) );
		assertEquals( resultSet[0].getAggregationValue( 0 ), new Double(22) );
		
		assertEquals( resultSet[1].length( ), 8 );
		assertEquals( resultSet[1].getAggregationDataType( 0 ), DataType.DOUBLE_TYPE );
		assertEquals( resultSet[1].getLevelIndex( dimLevel31 ), 0 );
		assertEquals( resultSet[1].getLevelKeyDataType( dimLevel31, "level31" ), DataType.INTEGER_TYPE );
		resultSet[1].seek( 0 );
		assertEquals( resultSet[1].getLevelKeyValue( 0 )[0], new Integer(1) );
		assertEquals( resultSet[1].getAggregationValue( 0 ), new Double(0) );
		resultSet[1].seek( 1 );
		assertEquals( resultSet[1].getLevelKeyValue( 0 )[0], new Integer(2) );
		assertEquals( resultSet[1].getAggregationValue( 0 ), new Double(1) );
		resultSet[1].seek( 2 );
		assertEquals( resultSet[1].getLevelKeyValue( 0 )[0], new Integer(3) );
		assertEquals( resultSet[1].getAggregationValue( 0 ), new Double(2) );
		resultSet[1].seek( 3 );
		assertEquals( resultSet[1].getLevelKeyValue( 0 )[0], new Integer(4) );
		assertEquals( resultSet[1].getAggregationValue( 0 ), new Double(3) );
		resultSet[1].seek( 4 );
		assertEquals( resultSet[1].getLevelKeyValue( 0 )[0], new Integer(5) );
		assertEquals( resultSet[1].getAggregationValue( 0 ), new Double(4) );
		resultSet[1].seek( 5 );
		assertEquals( resultSet[1].getLevelKeyValue( 0 )[0], new Integer(6) );
		assertEquals( resultSet[1].getAggregationValue( 0 ), new Double(5) );
		resultSet[1].seek( 6 );
		assertEquals( resultSet[1].getLevelKeyValue( 0 )[0], new Integer(7) );
		assertEquals( resultSet[1].getAggregationValue( 0 ), new Double(6) );
		resultSet[1].seek( 7 );
		assertEquals( resultSet[1].getLevelKeyValue( 0 )[0], new Integer(8) );
		assertEquals( resultSet[1].getAggregationValue( 0 ), new Double(7) );
		closeResultSets( resultSet );
	}
	
	/**
	 * 
	 * @throws IOException
	 * @throws BirtException
	 */
	@Test
    public void testFactTableSaveAndLoad4( ) throws IOException, BirtException
	{
		IDocumentManager documentManager = DocumentManagerFactory.createFileDocumentManager( );
		
		testFactTableSaveAndLoad4( documentManager );
		
		documentManager.close( );
	}
	
	private void testFactTableSaveAndLoad4( IDocumentManager documentManager ) throws IOException, BirtException
	{
		Dimension[] dimensions = new Dimension[3];
		
		// dimension0
		String[] levelNames = new String[3];
		levelNames[0] = "level11";
		levelNames[1] = "level12";
		levelNames[2] = "level13";
		DimensionForTest iterator = new DimensionForTest( levelNames );
		iterator.setLevelMember( 0, FactTable2.L1Col );
		iterator.setLevelMember( 1, FactTable2.L2Col );
		iterator.setLevelMember( 2, FactTable2.L3Col );
		
		ILevelDefn[] levelDefs = new ILevelDefn[3];
		levelDefs[0] = new LevelDefinition( "level11", new String[]{"level11"}, null );
		levelDefs[1] = new LevelDefinition( "level12", new String[]{"level12"}, null );
		levelDefs[2] = new LevelDefinition( "level13", new String[]{"level13"}, null );
		dimensions[0] = (Dimension) DimensionFactory.createDimension( "dimension1", documentManager, iterator, levelDefs, false, new StopSign() );
		IHierarchy hierarchy = dimensions[0].getHierarchy( );
		assertEquals( hierarchy.getName( ), "dimension1" );
		assertEquals( dimensions[0].length( ), FactTable2.L1Col.length );
		
		//dimension1
		levelNames = new String[1];
		levelNames[0] = "level21";
		iterator = new DimensionForTest( levelNames );
		iterator.setLevelMember( 0, distinct( FactTable2.L1Col ) );
		
		levelDefs = new ILevelDefn[1];
		levelDefs[0] = new LevelDefinition( "level21", new String[]{"level21"}, null );
		dimensions[1] = (Dimension) DimensionFactory.createDimension( "dimension2", documentManager, iterator, levelDefs, false, new StopSign() );
		hierarchy = dimensions[1].getHierarchy( );
		assertEquals( hierarchy.getName( ), "dimension2" );
		assertEquals( dimensions[1].length( ), 3 );
		
		// dimension2
//		 dimension2
		levelNames = new String[1];
		levelNames[0] = "level31";
		
		iterator = new DimensionForTest( levelNames );
		iterator.setLevelMember( 0, FactTable2.L3Col );

		levelDefs = new ILevelDefn[1];
		levelDefs[0] = new LevelDefinition( "level31", new String[]{"level31"}, null );
		dimensions[2] = (Dimension) DimensionFactory.createDimension( "dimension3",
				documentManager,
				iterator,
				levelDefs,
				false, new StopSign() );
		
		hierarchy = dimensions[2].getHierarchy( );
		assertEquals( hierarchy.getName( ), "dimension3" );
		assertEquals( dimensions[2].length( ), 12 );
		
		FactTable2 factTable2 = new FactTable2();
		String[] measureColumnName = new String[2];
		measureColumnName[0] = "measure1";
		measureColumnName[1] = "measure2";
		Cube cube = new Cube( "cube", documentManager );
		
		cube.create( CubeUtility.getKeyColNames(dimensions), dimensions, factTable2, measureColumnName, new StopSign( ) );
		CubeQueryExecutorHelper cubeQueryExcutorHelper = new CubeQueryExecutorHelper( cube );
		ISelection[][] filter = new ISelection[1][1];
		filter[0][0] = SelectionFactory.createRangeSelection(  new Object[]{new Integer( 1 )},
				 new Object[]{new Integer( 3 )},
				true,
				false );
		cubeQueryExcutorHelper.addSimpleLevelFilter( new SimpleLevelFilter( dimLevel21,
				filter[0] ) );
		
		
		AggregationDefinition[] aggregations = new AggregationDefinition[2];
		int[] sortType = new int[1];
		sortType[0] = IDimensionSortDefn.SORT_ASC;
		DimLevel[] levelsForFilter = new DimLevel[]{dimLevel21};
		AggregationFunctionDefinition[] funcitons = new AggregationFunctionDefinition[1];
		funcitons[0] = new AggregationFunctionDefinition( "measure1", IBuildInAggregation.TOTAL_SUM_FUNC );
		aggregations[0] = new AggregationDefinition( levelsForFilter, sortType, funcitons );
		sortType = new int[1];
		sortType[0] = IDimensionSortDefn.SORT_ASC;
		//sortType[1] = IDimensionSortDefn.SORT_ASC;
		levelsForFilter = new DimLevel[]{dimLevel31};
		aggregations[1] = new AggregationDefinition( levelsForFilter, sortType, funcitons );
		
		IAggregationResultSet[] resultSet = cubeQueryExcutorHelper.execute( aggregations,
				new StopSign( ) );
		assertEquals( resultSet[0].length( ), 2 );
		assertEquals( resultSet[0].getAggregationDataType( 0 ), DataType.DOUBLE_TYPE );
		assertEquals( resultSet[0].getLevelIndex( dimLevel21 ), 0 );
		assertEquals( resultSet[0].getLevelKeyDataType( dimLevel21, "level21" ), DataType.INTEGER_TYPE );
		resultSet[0].seek( 0 );
		assertEquals( resultSet[0].getLevelKeyValue( 0 )[0], new Integer(1) );
		assertEquals( resultSet[0].getAggregationValue( 0 ), new Double(6) );
		resultSet[0].seek( 1 );
		assertEquals( resultSet[0].getLevelKeyValue( 0 )[0], new Integer(2) );
		assertEquals( resultSet[0].getAggregationValue( 0 ), new Double(22) );
		
		assertEquals( resultSet[1].length( ), 8 );
		assertEquals( resultSet[1].getAggregationDataType( 0 ), DataType.DOUBLE_TYPE );
		assertEquals( resultSet[1].getLevelIndex( dimLevel31 ), 0 );
		assertEquals( resultSet[1].getLevelKeyDataType( dimLevel31, "level31" ), DataType.INTEGER_TYPE );
		resultSet[1].seek( 0 );
		assertEquals( resultSet[1].getLevelKeyValue( 0 )[0], new Integer(1) );
		assertEquals( resultSet[1].getAggregationValue( 0 ), new Double(0) );
		resultSet[1].seek( 1 );
		assertEquals( resultSet[1].getLevelKeyValue( 0 )[0], new Integer(2) );
		assertEquals( resultSet[1].getAggregationValue( 0 ), new Double(1) );
		resultSet[1].seek( 2 );
		assertEquals( resultSet[1].getLevelKeyValue( 0 )[0], new Integer(3) );
		assertEquals( resultSet[1].getAggregationValue( 0 ), new Double(2) );
		resultSet[1].seek( 3 );
		assertEquals( resultSet[1].getLevelKeyValue( 0 )[0], new Integer(4) );
		assertEquals( resultSet[1].getAggregationValue( 0 ), new Double(3) );
		resultSet[1].seek( 4 );
		assertEquals( resultSet[1].getLevelKeyValue( 0 )[0], new Integer(5) );
		assertEquals( resultSet[1].getAggregationValue( 0 ), new Double(4) );
		resultSet[1].seek( 5 );
		assertEquals( resultSet[1].getLevelKeyValue( 0 )[0], new Integer(6) );
		assertEquals( resultSet[1].getAggregationValue( 0 ), new Double(5) );
		resultSet[1].seek( 6 );
		assertEquals( resultSet[1].getLevelKeyValue( 0 )[0], new Integer(7) );
		assertEquals( resultSet[1].getAggregationValue( 0 ), new Double(6) );
		resultSet[1].seek( 7 );
		assertEquals( resultSet[1].getLevelKeyValue( 0 )[0], new Integer(8) );
		assertEquals( resultSet[1].getAggregationValue( 0 ), new Double(7) );
		closeResultSets( resultSet );
	}
	
	/**
	 * 
	 * @throws IOException
	 * @throws BirtException
	 */
	@Test
    public void testFactTableSaveAndLoad5( ) throws IOException, BirtException
	{
		DataEngineContext context = DataEngineContext.newInstance( DataEngineContext.DIRECT_PRESENTATION,
				null,
				null,
				null );
		context.setTmpdir( this.getTempDir( ) );
		DataEngineImpl engine = (DataEngineImpl)DataEngine.newDataEngine( context );
		CubeMaterializer cubeCreatorHelper = new CubeMaterializer( engine, "cub1" );
		
		testFactTableSaveAndLoad5( cubeCreatorHelper );
		
		cubeCreatorHelper.close( );
	}
	
	private void testFactTableSaveAndLoad5( CubeMaterializer cubeMaterializer ) throws IOException, BirtException
	{
		Dimension[] dimensions = new Dimension[3];
		
		// dimension0
		String[] levelNames = new String[3];
		levelNames[0] = "level11";
		levelNames[1] = "level12";
		levelNames[2] = "level13";
		DimensionForTest iterator = new DimensionForTest( levelNames );
		iterator.setLevelMember( 0, FactTable2.L1Col );
		iterator.setLevelMember( 1, FactTable2.L2Col );
		iterator.setLevelMember( 2, FactTable2.L3Col );
		
		ILevelDefn[] levelDefs = new ILevelDefn[3];
		levelDefs[0] = CubeElementFactory.createLevelDefinition( "level11",
				new String[]{
					"level11"
				},
				null );
		levelDefs[1] = CubeElementFactory.createLevelDefinition( "level12",
				new String[]{
					"level12"
				},
				null );
		levelDefs[2] = CubeElementFactory.createLevelDefinition( "level13",
				new String[]{
					"level13"
				},
				null );
		IHierarchy hierarchy = cubeMaterializer.createHierarchy( "dimension1",
				"hir1", iterator,
				levelDefs, new StopSign() );
		dimensions[0] = (Dimension) cubeMaterializer.createDimension( "dimension1",
				hierarchy );
		assertEquals( hierarchy.getName( ), "hir1" );
		assertEquals( dimensions[0].length( ), FactTable2.L1Col.length );
		
		//dimension1
		levelNames = new String[1];
		levelNames[0] = "level21";
		iterator = new DimensionForTest( levelNames );
		iterator.setLevelMember( 0, distinct( FactTable2.L1Col ) );
		
		levelDefs = new ILevelDefn[1];
		levelDefs[0] = CubeElementFactory.createLevelDefinition( "level21",
				new String[]{"level21"},
				null );
		hierarchy = cubeMaterializer.createHierarchy( "dimension2",
				"hir2",
				iterator,
				levelDefs,
				new StopSign() );
		dimensions[1] = (Dimension) cubeMaterializer.createDimension( "dimension2",
				hierarchy );
		hierarchy = dimensions[1].getHierarchy( );
		assertEquals( hierarchy.getName( ), "hir2" );
		assertEquals( dimensions[1].length( ), 3 );
		
		// dimension2
		levelNames = new String[1];
		levelNames[0] = "level31";
		
		iterator = new DimensionForTest( levelNames );
		iterator.setLevelMember( 0, FactTable2.L3Col );

		levelDefs = new ILevelDefn[1];
		levelDefs[0] = new LevelDefinition( "level31", new String[]{"level31"}, null );
		hierarchy = cubeMaterializer.createHierarchy( "dimension3","hir3",
				iterator,
				levelDefs,
				new StopSign() );
		dimensions[2] = (Dimension) cubeMaterializer.createDimension( "dimension3",
				hierarchy );
		hierarchy = dimensions[2].getHierarchy( );
		assertEquals( hierarchy.getName( ), "hir3" );
		assertEquals( dimensions[2].length( ), 12 );
		
		FactTable2 factTable2 = new FactTable2();
		String[] measureColumnName = new String[2];
		measureColumnName[0] = "measure1";
		measureColumnName[1] = "measure2";
		cubeMaterializer.createCube( "cube", CubeUtility.getKeyColNames(dimensions),dimensions, factTable2, measureColumnName, new StopSign( ) );
		
		CubeQueryExecutorHelper cubeQueryExcutorHelper = 
			new CubeQueryExecutorHelper( CubeQueryExecutorHelper.loadCube( "cube", cubeMaterializer.getDocumentManager( ), new StopSign( ) ) );
		ISelection[][] filter = new ISelection[1][2];
		filter[0][0] = SelectionFactory.createRangeSelection(  new Object[]{new Integer( 1 )},
				 new Object[]{new Integer( 2 )},
				true,
				false );
		filter[0][1] = SelectionFactory.createRangeSelection(  new Object[]{new Integer( 2 )},
				 new Object[]{new Integer( 3 )},
				true,
				false );
		cubeQueryExcutorHelper.addSimpleLevelFilter( new SimpleLevelFilter(dimLevel21, filter[0]) );
		
		AggregationDefinition[] aggregations = new AggregationDefinition[2];
		int[] sortType = new int[1];
		sortType[0] = IDimensionSortDefn.SORT_ASC;
		DimLevel[] levelsForFilter = new DimLevel[]{dimLevel21};
		AggregationFunctionDefinition[] funcitons = new AggregationFunctionDefinition[1];
		funcitons[0] = new AggregationFunctionDefinition( "measure1", IBuildInAggregation.TOTAL_SUM_FUNC );
		aggregations[0] = new AggregationDefinition( levelsForFilter, sortType, funcitons );
		sortType = new int[2];
		sortType[0] = IDimensionSortDefn.SORT_ASC;
		sortType[1] = IDimensionSortDefn.SORT_ASC;
		levelsForFilter = new DimLevel[]{dimLevel31};
		aggregations[1] = new AggregationDefinition( levelsForFilter, sortType, funcitons );
		
		IAggregationResultSet[] resultSet = cubeQueryExcutorHelper.execute( aggregations,
				new StopSign( ) );
		assertEquals( resultSet[0].length( ), 2 );
		assertEquals( resultSet[0].getAggregationDataType( 0 ), DataType.DOUBLE_TYPE );
		assertEquals( resultSet[0].getLevelIndex( dimLevel21 ), 0 );
		assertEquals( resultSet[0].getLevelKeyDataType( dimLevel21, "level21" ), DataType.INTEGER_TYPE );
		resultSet[0].seek( 0 );
		assertEquals( resultSet[0].getLevelKeyValue( 0 )[0], new Integer(1) );
		assertEquals( resultSet[0].getAggregationValue( 0 ), new Double(6) );
		resultSet[0].seek( 1 );
		assertEquals( resultSet[0].getLevelKeyValue( 0 )[0], new Integer(2) );
		assertEquals( resultSet[0].getAggregationValue( 0 ), new Double(22) );
		
		assertEquals( resultSet[1].length( ), 8 );
		assertEquals( resultSet[1].getAggregationDataType( 0 ), DataType.DOUBLE_TYPE );
		assertEquals( resultSet[1].getLevelIndex( dimLevel31 ), 0 );
		assertEquals( resultSet[1].getLevelKeyDataType( dimLevel31, "level31" ), DataType.INTEGER_TYPE );
		resultSet[1].seek( 0 );
		assertEquals( resultSet[1].getLevelKeyValue( 0 )[0], new Integer(1) );
		assertEquals( resultSet[1].getAggregationValue( 0 ), new Double(0) );
		resultSet[1].seek( 1 );
		assertEquals( resultSet[1].getLevelKeyValue( 0 )[0], new Integer(2) );
		assertEquals( resultSet[1].getAggregationValue( 0 ), new Double(1) );
		resultSet[1].seek( 2 );
		assertEquals( resultSet[1].getLevelKeyValue( 0 )[0], new Integer(3) );
		assertEquals( resultSet[1].getAggregationValue( 0 ), new Double(2) );
		resultSet[1].seek( 3 );
		assertEquals( resultSet[1].getLevelKeyValue( 0 )[0], new Integer(4) );
		assertEquals( resultSet[1].getAggregationValue( 0 ), new Double(3) );
		resultSet[1].seek( 4 );
		assertEquals( resultSet[1].getLevelKeyValue( 0 )[0], new Integer(5) );
		assertEquals( resultSet[1].getAggregationValue( 0 ), new Double(4) );
		resultSet[1].seek( 5 );
		assertEquals( resultSet[1].getLevelKeyValue( 0 )[0], new Integer(6) );
		assertEquals( resultSet[1].getAggregationValue( 0 ), new Double(5) );
		resultSet[1].seek( 6 );
		assertEquals( resultSet[1].getLevelKeyValue( 0 )[0], new Integer(7) );
		assertEquals( resultSet[1].getAggregationValue( 0 ), new Double(6) );
		resultSet[1].seek( 7 );
		assertEquals( resultSet[1].getLevelKeyValue( 0 )[0], new Integer(8) );
		assertEquals( resultSet[1].getAggregationValue( 0 ), new Double(7) );
		closeResultSets( resultSet );
	}
	@Test
    public void testMaxAggregation() throws IOException, BirtException
	{
		IDocumentManager documentManager = DocumentManagerFactory.createFileDocumentManager( );
		
		testMaxAggregation( documentManager );
		
		documentManager.close( );
	}
	
	private void testMaxAggregation( IDocumentManager documentManager ) throws IOException, BirtException, DataException
	{
		Dimension[] dimensions = new Dimension[3];
		
		// dimension0
		String[] levelNames = new String[3];
		levelNames[0] = "level11";
		levelNames[1] = "level12";
		levelNames[2] = "level13";
		DimensionForTest iterator = new DimensionForTest( levelNames );
		iterator.setLevelMember( 0, FactTable2.L1Col );
		iterator.setLevelMember( 1, FactTable2.L2Col );
		iterator.setLevelMember( 2, FactTable2.L3Col );
		
		ILevelDefn[] levelDefs = new ILevelDefn[3];
		levelDefs[0] = new LevelDefinition( "level11", new String[]{"level11"}, null );
		levelDefs[1] = new LevelDefinition( "level12", new String[]{"level12"}, null );
		levelDefs[2] = new LevelDefinition( "level13", new String[]{"level13"}, null );
		dimensions[0] = (Dimension) DimensionFactory.createDimension( "dimension1",
				documentManager,
				iterator,
				levelDefs,
				false, new StopSign() );
		IHierarchy hierarchy = dimensions[0].getHierarchy( );
		assertEquals( hierarchy.getName( ), "dimension1" );
		assertEquals( dimensions[0].length( ), FactTable2.L3Col.length );
		
		//dimension1
		levelNames = new String[1];
		levelNames[0] = "level21";
		iterator = new DimensionForTest( levelNames );
		iterator.setLevelMember( 0, distinct(FactTable2.L1Col) );
		
		levelDefs = new ILevelDefn[1];
		levelDefs[0] = new LevelDefinition( "level21", new String[]{"level21"}, null );
		dimensions[1] = (Dimension) DimensionFactory.createDimension( "dimension2", documentManager, iterator, levelDefs, false, new StopSign() );
		hierarchy = dimensions[1].getHierarchy( );
		assertEquals( hierarchy.getName( ), "dimension2" );
		assertEquals( dimensions[1].length( ), 3 );
		
		// dimension2
		levelNames = new String[1];
		levelNames[0] = "level31";
		
		iterator = new DimensionForTest( levelNames );
		iterator.setLevelMember( 0, FactTable2.L3Col );

		levelDefs = new ILevelDefn[1];
		levelDefs[0] = new LevelDefinition( "level31", new String[]{"level31"}, null );
		dimensions[2] = (Dimension) DimensionFactory.createDimension( "dimension3",
				documentManager,
				iterator,
				levelDefs,
				false, new StopSign() );
		hierarchy = dimensions[2].getHierarchy( );
		assertEquals( hierarchy.getName( ), "dimension3" );
		assertEquals( dimensions[2].length( ), 12 );
		
		FactTable2 factTable2 = new FactTable2();
		String[] measureColumnName = new String[2];
		measureColumnName[0] = "measure1";
		measureColumnName[1] = "measure2";
		FactTableAccessor factTableConstructor = new FactTableAccessor( documentManager );
		FactTable factTable = factTableConstructor.saveFactTable( NamingUtil.getFactTableName( "bigThreeDimensions" ),
				CubeUtility.getKeyColNames(dimensions),
				CubeUtility.getKeyColNames(dimensions),
				factTable2,
				dimensions,
				measureColumnName,
				new StopSign( ) );
		// assertEquals(factTable.getSegmentNumber( ), 1);
		factTable = factTableConstructor.load( NamingUtil.getFactTableName( "bigThreeDimensions" ),
				new StopSign( ) );
		String[] dimensionNames = new String[3];
		dimensionNames[0] = "dimension1";
		dimensionNames[1] = "dimension2";
		dimensionNames[2] = "dimension3";
		
		DimensionResultIterator[] dimesionResultSets = new DimensionResultIterator[1];
		IDiskArray positionArray = dimensions[0].findAll( );
		dimesionResultSets[0] = new DimensionResultIterator( dimensions[0],
				positionArray, new StopSign());
		
		String[] dimensionNamesForFilter = new String[1];
		dimensionNamesForFilter[0] = "dimension1";
		
		IDiskArray[] positionsForFilter = new IDiskArray[1];
		positionsForFilter[0] = positionArray;
		FactTableRowIterator facttableRowIterator = new FactTableRowIterator( factTable,
				dimensionNamesForFilter,
				positionsForFilter,
				new StopSign( ) );
		assertTrue( facttableRowIterator != null );
		AggregationDefinition[] aggregations = new AggregationDefinition[1];
		AggregationFunctionDefinition[] funcitons = new AggregationFunctionDefinition[1];
		funcitons[0] = new AggregationFunctionDefinition( "max", "measure1", IBuildInAggregation.TOTAL_MAX_FUNC );
		aggregations[0] = new AggregationDefinition( null, null, funcitons );

		IDataSet4Aggregation dataSet4Aggregation 
			= new DataSetFromOriginalCube( facttableRowIterator, dimesionResultSets, null );
		AggregationExecutor aggregationCalculatorExecutor = 
				new AggregationExecutor( null, dataSet4Aggregation,
						aggregations
						, 10*1024*1024);
		IAggregationResultSet[] resultSet = aggregationCalculatorExecutor.execute( new StopSign( ) );
		assertEquals( resultSet[0].length( ), 1 );
		
		resultSet[0].seek( 0 );
		assertEquals( resultSet[0].getAggregationValue( 0 ), 11 );
		closeResultSets( resultSet );
	}
	
	/**
	 * 
	 * @throws IOException
	 * @throws BirtException
	 */
	@Test
    public void testNestAggregation( ) throws IOException, BirtException
	{
		IDocumentManager documentManager = DocumentManagerFactory.createFileDocumentManager( );
		
		testNestAggregation( documentManager );
		
		documentManager.close( );
	}
	
	private void closeResultSets( IAggregationResultSet[] resultSets ) throws IOException
	{
		for ( IAggregationResultSet rs : resultSets )
		{
			rs.close( );
		}
	}
	
	private void testNestAggregation( IDocumentManager documentManager ) throws IOException, BirtException, DataException
	{
		Dimension[] dimensions = new Dimension[3];
		
		// dimension0
		String[] levelNames = new String[3];
		levelNames[0] = "level11";
		levelNames[1] = "level12";
		levelNames[2] = "level13";
		DimensionForTest iterator = new DimensionForTest( levelNames );
		iterator.setLevelMember( 0, FactTable2.L1Col );
		iterator.setLevelMember( 1, FactTable2.L2Col );
		iterator.setLevelMember( 2, FactTable2.L3Col );
		
		ILevelDefn[] levelDefs = new ILevelDefn[3];
		levelDefs[0] = new LevelDefinition( "level11", new String[]{"level11"}, null );
		levelDefs[1] = new LevelDefinition( "level12", new String[]{"level12"}, null );
		levelDefs[2] = new LevelDefinition( "level13", new String[]{"level13"}, null );
		dimensions[0] = (Dimension) DimensionFactory.createDimension( "dimension1",
				documentManager,
				iterator,
				levelDefs,
				false, new StopSign() );
		IHierarchy hierarchy = dimensions[0].getHierarchy( );
		assertEquals( hierarchy.getName( ), "dimension1" );
		assertEquals( dimensions[0].length( ), FactTable2.L3Col.length );
		
		//dimension1
		levelNames = new String[1];
		levelNames[0] = "level21";
		iterator = new DimensionForTest( levelNames );
		iterator.setLevelMember( 0, distinct(FactTable2.L1Col) );
		
		levelDefs = new ILevelDefn[1];
		levelDefs[0] = new LevelDefinition( "level21", new String[]{"level21"}, null );
		dimensions[1] = (Dimension) DimensionFactory.createDimension( "dimension2", documentManager, iterator, levelDefs, false, new StopSign() );
		hierarchy = dimensions[1].getHierarchy( );
		assertEquals( hierarchy.getName( ), "dimension2" );
		assertEquals( dimensions[1].length( ), 3 );
		
		// dimension2
		levelNames = new String[1];
		levelNames[0] = "level31";
		
		iterator = new DimensionForTest( levelNames );
		iterator.setLevelMember( 0, FactTable2.L3Col );

		levelDefs = new ILevelDefn[1];
		levelDefs[0] = new LevelDefinition( "level31", new String[]{"level31"}, null );
		dimensions[2] = (Dimension) DimensionFactory.createDimension( "dimension3",
				documentManager,
				iterator,
				levelDefs,
				false, new StopSign() );
		hierarchy = dimensions[2].getHierarchy( );
		assertEquals( hierarchy.getName( ), "dimension3" );
		assertEquals( dimensions[2].length( ), 12 );
		
		FactTable2 factTable2 = new FactTable2();
		String[] measureColumnName = new String[2];
		measureColumnName[0] = "measure1";
		measureColumnName[1] = "measure2";
		FactTableAccessor factTableConstructor = new FactTableAccessor( documentManager );
		FactTable factTable = factTableConstructor.saveFactTable( NamingUtil.getFactTableName( "bigThreeDimensions" ),
				CubeUtility.getKeyColNames(dimensions),
				CubeUtility.getKeyColNames(dimensions),
				factTable2,
				dimensions,
				measureColumnName,
				new StopSign( ) );
		// assertEquals(factTable.getSegmentNumber( ), 1);
		factTable = factTableConstructor.load( NamingUtil.getFactTableName( "bigThreeDimensions" ),
				new StopSign( ) );
		String[] dimensionNames = new String[3];
		dimensionNames[0] = "dimension1";
		dimensionNames[1] = "dimension2";
		dimensionNames[2] = "dimension3";
		
		DimensionResultIterator[] dimesionResultSets = new DimensionResultIterator[1];
		IDiskArray positionArray = dimensions[0].findAll( );
		dimesionResultSets[0] = new DimensionResultIterator( dimensions[0],
				positionArray, new StopSign());
		
		String[] dimensionNamesForFilter = new String[1];
		dimensionNamesForFilter[0] = "dimension1";
		
		IDiskArray[] positionsForFilter = new IDiskArray[1];
		positionsForFilter[0] = positionArray;
		FactTableRowIterator facttableRowIterator = new FactTableRowIterator( factTable,
				dimensionNamesForFilter,
				positionsForFilter,
				new StopSign( ) );
		assertTrue( facttableRowIterator != null );
		AggregationDefinition[] aggregations = new AggregationDefinition[1];
		int[] sortType = new int[2];
		sortType[0] = IDimensionSortDefn.SORT_ASC;
		sortType[1] = IDimensionSortDefn.SORT_ASC;
		DimLevel[] levelsForFilter = new DimLevel[]{dimLevel11, dimLevel12};
		AggregationFunctionDefinition[] funcitons = new AggregationFunctionDefinition[1];
		funcitons[0] = new AggregationFunctionDefinition( "total", "measure1", IBuildInAggregation.TOTAL_SUM_FUNC );
		aggregations[0] = new AggregationDefinition( levelsForFilter, sortType, funcitons );

		IDataSet4Aggregation dataSet4Aggregation 
			= new DataSetFromOriginalCube( facttableRowIterator, dimesionResultSets, null );
		AggregationExecutor aggregationCalculatorExecutor = 
				new AggregationExecutor( null, dataSet4Aggregation,
						aggregations, 10*1024*1024 );
		IAggregationResultSet[] resultSet = aggregationCalculatorExecutor.execute( new StopSign( ) );
		assertEquals( resultSet[0].length( ), 6 );
		assertEquals( resultSet[0].getAggregationDataType( 0 ), DataType.DOUBLE_TYPE );
		assertEquals( resultSet[0].getLevelIndex( dimLevel11 ), 0 );
		assertEquals( resultSet[0].getLevelIndex( dimLevel12 ), 1 );
		
		resultSet[0].seek( 0 );
		assertEquals( resultSet[0].getLevelKeyValue( 0 )[0], new Integer(1) );
		assertEquals( resultSet[0].getAggregationValue( 0 ), new Double(1) );
		resultSet[0].seek( 1 );
		assertEquals( resultSet[0].getLevelKeyValue( 0 )[0], new Integer(1) );
		assertEquals( resultSet[0].getAggregationValue( 0 ), new Double(5) );
		resultSet[0].seek( 2 );
		assertEquals( resultSet[0].getLevelKeyValue( 0 )[0], new Integer(2) );
		assertEquals( resultSet[0].getAggregationValue( 0 ), new Double(9) );
		resultSet[0].seek( 3 );
		assertEquals( resultSet[0].getLevelKeyValue( 0 )[0], new Integer(2) );
		assertEquals( resultSet[0].getAggregationValue( 0 ), new Double(13) );
		resultSet[0].seek( 4 );
		assertEquals( resultSet[0].getLevelKeyValue( 0 )[0], new Integer(3) );
		assertEquals( resultSet[0].getAggregationValue( 0 ), new Double(17) );
		resultSet[0].seek( 5 );
		assertEquals( resultSet[0].getLevelKeyValue( 0 )[0], new Integer(3) );
		assertEquals( resultSet[0].getAggregationValue( 0 ), new Double(21) );
		
		IAggregationResultSet sourceAggrResultSet = resultSet[0]; 
		
		/////////////////test nest aggregation with max function/////////////////
		aggregations = new AggregationDefinition[1];
		funcitons = new AggregationFunctionDefinition[1];
		funcitons[0] = new AggregationFunctionDefinition( "max_total", "total", IBuildInAggregation.TOTAL_MAX_FUNC );
		aggregations[0] = new AggregationDefinition( null, null, funcitons );

		Context context = Context.enter( );
		ScriptableObject scope = context.initStandardObjects( );
		ScriptContext cx = new ScriptContext( );
		IAggregationResultSet ars 
			= new AggregationResultSetWithOneMoreDummyAggr( sourceAggrResultSet, 
					"max_total", new ScriptExpression("data[\"total\"]"), scope, cx);
		resultSet = AggregationHelper.execute( ars, aggregations, new StopSign( ) );
		assertEquals( resultSet[0].length( ), 1 );
		
		resultSet[0].seek( 0 );
		assertEquals( resultSet[0].getAggregationValue( 0 ), new Double(21) );
		
		
	    /////////////////test nest aggregation with sum function/////////////////
		aggregations = new AggregationDefinition[1];
		sortType = new int[1];
		sortType[0] = IDimensionSortDefn.SORT_ASC;
		levelsForFilter = new DimLevel[]{dimLevel11};
		funcitons = new AggregationFunctionDefinition[1];
		funcitons[0] = new AggregationFunctionDefinition( "sum_total", "total", IBuildInAggregation.TOTAL_SUM_FUNC );
		aggregations[0] = new AggregationDefinition( levelsForFilter, sortType, funcitons );

		ars
			= new AggregationResultSetWithOneMoreDummyAggr( sourceAggrResultSet,
					"sum_total", new ScriptExpression("data[\"total\"]"), scope, cx);
		resultSet = AggregationHelper.execute( ars, aggregations, new StopSign( ) );
		assertEquals( resultSet[0].length( ), 3 );
		assertEquals( resultSet[0].getAggregationDataType( 0 ), DataType.DOUBLE_TYPE );
		assertEquals( resultSet[0].getLevelIndex( dimLevel11 ), 0 );
		
		resultSet[0].seek( 0 );
		assertEquals( resultSet[0].getLevelKeyValue( 0 )[0], new Integer(1) );
		assertEquals( resultSet[0].getAggregationValue( 0 ), new Double(6) );
		resultSet[0].seek( 1 );
		assertEquals( resultSet[0].getLevelKeyValue( 0 )[0], new Integer(2) );
		assertEquals( resultSet[0].getAggregationValue( 0 ), new Double(22) );
		resultSet[0].seek( 2 );
		assertEquals( resultSet[0].getLevelKeyValue( 0 )[0], new Integer(3) );
		assertEquals( resultSet[0].getAggregationValue( 0 ), new Double(38) );
		closeResultSets( resultSet );
		
		Context.exit( );
		cx.close( );
	}
}

class FactTable1 implements IDatasetIterator
{

	int ptr = -1;
	static int[] L1Col = {
			1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3
	};
	static int[] L2Col = {
			1, 1, 2, 2, 1, 1, 2, 2, 2, 2, 3, 3
	};

	static int[] L3Col = {
			1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12
	};

	public void close( ) throws BirtException
	{
		// TODO Auto-generated method stub

	}

	public Boolean getBoolean( int fieldIndex ) throws BirtException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Date getDate( int fieldIndex ) throws BirtException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Double getDouble( int fieldIndex ) throws BirtException
	{
		// TODO Auto-generated method stub
		return null;
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
		else if ( name.equals( "level31" ) )
		{
			return 4;
		}
		else if ( name.equals( "level32" ) )
		{
			return 5;
		}
		else if ( name.equals( "measure1" ) )
		{
			return 6;
		}
		else if ( name.equals( "measure2" ) )
		{
			return 7;
		}
		return -1;
	}

	public int getFieldType( String name ) throws BirtException
	{
		if ( name.equals( "level11" ) )
		{
			return DataType.INTEGER_TYPE;
		}
		else if ( name.equals( "level12" ) )
		{
			return DataType.INTEGER_TYPE;
		}
		else if ( name.equals( "level13" ) )
		{
			return DataType.INTEGER_TYPE;
		}
		else if ( name.equals( "level21" ) )
		{
			return DataType.INTEGER_TYPE;
		}
		else if ( name.equals( "level31" ) )
		{
			return DataType.INTEGER_TYPE;
		}
		else if ( name.equals( "level32" ) )
		{
			return DataType.INTEGER_TYPE;
		}
		else if ( name.equals( "measure1" ) )
		{
			return DataType.INTEGER_TYPE;
		}
		else if ( name.equals( "measure2" ) )
		{
			return DataType.DOUBLE_TYPE;
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
			return new Integer( L1Col[ptr] );
		}
		else if ( fieldIndex == 1 )
		{
			return new Integer( L2Col[ptr] );
		}
		else if ( fieldIndex == 2 )
		{
			return new Integer( L3Col[ptr] );
		}
		else if ( fieldIndex == 3 )
		{
			return new Integer( L1Col[ptr] );
		}
		else if ( fieldIndex == 4 )
		{
			return new Integer( L1Col[ptr] );
		}
		else if ( fieldIndex == 5 )
		{
			return new Integer( L2Col[ptr] );
		}
		else if ( fieldIndex == 6 )
		{
			return new Integer( ptr );
		}
		else if ( fieldIndex == 7 )
		{
			return new Double( ptr );
		}
		return null;
	}

	public boolean next( ) throws BirtException
	{
		ptr++;
		if ( ptr >= L1Col.length )
		{
			return false;
		}
		return true;
	}
}

class FactTable2 implements IDatasetIterator
{

	int ptr = -1;
	static int[] L1Col = {
			1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3
	};
	static int[] L2Col = {
			1, 1, 2, 2, 1, 1, 2, 2, 2, 2, 3, 3
	};

	static int[] L3Col = {
			1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12
	};

	public void close( ) throws BirtException
	{
		// TODO Auto-generated method stub

	}

	public Boolean getBoolean( int fieldIndex ) throws BirtException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Date getDate( int fieldIndex ) throws BirtException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Double getDouble( int fieldIndex ) throws BirtException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public int getFieldIndex( String name ) throws BirtException
	{
		if ( name.equals( "level11" ) )
		{
			return 0;
		}
		else if ( name.equals( "level12" ) )
		{
			return -1;
		}
		else if ( name.equals( "level13" ) )
		{
			return 2;
		}
		else if ( name.equals( "level21" ) )
		{
			return 3;
		}
		else if ( name.equals( "level31" ) )
		{
			return 4;
		}
		else if ( name.equals( "measure1" ) )
		{
			return 5;
		}
		else if ( name.equals( "measure2" ) )
		{
			return 6;
		}
		return -1;
	}

	public int getFieldType( String name ) throws BirtException
	{
		if ( name.equals( "level11" ) )
		{
			return DataType.INTEGER_TYPE;
		}
		else if ( name.equals( "level12" ) )
		{
			return -1;
		}
		else if ( name.equals( "level13" ) )
		{
			return DataType.INTEGER_TYPE;
		}
		else if ( name.equals( "level21" ) )
		{
			return DataType.INTEGER_TYPE;
		}
		else if ( name.equals( "level31" ) )
		{
			return DataType.INTEGER_TYPE;
		}
		else if ( name.equals( "measure1" ) )
		{
			return DataType.INTEGER_TYPE;
		}
		else if ( name.equals( "measure2" ) )
		{
			return DataType.DOUBLE_TYPE;
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
			return new Integer( L1Col[ptr] );
		}
		else if ( fieldIndex == 1 )
		{
			return new Integer( L2Col[ptr] );
		}
		else if ( fieldIndex == 2 )
		{
			return new Integer( L3Col[ptr] );
		}
		else if ( fieldIndex == 3 )
		{
			return new Integer( L1Col[ptr] );
		}
		else if ( fieldIndex == 4 )
		{
			return new Integer( L3Col[ptr] );
		}
		else if ( fieldIndex == 5 )
		{
			return new Integer( ptr );
		}
		else if ( fieldIndex == 6 )
		{
			return new Double( ptr );
		}
		return null;
	}

	public boolean next( ) throws BirtException
	{
		ptr++;
		if ( ptr >= L1Col.length )
		{
			return false;
		}
		return true;
	}
}

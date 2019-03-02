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
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.impl.StopSign;
import org.eclipse.birt.data.engine.olap.cursor.CubeUtility;
import org.eclipse.birt.data.engine.olap.data.api.cube.IDatasetIterator;
import org.eclipse.birt.data.engine.olap.data.api.cube.IHierarchy;
import org.eclipse.birt.data.engine.olap.data.api.cube.ILevelDefn;
import org.eclipse.birt.data.engine.olap.data.document.DocumentManagerFactory;
import org.eclipse.birt.data.engine.olap.data.document.IDocumentManager;
import org.eclipse.birt.data.engine.olap.data.impl.NamingUtil;
import org.eclipse.birt.data.engine.olap.data.impl.Traversalor;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Dimension;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.DimensionFactory;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.DimensionForTest;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.LevelDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.facttable.DimensionDivider.CombinedPositionContructor;
import org.eclipse.birt.data.engine.olap.data.impl.facttable.FactTableAccessor.FTSUDocumentObjectNamingUtil;
import org.eclipse.birt.data.engine.olap.data.util.BufferedPrimitiveDiskArray;
import org.eclipse.birt.data.engine.olap.data.util.DataType;
import org.eclipse.birt.data.engine.olap.data.util.IDiskArray;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * 
 */

public class FactTableHelperTest {

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
    public void testFactTableDocumentObjectNameUtil( ) throws IOException,
			BirtException
	{
		int[] i1 = {
				12, 13, 14, 15
		};
		assertEquals( FTSUDocumentObjectNamingUtil.getDocumentObjectName( "", i1 ),
				"12X13X14X15" );
		int[] i2 = {
				12
		};
		assertEquals( FTSUDocumentObjectNamingUtil.getDocumentObjectName( "", i2 ),
		"12" );
		int[] i3 = {
				1
		};
		assertEquals( FTSUDocumentObjectNamingUtil.getDocumentObjectName( "", i3 ),
		"1" );
		int[] i4 = {
				1,12, 313, 55514, 4415
		};
		assertEquals( FTSUDocumentObjectNamingUtil.getDocumentObjectName( "", i4 ),
				"1X12X313X55514X4415" );
	}
	
	/**
	 * 
	 * @throws IOException
	 * @throws BirtException
	 */
	@Test
    public void testCombinedPositionCalculator( ) throws IOException,
			BirtException
	{
		DimensionDivision[] subDimensions;
		subDimensions = new DimensionDivision[3];
		subDimensions[0] = new DimensionDivision( 300, 3 );
		subDimensions[1] = new DimensionDivision( 3000, 3 );
		subDimensions[2] = new DimensionDivision( 30000, 3 );
		CombinedPositionContructor combinedPositionCalculator = new CombinedPositionContructor( subDimensions );
		int[] subdimensionNumber = new int[3];
		int[] dimensionPosition = new int[3];
		subdimensionNumber[0] = 0;
		subdimensionNumber[1] = 0;
		subdimensionNumber[2] = 1;
		
		dimensionPosition[0] = 64;
		dimensionPosition[1] = 512;
		dimensionPosition[2] = 10024;
		
		BigInteger bigInteger = combinedPositionCalculator.calculateCombinedPosition( subdimensionNumber,
				dimensionPosition ); 
		assertEquals( bigInteger.longValue( ), 64<<24|512<<14|24 );
		equal( combinedPositionCalculator.calculateDimensionPosition( subdimensionNumber,
				bigInteger.toByteArray( ) ), dimensionPosition );
	}
	
	/**
	 * 
	 * @throws IOException
	 * @throws BirtException
	 */
	@Test
    public void testCombinedPositionCalculator1( ) throws IOException,
			BirtException
	{
		DimensionDivision[] subDimensions;
		subDimensions = new DimensionDivision[3];
		subDimensions[0] = new DimensionDivision( 256 * 3, 3 );
		subDimensions[1] = new DimensionDivision( 256 * 3, 3 );
		subDimensions[2] = new DimensionDivision( 256 * 3, 3 );
		CombinedPositionContructor combinedPositionCalculator = new CombinedPositionContructor( subDimensions );
		int[] subdimensionNumber = new int[3];
		int[] dimensionPosition = new int[3];
		subdimensionNumber[0] = 0;
		subdimensionNumber[1] = 1;
		subdimensionNumber[2] = 2;
		
		dimensionPosition[0] = 255;
		dimensionPosition[1] = 254 + 256;
		dimensionPosition[2] = 128 + 512;
		
		BigInteger bigInteger = combinedPositionCalculator.calculateCombinedPosition( subdimensionNumber,
				dimensionPosition ); 
		equal( combinedPositionCalculator.calculateDimensionPosition( subdimensionNumber,
				bigInteger.toByteArray( ) ), dimensionPosition );
	}
	
	/**
	 * 
	 * @throws IOException
	 * @throws BirtException
	 */
	@Test
    public void testCombinedPositionCalculator2( ) throws IOException,
			BirtException
	{
		DimensionDivision[] subDimensions;
		subDimensions = new DimensionDivision[5];
		subDimensions[0] = new DimensionDivision( 300, 3 );
		subDimensions[1] = new DimensionDivision( 3000, 3 );
		subDimensions[2] = new DimensionDivision( 30000, 3 );
		subDimensions[3] = new DimensionDivision( 3000000, 3 );
		subDimensions[4] = new DimensionDivision( 3000000, 3 );
		CombinedPositionContructor combinedPositionCalculator = new CombinedPositionContructor( subDimensions );
		int[] subdimensionNumber = new int[5];
		int[] dimensionPosition = new int[5];
		subdimensionNumber[0] = 0;
		subdimensionNumber[1] = 0;
		subdimensionNumber[2] = 1;
		subdimensionNumber[3] = 1;
		subdimensionNumber[4] = 2;
		
		dimensionPosition[0] = 64;
		dimensionPosition[1] = 512;
		dimensionPosition[2] = 10024;
		dimensionPosition[3] = 1000024;
		dimensionPosition[4] = 2000024;
		
		BigInteger bigInteger = combinedPositionCalculator.calculateCombinedPosition( subdimensionNumber,
				dimensionPosition );
		long l = 64l<<44|512l<<34|24l<<20|24;
		BigInteger bigInteger1 = BigInteger.valueOf( l );
		bigInteger1 = bigInteger1.shiftLeft( 20 );
		bigInteger1 = bigInteger1.or( BigInteger.valueOf( 24 ) );
		assertEquals( bigInteger, bigInteger1 );
		equal( combinedPositionCalculator.calculateDimensionPosition( subdimensionNumber,
				bigInteger.toByteArray( ) ), dimensionPosition );
		
		dimensionPosition[0] = 64;
		dimensionPosition[1] = 512;
		dimensionPosition[2] = 1024 * 16 | 1024*8;
		dimensionPosition[3] = 1000324;
		dimensionPosition[4] = 2030024;
		bigInteger = combinedPositionCalculator.calculateCombinedPosition( subdimensionNumber,
				dimensionPosition );
		equal( combinedPositionCalculator.calculateDimensionPosition( subdimensionNumber,
				bigInteger.toByteArray( ) ), dimensionPosition );
	}
	
	private void equal( int[] i1, int[] i2)
	{
		assertEquals(i1.length, i2.length);
		for ( int i = 0; i < i1.length; i++ )
		{
			assertEquals(i1[i], i2[i]);	
		}
	}
	
	
	/**
	 * 
	 * @throws IOException
	 * @throws BirtException
	 */
	@Test
    public void testFactTableSaveAndLoad1( ) throws IOException, BirtException
	{
		IDocumentManager documentManager = DocumentManagerFactory.createFileDocumentManager( );
		testFactTableSaveAndLoad1( documentManager );
		documentManager.close( );
	}
	

	private void testFactTableSaveAndLoad1( IDocumentManager documentManager ) throws IOException, BirtException
	{
		Dimension[] dimensions = new Dimension[3];
		
		ILevelDefn[] levelDefs = new ILevelDefn[1];
		String[] levelNames = new String[1];
		levelNames[0] = "dimension1";
		DimensionForTest iterator = new DimensionForTest( levelNames );
		iterator.setLevelMember( 0, distinct( LevelsAndFactTableDataset.dimension1Col ) );
		levelDefs[0] = new LevelDefinition( "dimension1", new String[]{"dimension1"}, null );
		dimensions[0] = (Dimension) DimensionFactory.createDimension( "dimension1", documentManager, iterator, levelDefs, false, new StopSign() );
		IHierarchy hierarchy = dimensions[0].getHierarchy( );
		assertEquals( hierarchy.getName( ), "dimension1" );
		
		levelNames = new String[1];
		levelNames[0] = "dimension2";
		iterator = new DimensionForTest( levelNames );
		iterator.setLevelMember( 0, distinct( LevelsAndFactTableDataset.dimension2Col ) );
		levelDefs[0] = new LevelDefinition( "dimension2", new String[]{"dimension2"}, null );
		dimensions[1] = (Dimension) DimensionFactory.createDimension( "dimension2", documentManager, iterator, levelDefs, false, new StopSign() );
		hierarchy = dimensions[1].getHierarchy( );
		assertEquals( hierarchy.getName( ), "dimension2" );
		
		levelNames = new String[1];
		levelNames[0] = "dimension3";
		iterator = new DimensionForTest( levelNames );
		iterator.setLevelMember( 0, distinct( LevelsAndFactTableDataset.dimension3Col ) );
		levelDefs[0] = new LevelDefinition( "dimension3", new String[]{"dimension3"}, null );
		dimensions[2] = (Dimension) DimensionFactory.createDimension( "dimension3", documentManager, iterator, levelDefs, false, new StopSign() );
		hierarchy = dimensions[2].getHierarchy( );
		assertEquals( hierarchy.getName( ), "dimension3" );
		
		IDatasetIterator factTableIterator = new LevelsAndFactTableDataset( );
		String[] measureColumnName = new String[2];
		measureColumnName[0] = "measure1";
		measureColumnName[1] = "measure2";
		FactTableAccessor factTableConstructor = new FactTableAccessor( documentManager );
		FactTable factTable = factTableConstructor.saveFactTable( NamingUtil.getFactTableName( "threeDimensions" ),
				CubeUtility.getKeyColNames(dimensions),
				CubeUtility.getKeyColNames(dimensions),
				factTableIterator,
				dimensions,
				measureColumnName,
				new StopSign( ) );
		assertEquals(factTable.getSegmentCount( ), 1);
		factTable = factTableConstructor.load( NamingUtil.getFactTableName( "threeDimensions" ),
				new StopSign( ) );
		assertEquals(factTable.getSegmentCount( ), 1);
		assertEquals(factTable.getDimensionInfo( )[0].getDimensionName(), "dimension1" );
		assertEquals(factTable.getDimensionInfo( )[0].getDimensionLength(), 3 );
		assertEquals(factTable.getDimensionInfo( )[1].getDimensionName(), "dimension2" );
		assertEquals(factTable.getDimensionInfo( )[1].getDimensionLength(), 7 );
		assertEquals(factTable.getDimensionInfo( )[2].getDimensionName(), "dimension3" );
		assertEquals(factTable.getDimensionInfo( )[2].getDimensionLength(), 14 );
		assertEquals(factTable.getMeasureInfo( )[0].getMeasureName(), "measure1" );
		assertEquals( factTable.getMeasureInfo( )[0].getDataType(), DataType.INTEGER_TYPE );
		assertEquals(factTable.getMeasureInfo( )[1].getMeasureName(), "measure2" );
		assertEquals( factTable.getMeasureInfo( )[1].getDataType(), DataType.DOUBLE_TYPE );
		String[] dimensionNames = new String[1];
		dimensionNames[0] = "dimension2";
		IDiskArray[] dimensionPosition = new IDiskArray[1];
		dimensionPosition[0] = new BufferedPrimitiveDiskArray( );
		dimensionPosition[0].add( new Integer(1) );
		dimensionPosition[0].add( new Integer(2) );
		FactTableRowIterator facttableRowIterator = new FactTableRowIterator( factTable, dimensionNames, dimensionPosition, new StopSign() );
		assertTrue( facttableRowIterator != null );
		
		assertTrue( facttableRowIterator.next( ));
		assertEquals(0, facttableRowIterator.getDimensionPosition( 0 ));
		assertEquals(1, facttableRowIterator.getDimensionPosition( 1 ));
		assertEquals(3, facttableRowIterator.getDimensionPosition( 2 ));
		assertEquals(new Integer(121), facttableRowIterator.getMeasure( 0 ));
		assertEquals(new Double(121), facttableRowIterator.getMeasure( 1 ));
		
		assertTrue( facttableRowIterator.next( ));
		assertEquals(0, facttableRowIterator.getDimensionPosition( 0 ));
		assertEquals(1, facttableRowIterator.getDimensionPosition( 1 ));
		assertEquals(4, facttableRowIterator.getDimensionPosition( 2 ));
		assertEquals(new Integer(122), facttableRowIterator.getMeasure( 0 ));
		assertEquals(new Double(122), facttableRowIterator.getMeasure( 1 ));
		
		assertTrue( facttableRowIterator.next( ));
		assertEquals(1, facttableRowIterator.getDimensionPosition( 0 ));
		assertEquals(2, facttableRowIterator.getDimensionPosition( 1 ));
		assertEquals(5, facttableRowIterator.getDimensionPosition( 2 ));
		assertEquals(new Integer(211), facttableRowIterator.getMeasure( 0 ));
		assertEquals(new Double(211), facttableRowIterator.getMeasure( 1 ));
		
		assertTrue( facttableRowIterator.next( ));
		assertEquals(1, facttableRowIterator.getDimensionPosition( 0 ));
		assertEquals(2, facttableRowIterator.getDimensionPosition( 1 ));
		assertEquals(6, facttableRowIterator.getDimensionPosition( 2 ));
		assertEquals(new Integer(212), facttableRowIterator.getMeasure( 0 ));
		assertEquals(new Double(212), facttableRowIterator.getMeasure( 1 ));
		
		assertFalse( facttableRowIterator.next( ));
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
		
		ILevelDefn[] levelDefs = new ILevelDefn[1];
		String[] levelNames = new String[1];
		levelNames[0] = "dimension1";
		DimensionForTest iterator = new DimensionForTest( levelNames );
		iterator.setLevelMember( 0, distinct( LevelsAndFactTableDataset.dimension1Col ) );
		levelDefs[0] = new LevelDefinition( "dimension1", new String[]{"dimension1"}, null );
		dimensions[0] = (Dimension) DimensionFactory.createDimension( "dimension1", documentManager, iterator, levelDefs, false, new StopSign() );
		IHierarchy hierarchy = dimensions[0].getHierarchy( );
		assertEquals( hierarchy.getName( ), "dimension1" );
		
		levelNames = new String[1];
		levelNames[0] = "dimension2";
		iterator = new DimensionForTest( levelNames );
		iterator.setLevelMember( 0, distinct( LevelsAndFactTableDataset.dimension2Col ) );
		levelDefs[0] = new LevelDefinition( "dimension2", new String[]{"dimension2"}, null );
		dimensions[1] = (Dimension) DimensionFactory.createDimension( "dimension2", documentManager, iterator, levelDefs, false, new StopSign() );
		hierarchy = dimensions[1].getHierarchy( );
		assertEquals( hierarchy.getName( ), "dimension2" );
		
		levelNames = new String[1];
		levelNames[0] = "dimension3";
		iterator = new DimensionForTest( levelNames );
		iterator.setLevelMember( 0, distinct( LevelsAndFactTableDataset.dimension3Col ) );
		levelDefs[0] = new LevelDefinition( "dimension3", new String[]{"dimension3"}, null );
		dimensions[2] = (Dimension) DimensionFactory.createDimension( "dimension3", documentManager, iterator, levelDefs, false, new StopSign() );
		hierarchy = dimensions[2].getHierarchy( );
		assertEquals( hierarchy.getName( ), "dimension3" );
		
		IDatasetIterator factTableIterator = new LevelsAndFactTableDataset( );
		String[] measureColumnName = new String[2];
		measureColumnName[0] = "measure1";
		measureColumnName[1] = "measure2";
		FactTableAccessor factTableConstructor = new FactTableAccessor( documentManager );
		FactTable factTable = factTableConstructor.saveFactTable( NamingUtil.getFactTableName( "threeDimensions" ),
				CubeUtility.getKeyColNames(dimensions),
				CubeUtility.getKeyColNames(dimensions),
				factTableIterator,
				dimensions,
				measureColumnName,
				new StopSign( ) );
		assertEquals( factTable.getSegmentCount( ), 1 );
		factTable = factTableConstructor.load(
				NamingUtil.getFactTableName( "threeDimensions" ),
				new StopSign());
		assertEquals(factTable.getSegmentCount( ), 1);
		assertEquals(factTable.getDimensionInfo( )[0].getDimensionName(), "dimension1" );
		assertEquals(factTable.getDimensionInfo( )[0].getDimensionLength(), 3 );
		assertEquals(factTable.getDimensionInfo( )[1].getDimensionName(), "dimension2" );
		assertEquals(factTable.getDimensionInfo( )[1].getDimensionLength(), 7 );
		assertEquals(factTable.getDimensionInfo( )[2].getDimensionName(), "dimension3" );
		assertEquals(factTable.getDimensionInfo( )[2].getDimensionLength(), 14 );
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
		dimensionPosition[0].add( new Integer(1) );
		dimensionPosition[0].add( new Integer(2) );
		dimensionPosition[1] = new BufferedPrimitiveDiskArray( );
		dimensionPosition[1].add( new Integer(1) );
		dimensionPosition[1].add( new Integer(2) );
		dimensionPosition[2] = new BufferedPrimitiveDiskArray( );
		dimensionPosition[2].add( new Integer(1) );
		dimensionPosition[2].add( new Integer(2) );
		dimensionPosition[2].add( new Integer(3) );
		dimensionPosition[2].add( new Integer(4) );
		dimensionPosition[2].add( new Integer(5) );
		dimensionPosition[2].add( new Integer(6) );
		FactTableRowIterator facttableRowIterator = new FactTableRowIterator( factTable, dimensionNames, dimensionPosition, new StopSign() );
		assertTrue( facttableRowIterator != null );
		
		assertTrue( facttableRowIterator.next( ));
		assertEquals(1, facttableRowIterator.getDimensionPosition( 0 ));
		assertEquals(2, facttableRowIterator.getDimensionPosition( 1 ));
		assertEquals(5, facttableRowIterator.getDimensionPosition( 2 ));
		assertEquals(new Integer(211), facttableRowIterator.getMeasure( 0 ));
		assertEquals(new Double(211), facttableRowIterator.getMeasure( 1 ));
		
		assertTrue( facttableRowIterator.next( ));
		assertEquals(1, facttableRowIterator.getDimensionPosition( 0 ));
		assertEquals(2, facttableRowIterator.getDimensionPosition( 1 ));
		assertEquals(6, facttableRowIterator.getDimensionPosition( 2 ));
		assertEquals(new Integer(212), facttableRowIterator.getMeasure( 0 ));
		assertEquals(new Double(212), facttableRowIterator.getMeasure( 1 ));
		
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
	

	private void testFactTableSaveAndLoad3( IDocumentManager documentManager ) throws IOException, BirtException
	{
		long startTime = System.currentTimeMillis( ); 
		Dimension[] dimensions = new Dimension[3];
		
		String[] levelNames = new String[1];
		levelNames[0] = "dimension1";
		DimensionForTest iterator = new DimensionForTest( levelNames );
		int[] data = new int[BigLevelsAndFactTableDataset.dimensionPositionLength[0]];
		for ( int i = 0; i < data.length; i++ )
		{
			data[i] = i;
		}
		iterator.setLevelMember( 0, data );
		ILevelDefn[] levelDefs = new ILevelDefn[1];
		levelDefs[0] = new LevelDefinition( "dimension1", new String[]{"dimension1"}, null );
		dimensions[0] = (Dimension) DimensionFactory.createDimension( "dimension1", documentManager, iterator, levelDefs, false, new StopSign() );
		IHierarchy hierarchy = dimensions[0].getHierarchy( );
		assertEquals( hierarchy.getName( ), "dimension1" );
		assertEquals( dimensions[0].length( ),
				BigLevelsAndFactTableDataset.dimensionPositionLength[0] );
		
		levelNames = new String[1];
		levelNames[0] = "dimension2";;
		iterator = new DimensionForTest( levelNames );
		data = new int[BigLevelsAndFactTableDataset.dimensionPositionLength[1]];
		for ( int i = 0; i < data.length; i++ )
		{
			data[i] = i;
		}
		iterator.setLevelMember( 0, data );
		levelDefs[0] = new LevelDefinition( "dimension2", new String[]{"dimension2"}, null );
		dimensions[1] = (Dimension) DimensionFactory.createDimension( "dimension2", documentManager, iterator, levelDefs, false, new StopSign() );
		hierarchy = dimensions[1].getHierarchy( );
		assertEquals( hierarchy.getName( ), "dimension2" );
		assertEquals( dimensions[1].length( ),
				BigLevelsAndFactTableDataset.dimensionPositionLength[1] );
		
		levelNames = new String[1];
		levelNames[0] = "dimension3";;
		iterator = new DimensionForTest( levelNames );
		data = new int[BigLevelsAndFactTableDataset.dimensionPositionLength[2]];
		for ( int i = 0; i < data.length; i++ )
		{
			data[i] = i;
		}
		iterator.setLevelMember( 0, data );
		levelDefs[0] = new LevelDefinition( "dimension3", new String[]{"dimension3"}, null );
		dimensions[2] = (Dimension) DimensionFactory.createDimension( "dimension3", documentManager, iterator, levelDefs, false, new StopSign() );
		hierarchy = dimensions[2].getHierarchy( );
		assertEquals( hierarchy.getName( ), "dimension3" );
		assertEquals( dimensions[2].length( ),
				BigLevelsAndFactTableDataset.dimensionPositionLength[2] );
		
		IDatasetIterator facttableIterator = new BigLevelsAndFactTableDataset( );
		String[] measureColumnName = new String[2];
		measureColumnName[0] = "measure1";
		measureColumnName[1] = "measure2";
		System.out.println( "Finish creating dimension... time: "  + (System.currentTimeMillis( ) - startTime)/1000);
		startTime = System.currentTimeMillis( );
		System.out.println( "start save fact table..." );
		FactTableAccessor factTableConstructor = new FactTableAccessor( documentManager );
		FactTable factTable = factTableConstructor.saveFactTable( NamingUtil.getFactTableName( "bigThreeDimensions" ),
				CubeUtility.getKeyColNames(dimensions),
				CubeUtility.getKeyColNames(dimensions),
				facttableIterator,
				dimensions,
				measureColumnName,
				new StopSign( ) );
		// assertEquals(factTable.getSegmentNumber( ), 1);
		System.out.println( "Save fact table, finished... time: "
				+ ( System.currentTimeMillis( ) - startTime ) / 1000 );
		factTable = factTableConstructor.load( NamingUtil.getFactTableName( "bigThreeDimensions" ),
				new StopSign( ) );
		
		System.out.println( "start iterator..." );
		startTime = System.currentTimeMillis( );
//		assertEquals(factTable.getSegmentNumber( ), 1);
		assertEquals(factTable.getDimensionInfo( )[0].getDimensionName(), "dimension1" );
		assertEquals( factTable.getDimensionInfo( )[0].getDimensionLength(),
				BigLevelsAndFactTableDataset.dimensionPositionLength[0] );
		assertEquals(factTable.getDimensionInfo( )[1].getDimensionName(), "dimension2" );
		assertEquals(factTable.getDimensionInfo( )[1].getDimensionLength(),
				BigLevelsAndFactTableDataset.dimensionPositionLength[1] );
		assertEquals(factTable.getDimensionInfo( )[2].getDimensionName(), "dimension3" );
		assertEquals(factTable.getDimensionInfo( )[2].getDimensionLength(),
				BigLevelsAndFactTableDataset.dimensionPositionLength[2] );
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
		dimensionPosition[0].add( new Integer(99) );
		dimensionPosition[1] = new BufferedPrimitiveDiskArray( );
		dimensionPosition[1].add( new Integer(10) );
		dimensionPosition[1].add( new Integer(99) );
		dimensionPosition[2] = new BufferedPrimitiveDiskArray( );
		dimensionPosition[2].add( new Integer(1) );
		dimensionPosition[2].add( new Integer(9) );
		FactTableRowIterator facttableRowIterator = new FactTableRowIterator( factTable, dimensionNames, dimensionPosition, new StopSign() );
		assertTrue( facttableRowIterator != null );
		
		while( facttableRowIterator.next( ))
		{
			System.out.print( facttableRowIterator.getDimensionPosition( 0 )
					+ "," + facttableRowIterator.getDimensionPosition( 1 )
					+ "," + facttableRowIterator.getDimensionPosition( 2 )+"  " );
			System.out.println( facttableRowIterator.getMeasure( 0 )
					+ "," + facttableRowIterator.getMeasure( 1 ) );
		}

		System.out.println( "Finish iterator... time: "  + (System.currentTimeMillis( ) - startTime)/1000);
	}
	
	private static String[] distinct( String[] sValues )
	{
		Arrays.sort( sValues );
		List tempList = new ArrayList( );
		tempList.add( sValues[0] );
		for ( int i = 1; i < sValues.length; i++ )
		{
			if ( !sValues[i].equals( sValues[i - 1] ) )
			{
				tempList.add( sValues[i] );
			}
		}
		String[] result = new String[tempList.size( )];
		for ( int i = 0; i < result.length; i++ )
		{
			result[i] = (String)tempList.get( i );
		}
		return result;
	}
}

class LevelsAndFactTableDataset implements IDatasetIterator
{
	int ptr = -1;
	
	static String[] dimension1Col = {
			"1",
			"1",
			"1",
			"1",
			"1",
			"2",
			"2",
			"2",
			"2",
			"2",
			"2",
			"2",
			"3",
			"3"
	};
	static String[] dimension2Col = {
			"11",
			"11",
			"11",
			"12",
			"12",
			"21",
			"21",
			"22",
			"22",
			"22",
			"23",
			"23",
			"31",
			"32"
	};
	static String[] dimension3Col = {
		"111",
		"112",
		"113",
		"121",
		"122",
		"211",
		"212",
		"221",
		"222",
		"223",
		"231",
		"232",
		"311",
		"321"
	};

	static int[] Measure1 = {
		111,
		112,
		113,
		121,
		122,
		211,
		212,
		221,
		222,
		223,
		231,
		232,
		311,
		321
	};
	
	static double[] Measure2 = {
		111,
		112,
		113,
		121,
		122,
		211,
		212,
		221,
		222,
		223,
		231,
		232,
		311,
		321
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
		if(name.equals( "dimension1" ))
		{
			return 0;
		}
		else if(name.equals( "dimension2" ))
		{
			return 1;
		}
		else if(name.equals( "dimension3" ))
		{
			return 2;
		}
		else if(name.equals( "measure1" ))
		{
			return 3;
		}else if(name.equals( "measure2" ))
		{
			return 4;
		}
		return -1;
	}

	public int getFieldType( String name ) throws BirtException
	{
		if(name.equals( "dimension1" ))
		{
			return DataType.STRING_TYPE;
		}
		else if(name.equals( "dimension2" ))
		{
			return DataType.STRING_TYPE;
		}
		else if(name.equals( "dimension3" ))
		{
			return DataType.STRING_TYPE;
		}
		else if(name.equals( "measure1" ))
		{
			return DataType.INTEGER_TYPE;
		}
		else if(name.equals( "measure2" ))
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
		if(fieldIndex==0)
		{
			return dimension1Col[ptr];
		}
		else if(fieldIndex==1)
		{
			return dimension2Col[ptr];
		}
		else if(fieldIndex==2)
		{
			return dimension3Col[ptr];
		}
		else if(fieldIndex==3)
		{
			return new Integer(Measure1[ptr]);
		}
		else if(fieldIndex==4)
		{
			return new Double(Measure2[ptr]);
		}
		return null;
	}
	
	public void first( ) throws BirtException
	{
		ptr = -1;
	}
	
	public boolean next( ) throws BirtException
	{
		ptr ++;
		if ( ptr >= dimension1Col.length )
		{
			return false;
		}
		return true;
	}
}

class BigLevelsAndFactTableDataset implements IDatasetIterator
{
	static int[] dimensionPositionLength = {100,100,10};
	Traversalor dimTraversalor = new Traversalor( dimensionPositionLength );
	int[] dimensionPosition = null;
	
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
		if(name.equals( "dimension1" ))
		{
			return 0;
		}
		else if(name.equals( "dimension2" ))
		{
			return 1;
		}
		else if(name.equals( "dimension3" ))
		{
			return 2;
		}
		else if(name.equals( "measure1" ))
		{
			return 3;
		}else if(name.equals( "measure2" ))
		{
			return 4;
		}
		return -1;
	}

	public int getFieldType( String name ) throws BirtException
	{
		if(name.equals( "dimension1" ))
		{
			return DataType.INTEGER_TYPE;
		}
		else if(name.equals( "dimension2" ))
		{
			return DataType.INTEGER_TYPE;
		}
		else if(name.equals( "dimension3" ))
		{
			return DataType.INTEGER_TYPE;
		}
		else if(name.equals( "measure1" ))
		{
			return DataType.INTEGER_TYPE;
		}
		else if(name.equals( "measure2" ))
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
		if(fieldIndex==0)
		{
			return new Integer(dimensionPosition[0]);
		}
		else if(fieldIndex==1)
		{
			return new Integer(dimensionPosition[1]);
		}
		else if(fieldIndex==2)
		{
			return new Integer(dimensionPosition[2]);
		}
		else if(fieldIndex==3)
		{
			return new Integer(dimensionPosition[0]*dimensionPosition[1]*dimensionPosition[2]);
		}
		else if(fieldIndex==4)
		{
			return new Double(dimensionPosition[0]*dimensionPosition[1]*dimensionPosition[2]);
		}
		return null;
	}
	
	public void first( ) throws BirtException
	{
		dimTraversalor = new Traversalor( dimensionPositionLength );
	}
	
	public boolean next( ) throws BirtException
	{
		if( !dimTraversalor.next( ) )
		{
			return false;
		}
		dimensionPosition = dimTraversalor.getIntArray( );
		return true;
	}
}


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
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Dimension;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.DimensionFactory;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.DimensionForTest;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.LevelDefinition;
import org.eclipse.birt.data.engine.olap.data.util.BufferedPrimitiveDiskArray;
import org.eclipse.birt.data.engine.olap.data.util.DataType;
import org.eclipse.birt.data.engine.olap.data.util.IDiskArray;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * 
 */

public class FactTableRowIteratorWithFilterTest {

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
/*
	 * @see TestCase#tearDown()
	 */
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
		IFactTableRowIterator facttableRowIterator = new FactTableRowIterator( factTable, dimensionNames, dimensionPosition, new StopSign() );
		facttableRowIterator = new FactTableRowIteratorWithFilter( dimensions, facttableRowIterator, new StopSign() );
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
		facttableRowIterator.close( );
	}
}

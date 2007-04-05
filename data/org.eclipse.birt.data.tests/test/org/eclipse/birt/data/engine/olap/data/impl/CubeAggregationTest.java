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

package org.eclipse.birt.data.engine.olap.data.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.core.archive.compound.ArchiveFile;
import org.eclipse.birt.core.archive.compound.ArchiveReader;
import org.eclipse.birt.core.archive.compound.ArchiveWriter;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.aggregation.BuiltInAggregationFactory;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.api.cube.CubeMaterializer;
import org.eclipse.birt.data.engine.olap.api.cube.IDatasetIterator;
import org.eclipse.birt.data.engine.olap.api.cube.IHierarchy;
import org.eclipse.birt.data.engine.olap.api.cube.ILevelDefn;
import org.eclipse.birt.data.engine.olap.api.cube.StopSign;
import org.eclipse.birt.data.engine.olap.data.api.CubeQueryExecutorHelper;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;
import org.eclipse.birt.data.engine.olap.data.api.IDimensionSortDefn;
import org.eclipse.birt.data.engine.olap.data.api.ISelection;
import org.eclipse.birt.data.engine.olap.data.document.DocumentManagerFactory;
import org.eclipse.birt.data.engine.olap.data.document.IDocumentManager;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Dimension;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.DimensionFactory;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.DimensionForTest;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.LevelDefinition;
import org.eclipse.birt.data.engine.olap.data.util.DataType;

/**
 * 
 */

public class CubeAggregationTest extends TestCase
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp( ) throws Exception
	{
		super.setUp( );
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown( ) throws Exception
	{
		super.tearDown( );
	}
	
	private static String[] distinct( String[] iValues )
	{
		Arrays.sort( iValues );
		List tempList = new ArrayList( );
		tempList.add( iValues[0] );
		for ( int i = 1; i < iValues.length; i++ )
		{
			if ( !iValues[i].equals( iValues[i - 1] ) )
			{
				tempList.add( iValues[i] );
			}
		}
		String[] result = new String[tempList.size( )];
		for ( int i = 0; i < result.length; i++ )
		{
			result[i] = ((String)tempList.get( i ));
		}
		return result;
	}
	
	
	/**
	 * 
	 * @throws IOException
	 * @throws BirtException
	 */
	public void testRAAggregation1( ) throws IOException, BirtException
	{
		CubeMaterializer materializer = new CubeMaterializer();
		IDocumentManager documentManager = materializer.getDocumentManager( );
		testCubeCreate1( documentManager );
		testCubeAggregation1( documentManager );
		IDocArchiveWriter writer = createRAWriter( );
		materializer.saveCubeToReportDocument( "cube", writer , new StopSign( ) );
		writer.flush( );
		writer.finish( );
		testCubeAggregation1( createRADocumentManager( ) );
	}

	private static IDocumentManager createRADocumentManager( ) throws IOException, DataException
	{
		String pathName = System.getProperty( "java.io.tmpdir" ) + File.separator+ "docForTest";
		ArchiveFile archiveFile = new ArchiveFile( pathName, "rw+" );
		ArchiveReader reader = new ArchiveReader( archiveFile );
		IDocumentManager documentManager = DocumentManagerFactory.createRADocumentManager( reader );
		return documentManager;
	}
	
	private static IDocArchiveWriter createRAWriter( ) throws IOException
	{
		String pathName = System.getProperty( "java.io.tmpdir" ) + File.separator+ "docForTest";
		ArchiveFile archiveFile = new ArchiveFile( pathName, "rw+" );
		ArchiveWriter writer = new ArchiveWriter( archiveFile );
		return writer;
	}
	
	public void testAggregation1( ) throws IOException, BirtException
	{
		IDocumentManager documentManager = DocumentManagerFactory.createFileDocumentManager( );
		testCubeCreate1( documentManager );
		testCubeAggregation1( documentManager );
	}

	private void testCubeCreate1( IDocumentManager documentManager ) throws IOException, BirtException, DataException
	{
		Dimension[] dimensions = new Dimension[3];
		
		// dimension0
		String[] colNames = new String[3];
		colNames[0] = "col11";
		colNames[1] = "col12";
		colNames[2] = "col13";
		DimensionForTest iterator = new DimensionForTest( colNames );
		iterator.setLevelMember( 0, TestFactTable.L1Col );
		iterator.setLevelMember( 1, TestFactTable.L2Col );
		iterator.setLevelMember( 2, TestFactTable.L3Col );
		
		ILevelDefn[] levelDefs = new ILevelDefn[3];
		levelDefs[0] = new LevelDefinition( "level11", new String[]{"col11"}, null );
		levelDefs[1] = new LevelDefinition( "level12", new String[]{"col12"}, null );
		levelDefs[2] = new LevelDefinition( "level13", new String[]{"col13"}, null );
		dimensions[0] = (Dimension) DimensionFactory.createDimension( "dimension1", documentManager, iterator, levelDefs, false );
		IHierarchy hierarchy = dimensions[0].getHierarchy( );
		assertEquals( hierarchy.getName( ), "dimension1" );
		assertEquals( dimensions[0].length( ), TestFactTable.L1Col.length );
		
		//dimension1
		colNames = new String[1];
		colNames[0] = "col21";
		iterator = new DimensionForTest( colNames );
		iterator.setLevelMember( 0, distinct( TestFactTable.L1Col ) );
		
		levelDefs = new ILevelDefn[1];
		levelDefs[0] = new LevelDefinition( "level21", new String[]{"col21"}, null );
		dimensions[1] = (Dimension) DimensionFactory.createDimension( "dimension2", documentManager, iterator, levelDefs, false );
		hierarchy = dimensions[1].getHierarchy( );
		assertEquals( hierarchy.getName( ), "dimension2" );
		assertEquals( dimensions[1].length( ), 3 );
		
		// dimension2
		colNames = new String[1];
		colNames[0] = "col31";
		
		iterator = new DimensionForTest( colNames );
		iterator.setLevelMember( 0, TestFactTable.L3Col );

		levelDefs = new ILevelDefn[1];
		levelDefs[0] = new LevelDefinition( "level31", new String[]{"col31"}, null );
		dimensions[2] = (Dimension) DimensionFactory.createDimension( "dimension3",
				documentManager,
				iterator,
				levelDefs,
				false );
		
		hierarchy = dimensions[2].getHierarchy( );
		assertEquals( hierarchy.getName( ), "dimension3" );
		assertEquals( dimensions[2].length( ), 12 );
		
		TestFactTable factTable2 = new TestFactTable();
		String[] measureColumnName = new String[2];
		measureColumnName[0] = "measure1";
		measureColumnName[1] = "measure2";
		Cube cube = new Cube( "cube", documentManager );
		
		cube.create( dimensions, factTable2, measureColumnName, new StopSign( ) );
		
		documentManager.flush( );
	}

	private void testCubeAggregation1( IDocumentManager documentManager ) throws IOException, DataException, BirtException
	{
		//query
		CubeQueryExecutorHelper cubeQueryExcutorHelper = new CubeQueryExecutorHelper( 
				CubeQueryExecutorHelper.loadCube( "cube", documentManager, new StopSign( ) ) );
		ISelection[][] filter = new ISelection[1][1];
		filter[0][0] = SelectionFactory.createRangeSelection(  new Object[]{"1"},
				 new Object[]{"3"},
				true,
				false );
		cubeQueryExcutorHelper.addFilter( "level21", filter[0] );
		
		
		AggregationDefinition[] aggregations = new AggregationDefinition[4];
		int[] sortType = new int[1];
		sortType[0] = IDimensionSortDefn.SORT_ASC;
		String[] levelNamesForFilter = new String[1];
		levelNamesForFilter[0] = "level21";
		AggregationFunctionDefinition[] funcitons = new AggregationFunctionDefinition[1];
		funcitons[0] = new AggregationFunctionDefinition( "measure1", BuiltInAggregationFactory.TOTAL_SUM_FUNC );
		aggregations[0] = new AggregationDefinition( levelNamesForFilter, sortType, funcitons );
		sortType = new int[2];
		sortType[0] = IDimensionSortDefn.SORT_ASC;
		sortType[1] = IDimensionSortDefn.SORT_ASC;
		levelNamesForFilter = new String[1];
		levelNamesForFilter[0] = "level31";
		aggregations[1] = new AggregationDefinition( levelNamesForFilter, sortType, funcitons );
		
		aggregations[2] = new AggregationDefinition( null, null, funcitons );
		
		aggregations[3] = new AggregationDefinition( levelNamesForFilter, sortType, null );
		
		IAggregationResultSet[] resultSet = cubeQueryExcutorHelper.execute( aggregations,
				new StopSign( ) );
		//result set for aggregation 0
		assertEquals( resultSet[0].length( ), 2 );
		assertEquals( resultSet[0].getAggregationDataType( 0 ), DataType.DOUBLE_TYPE );
		assertEquals( resultSet[0].getLevelIndex( "level21" ), 0 );
		assertEquals( resultSet[0].getLevelKeyDataType( "level21", "col21" ), DataType.STRING_TYPE );
		resultSet[0].seek( 0 );
		assertEquals( resultSet[0].getLevelKeyValue( 0 )[0], "1" );
		assertEquals( resultSet[0].getAggregationValue( 0 ), new Double(6) );
		resultSet[0].seek( 1 );
		assertEquals( resultSet[0].getLevelKeyValue( 0 )[0], "2" );
		assertEquals( resultSet[0].getAggregationValue( 0 ), new Double(22) );
		//result set for aggregation 1
		assertEquals( resultSet[1].length( ), 8 );
		assertEquals( resultSet[1].getAggregationDataType( 0 ), DataType.DOUBLE_TYPE );
		assertEquals( resultSet[1].getLevelIndex( "level31" ), 0 );
		assertEquals( resultSet[1].getLevelKeyDataType( "level31", "col31" ), DataType.INTEGER_TYPE );
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
		//result set for aggregation 2
		assertEquals( resultSet[2].length( ), 1 );
		assertEquals( resultSet[2].getAggregationDataType( 0 ), DataType.DOUBLE_TYPE );
		assertEquals( resultSet[2].getLevelIndex( "level31" ), -1 );
		resultSet[1].seek( 0 );
		assertEquals( resultSet[2].getLevelKeyValue( 0 ), null );
		assertEquals( resultSet[2].getAggregationValue( 0 ), new Double(28) );
		
		//result set for aggregation 3
		assertEquals( resultSet[3].length( ), 8 );
		assertEquals( resultSet[3].getAggregationDataType( 0 ), DataType.UNKNOWN_TYPE );
		assertEquals( resultSet[3].getLevelIndex( "level31" ), 0 );
		assertEquals( resultSet[3].getLevelKeyDataType( "level31", "col31" ), DataType.INTEGER_TYPE );
		resultSet[3].seek( 0 );
		assertEquals( resultSet[3].getLevelKeyValue( 0 )[0], new Integer(1) );
		assertEquals( resultSet[3].getAggregationValue( 0 ), null );
		resultSet[3].seek( 1 );
		assertEquals( resultSet[3].getLevelKeyValue( 0 )[0], new Integer(2) );
		resultSet[3].seek( 2 );
		assertEquals( resultSet[3].getLevelKeyValue( 0 )[0], new Integer(3) );
		assertEquals( resultSet[3].getAggregationValue( 0 ), null );
		resultSet[3].seek( 3 );
		assertEquals( resultSet[3].getLevelKeyValue( 0 )[0], new Integer(4) );
		resultSet[3].seek( 4 );
		assertEquals( resultSet[3].getLevelKeyValue( 0 )[0], new Integer(5) );
		assertEquals( resultSet[3].getAggregationValue( 0 ), null );
		resultSet[3].seek( 5 );
		assertEquals( resultSet[3].getLevelKeyValue( 0 )[0], new Integer(6) );
		resultSet[3].seek( 6 );
		assertEquals( resultSet[3].getLevelKeyValue( 0 )[0], new Integer(7) );
		resultSet[3].seek( 7 );
		assertEquals( resultSet[3].getLevelKeyValue( 0 )[0], new Integer(8) );
	}
	
	/**
	 * 
	 * @throws IOException
	 * @throws BirtException
	 */
	public void testAggregation2( ) throws IOException, BirtException
	{
		IDocumentManager documentManager = DocumentManagerFactory.createFileDocumentManager( );
		
		testCubeCreate2( documentManager );
	}
	
	private void testCubeCreate2( IDocumentManager documentManager ) throws IOException, BirtException
	{
		Dimension[] dimensions = new Dimension[2];
		
		// dimension0
		String[] ColNames = new String[3];
		ColNames[0] = "col11";
		ColNames[1] = "col12";
		ColNames[2] = "col13";
		DimensionForTest iterator = new DimensionForTest( ColNames );
		iterator.setLevelMember( 0, TestFactTable.L1Col );
		iterator.setLevelMember( 1, TestFactTable.L2Col );
		iterator.setLevelMember( 2, TestFactTable.L3Col );
		
		ILevelDefn[] levelDefs = new ILevelDefn[3];
		levelDefs[0] = new LevelDefinition( "level11", new String[]{"col11"}, null );
		levelDefs[1] = new LevelDefinition( "level12", new String[]{"col12"}, null );
		levelDefs[2] = new LevelDefinition( "level13", new String[]{"col13"}, null );
		dimensions[0] = (Dimension) DimensionFactory.createDimension( "dimension1", documentManager, iterator, levelDefs, false );
		IHierarchy hierarchy = dimensions[0].getHierarchy( );
		assertEquals( hierarchy.getName( ), "dimension1" );
		assertEquals( dimensions[0].length( ), TestFactTable.L1Col.length );
		
		//dimension1
		ColNames = new String[1];
		ColNames[0] = "col21";
		iterator = new DimensionForTest( ColNames );
		iterator.setLevelMember( 0, distinct( TestFactTable.L1Col ) );
		
		levelDefs = new ILevelDefn[1];
		levelDefs[0] = new LevelDefinition( "level21", new String[]{"col21"}, null );
		dimensions[1] = (Dimension) DimensionFactory.createDimension( "dimension2", documentManager, iterator, levelDefs, false );
		hierarchy = dimensions[1].getHierarchy( );
		assertEquals( hierarchy.getName( ), "dimension2" );
		assertEquals( dimensions[1].length( ), 3 );
		
		TestFactTable factTable2 = new TestFactTable();
		String[] measureColumnName = new String[2];
		measureColumnName[0] = "measure1";
		measureColumnName[1] = "measure2";
		Cube cube = new Cube( "cube", documentManager );
		
		cube.create( dimensions, factTable2, measureColumnName, new StopSign( ) );
		CubeQueryExecutorHelper cubeQueryExcutorHelper = new CubeQueryExecutorHelper( cube );
		ISelection[][] filter = new ISelection[1][1];
		filter[0][0] = SelectionFactory.createRangeSelection(  new Object[]{"1"},
				 new Object[]{"3"},
				true,
				false );
		cubeQueryExcutorHelper.addFilter( "level21", filter[0] );
		
		AggregationDefinition[] aggregations = new AggregationDefinition[4];
		int[] sortType = new int[1];
		sortType[0] = IDimensionSortDefn.SORT_ASC;
		String[] levelNamesForFilter = new String[1];
		levelNamesForFilter[0] = "level21";
		AggregationFunctionDefinition[] funcitons = new AggregationFunctionDefinition[1];
		funcitons[0] = new AggregationFunctionDefinition( "measure1", BuiltInAggregationFactory.TOTAL_SUM_FUNC );
		aggregations[0] = new AggregationDefinition( levelNamesForFilter, sortType, funcitons );
		
		sortType = new int[2];
		sortType[0] = IDimensionSortDefn.SORT_ASC;
		sortType[1] = IDimensionSortDefn.SORT_ASC;
		levelNamesForFilter = new String[2];
		levelNamesForFilter[0] = "level11";
		levelNamesForFilter[1] = "level12";
		funcitons = new AggregationFunctionDefinition[1];
		funcitons[0] = new AggregationFunctionDefinition( "measure1", BuiltInAggregationFactory.TOTAL_SUM_FUNC );
		aggregations[1] = new AggregationDefinition( levelNamesForFilter, sortType, funcitons );
		
		sortType = new int[1];
		sortType[0] = IDimensionSortDefn.SORT_ASC;
		levelNamesForFilter = new String[1];
		levelNamesForFilter[0] = "level21";
		funcitons = new AggregationFunctionDefinition[1];
		funcitons[0] = new AggregationFunctionDefinition( "measure1", BuiltInAggregationFactory.TOTAL_SUM_FUNC );
		aggregations[2] = new AggregationDefinition( levelNamesForFilter, sortType, funcitons );
		
		sortType = new int[1];
		sortType[0] = IDimensionSortDefn.SORT_ASC;
		levelNamesForFilter = new String[1];
		levelNamesForFilter[0] = "level11";
		funcitons = new AggregationFunctionDefinition[1];
		funcitons[0] = new AggregationFunctionDefinition( "measure1", BuiltInAggregationFactory.TOTAL_SUM_FUNC );
		aggregations[3] = new AggregationDefinition( levelNamesForFilter, sortType, funcitons );
		
		IAggregationResultSet[] resultSet = cubeQueryExcutorHelper.execute( aggregations,
				new StopSign( ) );
		//result set for aggregation 0
		assertEquals( resultSet[0].length( ), 2 );
		
		assertEquals( resultSet[0].getAggregationDataType( 0 ), DataType.DOUBLE_TYPE );
		assertEquals( resultSet[0].getLevelIndex( "level21" ), 0 );
		assertEquals( resultSet[0].getLevelKeyDataType( "level21", "col21" ), DataType.STRING_TYPE );
		resultSet[0].seek( 0 );
		assertEquals( resultSet[0].getLevelKeyValue( 0 )[0], "1" );
		assertEquals( resultSet[0].getAggregationValue( 0 ), new Double(6) );
		resultSet[0].seek( 1 );
		assertEquals( resultSet[0].getLevelKeyValue( 0 )[0], "2" );
		assertEquals( resultSet[0].getAggregationValue( 0 ), new Double(22) );
		
		//result set for aggregation 1
		assertEquals( resultSet[1].length( ), 4 );
		
		assertEquals( resultSet[1].getAggregationDataType( 0 ), DataType.DOUBLE_TYPE );
		assertEquals( resultSet[1].getLevelIndex( "level21" ), -1 );
		assertEquals( resultSet[1].getLevelIndex( "level11" ), 0 );
		assertEquals( resultSet[1].getLevelIndex( "level12" ), 1 );
		assertEquals( resultSet[1].getLevelKeyDataType( "level11", "col11" ), DataType.STRING_TYPE );
		assertEquals( resultSet[1].getLevelKeyDataType( "level12", "col12" ), DataType.INTEGER_TYPE );
		resultSet[1].seek( 0 );
		assertEquals( resultSet[1].getLevelKeyValue( 0 )[0], "1" );
		assertEquals( resultSet[1].getLevelKeyValue( 1 )[0], new Integer(1) );
		assertEquals( resultSet[1].getAggregationValue( 0 ), new Double(1) );
		resultSet[1].seek( 1 );
		assertEquals( resultSet[1].getLevelKeyValue( 0 )[0], "1" );
		assertEquals( resultSet[1].getLevelKeyValue( 1 )[0], new Integer(2) );
		assertEquals( resultSet[1].getAggregationValue( 0 ), new Double(5) );
		resultSet[1].seek( 2 );
		assertEquals( resultSet[1].getLevelKeyValue( 0 )[0], "2" );
		assertEquals( resultSet[1].getLevelKeyValue( 1 )[0], new Integer(1) );
		assertEquals( resultSet[1].getAggregationValue( 0 ), new Double(9) );
		resultSet[1].seek( 3 );
		assertEquals( resultSet[1].getLevelKeyValue( 0 )[0], "2" );
		assertEquals( resultSet[1].getLevelKeyValue( 1 )[0], new Integer(2) );
		assertEquals( resultSet[1].getAggregationValue( 0 ), new Double(13) );
		
		//result set for aggregation 2
		assertEquals( resultSet[2].length( ), 2 );
		
		assertEquals( resultSet[2].getAggregationDataType( 0 ), DataType.DOUBLE_TYPE );
		assertEquals( resultSet[2].getLevelIndex( "level21" ), 0 );
		assertEquals( resultSet[2].getLevelKeyDataType( "level21", "col21" ), DataType.STRING_TYPE );
		resultSet[2].seek( 0 );
		assertEquals( resultSet[2].getLevelKeyValue( 0 )[0], "1" );
		assertEquals( resultSet[2].getAggregationValue( 0 ), new Double(6) );
		resultSet[2].seek( 1 );
		assertEquals( resultSet[2].getLevelKeyValue( 0 )[0], "2" );
		assertEquals( resultSet[2].getAggregationValue( 0 ), new Double(22) );
		
		//result set for aggregation 3
		assertEquals( resultSet[3].length( ), 2 );
		
		assertEquals( resultSet[3].getAggregationDataType( 0 ), DataType.DOUBLE_TYPE );
		assertEquals( resultSet[3].getLevelIndex( "level11" ), 0 );
		assertEquals( resultSet[3].getLevelKeyDataType( "level11", "col11" ), DataType.STRING_TYPE );
		resultSet[3].seek( 0 );
		assertEquals( resultSet[3].getLevelKeyValue( 0 )[0], "1" );
		assertEquals( resultSet[3].getAggregationValue( 0 ), new Double(6) );
		resultSet[3].seek( 1 );
		assertEquals( resultSet[3].getLevelKeyValue( 0 )[0], "2" );
		assertEquals( resultSet[3].getAggregationValue( 0 ), new Double(22) );
	}
}

class TestFactTable implements IDatasetIterator
{

	int ptr = -1;
	static String[] L1Col = {
			"1", "1", "1", "1", "2", "2", "2", "2", "3", "3", "3", "3"
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
		if ( name.equals( "col11" ) )
		{
			return 0;
		}
		else if ( name.equals( "col12" ) )
		{
			return -1;
		}
		else if ( name.equals( "col13" ) )
		{
			return 2;
		}
		else if ( name.equals( "col21" ) )
		{
			return 3;
		}
		else if ( name.equals( "col31" ) )
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
		if ( name.equals( "col11" ) )
		{
			return DataType.STRING_TYPE;
		}
		else if ( name.equals( "col12" ) )
		{
			return -1;
		}
		else if ( name.equals( "col13" ) )
		{
			return DataType.INTEGER_TYPE;
		}
		else if ( name.equals( "col21" ) )
		{
			return DataType.STRING_TYPE;
		}
		else if ( name.equals( "col31" ) )
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
			return L1Col[ptr];
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
			return L1Col[ptr];
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
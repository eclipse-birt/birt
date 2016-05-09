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

package org.eclipse.birt.data.engine.olap.data.impl.dimension;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;


import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.StopSign;
import org.eclipse.birt.data.engine.olap.data.api.ILevel;
import org.eclipse.birt.data.engine.olap.data.api.ISelection;
import org.eclipse.birt.data.engine.olap.data.api.cube.IDatasetIterator;
import org.eclipse.birt.data.engine.olap.data.api.cube.IDimension;
import org.eclipse.birt.data.engine.olap.data.api.cube.IHierarchy;
import org.eclipse.birt.data.engine.olap.data.api.cube.ILevelDefn;
import org.eclipse.birt.data.engine.olap.data.document.DocumentManagerFactory;
import org.eclipse.birt.data.engine.olap.data.document.IDocumentManager;
import org.eclipse.birt.data.engine.olap.data.impl.SelectionFactory;
import org.eclipse.birt.data.engine.olap.data.util.BufferedPrimitiveDiskArray;
import org.eclipse.birt.data.engine.olap.data.util.DataType;
import org.eclipse.birt.data.engine.olap.data.util.IDiskArray;
import org.eclipse.birt.data.engine.olap.data.util.IndexKey;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;

/**
 * 
 */

public class DimensionTest {

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
    public void testDimensionCreateAndFind( ) throws IOException, BirtException
	{
		IDocumentManager documentManager = DocumentManagerFactory.createFileDocumentManager( );
		
		testDimensionCreate( documentManager );
		testDimensionFind( documentManager );
		documentManager.close( );
	}
	

	private void testDimensionCreate( IDocumentManager documentManager ) throws IOException, BirtException, DataException
	{
		ILevelDefn[] levelDefs = new ILevelDefn[1];
		String[] attrs = {
				"Name", "Age"
		};
		levelDefs[0] = new LevelDefinition( "student", new String[]{"ID"}, attrs );
		
		IDimension dimension = DimensionFactory.createDimension( "student",
				documentManager,
				new OneLevelDataset( ),
				levelDefs,
				false, new StopSign());
		IHierarchy hierarchy = dimension.getHierarchy( );
		assertEquals( hierarchy.getName( ), "student" );
		ILevel[] level = hierarchy.getLevels( );
		assertEquals( level[0].getName( ), "student" );
		Dimension realDimension = (Dimension) dimension;
		for ( int i = 0; i < OneLevelDataset.IDCol.length; i++ )
		{
			IndexKey indexKey = realDimension.findFirst( (Level) level[0],
					 new Object[]{new Integer( OneLevelDataset.IDCol[i] )} );
			assertEquals( indexKey.getKey()[0],
					new Integer( OneLevelDataset.IDCol[i] ) );
			assertEquals( indexKey.getDimensionPos()[0], i );
			Member levelMember = realDimension.getRowByPosition(
					indexKey.getDimensionPos()[0] ).getMembers()[0];
			assertEquals( levelMember.getKeyValues()[0],
					new Integer( OneLevelDataset.IDCol[i] ) );
			assertEquals( levelMember.getAttributes()[0], OneLevelDataset.NameCol[i] );
			assertEquals( levelMember.getAttributes()[1],
					OneLevelDataset.AgeCol[i] );

			levelMember = realDimension.getDimensionRowByOffset(
					indexKey.getOffset()[0] ).getMembers()[0];
			assertEquals( levelMember.getKeyValues()[0],
					new Integer( OneLevelDataset.IDCol[i] ) );
			assertEquals( levelMember.getAttributes()[0], OneLevelDataset.NameCol[i] );
			assertEquals( levelMember.getAttributes()[1],
					OneLevelDataset.AgeCol[i] );
		}
		
	}

	private void testDimensionFind( IDocumentManager documentManager ) throws DataException, IOException
	{
		IDimension dimension;
		IHierarchy hierarchy;
		ILevel[] level;
		Dimension realDimension;
		// test load dimension from disk
		dimension = DimensionFactory.loadDimension( "student", documentManager );
		hierarchy = dimension.getHierarchy( );
		assertEquals( hierarchy.getName( ), "student" );
		level = hierarchy.getLevels( );
		assertEquals( level[0].getName( ), "student" );
		realDimension = (Dimension) dimension;
		for ( int i = 0; i < OneLevelDataset.IDCol.length; i++ )
		{
			IndexKey indexKey = realDimension.findFirst( (Level) level[0],
					 new Object[]{new Integer( OneLevelDataset.IDCol[i] )} );
			assertEquals( indexKey.getKey()[0],
					new Integer( OneLevelDataset.IDCol[i] ) );
			assertEquals( indexKey.getDimensionPos()[0], i );
			Member levelMember = realDimension.getRowByPosition(
					indexKey.getDimensionPos()[0] ).getMembers()[0];
			assertEquals( levelMember.getKeyValues()[0],
					new Integer( OneLevelDataset.IDCol[i] ) );
			assertEquals( levelMember.getAttributes()[0], OneLevelDataset.NameCol[i] );
			assertEquals( levelMember.getAttributes()[1],
					OneLevelDataset.AgeCol[i] );

			levelMember = realDimension.getDimensionRowByOffset(
					indexKey.getOffset()[0] ).getMembers()[0];
			assertEquals( levelMember.getKeyValues()[0],
					new Integer( OneLevelDataset.IDCol[i] ) );
			assertEquals( levelMember.getAttributes()[0], OneLevelDataset.NameCol[i] );
			assertEquals( levelMember.getAttributes()[1],
					OneLevelDataset.AgeCol[i] );

		}
	}
	
	/**
	 * 
	 * @throws IOException
	 * @throws BirtException
	 */
	@Test
    public void testDimensionCreateAndFind1( ) throws IOException, BirtException
	{
		IDocumentManager documentManager = DocumentManagerFactory.createFileDocumentManager( );
		
		testDimensionCreateAndFind1( documentManager );
		documentManager.close( );
	}
	

	private void testDimensionCreateAndFind1( IDocumentManager documentManager ) throws IOException, BirtException, DataException
	{
		int memeberCount = 10000;
		ILevelDefn[] levelDefs = new ILevelDefn[1];
		levelDefs[0] = new LevelDefinition( "student", new String[]{"ID"}, null );
		String[] levelNames = new String[1];
		levelNames[0] = "ID";
		DimensionForTest iterator = new DimensionForTest( levelNames );
		String[] IDs = new String[memeberCount];
		
		for ( int i = 0; i < memeberCount; i++ )
		{
			IDs[i] = String.valueOf( i );
		}
		Arrays.sort( IDs );
		iterator.setLevelMember( 0, IDs );
		
		IDimension dimension = DimensionFactory.createDimension( "student",
				documentManager,
				iterator,
				levelDefs,
				false, new StopSign());
		IHierarchy hierarchy = dimension.getHierarchy( );
		assertEquals( hierarchy.getName( ), "student" );
		ILevel[] level = hierarchy.getLevels( );
		assertEquals( level[0].getName( ), "student" );
		Dimension realDimension = (Dimension) dimension;
		for ( int i = 0; i < memeberCount; i++ )
		{
			IndexKey indexKey = realDimension.findFirst( (Level) level[0],
					 new Object[]{IDs[i]} );
			assertEquals( indexKey.getKey()[0],
					IDs[i] );
			assertEquals( indexKey.getDimensionPos()[0], i );
			Member levelMember = realDimension.getRowByPosition(
					indexKey.getDimensionPos()[0] ).getMembers()[0];
			assertEquals( levelMember.getKeyValues()[0],
					IDs[i] );

			levelMember = realDimension.getDimensionRowByOffset(
					indexKey.getOffset()[0]).getMembers()[0];
			assertEquals( levelMember.getKeyValues()[0],
					IDs[i] );
		}

		// test load dimension from disk
		dimension = DimensionFactory.loadDimension( "student", documentManager );
		hierarchy = dimension.getHierarchy( );
		assertEquals( hierarchy.getName( ), "student" );
		level = hierarchy.getLevels( );
		assertEquals( level[0].getName( ), "student" );
		realDimension = (Dimension) dimension;
		for ( int i = 0; i < memeberCount; i++ )
		{
			IndexKey indexKey = realDimension.findFirst( (Level) level[0],
					 new Object[]{IDs[i]} );
			assertEquals( indexKey.getKey()[0],
					IDs[i] );
			assertEquals( indexKey.getDimensionPos()[0], i );
			Member levelMember = realDimension.getRowByPosition(
					indexKey.getDimensionPos()[0] ).getMembers()[0];
			assertEquals( levelMember.getKeyValues()[0],
					IDs[i] );

			levelMember = realDimension.getDimensionRowByOffset(
					indexKey.getOffset()[0] ).getMembers()[0];
			assertEquals( levelMember.getKeyValues()[0],
					IDs[i] );
		}
	}

	/**
	 * 
	 * @throws IOException
	 * @throws BirtException
	 */
	@Test
    public void testDimensionCreateAndFind2( ) throws IOException,
			BirtException
	{
		IDocumentManager documentManager = DocumentManagerFactory.createFileDocumentManager( );
		
		testDimensionCreate2( documentManager );
		testDimensionFind2( documentManager );
		documentManager.close( );
	}

	private void testDimensionCreate2( IDocumentManager documentManager ) throws IOException, BirtException, DataException
	{
		ILevelDefn[] levelDefs = new ILevelDefn[3];
		String[] attrs = {
			"level3Attribute"
		};
		levelDefs[0] = new LevelDefinition( "level1", new String[]{"level1"}, null );
		levelDefs[1] = new LevelDefinition( "level2", new String[]{"level2"}, null );
		levelDefs[2] = new LevelDefinition( "level3", new String[]{"level3"}, attrs );
		
		IDimension dimension = DimensionFactory.createDimension( "three",
				documentManager,
				new ThreeLevelDataset( ),
				levelDefs,
				false, new StopSign() );
		IHierarchy hierarchy = dimension.getHierarchy( );
		assertEquals( hierarchy.getName( ), "three" );
		ILevel[] level = hierarchy.getLevels( );
		assertEquals( level[0].getName( ), "level1" );
		assertEquals( level[0].getKeyDataType( "level1" ), DataType.STRING_TYPE );
		assertEquals( level[1].getName( ), "level2" );
		assertEquals( level[1].getKeyDataType( "level2" ), DataType.STRING_TYPE );
		assertEquals( level[2].getName( ), "level3" );
		assertEquals( level[2].getKeyDataType( "level3" ), DataType.STRING_TYPE );
		Dimension realDimension = (Dimension) dimension;
		for ( int i = 0; i < ThreeLevelDataset.Level1Col.length; i++ )
		{
			System.out.println( i );
			// level 0
			IndexKey indexKey = realDimension.findFirst( (Level) level[0],
					 new Object[]{ThreeLevelDataset.Level1Col[i]} );
			assertEquals( indexKey.getKey()[0], ThreeLevelDataset.Level1Col[i] );
			if ( i < 5 )
				assertEquals( indexKey.getDimensionPos()[0], 0 );
			if ( i >= 5 && i < 12 )
				assertEquals( indexKey.getDimensionPos()[0], 5 );
			if ( i >= 12 )
				assertEquals( indexKey.getDimensionPos()[0], 12 );

			Member levelMember = realDimension.getRowByPosition(
					indexKey.getDimensionPos()[0] ).getMembers()[0];
			assertEquals( levelMember.getKeyValues()[0], ThreeLevelDataset.Level1Col[i] );
			assertTrue( levelMember.getAttributes() == null );

			levelMember = realDimension.getDimensionRowByOffset(
					indexKey.getOffset()[0] ).getMembers()[0];
			assertEquals( levelMember.getKeyValues()[0], ThreeLevelDataset.Level1Col[i] );
			assertTrue( levelMember.getAttributes() == null );

			// level 1
			indexKey = realDimension.findFirst( (Level) level[1],
					 new Object[]{ThreeLevelDataset.Level2Col[i]} );
			assertEquals( indexKey.getKey()[0], ThreeLevelDataset.Level2Col[i] );

			levelMember = realDimension.getRowByPosition(
					indexKey.getDimensionPos()[0] ).getMembers()[1];
			assertEquals( levelMember.getKeyValues()[0], ThreeLevelDataset.Level2Col[i] );
			assertTrue( levelMember.getAttributes() == null );

			levelMember = realDimension.getDimensionRowByOffset(
					indexKey.getOffset()[0] ).getMembers()[1];
			assertEquals( levelMember.getKeyValues()[0], ThreeLevelDataset.Level2Col[i] );
			assertTrue( levelMember.getAttributes() == null );

			// level 2
			indexKey = realDimension.findFirst( (Level) level[2],
					 new Object[]{ThreeLevelDataset.Level3Col[i]} );
			assertEquals( indexKey.getKey()[0], ThreeLevelDataset.Level3Col[i] );
			assertEquals( indexKey.getDimensionPos()[0], i );

			levelMember = realDimension.getRowByPosition(
					indexKey.getDimensionPos()[0] ).getMembers()[2];
			assertEquals( levelMember.getKeyValues()[0], ThreeLevelDataset.Level3Col[i] );
			
			levelMember = realDimension.getDimensionRowByOffset(
					indexKey.getOffset()[0] ).getMembers()[2];
			assertEquals( levelMember.getKeyValues()[0], ThreeLevelDataset.Level3Col[i] );
			
		}

	}

	private void testDimensionFind2( IDocumentManager documentManager ) throws DataException, IOException
	{
		IDimension dimension;
		IHierarchy hierarchy;
		ILevel[] level;
		Dimension realDimension;
		// test load dimension from disk
		System.out.println( "load starting..." );
		dimension = DimensionFactory.loadDimension( "three",
				documentManager );
		System.out.println( "load end..." );
		hierarchy = dimension.getHierarchy( );
		assertEquals( hierarchy.getName( ), "three" );
		level = hierarchy.getLevels( );
		assertEquals( hierarchy.getName( ), "three" );
		level = hierarchy.getLevels( );
		assertEquals( level[0].getName( ), "level1" );
		assertEquals( level[0].getKeyDataType( "level1" ), DataType.STRING_TYPE );
		assertEquals( level[1].getName( ), "level2" );
		assertEquals( level[1].getKeyDataType( "level2" ), DataType.STRING_TYPE );
		assertEquals( level[2].getName( ), "level3" );
		assertEquals( level[2].getKeyDataType( "level3" ), DataType.STRING_TYPE );
		realDimension = (Dimension) dimension;
		for ( int i = 0; i < ThreeLevelDataset.Level1Col.length; i++ )
		{
			// System.out.println( "load" + i );
			// level 0
			IndexKey indexKey = realDimension.findFirst( (Level) level[0],
					 new Object[]{ThreeLevelDataset.Level1Col[i]} );
			assertEquals( indexKey.getKey()[0], ThreeLevelDataset.Level1Col[i] );
			if ( i < 5 )
				assertEquals( indexKey.getDimensionPos()[0], 0 );
			if ( i >= 5 && i < 12 )
				assertEquals( indexKey.getDimensionPos()[0], 5 );
			if ( i >= 12 )
				assertEquals( indexKey.getDimensionPos()[0], 12 );

			Member levelMember = realDimension.getRowByPosition(
					indexKey.getDimensionPos()[0] ).getMembers()[0];
			assertEquals( levelMember.getKeyValues()[0], ThreeLevelDataset.Level1Col[i] );
			assertTrue( levelMember.getAttributes() == null );

			levelMember = realDimension.getDimensionRowByOffset(
					indexKey.getOffset()[0] ).getMembers()[0];
			assertEquals( levelMember.getKeyValues()[0], ThreeLevelDataset.Level1Col[i] );
			assertTrue( levelMember.getAttributes() == null );

			// level 1
			indexKey = realDimension.findFirst( (Level) level[1],
					 new Object[]{ThreeLevelDataset.Level2Col[i]} );
			assertEquals( indexKey.getKey()[0], ThreeLevelDataset.Level2Col[i] );
			
			levelMember = realDimension.getRowByPosition(
					indexKey.getDimensionPos()[0] ).getMembers()[1];
			assertEquals( levelMember.getKeyValues()[0], ThreeLevelDataset.Level2Col[i] );

			levelMember = realDimension.getDimensionRowByOffset(
					indexKey.getOffset()[0] ).getMembers()[1];
			assertEquals( levelMember.getKeyValues()[0], ThreeLevelDataset.Level2Col[i] );
			assertTrue( levelMember.getAttributes() == null );

			// level 2
			indexKey = realDimension.findFirst( (Level) level[2],
					 new Object[]{ThreeLevelDataset.Level3Col[i]} );
			assertEquals( indexKey.getKey()[0], ThreeLevelDataset.Level3Col[i] );
			assertEquals( indexKey.getDimensionPos()[0], i );

			levelMember = realDimension.getRowByPosition(
					indexKey.getDimensionPos()[0] ).getMembers()[2];
			assertEquals( levelMember.getKeyValues()[0], ThreeLevelDataset.Level3Col[i] );
			
			levelMember = realDimension.getDimensionRowByOffset(
					indexKey.getOffset()[0] ).getMembers()[2];
			assertEquals( levelMember.getKeyValues()[0], ThreeLevelDataset.Level3Col[i] );
		}
	}

	/**
	 * 
	 * @throws IOException
	 * @throws BirtException
	 */
	@Test
    public void testDimensionCreateAndFind3( ) throws IOException,
			BirtException
	{
		IDocumentManager documentManager = DocumentManagerFactory.createFileDocumentManager( );
		
		testDimensionCreate3( documentManager );
		testDimensionFind3( documentManager );
		documentManager.close( );
	}


	private void testDimensionCreate3( IDocumentManager documentManager ) throws IOException, BirtException, DataException
	{
		ILevelDefn[] levelDefs = new ILevelDefn[3];
		String[] attrs = {
			"level3Attribute"
		};
		levelDefs[0] = new LevelDefinition( "level1", new String[]{"level1"}, null );
		levelDefs[1] = new LevelDefinition( "level2", new String[]{"level2"}, null );
		levelDefs[2] = new LevelDefinition( "level3", new String[]{"level3"}, attrs );
		
		IDimension dimension = DimensionFactory.createDimension( "three",
				documentManager,
				new ThreeLevelDataset( ),
				levelDefs,
				false, new StopSign() );
		IHierarchy hierarchy = dimension.getHierarchy( );
		assertEquals( hierarchy.getName( ), "three" );
		ILevel[] level = hierarchy.getLevels( );

		Dimension realDimension = (Dimension) dimension;
		for ( int i = 0; i < ThreeLevelDataset.Level1Col.length; i++ )
		{
			// System.out.println( i );
			// level 0
			IndexKey indexKey = realDimension.findFirst( (Level) level[0],
					 new Object[]{ThreeLevelDataset.Level1Col[i]} );

			Member levelMember = realDimension.getRowByPosition(
					indexKey.getDimensionPos()[0] ).getMembers()[0];
			assertEquals( levelMember.getKeyValues()[0], ThreeLevelDataset.Level1Col[i] );
			assertTrue( levelMember.getAttributes() == null );
			

			levelMember = realDimension.getDimensionRowByOffset(
					indexKey.getOffset()[0] ).getMembers()[0];
			assertEquals( levelMember.getKeyValues()[0], ThreeLevelDataset.Level1Col[i] );
			
			// level 1
			indexKey = realDimension.findFirst( (Level) level[1],
					 new Object[]{ThreeLevelDataset.Level2Col[i]} );
			assertEquals( indexKey.getKey()[0], ThreeLevelDataset.Level2Col[i] );
			
			levelMember = realDimension.getRowByPosition(
					indexKey.getDimensionPos()[0] ).getMembers()[1];
			assertEquals( levelMember.getKeyValues()[0], ThreeLevelDataset.Level2Col[i] );
			assertTrue( levelMember.getAttributes() == null );
			

			levelMember = realDimension.getDimensionRowByOffset(
					indexKey.getOffset()[0] ).getMembers()[1];
			assertEquals( levelMember.getKeyValues()[0], ThreeLevelDataset.Level2Col[i] );
			assertTrue( levelMember.getAttributes() == null );


			// level 2
			indexKey = realDimension.findFirst( (Level) level[2],
					 new Object[]{ThreeLevelDataset.Level3Col[i]} );
			assertEquals( indexKey.getKey()[0], ThreeLevelDataset.Level3Col[i] );
			assertEquals( indexKey.getDimensionPos()[0], i );

			levelMember = realDimension.getRowByPosition(
					indexKey.getDimensionPos()[0] ).getMembers()[2];
			assertEquals( levelMember.getKeyValues()[0], ThreeLevelDataset.Level3Col[i] );
			assertEquals( levelMember.getAttributes().length, 1 );
			assertEquals( levelMember.getAttributes()[0],
					new Integer( ThreeLevelDataset.Level3AttributeCol[i] ) );


			levelMember = realDimension.getDimensionRowByOffset(
					indexKey.getOffset()[0] ).getMembers()[2];
			assertEquals( levelMember.getKeyValues()[0], ThreeLevelDataset.Level3Col[i] );
			assertEquals( levelMember.getAttributes().length, 1 );
			assertEquals( levelMember.getAttributes()[0],
					new Integer( ThreeLevelDataset.Level3AttributeCol[i] ) );

		}
	}

	private void testDimensionFind3( IDocumentManager documentManager ) throws DataException, IOException
	{
		IDimension dimension;
		IHierarchy hierarchy;
		ILevel[] level;
		Dimension realDimension;
		// test load dimension from disk
		System.out.println( "load starting..." );
		dimension = DimensionFactory.loadDimension( "three", documentManager );
		System.out.println( "load end..." );
		hierarchy = dimension.getHierarchy( );
		assertEquals( hierarchy.getName( ), "three" );
		level = hierarchy.getLevels( );
		assertEquals( hierarchy.getName( ), "three" );
		level = hierarchy.getLevels( );
		assertEquals( level[0].getName( ), "level1" );
		assertEquals( level[0].getKeyDataType( "level1" ), DataType.STRING_TYPE );
		assertEquals( level[1].getName( ), "level2" );
		assertEquals( level[1].getKeyDataType( "level2" ), DataType.STRING_TYPE );
		assertEquals( level[2].getName( ), "level3" );
		assertEquals( level[2].getKeyDataType( "level3" ), DataType.STRING_TYPE );
		realDimension = (Dimension) dimension;
		for ( int i = 0; i < ThreeLevelDataset.Level1Col.length; i++ )
		{
			// System.out.println( "load" + i );
			// level 0
			IndexKey indexKey = realDimension.findFirst( (Level) level[0],
					 new Object[]{ThreeLevelDataset.Level1Col[i]} );

			Member levelMember = realDimension.getRowByPosition(
					indexKey.getDimensionPos()[0] ).getMembers()[0];
			assertEquals( levelMember.getKeyValues()[0], ThreeLevelDataset.Level1Col[i] );
			assertTrue( levelMember.getAttributes() == null );
			

			levelMember = realDimension.getDimensionRowByOffset(
					indexKey.getOffset()[0] ).getMembers()[0];
			assertEquals( levelMember.getKeyValues()[0], ThreeLevelDataset.Level1Col[i] );
			

			// level 1
			indexKey = realDimension.findFirst( (Level) level[1],
					 new Object[]{ThreeLevelDataset.Level2Col[i]} );
			assertEquals( indexKey.getKey()[0], ThreeLevelDataset.Level2Col[i] );
			
			levelMember = realDimension.getRowByPosition(
					indexKey.getDimensionPos()[0] ).getMembers()[1];
			assertEquals( levelMember.getKeyValues()[0], ThreeLevelDataset.Level2Col[i] );
			assertTrue( levelMember.getAttributes() == null );
			

			levelMember = realDimension.getDimensionRowByOffset(
					indexKey.getOffset()[0] ).getMembers()[1];
			assertEquals( levelMember.getKeyValues()[0], ThreeLevelDataset.Level2Col[i] );
			assertTrue( levelMember.getAttributes() == null );


			// level 2
			indexKey = realDimension.findFirst( (Level) level[2],
					 new Object[]{ThreeLevelDataset.Level3Col[i]} );
			assertEquals( indexKey.getKey()[0], ThreeLevelDataset.Level3Col[i] );
			assertEquals( indexKey.getDimensionPos()[0], i );

			levelMember = realDimension.getRowByPosition(
					indexKey.getDimensionPos()[0] ).getMembers()[2];
			assertEquals( levelMember.getKeyValues()[0], ThreeLevelDataset.Level3Col[i] );
			assertEquals( levelMember.getAttributes().length, 1 );
			assertEquals( levelMember.getAttributes()[0],
					new Integer( ThreeLevelDataset.Level3AttributeCol[i] ) );
			

			levelMember = realDimension.getDimensionRowByOffset(
					indexKey.getOffset()[0] ).getMembers()[2];
			assertEquals( levelMember.getKeyValues()[0], ThreeLevelDataset.Level3Col[i] );
			assertEquals( levelMember.getAttributes().length, 1 );
			assertEquals( levelMember.getAttributes()[0],
					new Integer( ThreeLevelDataset.Level3AttributeCol[i] ) );
			
		}
	}

	/**
	 * 
	 * @throws IOException
	 * @throws BirtException
	 */
	@Test
    public void testDimensionCreateAndFind4( ) throws IOException,
			BirtException
	{
		IDocumentManager documentManager = DocumentManagerFactory.createFileDocumentManager( );
		testDimensionCreateAndFind4( documentManager );
		documentManager.close( );

	}
	

	private void testDimensionCreateAndFind4( IDocumentManager documentManager ) throws IOException, BirtException, DataException
	{
		ILevelDefn[] levelDefs = new ILevelDefn[3];
		String[] attrs = {
			"level3Attribute"
		};
		levelDefs[0] = new LevelDefinition( "level1", new String[]{"level1"}, null );
		levelDefs[1] = new LevelDefinition( "level2", new String[]{"level2"}, null );
		levelDefs[2] = new LevelDefinition( "level3", new String[]{"level3"}, attrs );
		
		IDimension dimension = DimensionFactory.createDimension( "three",
				documentManager,
				new ThreeLevelDataset( ),
				levelDefs,
				false, new StopSign() );
		IHierarchy hierarchy = dimension.getHierarchy( );
		assertEquals( hierarchy.getName( ), "three" );
		ILevel[] level = hierarchy.getLevels( );

		Dimension realDimension = (Dimension) dimension;

		// level 0
		IndexKey indexKey = realDimension.findFirst( (Level) level[0],  new Object[]{"1"} );

		Member levelMember = realDimension.getDimensionRowByOffset(
				indexKey.getOffset()[0] ).getMembers()[0];
		assertEquals( levelMember.getKeyValues()[0], "1" );
		

		indexKey = realDimension.findFirst( (Level) level[0],  new Object[]{"2"} );

		levelMember = realDimension.getDimensionRowByOffset(
				indexKey.getOffset()[0] ).getMembers()[0];
		assertEquals( levelMember.getKeyValues()[0], "2" );
		

		indexKey = realDimension.findFirst( (Level) level[0],  new Object[]{"3"} );

		levelMember = realDimension.getDimensionRowByOffset(
				indexKey.getOffset()[0] ).getMembers()[0];
		assertEquals( levelMember.getKeyValues()[0], "3" );
		

		// level 1
		indexKey = realDimension.findFirst( (Level) level[1],  new Object[]{"11"} );
		levelMember = realDimension.getDimensionRowByOffset(
				indexKey.getOffset()[0] ).getMembers()[1];
		assertEquals( levelMember.getKeyValues()[0], "11" );
		

		indexKey = realDimension.findFirst( (Level) level[1],  new Object[]{"31"} );
		levelMember = realDimension.getDimensionRowByOffset(
				indexKey.getOffset()[0] ).getMembers()[1];
		assertEquals( levelMember.getKeyValues()[0], "31" );
		

		indexKey = realDimension.findFirst( (Level) level[1],  new Object[]{"32"} );
		levelMember = realDimension.getDimensionRowByOffset(
				indexKey.getOffset()[0] ).getMembers()[1];
		assertEquals( levelMember.getKeyValues()[0], "32" );
		
		// level 2
		indexKey = realDimension.findFirst( (Level) level[2],  new Object[]{"111"} );
		levelMember = realDimension.getDimensionRowByOffset(
				indexKey.getOffset()[0] ).getMembers()[2];
		assertEquals( levelMember.getKeyValues()[0], "111" );

		indexKey = realDimension.findFirst( (Level) level[2],  new Object[]{"311"} );
		levelMember = realDimension.getDimensionRowByOffset(
				indexKey.getOffset()[0] ).getMembers()[2];
		assertEquals( levelMember.getKeyValues()[0], "311" );

		indexKey = realDimension.findFirst( (Level) level[2],  new Object[]{"321"} );
		levelMember = realDimension.getDimensionRowByOffset(
				indexKey.getOffset()[0] ).getMembers()[2];
		assertEquals( levelMember.getKeyValues()[0], "321" );


		// test load dimension from disk
		System.out.println( "load starting..." );
		dimension = DimensionFactory.loadDimension( "three", documentManager );
		System.out.println( "load end..." );
		hierarchy = dimension.getHierarchy( );
		assertEquals( hierarchy.getName( ), "three" );
		level = hierarchy.getLevels( );
		assertEquals( hierarchy.getName( ), "three" );
		level = hierarchy.getLevels( );
		assertEquals( level[0].getName( ), "level1" );
		assertEquals( level[0].getKeyDataType( "level1" ), DataType.STRING_TYPE );
		assertEquals( level[1].getName( ), "level2" );
		assertEquals( level[1].getKeyDataType( "level2" ), DataType.STRING_TYPE );
		assertEquals( level[2].getName( ), "level3" );
		assertEquals( level[2].getKeyDataType( "level3" ), DataType.STRING_TYPE );
		realDimension = (Dimension) dimension;
	}
	

	/**
	 * 
	 * @throws IOException
	 * @throws BirtException
	 */
	@Test
    public void testDimensionCreateAndFind5( ) throws IOException,
			BirtException
	{
		IDocumentManager documentManager = DocumentManagerFactory.createFileDocumentManager( );
		
		testDimensionCreate5( documentManager );
		testDimensionFind5( documentManager );
		documentManager.close( );
	}
	

	private void testDimensionCreate5( IDocumentManager documentManager ) throws IOException, BirtException, DataException
	{
		ILevelDefn[] levelDefs = new ILevelDefn[4];
		levelDefs[0] = new LevelDefinition( "level1", new String[]{"level1"}, null );
		levelDefs[1] = new LevelDefinition( "level2", new String[]{"level2"}, null );
		levelDefs[2] = new LevelDefinition( "level3", new String[]{"level3"}, null );
		levelDefs[3] = new LevelDefinition( "level4", new String[]{"level4"}, null );
		
		IDimension dimension = DimensionFactory.createDimension( "four",
				documentManager,
				new FourLevelDataset( ),
				levelDefs,
				false, new StopSign() );
		IHierarchy hierarchy = dimension.getHierarchy( );
		assertEquals( hierarchy.getName( ), "four" );
		ILevel[] level = hierarchy.getLevels( );

		Dimension realDimension = (Dimension) dimension;

		// level 0
		IndexKey indexKey = realDimension.findFirst( (Level) level[0],  new Object[]{"1"} );

		Member levelMember = realDimension.getDimensionRowByOffset(
				indexKey.getOffset()[0] ).getMembers()[0];
		assertEquals( levelMember.getKeyValues()[0], "1" );
		

		indexKey = realDimension.findFirst( (Level) level[0],  new Object[]{"2"} );

		levelMember = realDimension.getDimensionRowByOffset(
				indexKey.getOffset()[0] ).getMembers()[0];
		assertEquals( levelMember.getKeyValues()[0], "2" );
		
		indexKey = realDimension.findFirst( (Level) level[0],  new Object[]{"3"} );

		levelMember = realDimension.getDimensionRowByOffset(
				indexKey.getOffset()[0] ).getMembers()[0];
		assertEquals( levelMember.getKeyValues()[0], "3" );
		
		// level 1
		indexKey = realDimension.findFirst( (Level) level[1],  new Object[]{"11"} );
		levelMember = realDimension.getDimensionRowByOffset(
				indexKey.getOffset()[0] ).getMembers()[1];
		assertEquals( levelMember.getKeyValues()[0], "11" );
		
		indexKey = realDimension.findFirst( (Level) level[1],  new Object[]{"31"} );
		levelMember = realDimension.getDimensionRowByOffset(
				indexKey.getOffset()[0] ).getMembers()[1];
		assertEquals( levelMember.getKeyValues()[0], "31" );
		
		indexKey = realDimension.findFirst( (Level) level[1],  new Object[]{"32"} );
		levelMember = realDimension.getDimensionRowByOffset(
				indexKey.getOffset()[0] ).getMembers()[1];
		assertEquals( levelMember.getKeyValues()[0], "32" );
		

		// level 2
		indexKey = realDimension.findFirst( (Level) level[2],  new Object[]{"111"} );
		levelMember = realDimension.getDimensionRowByOffset(
				indexKey.getOffset()[0] ).getMembers()[2];
		assertEquals( levelMember.getKeyValues()[0], "111" );
		

		indexKey = realDimension.findFirst( (Level) level[2],  new Object[]{"311"} );
		levelMember = realDimension.getDimensionRowByOffset(
				indexKey.getOffset()[0] ).getMembers()[2];
		assertEquals( levelMember.getKeyValues()[0], "311" );
		

		indexKey = realDimension.findFirst( (Level) level[2],  new Object[]{"321"} );
		levelMember = realDimension.getDimensionRowByOffset(
				indexKey.getOffset()[0] ).getMembers()[2];
		assertEquals( levelMember.getKeyValues()[0], "321" );
		

		// level 3
		indexKey = realDimension.findFirst( (Level) level[3],  new Object[]{"1111"} );
		levelMember = realDimension.getDimensionRowByOffset(
				indexKey.getOffset()[0] ).getMembers()[3];
		assertEquals( levelMember.getKeyValues()[0], "1111" );

		indexKey = realDimension.findFirst( (Level) level[3],  new Object[]{"3111"} );
		levelMember = realDimension.getDimensionRowByOffset(
				indexKey.getOffset()[0] ).getMembers()[3];
		assertEquals( levelMember.getKeyValues()[0], "3111" );

		indexKey = realDimension.findFirst( (Level) level[3],  new Object[]{"3211"} );
		levelMember = realDimension.getDimensionRowByOffset(
				indexKey.getOffset()[0] ).getMembers()[3];
		assertEquals( levelMember.getKeyValues()[0], "3211" );
		
		indexKey = realDimension.findFirst( (Level) level[3],  new Object[]{"3212"} );
		levelMember = realDimension.getDimensionRowByOffset(
				indexKey.getOffset()[0] ).getMembers()[3];
		assertEquals( levelMember.getKeyValues()[0], "3212" );
		
	}

	private void testDimensionFind5( IDocumentManager documentManager ) throws DataException, IOException
	{
		IDimension dimension;
		IHierarchy hierarchy;
		ILevel[] level;
		// test load dimension from disk
		System.out.println( "load starting..." );
		dimension = DimensionFactory.loadDimension( "four", documentManager );
		System.out.println( "load end..." );
		hierarchy = dimension.getHierarchy( );
		assertEquals( hierarchy.getName( ), "four" );
		level = hierarchy.getLevels( );
		assertEquals( hierarchy.getName( ), "four" );
		level = hierarchy.getLevels( );
		assertEquals( level[0].getName( ), "level1" );
		assertEquals( level[0].getKeyDataType( "level1" ), DataType.STRING_TYPE );
		assertEquals( level[1].getName( ), "level2" );
		assertEquals( level[1].getKeyDataType( "level2" ), DataType.STRING_TYPE );
		assertEquals( level[2].getName( ), "level3" );
		assertEquals( level[2].getKeyDataType( "level3" ), DataType.STRING_TYPE );
		assertEquals( level[3].getName( ), "level4" );
		assertEquals( level[3].getKeyDataType( "level4" ), DataType.STRING_TYPE );
	}

	/**
	 * 
	 * @throws IOException
	 * @throws BirtException
	 */
	@Test
    public void testDimensionCreateAndFind6( ) throws IOException,
			BirtException
	{
		IDocumentManager documentManager = DocumentManagerFactory.createFileDocumentManager( );
		testDimensionCreateAndFind6( documentManager );
		documentManager.close( );
	}
	
	private void testDimensionCreateAndFind6( IDocumentManager documentManager ) throws IOException, BirtException, DataException
	{
		ILevelDefn[] levelDefs = new ILevelDefn[4];
		levelDefs[0] = new LevelDefinition( "level1", new String[]{"level1"}, null );
		levelDefs[1] = new LevelDefinition( "level2", new String[]{"level2"}, null );
		levelDefs[2] = new LevelDefinition( "level3", new String[]{"level3"}, null );
		levelDefs[3] = new LevelDefinition( "level4", new String[]{"level4"}, null );
		
		IDimension dimension = DimensionFactory.createDimension( "four",
				documentManager,
				new FourLevelDataset( ),
				levelDefs,
				false, new StopSign() );
		IHierarchy hierarchy = dimension.getHierarchy( );
		assertEquals( hierarchy.getName( ), "four" );
		ILevel[] levels = hierarchy.getLevels( );

		Dimension realDimension = (Dimension) dimension;

		Level[] levelsForFilter = new Level[2];
		levelsForFilter[0] = (Level) levels[1];
		levelsForFilter[1] = (Level) levels[3];
		ISelection[][] filters = new ISelection[2][];
		filters[0] = new ISelection[2];
		filters[1] = new ISelection[2];
		Object[][] selectedObjects = {
				{"22"}, {"32"}
		};
		filters[0][0] = SelectionFactory.createMutiKeySelection( selectedObjects );
		filters[0][1] = SelectionFactory.createRangeSelection(  new Object[]{"21"},
				 new Object[]{"23"},
				true,
				true );

		selectedObjects = new String[4][];
		selectedObjects[0] =  new String[1];
		selectedObjects[0][0] = "3111";
		selectedObjects[1] =  new String[1];
		selectedObjects[1][0] =  "3211";
		selectedObjects[2] =  new String[1];
		selectedObjects[2][0] =  "2221";
		selectedObjects[3] =  new String[1];
		selectedObjects[3][0] =  "2211";

		filters[1][0] = SelectionFactory.createMutiKeySelection( selectedObjects );
		filters[1][1] = SelectionFactory.createRangeSelection(  new Object[]{"2211"},
				 new Object[]{"2231"},
				true,
				true );

		IDiskArray findResult = realDimension.find( levelsForFilter, filters );
		assertEquals( findResult.get( 0 ), new Integer( 7 ) );
		assertEquals( findResult.get( 1 ), new Integer( 8 ) );
		assertEquals( findResult.get( 2 ), new Integer( 9 ) );
		assertEquals( findResult.get( 3 ), new Integer( 13 ) );

		// test load dimension from disk
		System.out.println( "load starting..." );
		dimension = DimensionFactory.loadDimension( "four", documentManager );
		System.out.println( "load end..." );
		hierarchy = dimension.getHierarchy( );
		assertEquals( hierarchy.getName( ), "four" );
		levels = hierarchy.getLevels( );
		assertEquals( hierarchy.getName( ), "four" );
		levels = hierarchy.getLevels( );
		assertEquals( levels[0].getName( ), "level1" );
		assertEquals( levels[0].getKeyDataType( "level1" ), DataType.STRING_TYPE );
		assertEquals( levels[1].getName( ), "level2" );
		assertEquals( levels[1].getKeyDataType( "level2" ), DataType.STRING_TYPE );
		assertEquals( levels[2].getName( ), "level3" );
		assertEquals( levels[2].getKeyDataType( "level3" ), DataType.STRING_TYPE );
		realDimension = (Dimension) dimension;
		findResult = realDimension.find( levelsForFilter, filters );
		assertEquals( findResult.get( 0 ), new Integer( 7 ) );
		assertEquals( findResult.get( 1 ), new Integer( 8 ) );
		assertEquals( findResult.get( 2 ), new Integer( 9 ) );
		assertEquals( findResult.get( 3 ), new Integer( 13 ) );
	}

	/**
	 * 
	 * @throws IOException
	 * @throws BirtException
	 */
	@Test
    public void testDimensionCreateAndFind7( ) throws IOException,
			BirtException
	{
		IDocumentManager documentManager = DocumentManagerFactory.createFileDocumentManager( );
		testDimensionCreateAndFind7( documentManager );
		documentManager.close( );
	}
	

	private void testDimensionCreateAndFind7( IDocumentManager documentManager ) throws IOException, BirtException
	{
		ILevelDefn[] levelDefs = new ILevelDefn[4];
		levelDefs[0] = new LevelDefinition( "level1", new String[]{"level1"}, null );
		levelDefs[1] = new LevelDefinition( "level2", new String[]{"level2"}, null );
		levelDefs[2] = new LevelDefinition( "level3", new String[]{"level3"}, null );
		levelDefs[3] = new LevelDefinition( "level4", new String[]{"level4"}, null );
		
		IDimension dimension = DimensionFactory.createDimension( "four",
				documentManager,
				new FourLevelDataset( ),
				levelDefs,
				false, new StopSign() );
		IHierarchy hierarchy = dimension.getHierarchy( );
		assertEquals( hierarchy.getName( ), "four" );

		Dimension realDimension = (Dimension) dimension;

		IDiskArray indexArray = new BufferedPrimitiveDiskArray( );
		indexArray.add( new Integer( 2 ) );
		indexArray.add( new Integer( 5 ) );
		indexArray.add( new Integer( 8 ) );
		indexArray.add( new Integer( 9 ) );
		indexArray.add( new Integer( 10 ) );
		indexArray.add( new Integer( 11 ) );
		indexArray.add( new Integer( 14 ) );
		IDiskArray result = realDimension.getDimensionRowByPositions( 
				indexArray, new StopSign( ) );
		assertEquals( ( (DimensionRow) ( result.get( 0 ) ) ).getMembers()[3].getKeyValues()[0],
				"1131" );
		assertEquals( ( (DimensionRow) ( result.get( 1 ) ) ).getMembers()[3].getKeyValues()[0],
				"2111" );
		assertEquals( ( (DimensionRow) ( result.get( 2 ) ) ).getMembers()[3].getKeyValues()[0],
				"2221" );
		assertEquals( ( (DimensionRow) ( result.get( 3 ) ) ).getMembers()[3].getKeyValues()[0],
				"2231" );
		assertEquals( ( (DimensionRow) ( result.get( 4 ) ) ).getMembers()[3].getKeyValues()[0],
				"2311" );
		assertEquals( ( (DimensionRow) ( result.get( 5 ) ) ).getMembers()[3].getKeyValues()[0],
				"2321" );
		assertEquals( ( (DimensionRow) ( result.get( 6 ) ) ).getMembers()[3].getKeyValues()[0],
				"3212" );
	}

	/**
	 * 
	 * @throws IOException
	 * @throws BirtException
	 */
	@Test
    public void testDimensionCreateAndFind8( ) throws IOException,
			BirtException
	{
		IDocumentManager documentManager = DocumentManagerFactory.createFileDocumentManager( );
		
		testDimensionCreateAndFind8( documentManager );
		documentManager.close( );
	}
	

	private void testDimensionCreateAndFind8( IDocumentManager documentManager ) throws IOException, BirtException
	{
		ILevelDefn[] levelDefs = new ILevelDefn[4];
		levelDefs[0] = new LevelDefinition( "level1", new String[]{"level1"}, null );
		levelDefs[1] = new LevelDefinition( "level2", new String[]{"level2"}, null );
		levelDefs[2] = new LevelDefinition( "level3", new String[]{"level3"}, null );
		levelDefs[3] = new LevelDefinition( "level4", new String[]{"level4"}, null );
		
		IDimension dimension = DimensionFactory.createDimension( "four",
				documentManager,
				new FourLevelDataset( ),
				levelDefs,
				false, new StopSign() );
		assertEquals( dimension.isTime( ), false );
		IHierarchy hierarchy = dimension.getHierarchy( );
		assertEquals( hierarchy.getName( ), "four" );

		Dimension realDimension = (Dimension) dimension;

		IDiskArray indexArray = new BufferedPrimitiveDiskArray( );
		indexArray.add( new Integer( 2 ) );
		indexArray.add( new Integer( 5 ) );
		indexArray.add( new Integer( 8 ) );
		indexArray.add( new Integer( 9 ) );
		indexArray.add( new Integer( 10 ) );
		indexArray.add( new Integer( 11 ) );
		indexArray.add( new Integer( 14 ) );
		IDiskArray result = realDimension.getDimensionRowByPositions(
				indexArray, new StopSign( ) );
		assertEquals( ( (DimensionRow) ( result.get( 0 ) ) ).getMembers()[2].getKeyValues()[0],
				"113" );
		assertEquals( ( (DimensionRow) ( result.get( 1 ) ) ).getMembers()[2].getKeyValues()[0],
				"211" );
		assertEquals( ( (DimensionRow) ( result.get( 2 ) ) ).getMembers()[2].getKeyValues()[0],
				"222" );
		assertEquals( ( (DimensionRow) ( result.get( 3 ) ) ).getMembers()[2].getKeyValues()[0],
				"223" );
		assertEquals( ( (DimensionRow) ( result.get( 4 ) ) ).getMembers()[2].getKeyValues()[0],
				"231" );
		assertEquals( ( (DimensionRow) ( result.get( 5 ) ) ).getMembers()[2].getKeyValues()[0],
				"232" );
		assertEquals( ( (DimensionRow) ( result.get( 6 ) ) ).getMembers()[2].getKeyValues()[0],
				"321" );
	}
	
	

	/**
	 * 
	 * @throws IOException
	 * @throws BirtException
	 */
	@Test
    public void testDimensionGetAll( ) throws IOException, BirtException
	{
		ILevelDefn[] levelDefs = new ILevelDefn[4];
		levelDefs[0] = new LevelDefinition( "level1", new String[]{"level1"}, null );
		levelDefs[1] = new LevelDefinition( "level2", new String[]{"level2"}, null );
		levelDefs[2] = new LevelDefinition( "level3", new String[]{"level3"}, null );
		levelDefs[3] = new LevelDefinition( "level4", new String[]{"level4"}, null );
		IDocumentManager documentManager = DocumentManagerFactory.createFileDocumentManager( );
		IDimension dimension = DimensionFactory.createDimension( "four",
				documentManager,
				new FourLevelDataset( ),
				levelDefs,
				false, new StopSign() );
		assertEquals( dimension.isTime( ), false );
		IHierarchy hierarchy = dimension.getHierarchy( );
		assertEquals( hierarchy.getName( ), "four" );

		Dimension realDimension = (Dimension) dimension;

		IDiskArray result = realDimension.getAllRows( new StopSign( ) );
		for ( int i = 0; i < result.size( ); i++ )
		{
			DimensionRow dimRow = (DimensionRow) ( result.get( i ) );
			assertEquals( dimRow.getMembers()[0].getKeyValues()[0],
					FourLevelDataset.Level1Col[i] );
			assertEquals( dimRow.getMembers()[1].getKeyValues()[0],
					FourLevelDataset.Level2Col[i] );
			assertEquals( dimRow.getMembers()[2].getKeyValues()[0],
					FourLevelDataset.Level3Col[i] );
			assertEquals( dimRow.getMembers()[3].getKeyValues()[0],
					FourLevelDataset.Level4Col[i] );
		}
		documentManager.close( );
	}
}

class OneLevelDataset implements IDatasetIterator
{

	int ptr = -1;
	static int[] IDCol = {
			1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 14, 17
	};
	static String[] NameCol = {
			"name1",
			"name2",
			"name3",
			"name4",
			"name5",
			"name6",
			"name7",
			"name8",
			"name9",
			"name10",
			"name11",
			"name12",
			"name14",
			"name17"
	};
	static Integer[] AgeCol = {
			null, new Integer(2), new Integer(3), new Integer(4)
			, new Integer(5), new Integer(6), new Integer(7), new Integer(8)
			, new Integer(9), new Integer(10), new Integer(11), new Integer(12)
			, new Integer(14), new Integer(17)
	};

	static int[] ClassCol = {
			1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 14, 17
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
		if ( name.equals( "ID" ) )
		{
			return 0;
		}
		else if ( name.equals( "Name" ) )
		{
			return 1;
		}
		else if ( name.equals( "Age" ) )
		{
			return 2;
		}
		else if ( name.equals( "Class" ) )
		{
			return 3;
		}
		return -1;
	}

	public int getFieldType( String name ) throws BirtException
	{
		if ( name.equals( "ID" ) )
		{
			return DataType.INTEGER_TYPE;
		}
		else if ( name.equals( "Name" ) )
		{
			return DataType.STRING_TYPE;
		}
		else if ( name.equals( "Age" ) )
		{
			return DataType.INTEGER_TYPE;
		}
		else if ( name.equals( "Class" ) )
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
			return new Integer( IDCol[ptr] );
		}
		else if ( fieldIndex == 1 )
		{
			return NameCol[ptr];
		}
		else if ( fieldIndex == 2 )
		{
			return AgeCol[ptr];
		}
		else if ( fieldIndex == 3 )
		{
			return new Integer( ClassCol[ptr] );
		}
		return null;
	}

	public boolean next( ) throws BirtException
	{
		ptr++;
		if ( ptr >= NameCol.length )
		{
			return false;
		}
		return true;
	}
}

class ThreeLevelDataset implements IDatasetIterator
{

	int ptr = -1;
	static int[] Level1DimRange = {
			0, 4, 5, 11, 12, 13
	};

	static int[] Level2DimRange = {
			0, 2, 3, 4, 5, 6, 7, 9, 10, 11, 12, 12, 13, 13
	};

	static String[] Level1Col = {
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
	static String[] Level2Col = {
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
	static int[] Level2Index = {
			0, 0, 0, 1, 1, 2, 2, 3, 3, 3, 4, 4, 5, 6
	};
	static String[] Level3Col = {
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

	static int[] Level3AttributeCol = {
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
		if ( name.equals( "level1" ) )
		{
			return 0;
		}
		else if ( name.equals( "level2" ) )
		{
			return 1;
		}
		else if ( name.equals( "level3" ) )
		{
			return 2;
		}
		else if ( name.equals( "level3Attribute" ) )
		{
			return 3;
		}
		return -1;
	}

	public int getFieldType( String name ) throws BirtException
	{
		if ( name.equals( "level1" ) )
		{
			return DataType.STRING_TYPE;
		}
		else if ( name.equals( "level2" ) )
		{
			return DataType.STRING_TYPE;
		}
		else if ( name.equals( "level3" ) )
		{
			return DataType.STRING_TYPE;
		}
		else if ( name.equals( "level3Attribute" ) )
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
			return Level1Col[ptr];
		}
		else if ( fieldIndex == 1 )
		{
			return Level2Col[ptr];
		}
		else if ( fieldIndex == 2 )
		{
			return Level3Col[ptr];
		}
		else if ( fieldIndex == 3 )
		{
			return new Integer( Level3AttributeCol[ptr] );
		}
		return null;
	}

	public boolean next( ) throws BirtException
	{
		ptr++;
		if ( ptr >= Level1Col.length )
		{
			return false;
		}
		return true;
	}
}

class FourLevelDataset implements IDatasetIterator
{

	int ptr = -1;
	static int[] Level1DimRange = {
			0, 4, 5, 11, 12, 13
	};

	static int[] Level2DimRange = {
			0, 2, 3, 4, 5, 6, 7, 9, 10, 11, 12, 12, 13, 13
	};

	static String[] Level1Col = {
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
			"3",
			"3"
	};
	static String[] Level2Col = {
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
			"32",
			"32"
	};
	static int[] Level2Index = {
			0, 0, 0, 1, 1, 2, 2, 3, 3, 3, 4, 4, 5, 6
	};
	static String[] Level3Col = {
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
			"321",
			"321"
	};

	static String[] Level4Col = {
			"1111",
			"1121",
			"1131",
			"1211",
			"1221",
			"2111",
			"2121",
			"2211",
			"2221",
			"2231",
			"2311",
			"2321",
			"3111",
			"3211",
			"3212"
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
		if ( name.equals( "level1" ) )
		{
			return 0;
		}
		else if ( name.equals( "level2" ) )
		{
			return 1;
		}
		else if ( name.equals( "level3" ) )
		{
			return 2;
		}
		else if ( name.equals( "level4" ) )
		{
			return 3;
		}
		return -1;
	}

	public int getFieldType( String name ) throws BirtException
	{
		if ( name.equals( "level1" ) )
		{
			return DataType.STRING_TYPE;
		}
		else if ( name.equals( "level2" ) )
		{
			return DataType.STRING_TYPE;
		}
		else if ( name.equals( "level3" ) )
		{
			return DataType.STRING_TYPE;
		}
		else if ( name.equals( "level4" ) )
		{
			return DataType.STRING_TYPE;
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
			return Level1Col[ptr];
		}
		else if ( fieldIndex == 1 )
		{
			return Level2Col[ptr];
		}
		else if ( fieldIndex == 2 )
		{
			return Level3Col[ptr];
		}
		else if ( fieldIndex == 3 )
		{
			return Level4Col[ptr];
		}
		return null;
	}

	public boolean next( ) throws BirtException
	{
		ptr++;
		if ( ptr >= Level1Col.length )
		{
			return false;
		}
		return true;
	}
}

class IndexRange
{

	int start;
	int end;
}


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
import java.util.Date;

import junit.framework.TestCase;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.olap.api.cube.IDatasetIterator;
import org.eclipse.birt.data.engine.olap.api.cube.IDimension;
import org.eclipse.birt.data.engine.olap.api.cube.IHierarchy;
import org.eclipse.birt.data.engine.olap.api.cube.ILevelDefn;
import org.eclipse.birt.data.engine.olap.data.api.ILevel;
import org.eclipse.birt.data.engine.olap.data.api.ISelection;
import org.eclipse.birt.data.engine.olap.data.document.DocumentManagerFactory;
import org.eclipse.birt.data.engine.olap.data.document.IDocumentManager;
import org.eclipse.birt.data.engine.olap.data.impl.SelectionFactory;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Dimension;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.DimensionFactory;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.DimensionResultIterator;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Level;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.LevelDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Member;
import org.eclipse.birt.data.engine.olap.data.util.DataType;
import org.eclipse.birt.data.engine.olap.data.util.IDiskArray;
import org.eclipse.birt.data.engine.olap.data.util.IndexKey;


/**
 * 
 */

public class DimensionTest2 extends TestCase
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
	
	/**
	 * 
	 * @throws IOException
	 * @throws BirtException
	 */
	public void testDimensionCreateAndFind( ) throws IOException, BirtException
	{
		IDocumentManager documentManager = DocumentManagerFactory.createFileDocumentManager( );
		Dimension dimension = createDimension( documentManager ) ;
		ILevel[] level = dimension.getHierarchy( ).getLevels( );
		
		IDiskArray indexKeys = dimension.find( (Level) level[0],
				 new Object[]{new Integer( 1 )} );
		assertEquals( indexKeys.size( ), 4 );
		IndexKey indexKey;
		Member levelMember;
		for ( int i = 0; i < 4; i++ )
		{
			indexKey = (IndexKey) indexKeys.get( i );
			assertEquals( indexKey.key[0], new Integer( 1 ) );
			assertEquals( indexKey.dimensionPos, i );
			levelMember = dimension.getRowByPosition( indexKey.dimensionPos ).members[0];
			assertEquals( levelMember.keyValues[0], new Integer( 1 ) );

			levelMember = dimension.getDimensionRowByOffset( indexKey.offset ).members[0];
			assertEquals( levelMember.keyValues[0], new Integer( 1 ) );
		}
		
		// test load dimension from disk
		dimension = (Dimension)DimensionFactory.loadDimension( "student", documentManager );
		
		indexKeys = dimension.find( (Level) level[1],
				 new Object[]{new Integer( 1 )} );
		assertEquals( indexKeys.size( ), 4 );
		
		indexKey = (IndexKey)indexKeys.get( 0 );
		assertEquals( indexKey.key[0], new Integer( 1 ) );
		assertEquals( indexKey.dimensionPos, 0 );

		levelMember = dimension.getDimensionRowByOffset(
				indexKey.offset ).members[1];
		assertEquals( levelMember.keyValues[0], new Integer( 1 ) );
		
		indexKey = (IndexKey)indexKeys.get( 1 );
		assertEquals( indexKey.key[0], new Integer( 1 ) );
		assertEquals( indexKey.dimensionPos, 1 );

		levelMember = dimension.getDimensionRowByOffset(
				indexKey.offset ).members[1];
		assertEquals( levelMember.keyValues[0], new Integer( 1 ) );
		
		indexKey = (IndexKey)indexKeys.get( 2 );
		assertEquals( indexKey.key[0], new Integer( 1 ) );
		assertEquals( indexKey.dimensionPos, 4 );

		levelMember = dimension.getDimensionRowByOffset(
				indexKey.offset ).members[1];
		assertEquals( levelMember.keyValues[0], new Integer( 1 ) );
		
		indexKey = (IndexKey)indexKeys.get( 3 );
		assertEquals( indexKey.key[0], new Integer( 1 ) );
		assertEquals( indexKey.dimensionPos, 5 );

		levelMember = dimension.getDimensionRowByOffset(
				indexKey.offset ).members[1];
		assertEquals( levelMember.keyValues[0], new Integer( 1 ) );
		
	}
	
	/**
	 * 
	 * @throws IOException
	 * @throws BirtException
	 */
	public void testDimensionIterator( ) throws IOException, BirtException
	{
		IDocumentManager documentManager = DocumentManagerFactory.createFileDocumentManager( );
		Dimension dimension = createDimension( documentManager ) ;
		ILevel[] level = dimension.getHierarchy( ).getLevels( );
		
		ISelection[][] filter = new ISelection[1][1];
		filter[0][0] = SelectionFactory.createRangeSelection(  new Object[]{new Integer( 1 )},
				 new Object[]{new Integer( 2 )},
				true,
				false );
		Level[] findLevel = new Level[1];
		findLevel[0] = (Level)level[1];
		
		IDiskArray positionArray = dimension.find( findLevel,
				filter );
		assertEquals( positionArray.size( ), 4 );
		String[] levelNames = new String[1];
		levelNames[0] = "l2";
		DimensionResultIterator dimesionResultSet = new DimensionResultIterator( dimension,
				positionArray,
				levelNames);
		assertEquals(dimesionResultSet.getLevelIndex( "l2" ), 1 );
		assertEquals(dimesionResultSet.getLevelIndex( "l1" ), 0 );
		assertEquals(dimesionResultSet.getLevelKeyDataType( "l2" )[0], DataType.INTEGER_TYPE );
		assertEquals(dimesionResultSet.getLevelKeyDataType( "l1" )[0], DataType.INTEGER_TYPE );
		assertEquals(dimesionResultSet.length( ), 4 );
		dimesionResultSet.seek( 0 );
		assertEquals(dimesionResultSet.getLevelKeyValue( 0 )[0], new Integer(1) );
		assertEquals(dimesionResultSet.getLevelKeyValue( 1 )[0], new Integer(1) );
		assertEquals(dimesionResultSet.getLevelKeyValue( 2 )[0], new Integer(1) );
		dimesionResultSet.seek( 1 );
		assertEquals(dimesionResultSet.getLevelKeyValue( 0 )[0], new Integer(1) );
		assertEquals(dimesionResultSet.getLevelKeyValue( 1 )[0], new Integer(1) );
		assertEquals(dimesionResultSet.getLevelKeyValue( 2 )[0], new Integer(2) );
		dimesionResultSet.seek( 2 );
		assertEquals(dimesionResultSet.getLevelKeyValue( 0 )[0], new Integer(2) );
		assertEquals(dimesionResultSet.getLevelKeyValue( 1 )[0], new Integer(1) );
		assertEquals(dimesionResultSet.getLevelKeyValue( 2 )[0], new Integer(1) );
		dimesionResultSet.seek( 3 );
		assertEquals(dimesionResultSet.getLevelKeyValue( 0 )[0], new Integer(2) );
		assertEquals(dimesionResultSet.getLevelKeyValue( 1 )[0], new Integer(1) );
		assertEquals(dimesionResultSet.getLevelKeyValue( 2 )[0], new Integer(2) );
	}
	
	private Dimension createDimension( IDocumentManager documentManager )
			throws IOException, BirtException
	{
		ILevelDefn[] levelDefs = new ILevelDefn[3];
		levelDefs[0] = new LevelDefinition( "l1", new String[]{"l1"}, null );
		levelDefs[1] = new LevelDefinition( "l2", new String[]{"l2"}, null );
		levelDefs[2] = new LevelDefinition( "l3", new String[]{"l3"}, null );

		IDimension dimension = DimensionFactory.createDimension( "student",
				documentManager,
				new Dataset1( ),
				levelDefs,
				true );
		assertEquals( dimension.isTime( ), true );
		IHierarchy hierarchy = dimension.getHierarchy( );
		assertEquals( hierarchy.getName( ), "student" );
		ILevel[] level = hierarchy.getLevels( );
		assertEquals( level[0].getName( ), "l1" );
		assertEquals( level[1].getName( ), "l2" );
		assertEquals( level[2].getName( ), "l3" );
		return (Dimension) dimension;
	}
}

class Dataset1 implements IDatasetIterator
{

	int ptr = -1;
	static int[] L1Col = {
			1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3
	};
	static int[] L2Col = {
			1, 1, 2, 2, 1, 1, 2, 2, 2, 2, 3, 3
	};

	static int[] L3Col = {
			1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2
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
		if ( name.equals( "l1" ) )
		{
			return 0;
		}
		else if ( name.equals( "l2" ) )
		{
			return 1;
		}
		else if ( name.equals( "l3" ) )
		{
			return 2;
		}
		return -1;
	}

	public int getFieldType( String name ) throws BirtException
	{
		if ( name.equals( "l1" ) )
		{
			return DataType.INTEGER_TYPE;
		}
		else if ( name.equals( "l2" ) )
		{
			return DataType.INTEGER_TYPE;
		}
		else if ( name.equals( "l3" ) )
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
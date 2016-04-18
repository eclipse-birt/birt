/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.btree;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;

public class BTreeCursorTest
{
	@Test
    public void testCursor( ) throws Exception
	{
		new File( "./utest/btree.dat" ).delete( );
		FileBTreeFile file = new FileBTreeFile( "./utest/btree.dat" );
		try
		{
			BTreeOption<Integer, String> option = new BTreeOption<Integer, String>( );
			option.setFile( file, true );
			BTree<Integer, String> btree = new BTree<Integer, String>( option );

			try
			{
				BTreeCursor<Integer, String> cursor = btree.createCursor( );
				try
				{
					// test empty cursor
					doTestEmptyCursor( cursor );
					doTestInsert( cursor );
				}
				finally
				{
					cursor.close( );
				}
			}
			finally
			{
				btree.close( );
			}

			btree = new BTree<Integer, String>( option );
			try
			{
				BTreeCursor<Integer, String> cursor = btree.createCursor( );
				try
				{
					doTestPrev( cursor );
					doTestNext( cursor );
					doTestFirstLast( cursor );
					doTestMove( cursor );
				}
				finally
				{
					cursor.close( );
				}
			}
			finally
			{
				btree.close( );
			}
		}
		finally
		{
			file.close( );
		}
	}

	public void doTestNext( BTreeCursor<Integer, String> cursor )
			throws IOException
	{
		// first is the before last
		int rowCount = 0;
		assertTrue( cursor.isBeforeFirst( ) );
		assertFalse( cursor.isAfterLast( ) );
		while ( cursor.next( ) )
		{
			rowCount++;
		}
		assertTrue( cursor.isAfterLast( ) );
		assertFalse( cursor.isBeforeFirst( ) );
		assertEquals( rowCount, 10001 );
	}

	public void doTestPrev( BTreeCursor<Integer, String> cursor )
			throws IOException
	{
		// first is the before last
		int rowCount = 0;
		cursor.last( );
		cursor.next( );
		assertTrue( cursor.isAfterLast( ) );
		assertFalse( cursor.isBeforeFirst( ) );
		while ( cursor.previous( ) )
		{
			rowCount++;
		}
		assertTrue( cursor.isBeforeFirst( ) );
		assertFalse( cursor.isAfterLast( ) );
		assertEquals( rowCount, 10001 );
	}

	public void doTestFirstLast( BTreeCursor<Integer, String> cursor )
			throws IOException
	{
		// goto first
		int rowCount = 0;
		if ( cursor.first( ) )
		{
			do
			{
				assertEquals( new Integer( rowCount ), cursor.getKey( ) );
				rowCount++;
			} while ( cursor.next( ) );
			assertTrue( cursor.isAfterLast( ) );
			assertEquals( 10001, rowCount );
		}

		if ( cursor.last( ) )
		{
			do
			{
				rowCount--;
				assertEquals( new Integer( rowCount ), cursor.getKey( ) );
			} while ( cursor.previous( ) );
			assertTrue( cursor.isBeforeFirst( ) );
			assertEquals( 0, rowCount );
		}
	}

	public void doTestMove( BTreeCursor<Integer, String> cursor )
			throws IOException
	{

		// has the value
		assertTrue( cursor.moveTo( 6 ) );
		assertEquals( new Integer( 6 ), cursor.getKey( ) );

		// move to the last value
		assertFalse( cursor.moveTo( 10001 ) );
		assertEquals( new Integer( 10000 ), cursor.getKey( ) );

		// move to the first
		assertFalse( cursor.moveTo( -1 ) );
		assertTrue( cursor.isBeforeFirst( ) );
	}

	public void doTestEmptyCursor( BTreeCursor<Integer, String> cursor )
			throws IOException
	{
		assertTrue( cursor.isBeforeFirst( ) );
		assertFalse( cursor.next( ) );
		assertTrue( cursor.isAfterLast( ) );
		assertFalse( cursor.previous( ) );
		assertTrue( cursor.isBeforeFirst( ) );
		assertFalse( cursor.moveTo( 3 ) );
		assertTrue( cursor.isBeforeFirst( ) );
	}

	protected void doTestInsert( BTreeCursor<Integer, String> cursor )
			throws IOException
	{
		//
		cursor.insert( 10000, String.valueOf( 10000 ) );
		cursor.insert( 0, String.valueOf( 0 ) );
		for ( int i = 9999; i >= 1; i-- )
		{
			cursor.insert( new Integer( i ), String.valueOf( i ) );
			assertEquals( String.valueOf( i ), cursor.getValue( ) );
		}
	}
}

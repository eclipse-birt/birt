/*******************************************************************************
 * Copyright (c) 2008,2010 Actuate Corporation.
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;

import org.junit.Ignore;
import org.junit.Test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;

public class BTreeTest extends BTreeTestCase
{
	@Test
    public void testBTree( ) throws Exception
	{
		RAMBTreeFile file = new RAMBTreeFile( );
		BTreeOption<Integer, Object> option = new BTreeOption<Integer, Object>( );
		option.setHasValue( false );
		option.setKeySize( 4 );
		option.setKeySerializer( new IntegerSerializer( ) );
		option.setFile( file );

		BTree<Integer, Object> btree = new BTree<Integer, Object>( option );
		for ( int i = 0; i < 10000; i++ )
		{
			if ( !btree.exist( Integer.valueOf( i ) ) )
			{
				btree.insert( Integer.valueOf( i ), null );
			}
		}
		assertEquals( 10000, btree.getTotalKeys( ) );
		assertEquals( 0, btree.getTotalValues( ) );

		for ( int i = 0; i < 10000; i++ )
		{
			assertTrue( btree.exist( Integer.valueOf( i ) ) );
		}
		assertTrue( !btree.exist( Integer.valueOf( 10001 ) ) );
		assertTrue( !btree.exist( Integer.valueOf( -1 ) ) );
		btree.close( );

		// re-open the btree and test it is correct

		btree = new BTree<Integer, Object>( option );
		assertEquals( 10000, btree.getTotalKeys( ) );
		assertEquals( 0, btree.getTotalValues( ) );
		for ( int i = 0; i < 10000; i++ )
		{
			assertTrue( btree.exist( Integer.valueOf( i ) ) );
		}
		assertTrue( !btree.exist( Integer.valueOf( 10001 ) ) );
		assertTrue( !btree.exist( Integer.valueOf( -1 ) ) );
		btree.close( );
	}
	@Test
    public void testFixKey( ) throws Exception
	{
		RAMBTreeFile file = new RAMBTreeFile( );
		BTreeOption<Integer, Object> option = new BTreeOption<Integer, Object>( );
		option.setHasValue( false );
		option.setKeySize( 4 );
		option.setKeySerializer( new IntegerSerializer( ) );
		option.setFile( file );
		BTree<Integer, Object> btree = new BTree<Integer, Object>( option );

		Collection<String> input = createSampleInput( );
		HashSet<String> keys = new HashSet<String>( );
		for ( String key : input )
		{
			keys.add( key );
			btree.insert( Integer.valueOf( key ), null );
		}

		assertEquals( keys.size( ), btree.getTotalKeys( ) );
		for ( String key : keys )
		{
			assertTrue( btree.exist( Integer.valueOf( key ) ) );
		}
		btree.close( );

		btree = new BTree<Integer, Object>( option );
		assertEquals( keys.size( ), btree.getTotalKeys( ) );
		for ( String key : keys )
		{
			assertTrue( btree.exist( Integer.valueOf( key ) ) );
		}
		btree.close( );
	}
	@Test
    public void testFixValue( ) throws Exception
	{
		RAMBTreeFile file = new RAMBTreeFile( );
		BTreeOption<String, Integer> option = new BTreeOption<String, Integer>( );
		option.setHasValue( true );
		option.setValueSize( 4 );
		option.setValueSerializer( new IntegerSerializer( ) );
		option.setFile( file );
		BTree<String, Integer> btree = new BTree<String, Integer>( option );

		Collection<String> input = createSampleInput( );
		HashMap<String, Integer> values = new HashMap<String, Integer>( );
		for ( String key : input )
		{
			Integer value = Integer.valueOf( key );
			values.put( key, value );
			btree.insert( key, value );
		}

		assertEquals( values.size( ), btree.getTotalKeys( ) );
		assertEquals( values.size( ), btree.getTotalValues( ) );
		for ( Map.Entry<String, Integer> entry : values.entrySet( ) )
		{
			Integer value = btree.getValue( entry.getKey( ) );
			assertEquals( entry.getValue( ), value );
		}
		btree.close( );

		btree = new BTree<String, Integer>( option );
		assertEquals( values.size( ), btree.getTotalKeys( ) );
		assertEquals( values.size( ), btree.getTotalValues( ) );
		for ( Map.Entry<String, Integer> entry : values.entrySet( ) )
		{
			Integer value = btree.getValue( entry.getKey( ) );
			assertEquals( entry.getValue( ), value );
		}
		btree.close( );

	}
	@Test
    public void testDuplicate( ) throws Exception
	{
		new File( "./utest/btree.dat" ).delete( );
		FileBTreeFile file = new FileBTreeFile( "./utest/btree.dat" );
		try
		{
			BTreeOption<String, Integer> option = new BTreeOption<String, Integer>( );
			option.setHasValue( true );
			option.setAllowDuplicate( true );
			option.setFile( file, true );
			BTree<String, Integer> btree = new BTree<String, Integer>( option );

			Collection<String> input = createSampleInput( );
			HashMap<String, ArrayList<Integer>> map = new HashMap<String, ArrayList<Integer>>( );
			int totalValues = 0;
			for ( String key : input )
			{
				Integer value = Integer.valueOf( totalValues );
				ArrayList<Integer> values = map.get( key );
				if ( values == null )
				{
					values = new ArrayList<Integer>( );
					map.put( key, values );
				}
				values.add( value );
				btree.insert( key, value );
				totalValues++;
			}

			assertEquals( map.size( ), btree.getTotalKeys( ) );
			assertEquals( totalValues, btree.getTotalValues( ) );
			for ( Map.Entry<String, ArrayList<Integer>> entry : map.entrySet( ) )
			{
				Collection<Integer> values1 = btree.getValues( entry.getKey( ) );
				Collection<Integer> values2 = entry.getValue( );
				assertEquals( values2.size( ), values1.size( ) );
				assertTrue( values1.containsAll( values2 ) );
			}
			btree.close( );

			btree = new BTree<String, Integer>( option );
			assertEquals( map.size( ), btree.getTotalKeys( ) );
			assertEquals( totalValues, btree.getTotalValues( ) );
			for ( Map.Entry<String, ArrayList<Integer>> entry : map.entrySet( ) )
			{
				Collection<Integer> values1 = btree.getValues( entry.getKey( ) );
				Collection<Integer> values2 = entry.getValue( );

				assertEquals( values2.size( ), values1.size( ) );
				assertTrue( values1.containsAll( values2 ) );
			}
			btree.close( );

		}
		finally
		{
			file.close( );
		}
	}
	@Test
    public void testHugeKey( ) throws Exception
	{
		new File( "./utest/btree.dat" ).delete( );
		FileBTreeFile file = new FileBTreeFile( "./utest/btree.dat" );
		try
		{
			BTreeOption<String, Object> option = new BTreeOption<String, Object>( );
			option.setHasValue( false );
			option.setFile( file, true );
			BTree<String, Object> btree = new BTree<String, Object>( option );

			Random random = new Random( );
			ArrayList<Integer> input = new ArrayList<Integer>( );
			for ( int i = 0; i < 10000; i++ )
			{
				int value = random.nextInt( 40 ) + 1;
				char[] chars = new char[value * 1024];
				Arrays.fill( chars, 'a' );
				String key = new String( chars );
				if ( !btree.exist( key ) )
				{
					btree.insert( key, null );
					input.add( Integer.valueOf( value ) );
				}
			}

			assertEquals( input.size( ), btree.getTotalKeys( ) );
			for ( Integer value : input )
			{
				char[] chars = new char[value.intValue( ) * 1024];
				Arrays.fill( chars, 'a' );
				String key = new String( chars );
				assertTrue( btree.exist( key ) );
			}

			btree.close( );

			btree = new BTree<String, Object>( option );
			assertEquals( input.size( ), btree.getTotalKeys( ) );
			for ( Integer value : input )
			{
				char[] chars = new char[value.intValue( ) * 1024];
				Arrays.fill( chars, 'a' );
				String key = new String( chars );
				assertTrue( btree.exist( key ) );
			}

		}
		finally
		{
			file.close( );
		}

	}
	@Test
    public void testHugeValue( ) throws Exception
	{
		new File( "./utest/btree.dat" ).delete( );
		FileBTreeFile file = new FileBTreeFile( "./utest/btree.dat" );
		try
		{
			BTreeOption<Integer, char[]> option = new BTreeOption<Integer, char[]>( );
			option.setHasValue( true );
			option.setAllowDuplicate( false );
			option.setFile( file, true );
			BTree<Integer, char[]> btree = new BTree<Integer, char[]>( option );

			Random random = new Random( );
			ArrayList<Integer> input = new ArrayList<Integer>( );
			for ( int i = 0; i < 10000; i++ )
			{
				int v = random.nextInt( 40 ) + 1;
				Integer key = Integer.valueOf( v );
				if ( !btree.exist( key ) )
				{
					char[] value = new char[v * 1023];
					Arrays.fill( value, 'a' );
					btree.insert( key, value );
					input.add( Integer.valueOf( v ) );
				}
			}

			assertEquals( input.size( ), btree.getTotalKeys( ) );
			assertEquals( input.size( ), btree.getTotalValues( ) );
			for ( Integer key : input )
			{
				char[] value = btree.getValue( key );
				assertEquals( key, Integer.valueOf( value.length / 1023 ) );
			}

			btree.close( );

			btree = new BTree<Integer, char[]>( option );
			assertEquals( input.size( ), btree.getTotalKeys( ) );
			assertEquals( input.size( ), btree.getTotalValues( ) );
			for ( Integer key : input )
			{
				char[] value = btree.getValue( key );
				assertEquals( key, Integer.valueOf( value.length / 1023 ) );
			}
		}
		finally
		{
			file.close( );
		}
	}

	private String createTestKey( int value, int entryCount )
	{
		int a = value % 3;
		int b = value / 3;
		switch ( a )
		{
			case 0 :
				return String.valueOf( b );
			case 1 :
				return String.valueOf( ( entryCount / 3 ) + b );
			default :
				return String.valueOf( ( entryCount / 3 * 2 ) + b );
		}
	}

	@Ignore("long run test")
	@Test
    public void testHugeNumberOfEntries( ) throws Exception
	{
		int ENTRY_COUNT = 999999; // 1M
		new File( "./utest/btree.dat" ).delete( );
		FileBTreeFile file = new FileBTreeFile( "./utest/btree.dat" );
		try
		{
			BTreeOption<String, String> option = new BTreeOption<String, String>( );
			option.setHasValue( true );
			option.setFile( file, true );
			option.setCacheSize( 1024 );
			BTree<String, String> btree = new BTree<String, String>( option );

			long start = System.currentTimeMillis( );
			System.out.println( "INSERT 1M entries...." );
			for ( int i = 0; i < ENTRY_COUNT; i++ )
			{
				String key = createTestKey( i, ENTRY_COUNT );
				btree.insert( key, key );
				if ( i % 10000 == 0 )
				{
					System.out.print( '.' );
				}
			}
			btree.close( );
			long end = System.currentTimeMillis( );
			System.out.println( "FINISHED at " + ( end - start ) + "ms" );

			start = System.currentTimeMillis( );
			System.out.println( "QURRY 1M entries...." );
			btree = new BTree<String, String>( option );
			assertEquals( ENTRY_COUNT, btree.getTotalKeys( ) );
			assertEquals( ENTRY_COUNT, btree.getTotalValues( ) );
			for ( int i = 0; i < ENTRY_COUNT; i++ )
			{
				String key = String.valueOf( i );
				String value = btree.getValue( key );
				assertEquals( key, value );
				if ( i % 10000 == 0 )
				{
					System.out.print( '.' );
				}
			}
			end = System.currentTimeMillis( );
			System.out.println( "FINISHED at " + ( end - start ) + "ms" );
			btree.close( );
		}
		finally
		{
			file.close( );
		}

	}
	@Test
    public void testNullKeyValue( ) throws IOException
	{
		RAMBTreeFile file = new RAMBTreeFile( );
		BTreeOption<Integer, String> option = new BTreeOption<Integer, String>( );
		option.setHasValue( true );
		//option.setKeySize( 4 );
		option.setKeySerializer( new IntegerSerializer( ) );
		option.setFile( file );
		option.setAllowNullKey( true );

		BTree<Integer, String> btree = new BTree<Integer, String>( option );
		for ( int i = 0; i < 10000; i++ )
		{
			if ( !btree.exist( Integer.valueOf( i ) ) )
			{
				btree.insert( Integer.valueOf( i ), (String) null );
			}
		}
		btree.insert( null, "abc" );

		assertEquals( 10001, btree.getTotalKeys( ) );
		assertEquals( 10001, btree.getTotalValues( ) );

		btree.close( );
	}
	@Test
    public void testBatchInsert( ) throws IOException
	{
		RAMBTreeFile file = new RAMBTreeFile( );
		BTreeOption<Integer, String> option = new BTreeOption<Integer, String>( );
		option.setHasValue( true );
		option.setKeySize( 4 );
		option.setAllowDuplicate( true );
		option.setAllowNullKey( true );
		option.setKeySerializer( new IntegerSerializer( ) );
		option.setFile( file );

		BTree<Integer, String> btree = new BTree<Integer, String>( option );
		// insert null batches
		String[] values = new String[4];
		values[0] = null;
		for ( int i = 1; i < 4; i++ )
		{
			values[i] = "NULL" + i;
		}

		btree.insert( null, values );

		for ( int i = 0; i < 10000; i++ )
		{
			values[0] = null;
			for ( int j = 1; j < 4; j++ )
			{
				values[j] = i + "." + j;
			}
			btree.insert( Integer.valueOf( i ), values );
		}

		assertEquals( 10001, btree.getTotalKeys( ) );
		assertEquals( 40004, btree.getTotalValues( ) );

		btree.close( );
	}
}

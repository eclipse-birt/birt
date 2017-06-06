/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
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

import org.junit.Test;

import junit.framework.TestCase;

public class BTreeMultipleThreadTest extends TestCase
{

	static int KEY_COUNT = 10000;
	@Test
    public void testCursor( ) throws Exception
	{
		new File( "./utest/btree.dat" ).delete( );
		FileBTreeFile file = new FileBTreeFile( "./utest/btree.dat" );
		try
		{
			BTreeOption<String, String> option = new BTreeOption<String, String>( );
			option.setFile( file, true );
			BTree<String, String> btree = new BTree<String, String>( option );
			try
			{
				createBTree( btree );
				for ( int i = 0; i < 4; i++ )
				{
					new Thread( new TestThread( btree.createCursor( ) ) )
							.start( );
				}
				while ( TestThread.hasActiveThread( ) )
				{
					try
					{
						Thread.sleep( 200 );
					}
					catch ( Exception ex )
					{
					}
				}
			}
			finally
			{
				btree.close( );
			}

			if ( TestThread.hasErrors( ) )
			{
				TestThread.printErrors( );
				fail( "HAS ERROR!" );
			}
		}
		finally
		{
			file.close( );
		}
	}
	static boolean hasError;

	static void createBTree( BTree<String, String> btree ) throws IOException
	{
		for ( int i = 0; i < KEY_COUNT; i++ )
		{
			String value = String.valueOf( i );
			btree.insert( value, value );
		}
	}

	static class TestThread implements Runnable
	{

		static int threadCount;
		static ArrayList<Throwable> errors = new ArrayList<Throwable>( );

		static synchronized void increaseThreadCount( )
		{
			threadCount++;
		}

		static synchronized void decreaseThreadCount( )
		{
			threadCount--;
		}

		static synchronized boolean hasActiveThread( )
		{
			return threadCount > 0;
		}

		static boolean hasErrors( )
		{
			return !errors.isEmpty( );
		}

		synchronized static void throwError( Throwable ex )
		{
			errors.add( ex );
		}

		synchronized static void printErrors( ) throws Exception
		{
			for ( Throwable ex : errors )
			{
				ex.printStackTrace( );
			}
		}

		BTreeCursor<String, String> cursor;

		TestThread( BTreeCursor<String, String> cursor )
		{
			this.cursor = cursor;
		}

		public void run( )
		{
			increaseThreadCount( );
			try
			{
				// first is the before last
				int rowCount = 0;
				while ( cursor.next( ) )
				{
					String key = cursor.getKey( );
					String value = cursor.getValue( );
					if ( !( key.equals( value ) ) )
					{
						throw new IOException( key + " != " + value );
					}
					rowCount++;
				}
				if ( rowCount != KEY_COUNT )
				{
					throw new IOException( "KEY_COUNT INCORRECT" );
				}
			}
			catch ( Throwable ex )
			{
				throwError( ex );
			}
			finally
			{
				cursor.close( );

			}
			decreaseThreadCount( );
		}
	}

}

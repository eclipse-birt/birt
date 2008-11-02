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

import junit.framework.TestCase;

public class BTreeCursorTest extends TestCase
{

	public void testCursor( ) throws Exception
	{
		new File( "./utest/btree.dat" ).delete( );
		FileBTreeFile file = new FileBTreeFile( "./utest/btree.dat" );
		try
		{
			BTreeOption<Integer, String> option = new BTreeOption<Integer, String>( );
			option.setFile( file );
			BTree<Integer, String> btree = new BTree<Integer, String>( option );

			BTreeCursor<Integer, String> cursor = btree.createCursor( );

			for ( int i = 9999; i >= 0; i-- )
			{
				cursor.insert( new Integer( i ), String.valueOf( i ) );
				assertEquals( String.valueOf( i ), cursor.getValue( ) );
			}

			cursor.close( );
			btree.close( );

			btree = new BTree<Integer, String>( option );
			cursor = btree.createCursor( );
			boolean hasKey = cursor.moveTo( new Integer( 999 ) );
			int v = 999;
			while ( hasKey )
			{
				assertEquals( new Integer( v ), cursor.getKey( ) );
				assertEquals( String.valueOf( v ), cursor.getValue( ) );
				hasKey = cursor.previous( );
				v--;
			}

			hasKey = cursor.moveTo( new Integer( 0 ) );
			v = 0;
			while ( hasKey )
			{
				assertEquals( new Integer( v ), cursor.getKey( ) );
				assertEquals( String.valueOf( v ), cursor.getValue( ) );
				hasKey = cursor.next( );
				v++;
			}

			cursor.close( );

			btree.close( );
		}
		finally
		{
			file.close( );
		}
	}
}

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

abstract public class BTreeTestCase
{

	static final String BTREE_INPUT_RESOURCE = "org/eclipse/birt/core/btree/btree.input.txt";

	BTree createBTree( ) throws Exception
	{
		BTree btree = new BTree( );
		InputStream in = this.getClass( ).getClassLoader( )
				.getResourceAsStream( BTREE_INPUT_RESOURCE );
		try
		{
			BufferedReader reader = new BufferedReader( new InputStreamReader(
					in ) );
			String line = reader.readLine( );
			while ( line != null )
			{
				int value = Integer.parseInt( line );
				btree.insert( new Integer( value ), String.valueOf( value ) );
				line = reader.readLine( );
			}
		}
		finally
		{
			in.close( );
		}
		return btree;
	}

	Collection<String> createSampleInput( ) throws Exception
	{
		ArrayList<String> input = new ArrayList<String>( 10000 );
		Random random = new Random( );
		for ( int i = 0; i < 10000; i++ )
		{
			int value = random.nextInt( 500 );
			input.add( String.valueOf( value ) );
		}
		return input;
	}

	public static class IntegerSerializer implements BTreeSerializer<Integer>
	{

		public byte[] getBytes( Integer object ) throws IOException
		{
			byte[] bytes = new byte[4];
			BTreeUtils.integerToBytes( object.intValue( ), bytes );
			return bytes;
		}

		public Integer getObject( byte[] bytes ) throws IOException,
				ClassNotFoundException
		{
			return new Integer( BTreeUtils.bytesToInteger( bytes ) );
		}
	}
}

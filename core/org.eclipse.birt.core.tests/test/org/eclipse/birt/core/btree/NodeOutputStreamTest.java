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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.junit.Test;

import junit.framework.TestCase;

public class NodeOutputStreamTest extends TestCase
{
	@Test
    public void testEmptyOutputStream( ) throws IOException
	{
		RAMBTreeFile file = new RAMBTreeFile( );
		testOutputStream( file, new NodeOutputStream( file ) );
	}

	protected void testOutputStream( BTreeFile file, NodeOutputStream o )
			throws IOException
	{
		DataOutputStream out = new DataOutputStream( o );
		for ( int i = 0; i < 1022; i++ )
		{
			out.writeInt( i );
		}
		out.writeLong( 0x1234567887654321L );
		for ( int i = 0; i < 10; i++ )
		{
			out.writeUTF( String.valueOf( i ) );
		}
		o.close( );
		int[] blocks = o.getUsedBlocks( );
		assertEquals( 2, blocks.length );
		assertEquals( 0, blocks[0] );
		assertEquals( 1, blocks[1] );

		byte[] bytes = new byte[4096];
		file.readBlock( 0, bytes );
		DataInputStream in = new DataInputStream( new ByteArrayInputStream(
				bytes ) );
		assertEquals( 1, in.readInt( ) );
		for ( int i = 0; i < 1022; i++ )
		{
			assertEquals( i, in.readInt( ) );
		}
		assertEquals( 0x12345678, in.readInt( ) );
		file.readBlock( 1, bytes );
		in = new DataInputStream( new ByteArrayInputStream( bytes ) );
		assertEquals( -1, in.readInt( ) );
		assertEquals( 0x87654321, in.readInt( ) );
		for ( int i = 0; i < 10; i++ )
		{
			assertEquals( String.valueOf( i ), in.readUTF( ) );
		}
	}
	@Test
    public void testIncreaseOutputStream( ) throws IOException
	{
		RAMBTreeFile file = new RAMBTreeFile( );
		ByteArrayOutputStream buffer = new ByteArrayOutputStream( 4096 );
		DataOutputStream out = new DataOutputStream( buffer );
		out.writeInt( -1 );
		file.writeBlock( 0, buffer.toByteArray( ) );

		testOutputStream( file, new NodeOutputStream( file, new int[]{0} ) );
	}
	@Test
    public void testDecreaseOutputStream( ) throws IOException
	{
		RAMBTreeFile file = new RAMBTreeFile( );
		ByteArrayOutputStream buffer = new ByteArrayOutputStream( 4096 );
		DataOutputStream out = new DataOutputStream( buffer );
		out.writeInt( 1 );
		file.writeBlock( 0, buffer.toByteArray( ) );

		buffer.reset( );
		out = new DataOutputStream( buffer );
		out.writeInt( 2 );
		file.writeBlock( 1, buffer.toByteArray( ) );

		buffer.reset( );
		out = new DataOutputStream( buffer );
		out.writeInt( -1 );
		out = new DataOutputStream( buffer );
		file.writeBlock( 2, buffer.toByteArray( ) );

		testOutputStream( file, new NodeOutputStream( file, new int[]{0, 1, 2} ) );
	}

}

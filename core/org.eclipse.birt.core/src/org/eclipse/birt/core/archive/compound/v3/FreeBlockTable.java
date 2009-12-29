/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.archive.compound.v3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.LinkedList;

class FreeBlockTable implements Ext2Constants
{

	protected Ext2FileSystem fs;
	protected LinkedList<Ext2Node> freeNodes;
	protected Ext2Node freeNode;
	protected FreeBlockList freeBlockList;

	FreeBlockTable( Ext2FileSystem fs )
	{
		this.fs = fs;
		this.freeNodes = new LinkedList<Ext2Node>( );
	}

	public void read( ) throws IOException
	{
		freeNodes.clear( );
		byte[] buffer = new byte[Ext2Node.NODE_SIZE];
		Ext2File file = new Ext2File( fs, NodeTable.INODE_FREE_TABLE );
		try
		{
			int totalNode = (int) ( file.length( ) / Ext2Node.NODE_SIZE );
			for ( int i = 0; i < totalNode; i++ )
			{
				file.read( buffer, 0, buffer.length );
				Ext2Node freeNode = new Ext2Node( );
				freeNode.read( new DataInputStream( new ByteArrayInputStream(
						buffer ) ) );
				freeNodes.add( freeNode );
			}
		}
		finally
		{
			file.close( );
		}
	}

	public void write( ) throws IOException
	{
		if ( freeBlockList == null && freeNodes.isEmpty( ) )
		{
			return;
		}

		Ext2File file = new Ext2File( fs, NodeTable.INODE_FREE_TABLE );
		try
		{
			ByteArrayOutputStream buffer = new ByteArrayOutputStream(
					Ext2Node.NODE_SIZE );
			DataOutputStream out = new DataOutputStream( buffer );
			while ( !freeNodes.isEmpty( ) )
			{
				Ext2Node freeNode = freeNodes.removeFirst( );
				buffer.reset( );
				freeNode.write( out );
				file.write( buffer.toByteArray( ), 0, Ext2Node.NODE_SIZE );
			}
			if ( freeBlockList != null )
			{
				buffer.reset( );
				freeNode.write( out );
				file.write( buffer.toByteArray( ), 0, Ext2Node.NODE_SIZE );
			}
		}
		finally
		{
			file.close( );
		}
	}

	public int getFreeBlock( ) throws IOException
	{
		if ( freeBlockList != null )
		{
			int blockId = freeBlockList.removeLastBlock( );
			if ( blockId > 0 )
			{
				return blockId;
			}
			freeBlockList.clear( );
			freeBlockList = null;
			freeNode = null;
		}

		while ( !freeNodes.isEmpty( ) )
		{
			freeNode = freeNodes.removeLast( );
			freeBlockList = new FreeBlockList( fs, freeNode );
			int blockId = freeBlockList.removeLastBlock( );
			if ( blockId > 0 )
			{
				return blockId;
			}
			freeBlockList.clear( );
			freeBlockList = null;
			freeNode = null;
		}

		return -1;
	}

	public void addFreeBlocks( Ext2Node node )
	{
		freeNodes.add( node );
	}
}

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
import java.util.ArrayList;
import java.util.LinkedList;

public class NodeTable
{

	static final int INODE_NODE_TABLE = 0;
	static final int INODE_SYSTEM_HEAD = 1;
	static final int INODE_FREE_TABLE = 2;
	static final int INODE_ENTRY_TABLE = 3;

	Ext2FileSystem fs;

	ArrayList<Ext2Node> nodes = new ArrayList<Ext2Node>( 4 );
	LinkedList<Ext2Node> freeNodes = new LinkedList<Ext2Node>( );

	NodeTable( Ext2FileSystem fs )
	{
		this.fs = fs;

		Ext2Node tableNode = new Ext2Node( INODE_NODE_TABLE );
		tableNode.setDirectBlock( 0, 1 );
		tableNode.setBlockCount( 1 );
		tableNode.setStatus( Ext2Node.STATUS_USED );

		Ext2Node headNode = new Ext2Node( INODE_SYSTEM_HEAD );
		headNode.setDirectBlock( 0, 0 );
		headNode.setBlockCount( 1 );
		headNode.setStatus( Ext2Node.STATUS_USED );

		Ext2Node freeNode = new Ext2Node( INODE_ENTRY_TABLE );
		freeNode.setStatus( Ext2Node.STATUS_USED );

		Ext2Node entryNode = new Ext2Node( INODE_ENTRY_TABLE );
		entryNode.setStatus( Ext2Node.STATUS_USED );

		nodes.add( tableNode );
		nodes.add( headNode );
		nodes.add( freeNode );
		nodes.add( entryNode );
	}

	Ext2Node getNode( int id )
	{
		return nodes.get( id );
	}

	void read( ) throws IOException
	{
		nodes.clear( );
		byte[] buffer = new byte[Ext2Node.NODE_SIZE];
		fs.readBlock( 1, buffer, 0, Ext2Node.NODE_SIZE );
		Ext2Node node = new Ext2Node( 0 );
		readNode( node, buffer );
		nodes.add( node );
		Ext2File file = new Ext2File( fs, node );
		try
		{
			int totalNode = (int) ( file.length( ) / Ext2Node.NODE_SIZE );
			nodes.ensureCapacity( totalNode );
			file.seek( Ext2Node.NODE_SIZE );
			for ( int i = 1; i < totalNode; i++ )
			{
				file.read( buffer, 0, buffer.length );
				node = new Ext2Node( i );
				readNode( node, buffer );
				nodes.add( node );
				if ( node.getStatus( ) == Ext2Node.STATUS_UNUSED )
				{
					freeNodes.add( node );
				}
			}
		}
		finally
		{
			file.close( );
		}
	}

	private void readNode( Ext2Node node, byte[] bytes ) throws IOException
	{
		DataInputStream in = new DataInputStream( new ByteArrayInputStream(
				bytes ) );
		node.read( in );
	}

	void write( ) throws IOException
	{
		Ext2File file = new Ext2File( fs, nodes.get( INODE_NODE_TABLE ) );
		try
		{
			ByteArrayOutputStream buffer = new ByteArrayOutputStream(
					Ext2Node.NODE_SIZE );
			DataOutputStream out = new DataOutputStream( buffer );
			file.seek( Ext2Node.NODE_SIZE );
			for ( int i = 1; i < nodes.size( ); i++ )
			{
				buffer.reset( );
				nodes.get( i ).write( out );
				file.write( buffer.toByteArray( ), 0, Ext2Node.NODE_SIZE );
			}
			file.seek( 0 );
			buffer.reset( );
			nodes.get( 0 ).write( out );
			file.write( buffer.toByteArray( ), 0, Ext2Node.NODE_SIZE );
			file.setLength( nodes.size( ) * (long) Ext2Node.NODE_SIZE );
		}
		finally
		{
			file.close( );
		}
	}

	void write( int iNode ) throws IOException
	{
		assert iNode < nodes.size( );
		Ext2File file = new Ext2File( fs, nodes.get( INODE_NODE_TABLE ) );
		try
		{
			ByteArrayOutputStream buffer = new ByteArrayOutputStream(
					Ext2Node.NODE_SIZE );
			DataOutputStream out = new DataOutputStream( buffer );
			file.seek( Ext2Node.NODE_SIZE * iNode );
			nodes.get( iNode ).write( out );
			file.write( buffer.toByteArray( ), 0, Ext2Node.NODE_SIZE );
		}
		finally
		{
			file.close( );
		}
	}

	Ext2Node allocateNode( )
	{
		if ( !freeNodes.isEmpty( ) )
		{
			Ext2Node node = freeNodes.removeFirst( );
			node.setStatus( Ext2Node.STATUS_USED );
			return node;
		}
		Ext2Node node = new Ext2Node( nodes.size( ) );
		nodes.add( node );
		node.setStatus( Ext2Node.STATUS_USED );
		return node;
	}

	void releaseNode( int nodeId )
	{
		Ext2Node node = nodes.get( nodeId );
		Ext2Node freeNode = node.copyFreeNode( );
		fs.releaseFreeBlocks( freeNode );
		node.reset( );
		freeNodes.add( node );
	}
}

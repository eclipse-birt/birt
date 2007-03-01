/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.archive.compound;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Contains the report archive file 's head information.
 */
class ArchiveHeader implements ArchiveConstants
{
	protected ArchiveFileV2 af;
	/**
	 * the file status of the archive
	 */
	protected int fileStatus;

	/**
	 * Constructor
	 * 
	 * @param fs
	 *            the corresponding compound file system
	 */
	private ArchiveHeader( ArchiveFileV2 af )
	{
		this.af = af;
		this.fileStatus = 0;
	}

	static ArchiveHeader createHeader( ArchiveFileV2 af ) throws IOException
	{
		ArchiveHeader header = new ArchiveHeader( af );
		return header;
	}

	static ArchiveHeader loadHeader( ArchiveFileV2 af ) throws IOException
	{
		ArchiveHeader header = new ArchiveHeader( af );
		header.refresh( );
		return header;
	}

	int getStatus( )
	{
		return fileStatus;
	}

	void setStatus( int status )
	{
		this.fileStatus = status;
	}

	/**
	 * Read the header information from disk.
	 * 
	 * @throws IOException
	 */
	void refresh( ) throws IOException
	{
		byte[] b = new byte[Block.BLOCK_SIZE];
		af.read( 0, 0, b, 0, Block.BLOCK_SIZE );

		DataInputStream in = new DataInputStream( new ByteArrayInputStream( b ) );
		long magicTag = in.readLong( );
		if ( magicTag != DOCUMENT_TAG )
		{
			throw new IOException( "Not a compound file, the magic code is "
					+ magicTag );
		}
		long version = in.readLong( );
		if ( version != DOCUMENT_VERSION )
		{
			throw new IOException( "Unsupported compound archive version "
					+ DOCUMENT_VERSION );
		}
		fileStatus = in.readInt( );
	}

	/**
	 * Write header information to disk
	 * 
	 * @throws IOException
	 */
	void flush( ) throws IOException
	{
		ByteArrayOutputStream buffer = new ByteArrayOutputStream( );
		DataOutputStream out = new DataOutputStream( buffer );
		out.writeLong( DOCUMENT_TAG );
		out.writeLong( DOCUMENT_VERSION );
		out.writeInt( fileStatus );

		byte[] b = buffer.toByteArray( );
		af.write( 0, 0, b, 0, b.length );
	}
}

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
class ArchiveHeader
{

	/** The magic tag for Compound RDF File, the HEX of RPTARC */
	static final long MAGIC_TAG = 0x525054414243L;

	/** The file format version */
	static final long VERSION_0 = 0;

	static final int HEADER_BLOCK = 0;

	static final int FILE_STATUS_OFFSET = 16;

	protected ArchiveFile af;
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
	private ArchiveHeader( ArchiveFile af )
	{
		this.af = af;
		this.fileStatus = 0;
	}

	static ArchiveHeader createHeader( ArchiveFile af ) throws IOException
	{
		ArchiveHeader header = new ArchiveHeader( af );
		header.flush( );
		return header;
	}

	static ArchiveHeader loadHeader( ArchiveFile af ) throws IOException
	{
		ArchiveHeader header = new ArchiveHeader( af );
		header.refresh( );
		return header;

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
		if ( magicTag != MAGIC_TAG )
		{
			throw new IOException( "Not a compound file, the magic code is "
					+ magicTag );
		}
		long version = in.readLong( );
		if ( version != VERSION_0 )
		{
			throw new IOException( "Unsupported compound archive version "
					+ VERSION_0 );
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
		out.writeLong( MAGIC_TAG );
		out.writeLong( VERSION_0 );
		out.writeInt( fileStatus );

		byte[] b = buffer.toByteArray( );
		af.write( 0, 0, b, 0, b.length );
	}
}

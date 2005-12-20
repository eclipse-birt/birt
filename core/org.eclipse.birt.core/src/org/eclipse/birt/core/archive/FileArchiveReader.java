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

package org.eclipse.birt.core.archive;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FileArchiveReader implements IDocArchiveReader
{

	private String fileName;
	private RandomAccessFile file = null;
	private HashMap lookupMap = new HashMap( );

	/**
	 * @param absolute
	 *            fileName the name of the archive
	 */
	public FileArchiveReader( String fileName ) throws IOException
	{
		if ( fileName == null ||
			 fileName.length() == 0 )
			throw new IOException("The file name is null or empty string.");

		File fd = new File( fileName );
		fileName = fd.getCanonicalPath( ); // make sure the file name is an absolute path
		this.fileName = fileName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.core.archive.IDocArchiveReader#getName()
	 */
	public String getName( )
	{
		return fileName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.core.archive.IDocArchiveReader#open()
	 */
	public void open( ) throws IOException
	{
		if ( file != null )
		{
			// has been opend
			return;
		}
		
		// restore the in-memory lookup map
		file = new RandomAccessFile( new File( fileName ), "r" ); //$NON-NLS-1$

		long streamSectionPos = file.readLong( );
		long entryNumber = file.readLong( );

		// read lookup map
		for ( long i = 0; i < entryNumber; i++ )
		{
			String relativeFilePath = file.readUTF( );
			long[] positionAndLength = new long[2];
			// stream position (and convert it to absolute position)
			positionAndLength[0] = file.readLong( ) + streamSectionPos; 
			 // stream length
			positionAndLength[1] = file.readLong( );
			// generate map entry
			lookupMap.put( relativeFilePath, positionAndLength ); 
		}
	}

	public void close( ) throws IOException
	{
		if ( file != null )
		{
			file.close( );
			file = null;
		}
	}

	public RAInputStream getStream( String relativePath ) throws IOException
	{
		if ( !relativePath.startsWith( ArchiveUtil.UNIX_SEPERATOR ) )
			relativePath = ArchiveUtil.UNIX_SEPERATOR + relativePath;

		Object entryValue = lookupMap.get( relativePath );
		if ( entryValue == null )
			return null; // File doesn't exist

		long[] positionAndLength = (long[]) entryValue;

		RAFileInputStream inputStream = new RAFileInputStream( file,
				positionAndLength[0], positionAndLength[0]
						+ positionAndLength[1] );
		return inputStream;
	}

	public boolean exists( String relativePath )
	{
		if ( !relativePath.startsWith( ArchiveUtil.UNIX_SEPERATOR ) )
			relativePath = ArchiveUtil.UNIX_SEPERATOR + relativePath;

		return ( lookupMap.get( relativePath ) != null );
	}

	/**
	 * reurun a list of strings which are the relative path of streams
	 */
	public List listStreams( String relativeStoragePath ) throws IOException
	{
		ArrayList streamList = new ArrayList( );
		if ( !relativeStoragePath.startsWith( ArchiveUtil.UNIX_SEPERATOR ) )
			relativeStoragePath = ArchiveUtil.UNIX_SEPERATOR
					+ relativeStoragePath;

		// loop through the lookup map
		Iterator entryIter = lookupMap.entrySet( ).iterator( );
		while ( entryIter.hasNext( ) )
		{
			Map.Entry entry = (Map.Entry) entryIter.next( );
			String relativePath = (String) entry.getKey( );
			if ( relativePath.startsWith( relativeStoragePath )
					&& !relativePath.equalsIgnoreCase( relativeStoragePath ) )
			{
				String diffString = ArchiveUtil.generateRelativePath(
						relativeStoragePath, relativePath );
				if ( diffString.lastIndexOf( ArchiveUtil.UNIX_SEPERATOR ) == 0 )
				{
					streamList.add( relativePath );
				}
			}
		}

		return streamList;
	}

}
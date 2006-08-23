/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.archive;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class FolderArchive implements IDocArchiveWriter, IDocArchiveReader
{
	private String folderName;
	private FolderArchiveReader reader;
	private FolderArchiveWriter writer;
	private boolean isOpen = false;
	private LinkedList openStreams = new LinkedList( );
	
	/**
	 * @param absolute fileName the archive file name
	 */
	public FolderArchive( String folderName ) throws IOException
	{
		if ( folderName == null ||
			 folderName.length() == 0 )
			throw new IOException("The folder name is null or empty string.");
		
		File fd = new File( folderName );
		folderName = fd.getCanonicalPath();   // make sure the file name is an absolute path
		this.folderName = folderName;
		
		reader = new FolderArchiveReader( folderName );
		writer = new FolderArchiveWriter( folderName );
		this.initialize();
	}

	////////////////// Functions that are needed by IDocArchiveWriter ///////////////
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.core.archive.IDocArchiveWriter#initialize()
	 */
	public void initialize() throws IOException 
	{
		if ( !isOpen )
		{
			writer.initialize();
			reader.open();
			isOpen = true;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.core.archive.IDocArchiveWriter#getName()
	 */
	public String getName( )
	{
		return folderName;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.core.archive.IDocArchiveWriter#createRandomAccessStream(java.lang.String)
	 */
	public RAOutputStream createRandomAccessStream(String relativePath) throws IOException 
	{
		RAOutputStream raOutputStream = writer
				.createRandomAccessStream( relativePath );
		synchronized ( openStreams )
		{
			openStreams.add( raOutputStream );
		}
		return raOutputStream;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.core.archive.IDocArchiveWriter#dropStream(java.lang.String)
	 */
	public boolean dropStream(String relativePath) 
	{
		return writer.dropStream( relativePath );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.core.archive.IDocArchiveWriter#exists(java.lang.String)
	 */
	public boolean exists(String relativePath) 
	{
		return writer.exists( relativePath );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.core.archive.IDocArchiveWriter#setStreamSorter(org.eclipse.birt.core.archive.IStreamSorter)
	 */
	public void setStreamSorter(IStreamSorter streamSorter) 
	{
		writer.setStreamSorter( streamSorter );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.core.archive.IDocArchiveWriter#finish()
	 */
	public void finish() throws IOException 
	{
		if ( isOpen )
		{
			closeAllStream( );
			writer.finish( );
			reader.close( );
			isOpen = false;
		}
	}

	////////////////// Functions that are needed by IDocArchiveReader ///////////////
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.core.archive.IDocArchiveReader#open()
	 */
	public void open() throws IOException 
	{
		initialize();	
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.core.archive.IDocArchiveReader#getStream(java.lang.String)
	 */
	public RAInputStream getStream(String relativePath) throws IOException 
	{
		return reader.getStream( relativePath );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.core.archive.IDocArchiveReader#listStreams(java.lang.String)
	 */
	public List listStreams(String relativeStoragePath) throws IOException 
	{
		return reader.listStreams( relativeStoragePath );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.core.archive.IDocArchiveReader#close()
	 */
	public void close() throws IOException 
	{
		this.finish();		
	}	

	////////////////// Other FolderArchiveManager functions ///////////////
	
	public boolean isOpen()
	{
		return isOpen; // The archive will always be opened in the constructor. Do we need it?
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.core.archive.IDocArchiveWriter#flush()
	 */
	public void flush( ) throws IOException
	{
		IOException ioex = null;
		synchronized ( openStreams )
		{
			Iterator iter = openStreams.iterator( );
			while ( iter.hasNext( ) )
			{
				RAFolderOutputStream stream = (RAFolderOutputStream) iter
						.next( );
				if ( stream != null )
				{
					try
					{
						stream.flush( );
					}
					catch ( IOException ex )
					{
						ioex = ex;
					}
				}
			}
		}
		if ( ioex != null )
		{
			throw ioex;
		}
	}
	
	protected void closeAllStream()
	{
		synchronized ( openStreams )
		{
			Iterator iter = openStreams.iterator( );
			while ( iter.hasNext( ) )
			{
				RAFolderOutputStream stream = (RAFolderOutputStream) iter
						.next( );
				if ( stream != null )
				{
					try
					{
						stream.close( );
					}
					catch ( IOException ex )
					{
					}
				}
			}
			openStreams.clear( );
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.core.archive.IDocArchiveReader#lock(java.lang.String)
	 */
	public Object lock( String stream ) throws IOException
	{
		String path = ArchiveUtil.generateFullPath( folderName, stream + ".lck" );
		return DocArchiveLockManager.getInstance( ).lock( path );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.core.archive.IDocArchiveReader#unlock(java.lang.Object)
	 */
	public void unlock( Object lock )
	{
		DocArchiveLockManager.getInstance( ).unlock( lock );
	}
}
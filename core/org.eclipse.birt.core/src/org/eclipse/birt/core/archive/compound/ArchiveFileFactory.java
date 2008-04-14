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
 
package org.eclipse.birt.core.archive.compound;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;


public class ArchiveFileFactory implements IArchiveFileFactory
{

	public IArchiveFile createArchive( String archiveId, String fileName )
			throws IOException
	{
		return doCreateArchive( archiveId, fileName, "rw" );
	}

	public IArchiveFile createTransientArchive( String archiveId,
			String fileName ) throws IOException
	{
		return doCreateArchive( archiveId, fileName, "rwt" );
	}

	private IArchiveFile doCreateArchive( String archiveId, String fileName,
			String mode ) throws IOException
	{
		return new ArchiveFileV2( archiveId, fileName, mode );
	}

	public IArchiveFile createView( String viewId, String fileName,
			IArchiveFile archive ) throws IOException
	{
		return doCreateView( viewId, fileName, archive, "rw" );
	}

	public IArchiveFile createTransientView( String viewId, String fileName,
			IArchiveFile archive ) throws IOException
	{
		return doCreateView( viewId, fileName, archive, "rwt" );
	}
	
	private IArchiveFile doCreateView( String viewId, String fileName,
			IArchiveFile archive, String mode ) throws IOException
	{
		ArchiveFileV2 view = new ArchiveFileV2( viewId, archive.getSystemId( ), fileName, mode );
		return new ArchiveView( view, archive, true );
	}

	/*
	 * Open the archive with <code>archiveId</code> in <code>mode</code>. 
	 * The <code>mode</code> could be:
	 *  - r		read 
	 *  - rw	read & write (Here should first create a new file)
	 *  - rw+	read & append
	 *  
	 *  1. in "r" mode
	 *  	a. view: open view in r mode, and open archive in r mode at the same time.
	 *  	b. archive: open archive in r mode directly.
	 *  
	 *  2. in "rw" mode
	 *  	a. view: create new view file, so no depend file exists.
	 *  	b. the same as above.
	 *  
	 *  3. in "rw+"
	 *  	a. view: open view in rw+ mode, and open archive in r mode at the same time.
	 *  	b. archive: open archive in rw+ mode.
	 */
	public IArchiveFile openArchive( String archiveId, String mode )
			throws IOException
	{
		ArchiveFile file = new ArchiveFile( archiveId, mode );
		String dependId = file.getDependId( );
		if ( dependId != null && dependId.length( ) > 0 )
		{
			IArchiveFile archive = openArchive( dependId, "r" );
			return new ArchiveView( file, archive, false );
		}
		return file;
	}

	public IArchiveFile openView( String viewId, String mode,
			IArchiveFile archive ) throws IOException
	{
		ArchiveFile view = new ArchiveFile( viewId, mode );
		return new ArchiveView( view, archive, true );
	}
}

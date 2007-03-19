
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.olap.data.document;

import java.io.IOException;

import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.core.archive.RAOutputStream;
import org.eclipse.birt.core.archive.compound.ArchiveFile;
import org.eclipse.birt.core.archive.compound.ArchiveReader;
import org.eclipse.birt.core.archive.compound.ArchiveWriter;

/**
 * 
 */

public class RADocumentManager implements IDocumentManager
{
	private ArchiveFile archiveFile;
	private ArchiveWriter archiveWriter;
	private ArchiveReader archiveReader;
	
	/**
	 * 
	 * @param archiveFile
	 * @throws IOException 
	 */
	RADocumentManager( ArchiveFile archiveFile ) throws IOException
	{
		this.archiveFile = archiveFile;
		this.archiveWriter = new ArchiveWriter( archiveFile );
		this.archiveReader = new ArchiveReader( archiveFile );
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.data.document.IDocumentManager#close()
	 */
	public void close( ) throws IOException
	{
		flush( );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.data.document.IDocumentManager#createDocumentObject(java.lang.String)
	 */
	public IDocumentObject createDocumentObject( String documentObjectName ) throws IOException
	{
		RAOutputStream outputStream = archiveWriter.createRandomAccessStream( documentObjectName );
		if ( outputStream == null )
			return null;
		flush( );
		return new DocumentObject( new RandomDataAccessObject( new RAWriter( outputStream ) ) );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.data.document.IDocumentManager#exist(java.lang.String)
	 */
	public boolean exist( String documentObjectName )
	{
		return archiveFile.exists( documentObjectName );
	}

	public IDocumentObject openDocumentObject( String documentObjectName ) throws IOException
	{
		RAInputStream inputStream = archiveReader.getStream( documentObjectName );
		if ( inputStream == null )
			return null;
		return new DocumentObject( new RandomDataAccessObject( new RAReader( inputStream ) ) );
	}

	public void flush( ) throws IOException
	{
		archiveWriter.flush( );
		archiveFile.flush( );
	}

}

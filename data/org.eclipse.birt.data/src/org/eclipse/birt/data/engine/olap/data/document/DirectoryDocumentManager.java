
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

import java.io.File;
import java.io.IOException;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;

/**
 * An implementation of the <tt>IDocumentManager</tt> interface. This class
 * create document object in a disk directory. Each document object is saved in
 * a disk file.
 * 
 */

public class DirectoryDocumentManager implements IDocumentManager
{
	private String documentDir = null;

	/**
	 * 
	 * @param documentDir
	 * @param deleteOld
	 * @throws DataException
	 */
	public DirectoryDocumentManager( String documentDir, boolean deleteOld ) throws DataException
	{
		this.documentDir = documentDir;
		File dir = new File( documentDir );
		if(!dir.exists( )||!dir.isDirectory( ))
		{
			if ( !dir.mkdir( ) )
			{
				throw new DataException( ResourceConstants.OLAPDIR_CREATE_FAIL,
						documentDir );
			}
		}
		if ( deleteOld )
		{
			File[] oldFiles = dir.listFiles( );
			for ( int i = 0; i < oldFiles.length; i++ )
			{
				oldFiles[i].delete( );
			}
		}
	}

	public void close( ) throws IOException
	{
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.document.IDocumentManager#createDocumentObject(java.lang.String)
	 */
	public boolean createDocumentObject( String documentObjectName ) throws IOException
	{
		File file =  new File(documentDir + File.separatorChar + documentObjectName);
		if ( file.exists( ) )
		{
			return true;
		}
		else
		{
			return file.createNewFile( );
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.document.IDocumentManager#openDocumentObject(java.lang.String)
	 */
	public IDocumentObject openDocumentObject( String documentObjectName ) throws IOException
	{
		File file = new File( documentDir
				+ File.separatorChar + documentObjectName );
		if ( !file.exists( ) )
		{
			return null;
		}
		
		return new DocumentObject( new SimpleRandomAccessObject(file, "rw") );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.document.IDocumentManager#exist(java.lang.String)
	 */
	public boolean exist( String documentObjectName )
	{
		File file =  new File(documentDir + File.separatorChar + documentObjectName);
		return file.exists( );
	}

}

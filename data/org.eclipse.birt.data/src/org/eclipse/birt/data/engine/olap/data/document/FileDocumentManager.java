
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

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.data.util.BufferedRandomAccessFile;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;

/**
 * An implementation of the <tt>IDocumentManager</tt> interface. This class 
 * use three files to save any number of document objects.
 */

public class FileDocumentManager implements IDocumentManager, IObjectAllocTable
{
	
	private BufferedRandomAccessFile objectFile = null;
	private BufferedRandomAccessFile OatFile = null;
	private BufferedRandomAccessFile dataFile = null;
	private HashMap documentObjectMap = null;
	
	/**
	 * 
	 * @param dirName
	 * @param managerName
	 * @return
	 * @throws DataException
	 * @throws IOException
	 */
	static FileDocumentManager createManager( String dirName, String managerName ) throws DataException, IOException
	{
		FileDocumentManager manager = new FileDocumentManager( );
		manager.create( dirName, managerName );
		return manager;
	}
	
	/**
	 * 
	 * @param dirName
	 * @param managerName
	 * @return
	 * @throws DataException
	 * @throws IOException
	 */
	static FileDocumentManager loadManager( String dirName, String managerName ) throws DataException, IOException
	{
		FileDocumentManager manager = new FileDocumentManager( );
		manager.load( dirName, managerName );
		return manager;
	}
	
	private FileDocumentManager(  )
	{
		
	}
	
	/**
	 * 
	 * @param dirName
	 * @param managerName
	 * @throws IOException
	 * @throws DataException
	 */
	private void create( String dirName, String managerName ) throws IOException, DataException
	{
		documentObjectMap = new HashMap( );
		
		File file = new File( dirName + File.separatorChar + managerName + "obj" );
		if ( !file.exists( ) )
		{
			if ( !file.createNewFile( ) )
			{
				throw new DataException( ResourceConstants.OLAPFILE_CREATE_FAIL,
						file.getAbsolutePath( ) );
			}
		}
		objectFile = new BufferedRandomAccessFile( file, "rw", 1024 );
		objectFile.setLength( 0 );
		file = new File( dirName + File.separatorChar + managerName + "Oat" );
		if ( !file.exists( ) )
		{
			if ( !file.createNewFile( ) )
			{
				throw new DataException( ResourceConstants.OLAPFILE_CREATE_FAIL,
						file.getAbsolutePath( ) );
			}
		}
		OatFile = new BufferedRandomAccessFile( file, "rw", 1024 );
		OatFile.setLength( 0 );
		file = new File( dirName + File.separatorChar + managerName + "data" );
		if ( !file.exists( ) )
		{
			if ( !file.createNewFile( ) )
			{
				throw new DataException( ResourceConstants.OLAPFILE_CREATE_FAIL,
						file.getAbsolutePath( ) );
			}
		}
		dataFile = new BufferedRandomAccessFile( file, "rw", 1024 );
		dataFile.setLength( 0 );
	}
	
	/**
	 * 
	 * @param dirName
	 * @param managerName
	 * @throws IOException
	 * @throws DataException
	 */
	private void load( String dirName, String managerName ) throws IOException, DataException
	{
		documentObjectMap = new HashMap( );
		
		File file = new File( dirName + File.separatorChar + managerName + "obj" );
		objectFile = new BufferedRandomAccessFile( file, "rw", 1024 );
		if ( !file.exists( ) )
		{
			throw new DataException( ResourceConstants.OLAPFILE_NOT_FOUND,
					file.getAbsolutePath( ) );
		}
		
		file = new File( dirName + File.separatorChar + managerName + "Oat" );
		if ( !file.exists( ) )
		{
			throw new DataException( ResourceConstants.OLAPFILE_NOT_FOUND,
					file.getAbsolutePath( ) );
		}
		OatFile = new BufferedRandomAccessFile( file, "rw", 1024 );
		
		file = new File( dirName + File.separatorChar + managerName + "data" );
		if ( !file.exists( ) )
		{
			throw new DataException( ResourceConstants.OLAPFILE_NOT_FOUND,
					file.getAbsolutePath( ) );
		}
		dataFile = new BufferedRandomAccessFile( file, "rw", 1024 );
		
		objectFile.seek( 0 );
		while(true)
		{
			try
			{
				ObjectStructure structure = readObjectStructure( );
				if ( structure.firstBlock >= 0 )
					documentObjectMap.put( structure.name, structure );
			}
			catch( EOFException e )
			{
				return;
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.document.IDocumentManager#close()
	 */
	public void close( ) throws IOException
	{
		objectFile.close( );
		OatFile.close( );
		dataFile.close( );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.document.IDocumentManager#createDocumentObject(java.lang.String)
	 */
	public IDocumentObject createDocumentObject( String documentObjectName ) throws IOException
	{
		ObjectStructure objectStructure = new ObjectStructure( );
		objectStructure.name = documentObjectName;
		objectStructure.firstBlock = findFreeBlock( );
		objectStructure.length = 0;
		writeObjectStructure( objectStructure );
		this.documentObjectMap.put( objectStructure.name, objectStructure );
		return new DocumentObject( new BlockRandomAccessObject( dataFile,
				documentObjectName,
				objectStructure.firstBlock,
				objectStructure.length,
				this ) );
	}
	
	/**
	 * 
	 * @param structure
	 * @throws IOException
	 */
	private void writeObjectStructure( ObjectStructure structure ) throws IOException
	{
		objectFile.seek( objectFile.length( ) );
		structure.fileOffset = ( int )objectFile.getFilePointer( );
		objectFile.writeLong( structure.length );
		objectFile.writeInt( structure.firstBlock );
		objectFile.writeUTF( structure.name );
	}
	
	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	private ObjectStructure readObjectStructure( ) throws IOException
	{
		ObjectStructure structure = new ObjectStructure( );
		structure.fileOffset = (int) objectFile.getFilePointer( );
		structure.length = objectFile.readLong( );
		structure.firstBlock = objectFile.readInt( );
		structure.name = objectFile.readUTF( );
		return structure;
	}
	
	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	private int findFreeBlock() throws IOException
	{
		int oldLength = (int) OatFile.length( );
		OatFile.setLength( oldLength + 4 );
		return (int) ( oldLength / 4 );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.document.IDocumentManager#openDocumentObject(java.lang.String)
	 */
	public IDocumentObject openDocumentObject( String documentObjectName )
			throws IOException
	{
		ObjectStructure objectStructure = (ObjectStructure) this.documentObjectMap.get( documentObjectName );
		if ( objectStructure == null )
		{
			return null;
		}
		return new DocumentObject( new BlockRandomAccessObject( dataFile,
				documentObjectName,
				objectStructure.firstBlock,
				objectStructure.length,
				this ) );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.document.IDocumentManager#exist(java.lang.String)
	 */
	public boolean exist( String documentObjectName )
	{
		return this.documentObjectMap.get( documentObjectName ) != null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.document.IObjectAllocTable#getNextBlock(int)
	 */
	public int getNextBlock( int blockNo ) throws IOException
	{
		OatFile.seek( blockNo * 4 );
		return OatFile.readInt( );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.document.IObjectAllocTable#allocateBlock(int)
	 */
	public int allocateBlock( int blockNo ) throws IOException
	{
		int newBlock = findFreeBlock( );
		OatFile.seek( blockNo * 4 );
		OatFile.writeInt( newBlock );
		return newBlock;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.olap.data.document.IObjectAllocTable#setObjectLength(java.lang.String, long)
	 */
	public void setObjectLength( String documentObjectName, long length ) throws IOException
	{
		ObjectStructure objectStructure = (ObjectStructure) documentObjectMap.get( documentObjectName );
		if ( objectStructure == null )
		{
			return;
		}
		objectStructure.length = length;
		objectFile.seek( objectStructure.fileOffset );
		objectFile.writeLong( length );
	}

	public void flush( ) throws IOException
	{
		objectFile.flush( );
		OatFile.flush( );
		dataFile.flush( );
	}
}

class ObjectStructure
{
	int fileOffset;
	long length;
	int firstBlock;
	String name;
}

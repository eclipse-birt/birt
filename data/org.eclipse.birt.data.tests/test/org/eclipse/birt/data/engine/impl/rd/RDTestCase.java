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

package org.eclipse.birt.data.engine.impl.rd;

import java.io.File;
import java.io.IOException;

import org.eclipse.birt.core.archive.FileArchiveReader;
import org.eclipse.birt.core.archive.FileArchiveWriter;
import org.eclipse.birt.core.archive.FolderArchiveReader;
import org.eclipse.birt.core.archive.FolderArchiveWriter;
import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.APITestCase;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.core.DataException;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptableObject;

/**
 * 
 */
public abstract class RDTestCase extends APITestCase
{
	protected DataEngine myGenDataEngine;
	protected DataEngine myPreDataEngine;
	protected DataEngine myPreDataEngine2;

	protected IDocArchiveWriter archiveWriter;
	protected IDocArchiveReader archiveReader;

	protected ScriptableObject scope;
	protected String fileName;
	protected String fileName2;
	
	private static int index = 0;
	
	/*
	 * @see org.eclipse.birt.data.engine.api.APITestCase#setUp()
	 */
	public void setUp( ) throws Exception
	{
		super.setUp( );
		
		index++;
		fileName = getOutputPath( ) + this.getName( ) + File.separator  + "RptDocumentTemp" + File.separator + "testData_" + index;
		index++;
		fileName2 = getOutputPath( ) + this.getName( ) + File.separator + "RptDocumentTemp" + File.separator + "testData_" + index;

		DataEngineContext deContext1 = newContext( DataEngineContext.MODE_GENERATION,
				fileName,
				fileName2 );
		deContext1.setTmpdir( this.getTempDir( ) );
		myGenDataEngine = DataEngine.newDataEngine( deContext1 );

		myGenDataEngine.defineDataSource( this.dataSource );
		myGenDataEngine.defineDataSet( this.dataSet );

		Context context = Context.enter( );
		scope = context.initStandardObjects( );
		Context.exit( );
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.APITestCase#tearDown()
	 */
	public void tearDown( ) throws Exception
	{
		super.tearDown( );
		if( archiveWriter != null )
		{
			try
			{
				archiveWriter.finish( );
			}
			catch ( IOException e )
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if( archiveReader != null )
		{
			try
			{
				archiveReader.close( );
			}
			catch ( IOException e )
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if ( fileName != null )
		{
			File file = new File( fileName );
			if( !file.delete( ))
				file.deleteOnExit( );
		}

		if ( fileName2 != null )
		{
			File file = new File( fileName2 );
			file.delete( );
			if( !file.delete( ))
				file.deleteOnExit( );
		}
		
	}
	
	/**
	 * @return
	 */
	protected boolean useFolderArchive()
	{
		return false;
	}

	/**
	 * @return folder for report document
	 */
	private String getOutputPath( )
	{
		return this.getOutputFolder( ).getAbsolutePath( ) + File.separator;
	}

	/**
	 * @param type
	 * @param fileName
	 * @return
	 * @throws BirtException
	 */
	protected DataEngineContext newContext( int type, String fileName )
			throws BirtException
	{
		return newContext( type, fileName, null );
	}

	/**
	 * @param type
	 * @return context
	 * @throws BirtException
	 */
	protected DataEngineContext newContext( int type, String fileName,
			String fileName2 ) throws BirtException
	{
		boolean useFolder = useFolderArchive( );
		switch ( type )
		{
			case DataEngineContext.MODE_GENERATION :
			{
				try
				{
					if ( useFolder == true )
						archiveWriter = new FolderArchiveWriter( fileName );
					else
						archiveWriter = new FileArchiveWriter( fileName );
					archiveWriter.initialize( );
				}
				catch ( IOException e )
				{
					throw new IllegalArgumentException( e.getMessage( ) );
				}
				DataEngineContext context =  DataEngineContext.newInstance( DataEngineContext.MODE_GENERATION,
						null,
						null,
						archiveWriter );
				context.setTmpdir( this.getTempDir( ) );
				return context;
			}
			case DataEngineContext.MODE_PRESENTATION :
			{
				try
				{
					if ( useFolder == true )
						archiveReader = new FolderArchiveReader( fileName );
					else
						archiveReader = new FileArchiveReader( fileName );
					archiveReader.open( );
				}
				catch ( IOException e )
				{
					throw new IllegalArgumentException( e.getMessage( ) );
				}
				DataEngineContext context = DataEngineContext.newInstance( DataEngineContext.MODE_PRESENTATION,
						null,
						archiveReader,
						null );
				context.setTmpdir( this.getTempDir( ) );
				return context;
			}
			case DataEngineContext.MODE_UPDATE :
			{
				try
				{
					if ( useFolder == true )
						archiveReader = new FolderArchiveReader( fileName );
					else
						archiveReader = new FileArchiveReader( fileName );
					archiveReader.open( );

					if ( useFolder == true )
						archiveWriter = new FolderArchiveWriter( fileName2 );
					else
						archiveWriter = new FileArchiveWriter( fileName2 );
					
					archiveWriter.initialize( );
				}
				catch ( IOException e )
				{
					throw new IllegalArgumentException( e.getMessage( ) );
				}
				DataEngineContext context = DataEngineContext.newInstance( DataEngineContext.MODE_UPDATE,
						null,
						archiveReader,
						archiveWriter );
				context.setTmpdir( this.getTempDir( ) );
				return context;
			}
			default :
				throw new IllegalArgumentException( "" + type );
		}
	}

	/**
	 * @throws DataException
	 */
	protected void closeArchiveWriter( ) throws DataException
	{
		if ( archiveWriter != null )
			try
			{
				archiveWriter.finish( );
			}
			catch ( IOException e )
			{
				throw new DataException( "error", e );
			}
	}

	/**
	 * @throws DataException
	 */
	protected void closeArchiveReader( ) throws DataException
	{
		if ( archiveReader != null )
			try
			{
				archiveReader.close( );
			}
			catch ( Exception e )
			{
				throw new DataException( "error", e );
			}
	}
	
}

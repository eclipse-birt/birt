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
		fileName = getOutputFolder( ) + "testData_" + index;
		index++;
		fileName2 = getOutputFolder( ) + "testData_" + index;

		DataEngineContext deContext1 = newContext( DataEngineContext.MODE_GENERATION,
				fileName,
				fileName2 );
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
	public void tearDown( )
	{
		if ( fileName != null )
		{
			File file = new File( fileName );
			file.delete( );
		}

		if ( fileName2 != null )
		{
			File file = new File( fileName2 );
			file.delete( );
		}
	}
	
	/**
	 * @return
	 */
	protected boolean useFolderArchive()
	{
		return true;
	}

	/**
	 * @return folder for report document
	 */
	protected String getOutputFolder( )
	{
		String className = getClass( ).getName( );
		int lastDotIndex = className.lastIndexOf( "." );
		className = className.substring( 0, lastDotIndex );
		File classFolder = new File( "test", className.replace( '.', '/' ) );
		return classFolder.getAbsolutePath( )
				+ File.separator + "output" + File.separator;
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
				return DataEngineContext.newInstance( DataEngineContext.MODE_GENERATION,
						null,
						null,
						archiveWriter );
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
				return DataEngineContext.newInstance( DataEngineContext.MODE_PRESENTATION,
						null,
						archiveReader,
						null );
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
				return DataEngineContext.newInstance( DataEngineContext.MODE_UPDATE,
						null,
						archiveReader,
						archiveWriter );
			}
			default :
				throw new IllegalArgumentException( "" + type );
		}
	}

}

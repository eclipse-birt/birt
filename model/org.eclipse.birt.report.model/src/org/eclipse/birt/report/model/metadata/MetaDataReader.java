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

package org.eclipse.birt.report.model.metadata;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Reads the meta-data definition file. The parser populates the singleton
 * dictionary instance.
 */

public final class MetaDataReader
{

	/**
	 * Parses the source metadata config file, retrieve the data into data
	 * structures. <code>MetaLogManager</code> will be loaded to do the meta
	 * data error logging, don't forget to call
	 * {@link MetaLogManager#shutDown()}after reading of the metadata.
	 * 
	 * 
	 * @param fileName
	 *            meta source file name.
	 * @throws MetaDataReaderException
	 */

	public static void read( String fileName ) throws MetaDataReaderException
	{
		InputStream inputStream = null;
		try
		{
			inputStream = new FileInputStream( fileName );
		}
		catch ( FileNotFoundException e )
		{
			MetaLogManager.log( "Metadata definition file not found", e ); //$NON-NLS-1$
			throw new MetaDataReaderException( fileName,
					MetaDataReaderException.FILE_NOT_FOUND );
		}
        
		try
		{
			read( inputStream );
		}
		catch ( MetaDataReaderException e )
		{
			e.setFileName( fileName );
			throw e;
		}
		finally
		{
			try
			{
				inputStream.close( );
			}
			catch ( IOException e )
			{
				// Do nothing.
			}
		}
	}

	/**
	 * Parses the source metadata config file, retrieve the data into data
	 * structures. <code>MetaLogManager</code> will be loaded to do the meta
	 * data error logging, don't forget to call
	 * {@link MetaLogManager#shutDown()}after reading of the metadata.
	 * 
	 * @param inputStream
	 *            meta source file stream.
	 * @throws MetaDataReaderException
	 */

	public static void read( InputStream inputStream )
			throws MetaDataReaderException
	{
		assert MetaDataDictionary.getInstance( ).isEmpty( );
		MetaDataHandler handler = new MetaDataHandler( );
		try
		{
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance( );
			SAXParser parser = saxParserFactory.newSAXParser( );
			parser.parse( inputStream, handler );
		}
		catch ( Exception e )
		{
			MetaLogManager.log( "Metadata parsing error", e ); //$NON-NLS-1$
			throw new MetaDataReaderException( e,
					MetaDataReaderException.PARSER_ERROR );
		}

	}

}

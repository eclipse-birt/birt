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

package org.eclipse.birt.report.model.parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.core.DesignSession;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * This class provides the reader for the design file. Encapsulates the SAX
 * parser. The parser attempts to read the file even if errors occur.
 * <p>
 * <code>DesignFileException</code> will be thrown if fatal error occurs, such
 * as file not found, syntax error or invalid xml file. Semantic error is along
 * with the design.
 * 
 */

public final class DesignReader
{

	/**
	 * Parses an XML design file given an input stream. Creates and returns the
	 * internal representation of the report design
	 * 
	 * @param session
	 *            the session of the report
	 * 
	 * @param fileName
	 *            the design file that the input stream is associated to.
	 * @param inputStream
	 *            the input stream that reads the design file
	 * @throws DesignFileException
	 *             if the input stream is not well-formed xml, there is
	 *             unsupported tags and there is run-time exception.
	 * @return the internal representation of the design
	 */

	public static ReportDesign read( DesignSession session, String fileName,
			InputStream inputStream ) throws DesignFileException
	{
		DesignParserHandler handler = new DesignParserHandler( session );

		// set file name of the design file. Used to search relative path
		// to the file.
		
		ReportDesign design = handler.getDesign( );
		design.setFileName( fileName );
		
		try
		{
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance( );
			SAXParser parser = saxParserFactory.newSAXParser( );
			InputSource inputSource = new InputSource( inputStream );
			inputSource.setEncoding( "UTF-8" ); //$NON-NLS-1$
			parser.parse( inputSource, handler );
		}
		catch ( SAXException e )
		{
			// Syntax error is found

			if ( e.getException( ) instanceof DesignFileException )
			{
				throw (DesignFileException) e.getException( );
			}

			// Invalid xml error is found

			throw new DesignFileException( fileName, handler.getDesign( )
					.getErrors( ), e );
		}
		catch ( ParserConfigurationException e )
		{
			throw new DesignFileException( fileName, handler.getDesign( )
					.getErrors( ), e );
		}
		catch ( IOException e )
		{
			throw new DesignFileException( fileName, handler.getDesign( )
					.getErrors( ), e );
		}


		design.setValid( true );
		return design;
	}

	/**
	 * Parses an XML design file given a file name. Creates and returns the
	 * internal representation of the report design
	 * 
	 * @param session
	 *            the session of the report
	 * 
	 * @param fileName
	 *            the design file to parse
	 * @return the internal representation of the design
	 * @throws DesignFileException
	 *             if file is not found
	 */
	
	public static ReportDesign read( DesignSession session, String fileName )
			throws DesignFileException
	{
		InputStream inputStream = null;
		try
		{
			inputStream = new FileInputStream( fileName );
		}
		catch ( FileNotFoundException e )
		{
			throw new DesignFileException( fileName );
		}

		return read( session, fileName, inputStream );
	}
}

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

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.util.UnicodeUtil;
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
		InputStream internalStream = inputStream;
		if ( !inputStream.markSupported( ) )
			internalStream = new BufferedInputStream( inputStream );

		assert internalStream.markSupported( );

		// set file name of the design file. Used to search relative path
		// to the file.

		ReportDesign design = (ReportDesign) handler.getModule( );
		design.setFileName( fileName );

		try
		{
			String signature = checkUTFSignature( internalStream, fileName );
			design.setUTFSignature( signature );

			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance( );
			SAXParser parser = saxParserFactory.newSAXParser( );
			InputSource inputSource = new InputSource( internalStream );
			inputSource.setEncoding( signature );
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

			throw new DesignFileException( fileName, handler.getModule( )
					.getAllErrors( ), e );
		}
		catch ( ParserConfigurationException e )
		{
			throw new DesignFileException( fileName, handler.getModule( )
					.getAllErrors( ), e );
		}
		catch ( IOException e )
		{
			throw new DesignFileException( fileName, handler.getModule( )
					.getAllErrors( ), e );
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
			inputStream = new BufferedInputStream( new FileInputStream(
					fileName ) );
		}
		catch ( FileNotFoundException e )
		{
			DesignParserException ex = new DesignParserException(
					new String[]{fileName},
					DesignParserException.DESIGN_EXCEPTION_FILE_NOT_FOUND );
			List exceptionList = new ArrayList( );
			exceptionList.add( ex );
			throw new DesignFileException( fileName, exceptionList );
		}

		assert inputStream.markSupported( );
		return read( session, fileName, inputStream );
	}

	/**
	 * Checks whether the input stream has a compatible encoding signature with
	 * BIRT. Currently, BIRT only supports UTF-8 encoding.
	 * 
	 * @param inputStream
	 *            the input stream to check
	 * @param fileName
	 *            the design file name
	 * @return the signature from the UTF files.
	 * @throws IOException
	 *             if errors occur during opening the design file
	 * @throws SAXException
	 *             if the stream has unexpected encoding signature
	 */

	private static String checkUTFSignature( InputStream inputStream,
			String fileName ) throws IOException, SAXException
	{

		// This may fail if there are a lot of space characters before the end
		// of the encoding declaration

		String encoding = UnicodeUtil.checkUTFSignature( inputStream );

		if ( encoding != null && !UnicodeUtil.SIGNATURE_UTF_8.equals( encoding ) )
		{
			Exception cause = new DesignParserException(
					DesignParserException.DESIGN_EXCEPTION_UNSUPPORTED_ENCODING );
			Exception fileException = new DesignFileException( fileName, cause );

			throw new SAXException( fileException );
		}

		return encoding;
	}
}
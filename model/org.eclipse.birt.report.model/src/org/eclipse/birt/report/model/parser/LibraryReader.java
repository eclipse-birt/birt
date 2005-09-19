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

import java.io.InputStream;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.core.DesignSession;
import org.eclipse.birt.report.model.elements.Library;


/**
 * This class provides the reader for the library file. Encapsulates the SAX
 * parser. The parser attempts to read the file even if errors occur.
 * <p>
 * <code>DesignFileException</code> will be thrown if fatal error occurs, such
 * as file not found, syntax error or invalid xml file. Semantic error is along
 * with the library.
 */

public final class LibraryReader extends ModuleReader
{
	/**
	 * The one and only library reader.
	 */
	
	private static LibraryReader instance = new LibraryReader( );

	/**
	 * Default constructor.
	 *
	 */
	
	private LibraryReader( )
	{

	}

	/**
	 * Gets the only instance of the library reader.
	 * 
	 * @return the only instance of the library reader
	 */

	public static LibraryReader getInstance( )
	{
		return instance;
	}

	/**
	 * Parses an XML library file given an input stream. Creates and returns the
	 * internal representation of the library.
	 * 
	 * @param session
	 *            the session of the library
	 * 
	 * @param fileName
	 *            the library file that the input stream is associated to.
	 * @param inputStream
	 *            the input stream that reads the library file
	 * @throws DesignFileException
	 *             if the input stream is not well-formed xml, there is
	 *             unsupported tags and there is run-time exception.
	 * @return the internal representation of the library
	 */

	public Library read( DesignSession session, String fileName,
			InputStream inputStream ) throws DesignFileException
	{
		return (Library) readModule( session, fileName, inputStream );
	}

	/**
	 * Parses an XML library file given a file name. Creates and returns the
	 * internal representation of the library.
	 * 
	 * @param session
	 *            the session of the library
	 * 
	 * @param fileName
	 *            the library file to parse
	 * @return the internal representation of the library
	 * @throws DesignFileException
	 *             if file is not found
	 */

	public Library read( DesignSession session, String fileName )
			throws DesignFileException
	{
		return (Library) readModule( session, fileName );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.ModuleReader#getParserHandler(org.eclipse.birt.report.model.core.DesignSession)
	 */

	protected ModuleParserHandler getParserHandler( DesignSession session )
	{
		return new LibraryParserHandler( session );
	}
}

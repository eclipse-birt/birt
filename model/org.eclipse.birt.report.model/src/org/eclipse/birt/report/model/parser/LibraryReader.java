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
import java.net.URL;
import java.util.Map;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ModuleOption;
import org.eclipse.birt.report.model.core.DesignSessionImpl;
import org.eclipse.birt.report.model.core.Module;
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
		// Forbid to instance this class outside.
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
	 * internal representation of the library. This method is used to open
	 * library file which is included in one library or report.
	 * 
	 * @param session
	 *            the session of the library
	 * @param host
	 *            the host module, which includes the library to open.
	 * @param fileName
	 *            the library file that the input stream is associated to.
	 * @param namespace
	 *            the namespace of the library to open.
	 * @param inputStream
	 *            the input stream that reads the library file
	 * @param options
	 *            the options set for this module
	 * 
	 * @return the internal representation of the library
	 * @throws DesignFileException
	 *             if the library file is not found or has syntax error. The
	 *             syntax errors include that input stream is not well-formed
	 *             xml, that there is unsupported tags and that there is
	 *             run-time exception.
	 */

	public Library read( DesignSessionImpl session, Module host, String fileName,
			String namespace, InputStream inputStream, ModuleOption options )
			throws DesignFileException
	{
		LibraryParserHandler handler = new LibraryParserHandler( session, host,
				fileName, options );
		( (Library) handler.getModule( ) ).setNamespace( namespace );

		return (Library) readModule( handler, inputStream );
	}

	/**
	 * Parses an XML library file given an input stream. Creates and returns the
	 * internal representation of the library. This method is used to open
	 * library file which is included in one library or report.
	 * 
	 * @param session
	 *            the session of the library
	 * @param host
	 *            the host module, which includes the library to open.
	 * @param url
	 *            the url of the library file.
	 * @param namespace
	 *            the namespace of the library to open.
	 * @param inputStream
	 *            the input stream that reads the library file
	 * @param options
	 *            the options set for this module
	 * @param reloadLibs
	 *            libraries that have been reload
	 * @return the internal representation of the library
	 * @throws DesignFileException
	 *             if the library file is not found or has syntax error. The
	 *             syntax errors include that input stream is not well-formed
	 *             xml, that there is unsupported tags and that there is
	 *             run-time exception.
	 */
	public Library read( DesignSessionImpl session, Module host, URL url,
			String namespace, InputStream inputStream, ModuleOption options,
			Map<String, Library> reloadLibs ) throws DesignFileException
	{
		LibraryParserHandler handler = new LibraryParserHandler( session, host,
				url, options, reloadLibs );
		( (Library) handler.getModule( ) ).setNamespace( namespace );

		return (Library) readModule( handler, inputStream );
	}

	/**
	 * Parses an XML library file given an input stream. Creates and returns the
	 * internal representation of the library.
	 * 
	 * @param session
	 *            the session of the library
	 * @param fileName
	 *            the library file that the input stream is associated to.
	 * @param inputStream
	 *            the input stream that reads the library file
	 * @param options
	 *            the options set for this module
	 * @return the internal representation of the library
	 * @throws DesignFileException
	 *             if the library file is not found or has syntax error. The
	 *             syntax errors include that input stream is not well-formed
	 *             xml, that there is unsupported tags and that there is
	 *             run-time exception.
	 */

	public Library read( DesignSessionImpl session, String fileName,
			InputStream inputStream, ModuleOption options )
			throws DesignFileException
	{
		LibraryParserHandler handler = new LibraryParserHandler( session,
				fileName, options );
		return (Library) readModule( handler, inputStream );
	}

	/**
	 * Parses an XML library file given an input stream. Creates and returns the
	 * internal representation of the library
	 * 
	 * @param session
	 *            the session of the library
	 * @param systemId
	 *            the uri path for the library file
	 * @param inputStream
	 *            the input stream that reads the library file
	 * @param options
	 *            the options set for this module
	 * @throws DesignFileException
	 *             if the input stream is not well-formed xml, there is
	 *             unsupported tags and there is run-time exception.
	 * @return the internal representation of the library
	 */

	public Library read( DesignSessionImpl session, URL systemId,
			InputStream inputStream, ModuleOption options )
			throws DesignFileException
	{
		LibraryParserHandler handler = new LibraryParserHandler( session,
				systemId, options );
		return (Library) readModule( handler, inputStream );
	}

	/**
	 * Parses an XML library file given a file name. Creates and returns the
	 * internal representation of the library
	 * 
	 * @param session
	 *            the session of the report
	 * @param fileName
	 *            the library file to parse
	 * @param options
	 *            the options set for this module
	 * @return the internal representation of the library
	 * @throws DesignFileException
	 *             if the library file is not found or has syntax error. The
	 *             syntax errors include that input stream is not well-formed
	 *             xml, that there is unsupported tags and that there is
	 *             run-time exception.
	 */

	public Library read( DesignSessionImpl session, String fileName,
			ModuleOption options ) throws DesignFileException
	{
		LibraryParserHandler handler = new LibraryParserHandler( session,
				fileName, options );
		return (Library) readModule( handler );
	}

}

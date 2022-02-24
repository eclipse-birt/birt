/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.parser;

import java.io.InputStream;
import java.net.URL;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ModuleOption;
import org.eclipse.birt.report.model.core.DesignSessionImpl;
import org.eclipse.birt.report.model.elements.ReportDesign;

/**
 * This class provides the reader for the design file. Encapsulates the SAX
 * parser. The parser attempts to read the file even if errors occur.
 * <p>
 * <code>DesignFileException</code> will be thrown if fatal error occurs, such
 * as file not found, syntax error or invalid xml file. Semantic error is along
 * with the design.
 */

public final class DesignReader extends ModuleReader {

	/**
	 * The one and only design reader.
	 */

	private static DesignReader instance = new DesignReader();

	/**
	 * Default constructor.
	 * 
	 */

	private DesignReader() {
		// Forbid to instance this class outside.
	}

	/**
	 * Gets the only instance of the design reader.
	 * 
	 * @return the only instance of the design reader
	 */

	public static DesignReader getInstance() {
		return instance;
	}

	/**
	 * Parses an XML design file given an input stream. Creates and returns the
	 * internal representation of the report design
	 * 
	 * @param session     the session of the report
	 * 
	 * @param fileName    the design file that the input stream is associated to.
	 * @param inputStream the input stream that reads the design file
	 * @param options     the options set for this module
	 * @return the internal representation of the design
	 * @throws DesignFileException if the library file is not found or has syntax
	 *                             error. The syntax errors include that input
	 *                             stream is not well-formed xml, that there is
	 *                             unsupported tags and that there is run-time
	 *                             exception.
	 */

	public ReportDesign read(DesignSessionImpl session, String fileName, InputStream inputStream, ModuleOption options)
			throws DesignFileException {
		DesignParserHandler handler = new DesignParserHandler(session, fileName, options);
		return (ReportDesign) readModule(handler, inputStream);
	}

	/**
	 * Parses an XML design file given an input stream. Creates and returns the
	 * internal representation of the report design
	 * 
	 * @param session     the session of the report
	 * 
	 * @param systemId    the uri path for the design file
	 * @param inputStream the input stream that reads the design file
	 * @param options     the options set for this module
	 * @throws DesignFileException if the input stream is not well-formed xml, there
	 *                             is unsupported tags and there is run-time
	 *                             exception.
	 * @return the internal representation of the design
	 */

	public ReportDesign read(DesignSessionImpl session, URL systemId, InputStream inputStream, ModuleOption options)
			throws DesignFileException {
		DesignParserHandler handler = new DesignParserHandler(session, systemId, options);
		return (ReportDesign) readModule(handler, inputStream);
	}

	/**
	 * Parses an XML design file given a file name. Creates and returns the internal
	 * representation of the report design
	 * 
	 * @param session  the session of the report
	 * @param fileName the design file to parse
	 * @param options  the options set for this module
	 * 
	 * @return the internal representation of the design
	 * @throws DesignFileException if the library file is not found or has syntax
	 *                             error. The syntax errors include that input
	 *                             stream is not well-formed xml, that there is
	 *                             unsupported tags and that there is run-time
	 *                             exception.
	 */

	public ReportDesign read(DesignSessionImpl session, String fileName, ModuleOption options)
			throws DesignFileException {
		DesignParserHandler handler = new DesignParserHandler(session, fileName, options);
		return (ReportDesign) readModule(handler);
	}
}

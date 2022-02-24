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
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.util.URIUtilImpl;

/**
 * Reads a XML file for design and library.
 */

public class GenericModuleReader extends ModuleReader {

	/**
	 * The one and only design reader.
	 */

	private static GenericModuleReader instance = new GenericModuleReader();

	/**
	 * Default constructor.
	 * 
	 */

	private GenericModuleReader() {
		// Forbid to instance this class outside.
	}

	/**
	 * Gets the only instance of the design reader.
	 * 
	 * @return the only instance of the design reader
	 */

	public static GenericModuleReader getInstance() {
		return instance;
	}

	/**
	 * Parses an XML module(design/library) file given an input stream. Creates and
	 * returns the internal representation of the module.
	 * 
	 * @param session     the session of the module
	 * @param fileName    the module file that the input stream is associated to.
	 * @param inputStream the input stream that reads the module file
	 * @param options     the options set for this module
	 * @return the internal representation of the module
	 * @throws DesignFileException if the file is not found or has syntax error. The
	 *                             syntax errors include that input stream is not
	 *                             well-formed xml, that there is unsupported tags
	 *                             and that there is run-time exception.
	 */

	public Module read(DesignSessionImpl session, String fileName, InputStream inputStream, ModuleOption options)
			throws DesignFileException {
		URL systemId = URIUtilImpl.getDirectory(fileName);
		GenericModuleParserHandler handler = new GenericModuleParserHandler(session, systemId, fileName, options);
		return readModule(handler, inputStream);
	}

	/**
	 * Parses an XML library file given an input stream. Creates and returns the
	 * internal representation of the library
	 * 
	 * @param session     the session of the library
	 * @param systemId    the uri path for the library file
	 * @param inputStream the input stream that reads the library file
	 * @param options     the options set for this module
	 * @throws DesignFileException if the input stream is not well-formed xml, there
	 *                             is unsupported tags and there is run-time
	 *                             exception.
	 * @return the internal representation of the library
	 */

	public Module read(DesignSessionImpl session, URL systemId, InputStream inputStream, ModuleOption options)
			throws DesignFileException {
		GenericModuleParserHandler handler = new GenericModuleParserHandler(session, systemId, null, options);
		return readModule(handler, inputStream);
	}

	/**
	 * Parses an XML module file given a file name. Creates and returns the internal
	 * representation of the module
	 * 
	 * @param session  the session of the report
	 * @param fileName the module file to parse
	 * @param options  the options set for this module
	 * @return the internal representation of the module
	 * @throws DesignFileException if the module file is not found or has syntax
	 *                             error. The syntax errors include that input
	 *                             stream is not well-formed xml, that there is
	 *                             unsupported tags and that there is run-time
	 *                             exception.
	 */

	public Module read(DesignSessionImpl session, String fileName, ModuleOption options) throws DesignFileException {
		URL systemId = URIUtilImpl.getDirectory(fileName);
		GenericModuleParserHandler handler = new GenericModuleParserHandler(session, systemId, fileName, options);
		return readModule(handler);
	}
}

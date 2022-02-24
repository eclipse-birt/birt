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

import java.net.URL;
import java.util.Map;

import org.eclipse.birt.report.model.api.ModuleOption;
import org.eclipse.birt.report.model.core.DesignSessionImpl;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.ModelUtil;
import org.eclipse.birt.report.model.util.URIUtilImpl;

/**
 * Top-level handler for the XML library file. Recognizes the top-level tags in
 * the file.
 */

public class LibraryParserHandler extends ModuleParserHandler {

	/**
	 * Constructor.
	 * 
	 * @param theSession the design session
	 * @param host       the host module
	 * @param fileName   the file name in URL format
	 * @param options    module options.
	 * @param reloadLibs libraries that have been reload
	 */

	LibraryParserHandler(DesignSessionImpl theSession, Module host, URL fileName, ModuleOption options,
			Map<String, Library> reloadLibs) {
		super(theSession, fileName.toExternalForm(), reloadLibs);

		module = new Library(theSession, host);

		URL url = URIUtilImpl.getDirectory(fileName);
		module.setSystemId(url);
		module.setFileName(fileName.toExternalForm());
		module.setOptions(options);

		// setup the location

		URL location = ModelUtil.getURLPresentation(fileName.toExternalForm());
		module.setLocation(location);

		buildModuleOptions(options);
	}

	/**
	 * Constructor.
	 * 
	 * @param theSession the design session
	 * @param host       the host module
	 * @param systemId   the library system id
	 * @param fileName   the file name
	 * @param options    module options.
	 * @param reloadLibs libraries that have been reload
	 */

	LibraryParserHandler(DesignSessionImpl theSession, Module host, String fileName, ModuleOption options) {
		super(theSession, fileName);

		module = new Library(theSession, host);

		URL url = URIUtilImpl.getDirectory(fileName);
		module.setSystemId(url);
		module.setFileName(fileName);
		module.setOptions(options);

		// setup the location

		URL location = ModelUtil.getURLPresentation(fileName);
		module.setLocation(location);

		buildModuleOptions(options);
	}

	/**
	 * Constructor.
	 * 
	 * @param theSession the design session
	 * @param host       the host module
	 * @param systemId   the library system id
	 * @param fileName   the file name
	 * @param options    module options.
	 */

	LibraryParserHandler(DesignSessionImpl theSession, String fileName, ModuleOption options) {
		super(theSession, fileName);
		module = new Library(theSession, null);

		URL systemId = URIUtilImpl.getDirectory(fileName);
		module.setSystemId(systemId);
		module.setFileName(fileName);
		module.setOptions(options);

		// setup the location

		URL location = ModelUtil.getURLPresentation(fileName);
		module.setLocation(location);

		buildModuleOptions(options);
	}

	/**
	 * Constructor.
	 * 
	 * @param theSession the design session
	 * @param host       the host module
	 * @param systemId   the library system id
	 * @param fileName   the file name
	 * @param options    module options.
	 */

	LibraryParserHandler(DesignSessionImpl theSession, URL systemId, ModuleOption options) {
		super(theSession, systemId.toExternalForm());
		module = new Library(theSession, null);
		module.setSystemId(systemId);
		module.setOptions(options);

		buildModuleOptions(options);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.XMLParserHandler#createStartState()
	 */
	public AbstractParseState createStartState() {
		return new StartState();
	}

	/**
	 * Recognizes the top-level tags: Library.
	 */

	class StartState extends InnerParseState {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement
		 * (java.lang.String)
		 */

		public AbstractParseState startElement(String tagName) {
			if (tagName.equalsIgnoreCase(DesignSchemaConstants.LIBRARY_TAG)) {
				if (markLineNumber)
					tempLineNumbers.put(module, Integer.valueOf(locator.getLineNumber()));
				return new LibraryState(LibraryParserHandler.this);
			}
			return super.startElement(tagName);
		}
	}
}

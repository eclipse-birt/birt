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
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.ModelUtil;

/**
 * Generic module parser handler, used to parse a design file or a library file.
 *
 */

class GenericModuleParserHandlerImpl extends ModuleParserHandler {

	/**
	 * Cached file location ID.
	 */

	protected URL location = null;

	/**
	 * Cached system ID.
	 */

	protected URL systemID = null;

	/**
	 * Options set for this module.
	 */

	protected ModuleOption options = null;

	GenericModuleParserHandlerImpl(DesignSessionImpl theSession, URL systemID, String fileName, ModuleOption options) {
		super(theSession, fileName);
		this.systemID = systemID;
		this.fileName = fileName;
		this.options = options;

		this.location = ModelUtil.getURLPresentation(fileName);
	}

	GenericModuleParserHandlerImpl(DesignSessionImpl theSession, URL systemID, String fileName, ModuleOption options,
			Map<String, Library> reloadLibs) {
		super(theSession, fileName, reloadLibs);
		this.systemID = systemID;
		this.fileName = fileName;
		this.options = options;
	}

	@Override
	public AbstractParseState createStartState() {
		return new StartState();
	}

	/**
	 * Recognizes the top-level tags: Report or Library
	 */

	class StartState extends InnerParseState {

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement
		 * (java.lang.String)
		 */

		@Override
		public AbstractParseState startElement(String tagName) {
			if (DesignSchemaConstants.REPORT_TAG.equalsIgnoreCase(tagName)) {
				module = new ReportDesign(session);
				module.setSystemId(systemID);
				module.setFileName(fileName);
				module.setOptions(options);
				module.setLocation(location);

				buildModuleOptions(options);
				if (markLineNumber) {
					tempLineNumbers.put(module, Integer.valueOf(locator.getLineNumber()));
				}
				return new ReportState(GenericModuleParserHandlerImpl.this);
			} else if (DesignSchemaConstants.LIBRARY_TAG.equalsIgnoreCase(tagName)) {
				module = new Library(session);
				module.setSystemId(systemID);
				module.setFileName(fileName);
				module.setLocation(location);

				module.setOptions(options);
				buildModuleOptions(options);
				if (markLineNumber) {
					tempLineNumbers.put(module, Integer.valueOf(locator.getLineNumber()));
				}
				return new LibraryState(GenericModuleParserHandlerImpl.this);
			}

			return super.startElement(tagName);
		}
	}
}

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

import org.eclipse.birt.report.model.api.ModuleOption;
import org.eclipse.birt.report.model.core.DesignSessionImpl;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.ModelUtil;
import org.eclipse.birt.report.model.util.URIUtilImpl;

/**
 * Top-level handler for the XML design file. Recognizes the top-level tags in
 * the file.
 * 
 */

public class DesignParserHandler extends ModuleParserHandler {

	/**
	 * Constructs the design parser handler with the design session.
	 * 
	 * @param theSession the design session that is to own the design
	 * @param systemId   the uri path for the design file
	 * @param fileName   name of the design file
	 * @param options    the options set for this module
	 */

	public DesignParserHandler(DesignSessionImpl theSession, String fileName, ModuleOption options) {
		super(theSession, fileName);
		module = new ReportDesign(session);

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
	 * Constructs the design parser handler with the design session.
	 * 
	 * @param theSession the design session that is to own the design
	 * @param systemId   the uri path for the design file
	 * @param fileName   name of the design file
	 * @param options    the options set for this module
	 */

	public DesignParserHandler(DesignSessionImpl theSession, URL systemId, ModuleOption options) {
		super(theSession, systemId.toExternalForm());
		module = new ReportDesign(session);

		URL url = URIUtilImpl.getDirectory(systemId.toExternalForm());
		module.setSystemId(url);
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
	 * Recognizes the top-level tags: Report.
	 */

	class StartState extends InnerParseState {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement
		 * (java.lang.String)
		 */

		public AbstractParseState startElement(String tagName) {
			if (tagName.equalsIgnoreCase(DesignSchemaConstants.REPORT_TAG)) {
				if (markLineNumber)
					tempLineNumbers.put(module, Integer.valueOf(locator.getLineNumber()));

				return new ReportState(DesignParserHandler.this);
			}
			return super.startElement(tagName);
		}
	}

}

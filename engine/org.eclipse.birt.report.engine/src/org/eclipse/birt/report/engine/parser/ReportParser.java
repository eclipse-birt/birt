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

package org.eclipse.birt.report.engine.parser;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.impl.ReportDocumentConstants;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.IModuleOption;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.ModuleOption;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;

import com.ibm.icu.util.ULocale;

/**
 * Report Parser.
 *
 * used to parse the design file, and get the IR of design.
 *
 *
 */
public class ReportParser {

	/**
	 * logger used to log syntax errors.
	 */
	static protected Logger logger = Logger.getLogger(ReportParser.class.getName());

	private Map<String, Object> options = new HashMap<String, Object>();

	/**
	 * Constructor 1
	 */
	public ReportParser() {
	}

	/**
	 * Constructor 2
	 *
	 * @param engine report engine
	 */
	public ReportParser(IReportEngine engine) {
		loadOption(engine);
	}

	/**
	 * Constructor 3
	 *
	 * @param context execution context
	 */
	public ReportParser(ExecutionContext context) {

		if (context != null) {
			IReportEngine engine = context.getEngine();
			if (engine != null) {
				loadOption(engine);
			}
		}
	}

	protected void loadOption(IReportEngine engine) {
		if (engine != null)

		{
			EngineConfig config = engine.getConfig();
			if (config != null) {
				Object locator = config.getResourceLocator();
				if (locator != null) {
					options.put(IModuleOption.RESOURCE_LOCATOR_KEY, locator);
				}
				Object resourcePath = config.getResourcePath();
				if (resourcePath != null) {
					options.put(IModuleOption.RESOURCE_FOLDER_KEY, resourcePath);
				}
			}
		}
	}

	/**
	 * Constructor 4
	 *
	 * @param options report parser options
	 */
	public ReportParser(Map<String, Object> options) {
		this.options.putAll(options);
	}

	/**
	 * parse the XML input stream.
	 *
	 * @param name design file name
	 *
	 * @param in   design file
	 * @return created report IR, null if exit any errors.
	 * @throws DesignFileException
	 */
	public Report parse(String name, InputStream in) throws DesignFileException {
		ReportDesignHandle designHandle = getDesignHandle(name, in);

		return parse(designHandle);
	}

	/**
	 * parse the XML input stream.
	 *
	 * @param name design file name
	 * @return created report IR, null if exit any errors.
	 * @throws DesignFileException
	 */
	public Report parse(String name) throws DesignFileException {
		ReportDesignHandle designHandle = getDesignHandle(name, null);

		return parse(designHandle);
	}

	/**
	 * Get design handle
	 *
	 * @param name report design name
	 * @param in   report stream
	 * @return design handle
	 * @throws DesignFileException
	 */
	public ReportDesignHandle getDesignHandle(String name, InputStream in) throws DesignFileException {
		// Create new design session
		SessionHandle sessionHandle = new DesignEngine(new DesignConfig()).newSessionHandle(ULocale.getDefault());

		// get the resource locator form the options and set it to the session
		// handle
		IResourceLocator locator = (IResourceLocator) options.get(IModuleOption.RESOURCE_LOCATOR_KEY);
		if (locator != null) {
			sessionHandle.setResourceLocator(locator);
		}

		// Obtain design handle
		ReportDesignHandle designHandle = null;
		ModuleOption modOptions = new ModuleOption(options);
		if (in != null) {
			designHandle = sessionHandle.openDesign(name, in, modOptions);
		} else {
			designHandle = sessionHandle.openDesign(name, modOptions);
		}

		return designHandle;
	}

	/**
	 * parse the XML input stream.
	 *
	 * @param design DE's IR
	 * @return FPE's IR, null if there is any error.
	 */
	public Report parse(ReportDesignHandle design) {
		assert (design != null);
		// assert ( design.getErrorList().isEmpty());

		EngineIRVisitor visitor = new MultiViewEngineIRVisitor(design);
		Report report = visitor.translate();
		report.setVersion(ReportDocumentConstants.BIRT_ENGINE_VERSION);
		return report;

	}

}

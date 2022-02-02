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

package org.eclipse.birt.report.designer.testutil;

import junit.framework.TestCase;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.tests.ITestConstants;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.elements.ReportDesign;

/**
 * Base class of unit tests
 * 
 * 
 */

public abstract class BaseTestCase extends TestCase {

	private ReportDesignHandle report;

	/**
	 * Default constructor
	 */
	public BaseTestCase() {// Do nothing
	}

	/**
	 * Constructor
	 * 
	 * @param name
	 */
	public BaseTestCase(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		loadFile(getLoadFile());
		report = (ReportDesignHandle) SessionHandleAdapter.getInstance().getReportDesignHandle();
	}

	protected void tearDown() throws Exception {
		report.close();
		report = null;
	}

	private void loadFile(String fileName) throws DesignFileException {
		ModuleHandle module = SessionHandleAdapter.getInstance().init(fileName,
				BaseTestCase.class.getResourceAsStream(fileName));

		SessionHandleAdapter.getInstance().setReportDesignHandle(module);
	}

	protected String getLoadFile() {
		return ITestConstants.TEST_DESIGN_FILE;
	}

	/**
	 * Gets the report design for tests
	 * 
	 * @return the report design for tests
	 */
	protected ReportDesign getReportDesign() {
		return report.getDesign();
	}

	protected ReportDesignHandle getReportDesignHandle() {
		return report;
	}

}

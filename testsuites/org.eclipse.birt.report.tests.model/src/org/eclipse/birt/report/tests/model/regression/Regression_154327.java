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

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ModuleOption;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * <b>Bug description:</b>
 * </p>
 * When open a library/report design, the semantic check always performs. It
 * cost extra time. In some cases, this check is not necessary.
 * <p>
 * Need an option for disabling semantic-check when opens files.
 * <p>
 * <b>Test Description:</b>
 * <p>
 * Enable/disable semantic check optino, and open design file with semantic
 * error, check its error
 */
public class Regression_154327 extends BaseTestCase {

	private String filename = "Regression_154327.xml"; //$NON-NLS-1$

	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		// copyResource_INPUT( filename , filename );
		copyInputToFile(INPUT_FOLDER + "/" + filename);
	}

	public void tearDown() {
		removeResource();
	}

	/**
	 * @throws DesignFileException
	 */
	public void test_regression_154327() throws DesignFileException {
		ModuleOption options = new ModuleOption();
		options.setSemanticCheck(false);

		// open design without semantic check

		DesignEngine designEngine = new DesignEngine(new DesignConfig());
		sessionHandle = designEngine.newSessionHandle((ULocale) null);

		designHandle = sessionHandle.openDesign(getTempFolder() + "/" + INPUT_FOLDER + "/" + filename, options);
		assertEquals(0, designHandle.getModule().getAllErrors().size());
		designHandle.close();

		// open design with semantic check

		options = null;
		designHandle = sessionHandle.openDesign(getTempFolder() + "/" + INPUT_FOLDER + "/" + filename, options);
		assertEquals(1, designHandle.getModule().getAllErrors().size());

	}
}

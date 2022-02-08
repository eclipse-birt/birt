/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Regression description:
 * </p>
 * The Birt model seems to get confused when there are two reports open at the
 * same time and you load eclipse.
 * <ol>
 * <li>Create a report called emptyreport and save it. It should have nothing
 * defined (i.e. no datasources, nothing).
 * <li>Create a report called filledreport and save it. It should have a
 * datasource, a dataset, and other elements defined.
 * <li>Close all.
 * <li>Open the Navigator, and open emptyreport.
 * <li>Open fullreport from the navigator. You should now have two tabs in the
 * report perspective - emptyreport and fullreport.
 * <li>Close eclipse.
 * <li>Open eclipse.
 * </ol>
 * You will get errors upon loading.
 * </p>
 * Test description:
 * <p>
 * Follow the steps, open two reports at the same time, ensure that no
 * exceptions show up.
 * </p>
 */
public class Regression_91673 extends BaseTestCase {

	private final static String INPUT = "regression_91673_1.xml"; //$NON-NLS-1$
	private final static String INPUT2 = "regression_91673_2.xml"; //$NON-NLS-1$

	/**
	 * @throws DesignFileException
	 */

	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyInputToFile(INPUT_FOLDER + "/" + INPUT);
		copyInputToFile(INPUT_FOLDER + "/" + INPUT2);
	}

	public void tearDown() {
		removeResource();
	}

	public void test_regression_91673() throws DesignFileException {
		String f1 = getTempFolder() + "/" + INPUT_FOLDER + "/" + INPUT;

		String f2 = getTempFolder() + "/" + INPUT_FOLDER + "/" + INPUT2;

		DesignEngine engine = new DesignEngine(new DesignConfig());
		SessionHandle sessionHandle = engine.newSessionHandle(ULocale.ENGLISH);

		ReportDesignHandle report1 = sessionHandle.openDesign(f1);
		ReportDesignHandle report2 = sessionHandle.openDesign(f2);

		assertNotNull(report1);
		assertNotNull(report2);
	}
}

/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import java.util.List;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ScriptDataSetHandle;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * <b>Regression description:</b>
 * <p>
 * BIRT 2.0 Scripted Data Set converted incorrectly in 2.1
 * <p>
 * First attachment in this file is a report design created in 2.0.1, which
 * defines a Scripted Data Set. Open the design in 2.1RC5, the BIRT designer
 * converts the design to 2.1 format. (Attachment 2 [edit] is the converted
 * design file). However visual inspection of the XML source reveals that the
 * data set definition contains both a "resultSet" structure and an identical
 * "resultSetHints" structure. The "resultSet" structure here is wrong - it
 * should have been removed during the conversion.
 * <p>
 * As it stands the extranueous "resultSet" does not stop the data set from
 * working so this is not a critical issue. But it should be removed.
 * <p>
 * <b>Test description:</b>
 * <p>
 * Open the 2.1 report, make sure that the resultSet list property is removed
 * after convertion.
 * <p>
 */
public class Regression_148548 extends BaseTestCase {

	private final static String REPORT = "ScriptTest2.0.1.rptdesign"; //$NON-NLS-1$

	/**
	 * @throws DesignFileException
	 */

	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		// copyResource_INPUT( REPORT , REPORT );
	}

	public void tearDown() {
		removeResource();
	}

	public void test_regression_148548() throws DesignFileException {
		openDesign(REPORT);
		ScriptDataSetHandle ds = (ScriptDataSetHandle) designHandle.findDataSet("Data Set"); //$NON-NLS-1$
		assertNotNull(ds);
		List resultSets = ds.getListProperty(ScriptDataSetHandle.RESULT_SET_PROP);

		assertNotNull(resultSets);
	}
}

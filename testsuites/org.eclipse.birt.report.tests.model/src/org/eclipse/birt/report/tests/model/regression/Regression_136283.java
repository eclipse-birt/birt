/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import java.util.Iterator;

import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * <b>Regression description:</b>
 * <p>
 * [compatibility] rows[0]["COLUMN_10"] does not work properly in inner table.
 * <p>
 * <b>Test description:</b>
 * <p>
 * Open the old design that use rows[0]["COLUMN_10"] to reference outer column
 * binding, make sure Model provided one level _outer for rows[0] backward
 * compaitiblity
 * <p>
 */
public class Regression_136283 extends BaseTestCase {

	private final static String REPORT = "regression_136283.xml"; //$NON-NLS-1$

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		// retrieve two input files from tests-model.jar file
		copyResource_INPUT(REPORT, REPORT);

	}

	/**
	 * @throws DesignFileException
	 */

	public void test_regression_136283() throws DesignFileException {
		openDesign(REPORT);
		TableHandle innerTable = (TableHandle) designHandle.findElement("innerTable"); //$NON-NLS-1$

		ComputedColumnHandle ss = null;
		Iterator iter = innerTable.columnBindingsIterator();
		while (iter.hasNext()) {
			ComputedColumnHandle cc = (ComputedColumnHandle) iter.next();
			if ("ss".equalsIgnoreCase(cc.getName())) //$NON-NLS-1$
			{
				ss = cc;
				break;
			}
		}

		assertNotNull(ss);
		assertEquals("row._outer[\"COLUMN_10\"]", ss.getExpression()); //$NON-NLS-1$
	}
}

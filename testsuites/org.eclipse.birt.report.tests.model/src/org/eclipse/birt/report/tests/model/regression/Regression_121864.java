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

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * Element with user properties can't be deleted
 * </p>
 * Test description:
 * <p>
 * Delete elements which have user properties
 * </p>
 */

public class Regression_121864 extends BaseTestCase {

	private String filename = "Regression_121864.xml"; //$NON-NLS-1$

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		// retrieve two input files from tests-model.jar file
		copyResource_INPUT(filename, filename);
		System.out.println(filename);

	}

	/**
	 * @throws DesignFileException
	 * @throws SemanticException
	 */
	public void test_regression_121864() throws DesignFileException, SemanticException {
		System.out.println(filename);
		openDesign(filename);

		assertEquals(1, designHandle.getUserProperties().size());
		TableHandle table = (TableHandle) designHandle.findElement("table"); //$NON-NLS-1$
		RowHandle detail = (RowHandle) table.getDetail().get(0);
		assertEquals(1, table.getUserProperties().size());
		assertEquals(1, detail.getUserProperties().size());

		assertTrue(table.canDrop());
		table.drop();

		assertEquals(0, table.getUserProperties().size());
		assertEquals(0, detail.getUserProperties().size());
	}
}

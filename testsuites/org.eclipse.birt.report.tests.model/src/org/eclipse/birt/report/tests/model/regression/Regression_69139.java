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

import java.util.List;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ErrorDetail;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * In design.xml, under MasterPage, add: <Style name="wrongName"/>, where
 * "wrongName" does not exist in the Styles namespace.
 * </p>
 * Parse this design file. There is no exception threw out to catch the
 * non-existed style.
 *
 */

public class Regression_69139 extends BaseTestCase {

	public final static String INPUT = "Reg_69139.xml"; //$NON-NLS-1$

	/**
	 * @throws DesignFileException
	 */

	@Override
	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyResource_INPUT(INPUT, INPUT);

	}

	@Override
	protected void tearDown() {
		removeResource();
	}

	public void test_regression_69139() throws DesignFileException {
		openDesign(INPUT);
		List errors = designHandle.getErrorList();
		assertEquals(1, errors.size());
		ErrorDetail error = (ErrorDetail) errors.get(0);
		assertTrue(error.getExceptionName().endsWith("StyleException")); //$NON-NLS-1$
	}
}

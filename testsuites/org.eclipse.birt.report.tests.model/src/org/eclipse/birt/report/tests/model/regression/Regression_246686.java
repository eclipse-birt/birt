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

import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * <b>Bug Description:</b>
 * <p>
 * Data can share the result set of a chart sharing the crosstab's result set,
 * but exception is thrown out when preview.
 * </p>
 *
 * <B>Test Description:</B>
 * <p>
 * Data cannot share from datacube binded item.
 * </p>
 */
public class Regression_246686 extends BaseTestCase {

	private final static String INPUT = "regression_246686.xml"; //$NON-NLS-1$

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		// retrieve two input files from tests-model.jar file
		copyResource_INPUT(INPUT, INPUT);

	}

	public void test_regression_246686() throws DesignFileException {
		openDesign(INPUT);
		DataItemHandle dataHandle = (DataItemHandle) designHandle.findElement("data1");
		assertEquals(0, dataHandle.getAvailableDataSetBindingReferenceList().size());
		assertEquals(2, dataHandle.getAvailableCubeBindingReferenceList().size());
	}
}

/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

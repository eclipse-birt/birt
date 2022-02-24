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

import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * <b>Bug Description:</b>
 * <p>
 * Chart can't be converted to a template report item
 * <p>
 * <b>Test Description:</b>
 * <ol>
 * <li>New a chart
 * <li>Right click the chart
 * <li>Create Template Report Item
 * </ol>
 */
public class Regression_164436 extends BaseTestCase {

	private final static String REPORT = "regression_164436.xml";

	public void setUp() throws Exception {
		super.setUp();
		removeResource();

		copyInputToFile(INPUT_FOLDER + "/" + REPORT);
	}

	public void tearDown() {
		removeResource();
	}

	public void test_regression_164436() throws Exception {
		openDesign(REPORT);

		// find the chart
		ExtendedItemHandle chart = (ExtendedItemHandle) designHandle.findElement("Chart1");
		assertNotNull(chart);

		// create chart to template Report Item
//		TemplateElementHandle chartTemp = chart
//				.createTemplateElement( "Temp_Chart" );
//		assertNotNull( chartTemp );

	}
}

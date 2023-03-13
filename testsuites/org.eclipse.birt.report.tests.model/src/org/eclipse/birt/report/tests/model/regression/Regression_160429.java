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

import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * <b>Regression description:</b>
 * <p>
 * Parameter name is changed with data type
 * <p>
 * Drag the datasource and dataset and produce an error gradation in data
 * explorer<br>
 * Step:<br>
 * 1.New 4 datasource named d1, d2, d3, d4.<br>
 * 2.Drag the datasource in data explorer to change the gradation.<br>
 * (1) Drag d1 between d2 and d3.<br>
 * (2) Drag d2 between d3 and d4.<br>
 * (3) Drag d4 between d2 and d3.<br>
 * (4) Drag d4 to the top.<br>
 * (5) Drag d3 between d1 and d2.<br>
 * (6) Drag d3 to the top.<br>
 * Actual result:<br>
 * (1) d1 will be move to bottom.<br>
 * (2) d2 will be move to bottom.<br>
 * (3) d4 will not be move to anywhere.<br>
 * (4) d4 will not be move to anywhere.<br>
 * (5) d3 will be move to bottom.<br>
 * (6) d3 will be move to bottom.<br>
 * Excepted result:<br>
 * The datasource will be move to the correct station.<br>
 * <p>
 * <b>Test description:</b>
 * <p>
 * Create four datasource and change them positions<br>
 * <p>
 */
public class Regression_160429 extends BaseTestCase {

	public final static String REPORT = "regression_160429.xml";

	public void test_regression_160429() throws Exception {
		// open the report design
		openDesign(REPORT);

		// find the position of datasource
		OdaDataSourceHandle ds1 = (OdaDataSourceHandle) designHandle.findDataSource("d1");
		OdaDataSourceHandle ds2 = (OdaDataSourceHandle) designHandle.findDataSource("d2");
		assertNotNull(ds1);

		System.out.println(ds1.getIndex());
		System.out.println(ds2.getIndex());
		System.out.println(designHandle.findContentSlot(ds2));

	}
}

/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * Error happens in XML Source when a report extends the library's cube
 * <p>
 * Test description: Test that extends the library's cube is correctly
 * <p>
 * </p>
 */

public class Regression_180411 extends BaseTestCase {

	private String report = "regression_180411.rptdesign";

	public void test_Regression_180411() throws Exception {
		openDesign(report);

		CubeHandle cubeHandle = (CubeHandle) designHandle.getCubes().get(0);
		assertNotNull(cubeHandle);

	}
}

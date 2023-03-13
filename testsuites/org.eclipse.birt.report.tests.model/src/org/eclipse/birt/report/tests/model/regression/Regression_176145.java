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

import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * BIRT Exception when transfer parameter from List Box to Radio Button
 * <p>
 * Test description: Test that transfer parameter from List Box to Radio Button
 * is correctly.
 * <p>
 * </p>
 */

public class Regression_176145 extends BaseTestCase {

	public void test_Regression_176145() throws Exception {

		openDesign("regression_176145.xml");

		try {
			ScalarParameterHandle param = (ScalarParameterHandle) designHandle.getParameters().get(0);
			param.setControlType("text-box");
			param.setControlType("list-box");
		} catch (Exception e) {
			fail();
		}

	}

}

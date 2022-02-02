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

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * A line like "public static final String JOIN_TYPE_FULL_OUT = "full-out"; "
 * should be add in DesignChoiceConstants.java to support the full outter join.
 * <p>
 * Test description:
 * <p>
 * Ensure that JOIN_TYPE_FULL_OUT is supported in DesignChoiceConstants.
 * </p>
 */

public class Regression_150777 extends BaseTestCase {

	/**
	 * 
	 */
	public void test_regression_150777() {
		assertEquals("full-out", DesignChoiceConstants.JOIN_TYPE_FULL_OUT); //$NON-NLS-1$
	}
}

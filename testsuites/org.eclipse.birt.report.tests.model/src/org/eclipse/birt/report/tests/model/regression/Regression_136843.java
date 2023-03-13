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

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * NPE when create report from predefined template which includes chart.
 * </p>
 * Test description:
 * <p>
 * This bug requires that Model should be able to open the Template report file
 * which contains chart template items.
 * <p>
 * The template file is from Birt release.
 * </p>
 */

public class Regression_136843 extends BaseTestCase {

	private final static String TEMPLATE = "chart_listing.rptdesign"; //$NON-NLS-1$

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		// retrieve two input files from tests-model.jar file
		copyResource_INPUT(TEMPLATE, TEMPLATE);

	}

	/**
	 * @throws DesignFileException
	 */

	public void test_regression_136843() throws DesignFileException {
		openDesign(TEMPLATE);
	}
}

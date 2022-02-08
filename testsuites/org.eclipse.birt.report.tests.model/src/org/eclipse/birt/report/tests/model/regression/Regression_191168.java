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

import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * [Regression]Exception occurs when resize masterpage in library
 * <p>
 * Test description: Testing the getDefaultUnits() method can get the default
 * value.
 * <p>
 * </p>
 */

public class Regression_191168 extends BaseTestCase {

	public void test_Regression_191168() throws Exception {
		createLibrary();
		assertEquals("in", libraryHandle.getDefaultUnits());
	}
}

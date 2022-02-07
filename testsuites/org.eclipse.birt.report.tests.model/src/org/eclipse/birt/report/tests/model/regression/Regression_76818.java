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
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * Can't add comment for a label
 * </p>
 * Test description:
 * <p>
 * Add comment for a label
 * </p>
 */

public class Regression_76818 extends BaseTestCase {

	private String filename = "Regression_76818.xml"; //$NON-NLS-1$

	/**
	 * @throws DesignFileException
	 * @throws SemanticException
	 */

	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyResource_INPUT(filename, filename);
		// copyResource_INPUT( INPUT2, INPUT2 );
	}

	public void tearDown() {
		removeResource();
	}

	public void test_regression_76818() throws DesignFileException, SemanticException {
		openDesign(filename);

		LabelHandle label = (LabelHandle) designHandle.findElement("label"); //$NON-NLS-1$
		label.setComments("comment");//$NON-NLS-1$
		assertEquals("comment", label.getComments());//$NON-NLS-1$

	}
}

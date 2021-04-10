/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * <p>
 * [regression]Embedded image in attached file cannot display.
 * <p>
 * Description: Embedded image in attached file cannot display.
 * <p>
 * Steps to reproduce:
 * <p>
 * Preview attached design file, compare it with attached golden.
 * <p>
 * Test description:
 * <p>
 * This is because of the "imageName" property changed from Expression to
 * String. The test file is an really old design, we should provided
 * compatibility for this case.
 * <p>
 * Open the old design, ensure that "imageName" is correctly read in, and source
 * is set to embedded.
 * <p>
 */

public class Regression_136517 extends BaseTestCase {

	private final static String INPUT = "regression_136517.xml"; //$NON-NLS-1$

	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		// retrieve two input files from tests-model.jar file
		copyResource_INPUT(INPUT, INPUT);

	}

	/**
	 * @throws DesignFileException
	 */

	public void test_regression_136517() throws DesignFileException {
		openDesign(INPUT);
		ImageHandle image = (ImageHandle) designHandle.findElement("img1"); //$NON-NLS-1$
		assertEquals("embed", image.getSource()); //$NON-NLS-1$
		assertEquals("embedgif", image.getImageName()); //$NON-NLS-1$
	}
}

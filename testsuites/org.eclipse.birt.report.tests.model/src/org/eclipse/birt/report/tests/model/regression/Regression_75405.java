/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * Set text alignment to a column which contains a image, text alighment didn't
 * take effect on image
 * </p>
 * Test description:
 * <p>
 * Set column text alignment to right, check image property search algorithm
 * </p>
 */

public class Regression_75405 extends BaseTestCase {

	private String filename = "Regression_75405.xml"; //$NON-NLS-1$

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

	public void test_regression_75405() throws DesignFileException, SemanticException {
		openDesign(filename);
		GridHandle grid = (GridHandle) designHandle.findElement("grid"); //$NON-NLS-1$
		ColumnHandle column = (ColumnHandle) grid.getColumns().get(0);
		column.setProperty(Style.TEXT_ALIGN_PROP, "right"); //$NON-NLS-1$

		ImageHandle image = (ImageHandle) designHandle.findElement("image"); //$NON-NLS-1$
		assertEquals("right", image.getProperty(Style.TEXT_ALIGN_PROP)); //$NON-NLS-1$
	}
}

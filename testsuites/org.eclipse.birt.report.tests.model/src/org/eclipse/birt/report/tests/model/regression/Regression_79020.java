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

import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * Row vertical alignment doesn't take effect
 * </p>
 * Test description:
 * <p>
 * Set verticle-align of row to "bottom", check the property value on content
 * cells, ensure the property is cascaded.
 * </p>
 */

public class Regression_79020 extends BaseTestCase {

	private final static String INPUT = "regression_79020.rptdesign"; //$NON-NLS-1$

	/**
	 * @throws DesignFileException
	 * @throws SemanticException
	 */
	/*
	 * public void setup( )throws Exception { super.setUp( ); removeResource();
	 * copyResource_INPUT( INPUT, INPUT ); //copyResource_INPUT( INPUT2, INPUT2 ); }
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		// retrieve two input files from tests-model.jar file
		copyResource_INPUT(INPUT, INPUT);

	}

	@Override
	public void tearDown() {
		removeResource();
	}

	public void test_regression_79020() throws DesignFileException, SemanticException {
		openDesign(INPUT);
		GridHandle grid1Handle = (GridHandle) designHandle.findElement("grid1"); //$NON-NLS-1$
		RowHandle rowHandle = (RowHandle) grid1Handle.getRows().get(0);
		CellHandle cellHandle = (CellHandle) rowHandle.getCells().get(0);

		assertEquals(null, cellHandle.getProperty(StyleHandle.VERTICAL_ALIGN_PROP)); // $NON-NLS-1$

		rowHandle.setProperty(StyleHandle.VERTICAL_ALIGN_PROP, "bottom"); //$NON-NLS-1$
		assertEquals("bottom", cellHandle.getStringProperty(StyleHandle.VERTICAL_ALIGN_PROP)); //$NON-NLS-1$
	}
}

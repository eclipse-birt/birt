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

import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.command.StyleException;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * Drag a table into a grid cell. Set row style for this grid. Row style didn't
 * take effect on table
 * </p>
 * Test description:
 * <p>
 * Set style for grid row, check table style
 * </p>
 */

public class Regression_74987 extends BaseTestCase {

	final static String filename = "Regression_74987.xml"; //$NON-NLS-1$

	/**
	 * @throws DesignFileException
	 * @throws StyleException
	 * @throws ContentException
	 * @throws NameException
	 */

	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyResource_INPUT(filename, filename);
		// System.out.println(filename);
	}

	/*
	 * public void tearDown( ) { removeResource( ); }
	 */
	public void test_regression_74987() throws DesignFileException, StyleException, ContentException, NameException {
		System.out.println(filename);
		openDesign(filename);
		GridHandle grid = (GridHandle) designHandle.findElement("Grid"); //$NON-NLS-1$
		TableHandle table = designHandle.getElementFactory().newTableItem("Table"); //$NON-NLS-1$

		RowHandle row = (RowHandle) grid.getRows().get(0);
		CellHandle cell = (CellHandle) row.getCells().get(0);
		cell.addElement(table, 0);

		row.setStyleName("Style"); //$NON-NLS-1$
		assertEquals("red", table.getProperty(Style.COLOR_PROP)); //$NON-NLS-1$
	}
}

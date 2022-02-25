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

package org.eclipse.birt.report.tests.model.api;

import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.birt.report.model.api.DimensionHandle;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * TestCases for columnHandle class. AutoTextHandle can be created from
 * ElementFactory.
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 *
 *
 * <tr>
 * <td>{@link #testGetColumnProperty()}</td>
 * <td>Set and get variant column properties</td>
 * <td>Set/get methods work.</td>
 * </tr>
 * </table>
 *
 */

public class ColumnHandleTest extends BaseTestCase {

//	 define two input files
	final static String INPUT = "ColumnHandleTest.xml";

	// String fileName = "ColumnHandleTest.xml"; //$NON-NLS-1$

	/*
	 * @see TestCase#setUp()
	 */

	public ColumnHandleTest(String name) {
		super(name);
	}

	public static Test suite() {

		return new TestSuite(ColumnHandleTest.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		// retrieve two input files from tests-model.jar file
		copyResource_INPUT(INPUT, INPUT);
	}

	/**
	 * Tests Set and get variant column properties.
	 *
	 * @throws Exception
	 */

	public void testGetColumnProperty() throws Exception {

		openDesign(INPUT);

		// style property inherited from cell, row, column, table element.

		// color defined on the cell.

		TableHandle table = (TableHandle) designHandle.findElement("My Table"); //$NON-NLS-1$
		assertNotNull("should not be null", table);

		ColumnHandle column = (ColumnHandle) table.getColumns().get(0);
		column.setRepeatCount(2);
		assertEquals(2, column.getRepeatCount());

		DimensionHandle dh = column.getWidth();
		assertEquals(100, dh.getMeasure(), 0);
		assertEquals("pt", dh.getUnits());

		// suppressDuplicates Property
		assertFalse(column.suppressDuplicates());
		column.setSuppressDuplicates(true);
		assertTrue(column.suppressDuplicates());
		designHandle.getCommandStack().undo();
		assertFalse(column.suppressDuplicates());
		designHandle.getCommandStack().redo();
		assertTrue(column.suppressDuplicates());

		ElementFactory factory = new ElementFactory(designHandle.getModule());
		GridHandle grid = factory.newGridItem("mygrid", 3, 3);
		ColumnHandle gridcolumn = (ColumnHandle) grid.getColumns().get(0);
		assertFalse(gridcolumn.suppressDuplicates());

	}
}

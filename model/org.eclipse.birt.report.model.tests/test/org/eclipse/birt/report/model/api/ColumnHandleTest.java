/*******************************************************************************
 * Copyright (c) 2004, 2005, 2006, 2007, 2008, 2009, 2010 Actuate Corporation.
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

package org.eclipse.birt.report.model.api;

import java.util.Iterator;

import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Test for the column handle
 *
 * @see ColumnHandle
 */

public class ColumnHandleTest extends BaseTestCase {

	public void testColumnWidthConvertion() throws Exception {
		openDesign("TableItemHandleTest_2.xml");

		// No change expected since all widths are absolute.
		TableHandle table = (TableHandle) designHandle.findElement("testTable1");
		for (Iterator<DesignElementHandle> iter = table.getColumns().iterator(); iter.hasNext();) {
			ColumnHandle column = (ColumnHandle) iter.next();
			column.convertWidthToAbsoluteValue();
			assertEquals("1in", column.getWidth().getValue().toString());
		}

		// Expects the width of the second column converts to absolute value.
		table = (TableHandle) designHandle.findElement("testTable2");
		table.setWidthToFitColumns(); // Fix the table width
		for (Iterator<DesignElementHandle> iter = table.getColumns().iterator(); iter.hasNext();) {
			ColumnHandle column = (ColumnHandle) iter.next();
			column.convertWidthToAbsoluteValue();
			assertEquals("1in", column.getWidth().getValue().toString());
		}

		// No change expected since all widths are relative and not percentage.
		table = (TableHandle) designHandle.findElement("testTable4");
		for (int i = 0; i < table.getColumns().getCount(); i++) {
			ColumnHandle column = (ColumnHandle) table.getColumns().get(i);
			assertEquals(i + 1 + "px", column.getWidth().getValue().toString());
			column.convertWidthToAbsoluteValue();
			assertEquals(i + 1 + "px", column.getWidth().getValue().toString());
		}

		// No change expected since the container's width is not set
		table = (TableHandle) designHandle.findElement("testTable10");
		for (int i = 0; i < table.getColumns().getCount(); i++) {
			ColumnHandle column = (ColumnHandle) table.getColumns().get(i);
			DimensionValue width = (DimensionValue) column.getWidth().getValue();
			column.convertWidthToAbsoluteValue();
			assertEquals(width, column.getWidth().getValue());
		}
	}
}

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

package org.eclipse.birt.report.engine.ir;

import java.util.Random;

/**
 * Test TableItem
 * 
 * Colunm has repeat, so we need to test if the getColumnCount is right.
 * 
 */
public class TableItemTest extends ReportItemTestCase {

	/**
	 * @param e
	 */
	public TableItemTest() {
		super(new TableItemDesign());
	}

	/**
	 * create a column using name & repeat
	 * 
	 * @param name   name
	 * @param repeat repeat
	 * @return column type
	 */
	private ColumnDesign createColumn(String name, int repeat) {
		ColumnDesign column = new ColumnDesign();
		column.setName(name);
		return column;
	}

	/**
	 * test if the column is match the name an repeate
	 * 
	 * @param column column
	 * @param name   column name
	 * @param repeat repeat
	 */
	private void checkColumn(ColumnDesign column, String name, int repeat) {
		assertEquals(column.getName(), name);
	}

	/**
	 * test column related functions.
	 * 
	 * add several columns into the grid, get one by one to test if the column is
	 * right.
	 */

	public void testColumn() {
		TableItemDesign table = (TableItemDesign) element;
		int count = (new Random()).nextInt(10) + 1;

		// Add
		for (int i = 0; i < count; i++) {
			table.addColumn(createColumn((new Integer(i)).toString(), i + 1));
		}

		// Get
		assertEquals(table.getColumnCount(), count);
		for (int i = 0; i < count; i++) {
			checkColumn(table.getColumn(i), (new Integer(i)).toString(), i + 1);
		}
	}

}

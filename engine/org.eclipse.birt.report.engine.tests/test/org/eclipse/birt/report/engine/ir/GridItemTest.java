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

import junit.framework.TestCase;

/**
 *
 * Grid Item test
 *
 * Colunm has repeat, so we need to test if the getColumnCount is right.
 *
 */
public class GridItemTest extends TestCase {

	/**
	 * test column add/getColumn methods
	 *
	 * add several columns into the grid
	 *
	 * then get the columns one by one to test if they work correctly
	 */
	public void testColumn() {
		GridItemDesign grid = new GridItemDesign();

		// Add
		grid.addColumn(createColumn("1", 1));
		grid.addColumn(createColumn("2", 2));
		grid.addColumn(createColumn("3", 3));

		// Get
		assertEquals(grid.getColumnCount(), 3);
		checkColumn(grid.getColumn(0), "1", 1);
		checkColumn(grid.getColumn(1), "2", 2);
		checkColumn(grid.getColumn(2), "3", 3);
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

}

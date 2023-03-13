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

import org.eclipse.birt.report.engine.EngineCase;

/**
 * Test RowType.
 *
 */
public class RowTypeTest extends EngineCase {

	/**
	 * test addCell function.
	 *
	 * cell has column attribute, but the row keep the cell as the design order, all
	 * check will be done by DE' parser.
	 */
	public void testAddCell() {
		RowDesign row = new RowDesign();
		// add cell 1
		row.addCell(createCell(1));
		// skip 2
		// set the column 3
		row.addCell(createCell(3, 3));
		// column 4
		row.addCell(createCell(4));
		// skip 5
		// column 6
		row.addCell(createCell(6, 6));

		assertCell(row.getCell(0), 1);
		assertCell(row.getCell(1), 3);
		assertCell(row.getCell(2), 4);
		assertCell(row.getCell(3), 6);
	}

	private CellDesign createCell(int colId, int cellId) {
		CellDesign cell = new CellDesign();
		cell.setColumn(colId);
		cell.setColSpan(cellId);
		return cell;
	}

	private CellDesign createCell(int cellId) {
		return createCell(0, cellId);
	}

	private void assertCell(CellDesign cell, int cellId) {
		assertTrue(cell.getColSpan() == cellId);
	}

	public void testGetSet() {
		RowDesign row = new RowDesign();
		DimensionType height = new DimensionType(1.0, DimensionType.UNITS_CM);
		Expression bookmark = Expression.newConstant("");

		// set
		row.setHeight(height);
		row.setBookmark(bookmark);

		// set
		assertEquals(row.getHeight(), height);
		assertEquals(row.getBookmark(), bookmark);
	}
}

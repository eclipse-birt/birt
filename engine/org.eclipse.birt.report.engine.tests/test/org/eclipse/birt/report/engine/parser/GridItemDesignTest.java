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

package org.eclipse.birt.report.engine.parser;

import org.eclipse.birt.report.engine.ir.CellDesign;
import org.eclipse.birt.report.engine.ir.ColumnDesign;
import org.eclipse.birt.report.engine.ir.GridItemDesign;
import org.eclipse.birt.report.engine.ir.RowDesign;

/**
 * Test Parser.
 * 
 */

public class GridItemDesignTest extends AbstractDesignTestCase {

	public void setUp() throws Exception {
		loadDesign("griditem_test.xml");
	}

	/**
	 * test case to test the parser,especially the capability to parse the Grid. To
	 * get the content about Grid from an external file and then compare the
	 * expected result with the real result of each property of DataSet. If they are
	 * the same,that means the IR is correct, otherwise, there exists errors in the
	 * parser.In this test, we also do some validation job focusing on element row
	 * and cell, assure their correctness no matter where they are.
	 */
	public void testGridItem() {
		GridItemDesign grid = (GridItemDesign) report.getContent(0);
		assertTrue(grid != null);
		assertEquals(4, grid.getColumnCount());
		assertEquals(2, grid.getRowCount());

		// test different elements of the grid
		// The first column

		// test the width of the first column
		ColumnDesign column = grid.getColumn(0);
		assertEquals(10.0, column.getWidth().getMeasure(), Double.MIN_VALUE);

		// test the count of cell in the first row
		RowDesign row = grid.getRow(0);
		assertEquals(3, row.getCellCount());

		// test the first cell
		CellDesign cell = row.getCell(0);
		assertEquals(2, cell.getColSpan());
		assertEquals(2, cell.getRowSpan());
		assertEquals(1, cell.getContentCount());
	}

	/**
	 * This method pays attention to the Grid Item nestted in a grid
	 */

	public void testGridItemNesting() {
		GridItemDesign grid = (GridItemDesign) report.getContent(0);
		assertTrue(grid != null);
		grid = (GridItemDesign) grid.getRow(1).getCell(1).getContent(0);
		assertTrue(grid != null);

		assertEquals(4, grid.getColumnCount());
		assertEquals(2, grid.getRowCount());
		ColumnDesign column = grid.getColumn(0);
		assertEquals(10.0, column.getWidth().getMeasure(), Double.MIN_VALUE);

		//
		RowDesign row = grid.getRow(0);
		assertEquals(2, row.getCellCount());

		CellDesign cell = row.getCell(0);
		assertEquals(2, cell.getColSpan());
		assertEquals(2, cell.getRowSpan());
		assertEquals(2, cell.getContentCount());
	}

	/**
	 * If a grid has no column define, we should append the column definition
	 */
	public void testEmptyColumn() {
		GridItemDesign grid = (GridItemDesign) report.getContent(1);
		assertEquals(grid.getColumnCount(), 10);
		assertEquals(1, grid.getRowCount());
		RowDesign row = grid.getRow(0);
		assertEquals(1, row.getCellCount());
		CellDesign cell = row.getCell(0);
		assertEquals(0, cell.getContentCount());
	}
}

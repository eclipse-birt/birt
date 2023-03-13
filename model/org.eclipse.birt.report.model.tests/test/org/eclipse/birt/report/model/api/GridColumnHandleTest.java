/**
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

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.metadata.IColorConstants;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.TableColumn;
import org.eclipse.birt.report.model.util.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Test cases for copy/paste columns between grids.
 *
 */

public class GridColumnHandleTest extends BaseTestCase {

	private String fileName = "GridColumnHandleTest.xml"; //$NON-NLS-1$

	/*
	 * @see TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * Tests copy algorithm on tables.
	 *
	 * <ul>
	 * <li>without dropping, and col span are all 1, no column information.</li>
	 * <li>without dropping, and col span are all 1, with column information.</li>
	 * <li>without dropping, and one cell's col span are 2, with column
	 * information.</li>
	 * <li>with dropping, and one cell's col span are 2, with column
	 * information.</li>
	 * </ul>
	 *
	 * @throws Exception
	 *
	 */

	public void testColumnCopy() throws Exception {
		openDesign(fileName);

		GridHandle grid = (GridHandle) designHandle.findElement("My grid1"); //$NON-NLS-1$
		assertNotNull(grid);

		ColumnBandData data = grid.copyColumn(1);
		assertEquals(2, ApiTestUtil.getCopiedCells(data).size());

		grid = (GridHandle) designHandle.findElement("My grid2"); //$NON-NLS-1$
		assertNotNull(grid);

		data = grid.copyColumn(2);
		assertEquals(1, ApiTestUtil.getCopiedCells(data).size());
		TableColumn column = ApiTestUtil.getCopiedColumn(data);
		assertEquals(1, column.getIntProperty(design, TableColumn.REPEAT_PROP));
		assertEquals("red", column.getStringProperty(design, Style.COLOR_PROP)); //$NON-NLS-1$
//		CellContextInfo contextInfo = (CellContextInfo) ApiTestUtil
//				.getCopiedCells( data ).get( 0 );
		assertEquals(2, ApiTestUtil.getCopiedCell(data, 0).getRowSpan(design));

		grid = (GridHandle) designHandle.findElement("My grid3"); //$NON-NLS-1$
		assertNotNull(grid);
		try {
			data = grid.copyColumn(1);
			fail();
		} catch (SemanticException e) {
			assertEquals(SemanticError.DESIGN_EXCEPTION_COLUMN_COPY_FORBIDDEN, e.getErrorCode());
		}

		try {
			data = grid.copyColumn(2);
			fail();
		} catch (SemanticException e) {
			assertEquals(SemanticError.DESIGN_EXCEPTION_COLUMN_COPY_FORBIDDEN, e.getErrorCode());
		}
	}

	/**
	 * Tests copy actions on tables.
	 *
	 * <ul>
	 * <li>without dropping, and col span are all 1, the source has no column
	 * information but the target has.</li>
	 * </ul>
	 *
	 * @throws Exception
	 *
	 */

	public void testCopyPasteWithForbiddenLayout() throws Exception {
		openDesign(fileName);

		GridHandle grid = (GridHandle) designHandle.findElement("My grid1"); //$NON-NLS-1$
		assertNotNull(grid);

		ColumnBandData adapter = grid.copyColumn(1);

		ElementFactory factory = grid.getElementFactory();
		GridHandle newGrid = factory.newGridItem("newGrid1", 2, 1); //$NON-NLS-1$
		assertEquals(2, newGrid.getColumns().getCount());

		// cannot be pasted since no group row in the new grid

		try {
			newGrid.pasteColumn(adapter, 1, true);
			fail();
		} catch (SemanticException e) {
			assertEquals(SemanticError.DESIGN_EXCEPTION_COLUMN_PASTE_FORBIDDEN, e.getErrorCode());
		}
	}

	/**
	 * Tests the algorithm to copy one column without a column header to another
	 * grid that has a column header.
	 *
	 * @throws Exception
	 */

	public void testCopyNoColumnHeader2HasColumn() throws Exception {
		designHandle = new SessionHandle(ULocale.getDefault()).createDesign();
		design = (ReportDesign) designHandle.getModule();

		ElementFactory factory = new ElementFactory(design);

		GridHandle grid1 = factory.newGridItem("grid1", 2, 2); //$NON-NLS-1$
		GridHandle grid2 = factory.newGridItem("grid2", 2, 2); //$NON-NLS-1$
		SlotHandle columns2 = grid2.getColumns();
		ColumnHandle column1InTable2 = (ColumnHandle) columns2.get(0);
		ColumnHandle column2InTable2 = (ColumnHandle) columns2.get(1);

		column1InTable2.setStringProperty(Style.COLOR_PROP, IColorConstants.AQUA);
		column2InTable2.setStringProperty(Style.COLOR_PROP, IColorConstants.AQUA);

		// removes all columns in the grid 1.

		int numOfColumnsInTable1 = grid1.getColumns().getCount();
		for (int i = 0; i < numOfColumnsInTable1; i++) {
			grid1.getColumns().dropAndClear(0);
		}
		assertEquals(0, grid1.getColumns().getCount());

		ColumnBandData adapter = grid1.copyColumn(1);
		grid2.pasteColumn(adapter, 1, true);

		assertEquals(2, columns2.getCount());
		column1InTable2 = (ColumnHandle) columns2.get(0);
		assertEquals(IColorConstants.BLACK, column1InTable2.getProperty(Style.COLOR_PROP));

		column2InTable2 = (ColumnHandle) columns2.get(1);
		assertEquals(IColorConstants.AQUA, column2InTable2.getProperty(Style.COLOR_PROP));
	}

	/**
	 * Tests the algorithm to copy one column header to another grid that has no
	 * column header.
	 *
	 * @throws Exception
	 */

	public void testCopyHasColumnHeader2NoColumn() throws Exception {
		designHandle = new SessionHandle(ULocale.getDefault()).createDesign();
		design = (ReportDesign) designHandle.getModule();

		ElementFactory factory = new ElementFactory(design);

		GridHandle grid1 = factory.newGridItem("grid1", 2, 2); //$NON-NLS-1$
		GridHandle grid2 = factory.newGridItem("grid2", 2, 2); //$NON-NLS-1$
		SlotHandle columns1 = grid1.getColumns();
		ColumnHandle column1InTable1 = (ColumnHandle) columns1.get(0);
		ColumnHandle column2InTable1 = (ColumnHandle) columns1.get(1);

		column1InTable1.setStringProperty(Style.COLOR_PROP, IColorConstants.AQUA);
		column2InTable1.setStringProperty(Style.COLOR_PROP, IColorConstants.AQUA);

		// removes all columns in the grid 1.

		int numOfColumnsInTable1 = grid2.getColumns().getCount();
		for (int i = 0; i < numOfColumnsInTable1; i++) {
			grid2.getColumns().dropAndClear(0);
		}
		assertEquals(0, grid2.getColumns().getCount());

		ColumnBandData data = grid1.copyColumn(1);
		grid2.pasteColumn(data, 1, true);

		SlotHandle columns2 = grid2.getColumns();

		assertEquals(2, columns2.getCount());
		ColumnHandle column1InTable2 = (ColumnHandle) columns2.get(0);
		assertEquals(IColorConstants.AQUA, column1InTable2.getProperty(Style.COLOR_PROP));

		ColumnHandle column2InTable2 = (ColumnHandle) columns2.get(1);
		column2InTable2 = (ColumnHandle) columns2.get(1);
		assertEquals(IColorConstants.BLACK, column2InTable2.getProperty(Style.COLOR_PROP));
	}

	/**
	 * Tests the algorithm to copy one column to another column.
	 *
	 * @throws Exception
	 */

	public void testCopyColumnHeader() throws Exception {
		designHandle = new SessionHandle(ULocale.getDefault()).createDesign();
		design = (ReportDesign) designHandle.getModule();

		ElementFactory factory = new ElementFactory(design);

		GridHandle grid1 = factory.newGridItem("grid1", 3, 2); //$NON-NLS-1$
		GridHandle grid2 = factory.newGridItem("grid2", 3, 2); //$NON-NLS-1$
		SlotHandle columns1 = grid1.getColumns();

		// from column 1 to column 1.

		ColumnHandle column1InTable1 = (ColumnHandle) columns1.get(0);
		column1InTable1.setStringProperty(Style.COLOR_PROP, IColorConstants.AQUA);

		// make only 1 column in grid 2.

		SlotHandle columns2 = grid2.getColumns();

		int numOfColumnsInTable2 = columns2.getCount();
		for (int i = 0; i < numOfColumnsInTable2 - 1; i++) {
			columns2.dropAndClear(0);
		}
		ColumnHandle columnInTable2 = (ColumnHandle) columns2.get(0);
		columnInTable2.setRepeatCount(3);
		assertEquals(1, columns2.getCount());

		// copy from column 1 to column 1, splitting columns is required.

		ColumnBandData data = grid1.copyColumn(1);
		grid2.pasteColumn(data, 1, true);

		assertEquals(2, columns2.getCount());
		ColumnHandle column1InTable2 = (ColumnHandle) columns2.get(0);
		assertEquals(IColorConstants.AQUA, column1InTable2.getProperty(Style.COLOR_PROP));

		ColumnHandle column2InTable2 = (ColumnHandle) columns2.get(1);
		assertEquals(2, column2InTable2.getRepeatCount());
		assertEquals(IColorConstants.BLACK, column2InTable2.getProperty(Style.COLOR_PROP));

		// make only 1 column in grid 2.

		numOfColumnsInTable2 = columns2.getCount();
		for (int i = 0; i < numOfColumnsInTable2 - 1; i++) {
			columns2.dropAndClear(0);
		}
		columnInTable2 = (ColumnHandle) columns2.get(0);
		columnInTable2.setRepeatCount(3);
		assertEquals(1, columns2.getCount());

		// copy from column 1 to column 3, splitting columns is required.

		data = grid1.copyColumn(1);
		grid2.pasteColumn(data, 3, true);
		assertEquals(2, columns2.getCount());

		column1InTable2 = (ColumnHandle) columns2.get(0);
		assertEquals(2, column1InTable2.getRepeatCount());
		assertEquals(IColorConstants.BLACK, column1InTable2.getProperty(Style.COLOR_PROP));

		column2InTable2 = (ColumnHandle) columns2.get(1);
		assertEquals(IColorConstants.AQUA, column2InTable2.getProperty(Style.COLOR_PROP));

		// make only 1 column in grid 2.

		numOfColumnsInTable2 = columns2.getCount();
		columns2.dropAndClear(1);
		columnInTable2 = (ColumnHandle) columns2.get(0);
		columnInTable2.setRepeatCount(3);
		assertEquals(1, columns2.getCount());

		// copy from column 1 to column 2, splitting columns is required.
		// Becomes 3 columns

		data = grid1.copyColumn(1);
		grid2.pasteColumn(data, 2, true);
		assertEquals(3, columns2.getCount());

		// verify column 1.

		column1InTable2 = (ColumnHandle) columns2.get(0);
		assertEquals(1, column1InTable2.getRepeatCount());
		assertEquals(IColorConstants.BLACK, column1InTable2.getProperty(Style.COLOR_PROP));

		// verify column 2.

		column2InTable2 = (ColumnHandle) columns2.get(1);
		assertEquals(1, column2InTable2.getRepeatCount());
		assertEquals(IColorConstants.AQUA, column2InTable2.getProperty(Style.COLOR_PROP));

		// verify column 3.

		ColumnHandle column3InTable2 = (ColumnHandle) columns2.get(2);
		assertEquals(1, column3InTable2.getRepeatCount());
		assertEquals(IColorConstants.BLACK, column3InTable2.getProperty(Style.COLOR_PROP));
	}

	/**
	 * Copies non-merged cells in the source grid to another grid with merged cells.
	 *
	 * @throws Exception
	 */

	public void copyPasteNonMergedCells2MergedCells() throws Exception {
		openDesign(fileName);

		GridHandle copyGrid = (GridHandle) designHandle.findElement("CopyGrid1"); //$NON-NLS-1$
		assertNotNull(copyGrid);

		GridHandle pasteGrid = (GridHandle) designHandle.findElement("PasteGrid1"); //$NON-NLS-1$
		assertNotNull(pasteGrid);

		SlotHandle detail = pasteGrid.getRows();
		RowHandle row1 = (RowHandle) detail.get(0);
		RowHandle row2 = (RowHandle) detail.get(1);
		assertEquals(2, row1.getCells().getCount());
		assertEquals(1, row2.getCells().getCount());

		CellHandle cell1 = (CellHandle) row1.getCells().get(0);
		assertEquals(1, cell1.getRowSpan());

		SlotHandle columns = pasteGrid.getColumns();
		assertEquals(2, columns.getCount());
		ColumnHandle column2 = (ColumnHandle) columns.get(1);
		assertEquals(IColorConstants.RED, column2.getStringProperty(Style.COLOR_PROP));

		ColumnBandData data = copyGrid.copyColumn(1);

		// different layout, the exception is thrown.

		try {
			pasteGrid.pasteColumn(data, 2, false);
			fail();
		} catch (SemanticException e) {
			assertEquals(SemanticError.DESIGN_EXCEPTION_COLUMN_PASTE_DIFFERENT_LAYOUT, e.getErrorCode());
		}

		// paste cells in force.

		// verify the layout of the pasted grid.

		pasteGrid.pasteColumn(data, 2, true);
		detail = pasteGrid.getRows();
		assertEquals(2, detail.getCount());

		row1 = (RowHandle) detail.get(0);
		row2 = (RowHandle) detail.get(1);

		assertEquals(2, row1.getCells().getCount());
		assertEquals(2, row2.getCells().getCount());

		cell1 = (CellHandle) row1.getCells().get(0);
		assertEquals(2, cell1.getRowSpan());

		columns = pasteGrid.getColumns();
		assertEquals(2, columns.getCount());

		// verify column information

		column2 = (ColumnHandle) columns.get(1);
		assertEquals(IColorConstants.AQUA, column2.getStringProperty(Style.COLOR_PROP));
		assertEquals(1, column2.getRepeatCount());

		ColumnHandle column1 = (ColumnHandle) columns.get(0);
		assertEquals(IColorConstants.YELLOW, column1.getStringProperty(Style.COLOR_PROP));
		assertEquals(1, column2.getRepeatCount());
	}

	/**
	 * Copies merged cells in the source grid to another grid without merged cells.
	 *
	 * @throws Exception
	 */

	public void copyPasteMergedCells2NonMergedCells() throws Exception {

		openDesign(fileName);

		GridHandle copyGrid = (GridHandle) designHandle.findElement("CopyGrid1"); //$NON-NLS-1$
		assertNotNull(copyGrid);

		GridHandle pasteGrid = (GridHandle) designHandle.findElement("PasteGrid1"); //$NON-NLS-1$
		assertNotNull(pasteGrid);

		SlotHandle detail = pasteGrid.getRows();
		RowHandle row1 = (RowHandle) detail.get(0);
		RowHandle row2 = (RowHandle) detail.get(1);
		assertEquals(2, row1.getCells().getCount());
		assertEquals(2, row2.getCells().getCount());

		SlotHandle columns = pasteGrid.getColumns();
		assertEquals(1, columns.getCount());
		ColumnHandle column1 = (ColumnHandle) columns.get(0);
		assertEquals(2, column1.getRepeatCount());
		assertEquals(IColorConstants.AQUA, column1.getStringProperty(Style.COLOR_PROP));

		ColumnBandData adapter = copyGrid.copyColumn(1);
		assertEquals(2, adapter.getCells().size());

		// different layout, the exception is thrown.

		try {
			pasteGrid.pasteColumn(adapter, 2, false);
			fail();
		} catch (SemanticException e) {
			assertEquals(SemanticError.DESIGN_EXCEPTION_COLUMN_PASTE_DIFFERENT_LAYOUT, e.getErrorCode());
		}

		// paste cells in force.

		// verify the layout of the pasted grid.

		pasteGrid.pasteColumn(adapter, 2, true);
		detail = pasteGrid.getRows();
		assertEquals(2, detail.getCount());

		row1 = (RowHandle) detail.get(0);
		row2 = (RowHandle) detail.get(1);

		assertEquals(2, row1.getCells().getCount());
		assertEquals(1, row2.getCells().getCount());

		CellHandle cell1 = (CellHandle) row1.getCells().get(1);
		assertEquals(2, cell1.getRowSpan());

		// vefiry that the new cell has a label element.

		assertEquals(1, cell1.getContent().getCount());

		columns = pasteGrid.getColumns();
		assertEquals(2, columns.getCount());

		// verify column information

		ColumnHandle column2 = (ColumnHandle) columns.get(1);
		assertEquals(IColorConstants.YELLOW, column2.getStringProperty(Style.COLOR_PROP));
		assertEquals(1, column2.getRepeatCount());

		column1 = (ColumnHandle) columns.get(0);
		assertEquals(IColorConstants.AQUA, column1.getStringProperty(Style.COLOR_PROP));
		assertEquals(1, column2.getRepeatCount());
	}

	/**
	 * Copies and pastes columns between tables with undo/redo supports.
	 *
	 * @throws Exception
	 */

	public void copyPasteWithUndoRedo() throws Exception {
		openDesign(fileName);

		GridHandle copyGrid = (GridHandle) designHandle.findElement("CopyGrid2"); //$NON-NLS-1$
		assertNotNull(copyGrid);

		GridHandle pasteGrid = (GridHandle) designHandle.findElement("PasteGrid2"); //$NON-NLS-1$
		assertNotNull(pasteGrid);

		SlotHandle detail = pasteGrid.getRows();
		RowHandle row1 = (RowHandle) detail.get(0);
		RowHandle row2 = (RowHandle) detail.get(1);
		assertEquals(2, row1.getCells().getCount());
		assertEquals(2, row2.getCells().getCount());

		SlotHandle columns = pasteGrid.getColumns();
		assertEquals(1, columns.getCount());

		ColumnBandData data = copyGrid.copyColumn(1);

		// paste cells in force.

		// verify the layout of the pasted grid.

		pasteGrid.pasteColumn(data, 2, true);
		detail = pasteGrid.getRows();
		assertEquals(2, detail.getCount());

		row1 = (RowHandle) detail.get(0);
		row2 = (RowHandle) detail.get(1);

		assertEquals(2, row1.getCells().getCount());
		assertEquals(1, row2.getCells().getCount());

		columns = pasteGrid.getColumns();
		assertEquals(2, columns.getCount());

		design.getActivityStack().undo();

		assertEquals(2, row1.getCells().getCount());
		assertEquals(2, row2.getCells().getCount());

		columns = pasteGrid.getColumns();
		assertEquals(1, columns.getCount());

		design.getActivityStack().redo();

		assertEquals(2, row1.getCells().getCount());
		assertEquals(1, row2.getCells().getCount());

		columns = pasteGrid.getColumns();
		assertEquals(2, columns.getCount());
	}
}

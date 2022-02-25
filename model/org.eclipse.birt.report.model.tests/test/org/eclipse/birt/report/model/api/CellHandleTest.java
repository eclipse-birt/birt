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

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.IColorConstants;
import org.eclipse.birt.report.model.elements.AutoText;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.TableGroup;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.TableRow;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Test cases for property search algorithm of cell elements.
 *
 */

public class CellHandleTest extends BaseTestCase {

	String fileName = "CellHandleTest.xml"; //$NON-NLS-1$

	/*
	 * @see TestCase#setUp()
	 */

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * Tests to get a style property of a cell element in the table.
	 *
	 * @throws Exception
	 */

	public void testGetCellProperty() throws Exception {

		openDesign(fileName);

		// style property inherited from cell, row, column, table element.

		// color defined on the cell.

		TableHandle table = (TableHandle) designHandle.findElement("My table1"); //$NON-NLS-1$
		CellHandle cell = getCellInTableSlot(table, TableItem.DETAIL_SLOT, 0, 0);
		assertEquals(IColorConstants.LIME, cell.getProperty(Style.COLOR_PROP));
		assertEquals(IColorConstants.LIME, cell.getFactoryPropertyHandle(Style.COLOR_PROP).getStringValue());

		// color defined on the row.

		table = (TableHandle) designHandle.findElement("My table2"); //$NON-NLS-1$
		cell = getCellInTableSlot(table, TableItem.DETAIL_SLOT, 0, 0);
		assertEquals(IColorConstants.RED, cell.getProperty(Style.COLOR_PROP));
		assertEquals(IColorConstants.RED, cell.getFactoryPropertyHandle(Style.COLOR_PROP).getStringValue());

		// some properties cannot be inherited.

		assertNull(cell.getProperty(Style.BACKGROUND_COLOR_PROP));
		assertEquals(DesignChoiceConstants.BACKGROUND_REPEAT_REPEAT, cell.getProperty(Style.BACKGROUND_REPEAT_PROP));
		assertEquals(null, cell.getFactoryPropertyHandle(Style.BACKGROUND_COLOR_PROP));

		// color defined on the column.

		table = (TableHandle) designHandle.findElement("My table3"); //$NON-NLS-1$
		cell = getCellInTableSlot(table, TableItem.DETAIL_SLOT, 0, 0);
		assertEquals(IColorConstants.RED, cell.getProperty(Style.COLOR_PROP));
		assertEquals(IColorConstants.RED, cell.getFactoryPropertyHandle(Style.COLOR_PROP).getStringValue());

		// color defined on the table.

		table = (TableHandle) designHandle.findElement("My table4"); //$NON-NLS-1$
		cell = getCellInTableSlot(table, TableItem.DETAIL_SLOT, 0, 0);
		assertEquals(IColorConstants.SILVER, cell.getProperty(Style.COLOR_PROP));
		assertEquals(null, cell.getFactoryPropertyHandle(Style.COLOR_PROP));

		// nested tables.

		table = (TableHandle) designHandle.findElement("My table6"); //$NON-NLS-1$
		cell = getCellInTableSlot(table, TableItem.DETAIL_SLOT, 0, 0);
		assertEquals(IColorConstants.RED, cell.getProperty(Style.COLOR_PROP));
		assertEquals(null, cell.getFactoryPropertyHandle(Style.COLOR_PROP));

		/* for cases that considers dropping header effects */

		// test style properties with ignoring the dropping effect.
		// cells in the detail and group header.
		table = (TableHandle) designHandle.findElement("My table11"); //$NON-NLS-1$
		cell = getCellInTableSlot(table, TableItem.DETAIL_SLOT, 0, 0);

		// shared style in the column.

		assertEquals(IColorConstants.RED, cell.getProperty(Style.COLOR_PROP));
		assertEquals(IColorConstants.RED, cell.getFactoryPropertyHandle(Style.COLOR_PROP).getStringValue());

		/* ensure things right on GROUP_HEADER */

		TableGroupHandle group = (TableGroupHandle) (table.getSlot(TableItem.GROUP_SLOT).get(0));
		cell = getCellInGroup(group, TableItem.HEADER_SLOT, 0, 0);

		// shared style in the column.

		assertEquals(IColorConstants.RED, cell.getProperty(Style.COLOR_PROP));
		assertEquals(IColorConstants.RED, cell.getFactoryPropertyHandle(Style.COLOR_PROP).getStringValue());

		cell = getCellInGroup(group, TableItem.HEADER_SLOT, 0, 1);
		assertEquals(IColorConstants.YELLOW, cell.getProperty(Style.COLOR_PROP));
		assertEquals(IColorConstants.YELLOW, cell.getFactoryPropertyHandle(Style.COLOR_PROP).getStringValue());

		// test cells in the DETAIL, HEADER, FOOTER. And different cells have
		// different column numbers.

		table = (TableHandle) designHandle.findElement("My table13"); //$NON-NLS-1$
		cell = getCellInTableSlot(table, TableItem.HEADER_SLOT, 0, 0);
		assertEquals(IColorConstants.RED, cell.getProperty(Style.COLOR_PROP));
		assertEquals(IColorConstants.RED, cell.getFactoryPropertyHandle(Style.COLOR_PROP).getStringValue());

		cell = getCellInTableSlot(table, TableItem.HEADER_SLOT, 0, 3);
		assertEquals(IColorConstants.GREEN, cell.getProperty(Style.COLOR_PROP));
		assertEquals(IColorConstants.GREEN, cell.getFactoryPropertyHandle(Style.COLOR_PROP).getStringValue());

		cell = getCellInTableSlot(table, TableItem.FOOTER_SLOT, 0, 0);
		assertEquals(IColorConstants.RED, cell.getProperty(Style.COLOR_PROP));
		assertEquals(IColorConstants.RED, cell.getFactoryPropertyHandle(Style.COLOR_PROP).getStringValue());

		cell = getCellInTableSlot(table, TableItem.FOOTER_SLOT, 0, 1);
		assertEquals(IColorConstants.AQUA, cell.getProperty(Style.COLOR_PROP));
		assertEquals(IColorConstants.AQUA, cell.getFactoryPropertyHandle(Style.COLOR_PROP).getStringValue());

		cell = getCellInTableSlot(table, TableItem.DETAIL_SLOT, 0, 0);
		assertEquals(IColorConstants.AQUA, cell.getProperty(Style.COLOR_PROP));
		assertEquals(IColorConstants.AQUA, cell.getFactoryPropertyHandle(Style.COLOR_PROP).getStringValue());

		// style for nested tables that ignores dropping effects.

		table = (TableHandle) designHandle.findElement("My table15"); //$NON-NLS-1$
		cell = getCellInTableSlot(table, TableItem.DETAIL_SLOT, 0, 0);
		assertEquals(IColorConstants.GREEN, cell.getProperty(Style.COLOR_PROP));
		assertEquals(null, cell.getFactoryPropertyHandle(Style.COLOR_PROP));

		cell = getCellInTableSlot(table, TableItem.DETAIL_SLOT, 0, 1);
		assertEquals(IColorConstants.GREEN, cell.getProperty(Style.COLOR_PROP));
		assertEquals(null, cell.getFactoryPropertyHandle(Style.COLOR_PROP));

		table = (TableHandle) designHandle.findElement("My table14"); //$NON-NLS-1$
		group = (TableGroupHandle) (table.getSlot(TableItem.GROUP_SLOT).get(0));
		cell = getCellInGroup(group, TableGroup.FOOTER_SLOT, 0, 0);
		assertEquals(IColorConstants.GREEN, cell.getProperty(Style.COLOR_PROP));
		assertEquals(IColorConstants.GREEN, cell.getFactoryPropertyHandle(Style.COLOR_PROP).getStringValue());

		// style for nested tables with dropping effects.

		table = (TableHandle) designHandle.findElement("My table17"); //$NON-NLS-1$
		cell = getCellInTableSlot(table, TableItem.DETAIL_SLOT, 0, 0);
		assertEquals(IColorConstants.GREEN, cell.getProperty(Style.COLOR_PROP));
		assertEquals(null, cell.getFactoryPropertyHandle(Style.COLOR_PROP));

		cell = getCellInTableSlot(table, TableItem.DETAIL_SLOT, 0, 1);
		assertEquals(IColorConstants.GREEN, cell.getProperty(Style.COLOR_PROP));
		assertEquals(null, cell.getFactoryPropertyHandle(Style.COLOR_PROP));

		table = (TableHandle) designHandle.findElement("My table16"); //$NON-NLS-1$
		group = (TableGroupHandle) (table.getSlot(TableItem.GROUP_SLOT).get(0));
		cell = getCellInGroup(group, TableGroup.FOOTER_SLOT, 0, 0);
		assertEquals(IColorConstants.GREEN, cell.getProperty(Style.COLOR_PROP));
		assertEquals(IColorConstants.GREEN, cell.getFactoryPropertyHandle(Style.COLOR_PROP).getStringValue());

		// Style property set on column take effect for cells.
		table = new ElementFactory(design).newTableItem(null, 1);
		designHandle.getBody().add(table);
		ColumnHandle columnHandle = (ColumnHandle) table.getColumns().get(0);
		columnHandle.getPrivateStyle().setFontWeight("100"); //$NON-NLS-1$
		cell = (CellHandle) ((RowHandle) table.getDetail().get(0)).getCells().get(0);
		assertEquals("100", cell.getProperty(StyleHandle.FONT_WEIGHT_PROP)); //$NON-NLS-1$
	}

	/**
	 * Tests the partly inheritable property "vertical-align" for cells.
	 *
	 * @throws Exception
	 */

	public void testCssProperties() throws Exception {
		openDesign(fileName);

		// vertical-align defined on the row.

		TableHandle table = (TableHandle) designHandle.findElement("My table1"); //$NON-NLS-1$
		CellHandle cell = getCellInTableSlot(table, TableItem.DETAIL_SLOT, 0, 0);
		assertEquals(DesignChoiceConstants.VERTICAL_ALIGN_BOTTOM, cell.getProperty(Style.VERTICAL_ALIGN_PROP));

		// vertical-align defined on the columns, tables, not rows.

		table = (TableHandle) designHandle.findElement("My table2"); //$NON-NLS-1$
		cell = getCellInTableSlot(table, TableItem.DETAIL_SLOT, 0, 0);
		assertEquals(DesignChoiceConstants.VERTICAL_ALIGN_BOTTOM, cell.getProperty(Style.VERTICAL_ALIGN_PROP));

		// vertical-align defined on the tables, not rows and columns. So,
		// it gets the default value.

		table = (TableHandle) designHandle.findElement("My table3"); //$NON-NLS-1$
		cell = getCellInTableSlot(table, TableItem.DETAIL_SLOT, 0, 0);
		assertNull(cell.getProperty(Style.VERTICAL_ALIGN_PROP));
	}

	/**
	 * Tests to get a style property of a cell element in the grid.
	 *
	 * @throws Exception
	 */

	public void testGridCellProperty() throws Exception {
		openDesign(fileName);

		// <!-- style defined on the cell -->

		GridHandle grid = (GridHandle) designHandle.findElement("My grid1"); //$NON-NLS-1$
		RowHandle row = (RowHandle) (grid.getRows().get(0));
		CellHandle cell = (CellHandle) (row.getCells().get(0));
		assertEquals(IColorConstants.LIME, cell.getProperty(Style.COLOR_PROP));
		assertEquals(IColorConstants.LIME, cell.getFactoryPropertyHandle(Style.COLOR_PROP).getStringValue());

		SlotHandle slotHandle = cell.getContent();
		AutoTextHandle autoTextHandle = (AutoTextHandle) slotHandle.getContents().get(1);
		assertEquals(DesignChoiceConstants.AUTO_TEXT_TOTAL_PAGE,
				autoTextHandle.getProperty(AutoText.AUTOTEXT_TYPE_PROP));

		LabelHandle label = (LabelHandle) designHandle.findElement("My label1"); //$NON-NLS-1$
		assertEquals(IColorConstants.LIME, label.getProperty(Style.COLOR_PROP));

		// <!-- style defined on the row -->

		grid = (GridHandle) designHandle.findElement("My grid2"); //$NON-NLS-1$
		row = (RowHandle) (grid.getRows().get(0));
		cell = (CellHandle) (row.getCells().get(0));
		assertEquals(IColorConstants.AQUA, cell.getProperty(Style.COLOR_PROP));
		assertEquals(null, cell.getFactoryPropertyHandle(Style.COLOR_PROP));

		label = (LabelHandle) designHandle.findElement("My label21"); //$NON-NLS-1$
		assertEquals(IColorConstants.AQUA, label.getProperty(Style.COLOR_PROP));

		// <!-- style defined on the cell and row-->

		label = (LabelHandle) designHandle.findElement("My label22"); //$NON-NLS-1$
		assertEquals(IColorConstants.LIME, label.getProperty(Style.COLOR_PROP));

		// <!-- style defined on the column -->

		grid = (GridHandle) designHandle.findElement("My grid4"); //$NON-NLS-1$
		row = (RowHandle) (grid.getRows().get(0));
		cell = (CellHandle) (row.getCells().get(0));
		assertEquals(IColorConstants.RED, cell.getProperty(Style.COLOR_PROP));
		assertEquals(IColorConstants.RED, cell.getFactoryPropertyHandle(Style.COLOR_PROP).getStringValue());

		// default color in the style.

		cell = (CellHandle) (row.getCells().get(1));
		assertEquals(IColorConstants.BLACK, cell.getProperty(Style.COLOR_PROP));
		assertEquals(null, cell.getFactoryPropertyHandle(Style.COLOR_PROP));

		cell = (CellHandle) (row.getCells().get(2));
		assertEquals(IColorConstants.LIME, cell.getProperty(Style.COLOR_PROP));
		assertEquals(IColorConstants.LIME, cell.getFactoryPropertyHandle(Style.COLOR_PROP).getStringValue());

		// the color on the column.

		label = (LabelHandle) designHandle.findElement("My label41"); //$NON-NLS-1$
		assertEquals(IColorConstants.RED, label.getProperty(Style.COLOR_PROP));

		// the color on the column.

		label = (LabelHandle) designHandle.findElement("My label42"); //$NON-NLS-1$
		assertEquals(IColorConstants.LIME, label.getProperty(Style.COLOR_PROP));

		// <!-- backgroundColor property cannot be inherited, which is defined
		// on the column -->

		label = (LabelHandle) designHandle.findElement("My label52"); //$NON-NLS-1$
		assertNull(label.getProperty(Style.BACKGROUND_COLOR_PROP));

	}

	/**
	 * Returns a cell handle for a cell in TableItem.GROUP_SLOT.
	 *
	 * @param group     the table group
	 * @param slotID    <code>TableGroup.FOOTER_SLOT</code> or
	 *                  <code>HEADER_SLOT</code>.
	 * @param rowIndex  the number of row in the slot
	 * @param cellIndex the index of cell in the row.
	 *
	 * @return a cell handle with the given information.
	 */

	private CellHandle getCellInGroup(TableGroupHandle group, int slotID, int rowIndex, int cellIndex) {
		RowHandle row = (RowHandle) (group.getSlot(slotID).get(rowIndex));
		CellHandle cell = (CellHandle) (row.getSlot(TableRow.CONTENT_SLOT).get(cellIndex));
		return cell;
	}

	/**
	 * Returns a cell handle for a cell in the slot of the table.
	 *
	 * @param table     the table element
	 * @param slotID    <code>TableItem.FOOTER_SLOT</code> or
	 *                  <code>TableItem.HEADER_SLOT</code> or
	 *                  <code>TableItem.DETAIL_SLOT</code>.
	 * @param rowIndex  the number of row in the slot
	 * @param cellIndex the index of cell in the row.
	 *
	 * @return a cell handle with the given information.
	 */

	private CellHandle getCellInTableSlot(TableHandle table, int slotID, int rowIndex, int cellIndex) {
		RowHandle row = (RowHandle) (table.getSlot(slotID).get(rowIndex));
		CellHandle cell = (CellHandle) (row.getSlot(TableRow.CONTENT_SLOT).get(cellIndex));
		return cell;
	}

	/**
	 * Tests the cases for textAlign. If no value is set, then the default value of
	 * textAlign for table-header row is center. That is getProperty and
	 * getFactoryProperty will return center rather than null or other value.
	 * However, the table-header-cell and label in the cell will return null for
	 * getFactoryProperty and it return center for getProperty.
	 *
	 * @throws Exception
	 */
	public void testTextAlign() throws Exception {
		openDesign("CellHandleTest_1.xml"); //$NON-NLS-1$

		// no value is set of textAlign for this table
		TableHandle tableHandle = (TableHandle) designHandle.findElement("table1"); //$NON-NLS-1$
		assertNull(tableHandle.getStringProperty(IStyleModel.TEXT_ALIGN_PROP));
		assertNull(tableHandle.getFactoryPropertyHandle(IStyleModel.TEXT_ALIGN_PROP));

		// table-header-row returns center for getProperty and
		// getFactoryProperty
		RowHandle rowHandle = (RowHandle) tableHandle.getHeader().get(0);
		assertEquals(DesignChoiceConstants.TEXT_ALIGN_CENTER, rowHandle.getStringProperty(IStyleModel.TEXT_ALIGN_PROP));
		assertEquals(DesignChoiceConstants.TEXT_ALIGN_CENTER,
				rowHandle.getFactoryPropertyHandle(IStyleModel.TEXT_ALIGN_PROP).getStringValue());

		// cell in table-header: return center for getProperty and null for
		// getFactoryProperty
		CellHandle cellHandle = (CellHandle) rowHandle.getCells().get(0);
		assertEquals(DesignChoiceConstants.TEXT_ALIGN_CENTER,
				cellHandle.getStringProperty(IStyleModel.TEXT_ALIGN_PROP));
		assertNull(cellHandle.getFactoryPropertyHandle(IStyleModel.TEXT_ALIGN_PROP));

		// label in table-header-cell: return center for getProperty and null
		// for getFactoryProperty
		LabelHandle labelHandle = (LabelHandle) cellHandle.getContent().get(0);
		assertEquals(DesignChoiceConstants.TEXT_ALIGN_CENTER,
				labelHandle.getStringProperty(IStyleModel.TEXT_ALIGN_PROP));
		assertNull(labelHandle.getFactoryPropertyHandle(IStyleModel.TEXT_ALIGN_PROP));

		// table detail row: return null for getProperty and getFactoryProperty
		rowHandle = (RowHandle) tableHandle.getDetail().get(0);
		assertNull(rowHandle.getStringProperty(IStyleModel.TEXT_ALIGN_PROP));
		assertNull(rowHandle.getFactoryPropertyHandle(IStyleModel.TEXT_ALIGN_PROP));

		// right is set of textAlign in this table
		tableHandle = (TableHandle) designHandle.findElement("table2"); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.TEXT_ALIGN_RIGHT,
				tableHandle.getStringProperty(IStyleModel.TEXT_ALIGN_PROP));
		assertEquals(DesignChoiceConstants.TEXT_ALIGN_RIGHT,
				tableHandle.getFactoryPropertyHandle(IStyleModel.TEXT_ALIGN_PROP).getStringValue());

		// table-header-row returns null for getProperty and
		// getFactoryProperty
		rowHandle = (RowHandle) tableHandle.getHeader().get(0);
		assertEquals(DesignChoiceConstants.TEXT_ALIGN_RIGHT, rowHandle.getStringProperty(IStyleModel.TEXT_ALIGN_PROP));
		assertNull(rowHandle.getFactoryPropertyHandle(IStyleModel.TEXT_ALIGN_PROP));
	}

}

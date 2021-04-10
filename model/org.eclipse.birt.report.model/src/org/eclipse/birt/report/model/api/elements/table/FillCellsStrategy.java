/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.model.api.elements.table;

import java.util.Arrays;
import java.util.Iterator;

import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.TableGroup;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.TableRow;
import org.eclipse.birt.report.model.elements.interfaces.IGroupElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IListingElementModel;
import org.eclipse.birt.report.model.elements.interfaces.ITableRowModel;

/**
 * The strategy to fill empty areas with <code>LayoutCell</code>s and
 * <code>Cell</code> elements.
 */

public class FillCellsStrategy {

	/**
	 * The layout table to apply the drop effects.
	 */

	private LayoutTable layoutTable;

	/**
	 * The flag is set to fill cell elements for the table element.
	 */

	private boolean fillsEmptyCells;

	/**
	 * Constructs a <code>DropStrategy</code> with the given table.
	 * 
	 * @param layoutTable     the layout table
	 * @param fillsEmptyCells <code>true</code> if cell elements are filled in empty
	 *                        areas. Otherwise <code>false</code>.
	 * 
	 */

	public FillCellsStrategy(LayoutTable layoutTable, boolean fillsEmptyCells) {
		this.layoutTable = layoutTable;
		this.fillsEmptyCells = fillsEmptyCells;
	}

	/**
	 * Applies different stragegies to the layout table and table element with the
	 * given options.
	 * 
	 */

	public void applyStrategy() {
		fillsEmptyCellsForTable();
	}

	/**
	 * Fills the empty cells to the table. It includes two operations:
	 * <ul>
	 * <li>Fills <code>LayoutCell.EMPTY_CELL</code> to areas that are empty.</li>
	 * <li>Fills <code>Cell</code>s to areas that are empty in the
	 * <code>TableItem</code></li>
	 * </ul>
	 * 
	 */

	private void fillsEmptyCellsForTable() {
		int columnCount = layoutTable.getColumnCount();
		TableItem table = layoutTable.table;

		fillsEmptyCellsForSlot(table.getSlot(IListingElementModel.HEADER_SLOT), layoutTable.getHeader(), columnCount);

		ContainerSlot groups = table.getSlot(IListingElementModel.GROUP_SLOT);
		int groupCount = groups.getCount();
		for (int i = 0; i < groupCount; i++) {
			TableGroup group = (TableGroup) groups.getContent(i);
			fillsEmptyCellsForSlot(group.getSlot(IGroupElementModel.HEADER_SLOT),
					layoutTable.getGroupHeaders().getLayoutSlot(i), columnCount);
		}

		fillsEmptyCellsForSlot(table.getSlot(IListingElementModel.DETAIL_SLOT), layoutTable.getDetail(), columnCount);

		// the group level in the group is from 0 to groupCount - 1;
		// the group level in the layout slot band is from groupCount - 1 to 0.

		for (int i = groupCount - 1; i >= 0; i--) {
			TableGroup group = (TableGroup) groups.getContent(groupCount - i - 1);
			fillsEmptyCellsForSlot(group.getSlot(IGroupElementModel.FOOTER_SLOT),
					layoutTable.getGroupFooters().getLayoutSlot(i), columnCount);
		}

		fillsEmptyCellsForSlot(table.getSlot(IListingElementModel.FOOTER_SLOT), layoutTable.getFooter(), columnCount);
	}

	/**
	 * Fills the empty cells to the given slot. It includes two operations:
	 * <ul>
	 * <li>Fills <code>LayoutCell.EMPTY_CELL</code> to areas that are empty.</li>
	 * <li>Fills <code>Cell</code>s to areas that are empty in the
	 * <code>TableItem</code></li>
	 * </ul>
	 * 
	 * @param slot        the slot in the table element
	 * @param layoutSlot  the slot in the layout table
	 * @param columnCount the column number of the table
	 */

	private void fillsEmptyCellsForSlot(ContainerSlot slot, LayoutSlot layoutSlot, int columnCount) {
		for (int i = 0; i < slot.getCount(); i++) {
			TableRow row = (TableRow) slot.getContent(i);
			LayoutRow layoutRow = layoutSlot.getLayoutRow(i);

			fillsEmptyCellsForRow(row, layoutRow, columnCount);
		}
	}

	/**
	 * Fills the empty cells to the given row. It includes two operations:
	 * <ul>
	 * <li>Fills <code>LayoutCell.EMPTY_CELL</code> to areas that are empty.</li>
	 * <li>Fills <code>Cell</code>s to areas that are empty in the
	 * <code>TableItem</code></li>
	 * </ul>
	 * 
	 * @param row         the row in the table element
	 * @param layoutRow   the layout row in the layout table
	 * @param columnCount the column number of the table
	 */

	private void fillsEmptyCellsForRow(TableRow row, LayoutRow layoutRow, int columnCount) {
		int colPos = 0;
		for (Iterator iter = layoutRow.layoutCellsIterator(); iter.hasNext(); iter.next())
			colPos++;
		if (colPos < columnCount)
			doFillLayoutCells(layoutRow, columnCount - colPos);

		int[] positionsToAddCells = new int[columnCount];
		int[] positionsToFillLayoutCells = new int[columnCount];
		Arrays.fill(positionsToAddCells, -1);
		Arrays.fill(positionsToFillLayoutCells, -1);
		boolean isFillsNecessary = false;

		Iterator iter = layoutRow.layoutCellsIterator();
		colPos = 1;
		for (int passedCells = 0, toAddCellsIndex = 0; iter.hasNext(); colPos++) {
			LayoutCell layoutCell = (LayoutCell) iter.next();
			if (layoutCell.isUsed() && layoutCell.isCellStartPosition())
				passedCells++;
			if (!layoutCell.isUsed() && colPos <= columnCount) {
				// fills a empty cell at this place.
				// records the position, do not add cells during the iteration

				positionsToAddCells[toAddCellsIndex] = passedCells + toAddCellsIndex;
				positionsToFillLayoutCells[toAddCellsIndex++] = colPos - 1;

				isFillsNecessary = true;
			}
		}

		if (fillsEmptyCells && isFillsNecessary) {
			doFillCells(row, positionsToAddCells, layoutRow, positionsToFillLayoutCells);
		}
	}

	/**
	 * Fills empty areas in a row with multiple <code>LayoutCell</code>s.
	 * 
	 * @param row      the table row to fill
	 * @param numToAdd the number of <code>LayoutCell</code>s to add
	 */

	private void doFillLayoutCells(LayoutRow row, int numToAdd) {
		for (int i = 0; i < numToAdd; i++)
			row.addCell(LayoutCell.EMPTY_CELL);
	}

	/**
	 * Fills empty areas in a row with multiple <code>LayoutCell</code>s.
	 * 
	 * @param row                        the table row to fill
	 * @param positionsToAddCells        positions to add
	 * @param layoutRow                  the layout row to fill
	 * @param positionsToFillLayoutCells positions to add layout cells
	 */

	private void doFillCells(TableRow row, int[] positionsToAddCells, LayoutRow layoutRow,
			int[] positionsToFillLayoutCells) {
		assert row != null;
		assert positionsToAddCells != null;

		assert positionsToAddCells.length == positionsToFillLayoutCells.length;

		for (int i = 0; i < positionsToAddCells.length; i++) {
			int posn = positionsToAddCells[i];
			if (posn < 0)
				continue;

			Cell cell = new Cell();
			row.add(cell, ITableRowModel.CONTENT_SLOT, posn);
			layoutRow.fillCells(layoutTable.getNextCellId(), positionsToFillLayoutCells[i], 1, 0, cell, false);
		}
	}
}

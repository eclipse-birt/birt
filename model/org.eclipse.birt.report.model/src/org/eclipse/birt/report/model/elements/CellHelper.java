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

package org.eclipse.birt.report.model.elements;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.ICellModel;
import org.eclipse.birt.report.model.elements.interfaces.IGridItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IGroupElementModel;
import org.eclipse.birt.report.model.elements.interfaces.ITableRowModel;

/**
 * Provides methods for finding a cell. Currently, only TableItem and GridItem
 * have cells.
 */

public class CellHelper {

	/**
	 * Gets the content slot handle of the cell at the position where the given row
	 * and column intersect.
	 * 
	 * @param module the module
	 * @param grid   the grid item to find the cell
	 * @param rowNum the row position indexing from 1
	 * @param colNum the column position indexing from 1
	 * @return the the cell if found, otherwise <code>null</code>
	 */

	public static Cell findCell(Module module, GridItem grid, int rowNum, int colNum) {
		if (grid == null)
			return null;
		if (colNum > grid.findMaxCols(module))
			return null;
		ContainerSlot rowSlot = grid.getSlot(IGridItemModel.ROW_SLOT);
		for (int i = 0; i < rowSlot.getCount(); i++) {
			TableRow row = (TableRow) rowSlot.getContent(i);
			ContainerSlot cellSlot = row.getSlot(ITableRowModel.CONTENT_SLOT);
			for (int j = 0; j < cellSlot.getCount(); j++) {
				int rowIndex = i;
				Cell cell = (Cell) cellSlot.getContent(j);
				int rowSpan = cell.getIntProperty(module, ICellModel.ROW_SPAN_PROP);
				rowSpan = (rowSpan < 1) ? 1 : rowSpan;

				// compute the logic row position
				rowIndex += rowSpan;

				// the row position is not smaller than the rowNum
				// the the cell maybe the one we try to find

				if (rowIndex >= rowNum) {
					int colIndex = 0;
					int column = grid.getCellPositionInColumn(module, cell);
					assert column > 0;
					int colSpan = cell.getIntProperty(module, ICellModel.COL_SPAN_PROP);
					colSpan = (colSpan < 1) ? 1 : colSpan;
					colIndex = column + colSpan - 1;
					if (colIndex >= colNum)
						return cell;
				}
			}
		}

		return null;
	}

	/**
	 * Gets cells in the table row.
	 * 
	 * @param rowSlot the table row.
	 * @return the cells.
	 */
	public static List<Cell> getCells(ContainerSlot rowSlot) {
		List<Cell> list = new ArrayList<Cell>();
		List<DesignElement> rowList = rowSlot.getContents();
		for (int i = 0; i < rowList.size(); i++) {
			TableRow tableRow = (TableRow) rowList.get(i);
			List<DesignElement> cellList = tableRow.getContentsSlot();
			for (int j = 0; j < cellList.size(); j++) {
				Cell cell = (Cell) cellList.get(j);
				list.add(cell);
			}
		}
		return list;
	}

	/**
	 * Gets the cells in the table group.
	 * 
	 * @param rowSlot the table group slot
	 * @return the cells.
	 */
	public static List<Cell> getCellsInTableGroup(ContainerSlot rowSlot) {
		List<Cell> list = new ArrayList<Cell>();
		List<DesignElement> groupList = rowSlot.getContents();
		for (int i = 0; i < groupList.size(); i++) {
			TableGroup group = (TableGroup) groupList.get(i);
			ContainerSlot slot = group.getSlot(IGroupElementModel.HEADER_SLOT);
			list.addAll(getCells(slot));

			slot = group.getSlot(IGroupElementModel.FOOTER_SLOT);
			list.addAll(getCells(slot));
		}
		return list;
	}
}

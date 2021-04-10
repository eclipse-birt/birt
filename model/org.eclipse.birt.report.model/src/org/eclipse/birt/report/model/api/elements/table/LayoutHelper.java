/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.api.elements.table;

import java.util.List;

import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.TableGroup;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.TableRow;
import org.eclipse.birt.report.model.elements.interfaces.IGroupElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IListingElementModel;

/**
 * An algorithm to support rowSpan, colSpan for BIRT table elements. "column"
 * properties of cells are cleared after reads it.
 */

public final class LayoutHelper {

	/**
	 * Resolves the layout to specified rows. This is for rows in Table Header,
	 * Table Footer, Detail and Group Footer.
	 * 
	 * @param mappingSlot the slot information
	 * @param row         the table row to resolve
	 * @param module      the report module
	 */

	private static void applyLayoutOnRow(LayoutSlot mappingSlot, TableRow row, Module module) {
		List<DesignElement> cells = row.getContentsSlot();
		if (cells.size() == 0)
			return;

		// gets the current row, dropping effects has been taken.

		LayoutRow mappingRow = mappingSlot.getCurrentLayoutRow();

		for (int i = 0, startCol = 1; i < cells.size(); i++) {
			Cell cell = (Cell) cells.get(i);
			int colSpan = cell.getColSpan(module);
			int definedColumn = cell.getColumn(module);

			if (definedColumn != 0)
				startCol = definedColumn;
			else
				startCol = findFillInPosition(mappingRow, startCol, colSpan);

			mappingSlot.addCell(startCol, cell.getRowSpan(module), colSpan, cell, false);

			startCol += colSpan;
		}
	}

	/**
	 * Finds the space for the given cell information: the column position and the
	 * column span.
	 * 
	 * @param row      the row information.
	 * @param startPos 1-based the position where to start the search
	 * @param colSpan  the column span of the cell
	 * @return the 1-based the position for the cell
	 */

	private static int findFillInPosition(LayoutRow row, int startPos, int colSpan) {
		int startCol = 0;

		// the column number - 1 = the position in the array.

		for (int i = startPos - 1, interval = 0; i < row.getColumnCount(); i++) {
			LayoutCell cell = row.getLayoutCell(i);
			if (!cell.isUsed())
				interval++;
			else
				interval = 0;

			// to the first interval space between neighboring cells. Like
			// the behavior in HTML, do not wait until the enough space.

			if (interval > 0) {
				startCol = i + 1;
				break;
			}
		}

		// still not found

		if (startCol == 0) {
			// put to the end of the row.

			startCol = row.getColumnCount() + 1;

			// if there is available spaces before the end of the row

			for (int i = row.getColumnCount() - 1; i >= startPos - 1; i--) {
				LayoutCell cell = row.getLayoutCell(i);
				if (!cell.isUsed())
					startCol--;
				else
					break;
			}
		}

		return startCol;
	}

	/**
	 * Resolves the layout for the given table element. This methods resolves
	 * "colSpan", "rowSpan" and "dropping" properties of cells in the table. If
	 * there is any error, records and proceed as much as possible.
	 * 
	 * @param module the report module.
	 * @param table  the table element
	 * @return the table that holds the layout structure after resolving
	 */

	public static LayoutTable applyLayout(Module module, TableItem table) {

		LayoutTable mappingTable = new LayoutTable(table, module);

		// for any semantic error, ignore all dropping properties.

		applyLayoutOnSlot(mappingTable.getHeader(), table.getSlot(IListingElementModel.HEADER_SLOT), module);

		ContainerSlot groups = table.getSlot(IListingElementModel.GROUP_SLOT);
		int groupCount = groups.getCount();

		// check on group header by group header. From the outer to the
		// inner-most.

		for (int groupIndex = 0; groupIndex < groupCount; groupIndex++) {
			TableGroup group = (TableGroup) groups.getContent(groupIndex);
			ContainerSlot header = group.getSlot(IGroupElementModel.HEADER_SLOT);

			LayoutSlot slot = mappingTable.getGroupHeaders().addSlot(group.getGroupLevel(),
					mappingTable.getColumnCount());
			applyLayoutOnSlot(slot, header, module);
		}

		applyLayoutOnSlot(mappingTable.getDetail(), table.getSlot(IListingElementModel.DETAIL_SLOT), module);

		// check on group footer by group footer. From the outer to the
		// inner-most.

		for (int groupIndex = groupCount - 1; groupIndex >= 0; groupIndex--) {
			TableGroup group = (TableGroup) groups.getContent(groupIndex);
			ContainerSlot header = group.getSlot(IGroupElementModel.FOOTER_SLOT);

			LayoutSlot slot = mappingTable.getGroupFooters().addSlot(group.getGroupLevel(),
					mappingTable.getColumnCount());
			applyLayoutOnSlot(slot, header, module);
		}

		applyLayoutOnSlot(mappingTable.getFooter(), table.getSlot(IListingElementModel.FOOTER_SLOT), module);

		return mappingTable;
	}

	/**
	 * Resolve the layout of Table Detail slot.
	 * 
	 * @param mappingSlot the slot information
	 * @param slot        the detail slot
	 * @param module      the report module
	 */

	protected static void applyLayoutOnSlot(LayoutSlot mappingSlot, ContainerSlot slot, Module module) {
		for (int rowIndex = 0; rowIndex < slot.getCount(); rowIndex++) {
			TableRow row = (TableRow) slot.getContent(rowIndex);

			mappingSlot.newLayoutRow(row);
			applyLayoutOnRow(mappingSlot, row, module);
		}
	}
}
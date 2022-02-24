/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.model.api;

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.core.IDesignElement;
import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.TableRow;
import org.eclipse.birt.report.model.elements.interfaces.IGroupElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IListingElementModel;

/**
 * Abstract class that shared by copy , paste , shift , insert operation.
 * 
 */

abstract class RowBandAction {

	/**
	 * Adapter to work on the grid/table columns.
	 */

	protected RowBandAdapter adapter = null;

	/**
	 * Constructs a default <code>RowBandAdapter</code>.
	 * 
	 * @param adapter the adapter to work on tables and grids.
	 */

	RowBandAction(RowBandAdapter adapter) {
		this.adapter = adapter;
	}

	/**
	 * Copies a row with the given row handle.
	 * 
	 * @param rowHandle handle of row
	 * @return a new row instance
	 */

	protected IDesignElement copyRow(RowHandle rowHandle) {
		return rowHandle.copy();
	}

	/**
	 * Copies a row with the given table row
	 * 
	 * @param row table row
	 * @return a new row instance
	 */

	protected TableRow copyRow(TableRow row) {
		TableRow clonedRow = null;

		try {
			clonedRow = (TableRow) row.clone();
		} catch (CloneNotSupportedException e) {
			assert false;
		}

		return clonedRow;
	}

	/**
	 * Checks whether copied row handle is a rectangle.
	 * 
	 * @param rowHandle handle of row
	 * @return <code>true</code> if the shape of integrated row handle is a
	 *         rectangle, otherwise <code>false</code>.
	 */

	protected boolean isRectangleArea(RowHandle rowHandle) {
		if (rowHandle == null)
			return true;

		int numOfColumns = adapter.getColumnCount();
		int columnCount = 0;
		SlotHandle slotHandle = rowHandle.getCells();

		for (int i = 0; i < slotHandle.getCount(); i++) {
			CellHandle cellHandle = (CellHandle) slotHandle.get(i);
			columnCount += cellHandle.getColumnSpan();
		}

		if (columnCount != numOfColumns)
			return false;

		return true;
	}

	/**
	 * Checks every cell contains row span or not. If contains any row span , return
	 * <code>false</code>;Otherwise return <code>true</code>
	 * 
	 * @param rowHandle handle of row.
	 * @return If contains any row span , return <code>true</code>;Otherwise return
	 *         <code>false</code>
	 */

	protected boolean containsRowSpan(RowHandle rowHandle) {
		if (rowHandle == null)
			return true;

		SlotHandle cellsHandle = rowHandle.getCells();
		int count = cellsHandle.getCount();
		for (int i = 0; i < count; ++i) {
			CellHandle cell = (CellHandle) cellsHandle.get(i);
			if (cell.getRowSpan() > 1) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets position of table row.
	 * 
	 * @param row table row . can't be copied row, because copied row is not in
	 *            tree.
	 * @return position of table row.
	 */

	protected int getPositionOfRow(TableRow row) {
		ContainerContext containerInfor = row.getContainerInfo();
		if (containerInfor == null)
			return -1;
		return containerInfor.indexOf(adapter.getModule(), row);
	}

	/**
	 * Returns column count in the given row.
	 * 
	 * @param row table row including copied row and row in design file.
	 * @return column count in the given row.
	 */

	protected int computeColumnCount(TableRow row) {
		List contents = row.getContentsSlot();
		Iterator cellIter = contents.iterator();
		int count = 0;
		while (cellIter.hasNext()) {
			Cell cell = (Cell) cellIter.next();
			int columnSpan = cell.getColSpan(null);
			count = count + columnSpan;
		}
		return count;
	}

	/**
	 * Returns column count in the given row.
	 * 
	 * @param rowHandle row handle
	 * @return column count in the given row.
	 */

	protected int computeColumnCount(RowHandle rowHandle) {
		return computeColumnCount((TableRow) rowHandle.getElement());
	}

	/**
	 * Get slot handle in table or group according to the slot id and group id.
	 * 
	 * @param parameters parameters for getting slot container.
	 * @return if can be found, return <code>SlotHandle</code>.Otherwise return
	 *         null.
	 */

	protected SlotHandle getSlotHandle(RowOperationParameters parameters) {
		ReportItemHandle reportHandle = adapter.getElementHandle();
		SlotHandle slotHandle = null;

		int slotId = parameters.getSlotId();
		int groupId = parameters.getGroupId();

		if (reportHandle instanceof TableHandle) {
			if (groupId >= 0) {
				SlotHandle groups = ((ListingHandle) reportHandle).getGroups();
				if (groups == null || groupId < 0 || groupId >= groups.getCount())
					return null;
				GroupHandle groupHandle = (GroupHandle) groups.get(groupId);

				if (slotId != IGroupElementModel.HEADER_SLOT && slotId != IGroupElementModel.FOOTER_SLOT)
					return null;

				slotHandle = groupHandle.getSlot(slotId);
			} else {
				if (slotId < IListingElementModel.HEADER_SLOT || slotId > IListingElementModel.FOOTER_SLOT)
					return null;
				slotHandle = reportHandle.getSlot(slotId);
			}
		} else if (reportHandle instanceof GridHandle) {
			slotHandle = ((GridHandle) reportHandle).getRows();
		}

		return slotHandle;
	}

	/**
	 * Adjusts position of destination index. The range is from zero to count-1.
	 * 
	 * @param destIndex index of destination
	 * @param count     count of row.
	 * @return position after adjusting.
	 */

	protected int adjustPosition(int destIndex, int count) {
		if (destIndex < 0)
			destIndex = 0;
		if (destIndex > count - 1)
			destIndex = count - 1;
		return destIndex;
	}

}

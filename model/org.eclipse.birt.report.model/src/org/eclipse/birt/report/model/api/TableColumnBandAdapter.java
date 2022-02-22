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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.interfaces.IGroupElementModel;

/**
 * Represents an object of copied objects when do copy/paste operations between
 * tables.
 */

public final class TableColumnBandAdapter extends ColumnBandAdapter {

	/**
	 * The element where the copy/paste operation occurs.
	 */

	protected TableHandle element;

	TableColumnBandAdapter() {
	}

	TableColumnBandAdapter(TableHandle element) {
		this.element = element;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.ColumnBandAdapter#getElement()
	 */

	@Override
	protected ReportItemHandle getElementHandle() {
		return element;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.ColumnBandAdapter#getColumns()
	 */

	@Override
	protected SlotHandle getColumns() {
		return element.getColumns();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.ColumnBandAdapter#getCellsUnderColumn(int,
	 * boolean)
	 */

	@Override
	protected List getCellsUnderColumn(int columnIndex, boolean mustBeStartPosition) {
		List cells = new ArrayList(getCellsInSlot(element.getHeader(), columnIndex, mustBeStartPosition));

		SlotHandle groups = element.getGroups();
		for (int i = 0; i < groups.getCount(); i++) {
			GroupHandle group = (GroupHandle) groups.get(i);
			cells.addAll(getCellsInSlot(group.getHeader(), columnIndex, mustBeStartPosition));
			cells.addAll(getCellsInSlot(group.getFooter(), columnIndex, mustBeStartPosition));
		}

		cells.addAll(getCellsInSlot(element.getDetail(), columnIndex, mustBeStartPosition));
		cells.addAll(getCellsInSlot(element.getFooter(), columnIndex, mustBeStartPosition));

		return cells;
	}

	/**
	 * Returns the column number with the given cell.
	 *
	 * @param cell the cell to find.
	 * @return the column number
	 */

	@Override
	protected int getCellPosition(CellHandle cell) {
		assert cell != null;

		TableItem table = (TableItem) element.getElement();

		return table.getColumnPosition4Cell(getModule(), (Cell) cell.getElement());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.ColumnBandAdapter#getNumberOfRows()
	 */

	@Override
	protected int getRowCount() {
		// treat the table as a regular layout.

		int numOfRows = 0;
		numOfRows += element.getHeader().getCount();

		SlotHandle groups = element.getGroups();
		for (int i = 0; i < groups.getCount(); i++) {
			GroupHandle group = (GroupHandle) groups.get(i);
			numOfRows += group.getHeader().getCount();
			numOfRows += group.getFooter().getCount();
		}

		numOfRows += element.getDetail().getCount();
		numOfRows += element.getFooter().getCount();

		return numOfRows;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.ColumnBandAdapter#getColumnCount()
	 */

	@Override
	protected int getColumnCount() {
		return element.getColumnCount();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.ColumnBandAdapter#getRow(int, int,
	 * int)
	 */

	@Override
	protected RowHandle getRow(int slotId, int groupId, int rowNumber) {

		RowHandle row = null;

		if (groupId == -1) {
			row = (RowHandle) element.getSlot(slotId).get(rowNumber);
		} else {
			GroupHandle group = (GroupHandle) element.getGroups().get(groupId);
			row = (RowHandle) group.getSlot(slotId).get(rowNumber);
		}

		return row;
	}

	/**
	 * Checks whether any cell in <code>cells</code> has a value of
	 * <code>DesignChoiceConstants#DROP_TYPE_DETAIL</code> or
	 * <code>DesignChoiceConstants#DROP_TYPE_ALL</code> for the "drop" property.
	 *
	 * @param cells a list containing cell handles
	 * @return <code>true</code> if any cell has the "drop" property, otherwise
	 *         <code>false</code>.
	 */

	@Override
	protected boolean hasDroppingCell(List cells) {
		for (int i = 0; i < cells.size(); i++) {
			CellContextInfo cellInfo = (CellContextInfo) cells.get(i);
			if (isDroppingCell(cellInfo)) {
				return true;
			}

		}
		return false;
	}

	@Override
	protected List getRowContainerSlots() {
		List list = new ArrayList();

		list.add(element.getHeader());

		SlotHandle groups = element.getGroups();
		for (int i = 0; i < groups.getCount(); i++) {
			GroupHandle group = (GroupHandle) groups.get(i);
			list.add(group.getHeader());
			list.add(group.getFooter());
		}

		list.add(element.getDetail());
		list.add(element.getFooter());

		return list;
	}

	@Override
	protected boolean isDroppingCell(CellContextInfo cellInfo) {
		String containerDefnName = cellInfo.getContainerDefnName();
		int slotId = cellInfo.getSlotId();
		if (ReportDesignConstants.TABLE_GROUP_ELEMENT.equals(containerDefnName)
				&& slotId == IGroupElementModel.HEADER_SLOT
				&& !DesignChoiceConstants.DROP_TYPE_NONE.equalsIgnoreCase(cellInfo.getDrop())) {
			return true;
		}
		return false;
	}
}

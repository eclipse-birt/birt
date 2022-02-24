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

package org.eclipse.birt.report.model.api.elements.table;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.interfaces.IGroupElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IListingElementModel;

/**
 * The table model for the content layout.
 */

public class LayoutTable {

	/**
	 * The information of HEADER, DETAIL and FOOTER slots.
	 */

	private LayoutSlot[] tableSlots;

	/**
	 * The information of GROUP_HEADER and GROUP_FOOTER slots.
	 */

	private LayoutGroupBand[] groupSlots;

	/**
	 * The unique index for each cell.
	 */

	protected int nextCellId = 1;

	/**
	 * The table element that the layout belongs to.
	 */

	protected TableItem table;

	/**
	 * The report module that the table resides.
	 */

	private Module module;

	/**
	 * The list containing
	 */

	private List overlappedCells = new ArrayList();

	/**
	 * Constructs a table with the given numbers of rows and columns.
	 * 
	 * @param table  the table element
	 * @param module the module
	 */

	public LayoutTable(TableItem table, Module module) {
		assert table != null;

		this.table = table;
		this.module = module;

		tableSlots = new LayoutSlot[IListingElementModel.FOOTER_SLOT + 1];
		for (int i = 0; i < tableSlots.length; i++)
			tableSlots[i] = null;

		groupSlots = new LayoutGroupBand[IGroupElementModel.SLOT_COUNT];
		for (int i = 0; i < groupSlots.length; i++)
			groupSlots[i] = null;
	}

	/**
	 * Returns the handle of the table that the layout belongs to.
	 * 
	 * @return the table handle
	 */

	public TableHandle getTable() {
		return (TableHandle) table.getHandle(module);
	}

	/**
	 * Returns the column count in the table.
	 * 
	 * @return the column count in the table.
	 */

	public int getColumnCount() {
		return refreshColumnCount();
	}

	/**
	 * Return a cell element with the given poistion. Uses this method to find cells
	 * in Table Header, Detail and Footer slots.
	 * 
	 * @param slotId the slot index,
	 * @param rowId  the 1-based row index
	 * @param colId  the 1-based column index
	 * @return the cell element. If no cell on the position, return
	 *         <code>null</code>.
	 */

	public CellHandle getCell(int slotId, int rowId, int colId) {
		if (slotId > tableSlots.length - 1 || rowId < 1 || colId < 1)
			return null;

		LayoutRow row = getSimpleSlot(slotId).getLayoutRow(rowId - 1);
		if (row == null)
			return null;

		LayoutCell cell = row.getLayoutCell(colId - 1);
		if (cell == null)
			return null;

		return cell.getCell();
	}

	/**
	 * Return a cell element with the given poistion. Uses this method to find cells
	 * in Table Header, Detail and Footer slots.
	 * 
	 * @param groupLevel the 1-based group level
	 * @param slotId     the slot index,
	 * @param rowId      the 1-based row index
	 * @param colId      the 1-based column index
	 * @return the cell element. If no cell on the position, return
	 *         <code>null</code>.
	 */

	public CellHandle getCell(int groupLevel, int slotId, int rowId, int colId) {
		if (slotId > groupSlots.length - 1 || rowId < 1 || colId < 1)
			return null;

		if (groupLevel > getGroupCount())
			return null;

		LayoutSlot slot = getComplexSlot(slotId).getLayoutSlotWithGroupLevel(groupLevel);
		if (slot == null)
			return null;

		LayoutRow row = slot.getLayoutRow(rowId - 1);
		if (row == null)
			return null;

		LayoutCell cell = row.getLayoutCell(colId - 1);

		if (cell == null)
			return null;

		return cell.getCell();
	}

	/**
	 * Return the row handle with the given row position. The <code>rowPosn</code>
	 * is regardless of the slot.
	 * 
	 * @param rowPosn the 1-based row position
	 * @return the row handle
	 */

	public RowHandle getRow(int rowPosn) {
		LayoutRow layoutRow = getLayoutRow(rowPosn);
		if (layoutRow == null)
			return null;

		RowHandle row = layoutRow.getRow();
		assert row != null;
		return row;
	}

	/**
	 * Returns the cell at the given position. The table is viewed as be constructed
	 * by a set of flattened rows. Each row has a set of cells. Please note that the
	 * return <code>CellHandle</code> is an element that occupies the given position
	 * in the layout rendering.
	 * <p>
	 * For example, if a cell occupies the position (1, 1) and (1, 2), return
	 * <code>CellHandle</code>s with parameters (1, 1) and (1, 2) are same.
	 * 
	 * @param rowPosn the 1-based row position
	 * @param colPosn the 1-based column position
	 * @return the cell handle at the given position
	 */

	public CellHandle getCell(int rowPosn, int colPosn) {
		LayoutRow layoutRow = getLayoutRow(rowPosn);
		if (layoutRow == null)
			return null;

		LayoutCell layoutCell = layoutRow.getLayoutCell(colPosn - 1);
		if (layoutCell == null)
			return null;

		return layoutCell.getCell();
	}

	/**
	 * Return the layout row with the given row position. The <code>rowPosn</code>
	 * is regardless of the slot.
	 * 
	 * @param rowPosn the 1-based row position
	 * @return the layout row
	 */

	private LayoutRow getLayoutRow(int rowPosn) {
		List slots = LayoutUtil.getFlattenedLayoutSlots(this);

		LayoutRow row = null;

		for (int i = 0, rowNumber = rowPosn; i < slots.size(); i++) {
			LayoutSlot slot = (LayoutSlot) slots.get(i);
			int rowCount = slot.getRowCount();

			if (rowNumber <= rowCount) {
				row = slot.getLayoutRow(rowNumber - 1);
				break;
			} else
				rowNumber -= rowCount;
		}

		return row;
	}

	/**
	 * Return the column position for a given cell. Uses this method to find cells
	 * in Table Header, Detail and Footer slots.
	 * 
	 * @param slotId the index of the slot where the cell resides
	 * @param rowId  the 0-based row index
	 * @param cell   the cell element to find
	 * @return the 1-based column position
	 */

	public int getColumnPos(int slotId, int rowId, Cell cell) {
		assert slotId == IListingElementModel.DETAIL_SLOT || slotId == IListingElementModel.HEADER_SLOT
				|| slotId == IListingElementModel.FOOTER_SLOT;

		LayoutSlot slot = getSimpleSlot(slotId);
		int colPosn = slot.getColumnPos(rowId, cell);
		if (colPosn != 0)
			return colPosn;

		return getOverlappedColumnPos(cell);
	}

	/**
	 * Finds the column position for a cell of which areas is occupied by other cell
	 * elements.
	 * 
	 * @param cell the cell element
	 * @return 1-based column position
	 */

	private int getOverlappedColumnPos(Cell cell) {
		for (int i = 0; i < overlappedCells.size(); i++) {
			OverlappedArea overlappedCell = (OverlappedArea) overlappedCells.get(i);
			if (overlappedCell.getCell() == cell) {
				assert overlappedCell.getRowSpanOffset() == 0;
				assert overlappedCell.getColSpanOffset() == 0;

				return overlappedCell.colPosn;
			}
		}

		// assert false;
		return 0;
	}

	/**
	 * Return the column position for a given cell. Uses this method to find cells
	 * in Group Header and Footer slots.
	 * 
	 * @param groupLevel the group level
	 * @param slotId     the index of the slot where the cell resides
	 * @param rowId      the 0-based row index
	 * @param cell       the cell element to find
	 * @return the 1-based column position
	 */

	public int getColumnPos(int groupLevel, int slotId, int rowId, Cell cell) {
		LayoutGroupBand groupSlot = getComplexSlot(slotId);
		LayoutSlot slot = groupSlot.getLayoutSlotWithGroupLevel(groupLevel);

		int colPosn = slot.getColumnPos(rowId, cell);

		if (colPosn != 0)
			return colPosn;

		return getOverlappedColumnPos(cell);
	}

	/**
	 * Returns the slot with the given slot index.
	 * 
	 * @param slotId the slot index
	 * @return the layout slot.
	 */

	public LayoutSlot getLayoutSlot(int slotId) {
		assert slotId == IListingElementModel.DETAIL_SLOT || slotId == IListingElementModel.HEADER_SLOT
				|| slotId == IListingElementModel.FOOTER_SLOT;

		return getSimpleSlot(slotId);
	}

	/**
	 * Returns the slot with the given slot index and the group level.
	 * 
	 * @param groupLevel the 1-based group level
	 * @param slotId     the slot index
	 * @return the layout slot.
	 */

	public LayoutSlot getLayoutSlot(int groupLevel, int slotId) {
		assert groupLevel > 0;

		if (slotId > groupSlots.length)
			return null;

		return getComplexSlot(slotId).getLayoutSlotWithGroupLevel(groupLevel);
	}

	/**
	 * Returns the slot with the given index. Used this method to get Table Header,
	 * Detail and Footer slots.
	 * 
	 * @param slotId the slot index
	 * @return the slot
	 */

	private LayoutSlot getSimpleSlot(int slotId) {
		LayoutSlot slot = (LayoutSlot) tableSlots[slotId];

		if (slot == null) {
			slot = new LayoutSlot(this, getColumnCount());
			tableSlots[slotId] = slot;
		}
		return slot;
	}

	/**
	 * Returns the header slot.
	 * 
	 * @return the header slot
	 */

	public LayoutSlot getHeader() {
		return getSimpleSlot(IListingElementModel.HEADER_SLOT);
	}

	/**
	 * Returns the detail slot.
	 * 
	 * @return the detail slot
	 */

	public LayoutSlot getDetail() {
		return getSimpleSlot(IListingElementModel.DETAIL_SLOT);
	}

	/**
	 * Returns the footer slot.
	 * 
	 * @return the footer slot
	 */

	public LayoutSlot getFooter() {
		return getSimpleSlot(IListingElementModel.FOOTER_SLOT);
	}

	/**
	 * Returns the layout group with the given group level.
	 * 
	 * @param groupLevel the 1-based group level
	 * 
	 * @return the layout group
	 */

	public LayoutGroup getLayoutGroup(int groupLevel) {
		if (groupLevel < 1 || groupLevel > getGroupCount())
			return null;

		return new LayoutGroup(this, groupLevel);
	}

	/**
	 * Returns the count of the group in the table.
	 * 
	 * @return the count of the group
	 */

	protected int getGroupCount() {
		return table.getGroups().size();
	}

	/**
	 * Returns the slot with the given index. Used this method to get Group Header
	 * and Footer slots.
	 * 
	 * @param slotId the slot index
	 * @return the slot
	 */

	private LayoutGroupBand getComplexSlot(int slotId) {
		LayoutGroupBand slot = (LayoutGroupBand) groupSlots[slotId];
		if (slot == null) {
			slot = new LayoutGroupBand(this, getColumnCount());
			groupSlots[slotId] = slot;
		}
		return slot;
	}

	/**
	 * Returns the group header slot.
	 * 
	 * @return the group header slot
	 */

	protected LayoutGroupBand getGroupHeaders() {
		return getComplexSlot(IGroupElementModel.HEADER_SLOT);
	}

	/**
	 * Returns the group footer slot.
	 * 
	 * @return the group footer slot
	 */

	protected LayoutGroupBand getGroupFooters() {
		return getComplexSlot(IGroupElementModel.FOOTER_SLOT);
	}

	/**
	 * Updates the column count of the table.
	 * 
	 * @return the column count
	 */

	private int refreshColumnCount() {
		int columnCount = 0;

		for (int i = 0; i < tableSlots.length; i++) {
			if (tableSlots[i] == null)
				continue;

			int tmpCount = tableSlots[i].getColumnCount();
			if (tmpCount > columnCount)
				columnCount = tmpCount;
		}

		for (int i = 0; i < groupSlots.length; i++) {
			if (groupSlots[i] == null)
				continue;

			int tmpCount = groupSlots[i].getColumnCount();
			if (tmpCount > columnCount)
				columnCount = tmpCount;
		}

		return columnCount;
	}

	/**
	 * Returns the string that shows the layout. Mainly for the debug.
	 * 
	 * @return the string that shows the layout
	 */

	public String getLayoutString() {
		StringBuffer sb = new StringBuffer();

		sb.append("table " + table.getFullName() + " layout: \r\n"); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append(getHeader().getLayoutString());
		sb.append(getGroupHeaders().getLayoutString());
		sb.append(getDetail().getLayoutString());
		sb.append(getGroupFooters().getLayoutString());
		sb.append(getFooter().getLayoutString());
		sb.append("\r\n"); //$NON-NLS-1$

		return sb.toString();
	}

	/**
	 * Returns the next available cell index.
	 * 
	 * @return the next available cell index.
	 */

	protected int getNextCellId() {
		int id = nextCellId;
		nextCellId++;

		return id;
	}

	/**
	 * Returns the module where the table element belongs to.
	 * 
	 * @return the module
	 */

	protected Module getModule() {
		return module;
	}

	/**
	 * Update an overlapped area of a cell element into the list. The overlapped
	 * area is the area occupied by a <code>LayoutCell</code>.
	 * 
	 * @param cell          the cell element
	 * @param slot          the layout slot where the cell resides
	 * @param rowPosn       the 1-based row position in the slost
	 * @param colPosn       the 1-based column position in the slost
	 * @param rowSpanOffset the row span offset of the overlapped area
	 * @param colSpanOffset the column span offset of the overlapped area
	 */

	protected void addOverlappedCell(Cell cell, LayoutSlot slot, int rowPosn, int colPosn, int rowSpanOffset,
			int colSpanOffset) {
		overlappedCells.add(new OverlappedArea(cell, slot, rowPosn, colPosn, rowSpanOffset, colSpanOffset));
	}

	/**
	 * Checks whether the table has overlapped areas.
	 * 
	 * @return <code>true</code> if not have. Otherwise <code>false</code>.
	 */

	protected boolean hasOverlappedArea() {
		return !overlappedCells.isEmpty();
	}

	/**
	 * Represents an overlapped area of the cell element in the table.
	 * 
	 */

	protected static class OverlappedArea {

		private Cell cell;
		private int colPosn;
		private int rowPosn;
		private LayoutSlot layoutSlot;

		private int colSpanOffset;
		private int rowSpanOffset;

		/**
		 * Constructs an <code>OverlappedArea</code> with given parameters.
		 * 
		 * @param cell          the cell that causes the overlapped area
		 * @param slot          the slot in which the overlapped occurs
		 * @param rowPosn       the row position in the slot
		 * @param columnPosn    the column position
		 * @param rowSpanOffset the 0-based offset of the row span
		 * @param colSpanOffset the 0-based offset of the column span
		 */

		private OverlappedArea(Cell cell, LayoutSlot slot, int rowPosn, int columnPosn, int rowSpanOffset,
				int colSpanOffset) {
			this.cell = cell;
			this.colPosn = columnPosn;
			this.rowPosn = rowPosn;
			this.layoutSlot = slot;
			this.colSpanOffset = colSpanOffset;
			this.rowSpanOffset = rowSpanOffset;
		}

		/**
		 * Returns the cell element.
		 * 
		 * @return the cell element
		 */

		protected Cell getCell() {
			return cell;
		}

		/**
		 * Returns the column position
		 * 
		 * @return 1-based column position
		 */

		protected int getColPosn() {
			return colPosn;
		}

		/**
		 * Returns the row position
		 * 
		 * @return 1-based row position
		 */

		protected int getRowPosn() {
			return rowPosn;
		}

		/**
		 * Returns the slot in which the overlapped occurs
		 * 
		 * @return the slot in which the overlapped occurs
		 */

		protected LayoutSlot getSlot() {
			return layoutSlot;
		}

		/**
		 * Returns the offset of the column span
		 * 
		 * @return the 0-based offset of the column span
		 */

		protected int getColSpanOffset() {
			return colSpanOffset;
		}

		/**
		 * Returns the offset of the row span
		 * 
		 * @return the 0-based offset of the row span
		 */

		protected int getRowSpanOffset() {
			return rowSpanOffset;
		}

	}
}

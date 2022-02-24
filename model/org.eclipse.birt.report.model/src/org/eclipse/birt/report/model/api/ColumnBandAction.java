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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.IDesignElement;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.ColumnHelper;
import org.eclipse.birt.report.model.elements.TableColumn;
import org.eclipse.birt.report.model.elements.interfaces.ICellModel;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;

/**
 * Provides the fundmental operations to column band operations such as:
 * copy/paste, shift a column band, etc.
 * 
 */

abstract class ColumnBandAction {

	/**
	 * Adapter to work on the grid/table columns.
	 */

	protected ColumnBandAdapter adapter = null;

	/**
	 * Constructs a default <code>ColumnBandAction</code>.
	 * 
	 * @param adapter the adapter to work on tables and grids.
	 */

	ColumnBandAction(ColumnBandAdapter adapter) {
		this.adapter = adapter;
	}

	/**
	 * Checks whether copied cells can be integrated into a rectangle.
	 * 
	 * @param cells     cloned cells
	 * @param rectWidth the column width
	 * @return <code>true</code> if the shape of integrated cells is a rectangle,
	 *         otherwise <code>false</code>.
	 */

	protected boolean isRectangleArea(List cells, int rectWidth) {
		int numOfRows = adapter.getRowCount();
		int rowCount = 0;

		for (int i = 0; i < cells.size(); i++) {
			CellContextInfo contextInfo = (CellContextInfo) cells.get(i);

			int colSpan = contextInfo.getColumnSpan();
			if (colSpan > rectWidth)
				return false;

			rowCount += contextInfo.getRowSpan();
		}

		assert rowCount <= numOfRows;

		if (rowCount < numOfRows)
			return false;

		return true;
	}

	/**
	 * Adds all column headers for an element that has no column information.
	 * 
	 * @param column       the column from the copy operation
	 * @param columnNumber the column number of <code>column</code>
	 * @param isInsert     <code>true</code> if this is an insert and paste action.
	 *                     Otherwise <code>false</code>.
	 */

	protected void addColumnHeader(TableColumn column, int columnNumber, boolean isInsert) {
		SlotHandle columns = adapter.getColumns();
		assert columns.getCount() == 0;

		// the number of columns must be cached since this number changes during
		// the execution of table.getColumnCount()

		int columnCount = adapter.getColumnCount();
		if (isInsert)
			columnCount++;

		for (int i = 0; i < columnCount; i++) {
			ColumnHandle toAdd = null;

			// either paste action or insert and paste actions.

			if ((i == columnNumber - 1 && !isInsert) || (isInsert && i == columnNumber))
				toAdd = column.handle(adapter.getModule());
			else
				toAdd = adapter.getElementHandle().getElementFactory().newTableColumn();

			try {
				columns.add(toAdd);
			} catch (SemanticException e) {
				assert false;
			}
		}
	}

	/**
	 * Checks the element after the paste action.
	 * 
	 * @param content the pasted element
	 * 
	 * @return a list containing parsing errors. Each element in the list is
	 *         <code>ErrorDetail</code>.
	 */

	private List checkElementPostPaste(DesignElementHandle content) {
		if (content == null)
			return Collections.EMPTY_LIST;

		List exceptionList = content.getElement().validateWithContents(adapter.getModule());
		List errorDetailList = ErrorDetail.convertExceptionList(exceptionList);

		return errorDetailList;
	}

	/**
	 * Returns the context information of a cell. The cell must reside in a valid
	 * row container.
	 * 
	 * @param cell the cell handle
	 * @param row  the row that contains the context information
	 * @return a new <code>CellContextInfo</code> object
	 */

	protected CellContextInfo getCellContextInfo(Cell cell, RowHandle row) {
		DesignElementHandle rowContainer = row.getContainer();
		int slotId = rowContainer.findContentSlot(row);
		int groupId = -1;
		SlotHandle slot = rowContainer.getSlot(slotId);

		if (rowContainer instanceof TableGroupHandle) {
			TableHandle rowGrandPa = (TableHandle) rowContainer.getContainer();
			groupId = rowGrandPa.getGroups().findPosn(rowContainer);
		}

		CellContextInfo cellInfo = new CellContextInfo(cell, cell.getRowSpan(adapter.getModule()),
				cell.getColSpan(adapter.getModule()),
				cell.getStringProperty(adapter.getModule(), ICellModel.DROP_PROP));

		int rowNumber = slot.findPosn(row);
		cellInfo.setContainerDefnName(rowContainer.getDefn().getName());
		cellInfo.setSlotId(slotId);
		cellInfo.setGroupId(groupId);
		cellInfo.setRowIndex(rowNumber);

		return cellInfo;
	}

	/**
	 * Returns the column index that is the start column index of the
	 * <code>target</code>.
	 * 
	 * @param target the column to find
	 * @return a column index
	 */

	// private static int getColumnStartPos( ColumnHandle target )
	// {
	// SlotHandle columns = target.getContainerSlotHandle( );
	//
	// int colStartPos = 1;
	// int colPosInSlot = columns.findPosn( target );
	// for ( int i = 0; i < colPosInSlot; i++ )
	// {
	// ColumnHandle col = (ColumnHandle) columns.get( i );
	// colStartPos += col.getRepeatCount( );
	// }
	//
	// return colStartPos;
	// }
	/**
	 * Creates a <code>SlotLayoutInfo</code> with the given cell, container name,
	 * slot id and group id.
	 * 
	 * @param cells             a list containing cell handles
	 * @param containerDefnName the definition name of the container
	 * @param slotId            the slot id
	 * @param groupId           the group id
	 * @return a <code>SlotLayoutInfo</code> object
	 */

	private SlotLayoutInfo getLayoutOfSlot(List cells, String containerDefnName, int slotId, int groupId) {
		SlotLayoutInfo layoutInfo = new SlotLayoutInfo(containerDefnName, slotId, groupId);
		for (int i = 0; i < cells.size(); i++) {
			Object obj = cells.get(i);

			String tmpDefnName = null;
			int tmpSlotId = IDesignElementModel.NO_SLOT;
			int tmpGroupId = -1;

			CellContextInfo contextInfo = (CellContextInfo) obj;
			tmpDefnName = contextInfo.getContainerDefnName();
			tmpSlotId = contextInfo.getSlotId();
			tmpGroupId = contextInfo.getGroupId();

			if (containerDefnName.equals(tmpDefnName) && tmpSlotId == slotId && tmpGroupId == groupId)
				layoutInfo.addCell(contextInfo.getCell(), contextInfo.getRowSpan());
		}

		return layoutInfo;
	}

	/**
	 * Checks whether layouts in source element and destination element are the
	 * same. It is considered as the same if there are same numbers of rows in
	 * source and destination elements.
	 * 
	 * @param copiedCells the copied cells.
	 * @param targetCells the target cells to be replaced.
	 * @return <code>true</code> if layouts are exactly same. <code>false</code> if
	 *         two elements have the same number of rows in slot but cells have
	 *         different rowSpan values.
	 * @throws SemanticException if number of rows in slots of the source and
	 *                           destination are different.
	 */

	protected boolean isSameLayout(List copiedCells, List targetCells) throws SemanticException {
		String oldContainerDefnName = null;
		int oldSlotId = IDesignElementModel.NO_SLOT;
		int oldGroupId = -1;

		for (int i = 0; i < copiedCells.size(); i++) {
			CellContextInfo contextInfo = (CellContextInfo) copiedCells.get(i);

			String containerDefnName = contextInfo.getContainerDefnName();
			int slotId = contextInfo.getSlotId();
			int groupId = contextInfo.getGroupId();

			if (!containerDefnName.equals(oldContainerDefnName) || slotId != oldSlotId || groupId != oldGroupId) {
				SlotLayoutInfo info1 = getLayoutOfSlot(copiedCells, containerDefnName, slotId, groupId);
				SlotLayoutInfo info2 = getLayoutOfSlot(targetCells, containerDefnName, slotId, groupId);

				if (!info1.isSameNumOfRows(info2))
					throw new SemanticError(adapter.getElementHandle().getElement(),
							new String[] { adapter.getElementHandle().getName() },
							SemanticError.DESIGN_EXCEPTION_COLUMN_PASTE_FORBIDDEN);

				if (!info1.isSameLayoutOfRows(info2))
					return false;
			}
			oldContainerDefnName = containerDefnName;
			oldSlotId = slotId;
			oldGroupId = groupId;
		}

		return true;
	}

	/**
	 * Pastes the copied column <code>column</code> to the given
	 * <code>columnNumber</code> in the target element.
	 * 
	 * @param column       the copied column
	 * @param columnNumber the column number
	 * @param isInsert     <code>true</code> if this is an insert and paste action.
	 *                     Otherwise <code>false</code>.
	 * @throws SemanticException if any error occurs during pasting a column header
	 */

	protected void pasteColumn(TableColumn column, int columnNumber, boolean isInsert) throws SemanticException {
		TableColumn targetColumn = null;
		SlotHandle columns = adapter.getColumns();

		if (columns.getCount() == 0 && column == null)
			return;

		if (columns.getCount() == 0 && column != null) {
			addColumnHeader(column, columnNumber, isInsert);
			return;
		}

		if (isInsert && columnNumber == 0) {
			columns.add(column.handle(adapter.getModule()), 0);
			return;
		}

		targetColumn = ColumnHelper.findColumn(adapter.getModule(), columns.getSlot(), columnNumber);

		replaceColumn(column, targetColumn.handle(adapter.getModule()), columnNumber, isInsert);
	}

	/**
	 * Replaces the <code>target</code> column with the given <code>source</code>
	 * column at the given column number.
	 * 
	 * @param source       the column to replace
	 * @param target       the column to be replaced
	 * @param columnNumber the column number
	 * @param isInsert     <code>true</code> if this is an insert action instead of
	 *                     an paste action
	 * @throws SemanticException if error is encountered when adding or dropping
	 *                           elements.
	 */

	private void replaceColumn(TableColumn source, ColumnHandle target, int columnNumber, boolean isInsert)
			throws SemanticException {
		SlotHandle columns = target.getContainerSlotHandle();

		int colStartPos = ColumnBandAdapter.getColumnStartPos(target);
		int colEndPos = colStartPos + +target.getRepeatCount() - 1;

		ColumnHandle toAdd = null;
		if (source == null)
			toAdd = target.getElementFactory().newTableColumn();
		else
			toAdd = (ColumnHandle) source.getHandle(adapter.getModule());

		int oldPos = columns.findPosn(target);

		// removes the column required.

		if (target.getRepeatCount() == 1) {
			if (isInsert)
				oldPos++;
			else
				columns.drop(target);

			columns.add(toAdd, oldPos);
			return;
		}

		assert target.getRepeatCount() > 1;

		// the new column is replaced at the beginning or end the target column

		if ((!isInsert && (columnNumber == colStartPos || columnNumber == colEndPos))
				|| (isInsert && (columnNumber == colEndPos))) {
			// if it is only a paste operation, must tune the repeat count.

			if (!isInsert)
				target.setRepeatCount(target.getRepeatCount() - 1);

			int pos = oldPos;
			if (columnNumber != colStartPos)
				pos++;

			columns.add(toAdd, pos);
			return;
		}

		// the new column is replaced at the center of the target column (not
		// beginning or the end) for the paste operation.

		// the new column is in the start column index for the insert and paste
		// operation.

		if (((columnNumber > colStartPos && columnNumber < colEndPos)) || (isInsert && columnNumber == colStartPos)) {
			int repeat1 = columnNumber - colStartPos;

			// if is a insert and paste operation, do not reduce the repeat
			// count.

			if (isInsert)
				repeat1++;

			int repeat2 = target.getRepeatCount() - repeat1;

			// if is a paste operation, reduce the repeat count.

			if (!isInsert)
				repeat2 -= 1;

			ColumnHandle newColumn = null;

			try {
				newColumn = (ColumnHandle) ((IDesignElement) target.getElement().clone())
						.getHandle(adapter.getModule());
			} catch (CloneNotSupportedException e) {
				assert false;
				return;
			}

			target.setRepeatCount(repeat1);
			newColumn.setRepeatCount(repeat2);
			int pos = oldPos;
			columns.add(toAdd, pos + 1);
			columns.add(newColumn, pos + 2);
		}
	}

	/**
	 * Returns the context information for a list of cells. Cells must reside in a
	 * valid row container.
	 * 
	 * @param cells a list of cell handles
	 * @return a list containing new <code>CellContextInfo</code> objects.
	 */

	protected List getCellsContextInfo(List cells) {
		List list = new ArrayList();

		for (int i = 0; i < cells.size(); i++) {
			CellHandle cell = (CellHandle) cells.get(i);
			list.add(getCellContextInfo((Cell) cell.getElement(), (RowHandle) cell.getContainer()));
		}

		return list;
	}

	/**
	 * Checks element references after the paste operation.
	 * 
	 * @param column the column to check
	 * @param cells  cells to check
	 * 
	 * @return a list containing post-parsing errors. Each element in the list is
	 *         <code>ErrorDetail</code>.
	 */

	protected List doPostPasteCheck(TableColumn column, List cells) {
		List list = Collections.EMPTY_LIST;

		if (column != null)
			list = checkElementPostPaste(column.getHandle(adapter.getModule()));

		for (int i = 0; i < cells.size(); i++) {
			CellContextInfo contextInfo = (CellContextInfo) cells.get(i);
			CellHandle cell = contextInfo.getCell().handle(adapter.getModule());
			list.addAll(checkElementPostPaste(cell));
		}
		return list;
	}

	/**
	 * Represents the layout of a slot. The information includes the container of
	 * the slot, the slot id, the group id and rows in the slot.
	 */

	private static class SlotLayoutInfo {

		/**
		 * Rows in the slot.
		 */

		private List details = new ArrayList();

		/**
		 * The definition name of the container.
		 */

		private String containerDefnName;

		/**
		 * The slot Id.
		 */

		private int slotId;

		/**
		 * The group id. If the slot is not in the group, this value is -1.
		 */

		private int groupId;

		/**
		 * Constructs a <code>SlotLayoutInfo</code> for the given slot.
		 * 
		 * @param containerDefnName the definition name of the container.
		 * @param slotId            the slot id
		 * @param groupId           the group id
		 */

		protected SlotLayoutInfo(String containerDefnName, int slotId, int groupId) {
			this.containerDefnName = containerDefnName;
			this.slotId = slotId;
			this.groupId = groupId;
		}

		/**
		 * Adds a cell to the slot layout information.
		 * 
		 * @param cell    the cell handle
		 * @param rowSpan the row span
		 */

		protected void addCell(Cell cell, int rowSpan) {
			details.add(Integer.valueOf(rowSpan));
		}

		/**
		 * Checks whether numbers of rows in two <code>SlotLayoutInfo</code> are same.
		 * 
		 * @param info the slot information
		 * @return <code>true</code> if two numbers are same. Otherwise
		 *         <code>false</code>.
		 */

		public boolean isSameNumOfRows(SlotLayoutInfo info) {
			if (!containerDefnName.equals(info.containerDefnName))
				return false;

			if (slotId != info.slotId || groupId != info.groupId)
				return false;

			int myNumOfRows = getNumOfRows();
			int targetNumOfRows = info.getNumOfRows();

			return (myNumOfRows == targetNumOfRows);
		}

		/**
		 * Checks whether layout information in two <code>SlotLayoutInfo</code> are
		 * same.
		 * 
		 * @param info the slot information
		 * @return <code>true</code> if layout information is same. Otherwise
		 *         <code>false</code>.
		 */

		public boolean isSameLayoutOfRows(SlotLayoutInfo info) {
			if (details.size() != info.details.size())
				return false;

			for (int i = 0; i < details.size(); i++) {
				Integer myRowSpan = (Integer) details.get(i);

				Object targetRowSpan = info.details.get(i);
				if (!myRowSpan.equals(targetRowSpan))
					return false;
			}

			return true;
		}

		private int getNumOfRows() {
			int numOfRows = 0;
			for (int i = 0; i < details.size(); i++) {
				Integer rowSpan = (Integer) details.get(i);
				numOfRows += rowSpan.intValue();
			}

			return numOfRows;
		}

	}
}

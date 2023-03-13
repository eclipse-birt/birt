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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.elements.ColumnHelper;
import org.eclipse.birt.report.model.elements.TableColumn;
import org.eclipse.birt.report.model.elements.interfaces.ITableColumnModel;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.util.CommandLabelFactory;

/**
 * The action to shift one column from one position to another in the same
 * Table/Grid.
 *
 */

class ColumnBandShiftAction extends ColumnBandAction {

	private boolean weakMode;

	/**
	 * Constructs a default <code>ColumnBandShiftAction</code>.
	 *
	 * @param adapter the adapter to work on tables and grids.
	 */

	public ColumnBandShiftAction(ColumnBandAdapter adapter) {
		super(adapter);
	}

	public ColumnBandShiftAction(ColumnBandAdapter adapter, boolean weakMode) {
		this(adapter);
		this.weakMode = weakMode;
	}

	/**
	 * Returns the shift data of the column band.
	 *
	 * @param sourceIndex the source column number to shift
	 * @return a <code>ColumnBandData</code> object containing the data to shift
	 * @throws SemanticException if the source column is forbidden to shift because
	 *                           of column spans and dropping properties.
	 */

	protected ColumnBandData getShiftData(int sourceIndex) throws SemanticException {

		if (sourceIndex <= 0) {
			throw new IllegalArgumentException("wrong column to shift"); //$NON-NLS-1$
		}

		ColumnBandData data = new ColumnBandData();

		TableColumn column = ColumnHelper.findColumn(adapter.getModule(), adapter.getColumns().getSlot(), sourceIndex);

		if (column != null) {
			try {
				column = (TableColumn) column.clone();
				column.setProperty(ITableColumnModel.REPEAT_PROP, Integer.valueOf(1));
				data.setColumn(column);
			} catch (CloneNotSupportedException e) {
				assert false;
			}
		}

		List cells = getCellsContextInfo(adapter.getCellsUnderColumn(sourceIndex));

		if (weakMode) {
			cells = filter(cells, true);
		}

		data.setCells(cells);

		if ((!weakMode && !isRectangleArea(cells, 1)) || (!weakMode && adapter.hasDroppingCell(cells))) {
			throw new SemanticError(adapter.getElementHandle().getElement(),
					new String[] { Integer.toString(sourceIndex), adapter.getElementHandle().getName() },
					SemanticError.DESIGN_EXCEPTION_COLUMN_COPY_FORBIDDEN);
		}

		return data;
	}

	private List filter(List cellContextInfos, boolean valid) {
		List retval = new ArrayList();
		for (Object obj : cellContextInfos) {
			CellContextInfo cellInfo = (CellContextInfo) obj;
			if (!(valid ^ isValid(cellInfo))) {
				retval.add(cellInfo);
			}
		}
		return retval;
	}

	private boolean isValid(CellContextInfo cellInfo) {
		return cellInfo.getColumnSpan() == 1 || adapter.isDroppingCell(cellInfo);
	}

	/**
	 * Returns the actual position for the shift action. This is to keep consistent
	 * with the behavior in <code>ShiftHandle</code> and
	 * <code>PropertyHandle</code>.
	 *
	 * @param posn    the source position
	 * @param newPosn the new position
	 * @return the column destination position ready for the shift action
	 */

	private int adjustDestPosn(int posn, int newPosn) {
		int columnCount = adapter.getColumnCount();

		if (newPosn > columnCount) {
			return columnCount;
		}

		// If the new position is the same as the old, then skip the operation.

		if (posn == newPosn) { // )
			return -1;
		}

		return newPosn;
	}

	/**
	 * Moves one column band from <code>sourceColumn</code> to
	 * <code>destColumn</code>.
	 *
	 * @param sourceColumn the source column to shift
	 * @param destColumn   the target column to shift
	 * @throws SemanticException
	 */

	protected void shiftColumnBand(int sourceColumn, int destColumn) throws SemanticException {

		ColumnBandData data = getShiftData(sourceColumn);

		// If the new position is the same as the old, then skip the operation.

		int newPosn = adjustDestPosn(sourceColumn, destColumn);
		if (newPosn == -1) {
			return;
		}

		if (weakMode) {
			List invalidCells = filter(getCellsContextInfo(adapter.getCellsUnderColumn(destColumn)), false);
			for (Object obj : invalidCells) {
				CellContextInfo invalidCell = (CellContextInfo) obj;
				Iterator iter = data.getCells().iterator();
				while (iter.hasNext()) {
					CellContextInfo cell = (CellContextInfo) iter.next();
					if (cell.getSlotId() == invalidCell.getSlotId() && cell.getRowIndex() == invalidCell.getRowIndex()
							&& cell.getGroupId() == invalidCell.getGroupId()) {
						iter.remove();
					}
				}
			}
		}

		// shift in the same table, the layout must be same.

		if (!weakMode && !checkTargetColumn(sourceColumn, destColumn)) {
			throw new SemanticError(adapter.getElementHandle().getElement(),
					new String[] { Integer.toString(sourceColumn), adapter.getElementHandle().getName() },
					SemanticError.DESIGN_EXCEPTION_COLUMN_PASTE_FORBIDDEN);
		}

		ActivityStack as = adapter.getModule().getActivityStack();

		try {
			if (adapter instanceof TableColumnBandAdapter) {
				as.startSilentTrans(CommandLabelFactory.getCommandLabel(MessageConstants.SHIFT_COLUMN_BAND_MESSAGE));
			} else {
				as.startTrans(CommandLabelFactory.getCommandLabel(MessageConstants.SHIFT_COLUMN_BAND_MESSAGE));
			}

			shiftColumn(data.getColumn(), sourceColumn, newPosn);
			shiftCells(data.getCells(), sourceColumn, newPosn);
		} catch (SemanticException e) {
			as.rollback();
			throw e;
		}

		as.commit();
	}

	/**
	 * Moves one column from <code>sourceColumn</code> to <code>destColumn</code>.
	 *
	 * @param column      the column involved in shift action
	 * @param sourceIndex the source column to shift
	 * @param destIndex   the target column to shift
	 * @throws SemanticException if any error occurs during moving the column
	 */

	private void shiftColumn(TableColumn column, int sourceIndex, int destIndex) throws SemanticException {
		if (column == null) {
			return;
		}

		SlotHandle columns = adapter.getColumns();
		TableColumn sourceColumn = ColumnHelper.findColumn(adapter.getModule(), columns.getSlot(), sourceIndex);
		ColumnHandle sourceCol = (ColumnHandle) sourceColumn.getHandle(adapter.getModule());

		pasteColumn(column, destIndex, true);

		int repeat = sourceCol.getRepeatCount();

		if (repeat == 1) {
			columns.drop(sourceCol);
		} else {
			sourceCol.setRepeatCount(repeat - 1);
		}
	}

	/**
	 * Moves cells from one column band to another column band.
	 *
	 * @param cellInfos   a list containing information for cells to shift
	 * @param sourceIndex the source column to shift
	 * @param destIndex   the target column to shift
	 * @throws SemanticException if any error occurs during moving cells
	 */

	private void shiftCells(List cellInfos, int sourceIndex, int destIndex) throws SemanticException {
		int targetIndex = destIndex;

		List destCells = adapter.getCellsUnderColumn(destIndex);
		List destContexts = getCellsContextInfo(destCells);
		// adds the copied cells to the destination.

		for (int i = 0; i < cellInfos.size(); i++) {
			CellContextInfo contextInfo = (CellContextInfo) cellInfos.get(i);

			// groupId is equal to -1, means this is a top slot in the table

			RowHandle row = adapter.getRow(contextInfo.getSlotId(), contextInfo.getGroupId(),
					contextInfo.getRowIndex());

			assert row != null;

			CellHandle cell = contextInfo.getCell().handle(adapter.getModule());
			cell.setColumn(0);

			int oldPosn = row.getCells().findPosn(cell);

			// convert destIndex to position in slot
			int newPosn = 0;
			if (destIndex == 0) {
				newPosn = 0;
			} else {
				CellContextInfo destContext = findCorrespondingCell(destContexts, contextInfo);
				if (destContext == null) {
					continue;
				}
				CellHandle destCell = destContext.getCell().handle(adapter.getModule());
				newPosn = row.getCells().findPosn(destCell);
				// adjust the position since the rule is first drop then add.

				if (oldPosn > newPosn + 1) {
					newPosn = newPosn + 1;
				}
			}

			row.getCells().shift(cell, newPosn);

			clearsCellColumnProperties(row, oldPosn, newPosn);

		}
	}

	private CellContextInfo findCorrespondingCell(List destContexts, CellContextInfo srcCell) {
		for (Object obj : destContexts) {
			CellContextInfo cell = (CellContextInfo) obj;
			if (cell.getSlotId() == srcCell.getSlotId() && cell.getRowIndex() == srcCell.getRowIndex()
					&& cell.getGroupId() == srcCell.getGroupId()) {
				return cell;
			}
		}
		return null;
	}

	/**
	 * Checks whether the paste operation can be done with the given copied column
	 * band data, the column index and the operation flag.
	 *
	 * @param row      the row handle
	 * @param fromPosn the source position in the shift
	 * @param toPosn   the destination position in the shift
	 * @throws SemanticException
	 */

	protected void clearsCellColumnProperties(RowHandle row, int fromPosn, int toPosn) throws SemanticException {
		int fromIndex = fromPosn;
		int endIndex = toPosn;

		if (fromPosn > toPosn) {
			fromIndex = toPosn;
			endIndex = fromPosn;
		}

		if (row.getCells().getCount() <= endIndex) {
			endIndex = row.getCells().getCount() - 1;
		}

		for (int i = fromIndex; i <= endIndex; i++) {
			CellHandle cell = (CellHandle) row.getCells().get(i);
			cell.setColumn(0);
		}
	}

	/**
	 * Checks whether the paste operation can be done with the given copied column
	 * band data, the column index and the operation flag.
	 *
	 * @param sourceColumn the source column to shift
	 * @param destColumn   the target column to shift
	 * @return <code>true</code> indicates the paste operation can be done.
	 *         Otherwise <code>false</code>.
	 */

	protected boolean checkTargetColumn(int sourceColumn, int destColumn) {
		// if table has parent, its layout can't be changed. so can't do insert
		// operation.

		if (adapter.hasParent()) {
			return false;
		}

		// If the new position is the same as the old, then skip the operation.

		int newPosn = adjustDestPosn(sourceColumn, destColumn);
		if (newPosn == -1) {
			return true;
		}

		int columnCount = adapter.getColumnCount();
		if (newPosn == 0 || newPosn == columnCount) {
			return true;
		}

		List originalCells = getCellsContextInfo(adapter.getCellsUnderColumn(newPosn));

		if (!isRectangleArea(originalCells, 1)) {
			return false;
		}

		return true;
	}

}

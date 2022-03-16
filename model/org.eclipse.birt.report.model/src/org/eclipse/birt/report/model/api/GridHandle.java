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

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.IDesignElement;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.CellHelper;
import org.eclipse.birt.report.model.elements.GridItem;
import org.eclipse.birt.report.model.elements.TableRow;
import org.eclipse.birt.report.model.elements.interfaces.IGridItemModel;

/**
 * Represents a grid item in the design. A grid item contains a set of report
 * items arranged into a grid. Grids contains rows and columns. The grid
 * contains cells. Each cell can span one or more columns, or one or more rows.
 * Each cell can contain one or more items.
 * <p>
 * Grid layout is familiar to anyone who has used HTML tables, Word tables or
 * Excel: data is divided into a series of rows and columns.
 *
 * @see org.eclipse.birt.report.model.elements.GridItem
 */

public class GridHandle extends ReportItemHandle implements IGridItemModel {

	/**
	 * Constructs a grid handle with the given design and the design element. The
	 * application generally does not create handles directly. Instead, it uses one
	 * of the navigation methods available on other element handles.
	 *
	 * @param module  the module
	 * @param element the model representation of the element
	 */

	public GridHandle(Module module, DesignElement element) {
		super(module, element);
	}

	/**
	 * Returns a slot handle for the columns in the grid.
	 *
	 * @return a handle to the column slot
	 * @see SlotHandle
	 */

	public SlotHandle getColumns() {
		return getSlot(IGridItemModel.COLUMN_SLOT);
	}

	/**
	 * Returns a slot handle for the rows in the grid.
	 *
	 * @return a handle to the row slot
	 * @see SlotHandle
	 */

	public SlotHandle getRows() {
		return getSlot(IGridItemModel.ROW_SLOT);
	}

	/**
	 * Returns the number of columns in the Grid. The number is defined as the sum
	 * of columns described in the "column" slot.
	 *
	 * @return the number of columns in the grid.
	 */

	public int getColumnCount() {
		return ((GridItem) getElement()).getColumnCount(module);
	}

	/**
	 * Gets the cell at the position where the given row and column intersect.
	 *
	 * @param row    the row position indexing from 1
	 * @param column the column position indexing from 1
	 * @return the cell handle at the position if the cell exists, otherwise
	 *         <code>null</code>
	 */

	public CellHandle getCell(int row, int column) {
		Cell cell = CellHelper.findCell(getModule(), (GridItem) getElement(), row, column);

		if (cell == null) {
			return null;
		}
		return cell.handle(getModule());
	}

	/**
	 * Gets the content slot handle of the cell at the position where the given row
	 * and column intersect.
	 *
	 * @param row    the row position indexing from 1
	 * @param column the column position indexing from 1
	 * @return the content slot handle of the cell at the position if the cell
	 *         exists, otherwise <code>null</code>
	 */

	public SlotHandle getCellContent(int row, int column) {
		CellHandle cell = getCell(row, column);
		if (cell == null) {
			return null;
		}
		return cell.getContent();
	}

	/**
	 * Copies a column and cells under it with the given column number.
	 *
	 * @param columnIndex the column position indexing from 1.
	 * @return <code>true</code> if this column band can be copied. Otherwise
	 *         <code>false</code>.
	 */

	public boolean canCopyColumn(int columnIndex) {
		ColumnBandPasteAction pasteAction = new ColumnBandPasteAction(new GridColumnBandAdapter(this));

		try {
			pasteAction.copyColumnBand(columnIndex);
		} catch (SemanticException e) {
			return false;
		}

		return true;
	}

	/**
	 * Copies a column and cells under it with the given column number.
	 *
	 * @param columnIndex the column number
	 * @return a new <code>GridColumnBandAdapter</code> instance
	 * @throws SemanticException if the cell layout of the column is invalid.
	 */

	public ColumnBandData copyColumn(int columnIndex) throws SemanticException {
		// GridColumnBandAdapter adapter = new GridColumnBandAdapter( );
		// return adapter.copyColumn( this, columnIndex );
		ColumnBandPasteAction pasteAction = new ColumnBandPasteAction(new GridColumnBandAdapter(this));

		return pasteAction.copyColumnBand(columnIndex);
	}

	/**
	 * Pastes a column with its cells to the given column number.
	 *
	 * @param data         the data of a column band to paste
	 * @param columnNumber the column index from 1 to the number of columns in the
	 *                     grid
	 * @param inForce      <code>true</code> if pastes the column regardless of the
	 *                     warning. Otherwise <code>false</code>.
	 * @throws SemanticException
	 */

	public void pasteColumn(ColumnBandData data, int columnNumber, boolean inForce) throws SemanticException {
		if (data == null) {
			throw new IllegalArgumentException("empty column to paste."); //$NON-NLS-1$
		}

		// GridColumnBandAdapter adapter = new GridColumnBandAdapter( data );
		// adapter.pasteColumnBand( this, columnNumber, inForce );
		ColumnBandPasteAction pasteAction = new ColumnBandPasteAction(new GridColumnBandAdapter(this));

		pasteAction.pasteColumnBand(columnNumber, inForce, data);
	}

	/**
	 * Checks whether the paste operation can be done with the given copied column
	 * band data, the column index and the operation flag.
	 *
	 * @param data        the column band data to paste
	 * @param columnIndex the column index from 1 to the number of columns in the
	 *                    grid
	 * @param inForce     <code>true</code> indicates to paste the column regardless
	 *                    of the different layout of cells. <code>false</code>
	 *                    indicates not.
	 * @return <code>true</code> indicates the paste operation can be done.
	 *         Otherwise <code>false</code>.
	 */

	public boolean canPasteColumn(ColumnBandData data, int columnIndex, boolean inForce) {
		if (data == null) {
			throw new IllegalArgumentException("empty column to check."); //$NON-NLS-1$
		}

		ColumnBandPasteAction pasteAction = new ColumnBandPasteAction(new GridColumnBandAdapter(this));

		return pasteAction.canPaste(columnIndex, inForce, data);
		// GridColumnBandAdapter adapter = new GridColumnBandAdapter( data );
		// return adapter.canPaste( this, columnIndex, inForce );
	}

	/**
	 * Inserts and pastes a column with its cells to the given column number.
	 *
	 * @param data         the data of a column band to paste
	 * @param columnNumber the column index from 0 to the number of columns in the
	 *                     grid
	 * @throws SemanticException
	 */

	public void insertAndPasteColumn(ColumnBandData data, int columnNumber) throws SemanticException {
		if (data == null) {
			throw new IllegalArgumentException("empty column to paste."); //$NON-NLS-1$
		}

		// GridColumnBandAdapter adapter = new GridColumnBandAdapter( data );
		// adapter.insertAndPasteColumnBand( this, columnIndex );
		ColumnBandInsertPasteAction insertAction = new ColumnBandInsertPasteAction(new GridColumnBandAdapter(this));

		insertAction.insertAndPasteColumnBand(columnNumber, data);
	}

	/**
	 * Checks whether the insert and paste operation can be done with the given
	 * copied column band data, the column index and the operation flag. This is
	 * different from <code>canPasteColumn</code> since this action creates an extra
	 * column for the table.
	 *
	 * @param data        the column band data to paste
	 * @param columnIndex the column index from 0 to the number of columns in the
	 *                    grid
	 * @return <code>true</code> indicates the paste operation can be done.
	 *         Otherwise <code>false</code>.
	 */

	public boolean canInsertAndPasteColumn(ColumnBandData data, int columnIndex) {
		if (data == null) {
			throw new IllegalArgumentException("empty column to check."); //$NON-NLS-1$
		}

		ColumnBandInsertPasteAction insertAction = new ColumnBandInsertPasteAction(new GridColumnBandAdapter(this));

		return insertAction.canInsertAndPaste(columnIndex, data);
	}

	/**
	 * Moves the column from <code>sourceColumn</code> to <code>destIndex</code> .
	 *
	 * @param sourceColumn the source column ranging from 1 to the column number
	 * @param destColumn   the target column ranging from 0 to the column number
	 * @throws SemanticException if the chosen column band is forbidden to shift
	 */

	public void shiftColumn(int sourceColumn, int destColumn) throws SemanticException {

		ColumnBandShiftAction shiftAction = new ColumnBandShiftAction(new GridColumnBandAdapter(this));
		shiftAction.shiftColumnBand(sourceColumn, destColumn);
	}

	/**
	 * Moves the column from <code>sourceColumn</code> to <code>destColumn</code>.
	 *
	 * @param sourceColumn the source column ranging from 1 to the column number
	 * @param destColumn   the target column ranging from 0 to the column number
	 * @return <code>true</code> if the chosen column band is legal to shift.
	 *         Otherwise <code>false</code>.
	 */

	public boolean canShiftColumn(int sourceColumn, int destColumn) {
		ColumnBandShiftAction shiftAction = new ColumnBandShiftAction(new GridColumnBandAdapter(this));

		try {
			shiftAction.getShiftData(sourceColumn);
		} catch (SemanticException e) {
			return false;
		}
		return shiftAction.checkTargetColumn(sourceColumn, destColumn);
	}

	/**
	 * Checks whether the copy operation can be done with the given parameters.
	 *
	 * @param parameters parameters needed by insert operation.
	 * @return <code>true</code> if this row band can be copied. Otherwise
	 *         <code>false</code>.
	 *
	 */

	public boolean canCopyRow(RowOperationParameters parameters) {
		if (parameters == null) {
			return false;
		}
		RowBandCopyAction action = new RowBandCopyAction(new GridRowBandAdapter(this));

		return action.canCopy(parameters);
	}

	/**
	 * Checks whether the paste operation can be done with the given parameters.
	 *
	 * @param copiedRow  the copied table row
	 * @param parameters parameters needed by insert operation.
	 * @return <code>true</code> indicates the paste operation can be done.
	 *         Otherwise <code>false</code>.
	 */

	public boolean canPasteRow(IDesignElement copiedRow, RowOperationParameters parameters) {
		if (copiedRow == null || parameters == null || !(copiedRow instanceof TableRow)) {
			return false;
		}
		RowBandPasteAction pasteAction = new RowBandPasteAction(new GridRowBandAdapter(this));

		return pasteAction.canPaste((TableRow) copiedRow, parameters);
	}

	/**
	 * Checks whether the insert operation can be done with the given parameters.
	 *
	 * @param parameters parameters needed by insert operation.
	 * @return <code>true</code> indicates the insert operation can be done.
	 *         Otherwise <code>false</code>.
	 */
	public boolean canInsertRow(RowOperationParameters parameters) {
		if (parameters == null) {
			return false;
		}
		RowBandInsertAction pasteAction = new RowBandInsertAction(new GridRowBandAdapter(this));

		return pasteAction.canInsert(parameters);
	}

	/**
	 * Checks whether the insert and paste table row to the given destination row
	 * with the given parameters.
	 *
	 * @param copiedRow  the copied table row
	 * @param parameters parameters needed by insert operation.
	 * @return <code>true</code> indicates the insert and paste operation can be
	 *         done. Otherwise <code>false</code>.
	 */

	public boolean canInsertAndPasteRow(IDesignElement copiedRow, RowOperationParameters parameters) {
		if (copiedRow == null || parameters == null || !(copiedRow instanceof TableRow)) {
			return false;
		}

		RowBandInsertAndPasteAction action = new RowBandInsertAndPasteAction(new GridRowBandAdapter(this));

		return action.canInsertAndPaste((TableRow) copiedRow, parameters);
	}

	/**
	 * Checks whether the shift operation can be done with the given the given
	 * parameters.
	 *
	 * @param parameters parameters needed by insert operation.
	 * @return <code>true</code> indicates the shift operation can be done.
	 *         Otherwise <code>false</code>.
	 */
	public boolean canShiftRow(RowOperationParameters parameters) {
		if (parameters == null) {
			return false;
		}
		RowBandShiftAction action = new RowBandShiftAction(new GridRowBandAdapter(this));

		return action.canShift(parameters);
	}

	/**
	 * Copies table row with the given parameters.
	 *
	 * @param parameters parameters needed by insert operation.
	 * @return a new <code>TableRow</code> instance
	 * @throws SemanticException        throw if paste operation is forbidden
	 * @throws IllegalArgumentException throw if the input parameters are not valid
	 */
	public IDesignElement copyRow(RowOperationParameters parameters) throws SemanticException {
		if (parameters == null) {
			throw new IllegalArgumentException("empty row to copy.");//$NON-NLS-1$
		}
		RowBandCopyAction action = new RowBandCopyAction(new GridRowBandAdapter(this));

		return action.doCopy(parameters);

	}

	/**
	 * Pastes table row to destination row with the given parameters.
	 *
	 * @param copiedRow  the copied table row
	 * @param parameters parameters needed by insert operation.
	 * @throws SemanticException        throw if paste operation is forbidden
	 * @throws IllegalArgumentException throw if the input parameters are not valid
	 */

	public void pasteRow(IDesignElement copiedRow, RowOperationParameters parameters) throws SemanticException {
		if (copiedRow == null || parameters == null || !(copiedRow instanceof TableRow)) {
			throw new IllegalArgumentException("empty row to paste.");//$NON-NLS-1$
		}

		RowBandPasteAction pasteAction = new RowBandPasteAction(new GridRowBandAdapter(this));

		pasteAction.doPaste((TableRow) copiedRow, parameters);
	}

	/**
	 * Inserts table row to the given destination row with the given parameters.
	 *
	 * @param parameters parameters needed by insert operation.
	 * @throws SemanticException        throw if paste operation is forbidden
	 * @throws IllegalArgumentException throw if the input parameters are not valid
	 */

	public void insertRow(RowOperationParameters parameters) throws SemanticException {
		if (parameters == null) {
			throw new IllegalArgumentException("empty row to insert.");//$NON-NLS-1$
		}
		RowBandInsertAction action = new RowBandInsertAction(new GridRowBandAdapter(this));

		action.doInsert(parameters);
	}

	/**
	 * Inserts and paste table row to the given destination row with the given
	 * parameters.
	 *
	 * @param copiedRow  the copied table row
	 * @param parameters parameters needed by insert operation.
	 * @throws SemanticException        throw if paste operation is forbidden
	 * @throws IllegalArgumentException throw if the input parameters are not valid
	 */

	public void insertAndPasteRow(IDesignElement copiedRow, RowOperationParameters parameters)
			throws SemanticException {
		if (copiedRow == null || parameters == null || !(copiedRow instanceof TableRow)) {
			throw new IllegalArgumentException("empty row to insert and paste.");//$NON-NLS-1$
		}

		RowBandInsertAndPasteAction action = new RowBandInsertAndPasteAction(new GridRowBandAdapter(this));

		action.doInsertAndPaste((TableRow) copiedRow, parameters);
	}

	/**
	 * Shifts table row to the given destination row with the given parameters.
	 *
	 * @param parameters parameters needed by insert operation.
	 * @throws SemanticException        throw if paste operation is forbidden
	 * @throws IllegalArgumentException throw if the input parameters are not valid
	 */

	public void shiftRow(RowOperationParameters parameters) throws SemanticException {
		if (parameters == null) {
			throw new IllegalArgumentException("empty row to shift.");//$NON-NLS-1$
		}
		RowBandShiftAction action = new RowBandShiftAction(new GridRowBandAdapter(this));

		action.doShift(parameters);
	}

	/**
	 * Returns the caption text of this grid.
	 *
	 * @return the caption text
	 */

	public String getCaption() {
		return getStringProperty(IGridItemModel.CAPTION_PROP);
	}

	/**
	 * Sets the caption text of this grid.
	 *
	 * @param caption the caption text
	 * @throws SemanticException if the property is locked.
	 */

	public void setCaption(String caption) throws SemanticException {
		setStringProperty(IGridItemModel.CAPTION_PROP, caption);
	}

	/**
	 * Returns the resource key of the caption.
	 *
	 * @return the resource key of the caption
	 */

	public String getCaptionKey() {
		return getStringProperty(IGridItemModel.CAPTION_KEY_PROP);
	}

	/**
	 * Sets the resource key of the caption.
	 *
	 * @param captionKey the resource key of the caption
	 * @throws SemanticException if the caption resource-key property is locked.
	 */

	public void setCaptionKey(String captionKey) throws SemanticException {
		setStringProperty(IGridItemModel.CAPTION_KEY_PROP, captionKey);
	}

	/**
	 * Returns the value of the summary.
	 *
	 * @return the value of summary
	 */
	public String getSummary() {
		return getStringProperty(IGridItemModel.SUMMARY_PROP);
	}

	/**
	 * Sets the value of summary.
	 *
	 * @param summary the value of summary
	 * @throws SemanticException
	 */
	public void setSummary(String summary) throws SemanticException {
		setStringProperty(IGridItemModel.SUMMARY_PROP, summary);
	}

}

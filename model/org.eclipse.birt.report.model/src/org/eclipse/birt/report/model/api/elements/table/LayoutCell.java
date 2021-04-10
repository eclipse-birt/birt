/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.model.api.elements.table;

import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.interfaces.ICellModel;

/**
 * The minimal item in the table.
 */

public class LayoutCell {

	/**
	 * The empty cell.
	 */

	protected final static LayoutCell EMPTY_CELL = new LayoutCell(LayoutCell.CELL_EMPTY);

	/**
	 * CELL is empty
	 */

	public static final int CELL_EMPTY = 0;

	/**
	 * CELL is used, it contains a CELL
	 */

	public static final int CELL_USED = 1;

	/**
	 * CELL is used because of "drop" properties of a cell element.
	 */

	public static final int DROP_SPANNED = 2;

	/**
	 * The 1-based unique id for the same cell.
	 */

	private int cellId = 0;

	/**
	 * Indicates whether the drop property of the cell can take effects.
	 */

	private boolean isEffectualDrop = false;

	/**
	 * The status of the cell. Can be <code>{@link #CELL_EMPTY}</code> or
	 * <code>{@link #CELL_USED}</code>.
	 */

	private int status;

	/**
	 * The corresponding cell element.
	 */

	private Cell content;

	/**
	 * The 0-based offset of the column span.
	 */

	private int colSpanOffset;

	/**
	 * The 0-based offset of the row span.
	 */

	private int rowSpanOffset;

	/**
	 * 
	 */

	private int rowSpanForDrop;

	/**
	 * The layout row in which the layout cell resides.
	 */

	private LayoutRow container = null;

	/**
	 * Constructs a USED <code>LayoutCell</code> with the given information.
	 * 
	 * @param container       the layout row
	 * @param cellId          the unique cell id
	 * @param content         the cell element
	 * @param colSpanOffset   the 0-based offset of the column span
	 * @param rowSpanOffset   the 0-based offset of the row span
	 * @param isEffectualDrop Indicates whether the drop property of the cell can
	 *                        take effects.
	 */

	LayoutCell(LayoutRow container, int cellId, Cell content, int rowSpanOffset, int colSpanOffset,
			boolean isEffectualDrop) {
		this(cellId, CELL_USED);
		this.container = container;
		this.content = content;
		this.colSpanOffset = colSpanOffset;
		this.rowSpanOffset = rowSpanOffset;
		this.isEffectualDrop = isEffectualDrop;
	}

	/**
	 * Constructs a DROP_SPANNED <code>LayoutCell</code> with the given information.
	 * 
	 * @param container     the layout row
	 * @param cellId        the unique cell id
	 * @param content       the cell element
	 * @param colSpanOffset the 0-based offset of the column span
	 * @param rowSpanOffset the 0-based offset of the row span
	 */

	LayoutCell(LayoutRow container, int cellId, Cell content, int rowSpanOffset, int colSpanOffset) {
		this(cellId, DROP_SPANNED);
		this.container = container;
		this.content = content;
		this.colSpanOffset = colSpanOffset;
		this.rowSpanOffset = rowSpanOffset;
		this.isEffectualDrop = true;
	}

	/**
	 * Constructs a <code>LayoutCell</code> with the given status.
	 * 
	 * @param cellId the unique cell id
	 * @param status the status can be <code>{@link #CELL_EMPTY}</code> or
	 *               <code>{@link #CELL_USED}</code>.
	 */

	private LayoutCell(int cellId, int status) {
		this.cellId = cellId;
		this.status = status;
	}

	/**
	 * Constructs a <code>LayoutCell</code> with the given status.
	 * 
	 * @param status the status can be <code>{@link #CELL_EMPTY}</code> or
	 *               <code>{@link #CELL_USED}</code>.
	 */

	private LayoutCell(int status) {
		this.status = status;
	}

	/**
	 * Tests whether the atomic cell is occupied by any cell.
	 * 
	 * @return <code>true</code> if the atomic cell is occupied by any cell.
	 *         Otherwise <code>false</code>.
	 */

	public boolean isUsed() {
		return status != CELL_EMPTY;
	}

	/**
	 * Tests whether the atomic cell is occupied because of "drop" properties of
	 * cells.
	 * 
	 * @return <code>true</code> if the atomic cell is occupied. Otherwise
	 *         <code>false</code>.
	 */

	public boolean isDropSpanned() {
		return status == DROP_SPANNED;
	}

	/**
	 * Returns the corresponding cell element.
	 * 
	 * @return the corresponding cell element
	 */

	protected Cell getContent() {
		return content;
	}

	/**
	 * Returns the 0-based offset of the column span.
	 * 
	 * @return the 0-based offset of the column span
	 */

	public int getColumnSpanOffset() {
		return colSpanOffset;
	}

	/**
	 * Returns the 0-based offset of the row span.
	 * 
	 * @return the 0-based offset of the row span
	 */

	public int getRowSpanOffset() {
		return rowSpanOffset;
	}

	/**
	 * Returns the string that shows the layout. Mainly for the debug.
	 * 
	 * @return the string that shows the layout
	 */

	public String getLayoutString() {
		StringBuffer sb = new StringBuffer();
		switch (status) {
		case CELL_USED:
			sb.append(cellId);
			break;
		case DROP_SPANNED:
			sb.append(cellId);
			sb.append('.');
			break;
		default:
			sb.append(0);
		}

		sb.append("     "); //$NON-NLS-1$
		return sb.toString();
	}

	/**
	 * Checks whether the drop is effectual.
	 * 
	 * @return <code>true</code> if the drop is effectual. Otherwise
	 *         <code>false</code>.
	 */

	public boolean isEffectualDrop() {
		return isEffectualDrop;
	}

	/**
	 * Checks whether the current position is where the cell element begins to span.
	 * 
	 * @return <code>true</code> if it is. Otherwise <code>false</code>.
	 */

	protected boolean isCellStartPosition() {
		return (colSpanOffset == 0 && rowSpanOffset == 0);
	}

	/**
	 * Return the corresponding handle of the cell element.
	 * 
	 * @return the corresponding handle of the cell element.
	 */

	public CellHandle getCell() {
		return getCellRegardlessStartPosition();
	}

	/**
	 * Return the corresponding handle of the cell element regardless of the
	 * position where the cell starts.
	 * 
	 * @return the corresponding handle of the cell element.
	 */

	protected CellHandle getCellRegardlessStartPosition() {
		if (!isUsed())
			return null;

		LayoutTable table = container.getContainer().tableContainer;
		if (table.getModule() != null)
			return (CellHandle) content.getHandle(table.getModule());

		assert false;
		return null;
	}

	/**
	 * Returns the unique index of the cell element.
	 * 
	 * @return the unique index
	 */

	protected int getCellId() {
		return cellId;
	}

	/**
	 * Checks whether there is any element in the cell element.
	 * 
	 * @return <code>true</code> if there is one or more element in the cell.
	 *         Otherwise <code>false</code>.
	 */

	protected boolean isEmptyContent() {
		return isUsed() && content.getSlot(ICellModel.CONTENT_SLOT).getCount() == 0;
	}

	/**
	 * Checks whether "drop" value is "all" or "detail".
	 * 
	 * @return <code>true</code> if "drop" value is "all" or "detail". Otherwise
	 *         <code>false</code>.
	 */

	protected boolean isDropSet() {
		if (content == null)
			return false;

		String drop = (String) content.getLocalProperty(null, ICellModel.DROP_PROP);
		if (drop == null || DesignChoiceConstants.DROP_TYPE_NONE.equalsIgnoreCase(drop))
			return false;

		return true;
	}

	/**
	 * Returns the row number for the drop span.
	 * 
	 * @return the row number
	 */

	public int getRowSpanForDrop() {
		return rowSpanForDrop;
	}

	/**
	 * Sets the row number for the drop span.
	 * 
	 * @param rowSpanForDrop the row number
	 */

	protected void setRowSpanForDrop(int rowSpanForDrop) {
		this.rowSpanForDrop = rowSpanForDrop;
	}

	/**
	 * Sets whether the drop is effectual.
	 * 
	 * @param isEffectualDrop <code>true</code> if the drop is effectual. Otherwise
	 *                        <code>false</code>.
	 */

	protected void setEffectualDrop(boolean isEffectualDrop) {
		this.isEffectualDrop = isEffectualDrop;
	}

	/**
	 * Returns the column position of the current layout cell.
	 * 
	 * @return 1-based column position
	 */

	protected int getColumnPosn() {
		for (int i = 0; i < container.getColumnCount(); i++) {
			if (container.getLayoutCell(i) == this)
				return i + 1;
		}

		assert false;
		return -1;
	}

	/**
	 * Returns the layout row that this layout cell resides.
	 * 
	 * @return the layout row
	 */

	protected LayoutRow getLayoutContainer() {
		return container;
	}
}
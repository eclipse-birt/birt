
package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.elements.Cell;

/**
 * Represents the context information of a cell The information includes the
 * container of the row where the cell resides, the slot id, the group id , the
 * row number, the row span, the column span and the "drop" property in the
 * slot.
 */

class CellContextInfo implements Cloneable {

	/**
	 * The cell instance.
	 */

	private Cell cell;

	/**
	 * The definition name of the container of the row where the cell resides.
	 */

	private String containerDefnNameOfRow;

	/**
	 * The slot id.
	 */

	int slotId;

	/**
	 * The 0-based row number.
	 */

	int rowIndex;

	/**
	 * The group id.
	 */

	int groupId = -1;

	/**
	 * The row span.
	 */

	int rowSpan = 0;

	/**
	 * The column span.
	 */

	private int colSpan = 0;

	/**
	 * The dropping property.
	 */

	String drop = DesignChoiceConstants.DROP_TYPE_NONE;

	/**
	 * Constructs a <code>CellContextInfo</code>.
	 * 
	 * @param cell    the cell element
	 * @param rowSpan the row span of the cell
	 * @param colSpan the column span of the cell
	 * @param drop    the drop property of the cell.
	 */

	CellContextInfo(Cell cell, int rowSpan, int colSpan, String drop) {
		this.cell = cell;
		this.rowSpan = rowSpan;
		this.colSpan = colSpan;
		this.drop = drop;
	}

	/**
	 * Returns the definition name of the container of the row where the cell
	 * resides.
	 * 
	 * @return the definition name of the container of the row
	 */

	protected String getContainerDefnName() {
		return containerDefnNameOfRow;
	}

	/**
	 * Returns the index of the row where the cell resides.
	 * 
	 * @return the 0-based index of the row
	 */

	protected int getRowIndex() {
		return rowIndex;
	}

	/**
	 * Sets the definition name of the container of the row where the cell resides.
	 * 
	 * @param parent the definition name of the container of the row
	 */

	protected void setContainerDefnName(String parent) {
		this.containerDefnNameOfRow = parent;
	}

	/**
	 * Returns the slot id where the cell resides.
	 * 
	 * @return the slot id
	 */

	protected int getSlotId() {
		return slotId;
	}

	/**
	 * Sets the slot id where the cell resides.
	 * 
	 * @param slotId the slot id
	 */

	protected void setSlotId(int slotId) {
		this.slotId = slotId;
	}

	/**
	 * Sets the index of the row where the cell resides.
	 * 
	 * @param rowNumber the 0-based index of the row
	 */

	protected void setRowIndex(int rowNumber) {
		assert rowNumber != -1;
		this.rowIndex = rowNumber;
	}

	/**
	 * Return the cell element that this <code>CellContextInfo</code> corresponds
	 * to.
	 * 
	 * @return the cell element
	 */

	protected Cell getCell() {
		return cell;
	}

	/**
	 * Returns the group index where the cell resides.
	 * 
	 * @return the 0-based group index
	 */

	protected int getGroupId() {
		return groupId;
	}

	/**
	 * Sets the group index where the cell resides.
	 * 
	 * @param groupId the 0-based group index
	 */

	protected void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	/**
	 * Returns the row span of the cell.
	 * 
	 * @return the row span of the cell
	 */

	protected int getRowSpan() {
		return rowSpan;
	}

	/**
	 * Returns the column span of the cell.
	 * 
	 * @return the column span of the cell
	 */

	protected int getColumnSpan() {
		return colSpan;
	}

	/**
	 * Returns the value of the drop property of the cell.
	 * 
	 * @return the value of the drop property
	 */

	protected String getDrop() {
		return drop;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */

	protected Object clone() throws CloneNotSupportedException {
		CellContextInfo clonedContext = (CellContextInfo) super.clone();

		Cell clonedCell = (Cell) cell.clone();
		clonedContext.cell = clonedCell;

		return clonedContext;
	}
}
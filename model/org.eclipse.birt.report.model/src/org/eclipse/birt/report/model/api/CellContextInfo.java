
package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.elements.Cell;

/**
 * Represents the context information of a cell The information includes the
 * container of the row where the cell resides, the slot id, the group id , the
 * row number, the row span, the column span and the "drop" property in the
 * slot.
 */

class CellContextInfo implements Cloneable
{

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
	 * The row number.
	 */

	int rowNumber;

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
	 * @param cell
	 *            the cell element
	 * @param rowSpan
	 *            the row span of the cell
	 * @param colSpan
	 *            the column span of the cell
	 * @param drop
	 *            the drop property of the cell.
	 */

	CellContextInfo( Cell cell, int rowSpan, int colSpan, String drop )
	{
		this.cell = cell;
		this.rowSpan = rowSpan;
		this.colSpan = colSpan;
		this.drop = drop;
	}

	protected String getContainerDefnName( )
	{
		return containerDefnNameOfRow;
	}

	protected int getRowNumber( )
	{
		return rowNumber;
	}

	protected void setContainerDefnName( String parent )
	{
		this.containerDefnNameOfRow = parent;
	}

	protected int getSlotId( )
	{
		return slotId;
	}

	protected void setSlotId( int slotId )
	{
		this.slotId = slotId;
	}

	protected void setRowNumber( int rowNumber )
	{
		assert rowNumber != -1;
		this.rowNumber = rowNumber;
	}

	protected Cell getCell( )
	{
		return cell;
	}

	protected int getGroupId( )
	{
		return groupId;
	}

	protected void setGroupId( int groupId )
	{
		this.groupId = groupId;
	}

	protected int getRowSpan( )
	{
		return rowSpan;
	}

	protected int getColumnSpan( )
	{
		return colSpan;
	}

	protected String getDrop( )
	{
		return drop;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */

	protected Object clone( ) throws CloneNotSupportedException
	{
		CellContextInfo clonedContext = (CellContextInfo) super.clone( );

		Cell clonedCell = (Cell) cell.clone( );
		clonedContext.cell = clonedCell;

		return clonedContext;
	}
}
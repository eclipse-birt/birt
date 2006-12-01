package org.eclipse.birt.report.engine.api.script.element;

import org.eclipse.birt.report.engine.api.script.ScriptException;

/**
 * Represents a the design of a Cell in the scripting environment
 * 
 */
public interface ICell extends IReportElement
{
	/**
	 * Returns the cell's column span. This is the number of table or grid
	 * columns occupied by this cell.
	 * 
	 * @return the column span
	 */
	int getColumnSpan( );

	/**
	 * Returns the cell's row span. This is the number of table or grid rows
	 * occupied by this cell.
	 * 
	 * @return the row span
	 */
	int getRowSpan( );

	
	/**
	 * Returns the cell's drop property. This is how the cell should expand to
	 * fill the entire table or group. This property is valid only for cells
	 * within a table; but not for cells within a grid.
	 * 
	 * @return the string value of the drop property
	 * @see #setDrop(String)
	 */
	String getDrop( );

	/**
	 * Sets the cell's drop property. The input value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * 
	 * <ul>
	 * <li>DROP_TYPE_NONE</li>
	 * <li>DROP_TYPE_DETAIL</li>
	 * <li>DROP_TYPE_ALL</li>
	 * </ul>
	 * 
	 * <p>
	 * 
	 * Note that This property is valid only for cells within a table; but not
	 * for cells within a grid.
	 * 
	 * @param drop
	 *            the string value of the drop property
	 * 
	 * @throws ScriptException
	 *             if the property is locked or the input value is not one of
	 *             the above.
	 * 
	 * @see #getDrop()
	 */
	void setDrop( String drop ) throws ScriptException;

	/**
	 * Returns the cell's column property. The return value gives the column in
	 * which the cell starts. Columns are numbered from 1.
	 * 
	 * @return the column index, starting from 1.
	 */
	int getColumn( );

	/**
	 * Sets the cell's column property. The input value gives the column in
	 * which the cell starts. Columns are numbered from 1.
	 * 
	 * @param column
	 *            the column index, starting from 1.
	 * 
	 * @throws ScriptException
	 *             if this property is locked.
	 */
	void setColumn( int column ) throws ScriptException;

	/**
	 * Returns the cell's height.
	 * 
	 * @return the cell's height
	 */
	String getHeight( );

	/**
	 * Returns the cell's width.
	 * 
	 * @return the cell's width
	 */
	String getWidth( );

}
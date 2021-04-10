
package org.eclipse.birt.report.engine.api.script.element;

import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.model.api.activity.SemanticException;

/**
 * Represents the design of a Table in the scripting environment
 */
public interface ITable extends IListing {

	/**
	 * Returns the number of columns in the table. The number is defined as 1) the
	 * sum of columns described in the "column" slot, or 2) the widest row defined
	 * in the detail, header or footer slots if column slot is empty.
	 * 
	 * @return the number of columns in the table
	 */

	int getColumnCount();

	/**
	 * Tests whether to repeat the headings at the top of each page.
	 * 
	 * @return <code>true</code> if repeat the headings, otherwise
	 *         <code>false</code>.
	 */

	boolean repeatHeader();

	/**
	 * Sets whether to repeat the headings at the top of each page.
	 * 
	 * @param value <code>true</code> if repeat the headings, otherwise
	 *              <code>false</code>.
	 * @throws ScriptException if the property is locked.
	 */

	void setRepeatHeader(boolean value) throws ScriptException;

	/**
	 * Gets the summary of this table.
	 * 
	 * @return the summary.
	 */
	String getSummary();

	/**
	 * Sets the summary of this table.
	 * 
	 * @param summary the summary
	 * @throws SemanticException if this property is locked.
	 */
	void setSummary(String summary) throws ScriptException;

	/**
	 * Returns the caption text of this table.
	 * 
	 * @return the caption text
	 */

	/**
	 * Returns the caption text of this table.
	 * 
	 * @return the caption text
	 */

	String getCaption();

	/**
	 * Sets the caption text of this table.
	 * 
	 * @param caption the caption text
	 * @throws ScriptException if the property is locked.
	 */

	void setCaption(String caption) throws ScriptException;

	/**
	 * Returns the resource key of the caption.
	 * 
	 * @return the resource key of the caption
	 */

	String getCaptionKey();

	/**
	 * Sets the resource key of the caption.
	 * 
	 * @param captionKey the resource key of the caption
	 * @throws ScriptException if the caption resource-key property is locked.
	 */

	void setCaptionKey(String captionKey) throws ScriptException;

	/**
	 * Gets column.
	 * 
	 * @param index
	 * @return column wrapper
	 */

	IColumn getColumn(int index);

}
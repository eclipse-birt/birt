
package org.eclipse.birt.report.engine.api.script.element;

import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.model.api.activity.SemanticException;

/**
 * Represents a the design of a Grid in the scripting environment
 */
public interface IGrid extends IReportItem
{

	/**
	 * Returns the number of columns in the Grid. The number is defined as the
	 * sum of columns described in the "column" slot.
	 * 
	 * @return the number of columns in the grid.
	 */
	int getColumnCount( );

	/**
	 * Gets the summary of this grid.
	 * 
	 * @return the summary.
	 */
	String getSummary( );

	/**
	 * Sets the summary of this grid.
	 * 
	 * @param summary
	 *            the summary
	 * @throws SemanticException
	 *             if this property is locked.
	 */
	void setSummary( String summary ) throws ScriptException;

}
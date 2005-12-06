package org.eclipse.birt.report.engine.api.script.element;

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

}
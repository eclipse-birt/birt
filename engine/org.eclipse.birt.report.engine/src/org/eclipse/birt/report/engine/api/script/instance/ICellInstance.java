package org.eclipse.birt.report.engine.api.script.instance;

import org.eclipse.birt.report.engine.api.script.IScriptStyle;

public interface ICellInstance
{

	/**
	 * Get the style
	 * 
	 * @return the style
	 */
	IScriptStyle getStyle( );

	/**
	 * Get the column span
	 * 
	 * @return the column span
	 */
	int getColSpan( );

	/**
	 * Set the column span
	 * 
	 * @param colSpan,
	 *            the column span
	 */
	void setColSpan( int colSpan );

	/**
	 * @return Returns the rowSpan.
	 */
	int getRowSpan( );

	/**
	 * Set the rowspan
	 * 
	 * @param rowSpan,
	 *            the row span
	 */
	void setRowSpan( int rowSpan );

	/**
	 * Get the column number
	 * 
	 * @return the column number
	 */
	int getColumn( );

}
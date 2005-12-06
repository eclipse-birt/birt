/**
 * 
 */
package org.eclipse.birt.report.engine.api.script;

/**
 * Represents the computed expression results that are bound to the current row.
 * The index starts with 1, which reprents the first expression in the row.
 */

public interface IRowData
{
	/**
	 * Return the value of the provided expression. The provided expression must
	 * have been bound to the current row. Otherwise, it returns null.
	 */
	public Object getExpressionValue( String expression );

	/**
	 * Return the value of the i:th expression in the current row. Null will be
	 * return if the i:th expression doesn't exist.
	 */
	public Object getExpressionValue( int i );

	/**
	 * Return the number of expressions bound to the current row.
	 */
	public int getExpressionCount( );

}

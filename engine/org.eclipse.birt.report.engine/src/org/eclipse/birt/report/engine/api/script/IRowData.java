/**
 * 
 */
package org.eclipse.birt.report.engine.api.script;

/**
 * IRowData represents the computed expressions results that are binding to the current row.
 * The index starts with 1, which reprents the first expression in the row.
 */
 
public interface IRowData {
	// return the value of the provided expression. The provided expression must have been binding 
	// to the current row. Otherwise, it returns null.
	public Object getExpressionValue( String expression );
	
	// return the value of the ith expression in the current row. Null will be return if the ith 
	// expression doesn't exist.
	public Object getExpressionValue( int i );
	
	// return the number of expressions binding to the current row.
	public int getExpressionCount( );

}

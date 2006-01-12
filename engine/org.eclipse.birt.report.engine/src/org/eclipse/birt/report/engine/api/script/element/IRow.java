package org.eclipse.birt.report.engine.api.script.element;

import org.eclipse.birt.report.engine.api.script.ScriptException;

/**
 * Represents a row in the scripting environment
 */
public interface IRow extends IReportElement
{

	/**
	 * Gets a handle to deal with the row's height.
	 * 
	 * @return the row's height.
	 */

	String getHeight( );

	/**
	 * Returns the bookmark of the row. The bookmark value is evaluated
	 * as an expression.
	 * 
	 * @return the book mark as a string
	 */

	String getBookmark( );

	/**
	 * Sets the bookmark of the row. The bookmark value is evaluated as
	 * an expression. If you want the bookmark to be the string "bookmark", you
	 * need to use setBookmark("\"bookmark\"");
	 * 
	 * If bookmark is a JavaScript variable, use setBookmark("bookmark");
	 * 
	 * @param value
	 *            the bookmark expression
	 * @throws ScriptException
	 *             if the property is locked.
	 */

	void setBookmark( String value ) throws ScriptException;

}
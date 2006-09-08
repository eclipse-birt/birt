/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

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
	 * Returns the bookmark of the row. The bookmark value is evaluated as an
	 * expression.
	 * 
	 * @return the book mark as a string
	 */

	String getBookmark( );

	/**
	 * Sets the bookmark of the row. The bookmark value is evaluated as an
	 * expression. If you want the bookmark to be the string "bookmark", you
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

	/**
	 * Adds HighLightRule
	 * 
	 * @param rule
	 * @throws ScriptException
	 */

	void addHighLightRule( IHighLightRule rule ) throws ScriptException;

	/**
	 * Removes HighLightRule
	 * 
	 * @param name
	 * @throws ScriptException
	 */

	void removeHighLightRules( ) throws ScriptException;

	/**
	 * Returns all highlightrule
	 * 
	 * @return all highlightrule
	 */
	IHighLightRule[] getHighLightRule( );

}
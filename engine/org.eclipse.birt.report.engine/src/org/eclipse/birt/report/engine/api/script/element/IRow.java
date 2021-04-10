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

public interface IRow extends IDesignElement {

	/**
	 * Gets a handle to deal with the row's height.
	 * 
	 * @return the row's height.
	 */

	String getHeight();

	/**
	 * Returns the bookmark of the row. The bookmark value is evaluated as an
	 * expression.
	 * 
	 * @return the book mark as a string
	 */

	String getBookmark();

	/**
	 * Sets the bookmark of the row. The bookmark value is evaluated as an
	 * expression. If you want the bookmark to be the string "bookmark", you need to
	 * use setBookmark("\"bookmark\"");
	 * 
	 * If bookmark is a JavaScript variable, use setBookmark("bookmark");
	 * 
	 * @param value the bookmark expression
	 * @throws ScriptException if the property is locked.
	 */

	void setBookmark(String value) throws ScriptException;

	/**
	 * Removes all hide rules that matches formatType.
	 * 
	 * @param rule
	 * @exception ScriptException
	 */

	void removeHideRule(IHideRule rule) throws ScriptException;

	/**
	 * Removes all hide rules
	 * 
	 * @throws ScriptException
	 */

	void removeHideRules() throws ScriptException;

	/**
	 * Returns array of hide rule expression
	 * 
	 * @return array of hide rule expression
	 */

	IHideRule[] getHideRules();

	/**
	 * Add HideRule
	 * 
	 * @param rule
	 * @throws ScriptException
	 */

	void addHideRule(IHideRule rule) throws ScriptException;

	/**
	 * Gets all high light rules.
	 * 
	 * @return all high light rules
	 */

	IHighlightRule[] getHighlightRules();

	/**
	 * Adds high light rule.
	 * 
	 * @param rule
	 * @throws ScriptException
	 */

	void addHighlightRule(IHighlightRule rule) throws ScriptException;

	/**
	 * Removes all high light rules.
	 * 
	 * @throws ScriptException
	 */

	void removeHighlightRules() throws ScriptException;

	/**
	 * Removes high light rule.
	 * 
	 * @param rule
	 * @throws ScriptException
	 */

	void removeHighlightRule(IHighlightRule rule) throws ScriptException;

}
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

package org.eclipse.birt.report.model.api.simpleapi;

import org.eclipse.birt.report.model.api.activity.SemanticException;

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
	 * @throws SemanticException if the property is locked.
	 */

	void setBookmark(String value) throws SemanticException;

	/**
	 * Removes all hide rules that matches formatType.
	 * 
	 * @param rule
	 * @exception SemanticException
	 */

	void removeHideRule(IHideRule rule) throws SemanticException;

	/**
	 * Removes all hide rules
	 * 
	 * @throws SemanticException
	 */

	void removeHideRules() throws SemanticException;

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
	 * @throws SemanticException
	 */

	void addHideRule(IHideRule rule) throws SemanticException;

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
	 * @throws SemanticException
	 */

	void addHighlightRule(IHighlightRule rule) throws SemanticException;

	/**
	 * Removes all high light rules.
	 * 
	 * @throws SemanticException
	 */

	void removeHighlightRules() throws SemanticException;

	/**
	 * Removes high light rule.
	 * 
	 * @param rule
	 * @throws SemanticException
	 */

	void removeHighlightRule(IHighlightRule rule) throws SemanticException;

}
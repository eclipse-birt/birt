/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.report.engine.api.script.element;

import org.eclipse.birt.report.engine.api.script.ScriptException;

/**
 * Represents a the design of a multi line text item in the scripting
 * environment
 */
public interface IDynamicText extends IReportItem {

	/**
	 * Returns the expression that gives the text that the multi-line data item
	 * displays.
	 * 
	 * @return the value expression
	 */

	String getValueExpr();

	/**
	 * Sets the expression that gives the text that this multi-line data item
	 * displays.
	 * 
	 * @param expr the new expression for the value expression
	 * @throws ScriptException if the expression contains errors, or the property is
	 *                         locked.
	 */

	void setValueExpr(String expr) throws ScriptException;

	/**
	 * Returns the expression that that defines the type of text the multi-line data
	 * item holds. The content type can be one of:
	 * 
	 * <ul>
	 * <li><code>DesignChoiceConstants.TEXT_DATA_CONTENT_TYPE_AUTO</code> (default)
	 * <li><code>DesignChoiceConstants.TEXT_DATA_CONTENT_TYPE_PLAIN</code>: Plain
	 * text;
	 * <li><code>DesignChoiceConstants.TEXT_DATA_CONTENT_TYPE_HTML</code>: HTML
	 * format;
	 * <li><code>DesignChoiceConstants.TEXT_DATA_CONTENT_TYPE_RTF</code>: Rich Text
	 * format;
	 * </ul>
	 * 
	 * @return the text type
	 */

	String getContentType();

	/**
	 * Sets the expression that defines the text type this multi-line data item
	 * holds. The content type can be one of
	 * 
	 * <ul>
	 * <li><code>DesignChoiceConstants.TEXT_DATA_CONTENT_TYPE_AUTO</code> (default)
	 * <li><code>DesignChoiceConstants.TEXT_DATA_CONTENT_TYPE_PLAIN</code>: Plain
	 * text;
	 * <li><code>DesignChoiceConstants.TEXT_DATA_CONTENT_TYPE_HTML</code>: HTML
	 * format;
	 * <li><code>DesignChoiceConstants.TEXT_DATA_CONTENT_TYPE_RTF</code>: Rich Text
	 * format;
	 * </ul>
	 * 
	 * @param contentType the new text type
	 * @throws ScriptException if the property is locked or the
	 *                         <code>contentType</code> is not one of the above.
	 */

	void setContentType(String contentType) throws ScriptException;

}

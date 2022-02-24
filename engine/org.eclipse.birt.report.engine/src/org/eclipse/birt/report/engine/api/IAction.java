/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.api;

import java.util.Map;

/**
 * Defines an interface that allows engine to pass hyperlink information to an
 * emitter, if the emitter determines to customize the hyperlinks calculated in
 * engine, or it wants to use a totally different hyperlink string
 * <p>
 * Because it is allowed to customize hyperlinks through emitters, hyperlink
 * customization in presentation engine itself is not supported now. It could be
 * added later if it deems necessary.
 *
 */
public interface IAction {
	/**
	 * hyperlink action
	 */
	int ACTION_HYPERLINK = 1;
	/**
	 * bookmark action
	 */
	int ACTION_BOOKMARK = 2;
	/**
	 * drillthrough action
	 */
	int ACTION_DRILLTHROUGH = 3;

	/**
	 * @return the type of the hyperlink
	 */
	int getType();

	/**
	 * @return the bookmark string
	 */
	String getBookmark();

	/**
	 * @return the action string that is calculated using the engine's default
	 *         algorithm. valid for all three action types.
	 */
	String getActionString();

	/**
	 * @return the system id of the report design which create this action.
	 */
	String getSystemId();

	/**
	 * @return the report name if action type is drillthrough, null otherwise
	 */
	String getReportName();

	/**
	 * @return a set of name/value pairs for running the report in a drillthrough
	 *         link; null when the action type is not drillthrough, or no parameters
	 *         are defined for the drillthrough report to run. In the future, when
	 *         the drillthrough is against a report document, the parameter binding
	 *         map is also null.
	 */
	Map getParameterBindings();

	/**
	 * @return a set of name/value pairs for searching the report in a drillthrough
	 *         link; null when the action type is not drillthrough, or no search
	 *         criteria is used
	 */
	Map getSearchCriteria();

	/**
	 * @return The name of a frame where a document is to be opened.
	 */
	String getTargetWindow();

	/**
	 * return fomat of drillthrough report
	 *
	 * @return
	 */
	String getFormat();

	/**
	 * Return the bookmark type set in the drillthrough action. The return result
	 * indicated the target element is a toc or not.
	 *
	 * @return <code>true</code>, the target element is a bookmark.
	 *         <code>false</code>, the target element is indicated to be a toc.
	 */
	boolean isBookmark();

	/**
	 * @return the type of the target report file.
	 */
	String getTargetFileType();

	String getTooltip();
}

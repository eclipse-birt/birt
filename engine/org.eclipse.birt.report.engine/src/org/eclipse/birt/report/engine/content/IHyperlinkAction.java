/*******************************************************************************
 * Copyright (c) 2004 , 2008Actuate Corporation.
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

package org.eclipse.birt.report.engine.content;

import java.util.List;
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
public interface IHyperlinkAction {

	/**
	 * hyperlink action
	 */
	public final static int ACTION_HYPERLINK = 1;
	/**
	 * bookmark action
	 */
	public final static int ACTION_BOOKMARK = 2;
	/**
	 * drillthrough action
	 */
	public final static int ACTION_DRILLTHROUGH = 3;

	/**
	 * @return the type of the hyperlink
	 */
	public int getType();

	/**
	 * @deprecated replaced by IDrillThroughAction.isBookmark();
	 * 
	 *             Return the bookmark type set in the drillthrough action. The
	 *             return result indicated the target element is a toc or not.
	 * 
	 * @return <code>true</code>, the target element is a bookmark.
	 *         <code>false</code>, the target element is indicated to be a toc.
	 */
	public boolean isBookmark();

	/**
	 * @return the bookmark string (not the bookmark expression) when action type is
	 *         bookmark or drillthrough, or null whe action type is hyperlink.
	 */
	public String getBookmark();

	/**
	 * @return the action string that is calculated using the engine's default
	 *         algorithm. valid for all three action types.
	 */
	public String getHyperlink();

	/**
	 * @return the report name if action type is drillthrough, null otherwise
	 */
	public String getReportName();

	/**
	 * @return a set of name/value pairs for running the report in a drillthrough
	 *         link; null when the action type is not drillthrough, or no parameters
	 *         are defined for the drillthrough report to run. In the future, when
	 *         the drillthrough is against a report document, the parameter binding
	 *         map is also null.
	 */
	public Map getParameterBindings();

	/**
	 * @return a set of name/value pairs for searching the report in a drillthrough
	 *         link; null when the action type is not drillthrough, or no search
	 *         criteria is used
	 */
	public Map getSearchCriteria();

	public String getFormat();

	/**
	 * @return The name of a frame where a document is to be opened.
	 */
	public String getTargetWindow();

	public void setHyperlink(String hyperlink, String target);

	public void setReportName(String reportName);

	public void setBookmark(String bookmark);

	public void setBookmarkType(boolean isBookmark);

	/**
	 * @deprecated
	 * @param bookmark
	 * @param isBookmark
	 * @param reportName
	 * @param parameterBindings
	 * @param searchCriteria
	 * @param target
	 * @param format
	 */
	public void setDrillThrough(String bookmark, boolean isBookmark, String reportName, Map parameterBindings,
			Map searchCriteria, String target, String format);

	public void setDrillThrough(String bookmark, boolean isBookmark, String reportName,
			Map<String, List<Object>> parameterBindings, Map searchCriteria, String target, String format,
			String targetFileType);

	public IDrillThroughAction getDrillThrough();

	public void setDrillThrough(IDrillThroughAction drillThrough);

	public void setTooltip(String tooltip);

	public String getTooltip();
}

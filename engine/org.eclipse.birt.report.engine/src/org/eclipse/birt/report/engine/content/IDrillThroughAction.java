
/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.engine.content;

import java.util.Map;

/**
 * Providers interface for the action process of drill through.
 * <p>
 * Drill through is a term of actions that explorer elements like bookmark
 * between two reports. Generally, via a drill through, we will get a movement
 * from some element in source report to element in target report. The class
 * <code>IDrillThroughAction</code> indicates this action.
 */
public interface IDrillThroughAction {
	/**
	 * Get the target report name if the action type is drillthrough which is
	 * predefined.
	 * 
	 * @return the report name.
	 */
	String getReportName();

	/**
	 * Set the target report name if the action type is drillthrough which is
	 * predefined.
	 * 
	 * @param reportName the report name
	 */
	void setReportName(String reportName);

	/**
	 * Get a set of name/value pairs for running the target report in a drillthrough
	 * link.
	 * 
	 * @return a set of name/value pairs for running the target report in a
	 *         drillthrough link.
	 */
	Map getParameterBindings();

	/**
	 * Get a set of name/value pairs for searching the target report in a
	 * drillthrough link.
	 * 
	 * @return a set of name/value pairs for searching the target report in a
	 *         drillthrough link.
	 */
	Map getSearchCriteria();

	/**
	 * Get the format of the target report if the action type is drillthrough. This
	 * format is used for building the URL when explorer between reports, running
	 * the target report and related process.
	 * 
	 * @return the format of output report if action type is drillthrough.
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
	 * Set the bookmark type of this drillthrough action.
	 * 
	 * @param bookmark <code>true</code>, the target element is a bookmark. or
	 *                 <code>false</code>, the target element is indicated to be a
	 *                 toc.
	 */
	void setBookmark(String bookmark);

	/**
	 * Get the bookmark.
	 * 
	 * @return the bookmark string if the bookmark type is Bookmark and action type
	 *         is drillthrough. Return <code>null</code> if the bookmark type is TOC
	 *         and action type is drillthrough.
	 */
	String getBookmark();

	/**
	 * Get the target window.
	 * 
	 * @return the targetWindow string if action type is drillthrough.
	 */
	String getTargetWindow();

	/**
	 * Set the type of bookmark. <code>true</code>, is a bookmark.
	 * <code>false</code>, indicated to be a toc.
	 * 
	 * @param <code>isBookmark</code> the bookmark type is toc or not.
	 */
	void setBookmarkType(boolean isBookmark);

	/**
	 * Set a set of name/value pairs for running the target report in a drillthrough
	 * link.
	 * 
	 * @param parameterBindings a set of name/value pairs for running the report in
	 *                          a drillthrough link.
	 */
	void setParameterBindings(Map parameterBindings);

	/**
	 * Set a set of name/value pairs for searching the target report in a
	 * drillthrough link.
	 * 
	 * @param searchCriteria a set of name/value pairs for searching the report in a
	 *                       drillthrough link.
	 */
	void setSearchCriteria(Map searchCriteria);

	/**
	 * Set the target window in string format.
	 * 
	 * @param target the target window.
	 */
	void setTargetWindow(String target);

	/**
	 * set the format of the output report.
	 * 
	 * @param format the format of the output report.
	 */
	void setFormat(String format);

	/**
	 * Sets the type of target report file for a drill-through action.
	 * 
	 * @param targetFileType the type of the target file
	 */
	public void setTargetFileType(String targetFileType);

	/**
	 * Get the type of the target file.
	 * 
	 * @return the type of the target report file.
	 */
	public String getTargetFileType();
}

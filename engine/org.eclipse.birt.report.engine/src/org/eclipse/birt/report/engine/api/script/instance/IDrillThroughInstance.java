
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.engine.api.script.instance;

import java.util.Map;

/**
 * 
 */

public interface IDrillThroughInstance {
	/**
	 * @return the report name.
	 */
	String getReportName();

	/**
	 * Set the report name.
	 * 
	 * @param reportName
	 */
	void setReportName(String reportName);

	/**
	 * @return a set of name/value pairs for running the report in the drillthrough
	 *         link.
	 */
	Map getParameterBindings();

	/**
	 * Set a set of name/value pairs for running the report in the drillthrough
	 * link.
	 */
	void setParameterBindings(Map parameterBindings);

	/**
	 * @return a set of name/value pairs for searching the report in the
	 *         drillthrough link.
	 */
	Map getSearchCriteria();

	/**
	 * Set a set of name/value pairs for searching the report in the drillthrough
	 * link.
	 */
	void setSearchCriteria(Map searchCriteria);

	/**
	 * @return the format of output report.
	 * 
	 */
	String getFormat();

	/**
	 * Set the format of output report.
	 * 
	 */
	void setFormat(String format);

	/**
	 * @return the bookmark type. True, the bookmark is a bookmark. False, the
	 *         bookmark is a toc.
	 */
	boolean isBookmark();

	/**
	 * Set the bookmark to the drillThrough .
	 * 
	 * @param bookmark
	 */
	void setBookmark(String bookmark);

	/**
	 * @return the bookmark string if the bookmark type is Bookmark. NULL if the
	 *         bookmark type is TOC.
	 */
	String getBookmark();

	/**
	 * Set the TOC of this drillThrough.
	 * 
	 * @param toc
	 */
	void setTOC(String toc);

	/**
	 * @return the bookmark string if the bookmark type is TOC. NULL if the bookmark
	 *         type is Bookmark.
	 */
	String getTOC();

	/**
	 * @return the targetWindow string.
	 */
	String getTargetWindow();

	/**
	 * Set the targetWindow string.
	 */
	void setTargetWindow(String target);

	/**
	 * Sets target report file type for a drill-through action.
	 */
	void setTargetFileType(String targetFileType);

	/**
	 * @return the type of the target report file.
	 */
	String getTargetFileType();
}

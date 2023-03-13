/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.ir;

import java.util.List;
import java.util.Map;

/**
 *
 */
public class DrillThroughActionDesign {

	protected Expression reportName;
	/**
	 * The type of the target file.
	 */
	protected String targetFileType = null;

	protected Expression bookmark;
	protected boolean bookmarkType;
	protected String format;

	protected Map<String, List<Expression>> parameters;
	protected Map search;

	/**
	 * @return Returns the bookmark.
	 */
	public Expression getBookmark() {
		return bookmark;
	}

	/**
	 * @param bookmark The bookmark to set.
	 */
	public void setBookmark(Expression bookmark) {
		this.bookmark = bookmark;
	}

	/**
	 * @return Returns the reportName.
	 */
	public Expression getReportName() {
		return reportName;
	}

	/**
	 * @param reportName The reportName to set.
	 */
	public void setReportName(Expression reportName) {
		this.reportName = reportName;
	}

	/**
	 * @return Returns the parameters.
	 */
	public Map<String, List<Expression>> getParameters() {
		return parameters;
	}

	/**
	 * @param parameters The parameters to set.
	 */
	public void setParameters(Map<String, List<Expression>> parameters) {
		this.parameters = parameters;
	}

	/**
	 * @return Returns the search.
	 */
	public Map getSearch() {
		return search;
	}

	/**
	 * @param search The search to set.
	 */
	public void setSearch(Map search) {
		this.search = search;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public void setBookmarkType(boolean bookmarkType) {
		this.bookmarkType = bookmarkType;
	}

	public boolean getBookmarkType() {
		return bookmarkType;
	}

	/**
	 * Sets target report file type for a drill-through action. The format type for
	 * action are defined in DesignChoiceConstants.
	 *
	 * @param targetFileType the type of the target report file.
	 */
	public void setTargetFileType(String targetFileType) {
		this.targetFileType = targetFileType;
	}

	/**
	 * @return the type of the target report file.
	 */
	public String getTargetFileType() {
		return targetFileType;
	}
}

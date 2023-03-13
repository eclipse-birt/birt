
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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
package org.eclipse.birt.report.engine.content.impl;

import java.util.Map;

import org.eclipse.birt.report.engine.content.IDrillThroughAction;

/**
 *
 */

public class DrillThroughAction implements IDrillThroughAction {
	/**
	 * report name
	 */
	protected String reportName;

	/**
	 * bookmark string
	 */
	protected String bookmark;

	/**
	 * Flag indicating if this is a bookmark. False means this is a TOC.
	 */
	protected boolean isBookmark;

	protected String format;

	/**
	 * parameters and their values for running drillthrough reports
	 */
	protected Map parameterBindings;

	/**
	 * search keys and their values for searching drillthrough reports
	 */
	protected Map searchCriteria;

	/**
	 * the name of a frame where a document is to be opened.
	 */
	protected String target;

	/**
	 * The type of the target file.
	 */
	protected String targetFileType = null;

	public DrillThroughAction() {

	}

	public DrillThroughAction(String bookmark, boolean isBookmark, String reportName, Map parameterBindings,
			Map searchCriteria, String target, String format, String targetFileType) {
		this.bookmark = bookmark;
		this.isBookmark = isBookmark;
		this.reportName = reportName;
		this.parameterBindings = parameterBindings;
		this.searchCriteria = searchCriteria;
		this.target = target;
		this.format = format;
		this.targetFileType = targetFileType;
	}

	@Override
	public String getBookmark() {
		return bookmark;
	}

	@Override
	public String getFormat() {
		return format;
	}

	@Override
	public Map getParameterBindings() {
		return parameterBindings;
	}

	@Override
	public String getReportName() {
		return reportName;
	}

	@Override
	public Map getSearchCriteria() {
		return searchCriteria;
	}

	@Override
	public String getTargetWindow() {
		return target;
	}

	@Override
	public boolean isBookmark() {
		return isBookmark;
	}

	@Override
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	@Override
	public void setBookmark(String bookmark) {
		this.bookmark = bookmark;
	}

	@Override
	public void setBookmarkType(boolean isBookmark) {
		this.isBookmark = isBookmark;
	}

	@Override
	public void setParameterBindings(Map parameterBindings) {
		this.parameterBindings = parameterBindings;
	}

	@Override
	public void setSearchCriteria(Map searchCriteria) {
		this.searchCriteria = searchCriteria;
	}

	@Override
	public void setTargetWindow(String target) {
		this.target = target;
	}

	@Override
	public void setFormat(String format) {
		this.format = format;
	}

	/**
	 * Sets target report file type for a drill-through action. The format type for
	 * action are defined in DesignChoiceConstants.
	 *
	 * @param targetFileType the type of the target report file.
	 */
	@Override
	public void setTargetFileType(String targetFileType) {
		this.targetFileType = targetFileType;
	}

	/**
	 * @return the type of the target report file.
	 */
	@Override
	public String getTargetFileType() {
		return targetFileType;
	}
}

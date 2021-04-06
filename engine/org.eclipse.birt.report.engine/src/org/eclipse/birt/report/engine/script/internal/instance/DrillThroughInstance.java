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

package org.eclipse.birt.report.engine.script.internal.instance;

import java.util.Map;

import org.eclipse.birt.report.engine.api.script.instance.IDrillThroughInstance;
import org.eclipse.birt.report.engine.content.IDrillThroughAction;

/**
 * 
 */

public class DrillThroughInstance implements IDrillThroughInstance {

	IDrillThroughAction drillThrough;

	DrillThroughInstance(IDrillThroughAction drillThrough) {
		this.drillThrough = drillThrough;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.content.IDrillThroughInstance#getReportName()
	 */
	public String getReportName() {
		return drillThrough.getReportName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.content.IDrillThroughInstance#setReportName(
	 * java.lang.String )
	 */
	public void setReportName(String reportName) {
		drillThrough.setReportName(reportName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.IDrillThroughInstance#
	 * getParameterBindings()
	 */
	public Map getParameterBindings() {
		return drillThrough.getParameterBindings();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.IDrillThroughInstance#
	 * setParameterBindings( java.util.Map )
	 */
	public void setParameterBindings(Map parameterBindings) {
		drillThrough.setParameterBindings(parameterBindings);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.IDrillThroughInstance#
	 * getSearchCriteria()
	 */
	public Map getSearchCriteria() {
		return drillThrough.getSearchCriteria();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.IDrillThroughInstance#
	 * setSearchCriteria( java.util.Map )
	 */
	public void setSearchCriteria(Map searchCriteria) {
		drillThrough.setSearchCriteria(searchCriteria);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.IDrillThroughInstance#getFormat()
	 */
	public String getFormat() {
		return drillThrough.getFormat();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.IDrillThroughInstance#setFormat(
	 * java.lang.String )
	 */
	public void setFormat(String format) {
		drillThrough.setFormat(format);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.IDrillThroughInstance#isBookmark(
	 * boolean )
	 */
	public boolean isBookmark() {
		return drillThrough.isBookmark();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.content.IDrillThroughInstance#getBookmark()
	 */
	public String getBookmark() {
		if (drillThrough.isBookmark()) {
			return drillThrough.getBookmark();
		} else {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.content.IDrillThroughInstance#setBookmark(
	 * java.lang.String )
	 */
	public void setBookmark(String bookmark) {
		drillThrough.setBookmarkType(true);
		drillThrough.setBookmark(bookmark);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.IDrillThroughInstance#getTOC()
	 */
	public String getTOC() {
		if (!drillThrough.isBookmark()) {
			return drillThrough.getBookmark();
		} else {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.IDrillThroughInstance#setTOC(
	 * java.lang.String )
	 */
	public void setTOC(String toc) {
		drillThrough.setBookmarkType(false);
		drillThrough.setBookmark(toc);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.content.IDrillThroughInstance#getTargetWindow(
	 * )
	 */
	public String getTargetWindow() {
		return drillThrough.getTargetWindow();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.content.IDrillThroughInstance#getTargetWindow(
	 * java.lang.String )
	 */
	public void setTargetWindow(String target) {
		drillThrough.setTargetWindow(target);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.IDrillThroughInstance#
	 * setTargetFileType( java.lang.String )
	 */
	public void setTargetFileType(String targetFileType) {
		drillThrough.setTargetFileType(targetFileType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.IDrillThroughInstance#
	 * getTargetFileType( java.lang.String )
	 */
	public String getTargetFileType() {
		return drillThrough.getTargetFileType();
	}

	/**
	 * @return this.hyperlink, HyperlinkAction
	 */
	IDrillThroughAction getDrillThroughAction() {
		return drillThrough;
	}
}

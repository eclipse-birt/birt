/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.ir;

/**
 * Group type.
 * 
 * used by ListItem and TableItem.
 * 
 */
public abstract class GroupDesign extends ReportItemDesign {
	/**
	 * group level, start from 0
	 */
	protected int groupLevel;

	/**
	 * the page break before property
	 */
	protected String pageBreakBefore;

	/**
	 * the page break after property
	 */
	protected String pageBreakAfter;

	/**
	 * the page break inside property
	 */
	protected String pageBreakInside;

	/**
	 * group hideDetail
	 */
	protected boolean hideDetail;

	/**
	 * does the header need to be repeated in each page.
	 */
	protected boolean headerRepeat;

	/**
	 * group header
	 */
	protected BandDesign header;
	/**
	 * group footer
	 */
	protected BandDesign footer;

	/**
	 * @param hide The hideDetail to set.
	 */
	public void setHideDetail(boolean hide) {
		hideDetail = hide;
	}

	/**
	 * @return Returns the hideDetail.
	 */
	public boolean getHideDetail() {
		return hideDetail;
	}

	public String getPageBreakBefore() {
		return pageBreakBefore;
	}

	public void setPageBreakBefore(String pageBreak) {
		pageBreakBefore = pageBreak;
	}

	public String getPageBreakAfter() {
		return pageBreakAfter;
	}

	public void setPageBreakAfter(String pageBreak) {
		pageBreakAfter = pageBreak;
	}

	public int getGroupLevel() {
		return groupLevel;
	}

	public void setGroupLevel(int groupLevel) {
		this.groupLevel = groupLevel;
	}

	/**
	 * @return Returns the footer.
	 */
	public BandDesign getFooter() {
		return footer;
	}

	/**
	 * @param footer The footer to set.
	 */
	public void setFooter(BandDesign footer) {
		this.footer = footer;
	}

	/**
	 * @return Returns the header.
	 */
	public BandDesign getHeader() {
		return header;
	}

	/**
	 * @param header The header to set.
	 */
	public void setHeader(BandDesign header) {
		this.header = header;
	}

	public boolean isHeaderRepeat() {
		return headerRepeat;
	}

	public void setHeaderRepeat(boolean repeat) {
		headerRepeat = repeat;
	}

	public Object accept(IReportItemVisitor visitor, Object value) {
		return visitor.visitGroup(this, value);
	}

	public String getPageBreakInside() {
		return pageBreakInside;
	}

	public void setPageBreakInside(String pageBreakInside) {
		this.pageBreakInside = pageBreakInside;
	}
}

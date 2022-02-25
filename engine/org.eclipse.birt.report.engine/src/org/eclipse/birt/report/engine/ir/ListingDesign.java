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

import java.util.ArrayList;

/**
 *
 */
abstract public class ListingDesign extends ReportItemDesign {

	protected boolean repeatHeader;

	/**
	 * header of the listing design.
	 */
	protected BandDesign header;
	/**
	 * footer of the listing design
	 */
	protected BandDesign footer;
	/**
	 * detail of the listing design
	 */
	protected BandDesign detail;
	/**
	 * the page break interval. it will create a softpage break for each page break
	 * interval rows.
	 */
	protected int pageBreakInterval = -1;
	/**
	 * groups. which corresponds to the group in query. which is created at begin &
	 * end of each group.
	 */
	protected ArrayList groups = new ArrayList();

	/**
	 * @return Returns the repeatHeader.
	 */
	public boolean isRepeatHeader() {
		return repeatHeader;
	}

	/**
	 * @param repeatHeader The repeatHeader to set.
	 */
	public void setRepeatHeader(boolean repeatHeader) {
		this.repeatHeader = repeatHeader;
	}

	public BandDesign getHeader() {
		return header;
	}

	public void setHeader(BandDesign header) {
		this.header = header;
	}

	public BandDesign getFooter() {
		return footer;
	}

	public void setFooter(BandDesign footer) {
		this.footer = footer;
	}

	public BandDesign getDetail() {
		return detail;
	}

	public void setDetail(BandDesign detail) {
		this.detail = detail;
	}

	/**
	 * get all the groups in this listing.
	 *
	 * @return collection of groups.
	 */
	public ArrayList getGroups() {
		return this.groups;
	}

	/**
	 * get group count
	 *
	 * @return group count
	 */
	public int getGroupCount() {
		return this.groups.size();
	}

	public GroupDesign getGroup(int level) {
		return (GroupDesign) groups.get(level);
	}

	/**
	 * append a group into this listing. the group will be appended at the end of
	 * this listing.
	 *
	 * @param group group to be added
	 */
	public void addGroup(GroupDesign group) {
		this.groups.add(group);
	}

	public void setPageBreakInterval(int interval) {
		this.pageBreakInterval = interval;
	}

	public int getPageBreakInterval() {
		return pageBreakInterval;
	}

	@Override
	public Object accept(IReportItemVisitor visitor, Object value) {
		return visitor.visitListing(this, value);
	}
}

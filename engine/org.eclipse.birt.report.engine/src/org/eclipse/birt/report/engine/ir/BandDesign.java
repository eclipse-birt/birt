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
package org.eclipse.birt.report.engine.ir;

import java.util.ArrayList;

abstract public class BandDesign extends ReportItemDesign {
	public static final int BAND_DETAIL = 0;
	public static final int BAND_HEADER = 1;
	public static final int BAND_FOOTER = 2;
	public static final int GROUP_HEADER = 3;
	public static final int GROUP_FOOTER = 4;

	private GroupDesign group;

	private ArrayList contents = new ArrayList();

	/*
	 * bandType is used to output the row type.
	 */
	private int bandType = BAND_DETAIL;

	/**
	 * get section in this band
	 * 
	 * @param index section index
	 * @return Returns the sections.
	 */
	public ReportItemDesign getContent(int index) {
		assert (index >= 0 && index < contents.size());
		return (ReportItemDesign) contents.get(index);
	}

	/**
	 * get total sections
	 * 
	 * @return total count sections in this list band.
	 */
	public int getContentCount() {
		return this.contents.size();
	}

	/**
	 * get all the sections.
	 * 
	 * @return array list contains all the sections
	 */
	public ArrayList getContents() {
		return this.contents;
	}

	/**
	 * set the section of this band
	 * 
	 * @param item The sections to set.
	 */
	public void addContent(ReportItemDesign item) {
		this.contents.add(item);
	}

	public void setGroup(GroupDesign group) {
		this.group = group;
	}

	public GroupDesign getGroup() {
		return group;
	}

	/**
	 * get band type
	 * 
	 * @return the band type
	 */
	public int getBandType() {
		return bandType;
	}

	/**
	 * set band type
	 * 
	 * @param bandType the band type
	 */
	public void setBandType(int bandType) {
		this.bandType = bandType;
	}

	public Object accept(IReportItemVisitor visitor, Object value) {
		return visitor.visitBand(this, value);
	}
}

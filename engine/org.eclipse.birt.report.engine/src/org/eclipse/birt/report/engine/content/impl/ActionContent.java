/*******************************************************************************
 * Copyright (c) 2004 , 2008 Actuate Corporation.
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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.engine.content.IDrillThroughAction;
import org.eclipse.birt.report.engine.content.IHyperlinkAction;

/**
 * Implements the <code>IHyperlinkAction</code> interface for passing action
 * informaiton to emitters
 */
public class ActionContent implements IHyperlinkAction {

	/**
	 * action type
	 */
	protected int type = -1;

	/**
	 * bookmark string
	 */
	protected String bookmark;

	/**
	 * action string. See base interface
	 */
	protected String hyperlink;

	protected String tooltip;

	/**
	 * the name of a frame where a document is to be opened.
	 */
	protected String target = null;

	/**
	 *
	 */
	protected IDrillThroughAction drillThrough;

	/**
	 * Constructor for hyperlink action type
	 *
	 * @param actionString the action string
	 * @param target       the target window
	 */
	public ActionContent() {
	}

	@Override
	public void setHyperlink(String hyperlink, String target) {
		this.type = IHyperlinkAction.ACTION_HYPERLINK;
		this.hyperlink = hyperlink;
		this.target = target;
	}

	/**
	 * Constructor for bookmark action type
	 *
	 * @param bookmark the bookmark value.
	 */
	@Override
	public void setBookmark(String bookmark) {
		this.type = IHyperlinkAction.ACTION_BOOKMARK;
		this.bookmark = bookmark;
	}

	/**
	 * @deprecated Constructor for drill-through action type
	 *
	 * @param bookmark          the bookmark string
	 * @param bookmarkType      the bookmark type
	 * @param reportName        the report name navigated
	 * @param parameterBindings the parameters of the report navigated
	 * @param searchCriteria    the search criteria
	 * @param target            the target window
	 */
	@Deprecated
	@Override
	public void setDrillThrough(String bookmark, boolean isBookmark, String reportName, Map parameterBindings,
			Map searchCriteria, String target, String format) {
		setDrillThrough(bookmark, isBookmark, reportName, parameterBindings, searchCriteria, target, format, null);
	}

	/**
	 * Constructor for drill-through action type
	 *
	 * @param bookmark          the bookmark string
	 * @param bookmarkType      the bookmark type
	 * @param reportName        the report name navigated
	 * @param parameterBindings the parameters of the report navigated
	 * @param searchCriteria    the search criteria
	 * @param target            the target window
	 * @param targetFileType    the target file type
	 */
	@Override
	public void setDrillThrough(String bookmark, boolean isBookmark, String reportName, Map parameterBindings,
			Map searchCriteria, String target, String format, String targetFileType) {
		this.type = IHyperlinkAction.ACTION_DRILLTHROUGH;
		drillThrough = new DrillThroughAction(bookmark, isBookmark, reportName, parameterBindings, searchCriteria,
				target, format, targetFileType);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.IHyperlinkAction#getType()
	 */
	@Override
	public int getType() {
		return type;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.IHyperlinkAction#getBookmark()
	 */
	@Override
	public String getBookmark() {
		if (isDrillThrough()) {
			return drillThrough.getBookmark();
		}
		return bookmark;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.IHyperlinkAction#getReportName()
	 */
	@Override
	public String getReportName() {
		assert type == IHyperlinkAction.ACTION_DRILLTHROUGH;
		if (isDrillThrough()) {
			return drillThrough.getReportName();
		}
		return null;
	}

	@Override
	public void setReportName(String reportName) {
		assert type == IHyperlinkAction.ACTION_DRILLTHROUGH;
		if (isDrillThrough()) {
			drillThrough.setReportName(reportName);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.IHyperlinkAction#getParameterbindings()
	 */
	@Override
	public Map getParameterBindings() {
		if (isDrillThrough()) {
			return drillThrough.getParameterBindings();
		} else {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.IHyperlinkAction#getSearchCriteria()
	 */
	@Override
	public Map getSearchCriteria() {
		assert type == IHyperlinkAction.ACTION_DRILLTHROUGH;
		if (isDrillThrough()) {
			return drillThrough.getSearchCriteria();
		} else {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.content.IHyperlinkAction#getTargetWindow()
	 */
	@Override
	public String getTargetWindow() {
		if (isDrillThrough()) {
			return drillThrough.getTargetWindow();
		}
		return target;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.content.IHyperlinkAction#getHyperlink()
	 */
	@Override
	public String getHyperlink() {
		return hyperlink;
	}

	/**
	 * object document version
	 */
	static final protected int VERSION = 0;

	final static int FIELD_NONE = -1;
	final static int FIELD_TYPE = 0;
	final static int FIELD_BOOKMARK = 1;
	final static int FIELD_HYPERLINK = 2;
	final static int FIELD_REPORTNAME = 3;
	final static int FIELD_PARAMETERBINDINGS = 4;
	final static int FIELD_SEARCHCRITERIA = 5;
	final static int FIELD_TARGET = 6;
	final static int FIELD_FORMAT = 7;
	final static int FIELD_ISBOOKMARK = 8;
	final static int FIELD_TARGETFILETYPE = 9;
	final static int FIELD_TOOLTIP = 10;

	protected void writeFields(DataOutputStream out) throws IOException {
		if (type != -1) {
			IOUtil.writeInt(out, FIELD_TYPE);
			IOUtil.writeInt(out, type);
		}
		if (isDrillThrough()) {
			if (drillThrough.getBookmark() != null) {
				IOUtil.writeInt(out, FIELD_BOOKMARK);
				IOUtil.writeString(out, drillThrough.getBookmark());
			}
		} else if (bookmark != null) {
			IOUtil.writeInt(out, FIELD_BOOKMARK);
			IOUtil.writeString(out, bookmark);
		}
		if (isDrillThrough()) {
			if (drillThrough.isBookmark()) {
				IOUtil.writeInt(out, FIELD_ISBOOKMARK);
				IOUtil.writeBool(out, drillThrough.isBookmark());
			}
		}
		if (hyperlink != null) {
			IOUtil.writeInt(out, FIELD_HYPERLINK);
			IOUtil.writeString(out, hyperlink);
		}
		if (isDrillThrough() && drillThrough.getReportName() != null) {
			IOUtil.writeInt(out, FIELD_REPORTNAME);
			IOUtil.writeString(out, drillThrough.getReportName());
		}
		if (isDrillThrough() && drillThrough.getParameterBindings() != null) {
			IOUtil.writeInt(out, FIELD_PARAMETERBINDINGS);
			IOUtil.writeMap(out, drillThrough.getParameterBindings());
		}
		if (isDrillThrough() && drillThrough.getSearchCriteria() != null) {
			IOUtil.writeInt(out, FIELD_SEARCHCRITERIA);
			IOUtil.writeMap(out, drillThrough.getSearchCriteria());
		}
		if (isDrillThrough() && drillThrough.getTargetWindow() != null) {
			IOUtil.writeInt(out, FIELD_TARGET);
			IOUtil.writeString(out, drillThrough.getTargetWindow());
		} else if (target != null) {
			IOUtil.writeInt(out, FIELD_TARGET);
			IOUtil.writeString(out, target);
		}
		if (tooltip != null) {
			IOUtil.writeInt(out, FIELD_TOOLTIP);
			IOUtil.writeString(out, tooltip);
		}

		if (isDrillThrough() && drillThrough.getFormat() != null) {
			IOUtil.writeInt(out, FIELD_FORMAT);
			IOUtil.writeString(out, drillThrough.getFormat());
		}
		if (isDrillThrough() && drillThrough.getTargetFileType() != null) {
			IOUtil.writeInt(out, FIELD_TARGETFILETYPE);
			IOUtil.writeString(out, drillThrough.getTargetFileType());
		}
	}

	protected void readField(int version, int filedId, DataInputStream in, ClassLoader loader) throws IOException {
		switch (filedId) {
		case FIELD_TYPE:
			type = IOUtil.readInt(in);
			if (type == ACTION_DRILLTHROUGH) {
				drillThrough = new DrillThroughAction();
			}
			break;
		case FIELD_BOOKMARK:
			String bk = IOUtil.readString(in);
			if (isDrillThrough()) {
				drillThrough.setBookmark(bk);
			} else {
				bookmark = bk;
			}
			break;
		case FIELD_HYPERLINK:
			hyperlink = IOUtil.readString(in);
			break;
		case FIELD_REPORTNAME:
			String name = IOUtil.readString(in);
			if (isDrillThrough()) {
				drillThrough.setReportName(name);
			}
			break;
		case FIELD_PARAMETERBINDINGS:
			Map bindings = IOUtil.readMap(in, loader);
			if (isDrillThrough()) {
				drillThrough.setParameterBindings(bindings);
			}
			break;
		case FIELD_SEARCHCRITERIA:
			Map search = IOUtil.readMap(in, loader);
			if (isDrillThrough()) {
				drillThrough.setSearchCriteria(search);
			}
			break;
		case FIELD_TARGET:
			String tgt = IOUtil.readString(in);
			if (isDrillThrough()) {
				drillThrough.setTargetWindow(tgt);
			} else {
				target = tgt;
			}
			break;
		case FIELD_FORMAT:
			String fmt = IOUtil.readString(in);
			if (isDrillThrough()) {
				drillThrough.setFormat(fmt);
			}
			break;
		case FIELD_ISBOOKMARK:
			boolean isBk = IOUtil.readBool(in);
			if (isDrillThrough()) {
				drillThrough.setBookmarkType(isBk);
			}
			break;
		case FIELD_TARGETFILETYPE:
			String tgtType = IOUtil.readString(in);
			if (isDrillThrough()) {
				drillThrough.setTargetFileType(tgtType);
			}
			break;
		case FIELD_TOOLTIP:
			tooltip = IOUtil.readString(in);
			break;
		}
	}

	public void readObject(DataInputStream in, ClassLoader loader) throws IOException {
		int version = IOUtil.readInt(in);
		int filedId = IOUtil.readInt(in);
		while (filedId != FIELD_NONE) {
			readField(version, filedId, in, loader);
			filedId = IOUtil.readInt(in);
		}
	}

	public void writeObject(DataOutputStream out) throws IOException {
		IOUtil.writeInt(out, VERSION);
		writeFields(out);
		IOUtil.writeInt(out, FIELD_NONE);
	}

	@Override
	public String getFormat() {
		if (isDrillThrough()) {
			return drillThrough.getFormat();
		}
		return null;
	}

	@Override
	public void setBookmarkType(boolean isBookmark) {
		if (isDrillThrough()) {
			drillThrough.setBookmarkType(isBookmark);
		}
	}

	@Override
	public boolean isBookmark() {
		if (isDrillThrough()) {
			return drillThrough.isBookmark();
		}
		return false;
	}

	@Override
	public IDrillThroughAction getDrillThrough() {
		if (isDrillThrough()) {
			return drillThrough;
		}
		return null;
	}

	@Override
	public void setDrillThrough(IDrillThroughAction drillThrough) {
		this.type = IHyperlinkAction.ACTION_DRILLTHROUGH;
		this.drillThrough = drillThrough;
	}

	private boolean isDrillThrough() {
		return type == ACTION_DRILLTHROUGH;
	}

	@Override
	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	@Override
	public String getTooltip() {
		return tooltip;
	}

}

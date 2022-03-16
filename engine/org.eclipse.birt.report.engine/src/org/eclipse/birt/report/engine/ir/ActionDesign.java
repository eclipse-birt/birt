/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
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

/**
 * Action. Action include: hyperlink, drill through and bookmark.
 *
 */
public class ActionDesign {

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
	 * action type, one of the hyperlink, bookmark drillthrough.
	 */
	protected int actionType;

	protected String tooltip;
	/**
	 * hyper link
	 */
	protected Expression hyperlink;
	/**
	 * bookmark.
	 */
	protected Expression bookmark;

	/**
	 * drill through
	 */
	protected DrillThroughActionDesign drillThrough;

	/**
	 * The name of a frame where a document is to be opened.
	 */
	protected String target = null;

	/**
	 * @return Returns the bookmark.
	 */
	public Expression getBookmark() {
		assert this.actionType == ACTION_BOOKMARK;
		return bookmark;
	}

	/**
	 * @param bookmark The bookmark to set.
	 */
	public void setBookmark(Expression bookmark) {
		this.actionType = ActionDesign.ACTION_BOOKMARK;
		this.bookmark = bookmark;
	}

	/**
	 * @return Returns the hyperlink.
	 */
	public Expression getHyperlink() {
		assert this.actionType == ACTION_HYPERLINK;
		return hyperlink;
	}

	/**
	 * @param hyperlink The hyperlink to set.
	 */
	public void setHyperlink(Expression hyperlink) {
		this.hyperlink = hyperlink;
		this.actionType = ActionDesign.ACTION_HYPERLINK;
	}

	/**
	 * @return Returns the drillThrough.
	 */
	public DrillThroughActionDesign getDrillThrough() {
		assert this.actionType == ACTION_DRILLTHROUGH;
		return drillThrough;
	}

	/**
	 * @param drillThrough The drillThrough to set.
	 */
	public void setDrillThrough(DrillThroughActionDesign drillThrough) {
		this.actionType = ACTION_DRILLTHROUGH;
		this.drillThrough = drillThrough;
	}

	/**
	 * @return Returns the type.
	 */
	public int getActionType() {
		return actionType;
	}

	/**
	 * @return the target window.
	 */
	public String getTargetWindow() {
		return target;
	}

	/**
	 * @param target The name of a frame where a document is to be opened.
	 */
	public void setTargetWindow(String target) {
		this.target = target;
	}

	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	public String getTooltip() {
		return tooltip;
	}
}

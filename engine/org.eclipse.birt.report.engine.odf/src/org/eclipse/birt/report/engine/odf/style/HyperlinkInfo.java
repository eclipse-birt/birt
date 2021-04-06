/*******************************************************************************
 * Copyright (c) 2006 Inetsoft Technology Corp.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Inetsoft Technology Corp  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.odf.style;

public class HyperlinkInfo {

	private int type;

	private String url;

	private String tooltip;

	private StyleEntry style;

	private StyleEntry visitedStyle;

	public static final int BOOKMARK = 0;

	public static final int HYPERLINK = 1;

	public static final int DRILL = 2;

	public HyperlinkInfo(int type, String url, String toolTip) {
		this.type = type;
		this.url = url;
		this.tooltip = toolTip;
	}

	public String getUrl() {
		return this.url;
	}

	public String getTooltip() {
		return this.tooltip;
	}

	public int getType() {
		return this.type;
	}

	/**
	 * @return the style
	 */
	public StyleEntry getStyle() {
		return style;
	}

	/**
	 * @return the visitedStyle
	 */
	public StyleEntry getVisitedStyle() {
		return visitedStyle;
	}

	/**
	 * @param style the style to set
	 */
	public void setStyle(StyleEntry style) {
		this.style = style;
	}

	/**
	 * @param visitedStyle the visitedStyle to set
	 */
	public void setVisitedStyle(StyleEntry visitedStyle) {
		this.visitedStyle = visitedStyle;
	}

}
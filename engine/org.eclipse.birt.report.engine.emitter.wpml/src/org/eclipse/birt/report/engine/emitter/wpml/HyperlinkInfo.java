/*******************************************************************************
 * Copyright (c) 2006 Inetsoft Technology Corp.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Inetsoft Technology Corp  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.wpml;

public class HyperlinkInfo {

	private int type;

	private String url, bookmark;

	private String tooltip;

	private String color;

	public static final int BOOKMARK = 0;

	public static final int HYPERLINK = 1;

	public static final int DRILL = 2;

	public HyperlinkInfo(int type, String url, String toolTip) {
		this(type, url, null, toolTip);
	}

	public HyperlinkInfo(int type, String url, String bookmark, String toolTip) {
		this.type = type;
		this.url = url;
		this.bookmark = bookmark;
		this.tooltip = toolTip;
	}

	public String getUrl() {
		return this.url;
	}

	public String getBookmark() {
		return this.bookmark;
	}

	public String getTooltip() {
		return this.tooltip;
	}

	public int getType() {
		return this.type;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getColor() {
		return this.color;
	}
}

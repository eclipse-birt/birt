/*******************************************************************************
 * Copyright (c) 2006, 2024 Inetsoft Technology Corp and others
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

/**
 * Information class to handle the properties of the hyperlink
 *
 * @since 3.3
 *
 */
public class HyperlinkInfo {

	private int type;

	private String url, bookmark;

	private String tooltip;

	private String color;

	private boolean isHasHyperlinkDecoration = true;

	/** property: link type bookmark */
	public static final int BOOKMARK = 0;

	/** property: link type hyperlink */
	public static final int HYPERLINK = 1;

	/** property: link type drill through */
	public static final int DRILL = 2;

	/**
	 * Constructor 1
	 *
	 * @param type    link type
	 * @param url     link URL
	 * @param toolTip link tooltip text
	 */
	public HyperlinkInfo(int type, String url, String toolTip) {
		this(type, url, null, toolTip);
	}

	/**
	 * Constructor 2
	 *
	 * @param type     link type
	 * @param url      link URL
	 * @param bookmark link bookmark
	 * @param toolTip  link tooltip text
	 */
	public HyperlinkInfo(int type, String url, String bookmark, String toolTip) {
		this.type = type;
		this.url = url;
		this.bookmark = bookmark;
		this.tooltip = toolTip;
	}

	/**
	 * Get the link URL
	 *
	 * @return the link URL
	 */
	public String getUrl() {
		return this.url;
	}

	/**
	 * Get the bookmark
	 *
	 * @return the bookmark
	 */
	public String getBookmark() {
		return this.bookmark;
	}

	/**
	 * Get the hyperlink tooltip
	 *
	 * @return the hyperlink tooltip
	 */
	public String getTooltip() {
		return this.tooltip;
	}

	/**
	 * Get the hyperlink type
	 *
	 * @return the hyperlink type
	 */
	public int getType() {
		return this.type;
	}

	/**
	 * Set the text color
	 *
	 * @param color text color
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * Get the text color
	 *
	 * @return the text color
	 */
	public String getColor() {
		return this.color;
	}

	/**
	 * Set the information is the hyperlink text decoration in use
	 *
	 * @param hasHyperlinkDecoration is the hyperlink text decoration in use
	 */
	public void setHasHyperlinkDecoration(boolean hasHyperlinkDecoration) {
		this.isHasHyperlinkDecoration = hasHyperlinkDecoration;
	}

	/**
	 * Get the information is the hyperlink text decoration in use
	 *
	 * @return is the hyperlink text decoration in use
	 */
	public boolean isHasHyperlinkDecoration() {
		return this.isHasHyperlinkDecoration;
	}

}

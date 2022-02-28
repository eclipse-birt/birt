/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.service.api;

import java.util.List;

/**
 * Representation of a TOC
 *
 */
public class ToC {

	/**
	 * Children list
	 */
	private List children;

	/**
	 * TOC id
	 */
	private String id;

	/**
	 * Display name for toc
	 */
	private String displayName;

	/**
	 * Bookmark name
	 */
	private String bookmark;

	/**
	 * CSS style for toc
	 */
	private String style;

	/**
	 * Constructor
	 *
	 * @param id
	 * @param displayName
	 * @param bookmark
	 * @param style
	 */
	public ToC(String id, String displayName, String bookmark, String style) {
		this.id = id;
		this.displayName = displayName;
		this.bookmark = bookmark;
		this.style = style;
	}

	/**
	 * @return the children list
	 */
	public List getChildren() {
		return children;
	}

	/**
	 * Set children list
	 *
	 * @param children
	 */
	public void setChildren(List children) {
		this.children = children;
	}

	/**
	 * @return the id
	 */
	public String getID() {
		return id;
	}

	/**
	 * @return the display name
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @return the bookmark
	 */
	public String getBookmark() {
		return bookmark;
	}

	/**
	 * @return the style
	 */
	public String getStyle() {
		return style;
	}
}

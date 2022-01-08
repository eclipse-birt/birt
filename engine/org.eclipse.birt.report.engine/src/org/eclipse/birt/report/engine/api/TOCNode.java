/*******************************************************************************
 * Copyright (c) 2004,2008 Actuate Corporation.
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

package org.eclipse.birt.report.engine.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.engine.api.script.instance.IScriptStyle;

/**
 * A node that wraps around a TOC entry. To navigate to a specific TOC entry,
 * one starts with the TOC root and traverse down the TOC tree, obtaining list
 * of TOC nodes.
 * 
 * For each node, the user can go to a specific page by following the bookmark.
 */
public class TOCNode {
	public static final List EMPTY_CHILDREN = Collections.unmodifiableList(new ArrayList(0));
	/**
	 * the string to be displayed for the TOC entry
	 */
	protected String displayString;

	/**
	 * A bookmark that is stored for the TOC
	 */
	protected String bookmark;

	/**
	 * the parent node
	 */
	protected TOCNode parent;

	/**
	 * identifier for the current node
	 */
	protected String nodeId;

	/**
	 * A list of children for the TOC node
	 */
	protected List children;

	/**
	 * The TOC's style
	 */
	protected IScriptStyle tocStyle;

	/**
	 * Constructor.
	 */
	public TOCNode() {
	}

	/**
	 * Constructor.
	 * 
	 * @param node
	 */
	public TOCNode(TOCNode node) {
		this.bookmark = node.bookmark;
		this.displayString = node.displayString;
		this.nodeId = node.nodeId;
	}

	/**
	 * @return the unique ID for the TOC node
	 */
	public String getNodeID() {
		return nodeId;
	}

	/**
	 * @return returns the parent node of the current TOC node
	 */
	public TOCNode getParent() {
		return parent;
	}

	/**
	 * @return the list of child TOC nodes
	 */
	public List getChildren() {
		if (children == null) {
			children = EMPTY_CHILDREN;
		}
		return children;
	}

	/**
	 * @return the display string for the TOC entry
	 */
	public String getDisplayString() {
		return displayString;
	}

	/**
	 * @param display the display string
	 */
	public void setDisplayString(String displayStr) {
		this.displayString = displayStr;
	}

	/**
	 * @return the bookmark string that the TOC item points to.
	 */
	public String getBookmark() {
		return bookmark;
	}

	/**
	 * Set bookmark
	 * 
	 * @param bookmark
	 */
	public void setBookmark(String bookmark) {
		this.bookmark = bookmark;
	}

	/**
	 * Set Node id
	 * 
	 * @param id
	 */
	public void setNodeID(String id) {
		this.nodeId = id;
	}

	/**
	 * Set parent toc.
	 * 
	 * @param parent
	 */
	public void setParent(TOCNode parent) {
		this.parent = parent;
	}

	/**
	 * Get toc style.
	 * 
	 * @return toc style
	 */
	public IScriptStyle getTOCStyle() {
		return tocStyle;
	}

	/**
	 * Set toc style.
	 * 
	 * @param toc style
	 */
	public void setTOCStyle(IScriptStyle tocStyle) {
		this.tocStyle = tocStyle;
	}
}

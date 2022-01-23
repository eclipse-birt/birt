/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.engine.toc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.engine.api.TOCNode;

public class ViewNode extends TOCNode {

	static final List<ViewNode> EMPTY_CHILDREN = Collections.unmodifiableList(new ArrayList<ViewNode>(0));

	TOCView view;
	ITreeNode node;
	int level;

	ViewNode(ViewNode parent, ITreeNode node) {
		this(parent.view, parent, node);
	}

	ViewNode(TOCView view, ViewNode parent, ITreeNode node) {
		this.view = view;

		this.node = node;
		this.nodeId = node.getNodeId();
		this.bookmark = node.getBookmark();

		this.parent = parent;
		// setup the fields if the node is not the root.
		if (parent != null) {
			this.level = parent.level + 1;
			this.tocStyle = view.getTOCStyle(level - 1, node.getElementId());
			Object value = node.getTOCValue();
			if (value != null) {
				this.displayString = view.localizeValue(value, tocStyle);
			}
		}
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public List getChildren() {
		if (children == null) {
			children = new ViewNodeList(this);
			;
		}
		return children;
	}

	public Object getTOCValue() {
		return node.getTOCValue();
	}

}

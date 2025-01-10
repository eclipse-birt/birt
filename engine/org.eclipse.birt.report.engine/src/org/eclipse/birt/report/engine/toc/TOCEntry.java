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

package org.eclipse.birt.report.engine.toc;

import java.util.Collection;

public class TOCEntry extends TreeNode {

	TOCEntry parent;

	ITreeNode treeNode;

	int nextChildId;

	@Override
	public Collection<ITreeNode> getChildren() {
		return null;
	}

	public ITreeNode getTreeNode() {
		return treeNode;
	}

	public TOCEntry getParent() {
		return parent;
	}

	public void setParent(TOCEntry parent) {
		this.parent = parent;
	}

	public void setTreeNode(ITreeNode treeNode) {
		this.treeNode = treeNode;
	}

	/**
	 * Returns the nesting level.
	 *
	 * @return 0 for the root note, 1 + parent level otherwise.
	 */
	public int getLevel() {
		if (parent == null) {
			return 0;
		}
		return 1 + parent.getLevel();
	}

}

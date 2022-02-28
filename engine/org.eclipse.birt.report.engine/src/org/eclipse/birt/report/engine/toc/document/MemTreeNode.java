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

package org.eclipse.birt.report.engine.toc.document;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.birt.report.engine.toc.ITreeNode;
import org.eclipse.birt.report.engine.toc.TreeNode;

public class MemTreeNode extends TreeNode {

	protected ArrayList<ITreeNode> children;

	protected MemTreeNode parent;

	public MemTreeNode() {
	}

	public MemTreeNode(TreeNode entry) {
		super(entry);
	}

	@Override
	public Collection<ITreeNode> getChildren() {
		if (children == null) {
			children = new ArrayList<>();
		}
		return children;
	}

	public void addChild(MemTreeNode node) {
		if (children == null) {
			children = new ArrayList<>();
		}
		children.add(node);
	}

	public MemTreeNode getParent() {
		return parent;
	}

	public void setParent(MemTreeNode parent) {
		this.parent = parent;
	}

}

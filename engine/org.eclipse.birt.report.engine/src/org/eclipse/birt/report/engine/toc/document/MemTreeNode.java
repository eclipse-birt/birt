/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

	public Collection<ITreeNode> getChildren() {
		if (children == null) {
			children = new ArrayList<ITreeNode>();
		}
		return children;
	}

	public void addChild(MemTreeNode node) {
		if (children == null) {
			children = new ArrayList<ITreeNode>();
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

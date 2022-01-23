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

import java.util.Collection;

import org.eclipse.birt.report.engine.toc.ITreeNode;
import org.eclipse.birt.report.engine.toc.TreeNode;

public class DocTreeNode extends TreeNode {

	static final int OFFSET_NEXT = 0;
	static final int OFFSET_CHILD = 4;
	static final int OFFSET_CHILD_COUNT = 8;

	protected DocTreeNode parent;
	protected int offset = -1;
	protected int next = -1;
	protected int child = -1;
	protected int childCount = 0;

	protected Collection<ITreeNode> children;

	public DocTreeNode() {
	}

	public DocTreeNode(TreeNode entry) {
		super(entry);
	}

	public Collection<ITreeNode> getChildren() {
		return children;
	}

	public DocTreeNode getParent() {
		return parent;
	}

	public void setParent(DocTreeNode parent) {
		this.parent = parent;
	}
}

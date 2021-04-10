
package org.eclipse.birt.report.engine.toc;

import java.util.Collection;

public class TOCEntry extends TreeNode {

	TOCEntry parent;

	ITreeNode treeNode;

	int nextChildId;

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

}

/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.toc;

import java.util.AbstractList;
import java.util.Iterator;
import java.util.LinkedList;

import org.eclipse.birt.report.engine.api.script.instance.IScriptStyle;

public class ViewNodeList extends AbstractList<ViewNode> {

	ViewNode parent;

	private static final int MAX_CACHE_SIZE = 16;
	private ViewNode[] cacheChildren = new ViewNode[MAX_CACHE_SIZE];
	private ViewNodeIterator cacheIter;
	private int cacheIndex;
	private int size;

	public ViewNodeList(ViewNode parent) {
		this.parent = parent;
		this.size = -1;
	}

	synchronized public ViewNode get(int index) {
		if (index < 0) {
			throw new IndexOutOfBoundsException("Index: " + index);
		}
		refreshCaches(index);
		if (index >= cacheIndex - MAX_CACHE_SIZE && index < cacheIndex) {
			return cacheChildren[index % MAX_CACHE_SIZE];
		}
		throw new IndexOutOfBoundsException("Index: " + index);
	}

	protected void refreshCaches(int index) {
		if (index < cacheIndex) {
			int firstCache = cacheIndex - MAX_CACHE_SIZE;
			if (index >= firstCache) {
				return;
			}
		}

		if (index < cacheIndex || cacheIter == null) {
			cacheIter = new ViewNodeIterator();
			cacheIndex = 0;
		}

		for (int i = cacheIndex; i <= index; i++) {
			if (cacheIter.hasNext()) {
				cacheChildren[i % MAX_CACHE_SIZE] = cacheIter.next();
				cacheIndex++;
				continue;
			}
			size = cacheIndex;
		}
	}

	synchronized public int size() {
		if (size == -1) {
			size = calculateListSize();
		}
		return size;
	}

	private int calculateListSize() {
		ViewNodeIterator iter = new ViewNodeIterator();
		int size = 0;
		while (iter.hasNext()) {
			iter.next();
			size++;
		}
		return size;
	}

	class ViewNodeIterator implements Iterator<ViewNode> {

		LinkedList<Iterator<ITreeNode>> nodeIters = new LinkedList<Iterator<ITreeNode>>();
		ViewNode nextNode;

		ViewNodeIterator() {
			nodeIters.addLast(parent.node.getChildren().iterator());
			nextNode = getNextNode();
		}

		public boolean hasNext() {
			return nextNode != null;
		}

		public ViewNode next() {
			if (nextNode != null) {
				ViewNode returnNode = nextNode;
				nextNode = getNextNode();
				return returnNode;
			}
			return null;
		}

		ViewNode getNextNode() {
			while (!nodeIters.isEmpty()) {
				Iterator<ITreeNode> nodeIter = nodeIters.getLast();
				while (nodeIter.hasNext()) {
					ITreeNode node = nodeIter.next();
					if (isHidden(node)) {
						continue;
					}

					if (!isVisible(node)) {
						// try to the iterator
						nodeIter = node.getChildren().iterator();
						nodeIters.addLast(nodeIter);
						continue;
					}

					if (node.isGroup()) {
						ViewNode group = createGroupNode(parent, node);
						if (group != null) {
							return group;
						}
						continue;
					}

					if (node.getTOCValue() == null) {
						nodeIter = node.getChildren().iterator();
						nodeIters.addLast(nodeIter);
						continue;
					}
					return new ViewNode(parent, node);

				}
				nodeIters.removeLast();
			}
			return null;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	protected ViewNode createGroupNode(ViewNode parent, ITreeNode treeNode) {
		ITreeNode labelNode = getFirstNoneNode(treeNode);
		if (labelNode != null) {
			ViewNode groupNode = new ViewNode(parent, treeNode);
			Object groupValue = labelNode.getTOCValue();
			IScriptStyle groupStyle = groupNode.getTOCStyle();
			String groupLabel = parent.view.localizeValue(groupValue, groupStyle);
			groupNode.setDisplayString(groupLabel);
			return groupNode;
		}
		return null;
	}

	private ITreeNode getFirstNoneNode(ITreeNode treeNode) {
		if (isHidden(treeNode)) {
			return null;
		}
		if (isVisible(treeNode)) {
			if (treeNode.getTOCValue() != null) {
				return treeNode;
			}
		}
		for (ITreeNode childNode : treeNode.getChildren()) {
			ITreeNode firstNode = getFirstNoneNode(childNode);
			if (firstNode != null) {
				return firstNode;
			}
		}
		return null;
	}

	protected boolean isHidden(ITreeNode node) {
		return parent.view.isHidden(node);
	}

	protected boolean isVisible(ITreeNode node) {
		return parent.view.isVisible(node);
	}

}

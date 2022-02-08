/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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
package org.eclipse.birt.data.engine.impl.jointdataset;

import java.util.List;

import org.eclipse.birt.data.engine.core.DataException;

/**
 * The helper class which encapsulate the binary search algorithm.
 */
class BinaryTreeROSeekerHelper {
	private Node rootNode;
	private Node latestNode;

	/**
	 * Constructor.
	 * 
	 * @param segmentInfoList
	 */
	BinaryTreeROSeekerHelper(List segmentInfoList) {
		rootNode = populateNode(segmentInfoList);
	}

	/**
	 * Search the match SegmentInfo in the tree.
	 * 
	 * @param o
	 * @return
	 * @throws DataException
	 */
	SegmentInfo search(Object[] o) throws DataException {
		if (latestNode != null && JointDataSetUtil.compare(o, latestNode.getSegmentInfo().getMinValue()) >= 0
				&& JointDataSetUtil.compare(o, latestNode.getSegmentInfo().getMaxValue()) <= 0) {
			return latestNode.getSegmentInfo();
		} else {
			Node n = findNodeWithValue(o, this.rootNode);
			latestNode = n;
			return n == null ? null : n.getSegmentInfo();
		}
	}

	/**
	 * 
	 * @param list
	 * @return
	 */
	private Node populateNode(List list) {
		int index = list.size() / 2;
		if (index >= list.size())
			return null;
		Node n = new Node((SegmentInfo) list.get(index));
		List left = list.subList(0, index);

		List right = list.subList(index + 1, list.size());

		n.setLeftChild(left.size() == 0 ? null : populateNode(left));
		n.setRightChild(right.size() == 0 ? null : populateNode(right));
		return n;
	}

	/**
	 * Find the node which encloses the given object array values.
	 * 
	 * @param o
	 * @param node
	 * @return
	 * @throws DataException
	 */
	private Node findNodeWithValue(Object[] o, Node node) throws DataException {
		int compareMin = JointDataSetUtil.compare(o, node.getSegmentInfo().getMinValue());
		int compareMax = JointDataSetUtil.compare(o, node.getSegmentInfo().getMaxValue());
		if (compareMin >= 0 && compareMax <= 0)
			return node;
		if (compareMin < 0 && node.getLeftChild() != null)
			return findNodeWithValue(o, node.getLeftChild());
		if (compareMax > 0 && node.getRightChild() != null)
			return findNodeWithValue(o, node.getRightChild());
		return null;
	}

	/**
	 * Tree node classes.
	 *
	 */
	private static class Node {
		//
		private Node leftChild;
		private Node rightChild;

		private SegmentInfo sinfo;

		/**
		 * Constructor.
		 * 
		 * @param sinfo
		 */
		private Node(SegmentInfo sinfo) {
			this.sinfo = sinfo;
		}

		/**
		 * Get the SegmentInfo of this tree node.
		 * 
		 * @return
		 */
		private SegmentInfo getSegmentInfo() {
			return this.sinfo;
		}

		/**
		 * Get left child.
		 * 
		 * @return
		 */
		private Node getLeftChild() {
			return this.leftChild;
		}

		private Node getRightChild() {
			return this.rightChild;
		}

		private void setLeftChild(Node leftChild) {
			if (leftChild == null)
				return;

			this.leftChild = leftChild;
		}

		private void setRightChild(Node rightChild) {
			if (rightChild == null)
				return;
			this.rightChild = rightChild;
		}
	}
}

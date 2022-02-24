/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
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
package org.eclipse.birt.data.engine.olap.cursor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 *
 */
class MemberTreeNode {

	List<MemberTreeNode> childNodesList;
	Object key;
	MemberTreeNode parentNode;

	MemberTreeNode(Object key) {
		this.childNodesList = new ArrayList<MemberTreeNode>();
		this.key = key;
	}

	void insertNode(MemberTreeNode node) {
		childNodesList.add(node);
	}

	void addAllNodes(MemberTreeNode[] nodes) {
		List nodesList = Arrays.asList(nodes);
		childNodesList.addAll(nodesList);
		for (int i = 0; i < nodesList.size(); i++) {
			((MemberTreeNode) nodesList.get(i)).parentNode = this;
		}
	}

	MemberTreeNode getChild(Object childKey) {
		for (int i = 0; i < this.childNodesList.size(); i++) {
			MemberTreeNode node = (MemberTreeNode) childNodesList.get(i);
			if (node.key.equals(childKey)) {
				return node;
			}
		}
		return null;
	}

	boolean containsChild(Object childKey) {
		for (int i = 0; i < this.childNodesList.size(); i++) {
			MemberTreeNode node = (MemberTreeNode) childNodesList.get(i);
			if (node.key.equals(childKey)) {
				return true;
			}
		}
		return false;
	}
}

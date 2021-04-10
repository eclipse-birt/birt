/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.core.ui.frameworks.taskwizard.composites;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.ibm.icu.util.StringTokenizer;

/**
 * @author Actuate Corporation
 */
public class NavTree extends Tree {

	public final static String SEPARATOR = "."; //$NON-NLS-1$

	public NavTree(Composite parent, int style) {
		super(parent, style);
	}

	protected void checkSubclass() {
	}

	/**
	 * Adds nodes with full path and display name.
	 * 
	 * @param nodePath    Full path is used to search. Every section of path is
	 *                    stored in item's data.
	 * @param displayName Name is used to display only. If null or blank, use
	 *                    current path instead.
	 */
	public boolean addNode(String nodePath, String displayName) {
		StringTokenizer stk = new StringTokenizer(nodePath, SEPARATOR);
		TreeItem currentItem = null;
		while (stk.hasMoreTokens()) {
			String str = stk.nextToken();
			currentItem = findAndAdd(str, currentItem, displayName);
		}
		return true;
	}

	/**
	 * Adds nodes with full path.
	 * 
	 * @param nodePath Full path is used to search. Every section of path is stored
	 *                 in item's data.
	 */
	public boolean addNode(String nodePath) {
		return addNode(nodePath, null);
	}

	private TreeItem findAndAdd(String sValue, TreeItem tiRoot, String displayName) {
		TreeItem tiTmp = findDirectChildren(tiRoot, sValue);
		if (tiTmp != null) {
			return tiTmp;
		}
		if (tiRoot != null) {
			tiTmp = add(tiRoot, sValue, displayName);
			if (!tiRoot.getExpanded()) {
				tiRoot.setExpanded(true);
			}
		} else {
			tiTmp = add(null, sValue, displayName);
		}
		return tiTmp;
	}

	private TreeItem findDirectChildren(TreeItem tiSubTree, String nodePath) {
		TreeItem[] tiNodes = null;
		if (tiSubTree == null) {
			tiNodes = getItems();
		} else {
			tiNodes = tiSubTree.getItems();
		}
		for (int iC = 0; iC < tiNodes.length; iC++) {
			if (tiNodes[iC].getData().equals(nodePath)) {
				return tiNodes[iC];
			}
		}
		return null;
	}

	private TreeItem add(TreeItem tiParent, String sNode, String displayName) {
		TreeItem tiTmp = findDirectChildren(tiParent, sNode);
		if (tiTmp == null) {
			if (tiParent != null) {
				tiTmp = new TreeItem(tiParent, SWT.NONE);
			} else {
				tiTmp = new TreeItem(this, SWT.NONE);
			}
			// Display name is optional
			if (displayName == null || displayName.trim().length() == 0) {
				tiTmp.setText(sNode);
			} else {
				tiTmp.setText(displayName);
			}
			// Add the path into the tree node
			tiTmp.setData(sNode);
		}
		return tiTmp;
	}

	/**
	 * Finds tree item according to full path.
	 * 
	 * @param nodePath Full path with <code>NavTree.SEPARATOR</code>.
	 * @return TreeItem or null if not found
	 */
	public TreeItem findTreeItem(String nodePath) {
		if (nodePath == null) {
			return null;
		}
		StringTokenizer tokens = new StringTokenizer(nodePath, NavTree.SEPARATOR);
		TreeItem item = null;
		TreeItem[] children = getItems();
		while (tokens.hasMoreTokens()) {
			String nodeText = tokens.nextToken();
			if (children.length == 0) {
				return item;
			}

			boolean isFound = false;
			for (int i = 0; i < children.length; i++) {
				if (children[i].getData().equals(nodeText)) {
					isFound = true;
					item = children[i];
					children = item.getItems();
					break;
				}
			}
			if (!isFound) {
				return null;
			}
		}
		return item;
	}

	/**
	 * Returns the full path of the node.
	 * 
	 * @return Full path of current node with <code>NavTree.SEPARATOR</code>
	 *         separated
	 */
	public String getNodePath(TreeItem item) {
		String nodePath = (String) item.getData();
		while (item.getParentItem() != null) {
			item = item.getParentItem();
			nodePath = item.getData() + NavTree.SEPARATOR + nodePath;
		}
		return nodePath;
	}
}
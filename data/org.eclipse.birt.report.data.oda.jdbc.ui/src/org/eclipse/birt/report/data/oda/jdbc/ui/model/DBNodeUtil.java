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
package org.eclipse.birt.report.data.oda.jdbc.ui.model;

import org.eclipse.birt.report.data.oda.jdbc.ui.JdbcPlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class DBNodeUtil {
	private DBNodeUtil() {
	}

	// bidi_hcg: add metadataBidiFormatStr parameter to allow Bidi transformations
	// (if required)
	public static void createRootTip(Tree tree, RootNode node, String metadataBidiFormatStr) {
		tree.removeAll();
		TreeItem root = new TreeItem(tree, SWT.NONE);
		// bidi_hcg: pass value of metadataBidiFormatStr
		root.setText(node.getDisplayName(metadataBidiFormatStr));
		root.setImage(node.getImage());
		root.setData(node);
	}

	// bidi_hcg: add metadataBidiFormatStr parameter to allow Bidi transformations
	// (if required)
	public static void createTreeRoot(Tree tree, RootNode node, FilterConfig fc, String metadataBidiFormatStr,
			long timeout) {
		tree.removeAll();
		TreeItem dummyItem = new TreeItem(tree, SWT.NONE);
		dummyItem.setText(JdbcPlugin.getResourceString("tablepage.refreshing"));

		if (!node.isChildrenPrepared()) {
			node.prepareChildren(fc, timeout);
		}
		tree.removeAll();
		TreeItem root = new TreeItem(tree, SWT.NONE);
		root.setText(node.getDisplayName(null));
		root.setImage(node.getImage());
		root.setData(node);
		IDBNode[] children = node.getChildren();
		if (children != null) {
			for (IDBNode child : children) {
				// bidi_hcg: pass value of metadataBidiFormatStr to child element
				createTreeItem(root, child, metadataBidiFormatStr);
			}
		}
		root.setExpanded(true);
	}

	// bidi_hcg: add metadataBidiFormatStr parameter to allow Bidi transformations
	// (if required)
	public static TreeItem createTreeItem(TreeItem parent, IDBNode node, String metadataBidiFormatStr) {
		TreeItem item = new TreeItem(parent, SWT.NONE);
		// bidi_hcg: pass value of metadataBidiFormatStr
		item.setText(node.getDisplayName(metadataBidiFormatStr));
		item.setImage(node.getImage());
		item.setData(node);
		item.setExpanded(false);
		if (node instanceof ChildrenAllowedNode) {
			if (((ChildrenAllowedNode) node).isChildrenPrepared()) {
				// add all prepared children
				IDBNode[] children = ((ChildrenAllowedNode) node).getChildren();
				if (children != null) {
					for (IDBNode child : children) {
						// bidi_hcg: pass value of metadataBidiFormatStr
						createTreeItem(item, child, metadataBidiFormatStr);
					}
				}
			} else {
				// create a dummy child to flag that this tree node may have children waiting to
				// be explored
				new TreeItem(item, SWT.NONE);
			}
		}
		return item;
	}
}

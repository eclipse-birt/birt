/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.designer.testutil;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * Viewer utility
 * 
 * 
 * 
 */
public class ViewerUnti {

	/**
	 * View Tree
	 * 
	 * @param treeViewer the tree viewer to display
	 */
	public static void viewTree(TreeViewer treeViewer) {
		viewTree(treeViewer.getTree());
	}

	/**
	 * View Tree
	 * 
	 * @param tree the tree to display
	 */
	public static void viewTree(Tree tree) {
		viewTree(tree.getItems()[0]);
	}

	/**
	 * View Tree
	 * 
	 * @param root the tree item to display
	 */

	public static void viewTree(TreeItem root) {
		viewTree(root, root);
	}

	private static void viewTree(TreeItem item, TreeItem root) {
		if (item.getItemCount() != 0) {
			if (item == root) {
				System.err.println();
			}
			if (item.getExpanded()) {
				TreeItem[] ti = item.getItems();
				System.err.println("-" //$NON-NLS-1$
						+ item.getText() + "(" + ti.length + ")"); //$NON-NLS-1$ //$NON-NLS-2$
				for (int i = 0; i < ti.length; i++) {
					String out = ""; //$NON-NLS-1$
					TreeItem parent = item;
					while (parent != root) {
						TreeItem[] tp = parent.getParentItem().getItems();
						if (parent != tp[tp.length - 1]) {
							out = " \u2502" + out; //$NON-NLS-1$
						} else {
							out = "  " + out; //$NON-NLS-1$
						}
						parent = parent.getParentItem();
					}
					if (i == item.getItemCount() - 1) {
						out += " \u2514"; //$NON-NLS-1$
					} else {
						out += " \u251C"; //$NON-NLS-1$
					}

					System.err.print(out);
					viewTree(ti[i], root);
				}
			} else
				System.err.println("+" + item.getText() + "(?)"); //$NON-NLS-1$ //$NON-NLS-2$
		} else
			System.err.println(item.getText());
	}
}

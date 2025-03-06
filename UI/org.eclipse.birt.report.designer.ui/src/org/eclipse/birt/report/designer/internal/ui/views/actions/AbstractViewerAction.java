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

package org.eclipse.birt.report.designer.internal.ui.views.actions;

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * The super class of all the action attached with viewers
 */

public abstract class AbstractViewerAction extends AbstractViewAction {

	/**
	 * Creates an action on the specific viewer
	 *
	 * @param sourceViewer the source viewer
	 */
	public AbstractViewerAction(TreeViewer sourceViewer) {
		super(sourceViewer);
	}

	/**
	 * Creates an action on the specific viewer with given text
	 *
	 * @param sourceViewer the source viewer
	 * @param text         the text for the action
	 */
	public AbstractViewerAction(TreeViewer sourceViewer, String text) {
		super(sourceViewer, text);
	}

	/**
	 * Gets the source viewer
	 *
	 * @return Returns the source viewer
	 */
	public TreeViewer getSourceViewer() {
		return (TreeViewer) getSelection();
	}

	/**
	 * Gets the list of the selected objects
	 *
	 * @return Returns the list of the selected objects
	 */
	public StructuredSelection getSelectedObjects() {
		return (StructuredSelection) getSourceViewer().getSelection();
	}

	/**
	 * Gets selected tree items
	 *
	 * @return Returns selected tree items
	 */
	public TreeItem[] getSelectedItems() {
		TreeViewer treeViewer = getSourceViewer();
		Tree tree = treeViewer.getTree();
		TreeItem[] treeItems = tree.getSelection();
		return treeItems;
	}
}

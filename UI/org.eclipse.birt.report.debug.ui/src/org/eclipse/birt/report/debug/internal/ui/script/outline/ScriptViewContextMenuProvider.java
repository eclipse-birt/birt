/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.debug.internal.ui.script.outline;

import org.eclipse.birt.report.designer.ui.ContextMenuProvider;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IWorkbenchActionConstants;

/**
 * Create the menu in the outline node.
 */

public class ScriptViewContextMenuProvider extends ContextMenuProvider {

	public ScriptViewContextMenuProvider(ISelectionProvider viewer) {
		super(viewer);
	}

	public void buildContextMenu(IMenuManager menu) {
		menu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		TreeViewer treeViewer = (TreeViewer) getViewer();

		IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();

		// temporary solution

		if (selection.size() == 1) {
			Object obj = selection.getFirstElement();
			ScriptProviderFactory.createProvider(obj).createContextMenu(treeViewer, obj, menu);
		} else {
			ScriptProviderFactory.getDefaultProvider().createContextMenu(treeViewer, selection, menu);

		}
	}

}

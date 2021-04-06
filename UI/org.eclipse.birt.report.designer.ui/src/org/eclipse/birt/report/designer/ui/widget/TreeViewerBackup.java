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

package org.eclipse.birt.report.designer.ui.widget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.osgi.service.environment.Constants;

public class TreeViewerBackup implements ITreeViewerBackup {

	protected List leafList = new ArrayList();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.ui.editors.PageViewerBackup#restoreBackup(
	 * org.eclipse.jface.viewers.TreeViewer)
	 */
	public void restoreBackup(TreeViewer treeviewer) {
		for (int i = 0; i < leafList.size(); i++) {
			treeviewer.expandToLevel(leafList.get(i), 1);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.editors.PageViewerBackup#dispose()
	 */
	public void dispose() {
		leafList.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.editors.PageViewerBackup#
	 * updateCollapsedBackup(org.eclipse.jface.viewers.TreeViewer, java.lang.Object)
	 */
	public void updateCollapsedStatus(TreeViewer treeViewer, Object data) {
		if (!Constants.OS_MACOSX.equalsIgnoreCase(Platform.getOS())) {// collapse all sub nodes if not in MacOS
			treeViewer.collapseToLevel(data, TreeViewer.ALL_LEVELS);
		}
		updateStatus(treeViewer);
	}

	public void updateStatus(TreeViewer treeViewer) {
		TreePath[] treepaths = treeViewer.getExpandedTreePaths();
		List list = Arrays.asList(treepaths);
		leafList.clear();
		leafList.addAll(list);
		for (int i = 0; i < leafList.size(); i++) {
			TreePath path = ((TreePath) leafList.get(i)).getParentPath();
			if (path == null) {
				leafList.remove(i);
				i--;
			}
			if (leafList.contains(path)) {
				leafList.remove(path);
				i--;
			}
		}
	}

	public void updateExpandedStatus(TreeViewer treeViewer, Object data) {
		treeViewer.expandToLevel(data, 1);
		updateStatus(treeViewer);
	}

}

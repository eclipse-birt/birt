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

package org.eclipse.birt.report.designer.ui.widget;

import org.eclipse.jface.viewers.TreeViewer;

public interface ITreeViewerBackup {

	void restoreBackup(TreeViewer treeviewer);

	void dispose();

	void updateCollapsedStatus(TreeViewer treeViewer, Object collapsedElement);

	void updateExpandedStatus(TreeViewer treeViewer, Object expandedElement);

	void updateStatus(TreeViewer treeViewer);

}

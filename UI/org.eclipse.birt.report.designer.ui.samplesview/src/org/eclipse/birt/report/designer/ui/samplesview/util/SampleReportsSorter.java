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

package org.eclipse.birt.report.designer.ui.samplesview.util;

import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

public class SampleReportsSorter extends ViewerSorter {
	public int compare(Viewer viewer, Object e1, Object e2) {
		IBaseLabelProvider provider = null;
		if (viewer instanceof TreeViewer) {
			provider = ((TreeViewer) viewer).getLabelProvider();
		}
		if (provider != null && provider instanceof SampleReportsExplorerProvider) {
			if (((SampleReportsExplorerProvider) provider).getText(e1).equals("Contribute Samples")) //$NON-NLS-1$
			{
				return 1;
			} else if (((SampleReportsExplorerProvider) provider).getText(e2).equals("Contribute Samples")) //$NON-NLS-1$
			{
				return -1;
			}
		}
		return 0;
	}
}

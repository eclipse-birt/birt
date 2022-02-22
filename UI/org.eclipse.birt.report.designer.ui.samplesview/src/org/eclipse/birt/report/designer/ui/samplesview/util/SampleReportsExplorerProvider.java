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

package org.eclipse.birt.report.designer.ui.samplesview.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceEntry;
import org.eclipse.birt.report.designer.internal.ui.views.ViewsTreeProvider;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

public class SampleReportsExplorerProvider extends ViewsTreeProvider {

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.ViewsTreeProvider#
	 * getChildren(java.lang.Object)
	 */
	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof ResourceEntry) {
			Object[] children = ((ResourceEntry) parentElement).getChildren();
			List childrenList = new ArrayList();
			for (int i = 0; i < children.length; i++) {
				Object report = ((ResourceEntry) children[i]).getAdapter(ReportDesignHandle.class);
				if (report != null) {
					childrenList.add(report);
				} else {
					childrenList.add(children[i]);
				}
			}
			return childrenList.toArray();
		}
		return super.getChildren(parentElement);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.views.ViewsTreeProvider#getImage
	 * (java.lang.Object)
	 */
	@Override
	public Image getImage(Object element) {
		if (element instanceof ResourceEntry) {
			return ((ResourceEntry) element).getImage();
		}
		return super.getImage(element);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.views.ViewsTreeProvider#getText(
	 * java.lang.Object)
	 */
	@Override
	public String getText(Object element) {
		if (element instanceof ReportDesignHandle) {
			String fileName = ((ReportDesignHandle) element).getFileName();
			// fileName is a URL string.
			return fileName.substring(fileName.lastIndexOf("/") + 1); //$NON-NLS-1$
		}
		if (element instanceof ResourceEntry) {
			return ((ResourceEntry) element).getName();
		}
		if (element instanceof String) {
			return element.toString();
		}
		return super.getText(element);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.ViewsTreeProvider#
	 * hasChildren(java.lang.Object)
	 */
	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof ResourceEntry) {
			return true;
		}
		return super.hasChildren(element);
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if (oldInput instanceof Object[]) {
			Object[] array = (Object[]) oldInput;
			for (int i = 0; i < array.length; i++) {
				if (array[i] instanceof ResourceEntry) {
					((ResourceEntry) array[i]).dispose();
				}
			}
		}
		super.inputChanged(viewer, oldInput, newInput);
	}
}

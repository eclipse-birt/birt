/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.util;

import java.util.List;

import org.eclipse.birt.report.designer.core.model.views.property.GroupPropertyHandleWrapper;
import org.eclipse.birt.report.model.api.GroupPropertyHandle;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

/**
 * A viewer sorter is used by property sheet to reorder the elements provided by
 * its content provider.
 *
 * @see IStructuredContentProvider
 * @see StructuredViewer
 */
public class AlphabeticallyViewSorter extends ViewerSorter {

	private boolean ascending = true;

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.ViewerSorter#compare(org.eclipse.jface.viewers
	 * .Viewer, java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {

		Object o1 = null, o2 = null;
		o1 = getColumnText(e1);
		o2 = getColumnText(e2);

		if (ascending) {
			return super.compare(viewer, o1, o2);
		} else {
			return super.compare(viewer, o2, o1);
		}
	}

	/**
	 * @param e1
	 * @return
	 */
	private Object getColumnText(Object e1) {
		if (e1 instanceof List) {
			GroupPropertyHandle property = null;
			if (((List) e1).size() == 0) {
				return null;
			}
			Object obj = ((List) e1).get(0);
			if (obj instanceof GroupPropertyHandle) {
				property = (GroupPropertyHandle) obj;
			} else if (obj instanceof GroupPropertyHandleWrapper) {
				property = (GroupPropertyHandle) ((GroupPropertyHandleWrapper) obj).getModel();
			}
			if (property == null) {
				return null;
			}
			return property.getPropertyDefn().getGroupName();
		} else if (e1 instanceof GroupPropertyHandle) {
			GroupPropertyHandle property = (GroupPropertyHandle) e1;
			return property.getPropertyDefn().getDisplayName();
		} else if (e1 instanceof GroupPropertyHandleWrapper) {
			GroupPropertyHandle property = (GroupPropertyHandle) ((GroupPropertyHandleWrapper) e1).getModel();
			return property.getPropertyDefn().getDisplayName();
		} else {
			return null;
		}
	}

	/**
	 * Set order of this sort True: Ascending False: Deascending
	 *
	 * @param ascending
	 */
	public void setAscending(boolean ascending) {
		this.ascending = ascending;
	}
}

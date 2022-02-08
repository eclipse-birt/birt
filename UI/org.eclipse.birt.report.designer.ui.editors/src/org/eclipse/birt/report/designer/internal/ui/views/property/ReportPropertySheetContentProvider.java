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

package org.eclipse.birt.report.designer.internal.ui.views.property;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.views.property.GroupPropertyHandleWrapper;
import org.eclipse.birt.report.designer.core.model.views.property.PropertySheetRootElement;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.GroupElementHandle;
import org.eclipse.birt.report.model.api.GroupPropertyHandle;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * Content provider for property sheet table tree view
 * 
 */
public class ReportPropertySheetContentProvider implements ITreeContentProvider {

	private static final String ROOT_DEFAUL_TITLE = Messages.getString("ReportPropertySheetPage.Root.Default.Title"); //$NON-NLS-1$

	public final static int MODE_GROUPED = 0;
	public final static int MODE_ALPHABETIC = 1;
	public final static int MODE_LOCAL_ONLY = 2;

	private int viewMode = MODE_GROUPED;

	public void setViewMode(int mode) {
		this.viewMode = mode;
	}

	public int getViewMode() {
		return this.viewMode;
	}

	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof List) {
			return ((List) parentElement).toArray();
		}
		if (parentElement instanceof PropertySheetRootElement) {
			ArrayList items = new ArrayList();
			GroupElementHandle handle = (GroupElementHandle) ((PropertySheetRootElement) parentElement).getModel();

			if (viewMode == MODE_GROUPED) {
				HashMap map = new HashMap();
				for (Iterator it = handle.visiblePropertyIterator(); it.hasNext();) {
					GroupPropertyHandle property = (GroupPropertyHandle) it.next();
					IElementPropertyDefn defn = property.getPropertyDefn();
					if (defn.getGroupNameKey() == null)
						items.add(new GroupPropertyHandleWrapper(property));
					else {
						List group = (List) map.get(defn.getGroupNameKey());
						if (group == null) {
							group = new ArrayList();
							items.add(group);
							map.put(defn.getGroupNameKey(), group);
						}
						group.add(new GroupPropertyHandleWrapper(property));
					}
				}
			} else if (viewMode == MODE_ALPHABETIC) {
				for (Iterator it = handle.visiblePropertyIterator(); it.hasNext();) {
					GroupPropertyHandle property = (GroupPropertyHandle) it.next();

					items.add(new GroupPropertyHandleWrapper(property));
				}
			} else if (viewMode == MODE_LOCAL_ONLY) {
				for (Iterator it = handle.visiblePropertyIterator(); it.hasNext();) {
					GroupPropertyHandle property = (GroupPropertyHandle) it.next();
					if (property != null && property.getLocalStringValue() != null)
						items.add(new GroupPropertyHandleWrapper(property));
				}
			}
			return items.toArray();
		}
		return null;
	}

	public Object getParent(Object element) {
		return null;
	}

	public boolean hasChildren(Object element) {
		return ((element instanceof List && ((List) element).size() > 0)
				|| element instanceof PropertySheetRootElement);
	}

	public Object[] getElements(Object inputElement) {
		ArrayList items = new ArrayList();

		if (inputElement instanceof GroupElementHandle) {

			PropertySheetRootElement root = new PropertySheetRootElement((GroupElementHandle) inputElement);

			String displayName = null;
			Object element = ((GroupElementHandle) inputElement).getElements().get(0);

			if (element instanceof DesignElementHandle) {
				displayName = ((DesignElementHandle) element).getDefn().getDisplayName();

				if (displayName == null || "".equals(displayName))//$NON-NLS-1$
				{
					displayName = ((DesignElementHandle) element).getDefn().getName();
				}
			}

			if (displayName == null || "".equals(displayName))//$NON-NLS-1$
			{
				displayName = ROOT_DEFAUL_TITLE;
			}
			root.setDisplayName(displayName);

			items.add(root);
		}
		return items.toArray();
	}

	public void dispose() {
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

}

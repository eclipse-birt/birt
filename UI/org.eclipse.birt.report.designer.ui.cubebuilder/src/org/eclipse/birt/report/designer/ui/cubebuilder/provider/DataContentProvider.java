/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

package org.eclipse.birt.report.designer.ui.cubebuilder.provider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.eclipse.birt.report.designer.ui.cubebuilder.util.OlapUtil;
import org.eclipse.birt.report.designer.ui.cubebuilder.util.VirtualField;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.HierarchyHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * Tree viewer content provider adapter for resource browser.
 *
 */

public class DataContentProvider implements ITreeContentProvider {

	static class CustomComparator implements Comparator {

		@Override
		public int compare(Object arg0, Object arg1) {
			String name1 = OlapUtil.getDataFieldDisplayName((ResultSetColumnHandle) arg0);
			String name2 = OlapUtil.getDataFieldDisplayName((ResultSetColumnHandle) arg1);
			if (name1 == null) {
				return -1;
			} else {
				return name1.compareTo(name2);
			}
		}
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof Object[]) {
			return (Object[]) parentElement;
		}
		if (parentElement instanceof DataSetHandle) {
			ResultSetColumnHandle[] children = OlapUtil.getDataFields((DataSetHandle) parentElement);
			Arrays.sort(children, new CustomComparator());
			return children;
		}
		if (parentElement instanceof TabularCubeHandle) {
			List list = new ArrayList();
			DataSetHandle primary = ((TabularCubeHandle) parentElement).getDataSet();
			list.add(primary);
			Object adapter = ElementAdapterManager.getAdapter(((TabularCubeHandle) parentElement).getModuleHandle(),
					List.class);
			if (adapter instanceof List && ((List) adapter).size() > 0) {
				VirtualField sharedDimensions = new VirtualField(VirtualField.TYPE_SHARED_DIMENSIONS);
				sharedDimensions.setModel(adapter);
				list.add(sharedDimensions);
			}
			if (OlapUtil.getAvailableDatasets().length > 1) {
				VirtualField other = new VirtualField(VirtualField.TYPE_OTHER_DATASETS);
				other.setModel(parentElement);
				list.add(other);
			}
			return list.toArray();
		}
		if (parentElement instanceof VirtualField
				&& ((VirtualField) parentElement).getType().equals(VirtualField.TYPE_OTHER_DATASETS)) {
			ArrayList datasets = new ArrayList(Arrays.asList(OlapUtil.getAvailableDatasets()));
			datasets.remove(((TabularCubeHandle) ((VirtualField) parentElement).getModel()).getDataSet());
			return datasets.toArray();
		}
		if (parentElement instanceof VirtualField
				&& ((VirtualField) parentElement).getType().equals(VirtualField.TYPE_SHARED_DIMENSIONS)) {
			return ((List) ((VirtualField) parentElement).getModel()).toArray();
		}
		if (parentElement instanceof DimensionHandle) {
			HierarchyHandle hierarchy = (HierarchyHandle) ((DimensionHandle) parentElement)
					.getContent(DimensionHandle.HIERARCHIES_PROP, 0);
			if (hierarchy.getLevelCount() > 0) {
				return new Object[] { hierarchy.getLevel(0) };
			}
		}
		if (parentElement instanceof LevelHandle) {
			HierarchyHandle hierarchy = (HierarchyHandle) ((LevelHandle) parentElement).getContainer();
			int pos = ((LevelHandle) parentElement).getIndex();
			if (hierarchy.getLevel(pos + 1) != null) {
				return new Object[] { hierarchy.getLevel(pos + 1) };
			}
		}
		return new Object[0];
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object )
	 */
	@Override
	public Object getParent(Object element) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.
	 * Object)
	 */
	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof Object[]) {
			return ((Object[]) element).length > 0;
		}
		if (element instanceof DataSetHandle) {
			return OlapUtil.getDataFields((DataSetHandle) element).length > 0;
		}
		if (element instanceof TabularCubeHandle) {
			if (((TabularCubeHandle) element).getDataSet() != null) {
				return true;
			}
		}
		if (element instanceof VirtualField
				&& ((VirtualField) element).getType().equals(VirtualField.TYPE_OTHER_DATASETS)
				&& OlapUtil.getAvailableDatasets().length > 1) {
			return true;
		}
		if (element instanceof VirtualField
				&& ((VirtualField) element).getType().equals(VirtualField.TYPE_SHARED_DIMENSIONS)) {
			return true;
		}
		if (element instanceof DimensionHandle) {
			HierarchyHandle hierarchy = (HierarchyHandle) ((DimensionHandle) element)
					.getContent(DimensionHandle.HIERARCHIES_PROP, 0);
			if (hierarchy.getLevelCount() > 0) {
				return true;
			}
		}
		if (element instanceof LevelHandle) {
			HierarchyHandle hierarchy = (HierarchyHandle) ((LevelHandle) element).getContainer();
			int pos = ((LevelHandle) element).getIndex();
			return hierarchy.getLevel(pos + 1) != null;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java
	 * .lang.Object)
	 */
	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public void dispose() {

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

}

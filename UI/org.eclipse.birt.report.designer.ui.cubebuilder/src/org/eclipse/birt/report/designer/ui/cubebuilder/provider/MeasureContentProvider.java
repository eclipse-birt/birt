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

import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.MeasureGroupHandle;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class MeasureContentProvider implements ITreeContentProvider {

	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof Object[]) {
			return (Object[]) parentElement;
		}
		if (parentElement instanceof MeasureGroupHandle) {
			return ((MeasureGroupHandle) parentElement).getContents(MeasureGroupHandle.MEASURES_PROP).toArray();
		}
		if (parentElement instanceof CubeHandle) {
			return ((CubeHandle) parentElement).getContents(CubeHandle.MEASURE_GROUPS_PROP).toArray();
		}
		return new Object[0];
	}

	public Object getParent(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean hasChildren(Object element) {
		if (element instanceof Object[]) {
			return ((Object[]) element).length > 0;
		}
		if (element instanceof MeasureGroupHandle) {
			return ((MeasureGroupHandle) element).getContentCount(MeasureGroupHandle.MEASURES_PROP) > 0;
		}
		if (element instanceof CubeHandle) {
			return ((CubeHandle) element).getContentCount(CubeHandle.MEASURE_GROUPS_PROP) > 0;
		}
		return false;
	}

	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub

	}

}

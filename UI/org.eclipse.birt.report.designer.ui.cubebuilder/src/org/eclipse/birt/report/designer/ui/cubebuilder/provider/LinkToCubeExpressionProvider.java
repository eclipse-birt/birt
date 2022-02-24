/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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

import java.util.Arrays;
import java.util.List;

import org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider;
import org.eclipse.birt.report.designer.ui.expressions.ExpressionFilter;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.designer.ui.views.INodeProvider;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.HierarchyHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.MeasureGroupHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;

/**
 * 
 */

public class LinkToCubeExpressionProvider extends ExpressionProvider {

	public LinkToCubeExpressionProvider(DesignElementHandle handle) {
		super(handle);
		addFilterToProvider();
	}

	protected void addFilterToProvider() {
		this.addFilter(new ExpressionFilter() {

			public boolean select(Object parentElement, Object element) {
				if (ExpressionFilter.CATEGORY.equals(parentElement) && ExpressionProvider.DATASETS.equals(element)) {
					return false;
				}
				return true;
			}
		});
	}

	private CubeHandle getCubeHandle(Object input) {
		Object parent = null;
		if (input instanceof LevelHandle) {
			parent = ((LevelHandle) input).getContainer().getContainer().getContainer();
		} else if (input instanceof HierarchyHandle) {
			parent = ((HierarchyHandle) input).getContainer().getContainer();
		} else if (input instanceof DimensionHandle) {
			parent = ((DimensionHandle) input).getContainer();
		} else if (input instanceof MeasureHandle) {
			parent = ((MeasureHandle) input).getContainer().getContainer();
		} else if (input instanceof MeasureGroupHandle) {
			parent = ((MeasureGroupHandle) input).getContainer();
		}
		if (parent instanceof CubeHandle)
			return (CubeHandle) parent;
		return null;
	}

	protected List<Object> getCategoryList() {
		List<Object> list = super.getCategoryList();
		if (!list.contains(CURRENT_CUBE) && getCubeHandle(elementHandle) != null) {
			list.add(CURRENT_CUBE);
		}
		return list;
	}

	protected List<Object> getChildrenList(Object parent) {
		if (CURRENT_CUBE.equals(parent) && getCubeHandle(elementHandle) != null) {
			CubeHandle cube = getCubeHandle(elementHandle);
			Object nodeProviderAdapter = ElementAdapterManager.getAdapter(cube, INodeProvider.class);
			if (nodeProviderAdapter != null) {
				return Arrays.asList(((INodeProvider) nodeProviderAdapter).getChildren(cube));
			}
		}
		return super.getChildrenList(parent);
	}
}

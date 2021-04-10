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

package org.eclipse.birt.report.item.crosstab.internal.ui.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider;
import org.eclipse.birt.report.designer.ui.expressions.ExpressionFilter;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.elements.interfaces.ICubeModel;
import org.eclipse.birt.report.model.elements.interfaces.IHierarchyModel;
import org.eclipse.core.runtime.IAdaptable;

public class CrosstabFilterExpressionProvider extends CrosstabExpressionProvider {

	private boolean isDetail = false;

	public void setDetail(boolean isDetail) {
		this.isDetail = isDetail;
	}

	/**
	 * @param handle
	 */
	public CrosstabFilterExpressionProvider(DesignElementHandle handle) {
		super(handle, null);
	}

	protected void addFilterToProvider() {
		addFilter(new ExpressionFilter() {

			public boolean select(Object parentElement, Object element) {

				if (ExpressionFilter.CATEGORY.equals(parentElement) && ExpressionProvider.MEASURE.equals(element)) {
					return false;
				}

				if ((parentElement instanceof String && ((String) parentElement).equals(CURRENT_CUBE))) {
					PropertyHandle handle = null;
					if (element instanceof PropertyHandle)
						handle = (PropertyHandle) element;
					else if (element instanceof IAdaptable
							&& ((IAdaptable) element).getAdapter(PropertyHandle.class) instanceof PropertyHandle)
						handle = (PropertyHandle) ((IAdaptable) element).getAdapter(PropertyHandle.class);

					if (handle != null && handle.getPropertyDefn().getName().equals(ICubeModel.MEASURE_GROUPS_PROP)) {
						return false;
					}
				}

				if (parentElement instanceof PropertyHandle) {
					PropertyHandle handle = (PropertyHandle) parentElement;
					if (handle.getPropertyDefn().getName().equals(ICubeModel.DIMENSIONS_PROP)) {
						try {
							CrosstabReportItemHandle xtabHandle = getCrosstabReportItemHandle();
							boolean result;
							if (xtabHandle.getDimension(((DimensionHandle) element).getName()) != null)
								result = true;
							else
								result = false;
							if (isDetail)
								result = !result;
							return result;
						} catch (ExtendedElementException e) {
							return false;
						}
					}
				}
				return true;
			}
		});
	}

	protected List getChildrenList(Object parent) {
		if (isDetail) {
			if (parent instanceof DimensionHandle) {
				List children = new ArrayList();
				DimensionHandle handle = (DimensionHandle) parent;
				if (handle.getDefaultHierarchy().getLevelCount() > 0)
					children.addAll(
							handle.getDefaultHierarchy().getPropertyHandle(IHierarchyModel.LEVELS_PROP).getContents());
				return children;
			} else if (parent instanceof LevelHandle) {
				List children = new ArrayList();
				return children;
			}
		}
		return super.getChildrenList(parent);
	}

}

/*******************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
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
import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.data.adapter.impl.CubeMeasureUtil;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider;
import org.eclipse.birt.report.designer.ui.expressions.ExpressionFilter;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.MeasureGroupHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.birt.report.model.elements.interfaces.ICubeModel;

public class CubeMeasureExpressionProvider extends CubeExpressionProvider {

	private MeasureHandle handle = null;
	private DataSetHandle dataSetHandle = null;
	private ExpressionFilter filter = null;

	private boolean isDerivedMeasure;

	public boolean isDerivedMeasure() {
		return isDerivedMeasure;
	}

	public void setDerivedMeasure(boolean isDerivedMeasure) {
		this.isDerivedMeasure = isDerivedMeasure;

		this.clearFilters();
		this.addFilterToProvider(handle);
	}

	public CubeMeasureExpressionProvider(MeasureHandle handle, boolean isDerivedMeasure) {
		super(handle);
		this.isDerivedMeasure = isDerivedMeasure;
		this.handle = handle;
		this.clearFilters();

		if (isDerivedMeasure) {
			dataSetHandle = null;
		} else {
			Object parent = handle.getContainer().getContainer();
			if (parent instanceof TabularCubeHandle) {
				dataSetHandle = ((TabularCubeHandle) parent).getDataSet();
			}
		}

		addFilterToProvider(handle);
	}

	protected void addFilterToProvider(final DesignElementHandle handle) {
		filter = new ExpressionFilter() {

			@Override
			public boolean select(Object parentElement, Object element) {
				if (isDerivedMeasure) // filters DATA_SET
				{
					if (ExpressionFilter.CATEGORY.equals(parentElement)
							&& ExpressionProvider.DATASETS.equals(element)) {
						return false;
					}
				} else {
					if ((ExpressionFilter.CATEGORY.equals(parentElement)
							&& ExpressionProvider.CURRENT_CUBE.equals(element)) || (ExpressionFilter.CATEGORY.equals(parentElement) && ExpressionProvider.MEASURE.equals(element))) {
						return false;
					}
				}
				if (CURRENT_CUBE.equals(parentElement) && element instanceof PropertyHandle) {
					if (((PropertyHandle) element).getPropertyDefn().getName().equals(ICubeModel.MEASURE_GROUPS_PROP)) {
						return true;
					}
					return false;
				}
				if (parentElement instanceof MeasureGroupHandle) {
					if (!isDerivedMeasure() || !(elementHandle instanceof MeasureHandle)) {
						return true;
					}
					CubeHandle cubeHandle = (CubeHandle) ((MeasureGroupHandle) parentElement).getContainer();
					List<MeasureHandle> measureHnadles = new ArrayList<>();
					try {
						measureHnadles = CubeMeasureUtil.getIndependentReferences(cubeHandle, elementHandle.getName());
					} catch (BirtException e) {
						// Do nothing now
						return true;
					}
					if (measureHnadles.contains(element)) {
						return true;
					}
					return false;
				}
				return true;
			}
		};

		this.addFilter(filter);
	}

	@Override
	protected List getCategoryList() {
		List categoryList = super.getCategoryList();

		if (isDerivedMeasure) {
			categoryList.add(CURRENT_CUBE);
		}
		return categoryList;
	}

	@Override
	public Object[] getChildren(Object parent) {
		Object[] children = super.getChildren(parent);
		return children;
	}

}

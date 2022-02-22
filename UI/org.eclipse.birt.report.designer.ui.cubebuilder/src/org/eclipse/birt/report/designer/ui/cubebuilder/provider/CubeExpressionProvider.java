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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.util.DataUtil;
import org.eclipse.birt.report.designer.ui.cubebuilder.util.OlapUtil;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider;
import org.eclipse.birt.report.designer.ui.expressions.ExpressionFilter;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSetParameterHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.MeasureGroupHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.birt.report.model.api.olap.TabularHierarchyHandle;
import org.eclipse.birt.report.model.api.olap.TabularLevelHandle;
import org.eclipse.birt.report.model.api.olap.TabularMeasureHandle;

public class CubeExpressionProvider extends ExpressionProvider {

	private DataSetHandle dataSetHandle = null;

	public CubeExpressionProvider(DesignElementHandle handle) {
		super(handle);
		if (handle instanceof TabularCubeHandle) {
			dataSetHandle = ((TabularCubeHandle) handle).getDataSet();
		}
		if (handle instanceof DimensionHandle) {
			if (((DimensionHandle) handle).getDefaultHierarchy() instanceof TabularHierarchyHandle) {
				dataSetHandle = OlapUtil
						.getHierarchyDataset((TabularHierarchyHandle) ((DimensionHandle) handle).getDefaultHierarchy());
			}
		} else if (handle instanceof TabularHierarchyHandle) {
			dataSetHandle = OlapUtil.getHierarchyDataset((TabularHierarchyHandle) handle);
		} else if (handle instanceof TabularMeasureHandle) {
			Object parent = ((MeasureHandle) handle).getContainer().getContainer();
			if (parent instanceof TabularCubeHandle) {
				dataSetHandle = ((TabularCubeHandle) parent).getDataSet();
			}
		} else if (handle instanceof MeasureGroupHandle) {
			Object parent = ((MeasureGroupHandle) handle).getContainer().getContainer();
			if (parent instanceof TabularCubeHandle) {
				dataSetHandle = ((TabularCubeHandle) parent).getDataSet();
			}
		} else if (handle instanceof TabularLevelHandle) {
			dataSetHandle = OlapUtil.getHierarchyDataset((TabularHierarchyHandle) handle.getContainer());
		}
		addFilterToProvider();
	}

	protected void addFilterToProvider() {
		this.addFilter(new ExpressionFilter() {

			@Override
			public boolean select(Object parentElement, Object element) {
				if ((ExpressionFilter.CATEGORY.equals(parentElement)
						&& ExpressionProvider.CURRENT_CUBE.equals(element)) || (ExpressionFilter.CATEGORY.equals(parentElement) && ExpressionProvider.MEASURE.equals(element))) {
					return false;
				}
				return true;
			}
		});
	}

	@Override
	protected List getCategoryList() {
		List categoryList = super.getCategoryList();
		if (dataSetHandle != null) {
			categoryList.add(DATASETS);
		}
		return categoryList;
	}

	@Override
	protected List getChildrenList(Object parent) {
		if (DATASETS.equals(parent)) {
			List dataSeList = new ArrayList();
			dataSeList.add(dataSetHandle);
			return dataSeList;
		}
		if (parent instanceof DataSetHandle) {
			try {
				List columnList = DataUtil.getColumnList((DataSetHandle) parent);
				List outputList = getOutputList((DataSetHandle) parent);
				columnList.addAll(outputList);
				return columnList;
			} catch (SemanticException e) {
				ExceptionUtil.handle(e);
				return Collections.EMPTY_LIST;
			}
		}
		return super.getChildrenList(parent);
	}

	/**
	 * Get output parameters if handle has.
	 *
	 * @param handle
	 * @return
	 */
	protected List getOutputList(DataSetHandle handle) {
		List outputList = new ArrayList();
		PropertyHandle parameters = handle.getPropertyHandle(DataSetHandle.PARAMETERS_PROP);
		Iterator iter = parameters.iterator();

		if (iter != null) {
			while (iter.hasNext()) {
				Object dataSetParameter = iter.next();
				if (((DataSetParameterHandle) dataSetParameter).isOutput()) {
					outputList.add(dataSetParameter);
				}
			}
		}
		return outputList;
	}

	@Override
	public String getDisplayText(Object element) {
		if (element instanceof DataSetHandle) {
			return ((DataSetHandle) element).getName();
		} else if (element instanceof ResultSetColumnHandle) {
			return ((ResultSetColumnHandle) element).getColumnName();
		} else if (element instanceof DataSetParameterHandle) {
			return ((DataSetParameterHandle) element).getName();
		}
		return super.getDisplayText(element);
	}

	@Override
	public String getInsertText(Object element) {
		if (element instanceof ResultSetColumnHandle || element instanceof DataSetParameterHandle) {
			return DEUtil.getExpression(element);
		}
		return super.getInsertText(element);
	}
}

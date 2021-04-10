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

package org.eclipse.birt.report.designer.ui.dialogs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.util.DataUtil;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.ui.expressions.ExpressionFilter;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSetParameterHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.birt.report.model.api.olap.TabularHierarchyHandle;

/**
 * BindingExpressionProvider
 */
public class BindingExpressionProvider extends ExpressionProvider {

	private DataSetHandle dataSetHandle = null;

	public BindingExpressionProvider(final DesignElementHandle handle,
			final ComputedColumnHandle computedColumnHandle) {
		super(handle);
		if (handle instanceof TabularCubeHandle) {
			dataSetHandle = ((TabularCubeHandle) handle).getDataSet();
		} else if (handle instanceof TabularHierarchyHandle) {
			dataSetHandle = ((TabularHierarchyHandle) handle).getDataSet();
			if (dataSetHandle == null && ((TabularHierarchyHandle) handle).getLevelCount() > 0) {
				dataSetHandle = ((TabularCubeHandle) ((TabularHierarchyHandle) handle).getContainer().getContainer())
						.getDataSet();
			}
		}
		if (handle instanceof ReportItemHandle) {
			dataSetHandle = ((ReportItemHandle) handle).getDataSet();
		} else if (handle instanceof GroupHandle) {
			dataSetHandle = ((ReportItemHandle) ((GroupHandle) handle).getContainer()).getDataSet();
		}

		if (computedColumnHandle != null) {
			addFilter(new ExpressionFilter() {

				public boolean select(Object parentElement, Object element) {
					if (element instanceof ComputedColumnHandle && computedColumnHandle != null) {
						ComputedColumnHandle column = (ComputedColumnHandle) element;
						if (column.getName().equals(computedColumnHandle.getName()))
							return false;
					}
					return true;
				}
			});
		}
	}

	protected DataSetHandle getDataSetHandle() {
		return this.dataSetHandle;
	}

	protected List getCategoryList() {
		List categoryList = super.getCategoryList();
		if (dataSetHandle != null) {
			// 185280
			categoryList.add(0, DATASETS);
		}
		return categoryList;
	}

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
				ExceptionHandler.handle(e);
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
	private List getOutputList(DataSetHandle handle) {
		List outputList = new ArrayList();
		PropertyHandle parameters = handle.getPropertyHandle(DataSetHandle.PARAMETERS_PROP);
		Iterator iter = parameters.iterator();

		if (iter != null) {
			while (iter.hasNext()) {
				Object dataSetParameter = iter.next();
				if (((DataSetParameterHandle) dataSetParameter).isOutput() == true) {
					outputList.add(dataSetParameter);
				}
			}
		}
		return outputList;
	}

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

	public String getInsertText(Object element) {
		if (element instanceof ResultSetColumnHandle || element instanceof DataSetParameterHandle) {
			return DEUtil.getExpression(element);
		}
		return super.getInsertText(element);
	}

}

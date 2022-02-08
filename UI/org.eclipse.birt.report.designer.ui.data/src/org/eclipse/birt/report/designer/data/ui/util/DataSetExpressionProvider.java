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

package org.eclipse.birt.report.designer.data.ui.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.birt.report.designer.data.ui.dataset.DataSetViewData;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;

/**
 * Provider class to provide the available dataset items
 *
 */
public class DataSetExpressionProvider extends ExpressionProvider {
	// data set list
	private List dataSetList;

	/**
	 * provider constructor
	 * 
	 * @param handle
	 */
	public DataSetExpressionProvider(DesignElementHandle handle) {
		super(handle);
		dataSetList = new ArrayList();
		dataSetList.add(handle);
	}

	/*
	 * @see org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider#
	 * getCategoryList()
	 */
	protected List getCategoryList() {
		List categoryList = super.getCategoryList();
		if (dataSetList != null && !dataSetList.isEmpty()) {
			categoryList.add(0, DATASETS);
		}
		return categoryList;
	}

	/*
	 * @see org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider#
	 * getChildrenList(java.lang.Object)
	 */
	protected List getChildrenList(Object parent) {

		if (DATASETS.equals(parent)) {
			return dataSetList;
		}
		if (parent instanceof DataSetHandle) {
			List list = new ArrayList();
			try {
				list.addAll(
						Arrays.asList(DataSetProvider.getCurrentInstance().getColumns((DataSetHandle) parent, false)));
			} catch (Exception e) {
				DataSetExceptionHandler.handle(e);
			}
			return list;
		}
		return super.getChildrenList(parent);
	}

	/*
	 * @see
	 * org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider#getDisplayText
	 * (java.lang.Object)
	 */
	public String getDisplayText(Object element) {
		if (element instanceof DataSetHandle) {
			return ((DataSetHandle) element).getName();
		} else if (element instanceof DataSetViewData) {
			return ((DataSetViewData) element).getName();
		}
		return super.getDisplayText(element);
	}

	/*
	 * @see
	 * org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider#getInsertText(
	 * java.lang.Object)
	 */
	public String getInsertText(Object element) {
		if (element instanceof DataSetViewData) {
			return Utility.getExpression(element);
		}
		return super.getInsertText(element);
	}

}

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

package org.eclipse.birt.report.designer.ui.cubebuilder.dialog;

import java.util.Collections;

import org.eclipse.birt.report.designer.internal.ui.util.DataUtil;
import org.eclipse.birt.report.designer.ui.dialogs.FilterConditionBuilder;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.birt.report.model.api.olap.TabularDimensionHandle;
import org.eclipse.birt.report.model.api.olap.TabularHierarchyHandle;
import org.eclipse.swt.widgets.Shell;

public class DatasetFilterConditionBuilder extends FilterConditionBuilder {

	public DatasetFilterConditionBuilder(String title, String message) {
		super(title, message);
	}

	public DatasetFilterConditionBuilder(Shell parentShell, String title, String message) {
		super(parentShell, title, message);
	}

	protected void setColumnList(DesignElementHandle handle) {
		try {
			DataSetHandle dataset = null;
			if (handle instanceof TabularCubeHandle) {
				dataset = ((TabularCubeHandle) handle).getDataSet();
			} else if (handle instanceof TabularDimensionHandle) {
				TabularHierarchyHandle hierarchy = (TabularHierarchyHandle) ((TabularDimensionHandle) handle)
						.getDefaultHierarchy();
				if (hierarchy != null)
					dataset = hierarchy.getDataSet();
			} else if (handle instanceof TabularHierarchyHandle) {
				TabularHierarchyHandle hierarchy = (TabularHierarchyHandle) handle;
				if (hierarchy != null)
					dataset = hierarchy.getDataSet();
			}
			if (dataset != null)
				columnList = DataUtil.getColumnList(dataset);
			else
				columnList = Collections.EMPTY_LIST;
		} catch (SemanticException e) {
			ExceptionUtil.handle(e);
		}
	}
}

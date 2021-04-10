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
package org.eclipse.birt.report.designer.data.ui.dataset;

import java.util.Map;

import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.jface.wizard.IWizardPage;

public class DataSetBasePageHelper {
	public void addExternalDataSource(Map dataSourceMap, DataSourceHandle handle) {
	}

	public IWizardPage getNextPage(DataSourceHandle dataSourceHandle, DataSetTypeElement dataSetElement) {
		return null;
	}

	public DataSetHandle createDataSet(String dataSetName, String dataSetType) {
		return null;
	}

	public boolean hasWizard(DataSourceHandle selectedDataSource) {
		return false;
	}
}

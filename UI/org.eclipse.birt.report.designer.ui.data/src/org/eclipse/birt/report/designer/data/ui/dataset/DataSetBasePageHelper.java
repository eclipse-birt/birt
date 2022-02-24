/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.views.attributes.providers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.olap.CubeHandle;

public class LinkedDataSetAdapter {

	public List<DataSetHandle> getVisibleLinkedDataSetsDataSetHandles(ModuleHandle handle) {
		ILinkedDataSetHelper helper = (ILinkedDataSetHelper) ElementAdapterManager.getAdapter(this,
				ILinkedDataSetHelper.class);
		if (helper != null) {
			return helper.getVisibleLinkedDataSetsDataSetHandles(handle);
		}
		return new ArrayList<>();
	}

	public List<CubeHandle> getVisibleLinkedDataSetsCubeHandles(ModuleHandle handle) {
		ILinkedDataSetHelper helper = (ILinkedDataSetHelper) ElementAdapterManager.getAdapter(this,
				ILinkedDataSetHelper.class);
		if (helper != null) {
			return helper.getVisibleLinkedDataSetsCubeHandles(handle);
		}
		return new ArrayList<>();
	}

	public List<String> getVisibleLinkedDataSets() {
		ILinkedDataSetHelper helper = (ILinkedDataSetHelper) ElementAdapterManager.getAdapter(this,
				ILinkedDataSetHelper.class);
		if (helper != null) {
			return helper.getVisibleLinkedDataSets();
		}
		return new ArrayList<>();
	}

	public boolean setLinkedDataModel(ReportItemHandle handle, Object value) {

		ILinkedDataSetHelper helper = (ILinkedDataSetHelper) ElementAdapterManager.getAdapter(this,
				ILinkedDataSetHelper.class);
		if (helper != null) {
			return helper.setLinkedDataModel(handle, value);
		}
		return false;
	}

	public Iterator getLinkedDataModelResultSetColumns(String datasetName) {

		ILinkedDataSetHelper helper = (ILinkedDataSetHelper) ElementAdapterManager.getAdapter(this,
				ILinkedDataSetHelper.class);
		if (helper != null) {
			return helper.getResultSetIterator(datasetName);
		}
		return null;
	}

	public Map<String, List<ResultSetColumnHandle>> getGroupedResultSetColumns(String datasetName) {

		ILinkedDataSetHelper helper = (ILinkedDataSetHelper) ElementAdapterManager.getAdapter(this,
				ILinkedDataSetHelper.class);
		if (helper != null) {
			return helper.getGroupedResultSetColumns(datasetName);
		}
		return null;
	}
}

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

package org.eclipse.birt.report.designer.internal.ui.extension;

import org.eclipse.birt.report.data.adapter.api.LinkedDataSetUtil;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.model.api.ReportItemHandle;

public class ExtendedDataModelUIAdapterHelper {

	private static final ExtendedDataModelUIAdapterHelper instance = new ExtendedDataModelUIAdapterHelper();

	private ExtendedDataModelUIAdapterHelper() {
	}

	public static ExtendedDataModelUIAdapterHelper getInstance() {
		return instance;
	}

	public IExtendedDataModelUIAdapter getAdapter() {
		return (IExtendedDataModelUIAdapter) ElementAdapterManager.getAdapter(this, IExtendedDataModelUIAdapter.class);
	}

	public static boolean isBoundToExtendedData(ReportItemHandle reportItemHandle) {
		if (reportItemHandle == null) {
			return false;
		}

		return LinkedDataSetUtil.bindToLinkedDataSet(reportItemHandle);
	}

}

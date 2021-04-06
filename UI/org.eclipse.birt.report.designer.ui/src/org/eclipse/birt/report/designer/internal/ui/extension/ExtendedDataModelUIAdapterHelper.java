
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

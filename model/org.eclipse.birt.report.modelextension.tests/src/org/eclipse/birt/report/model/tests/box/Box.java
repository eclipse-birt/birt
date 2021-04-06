/**
 * 
 */
package org.eclipse.birt.report.model.tests.box;

import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.extension.SimpleRowItem;

class Box extends SimpleRowItem implements org.eclipse.birt.report.model.api.simpleapi.IReportItem {

	/**
	 * 
	 */
	private ReportItemImpl Box;

	public Box(ReportItemImpl reportItemImpl, ExtendedItemHandle handle) {
		super(handle);
		Box = reportItemImpl;
	}

	public String getMethod1() {
		return "box"; //$NON-NLS-1$
	}

	public void setMethod1(int param1, boolean param2) {
	}
}
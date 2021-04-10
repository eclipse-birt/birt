
package org.eclipse.birt.report.designer.internal.ui.util;

import org.eclipse.birt.report.model.api.metadata.ILocalizableInfo;

public class IIndexInfo implements ILocalizableInfo {

	private int idx;

	public int getIndex() {
		return idx;
	}

	public IIndexInfo(int idx) {
		this.idx = idx;
	}

	public String getDisplayName() {
		return null;
	}

	public String getDisplayNameKey() {
		return null;
	}

	public String getName() {
		return null;
	}

	public String getToolTip() {
		return null;
	}

	public String getToolTipKey() {
		return null;
	}

}
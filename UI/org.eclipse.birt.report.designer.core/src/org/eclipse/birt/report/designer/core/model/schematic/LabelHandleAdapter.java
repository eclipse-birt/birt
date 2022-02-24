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

package org.eclipse.birt.report.designer.core.model.schematic;

import org.eclipse.birt.report.designer.core.model.IModelAdapterHelper;
import org.eclipse.birt.report.designer.core.model.ReportItemtHandleAdapter;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DimensionHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.draw2d.geometry.Dimension;

/**
 * Adapter class to adapt model handle. This adapter provides convenience
 * methods to GUI requirement LabelHandleAdapter responds to model LabelHandle
 */
public class LabelHandleAdapter extends ReportItemtHandleAdapter {

	/**
	 * Constructor
	 * 
	 * @param labelHandle The label handle.
	 * @param mark
	 */
	public LabelHandleAdapter(ReportItemHandle labelHandle, IModelAdapterHelper mark) {
		super(labelHandle, mark);
	}

	/**
	 * Gets size of label item.
	 * 
	 * @return the size of label item.
	 */
	public Dimension getSize() {
		DimensionHandle widthHandle = ((ReportItemHandle) getHandle()).getWidth();

		int px = 0;
		int py = 0;

		// percentage unit is handled in layout, here always return 0;

		if (!DesignChoiceConstants.UNITS_PERCENTAGE.equals(widthHandle.getUnits())) {
			px = (int) DEUtil.convertoToPixel(widthHandle);
		}

		DimensionHandle heightHandle = ((ReportItemHandle) getHandle()).getHeight();

		if (!DesignChoiceConstants.UNITS_PERCENTAGE.equals(heightHandle.getUnits())) {
			py = (int) DEUtil.convertoToPixel(heightHandle);
		}

		px = Math.max(0, px);
		py = Math.max(0, py);

		if (DEUtil.isFixLayout(getHandle())) {
			if (px == 0 && widthHandle.isSet()) {
				px = 1;
			}
			if (py == 0 && heightHandle.isSet()) {
				py = 1;
			}
		}
		return new Dimension(px, py);
	}
}

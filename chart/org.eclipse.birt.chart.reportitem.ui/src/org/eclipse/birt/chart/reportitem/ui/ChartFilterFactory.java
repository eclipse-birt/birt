/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
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

package org.eclipse.birt.chart.reportitem.ui;

import org.eclipse.birt.chart.reportitem.api.ChartItemUtil;
import org.eclipse.birt.chart.reportitem.ui.dialogs.ChartCubeFilterConditionBuilder;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.swt.widgets.Shell;

/**
 * This factory class is responsible to create different filter interfaces for
 * chart against different data dependence.
 * 
 * @since 2.5.3
 */

public class ChartFilterFactory {
	/**
	 * Create an instance of cube filter condition builder dialog.
	 * 
	 * @param parentShell
	 * @param title
	 * @param message
	 * @return
	 */
	public ChartCubeFilterConditionBuilder createCubeFilterConditionBuilder(Shell parentShell, String title,
			String message) {
		return new ChartCubeFilterConditionBuilder(parentShell, title, message);
	}

	/**
	 * Check if specified element handle contains chart model.
	 * 
	 * @param handle
	 * @return
	 */
	public boolean isChartHandle(DesignElementHandle handle) {
		return ChartItemUtil.isChartHandle(handle);
	}
}

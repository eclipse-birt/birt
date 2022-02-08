/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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

package org.eclipse.birt.chart.reportitem.api;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.extension.IReportItem;

/**
 * 
 */

public interface IChartReportItem extends IReportItem {

	/**
	 * Sets the new chart through a command for command stack integration.
	 * 
	 * @param eih      handle
	 * @param oldChart old model
	 * @param newChart new model
	 */
	public void executeSetModelCommand(ExtendedItemHandle eih, Chart oldChart, Chart newChart);

	/**
	 * Set the chart directly without command.
	 * 
	 * @param chart chart model
	 */
	public void setModel(Chart chart);
}

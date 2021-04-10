/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

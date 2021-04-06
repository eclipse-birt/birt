/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.script.api;

import org.eclipse.birt.chart.script.api.component.IAxis;
import org.eclipse.birt.chart.script.api.component.IValueSeries;

/**
 * Represents the design of a ChartWithAxis in the scripting environment
 */
public interface IChartWithAxes extends IChart {

	/**
	 * Gets category(X) axis from Chart
	 * 
	 * @return category axis
	 */
	IAxis getCategoryAxis();

	/**
	 * Gets all value(Y) axes from Chart
	 * 
	 * @return value axes array
	 */
	IAxis[] getValueAxes();

	/**
	 * Checks if Chart is transposed, i.e. value(Y) axis is displayed as horizontal
	 * line.
	 * 
	 * @return true horizontal, false vertical
	 */
	boolean isHorizontal();

	/**
	 * Sets Chart to be transposed, i.e. value(Y) axis is displayed as horizontal
	 * line.
	 * 
	 * @param horizontal true horizontal, false vertical
	 */
	void setHorizontal(boolean horizontal);

	/**
	 * Gets all value(Y) series from Chart.
	 * 
	 * @return series array
	 */
	IValueSeries[][] getValueSeries();
}

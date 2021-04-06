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

import org.eclipse.birt.chart.script.api.component.IValueSeries;

/**
 * Represents the design of a Chart in the scripting environment
 */

public interface IChartWithoutAxes extends IChart {

	/**
	 * Gets all value(Y) series from Chart
	 * 
	 * @return series array
	 */
	IValueSeries[] getValueSeries();
}

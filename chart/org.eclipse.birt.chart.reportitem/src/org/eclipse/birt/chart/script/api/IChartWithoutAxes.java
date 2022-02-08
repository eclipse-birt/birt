/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
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

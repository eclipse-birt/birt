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

package org.eclipse.birt.chart.script.api.component;

/**
 * Represents the Legend of a Chart in the scripting environment
 */

public interface ILegend extends IChartComponent {

	/**
	 * Checks if series value could be shown in the Legend. Note that if chart is
	 * colored by category, this option is useless.
	 * 
	 * @see org.eclipse.birt.chart.script.api.IChart#isColorByCategory()
	 * @see org.eclipse.birt.chart.script.api.IChart#setColorByCategory(boolean)
	 * @return show value or not
	 */
	boolean isShowValue();

	/**
	 * Sets the option if series value could be shown in the Legend. Note that if
	 * chart is colored by category, this option is useless.
	 * 
	 * @see org.eclipse.birt.chart.script.api.IChart#isColorByCategory()
	 * @see org.eclipse.birt.chart.script.api.IChart#setColorByCategory(boolean)
	 * @param show show value or not
	 */
	void setShowValue(boolean show);
}

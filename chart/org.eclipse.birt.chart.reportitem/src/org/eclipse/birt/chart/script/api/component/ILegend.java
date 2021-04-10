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

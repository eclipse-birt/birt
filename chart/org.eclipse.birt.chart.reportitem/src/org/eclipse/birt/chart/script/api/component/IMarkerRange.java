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

import org.eclipse.birt.chart.script.api.data.IDataElement;

/**
 * Represents the Marker Range of Axis in the scripting environment
 */

public interface IMarkerRange extends IChartComponent {

	/**
	 * Gets the start value for defining where the range will start
	 * 
	 * @return value start value
	 */
	IDataElement getStartValue();

	/**
	 * Sets the start value for defining where the range will start
	 * 
	 * @param start value
	 */
	void setStartValue(IDataElement value);

	/**
	 * Gets the end value for defining where the range will end
	 * 
	 * @return value end value
	 */
	IDataElement getEndValue();

	/**
	 * Sets the end value for defining where the range will end
	 * 
	 * @param end value
	 */
	void setEndValue(IDataElement value);
}

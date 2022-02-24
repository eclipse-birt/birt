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

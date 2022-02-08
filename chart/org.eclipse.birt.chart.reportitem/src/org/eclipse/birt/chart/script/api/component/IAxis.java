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

import org.eclipse.birt.chart.script.api.scale.IScale;

/**
 * Represents the Axis of a Chart in the scripting environment
 */

public interface IAxis extends IChartComponent {

	/**
	 * Gets theScale for axis scaling
	 * 
	 * @return Scale
	 */
	IScale getScale();

	/**
	 * Gets all marker lines in Axis
	 * 
	 * @return an array of marker lines
	 */
	IMarkerLine[] getMarkerLines();

	/**
	 * Gets all marker ranges in Axis
	 * 
	 * @return an array of marker ranges
	 */
	IMarkerRange[] getMarkerRanges();

	/**
	 * Gets the name of AxisType. Return values are an enumeration including "Text",
	 * "Linear", "Logarithmic" and "DateTime". Default value is "Linear".
	 * 
	 * @return the name of AxisType
	 * @see org.eclipse.birt.chart.model.attribute.AxisType
	 */
	String getType();

	/**
	 * Sets AxisType by name. Names are an enumeration including "Text", "Linear",
	 * "Logarithmic" and "DateTime". Default value is "Linear". If the AxisType name
	 * is invalid, will set the default value.
	 * 
	 * @param type the name of AxisType
	 * @see org.eclipse.birt.chart.model.attribute.AxisType
	 */
	void setType(String type);

}

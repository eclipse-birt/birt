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

package org.eclipse.birt.chart.script.api.scale;

import java.util.Date;

/**
 * Represents the Scale for chart scaling when Axis is Datetime type.
 */

public interface ITimeScale extends IScale {

	/**
	 * Gets the step size for scaling.
	 * 
	 * @return step size
	 */
	int getStepSize();

	/**
	 * Sets the step size for scaling.
	 * 
	 * @param size step size
	 */
	void setStepSize(int size);

	/**
	 * Gets the name of ScaleUnitType for time scaling
	 * 
	 * @return the name of ScaleUnitType
	 */
	String getStepTimeUnit();

	/**
	 * Sets the name of ScaleUnitType for time scaling
	 * 
	 * @param the name of ScaleUnitType
	 */
	void setStepTimeUnit(String unit);

	/**
	 * Gets the minimum value that appears in Axis
	 * 
	 * @return minimum value
	 */
	Date getMin();

	/**
	 * Gets the maximum value that appears in Axis
	 * 
	 * @return maximum value
	 */
	Date getMax();

	/**
	 * Sets the minimum value that appears in Axis
	 * 
	 * @param min minimum value
	 */
	void setMin(Date min);

	/**
	 * Sets the maximum value that appears in Axis
	 * 
	 * @param max maximum value
	 */
	void setMax(Date max);
}

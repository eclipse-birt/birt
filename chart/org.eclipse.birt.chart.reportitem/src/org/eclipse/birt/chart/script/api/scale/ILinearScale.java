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

/**
 * Represents the Scale for chart scaling when Axis is Linear type.
 */

public interface ILinearScale extends IScale {

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
	 * Gets the number of steps for scaling
	 * 
	 * @return number of steps
	 */
	int getNumberOfSteps();

	/**
	 * Sets the number of steps for scaling
	 * 
	 * @param steps number of steps
	 */
	void setNumberOfSteps(int steps);

	/**
	 * Gets the minimum value that appears in Axis
	 * 
	 * @return minimum value
	 */
	double getMin();

	/**
	 * Gets the maximum value that appears in Axis
	 * 
	 * @return maximum value
	 */
	double getMax();

	/**
	 * Sets the minimum value that appears in Axis
	 * 
	 * @param min minimum value
	 */
	void setMin(double min);

	/**
	 * Sets the maximum value that appears in Axis
	 * 
	 * @param max maximum value
	 */
	void setMax(double max);
}

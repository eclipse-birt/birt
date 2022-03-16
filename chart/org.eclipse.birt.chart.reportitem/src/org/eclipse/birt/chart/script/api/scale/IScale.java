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
 * Represents the Scale for chart scaling in the scripting environment
 */

public interface IScale {

	/**
	 * Checks if Chart will be scaled automatically
	 *
	 * @return auto scale or not
	 */
	boolean isAuto();

	/**
	 * Sets if Chart will be scaled automatically, i.e. unset step size and number
	 * of steps
	 */
	void setAuto();

	/**
	 * Gets if scale is by category, meanwhile all scale attributes are invalid
	 *
	 * @return by category or not
	 */
	boolean isCategory();

	/**
	 * Sets if scale is by category, meanwhile all scale attributes are invalid
	 *
	 * @param category category or not
	 */
	void setCategory(boolean category);
}

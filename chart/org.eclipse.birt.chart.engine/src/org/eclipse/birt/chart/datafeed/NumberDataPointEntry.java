/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
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

package org.eclipse.birt.chart.datafeed;

/**
 * The class defines abstract methods to access number values from number data
 * set.
 *
 * @since 2.6
 */

public abstract class NumberDataPointEntry implements IDataPointEntry {
	/**
	 * Sets number data into data set.
	 *
	 * @param data
	 */
	abstract public void setNumberData(Number[] data);

	/**
	 * Gets number data from data set.
	 *
	 * @return
	 */
	abstract public Number[] getNumberData();
}

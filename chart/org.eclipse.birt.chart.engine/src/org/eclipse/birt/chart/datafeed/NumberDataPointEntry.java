/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

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

package org.eclipse.birt.chart.datafeed;

/**
 * A datapoint definition is responsible for defining the data types of a
 * datapoint entry and the display text.
 */

public interface IDataPointDefinition {

	/**
	 * Creates and returns a data types array.
	 * 
	 * @return data types
	 */
	String[] getDataPointTypes();

	/**
	 * Returns the externalized text for display
	 * 
	 * @param type data type
	 * @return display text
	 */
	String getDisplayText(String type);

	/**
	 * Check if data type of specified component type is any.
	 * 
	 * @param type component type
	 * @return the compatible data types, it's a combination of
	 *         (IConstants.NUMERICAL, IConstants.TEXT and IConstants.DATE_TIME).
	 */
	int getCompatibleDataType(String type);
}

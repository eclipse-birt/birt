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

package org.eclipse.birt.chart.script.api.data;

/**
 * Represents the grouping for series in the scripting environment.
 * 
 * @see org.eclipse.birt.chart.model.data.SeriesGrouping
 */

public interface ISeriesGrouping {

	/**
	 * Checks if grouping is enabled
	 * 
	 * @return enable state
	 */
	boolean isEnabled();

	/**
	 * Sets if grouping is enabled
	 * 
	 * @param value enable state
	 */
	void setEnabled(boolean value);

	/**
	 * Gets the interval value between two groups.
	 * 
	 * @return group interval
	 */
	double getGroupInterval();

	/**
	 * Sets the interval value between two groups.
	 * 
	 * @param value group interval
	 */
	void setGroupInterval(double value);

	/**
	 * Gets the type of group. Return values are an enumeration including "Numeric",
	 * "DateTime" and "Text". Default value is "Numeric".
	 * 
	 * @return group type
	 * @see org.eclipse.birt.chart.model.attribute.DataType
	 */
	String getGroupType();

	/**
	 * Sets the type of group. Group types are an enumeration including "Numeric",
	 * "DateTime" and "Text". Default value is "Numeric". If group types are
	 * invalid, will set the default value.
	 * 
	 * @param type group type
	 * @see org.eclipse.birt.chart.model.attribute.DataType
	 */
	void setGroupType(String type);

	/**
	 * Gets the unit of group. Return values are an enumeration including "Seconds",
	 * "Minutes", "Hours", "Days", "Weeks", "Months" and "Years". Default value is
	 * "Seconds".
	 * 
	 * @return group unit
	 * @see org.eclipse.birt.chart.model.attribute.GroupingUnitType
	 */
	String getGroupUnit();

	/**
	 * Sets the unit of group. Group units are an enumeration including "Seconds",
	 * "Minutes", "Hours", "Days", "Weeks", "Months" and "Years". Default value is
	 * "Seconds". If group units are invalid, will set the default value.
	 * 
	 * @param unit group unit
	 * @see org.eclipse.birt.chart.model.attribute.GroupingUnitType
	 */
	void setGroupUnit(String unit);
}

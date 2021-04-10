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

package org.eclipse.birt.chart.script.api;

import org.eclipse.birt.chart.script.api.data.IDateTimeDataElement;
import org.eclipse.birt.chart.script.api.data.INumberDataElement;

import com.ibm.icu.util.Calendar;

/**
 * Provides methods for creating simple API classes
 */

public interface IComponentFactory {

	/**
	 * Creates numeric data element
	 * 
	 * @param value double value
	 * @return numeric data element
	 */
	INumberDataElement createNumberElement(double value);

	/**
	 * Creates datetime data element
	 * 
	 * @param date long Date
	 * @return datetime data element
	 */
	IDateTimeDataElement createDateTimeElement(long date);

	/**
	 * Creates datetime data element
	 * 
	 * @param date calendar
	 * @return datetime data element
	 */
	IDateTimeDataElement createDateTimeElement(Calendar date);
}

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

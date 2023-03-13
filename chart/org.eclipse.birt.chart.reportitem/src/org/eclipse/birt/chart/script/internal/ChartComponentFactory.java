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

package org.eclipse.birt.chart.script.internal;

import org.eclipse.birt.chart.script.api.IComponentFactory;
import org.eclipse.birt.chart.script.api.data.IDateTimeDataElement;
import org.eclipse.birt.chart.script.api.data.INumberDataElement;
import org.eclipse.birt.chart.script.internal.data.DateTimeElementImpl;
import org.eclipse.birt.chart.script.internal.data.NumberElementImpl;

import com.ibm.icu.util.Calendar;

/**
 * Provides methods for creating simple API classes
 */

public class ChartComponentFactory implements IComponentFactory {

	ChartComponentFactory() {

	}

	/**
	 * Creates number data element
	 *
	 * @param value double value
	 * @return number data element
	 */
	@Override
	public INumberDataElement createNumberElement(double value) {
		return new NumberElementImpl(value);
	}

	/**
	 * Creates datetime data element
	 *
	 * @param date long Date
	 * @return datetime data element
	 */
	@Override
	public IDateTimeDataElement createDateTimeElement(long date) {
		return new DateTimeElementImpl(date);
	}

	/**
	 * Creates datetime data element
	 *
	 * @param date calendar
	 * @return datetime data element
	 */
	@Override
	public IDateTimeDataElement createDateTimeElement(Calendar date) {
		if (date == null) {
			return null;
		}
		return new DateTimeElementImpl(date.getTimeInMillis());
	}
}

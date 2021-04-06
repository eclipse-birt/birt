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
	public INumberDataElement createNumberElement(double value) {
		return new NumberElementImpl(value);
	}

	/**
	 * Creates datetime data element
	 * 
	 * @param date long Date
	 * @return datetime data element
	 */
	public IDateTimeDataElement createDateTimeElement(long date) {
		return new DateTimeElementImpl(date);
	}

	/**
	 * Creates datetime data element
	 * 
	 * @param date calendar
	 * @return datetime data element
	 */
	public IDateTimeDataElement createDateTimeElement(Calendar date) {
		if (date == null) {
			return null;
		}
		return new DateTimeElementImpl(date.getTimeInMillis());
	}
}

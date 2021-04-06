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

package org.eclipse.birt.chart.script.internal.data;

import org.eclipse.birt.chart.model.data.DateTimeDataElement;
import org.eclipse.birt.chart.script.api.data.IDateTimeDataElement;

import com.ibm.icu.util.Calendar;

/**
 * 
 */

public class DateTimeElementImpl implements IDateTimeDataElement {

	private long data;

	public DateTimeElementImpl(DateTimeDataElement data) {
		this.data = data.getValue();
	}

	public DateTimeElementImpl(long data) {
		this.data = data;
	}

	public long getValue() {
		return data;
	}

	public Calendar getValueAsCalendar() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(data);
		return calendar;
	}

	public void setValue(long value) {
		data = value;
	}

}

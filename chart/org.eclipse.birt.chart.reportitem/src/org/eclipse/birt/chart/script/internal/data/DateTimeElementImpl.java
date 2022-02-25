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

	@Override
	public long getValue() {
		return data;
	}

	@Override
	public Calendar getValueAsCalendar() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(data);
		return calendar;
	}

	@Override
	public void setValue(long value) {
		data = value;
	}

}

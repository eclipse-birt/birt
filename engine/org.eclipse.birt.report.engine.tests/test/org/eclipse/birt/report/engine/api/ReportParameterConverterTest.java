/*******************************************************************************
* Copyright (c) 2007 Actuate Corporation.
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

package org.eclipse.birt.report.engine.api;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.ibm.icu.util.ULocale;

import junit.framework.TestCase;

public class ReportParameterConverterTest extends TestCase {

	public void testDate() {
		Calendar dateCal = Calendar.getInstance(Locale.US);
		dateCal.clear();
		dateCal.set(1998, 8, 13, 20, 1, 44);
		java.util.Date dateTime = dateCal.getTime();
		java.sql.Date date = new java.sql.Date(dateTime.getTime());
		java.sql.Time time = new java.sql.Time(dateTime.getTime());

		ReportParameterConverter converter = new ReportParameterConverter("i", ULocale.US);
		String strDateTime = converter.format(dateTime);
		String strTime = converter.format(time);
		String strDate = converter.format(date);

		assertEquals("9/13/1998, 8:01:44 PM", strDateTime.replace("\u202f", " "));
		assertEquals("9/13/1998", strDate);
		assertEquals("8:01:44 PM", strTime.replace("\u202f", " "));

		Date newDateTime = (java.util.Date) converter.parse(strDateTime, IScalarParameterDefn.TYPE_DATE_TIME);
		java.sql.Date newDate = (java.sql.Date) converter.parse(strDate, IScalarParameterDefn.TYPE_DATE);
		java.sql.Time newTime = (java.sql.Time) converter.parse(strTime, IScalarParameterDefn.TYPE_TIME);

		assertEquals(strDateTime, converter.format(newDateTime));
		assertEquals(strDate, converter.format(newDate));
		assertEquals(strTime, converter.format(newTime));
	}
}

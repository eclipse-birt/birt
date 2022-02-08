/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.data.engine.olap.data.impl.aggregation.function;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.data.engine.api.timefunction.IPeriodsFunction;
import org.eclipse.birt.data.engine.api.timefunction.TimeMember;

import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.GregorianCalendar;

public class QuarterToDateFunction extends AbstractMDX implements IPeriodsFunction {

	public List<TimeMember> getResult(TimeMember member) {
		String[] levels = member.getLevelType();
		int[] values = member.getMemberValue();

		Calendar cal = new GregorianCalendar(TimeMemberUtil.getTimeZone(), TimeMemberUtil.getDefaultLocale());
		cal.clear();
		String calculateUnit = this.translateToCal(cal, levels, values);
		if (isCurrent) {
			int quarter = cal.get(Calendar.MONTH) / 3 + 1;
			while (true) {
				if (cal.get(Calendar.MONTH) / 3 + 1 != quarter) {
					cal.add(Calendar.DAY_OF_YEAR, -1);
					break;
				}

				cal.add(Calendar.DAY_OF_YEAR, 1);
			}
		}

		List<TimeMember> list = new ArrayList<TimeMember>();

		if (calculateUnit.equals(QUARTER)) {
			TimeMember newMember = new TimeMember(values, levels);
			list.add(newMember);
		} else if (calculateUnit.equals(MONTH)) {
			int month = cal.get(Calendar.MONTH) + 1;
			int quarter = cal.get(Calendar.MONTH) / 3 + 1;
			int startMonth = quarter * 3 - 2;
			TimeMember newMember = null;
			for (int i = startMonth; i <= month; i++) {
				cal.set(Calendar.DAY_OF_MONTH, 1);
				cal.set(Calendar.MONTH, i - 1);
				int[] newValues = getValueFromCal(cal, levels);
				newMember = new TimeMember(newValues, levels);
				list.add(newMember);
			}

		} else if (calculateUnit.equals(WEEK)) {
			retrieveWeek(list, cal, levels, "quarterToDate");
		} else if (calculateUnit.equals(DAY)) {
			int dayOfYear = cal.get(Calendar.DAY_OF_YEAR);
			int quarter = cal.get(Calendar.MONTH) / 3 + 1;
			int startMonth = quarter * 3 - 2;
			Calendar startCal = (Calendar) cal.clone();
			startCal.set(Calendar.MONTH, startMonth - 1);
			startCal.set(Calendar.DAY_OF_MONTH, 1);
			int startDay = startCal.get(Calendar.DAY_OF_YEAR);
			TimeMember newMember = null;
			for (int i = startDay; i <= dayOfYear; i++) {
				cal.set(Calendar.DAY_OF_YEAR, i);
				int[] newValues = getValueFromCal(cal, levels);
				newMember = new TimeMember(newValues, levels);
				list.add(newMember);
			}
		}

		return list;
	}
}

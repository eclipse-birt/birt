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

/**
 * Use the WTD function to return a set of members of the Time hierarchy from
 * the same week, up to and including a particular member.
 * 
 * 
 */
public class WeekToDateFunciton extends AbstractMDX implements IPeriodsFunction {

	private final long dayTimeInMills = 24 * 3600 * 1000;

	/*
	 * @see org.eclipse.birt.data.engine.olap.data.impl.aggregation.function.
	 * IPeriodsFunction#getResult(org.eclipse.birt.data.engine.olap.data.impl.
	 * aggregation.function.TimeMember)
	 */
	public List<TimeMember> getResult(TimeMember member) {
		List timeMembers = new ArrayList<TimeMember>();
		String[] levelTypes = member.getLevelType();
		int[] values = member.getMemberValue();

		Calendar cal = new GregorianCalendar(TimeMemberUtil.getTimeZone(), TimeMemberUtil.getDefaultLocale());
		cal.clear();
		String baseType = translateToCal(cal, levelTypes, values);

		if (isCurrent) {
			int weekDay = cal.get(Calendar.DAY_OF_WEEK);
			while (weekDay < 7) {
				cal.add(Calendar.DAY_OF_YEAR, 1);
				weekDay = cal.get(Calendar.DAY_OF_WEEK);
			}
		}

		if (baseType.equals(WEEK)) {
			timeMembers.add(member);
		} else if (baseType.equals(DAY)) {
			int weekday = cal.get(Calendar.DAY_OF_WEEK);

			int[] tmp;
			Calendar newCal = new GregorianCalendar(TimeMemberUtil.getTimeZone(), TimeMemberUtil.getDefaultLocale());
			for (int i = 1; i <= weekday; i++) {
				newCal.setTimeInMillis(cal.getTimeInMillis() - (weekday - i) * dayTimeInMills);
				tmp = getValueFromCal(newCal, levelTypes);
				TimeMember timeMember = new TimeMember(tmp, levelTypes);
				timeMembers.add(timeMember);
			}

		}

		return timeMembers;
	}

}

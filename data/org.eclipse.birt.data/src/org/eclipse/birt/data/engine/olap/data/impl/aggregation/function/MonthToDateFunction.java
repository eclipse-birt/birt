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
 * 
 * Use the MTD function to return a set of members of the Time hierarchy from
 * the same month, up to and including a particular member. For example, you
 * might use the function to return the set of days in 2007.8 up to and
 * including 2007.8.21.
 * 
 * 
 */
public class MonthToDateFunction extends AbstractMDX implements IPeriodsFunction {

	/*
	 * (non-Javadoc)
	 * 
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
			int month = cal.get(Calendar.MONTH);
			while (true) {
				if (cal.get(Calendar.MONTH) != month) {
					cal.add(Calendar.DAY_OF_YEAR, -1);
					break;
				}
				cal.add(Calendar.DAY_OF_YEAR, 1);
			}

		}
		int[] tmp;
		if (baseType.equals(MONTH)) {
			timeMembers.add(member);
		} else if (baseType.equals(WEEK)) {
			retrieveWeek(timeMembers, cal, levelTypes, "monthToDate");
		} else if (baseType.equals(DAY)) {
			int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
			for (int i = 1; i <= dayOfMonth; i++) {
				cal.set(Calendar.DAY_OF_MONTH, i);
				tmp = getValueFromCal(cal, levelTypes);
				TimeMember timeMember = new TimeMember(tmp, levelTypes);
				timeMembers.add(timeMember);
			}
		}

		return timeMembers;
	}

}

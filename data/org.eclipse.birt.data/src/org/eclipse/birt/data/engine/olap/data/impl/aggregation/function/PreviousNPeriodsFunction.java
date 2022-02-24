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

import org.eclipse.birt.data.engine.api.timefunction.IParallelPeriod;
import org.eclipse.birt.data.engine.api.timefunction.TimeMember;

import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.GregorianCalendar;

public class PreviousNPeriodsFunction extends AbstractMDX implements IParallelPeriod {
	private String levelName;
	private int offset;

	public PreviousNPeriodsFunction(String levelName, int offset) {
		this.levelName = levelName;
		this.offset = offset;
	}

	public String getLevelName() {
		return levelName;
	}

	public int getOffset() {
		return offset;
	}

	@Override
	public TimeMember getResult(TimeMember member) {
		String[] levels = member.getLevelType();
		int[] values = member.getMemberValue();

		Calendar cal = new GregorianCalendar(TimeMemberUtil.getTimeZone(), TimeMemberUtil.getDefaultLocale());
		cal.clear();
		translateToCal(cal, levels, values);

		if (levelName.equals(TimeMember.TIME_LEVEL_TYPE_YEAR)) {
			cal.add(Calendar.YEAR, offset);
		} else if (levelName.equals(TimeMember.TIME_LEVEL_TYPE_QUARTER)) {
			cal.add(Calendar.MONTH, offset * 3);
		} else if (levelName.equals(TimeMember.TIME_LEVEL_TYPE_MONTH)) {
			cal.add(Calendar.MONTH, offset);
		} else if (levelName.equals(TimeMember.TIME_LEVEL_TYPE_WEEK_OF_MONTH)) {
			cal.add(Calendar.WEEK_OF_MONTH, offset);
		} else if (levelName.equals(TimeMember.TIME_LEVEL_TYPE_WEEK_OF_YEAR)) {
			cal.add(Calendar.WEEK_OF_YEAR, offset);
		} else if (levelName.equals(TimeMember.TIME_LEVEL_TYPE_DAY_OF_MONTH)) {
			cal.add(Calendar.DAY_OF_MONTH, offset);
		} else if (levelName.equals(TimeMember.TIME_LEVEL_TYPE_DAY_OF_YEAR)) {
			cal.add(Calendar.DAY_OF_YEAR, offset);
		} else if (levelName.equals(TimeMember.TIME_LEVEL_TYPE_DAY_OF_WEEK)) {
			cal.add(Calendar.DAY_OF_WEEK, offset);
		}

		int[] newValues = getValueFromCal(cal, levels);

		TimeMember newMember = new TimeMember(newValues, levels);
		return newMember;
	}
}

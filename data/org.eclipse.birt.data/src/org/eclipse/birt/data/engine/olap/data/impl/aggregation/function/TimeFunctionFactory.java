/*
 *************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
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
 *  
 *************************************************************************
 */
package org.eclipse.birt.data.engine.olap.data.impl.aggregation.function;

import org.eclipse.birt.data.engine.api.timefunction.IParallelPeriod;
import org.eclipse.birt.data.engine.api.timefunction.IPeriodsFunction;
import org.eclipse.birt.data.engine.api.timefunction.TimeMember;

public class TimeFunctionFactory {
	public static IPeriodsFunction createPeriodsToDateFunction(String levelType, boolean isCurrent) {
		IPeriodsFunction function = null;
		if (levelType.equals(TimeMember.TIME_LEVEL_TYPE_YEAR))
			function = new YearToDateFunction();
		else if (levelType.equals(TimeMember.TIME_LEVEL_TYPE_QUARTER))
			function = new QuarterToDateFunction();
		else if (levelType.equals(TimeMember.TIME_LEVEL_TYPE_MONTH))
			function = new MonthToDateFunction();
		else if (levelType.equals(TimeMember.TIME_LEVEL_TYPE_WEEK_OF_MONTH))
			function = new WeekToDateFunciton();
		else if (levelType.equals(TimeMember.TIME_LEVEL_TYPE_WEEK_OF_YEAR))
			function = new WeekToDateFunciton();
		if (isCurrent)
			((AbstractMDX) function).setIsCurrent(isCurrent);
		return function;
	}

	public static IPeriodsFunction createTrailingFunction(String levelType, int Offset) {
		return new TrailingFunction(levelType, Offset);
	}

	public static IParallelPeriod createParallelPeriodFunction(String levelType, int Offset) {
		return new PreviousNPeriodsFunction(levelType, Offset);
	}
}

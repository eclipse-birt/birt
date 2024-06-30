/*******************************************************************************
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
 *******************************************************************************/
package org.eclipse.birt.report.data.adapter.api.timeFunction;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.data.adapter.api.ArgumentInfo;
import org.eclipse.birt.report.data.adapter.api.timeFunction.IArgumentInfo.Period_Type;
import org.eclipse.birt.report.data.adapter.i18n.Message;
import org.eclipse.birt.report.data.adapter.i18n.ResourceConstants;

public class BaseTimeFunction implements ITimeFunction {

	private String name, displayName, description;
	private IArgumentInfo period1, period2;
	private List<IArgumentInfo.Period_Type> period_type1, period_type2;

	public BaseTimeFunction(String functionName, String displayName, String description) {
		this.name = functionName;
		this.displayName = displayName;
		this.description = description;
	}

	public BaseTimeFunction(ITimeFunction function, List<IArgumentInfo.Period_Type> timeType) {
		this(function.getName(), function.getDisplayName(), function.getDescription());
		period_type1 = new ArrayList<>();
		period_type2 = new ArrayList<>();
		period_type1.addAll(timeType);
		period_type2.addAll(timeType);
	}

	public BaseTimeFunction(ITimeFunction function, List<IArgumentInfo.Period_Type> timeType1,
			List<IArgumentInfo.Period_Type> timeType2) {
		this(function.getName(), function.getDisplayName(), function.getDescription());
		period_type1 = new ArrayList<>();
		period_type2 = new ArrayList<>();
		period_type1.addAll(timeType1);
		period_type2.addAll(timeType2);
	}

	/**
	 * Get time function name
	 *
	 * @return the time function name
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * Get display name for time function
	 */
	@Override
	public String getDisplayName() {
		return this.displayName;
	}

	/**
	 * Get the description for time function
	 *
	 * @return the description for time function
	 */
	@Override
	public String getDescription() {
		return this.description;
	}

	/**
	 * Get the argument info list if the function has
	 *
	 * @return the corresponding argument info in time functions.
	 */
	@Override
	public List<IArgumentInfo> getArguments() {
		List<IArgumentInfo> arguments = new ArrayList<>();
		period1 = new ArgumentInfo(IArgumentInfo.PERIOD_1,
				Message.getMessage(ResourceConstants.TIMEFUNCITON_PERIOD1_DISPLAYNAME),
				Message.getMessage(ResourceConstants.TIMEFUNCITON_PERIOD1), false);
		period2 = new ArgumentInfo(IArgumentInfo.PERIOD_2,
				Message.getMessage(ResourceConstants.TIMEFUNCITON_PERIOD2_DISPLAYNAME),
				Message.getMessage(ResourceConstants.TIMEFUNCITON_PERIOD2), false);

		if (this.name.equals(IBuildInBaseTimeFunction.PREVIOUS_MONTH)) {
			arguments.add(new ArgumentInfo(IArgumentInfo.N_PERIOD1,
					Message.getMessage(ResourceConstants.TIMEFUNCITON_N1_MONTH_DISPLAYNAME),
					Message.getMessage(ResourceConstants.TIMEFUNCITON_N1_MONTH), true));
		} else if (this.name.equals(IBuildInBaseTimeFunction.PREVIOUS_QUARTER)) {
			arguments.add(new ArgumentInfo(IArgumentInfo.N_PERIOD1,
					Message.getMessage(ResourceConstants.TIMEFUNCITON_N1_QUARTER_DISPLAYNAME),
					Message.getMessage(ResourceConstants.TIMEFUNCITON_N1_QUARTER), true));
		} else if (this.name.equals(IBuildInBaseTimeFunction.PREVIOUS_YEAR)) {
			arguments.add(new ArgumentInfo(IArgumentInfo.N_PERIOD1,
					Message.getMessage(ResourceConstants.TIMEFUNCITON_N1_YEAR_DISPLAYNAME),
					Message.getMessage(ResourceConstants.TIMEFUNCITON_N1_YEAR), true));
		} else if (this.name.equals(IBuildInBaseTimeFunction.WEEK_TO_DATE_LAST_YEAR)) {
			arguments.add(new ArgumentInfo(IArgumentInfo.N_PERIOD1,
					Message.getMessage(ResourceConstants.TIMEFUNCITON_N1_YEAR_DISPLAYNAME),
					Message.getMessage(ResourceConstants.TIMEFUNCITON_N1_YEAR), true));
		} else if (this.name.equals(IBuildInBaseTimeFunction.MONTH_TO_DATE_LAST_YEAR)) {
			arguments.add(new ArgumentInfo(IArgumentInfo.N_PERIOD1,
					Message.getMessage(ResourceConstants.TIMEFUNCITON_N1_YEAR_DISPLAYNAME),
					Message.getMessage(ResourceConstants.TIMEFUNCITON_N1_YEAR), true));
		} else if (this.name.equals(IBuildInBaseTimeFunction.QUARTER_TO_DATE_LAST_YEAR)) {
			arguments.add(new ArgumentInfo(IArgumentInfo.N_PERIOD1,
					Message.getMessage(ResourceConstants.TIMEFUNCITON_N1_YEAR_DISPLAYNAME),
					Message.getMessage(ResourceConstants.TIMEFUNCITON_N1_YEAR), true));
		} else if (this.name.equals(IBuildInBaseTimeFunction.PREVIOUS_WEEK_TO_DATE)) {
			arguments.add(new ArgumentInfo(IArgumentInfo.N_PERIOD1,
					Message.getMessage(ResourceConstants.TIMEFUNCITON_N1_WEEK_DISPLAYNAME),
					Message.getMessage(ResourceConstants.TIMEFUNCITON_N1_WEEK), true));
		} else if (this.name.equals(IBuildInBaseTimeFunction.PREVIOUS_MONTH_TO_DATE)) {
			arguments.add(new ArgumentInfo(IArgumentInfo.N_PERIOD1,
					Message.getMessage(ResourceConstants.TIMEFUNCITON_N1_MONTH_DISPLAYNAME),
					Message.getMessage(ResourceConstants.TIMEFUNCITON_N1_MONTH), true));
		} else if (this.name.equals(IBuildInBaseTimeFunction.PREVIOUS_QUARTER_TO_DATE)) {
			arguments.add(new ArgumentInfo(IArgumentInfo.N_PERIOD1,
					Message.getMessage(ResourceConstants.TIMEFUNCITON_N1_QUARTER_DISPLAYNAME),
					Message.getMessage(ResourceConstants.TIMEFUNCITON_N1_QUARTER), true));

		} else if (this.name.equals(IBuildInBaseTimeFunction.PREVIOUS_YEAR_TO_DATE)) {
			arguments.add(new ArgumentInfo(IArgumentInfo.N_PERIOD1,
					Message.getMessage(ResourceConstants.TIMEFUNCITON_N1_YEAR_DISPLAYNAME),
					Message.getMessage(ResourceConstants.TIMEFUNCITON_N1_YEAR), true));
		} else if (this.name.equals(IBuildInBaseTimeFunction.CURRENT_PERIOD_FROM_N_PERIOD_AGO)) {
			for (int i = 0; i < this.period_type1.size(); i++) {
				if (this.period_type1.get(i).name().equals(IArgumentInfo.Period_Type.Period_Type_ENUM.DAY.name())) {
					this.period_type1.remove(this.period_type1.get(i));
					break;
				}
			}
			((ArgumentInfo) period1).setPeriodChoices(period_type1);
			((ArgumentInfo) period1)
					.setDisplayname(Message.getMessage(ResourceConstants.TIMEFUNCITON_N1_CURRENT_DISPLAYNAME));
			arguments.add(period1);
			arguments.add(new ArgumentInfo(IArgumentInfo.N_PERIOD2,
					Message.getMessage(ResourceConstants.TIMEFUNCITON_N2_DISPLAYNAME),
					Message.getMessage(ResourceConstants.TIMEFUNCITON_N2), false));
			((ArgumentInfo) period2).setPeriodChoices(period_type2);
			((ArgumentInfo) period2)
					.setDisplayname(Message.getMessage(ResourceConstants.TIMEFUNCITON_N2_AGO_DISPLAYNAME));
			arguments.add(period2);
		} else if (this.name.equals(IBuildInBaseTimeFunction.PERIOD_TO_DATE_FROM_N_PERIOD_AGO)) {
			for (int i = 0; i < this.period_type1.size(); i++) {
				if (this.period_type1.get(i).name().equals(IArgumentInfo.Period_Type.Period_Type_ENUM.DAY.name())) {
					this.period_type1.remove(this.period_type1.get(i));
					break;
				}
			}
			((ArgumentInfo) period1).setPeriodChoices(period_type1);
			((ArgumentInfo) period1)
					.setDisplayname((Message.getMessage(ResourceConstants.TIMEFUNCITON_N1_PERIOD_DISPLAYNAME)));
			arguments.add(period1);
			arguments.add(new ArgumentInfo(IArgumentInfo.N_PERIOD2,
					Message.getMessage(ResourceConstants.TIMEFUNCITON_N2_DISPLAYNAME),
					Message.getMessage(ResourceConstants.TIMEFUNCITON_N2), false));
			((ArgumentInfo) period2).setPeriodChoices(period_type2);
			((ArgumentInfo) period2)
					.setDisplayname(Message.getMessage(ResourceConstants.TIMEFUNCITON_N2_AGO_DISPLAYNAME));
			arguments.add(period2);
		} else if (this.name.equals(IBuildInBaseTimeFunction.TRAILING_N_MONTHS)) {
			arguments.add(new ArgumentInfo(IArgumentInfo.N_PERIOD1,
					Message.getMessage(ResourceConstants.TIMEFUNCITON_TRAILING_N1_MONTH_DISPLAYNAME),
					Message.getMessage(ResourceConstants.TIMEFUNCITON_N1_MONTH), true));
		} else if (this.name.equals(IBuildInBaseTimeFunction.TRAILING_N_DAYS)) {
			arguments.add(new ArgumentInfo(IArgumentInfo.N_PERIOD1,
					Message.getMessage(ResourceConstants.TIMEFUNCITON_N1_DAY_DISPLAYNAME),
					Message.getMessage(ResourceConstants.TIMEFUNCITON_N1_DAY), true));
		} else if (this.name.equals(IBuildInBaseTimeFunction.TRAILING_N_PERIOD_FROM_N_PERIOD_AGO)) {
			((ArgumentInfo) period1).setPeriodChoices(period_type1);
			arguments.add(new ArgumentInfo(IArgumentInfo.N_PERIOD1,
					Message.getMessage(ResourceConstants.TIMEFUNCITON_TRAILING_N1_DISPLAYNAME),
					Message.getMessage(ResourceConstants.TIMEFUNCITON_N1), false));
			arguments.add(period1);
			arguments.add(new ArgumentInfo(IArgumentInfo.N_PERIOD2,
					Message.getMessage(ResourceConstants.TIMEFUNCITON_N2_DISPLAYNAME),
					Message.getMessage(ResourceConstants.TIMEFUNCITON_N2), false));
			((ArgumentInfo) period2).setPeriodChoices(period_type2);
			((ArgumentInfo) period2)
					.setDisplayname(Message.getMessage(ResourceConstants.TIMEFUNCITON_N2_AGO_DISPLAYNAME));
			arguments.add(period2);
		} else if (this.name.equals(IBuildInBaseTimeFunction.NEXT_N_PERIODS)) {
			arguments.add(new ArgumentInfo(IArgumentInfo.N_PERIOD1,
					Message.getMessage(ResourceConstants.TIMEFUNCTION_N1_NEXT_DISPLAYNAME),
					Message.getMessage(ResourceConstants.TIMEFUNCITON_N1), false));
			((ArgumentInfo) period1).setPeriodChoices(period_type1);
			arguments.add(period1);
		}
		return arguments;
	}

}

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

import java.util.List;

import org.eclipse.birt.report.data.adapter.i18n.Message;
import org.eclipse.birt.report.data.adapter.i18n.ResourceConstants;

import com.ibm.icu.util.ULocale;

public interface IArgumentInfo {
	/**
	 * Arguments for time function
	 */
	String PERIOD_1 = "Period1";
	String PERIOD_2 = "Period2";
	String N_PERIOD1 = "N for Period1";
	String N_PERIOD2 = "N for Period2";

	/**
	 * Get argument name for time function
	 *
	 * @return
	 */
	String getName();

	/**
	 * Get argument display name for time function
	 *
	 * @return
	 */
	String getDisplayName();

	/**
	 * Is this argument required for this time function
	 *
	 * @return
	 */
	boolean isOptional();

	/**
	 * Available value choices for this time function
	 *
	 * @return
	 */
	List<Period_Type> getPeriodChoices();

	/**
	 * Get description for this argument
	 *
	 * @return
	 */
	String getDescription();

	public class Period_Type {
		public enum Period_Type_ENUM {
			YEAR, QUARTER, MONTH, WEEK, DAY
		}

		private Period_Type_ENUM type;
		private ULocale locale;
		private boolean isPeriodToDate = false;

		public Period_Type(Period_Type_ENUM type, ULocale locale) {
			this.type = type;
			this.locale = locale;
			this.isPeriodToDate = false;
		}

		public Period_Type(Period_Type_ENUM type, ULocale locale, boolean isPeriodToDate) {
			this.type = type;
			this.locale = locale;
			this.isPeriodToDate = isPeriodToDate;
		}

		/**
		 *
		 * @return
		 */
		public String name() {
			return this.type.name();
		}

		/**
		 *
		 * @return
		 */
		public String displayName() {
			if (this.type.equals(Period_Type_ENUM.YEAR)) {
				if (isPeriodToDate) {
					return Message.getMessage(ResourceConstants.TIMEFUNCITON_PERIODCHOICE_PERIODTODATE_YEAR_DISPLAYNAME,
							locale);
				}
				return Message.getMessage(ResourceConstants.TIMEFUNCITON_PERIODCHOICE_YEAR_DISPLAYNAME, locale);
			}
			if (this.type.equals(Period_Type_ENUM.QUARTER)) {
				if (isPeriodToDate) {
					return Message.getMessage(
							ResourceConstants.TIMEFUNCITON_PERIODCHOICE_PERIODTODATE_QUARTER_DISPLAYNAME, locale);
				}
				return Message.getMessage(ResourceConstants.TIMEFUNCITON_PERIODCHOICE_QUARTER_DISPLAYNAME, locale);
			}
			if (this.type.equals(Period_Type_ENUM.MONTH)) {
				if (isPeriodToDate) {
					return Message.getMessage(
							ResourceConstants.TIMEFUNCITON_PERIODCHOICE_PERIODTODATE_MONTH_DISPLAYNAME, locale);
				}
				return Message.getMessage(ResourceConstants.TIMEFUNCITON_PERIODCHOICE_MONTH_DISPLAYNAME, locale);
			}
			if (this.type.equals(Period_Type_ENUM.WEEK)) {
				if (isPeriodToDate) {
					return Message.getMessage(ResourceConstants.TIMEFUNCITON_PERIODCHOICE_PERIODTODATE_WEEK_DISPLAYNAME,
							locale);
				}
				return Message.getMessage(ResourceConstants.TIMEFUNCITON_PERIODCHOICE_WEEK_DISPLAYNAME, locale);
			}
			if (this.type.equals(Period_Type_ENUM.DAY)) {
				return Message.getMessage(ResourceConstants.TIMEFUNCITON_PERIODCHOICE_DAY_DISPLAYNAME, locale);
			}
			return this.type.name();
		}
	}
}

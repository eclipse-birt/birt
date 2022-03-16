/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.chart.internal.factory;

import java.text.FieldPosition;
import java.util.Date;

import org.eclipse.birt.chart.util.CDateTime;
import org.eclipse.birt.chart.util.ChartUtil;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;

/**
 * An internal factory help to generate IDateFormatWrapper.
 */
public class DateFormatWrapperFactory {

	/**
	 * Prevent from instanciation
	 */
	private DateFormatWrapperFactory() {
	}

	/**
	 * Returns a preferred format specifier for tick labels that represent axis
	 * values that will be computed based on the difference between cdt1 and cdt2
	 *
	 * @param iUnit The unit for which a preferred pattern is being requested
	 *
	 * @return A preferred datetime format for the given unit
	 */
	public static final IDateFormatWrapper getPreferredDateFormat(int iUnit) {
		return getPreferredDateFormat(iUnit, ULocale.getDefault());
	}

	/**
	 * Returns a preferred format specifier for tick labels that represent axis
	 * values that will be computed based on the difference between cdt1 and cdt2
	 *
	 * @param iUnit  The unit for which a preferred pattern is being requested
	 * @param locale The locale for format style
	 *
	 * @return A preferred datetime format for the given unit
	 */
	public static final IDateFormatWrapper getPreferredDateFormat(int iUnit, ULocale locale) {
		return getPreferredDateFormat(iUnit, locale, true);
	}

	/**
	 * Returns a preferred format specifier for tick labels that represent axis
	 * values that will be computed based on the difference between cdt1 and cdt2
	 *
	 * @param iUnit         The unit for which a preferred pattern is being
	 *                      requested
	 * @param locale        The locale for format style
	 * @param keepHierarchy indicates if the format should keep hierarchy
	 *
	 * @return A preferred datetime format for the given unit
	 */
	public static final IDateFormatWrapper getPreferredDateFormat(int iUnit, ULocale locale, boolean keepHierarchy) {
		IDateFormatWrapper df;
		String pattern = ChartUtil.createDefaultFormatPattern(iUnit, keepHierarchy);
		df = new CommonDateFormatWrapper(new SimpleDateFormat(pattern, locale));
		// Special cases for dynamic patterns
		switch (iUnit) {
		case Calendar.MONTH:
			if (keepHierarchy) {
				df = new MonthDateFormat(locale);
			}
			break;
		case Calendar.DAY_OF_MONTH:// Same as DATE
			if (keepHierarchy) {
				df = new CommonDateFormatWrapper(DateFormat.getDateInstance(DateFormat.MEDIUM, locale));
			}
			break;
		}
		return df;
	}

	static class CommonDateFormatWrapper implements IDateFormatWrapper {

		private DateFormat formater;

		public CommonDateFormatWrapper(DateFormat formater) {
			this.formater = formater;
		}

		@Override
		public String format(Date date) {
			return formater.format(date);
		}

		@Override
		public String format(CDateTime calendar) {
			if (calendar.isFullDateTime()) {
				formater.setTimeZone(calendar.getTimeZone());
			}
			return format(calendar.getTime());
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.eclipse.birt.chart.internal.factory.IDateFormatWrapper#toLocalizedPattern
		 * ()
		 */
		@Override
		public String toLocalizedPattern() {
			if (formater instanceof SimpleDateFormat) {
				return ((SimpleDateFormat) formater).toLocalizedPattern();
			}
			return "MMM d, yyyy h:mm:ss a"; //$NON-NLS-1$
		}

	}

	static class HourDateFormat implements IDateFormatWrapper {

		private ULocale locale;
		private TimeZone tz;

		public HourDateFormat(ULocale locale) {
			super();
			this.locale = locale;
		}

		@Override
		public String format(Date date) {
			StringBuilder sb = new StringBuilder();
			DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, locale);
			if (tz != null) {
				df.setTimeZone(tz);
			}
			sb.append(df.format(date));
			sb.append("\n");//$NON-NLS-1$
			df = new SimpleDateFormat("HH:mm", locale);//$NON-NLS-1$
			if (tz != null) {
				df.setTimeZone(tz);
			}
			sb.append(df.format(date));
			return sb.toString();
		}

		@Override
		public String format(CDateTime calendar) {
			if (calendar.isFullDateTime()) {
				tz = calendar.getTimeZone();
			}
			return format(calendar.getTime());
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.eclipse.birt.chart.internal.factory.IDateFormatWrapper#toLocalizedPattern
		 * ()
		 */
		@Override
		public String toLocalizedPattern() {
			DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, locale);
			if (df instanceof SimpleDateFormat) {
				return ((SimpleDateFormat) df).toLocalizedPattern() + "\n" //$NON-NLS-1$
						+ new SimpleDateFormat("HH:mm", locale).toLocalizedPattern(); //$NON-NLS-1$
			}
			return "MMMM d, yyyy HH:mm"; //$NON-NLS-1$
		}

	}

	static class MonthDateFormat implements IDateFormatWrapper {

		private ULocale locale;
		private TimeZone tz;

		public MonthDateFormat(ULocale locale) {
			super();
			this.locale = locale;
		}

		@Override
		public String format(Date date) {
			StringBuffer str = new StringBuffer();
			FieldPosition pos = new FieldPosition(DateFormat.DATE_FIELD);
			DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
			if (tz != null) {
				df.setTimeZone(tz);
			}
			df.format(date, str, pos);
			int endIndex;
			if (pos.getEndIndex() >= str.length()) {
				endIndex = pos.getEndIndex();
			} else {
				endIndex = pos.getEndIndex() + (str.charAt(pos.getEndIndex()) == ',' ? 2 : 1);
			}
			if (endIndex >= str.length()) // means date is the last one, need
											// to remove separator
			{
				endIndex = pos.getBeginIndex();
				while (endIndex > 0) {
					char ch = str.charAt(endIndex - 1);
					if (ch == ' ' || ch == ',' || ch == '/' || ch == '-' || ch == '.') {
						endIndex--;
					} else {
						break;
					}
				}
				return str.substring(0, endIndex);
			}
			return str.substring(0, pos.getBeginIndex()) + str.substring(endIndex);
		}

		@Override
		public String format(CDateTime calendar) {
			if (calendar.isFullDateTime()) {
				tz = calendar.getTimeZone();
			}
			return format(calendar.getTime());
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.eclipse.birt.chart.internal.factory.IDateFormatWrapper#toLocalizedPattern
		 * ()
		 */
		@Override
		public String toLocalizedPattern() {
			DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
			if (df instanceof SimpleDateFormat) {
				String pattern = ((SimpleDateFormat) df).toLocalizedPattern();
				return pattern.replaceAll("(-|/)?d+(\\.|,|/|-)?\\s?", "").trim(); //$NON-NLS-1$ //$NON-NLS-2$
			}
			return "MMM yyyy"; //$NON-NLS-1$
		}
	}
}

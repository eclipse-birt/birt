/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.util;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

import com.ibm.icu.util.ULocale;

/**
 * Class to handle the format pattern of date time
 *
 * @since 3.3
 */
public class FormatDateTimePattern {

	/** property: date time format of type year */
	public static final String DATETIME_FORMAT_TYPE_YEAR = "datetiem_format_type_year"; //$NON-NLS-1$

	/** property: date time format of type short year */
	public static final String DATETIME_FORMAT_TYPE_SHORT_YEAR = "datetiem_format_type_short_year"; //$NON-NLS-1$

	/** property: date time format of type long month year */
	public static final String DATETIME_FORMAT_TYPE_LONG_MONTH_YEAR = "datetiem_format_type_long_month_year"; //$NON-NLS-1$

	/** property: date time format of type short month year */
	public static final String DATETIME_FORMAT_TYPE_SHORT_MONTH_YEAR = "datetiem_format_type_shot_month_year"; //$NON-NLS-1$

	/** property: date time format of type month */
	public static final String DATETIME_FORMAT_TYPE_MONTH = "datetiem_format_type_month"; //$NON-NLS-1$

	/** property: date time format of type long day of week */
	public static final String DATETIME_FORMAT_TYPE_LONG_DAY_OF_WEEK = "datetiem_format_type_long_day_of_week"; //$NON-NLS-1$

	/** property: date time format of type day of month */
	public static final String DATETIME_FORMAT_TYPE_DAY_OF_MONTH = "datetiem_format_type_day_of_month"; //$NON-NLS-1$

	/** property: date time format of type medium day of year */
	public static final String DATETIME_FORMAT_TYPE_MEDIUM_DAY_OF_YEAR = "datetiem_format_type_medium_day_of_year"; //$NON-NLS-1$

	/** property: date time format of type minutes */
	public static final String DATETIME_FORMAT_TYPE_MINUTES = "datetiem_format_type_minutes"; //$NON-NLS-1$

	/** property: date time format of type seconds */
	public static final String DATETIME_FORMAT_TYPE_SECONDS = "datetiem_format_type_secontds"; //$NON-NLS-1$

	/** property: date time format of type general time */
	public static final String DATETIME_FORMAT_TYPE_GENERAL_TIME = "datetiem_format_type_general_time"; //$NON-NLS-1$

	private static final String[] customCategories = { DATETIME_FORMAT_TYPE_YEAR, DATETIME_FORMAT_TYPE_SHORT_YEAR,
			DATETIME_FORMAT_TYPE_LONG_MONTH_YEAR, DATETIME_FORMAT_TYPE_SHORT_MONTH_YEAR, DATETIME_FORMAT_TYPE_MONTH,
			DATETIME_FORMAT_TYPE_LONG_DAY_OF_WEEK, DATETIME_FORMAT_TYPE_DAY_OF_MONTH,
			DATETIME_FORMAT_TYPE_MEDIUM_DAY_OF_YEAR, DATETIME_FORMAT_TYPE_MINUTES, DATETIME_FORMAT_TYPE_SECONDS,
			DATETIME_FORMAT_TYPE_GENERAL_TIME, };

	/**
	 * Get all categories of custom pattern
	 *
	 * @return Return all categories of custom pattern
	 */
	public static String[] getCustormPatternCategorys() {
		return customCategories;
	}

	/**
	 * Get the display name for the custom category
	 *
	 * @param custormCategory custom category
	 * @return Return the display name for the custom category
	 */
	public static String getDisplayName4CustomCategory(String custormCategory) {
		return Messages.getString("FormatDateTimePattern." + custormCategory); //$NON-NLS-1$
	}

	/**
	 * Get the custom format pattern
	 *
	 * @param customCategory custom category
	 * @param locale         locale
	 * @return Return the custom format pattern
	 */
	public static String getCustomFormatPattern(String customCategory, ULocale locale) {
		if (locale == null) {
			locale = ULocale.getDefault();
		}

		if (DATETIME_FORMAT_TYPE_GENERAL_TIME.equals(customCategory)) {
			return Messages.getString("FormatDateTimePattern.pattern." + DATETIME_FORMAT_TYPE_GENERAL_TIME, //$NON-NLS-1$
					locale.toLocale());
		}
		if (DATETIME_FORMAT_TYPE_YEAR.equals(customCategory)) {
			return Messages.getString("FormatDateTimePattern.pattern." + DATETIME_FORMAT_TYPE_YEAR, locale.toLocale()); //$NON-NLS-1$
		}
		if (DATETIME_FORMAT_TYPE_SHORT_YEAR.equals(customCategory)) {
			return Messages.getString("FormatDateTimePattern.pattern." + DATETIME_FORMAT_TYPE_SHORT_YEAR, //$NON-NLS-1$
					locale.toLocale());
		}
		if (DATETIME_FORMAT_TYPE_LONG_MONTH_YEAR.equals(customCategory)) {
			return Messages.getString("FormatDateTimePattern.pattern." + DATETIME_FORMAT_TYPE_LONG_MONTH_YEAR, //$NON-NLS-1$
					locale.toLocale());
		}
		if (DATETIME_FORMAT_TYPE_SHORT_MONTH_YEAR.equals(customCategory)) {
			return Messages.getString("FormatDateTimePattern.pattern." + DATETIME_FORMAT_TYPE_SHORT_MONTH_YEAR, //$NON-NLS-1$
					locale.toLocale());
		}
		if (DATETIME_FORMAT_TYPE_MONTH.equals(customCategory)) {
			return Messages.getString("FormatDateTimePattern.pattern." + DATETIME_FORMAT_TYPE_MONTH, locale.toLocale()); //$NON-NLS-1$
		}
		if (DATETIME_FORMAT_TYPE_LONG_DAY_OF_WEEK.equals(customCategory)) {
			return Messages.getString("FormatDateTimePattern.pattern." + DATETIME_FORMAT_TYPE_LONG_DAY_OF_WEEK, //$NON-NLS-1$
					locale.toLocale());
		}
		if (DATETIME_FORMAT_TYPE_DAY_OF_MONTH.equals(customCategory)) {
			return Messages.getString("FormatDateTimePattern.pattern." + DATETIME_FORMAT_TYPE_DAY_OF_MONTH, //$NON-NLS-1$
					locale.toLocale());
		}
		if (DATETIME_FORMAT_TYPE_MEDIUM_DAY_OF_YEAR.equals(customCategory)) {
			return Messages.getString("FormatDateTimePattern.pattern." + DATETIME_FORMAT_TYPE_MEDIUM_DAY_OF_YEAR, //$NON-NLS-1$
					locale.toLocale());
		}
		if (DATETIME_FORMAT_TYPE_MINUTES.equals(customCategory)) {
			return Messages.getString("FormatDateTimePattern.pattern." + DATETIME_FORMAT_TYPE_MINUTES, //$NON-NLS-1$
					locale.toLocale());
		}
		if (DATETIME_FORMAT_TYPE_SECONDS.equals(customCategory)) {
			return Messages.getString("FormatDateTimePattern.pattern." + DATETIME_FORMAT_TYPE_SECONDS, //$NON-NLS-1$
					locale.toLocale());
		}

		return ""; //$NON-NLS-1$
	}

	/**
	 * Retrieves format pattern from arrays given format type categories.
	 *
	 * @param category Given format type category.
	 * @return The corresponding format pattern string.
	 */

	public static String getPatternForCategory(String category) {
		String pattern;
		if (DesignChoiceConstants.DATETIME_FORMAT_TYPE_GENERAL_DATE.equals(category)
				|| DesignChoiceConstants.DATE_FORMAT_TYPE_GENERAL_DATE.equals(category)) {
			pattern = "General Date"; //$NON-NLS-1$

		} else if (DesignChoiceConstants.DATETIME_FORMAT_TYPE_LONG_DATE.equals(category)
				|| DesignChoiceConstants.DATE_FORMAT_TYPE_LONG_DATE.equals(category)
		) {
			pattern = "Long Date"; //$NON-NLS-1$
		} else if (DesignChoiceConstants.DATETIME_FORMAT_TYPE_MEDIUM_DATE.equals(category)
				|| DesignChoiceConstants.DATE_FORMAT_TYPE_MEDIUM_DATE.equals(category)
		) {
			pattern = "Medium Date"; //$NON-NLS-1$

		} else if (DesignChoiceConstants.DATETIME_FORMAT_TYPE_SHORT_DATE.equals(category)
				|| DesignChoiceConstants.DATE_FORMAT_TYPE_SHORT_DATE.equals(category)) {
			pattern = "Short Date"; //$NON-NLS-1$

		} else if (DesignChoiceConstants.DATETIME_FORMAT_TYPE_LONG_TIME.equals(category)
				|| DesignChoiceConstants.TIME_FORMAT_TYPE_LONG_TIME.equals(category)) {
			pattern = "Long Time"; //$NON-NLS-1$

		} else if (DesignChoiceConstants.DATETIME_FORMAT_TYPE_MEDIUM_TIME.equals(category)
				|| DesignChoiceConstants.TIME_FORMAT_TYPE_MEDIUM_TIME.equals(category)
		) {
			pattern = "Medium Time"; //$NON-NLS-1$

		} else if (DesignChoiceConstants.DATETIME_FORMAT_TYPE_SHORT_TIME.equals(category)
				|| DesignChoiceConstants.TIME_FORMAT_TYPE_SHORT_TIME.equals(category)
		) {
			pattern = "Short Time"; //$NON-NLS-1$

		} else if (DesignChoiceConstants.TIME_FORMAT_TYPE_TIME_PICKER_SHORT_TIME.equals(category)) {
			pattern = "HH:mm"; //$NON-NLS-1$

		} else if (DesignChoiceConstants.TIME_FORMAT_TYPE_TIME_PICKER_MEDIUM_TIME.equals(category)) {
			pattern = "HH:mm:ss"; //$NON-NLS-1$

		} else if (DesignChoiceConstants.DATE_FORMAT_TYPE_DATE_PICKER.equals(category)
				|| DesignChoiceConstants.DATETIME_FORMAT_TYPE_DATE_PICKER.equals(category)) {
			pattern = "yyyy-MM-dd"; //$NON-NLS-1$

		} else if (DesignChoiceConstants.DATETIME_FORMAT_TYPE_DATE_TIME_PICKER_SHORT_TIME.equals(category)) {
			pattern = "yyyy-MM-dd HH:mm"; //$NON-NLS-1$

		} else if (DesignChoiceConstants.DATETIME_FORMAT_TYPE_DATE_TIME_PICKER_MEDIUM_TIME.equals(category)) {
			pattern = "yyyy-MM-dd HH:mm:ss"; //$NON-NLS-1$

		} else {
			// default, unformatted.
			pattern = ""; //$NON-NLS-1$
		}
		return pattern;
	}
}

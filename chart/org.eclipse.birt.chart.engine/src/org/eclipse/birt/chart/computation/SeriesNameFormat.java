/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.chart.computation;

import org.eclipse.birt.chart.internal.datafeed.GroupingUtil;
import org.eclipse.birt.chart.internal.factory.DateFormatWrapperFactory;
import org.eclipse.birt.chart.internal.factory.IDateFormatWrapper;
import org.eclipse.birt.chart.model.attribute.DataType;
import org.eclipse.birt.chart.model.attribute.GroupingUnitType;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.SeriesGrouping;
import org.eclipse.birt.chart.util.CDateTime;

import com.ibm.icu.text.DecimalFormat;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.ULocale;

/**
 * SeriesNameFormat is a Formatter special for SeriesIdentifier with optional
 * Y-Grouping.
 */

public class SeriesNameFormat {

	public static final SeriesNameFormat DEFAULT_FORMAT = new SeriesNameFormat();

	private SeriesNameFormat() {
		// disable public construactor
	}

	/**
	 * Returns default series format
	 *
	 * @param sd  series definition
	 * @param loc locale
	 * @return default series format
	 * @deprecated to use
	 *             {@link #getSeriesNameFormat(SeriesDefinition, ULocale, boolean)}
	 */
	@Deprecated
	public static SeriesNameFormat getSeriesNameFormat(SeriesDefinition sd, ULocale loc) {
		return getSeriesNameFormat(sd, loc, true);
	}

	/**
	 * Returns default series format
	 *
	 * @param sd            series definition
	 * @param loc           locale
	 * @param keepHierarchy indicates if hierarchy should be kept
	 * @return default series format
	 */
	public static SeriesNameFormat getSeriesNameFormat(SeriesDefinition sd, ULocale loc, boolean keepHierarchy) {
		if (sd != null && sd.getQuery() != null && sd.getQuery().getGrouping() != null) {
			SeriesGrouping sg = sd.getQuery().getGrouping();
			if (sg.getGroupType() == DataType.DATE_TIME_LITERAL) {
				return new SeriesNameDateFormat(sg.getGroupingUnit(), loc, keepHierarchy);
			}
		}

		return DEFAULT_FORMAT;
	}

	public String format(Object obj) {
		String str = ""; //$NON-NLS-1$

		if (obj != null) {
			if (obj instanceof Number) {
				// TODO: use format cache to improve performance
				double d = ((Number) obj).doubleValue();
				String sPattern = ValueFormatter.getNumericPattern(d);
				DecimalFormat df = new DecimalFormat(sPattern);
				str = df.format(d);
			} else {
				str = obj.toString();
			}
		}

		return str;
	}

	/**
	 * sub class for DateTime grouping type SeriesNameDateFormat
	 */
	private static class SeriesNameDateFormat extends SeriesNameFormat {

		private GroupingUnitType unitType;
		private IDateFormatWrapper dfWrapper;

		/**
		 * @param unitType
		 */
		public SeriesNameDateFormat(GroupingUnitType unitType, ULocale loc, boolean keepHierarchy) {
			this.unitType = unitType;
			dfWrapper = DateFormatWrapperFactory.getPreferredDateFormat(GroupingUtil.groupingUnit2CDateUnit(unitType),
					loc, keepHierarchy);
		}

		/**
		 * Convert GroupingUnit type to CDateUnit type.
		 *
		 * @param groupingUnitType the GroupingUnit type.
		 * @return CDateUnit type of integer.
		 * @since 2.3, it is merged from <code>DataProcessor</code>, make the method to
		 *        be a static usage.
		 */
		private static int groupingUnit2CDateUnit(GroupingUnitType groupingUnitType) {
			if (groupingUnitType != null) {
				switch (groupingUnitType.getValue()) {
				case GroupingUnitType.SECONDS:
					return Calendar.SECOND;
				case GroupingUnitType.MINUTES:
					return Calendar.MINUTE;
				case GroupingUnitType.HOURS:
					return Calendar.HOUR_OF_DAY;
				case GroupingUnitType.DAYS:
					return Calendar.DATE;
				case GroupingUnitType.WEEKS:
					return Calendar.WEEK_OF_YEAR;
				case GroupingUnitType.MONTHS:
					return Calendar.MONTH;
				case GroupingUnitType.YEARS:
					return Calendar.YEAR;
				case GroupingUnitType.QUARTERS:
					return GroupingUnitType.QUARTERS;
				}
			}

			return Calendar.MILLISECOND;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.birt.chart.computation.SeriesNameFormat#format(java.lang
		 * .Object)
		 */
		@Override
		public String format(Object obj) {
			if (obj instanceof CDateTime) {
				return formatCDateTime((CDateTime) obj);
			} else {
				return super.format(obj);
			}
		}

		private String formatCDateTime(CDateTime cd) {
			cd.clearBelow(groupingUnit2CDateUnit(unitType));
			return dfWrapper.format(cd.getTime());
		}
	}
}

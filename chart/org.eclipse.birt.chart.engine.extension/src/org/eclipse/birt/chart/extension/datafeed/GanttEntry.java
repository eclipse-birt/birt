/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.extension.datafeed;

import java.util.Date;

import org.eclipse.birt.chart.computation.ValueFormatter;
import org.eclipse.birt.chart.datafeed.IDataPointEntry;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.util.CDateTime;

import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.ULocale;

/**
 * GanttEntry
 */
public final class GanttEntry implements IDataPointEntry {

	private CDateTime dateStart;

	private CDateTime dateEnd;

	private String strLabel;

	/**
	 * The constructor.
	 *
	 * @param dateStart
	 * @param dateEnd
	 * @param strLabel
	 */
	public GanttEntry(Calendar dateStart, Calendar dateEnd, String strLabel) {
		this.dateStart = new CDateTime(dateStart);
		this.dateEnd = new CDateTime(dateEnd);
		this.strLabel = strLabel;
	}

	/**
	 * The constructor.
	 *
	 * @param dateStart
	 * @param dateEnd
	 * @param strLabel
	 */
	public GanttEntry(Date dateStart, Date dateEnd, String strLabel) {
		this.dateStart = new CDateTime(dateStart);
		this.dateEnd = new CDateTime(dateEnd);
		this.strLabel = strLabel;
	}

	/**
	 * The constructor.
	 *
	 * @param dateStart
	 * @param dateEnd
	 * @param strLabel
	 */
	public GanttEntry(CDateTime dateStart, CDateTime dateEnd, String strLabel) {
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
		this.strLabel = strLabel;
	}

	/**
	 * The constructor.
	 *
	 * @param oaThreeComponents
	 */
	GanttEntry(Object[] oaThreeComponents) {
		if (oaThreeComponents[0] instanceof CDateTime) {
			this.dateStart = (CDateTime) oaThreeComponents[0];
		} else {
			this.dateStart = null;
		}

		if (oaThreeComponents[1] instanceof CDateTime) {
			this.dateEnd = (CDateTime) oaThreeComponents[1];
		} else {
			this.dateEnd = null;
		}

		if (oaThreeComponents[2] != null) {
			this.strLabel = String.valueOf(oaThreeComponents[2]);
		} else {
			this.strLabel = null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getFormattedString(null, ULocale.getDefault());
	}

	/**
	 * @return Returns the start datetime.
	 */
	public CDateTime getStart() {
		return dateStart;
	}

	/**
	 * @param start The start datetime to set.
	 */
	public void setStart(CDateTime start) {
		this.dateStart = start;
	}

	/**
	 * @return Returns the end datetime.
	 */
	public CDateTime getEnd() {
		return dateEnd;
	}

	/**
	 * @param end The end datetime to set.
	 */
	public void setEnd(CDateTime end) {
		this.dateEnd = end;
	}

	/**
	 * @return Returns the label.
	 */
	public String getLabel() {
		return strLabel;
	}

	/**
	 * @param end The label to set.
	 */
	public void setLabel(String strLabel) {
		this.strLabel = strLabel;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.datafeed.IFormattable#getFormattedString(java.
	 * lang.Object, com.ibm.icu.util.ULocale)
	 */
	@Override
	public String getFormattedString(FormatSpecifier formatter, ULocale locale) {
		String strStart = getFormattedString(GanttDataPointDefinition.TYPE_START_DATE, formatter, locale);
		String strEnd = getFormattedString(GanttDataPointDefinition.TYPE_END_DATE, formatter, locale);
		String formattedString = "S " + strStart + "; E " + strEnd; //$NON-NLS-1$ //$NON-NLS-2$
		if (strLabel != null) {
			formattedString += "; " + strLabel; //$NON-NLS-1$
		}

		return formattedString;
	}

	@Override
	public String getFormattedString(String type, FormatSpecifier formatter, ULocale locale) {
		String str = "";//$NON-NLS-1$
		try {
			if (GanttDataPointDefinition.TYPE_START_DATE.equals(type)) {
				str = ValueFormatter.format(dateStart, formatter, locale, null);
			} else if (GanttDataPointDefinition.TYPE_END_DATE.equals(type)) {
				str = ValueFormatter.format(dateEnd, formatter, locale, null);
			} else if (GanttDataPointDefinition.TYPE_DECORATION_LABEL.equals(type)) {
				str = ValueFormatter.format(strLabel, formatter, locale, null);
			}
		} catch (ChartException e) {
			Logger.getLogger("org.eclipse.birt.chart.engine/exception") //$NON-NLS-1$
					.log(e);
		}
		return str;
	}

	@Override
	public boolean isValid() {
		return (dateStart != null || dateEnd != null);
	}

}

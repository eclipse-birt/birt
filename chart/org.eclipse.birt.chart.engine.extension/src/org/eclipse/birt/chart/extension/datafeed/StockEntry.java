/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.extension.datafeed;

import org.eclipse.birt.chart.computation.ValueFormatter;
import org.eclipse.birt.chart.datafeed.IDataPointEntry;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;

import com.ibm.icu.util.ULocale;

/**
 * StockEntry
 */
public final class StockEntry implements IDataPointEntry {

	private double dOpen;

	private double dLow;

	private double dHigh;

	private double dClose;

	/**
	 * 
	 * @param dOpen
	 * @param dLow
	 * @param dHigh
	 * @param dClose
	 */
	public StockEntry(double dOpen, double dLow, double dHigh, double dClose) {
		this.dOpen = dOpen;
		this.dLow = dLow;
		this.dHigh = dHigh;
		this.dClose = dClose;
	}

	/**
	 * 
	 * @param oaFourComponents
	 */
	public StockEntry(Object[] oaFourComponents) {
		assert oaFourComponents.length == 4;
		this.dHigh = (oaFourComponents[0] instanceof Number) ? ((Number) oaFourComponents[0]).doubleValue()
				: Double.NaN;
		this.dLow = (oaFourComponents[1] instanceof Number) ? ((Number) oaFourComponents[1]).doubleValue() : Double.NaN;
		this.dOpen = (oaFourComponents[2] instanceof Number) ? ((Number) oaFourComponents[2]).doubleValue()
				: Double.NaN;
		this.dClose = (oaFourComponents[3] instanceof Number) ? ((Number) oaFourComponents[3]).doubleValue()
				: Double.NaN;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getFormattedString(null, ULocale.getDefault());
	}

	/**
	 * @return Returns the close.
	 */
	public final double getClose() {
		return dClose;
	}

	/**
	 * @param close The close to set.
	 */
	public final void setClose(double close) {
		this.dClose = close;
	}

	/**
	 * @return Returns the high.
	 */
	public final double getHigh() {
		return dHigh;
	}

	/**
	 * @param high The high to set.
	 */
	public final void setHigh(double high) {
		this.dHigh = high;
	}

	/**
	 * @return Returns the low.
	 */
	public final double getLow() {
		return dLow;
	}

	/**
	 * @param low The low to set.
	 */
	public final void setLow(double low) {
		this.dLow = low;
	}

	/**
	 * @return Returns the open.
	 */
	public final double getOpen() {
		return dOpen;
	}

	/**
	 * @param open The open to set.
	 */
	public final void setOpen(double open) {
		this.dOpen = open;
	}

	public String getFormattedString(String type, FormatSpecifier formatter, ULocale locale) {
		String str = null;
		try {
			double dValue = Double.NaN;
			if (StockDataPointDefinition.TYPE_HIGH.equals(type)) {
				dValue = dHigh;
			} else if (StockDataPointDefinition.TYPE_LOW.equals(type)) {
				dValue = dLow;
			} else if (StockDataPointDefinition.TYPE_OPEN.equals(type)) {
				dValue = dOpen;
			} else if (StockDataPointDefinition.TYPE_CLOSE.equals(type)) {
				dValue = dClose;
			} else {
				return null;
			}

			if (formatter == null) {
				str = Double.toString(dValue);
			} else {
				str = ValueFormatter.format(new Double(dValue), formatter, locale, null);
			}

		} catch (ChartException e) {
			Logger.getLogger("org.eclipse.birt.chart.engine/exception") //$NON-NLS-1$
					.log(e);
		}
		return str;
	}

	public String getFormattedString(FormatSpecifier formatter, ULocale locale) {
		StringBuilder sb = new StringBuilder();
		sb.append('H');
		sb.append(getFormattedString(StockDataPointDefinition.TYPE_HIGH, formatter, locale));
		sb.append(" L"); //$NON-NLS-1$
		sb.append(getFormattedString(StockDataPointDefinition.TYPE_LOW, formatter, locale));
		sb.append(" O"); //$NON-NLS-1$
		sb.append(getFormattedString(StockDataPointDefinition.TYPE_OPEN, formatter, locale));
		sb.append(" C"); //$NON-NLS-1$
		sb.append(getFormattedString(StockDataPointDefinition.TYPE_CLOSE, formatter, locale));
		return sb.toString();
	}

	public boolean isValid() {
		return (!(Double.isNaN(dHigh) || Double.isNaN(dLow) || Double.isNaN(dClose) || Double.isNaN(dOpen)));
	}
}

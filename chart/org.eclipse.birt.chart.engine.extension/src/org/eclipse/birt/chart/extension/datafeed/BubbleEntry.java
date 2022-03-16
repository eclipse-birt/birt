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

import org.eclipse.birt.chart.computation.ValueFormatter;
import org.eclipse.birt.chart.datafeed.NumberDataPointEntry;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.FractionNumberFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.JavaNumberFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.NumberFormatSpecifier;
import org.eclipse.birt.chart.util.BigNumber;
import org.eclipse.birt.chart.util.NumberUtil;

import com.ibm.icu.math.BigDecimal;
import com.ibm.icu.util.ULocale;

/**
 * BubbleEntry
 */
public final class BubbleEntry extends NumberDataPointEntry {

	private static ILogger logger = Logger.getLogger("org.eclipse.birt.chart.engine.extension/render"); //$NON-NLS-1$

	private Object oValue;

	private double dSize;

	private BigNumber bnSize;

	private Number bdSize;

	private boolean bIsBigNumber = false;

	private boolean bIsBigDecimal = false;

	private BigDecimal divisor;

	/** Index for category value, starting with 1. Default value is 0 */
	private int index = 0;

	/**
	 * The constructor.
	 *
	 * @param value value could be any type or null. Value will represent a category
	 *              entry with the specified index.
	 * @param size  size could be Number or null. Null means this entry will be
	 *              omitted
	 * @param index index for category value. Starting with 1
	 */
	public BubbleEntry(Object value, Object size, int index) {
		this(value, size);
		this.index = index;
	}

	/**
	 * The constructor.
	 *
	 * @param value value could be Number, String, CDateTime or null. Null means
	 *              this entry will be omitted.
	 * @param size  size could be Number or null. Null means this entry will be
	 *              omitted
	 */
	public BubbleEntry(Object value, Object size) {
		init(value, size);
	}

	private void init(Object value, Object size) {
		if (NumberUtil.isBigNumber(value)) {
			bIsBigNumber = true;
			divisor = ((BigNumber) value).getDivisor();
			bnSize = (BigNumber) size;
		} else if (NumberUtil.isBigDecimal(value)) {
			bIsBigDecimal = true;
			if (NumberUtil.isJavaMathBigDecimal(value)) {
				bdSize = NumberUtil.asJavaMathBigDecimal((Number) size);
			} else {
				bdSize = NumberUtil.asBigDecimal((Number) size);
			}
		}

		this.oValue = value;
		if (value instanceof Double && ((Double) value).isNaN()) {
			// Handle NaN as null
			this.oValue = null;
		}

		// Invalid size is set to 0 by default
		this.dSize = (size instanceof Number) ? ((Number) size).doubleValue() : 0;
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
	 * @return Returns the Object value.
	 */
	public Object getValue() {
		if (index > 0) {
			return Integer.valueOf(index);
		}
		return this.oValue;
	}

	/**
	 * @param value The value to set.
	 */
	public void setValue(Object value) {
		this.oValue = value;
		if (NumberUtil.isBigNumber(value)) {
			bIsBigNumber = true;
			divisor = ((BigNumber) value).getDivisor();
		} else {
			bIsBigNumber = false;
		}
	}

	/**
	 * @return Returns the size.
	 */
	public double getSize() {
		return bIsBigNumber ? bnSize.doubleValue() : dSize;
	}

	public Number getSizeNumber() {
		if (bIsBigNumber) {
			return bnSize;
		} else if (bIsBigDecimal) {
			return bdSize;
		}
		return Double.valueOf(dSize);
	}

	/**
	 * @param end The size to set.
	 */
	public void setSize(double dSize) {
		this.dSize = dSize;
	}

	public void setSize(Number size) {
		if (NumberUtil.isBigNumber(size)) {
			this.dSize = ((BigNumber) size).doubleValue();
			this.bnSize = (BigNumber) size;
			return;
		}

		this.setSize(size.doubleValue());
	}

	@Override
	public String getFormattedString(String type, FormatSpecifier formatter, ULocale locale) {
		String str = null;
		try {
			if (BubbleDataPointDefinition.TYPE_VALUE.equals(type)) {
				str = ValueFormatter.format(oValue, formatter, locale, null);
			} else if (BubbleDataPointDefinition.TYPE_SIZE.equals(type)) {
				Object size = null;
				if (bIsBigNumber) {
					size = bnSize;
				} else {
					size = new Double(dSize);
				}
				str = ValueFormatter.format(size, formatter, locale, null);
			}
		} catch (ChartException e) {
			logger.log(e);
		}
		return str;
	}

	@Override
	public String getFormattedString(FormatSpecifier formatter, ULocale locale) {
		String strSize = bIsBigNumber ? String.valueOf(bnSize) : String.valueOf(dSize);
		if (formatter instanceof NumberFormatSpecifier || formatter instanceof JavaNumberFormatSpecifier
				|| formatter instanceof FractionNumberFormatSpecifier) {
			try {
				strSize = ValueFormatter.format(bIsBigNumber ? bnSize : dSize, formatter, locale, null);
			} catch (ChartException e) {
				logger.log(e);
			}
		}
		String strValue = "";//$NON-NLS-1$
		try {
			if (oValue == null) {
				// Do not display value if it's null
				return "S" + strSize;//$NON-NLS-1$
			} else {
				strValue = ValueFormatter.format(oValue, formatter, locale, null);
			}
		} catch (ChartException e) {
			logger.log(e);
		}
		return "Y" + strValue + " S" + strSize; //$NON-NLS-1$//$NON-NLS-2$
	}

	@Override
	public boolean isValid() {
		if (!bIsBigNumber) {
			return getValue() != null && !Double.isNaN(dSize) && dSize != 0;
		}
		return bnSize != null && bnSize.doubleValue() != 0;
	}

	/**
	 * Checks if the value is big number.
	 *
	 * @return
	 */
	public boolean isBigNumber() {
		return bIsBigNumber;
	}

	/**
	 * Returns divisor of big number.
	 *
	 * @return
	 */
	public BigDecimal getDivisor() {
		return divisor;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.datafeed.NumberDataPointEntry#setNumberData(java.lang.
	 * Number[])
	 */
	@Override
	public void setNumberData(Number[] data) {
		if (data == null || data.length < 2) {
			return;
		}

		init(data[0], data[1]);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.datafeed.NumberDataPointEntry#getNumberData()
	 */
	@Override
	public Number[] getNumberData() {
		if (bIsBigNumber) {
			return new BigNumber[] { (BigNumber) oValue, bnSize };
		} else if (bIsBigDecimal) {
			if (oValue instanceof BigDecimal) {
				return new BigDecimal[] { (BigDecimal) oValue, (BigDecimal) bdSize };
			}
			return new java.math.BigDecimal[] { (java.math.BigDecimal) oValue, (java.math.BigDecimal) bdSize };
		}

		if (oValue instanceof Number) {
			return new Double[] { ((Number) oValue).doubleValue(), dSize };
		}
		return null;
	}
}

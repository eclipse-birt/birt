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

package org.eclipse.birt.chart.extension.datafeed;

import org.eclipse.birt.chart.computation.ValueFormatter;
import org.eclipse.birt.chart.datafeed.NumberDataPointEntry;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.util.BigNumber;
import org.eclipse.birt.chart.util.NumberUtil;

import com.ibm.icu.math.BigDecimal;
import com.ibm.icu.util.ULocale;

/**
 * DifferenceEntry
 */
public final class DifferenceEntry extends NumberDataPointEntry {

	private double dPosValue;

	private double dNegValue;

	private BigNumber bnPosValue;

	private BigNumber bnNegValue;

	private boolean isBigNumber = false;

	private BigDecimal divisor;

	private boolean isBigDecimal = false;

	private Number bdPosValue;

	private Number bdNegValue;;

	/**
	 * The constructor.
	 */
	public DifferenceEntry(double dPositiveValue, double dNegativeValue) {
		this.dPosValue = dPositiveValue;
		this.dNegValue = dNegativeValue;
	}

	/**
	 * The constructor.
	 * 
	 * @param oaTwoComponents
	 */
	public DifferenceEntry(Object[] oaTwoComponents) {
		assert oaTwoComponents.length == 2;

		if (oaTwoComponents[0] instanceof BigNumber) {
			init((BigNumber) oaTwoComponents[0], (BigNumber) oaTwoComponents[1]);
		} else if (NumberUtil.isBigDecimal(oaTwoComponents[0]) || NumberUtil.isBigDecimal(oaTwoComponents[1])) {
			isBigDecimal = true;
			init((Number) oaTwoComponents[0], (Number) oaTwoComponents[1]);
		} else {
			init(oaTwoComponents);
		}
	}

	protected void init(Object[] oaTwoComponents) {
		isBigNumber = false;
		this.dPosValue = (oaTwoComponents[0] instanceof Number) ? ((Number) oaTwoComponents[0]).doubleValue()
				: Double.NaN;
		this.dNegValue = (oaTwoComponents[1] instanceof Number) ? ((Number) oaTwoComponents[1]).doubleValue()
				: Double.NaN;
	}

	protected void init(BigNumber bnPositiveValue, BigNumber bnNegativeValue) {
		isBigNumber = true;
		this.bnPosValue = bnPositiveValue;
		this.bnNegValue = bnNegativeValue;
		divisor = null;
		if (bnPosValue != null) {
			divisor = bnPosValue.getDivisor();
			dPosValue = bnPosValue.doubleValue();
		} else {
			dPosValue = 0;
		}
		if (bnNegValue != null) {
			if (divisor == null) {
				divisor = bnNegValue.getDivisor();
			}
			dNegValue = bnNegValue.doubleValue();
		} else {
			dNegValue = 0;
		}

	}

	protected void init(Number bdPositiveValue, Number bdNegativeValue) {
		if (isBigDecimal) {
			if (NumberUtil.isJavaMathBigDecimal(bdPositiveValue)
					|| ((bdPositiveValue == null) && NumberUtil.isJavaMathBigDecimal(bdNegativeValue))) {
				bdPosValue = NumberUtil.asJavaMathBigDecimal(bdPositiveValue);
				bdNegValue = NumberUtil.asJavaMathBigDecimal(bdNegativeValue);
			} else {
				bdPosValue = NumberUtil.asBigDecimal(bdPositiveValue);
				bdNegValue = NumberUtil.asBigDecimal(bdNegativeValue);
			}
			dPosValue = (bdPosValue == null) ? 0 : bdPosValue.doubleValue();
			dNegValue = (bdNegValue == null) ? 0 : bdNegValue.doubleValue();
		}
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
	 * @return Returns the positive value.
	 */
	public final double getPositiveValue() {
		return dPosValue;
	}

	public final Number getPositiveValueNumber() {
		if (isBigNumber) {
			return bnPosValue;
		} else if (isBigDecimal) {
			return bdPosValue;
		}
		return Double.valueOf(dPosValue);
	}

	/**
	 * @param value The positive value to set.
	 */
	public final void setPositiveValue(double value) {
		this.dPosValue = value;
	}

	public final void setPositiveValue(Number value) {
		if (isBigNumber && value instanceof BigNumber) {
			bnPosValue = (BigNumber) value;
		}
		setPositiveValue(value.doubleValue());
	}

	/**
	 * @return Returns the negative value.
	 */
	public final double getNegativeValue() {
		return dNegValue;
	}

	public final Number getNegativeValueNumber() {
		if (isBigNumber) {
			return bnNegValue;
		} else if (isBigDecimal) {
			return bdNegValue;
		}
		return Double.valueOf(dNegValue);
	}

	/**
	 * @param end The negative value to set.
	 */
	public final void setNegativeValue(double value) {
		this.dNegValue = value;
	}

	public final void setNegativeValue(Number value) {
		if (isBigNumber && value instanceof BigNumber) {
			bnNegValue = (BigNumber) value;
		}
		setNegativeValue(value.doubleValue());
	}

	public String getFormattedString(String type, FormatSpecifier formatter, ULocale locale) {
		String str = "";//$NON-NLS-1$
		try {

			if (DifferenceDataPointDefinition.TYPE_POSITIVE_VALUE.equals(type)) {
				Number posValue = isBigNumber ? bnPosValue : Double.valueOf(dPosValue);
				str = ValueFormatter.format(posValue, formatter, locale, null);
			} else if (DifferenceDataPointDefinition.TYPE_NEGATIVE_VALUE.equals(type)) {
				Number negValue = isBigNumber ? bnNegValue : Double.valueOf(dNegValue);
				str = ValueFormatter.format(negValue, formatter, locale, null);
			}
		} catch (ChartException e) {
			Logger.getLogger("org.eclipse.birt.chart.engine/exception") //$NON-NLS-1$
					.log(e);
		}
		return str;
	}

	public String getFormattedString(FormatSpecifier formatter, ULocale locale) {
		String strPos = getFormattedString(DifferenceDataPointDefinition.TYPE_POSITIVE_VALUE, formatter, locale);
		String strNeg = getFormattedString(DifferenceDataPointDefinition.TYPE_NEGATIVE_VALUE, formatter, locale);
		return "P " + strPos + "; N " + strNeg; //$NON-NLS-1$//$NON-NLS-2$
	}

	public boolean isValid() {
		if (isBigNumber) {
			return true;
		}

		return !Double.isNaN(dNegValue) && !Double.isNaN(dPosValue);
	}

	/**
	 * Checks if values are big number.
	 * 
	 * @return
	 */
	public boolean isBigNumber() {
		return isBigNumber;
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

		if (data instanceof BigNumber[]) {
			init(((BigNumber[]) data)[0], ((BigNumber[]) data)[1]);
		} else {
			init(data);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.datafeed.NumberDataPointEntry#getNumberData()
	 */
	@Override
	public Number[] getNumberData() {
		if (isBigNumber) {
			return new BigNumber[] { bnPosValue, bnNegValue };
		} else if (isBigDecimal) {
			if (bdPosValue instanceof BigDecimal) {
				return new BigDecimal[] { (BigDecimal) bdPosValue, (BigDecimal) bdNegValue };
			}
			return new java.math.BigDecimal[] { (java.math.BigDecimal) bdPosValue, (java.math.BigDecimal) bdNegValue };
		}
		return new Double[] { Double.valueOf(dPosValue), Double.valueOf(dNegValue) };
	}
}

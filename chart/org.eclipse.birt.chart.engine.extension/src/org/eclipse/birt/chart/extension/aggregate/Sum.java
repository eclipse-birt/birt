/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/
/**
 *
 */

package org.eclipse.birt.chart.extension.aggregate;

import java.math.BigDecimal;

import org.eclipse.birt.chart.aggregate.AggregateFunctionAdapter;
import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.core.data.DataType;

/**
 *
 */
public class Sum extends AggregateFunctionAdapter {

	/**
	 *
	 */
	private Object oSum = null;

	/**
	 * A zero-arg public constructor is needed
	 */
	public Sum() {

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.aggregate.IAggregateFunction#reset()
	 */
	@Override
	public void initialize() {
		super.initialize();
		oSum = null; // LAZY INITIALIZATION
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.aggregate.IAggregateFunction#accumulate(java.lang.
	 * Object)
	 */
	@Override
	public void accumulate(Object oValue) throws IllegalArgumentException {
		if (oValue == null) {
			return;
		}

		super.accumulate(oValue);

		if (getDataType() != UNKNOWN && getDataType() != NUMBER && getDataType() != BIGDECIMAL) {
			throw new IllegalArgumentException(Messages.getString("exception.unsupported.aggregate.function.input", //$NON-NLS-1$
					getClass().getName(), getLocale())); // i18n_CONCATENATIONS_REMOVED
		}

		switch (getDataType()) {
		case NUMBER:
			if (oSum == null) {
				oSum = new double[1]; // SO WE CAN UPDATE THE PRIMITIVE
										// REFERENCE
				((double[]) oSum)[0] = 0;
			}
			((double[]) oSum)[0] += ((Number) oValue).doubleValue();
			break;

		case BIGDECIMAL:
			if (oSum == null) {
				oSum = new BigDecimal(0);
			}
			oSum = ((BigDecimal) oSum).add((BigDecimal) oSum);
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.aggregate.IAggregateFunction#getAggregatedValue()
	 */
	@Override
	public Object getAggregatedValue() {
		switch (getDataType()) {
		case NUMBER:
			return new Double(((double[]) oSum)[0]);

		default:
			return oSum;
		}
	}

	@Override
	public int getBIRTDataType() {
		return DataType.DOUBLE_TYPE;
	}

}

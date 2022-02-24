/***********************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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

package org.eclipse.birt.chart.extension.aggregate;

import java.math.BigDecimal;

import org.eclipse.birt.chart.aggregate.AggregateFunctionAdapter;
import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.core.data.DataType;

/**
 *  
 */
public class Average extends AggregateFunctionAdapter {

	/**
	 *  
	 */
	private Object oSum = null;

	/**
	 *  
	 */
	private int iIterationCount = 0;

	/**
	 * A zero-arg public constructor is needed
	 */
	public Average() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.aggregate.IAggregateFunction#reset()
	 */
	public void initialize() {
		super.initialize();
		oSum = null; // LAZY INITIALIZATION
		iIterationCount = 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.aggregate.IAggregateFunction#accumulate(java.lang.
	 * Object)
	 */
	public void accumulate(Object oValue) throws IllegalArgumentException {
		super.accumulate(oValue);
		// Fixed bugzilla bug 193263
		if (oValue != null) {
			iIterationCount++;
		}

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
	public Object getAggregatedValue() {
		switch (getDataType()) {
		case NUMBER:
			return new Double(((double[]) oSum)[0] / iIterationCount);

		case BIGDECIMAL:
			return ((BigDecimal) oSum).divide(new BigDecimal(iIterationCount), BigDecimal.ROUND_UNNECESSARY);

		default:
			return null; // THIS CONDITION SHOULD NEVER ARISE
		}
	}

	@Override
	public int getBIRTDataType() {
		return DataType.DOUBLE_TYPE;
	}

}

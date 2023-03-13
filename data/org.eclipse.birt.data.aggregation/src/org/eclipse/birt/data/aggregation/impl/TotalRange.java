/**
 *************************************************************************
 * Copyright (c) 2004, 2016 Actuate Corporation.
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
 *
 *************************************************************************
 */

package org.eclipse.birt.data.aggregation.impl;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.data.aggregation.api.IBuildInAggregation;
import org.eclipse.birt.data.aggregation.calculator.CalculatorFactory;
import org.eclipse.birt.data.aggregation.calculator.ICalculator;
import org.eclipse.birt.data.aggregation.i18n.Messages;
import org.eclipse.birt.data.engine.api.aggregation.Accumulator;
import org.eclipse.birt.data.engine.api.aggregation.IParameterDefn;
import org.eclipse.birt.data.engine.core.DataException;

/**
 *
 * Implements the built-in Total.Range aggregation
 */
public class TotalRange extends AggrFunction {

	@Override
	public String getName() {
		return IBuildInAggregation.TOTAL_RANGE_FUNC;
	}

	@Override
	public int getType() {
		return SUMMARY_AGGR;
	}

	@Override
	public int getDataType() {
		return DataType.DOUBLE_TYPE;
	}

	@Override
	public IParameterDefn[] getParameterDefn() {
		return new IParameterDefn[] { new ParameterDefn(Constants.EXPRESSION_NAME, Constants.EXPRESSION_DISPLAY_NAME,
				false, true, SupportedDataTypes.CALCULATABLE, "")//$NON-NLS-1$
		};
	}

	@Override
	public Accumulator newAccumulator() {
		return new MyAccumulator(CalculatorFactory.getCalculator(getDataType()));
	}

	private static class MyAccumulator extends SummaryAccumulator {

		private Object max = null;
		private Object min = null;

		private boolean isRowAvailable = false;

		MyAccumulator(ICalculator calc) {
			super(calc);
		}

		@Override
		public void start() {
			super.start();
			max = null;
			min = null;
			isRowAvailable = false;
		}

		@Override
		public void onRow(Object[] args) {
			assert (args.length > 0);
			if (args[0] != null) {
				if (!isRowAvailable) {
					isRowAvailable = true;
					max = args[0];
					min = max;
					return;
				}
				if (isGreaterThan(args[0], max)) {
					max = args[0];
				} else if (isLessThan(args[0], min)) {
					min = args[0];
				}
			}
		}

		@Override
		public Object getSummaryValue() throws DataException {
			// Null data returns null
			if (max == null || min == null) {
				return null;
			}
			if (max instanceof Number && min instanceof Number) {
				return calculator.subtract(calculator.getTypedObject(max), calculator.getTypedObject(min));
			}
			// Non numeric data returns 0
			return 0d;
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		private boolean isGreaterThan(Object origin, Object target) {
			if ((origin instanceof Comparable) && (target instanceof Comparable)) {
				return ((Comparable) origin).compareTo(target) > 0;
			} else {
				throw new RuntimeException(Messages.getString("TotalMax.exception.cannot_get_max_value")); //$NON-NLS-1$
			}
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		private boolean isLessThan(Object origin, Object target) {
			if ((origin instanceof Comparable) && (target instanceof Comparable)) {
				return ((Comparable) origin).compareTo(target) < 0;
			} else {
				throw new RuntimeException(Messages.getString("TotalMin.exception.cannot_get_min_value")); //$NON-NLS-1$
			}
		}
	}

	@Override
	public String getDescription() {
		return Messages.getString("TotalRange.description"); //$NON-NLS-1$
	}

	@Override
	public String getDisplayName() {
		return Messages.getString("TotalRange.displayName"); //$NON-NLS-1$
	}
}

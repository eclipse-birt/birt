/*
 *************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
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

package org.eclipse.birt.data.aggregation.impl.rank;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.data.aggregation.api.IBuildInAggregation;
import org.eclipse.birt.data.aggregation.i18n.Messages;
import org.eclipse.birt.data.aggregation.impl.AggrFunction;
import org.eclipse.birt.data.aggregation.impl.Constants;
import org.eclipse.birt.data.aggregation.impl.ParameterDefn;
import org.eclipse.birt.data.aggregation.impl.RunningAccumulator;
import org.eclipse.birt.data.aggregation.impl.SupportedDataTypes;
import org.eclipse.birt.data.engine.api.aggregation.Accumulator;
import org.eclipse.birt.data.engine.api.aggregation.IParameterDefn;
import org.eclipse.birt.data.engine.core.DataException;

/**
 * Implements the built-in Total.Rank aggregation.
 */
public class TotalPercentRank extends AggrFunction {

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.aggregation.Aggregation#getName()
	 */
	@Override
	public String getName() {
		return IBuildInAggregation.TOTAL_PERCENT_RANK_FUNC;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.aggregation.Aggregation#getType()
	 */
	@Override
	public int getType() {
		return RUNNING_AGGR;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.aggregation.IAggregation#getDateType()
	 */
	@Override
	public int getDataType() {
		return DataType.DOUBLE_TYPE;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.aggregation.Aggregation#getParameterDefn()
	 */
	@Override
	public IParameterDefn[] getParameterDefn() {
		return new IParameterDefn[] { new ParameterDefn(Constants.EXPRESSION_NAME, Constants.EXPRESSION_DISPLAY_NAME,
				false, true, SupportedDataTypes.CALCULATABLE, "") //$NON-NLS-1$
		};
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.aggregation.MultipassAggregation#
	 * getNumberOfPasses()
	 */
	@Override
	public int getNumberOfPasses() {
		return 2;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.aggregation.Aggregation#newAccumulator()
	 */
	@Override
	public Accumulator newAccumulator() {
		return new MyAccumulator();
	}

	private static class MyAccumulator extends RunningAccumulator {

		private Double sum;
		private List cachedValues;
		private int passCount = 0;
		private Object[] sortedObjs;

		@Override
		public void start() {
			if (passCount == 0) {
				cachedValues = new ArrayList();
				sum = new Double(0);
			}
			passCount++;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.eclipse.birt.data.engine.aggregation.Accumulator#onRow(java.lang.Object[]
		 * )
		 */
		@Override
		public void onRow(Object[] args) throws DataException {
			assert (args.length > 0);
			if (passCount == 1) {
				if (args[0] != null) {
					cachedValues.add(args[0]);
				} else {
					cachedValues.add(RankAggregationUtil.getNullObject());
				}
			} else {
				Object compareValue;
				if (args[0] != null) {
					compareValue = args[0];
				} else {
					compareValue = RankAggregationUtil.getNullObject();
				}
				sum = new Double(getPercentRank(compareValue, sortedObjs));
			}
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.birt.data.engine.api.aggregation.Accumulator#finish()
		 */
		@Override
		public void finish() throws DataException {
			if (this.passCount == 1) {
				sortedObjs = this.cachedValues.toArray();
				RankAggregationUtil.sortArray(sortedObjs);
			}

		}

		/**
		 *
		 * @param o
		 * @param objs
		 * @return
		 */
		private double getPercentRank(Object o, Object[] objs) {
			double smaller = -1;

			for (int i = 0; i < objs.length; i++) {
				if (o.equals(objs[i])) {
					// first time meet
					if (smaller == -1) {
						smaller = i;
					}
				}
			}
			if (smaller == -1) {
				return 0;
			}

			// return same result with Excel for this special case
			if (objs.length == 1) {
				return 1;
			}

			double result = smaller / (objs.length - 1);

			return result;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.birt.data.engine.api.aggregation.Accumulator#getValue()
		 */
		@Override
		public Object getValue() throws DataException {
			return sum;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.api.aggregation.IAggrFunction#getDescription()
	 */
	@Override
	public String getDescription() {
		return Messages.getString("TotalPercentRank.description"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.api.aggregation.IAggrFunction#getDisplayName()
	 */
	@Override
	public String getDisplayName() {
		return Messages.getString("TotalPercentRank.displayName"); //$NON-NLS-1$
	}
}

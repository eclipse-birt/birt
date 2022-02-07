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
import java.util.Collections;
import java.util.Comparator;
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
public class TotalRank extends AggrFunction {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.aggregation.Aggregation#getName()
	 */
	public String getName() {
		return IBuildInAggregation.TOTAL_RANK_FUNC;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.aggregation.Aggregation#getType()
	 */
	public int getType() {
		return RUNNING_AGGR;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.aggregation.IAggregation#getDateType()
	 */
	public int getDataType() {
		return DataType.INTEGER_TYPE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.aggregation.Aggregation#getParameterDefn()
	 */
	public IParameterDefn[] getParameterDefn() {
		return new IParameterDefn[] {
				new ParameterDefn(Constants.EXPRESSION_NAME, Constants.EXPRESSION_DISPLAY_NAME, false, true,
						SupportedDataTypes.CALCULATABLE, ""), //$NON-NLS-1$
				new ParameterDefn("ascending", Messages.getString("TotalRank.param.ascending"), true, false, //$NON-NLS-1$ //$NON-NLS-2$
						SupportedDataTypes.ANY, "") //$NON-NLS-1$
		};
	}

	/*
	 * 
	 */
	public int getNumberOfPasses() {
		return 2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.aggregation.Aggregation#newAccumulator()
	 */
	public Accumulator newAccumulator() {
		return new MyAccumulator();
	}

	private class MyAccumulator extends RunningAccumulator {

		private Integer sum;
		private List cachedValues;
		private boolean asc;
		private boolean hasInitialized;
		private int passCount = 0;
		private RankObjComparator comparator;

		public void start() {
			if (passCount == 0) {
				cachedValues = new ArrayList();
				sum = Integer.valueOf(0);
				asc = true;
				hasInitialized = false;
				comparator = new RankObjComparator();
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
		public void onRow(Object[] args) throws DataException {
			assert (args.length > 0);
			if (passCount == 1) {
				if (args[0] != null) {
					cachedValues.add(args[0]);
				} else {
					cachedValues.add(RankAggregationUtil.getNullObject());
				}
				if ((!hasInitialized) && args[1] != null) {
					hasInitialized = true;
					if (args[1].toString().equals("false")) //$NON-NLS-1$
						asc = false;
					else if (args[1] instanceof Double && ((Double) args[1]).equals(new Double(0))) {
						asc = false;
					} else
						asc = true;
				}
			} else {
				Object compareValue;
				if (args[0] != null) {
					compareValue = args[0];
				} else {
					compareValue = RankAggregationUtil.getNullObject();
				}
				sum = Integer.valueOf(getRank(compareValue));
			}
		}

		public void finish() throws DataException {
			if (this.passCount == 1) {
				Collections.sort(cachedValues, comparator);
			}
		}

		/**
		 * Precondition: The parameter <code>objs</code> should be sorted acsending
		 * previously. Note: rank is 1-based. ex.
		 * <code>The following table give details:
		 *	Value   |    Rank
		 *	20      |     4 
		 *	10      |     5
		 *	30      |     2
		 *	30      |     2
		 *	40      |     1
		 * </code>
		 * 
		 * @param key
		 * @return rank
		 */
		private int getRank(Object key) {
			// search the index of key in the cachedValues list using
			// binary search algorithm
			int index = Collections.binarySearch(cachedValues, key, comparator);
			if (index < 0) {// not found, return default rank: -1
				return -1;
			}
			// Note:index is 0-based, but rank is 1-based
			if (asc) {// caculate the rank in ascending order
				for (int i = index - 1; i >= 0; i--) {
					if (cachedValues.get(i).equals(key) == false) {
						return i + 2;
					}
				}
			} else {// caculate the rank in descending order
				for (int i = index + 1; i < cachedValues.size(); i++) {
					if (cachedValues.get(i).equals(key) == false) {
						return cachedValues.size() - i + 1;
					}
				}
			}
			return 1;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.data.engine.api.aggregation.Accumulator#getValue()
		 */
		public Object getValue() throws DataException {
			return sum;
		}
	}

	/*
	 * 
	 */
	static class RankObjComparator implements Comparator {

		public int compare(Object o1, Object o2) {// for efficiency, we assure o1 and o2 can be just Comparable or
													// NullObject
			if (o1 instanceof Comparable) {
				if (o2 instanceof Comparable) {// Comparable ? Comparable
					Comparable obj1 = (Comparable) o1;
					Comparable obj2 = (Comparable) o2;
					return obj1.compareTo(obj2);
				} else {// Comparable > NullObject
					return 1;
				}
			} else {
				if (o2 instanceof Comparable) {// NullObject < Comparable
					return -1;
				} else {// NullObject == NullObject
					return 0;
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.api.aggregation.IAggrFunction#getDescription()
	 */
	public String getDescription() {
		return Messages.getString("TotalRank.description"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.api.aggregation.IAggrFunction#getDisplayName()
	 */
	public String getDisplayName() {
		return Messages.getString("TotalRank.displayName"); //$NON-NLS-1$
	}
}

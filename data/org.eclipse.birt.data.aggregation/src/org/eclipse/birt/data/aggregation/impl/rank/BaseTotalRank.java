package org.eclipse.birt.data.aggregation.impl.rank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.data.aggregation.i18n.Messages;
import org.eclipse.birt.data.aggregation.impl.AggrFunction;
import org.eclipse.birt.data.aggregation.impl.Constants;
import org.eclipse.birt.data.aggregation.impl.ParameterDefn;
import org.eclipse.birt.data.aggregation.impl.RunningAccumulator;
import org.eclipse.birt.data.aggregation.impl.SupportedDataTypes;
import org.eclipse.birt.data.engine.api.aggregation.IParameterDefn;
import org.eclipse.birt.data.engine.core.DataException;

/**
 * @since 3.3
 *
 */
abstract class BaseTotalRank extends AggrFunction {


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
		return DataType.INTEGER_TYPE;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.aggregation.Aggregation#getParameterDefn()
	 */
	@Override
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
	@Override
	public int getNumberOfPasses() {
		return 2;
	}

	static class TotalRankAccumulator extends RunningAccumulator {

		private Integer sum;
		private List<Object> cachedValues;
		private Map<Object, Integer> rankMap;
		private boolean asc;
		private boolean denseRank;
		private boolean hasInitialized;
		private int passCount = 0;
		private Comparator comparator;

		/**
		 * @param denseRank If the dense rank algorithm should be used or not
		 *
		 */
		public TotalRankAccumulator(boolean denseRank) {
			this.denseRank = denseRank;
		}

		@Override
		public void start() {
			if (passCount == 0) {
				cachedValues = new ArrayList<>();
				rankMap = new HashMap<>();
				sum = 0;
				asc = true;
				hasInitialized = false;
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
				if ((!hasInitialized) && args[1] != null) {
					hasInitialized = true;
					if (args[1].toString().equals("false")) { //$NON-NLS-1$
						asc = false;
					} else if (args[1] instanceof Double doubleArg && doubleArg.equals(Double.valueOf(0))) {
						asc = false;
					} else {
						asc = true;
					}

					comparator = this.asc ? RankObjComparator.INSTANCE : RankObjComparator.INSTANCE.reversed();
				}
			} else {
				Object compareValue;
				if (args[0] != null) {
					compareValue = args[0];
				} else {
					compareValue = RankAggregationUtil.getNullObject();
				}

				Integer calculatedRank = this.rankMap.get(compareValue);
				sum = calculatedRank != null ? calculatedRank.intValue() : -1;
			}
		}

		@Override
		public void finish() throws DataException {
			if (this.passCount == 1) {
				Collections.sort(cachedValues, comparator);
				calculateRank(cachedValues, this.denseRank);
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
		private void calculateRank(List<Object> sortedList, boolean useDenseRank) {

			int currentRank = 1;
			int currentDenseRank = 1;
			Object currentValue = sortedList.get(0);

			for (int i = 0; i < sortedList.size(); i++) {
				Object integer = sortedList.get(i);
				if (!Objects.equals(integer, currentValue)) {

					rankMap.put(currentValue, useDenseRank ? currentDenseRank : currentRank);
					currentValue = integer;
					currentDenseRank++;
					currentRank = i + 1;
				}

			}

			// Handle the last integer in the list
			rankMap.put(currentValue, useDenseRank ? currentDenseRank : currentRank);
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
	 *
	 */
	static class RankObjComparator implements Comparator {

		public static RankObjComparator INSTANCE = new RankObjComparator();

		private RankObjComparator() {
			// No need
		}

		@Override
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
			} else if (o2 instanceof Comparable) {// NullObject < Comparable
				return -1;
			} else {// NullObject == NullObject
				return 0;
			}
		}
	}
}

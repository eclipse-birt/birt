
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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
package org.eclipse.birt.data.engine.olap.data.impl.aggregation;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.eclipse.birt.data.engine.aggregation.AggregationUtil;
import org.eclipse.birt.data.engine.api.aggregation.Accumulator;
import org.eclipse.birt.data.engine.api.aggregation.AggregationManager;
import org.eclipse.birt.data.engine.api.aggregation.IAggrFunction;
import org.eclipse.birt.data.engine.api.timefunction.IParallelPeriod;
import org.eclipse.birt.data.engine.api.timefunction.ITimeFunction;
import org.eclipse.birt.data.engine.api.timefunction.ReferenceDate;
import org.eclipse.birt.data.engine.api.timefunction.TimePeriodType;
import org.eclipse.birt.data.engine.api.timefunction.IPeriodsFunction;
import org.eclipse.birt.data.engine.api.timefunction.TimeMember;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.cache.SizeOfUtil;
import org.eclipse.birt.data.engine.i18n.DataResourceHandle;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.olap.data.api.DimLevel;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultRow;
import org.eclipse.birt.data.engine.olap.data.api.MeasureInfo;
import org.eclipse.birt.data.engine.olap.data.api.cube.IDimension;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationFunctionDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.DimColumn;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.function.AbstractMDX;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.function.TimeFunctionFactory;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.function.TimeMemberUtil;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Member;
import org.eclipse.birt.data.engine.olap.data.util.BufferedStructureArray;
import org.eclipse.birt.data.engine.olap.data.util.IDiskArray;
import org.eclipse.birt.data.engine.olap.util.filter.IJSFacttableFilterEvalHelper;

/**
 * The AggregationCalculator class calculates values for its associated
 * Aggregation.
 */

public class AggregationCalculator {
	AggregationDefinition aggregation;
	private Accumulator[] accumulators;
	private Set<TimeMember>[] timeFunctionFilter;
	private int[] timeFilterDimensionIndex;
	private int[] timeFilterLevelCount;
	private int levelCount;
	private int[] measureIndexes;
	private MeasureInfo[] measureInfos;
	private IDiskArray result = null;
	private IAggregationResultRow currentResultObj = null;
	private int[] parameterColIndex;
	private FacttableRow facttableRow;
	private ICubeDimensionReader cubeDimensionReader;
	private static Logger logger = Logger.getLogger(AggregationCalculator.class.getName());

	/**
	 * 
	 * @param aggregationDef
	 * @param facttableRowIterator
	 * @throws DataException
	 */
	AggregationCalculator(AggregationDefinition aggregationDef, DimColumn[] parameterColNames, // the parameter sequence
																								// corresponding with
																								// <code>Row4Aggregation.getParameterValues()</code>
			IDataSet4Aggregation.MetaInfo metaInfo, ICubeDimensionReader cubeDimensionReader, long memoryCacheSize)
			throws IOException, DataException {
		Object[] params = { aggregationDef, parameterColNames, metaInfo };
		logger.entering(AggregationCalculator.class.getName(), "AggregationCalculator", params);
		this.aggregation = aggregationDef;
		AggregationFunctionDefinition[] aggregationFunction = aggregationDef.getAggregationFunctions();
		if (aggregationDef.getLevels() == null)
			this.levelCount = 0;
		else
			this.levelCount = aggregationDef.getLevels().length;
		if (aggregationFunction != null) {
			this.accumulators = new Accumulator[aggregationFunction.length];
			this.timeFunctionFilter = new Set[aggregationFunction.length];
			this.timeFilterDimensionIndex = new int[aggregationFunction.length];
			this.timeFilterLevelCount = new int[aggregationFunction.length];
			this.measureIndexes = new int[aggregationFunction.length];
			this.parameterColIndex = new int[aggregationFunction.length];

			for (int i = 0; i < aggregationFunction.length; i++) {
				IAggrFunction aggregation = AggregationManager.getInstance()
						.getAggregation(aggregationFunction[i].getFunctionName());
				if (aggregationFunction[i].getTimeFunctionFilter() != null) {
					String tDimName = aggregationFunction[i].getTimeFunctionFilter().getTimeDimension();
					IDimension timeDimension = cubeDimensionReader.getDimension(tDimName);
					this.timeFunctionFilter[i] = getTimeFunctinResult(timeDimension,
							aggregationFunction[i].getTimeFunctionFilter());
					this.timeFilterDimensionIndex[i] = cubeDimensionReader.getDimensionIndex(tDimName);
					this.timeFilterLevelCount[i] = cubeDimensionReader.getlowestLevelIndex(tDimName) - 1;
				}
				if (aggregation == null) {
					throw new DataException(
							DataResourceHandle.getInstance().getMessage(ResourceConstants.UNSUPPORTED_FUNCTION)
									+ aggregationFunction[i].getFunctionName());
				}
				if (AggregationUtil.needDataField(aggregation)) {
					this.parameterColIndex[i] = find(parameterColNames, aggregationFunction[i].getParaCol());
				} else {
					this.parameterColIndex[i] = -1;
				}
				this.accumulators[i] = aggregation.newAccumulator();
				this.accumulators[i].start();
				final String measureName = aggregationFunction[i].getMeasureName();
				this.measureIndexes[i] = metaInfo.getMeasureIndex(measureName);

				if (this.measureIndexes[i] == -1 && measureName != null) {
					throw new DataException(ResourceConstants.MEASURE_NAME_NOT_FOUND, measureName);
				}
			}
		}
		int levelSize = 0;
		if (levelCount != 0) {
			levelSize = getLevelSize(metaInfo, aggregationDef.getLevels());
		}
		int measureSize = 0;
		if (aggregationFunction != null && aggregationFunction.length > 0) {
			measureSize = aggregationFunction.length * 64;
		}
		int rowSize = 16 + (4 + (levelSize + measureSize) - 1) / 8 * 8;
		int bufferSize = (int) (memoryCacheSize / rowSize);
		if (bufferSize != 0)
			result = new BufferedStructureArray(AggregationResultRow.getCreator(), bufferSize);
		else {
			result = new BufferedStructureArray(AggregationResultRow.getCreator(), 1000);
			((BufferedStructureArray) result).setUseMemoryOnly(true);
		}
		measureInfos = metaInfo.getMeasureInfos();
		facttableRow = new FacttableRow(measureInfos, cubeDimensionReader, metaInfo);
		this.cubeDimensionReader = cubeDimensionReader;

		logger.exiting(AggregationCalculator.class.getName(), "AggregationCalculator");
	}

	private static Set<TimeMember> getTimeFunctinResult(IDimension timeDimension, ITimeFunction function)
			throws DataException {
		Set<TimeMember> set = new HashSet<TimeMember>();

		IPeriodsFunction periodsFunction = createTimeFunction(function);
		TimeMember member = TimeMemberUtil.toMember(timeDimension, function.getReferenceDate().getDate());
		List<TimeMember> result = periodsFunction.getResult(member);
		for (int i = 0; i < result.size(); i++) {
			set.add(result.get(i));
		}
		return set;
	}

	private static IPeriodsFunction createTimeFunction(ITimeFunction function) throws DataException {
		IPeriodsFunction periodsFunction = null;
		String toDatelevelType = null;
		String paralevelType = null;

		toDatelevelType = toLevelType(function.getBaseTimePeriod().getType());
		if (function.getBaseTimePeriod().countOfUnit() == 0) {
			periodsFunction = TimeFunctionFactory.createPeriodsToDateFunction(toDatelevelType,
					function.getBaseTimePeriod().isCurrent());
		} else {
			periodsFunction = TimeFunctionFactory.createTrailingFunction(toDatelevelType,
					function.getBaseTimePeriod().countOfUnit());
		}
		((AbstractMDX) periodsFunction).setReferenceDate((ReferenceDate) function.getReferenceDate());

		if (function.getRelativeTimePeriod() != null) {
			paralevelType = toLevelType(function.getRelativeTimePeriod().getType());
			IParallelPeriod parallelPeriod = TimeFunctionFactory.createParallelPeriodFunction(paralevelType,
					function.getRelativeTimePeriod().countOfUnit());
			((AbstractMDX) parallelPeriod).setReferenceDate((ReferenceDate) function.getReferenceDate());

			periodsFunction = new PeriodsToDateWithParallel(parallelPeriod, periodsFunction);
		}

		return periodsFunction;
	}

	private static String toLevelType(TimePeriodType timePeriodType) {
		if (timePeriodType == TimePeriodType.YEAR) {
			return TimeMember.TIME_LEVEL_TYPE_YEAR;
		} else if (timePeriodType == TimePeriodType.QUARTER) {
			return TimeMember.TIME_LEVEL_TYPE_QUARTER;
		} else if (timePeriodType == TimePeriodType.MONTH) {
			return TimeMember.TIME_LEVEL_TYPE_MONTH;
		} else if (timePeriodType == TimePeriodType.WEEK) {
			return TimeMember.TIME_LEVEL_TYPE_WEEK_OF_YEAR;
		} else if (timePeriodType == TimePeriodType.DAY) {
			return TimeMember.TIME_LEVEL_TYPE_DAY_OF_MONTH;
		}
		return null;
	}

	private int getLevelSize(IDataSet4Aggregation.MetaInfo metaInfo, DimLevel[] dimLevel) throws DataException {
		if (dimLevel == null || dimLevel.length == 0) {
			return 0;
		}
		int[] dataType = new int[dimLevel.length];
		for (int i = 0; i < dimLevel.length; i++) {
			DimColumn dimColumn = null;
			if (dimLevel[i].getAttrName() == null)
				dimColumn = new DimColumn(dimLevel[i].getDimensionName(), dimLevel[i].getLevelName(),
						dimLevel[i].getLevelName());
			else
				dimColumn = new DimColumn(dimLevel[i].getDimensionName(), dimLevel[i].getLevelName(),
						dimLevel[i].getAttrName());
			ColumnInfo columnInfo = metaInfo.getColumnInfo(dimColumn);
			dataType[i] = columnInfo.getDataType();
		}
		return SizeOfUtil.getObjectSize(dataType);
	}

	/**
	 * 
	 * @param colArray
	 * @param col
	 * @return
	 */
	private static int find(DimColumn[] colArray, DimColumn col) {
		if (colArray == null || col == null) {
			return -1;
		}
		for (int i = 0; i < colArray.length; i++) {
			if (col.equals(colArray[i])) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 
	 * @param row
	 * @throws IOException
	 * @throws DataException
	 */
	void onRow(Row4Aggregation row) throws IOException, DataException {
		if (currentResultObj == null) {
			newAggregationResultRow(row);
		} else {
			if (currentResultObj.getLevelMembers() == null
					|| compare(row.getLevelMembers(), currentResultObj.getLevelMembers()) == 0) {
				if (accumulators != null) {
					while (row.nextMeasures()) {
						for (int i = 0; i < accumulators.length; i++) {
							if (!getFilterResult(row, i)) {
								continue;
							}
							accumulators[i].onRow(getAccumulatorParameter(row, i));
						}
					}
					row.firstMeasure();
				}
			} else {
				if (accumulators != null) {
					currentResultObj.setAggregationValues(new Object[accumulators.length]);
					for (int i = 0; i < accumulators.length; i++) {
						accumulators[i].finish();
						currentResultObj.getAggregationValues()[i] = accumulators[i].getValue();
						accumulators[i].start();
					}
				}
				result.add(currentResultObj);
				newAggregationResultRow(row);
			}
		}
	}

	/**
	 * 
	 * @param row
	 * @param functionNo
	 * @return
	 * @throws DataException
	 * @throws IOException
	 */
	private boolean getFilterResult(Row4Aggregation row, int functionNo) throws DataException, IOException {
		boolean result = true;
		facttableRow.setDimPos(row.getDimPos());
		facttableRow.setMeasure(row.getMeasures());
		IJSFacttableFilterEvalHelper filterEvalHelper = (aggregation.getAggregationFunctions()[functionNo])
				.getFilterEvalHelper();
		if (filterEvalHelper != null) {
			result = filterEvalHelper.evaluateFilter(facttableRow);
		}
		if (!result)
			return result;
		if (this.timeFunctionFilter[functionNo] != null) {
			Member[] members = this.cubeDimensionReader.getLevelMembers(timeFilterDimensionIndex[functionNo],
					timeFilterLevelCount[functionNo], row.getDimPos()[timeFilterDimensionIndex[functionNo]]);
			int[] timeMember = new int[members.length];
			for (int i = 0; i < timeMember.length; i++) {
				timeMember[i] = ((Integer) (members[i].getKeyValues()[0])).intValue();
			}
			TimeMember tMember = new TimeMember(timeMember, null);
			if (this.timeFunctionFilter[functionNo].contains(tMember))
				return true;
			else
				return false;
		}
		return result;

	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 * @throws DataException
	 */
	IDiskArray getResult() throws IOException, DataException {
		if (currentResultObj != null && accumulators != null) {
			currentResultObj.setAggregationValues(new Object[accumulators.length]);
			for (int i = 0; i < accumulators.length; i++) {
				accumulators[i].finish();
				currentResultObj.getAggregationValues()[i] = accumulators[i].getValue();
				accumulators[i].start();
			}
		}
		if (currentResultObj != null)
			result.add(currentResultObj);
		/*
		 * else result.add( new AggregationResultRow( ) );
		 */
		return this.result;
	}

	/**
	 * 
	 * @param row
	 * @throws DataException
	 * @throws IOException
	 */
	private void newAggregationResultRow(Row4Aggregation row) throws DataException, IOException {
		currentResultObj = new AggregationResultRow();
		if (levelCount > 0) {
			currentResultObj.setLevelMembers(new Member[levelCount]);
			System.arraycopy(row.getLevelMembers(), 0, currentResultObj.getLevelMembers(), 0,
					currentResultObj.getLevelMembers().length);
		}
		if (accumulators != null) {
			while (row.nextMeasures()) {
				for (int i = 0; i < accumulators.length; i++) {
					if (!getFilterResult(row, i)) {
						continue;
					}
					accumulators[i].onRow(getAccumulatorParameter(row, i));
				}
			}
			row.firstMeasure();
		}
	}

	private Object[] getAccumulatorParameter(Row4Aggregation row, int funcIndex) {
		Object[] parameters = null;
		if (parameterColIndex[funcIndex] == -1) {
			parameters = new Object[1];
			if (measureIndexes[funcIndex] < 0) {
				return null;
			} else {
				parameters[0] = row.getMeasures()[measureIndexes[funcIndex]];
			}
		} else {
			parameters = new Object[2];
			if (measureIndexes[funcIndex] < 0) {
				parameters[0] = null;
			} else {
				parameters[0] = row.getMeasures()[measureIndexes[funcIndex]];
			}
			parameters[1] = row.getParameterValues()[parameterColIndex[funcIndex]];
		}
		return parameters;
	}

	/**
	 * 
	 * @param key1
	 * @param key2
	 * @return
	 */
	private int compare(Object[] key1, Object[] key2) {
		for (int i = 0; i < aggregation.getLevels().length; i++) {
			int result = ((Comparable) key1[i]).compareTo(key2[i]);
			if (result < 0) {
				return result;
			} else if (result > 0) {
				return result;
			}
		}
		return 0;
	}
}

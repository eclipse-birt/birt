package org.eclipse.birt.data.engine.olap.data.impl.aggregation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.data.engine.aggregation.AggregationUtil;
import org.eclipse.birt.data.engine.api.aggregation.Accumulator;
import org.eclipse.birt.data.engine.api.aggregation.AggregationManager;
import org.eclipse.birt.data.engine.api.aggregation.IAggrFunction;
import org.eclipse.birt.data.engine.api.timefunction.ITimeFunction;
import org.eclipse.birt.data.engine.api.timefunction.ReferenceDate;
import org.eclipse.birt.data.engine.api.timefunction.TimePeriodType;
import org.eclipse.birt.data.engine.api.timefunction.IParallelPeriod;
import org.eclipse.birt.data.engine.api.timefunction.IPeriodsFunction;
import org.eclipse.birt.data.engine.api.timefunction.TimeMember;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.cache.SizeOfUtil;
import org.eclipse.birt.data.engine.i18n.DataResourceHandle;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.olap.data.api.DimLevel;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultRow;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;
import org.eclipse.birt.data.engine.olap.data.api.IDimensionSortDefn;
import org.eclipse.birt.data.engine.olap.data.api.ILevel;
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
import org.eclipse.birt.data.engine.olap.data.util.DiskSortedStack;
import org.eclipse.birt.data.engine.olap.data.util.IComparableStructure;
import org.eclipse.birt.data.engine.olap.data.util.IStructure;
import org.eclipse.birt.data.engine.olap.data.util.IStructureCreator;
import org.eclipse.birt.data.engine.olap.data.util.ObjectArrayUtil;
import org.eclipse.birt.data.engine.olap.util.filter.IJSFacttableFilterEvalHelper;

public class TimeFunctionCalculator {
	AggregationDefinition aggregation;

	private int timeDimensionIndex = 0;
	private int endLevelIndex = 0;
	private int lowestTimeLevel = 0;
	private int firstTimeLevel = 0;
	private int newMemberSize = 0;

	private int[] measureIndexes;
	private MeasureInfo[] measureInfos;
	private int[] parameterColIndex;
	private FacttableRow facttableRow;

	private Accumulator[][] accumulators;
	private IAggrFunction[] aggregationFunction;
	private DiskSortedStack sortedFactRows;
	private BufferedStructureArray factRows;
	private int factRowPostion;
	private DiskSortedStack timeMemberFilters[];
	private boolean existTimeFunction;
	private ICubeDimensionReader cubeDimensionReader;
	private IPeriodsFunction[] periodFunction;
	private Map<TimeMember, List<TimeMember>>[] periodFunctionResultCache;
	private String tDimName;
	private IDimension timeDimension;
	private String[] levelType;
	private List<MemberCellIndex>[] currentFilterList;
	private List<Row4Aggregation> currentRowList;
	private MemberCellIndex[] currentFilter;
	private Row4Aggregation currentRow;
	private boolean existReferenceDate;
	private boolean existLastDate;
	private boolean avoidExtraSort;
	private Date[] referenceDate;
	private int orignalLevelCount;
	private int[] sortType;

	TimeFunctionCalculator(AggregationDefinition aggr, DimColumn[] parameterColNames,
			IDataSet4Aggregation.MetaInfo metaInfo, ICubeDimensionReader cubeDimensionReader, long memoryCacheSize)
			throws DataException, IOException {
		AggregationFunctionDefinition[] timeFunction = aggr.getAggregationTimeFunctions();
		if (timeFunction == null) {
			existTimeFunction = false;
			return;
		}
		existTimeFunction = true;
		tDimName = timeFunction[0].getTimeFunction().getTimeDimension();
		timeDimension = cubeDimensionReader.getDimension(tDimName);
		periodFunction = createTimeFunction(timeFunction);
		periodFunctionResultCache = new Map[periodFunction.length];
		for (int i = 0; i < periodFunctionResultCache.length; i++) {
			periodFunctionResultCache[i] = new HashMap<TimeMember, List<TimeMember>>();
		}
		timeDimensionIndex = cubeDimensionReader.getDimensionIndex(tDimName);
		lowestTimeLevel = getLowestTimeLevel(aggr);
		firstTimeLevel = getFirstTimeLevel(aggr);
		existReferenceDate = false;
		existLastDate = false;
		referenceDate = new Date[timeFunction.length];
		for (int i = 0; i < timeFunction.length; i++) {
			if (timeFunction[i].getTimeFunction().getReferenceDate() != null)
				referenceDate[i] = timeFunction[i].getTimeFunction().getReferenceDate().getDate();
			if (referenceDate[i] != null) {
				existReferenceDate = true;

				int levelIndex1 = cubeDimensionReader.getlowestLevelIndex(tDimName) - 1;
				int levelIndex2 = cubeDimensionReader.getLevelIndex(tDimName,
						aggr.getLevels()[lowestTimeLevel].getLevelName());
				if (levelIndex1 == levelIndex2) {
					this.avoidExtraSort = true;
				}
			} else {
				existLastDate = true;
			}
		}
		if (existReferenceDate) {
			if (cubeDimensionReader.getlowestLevelIndex(tDimName) == 0)
				endLevelIndex = 0;
			else
				endLevelIndex = cubeDimensionReader.getlowestLevelIndex(tDimName) - 1;
		} else {
			endLevelIndex = cubeDimensionReader.getLevelIndex(tDimName,
					aggr.getLevels()[lowestTimeLevel].getLevelName());
		}
		orignalLevelCount = aggr.getLevels().length;
		newMemberSize = aggr.getLevels().length - (lowestTimeLevel - firstTimeLevel + 1) + (endLevelIndex + 1);

		sortType = getSortType(aggr, cubeDimensionReader);
		Comparator comparator = new Row4AggregationComparator(sortType);

		int levelCount = 0;
		if (aggr.getLevels() == null)
			levelCount = 0;
		else
			levelCount = aggr.getLevels().length;
		int levelSize = 0;
		if (levelCount != 0) {
			levelSize = getLevelSize(metaInfo, aggr.getLevels());
		}
		int measureSize = 0;
		if (aggregationFunction != null && aggregationFunction.length > 0) {
			measureSize = aggregationFunction.length * 64;
		}
		int rowSize = 16 + (4 + (levelSize + measureSize) - 1) / 8 * 8;
		int bufferSize = (int) (memoryCacheSize / rowSize);
		if (bufferSize < 100)
			bufferSize = 100;
		if (this.existReferenceDate) {
			if (!this.avoidExtraSort) {
				sortedFactRows = new DiskSortedStack(bufferSize, false, comparator, Row4Aggregation.getCreator());
				if (memoryCacheSize == 0) {
					sortedFactRows.setUseMemoryOnly(true);
				}
			} else {
				factRows = new BufferedStructureArray(Row4Aggregation.getCreator(), bufferSize);
				if (memoryCacheSize == 0) {
					factRows.setUseMemoryOnly(true);
				}
				factRowPostion = 0;
			}
		}
		if (this.existLastDate) {
			factRows = new BufferedStructureArray(Row4Aggregation.getCreator(), bufferSize);
			if (memoryCacheSize == 0) {
				factRows.setUseMemoryOnly(true);
			}
			factRowPostion = 0;
		}

		comparator = new MemberCellIndexComparator(getSortType(aggr, cubeDimensionReader));
		timeMemberFilters = new DiskSortedStack[timeFunction.length];
		for (int i = 0; i < timeMemberFilters.length; i++) {
			timeMemberFilters[i] = new DiskSortedStack(bufferSize, false, comparator, MemberCellIndex.getCreator());
			if (memoryCacheSize == 0) {
				timeMemberFilters[i].setUseMemoryOnly(true);
			}
		}
		this.aggregation = aggr;

		this.measureIndexes = new int[timeFunction.length];
		this.parameterColIndex = new int[timeFunction.length];
		this.aggregationFunction = new IAggrFunction[timeFunction.length];
		for (int i = 0; i < timeFunction.length; i++) {
			aggregationFunction[i] = AggregationManager.getInstance().getAggregation(timeFunction[i].getFunctionName());
			if (aggregationFunction[i] == null) {
				throw new DataException(
						DataResourceHandle.getInstance().getMessage(ResourceConstants.UNSUPPORTED_FUNCTION)
								+ timeFunction[i].getFunctionName());
			}
			if (AggregationUtil.needDataField(aggregationFunction[i])) {
				this.parameterColIndex[i] = find(parameterColNames, timeFunction[i].getParaCol());
			} else {
				this.parameterColIndex[i] = -1;
			}

			final String measureName = timeFunction[i].getMeasureName();
			this.measureIndexes[i] = metaInfo.getMeasureIndex(measureName);
			if (this.measureIndexes[i] == -1 && measureName != null) {
				throw new DataException(ResourceConstants.MEASURE_NAME_NOT_FOUND, measureName);
			}
		}

		measureInfos = metaInfo.getMeasureInfos();
		facttableRow = new FacttableRow(measureInfos, cubeDimensionReader, metaInfo);
		this.cubeDimensionReader = cubeDimensionReader;
		getLevelType();
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

	private void getLevelType() {
		DimLevel[] aggrLevel = this.aggregation.getLevels();
		this.levelType = new String[lowestTimeLevel - firstTimeLevel + 1];
		ILevel[] level = this.timeDimension.getHierarchy().getLevels();
		for (int i = firstTimeLevel; i <= lowestTimeLevel; i++) {
			for (int j = 0; j < level.length; j++) {
				if (aggrLevel[i].getLevelName().equals(level[j].getName())) {
					this.levelType[i - firstTimeLevel] = level[j].getLeveType();
					break;
				}
			}
		}
	}

	private static IPeriodsFunction[] createTimeFunction(AggregationFunctionDefinition[] timeFunction)
			throws DataException {
		IPeriodsFunction[] periodsFunction = new IPeriodsFunction[timeFunction.length];
		String toDatelevelType = null;
		String paralevelType = null;
		for (int i = 0; i < periodsFunction.length; i++) {
			ITimeFunction function = timeFunction[i].getTimeFunction();
			toDatelevelType = toLevelType(function.getBaseTimePeriod().getType());
			if (function.getBaseTimePeriod().countOfUnit() == 0) {
				periodsFunction[i] = TimeFunctionFactory.createPeriodsToDateFunction(toDatelevelType,
						function.getBaseTimePeriod().isCurrent());
			} else {
				periodsFunction[i] = TimeFunctionFactory.createTrailingFunction(toDatelevelType,
						function.getBaseTimePeriod().countOfUnit());
			}
			((AbstractMDX) periodsFunction[i]).setReferenceDate((ReferenceDate) function.getReferenceDate());

			if (function.getRelativeTimePeriod() != null) {
				paralevelType = toLevelType(function.getRelativeTimePeriod().getType());
				IParallelPeriod parallelPeriod = TimeFunctionFactory.createParallelPeriodFunction(paralevelType,
						function.getRelativeTimePeriod().countOfUnit());
				((AbstractMDX) parallelPeriod).setReferenceDate((ReferenceDate) function.getReferenceDate());
				periodsFunction[i] = new PeriodsToDateWithParallel(parallelPeriod, periodsFunction[i]);
			}
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

	private int[] getSortType(AggregationDefinition aggregationDef, ICubeDimensionReader cubeDimensionReader) {
		ArrayList<DimLevel> list = new ArrayList<DimLevel>();
		DimLevel[] aggrDimLevel = aggregationDef.getLevels();

		ILevel[] levels = timeDimension.getHierarchy().getLevels();
		for (int i = 0; i < firstTimeLevel; i++) {
			list.add(aggrDimLevel[i]);
		}
		for (int i = 0; i <= this.endLevelIndex; i++) {
			list.add(new DimLevel(tDimName, levels[i].getName()));
		}
		for (int i = lowestTimeLevel + 1; i < aggregationDef.getLevels().length; i++) {
			list.add(aggrDimLevel[i]);
		}
		int[] sortType = new int[list.size()];
		for (int i = 0; i < sortType.length; i++) {
			sortType[i] = getSortType(aggregationDef, list.get(i));
		}
		return sortType;
	}

	private int getLowestTimeLevel(AggregationDefinition aggregationDef) {
		DimLevel[] levels = aggregationDef.getLevels();
		int index = 0;
		for (int i = 0; i < levels.length; i++) {
			if (this.tDimName.equals(levels[i].getDimensionName())) {
				index = i;
			}
		}
		return index;
	}

	private int getFirstTimeLevel(AggregationDefinition aggregationDef) {

		DimLevel[] levels = aggregationDef.getLevels();

		for (int i = 0; i < levels.length; i++) {
			if (this.tDimName.equals(levels[i].getDimensionName())) {
				return i;
			}
		}
		return -1;
	}

	private static int getSortType(AggregationDefinition aggregationDef, DimLevel level) {
		DimLevel[] levels = aggregationDef.getLevels();
		int[] sortTypes = aggregationDef.getSortTypes();
		for (int i = 0; i < levels.length; i++) {
			if (levels[i].equals(level))
				return sortTypes[i];
		}
		return IDimensionSortDefn.SORT_ASC;
	}

	public boolean existTimeFunction() {
		return this.existTimeFunction;
	}

	public void onRow(Row4Aggregation row) throws IOException, DataException {
		Row4Aggregation newRow = new Row4Aggregation();
		if (row.getLevelMembers().length != newMemberSize || orignalLevelCount != newMemberSize) {
			Member[] nMembers = new Member[newMemberSize];
			System.arraycopy(row.getLevelMembers(), 0, nMembers, 0, firstTimeLevel);
			Member[] timeMember = cubeDimensionReader.getLevelMembers(timeDimensionIndex, endLevelIndex,
					row.getDimPos()[timeDimensionIndex]);
			System.arraycopy(timeMember, 0, nMembers, firstTimeLevel, timeMember.length);
			if ((orignalLevelCount - (lowestTimeLevel + 1)) > 0) {
				System.arraycopy(row.getLevelMembers(), lowestTimeLevel + 1, nMembers,
						firstTimeLevel + timeMember.length, orignalLevelCount - (lowestTimeLevel + 1));
			}
			newRow.setLevelMembers(nMembers);
			newRow.setDimPos(row.getDimPos());
			newRow.setMeasures(row.getMeasures());
			newRow.setMeasureList(row.getMeasureList());
			newRow.setParameterValues(row.getParameterValues());

		} else {
			newRow = row;
		}
		if (this.existReferenceDate && !this.avoidExtraSort) {
			sortedFactRows.push(newRow);
		}
		if (this.existLastDate || this.avoidExtraSort) {
			factRows.add(newRow);
		}
	}

	private Row4Aggregation retrieveOneFactRow() throws IOException {
		if (factRowPostion >= this.factRows.size())
			return null;
		Row4Aggregation row = (Row4Aggregation) this.factRows.get(this.factRowPostion);
		this.factRowPostion++;
		return row;
	}

	public List<TimeResultRow> getAggregationResultSet(IAggregationResultSet resultSet)
			throws DataException, IOException {
		createCalculator(resultSet.length());
		for (int i = 0; i < resultSet.length(); i++) {
			resultSet.seek(i);
			IAggregationResultRow aggrRow = resultSet.getCurrentRow();
			Member[] member = aggrRow.getLevelMembers();
			Member[] nMembers = new Member[newMemberSize];
			System.arraycopy(member, 0, nMembers, 0, firstTimeLevel);
			if ((member.length - (lowestTimeLevel + 1)) > 0) {
				System.arraycopy(member, lowestTimeLevel + 1, nMembers, firstTimeLevel + (endLevelIndex + 1),
						member.length - (lowestTimeLevel + 1));
			}
			for (int j = 0; j < periodFunction.length; j++) {
				TimeMember tMember = getCurrentMember(timeDimension, referenceDate[j], member);
				List<TimeMember> validTimeMember = periodFunctionResultCache[j].get(tMember);
				if (validTimeMember == null) {
					validTimeMember = periodFunction[j].getResult(tMember);
					periodFunctionResultCache[j].put(tMember, validTimeMember);
				}
				for (int k = 0; k < validTimeMember.size(); k++) {
					Member[] filterMembers = new Member[nMembers.length];
					System.arraycopy(nMembers, 0, filterMembers, 0, nMembers.length);
					Member[] timeMember = toMember(validTimeMember.get(k));
					System.arraycopy(timeMember, 0, filterMembers, firstTimeLevel, timeMember.length);
					MemberCellIndex memberCellIndex = new MemberCellIndex(filterMembers, i);
					this.timeMemberFilters[j].push(memberCellIndex);
				}
			}
		}
		currentFilterList = new List[timeMemberFilters.length];
		currentFilter = new MemberCellIndex[timeMemberFilters.length];
		for (int i = 0; i < timeMemberFilters.length; i++) {
			if (referenceDate[i] == null)
				continue;
			currentFilterList[i] = new ArrayList<MemberCellIndex>();
			currentFilter[i] = (MemberCellIndex) this.timeMemberFilters[i].pop();
			retrieveFilter(i);
		}
		currentRowList = new ArrayList<Row4Aggregation>();
		if (this.existReferenceDate) {
			currentRow = retrieveOneDetailRow();
			retrieveGroupRows(true);

			while (currentRowList.size() > 0) {
				for (int i = 0; i < timeMemberFilters.length; i++) {
					if (referenceDate[i] == null)
						continue;
					if (currentFilterList[i].size() == 0)
						continue;
					int compareResult = compare(currentRowList.get(0), currentFilterList[i].get(0));
					while (compareResult > 0) {
						retrieveFilter(i);
						if (currentFilterList[i].size() == 0)
							break;
						compareResult = compare(currentRowList.get(0), currentFilterList[i].get(0));
					}
					if (compareResult == 0) {
						doCalculate(i);
					}
				}
				retrieveGroupRows(true);
			}
		}

		for (int i = 0; i < timeMemberFilters.length; i++) {
			if (referenceDate[i] != null)
				continue;
			currentFilterList[i] = new ArrayList<MemberCellIndex>();
			currentFilter[i] = (MemberCellIndex) this.timeMemberFilters[i].pop();
			retrieveFilter(i);
		}
		currentRowList = new ArrayList<Row4Aggregation>();
		if (this.existLastDate) {
			currentRow = retrieveOneFactRow();
			retrieveGroupRows(false);

			while (currentRowList.size() > 0) {
				for (int i = 0; i < timeMemberFilters.length; i++) {
					if (referenceDate[i] != null)
						continue;
					if (currentFilterList[i].size() == 0)
						continue;
					int compareResult = compare(currentRowList.get(0), currentFilterList[i].get(0));
					while (compareResult > 0) {
						retrieveFilter(i);
						if (currentFilterList[i].size() == 0)
							break;
						compareResult = compare(currentRowList.get(0), currentFilterList[i].get(0));
					}
					if (compareResult == 0) {
						doCalculate(i);
					}
				}
				retrieveGroupRows(false);
			}
		}

		List<TimeResultRow> result = new ArrayList<TimeResultRow>();
		for (int i = 0; i < accumulators.length; i++) {
			Object[] value = new Object[accumulators[i].length];
			for (int j = 0; j < accumulators[i].length; j++) {
				this.accumulators[i][j].finish();
				value[j] = this.accumulators[i][j].getValue();
			}
			result.add(new TimeResultRow(value));
		}
		return result;
	}

	private void doCalculate(int functionIndex) throws DataException {
		for (int i = 0; i < currentRowList.size(); i++) {
			if (!getFilterResult(currentRowList.get(i), functionIndex))
				continue;
			while (currentRowList.get(i).nextMeasures()) {
				Object[] para = getAccumulatorParameter(currentRowList.get(i), functionIndex);
				for (int j = 0; j < currentFilterList[functionIndex].size(); j++) {
					this.accumulators[currentFilterList[functionIndex].get(j).cellPosition][functionIndex].onRow(para);
				}
			}
			currentRowList.get(i).firstMeasure();
		}
	}

	private boolean getFilterResult(Row4Aggregation row, int functionNo) throws DataException {
		facttableRow.setDimPos(row.getDimPos());
		facttableRow.setMeasure(row.getMeasures());
		IJSFacttableFilterEvalHelper filterEvalHelper = (aggregation.getAggregationTimeFunctions()[functionNo])
				.getFilterEvalHelper();
		if (filterEvalHelper == null) {
			return true;
		} else {
			return filterEvalHelper.evaluateFilter(facttableRow);
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

	private void retrieveFilter(int functionIndex) throws IOException {
		MemberCellIndex filter;
		currentFilterList[functionIndex].clear();
		if (currentFilter[functionIndex] == null) {
			return;
		}
		currentFilterList[functionIndex].add(currentFilter[functionIndex]);
		filter = (MemberCellIndex) this.timeMemberFilters[functionIndex].pop();
		while (filter != null && compare(currentFilter[functionIndex], filter) == 0) {
			currentFilterList[functionIndex].add(filter);
			filter = (MemberCellIndex) this.timeMemberFilters[functionIndex].pop();
		}
		currentFilter[functionIndex] = filter;
	}

	private void retrieveGroupRows(boolean isDetailRow) throws IOException {
		Row4Aggregation row;
		currentRowList.clear();
		if (currentRow == null) {
			return;
		}
		currentRowList.add(currentRow);
		if (isDetailRow)
			row = retrieveOneDetailRow();
		else
			row = retrieveOneFactRow();
		while (row != null && compare(currentRow, row) == 0) {
			currentRowList.add(row);
			if (isDetailRow)
				row = retrieveOneDetailRow();
			else
				row = retrieveOneFactRow();
		}
		currentRow = row;
	}

	private Row4Aggregation retrieveOneDetailRow() throws IOException {
		Row4Aggregation row = null;
		if (!avoidExtraSort && this.existReferenceDate) {
			row = (Row4Aggregation) this.sortedFactRows.pop();
		} else {
			row = this.retrieveOneFactRow();
		}
		return row;
	}

	/**
	 * indicate whether should retrieve filter forward to do the compare
	 * 
	 * @param r
	 * @param m
	 * @return
	 */
	private int compare(Row4Aggregation r, MemberCellIndex m) {
		int result = 0;
		for (int i = 0; i < r.getLevelMembers().length; i++) {
			if (m.member[i] == null)
				continue;
			result = r.getLevelMembers()[i].compareTo(m.member[i]);
			// if it is DESC, we should make it reversed
			if (sortType[i] == IDimensionSortDefn.SORT_DESC) {
				result *= -1;
			}
			if (result != 0)
				return result;
		}
		return 0;
	}

	private static int compare(Row4Aggregation r1, Row4Aggregation r2) {
		int result = 0;
		for (int i = 0; i < r1.getLevelMembers().length; i++) {
			result = r1.getLevelMembers()[i].compareTo(r2.getLevelMembers()[i]);
			if (result != 0)
				return result;
		}
		return 0;
	}

	private static int compare(MemberCellIndex m1, MemberCellIndex m2) {
		int result = 0;
		for (int i = 0; i < m1.member.length; i++) {
			if (m1.member[i] == null && m2.member[i] == null)
				continue;
			if (m1.member[i] == null)
				return -1;
			if (m2.member[i] == null)
				return 1;
			result = m1.member[i].compareTo(m2.member[i]);
			if (result != 0)
				return result;
		}
		return 0;
	}

	private Member[] toMember(TimeMember tMember) {
		int[] tMemberValues = tMember.getMemberValue();
		Member[] member = new Member[tMemberValues.length];
		for (int i = 0; i < tMemberValues.length; i++) {
			member[i] = new Member();
			member[i].setKeyValues(new Integer[] { tMemberValues[i] });
		}
		return member;
	}

	private TimeMember getCellTimeMember(Member[] member) {
		int[] timeMember = new int[lowestTimeLevel - firstTimeLevel + 1];
		for (int i = 0; i < timeMember.length; i++) {
			timeMember[i] = ((Integer) (member[this.firstTimeLevel + i].getKeyValues()[0])).intValue();
		}
		return new TimeMember(timeMember, levelType);
	}

	private TimeMember getCurrentMember(IDimension timeDimension, Date referenceDate, Member[] member) {
		TimeMember cellTimeMember = getCellTimeMember(member);
		if (referenceDate == null)
			return TimeMemberUtil.getCurrentMember(timeDimension, cellTimeMember);
		else
			return TimeMemberUtil.toMember(timeDimension, referenceDate, cellTimeMember);
	}

	private void createCalculator(int size) throws DataException {
		AggregationFunctionDefinition[] timeFunction = this.aggregation.getAggregationTimeFunctions();
		accumulators = new Accumulator[size][];
		for (int i = 0; i < size; i++) {
			this.accumulators[i] = new Accumulator[timeFunction.length];
			for (int j = 0; j < timeFunction.length; j++) {
				this.accumulators[i][j] = this.aggregationFunction[j].newAccumulator();
				this.accumulators[i][j].start();
			}
		}
	}
}

class MemberCellIndex implements IComparableStructure {
	Member[] member = null;
	int cellPosition = 0;

	MemberCellIndex(Member[] member, int cellPosition) {
		this.member = member;
		this.cellPosition = cellPosition;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.data.util.IStructure#getFieldValues()
	 */
	public Object[] getFieldValues() {
		Object[][] objects = new Object[member.length + 1][];
		for (int i = 0; i < member.length; i++) {
			objects[i] = member[i].getFieldValues();
		}
		objects[objects.length - 1] = new Object[1];
		objects[objects.length - 1][0] = cellPosition;
		return ObjectArrayUtil.convert(objects);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object o) {
		int result;
		int oCellPosition = ((MemberCellIndex) o).cellPosition;
		Member[] oMember = ((MemberCellIndex) o).member;
		for (int i = 0; i < member.length; i++) {
			result = member[i].compareTo(oMember[i]);
			if (result != 0)
				return result;
		}
		if (cellPosition > oCellPosition) {
			return 1;
		} else if (cellPosition < oCellPosition) {
			return -1;
		} else {
			return 0;
		}
	}

	/*
	 * 
	 */
	public static IStructureCreator getCreator() {
		return new MemberCellIndexCreator();
	}
}

/**
 * 
 * @author Administrator
 *
 */
class MemberCellIndexCreator implements IStructureCreator {
	private static IStructureCreator levelMemberCreator = Member.getCreator();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.olap.data.util.IStructureCreator#createInstance(java.
	 * lang.Object[])
	 */
	public IStructure createInstance(Object[] fields) {
		Object[][] objectArrays = ObjectArrayUtil.convert(fields);
		Member[] member = new Member[objectArrays.length - 1];
		for (int i = 0; i < member.length; i++) {
			if (objectArrays[i] == null)
				member[i] = null;
			else
				member[i] = (Member) levelMemberCreator.createInstance(objectArrays[i]);
		}
		int cellIndex = ((Integer) objectArrays[objectArrays.length - 1][0]);

		return new MemberCellIndex(member, cellIndex);
	}
}

/**
 * 
 * @author Administrator
 *
 */
class MemberCellIndexComparator implements Comparator {

	private int[] sortType = null;

	/**
	 * 
	 * @param sortType
	 */
	MemberCellIndexComparator(int[] sortType) {
		this.sortType = sortType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object o1, Object o2) {
		MemberCellIndex m1 = (MemberCellIndex) o1;
		MemberCellIndex m2 = (MemberCellIndex) o2;

		assert m1.member.length == m2.member.length;

		for (int i = 0; i < m1.member.length; i++) {
			if (sortType == null || sortType.length <= i || sortType[i] == IDimensionSortDefn.SORT_UNDEFINED
					|| sortType[i] == IDimensionSortDefn.SORT_ASC) {
				if (m1.member[i] == null && m2.member[i] == null)
					continue;
				if (m1.member[i].compareTo(m2.member[i]) < 0) {
					return -1;
				} else if (m1.member[i].compareTo(m2.member[i]) > 0) {
					return 1;
				}
			} else {
				if (m1.member[i] == null && m2.member[i] == null)
					continue;
				if (m1.member[i].compareTo(m2.member[i]) < 0) {
					return 1;
				} else if (m1.member[i].compareTo(m2.member[i]) > 0) {
					return -1;
				}
			}
		}
		return 0;
	}

}

class PeriodsToDateWithParallel implements IPeriodsFunction {
	private IParallelPeriod parallelFunc;
	private IPeriodsFunction periodsToDateFunc;

	PeriodsToDateWithParallel(IParallelPeriod parallelFunc, IPeriodsFunction periodsToDateFunc) {
		this.parallelFunc = parallelFunc;
		this.periodsToDateFunc = periodsToDateFunc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.data.impl.aggregation.function.
	 * IPeriodsFunction#getResult(org.eclipse.birt.data.engine.olap.data.impl.
	 * aggregation.function.TimeMember)
	 */
	public List<TimeMember> getResult(TimeMember member) {
		TimeMember timeMember = parallelFunc.getResult(member);
		((AbstractMDX) periodsToDateFunc).setReferenceDate(((AbstractMDX) parallelFunc).getReferenceDate());
		return periodsToDateFunc.getResult(timeMember);
	}

	public String getToolTip(TimeMember member) {
		return null;
	}
}
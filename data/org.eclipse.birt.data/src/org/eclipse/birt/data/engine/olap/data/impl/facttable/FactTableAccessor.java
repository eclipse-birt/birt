/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: 
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.olap.data.impl.facttable;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.aggregation.AggregationManager;
import org.eclipse.birt.data.engine.api.aggregation.IAggrFunction;
import org.eclipse.birt.data.engine.cache.Constants;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.cache.SizeOfUtil;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.StopSign;
import org.eclipse.birt.data.engine.olap.data.api.ILevel;
import org.eclipse.birt.data.engine.olap.data.api.MeasureInfo;
import org.eclipse.birt.data.engine.olap.data.api.cube.IDatasetIterator;
import org.eclipse.birt.data.engine.olap.data.document.DocumentObjectCache;
import org.eclipse.birt.data.engine.olap.data.document.DocumentObjectUtil;
import org.eclipse.birt.data.engine.olap.data.document.IDocumentManager;
import org.eclipse.birt.data.engine.olap.data.document.IDocumentObject;
import org.eclipse.birt.data.engine.olap.data.impl.NamingUtil;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Dimension;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.DimensionKey;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.DimensionRow;
import org.eclipse.birt.data.engine.olap.data.impl.facttable.DimensionDivider.CombinedPositionContructor;
import org.eclipse.birt.data.engine.olap.data.impl.facttable.DimensionDivider.DimensionPositionSeeker;
import org.eclipse.birt.data.engine.olap.data.impl.facttable.DimensionDivider.DimensionPositionSeeker.DimensionInfo;
import org.eclipse.birt.data.engine.olap.data.impl.facttable.DimensionDivision.IntRange;
import org.eclipse.birt.data.engine.olap.data.util.BufferedStructureArray;
import org.eclipse.birt.data.engine.olap.data.util.Bytes;
import org.eclipse.birt.data.engine.olap.data.util.DiskSortedStack;
import org.eclipse.birt.data.engine.olap.data.util.IDiskArray;
import org.eclipse.birt.data.engine.olap.data.util.StructureDiskArray;

/**
 * This a accessor class for fact table which can be used to save or load a
 * FactTable.
 */

public class FactTableAccessor {
	private IDocumentManager documentManager = null;
	private static Logger logger = Logger.getLogger(FactTableAccessor.class.getName());
	private long memoryCacheSize = 0;

	public FactTableAccessor(IDocumentManager documentManager) {
		logger.entering(FactTableAccessor.class.getName(), "FactTableAccessor", documentManager);
		this.documentManager = documentManager;
		logger.exiting(FactTableAccessor.class.getName(), "FactTableAccessor");
	}

	/**
	 * 
	 * @param factTableName
	 * @param iterator
	 * @param dimensions
	 * @param measureColumnName
	 * @param stopSign
	 * @return
	 * @throws BirtException
	 * @throws IOException
	 */
	public FactTable saveFactTable(String factTableName, String[][] factTableJointColumnNames,
			String[][] DimJointColumnNames, IDatasetIterator iterator, Dimension[] dimensions,
			String[] measureColumnName, Map calculatedMeasure, String[] measureColumnAggregations, StopSign stopSign)
			throws BirtException, IOException {
		FacttableRowContainer sortedFactTableRows = null;
		if (measureColumnAggregations == null || measureColumnAggregations.length == 0) {
			sortedFactTableRows = populateSortedFacttableRowsWithoutAggregationCalculation(factTableJointColumnNames,
					iterator, measureColumnName, stopSign);
		} else {
			sortedFactTableRows = populatedSortedFacttableRowsWithAggregationCalculation(factTableJointColumnNames,
					iterator, measureColumnName, measureColumnAggregations, stopSign);
		}
		int segmentCount = getSegmentCount(sortedFactTableRows.size());

		DimensionInfo[] dimensionInfo = getDimensionInfo(dimensions);
		MeasureInfo[] measureInfo = getMeasureInfo(iterator, measureColumnName);
		MeasureInfo[] calMeasureInfo = getCalculatedMeasureInfo(calculatedMeasure);

		saveFactTableMetadata(factTableName, dimensionInfo, measureInfo, calMeasureInfo, segmentCount);

		DimensionDivision[] subDimensions = calculateDimensionDivision(getDimensionMemberCount(dimensions),
				segmentCount);

		int[][][] columnIndex = getColumnIndex(DimJointColumnNames, dimensions);
		DimensionPositionSeeker[] dimensionSeekers = new DimensionPositionSeeker[dimensions.length];
		for (int i = 0; i < dimensionSeekers.length; i++) {
			dimensionSeekers[i] = new DimensionPositionSeeker(
					getDimCombinatedKey(columnIndex[i], dimensions[i].getAllRows(stopSign)));
		}

		int[] dimensionPosition = new int[dimensions.length];
		DocumentObjectCache documentObjectManager = new DocumentObjectCache(documentManager,
				(long) (memoryCacheSize * 0.25));
		CombinedPositionContructor combinedPositionCalculator = new CombinedPositionContructor(subDimensions);

		FTSUNameSaveHelper saveHelper = new FTSUNameSaveHelper(documentManager, factTableName);
		FactTableRow currentRow = sortedFactTableRows.pop();
		boolean invalidDimensionKey = false;
		int invalidRowNumber = 0;
		while (currentRow != null && !stopSign.isStopped()) {
			invalidDimensionKey = false;
			for (int i = 0; i < dimensionPosition.length; i++) {
				dimensionPosition[i] = dimensionSeekers[i].find(currentRow.getDimensionKeys()[i]);
				if (dimensionPosition[i] < 0) {
					invalidDimensionKey = true;
					logger.fine("The fact table of cube " + factTableName
							+ " has an invalid data row where the value of dimension key "
							+ Arrays.toString(factTableJointColumnNames[i]) + " is "
							+ currentRow.getDimensionKeys()[i].toString()
							+ " which however does not exist in dimension " + dimensions[i].getName() + ".");
				}
			}
			if (invalidDimensionKey) {
				currentRow = sortedFactTableRows.pop();
				invalidRowNumber++;
				continue;
			}
			int[] subDimensionIndex = getSubDimensionIndex(dimensionPosition, subDimensions);
			String FTSUDocName = FTSUDocumentObjectNamingUtil
					.getDocumentObjectName(NamingUtil.getFactTableName(factTableName), subDimensionIndex);
			saveHelper.add(FTSUDocName);

			IDocumentObject documentObject = documentObjectManager.getIDocumentObject(FTSUDocName);
			documentObject.writeBytes(new Bytes(combinedPositionCalculator
					.calculateCombinedPosition(subDimensionIndex, dimensionPosition).toByteArray()));
			for (int i = 0; i < measureInfo.length; i++) {
				DocumentObjectUtil.writeValue(documentObject, measureInfo[i].getDataType(),
						currentRow.getMeasures()[i]);
			}
			currentRow = sortedFactTableRows.pop();
		}
		saveHelper.save();
		if (invalidRowNumber > 0) {
			logger.warning("The fact table of cube " + factTableName + " has " + invalidRowNumber
					+ "invalid rows where the value of dimension key does not exist in dimension.");
		}
		documentObjectManager.closeAll();
		documentManager.flush();
		return new FactTable(factTableName, documentManager, dimensionInfo, measureInfo, calMeasureInfo, segmentCount,
				subDimensions);

	}

	private FacttableRowContainer populatedSortedFacttableRowsWithAggregationCalculation(
			String[][] factTableJointColumnNames, IDatasetIterator iterator, String[] measureColumnName,
			String[] measureColumnAggregations, StopSign stopSign) throws BirtException, IOException, DataException {
		FacttableRowContainer sortedFactTableRows;
		DiskSortedStack sortedRows = getSortedFactTableRows(iterator, factTableJointColumnNames, measureColumnName,
				false, stopSign);

		final StructureDiskArray aggregatedRows = new StructureDiskArray(FactTableRow.getCreator());
		IAggrFunction[] functions = new IAggrFunction[measureColumnAggregations.length];
		for (int i = 0; i < measureColumnAggregations.length; i++) {
			functions[i] = AggregationManager.getInstance().getAggregation(measureColumnAggregations[i]);
		}
		FTAggregationHelper aggrHelper = new FTAggregationHelper(functions);
		FactTableRow lastRow = (FactTableRow) sortedRows.pop();
		if (lastRow != null) {
			FactTableRow currentRow = null;
			while (true && !stopSign.isStopped()) {
				currentRow = (FactTableRow) sortedRows.pop();
				if (lastRow.equals(currentRow)) {
					aggrHelper.onRow(false, lastRow);
					lastRow = currentRow;
				} else {
					aggrHelper.onRow(true, lastRow);
					lastRow.setMeasures(aggrHelper.getCurrentValues());
					aggregatedRows.add(lastRow);
					lastRow = currentRow;
				}
				if (currentRow == null)
					break;
			}
		}

		sortedFactTableRows = new FacttableRowContainer() {

			private int index = 0;

			public FactTableRow pop() throws IOException {
				if (index >= aggregatedRows.size())
					return null;
				FactTableRow result = (FactTableRow) aggregatedRows.get(index);
				index++;
				return result;
			}

			public int size() {
				return aggregatedRows.size();
			}
		};
		return sortedFactTableRows;
	}

	private FacttableRowContainer populateSortedFacttableRowsWithoutAggregationCalculation(
			String[][] factTableJointColumnNames, IDatasetIterator iterator, String[] measureColumnName,
			StopSign stopSign) throws BirtException, IOException {
		FacttableRowContainer sortedFactTableRows;
		final DiskSortedStack facttableRows = getSortedFactTableRows(iterator, factTableJointColumnNames,
				measureColumnName, false, stopSign);

		sortedFactTableRows = new FacttableRowContainer() {

			public FactTableRow pop() throws IOException {
				return (FactTableRow) facttableRows.pop();
			}

			public int size() {
				return facttableRows.size();
			}
		};
		return sortedFactTableRows;
	}

	public FactTable saveFactTable(String factTableName, String[][] factTableJointColumnNames,
			String[][] DimJointColumnNames, IDatasetIterator iterator, Dimension[] dimensions,
			String[] measureColumnName, StopSign stopSign) throws BirtException, IOException {
		return this.saveFactTable(factTableName, factTableJointColumnNames, DimJointColumnNames, iterator, dimensions,
				measureColumnName, null, null, stopSign);
	}

	private int[][][] getColumnIndex(String[][] keyColumnNames, Dimension[] dimensions) throws DataException {
		int[][][] columnIndex = new int[keyColumnNames.length][][];
		for (int i = 0; i < keyColumnNames.length; i++) {
			columnIndex[i] = new int[keyColumnNames[i].length][];
			ILevel[] levels = dimensions[i].getHierarchy().getLevels();
			for (int j = 0; j < keyColumnNames[i].length; j++) {
				columnIndex[i][j] = new int[3];
				columnIndex[i][j][0] = -1;
				for (int k = 0; k < levels.length; k++) {
					String[] columns = levels[k].getKeyNames();
					int index = find(columns, keyColumnNames[i][j]);
					if (index >= 0) {
						// is key column
						columnIndex[i][j][0] = 0;
						columnIndex[i][j][1] = k;
						columnIndex[i][j][2] = index;
						break;
					}
					columns = levels[k].getAttributeNames();
					index = find(columns, keyColumnNames[i][j]);
					if (index >= 0) {
						// is key column
						columnIndex[i][j][0] = 1;
						columnIndex[i][j][1] = k;
						columnIndex[i][j][2] = index;
						break;
					}
				}
				if (columnIndex[i][j][0] == -1) {
					throw new DataException(ResourceConstants.FACTTABLE_JOINT_COL_NOT_EXIST, keyColumnNames[i][j]);
				}
			}
		}
		return columnIndex;
	}

	/**
	 * 
	 * @param strArray
	 * @param str
	 * @return
	 */
	private int find(String[] strArray, String str) {
		if (strArray == null) {
			return -1;
		}
		for (int i = 0; i < strArray.length; i++) {
			if (strArray[i].equals(str)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 
	 * @param dimRowArray
	 * @return
	 * @throws IOException
	 */
	private static IDiskArray getDimCombinatedKey(int[][] columnIndex, IDiskArray dimRowArray) throws IOException {
		BufferedStructureArray resultArray = new BufferedStructureArray(DimensionKey.getCreator(), dimRowArray.size());
		for (int i = 0; i < dimRowArray.size(); i++) {
			DimensionRow dimRow = (DimensionRow) dimRowArray.get(i);
			DimensionKey key = new DimensionKey(columnIndex.length);
			Object[] values = new Object[columnIndex.length];
			for (int j = 0; j < columnIndex.length; j++) {
				if (columnIndex[j][0] == 0) {
					// this is a key column
					values[j] = dimRow.getMembers()[columnIndex[j][1]].getKeyValues()[columnIndex[j][2]];
				} else {
					values[j] = dimRow.getMembers()[columnIndex[j][1]].getAttributes()[columnIndex[j][2]];
				}
			}
			key.setKeyValues(values);
			key.setDimensionPos(i);
			resultArray.add(key);
		}
		return resultArray;
	}

	/**
	 * 
	 * @param factTableRowCount
	 * @return
	 */
	private static int getSegmentCount(int factTableRowCount) {
		int segmentCount = factTableRowCount / Constants.FACT_TABLE_BLOCK_SIZE;
		if (segmentCount * Constants.FACT_TABLE_BLOCK_SIZE < factTableRowCount) {
			segmentCount++;
		}
		return segmentCount;
	}

	/**
	 * 
	 * @param dimension
	 * @return
	 */
	private static DimensionInfo[] getDimensionInfo(Dimension[] dimension) {
		DimensionInfo[] dimensionInfo = new DimensionInfo[dimension.length];
		for (int i = 0; i < dimension.length; i++) {
			dimensionInfo[i] = new DimensionInfo();
			dimensionInfo[i].dimensionName = dimension[i].getName();
			dimensionInfo[i].dimensionLength = dimension[i].length();
		}
		return dimensionInfo;
	}

	/**
	 * 
	 * @param iterator
	 * @param measureColumnName
	 * @return
	 * @throws BirtException
	 */
	private static MeasureInfo[] getMeasureInfo(IDatasetIterator iterator, String[] measureColumnName)
			throws BirtException {
		MeasureInfo[] measureInfo = new MeasureInfo[measureColumnName.length];
		for (int i = 0; i < measureColumnName.length; i++) {
			measureInfo[i] = new MeasureInfo(measureColumnName[i], iterator.getFieldType(measureColumnName[i]));
		}
		return measureInfo;
	}

	/**
	 * 
	 * @param iterator
	 * @param measureColumnName
	 * @param calculatedMeasure
	 * @return
	 * @throws BirtException
	 */
	private static MeasureInfo[] getCalculatedMeasureInfo(Map calculatedMeasure) throws BirtException {
		if (calculatedMeasure == null) {
			return new MeasureInfo[0];
		}
		MeasureInfo[] measureInfo = new MeasureInfo[calculatedMeasure.size()];
		int i = 0;
		Iterator entry = calculatedMeasure.entrySet().iterator();
		while (entry.hasNext()) {
			Entry val = (Entry) entry.next();
			String name = NamingUtil.getDerivedMeasureName(val.getKey().toString());
			int dataType = ((Integer) val.getValue()).intValue();
			measureInfo[i] = new MeasureInfo(name, dataType);
			i++;
		}
		return measureInfo;
	}

	/**
	 * 
	 * @param factTableName
	 * @param dimensionInfo
	 * @param measureInfo
	 * @param segmentNumber
	 * @throws IOException
	 * @throws BirtException
	 */
	private void saveFactTableMetadata(String factTableName, DimensionInfo[] dimensionInfo, MeasureInfo[] measureInfo,
			MeasureInfo[] calculatedMeasureInfo, int segmentNumber) throws IOException, BirtException {
		IDocumentObject documentObject = documentManager
				.createDocumentObject(NamingUtil.getFactTableName(factTableName));
		// write dimension name and dimension member count
		documentObject.writeInt(dimensionInfo.length);
		for (int i = 0; i < dimensionInfo.length; i++) {
			documentObject.writeString(dimensionInfo[i].dimensionName);
			documentObject.writeInt(dimensionInfo[i].dimensionLength);
		}
		// write measure name and measure data type
		documentObject.writeInt(measureInfo.length + calculatedMeasureInfo.length);
		for (int i = 0; i < measureInfo.length; i++) {
			documentObject.writeString(measureInfo[i].getMeasureName());
			documentObject.writeInt(measureInfo[i].getDataType());
		}
		for (int i = 0; i < calculatedMeasureInfo.length; i++) {
			documentObject.writeString(calculatedMeasureInfo[i].getMeasureName());
			documentObject.writeInt(calculatedMeasureInfo[i].getDataType());
		}
		// write segment count
		documentObject.writeInt(segmentNumber);
		documentObject.close();
	}

	public void setMemoryCacheSize(long memoryCacheSize) {
		this.memoryCacheSize = memoryCacheSize;
	}

	private static int getObjectSize(int[] dataType) {
		int size = 0;
		for (int i = 0; i < dataType.length; i++) {
			size += SizeOfUtil.sizeOf(dataType[i]);
		}
		size += SizeOfUtil.getArraySize(dataType.length);

		return size;
	}

	/**
	 * 
	 * @param iterator
	 * @param keyColumnNames
	 * @param measureColumnNames
	 * @param stopSign
	 * @return
	 * @throws BirtException
	 * @throws IOException
	 */
	private DiskSortedStack getSortedFactTableRows(IDatasetIterator iterator, String[][] keyColumnNames,
			String[] measureColumnNames, boolean forceRemoveDuplicate, StopSign stopSign)
			throws BirtException, IOException {
		DiskSortedStack result = null;
		if (this.memoryCacheSize != 0) {
			int bufferSize = caculateBufferSize(iterator, keyColumnNames, measureColumnNames);
			result = new DiskSortedStack(bufferSize, true, false, FactTableRow.getCreator());
		} else {
			result = new DiskSortedStack(10000, true, false, FactTableRow.getCreator());
			result.setUseMemoryOnly(true);
		}

		int[][] levelKeyColumnIndex = new int[keyColumnNames.length][];
		int[] measureColumnIndex = new int[measureColumnNames.length];
		for (int i = 0; i < keyColumnNames.length; i++) {
			levelKeyColumnIndex[i] = new int[keyColumnNames[i].length];
			for (int j = 0; j < keyColumnNames[i].length; j++) {
				levelKeyColumnIndex[i][j] = iterator.getFieldIndex(keyColumnNames[i][j]);
			}
		}
		for (int i = 0; i < measureColumnIndex.length; i++) {
			measureColumnIndex[i] = iterator.getFieldIndex(measureColumnNames[i]);
		}
		while (iterator.next() && !stopSign.isStopped()) {
			FactTableRow factTableRow = new FactTableRow();
			DimensionKey[] dimensionKeys = new DimensionKey[levelKeyColumnIndex.length];
			for (int i = 0; i < levelKeyColumnIndex.length; i++) {
				dimensionKeys[i] = new DimensionKey(levelKeyColumnIndex[i].length);
				for (int j = 0; j < levelKeyColumnIndex[i].length; j++) {
					if (levelKeyColumnIndex[i][j] >= 0)
						dimensionKeys[i].getKeyValues()[j] = iterator.getValue(levelKeyColumnIndex[i][j]);
				}
			}
			factTableRow.setDimensionKeys(dimensionKeys);

			Object[] measures = new Object[measureColumnIndex.length];
			for (int i = 0; i < measureColumnIndex.length; i++) {
				measures[i] = iterator.getValue(measureColumnIndex[i]);
			}
			factTableRow.setMeasures(measures);
			result.push(factTableRow);
		}
		return result;
	}

	private int caculateBufferSize(IDatasetIterator iterator, String[][] keyColumnNames, String[] measureColumnNames)
			throws BirtException {
		int[][] levelKeyColumnDataType = new int[keyColumnNames.length][];
		int[] measureColumnType = new int[measureColumnNames.length];

		for (int i = 0; i < keyColumnNames.length; i++) {
			levelKeyColumnDataType[i] = new int[keyColumnNames[i].length];
			for (int j = 0; j < keyColumnNames[i].length; j++) {
				levelKeyColumnDataType[i][j] = iterator.getFieldType(keyColumnNames[i][j]);
			}
		}
		for (int i = 0; i < measureColumnType.length; i++) {
			measureColumnType[i] = iterator.getFieldType(measureColumnNames[i]);
		}
		int levelSize = 0;
		for (int i = 0; i < levelKeyColumnDataType.length; i++) {
			levelSize += getObjectSize(levelKeyColumnDataType[i]);
		}
		int measureSize = getObjectSize(measureColumnType);

		int rowSize = 16 + (4 + (levelSize + measureSize) - 1) / 8 * 8;
		return (int) ((memoryCacheSize * 0.75) / rowSize);
	}

	/**
	 * 
	 * @param dimensionNumbers
	 * @param multiple
	 * @return
	 */
	private static int[] getDimensionMemberCount(Dimension[] dimension) {
		int[] dimensionMemberCount = new int[dimension.length];
		for (int i = 0; i < dimension.length; i++) {
			dimensionMemberCount[i] = dimension[i].length();
		}
		return dimensionMemberCount;
	}

	/**
	 * 
	 * @param dimensionMemberCount
	 * @param multiple
	 * @return
	 */
	static DimensionDivision[] calculateDimensionDivision(int[] dimensionMemberCount, int blockNumber) {
		int[] subDimensionCount = DimensionDivider.divideDimension(dimensionMemberCount, blockNumber);
		DimensionDivision[] result = new DimensionDivision[dimensionMemberCount.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = new DimensionDivision(dimensionMemberCount[i], subDimensionCount[i]);
		}

		return result;
	}

	/**
	 * 
	 * @param dimensionPosition
	 * @param dimensionDivision
	 * @return
	 */
	private static int[] getSubDimensionIndex(int[] dimensionPosition, DimensionDivision[] dimensionDivision) {
		assert dimensionPosition.length == dimensionDivision.length;
		int[] result = new int[dimensionPosition.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = dimensionDivision[i].getSubDimensionIndex(dimensionPosition[i]);
		}
		return result;
	}

	/**
	 * 
	 * @param factTableName
	 * @param stopSign
	 * @return
	 * @throws IOException
	 */
	public FactTable load(String factTableName, StopSign stopSign) throws IOException {
		int segmentNumber = 0;
		IDocumentObject documentObject = documentManager.openDocumentObject(NamingUtil.getFactTableName(factTableName));
		DimensionInfo[] dimensionInfo = new DimensionInfo[documentObject.readInt()];
		for (int i = 0; i < dimensionInfo.length; i++) {
			dimensionInfo[i] = new DimensionInfo();
			dimensionInfo[i].dimensionName = documentObject.readString();
			dimensionInfo[i].dimensionLength = documentObject.readInt();
		}
		int measureSize = documentObject.readInt();
		List<MeasureInfo> measureInfoList = new ArrayList<MeasureInfo>();
		List<MeasureInfo> calMeasureInfoList = new ArrayList<MeasureInfo>();
		for (int i = 0; i < measureSize; i++) {
			String measureName = documentObject.readString();
			if (measureName.startsWith(NamingUtil.DERIVED_MEASURE_PREFIX)) {
				calMeasureInfoList
						.add(new MeasureInfo(NamingUtil.getMeasureName(measureName), documentObject.readInt()));
			} else {
				measureInfoList.add(new MeasureInfo(measureName, documentObject.readInt()));
			}
		}

		MeasureInfo[] measureInfo = (MeasureInfo[]) measureInfoList.toArray(new MeasureInfo[0]);
		MeasureInfo[] calMeasureInfo = (MeasureInfo[]) calMeasureInfoList.toArray(new MeasureInfo[0]);

		segmentNumber = documentObject.readInt();

		int[] dimensionMemberCount = new int[dimensionInfo.length];
		for (int i = 0; i < dimensionInfo.length; i++) {
			dimensionMemberCount[i] = dimensionInfo[i].dimensionLength;
		}
		DimensionDivision[] subDimensions = calculateDimensionDivision(dimensionMemberCount, segmentNumber);
		documentObject.close();
		return new FactTable(factTableName, documentManager, dimensionInfo, measureInfo, calMeasureInfo, segmentNumber,
				subDimensions);
	}

	/**
	 * 
	 * @author Administrator
	 *
	 */
	public static class FTSUDocumentObjectNamingUtil {

		/**
		 * All possible chars for representing a number as a String
		 */
		final static char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

		private static char[] buffer = new char[500];

		public static String getDocumentObjectName(String factTableName, int[] subDimensionNumber) {
			int radix = 10;

			int position = 0;
			int i;

			for (int k = subDimensionNumber.length - 1; k >= 0; k--) {
				i = subDimensionNumber[k];
				while (i >= radix) {
					buffer[position++] = digits[i % radix];
					i = i / radix;
				}
				buffer[position++] = digits[i % radix];

				if (k != 0) {
					buffer[position++] = 'X';
				}
			}
			for (int k = 0; k < position / 2; k++) {
				char c = buffer[position - 1 - k];
				buffer[position - 1 - k] = buffer[k];
				buffer[k] = c;
			}
			return factTableName + new String(buffer, 0, position);
		}
	}

}

interface FacttableRowContainer {
	public FactTableRow pop() throws IOException;

	public int size();
}

/**
 * 
 * @author Administrator
 *
 */
class FTSUNameSaveHelper {
	private HashMap map;
	private IDocumentManager documentManager;
	private String factTableName;

	/**
	 * 
	 * @param documentManager
	 * @param factTableName
	 */
	FTSUNameSaveHelper(IDocumentManager documentManager, String factTableName) {
		this.documentManager = documentManager;
		this.factTableName = factTableName;
		this.map = new HashMap();
	}

	/**
	 * 
	 * @param name
	 */
	void add(String name) {
		if (!map.containsKey(name)) {
			map.put(name, null);
		}
	}

	/**
	 * 
	 * @throws IOException
	 */
	void save() throws IOException {
		IDocumentObject FTSUNameSave = documentManager.createDocumentObject(NamingUtil.getFTSUListName(factTableName));

		Iterator nameIterator = map.keySet().iterator();
		while (nameIterator.hasNext()) {
			FTSUNameSave.writeString((String) nameIterator.next());
		}
		FTSUNameSave.close();
	}
}

class DimensionDivider {

	static int[] divideDimension(int[] dimensionLength, int blockNumber) {
		Set indexSet = new HashSet();

		int[] subDimensionCount = new int[dimensionLength.length];
		for (int i = 0; i < subDimensionCount.length; i++) {
			subDimensionCount[i] = 1;
		}

		if (blockNumber > 1)
			calculateSubDimensionCount(dimensionLength, blockNumber, subDimensionCount, indexSet);

		return subDimensionCount;
	}

	private static void calculateSubDimensionCount(int[] dimensionLength, int maxSubDimensionCount,
			int[] subDimensionCount, Set indexSet) {
		if (indexSet.size() == subDimensionCount.length)
			return;

		for (int i = 0; i < subDimensionCount.length; i++) {
			if (indexSet.contains(Integer.valueOf(i)))
				continue;

			if (subDimensionCount[i] + 1 > dimensionLength[i]) {
				indexSet.add(Integer.valueOf(i));
				continue;
			}

			subDimensionCount[i]++;
			if (isOver(subDimensionCount, maxSubDimensionCount)) {
				subDimensionCount[i]--;
				return;
			}
		}

		calculateSubDimensionCount(dimensionLength, maxSubDimensionCount, subDimensionCount, indexSet);
	}

	private static boolean isOver(int[] candidateArray, int target) {
		int candidate = 1;
		for (int i = 0; i < candidateArray.length; i++) {
			candidate *= candidateArray[i];
			if (candidate > target)
				return true;
		}

		return false;
	}

	/**
	 * This class is used to find dimension position by dimension key quickly.
	 * 
	 * @author Administrator
	 *
	 */
	public static class DimensionPositionSeeker {
		private IDiskArray diskMemberArray;
		private DimensionKey[] memberArray;
		private int diskPostion;
		private int position;

		/**
		 * 
		 * @param members
		 * @throws IOException
		 */
		DimensionPositionSeeker(IDiskArray member) throws IOException {
			IDiskArray members = getSortedDimensionKeys(member);

			if (Constants.isAggressiveMemoryUsage()) {
				this.memberArray = new DimensionKey[members.size()];
			} else {
				this.memberArray = new DimensionKey[members.size()];
			}
			for (int i = 0; i < memberArray.length; i++) {
				memberArray[i] = (DimensionKey) members.get(i);
			}
			if (members.size() > memberArray.length) {
				this.diskMemberArray = members;
				this.diskPostion = memberArray.length;
				this.position = this.diskPostion;
			}
		}

		private IDiskArray getSortedDimensionKeys(IDiskArray members) throws IOException {
			DiskSortedStack sortedStack = new DiskSortedStack(members.size(), true, false, DimensionKey.getCreator());
			for (int i = 0; i < members.size(); i++) {
				sortedStack.push(members.get(i));
			}
			IDiskArray resultArray = new BufferedStructureArray(DimensionKey.getCreator(), sortedStack.size());
			Object key = sortedStack.pop();
			while (key != null) {
				resultArray.add(key);
				key = sortedStack.pop();
			}
			return resultArray;
		}

		/**
		 * Find dimension position by dimension key.
		 * 
		 * @param key
		 * @return
		 * @throws IOException
		 */
		int find(DimensionKey key) throws IOException {
			int result = binarySearch(key);
			if (result >= 0) {
				return result;
			}
			if (diskMemberArray != null) {
				return traverseFind(key);
			}
			return result;
		}

		/**
		 * 
		 * @param key
		 * @return
		 */
		private int binarySearch(DimensionKey key) {
			int result = Arrays.binarySearch(memberArray, key);
			if (result >= 0) {
				return memberArray[result].getDimensionPos();
			}
			return -1;
		}

		/**
		 * 
		 * @param key
		 * @return
		 * @throws IOException
		 */
		private int traverseFind(DimensionKey key) throws IOException {
			for (int i = position; i < diskMemberArray.size(); i++) {
				if (((DimensionKey) diskMemberArray.get(i)).compareTo(key) == 0) {
					position = i;
					return ((DimensionKey) diskMemberArray.get(i)).getDimensionPos();
				}
			}
			for (int i = diskPostion; i < position; i++) {
				if (((DimensionKey) diskMemberArray.get(i)).compareTo(key) == 0) {
					position = i;
					return ((DimensionKey) diskMemberArray.get(i)).getDimensionPos();
				}
			}
			return -1;
		}

		public static class DimensionInfo {
			String dimensionName;
			int dimensionLength;

			public String getDimensionName() {
				return dimensionName;
			}

			public int getDimensionLength() {
				return dimensionLength;
			}
		}
	}

	public static class CombinedPositionContructor {
		private DimensionDivision[] subDimensions;
		private int[] dimensionBitLength;
		private int totalBitLength;

		public CombinedPositionContructor(DimensionDivision[] subDimensions) {
			this.subDimensions = subDimensions;
			calculateBitLength(subDimensions);
		}

		private void calculateBitLength(DimensionDivision[] dimensionDivision) {
			int maxRange;
			dimensionBitLength = new int[dimensionDivision.length];
			for (int i = 0; i < dimensionDivision.length; i++) {
				IntRange[] ranges = dimensionDivision[i].getRanges();
				maxRange = 0;
				for (int j = 0; j < ranges.length; j++) {
					if (ranges[j].end - ranges[j].start > maxRange) {
						maxRange = ranges[j].end - ranges[j].start + 1;
					}
				}
				dimensionBitLength[i] = getBitLength(maxRange);
				totalBitLength += dimensionBitLength[i];
			}
		}

		/**
		 * 
		 * @param maxInt
		 * @return
		 */
		private int getBitLength(int maxInt) {
			int bitLength = 1;
			int powerValue = 2;

			while (powerValue < maxInt) {
				bitLength++;
				powerValue *= 2;
			}
			return bitLength;
		}

		/**
		 * 
		 * @param subdimensionIndex
		 * @param dimensionPosition
		 * @return
		 */
		public BigInteger calculateCombinedPosition(int[] subdimensionIndex, int[] dimensionPosition) {
			long l = dimensionPosition[0] - subDimensions[0].getRanges()[subdimensionIndex[0]].start;
			int bitLength = dimensionBitLength[0];
			int i;
			for (i = 1; i < dimensionPosition.length; i++) {
				if (bitLength + dimensionBitLength[i] >= 63) {
					break;
				}
				l <<= dimensionBitLength[i];
				l |= dimensionPosition[i] - subDimensions[i].getRanges()[subdimensionIndex[i]].start;
				bitLength += dimensionBitLength[i];
			}

			BigInteger bigInteger = BigInteger.valueOf(l);
			for (; i < dimensionPosition.length; i++) {
				bigInteger = bigInteger.shiftLeft(dimensionBitLength[i]);
				bigInteger = bigInteger.or(BigInteger
						.valueOf(dimensionPosition[i] - subDimensions[i].getRanges()[subdimensionIndex[i]].start));
			}

			return bigInteger;
		}

		/**
		 * 
		 * @param subdimensionIndex
		 * @param combinedPosition
		 * @return
		 */
		public int[] calculateDimensionPosition(int[] subdimensionIndex, byte[] combinedPosition) {
			BigInteger bigInteger = new BigInteger(combinedPosition);
			int[] dimensionPosition = new int[dimensionBitLength.length];
			if (totalBitLength <= 63) {
				long l = bigInteger.longValue();
				for (int i = dimensionBitLength.length - 1; i >= 0; i--) {
					dimensionPosition[i] = subDimensions[i].getRanges()[subdimensionIndex[i]].start
							+ (int) (l & (0x7fffffff >> (31 - dimensionBitLength[i])));
					l >>= dimensionBitLength[i];
				}
				return dimensionPosition;
			}
			for (int i = dimensionBitLength.length - 1; i >= 0; i--) {
				dimensionPosition[i] = subDimensions[i].getRanges()[subdimensionIndex[i]].start + (int) (bigInteger
						.and(BigInteger.valueOf(0x7fffffff >> (31 - dimensionBitLength[i]))).longValue());
				bigInteger = bigInteger.shiftRight(dimensionBitLength[i]);
			}

			return dimensionPosition;
		}

	}
}

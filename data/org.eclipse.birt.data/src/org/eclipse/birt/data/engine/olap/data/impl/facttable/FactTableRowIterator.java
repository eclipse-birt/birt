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

package org.eclipse.birt.data.engine.olap.data.impl.facttable;

import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.StopSign;
import org.eclipse.birt.data.engine.olap.data.api.IComputedMeasureHelper;
import org.eclipse.birt.data.engine.olap.data.api.IDimensionResultIterator;
import org.eclipse.birt.data.engine.olap.data.api.MeasureInfo;
import org.eclipse.birt.data.engine.olap.data.api.cube.IDimension;
import org.eclipse.birt.data.engine.olap.data.document.DocumentObjectUtil;
import org.eclipse.birt.data.engine.olap.data.document.IDocumentObject;
import org.eclipse.birt.data.engine.olap.data.impl.NamingUtil;
import org.eclipse.birt.data.engine.olap.data.impl.Traversalor;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Dimension;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.DimensionResultIterator;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Member;
import org.eclipse.birt.data.engine.olap.data.impl.facttable.FactTableAccessor.FTSUDocumentObjectNamingUtil;
import org.eclipse.birt.data.engine.olap.data.util.Bytes;
import org.eclipse.birt.data.engine.olap.data.util.DataType;
import org.eclipse.birt.data.engine.olap.data.util.IDiskArray;
import org.eclipse.birt.data.engine.olap.util.filter.ICubePosFilter;
import org.eclipse.birt.data.engine.olap.util.filter.IFacttableRow;
import org.eclipse.birt.data.engine.olap.util.filter.IJSFacttableFilterEvalHelper;

/**
 * An iterator on a result set from a executed fact table query.
 */

public class FactTableRowIterator implements IFactTableRowIterator {
	private FactTable factTable;
	private MeasureInfo[] computedMeasureInfo;
	private MeasureInfo[] allMeasureInfo; // include measures and computed measures

	private IDiskArray[] selectedPos;
	private int[] dimensionIndex;
	private int[] currentSubDim;
	private List[] selectedSubDim = null;

	private IDocumentObject currentSegment;
	private int[] currentPos;
	private Object[] currentMeasureValues; // current values for measures
	private MeasureMap currentMeasureMap; // <name, value> map for current measures
	private Object[] currentComputedMeasureValues; // current values for computed measures

	private Traversalor traversalor;
	private StopSign stopSign;

	private int[][] selectedPosOfCurSegment;

	private IComputedMeasureHelper computedMeasureHelper;

	private List measureFilters;

	private List cubePosFilters;

	private static Logger logger = Logger.getLogger(FactTableRowIterator.class.getName());

	// All the dimensions, dimIndex and levelIndex are got from it
	private IDimensionResultIterator[] allCubeDimensionResultIterators;
	private IDimension[] allCubeDimensions;

	private int[] subDimensionIndex;
	private boolean existMeasureFilter = false;
	private boolean readMeasure = false;
	private int[] measureSize;
	private Bytes lastCombinedDimensionPosition;
	private int[] lastCurrentPos;
	private boolean lastFilterResult;
	private boolean isDuplicatedRow;

	/**
	 * 
	 * @param factTable
	 * @param dimensionName
	 * @param dimensionPos
	 * @param stopSign
	 * @throws IOException
	 */
	public FactTableRowIterator(FactTable factTable, String[] dimensionName, IDiskArray[] dimensionPos,
			StopSign stopSign) throws IOException {
		this(factTable, dimensionName, dimensionPos, null, null, stopSign);
	}

	/**
	 * 
	 * @param factTable
	 * @param dimensionName
	 * @param dimensionPos
	 * @param stopSign
	 * @throws IOException
	 */
	public FactTableRowIterator(FactTable factTable, String[] dimensionName, IDiskArray[] dimensionPos,
			IDimension[] allCubeDimensions, IComputedMeasureHelper computedMeasureHelper, StopSign stopSign)
			throws IOException {
		Object[] params = { factTable, dimensionName, dimensionPos, stopSign };
		logger.entering(FactTableRowIterator.class.getName(), "FactTableRowIterator", params);
		this.factTable = factTable;
		this.selectedPos = dimensionPos;
		this.selectedSubDim = new List[factTable.getDimensionInfo().length];
		this.selectedPosOfCurSegment = new int[factTable.getDimensionInfo().length][];
		this.stopSign = stopSign;
		this.measureFilters = new ArrayList();
		this.cubePosFilters = new ArrayList();
		if (allCubeDimensions != null)
			this.allCubeDimensionResultIterators = new IDimensionResultIterator[allCubeDimensions.length];
		this.allCubeDimensions = allCubeDimensions;
		this.computedMeasureHelper = computedMeasureHelper;
		assert dimensionName.length == dimensionPos.length;

		for (int i = 0; i < selectedSubDim.length; i++) {
			this.selectedSubDim[i] = new ArrayList();
		}
		dimensionIndex = new int[factTable.getDimensionInfo().length];
		for (int i = 0; i < dimensionIndex.length; i++) {
			dimensionIndex[i] = -1;
		}
		for (int i = 0; i < dimensionName.length; i++) {
			dimensionIndex[factTable.getDimensionIndex(dimensionName[i])] = i;

		}

		caculateMeasuerSize();

		filterSubDimension();
		this.currentPos = new int[factTable.getDimensionInfo().length];
		this.currentMeasureValues = new Object[factTable.getMeasureInfo().length];
		this.currentMeasureMap = new MeasureMap(this.factTable.getMeasureInfo());
		if (this.computedMeasureHelper != null) {
			computedMeasureInfo = this.computedMeasureHelper.getAllComputedMeasureInfos();
		}
		computeAllMeasureInfo();

		nextSegment();

		logger.exiting(FactTableRowIterator.class.getName(), "FactTableRowIterator");
	}

	/**
	 * Filter sub dimensions by dimension position array. The filter result is saved
	 * in the variable selectedSubDim.
	 * 
	 * @throws IOException
	 */
	private void filterSubDimension() throws IOException {
		DimensionDivision[] dimensionDivisions = factTable.getDimensionDivision();
		SelectedSubDimension selectedSubDimension = null;
		int[] selectedSubDimensionCount = new int[selectedSubDim.length];

		for (int i = 0; i < selectedSubDim.length; i++) {
			int pointer = 0;
			for (int j = 0; j < dimensionDivisions[i].getRanges().length; j++) {
				if (dimensionIndex[i] > -1) {
					while (pointer < selectedPos[dimensionIndex[i]].size()
							&& ((Integer) selectedPos[dimensionIndex[i]].get(pointer))
									.intValue() < dimensionDivisions[i].getRanges()[j].start) {
						pointer++;
					}
					if (pointer >= selectedPos[dimensionIndex[i]].size()) {
						break;
					}
					if (((Integer) selectedPos[dimensionIndex[i]].get(pointer))
							.intValue() > dimensionDivisions[i].getRanges()[j].end) {
						continue;
					}
					selectedSubDimension = new SelectedSubDimension();
					selectedSubDimension.subDimensionIndex = j;
					selectedSubDimension.start = pointer;
					while (pointer < selectedPos[dimensionIndex[i]].size()
							&& ((Integer) selectedPos[dimensionIndex[i]].get(pointer))
									.intValue() <= dimensionDivisions[i].getRanges()[j].end) {
						pointer++;
					}
					selectedSubDimension.end = pointer - 1;
					selectedSubDim[i].add(selectedSubDimension);
				} else {
					selectedSubDimension = new SelectedSubDimension();
					selectedSubDimension.subDimensionIndex = j;
					selectedSubDimension.start = -1;
					selectedSubDimension.end = -1;
					selectedSubDim[i].add(selectedSubDimension);
				}
			}
			selectedSubDimensionCount[i] = selectedSubDim[i].size();
		}
		traversalor = new Traversalor(selectedSubDimensionCount);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.impl.facttable.IFactTableRowIterator#
	 * next()
	 */
	public boolean next() throws IOException, DataException {
		while (!stopSign.isStopped()) {
			try {
				if (currentSegment == null) {
					return false;
				}
				Bytes combinedDimensionPosition = currentSegment.readBytes();
				if (this.lastCombinedDimensionPosition == null) {
					this.lastCombinedDimensionPosition = combinedDimensionPosition;
					currentPos = factTable.getCombinedPositionCalculator().calculateDimensionPosition(subDimensionIndex,
							combinedDimensionPosition.bytesValue());
					this.lastCurrentPos = currentPos;
					this.isDuplicatedRow = false;
				} else {
					if (this.lastCombinedDimensionPosition.equals(combinedDimensionPosition)) {
						currentPos = this.lastCurrentPos;
						this.isDuplicatedRow = true;
					} else {
						this.lastCombinedDimensionPosition = combinedDimensionPosition;
						currentPos = factTable.getCombinedPositionCalculator()
								.calculateDimensionPosition(subDimensionIndex, combinedDimensionPosition.bytesValue());
						this.lastCurrentPos = currentPos;
						this.isDuplicatedRow = false;
					}
				}
				readMeasure = false;
				if (!isSelectedRow()) {
					if (!readMeasure) {
						if (!skipMeasure())
							break;
					}
					continue;
				} else {
					if (!readMeasure)
						readMeasure();
					return true;
				}
			} catch (EOFException e) {
				break;
			}
		}
		if (stopSign.isStopped() || !nextSegment()) {
			return false;
		}
		return next();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.impl.facttable.IFactTableRowIterator#
	 * isDuplicatedRow()
	 */
	public boolean isDuplicatedRow() {
		return this.isDuplicatedRow;
	}

	public Member getMember(int dimIndex, int levelIndex) throws DataException, IOException {
//		String dimensionName = allCubeDimensionResultIterators[dimIndex].getDimesion( )
//				.getName( );
//		int indexInFact = getDimensionIndex( dimensionName );
		try {
			return getLevelObject(dimIndex, levelIndex, getDimensionPosition(dimIndex));
		} catch (BirtException e) {
			throw DataException.wrap(e);
		}
	}

	/**
	 * 
	 * @param iteratorIndex
	 * @param levelIndex
	 * @param dimensionPosition
	 * @return
	 * @throws BirtException
	 * @throws IOException
	 */
	private Member getLevelObject(int dimIndex, int levelIndex, int dimensionPosition)
			throws BirtException, IOException {
		checkAndInitDimIterator(dimIndex);
		allCubeDimensionResultIterators[dimIndex].seek(dimensionPosition);
		return allCubeDimensionResultIterators[dimIndex].getLevelMember(levelIndex);
	}

	/**
	 * 
	 * @throws DataException
	 * @throws IOException
	 */
	public void close() throws DataException, IOException {
		if (this.computedMeasureHelper != null)
			this.computedMeasureHelper.cleanUp();
		if (allCubeDimensionResultIterators != null) {
			for (int i = 0; i < allCubeDimensionResultIterators.length; i++)
				try {
					if (allCubeDimensionResultIterators[i] != null)
						allCubeDimensionResultIterators[i].close();
				} catch (BirtException e) {
					throw DataException.wrap(e);
				}
		}
		if (this.currentSegment != null)
			this.currentSegment.close();
	}

	/**
	 * 
	 * @return
	 */
	private int[] getSubDimensionIndex() {
		int[] result = new int[selectedSubDim.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = ((SelectedSubDimension) selectedSubDim[i].get(currentSubDim[i])).subDimensionIndex;
		}
		return result;
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 * @throws DataException
	 */
	private boolean isSelectedRow() throws IOException, DataException {
		if (!this.isDuplicatedRow) {
			for (int i = 0; i < currentPos.length; i++) {
				if (dimensionIndex[i] != -1) {
					if (Arrays.binarySearch(selectedPosOfCurSegment[i], currentPos[i]) < 0) {
						lastFilterResult = false;
						return false;
					}
				}
			}
			for (int i = 0; i < cubePosFilters.size(); i++) {
				if (!((CubePosFilterHelper) cubePosFilters.get(i)).getFilterResult(currentPos)) {
					lastFilterResult = false;
					return false;
				}
			}
			lastFilterResult = true;
		} else {
			if (!lastFilterResult)
				return false;
		}
		if (existMeasureFilter) {
			readMeasure();
			for (int i = 0; i < measureFilters.size(); i++) {
				IJSFacttableFilterEvalHelper measureFilter = (IJSFacttableFilterEvalHelper) measureFilters.get(i);
				if (!measureFilter.evaluateFilter(currentMeasureMap))
					return false;
			}
		}

		return true;
	}

	/**
	 * 
	 * @return
	 */
	private void caculateMeasuerSize() {
		this.measureSize = new int[factTable.getMeasureInfo().length];
		for (int i = 0; i < factTable.getMeasureInfo().length; i++) {
			if (factTable.getMeasureInfo()[i].getDataType() == DataType.DOUBLE_TYPE)
				measureSize[i] = 8;
			else if (factTable.getMeasureInfo()[i].getDataType() == DataType.INTEGER_TYPE)
				measureSize[i] = 4;
			else
				measureSize[i] = -1;
		}
	}

	/**
	 * 
	 * @throws IOException
	 * @throws DataException
	 */
	private void readMeasure() throws IOException, DataException {
		for (int i = 0; i < this.currentMeasureValues.length; i++) {
			currentMeasureValues[i] = DocumentObjectUtil.readValue(currentSegment,
					factTable.getMeasureInfo()[i].getDataType());
		}
		currentMeasureMap.setMeasureValue(currentMeasureValues);
		if (computedMeasureHelper != null) {
			try {
				currentComputedMeasureValues = computedMeasureHelper.computeMeasureValues(currentMeasureMap);
			} catch (DataException e) {
				throw new DataException(ResourceConstants.FAIL_COMPUTE_COMPUTED_MEASURE_VALUE, e);
			}
		}
		readMeasure = true;
	}

	private boolean skipMeasure() throws IOException, DataException {
		for (int i = 0; i < this.measureSize.length; i++) {
			if (measureSize[i] <= 0) {
				DocumentObjectUtil.readValue(currentSegment, factTable.getMeasureInfo()[i].getDataType());
			} else {
				byte nullSign = currentSegment.readByte();
				if (nullSign != 0) {
					if (currentSegment.skipBytes(measureSize[i]) == -1)
						return false;
				}
			}
		}
		return true;
	}

	/**
	 * Moves down one segment from its current segment of the iterator.
	 * 
	 * @return
	 * @throws IOException
	 */
	private boolean nextSegment() throws IOException {
		while (true) {
			if (stopSign.isStopped()) {
				return false;
			}
			if (!traversalor.next()) {
				return false;
			}
			currentSubDim = traversalor.getIntArray();
			subDimensionIndex = getSubDimensionIndex();
			String FTSUDocName = FTSUDocumentObjectNamingUtil
					.getDocumentObjectName(NamingUtil.getFactTableName(factTable.getName()), subDimensionIndex);
			if (!factTable.getDocumentManager().exist(FTSUDocName)) {
				continue;
			}

			if (currentSegment != null)
				currentSegment.close();

			currentSegment = factTable.getDocumentManager().openDocumentObject(FTSUDocName);

			for (int i = 0; i < dimensionIndex.length; i++) {
				if (dimensionIndex[i] != -1) {
					SelectedSubDimension selectedSubDimension = ((SelectedSubDimension) selectedSubDim[i]
							.get(currentSubDim[i]));
					selectedPosOfCurSegment[i] = new int[selectedSubDimension.end - selectedSubDimension.start + 1];
					for (int j = 0; j < selectedSubDimension.end - selectedSubDimension.start + 1; j++) {
						selectedPosOfCurSegment[i][j] = ((Integer) selectedPos[dimensionIndex[i]]
								.get(selectedSubDimension.start + j)).intValue();
					}
				}
			}
			break;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.impl.facttable.IFactTableRowIterator#
	 * getDimensionCount()
	 */
	public int getDimensionCount() {
		return factTable.getDimensionInfo().length;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.impl.facttable.IFactTableRowIterator#
	 * getDimensionIndex(java.lang.String)
	 */
	public int getDimensionIndex(String dimensionName) {
		return factTable.getDimensionIndex(dimensionName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.impl.facttable.IFactTableRowIterator#
	 * getMeasureIndex(java.lang.String)
	 */
	public int getMeasureIndex(String measureName) {
		int reValue = factTable.getMeasureIndex(measureName);
		if (reValue < 0 && computedMeasureInfo != null) {
			for (int i = 0; i < computedMeasureInfo.length; i++) {
				if (measureName.equals(computedMeasureInfo[i].getMeasureName())) {
					reValue = i + factTable.getMeasureInfo().length;
					break;
				}
			}
		}
		return reValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.impl.facttable.IFactTableRowIterator#
	 * getDimensionPosition(int)
	 */
	public int getDimensionPosition(int dimensionIndex) {
		return currentPos[dimensionIndex];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.impl.facttable.IFactTableRowIterator#
	 * getDimensionPosition()
	 */
	public int[] getDimensionPosition() {
		return currentPos;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.impl.facttable.IFactTableRowIterator#
	 * getMeasureCount()
	 */
	public int getMeasureCount() {
		if (computedMeasureInfo != null) {
			return factTable.getMeasureInfo().length + computedMeasureInfo.length;
		} else {
			return factTable.getMeasureInfo().length;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.impl.facttable.IFactTableRowIterator#
	 * getMeasure(int)
	 */
	public Object getMeasure(int measureIndex) {
		if (measureIndex < currentMeasureValues.length) {
			return currentMeasureValues[measureIndex];
		} else {
			if (currentComputedMeasureValues != null
					&& (measureIndex - currentMeasureValues.length) < currentComputedMeasureValues.length) {
				return currentComputedMeasureValues[measureIndex - currentMeasureValues.length];
			} else {
				return null;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.impl.facttable.IFactTableRowIterator#
	 * getMeasureInfo()
	 */
	public MeasureInfo[] getMeasureInfos() {
		return allMeasureInfo;
	}

	/**
	 * 
	 * @param measureList
	 */
	public void addMeasureFilter(IJSFacttableFilterEvalHelper measureFilter) {
		measureFilters.add(measureFilter);
		existMeasureFilter = true;
	}

	/**
	 * 
	 * @param cubePosFilter
	 */
	public void addCubePosFilter(ICubePosFilter cubePosFilter) {
		cubePosFilters.add(new CubePosFilterHelper(factTable, cubePosFilter));
	}

	/**
	 * 
	 */
	private void computeAllMeasureInfo() {
		int len = factTable.getMeasureInfo().length;
		if (computedMeasureInfo != null) {
			len = len + computedMeasureInfo.length;
		}
		allMeasureInfo = new MeasureInfo[len];
		System.arraycopy(factTable.getMeasureInfo(), 0, allMeasureInfo, 0, factTable.getMeasureInfo().length);
		if (computedMeasureInfo != null) {
			System.arraycopy(computedMeasureInfo, 0, allMeasureInfo, factTable.getMeasureInfo().length,
					computedMeasureInfo.length);
		}
	}

	/**
	 * 
	 * @param index
	 * @throws IOException
	 */
	void checkAndInitDimIterator(int index) throws IOException {
		if (allCubeDimensionResultIterators[index] != null)
			return;
		allCubeDimensionResultIterators[index] = new DimensionResultIterator((Dimension) allCubeDimensions[index],
				allCubeDimensions[index].findAll(), stopSign);
	}

	class MeasureMap implements IFacttableRow {
		private MeasureInfo[] measureInfos = null;
		private Object[] measureValues = null;

		/**
		 * 
		 * @param measureInfo
		 */
		MeasureMap(MeasureInfo[] measureInfo) {
			this.measureInfos = measureInfo;
		}

		/**
		 * 
		 * @param measureValues
		 */
		void setMeasureValue(Object[] measureValues) {
			this.measureValues = measureValues;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.birt.data.engine.olap.data.api.IMeasureList#getMeasureValue(java.
		 * lang.String)
		 */
		public Object getMeasureValue(String measureName) {
			for (int i = 0; i < measureInfos.length; i++) {
				if (measureInfos[i].getMeasureName().equals(measureName)) {
					return measureValues[i];
				}
			}
			return null;
		}

		public Object getLevelAttributeValue(String dimensionName, String levelName, String attributeName)
				throws DataException, IOException {
			int dimensionIndex = getDimensionIndex(dimensionName);
			if (dimensionIndex < 0) {
				return null;
			}
			Member member;
			try {
				member = getLevelMember(dimensionIndex, levelName);
			} catch (BirtException e) {
				throw DataException.wrap(e);
			}
			checkAndInitDimIterator(dimensionIndex);
			int attributeIndex = allCubeDimensionResultIterators[dimensionIndex].getLevelAttributeIndex(levelName,
					attributeName);
			if (member != null && attributeIndex >= 0)
				return member.getAttributes()[attributeIndex];
			return null;
		}

		public Object[] getLevelKeyValue(String dimensionName, String levelName) throws DataException, IOException {
			Member member;
			try {
				member = getLevelMember(dimensionName, levelName);
			} catch (BirtException e) {
				throw DataException.wrap(e);
			}
			if (member != null)
				return member.getKeyValues();
			return null;
		}

		private Member getLevelMember(String dimensionName, String levelName) throws BirtException, IOException {
			int dimIndex = getDimensionIndex(dimensionName);
			return getLevelMember(dimIndex, levelName);
		}

		private Member getLevelMember(int dimIndex, String levelName) throws BirtException, IOException {
			int levelIndex = -1;
			if (dimIndex >= 0) {
				checkAndInitDimIterator(dimIndex);
				IDimensionResultIterator itr = allCubeDimensionResultIterators[dimIndex];
				levelIndex = itr.getLevelIndex(levelName);
				if (levelIndex >= 0)
					return getMember(dimIndex, levelIndex);
			}

			return null;
		}
	}

}

class SelectedSubDimension {
	int subDimensionIndex;
	int start;
	int end;
}

class CubePosFilterHelper {
	private int[] filterDimensionIndexes;
	private ICubePosFilter cubePosFilter;
	private int[] filterDimPos;

	CubePosFilterHelper(FactTable factTable, ICubePosFilter cubePosFilter) {
		String[] filterDimensionNames = cubePosFilter.getFilterDimensionNames();
		filterDimensionIndexes = new int[filterDimensionNames.length];
		for (int i = 0; i < filterDimensionNames.length; i++) {
			filterDimensionIndexes[i] = factTable.getDimensionIndex(filterDimensionNames[i]);
		}
		this.cubePosFilter = cubePosFilter;
		this.filterDimPos = new int[filterDimensionNames.length];
	}

	boolean getFilterResult(int[] dimensionPositions) {
		for (int i = 0; i < filterDimensionIndexes.length; i++) {
			filterDimPos[i] = dimensionPositions[filterDimensionIndexes[i]];
		}
		return cubePosFilter.getFilterResult(filterDimPos);
	}
}

/*
 *************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.data.engine.olap.data.impl.aggregation;

import java.io.IOException;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.DataResourceHandle;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.olap.data.api.IComputedMeasureHelper;
import org.eclipse.birt.data.engine.olap.data.api.IDimensionResultIterator;
import org.eclipse.birt.data.engine.olap.data.api.ILevel;
import org.eclipse.birt.data.engine.olap.data.api.MeasureInfo;
import org.eclipse.birt.data.engine.olap.data.api.cube.TimeDimensionUtil;
import org.eclipse.birt.data.engine.olap.data.impl.DimColumn;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Member;
import org.eclipse.birt.data.engine.olap.data.impl.facttable.IFactTableRowIterator;
import org.eclipse.birt.data.engine.olap.util.filter.IFacttableRow;

/**
 * The data prepared for aggregation is from cube
 */
public class DataSetFromOriginalCube implements IDataSet4Aggregation {

	protected IFactTableRowIterator factTableRowIterator;

	// All the dimensions, dimIndex and levelIndex are got from it
	private IDimensionResultIterator[] dimensionResultIterators;

	private IComputedMeasureHelper computedMeasureHelper;

	private IFacttableRow facttableRow = null;

	public DataSetFromOriginalCube(IFactTableRowIterator factTableRowIterator,
			IDimensionResultIterator[] dimensionResultIterators, IComputedMeasureHelper computedMeasureHelper) {
		this.dimensionResultIterators = dimensionResultIterators;
		this.factTableRowIterator = factTableRowIterator;
		this.computedMeasureHelper = computedMeasureHelper;
		this.facttableRow = new FacttableRowForComputedMeasure();

	}

	@Override
	public MetaInfo getMetaInfo() {
		return new IDataSet4Aggregation.MetaInfo() {

			@Override
			public String[] getAttributeNames(int dimIndex, int levelIndex) {
				IDimensionResultIterator itr = dimensionResultIterators[dimIndex];
				if (!itr.getDimesion().isTime()) {
					return itr.getDimesion().getHierarchy().getLevels()[levelIndex].getAttributeNames();
				} else {
					return null;
				}
			}

			@Override
			public ColumnInfo getColumnInfo(DimColumn dimColumn) throws DataException {
				String dimensionName = dimColumn.getDimensionName();
				String levelName = dimColumn.getLevelName();
				String columnName = dimColumn.getColumnName();

				int dimIndex = getDimensionIndex(dimensionName);
				if (dimIndex < 0) {
					throw new DataException(
							DataResourceHandle.getInstance().getMessage(ResourceConstants.NONEXISTENT_DIMENSION)
									+ dimensionName);
				}
				IDimensionResultIterator itr = dimensionResultIterators[dimIndex];
				int levelIndex = itr.getLevelIndex(levelName);
				if (levelIndex < 0) {
					throw new DataException(
							DataResourceHandle.getInstance().getMessage(ResourceConstants.NONEXISTENT_LEVEL) + "<"
									+ dimensionName + " , " + levelName + ">");
				}
				ILevel levelInfo = itr.getDimesion().getHierarchy().getLevels()[levelIndex];
				int columnIndex = -1;
				int dataType = DataType.UNKNOWN_TYPE;
				boolean isKey = false;
				for (int i = 0; i < levelInfo.getKeyNames().length; i++) {
					if (levelInfo.getKeyNames()[i].equals(columnName)) {
						columnIndex = i;
						dataType = levelInfo.getKeyDataType(columnName);
						isKey = true;
						break;
					} else if (levelInfo.getName().equals(columnName)) {
						columnIndex = i;
						dataType = levelInfo.getKeyDataType(levelInfo.getKeyNames()[0]);
						isKey = true;
						break;
					}
				}
				if (!isKey && levelInfo.getAttributeNames() != null) {
					for (int i = 0; i < levelInfo.getAttributeNames().length; i++) {
						if (levelInfo.getAttributeNames()[i].equals(columnName)) {
							columnIndex = i;
							dataType = levelInfo.getAttributeDataType(columnName);
							break;
						}
					}
				}
				if (columnIndex < 0) {
					throw new DataException(
							DataResourceHandle.getInstance().getMessage(ResourceConstants.NONEXISTENT_KEY_OR_ATTR) + "<"
									+ dimensionName + " , " + levelName + " , " + columnName + ">");
				}
				return new ColumnInfo(dimIndex, levelIndex, columnIndex, dataType, isKey);
			}

			@Override
			public int getDimensionIndex(String dimensionName) {
				for (int i = 0; i < dimensionResultIterators.length; i++) {
					if (dimensionResultIterators[i].getDimesion().getName().equals(dimensionName)) {
						return i;
					}
				}
				return -1;
			}

			@Override
			public String[] getKeyNames(int dimIndex, int levelIndex) {
				IDimensionResultIterator itr = dimensionResultIterators[dimIndex];
				if (itr.getDimesion().isTime()) {
					return new String[] { TimeDimensionUtil.getFieldName(levelIndex) };
				} else {
					return itr.getDimesion().getHierarchy().getLevels()[levelIndex].getKeyNames();
				}
			}

			@Override
			public int getLevelIndex(String dimensionName, String levelName) {
				int dimIndex = getDimensionIndex(dimensionName);
				if (dimIndex >= 0) {
					IDimensionResultIterator itr = dimensionResultIterators[dimIndex];
					return itr.getLevelIndex(levelName);
				}
				return -1;
			}

			@Override
			public int getMeasureIndex(String measureName) {
				if (measureName == null) {
					return -1;
				}
				MeasureInfo[] measureInfo = getMeasureInfos();
				for (int i = 0; i < measureInfo.length; i++) {
					if (measureName.equals(measureInfo[i].getMeasureName())) {
						return i;
					}
				}
				return -1;
			}

			@Override
			public MeasureInfo[] getMeasureInfos() {
				if (computedMeasureHelper != null && computedMeasureHelper.getAllComputedMeasureInfos() != null) {
					MeasureInfo[] cubeMeasureInfo = factTableRowIterator.getMeasureInfos();
					MeasureInfo[] computedMeasureInfo = computedMeasureHelper.getAllComputedMeasureInfos();

					MeasureInfo[] result = new MeasureInfo[computedMeasureInfo.length + cubeMeasureInfo.length];
					System.arraycopy(cubeMeasureInfo, 0, result, 0, cubeMeasureInfo.length);
					System.arraycopy(computedMeasureInfo, 0, result, cubeMeasureInfo.length,
							computedMeasureInfo.length);
					return result;
				} else {
					return factTableRowIterator.getMeasureInfos();
				}
			}

		};
	}

	@Override
	public boolean next() throws DataException, IOException {
		return factTableRowIterator.next();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.impl.aggregation.IDataSet4Aggregation#
	 * isDuplicatedRow()
	 */
	@Override
	public boolean isDuplicatedRow() {
		return factTableRowIterator.isDuplicatedRow();
	}

	@Override
	public int[] getDimensionPosition() {
		return factTableRowIterator.getDimensionPosition();
	}

	@Override
	public Object getMeasureValue(int measureIndex) throws DataException {
		if (measureIndex < factTableRowIterator.getMeasureCount()) {
			return factTableRowIterator.getMeasure(measureIndex);
		} else if (computedMeasureHelper != null) {
			Object[] computedMeasure = computedMeasureHelper.computeMeasureValues(facttableRow);
			if (computedMeasure != null
					&& measureIndex < factTableRowIterator.getMeasureCount() + computedMeasure.length) {
				return computedMeasure[measureIndex - factTableRowIterator.getMeasureCount()];
			}
			return null;
		}
		return null;
	}

	@Override
	public Member getMember(int dimIndex, int levelIndex) throws DataException, IOException {
		String dimensionName = dimensionResultIterators[dimIndex].getDimesion().getName();
		int indexInFact = factTableRowIterator.getDimensionIndex(dimensionName);
		try {
			return getLevelObject(dimIndex, levelIndex, factTableRowIterator.getDimensionPosition(indexInFact));
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
		if (dimensionResultIterators[dimIndex].locate(dimensionPosition)) {
			return dimensionResultIterators[dimIndex].getLevelMember(levelIndex);
		} else {
			return null;
		}

	}

	public class FacttableRowForComputedMeasure implements IFacttableRow {

		@Override
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

			int attributeIndex = dimensionResultIterators[dimensionIndex].getLevelAttributeIndex(levelName,
					attributeName);
			if (member != null && attributeIndex >= 0) {
				return member.getAttributes()[attributeIndex];
			}
			return null;
		}

		@Override
		public Object[] getLevelKeyValue(String dimensionName, String levelName) throws DataException, IOException {
			Member member;
			try {
				member = getLevelMember(dimensionName, levelName);
			} catch (BirtException e) {
				throw DataException.wrap(e);
			}
			if (member != null) {
				return member.getKeyValues();
			}
			return null;
		}

		private Member getLevelMember(String dimensionName, String levelName) throws BirtException, IOException {
			int dimIndex = getDimensionIndex(dimensionName);
			return getLevelMember(dimIndex, levelName);
		}

		private Member getLevelMember(int dimIndex, String levelName) throws BirtException, IOException {
			int levelIndex = -1;
			if (dimIndex >= 0) {
				IDimensionResultIterator itr = dimensionResultIterators[dimIndex];
				levelIndex = itr.getLevelIndex(levelName);
				if (levelIndex >= 0) {
					return getMember(dimIndex, levelIndex);
				}
			}

			return null;
		}

		private int getDimensionIndex(String dimensionName) {
			int dimIndex = -1;
			for (int i = 0; i < dimensionResultIterators.length; i++) {
				if (dimensionResultIterators[i].getDimesion().getName().equals(dimensionName)) {
					dimIndex = i;
				}
			}
			return dimIndex;
		}

		@Override
		public Object getMeasureValue(String measureName) throws DataException {
			return factTableRowIterator.getMeasure(factTableRowIterator.getMeasureIndex(measureName));
		}
	}

	@Override
	public void close() throws DataException, IOException {
		factTableRowIterator.close();
		factTableRowIterator = null;
		for (int i = 0; i < dimensionResultIterators.length; i++) {
			try {
				dimensionResultIterators[i].close();
			} catch (BirtException e) {
				throw DataException.wrap(e);
			}
		}
		dimensionResultIterators = null;
	}
}

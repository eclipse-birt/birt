/*******************************************************************************
 * Copyright (c) 2004, 2008Actuate Corporation.
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

package org.eclipse.birt.report.engine.emitter.excel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

public class DataCache {
	/**
	 * columns is an ArrayList. Its elements are each column. Each column is
	 * also an arrayList. Its elements are the rows in the column.
	 */
	protected static Logger logger = Logger.getLogger(DataCache.class.getName());
	private List<ArrayList<SheetData>> columns = new ArrayList<ArrayList<SheetData>>();
	private int maxColumnCount;
	private int maxRowIndex = 0;
	private int offset = 0;
	private Map<Integer, Float> rowIndex2Height = new HashMap<Integer, Float>();

	public DataCache(DataCache cache) {
		for (int i = 0; i < cache.columns.size(); i++) {
			columns.add(new ArrayList<SheetData>());
		}
		this.maxColumnCount = cache.maxColumnCount;
	}

	public DataCache(int offset, int maxColumnCount) {
		columns.add(new ArrayList<SheetData>());
		this.maxColumnCount = maxColumnCount;
	}

	public void insertColumns(int startColumn, int columnCount) {
		if (columnCount == 0) {
			return;
		}

		int startPosition = startColumn + 1;

		for (int i = startPosition; i <= startColumn + columnCount; i++) {
			if (i < maxColumnCount) {
				columns.add(i, new ArrayList<SheetData>());
			}
		}
	}

	public void insertColumns(int columnCount) {
		if (columnCount == 0) {
			return;
		}

		int currentColumnCount = columns.size();
		for (int i = 0; i <= columnCount; i++) {
			if (i + currentColumnCount < maxColumnCount) {
				columns.add(new ArrayList<SheetData>());
			}
		}
	}

	public void addData(int col, SheetData data) {
		if (col < getColumnCount()) {
			int rowIndex = data.getRowIndex();
			columns.get(col).add(data);
			maxRowIndex = maxRowIndex > rowIndex ? maxRowIndex : rowIndex;
			BookmarkDef bookmark = data.getBookmark();
			if (bookmark != null) {
				bookmark.setStartColumn(data.getStartX());
				bookmark.setStartRow(rowIndex);
			}
		}
	}

	public int getMaxRow() {
		return maxRowIndex;
	}

	protected boolean valid(int row, int col) {
		if (col >= getColumnCount()) {
			return false;
		}
		return true;
	}

	public int getColumnCount() {
		return columns.size();
	}

	/**
	 * @param column
	 * @return
	 */
	public int getMaxRowIndex(int column) {
		SheetData lastData = getColumnLastData(column);
		if (lastData != null)
			return lastData.getRowIndex();
		return 0;
	}

	public void setRowHeight(int rowIndex, float height) {
		if (!rowIndex2Height.containsKey(rowIndex) || height > rowIndex2Height.get(rowIndex))
			rowIndex2Height.put(rowIndex, height);
	}

	public float getRowHeight(int rowIndex) {
		if (rowIndex2Height.containsKey(rowIndex))
			return rowIndex2Height.get(rowIndex);
		return 0f;
	}

	public boolean hasRowHeight(int rowIndex) {
		if (rowIndex2Height.containsKey(rowIndex) && rowIndex2Height.get(rowIndex) != 0)
			return true;
		else
			return false;
	}

	/**
	 * @param index
	 * @return
	 */
	public SheetData getColumnLastData(int index) {
		if (index < getColumnCount()) {
			ArrayList<SheetData> columnDatas = columns.get(index);
			if (!columnDatas.isEmpty())
				return columnDatas.get(columnDatas.size() - 1);
		}
		return null;
	}

	public Iterator<SheetData[]> getRowIterator() {
		return getRowIterator(null, null);
	}

	public Iterator<SheetData[]> getRowIterator(DataFilter filter, RowIndexAdjuster rowIndexAdjuster) {
		return new DataCacheIterator(filter, rowIndexAdjuster);
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getOffset() {
		return this.offset;
	}

	protected class DataCacheIterator implements Iterator<SheetData[]> {

		private int[] columnIndexes;
		private int rowIndex = 1;
		private DataFilter dataFilter;
		private RowIndexAdjuster rowIndexAdjuster;

		public DataCacheIterator(DataFilter dataFilter, RowIndexAdjuster rowIndexAdjuster) {
			this.dataFilter = dataFilter;
			this.rowIndexAdjuster = rowIndexAdjuster;
			columnIndexes = new int[columns.size()];
		}

		public boolean hasNext() {
			return rowIndex <= maxRowIndex;
		}

		public SheetData[] next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			SheetData[] rowDatas = new SheetData[columnIndexes.length];
			for (int i = 0; i < columnIndexes.length; i++) {
				ArrayList<SheetData> columnData = columns.get(i);
				int cursor = columnIndexes[i];
				int size = columnData.size();
				for (int j = cursor; j < size; j++) {
					SheetData data = columnData.get(j);
					int dataRowIndex = getRowIndex(data);
					if (dataRowIndex == rowIndex) {
						if (dataFilter == null || dataFilter.accept(data)) {
							rowDatas[i] = data;
						}
						columnIndexes[i] = j + 1;
						break;
					} else if (dataRowIndex > rowIndex) {
						columnIndexes[i] = j;
						break;
					}
				}
			}
			rowIndex++;
			return rowDatas;
		}

		protected int getRowIndex(SheetData data) {
			if (rowIndexAdjuster != null) {
				return rowIndexAdjuster.getRowIndex(data);
			}
			return data.getRowIndex();
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	public static interface RowIndexAdjuster {

		int getRowIndex(SheetData data);
	}

	public static interface DataFilter {

		// The data won't be output if it's not accept.
		boolean accept(SheetData data);
	}
}

/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.engine.emitter.ods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.emitter.EmitterUtil;
import org.eclipse.birt.report.engine.emitter.ods.layout.OdsLayoutEngine;

public class DataCache {
	/**
	 * columns is an ArrayList. Its elements are each column. Each column is also an
	 * arrayList. Its elements are the rows in the column.
	 */
	private List<ArrayList<SheetData>> columns = new ArrayList<ArrayList<SheetData>>();
	private int width;
	protected static Logger logger = Logger.getLogger(EmitterUtil.class.getName());

	/**
	 * All the bookmarks defined in this ODS file.
	 */
	private List<BookmarkDef> bookmarks = new ArrayList<BookmarkDef>();
	private int maxRowIndex = 0;

	private Map<Integer, Float> rowIndex2Height = new HashMap<Integer, Float>();

	public DataCache(int width, int height) {
		columns.add(new ArrayList<SheetData>());
		this.width = width;
	}

	public void insertColumns(int startColumn, int columnCount) {
		if (columnCount == 0) {
			return;
		}

		int startPosition = startColumn + 1;

		for (int i = startPosition; i <= startColumn + columnCount; i++) {
			if (i < width) {
				columns.add(i, new ArrayList<SheetData>());
			}
		}
	}

	public void addData(int col, SheetData data) {
		if (col < getColumnCount()) {
			int rowIndex = data.getRowIndex();
			columns.get(col).add(data);
			maxRowIndex = maxRowIndex > rowIndex ? maxRowIndex : rowIndex;
			BookmarkDef bookmark = data.getBookmark();
			if (bookmark == null) {
				return;
			}
			bookmark.setColumnNo(col + 1);
			bookmark.setRowNo(rowIndex);
			bookmarks.add(bookmark);
		}
	}

	public void clearCachedSheetData() {
		for (int i = 0; i < getColumnCount(); i++) {
			columns.set(i, new ArrayList<SheetData>());
		}
		bookmarks.clear();
		maxRowIndex = 1;
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

	public List<BookmarkDef> getBookmarks() {
		return bookmarks;
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
			return Math.max(rowIndex2Height.get(rowIndex), OdsLayoutEngine.DEFAULT_ROW_HEIGHT);
		return OdsLayoutEngine.DEFAULT_ROW_HEIGHT;
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
		return new DataCacheIterator();
	}

	protected class DataCacheIterator implements Iterator<SheetData[]> {

		private int[] columnIndexes;
		private int rowIndex = 1;

		public DataCacheIterator() {
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
					int dataRowIndex = data.getRowIndex();
					if (dataRowIndex == rowIndex) {
						rowDatas[i] = data;
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

		public void remove() {
			throw new UnsupportedOperationException();
		}

	}
}
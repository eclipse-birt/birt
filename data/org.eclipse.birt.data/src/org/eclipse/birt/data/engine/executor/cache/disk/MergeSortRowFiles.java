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

package org.eclipse.birt.data.engine.executor.cache.disk;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 * Provide the service of sorting objects existed in several files. The objects
 * in every file are sorted. It makes the reading objects transparent to
 * DiskMergeSort.
 */
class MergeSortRowFiles implements IRowIterator {
	private IRowIterator[] subRowIterators = null;
	private MergeSortUtil mergeSortUtil = null;
	private ValueIndex[] rowBuffer = null;
	private ValueIndex mValueIndex = null;
	private int rowBufferSize = 0;

	/**
	 * @param rowFiles      The objects in every file are sorted.
	 * @param mergeSortUtil
	 */
	MergeSortRowFiles(IRowIterator[] subRowIterators, MergeSortUtil mergeSortUtil) {
		assert subRowIterators != null;

		this.subRowIterators = subRowIterators;
		this.mergeSortUtil = mergeSortUtil;
		this.mValueIndex = new ValueIndex(null, 0, mergeSortUtil.getComparator());
	}

	/*
	 * Moves the cursor to the first object in this MergeSortObjectFile object.
	 * 
	 * @see org.eclipse.birt.data.engine.executor.cache.IRowIterator#first()
	 */
	public void reset() throws DataException {
		for (int i = 0; i < subRowIterators.length; i++) {
			subRowIterators[i].reset();
		}

		rowBuffer = null;
	}

	/*
	 * @see org.eclipse.birt.data.engine.executor.cache.IRowIterator#next()
	 */
	public IResultObject fetch() throws IOException, DataException {
		if (rowBuffer == null) {
			prepareFirstFetch();
		}
		if (rowBufferSize == 0) {
			return null;
		}

		ValueIndex reObj = rowBuffer[0];
		IResultObject value = reObj.value;

		IResultObject readValue = subRowIterators[reObj.index].fetch();
		if (readValue == null) {
			rowBufferSize--;
			if (rowBufferSize > 0) {
				ValueIndex[] tBuffer = new ValueIndex[rowBufferSize];
				System.arraycopy(rowBuffer, 1, tBuffer, 0, rowBufferSize);
				rowBuffer = tBuffer;
			}
		} else {
			int pos = 0;
			mValueIndex.value = readValue;
			mValueIndex.index = reObj.index;
			if (rowBufferSize > 1) {
				pos = Arrays.binarySearch(rowBuffer, mValueIndex);

				if (pos < 0)
					pos = (pos + 1) * -1;
				pos--;
				if (pos == -1)
					pos = 0;

				if (pos > 0) {
					System.arraycopy(rowBuffer, 1, rowBuffer, 0, pos);
				}
			}
			rowBuffer[pos] = mValueIndex;
			mValueIndex = reObj;
		}
		return value;
	}

	/**
	 * @throws IOException
	 * @throws DataException
	 */
	private void prepareFirstFetch() throws IOException, DataException {
		rowBuffer = new ValueIndex[subRowIterators.length];

		for (int i = 0; i < rowBuffer.length; i++) {
			IResultObject value = subRowIterators[i].fetch();
			if (value != null)
				rowBuffer[i] = new ValueIndex(value, i, this.mergeSortUtil.getComparator());
			;
		}
		rowBufferSize = 0;
		for (int i = 0; i < rowBuffer.length; i++) {
			if (rowBuffer[i] != null) {
				rowBuffer[rowBufferSize] = rowBuffer[i];
				rowBufferSize++;
			}
		}
		Arrays.sort(rowBuffer, 0, rowBufferSize);

	}

	/*
	 * Delete all the files correlated with this MergeSortObjectFile object.
	 * 
	 * @see org.eclipse.birt.data.engine.executor.cache.IRowIterator#close()
	 */
	public void close() throws DataException {
		for (int i = 0; i < subRowIterators.length; i++) {
			subRowIterators[i].close();
		}

		subRowIterators = null;
	}

	static class ValueIndex implements Comparable {
		IResultObject value;
		int index;
		private Comparator comparator;

		ValueIndex(IResultObject value, int index, Comparator comparator) {
			this.value = value;
			this.index = index;
			this.comparator = comparator;
		}

		public int compareTo(Object o) {
			ValueIndex other = ((ValueIndex) o);
			int result = comparator.compare(value, other.value);
			if (result == 0) {
				if (index > other.index)
					return 1;
				else if (index == other.index)
					return 0;
				else
					return -1;

			}
			return result;
		}
	}

}

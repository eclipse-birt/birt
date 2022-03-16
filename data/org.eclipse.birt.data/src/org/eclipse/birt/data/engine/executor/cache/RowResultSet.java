/*******************************************************************************
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
 *******************************************************************************/
package org.eclipse.birt.data.engine.executor.cache;

import java.util.List;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultObject;
import org.eclipse.birt.data.engine.odi.IResultObjectEvent;

/**
 * This class simulates the mechanism of java.sql.ResultSet. Such an approach is
 * a passive model, which will give caller more flexibility for upper level
 * control. This feature is showed in DiskMergeSort.
 */
public class RowResultSet implements IRowResultSet {
	//
	private List eventList;

	// basic data provider
	private OdiAdapter odiAdpater;

	// result meta data
	private IResultClass resultClass;

	// max rows will be fetched
	private int maxRows;

	// current row index
	private int currIndex;

	private int actualIndex = -1;
	// distinct value flag
	private boolean distinctValueFlag;

	// result object
	private IResultObject lastResultObject;

	private IResultObject nextResultObject;

	private boolean finished = false;

	/**
	 * Construction
	 *
	 * @param query
	 * @param odaResultSet
	 * @param resultClass
	 */
	public RowResultSet(SmartCacheRequest smartCacheRequest) {
		this(smartCacheRequest, smartCacheRequest.getMaxRow());
	}

	public RowResultSet(SmartCacheRequest smartCacheRequest, int maxRow) {
		this.eventList = smartCacheRequest.getEventList();
		this.odiAdpater = smartCacheRequest.getOdiAdapter();
		this.resultClass = smartCacheRequest.getResultClass();

		this.maxRows = maxRow;
		if (maxRows <= 0) {
			maxRows = Integer.MAX_VALUE;
		}

		this.distinctValueFlag = smartCacheRequest.getDistinctValueFlag();
	}

	/**
	 * @return result meta data
	 */
	@Override
	public IResultClass getMetaData() {
		return resultClass;
	}

	/**
	 * Notice the return value of this function is IResultObject. The null value
	 * indicates the cursor exceeds the end of result set.
	 *
	 * @param stopSign
	 * @return next result data
	 * @throws DataException
	 */
	@Override
	public IResultObject next() throws DataException {
		this.beforeNext();
		IResultObject result = doNext();
		this.afterNext();
		return result;
	}

	private IResultObject doNext() throws DataException {
		if (finished) {
			return null;
		}
		if (this.nextResultObject != null) {
			this.lastResultObject = this.nextResultObject;
			this.nextResultObject = null;
			return this.lastResultObject;
		}

		if (currIndex >= maxRows) {
			return null;
		}

		IResultObject odaObject = null;
		while (true) {
			odaObject = fetch();
			if (odaObject == null) {
				break;
			} else {
				actualIndex++;
				if (!processFetchEvent(odaObject, actualIndex) || (this.distinctValueFlag && isDuplicatedObject(odaObject))) {
					continue;
				}
				currIndex++;
				break;
			}
		}

		return odaObject;
	}

	protected IResultObject fetch() throws DataException {
		return odiAdpater.fetch();
	}

	/**
	 * Process onFetchEvent in such a time window that closely after data gotten
	 * from data source and closely before data will be done grouping and sorting
	 *
	 * @param resultObject row object
	 * @return boolean indicate whether passed resultObject is accepted or refused
	 * @throws DataException
	 */
	private boolean processFetchEvent(IResultObject resultObject, int currentIndex) throws DataException {
		assert resultObject != null;

		if (eventList != null) {
			beforeProcessFetchEvent(resultObject, currentIndex);
			try {
				int size = eventList.size();
				for (int i = 0; i < size; i++) {
					IResultObjectEvent onFetchEvent = (IResultObjectEvent) eventList.get(i);
					if (!onFetchEvent.process(resultObject, currentIndex)) {
						return false;
					}
				}
			} finally {
				afterProcessFetchEvent(resultObject, currentIndex);
			}
		}

		return true;
	}

	protected void beforeProcessFetchEvent(IResultObject resultObject, int currentIndex) throws DataException {
		// Template method for subclasses
	}

	protected void afterProcessFetchEvent(IResultObject resultObject, int currentIndex) throws DataException {
		// Template method for subclasses
	}

	/**
	 * @param currRowObject
	 * @return
	 */
	private boolean isDuplicatedObject(IResultObject currRowObject) {
		if (currRowObject.equals(lastResultObject)) {
			return true;
		}

		lastResultObject = currRowObject;
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.executor.cache.IRowResultSet#getIndex()
	 */
	@Override
	public int getIndex() throws DataException {
		if (this.nextResultObject != null) {
			return this.currIndex - 2;
		}
		return this.currIndex - 1;
	}

	public IResultObject getNext() throws DataException {
		if (finished) {
			return null;
		}
		if (nextResultObject != null) {
			return nextResultObject;
		}
		nextResultObject = this.next();
		if (nextResultObject != null) {
			return nextResultObject;
		} else {
			finished = true;
			return null;
		}
	}

	/**
	 * Being called before next() function call.
	 *
	 * @throws DataException
	 */
	protected void beforeNext() throws DataException {
		// Do nothing
	}

	/**
	 * Being called after next() function call.
	 *
	 * @throws DataException
	 */
	protected void afterNext() throws DataException {
		// Do nothing
	}
}

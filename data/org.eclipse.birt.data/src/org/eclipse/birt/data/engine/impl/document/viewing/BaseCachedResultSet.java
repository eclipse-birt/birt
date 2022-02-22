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
package org.eclipse.birt.data.engine.impl.document.viewing;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.cache.ResultSetCache;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.IExecutorHelper;
import org.eclipse.birt.data.engine.impl.document.CacheProvider;
import org.eclipse.birt.data.engine.impl.document.IRDGroupUtil;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 * Abstract class to generate the expression result set when query result is
 * generated from a report document according to newly defined query definition.
 */
abstract class BaseCachedResultSet implements CacheProvider, IResultIterator {
	private int currRowIndex;

	// These four members must be initialized by sub class
	protected int rowCount;
	protected IResultClass resultClass;

	protected ResultSetCache smartCache;
	protected IRDGroupUtil rdGroupUtil;

	/**
	 *
	 */
	BaseCachedResultSet() {
		this.currRowIndex = -1;
	}

	/*
	 * @see org.eclipse.birt.data.engine.executor.transformation.CachedResultSet#
	 * getResultClass()
	 */
	@Override
	public IResultClass getResultClass() throws DataException {
		return resultClass;
	}

	/*
	 * @see org.eclipse.birt.data.engine.odi.IResultIterator#getRowCount()
	 */
	@Override
	public int getRowCount() throws DataException {
		return this.rowCount;
	}

	/*
	 * Move to the row which is available to this cached result set after filering
	 * operation is done.
	 *
	 * @see
	 * org.eclipse.birt.data.engine.executor.transformation.CachedResultSet#next()
	 */
	@Override
	public boolean next() throws DataException {
		boolean hasNext = smartCache.next();

		if (hasNext) {
			this.currRowIndex++;
		} else {
			this.currRowIndex = -1;
		}

		if (rdGroupUtil != null) {
			rdGroupUtil.next(hasNext);
		}
		return hasNext;
	}

	/*
	 * @see org.eclipse.birt.data.engine.executor.transformation.CachedResultSet#
	 * getCurrentResult()
	 */
	@Override
	public IResultObject getCurrentResult() throws DataException {
		return smartCache.getCurrentResult();
	}

	/*
	 * @see org.eclipse.birt.data.engine.executor.transformation.CachedResultSet#
	 * getCurrentResultIndex()
	 */
	@Override
	public int getCurrentResultIndex() throws DataException {
		if (this.getCurrentResult() != null) {
			return this.currRowIndex;
		} else {
			return -1;
		}
	}

	/*
	 * @see org.eclipse.birt.data.engine.executor.transformation.CachedResultSet#
	 * getCurrentGroupIndex(int)
	 */
	@Override
	public int getCurrentGroupIndex(int groupLevel) throws DataException {
		return rdGroupUtil.getCurrentGroupIndex(groupLevel);
	}

	/*
	 * @see org.eclipse.birt.data.engine.executor.transformation.CachedResultSet#
	 * getStartingGroupLevel()
	 */
	@Override
	public int getStartingGroupLevel() throws DataException {
		return rdGroupUtil.getStartingGroupLevel();
	}

	/*
	 * @see org.eclipse.birt.data.engine.executor.transformation.CachedResultSet#
	 * getEndingGroupLevel()
	 */
	@Override
	public int getEndingGroupLevel() throws DataException {
		return rdGroupUtil.getEndingGroupLevel();
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.executor.transformation.CachedResultSet#first(
	 * int)
	 */
	@Override
	public void first(int groupingLevel) throws DataException {
		throw new DataException(ResourceConstants.UNSUPPORT_OPERATION_EXCEPTION);
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.executor.transformation.CachedResultSet#last(
	 * int)
	 */
	@Override
	public void last(int groupingLevel) throws DataException {
		rdGroupUtil.last(groupingLevel);
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.executor.transformation.CachedResultSet#close()
	 */
	@Override
	public void close() throws DataException {
		smartCache.close();
	}

	/*
	 * @see org.eclipse.birt.data.engine.executor.transformation.CachedResultSet#
	 * getGroupStartAndEndIndex(int)
	 */
	@Override
	public int[] getGroupStartAndEndIndex(int groupLevel) throws DataException {
		return rdGroupUtil.getGroupStartAndEndIndex(groupLevel);
	}

	/*
	 * @see org.eclipse.birt.data.engine.odi.IResultIterator#getResultSetCache()
	 */
	@Override
	public ResultSetCache getResultSetCache() {
		return smartCache;
	}

	// -------------------------CacheProvider---------------------------------

	/*
	 * @see org.eclipse.birt.data.engine.impl.document.CacheProvider#getCount()
	 */
	@Override
	public int getCount() {
		return rowCount;
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.impl.document.CacheProvider#getCurrentIndex()
	 */
	@Override
	public int getCurrentIndex() {
		return this.currRowIndex;
	}

	/*
	 * @see org.eclipse.birt.data.engine.impl.document.CacheProvider#moveTo(int)
	 */
	@Override
	public void moveTo(int destIndex) throws DataException {
		assert destIndex >= currRowIndex;

		int forwardSteps = destIndex - currRowIndex;
		for (int i = 0; i < forwardSteps; i++) {
			next();
		}
	}

	/*
	 * @see org.eclipse.birt.data.engine.odi.IResultIterator#getExecutorHelper()
	 */
	@Override
	public IExecutorHelper getExecutorHelper() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.odi.IResultIterator#getAggrValue(java.lang.
	 * String)
	 */
	@Override
	public Object getAggrValue(String aggrName) throws DataException {
		throw new UnsupportedOperationException();
	}
}

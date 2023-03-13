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

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.ResultObject;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 * This class implements IRowResultSet and delegate the behavior of class
 * RowResultSet, besides that it can adjust the IResultObject instance return by
 * its "next()" method according to the given metadata in a limited way.
 */
class ExpandableRowResultSet implements IRowResultSet {
	// result meta data
	private IResultClass resultClass;

	//
	private RowResultSet rowResultSet;

	/**
	 * Construction
	 *
	 * @param query
	 * @param odaResultSet
	 * @param resultClass
	 */
	ExpandableRowResultSet(SmartCacheRequest smartCacheRequest) {
		this.resultClass = smartCacheRequest.getResultClass();

		this.rowResultSet = new RowResultSet(smartCacheRequest);
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
		IResultObject ro = this.rowResultSet.next();
		if (ro == null) {
			return null;
		}
		Object[] objs = new Object[this.resultClass.getFieldCount()];
		if (objs.length > 0) {
			int roFieldCount = ro.getResultClass().getFieldCount();
			for (int i = 0; i < objs.length; i++) {
				if (i + 1 <= roFieldCount) {
					objs[i] = ro.getFieldValue(i + 1);
				} else {
					objs[i] = null;
				}
			}
		}
		return new ResultObject(resultClass, objs);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.executor.cache.IRowResultSet#getIndex()
	 */
	@Override
	public int getIndex() throws DataException {
		return this.rowResultSet.getIndex();
	}
}

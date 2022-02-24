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
package org.eclipse.birt.data.engine.executor.dscache;

import java.util.List;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.BaseQuery;
import org.eclipse.birt.data.engine.executor.transform.CachedResultSet;
import org.eclipse.birt.data.engine.impl.DataEngineSession;
import org.eclipse.birt.data.engine.odi.ICandidateQuery;
import org.eclipse.birt.data.engine.odi.ICustomDataSet;
import org.eclipse.birt.data.engine.odi.IEventHandler;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultIterator;

/**
 * In design time, this query will retrieve data from cache.
 */
public class CandidateQuery extends BaseQuery implements ICandidateQuery {
	//
	private DataSetFromCache datasetFromCache;
	private DataEngineSession session;

	public CandidateQuery(DataEngineSession session) {
		this.session = session;
	}

	/*
	 * @see org.eclipse.birt.data.engine.odi.ICandidateQuery#getResultClass()
	 */
	public IResultClass getResultClass() throws DataException {
		return getOdaCacheResultSet().getResultClass();
	}

	/*
	 * @see org.eclipse.birt.data.engine.odi.ICandidateQuery#execute()
	 */
	public IResultIterator execute(IEventHandler eventHandler) throws DataException {
		return new CachedResultSet(this, getOdaCacheResultSet().getResultClass(), getOdaCacheResultSet(), eventHandler,
				session);
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.odi.ICandidateQuery#setCandidates(org.eclipse.
	 * birt.data.engine.odi.IResultIterator, int)
	 */
	public void setCandidates(IResultIterator resultObjsIterator, int groupingLevel) throws DataException {
		// do nothing
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.odi.ICandidateQuery#setCandidates(org.eclipse.
	 * birt.data.engine.odi.ICustomDataSet)
	 */
	public void setCandidates(ICustomDataSet customDataSet) throws DataException {
		// do nothing
	}

	/*
	 * @see org.eclipse.birt.data.engine.odi.IQuery#close()
	 */
	public void close() {
		try {
			if (datasetFromCache != null) {
				datasetFromCache.close();
				datasetFromCache = null;
			}
		} catch (DataException e) {
			// ignore it
		}
	}

	/**
	 * 
	 * @param columns
	 */
	public void setTempComputedColumn(List columns) {
		this.getOdaCacheResultSet().setTempComputedColumn(columns);
	}

	/**
	 * @return OdaCacheResultSet
	 */
	private DataSetFromCache getOdaCacheResultSet() {
		if (datasetFromCache == null)
			datasetFromCache = new DataSetFromCache(session);

		return datasetFromCache;
	}

}

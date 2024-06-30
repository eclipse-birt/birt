/**
 *************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
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
package org.eclipse.birt.data.engine.executor.transform;

import java.util.ArrayList;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.ResultClass;
import org.eclipse.birt.data.engine.executor.cache.ResultSetCache;
import org.eclipse.birt.data.engine.impl.IExecutorHelper;
import org.eclipse.birt.data.engine.impl.document.StreamWrapper;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.eclipse.birt.data.engine.odi.IResultObject;

public class EmptyResultIterator implements IResultIterator {

	@Override
	public IResultClass getResultClass() throws DataException {
		return new ResultClass(new ArrayList());
	}

	@Override
	public boolean next() throws DataException {
		return false;
	}

	@Override
	public void first(int groupingLevel) throws DataException {
	}

	@Override
	public void last(int groupingLevel) throws DataException {
	}

	@Override
	public IResultObject getCurrentResult() throws DataException {
		return null;
	}

	@Override
	public int getCurrentResultIndex() throws DataException {
		return 0;
	}

	@Override
	public int getCurrentGroupIndex(int groupLevel) throws DataException {
		return 0;
	}

	@Override
	public int getStartingGroupLevel() throws DataException {
		return 0;
	}

	@Override
	public int getEndingGroupLevel() throws DataException {
		return 0;
	}

	@Override
	public void close() throws DataException {
	}

	@Override
	public int[] getGroupStartAndEndIndex(int groupLevel) throws DataException {
		return null;
	}

	@Override
	public ResultSetCache getResultSetCache() {
		return null;
	}

	@Override
	public int getRowCount() throws DataException {
		return 0;
	}

	@Override
	public IExecutorHelper getExecutorHelper() {
		return null;
	}

	@Override
	public void doSave(StreamWrapper streamsWrapper, boolean isSubQuery) throws DataException {
	}

	@Override
	public void incrementalUpdate(StreamWrapper streamsWrapper, int rowCount, boolean isSubQuery) throws DataException {
	}

	@Override
	public Object getAggrValue(String aggrName) throws DataException {
		return null;
	}
}

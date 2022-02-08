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

	public IResultClass getResultClass() throws DataException {
		return new ResultClass(new ArrayList());
	}

	public boolean next() throws DataException {
		return false;
	}

	public void first(int groupingLevel) throws DataException {
	}

	public void last(int groupingLevel) throws DataException {
	}

	public IResultObject getCurrentResult() throws DataException {
		return null;
	}

	public int getCurrentResultIndex() throws DataException {
		return 0;
	}

	public int getCurrentGroupIndex(int groupLevel) throws DataException {
		return 0;
	}

	public int getStartingGroupLevel() throws DataException {
		return 0;
	}

	public int getEndingGroupLevel() throws DataException {
		return 0;
	}

	public void close() throws DataException {
	}

	public int[] getGroupStartAndEndIndex(int groupLevel) throws DataException {
		return null;
	}

	public ResultSetCache getResultSetCache() {
		return null;
	}

	public int getRowCount() throws DataException {
		return 0;
	}

	public IExecutorHelper getExecutorHelper() {
		return null;
	}

	public void doSave(StreamWrapper streamsWrapper, boolean isSubQuery) throws DataException {
	}

	public void incrementalUpdate(StreamWrapper streamsWrapper, int rowCount, boolean isSubQuery) throws DataException {
	}

	public Object getAggrValue(String aggrName) throws DataException {
		return null;
	}
}

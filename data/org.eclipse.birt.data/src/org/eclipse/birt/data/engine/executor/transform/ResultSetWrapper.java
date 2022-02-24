
/*******************************************************************************
 * Copyright (c) 2004, 2011 Actuate Corporation.
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
package org.eclipse.birt.data.engine.executor.transform;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.data.engine.cache.CachedList;
import org.eclipse.birt.data.engine.cache.ICachedObject;
import org.eclipse.birt.data.engine.cache.ICachedObjectCreator;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.ResultClass;
import org.eclipse.birt.data.engine.executor.ResultFieldMetadata;
import org.eclipse.birt.data.engine.executor.ResultObject;
import org.eclipse.birt.data.engine.executor.cache.ResultSetCache;
import org.eclipse.birt.data.engine.impl.DataEngineSession;
import org.eclipse.birt.data.engine.impl.IExecutorHelper;
import org.eclipse.birt.data.engine.impl.document.StreamWrapper;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 *
 */

public class ResultSetWrapper implements IResultIterator {
	private SimpleResultSet source;
	private int index;
	private CachedList cachedRows;
	private IResultClass trimedResultClass;

	public ResultSetWrapper(DataEngineSession session, SimpleResultSet source) throws DataException {
		this.source = source;
		this.index = source.getCurrentResultIndex();
		this.cachedRows = new CachedList(session.getTempDir(), DataEngineSession.getCurrentClassLoader(),
				new ResultObjectHolderCreator());
		List<ResultFieldMetadata> metas = new ArrayList<>();
		for (int i = 1; i <= this.source.getResultClass().getFieldCount(); i++) {
			ResultFieldMetadata meta = this.source.getResultClass().getFieldMetaData(i);
			if (meta.getName().startsWith("_{$TEMP")) {
				continue;
			}
			metas.add(meta);
		}
		this.trimedResultClass = new ResultClass(metas);
		if (this.index == 0) {
			this.cachedRows.add(new ResultObjectHolder(source.getCurrentResult(), source.getStartingGroupLevel(),
					source.getEndingGroupLevel(), source.getGroupIndex()));
		}
	}

	protected void initialize() throws DataException {
		this.index = source.getCurrentResultIndex();
		if (this.index == 0) {
			this.cachedRows.add(new ResultObjectHolder(source.getCurrentResult(), source.getStartingGroupLevel(),
					source.getEndingGroupLevel(), source.getGroupIndex()));
		}
	}

	@Override
	public IResultClass getResultClass() throws DataException {
		return this.source.getResultClass();
	}

	@Override
	public boolean next() throws DataException {
		if (this.index < this.cachedRows.size() - 1) {
			this.index++;
			return true;
		} else if (this.index == this.cachedRows.size() - 1) {
			boolean result = this.source.next();
			this.index++;
			if (result) {
				this.cachedRows
						.add(new ResultObjectHolder(this.source.getCurrentResult(), this.source.getStartingGroupLevel(),
								this.source.getEndingGroupLevel(), source.getGroupIndex()));
			}
			return result;
		} else {
			return false;
		}
	}

	@Override
	public void first(int groupingLevel) throws DataException {
		if (groupingLevel != 0) {
			throw new UnsupportedOperationException();
		}
	}

	@Override
	public void last(int groupingLevel) throws DataException {
		throw new UnsupportedOperationException();
	}

	@Override
	public IResultObject getCurrentResult() throws DataException {
		if (this.index >= this.cachedRows.size() || this.index < 0) {
			return null;
		}
		return getResultObjectHolder().getResultObject();
	}

	@Override
	public int getCurrentResultIndex() throws DataException {
		return this.index;
	}

	@Override
	public int getCurrentGroupIndex(int groupLevel) throws DataException {
		return this.getResultObjectHolder().getCurrentGroupIndex(groupLevel - 1);
	}

	@Override
	public int getStartingGroupLevel() throws DataException {
		assert this.index < this.cachedRows.size();
		return getResultObjectHolder().getStartingGroupIndex();
	}

	private ResultObjectHolder getResultObjectHolder() {
		return ((ResultObjectHolder) this.cachedRows.get(this.index));
	}

	@Override
	public int getEndingGroupLevel() throws DataException {
		assert this.index < this.cachedRows.size();
		return getResultObjectHolder().getEndingGroupIndex();
	}

	@Override
	public void close() throws DataException {
		this.source.close();
	}

	@Override
	public int[] getGroupStartAndEndIndex(int groupLevel) throws DataException {
		return this.source.getGroupStartAndEndIndex(groupLevel);
	}

	@Override
	public ResultSetCache getResultSetCache() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getRowCount() throws DataException {
		return this.source.getRowCount();
	}

	@Override
	public IExecutorHelper getExecutorHelper() {
		return this.source.getExecutorHelper();
	}

	@Override
	public void doSave(StreamWrapper streamsWrapper, boolean isSubQuery) throws DataException {
		this.source.doSave(streamsWrapper, isSubQuery);
	}

	@Override
	public void incrementalUpdate(StreamWrapper streamsWrapper, int rowCount, boolean isSubQuery) throws DataException {
		this.source.incrementalUpdate(streamsWrapper, rowCount, isSubQuery);
	}

	@Override
	public Object getAggrValue(String aggrName) throws DataException {
		while (!this.source.aggrValueAvailable(aggrName, this.index)) {
			if (this.source.next()) {
				this.cachedRows
						.add(new ResultObjectHolder(this.source.getCurrentResult(), this.source.getStartingGroupLevel(),
								this.source.getEndingGroupLevel(), this.source.getGroupIndex()));
			} else {
				break;
			}
		}
		return this.source.getAggrHelper().getAggrValue(aggrName, this);
	}

	private class ResultObjectHolder implements ICachedObject {
		private IResultObject ro;
		private int startingGroupIndex;
		private int endingGroupIndex;
		private Object[] groupIndex;

		public ResultObjectHolder(IResultObject ro, Integer startingGroupIndex, Integer endingGroupIndex,
				Object[] groupIndex) {
			this.ro = ro;
			this.startingGroupIndex = startingGroupIndex;
			this.endingGroupIndex = endingGroupIndex;
			this.groupIndex = groupIndex;
		}

		public int getCurrentGroupIndex(int groupLevel) {
			int candidateIndex = (Integer) this.groupIndex[groupLevel] - 1;
			if (candidateIndex >= 0) {
				return candidateIndex;
			}
			return 0;
		}

		public IResultObject getResultObject() {
			return this.ro;
		}

		public int getStartingGroupIndex() {
			return this.startingGroupIndex;
		}

		public int getEndingGroupIndex() {
			return this.endingGroupIndex;
		}

		@Override
		public Object[] getFieldValues() {
			Object[] result = new Object[trimedResultClass.getFieldCount() + 2 + this.groupIndex.length];
			for (int i = 0; i < trimedResultClass.getFieldCount(); i++) {
				try {
					result[i] = this.ro.getFieldValue(i + 1);
				} catch (DataException e) {
					result[i] = e;
				}
			}
			result[trimedResultClass.getFieldCount()] = this.startingGroupIndex;
			result[trimedResultClass.getFieldCount() + 1] = this.endingGroupIndex;
			for (int i = trimedResultClass.getFieldCount() + 2; i < result.length; i++) {
				result[i] = this.groupIndex[i - trimedResultClass.getFieldCount() - 2];
			}
			return result;
		}
	}

	/**
	 * A creator class implemented ICachedObjectCreator. This class is used to
	 * create GroupInfo object.
	 *
	 * @author Administrator
	 *
	 */
	class ResultObjectHolderCreator implements ICachedObjectCreator {
		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.eclipse.birt.data.engine.cache.ICachedObjectCreator#createInstance(java.
		 * lang.Object[])
		 */
		@Override
		public ICachedObject createInstance(Object[] fields) {
			try {

				Object[] resultValues = new Object[trimedResultClass.getFieldCount()];
				System.arraycopy(fields, 0, resultValues, 0, Math.min(fields.length, resultValues.length));

				Object[] results = new Object[fields.length - trimedResultClass.getFieldCount() - 2];
				System.arraycopy(fields, trimedResultClass.getFieldCount() + 2, results, 0, results.length);

				return new ResultObjectHolder(new ResultObject(trimedResultClass, resultValues),
						(Integer) fields[trimedResultClass.getFieldCount()],
						(Integer) fields[trimedResultClass.getFieldCount() + 1], results);
			} catch (Exception e) {
				return null;
			}
		}
	}

}

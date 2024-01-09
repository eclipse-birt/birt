/*
 *************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
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

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.core.archive.RAOutputStream;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.ICloseable;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.BaseQuery;
import org.eclipse.birt.data.engine.executor.CandidateQuery;
import org.eclipse.birt.data.engine.executor.ResultClass;
import org.eclipse.birt.data.engine.executor.ResultFieldMetadata;
import org.eclipse.birt.data.engine.executor.aggregation.AggrDefnManager;
import org.eclipse.birt.data.engine.executor.aggregation.IProgressiveAggregationHelper;
import org.eclipse.birt.data.engine.executor.aggregation.ProgressiveAggregationHelper;
import org.eclipse.birt.data.engine.executor.cache.IRowResultSet;
import org.eclipse.birt.data.engine.executor.cache.OdiAdapter;
import org.eclipse.birt.data.engine.executor.cache.ResultSetCache;
import org.eclipse.birt.data.engine.executor.cache.ResultSetUtil;
import org.eclipse.birt.data.engine.executor.cache.RowResultSet;
import org.eclipse.birt.data.engine.executor.cache.SmartCacheRequest;
import org.eclipse.birt.data.engine.impl.ComputedColumnHelper;
import org.eclipse.birt.data.engine.impl.DataEngineSession;
import org.eclipse.birt.data.engine.impl.DataSetRuntime;
import org.eclipse.birt.data.engine.impl.DataSetRuntime.Mode;
import org.eclipse.birt.data.engine.impl.FilterByRow;
import org.eclipse.birt.data.engine.impl.IExecutorHelper;
import org.eclipse.birt.data.engine.impl.StringTable;
import org.eclipse.birt.data.engine.impl.document.StreamWrapper;
import org.eclipse.birt.data.engine.impl.document.stream.StreamManager;
import org.eclipse.birt.data.engine.impl.document.viewing.ExprMetaUtil;
import org.eclipse.birt.data.engine.impl.index.IAuxiliaryIndexCreator;
import org.eclipse.birt.data.engine.impl.index.IIndexSerializer;
import org.eclipse.birt.data.engine.odaconsumer.ResultSet;
import org.eclipse.birt.data.engine.odi.IAggrInfo;
import org.eclipse.birt.data.engine.odi.ICustomDataSet;
import org.eclipse.birt.data.engine.odi.IDataSetPopulator;
import org.eclipse.birt.data.engine.odi.IEventHandler;
import org.eclipse.birt.data.engine.odi.IQuery.GroupSpec;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.eclipse.birt.data.engine.odi.IResultObject;
import org.eclipse.birt.data.engine.odi.IResultObjectEvent;
import org.eclipse.birt.data.engine.script.JSResultSetRow;
import org.eclipse.birt.data.engine.script.OnFetchScriptHelper;
import org.eclipse.birt.data.engine.storage.DataSetStore;
import org.eclipse.birt.data.engine.storage.IDataSetWriter;

/**
 * A Result Set that directly fetch data from ODA w/o using any cache.
 *
 * @author Work
 *
 */
public class SimpleResultSet implements IResultIterator {

	private RowResultSet rowResultSet;
	private IResultObject currResultObj;
	private IEventHandler handler;
	private int initialRowCount, rowCount;
	private StreamWrapper streamsWrapper;
	private OutputStream dataSetStream;
	private DataOutputStream dataSetLenStream;
	private long offset = 4;
	private long rowCountOffset = 0;
	private Set<String> resultSetNameSet = null;
	private IBaseQueryDefinition query;
	private IResultClass resultClass;
	private IGroupCalculator groupCalculator;
	private IProgressiveAggregationHelper aggrHelper;
	private boolean isClosed;
	private ICloseable closeable;

	private IDataSetWriter writer;
	private List<IAuxiliaryIndexCreator> auxiliaryIndexCreators;

	private boolean forceLookForward;
	private BaseQuery dataSourceQuery;
	private DataEngineSession session;
	private ComputedColumnHelper ccHelper;
	private FilterByRow filterByRow;
	private List<OnFetchScriptHelper> onFetchEvents;

	// TODO: refactor me. Add this for emergence -- release.
	private boolean firstRowSaved = false;

	/**
	 *
	 * @param dataSourceQuery
	 * @param resultSet
	 * @param resultClass
	 * @param stopSign
	 * @throws DataException
	 */
	public SimpleResultSet(BaseQuery dataSourceQuery, final ResultSet resultSet, IResultClass resultClass,
			IEventHandler handler, GroupSpec[] groupSpecs, DataEngineSession session, boolean forceLookingForward)
			throws DataException {
		SmartCacheRequest scRequest = new SmartCacheRequest(dataSourceQuery.getMaxRows(),
				dataSourceQuery.getFetchEvents(), new OdiAdapter(resultSet, resultClass), resultClass, false);

		this.closeable = new ICloseable() {

			@Override
			public void close() throws DataException {
				resultSet.close();

			}
		};

		this.handler = handler;
		this.initialize(dataSourceQuery, handler, scRequest, resultClass, groupSpecs, session, forceLookingForward);
	}

	public SimpleResultSet(BaseQuery dataSourceQuery, IDataSetPopulator populator, IResultClass resultClass,
			IEventHandler handler, GroupSpec[] groupSpecs, DataEngineSession session, boolean forceLookingForward)
			throws DataException {
		SmartCacheRequest scRequest = new SmartCacheRequest(dataSourceQuery.getMaxRows(),
				dataSourceQuery.getFetchEvents(), new OdiAdapter(populator), resultClass, false);

		this.closeable = (populator instanceof ICloseable) ? (ICloseable) populator : null;
		this.handler = handler;
		this.initialize(dataSourceQuery, handler, scRequest, resultClass, groupSpecs, session, forceLookingForward);

	}

	public SimpleResultSet(CandidateQuery dataSourceQuery, final ICustomDataSet customDataSet, IResultClass resultClass,
			IEventHandler handler, GroupSpec[] groupSpecs, DataEngineSession session, boolean forceLookingForward)
			throws DataException {
		SmartCacheRequest scRequest = new SmartCacheRequest(dataSourceQuery.getMaxRows(),
				dataSourceQuery.getFetchEvents(), new OdiAdapter(customDataSet), resultClass, false);

		this.closeable = new ICloseable() {

			@Override
			public void close() throws DataException {
				customDataSet.close();

			}
		};

		this.handler = handler;
		this.initialize(dataSourceQuery, handler, scRequest, resultClass, groupSpecs, session, forceLookingForward);
	}

	private void initialize(BaseQuery baseQuery, IEventHandler handler, SmartCacheRequest scRequest,
			IResultClass resultMetadata, GroupSpec[] groupSpecs, DataEngineSession session, boolean forceLookingForward)
			throws DataException {
		this.dataSourceQuery = baseQuery;
		this.query = baseQuery.getQueryDefinition();
		this.session = session;
		this.forceLookForward = forceLookingForward;
		boolean needLookForward = needLookingForwardFor1Row(groupSpecs, forceLookingForward);

		populateComputedColumnHelper(baseQuery);
		populateRowResultSet(handler, scRequest, needLookForward);
		populateDataSetColumns(handler, this.query, resultMetadata, groupSpecs, forceLookingForward);
		populateAggregationHelper(handler, session, groupSpecs, needLookForward);
		populateGroupCalculator(groupSpecs, needLookForward, session, this.rowResultSet.getMetaData(), this.aggrHelper);
	}

	private void populateComputedColumnHelper(BaseQuery baseQuery) {
		if (baseQuery.getFetchEvents() == null) {
			return;
		}
		this.onFetchEvents = new ArrayList<>();
		for (int i = 0; i < baseQuery.getFetchEvents().size(); i++) {
			IResultObjectEvent event = baseQuery.getFetchEvents().get(i);
			if (event instanceof ComputedColumnHelper) {
				this.ccHelper = (ComputedColumnHelper) event;
			} else if (event instanceof OnFetchScriptHelper) {
				onFetchEvents.add((OnFetchScriptHelper) event);
			} else if (event instanceof FilterByRow) {
				this.filterByRow = (FilterByRow) event;
			}
		}
	}

	private void updateFetchEventMode(int mode) throws DataException {
		if (this.ccHelper != null) {
			this.ccHelper.setModel(mode);
		}
		if (this.filterByRow != null) {
			this.filterByRow
					.setWorkingFilterSet(mode == TransformationConstants.DATA_SET_MODEL ? FilterByRow.DATASET_FILTER
							: FilterByRow.QUERY_FILTER);
		}
	}

	private void populateRowResultSet(IEventHandler handler, SmartCacheRequest scRequest, boolean lookingForward) {
		DataSetRuntime runtime = handler.getDataSetRuntime();
		if (runtime == null) {
			this.rowResultSet = new RowResultSet(scRequest);
		} else {
			this.rowResultSet = new RowResultSetWithResultSetScope(scRequest, runtime);
		}
	}

	private void populateDataSetColumns(IEventHandler handler, IBaseQueryDefinition query, IResultClass resultClass,
			GroupSpec[] groupSpecs, boolean forceLookingForward) throws DataException {
		this.resultSetNameSet = ResultSetUtil.getRsColumnRequestMap(handler.getAllColumnBindings());
		if (query instanceof IQueryDefinition && ((IQueryDefinition) query).needAutoBinding()) {
			for (int i = 1; i <= resultClass.getFieldCount(); i++) {
				this.resultSetNameSet.add(resultClass.getFieldName(i));
				this.resultSetNameSet.add(resultClass.getFieldAlias(i));
			}
		}
	}

	private void populateAggregationHelper(IEventHandler handler, DataEngineSession session, GroupSpec[] groupSpecs,
			boolean lookForward) throws DataException {
		AggrDefnManager manager = new AggrDefnManager(handler.getAggrDefinitions());
		this.aggrHelper = lookForward
				? new ProgressiveAggregationHelper(handler.getColumnBindings(), manager, session.getTempDir(),
						session.getSharedScope(), session.getEngineContext().getScriptContext(),
						handler.getExecutorHelper())
				: new DummyAggregationHelper();
	}

	private void populateGroupCalculator(GroupSpec[] groupSpecs, boolean lookForward, DataEngineSession session,
			IResultClass resultMeta, IProgressiveAggregationHelper aggrHelper) throws DataException {
		this.groupCalculator = lookForward ? new SimpleGroupCalculator(session, groupSpecs, resultMeta)
				: new DummyGroupCalculator();
		this.groupCalculator.setAggrHelper(aggrHelper);
	}

	private void prepareFirstRow() throws DataException {
		this.currResultObj = this.rowResultSet.next();
		this.groupCalculator.registerCurrentResultObject(this.currResultObj);
		this.groupCalculator.registerNextResultObject(this.rowResultSet);
		this.initialRowCount = (this.currResultObj != null) ? -1 : 0;
		this.rowCount = (this.currResultObj != null) ? 1 : 0;
		if (this.currResultObj != null) {
			/* Only call group calculator if it actually is rows in the result set. */
			this.groupCalculator.next(0);
		}
	}

	private boolean needLookingForwardFor1Row(GroupSpec[] groupSpecs, boolean forceLookingForward) {
		return (forceLookingForward || groupSpecs.length > 0 || this.query.cacheQueryResults());
	}

	public IResultIterator getResultSetIterator() throws DataException {
		IResultIterator itr = this.forceLookForward ? new ResultSetWrapper(this.session, this) : this;
		this.handler.handleEndOfDataSetProcess(itr);
		this.prepareFirstRow();
		if (this.forceLookForward) {
			((ResultSetWrapper) itr).initialize();
		}
		return itr;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.odi.IResultIterator#close()
	 */
	@Override
	public void close() throws DataException {
		if (this.isClosed) {
			return;
		}
		if (this.closeable != null) {
			this.closeable.close();
			this.closeable = null;
		}

		if (this.writer != null) {
			this.writer.close();
			this.writer = null;
		}

		this.groupCalculator.close();

		if (this.dataSetStream != null) {
			try {
				if (dataSetStream instanceof RAOutputStream) {
					((RAOutputStream) dataSetStream).seek(rowCountOffset);
					// if there is no rows saved in document, save rowCount as 0
					if (!firstRowSaved) {
						IOUtil.writeInt(dataSetStream, 0);
					} else {
						IOUtil.writeInt(dataSetStream, rowCount);
					}
				}

				if (this.streamsWrapper.getStreamForIndex(this.getResultClass(), handler.getAppContext()) != null) {
					Map<String, IIndexSerializer> hashes = this.streamsWrapper.getStreamForIndex(this.getResultClass(),
							handler.getAppContext());
					for (IIndexSerializer hash : hashes.values()) {
						hash.close();
					}
				}
				Map<String, StringTable> stringTables = this.streamsWrapper.getOutputStringTable(this.getResultClass());
				for (StringTable stringTable : stringTables.values()) {
					stringTable.close();
				}
				if (this.streamsWrapper.getStreamManager().hasOutStream(DataEngineContext.EXPR_VALUE_STREAM,
						StreamManager.ROOT_STREAM, StreamManager.SELF_SCOPE)) {
					OutputStream exprValueStream = this.streamsWrapper.getStreamManager().getOutStream(
							DataEngineContext.EXPR_VALUE_STREAM, StreamManager.ROOT_STREAM, StreamManager.SELF_SCOPE);
					if (exprValueStream instanceof RAOutputStream) {
						((RAOutputStream) exprValueStream).seek(0);
						IOUtil.writeInt(exprValueStream, rowCount);
					}

					exprValueStream.close();
				}

				dataSetStream.close();
				dataSetStream = null;
			} catch (Exception e) {
				throw new DataException(e.getLocalizedMessage(), e);
			}
			dataSetStream = null;
		}
		if (this.dataSetLenStream != null) {
			try {
				dataSetLenStream.close();
			} catch (Exception e) {
			}
			dataSetLenStream = null;
		}

		if (auxiliaryIndexCreators != null) {
			for (IAuxiliaryIndexCreator creator : auxiliaryIndexCreators) {
				creator.close();
			}
		}

		this.rowResultSet = null;
		this.ccHelper = null;
		this.aggrHelper = null;
		this.handler = null;
		this.session = null;
		this.filterByRow = null;
		this.dataSourceQuery = null;
		this.resultSetNameSet.clear();
		if (onFetchEvents != null) {
			onFetchEvents.clear();
		}

		this.isClosed = true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.odi.IResultIterator#addIncrement(org.eclipse.
	 * birt.data.engine.impl.document.StreamWrapper, int, boolean)
	 */
	@Override
	public void incrementalUpdate(StreamWrapper streamsWrapper, int originalRowCount, boolean isSubQuery)
			throws DataException {
		this.streamsWrapper = streamsWrapper;
		this.auxiliaryIndexCreators = streamsWrapper.getAuxiliaryIndexCreators();

		try {
			writer = DataSetStore.createUpdater(this.streamsWrapper.getStreamManager(), getResultClass(),
					handler.getAppContext(), this.session, auxiliaryIndexCreators);

			if (writer == null) {
				dataSetStream = this.streamsWrapper.getStreamManager().getOutStream(
						DataEngineContext.DATASET_DATA_STREAM, StreamManager.ROOT_STREAM, StreamManager.SELF_SCOPE);
				OutputStream dlenStream = this.streamsWrapper.getStreamManager().getOutStream(
						DataEngineContext.DATASET_DATA_LEN_STREAM, StreamManager.ROOT_STREAM, StreamManager.SELF_SCOPE);
				if (dataSetStream instanceof RAOutputStream) {
					rowCountOffset = ((RAOutputStream) dataSetStream).getOffset();
					((RAOutputStream) dataSetStream).seek(((RAOutputStream) dataSetStream).length());
					offset = ((RAOutputStream) dataSetStream).getOffset();
				}
				if (dlenStream instanceof RAOutputStream) {
					((RAOutputStream) dlenStream).seek(((RAOutputStream) dlenStream).length());
				}
				dataSetLenStream = new DataOutputStream(dlenStream);
			}

			this.rowCount += originalRowCount;
		} catch (IOException e) {
			throw new DataException(e.getLocalizedMessage(), e);
		}

	}

	private List<IBinding> getRequestColumnMap() {
		try {
			if (DataSetStore.isDataMartStore(handler.getAppContext(), this.session)) {
				return null;
			}
		} catch (DataException e) {
		}
		return (this.query instanceof IQueryDefinition) && ((IQueryDefinition) this.query).needAutoBinding() ? null
				: this.handler.getAllColumnBindings();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.odi.IResultIterator#doSave(org.eclipse.birt.data
	 * .engine.impl.document.StreamWrapper, boolean)
	 */
	@Override
	public void doSave(StreamWrapper streamsWrapper, boolean isSubQuery) throws DataException {
		assert streamsWrapper != null;
		this.streamsWrapper = streamsWrapper;
		this.auxiliaryIndexCreators = streamsWrapper.getAuxiliaryIndexCreators();
		this.groupCalculator.doSave(streamsWrapper.getStreamManager());
		this.writer = DataSetStore.createWriter(streamsWrapper.getStreamManager(), getResultClass(),
				handler.getAppContext(), this.session, auxiliaryIndexCreators);
		try {
			if (streamsWrapper.getStreamForResultClass() != null) {
				((ResultClass) populateResultClass(getResultClass())).doSave(streamsWrapper.getStreamForResultClass(),
						getRequestColumnMap(), streamsWrapper.getStreamManager().getVersion());
				streamsWrapper.getStreamForResultClass().close();
			}

			if (writer == null) {
				dataSetStream = this.streamsWrapper.getStreamManager().getOutStream(
						DataEngineContext.DATASET_DATA_STREAM, StreamManager.ROOT_STREAM, StreamManager.SELF_SCOPE);
				dataSetLenStream = streamsWrapper.getStreamForDataSetRowLens();
				if (dataSetStream instanceof RAOutputStream) {
					rowCountOffset = ((RAOutputStream) dataSetStream).getOffset();
				}
				IOUtil.writeInt(dataSetStream, this.initialRowCount);

				if (auxiliaryIndexCreators != null) {
					for (IAuxiliaryIndexCreator aIndex : this.auxiliaryIndexCreators) {
						aIndex.initialize(this.resultClass, this.getExecutorHelper().getScriptable());
					}
				}
			}
			// try to save the first row
			if (this.currResultObj != null) {
				saveDataSetResultSet(this.currResultObj, rowCount - 1);
				firstRowSaved = true;
			}
		} catch (IOException e) {
			throw new DataException(e.getLocalizedMessage(), e);
		}
	}

	private IResultClass populateResultClass(IResultClass meta) throws DataException {
		if (resultClass == null) {
			List<ResultFieldMetadata> list = new ArrayList<>();
			for (int i = 1; i <= meta.getFieldCount(); i++) {
				if (!meta.getFieldName(i).equals(ExprMetaUtil.POS_NAME)) {
					list.add(meta.getFieldMetaData(i));
				}
			}
			resultClass = new ResultClass(list);
		}
		return resultClass;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.odi.IResultIterator#first(int)
	 */
	@Override
	public void first(int groupingLevel) throws DataException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.odi.IResultIterator#getAggrValue(java.lang.
	 * String)
	 */
	@Override
	public Object getAggrValue(String aggrName) throws DataException {
		return this.aggrHelper.getAggrValue(aggrName, this);
	}

	public IProgressiveAggregationHelper getAggrHelper() throws DataException {
		return this.aggrHelper;
	}

	public Integer[] getGroupIndex() throws DataException {
		// For the first group, the group have not been populated yet.
		if (this.groupCalculator.getStartingGroup() == 0) {
			Integer[] result = new Integer[this.groupCalculator.getGroupInstanceIndex().length];
			Arrays.fill(result, 0);
			return result;
		}

		Integer[] groupIndex = this.groupCalculator.getGroupInstanceIndex();
		Integer[] copy = new Integer[groupIndex.length];
		System.arraycopy(groupIndex, 0, copy, 0, copy.length);
		return copy;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.odi.IResultIterator#getCurrentGroupIndex(int)
	 */
	@Override
	public int getCurrentGroupIndex(int groupLevel) throws DataException {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.odi.IResultIterator#getCurrentResult()
	 */
	@Override
	public IResultObject getCurrentResult() throws DataException {
		return this.currResultObj;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.odi.IResultIterator#getCurrentResultIndex()
	 */
	@Override
	public int getCurrentResultIndex() throws DataException {
		return this.rowResultSet.getIndex();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.odi.IResultIterator#getEndingGroupLevel()
	 */
	@Override
	public int getEndingGroupLevel() throws DataException {
		return this.groupCalculator.getEndingGroup();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.odi.IResultIterator#getExecutorHelper()
	 */
	@Override
	public IExecutorHelper getExecutorHelper() {
		return this.handler.getExecutorHelper();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.odi.IResultIterator#getGroupStartAndEndIndex(
	 * int)
	 */
	@Override
	public int[] getGroupStartAndEndIndex(int groupLevel) throws DataException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.odi.IResultIterator#getResultClass()
	 */
	@Override
	public IResultClass getResultClass() throws DataException {
		// TODO Auto-generated method stub
		return this.rowResultSet.getMetaData();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.odi.IResultIterator#getResultSetCache()
	 */
	@Override
	public ResultSetCache getResultSetCache() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.odi.IResultIterator#getRowCount()
	 */
	@Override
	public int getRowCount() throws DataException {
		return this.initialRowCount;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.odi.IResultIterator#getStartingGroupLevel()
	 */
	@Override
	public int getStartingGroupLevel() throws DataException {
		return this.groupCalculator.getStartingGroup();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.odi.IResultIterator#last(int)
	 */
	@Override
	public void last(int groupingLevel) throws DataException {
		if (this.getEndingGroupLevel() <= groupingLevel) {
		} else {
			while (this.next()) {
				if (this.getEndingGroupLevel() <= groupingLevel) {
					return;
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.odi.IResultIterator#next()
	 */
	@Override
	public boolean next() throws DataException {
		if (currResultObj == null) {
			return false;
		}

		if (!this.firstRowSaved) {
			this.firstRowSaved = true;
			saveDataSetResultSet(currResultObj, 0);
		}
		doNext();

		if (currResultObj != null) {
			saveDataSetResultSet(currResultObj, rowCount - 1);
		}
		return this.currResultObj != null;
	}

	private void doNext() throws DataException {
		try {
			this.groupCalculator.registerPreviousResultObject(this.currResultObj);
			this.currResultObj = this.rowResultSet.next();

			this.groupCalculator.registerCurrentResultObject(this.currResultObj);

			this.groupCalculator.registerNextResultObject(this.rowResultSet);
			if (this.currResultObj != null) {
				this.groupCalculator.next(this.rowResultSet.getIndex());
			}
		} catch (DataException e) {
			this.currResultObj = null;
			throw e;
		}

		if (this.currResultObj != null) {
			rowCount++;
		}
	}

	private void saveDataSetResultSet(IResultObject rs, int index) throws DataException {
		if (this.streamsWrapper != null && rs != null) {
			try {
				if (writer != null) {
					writer.save(currResultObj, rowCount - 1);
				} else if (dataSetStream != null) {
					int colCount = this.populateResultClass(rs.getResultClass()).getFieldCount();
					IOUtil.writeLong(dataSetLenStream, offset);

					offset += ResultSetUtil.writeResultObject(new DataOutputStream(dataSetStream), currResultObj,
							colCount, resultSetNameSet, streamsWrapper.getOutputStringTable(getResultClass()),
							streamsWrapper.getStreamForIndex(getResultClass(), handler.getAppContext()),
							this.rowCount - 1, streamsWrapper.getStreamManager().getVersion());

					if (auxiliaryIndexCreators != null) {
						for (IAuxiliaryIndexCreator creator : auxiliaryIndexCreators) {
							creator.save(currResultObj, this.rowCount - 1);
						}
					}
				}
			} catch (IOException e) {
				throw new DataException(e.getLocalizedMessage(), e);
			}
		}
	}

	public boolean aggrValueAvailable(String aggrName, int index) throws DataException {
		return this.groupCalculator.isAggrAtIndexAvailable(aggrName, index);
	}

	private class DummyAggregationHelper implements IProgressiveAggregationHelper {

		@Override
		public void onRow(int startingGroupLevel, int endingGroupLevel, IResultObject ro, int currentRowIndex)
				throws DataException {
			// TODO Auto-generated method stub

		}

		@Override
		public void close() throws DataException {
			// TODO Auto-generated method stub

		}

		@Override
		public Object getLatestAggrValue(String name) throws DataException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object getAggrValue(String name, IResultIterator ri) throws DataException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List getAggrValues(String name) throws DataException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean hasAggr(String name) throws DataException {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Set<String> getAggrNames() throws DataException {
			// TODO Auto-generated method stub
			return new HashSet<>();
		}

		@Override
		public IAggrInfo getAggrInfo(String aggrName) throws DataException {
			// TODO Auto-generated method stub
			return null;
		}

	}

	private class DummyGroupCalculator implements IGroupCalculator {
		@Override
		public void registerPreviousResultObject(IResultObject previous) {
			// TODO Auto-generated method stub

		}

		@Override
		public void registerCurrentResultObject(IResultObject current) {
			// TODO Auto-generated method stub

		}

		@Override
		public void registerNextResultObject(RowResultSet rowResultSet) throws DataException {
			// TODO Auto-generated method stub
		}

		@Override
		public void next(int rowId) throws DataException {
			// TODO Auto-generated method stub

		}

		@Override
		public int getStartingGroup() throws DataException {
			if (rowCount == 1) {
				return 0;
			}
			return 1;
		}

		@Override
		public int getEndingGroup() throws DataException {
			if (currResultObj == null) {
				return 0;
			}
			return 1;
		}

		@Override
		public void close() throws DataException {
			// TODO Auto-generated method stub

		}

		@Override
		public void doSave(StreamManager manager) throws DataException {
			// TODO Auto-generated method stub

		}

		@Override
		public void setAggrHelper(IProgressiveAggregationHelper aggrHelper) throws DataException {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean isAggrAtIndexAvailable(String aggrName, int currentIndex) throws DataException {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Integer[] getGroupInstanceIndex() {
			// TODO Auto-generated method stub
			return new Integer[0];
		}

	}

	private class RowResultSetWithDataSetScopeAwareness extends RowResultSet {
		private DataSetRuntime runtime;
		private Mode cachedMode;

		public RowResultSetWithDataSetScopeAwareness(SmartCacheRequest smartCacheRequest, DataSetRuntime runtime) {
			super(smartCacheRequest, 0);
			this.runtime = runtime;
		}

		@Override
		protected void beforeNext() throws DataException {
			this.cachedMode = this.runtime.getMode();
			this.runtime.setMode(Mode.DataSet);
		}

		@Override
		protected void afterNext() throws DataException {
			this.runtime.setMode(this.cachedMode);
		}

		@Override
		protected void beforeProcessFetchEvent(IResultObject resultObject, int currentIndex) throws DataException {
			updateFetchEventMode(TransformationConstants.DATA_SET_MODEL);
		}

		@Override
		protected void afterProcessFetchEvent(IResultObject resultObject, int currentIndex) throws DataException {
			updateFetchEventMode(TransformationConstants.RESULT_SET_MODEL);
		}
	}

	/**
	 * This class help evaluate row[xx] object.
	 *
	 */
	private class RowResultSetWithResultSetScope extends RowResultSet {

		private IRowResultSet rowResultSet;
		private IResultObject current;
		private JSResultSetRow savedJSResultSetRow;
		private JSResultSetRow evalJSResultSetRow;
		private DataSetRuntime runtime;
		private IResultClass rsMeta;
		private boolean initialized = false;

		RowResultSetWithResultSetScope(SmartCacheRequest smartCacheRequest, DataSetRuntime runtime) {
			super(smartCacheRequest);
			this.rowResultSet = new RowResultSetWithDataSetScopeAwareness(smartCacheRequest, runtime);
			this.runtime = runtime;
			this.rsMeta = smartCacheRequest.getResultClass();
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.birt.data.engine.executor.cache.RowResultSet#fetch()
		 */
		@Override
		protected IResultObject fetch() throws DataException {
			return this.rowResultSet.next();
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.birt.data.engine.executor.cache.RowResultSet#beforeProcess
		 * (org.eclipse.birt.data.engine.odi.IResultObject, int)
		 */
		@Override
		protected void beforeProcessFetchEvent(IResultObject resultObject, int currentIndex) throws DataException {
			initialize();
			updateFetchEventMode(TransformationConstants.RESULT_SET_MODEL);
			this.runtime.setJSResultSetRow(this.evalJSResultSetRow);
			this.current = resultObject;
			removeOnFetchScriptHelper();
		}

		private void removeOnFetchScriptHelper() {
			if (SimpleResultSet.this.dataSourceQuery.getFetchEvents() == null) {
				return;
			}
			SimpleResultSet.this.dataSourceQuery.getFetchEvents().removeAll(SimpleResultSet.this.onFetchEvents);
		}

		private void restoreOnFetchScriptHelper() {
			if (SimpleResultSet.this.dataSourceQuery.getFetchEvents() == null) {
				return;
			}
			SimpleResultSet.this.dataSourceQuery.getFetchEvents().addAll(SimpleResultSet.this.onFetchEvents);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.birt.data.engine.executor.cache.RowResultSet#afterProcess
		 * (org.eclipse.birt.data.engine.odi.IResultObject, int)
		 */
		@Override
		protected void afterProcessFetchEvent(IResultObject rsRow, int currentIndex) throws DataException {
			updateFetchEventMode(TransformationConstants.DATA_SET_MODEL);
			this.runtime.setJSResultSetRow(this.savedJSResultSetRow);
			restoreOnFetchScriptHelper();
		}

		private void initialize() {
			if (this.initialized) {
				return;
			}

			this.initialized = true;
			if (!(this.runtime.getJSResultRowObject() instanceof JSResultSetRow)) {
				return;
			}

			this.savedJSResultSetRow = (JSResultSetRow) this.runtime.getJSResultRowObject();
			IResultIterator itr = new IResultIterator() {

				@Override
				public IResultClass getResultClass() throws DataException {
					return RowResultSetWithResultSetScope.this.rsMeta;
				}

				@Override
				public Object getAggrValue(String aggrName) throws DataException {
					return SimpleResultSet.this.aggrHelper.getAggrValue(aggrName, this);
				}

				@Override
				public IResultObject getCurrentResult() throws DataException {
					return RowResultSetWithResultSetScope.this.current;
				}

				@Override
				public int getCurrentResultIndex() throws DataException {
					return RowResultSetWithResultSetScope.this.rowResultSet.getIndex();
				}

				@Override
				public boolean next() throws DataException {
					// Dummy stuff
					return false;
				}

				@Override
				public void first(int groupingLevel) throws DataException {
					// Dummy stuff
				}

				@Override
				public void last(int groupingLevel) throws DataException {
					// Dummy stuff
				}

				@Override
				public int getCurrentGroupIndex(int groupLevel) throws DataException {
					// Dummy stuff
					return 0;
				}

				@Override
				public int getStartingGroupLevel() throws DataException {
					// Dummy stuff
					return 0;
				}

				@Override
				public int getEndingGroupLevel() throws DataException {
					// Dummy stuff
					return 0;
				}

				@Override
				public void close() throws DataException {
					// Dummy stuff
				}

				@Override
				public int[] getGroupStartAndEndIndex(int groupLevel) throws DataException {
					// Dummy stuff
					return null;
				}

				@Override
				public ResultSetCache getResultSetCache() {
					// Dummy stuff
					return null;
				}

				@Override
				public int getRowCount() throws DataException {
					// Dummy stuff
					return 0;
				}

				@Override
				public IExecutorHelper getExecutorHelper() {
					// Dummy stuff
					return null;
				}

				@Override
				public void doSave(StreamWrapper streamsWrapper, boolean isSubQuery) throws DataException {
					// Dummy stuff
				}

				@Override
				public void incrementalUpdate(StreamWrapper streamsWrapper, int rowCount, boolean isSubQuery)
						throws DataException {
					// Dummy stuff
				}

			};

			this.evalJSResultSetRow = new JSResultSetRow(itr, this.savedJSResultSetRow);
		}

	}
}

/*
 *************************************************************************
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
 *  
 *************************************************************************
 */
package org.eclipse.birt.data.engine.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IComputedColumn;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.ISubqueryDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.DataSourceFactory;
import org.eclipse.birt.data.engine.executor.ResultClass;
import org.eclipse.birt.data.engine.executor.ResultFieldMetadata;
import org.eclipse.birt.data.engine.executor.transform.EmptyResultIterator;
import org.eclipse.birt.data.engine.odi.ICandidateQuery;
import org.eclipse.birt.data.engine.odi.ICustomDataSet;
import org.eclipse.birt.data.engine.odi.IDataSource;
import org.eclipse.birt.data.engine.odi.IEventHandler;
import org.eclipse.birt.data.engine.odi.IQuery;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.eclipse.birt.data.engine.odi.IResultObject;
import org.mozilla.javascript.Scriptable;

/**
 * A prepared Sub query, which does not have its own data set, but rather
 * queries a subset of data produced by its a parent query.
 */
class PreparedSubquery implements IPreparedQueryService {
	private int groupLevel;
	private PreparedQuery preparedQuery;
	private IPreparedQueryService queryService;
	private DataEngineSession session;
	private boolean subQueryOnGroup;

	private static Logger logger = Logger.getLogger(PreparedSubquery.class.getName());

	/**
	 * @param subquery    Subquery definition
	 * @param parentQuery Parent query (which can be a subquery itself, or a
	 *                    PreparedReportQuery)
	 * @param groupLevel  Index of group in which this subquery is defined within
	 *                    the parent query. If 0, subquery is defined outside of any
	 *                    groups.
	 * @throws DataException
	 */
	PreparedSubquery(DataEngineSession session, DataEngineContext context, ISubqueryDefinition subquery,
			IPreparedQueryService queryService, int groupLevel) throws DataException {
		Object[] params = { session, context, subquery, queryService, Integer.valueOf(groupLevel) };
		logger.entering(PreparedSubquery.class.getName(), "PreparedSubquery", params);
		this.groupLevel = groupLevel;
		this.queryService = queryService;
		this.subQueryOnGroup = subquery.applyOnGroup();

		logger.logp(Level.FINER, PreparedSubquery.class.getName(), "PreparedSubquery", "PreparedSubquery starts up.");
		this.session = session;
		this.preparedQuery = new PreparedQuery(session, context, subquery, this,
				queryService.getDataSourceQuery().appContext);
		logger.exiting(PreparedSubquery.class.getName(), "PreparedSubquery");
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.impl.IPreparedQueryService#getDataSourceQuery()
	 */
	public PreparedDataSourceQuery getDataSourceQuery() {
		// Gets the parent's report query
		return queryService.getDataSourceQuery();
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.impl.IPreparedQueryService#execSubquery(org.
	 * eclipse.birt.data.engine.odi.IResultIterator, java.lang.String,
	 * org.mozilla.javascript.Scriptable)
	 */
	public IQueryResults execSubquery(IResultIterator iterator, IQueryExecutor parentExecutor, String subQueryName,
			Scriptable subScope) throws DataException {
		return this.preparedQuery.execSubquery(iterator, parentExecutor, subQueryName, subScope);
	}

	/**
	 * @return group level of current sub query
	 */
	int getGroupLevel() {
		return this.groupLevel;
	}

	/**
	 * Executes this subquery
	 * 
	 * @param parentIterator
	 * @param scope
	 * @return
	 * @throws DataException
	 */
	QueryResults execute(IResultIterator parentIterator, IQueryExecutor parentExecutor, Scriptable scope)
			throws DataException {
		logger.logp(Level.FINER, PreparedSubquery.class.getName(), "execute", "start to execute a PreparedSubquery.");
		try {
			return preparedQuery.doPrepare(null, scope,
					new SubQueryExecutor(parentIterator, parentExecutor, parentExecutor.getQueryContextVisitor()),
					getDataSourceQuery());
		} finally {
			logger.logp(Level.FINER, PreparedSubquery.class.getName(), "execute",
					"finish executing a PreparedSubquery.");
		}
	}

	/**
	 * Concrete class of PreparedQuery.Executor used in PreparedSubquery
	 */
	public class SubQueryExecutor extends QueryExecutor implements ISubQueryExecutor {
		private IResultIterator parentIterator;
		private IQueryExecutor parentExecutor;

		/**
		 * @param parentIterator
		 */
		public SubQueryExecutor(IResultIterator parentIterator, IQueryExecutor parentExecutor,
				IQueryContextVisitor contextVisitor) {
			super(preparedQuery.getSharedScope(), preparedQuery.getBaseQueryDefn(), preparedQuery.getAggrTable(),
					session, contextVisitor);

			this.parentIterator = parentIterator;
			this.parentExecutor = parentExecutor;
			this.setParentExecutorHelper(parentIterator.getExecutorHelper());
		}

		public IResultIterator getParentIterator() {
			return this.parentIterator;
		}

		/*
		 * @see
		 * org.eclipse.birt.data.engine.impl.PreparedQuery.Executor#createOdiDataSource(
		 * )
		 */
		protected IDataSource createOdiDataSource() {
			// Subqueries don't have its own data source
			return null;
		}

		/*
		 * @see
		 * org.eclipse.birt.data.engine.impl.PreparedQuery.Executor#findDataSource()
		 */
		protected DataSourceRuntime findDataSource() {
			// Subqueries don't have its own data source
			return null;
		}

		/*
		 * @see
		 * org.eclipse.birt.data.engine.impl.PreparedQuery.Executor#newDataSetRuntime()
		 */
		protected DataSetRuntime newDataSetRuntime() {
			return new SubqueryDataSetRuntime(this, session);
		}

		protected String getDataSetName() {
			if (preparedQuery.getBaseQueryDefn() instanceof IQueryDefinition)
				return ((IQueryDefinition) preparedQuery.getBaseQueryDefn()).getDataSetName();
			return null;
		}

		/*
		 * @see
		 * org.eclipse.birt.data.engine.impl.PreparedQuery.Executor#createOdiQuery()
		 */
		protected IQuery createOdiQuery() throws DataException {
			// An empty odi data source is used for sub query data set
			return DataSourceFactory.getFactory().getEmptyDataSource(session).newCandidateQuery(false);
		}

		/*
		 * @see
		 * org.eclipse.birt.data.engine.impl.PreparedQuery.Executor#executeOdiQuery()
		 */
		protected IResultIterator executeOdiQuery(IEventHandler eventHandler) throws DataException {
			assert parentIterator != null;

			if (parentIterator instanceof EmptyResultIterator)
				return new EmptyResultIterator();

			IResultIterator ret = null;
			ICandidateQuery cdQuery = (ICandidateQuery) odiQuery;

			if (PreparedSubquery.this.subQueryOnGroup == true)
				cdQuery.setCandidates(parentIterator, groupLevel);
			else
				cdQuery.setCandidates(new CustomDataSet(parentIterator, getMergedResultClass()));

			ret = cdQuery.execute(eventHandler);
			// parentIterator = null;
			return ret;
		}

		/**
		 * @return
		 * @throws DataException
		 */
		private IResultClass getMergedResultClass() throws DataException {
			IResultClass parentResultClass = parentIterator.getResultClass();

			ICandidateQuery candidateQuery = (ICandidateQuery) odiQuery;
			assert candidateQuery != null;

			List computedColumns = dataSet.getComputedColumns();
			List columnsList = new ArrayList();

			for (int i = 0; i < parentResultClass.getFieldCount(); i++) {
				ResultFieldMetadata columnMetaData = new ResultFieldMetadata(i + 1,
						parentResultClass.getFieldName(i + 1), parentResultClass.getFieldName(i + 1),
						parentResultClass.getFieldValueClass(i + 1), parentResultClass.getFieldNativeTypeName(i + 1),
						parentResultClass.isCustomField(i + 1), -1);
				columnsList.add(columnMetaData);
				columnMetaData.setAlias(parentResultClass.getFieldAlias(i + 1));
			}

			// Add computed columns
			int count = columnsList.size();
			Iterator it = computedColumns.iterator();
			for (int j = columnsList.size(); it.hasNext(); j++) {
				IComputedColumn compColumn = (IComputedColumn) it.next();
				ResultFieldMetadata columnMetaData = new ResultFieldMetadata(++count, compColumn.getName(),
						compColumn.getName(), DataType.getClass(compColumn.getDataType()), null /* nativeTypeName */,
						true, -1);
				columnsList.add(columnMetaData);
			}

			return new ResultClass(columnsList);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.birt.data.engine.impl.ISubQueryExecutor#getSubQueryStartingIndex(
		 * )
		 */
		public int getSubQueryStartingIndex() throws DataException {
			if (!subQueryOnGroup)
				return this.parentIterator.getCurrentResultIndex();
			if (parentIterator instanceof EmptyResultIterator)
				return 0;

			int groupIndex = this.parentIterator.getCurrentGroupIndex(groupLevel);

			int[] groupStartingEndingIndex = this.parentIterator.getGroupStartAndEndIndex(groupLevel);

			// For the subquery of subquery, the starting index should still point to the
			// ultimate
			// parent.
			return (this.parentExecutor instanceof ISubQueryExecutor)
					? ((ISubQueryExecutor) this.parentExecutor).getSubQueryStartingIndex()
							+ groupStartingEndingIndex[groupIndex * 2]
					: groupStartingEndingIndex[groupIndex * 2];
		}
	}

	/**
	 *
	 */
	private static final class CustomDataSet implements ICustomDataSet {
		private IResultIterator resultIterator;
		private IResultClass resultClass;

		private boolean finished;

		CustomDataSet(IResultIterator resultIterator, IResultClass resultClass) {
			this.resultIterator = resultIterator;
			this.resultClass = resultClass;
		}

		/*
		 * @see org.eclipse.birt.data.engine.odi.ICustomDataSet#getResultClass()
		 */
		public IResultClass getResultClass() {
			return resultClass;
		}

		/*
		 * @see org.eclipse.birt.data.engine.odi.ICustomDataSet#open()
		 */
		public void open() throws DataException {
		}

		/*
		 * @see org.eclipse.birt.data.engine.odi.ICustomDataSet#fetc h()
		 */
		public IResultObject fetch() throws DataException {
			if (finished)
				return null;

			finished = true;
			return resultIterator.getCurrentResult();
		}

		/*
		 * @see org.eclipse.birt.data.engine.odi.ICustomDataSet#close()
		 */
		public void close() throws DataException {
		}
	}

}

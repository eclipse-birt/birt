/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.impl;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IBaseQueryResults;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IDataScriptEngine;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.data.engine.api.ISubqueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.core.security.FileSecurity;
import org.eclipse.birt.data.engine.executor.ResultClass;
import org.eclipse.birt.data.engine.executor.ResultFieldMetadata;
import org.eclipse.birt.data.engine.executor.transform.CachedResultSet;
import org.eclipse.birt.data.engine.expression.ExprEvaluateUtil;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.document.IRDSave;
import org.eclipse.birt.data.engine.impl.document.QueryResultInfo;
import org.eclipse.birt.data.engine.impl.document.RDUtil;
import org.eclipse.birt.data.engine.impl.document.StreamWrapper;
import org.eclipse.birt.data.engine.impl.document.stream.VersionManager;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.script.JSDummyRowObject;
import org.mozilla.javascript.Scriptable;

/**
 * When there is no data set, this instance will be created.
 */
public class PreparedDummyQuery implements IPreparedQuery {

	private ExprManager exprManager;

	private DataEngineSession session;
	private IQueryDefinition queryDefn;
	private ISubqueryDefinition subQueryDefn;

	private String subQueryName;
	private int subQueryIndex;

	private Map subQueryMap;

	private static Logger logger = Logger.getLogger(PreparedDummyQuery.class.getName());

	/**
	 * @param context
	 * @param queryDefn
	 * @param sharedScope
	 */
	PreparedDummyQuery(IQueryDefinition queryDefn, DataEngineSession session) {
		Object[] params = { queryDefn, session };
		logger.entering(PreparedDummyQuery.class.getName(), "PreparedDummyQuery", params);
		this.queryDefn = queryDefn;
		this.session = session;

		init(session.getEngineContext(), queryDefn, session.getSharedScope());
		logger.exiting(PreparedDummyQuery.class.getName(), "PreparedDummyQuery");
	}

	/**
	 * @param context
	 * @param subQueryDefn
	 * @param sharedScope
	 */
	PreparedDummyQuery(ISubqueryDefinition subQueryDefn, DataEngineSession session) {
		Object[] params = { subQueryDefn, session };
		logger.entering(PreparedDummyQuery.class.getName(), "PreparedDummyQuery", params);
		this.subQueryDefn = subQueryDefn;
		this.session = session;
		init(session.getEngineContext(), subQueryDefn, session.getSharedScope());
		logger.exiting(PreparedDummyQuery.class.getName(), "PreparedDummyQuery");
	}

	/**
	 * @param context
	 * @param queryDefn
	 * @param sharedScope
	 */
	void init(DataEngineContext context, IBaseQueryDefinition queryDefn, Scriptable sharedScope) {
		assert queryDefn != null;

		this.exprManager = new ExprManager(queryDefn, session.getEngineContext().getScriptContext());
		this.exprManager.addBindingExpr(null, queryDefn.getBindings(), 0);
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.IPreparedQuery#getReportQueryDefn()
	 */
	public IQueryDefinition getReportQueryDefn() {
		return queryDefn;
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.IPreparedQuery#getParameterMetaData()
	 */
	public Collection getParameterMetaData() throws BirtException {
		return null;
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.IPreparedQuery#execute(org.mozilla.
	 * javascript.Scriptable)
	 */
	public IQueryResults execute(Scriptable queryScope) throws BirtException {
		return execute(null, queryScope);
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.api.IPreparedQuery#execute(org.eclipse.birt.data
	 * .engine.api.IQueryResults, org.mozilla.javascript.Scriptable)
	 */
	public IQueryResults execute(IQueryResults outerResults, Scriptable queryScope) throws BirtException {
		return executeQuery(queryScope, null);
	}

	/**
	 * @param queryScope
	 * @param parentScope
	 * @return
	 * @throws BirtException
	 */
	IQueryResults executeQuery(Scriptable queryScope, Scriptable parentScope) throws BirtException {
		processSubQuery();
		return new QueryResults(this, exprManager, getScope(queryScope), parentScope);
	}

	/**
	 *
	 */
	private void processSubQuery() {
		IBaseQueryDefinition queryDefn2 = null;
		if (queryDefn != null)
			queryDefn2 = queryDefn;
		else
			queryDefn2 = subQueryDefn;

		subQueryMap = new HashMap();
		registerSubQuery(queryDefn2);
	}

	/**
	 * register all subQuery
	 * 
	 * @param queryDefn2
	 */
	private void registerSubQuery(IBaseQueryDefinition queryDefn2) {
		Collection subQueryDefns = queryDefn2.getSubqueries();
		if (subQueryDefns != null) {
			Iterator it = subQueryDefns.iterator();
			while (it.hasNext()) {
				ISubqueryDefinition subQueryDefn = (ISubqueryDefinition) it.next();
				subQueryMap.put(subQueryDefn.getName(), subQueryDefn);
				registerSubQuery(subQueryDefn);
			}
		}
		List groupList = queryDefn2.getGroups();
		if (groupList != null) {
			for (int i = 0; i < groupList.size(); i++) {
				GroupDefinition groupDefn = (GroupDefinition) groupList.get(i);
				Collection subQueryDefnsOnGroup = groupDefn.getSubqueries();
				if (subQueryDefnsOnGroup != null) {
					Iterator it = subQueryDefnsOnGroup.iterator();
					while (it.hasNext()) {
						ISubqueryDefinition subQueryDefn = (ISubqueryDefinition) it.next();
						subQueryMap.put(subQueryDefn.getName(), subQueryDefn);
						registerSubQuery(subQueryDefn);
					}
				}
			}
		}
	}

	/**
	 * @param queryScope
	 * @return
	 * @throws BirtException
	 */
	private Scriptable getScope(Scriptable queryScope) throws BirtException {
		Scriptable topScope = null;
		if (queryScope != null)
			topScope = queryScope;
		else
			topScope = session.getSharedScope();

		Scriptable executionScope = null;
		executionScope = ((IDataScriptEngine) session.getEngineContext().getScriptContext()
				.getScriptEngine(IDataScriptEngine.ENGINE_NAME))
						.getJSContext(session.getEngineContext().getScriptContext()).newObject(topScope);
		executionScope.setParentScope(topScope);
		executionScope.setPrototype(session.getSharedScope());

		return executionScope;
	}

	/**
	 * @return
	 * @throws BirtException
	 */
	private IResultIterator execSubQuery(String parentQueryResultID, String name, Scriptable scope,
			Scriptable parentScope) throws BirtException {
		Object ob = subQueryMap.get(name);
		if (ob == null)
			return null;

		PreparedQueryUtil.mappingParentColumnBinding((ISubqueryDefinition) ob);
		PreparedDummyQuery preparedQuery = new PreparedDummyQuery((ISubqueryDefinition) ob, session);
		preparedQuery.subQueryName = name;
		preparedQuery.subQueryIndex = 0;

		QueryResults queryResults = (QueryResults) preparedQuery.executeQuery(scope, parentScope);
		queryResults.setID(parentQueryResultID);

		return queryResults.getResultIterator();
	}

	/**
	 * 
	 */
	private class QueryResults implements IQueryResults, IQueryService {
		private PreparedDummyQuery preparedQuery;
		private ExprManager exprManager;
		private Scriptable queryScope;
		private Scriptable parentScope;

		private ResultIterator resultIterator;

		private String queryResultID;

		private boolean isClosed;
		private String name;

		/**
		 * @param preparedQuery
		 */
		private QueryResults(PreparedDummyQuery preparedQuery, ExprManager exprManager, Scriptable queryScope,
				Scriptable parentScope) {
			this.preparedQuery = preparedQuery;
			this.exprManager = exprManager;
			this.queryScope = queryScope;
			this.parentScope = parentScope;
			this.isClosed = false;
		}

		/*
		 * @see org.eclipse.birt.data.engine.api.IQueryResults#getID()
		 */
		public String getID() {
			if (queryResultID == null)
				queryResultID = session.getQueryResultIDUtil().nextID();

			return queryResultID;
		}

		/**
		 * @param queryResultID
		 */
		private void setID(String queryResultID) {
			this.queryResultID = queryResultID;
		}

		/*
		 * @see org.eclipse.birt.data.engine.api.IQueryResults#getPreparedQuery()
		 */
		public IPreparedQuery getPreparedQuery() {
			return this.preparedQuery;
		}

		/*
		 * @see org.eclipse.birt.data.engine.api.IQueryResults#getResultMetaData()
		 */
		public IResultMetaData getResultMetaData() throws BirtException {
			return null;
		}

		/*
		 * @see org.eclipse.birt.data.engine.api.IQueryResults#getResultIterator()
		 */
		public IResultIterator getResultIterator() throws BirtException {
			if (resultIterator == null) {
				this.exprManager.validateColumnBinding();
				resultIterator = new ResultIterator(this, exprManager, queryScope, parentScope);
			}

			return resultIterator;
		}

		/*
		 * @see org.eclipse.birt.data.engine.api.IQueryResults#close()
		 */
		public void close() throws BirtException {
			this.isClosed = true;
			NamingRelationUtil.merge(session, preparedQuery.getReportQueryDefn(), this);
		}

		/*
		 * @see org.eclipse.birt.data.engine.impl.IQueryService#isClosed()
		 */
		public boolean isClosed() {
			return isClosed;
		}

		/*
		 * Dummy query only can be the most outer query.
		 * 
		 * @see org.eclipse.birt.data.engine.impl.IQueryService#getNestedLevel()
		 */
		public int getNestedLevel() {
			return 0;
		}

		/*
		 * @see org.eclipse.birt.data.engine.impl.IQueryService#getQueryScope()
		 */
		public Scriptable getQueryScope() {
			return queryScope;
		}

		/*
		 * @see org.eclipse.birt.data.engine.impl.IQueryService#getExecutorHelper()
		 */
		public IExecutorHelper getExecutorHelper() throws DataException {
			return new IExecutorHelper() {

				/*
				 * @see org.eclipse.birt.data.engine.impl.IExecutorHelper#getParent()
				 */
				public IExecutorHelper getParent() {
					return null;
				}

				/*
				 * @see org.eclipse.birt.data.engine.impl.IExecutorHelper#getJSRowObject()
				 */
				public Scriptable getScriptable() {
					return resultIterator.getJSDummyRowObject();
				}
			};
		}

		/*
		 * This can be not implemented, since it is only used for rows JS object.
		 * 
		 * @see org.eclipse.birt.data.engine.impl.IQueryService#getDataSetRuntime(int)
		 */
		public DataSetRuntime[] getDataSetRuntime(int nestedCount) {
			return null;
		}

		public void cancel() {
			// TODO Auto-generated method stub

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.data.engine.api.INamedObject#setName(java.lang.String)
		 */
		public void setName(String name) {
			this.name = name;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.data.engine.api.INamedObject#getName()
		 */
		public String getName() {
			return name;
		}
	}

	/**
	 * 
	 */
	private class ResultIterator implements IResultIterator {
		private QueryResults queryResults;
		private ExprManager exprManager;
		private Scriptable queryScope;

		private Scriptable jsDummyRowObject;

		private RDSaveUtil rdSaveUtil;
		private Map exprValueMap;

		private final static int NOT_START = 0;
		private final static int IN_ROW = 1;
		private final static int ENDED = 2;

		private int openStatus = NOT_START;

		private OutputStream metaOutputStream = null;
		private DataOutputStream rowOutputStream = null;

		/**
		 * @throws BirtException
		 */
		private void checkOpened() throws BirtException {
			if (openStatus != IN_ROW)
				throw new DataException(ResourceConstants.RESULTITERATOR_NOT_OPEN);
		}

		/**
		 * @param queryResults
		 * @param queryScope
		 * @throws BirtException
		 */
		private ResultIterator(QueryResults queryResults, ExprManager exprManager, Scriptable queryScope,
				Scriptable parentScope) throws BirtException {
			this.queryResults = queryResults;
			this.exprManager = exprManager;
			this.queryScope = queryScope;
			this.jsDummyRowObject = new JSDummyRowObject(exprManager, queryScope, parentScope,
					session.getEngineContext().getScriptContext());

			queryScope.put("row", queryScope, jsDummyRowObject);

			this.getRdSaveUtil().doSaveStart();

			exprValueMap = new HashMap();
			Map exprMap = getBindingMap(exprManager.getBindingExprs());
			Iterator it = exprMap.entrySet().iterator();

			while (it.hasNext()) {
				Map.Entry entry = (Entry) it.next();
				String exprName = (String) entry.getKey();
				IBaseExpression baseExpr = (IBaseExpression) entry.getValue();
				Object exprValue = ExprEvaluateUtil.evaluateRawExpression(baseExpr, queryScope,
						session.getEngineContext().getScriptContext());
				IBinding binding = exprManager.getBinding(exprName);
				if (baseExpr != null && binding.getDataType() != baseExpr.getDataType())
					exprValue = DataTypeUtil.convert(exprValue, binding.getDataType());

				exprValueMap.put(exprName, exprValue);
			}

			this.getRdSaveUtil().doSaveExpr(exprValueMap);

			if (needCache()) {
				try {
					createCacheOutputStream();
					saveMetaData();
					IOUtil.writeInt(this.rowOutputStream, 1);
					cacheRow();
				} catch (IOException e) {
					throw new DataException(ResourceConstants.CREATE_CACHE_TEMPFILE_ERROR);
				}
			}
		}

		/**
		 * 
		 * @throws IOException
		 * @throws BirtException
		 */
		private void cacheRow() throws IOException, BirtException {
			Object[] columns = exprValueMap.keySet().toArray();
			IOUtil.writeInt(rowOutputStream, columns.length);
			for (int i = 0; i < columns.length; i++) {
				IOUtil.writeObject(rowOutputStream, columns[i]);
			}
			IOUtil.writeInt(rowOutputStream, 0);
			IOUtil.writeInt(rowOutputStream, 0);
			IOUtil.writeInt(rowOutputStream, 0);
			Iterator iterator = exprValueMap.values().iterator();
			while (iterator.hasNext()) {
				IOUtil.writeObject(rowOutputStream, iterator.next());
			}
			closeCacheOutputStream();
		}

		/**
		 * @throws DataException
		 * @throws IOException
		 * 
		 */
		private void closeCacheOutputStream() throws DataException {
			try {
				if (rowOutputStream != null) {
					IOUtil.writeInt(rowOutputStream, -1);
					rowOutputStream.close();
					rowOutputStream = null;
				}
			} catch (IOException e) {
				throw new DataException(ResourceConstants.CLOSE_CACHE_TEMPFILE_ERROR);
			}
		}

		/**
		 * @return
		 */
		private Scriptable getJSDummyRowObject() {
			return this.jsDummyRowObject;
		}

		/*
		 * 
		 */
		private void createCacheOutputStream() throws FileNotFoundException, DataException {
			File tmpDir = new File(session.getTempDir());
			if (!FileSecurity.fileExist(tmpDir) || !FileSecurity.fileIsDirectory(tmpDir)) {
				FileSecurity.fileMakeDirs(tmpDir);
			}
			metaOutputStream = new BufferedOutputStream(FileSecurity.createFileOutputStream(
					ResultSetCacheUtil.getMetaFile(session.getTempDir(), queryResults.getID())), 1024);
			rowOutputStream = new DataOutputStream(new BufferedOutputStream(FileSecurity.createFileOutputStream(
					ResultSetCacheUtil.getDataFile(session.getTempDir(), queryResults.getID())), 1024));
			File file = ResultSetCacheUtil.getDataFile(session.getTempDir(), queryResults.getID());
//			FileSecurity.fileDeleteOnExit( file );
			file = ResultSetCacheUtil.getMetaFile(session.getTempDir(), queryResults.getID());
//			FileSecurity.fileDeleteOnExit( file );
		}

		/*
		 * 
		 */
		private void saveMetaData() throws DataException, IOException {
			List<IBinding> metaMap = new ArrayList<IBinding>();
			populateDataSetRowMapping(metaMap, getResultClass());
			((ResultClass) (getResultClass())).doSave(metaOutputStream, metaMap, VersionManager.getLatestVersion());
			if (metaOutputStream != null) {
				metaOutputStream.close();
				metaOutputStream = null;
			}
		}

		/**
		 * Populate the new rsClass object instance
		 * 
		 * @param metaMap
		 * @throws DataException
		 */
		private void populateDataSetRowMapping(List<IBinding> metaMap, IResultClass rsClass) throws DataException {
			for (int i = 0; i < rsClass.getFieldCount(); i++) {
				IBinding binding = new Binding(rsClass.getFieldName(i + 1));
				binding.setExpression(
						new ScriptExpression(ExpressionUtil.createJSDataSetRowExpression(rsClass.getFieldName(i + 1))));
				metaMap.add(binding);
			}
		}

		/*
		 * 
		 */
		private boolean needCache() {
			if (queryResults == null || queryResults.getPreparedQuery().getReportQueryDefn() == null)
				return false;
			return queryResults.getPreparedQuery().getReportQueryDefn().cacheQueryResults();
		}

		/*
		 * @see org.eclipse.birt.data.engine.api.IResultIterator#getQueryResults()
		 */
		public IQueryResults getQueryResults() {
			return this.queryResults;
		}

		/*
		 * @see org.eclipse.birt.data.engine.api.IResultIterator#getScope()
		 */
		public Scriptable getScope() {
			return this.queryScope;
		}

		/*
		 * @see org.eclipse.birt.data.engine.api.IResultIterator#getResultMetaData()
		 */
		public IResultMetaData getResultMetaData() throws BirtException {
			return new ResultMetaData(new ResultClass(new ArrayList()));
		}

		/*
		 * 
		 */
		private IResultClass getResultClass() throws DataException {
			List columns = new ArrayList();
			Iterator bindings = queryResults.getPreparedQuery().getReportQueryDefn().getBindings().values().iterator();
			int position = 0;
			while (bindings.hasNext()) {
				IBinding binding = (IBinding) (bindings.next());
				columns.add(new ResultFieldMetadata(position, binding.getBindingName(), binding.getDisplayName(),
						binding.getClass(), null, false, -1));
				position++;
			}
			return new ResultClass(columns);
		}

		/*
		 * @see org.eclipse.birt.data.engine.api.IResultIterator#next()
		 */
		public boolean next() throws BirtException {
			if (this.openStatus == NOT_START) {
				this.openStatus = IN_ROW;
				return true;
			} else if (this.openStatus == IN_ROW) {
				this.openStatus = ENDED;
				return false;
			} else {
				throw new DataException(ResourceConstants.RESULTITERATOR_CLOSED);
			}
		}

		/*
		 * @see org.eclipse.birt.data.engine.api.IResultIterator#getRowId()
		 */
		public int getRowId() throws BirtException {
			return getRowIndex();
		}

		/*
		 * @see org.eclipse.birt.data.engine.api.IResultIterator#getRowIndex()
		 */
		public int getRowIndex() throws BirtException {
			checkOpened();

			return 0;
		}

		/*
		 * @see org.eclipse.birt.data.engine.api.IResultIterator#moveTo(int)
		 */
		public void moveTo(int rowIndex) throws BirtException {
			this.checkOpened();

			if (rowIndex > 0)
				throw new DataException(ResourceConstants.INVALID_ROW_INDEX, Integer.valueOf(rowIndex));
		}

		/*
		 * @see
		 * org.eclipse.birt.data.engine.api.IResultIterator#getValue(java.lang.String)
		 */
		public Object getValue(String name) throws BirtException {
			checkOpened();

			if (exprManager.getExpr(name) == null)
				throw new DataException(ResourceConstants.INVALID_BOUND_COLUMN_NAME, name);

			Object o = exprValueMap.get(name);
			IBinding b = exprManager.getBinding(name);
			if (o != null && b != null) {
				o = DataTypeUtil.convert(o, b.getDataType());
			}
			return o;
		}

		/**
		 * @param manualBindingExprs
		 * @return
		 * @throws DataException
		 */
		private Map getBindingMap(List manualBindingExprs) throws DataException {
			Map exprMap = new HashMap();
			// put the expressions of array into a list
			int size = manualBindingExprs.size();
			GroupBindingColumn[] groupBindingColumns = new GroupBindingColumn[size];
			Iterator itr = manualBindingExprs.iterator();
			while (itr.hasNext()) {
				GroupBindingColumn temp = (GroupBindingColumn) itr.next();
				groupBindingColumns[temp.getGroupLevel()] = temp;
			}

			for (int i = 0; i < size; i++) {
				itr = groupBindingColumns[i].getColumnNames().iterator();
				while (itr.hasNext()) {
					String exprName = (String) itr.next();
					IBaseExpression baseExpr = groupBindingColumns[i].getExpression(exprName);
					exprMap.put(exprName, baseExpr);
				}
			}
			return exprMap;
		}

		/*
		 * @see
		 * org.eclipse.birt.data.engine.api.IResultIterator#getBoolean(java.lang.String)
		 */
		public Boolean getBoolean(String name) throws BirtException {
			return DataTypeUtil.toBoolean(this.getValue(name));
		}

		/*
		 * @see
		 * org.eclipse.birt.data.engine.api.IResultIterator#getInteger(java.lang.String)
		 */
		public Integer getInteger(String name) throws BirtException {
			return DataTypeUtil.toInteger(this.getValue(name));
		}

		/*
		 * @see
		 * org.eclipse.birt.data.engine.api.IResultIterator#getDouble(java.lang.String)
		 */
		public Double getDouble(String name) throws BirtException {
			return DataTypeUtil.toDouble(this.getValue(name));
		}

		/*
		 * @see
		 * org.eclipse.birt.data.engine.api.IResultIterator#getString(java.lang.String)
		 */
		public String getString(String name) throws BirtException {
			return DataTypeUtil.toString(this.getValue(name));
		}

		/*
		 * @see
		 * org.eclipse.birt.data.engine.api.IResultIterator#getBigDecimal(java.lang.
		 * String)
		 */
		public BigDecimal getBigDecimal(String name) throws BirtException {
			return DataTypeUtil.toBigDecimal(this.getValue(name));
		}

		/*
		 * @see
		 * org.eclipse.birt.data.engine.api.IResultIterator#getDate(java.lang.String)
		 */
		public Date getDate(String name) throws BirtException {
			return DataTypeUtil.toDate(this.getValue(name));
		}

		/*
		 * @see
		 * org.eclipse.birt.data.engine.api.IResultIterator#getBlob(java.lang.String)
		 */
		public Blob getBlob(String name) throws BirtException {
			return DataTypeUtil.toBlob(this.getValue(name));
		}

		/*
		 * @see
		 * org.eclipse.birt.data.engine.api.IResultIterator#getBytes(java.lang.String)
		 */
		public byte[] getBytes(String name) throws BirtException {
			return DataTypeUtil.toBytes(this.getValue(name));
		}

		/*
		 * @see org.eclipse.birt.data.engine.api.IResultIterator#skipToEnd(int)
		 */
		public void skipToEnd(int groupLevel) throws BirtException {
			this.checkOpened();

			if (groupLevel > 0)
				throw new DataException(ResourceConstants.INVALID_GROUP_LEVEL, Integer.valueOf(groupLevel));
		}

		/*
		 * @see org.eclipse.birt.data.engine.api.IResultIterator#getStartingGroupLevel()
		 */
		public int getStartingGroupLevel() throws BirtException {
			this.checkOpened();

			return 0;
		}

		/*
		 * @see org.eclipse.birt.data.engine.api.IResultIterator#getEndingGroupLevel()
		 */
		public int getEndingGroupLevel() throws BirtException {
			this.checkOpened();

			return 0;
		}

		/*
		 * @see
		 * org.eclipse.birt.data.engine.api.IResultIterator#getSecondaryIterator(java.
		 * lang.String, org.mozilla.javascript.Scriptable)
		 */
		public IResultIterator getSecondaryIterator(String subQueryName, Scriptable scope) throws BirtException {
			this.checkOpened();

			return queryResults.preparedQuery.execSubQuery(getQueryResultsID(), subQueryName,
					scope != null ? scope : queryScope, this.jsDummyRowObject);
		}

		public IResultIterator getSecondaryIterator(ScriptContext context, String subQueryName) throws BirtException {
			Scriptable scope = null;
			if (context != null)
				scope = ((IDataScriptEngine) context.getScriptEngine(IDataScriptEngine.ENGINE_NAME))
						.getJSScope(context);
			return this.getSecondaryIterator(subQueryName, scope);
		}

		/**
		 * @return
		 */
		private String getQueryResultsID() {
			if (subQueryName == null)
				return this.queryResults.getID();
			else
				return this.queryResults.getID() + "/" + subQueryName + "/" + subQueryIndex;
		}

		/*
		 * @see org.eclipse.birt.data.engine.api.IResultIterator#close()
		 */
		public void close() throws BirtException {
			this.openStatus = ENDED;
			this.getRdSaveUtil().doSaveFinish();
		}

		/*
		 * @see
		 * org.eclipse.birt.data.engine.api.IResultIterator#findGroup(java.lang.Object[]
		 * )
		 */
		public boolean findGroup(Object[] groupKeyValues) throws BirtException {
			this.checkOpened();

			return false;
		}

		/**
		 * @return
		 */
		private RDSaveUtil getRdSaveUtil() {
			if (this.rdSaveUtil == null) {
				rdSaveUtil = new RDSaveUtil(session.getEngineContext(), queryDefn, queryResults.getID());
			}

			return this.rdSaveUtil;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.data.engine.api.IResultIterator#isEmpty()
		 */
		public boolean isEmpty() throws BirtException {
			return false;
		}

		public boolean isBeforeFirst() throws BirtException {
			return !isEmpty() && openStatus == NOT_START;
		}

		public boolean isFirst() throws BirtException {
			return !isEmpty() && openStatus == IN_ROW;
		}

	}

	/**
	 * 
	 */
	private class RDSaveUtil {

		// context info
		private DataEngineContext context;
		private String queryResultID;
		private IBaseQueryDefinition queryDefn;

		// report document save and load instance
		private IRDSave rdSave;

		private boolean isBasicSaved;

		/**
		 * @param context
		 * @param queryDefn
		 * @param queryResultID
		 */
		RDSaveUtil(DataEngineContext context, IBaseQueryDefinition queryDefn, String queryResultID) {
			this.context = context;
			this.queryDefn = queryDefn;
			this.queryResultID = queryResultID;
		}

		/**
		 * @param name
		 * @param value
		 * @throws DataException
		 */
		void doSaveExpr(Map valueMap) throws DataException {
			if (needsSaveToDoc() == false)
				return;

			if (isBasicSaved == false) {
				isBasicSaved = true;

				int groupLevel;
				int[] subQueryInfo;

				if (subQueryName == null) {
					groupLevel = -1;
					subQueryInfo = null;
				} else {
					groupLevel = 1;
					subQueryInfo = new int[] { 0, 1 };
				}

				this.getRdSave().saveResultIterator(new DummyCachedResult(), groupLevel, subQueryInfo);
			}

			this.getRdSave().saveExprValue(0, valueMap);
		}

		/**
		 * @throws DataException
		 */
		void doSaveFinish() throws DataException {
			if (needsSaveToDoc() == false)
				return;

			if (isBasicSaved == false) {
				isBasicSaved = true;
				this.getRdSave().saveResultIterator(new DummyCachedResult(), -1, new int[] { 0, 1 });
			}

			this.getRdSave().saveFinish(0);
		}

		/**
		 * 
		 * @throws DataException
		 */
		void doSaveStart() throws DataException {
			if (needsSaveToDoc() == false)
				return;

			this.getRdSave().saveStart();
		}

		/**
		 * @return
		 */
		private boolean needsSaveToDoc() {
			if (context == null || context.getMode() != DataEngineContext.MODE_GENERATION)
				return false;

			return true;
		}

		/**
		 * @return
		 * @throws DataException
		 */
		private IRDSave getRdSave() throws DataException {
			if (rdSave == null) {
				rdSave = RDUtil.newSave(this.context, this.queryDefn != null ? this.queryDefn : subQueryDefn, 1,
						new QueryResultInfo(this.queryResultID, subQueryName, subQueryIndex));
			}

			return rdSave;
		}
	}

	/**
	 * 
	 */
	private static class DummyCachedResult extends CachedResultSet {
		/*
		 * @see
		 * org.eclipse.birt.data.engine.executor.transform.CachedResultSet#doSave(org.
		 * eclipse.birt.data.engine.impl.document.StreamWrapper, boolean)
		 */
		public void doSave(StreamWrapper streamWrapper, boolean isSubQuery) throws DataException {
			try {
				if (streamWrapper.getStreamForResultClass() != null) {
					IOUtil.writeInt(streamWrapper.getStreamForResultClass(), 0);
					streamWrapper.getStreamForResultClass().close();
					if (streamWrapper.getStreamForDataSet() != null) {
						IOUtil.writeInt(streamWrapper.getStreamForDataSet(), 0);
						streamWrapper.getStreamForDataSet().close();
					}
					if (streamWrapper.getStreamForDataSetRowLens() != null) {
						IOUtil.writeLong(streamWrapper.getStreamForDataSetRowLens(), 0);
						streamWrapper.getStreamForDataSetRowLens().close();
					}
				}
				IOUtil.writeInt(streamWrapper.getStreamForGroupInfo(), 0);
			} catch (IOException e) {
				throw new DataException(ResourceConstants.RD_SAVE_ERROR, e, "Result Class");
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.birt.data.engine.executor.transform.CachedResultSet#getRowCount()
		 */
		public int getRowCount() throws DataException {
			return 1;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.api.IBasePreparedQuery#execute(org.eclipse.birt.
	 * data.engine.api.IBaseQueryResults, org.mozilla.javascript.Scriptable)
	 */
	public IQueryResults execute(IBaseQueryResults outerResults, Scriptable scope) throws DataException {
		try {
			return executeQuery(scope, null);
		} catch (BirtException e) {
			throw DataException.wrap(e);
		}
	}

}

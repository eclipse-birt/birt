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

package org.eclipse.birt.data.engine.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IColumnDefinition;
import org.eclipse.birt.data.engine.api.IComputedColumn;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IScriptDataSetDesign;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.DataSourceFactory;
import org.eclipse.birt.data.engine.executor.ResultClass;
import org.eclipse.birt.data.engine.executor.ResultFieldMetadata;
import org.eclipse.birt.data.engine.executor.ResultObject;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.odi.ICandidateQuery;
import org.eclipse.birt.data.engine.odi.ICustomDataSet;
import org.eclipse.birt.data.engine.odi.IDataSource;
import org.eclipse.birt.data.engine.odi.IEventHandler;
import org.eclipse.birt.data.engine.odi.IQuery;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 * A prepared query which uses a Script Data Source.
 */
class PreparedScriptDSQuery extends PreparedDataSourceQuery implements IPreparedQuery {
	/**
	 * @param dataEngine
	 * @param queryDefn
	 * @param dataSetDesign
	 * @param appContext
	 * @throws DataException
	 */
	PreparedScriptDSQuery(DataEngineImpl dataEngine, IQueryDefinition queryDefn, IBaseDataSetDesign dataSetDesign,
			Map appContext, IQueryContextVisitor contextVisitor) throws DataException {
		super(dataEngine, queryDefn, dataSetDesign, appContext, contextVisitor);
		Object[] params = { dataEngine, queryDefn, dataSetDesign, appContext };
		logger.entering(PreparedScriptDSQuery.class.getName(), "PreparedScriptDSQuery", params);

		logger.exiting(PreparedScriptDSQuery.class.getName(), "PreparedScriptDSQueryss");
		logger.logp(Level.FINER, PreparedScriptDSQuery.class.getName(), "PreparedScriptDSQuery",
				"PreparedScriptDSQuery starts up.");
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.IPreparedQuery#getParameterMetaData()
	 */
	public Collection getParameterMetaData() throws DataException {
		DataException e = new DataException(ResourceConstants.PARAMETER_METADATA_NOT_SUPPORTED);
		logger.logp(Level.FINE, PreparedDataSourceQuery.class.getName(), "getParameterMetaData",
				"Cannot get parameter metadata for this type of data source.", e);
		throw e;
	}

	/*
	 * @see org.eclipse.birt.data.engine.impl.PreparedDataSourceQuery#newExecutor()
	 */
	protected QueryExecutor newExecutor() {
		return new ScriptDSQueryExecutor();
	}

	/**
	 * Concrete class of DSQueryExecutor used in PreparedScriptDSQuery
	 */
	class ScriptDSQueryExecutor extends DSQueryExecutor {
		private ResultClass resultClass;
		private CustomDataSet customDataSet;

		/*
		 * @see
		 * org.eclipse.birt.data.engine.impl.PreparedQuery.Executor#createOdiDataSource(
		 * )
		 */
		protected IDataSource createOdiDataSource() throws DataException {
			// An empty odi data source is used for script data set
			PreparedScriptDSQuery self = PreparedScriptDSQuery.this;
			return DataSourceFactory.getFactory().getDataSource(null, null, self.dataEngine.getSession());
		}

		/*
		 * @see
		 * org.eclipse.birt.data.engine.impl.PreparedQuery.Executor#createOdiQuery()
		 */
		protected IQuery createOdiQuery() throws DataException {
			assert odiDataSource != null;
			ICandidateQuery candidateQuery = odiDataSource.newCandidateQuery(this.fromCache());
			return candidateQuery;
		}

		@Override
		protected boolean fromCache() throws DataException {
			return super.fromCache() && (this.dataSet.getDesign() instanceof IScriptDataSetDesign);
		}

		/*
		 * @see
		 * org.eclipse.birt.data.engine.impl.PreparedQuery.Executor#populateOdiQuery()
		 */
		protected void populateOdiQuery() throws DataException {
			super.populateOdiQuery();

			ICandidateQuery candidateQuery = (ICandidateQuery) odiQuery;
			assert candidateQuery != null;

			ScriptDataSetRuntime scriptDataSet = (ScriptDataSetRuntime) dataSet;
			List resultHints = dataSet.getResultSetHints();
			List computedColumns = dataSet.getComputedColumns();
			List columnsList = new ArrayList();

			// Resolve parameter binding
			resolveDataSetParameters(true);

			// If a "describe" script or handler exists and it
			// returns true, use dynamic metadata;
			// otherwise use static columns defined in result hint
			if (scriptDataSet.describe()) {
				columnsList.addAll(scriptDataSet.getDescribedMetaData());
			} else {
				Iterator it = resultHints.iterator();
				for (int j = 0; it.hasNext(); j++) {
					IColumnDefinition columnDefn = (IColumnDefinition) it.next();

					// All columns are declared as custom to allow as to set column value
					// at runtime
					ResultFieldMetadata columnMetaData = new ResultFieldMetadata(j + 1, columnDefn.getColumnName(),
							columnDefn.getColumnName(), DataType.getClass(columnDefn.getDataType()),
							null /* nativeTypeName */, true, columnDefn.getAnalysisType(),
							columnDefn.getAnalysisColumn(), columnDefn.isIndexColumn(),
							columnDefn.isCompressedColumn());
					columnsList.add(columnMetaData);
					columnMetaData.setAlias(columnDefn.getAlias());
				}
			}

			// Add computed columns
			int count = columnsList.size();
			Iterator it = computedColumns.iterator();
			for (int j = resultHints.size(); it.hasNext(); j++) {
				IComputedColumn compColumn = (IComputedColumn) it.next();
				ResultFieldMetadata columnMetaData = new ResultFieldMetadata(++count, compColumn.getName(),
						compColumn.getName(), DataType.getClass(compColumn.getDataType()), null /* nativeTypeName */,
						true, -1);
				columnsList.add(columnMetaData);
			}

			resultClass = new ResultClass(columnsList);
		}

		/*
		 * @see
		 * org.eclipse.birt.data.engine.impl.PreparedQuery.Executor#prepareOdiQuery()
		 */
		protected void prepareOdiQuery() throws DataException {
			assert odiQuery != null;
			assert resultClass != null;
			assert dataSet instanceof ScriptDataSetRuntime;

			ICandidateQuery candidateQuery = (ICandidateQuery) odiQuery;
			customDataSet = new CustomDataSet();
			candidateQuery.setCandidates(customDataSet);
		}

		/*
		 * @see
		 * org.eclipse.birt.data.engine.impl.PreparedQuery.Executor#executeOdiQuery()
		 */
		protected IResultIterator executeOdiQuery(IEventHandler eventHandler) throws DataException {
			// prepareOdiQuery must be called before
			customDataSet.open();
			dataSetAfterOpen();
			ICandidateQuery candidateQuery = (ICandidateQuery) odiQuery;
			return candidateQuery.execute(eventHandler);
		}

		/**
		 * 
		 */
		private final class CustomDataSet implements ICustomDataSet {
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
				((ScriptDataSetRuntime) dataSet).open();
			}

			/*
			 * @see org.eclipse.birt.data.engine.odi.ICustomDataSet#fetch()
			 */
			public IResultObject fetch() throws DataException {
				Object[] fields = new Object[resultClass.getFieldCount()];
				ResultObject resultObject = new ResultObject(resultClass, fields);

				dataSet.setRowObject(resultObject, true);
				boolean evalResult = ((ScriptDataSetRuntime) dataSet).fetch();

				if (!evalResult)
					resultObject = null;

				return resultObject;
			}

			/*
			 * @see org.eclipse.birt.data.engine.odi.ICustomDataSet#close()
			 */
			public void close() throws DataException {
				((ScriptDataSetRuntime) dataSet).close();
			}
		}
	}

}

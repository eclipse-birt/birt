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
package org.eclipse.birt.data.engine.executor.dscache;

import java.util.Collection;
import java.util.List;

import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.BaseQuery;
import org.eclipse.birt.data.engine.executor.QueryExecutionStrategyUtil;
import org.eclipse.birt.data.engine.executor.QueryExecutionStrategyUtil.Strategy;
import org.eclipse.birt.data.engine.executor.transform.CachedResultSet;
import org.eclipse.birt.data.engine.executor.transform.SimpleResultSet;
import org.eclipse.birt.data.engine.impl.DataEngineImpl;
import org.eclipse.birt.data.engine.impl.DataEngineSession;
import org.eclipse.birt.data.engine.odi.IDataSetPopulator;
import org.eclipse.birt.data.engine.odi.IDataSourceQuery;
import org.eclipse.birt.data.engine.odi.IEventHandler;
import org.eclipse.birt.data.engine.odi.IPreparedDSQuery;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.eclipse.birt.data.engine.odi.IResultObject;
import org.eclipse.datatools.connectivity.oda.spec.QuerySpecification;

/**
 * In design time, this query will retrieve data from cache.
 */
public class DataSourceQuery extends BaseQuery implements IDataSourceQuery, IPreparedDSQuery {
	//
	private DataEngineSession session;

	/**
	 * 
	 * @param context
	 */
	public DataSourceQuery(DataEngineSession session) {
		this.session = session;
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.odi.IDataSourceQuery#setResultHints(java.util.
	 * Collection)
	 */
	public void setResultHints(Collection columnDefns) {
		// do nothing
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.odi.IDataSourceQuery#setResultProjection(java.
	 * lang.String[])
	 */
	public void setResultProjection(String[] fieldNames) throws DataException {
		// do nothing
	}

	public void setParameterHints(Collection parameterDefns) {
		// do nothing
	}

	/*
	 * @see org.eclipse.birt.data.engine.odi.IDataSourceQuery#addProperty(java.lang.
	 * String, java.lang.String)
	 */
	public void addProperty(String name, String value) throws DataException {
		// do nothing
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.odi.IDataSourceQuery#declareCustomField(java.
	 * lang.String, int)
	 */
	public void declareCustomField(String fieldName, int dataType) throws DataException {
		// do nothing
	}

	/*
	 * @see org.eclipse.birt.data.engine.odi.IDataSourceQuery#prepare()
	 */
	public IPreparedDSQuery prepare() throws DataException {
		return this;
	}

	/** */
	private DataSetFromCache datasetFromCache;

	/*
	 * @see org.eclipse.birt.data.engine.odi.IPreparedDSQuery#getResultClass()
	 */
	public IResultClass getResultClass() throws DataException {
		return getOdaCacheResultSet().getResultClass();
	}

	/*
	 * @see org.eclipse.birt.data.engine.odi.IPreparedDSQuery#getParameterMetaData()
	 */
	public Collection getParameterMetaData() throws DataException {
		return null;
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.odi.IPreparedDSQuery#getOutputParameterValue(
	 * int)
	 */
	public Object getOutputParameterValue(int index) throws DataException {
		return null;
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.odi.IPreparedDSQuery#getOutputParameterValue(
	 * java.lang.String)
	 */
	public Object getOutputParameterValue(String name) throws DataException {
		return null;
	}

	/**
	 * Set temporary computed columns to DatasetCache. DatasetCache will use these
	 * objects to produce ResultClass.
	 * 
	 * @param addedTempComputedColumn
	 */
	public void setTempComputedColumn(List addedTempComputedColumn) {
		getOdaCacheResultSet().setTempComputedColumn(addedTempComputedColumn);
	}

	/*
	 * @see org.eclipse.birt.data.engine.odi.IPreparedDSQuery#execute()
	 */
	public IResultIterator execute(IEventHandler eventHandler) throws DataException {
		if (((session.getEngineContext().getMode() == DataEngineContext.DIRECT_PRESENTATION
				|| session.getEngineContext().getMode() == DataEngineContext.MODE_GENERATION))
				&& this.getQueryDefinition() instanceof IQueryDefinition) {
			IQueryDefinition queryDefn = (IQueryDefinition) this.getQueryDefinition();

			Strategy strategy = QueryExecutionStrategyUtil.getQueryExecutionStrategy(this.session, queryDefn,
					queryDefn.getDataSetName() == null ? null
							: ((DataEngineImpl) this.session.getEngine()).getDataSetDesign(queryDefn.getDataSetName()));
			if (strategy != Strategy.Complex) {
				SimpleResultSet simpleResult = new SimpleResultSet(this, new IDataSetPopulator() {

					public IResultObject next() throws DataException {
						return getOdaCacheResultSet().fetch();
					}

				}, getOdaCacheResultSet().getResultClass(), eventHandler, this.getGrouping(), this.session,
						strategy == Strategy.SimpleLookingFoward);

				return simpleResult.getResultSetIterator();
			}
		}

		return new CachedResultSet(this, getOdaCacheResultSet().getResultClass(), getOdaCacheResultSet(), eventHandler,
				session);
	}

	/*
	 * @see org.eclipse.birt.data.engine.odi.IPreparedDSQuery#close()
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
	 * @return OdaCacheResultSet
	 */
	private DataSetFromCache getOdaCacheResultSet() {
		if (datasetFromCache == null)
			datasetFromCache = new DataSetFromCache(session);

		return datasetFromCache;
	}

	public void setQuerySpecification(QuerySpecification spec) {
		// TODO Auto-generated method stub

	}

}

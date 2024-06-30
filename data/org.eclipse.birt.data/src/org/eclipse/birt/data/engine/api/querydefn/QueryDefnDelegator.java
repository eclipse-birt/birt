/*******************************************************************************
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
 *******************************************************************************/

package org.eclipse.birt.data.engine.api.querydefn;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryExecutionHints;
import org.eclipse.birt.data.engine.core.DataException;

public class QueryDefnDelegator extends QueryDefinition {

	protected IBaseQueryDefinition baseQuery;
	protected String queryResultsId;
	protected String dataSetName;

	public QueryDefnDelegator(IBaseQueryDefinition query) {
		baseQuery = query;
		if (query instanceof IQueryDefinition) {
			queryResultsId = ((IQueryDefinition) query).getQueryResultsID();
			dataSetName = ((IQueryDefinition) query).getDataSetName();
		}
	}

	public QueryDefnDelegator(IBaseQueryDefinition query, String queryResultsId, String dataSetName) {
		this.baseQuery = query;
		this.queryResultsId = queryResultsId;
		this.dataSetName = dataSetName;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.api.querydefn.BaseQueryDefinition#usesDetails ()
	 */
	@Override
	public boolean usesDetails() {
		return baseQuery.usesDetails();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.querydefn.BaseQueryDefinition#
	 * cacheQueryResults()
	 */
	@Override
	public boolean cacheQueryResults() {
		return baseQuery.cacheQueryResults();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.api.querydefn.BaseQueryDefinition#getParentQuery
	 * ()
	 */
	@Override
	public IBaseQueryDefinition getParentQuery() {
		return baseQuery.getParentQuery();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.api.querydefn.BaseQueryDefinition#getMaxRows ()
	 */
	@Override
	public int getMaxRows() {
		return baseQuery.getMaxRows();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.api.querydefn.BaseQueryDefinition#setMaxRows
	 * (int)
	 */
	@Override
	public void setMaxRows(int maxRows) {
		baseQuery.setMaxRows(maxRows);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.querydefn.BaseQueryDefinition#
	 * getResultSetExpressions()
	 */
	@Override
	public Map getResultSetExpressions() {
		return baseQuery.getResultSetExpressions();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.api.querydefn.BaseQueryDefinition#addBinding
	 * (org.eclipse.birt.data.engine.api.IBinding)
	 */
	@Override
	public void addBinding(IBinding binding) throws DataException {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.api.querydefn.BaseQueryDefinition#getBindings ()
	 */
	@Override
	public Map getBindings() {
		return baseQuery.getBindings();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.querydefn.BaseQueryDefinition#
	 * getQueryExecutionHints()
	 */
	@Override
	public IQueryExecutionHints getQueryExecutionHints() {
		return baseQuery.getQueryExecutionHints();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.api.querydefn.BaseQueryDefinition#getStartingRow
	 * ()
	 */
	@Override
	public int getStartingRow() {
		return baseQuery.getStartingRow();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.querydefn.BaseQueryDefinition#
	 * getDistinctValue()
	 */
	@Override
	public boolean getDistinctValue() {
		return baseQuery.getDistinctValue();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.querydefn.BaseTransform#getFilters()
	 */
	@Override
	public List getFilters() {
		return baseQuery.getFilters();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.querydefn.BaseTransform#getSubqueries()
	 */
	@Override
	public Collection getSubqueries() {
		return baseQuery.getSubqueries();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.querydefn.BaseTransform#getSorts()
	 */
	@Override
	public List getSorts() {
		return baseQuery.getSorts();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.querydefn.BaseQueryDefinition#setName
	 * (java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.dataSetName = name;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.querydefn.BaseQueryDefinition#getName()
	 */
	@Override
	public String getName() {
		return baseQuery.getName();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.api.querydefn.QueryDefinition#getDataSetName ()
	 */
	@Override
	public String getDataSetName() {
		return dataSetName;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.api.querydefn.QueryDefinition#getQueryResultsID
	 * ()
	 */
	@Override
	public String getQueryResultsID() {
		return queryResultsId;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.api.querydefn.QueryDefinition#needAutoBinding ()
	 */
	@Override
	public boolean needAutoBinding() {
		if (baseQuery instanceof QueryDefinition) {
			return ((QueryDefinition) baseQuery).needAutoBinding();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.querydefn.QueryDefinition#
	 * getInputParamBindings()
	 */
	@Override
	public Collection getInputParamBindings() {
		if (baseQuery instanceof QueryDefinition) {
			return ((QueryDefinition) baseQuery).getInputParamBindings();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.querydefn.QueryDefinition#
	 * getColumnProjection()
	 */
	@Override
	public String[] getColumnProjection() {
		if (baseQuery instanceof QueryDefinition) {
			return ((QueryDefinition) baseQuery).getColumnProjection();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.api.querydefn.QueryDefinition#getSourceQuery()
	 */
	@Override
	public IBaseQueryDefinition getSourceQuery() {
		if (baseQuery instanceof QueryDefinition) {
			return ((QueryDefinition) baseQuery).getSourceQuery();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.api.querydefn.QueryDefinition#isSummaryQuery()
	 */
	@Override
	public boolean isSummaryQuery() {
		if (baseQuery instanceof QueryDefinition) {
			return ((QueryDefinition) baseQuery).isSummaryQuery();
		}
		return false;
	}

	public IQueryDefinition getBaseQuery() {
		if (baseQuery instanceof QueryDefinition) {
			return (QueryDefinition) baseQuery;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.api.querydefn.QueryDefinition#setSourceQuery(org
	 * .eclipse.birt.data.engine.api.IBaseQueryDefinition)
	 */
	@Override
	public void setSourceQuery(IBaseQueryDefinition object) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.api.querydefn.BaseQueryDefinition#getGroups()
	 */
	@Override
	public List getGroups() {
		return baseQuery.getGroups();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.api.querydefn.QueryDefinition#setQueryResultsID(
	 * java.lang.String)
	 */
	@Override
	public void setQueryResultsID(String queryResultsID) {
		this.queryResultsId = queryResultsID;
	}

}

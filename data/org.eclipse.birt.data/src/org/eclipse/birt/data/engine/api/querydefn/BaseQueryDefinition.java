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
package org.eclipse.birt.data.engine.api.querydefn;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.data.engine.api.IGroupDefinition;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryExecutionHints;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;

/**
 * Default implementation of the
 * {@link org.eclipse.birt.data.engine.api.IBaseQueryDefinition} interface.
 *
 */

abstract public class BaseQueryDefinition extends BaseTransform implements IBaseQueryDefinition {
	protected List groups = new ArrayList();
	protected boolean hasDetail = true;
	private IDataQueryDefinition parentDataQuery; // parent query for nest inside xtab
	protected IBaseQueryDefinition parentQuery;
	protected int maxRowCount = 0;
	protected int startingRow = 0;
	protected boolean distinctValue = false;

	private boolean cacheQueryResults = false;

	// order might be sensitive, use LinkedHashMap instead of HashMap
	private Map resultExprsMap = new LinkedHashMap();
	private Map bindingMap = new LinkedHashMap();
	private IQueryExecutionHints queryExecutionHints = new QueryExecutionHints();
	private String name;
	private boolean isTempQuery = false;

	/**
	 * Constructs an instance with parent set to the specified
	 * <code>BaseQueryDefinition</code>
	 */
	BaseQueryDefinition(IDataQueryDefinition parent) {
		/*
		 * original implemenetation doesn't support nest query in Xtab. Add a new API to
		 * return parent XTAB but keep current API unchanged.
		 *
		 * TODO: need merge those two API as a single one.
		 */
		this.parentDataQuery = parent;
		if (parent instanceof IBaseQueryDefinition) {
			this.parentQuery = (IBaseQueryDefinition) parent;
		}

	}

	/**
	 * Returns the group definitions as an ordered collection of
	 * <code>GroupDefinition</code> objects. Groups are organizations within the
	 * data that support aggregation, filtering and sorting. Reports use groups to
	 * trigger level breaks.
	 *
	 * @return the list of groups. If no group is defined, null is returned.
	 */

	@Override
	public List getGroups() {
		return groups;
	}

	/**
	 * Appends a group definition to the group list.
	 *
	 * @param group Group definition to add
	 */
	public void addGroup(IGroupDefinition group) {
		groups.add(group);
	}

	/**
	 * Indicates if the report will use the detail rows. Allows the data transform
	 * engine to optimize the query if the details are not used.
	 *
	 * @return true if the detail rows are used, false if not used
	 */
	@Override
	public boolean usesDetails() {
		return hasDetail;
	}

	/**
	 * @param usesDetails Whether detail rows are used in this query
	 */
	public void setUsesDetails(boolean usesDetails) {
		this.hasDetail = usesDetails;
	}

	/**
	 * Returns the parent query. The parent query is the outer query which encloses
	 * this query
	 */
	@Override
	public IBaseQueryDefinition getParentQuery() {
		return parentQuery;
	}

	/**
	 * Returns the parent query. The parent query is the outer query which encloses
	 * this query.
	 *
	 * if the parent is XTAB query, it will return null. Call getParentDataQuery
	 * instead for this case.
	 */
	public IDataQueryDefinition getParentDataQuery() {
		return parentDataQuery;
	}

	/**
	 * Gets the maximum number of detail rows that can be retrieved by this report
	 * query
	 *
	 * @return Maximum number of rows. If 0, there is no limit on how many rows this
	 *         query can retrieve.
	 */
	@Override
	public int getMaxRows() {
		return maxRowCount;
	}

	/**
	 * Sets the maximum number of detail rows that can be retrieved by this report
	 * query
	 *
	 */
	@Override
	public void setMaxRows(int maxRows) {
		maxRowCount = maxRows;
	}

	/**
	 * Sets the starting row that will be retrieved by this query
	 *
	 * @param startingRow
	 */
	public void setStartingRow(int startingRow) {
		this.startingRow = startingRow;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.IBaseQueryDefinition#getStartingRow()
	 */
	@Override
	public int getStartingRow() {
		return this.startingRow;
	}

	/**
	 * Sets the distinct value flag.
	 *
	 * @return
	 */
	public void setDistinctValue(boolean distinctValue) {
		this.distinctValue = distinctValue;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.IBaseQueryDefinition#isDistinct()
	 */
	@Override
	public boolean getDistinctValue() {
		return this.distinctValue;
	}

	/**
	 * @param name
	 * @param expression
	 * @deprecated
	 */
	@Deprecated
	public void addResultSetExpression(String name, IBaseExpression expression) {
		Binding binding = new Binding(name);
		binding.setExpression(expression);
		if (expression != null) {
			binding.setDataType(expression.getDataType());
		}
		this.bindingMap.put(name, binding);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.api.IBaseQueryDefinition#addBinding(java.lang.
	 * String, org.eclipse.birt.data.engine.api.IBinding)
	 */
	@Override
	public void addBinding(IBinding binding) throws DataException {
		// TODO remove me
		// Temp solution for backward compatibility util Model make the changes.
		if (binding.getExpression() != null
				&& binding.getExpression().getGroupName().equals(IBaseExpression.GROUP_OVERALL)) {
			binding.getExpression().setGroupName(null);
		}
		final String bindingName = binding.getBindingName();
		if (bindingMap.containsKey(bindingName)) {
			throw new DataException(ResourceConstants.DUPLICATED_BINDING_NAME, bindingName);
		}
		this.bindingMap.put(bindingName, binding);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.IBaseQueryDefinition#getBindings()
	 */
	@Override
	public Map getBindings() {
		for (Iterator it = this.resultExprsMap.keySet().iterator(); it.hasNext();) {
			String key = it.next().toString();
			IBaseExpression expr = (IBaseExpression) this.resultExprsMap.get(key);
			if (this.bindingMap.get(key) == null) {
				Binding binding = new Binding(key);
				binding.setExpression(expr);
				this.bindingMap.put(key, binding);
			}
		}
		return this.bindingMap;
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.api.IBaseTransform#getResultSetExpressions()
	 */
	@Override
	public Map getResultSetExpressions() {
		return this.resultExprsMap;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.IBaseQueryDefinition#needCache()
	 */
	@Override
	public boolean cacheQueryResults() {
		return cacheQueryResults;
	}

	/*
	 * set whether cache query results
	 */
	public void setCacheQueryResults(boolean cacheQueryResults) {
		this.cacheQueryResults = cacheQueryResults;
	}

	/**
	 * Set the query execution hints.
	 *
	 * @param hints
	 */
	public void setQueryExecutionHints(IQueryExecutionHints hints) {
		this.queryExecutionHints = hints;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.api.IBaseQueryDefinition#getQueryExecutionHints(
	 * )
	 */
	@Override
	public IQueryExecutionHints getQueryExecutionHints() {
		return this.queryExecutionHints;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.INamedObject#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.INamedObject#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;

	}

	public void setAsTempQuery() {
		this.isTempQuery = true;
	}

	public boolean isTempQuery() {
		return this.isTempQuery;
	}

	protected void cloneFields(BaseQueryDefinition clone) {
		clone.groups.addAll(groups);
		clone.hasDetail = hasDetail;
		if (parentQuery instanceof IQueryDefinition) {
			clone.parentQuery = ((IQueryDefinition) parentQuery).clone();
		}
		clone.maxRowCount = maxRowCount;
		clone.startingRow = startingRow;
		clone.distinctValue = distinctValue;
		clone.cacheQueryResults = cacheQueryResults;
		clone.resultExprsMap.putAll(resultExprsMap);
		clone.bindingMap.putAll(bindingMap);
		clone.queryExecutionHints = queryExecutionHints;
		clone.name = name;
		clone.isTempQuery = isTempQuery;
	}
}

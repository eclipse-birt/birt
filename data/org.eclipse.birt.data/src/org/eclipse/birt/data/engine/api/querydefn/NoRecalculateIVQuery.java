
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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.core.DataException;

/**
 * Query Definition for optimizing IV.
 * <p>
 * The optimize query will only do no recalculate filtering based on the
 * previous query result set.
 *
 */

public class NoRecalculateIVQuery extends QueryDefnDelegator {
	private IBaseQueryDefinition sourceQuery;
	private HashMap<String, IBinding> bindingsMap = new HashMap<>();
	private List<IFilterDefinition> filters;
	private List<ISortDefinition> sortings;
	private List groups;
	private String name;

	public NoRecalculateIVQuery(IQueryDefinition queryDefn, IBaseQueryDefinition sourceQuery,
			List<ISortDefinition> sorts, List<IFilterDefinition> filters, List groups, String queryResultId)
			throws DataException {
		super(queryDefn);
		this.queryResultsId = queryResultId;
		this.dataSetName = queryDefn.getDataSetName();
		this.sourceQuery = new QueryDefnDelegator(sourceQuery, this.queryResultsId, this.dataSetName);
		this.filters = filters;
		this.sortings = sorts;
		this.groups = groups;

		initBindings();
	}

	private void initBindings() throws DataException {
		Iterator<Map.Entry<String, IBinding>> it = baseQuery.getBindings().entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, IBinding> e = it.next();
			IBinding b = e.getValue();

			Binding n = new Binding(b.getBindingName());
			n.setDataType(b.getDataType());
			n.setDisplayName(b.getDisplayName());
			n.setExportable(b.exportable());
			n.setFilter(b.getFilter());
			n.setTimeFunction(b.getTimeFunction());

			n.setExpression(new ScriptExpression("dataSetRow[\"" + b.getBindingName() + "\"]")); //$NON-NLS-1$//$NON-NLS-2$
			bindingsMap.put(n.getBindingName(), n);
		}
	}

	@Override
	public List getGroups() {
		return groups;
	}

	@Override
	public void addBinding(IBinding binding) throws DataException {
		this.bindingsMap.put(binding.getBindingName(), binding);
	}

	@Override
	public Map getBindings() {
		return this.bindingsMap;
	}

	@Override
	public List getFilters() {
		return filters;
	}

	@Override
	public List getSorts() {
		return sortings;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public IBaseQueryDefinition getSourceQuery() {
		return sourceQuery;
	}

	@Override
	public IQueryDefinition getBaseQuery() {
		return (IQueryDefinition) baseQuery;
	}

	@Override
	public void setSourceQuery(IBaseQueryDefinition object) {
		sourceQuery = object;
	}

}

/*
 *************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */
package org.eclipse.birt.data.engine.api.querydefn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.birt.data.engine.api.IBaseLinkDefinition;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryDefinition;

/**
 * Default implementation of the
 * {@link org.eclipse.birt.data.engine.api.IQueryDefinition} interface
 */

public class QueryDefinition extends BaseQueryDefinition implements IQueryDefinition {
	protected String dataSetName;
	private String queryResultsID;
	protected List bindings = new ArrayList();
	protected String[] projectedColumns;

	private boolean autoBinding = false;
	private boolean isSummaryQuery = false;
	private IBaseQueryDefinition sourceQuery;

	private Set<IBaseLinkDefinition> links = new HashSet<IBaseLinkDefinition>();

	/** Constructs an empty query definition */
	public QueryDefinition() {
		super(null);
	}

	/**
	 * This constructor can only be used in DTE, other module should not call this
	 * constructor to pass the autobinding.
	 * 
	 * @param autoBinding
	 */
	public QueryDefinition(boolean autoBinding) {
		super(null);
		this.autoBinding = autoBinding;
	}

	/**
	 * Constructs a query that is nested within another query. The outer query
	 * (parent) can be another query, or a sub query.
	 * 
	 * @param parent The outer query or subquery
	 */
	public QueryDefinition(IDataQueryDefinition parent) {
		super(parent);
	}

	/**
	 * This constructor can only be used in DTE, other module should not call this
	 * constructor to pass the autobinding.
	 * 
	 * @param parent
	 * @param autoBinding
	 */
	public QueryDefinition(IDataQueryDefinition parent, boolean autoBinding) {
		super(parent);
		this.autoBinding = autoBinding;
	}

	/**
	 * Gets the name of the data set used by this query
	 */
	public String getDataSetName() {
		return dataSetName;
	}

	/**
	 * @param dataSetName Name of data set used by this query.
	 */
	public void setDataSetName(String dataSetName) {
		this.dataSetName = dataSetName;
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.IQueryDefinition#needAutoBinding()
	 */
	public boolean needAutoBinding() {
		return this.autoBinding;
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.IQueryDefinition#getQueryResultID()
	 */
	public String getQueryResultsID() {
		return this.queryResultsID;
	}

	/**
	 * @param queryResultID
	 */
	public void setQueryResultsID(String queryResultsID) {
		this.queryResultsID = queryResultsID;
	}

	/**
	 * Returns the set of input parameter bindings as an unordered collection of
	 * <code>InputParameterBinding</code> objects.
	 * 
	 * @return the input parameter bindings. If no binding is defined, null is
	 *         returned.
	 */
	public Collection getInputParamBindings() {
		return bindings;
	}

	/**
	 * Adds an input parameter binding to this report query.
	 * 
	 * @param binding The bindings to set.
	 */
	public void addInputParamBinding(InputParameterBinding binding) {
		this.bindings.add(binding);
	}

	/**
	 * Provides a column projection hint to the data engine. The caller informs the
	 * data engine that only a selected list of columns defined by the data set are
	 * used by this report query. The names of those columns (the "projected
	 * columns") are passed in as an array of string. <br>
	 * If a column projection is set, runtime error may occur if the report query
	 * uses columns that are not defined in the projected column list.
	 */
	public void setColumnProjection(String[] projectedColumns) {
		this.projectedColumns = projectedColumns;
	}

	/**
	 * @see org.eclipse.birt.data.engine.api.IQueryDefinition#getColumnProjection()
	 */
	public String[] getColumnProjection() {
		return projectedColumns;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IQueryDefinition#getBaseQuery()
	 */
	public IBaseQueryDefinition getSourceQuery() {
		return sourceQuery;
	}

	/**
	 * 
	 * @param sourceQuery
	 */
	public void setSourceQuery(IBaseQueryDefinition sourceQuery) {
		this.sourceQuery = sourceQuery;
	}

	public void setIsSummaryQuery(boolean isSummaryQuery) {
		this.isSummaryQuery = isSummaryQuery;
	}

	public boolean isSummaryQuery() {
		return this.isSummaryQuery;
	}

	public Set<IBaseLinkDefinition> getLinks() {
		return this.links;
	}

	public void addLink(IBaseLinkDefinition link) {
		this.links.add(link);
	}

	public void setLinks(Set<IBaseLinkDefinition> links) {
		this.links = links;
	}

	protected void cloneFields(QueryDefinition clone) {
		super.cloneFields(clone);
		clone.dataSetName = dataSetName;
		clone.queryResultsID = queryResultsID;
		clone.bindings.addAll(bindings);
		clone.projectedColumns = projectedColumns;
		clone.autoBinding = autoBinding;
		clone.isSummaryQuery = isSummaryQuery;
		if (sourceQuery instanceof IQueryDefinition) {
			clone.sourceQuery = ((IQueryDefinition) sourceQuery).clone();
		}
		clone.links.addAll(links);
	}

	public IQueryDefinition clone() {
		QueryDefinition clone = new QueryDefinition();
		cloneFields(clone);

		return clone;
	}

}

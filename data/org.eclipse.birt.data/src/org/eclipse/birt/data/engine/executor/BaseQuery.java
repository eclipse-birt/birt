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
package org.eclipse.birt.data.engine.executor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.transform.IExpressionProcessor;
import org.eclipse.birt.data.engine.odi.IQuery;
import org.eclipse.birt.data.engine.odi.IResultObjectEvent;

/**
 * Implementation of the ODI IQuery interface. Common base class for
 * DataSourceQuery and CandidateQuery
 */
public abstract class BaseQuery implements IQuery {
	private SortSpec[] sorts = new SortSpec[0];
	private GroupSpec[] groups = new GroupSpec[0];
	private int maxRows = 0;
	private int rowFetchLimit = 0;
	private List fetchEventList = null;

	private IExpressionProcessor exprProcessor;

	private boolean distinctValueFlag;
	private IBaseQueryDefinition queryDefinition;

	/**
	 * @see org.eclipse.birt.data.engine.odi.IQuery#setOrdering(java.util.List)
	 */
	public void setOrdering(List sortSpecs) throws DataException {
		if (sortSpecs == null)
			sorts = new SortSpec[0];
		else
			sorts = (SortSpec[]) sortSpecs.toArray(new SortSpec[0]);
	}

	/**
	 * @see org.eclipse.birt.data.engine.odi.IQuery#setGrouping(java.util.List)
	 */
	public void setGrouping(List groupSpecs) throws DataException {
		if (groupSpecs == null)
			groups = new GroupSpec[0];
		else
			groups = (GroupSpec[]) groupSpecs.toArray(new GroupSpec[0]);
	}

	/**
	 * @see org.eclipse.birt.data.engine.odi.IQuery#setMaxRows(int)
	 */
	public void setMaxRows(int maxRows) {
		this.maxRows = maxRows;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.odi.IQuery#setRowFetchLimit(int)
	 */
	public void setRowFetchLimit(int limit) {
		this.rowFetchLimit = limit > 0 ? limit : 0;
	}

	/**
	 * Return the row fetch limit of the data set current query bound to.
	 */
	protected int getRowFetchLimit() {
		return this.rowFetchLimit;
	}

	/**
	 * Gets the query's sort specification. Returns null if no sort specs are
	 * defined.
	 */
	public SortSpec[] getOrdering() {
		return sorts;
	}

	/**
	 * Gets the query's grouping specification. Returns null if no groups are
	 * defined.
	 */
	public GroupSpec[] getGrouping() {
		return groups;
	}

	public int getMaxRows() {
		return maxRows;
	}

	/**
	 * Add event to fetch event list
	 */
	public void addOnFetchEvent(IResultObjectEvent event) {
		assert event != null;

		if (fetchEventList == null)
			fetchEventList = new ArrayList();

		fetchEventList.add(event);
	}

	public List getFetchEvents() {
		return fetchEventList;
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.odi.IQuery#setExprProcessor(org.eclipse.birt.
	 * data.engine.executor.transformation.IExpressionProcessor)
	 */
	public void setExprProcessor(IExpressionProcessor exprProcessor) {
		this.exprProcessor = exprProcessor;
	}

	/**
	 * @return
	 */
	public IExpressionProcessor getExprProcessor() {
		return exprProcessor;
	}

	/**
	 * @return
	 */
	public boolean getDistinctValueFlag() {
		return distinctValueFlag;
	}

	/**
	 * @param distinctValueFlag
	 */
	public void setDistinctValueFlag(boolean distinctValueFlag) {
		this.distinctValueFlag = distinctValueFlag;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.odi.IQuery#setQueryDefinition(org.eclipse.birt.
	 * data.engine.api.IBaseQueryDefinition)
	 */
	public void setQueryDefinition(IBaseQueryDefinition queryDefn) {
		this.queryDefinition = queryDefn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.odi.IQuery#getQueryDefinition()
	 */
	public IBaseQueryDefinition getQueryDefinition() {
		return this.queryDefinition;
	}
}

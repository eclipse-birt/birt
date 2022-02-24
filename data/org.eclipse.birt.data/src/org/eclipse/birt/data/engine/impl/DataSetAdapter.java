/*******************************************************************************
 * Copyright (c) 2004, 2010 Actuate Corporation.
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
import java.util.List;

import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.script.IBaseDataSetEventHandler;

import com.ibm.icu.util.ULocale;

public abstract class DataSetAdapter implements IBaseDataSetDesign {

	private List computedColumns;
	private List filters;
	protected IBaseDataSetDesign source;

	public DataSetAdapter(IBaseDataSetDesign source) {
		this.source = source;
		this.computedColumns = new ArrayList();
		if (this.source.getComputedColumns() != null) {
			this.computedColumns.addAll(this.source.getComputedColumns());
		}
		this.filters = new ArrayList();
		if (this.source.getFilters() != null) {
			this.filters.addAll(this.source.getFilters());
		}
	}

	public IBaseDataSetDesign getSource() {
		return this.source;
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.api.IBaseDataSetDesign#getAfterCloseScript()
	 */
	@Override
	public String getAfterCloseScript() {
		return this.source.getAfterCloseScript();
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.IBaseDataSetDesign#getAfterOpenScript()
	 */
	@Override
	public String getAfterOpenScript() {
		return this.source.getAfterOpenScript();
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.api.IBaseDataSetDesign#getBeforeCloseScript()
	 */
	@Override
	public String getBeforeCloseScript() {
		return this.source.getBeforeCloseScript();
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.api.IBaseDataSetDesign#getBeforeOpenScript()
	 */
	@Override
	public String getBeforeOpenScript() {
		return this.source.getBeforeOpenScript();
	}

	/**
	 * @deprecated
	 */
	@Deprecated
	@Override
	public int getCacheRowCount() {
		return this.source.getCacheRowCount();
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.IBaseDataSetDesign#getComputedColumns()
	 */
	@Override
	public List getComputedColumns() {
		return this.computedColumns;
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.IBaseDataSetDesign#getDataSourceName()
	 */
	@Override
	public String getDataSourceName() {
		return this.source.getDataSourceName();
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.IBaseDataSetDesign#getEventHandler()
	 */
	@Override
	public IBaseDataSetEventHandler getEventHandler() {
		return this.source.getEventHandler();
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.IBaseDataSetDesign#getFilters()
	 */
	@Override
	public List getFilters() {
		return this.filters;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.IBaseDataSetDesign#getSortHints()
	 */
	@Override
	public List getSortHints() {
		return this.source.getSortHints();
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.api.IBaseDataSetDesign#getInputParamBindings()
	 */
	@Override
	public Collection getInputParamBindings() {
		return this.source.getInputParamBindings();
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.IBaseDataSetDesign#getName()
	 */
	@Override
	public String getName() {
		return this.source.getName();
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.IBaseDataSetDesign#getOnFetchScript()
	 */
	@Override
	public String getOnFetchScript() {
		return this.source.getOnFetchScript();
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.IBaseDataSetDesign#getParameters()
	 */
	@Override
	public List getParameters() {
		return this.source.getParameters();
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.IBaseDataSetDesign#getResultSetHints()
	 */
	@Override
	public List getResultSetHints() {
		return this.source.getResultSetHints();
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.IBaseDataSetDesign#getRowFetchLimit()
	 */
	@Override
	public int getRowFetchLimit() {
		return this.source.getRowFetchLimit();
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.IBaseDataSetDesign#needDistinctValue()
	 */
	@Override
	public boolean needDistinctValue() {
		return this.source.needDistinctValue();
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.api.IBaseDataSetDesign#setRowFetchLimit(int)
	 */
	@Override
	public void setRowFetchLimit(int max) {
		this.source.setRowFetchLimit(max);
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.IBaseDataSetDesign#getCompareLocale()
	 */
	@Override
	public ULocale getCompareLocale() {
		return this.source.getCompareLocale();
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.IBaseDataSetDesign#getNullsOrdering()
	 */
	@Override
	public String getNullsOrdering() {
		return this.source.getNullsOrdering();
	}
}

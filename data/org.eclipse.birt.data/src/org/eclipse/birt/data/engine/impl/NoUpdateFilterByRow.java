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
package org.eclipse.birt.data.engine.impl;

import java.util.List;

import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.transform.ResultSetPopulator;
import org.eclipse.birt.data.engine.executor.transform.group.IncrementalUpdateRowFilter;
import org.eclipse.birt.data.engine.odi.IResultObject;

public class NoUpdateFilterByRow implements IFilterByRow {

	private IncrementalUpdateRowFilter rowFilter;
	private IFilterByRow filterByRow;
	private boolean updateGroupInfo;

	public NoUpdateFilterByRow(IFilterByRow filterByRow, ResultSetPopulator populator) throws DataException {
		this.filterByRow = filterByRow;
		this.filterByRow.setWorkingFilterSet(FilterByRow.NOUPDATE_ROW_FILTER);
		this.rowFilter = new IncrementalUpdateRowFilter(populator);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.impl.FilterByRow#process(org.eclipse.birt
	 * .data.engine.odi.IResultObject, int)
	 */
	public boolean process(IResultObject row, int rowIndex) throws DataException {
		boolean accepted = filterByRow.process(row, rowIndex);

		if (updateGroupInfo) {
			if (accepted)
				rowFilter.onGroup(rowIndex);
			else
				rowFilter.notOnGroup(rowIndex);
		}

		return accepted;
	}

	public void setUpdateGroupInfo(boolean updateGroup) {
		this.updateGroupInfo = updateGroup;
	}

	public void close() throws DataException {
		filterByRow.setWorkingFilterSet(FilterByRow.NO_FILTER);
		filterByRow.close();
		filterByRow = null;
		rowFilter.close();
		rowFilter = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.impl.IFilterByRow#getFilterList()
	 */
	public List<IFilterDefinition> getFilterList() throws DataException {
		return filterByRow.getFilterList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.impl.IFilterByRow#setWorkingFilterSet(int)
	 */
	public void setWorkingFilterSet(int filterSetType) throws DataException {
		filterByRow.setWorkingFilterSet(filterSetType);
	}
}

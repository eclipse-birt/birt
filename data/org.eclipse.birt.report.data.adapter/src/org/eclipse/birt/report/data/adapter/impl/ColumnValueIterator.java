
/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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
package org.eclipse.birt.report.data.adapter.impl;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.report.data.adapter.api.IColumnValueIterator;
import org.eclipse.birt.report.data.adapter.api.IRequestInfo;

/**
 *
 */

public class ColumnValueIterator implements IColumnValueIterator {
	private IResultIterator resultIterator;
	private String boundColumnName;
	private IQueryResults queryResults;
	private Object value;
	private Set visitedValues;
	private int startRow;
	private int maxRows = 10000;

	/**
	 * @throws BirtException
	 */
	ColumnValueIterator(IQueryResults queryResults, String boundColumnName, IRequestInfo requestInfo)
			throws BirtException {
		this.queryResults = queryResults;
		this.boundColumnName = boundColumnName;
		this.visitedValues = new HashSet();
		if (requestInfo != null) {
			this.startRow = requestInfo.getStartRow();
			this.maxRows = requestInfo.getMaxRow();
		}

		this.moveTo(this.startRow);
	}

	/**
	 *
	 * @return
	 * @throws BirtException
	 */
	@Override
	public boolean next() throws BirtException {
		if (this.visitedValues.size() > this.maxRows) {
			return false;
		}
		if (resultIterator == null) {
			if (queryResults == null) {
				return false;
			}
			resultIterator = queryResults.getResultIterator();
		}
		if (resultIterator == null || !resultIterator.next()) {
			return false;
		}
		value = resultIterator.getValue(boundColumnName);
		while (this.visitedValues.contains(value)) {
			boolean hasNext = resultIterator.next();
			if (hasNext) {
				value = resultIterator.getValue(boundColumnName);
			} else {
				return false;
			}
		}
		this.visitedValues.add(value);
		return true;
	}

	/**
	 *
	 * @return
	 * @throws BirtException
	 */
	@Override
	public Object getValue() throws BirtException {
		return value;
	}

	/**
	 *
	 * @throws BirtException
	 */
	@Override
	public void close() throws BirtException {
		if (resultIterator != null) {
			resultIterator.close();
		}
		if (queryResults != null) {
			queryResults.close();
		}
	}

	/**
	 *
	 */
	private void moveTo(int rowIndex) throws BirtException {
		if (resultIterator == null) {
			if (queryResults == null) {
				return;
			}
			resultIterator = queryResults.getResultIterator();
		}
		if (resultIterator == null || resultIterator.isEmpty()) {
			return;
		}
		resultIterator.moveTo(rowIndex);
		value = resultIterator.getValue(boundColumnName);
		this.visitedValues.add(value);
	}
}

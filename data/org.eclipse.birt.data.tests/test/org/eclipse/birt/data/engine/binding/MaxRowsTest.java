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

package org.eclipse.birt.data.engine.binding;

import static org.junit.Assert.assertTrue;

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.querydefn.FilterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.junit.Test;

import testutil.ConfigText;

/**
 * Test whehter maxRows of IBaseQueryDefiniton property has effect in doing
 * query.
 */

public class MaxRowsTest extends APITestCase {

	/*
	 * @see org.eclipse.birt.data.engine.api.APITestCase#getDataSourceInfo()
	 */
	@Override
	protected DataSourceInfo getDataSourceInfo() {
		return new DataSourceInfo(ConfigText.getString("Api.TestData1.TableName"),
				ConfigText.getString("Api.TestData1.TableSQL"), ConfigText.getString("Api.TestData1.TestDataFileName"));
	}

	// Test case
	@Test
	public void test() throws Exception {
		// get the number of rows
		QueryDefinition queryDefn = newReportQuery();
		IResultIterator resultIt = executeQuery(queryDefn);

		int realRows = 0;
		// the rows value is supposed to be 81
		while (resultIt.next()) {
			realRows++;
		}

		String bindingNameFilter = "FILTER_COL0";
		IBaseExpression bindingExprFilter = new ScriptExpression("dataSetRow.COL0");
		FilterDefinition filterDefn = new FilterDefinition(new ScriptExpression("row.FILTER_COL0 > 0"));

		// test 1
		int MAX_ROW = realRows / 2;
		queryDefn = newReportQuery();
		this.addFilterDefinition(bindingNameFilter, bindingExprFilter, filterDefn, queryDefn);
		queryDefn.setMaxRows(MAX_ROW);

		// Exeute query to get result iterator
		resultIt = executeQuery(queryDefn);
		int count = 0;
		while (resultIt.next()) {
			count++;
		}
		assertTrue(count == MAX_ROW);

		// test 2
		MAX_ROW = realRows / 3;
		queryDefn = newReportQuery();
		this.addFilterDefinition(bindingNameFilter, bindingExprFilter, filterDefn, queryDefn);
		queryDefn.setMaxRows(MAX_ROW);

		// Execute query to get result iterator
		resultIt = executeQuery(queryDefn);
		count = 0;
		while (resultIt.next()) {
			count++;
		}
		assertTrue(count == MAX_ROW);

		// after doing filter, the rows value is supposed to be 54
		bindingNameFilter = "FILTER_COL0";
		bindingExprFilter = new ScriptExpression("dataSetRow.COL0");
		filterDefn = new FilterDefinition(new ScriptExpression("row.FILTER_COL0 > 3"));

		// test 3
		MAX_ROW = realRows / 3;
		queryDefn = newReportQuery();
		this.addFilterDefinition(bindingNameFilter, bindingExprFilter, filterDefn, queryDefn);
		queryDefn.addFilter(filterDefn);
		queryDefn.setMaxRows(MAX_ROW);

		// Exeute query to get result iterator
		resultIt = executeQuery(queryDefn);
		count = 0;
		while (resultIt.next()) {
			count++;
		}
		assertTrue(count < MAX_ROW);
	}

	private void addFilterDefinition(String bindingNameFilter, IBaseExpression bindingExprFilter,
			FilterDefinition filterDefn, QueryDefinition queryDefn) {
		if (filterDefn != null) {
			if (bindingNameFilter != null) {
				queryDefn.addResultSetExpression(bindingNameFilter, bindingExprFilter);
			}
			queryDefn.addFilter(filterDefn);
		}
	}
}

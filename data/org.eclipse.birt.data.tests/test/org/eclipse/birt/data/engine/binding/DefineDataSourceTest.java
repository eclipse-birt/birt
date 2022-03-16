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

import org.eclipse.birt.data.engine.api.APITestCase;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.impl.DataEngineImpl;
import org.junit.Test;

import testutil.ConfigText;

/**
 *
 */

public class DefineDataSourceTest extends APITestCase {

	/*
	 * @see org.eclipse.birt.data.engine.api.APITestCase#getDataSourceInfo()
	 */
	@Override
	protected DataSourceInfo getDataSourceInfo() {
		return new DataSourceInfo(ConfigText.getString("Binding.TestData.TableName"),
				ConfigText.getString("Binding.TestData.TableSQL"),
				ConfigText.getString("Binding.TestData.TestDataFileName"));
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testDefineDataSource() throws Exception {
		DataEngine testEngine = new DataEngineImpl(
				DataEngineContext.newInstance(DataEngineContext.DIRECT_PRESENTATION, null, null, null));
		testEngine.defineDataSource(this.dataSource);
		testEngine.defineDataSet(this.dataSet);

		// column mapping
		IPreparedQuery preparedQuery = testEngine.prepare(newQueryDefn());
		IQueryResults queryResults = preparedQuery.execute(null);
		IResultMetaData meta = queryResults.getResultMetaData();
		assertTrue(meta != null);

		testEngine.defineDataSource(this.dataSource);
		testEngine.defineDataSet(this.dataSet);
		IResultIterator ri2 = testEngine.prepare(newQueryDefn()).execute(null).getResultIterator();
		while (ri2.next()) {
		}
		ri2.close();

		IResultIterator ri = queryResults.getResultIterator();
		ri.close();

		testEngine.shutdown();
	}

	/**
	 * @return
	 */
	private IQueryDefinition newQueryDefn() {
		QueryDefinition queryDefn = new QueryDefinition();
		queryDefn.setDataSetName(this.dataSet.getName());

		String[] name = { "testColumn1" };
		ScriptExpression[] se = new ScriptExpression[name.length];
		se[0] = new ScriptExpression("dataSetRow.COUNTRY");
		for (int i = 0; i < name.length; i++) {
			queryDefn.addResultSetExpression(name[i], se[i]);
		}

		return queryDefn;
	}

}

/*
 *************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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
package org.eclipse.birt.data.engine.api;

import static org.junit.Assert.assertTrue;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.junit.Test;

import testutil.ConfigText;

/**
 * Test cases for data engine scripting features
 */
public class ScriptTest extends APITestCase {

	private static String dumpDataSourceScript = ConfigText.getString("Script.DumpDataSource");

	/*
	 * @see org.eclipse.birt.data.engine.api.APITestCase#getDataSourceInfo()
	 */
	@Override
	protected DataSourceInfo getDataSourceInfo() {
		return new DataSourceInfo("ScriptTestTable",
				"CREATE TABLE ScriptTestTable (COUNTRY varchar(10), CITY varchar(10), SALE_DATE timestamp, AMOUNT int, ORDERED int, NULL_COLUMN varchar(10))",
				ConfigText.getString("Api.TestData.TestDataFileName"));
	}

	/** Test data source script object */
	@Test
	public void test1_ReadDataSource() throws Exception {
		// Before-Open: read data source properties
		this.dataSource.setBeforeOpenScript("testPrintln(\"DataSource.beforeOpen:\"); " + dumpDataSourceScript);

		// After-Open: read data source properties
		this.dataSource.setAfterOpenScript("testPrintln(\"DataSource.afterOpen:\"); " + dumpDataSourceScript);

		this.dataSource.setBeforeCloseScript("testPrintln(\"DataSource.beforeClose:\"); " + dumpDataSourceScript);

		this.dataSource.setAfterCloseScript("testPrintln(\"DataSource.afterClose:\"); " + dumpDataSourceScript);

		createAndRunQuery();
		// Force close data source to verify beforeClose and afterClose
		this.dataEngine.shutdown();

		checkOutputFile();
	}

	@Test
	public void test2_UpdateDataSource() throws Exception {
		// Before-open: change user name. This is expected to cause SELECT SQL to fail
		// since
		// we are running against a different name space (in Derby)
		// Also, update to extensionID should be ignored
		final String INVALID_USER = "fake_user";
		this.dataSource.setBeforeOpenScript(
				"extensionProperties.odaUser=\"" + INVALID_USER + "\"; this.extensionID=\"invalid_extension\"; "
						+ "testPrintln(\"DataSource.beforeOpen:\"); " + dumpDataSourceScript);

		try {
			createAndRunQuery();
			assertTrue(false);
		} catch (DataException e) {
		}
		checkOutputFile();
	}

	static private final String dumpDataSetScript = ConfigText.getString("Script.DumpDataSet");

	@Test
	public void test3_ReadDataSet() throws Exception {
		// Before-Open: read data source properties
		this.dataSet.setBeforeOpenScript("testPrintln(\"DataSet.beforeOpen:\"); " + dumpDataSetScript);
		this.dataSet.setAfterOpenScript("testPrintln(\"DataSet.AfterOpen:\"); " + dumpDataSetScript);
		this.dataSet.setBeforeCloseScript("testPrintln(\"DataSet.beforeClose:\"); " + dumpDataSetScript);
		this.dataSet.setAfterCloseScript("testPrintln(\"DataSet.afterClose:\"); " + dumpDataSetScript);
		this.dataSet.setOnFetchScript(ConfigText.getString("Script.OnFetch"));
		createAndRunQuery();
		this.dataEngine.shutdown();
		checkOutputFile();
	}

	@Test
	public void test4_UpdateQueryText() throws Exception {
		// Before-Open:
		// (1)update queryText to include a WHERE clause
		// (2) Attempts to update dataSet.extensionID, which should be ignored
		this.dataSet.setBeforeOpenScript("queryText = queryText + \" WHERE COUNTRY = 'US' \"; "
				+ "extensionID = \"bad_id\"; " + "testPrintln(\"DataSet.beforeOpen:\"); " + dumpDataSetScript);
		createAndRunQuery();
		checkOutputFile();
	}

	@Test
	public void test5_UpdateProps() throws Exception {
		// Before-Open:
		// Add a data set property fake_property, which should result in a driver error
		String badPropertyName = "fake_property";
		String badPropertyValue = "fake_prop_value";
		this.dataSet.setBeforeOpenScript("extensionProperties[\"" + badPropertyName + "\"] = \"" + badPropertyValue
				+ "\";" + "testPrintln(\"DataSet.beforeOpen:\"); " + dumpDataSetScript);
		try {
			createAndRunQuery();
		} catch (DataException e) {
			// Exception mesasge should contain bad property name
			assertTrue(e.getMessage().indexOf(badPropertyName) >= 0);
		}
		checkOutputFile();
	}

	private void createAndRunQuery() throws Exception {
		// define a query design
		QueryDefinition queryDefn = newReportQuery();

		String names[] = { "_COUNTRY" };
		ScriptExpression expression = new ScriptExpression("dataSetRow.COUNTRY", DataType.STRING_TYPE);
		Binding binding = new Binding(names[0]);
		binding.setExpression(expression);
		queryDefn.addBinding(binding);

		IPreparedQuery preparedQuery = dataEngine.prepare(queryDefn);
		IQueryResults queryResults = preparedQuery.execute(null);
		IResultIterator it = queryResults.getResultIterator();
		outputQueryResult(it, names);
		it.close();
		queryResults.close();
	}

}

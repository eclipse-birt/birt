
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.querydefn.ColumnDefinition;
import org.eclipse.birt.data.engine.api.querydefn.OdaDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.junit.Before;
import org.junit.Test;

import testutil.ConfigText;

/**
 * Test feature of ColumnHint
 */
public class ColumnHintTest extends APITestCase {
	private String tableCalls;
	private String tableCustomer;

	/*
	 * @see org.eclipse.birt.data.engine.api.APITestCase#setUp()
	 */
	@Before
	public void columnHintSetUp() throws Exception {

		prepareDataSet(new DataSourceInfo(ConfigText.getString("Api.TestDataCalls.TableName"),
				ConfigText.getString("Api.TestDataCalls.TableSQL"),
				ConfigText.getString("Api.TestDataCalls.TestDataFileName")));

		tableCalls = ConfigText.getString("Api.TestDataCalls.TableName");
		tableCustomer = ConfigText.getString("Api.TestDataCustomer.TableName");
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.APITestCase#getDataSourceInfo()
	 */
	@Override
	protected DataSourceInfo getDataSourceInfo() {
		return new DataSourceInfo(ConfigText.getString("Api.TestDataCustomer.TableName"),
				ConfigText.getString("Api.TestDataCustomer.TableSQL"),
				ConfigText.getString("Api.TestDataCustomer.TestDataFileName"));
	}

	/**
	 * Test feature of duplicate column name from different tables
	 */
	@Test
	public void testDuplicateColName() throws Exception {

		String testSQL = "select " + tableCalls + ".CustomerID, " + tableCustomer + ".CustomerID, Charge from "
				+ tableCalls + ", " + tableCustomer;

		((OdaDataSetDesign) this.dataSet).setQueryText(testSQL);

		ColumnDefinition colDef = new ColumnDefinition("CUSTOMERID_2");
		colDef.setAlias("a");
		colDef.setDataType(0);
		colDef.setExportHint(2);
		colDef.setColumnPosition(2);
		colDef.setSearchHint(1);

		this.dataSet.addResultSetHint(colDef);

		String[] bindingNameRow = new String[4];
		bindingNameRow[0] = "ROW_CUSTOMERID";
		bindingNameRow[1] = "ROW_CUSTOMERID_2";
		bindingNameRow[2] = "ROW_A";
		bindingNameRow[3] = "ROW_CHARGE";

		IBaseExpression[] bindingExprRow = { new ScriptExpression("dataSetRow.CUSTOMERID", 0),
				new ScriptExpression("dataSetRow.CUSTOMERID_2", 0), new ScriptExpression("dataSetRow.a", 0),
				new ScriptExpression("dataSetRow.CHARGE", 0) };

		QueryDefinition queryDefn = createQuery(null, null, null, null, null, null, null, null, null, bindingNameRow,
				bindingExprRow);

		outputQueryResult(executeQuery(queryDefn), bindingNameRow);
		checkOutputFile();
	}
}

/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.tests.data.engine.api;

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.OdaDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;

import testutil.APITestCase;
import testutil.ConfigText;

public class MultiPassTest extends APITestCase {

	protected DataSourceInfo getDataSourceInfo() {
		return new DataSourceInfo(ConfigText.getString("Api.TestData.TableName"),
				ConfigText.getString("Api.TestData.TableSQL"), ConfigText.getString("Api.TestData.TestDataFileName"));
	}

	/**
	 * Test feature of aggregation expression
	 */
	public void test_RunningAggregationExpression() throws Exception {
		// Test a SQL with duplicate column name (quite common with join data
		// sets)
		String testSQL = "select COUNTRY, AMOUNT from " + getTestTableName();
		((OdaDataSetDesign) this.dataSet).setQueryText(testSQL);

		IBaseExpression[] bindingExprGroup = new IBaseExpression[] { new ScriptExpression("dataSetRow.COUNTRY", 0),
				new ScriptExpression("Total.Sum( dataSetRow.AMOUNT,null,1 )", 2) };

		String names[] = { "group_COUNTRY", "group_AMOUNT" };

		GroupDefinition[] groupDefn = new GroupDefinition[] { new GroupDefinition("G1"), new GroupDefinition("G2") };
		groupDefn[0].setKeyExpression("row.group_COUNTRY");

		groupDefn[1].setKeyExpression("row.group_AMOUNT");

		String[] bindingNameRow = new String[3];
		bindingNameRow[0] = "country";
		bindingNameRow[1] = "amount";
		bindingNameRow[2] = "binding3";

		IBaseExpression[] bindingExprRow = new IBaseExpression[3];
		bindingExprRow[0] = new ScriptExpression("dataSetRow.COUNTRY");
		bindingExprRow[1] = new ScriptExpression("dataSetRow.AMOUNT");
		bindingExprRow[2] = new ScriptExpression("Total.sum( dataSetRow.AMOUNT,null,1)");

		createAndRunQuery(names, bindingExprGroup, groupDefn, null, null, null, null, null, null, bindingNameRow,
				bindingExprRow);

		checkOutputFile();
	}

}

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
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.FilterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.OdaDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;

import testutil.APITestCase;
import testutil.ConfigText;

public class MultiPass_NestedQueryTest extends APITestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
	protected DataSourceInfo getDataSourceInfo() {
		return new DataSourceInfo(ConfigText.getString("Api.TestData.TableName"),
				ConfigText.getString("Api.TestData.TableSQL"), ConfigText.getString("Api.TestData.TestDataFileName"));
	}

	/**
	 * filter on group with nested query expression
	 *
	 * @throws Exception
	 */
	public void test_NestedAggregation() throws Exception {
		String sqlStatement = "select COUNTRY,AMOUNT,SALE_DATE from " + getTestTableName();
		((OdaDataSetDesign) this.dataSet).setQueryText(sqlStatement);

		IBaseExpression[] expressions = { new ScriptExpression("dataSetRow.COUNTRY", 0),
				new ScriptExpression("dataSetRow.AMOUNT", 0), new ScriptExpression("dataSetRow.SALE_DATE", 0) };

		String names[] = { "COL_COUNTRY", "COL_AMOUNT", "COL_SALE_DATE" };

		FilterDefinition filterDefn = new FilterDefinition(
				new ConditionalExpression("Total.percentSum( dataSetRow.AMOUNT )", IConditionalExpression.OP_GT, "0"));

		// FilterDefinition filterDefn = new FilterDefinition (
		// new
		// ConditionalExpression("row.AMOUNT",IConditionalExpression.OP_TOP_N,"4")
		// );

		GroupDefinition groupDefn = new GroupDefinition();
		groupDefn.setKeyExpression("dataSetRow.COUNTRY");
		groupDefn.addFilter(filterDefn);

		QueryDefinition queryDefn = new QueryDefinition();
		queryDefn.setDataSetName(this.dataSet.getName());

		for (int i = 0; i < expressions.length; i++) {
			queryDefn.addResultSetExpression(names[i], expressions[i]);
		}

		queryDefn.addFilter(filterDefn);

		IPreparedQuery preparedQuery = dataEngine.prepare(queryDefn);
		IQueryResults queryResult = preparedQuery.execute(null);
		IResultIterator resultIt = queryResult.getResultIterator();

		outputQueryResult(resultIt, names);
		checkOutputFile();

	}

}

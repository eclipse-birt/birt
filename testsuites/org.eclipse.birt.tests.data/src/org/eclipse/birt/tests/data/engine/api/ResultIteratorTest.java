
package org.eclipse.birt.tests.data.engine.api;

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.querydefn.OdaDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;

import testutil.APITestCase;
import testutil.ConfigText;

public class ResultIteratorTest extends APITestCase {

	@Override
	protected DataSourceInfo getDataSourceInfo() {
		return new DataSourceInfo(ConfigText.getString("Api.TestData.TableName"),
				ConfigText.getString("Api.TestData.TableSQL"), ConfigText.getString("Api.TestData.TestDataFileName"));
	}

	public void test_isFirstAndBeforeFirst() throws Exception {
		// Test a SQL with duplicate column name (quite common with join data
		// sets)
		String testSQL = "select COUNTRY, COUNTRY, CITY from " + getTestTableName();
		((OdaDataSetDesign) this.dataSet).setQueryText(testSQL);

		String[] bindingNameRow = new String[] { "Row_COUNTRY", "Row_CITY" };

		IBaseExpression[] bindingExprRow = new IBaseExpression[] { new ScriptExpression("dataSetRow.COUNTRY"),
				new ScriptExpression("dataSetRow.CITY"), };

		QueryDefinition queryDefn = this.createQuery(null, null, null, null, null, null, null, null, null,
				bindingNameRow, bindingExprRow);

		IPreparedQuery preparedQuery = dataEngine.prepare(queryDefn);
		IQueryResults queryResults = preparedQuery.execute(null);
		IResultIterator resultIt = queryResults.getResultIterator();

		assertTrue(resultIt.isBeforeFirst());
		while (resultIt.next()) {

			if (resultIt.isFirst()) {
				assertEquals(resultIt.getValue("Row_COUNTRY"), "CHINA");
				assertFalse(resultIt.isBeforeFirst());
			}

			assertFalse(resultIt.isBeforeFirst());
		}

	}

	public void test_isBeforeFirstAgainstEmptyResult() throws Exception {
		// Test a SQL with duplicate column name (quite common with join data
		// sets)
		String testSQL = "select COUNTRY, COUNTRY, CITY from " + getTestTableName() + " where COUNTRY = 'JAPAN'";
		((OdaDataSetDesign) this.dataSet).setQueryText(testSQL);

		String[] bindingNameRow = null;

		IBaseExpression[] bindingExprRow = null;
		QueryDefinition queryDefn = this.createQuery(null, null, null, null, null, null, null, null, null,
				bindingNameRow, bindingExprRow);

		IPreparedQuery preparedQuery = dataEngine.prepare(queryDefn);
		IQueryResults queryResults = preparedQuery.execute(null);
		IResultIterator resultIt = queryResults.getResultIterator();
		assertTrue(resultIt.isEmpty());
		assertFalse(resultIt.isBeforeFirst());

	}
}

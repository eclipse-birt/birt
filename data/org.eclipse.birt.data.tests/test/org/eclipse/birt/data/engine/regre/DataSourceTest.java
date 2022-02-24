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

package org.eclipse.birt.data.engine.regre;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.APITestCase;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.querydefn.ColumnDefinition;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.ScriptDataSourceDesign;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;

import testutil.ConfigText;

import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * 78069: Error message pops up when click on preview result in data set editor
 */
public class DataSourceTest extends APITestCase {
	/** this class will not use parent DataEngine */
	private DataEngine myDataEngine;

	public DataSourceTest() throws BirtException {
		DataEngineContext context = DataEngineContext.newInstance(DataEngineContext.DIRECT_PRESENTATION, null, null,
				null);
		context.setTmpdir(this.getTempDir());
		myDataEngine = DataEngine.newDataEngine(context);
	}

	@After
	public void tearDown() throws Exception {
		myDataEngine.shutdown();
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.APITestCase#getDataSourceInfo()
	 */
	protected DataSourceInfo getDataSourceInfo() {
		return new DataSourceInfo(ConfigText.getString("Api.TestDataCalls.TableName"),
				ConfigText.getString("Api.TestDataCalls.TableSQL"),
				ConfigText.getString("Api.TestDataCalls.TestDataFileName"));
	}

	/**
	 * @throws BirtException
	 */
	@Test
	public void testDefineDataSource() throws BirtException {
		ScriptExpression expr;
		IQueryDefinition queryDefn;

		// do script query
		expr = new ScriptExpression("dataSetRow.NUM");
		queryDefn = getScriptQueryDefn(expr);
		try {
			IPreparedQuery preparedQuery = myDataEngine.prepare(queryDefn);
			IQueryResults queryResults = preparedQuery.execute(null);
			IResultIterator it = queryResults.getResultIterator();
			it.close();
			queryResults.close();

		} catch (Exception e) {
			fail(e.getMessage());
		}

		// do jdbc query
		expr = new ScriptExpression("dataSetRow.CUSTOMERID");
		queryDefn = getJDBCQueryDefn(expr);
		try {
			IPreparedQuery preparedQuery = myDataEngine.prepare(queryDefn);
			IQueryResults queryResults = preparedQuery.execute(null);
			IResultIterator it = queryResults.getResultIterator();
			it.close();
			queryResults.close();
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	/**
	 * return JDBC query defintion
	 * 
	 * @param expr
	 * @return IQueryDefinition
	 * @throws BirtException
	 */
	private IQueryDefinition getJDBCQueryDefn(ScriptExpression expr) throws BirtException {
		QueryDefinition queryDefn = new QueryDefinition();
		queryDefn.setDataSetName(this.dataSet.getName());

		queryDefn.addResultSetExpression("NAME_1", expr);

		myDataEngine.defineDataSource(dataSourceInstance.getOdaDataSourceDesign());
		myDataEngine.defineDataSet(dataSourceInstance.getOdaDataSetDesign());

		return queryDefn;
	}

	/**
	 * return script query defintion
	 * 
	 * @param expr
	 * @return IQueryDefinition
	 * @throws BirtException
	 */
	private IQueryDefinition getScriptQueryDefn(ScriptExpression expr) throws BirtException {
		// data source
		ScriptDataSourceDesign scriptDatasource = new ScriptDataSourceDesign(
				dataSourceInstance.getOdaDataSourceDesign().getName());

		// data set
		ScriptDataSetDesign scriptDataSet = new ScriptDataSetDesign(dataSourceInstance.getOdaDataSetDesign().getName());
		scriptDataSet.setDataSource(scriptDatasource.getName());
		scriptDataSet.setOpenScript("count=11; dset_open=true; count--;");
		scriptDataSet.setFetchScript(
				"if (count==0) {return false; } else " + "{ dataSetRow.NUM=count; --count; return true; }");

		ColumnDefinition col = new ColumnDefinition("NUM");
		col.setDataType(DataType.INTEGER_TYPE);
		scriptDataSet.getResultSetHints().add(col);

		// query
		QueryDefinition queryDefn = new QueryDefinition();
		queryDefn.setDataSetName(scriptDataSet.getName());
		queryDefn.addResultSetExpression("NAME_2", expr);

		myDataEngine.defineDataSource(scriptDatasource);
		myDataEngine.defineDataSet(scriptDataSet);

		return queryDefn;
	}

}

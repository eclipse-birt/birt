/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.api;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.PlatformConfig;
import org.eclipse.birt.data.engine.api.querydefn.ColumnDefinition;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.ScriptDataSourceDesign;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;

import testutil.BaseTestCase;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * 
 * Test results of group level are: 1: when there is at least one row, 1: the
 * starting group level is 0 if and only if it is the first row 2: the ending
 * group level is 0 if and only it is the last row 3: before first row has the
 * same result as the first row 4: the retrievation of the after last row is not
 * available 2: when there is no row 1: the retrievation of group level function
 * is not available 2: the retrievation of data is still available
 * 
 */
public class GroupLevelTest extends BaseTestCase {
	private IQueryResults qr;
	private DataEngine dataEngine;

	private String rowName;
	private ScriptExpression rowExpr;
	private String strName;
	private ScriptExpression strExpr;
	private String aggrName;
	private ScriptExpression aggrExpr;

	/*
	 * @see junit.framework.TestCase#setUp()
	 */
	/**
	 * The row count is 10
	 * 
	 * @throws BirtException
	 */
	@Test
	public void testGroupLevelValue() throws BirtException {
		int rowCount = 10;
		this.prepare(rowCount);

		IResultIterator ri = qr.getResultIterator();

		// before the first row
		assertTrue(ri.getStartingGroupLevel() == 0);
		assertTrue(ri.getEndingGroupLevel() != 0);

		ri.next();

		// in the first row
		assertTrue(ri.getStartingGroupLevel() == 0);
		assertTrue(ri.getEndingGroupLevel() != 0);

		assertTrue(ri.getValue(rowName).equals(new Integer(10)));
		assertTrue(ri.getValue(aggrName).equals(new Double(55)));

		for (int i = 0; i < rowCount - 1; i++) {
			ri.next();

			assertTrue(ri.getStartingGroupLevel() > 0);
			if (i < rowCount - 2)
				assertTrue(ri.getEndingGroupLevel() > 0);
		}

		// in the last row
		assertTrue(ri.getStartingGroupLevel() != 0);
		assertTrue(ri.getEndingGroupLevel() == 0);

		ri.next();

		// after the last row
		try {
			ri.getStartingGroupLevel();
		} catch (BirtException e) {
			assertTrue(ResourceConstants.NO_CURRENT_ROW == e.getErrorCode());
		}

		qr.close();
		dataEngine.shutdown();
	}

	/**
	 * The row count is 1
	 * 
	 * @throws BirtException
	 */
	@Test
	public void testGroupLevelValue2() throws BirtException {
		int rowCount = 1;
		this.prepare(rowCount);

		IResultIterator ri = qr.getResultIterator();

		// before the first row
		assertTrue(ri.getStartingGroupLevel() == 0);
		assertTrue(ri.getEndingGroupLevel() == 0);

		ri.next();

		// in the first and the last row
		assertTrue(ri.getStartingGroupLevel() == 0);
		assertTrue(ri.getEndingGroupLevel() == 0);

		assertTrue(ri.getValue(rowName).equals(new Integer(1)));
		assertTrue(ri.getValue(aggrName).equals(new Double(1)));

		ri.next();

		// after the last row
		try {
			ri.getStartingGroupLevel();
		} catch (BirtException e) {
			assertTrue(ResourceConstants.NO_CURRENT_ROW == e.getErrorCode());
		}

		qr.close();
		dataEngine.shutdown();
	}

	/**
	 * The row count is 0
	 * 
	 * @throws BirtException
	 */
	@Test
	public void testGroupLevelValue3() throws BirtException {
		int rowCount = 0;
		this.prepare(rowCount);

		IResultIterator ri = qr.getResultIterator();

		ri.next();

		// after the last row
		try {
			ri.getStartingGroupLevel();
		} catch (BirtException e) {
			assertTrue(ResourceConstants.NO_CURRENT_ROW == e.getErrorCode());
		}

		qr.close();
		dataEngine.shutdown();
	}

	/**
	 * The row count is 0
	 * 
	 * @throws BirtException
	 * 
	 * @throws BirtException
	 */
	@Test
	public void testValueOnNullResult() throws BirtException {
		int rowCount = 0;
		this.prepare(rowCount);

		IResultIterator ri = qr.getResultIterator();

		ri.next();
		assertTrue(ri.getValue(this.strName).toString().endsWith("abc"));

		qr.close();
		dataEngine.shutdown();
	}

	/**
	 * @param rowCount
	 * @throws BirtException
	 */
	private void prepare(int rowCount) throws BirtException {
		ScriptDataSourceDesign dataSource = new ScriptDataSourceDesign("JUST as place folder");

		ScriptDataSetDesign dataSet = new ScriptDataSetDesign("data set");
		dataSet.setDataSource(dataSource.getName());
		dataSet.setOpenScript("count=" + rowCount + ";");
		dataSet.setFetchScript("if (count==0) " + "{" + "return false; " + "} " + "else " + "{ " + "row.NUM=count; "
				+ "row.SQUARE=count*count; " + "row.STR=\"row#\" + count; " + "--count; " + "return true; " + "}");

		// set column defintion for data set
		String[] scriptColumnNames = new String[] { "NUM", "SQUARE", "STR" };
		int[] scriptColumnTypes = new int[] { DataType.INTEGER_TYPE, DataType.DOUBLE_TYPE, DataType.STRING_TYPE };
		for (int i = 0; i < scriptColumnNames.length; i++) {
			ColumnDefinition colInfo = new ColumnDefinition(scriptColumnNames[i]);
			colInfo.setDataType(scriptColumnTypes[i]);
			dataSet.getResultSetHints().add(colInfo);
		}

		DataEngineContext context = DataEngineContext.newInstance(DataEngineContext.DIRECT_PRESENTATION,
				this.scriptContext, null, null, null);
		context.setTmpdir(this.getTempDir());
		PlatformConfig platformConfig = new PlatformConfig();
		platformConfig.setTempDir(this.getTempDir());
		dataEngine = DataEngine.newDataEngine(platformConfig, context);
		dataEngine.defineDataSource(dataSource);
		dataEngine.defineDataSet(dataSet);

		QueryDefinition qd = new QueryDefinition();
		qd.setDataSetName(dataSet.getName());

		rowName = "_NUM";
		rowExpr = new ScriptExpression("dataSetRow.NUM");
		qd.addResultSetExpression(rowName, rowExpr);
		// qd.addExpression( rowExpr, BaseTransform.ON_EACH_ROW );

		strName = "_abc";
		strExpr = new ScriptExpression("\"abc\"");
		qd.addResultSetExpression(strName, strExpr);
		// qd.addExpression( strExpr, BaseTransform.ON_EACH_ROW );

		aggrName = "_SUM_NUM";
		aggrExpr = new ScriptExpression("Total.sum(dataSetRow.NUM)");
		qd.addResultSetExpression(aggrName, aggrExpr);
		// qd.addExpression( aggrExpr, BaseTransform.AFTER_LAST_ROW );

		qr = dataEngine.prepare(qd).execute(null);
	}

}

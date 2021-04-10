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
package org.eclipse.birt.data.engine.binding;

import java.io.IOException;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IGroupDefinition;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.api.querydefn.BaseDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.ComputedColumn;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.SortDefinition;

import testutil.ConfigText;

import com.ibm.icu.util.Calendar;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *  
 */
public class GroupOnRowTest extends APITestCase {
	private Calendar calendar = Calendar.getInstance();

	@Before
	public void groupOnRowSetUp() throws Exception {

		calendar = Calendar.getInstance();
		calendar.clear();
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.APITestCase#getDataSourceInfo()
	 */
	protected DataSourceInfo getDataSourceInfo() {
		return new DataSourceInfo(ConfigText.getString("Api.TestData2.TableName"),
				ConfigText.getString("Api.TestData2.TableSQL"), ConfigText.getString("Api.TestData2.TestDataFileName"));
	}

	@Test
	public void testValidateInterval() {
		QueryDefinition qd = populateNumericGroupQueryDefinition(-1, 0);
		try {
			this.executeQuery(qd);
			fail("should catch invalid interval type exception");
		} catch (Exception e1) {
		}

		qd = populateNumericGroupQueryDefinition(IGroupDefinition.STRING_PREFIX_INTERVAL, 0);
		try {
			this.executeQuery(qd);
			fail("should catch (interval type)/(data type) mismatch exception");
		} catch (Exception e1) {
		}

		qd = populateNumericGroupQueryDefinition(IGroupDefinition.DAY_INTERVAL, 0);
		try {
			this.executeQuery(qd);
			fail("should catch (interval type)/(data type) mismatch exception");
		} catch (Exception e1) {
		}
	}

	@Test
	public void testValidateIntervalRange() {
		QueryDefinition qd = populateNumericGroupQueryDefinition(IGroupDefinition.NUMERIC_INTERVAL, -1);
		try {
			this.executeQuery(qd);
			fail("should catch invalid interval range exception");
		} catch (Exception e1) {
		}
	}

	@Test
	public void testGroupOnRowKeyCount0() throws Exception {
		groupOnRowKeyCount(0);
	}

	@Test
	public void testGroupOnRowKeyCount1() throws Exception {
		groupOnRowKeyCount(1);
	}

	@Test
	public void testGroupOnRowKeyCount3() throws Exception {
		groupOnRowKeyCount(3);
	}

	/**
	 * Currently, don't support any other group within a rowKeyCountGroup. It's
	 * because data rows in a rowKeyCountGroup may be not sorted according to the
	 * current group model
	 * 
	 * @throws Exception
	 */
	/*
	 * public void testMultiGroupOnRowKeyCount() throws Exception { String[]
	 * bindingNameGroup = new String[3]; bindingNameGroup[0] = "GROUP_NUMBER";
	 * bindingNameGroup[1] = "GROUP_AMOUNT1"; bindingNameGroup[2] = "GROUP_AMOUNT2";
	 * IBaseExpression[] bindingExprGroup = new IBaseExpression[3];
	 * bindingExprGroup[0] = new ScriptExpression( "dataSetRow.ID" );
	 * bindingExprGroup[1] = new ScriptExpression( "dataSetRow.AMOUNT1" );
	 * bindingExprGroup[2] = new ScriptExpression( "dataSetRow.AMOUNT2" );
	 * GroupDefinition[] groupDefn = new GroupDefinition[]{ new GroupDefinition(
	 * "group1" ), new GroupDefinition( "group2" ), new GroupDefinition( "group3" ),
	 * }; groupDefn[0].setKeyExpression( "row.GROUP_NUMBER" );
	 * groupDefn[0].setInterval( IGroupDefinition.NO_INTERVAL );
	 * groupDefn[0].setIntervalRange(3);
	 * 
	 * groupDefn[1].setKeyExpression( "row.GROUP_AMOUNT1" );
	 * groupDefn[1].setInterval( IGroupDefinition.NO_INTERVAL );
	 * groupDefn[1].setIntervalRange(2);
	 * 
	 * groupDefn[2].setKeyExpression( "row.GROUP_AMOUNT2" );
	 * groupDefn[2].setInterval( IGroupDefinition.NO_INTERVAL );
	 * groupDefn[2].setIntervalRange(2);
	 * 
	 * String[] bindingNameRow = new String[3]; bindingNameRow[0] = "ROW_ID";
	 * bindingNameRow[1] = "ROW_AMOUT1"; bindingNameRow[2] = "ROW_AMOUT2";
	 * IBaseExpression[] bindingExprRow = new IBaseExpression[3]; bindingExprRow[0]
	 * = new ScriptExpression( "dataSetRow.ID" ); bindingExprRow[1] = new
	 * ScriptExpression( "dataSetRow.AMOUNT1" ); bindingExprRow[2] = new
	 * ScriptExpression( "dataSetRow.AMOUNT2" );
	 * 
	 * String[] columnStr = new String[]{ "id", "amount1", "amount2" };
	 * 
	 * QueryDefinition qd = this.createQuery( bindingNameGroup, bindingExprGroup,
	 * groupDefn, null, null, null, null, null, null, bindingNameRow, bindingExprRow
	 * );
	 * 
	 * String outputStr = getOutputStrForGroupTest( 30, qd, groupDefn.length,
	 * bindingNameRow, columnStr ); testPrint( outputStr ); this.checkOutputFile( );
	 * }
	 */

	private void groupOnRowKeyCount(double intervalRange) throws Exception, IOException {
		String[] bindingNameGroup = new String[1];
		bindingNameGroup[0] = "GROUP_NUMBER";
		IBaseExpression[] bindingExprGroup = new IBaseExpression[1];
		bindingExprGroup[0] = new ScriptExpression("dataSetRow.ID");
		GroupDefinition[] groupDefn = new GroupDefinition[] { new GroupDefinition("group1") };
		groupDefn[0].setKeyExpression("row.GROUP_NUMBER");
		groupDefn[0].setInterval(IGroupDefinition.NO_INTERVAL);
		groupDefn[0].setIntervalRange(intervalRange);

		String[] bindingNameRow = new String[3];
		bindingNameRow[0] = "ROW_ID";
		bindingNameRow[1] = "ROW_AMOUT1";
		bindingNameRow[2] = "ROW_AMOUT2";
		IBaseExpression[] bindingExprRow = new IBaseExpression[3];
		bindingExprRow[0] = new ScriptExpression("dataSetRow.ID");
		bindingExprRow[1] = new ScriptExpression("dataSetRow.AMOUNT1");
		bindingExprRow[2] = new ScriptExpression("dataSetRow.AMOUNT2");

		String[] columnStr = new String[] { "id", "amount1", "amount2" };

		QueryDefinition qd = this.createQuery(bindingNameGroup, bindingExprGroup, groupDefn, null, null, null, null,
				null, null, bindingNameRow, bindingExprRow);

		String outputStr = getOutputStrForGroupTest(30, qd, groupDefn.length, bindingNameRow, columnStr);
		testPrint(outputStr);

		this.checkOutputFile();
	}

	private QueryDefinition populateNumericGroupQueryDefinition(int interval, double intervalRange) {
		String[] bindingNameGroup = new String[1];
		bindingNameGroup[0] = "GROUP_NUMBER";
		IBaseExpression[] bindingExprGroup = new IBaseExpression[1];
		bindingExprGroup[0] = new ScriptExpression("dataSetRow.ID", DataType.INTEGER_TYPE);
		GroupDefinition[] groupDefn = new GroupDefinition[] { new GroupDefinition("group1") };
		groupDefn[0].setKeyExpression("row.GROUP_NUMBER");
		groupDefn[0].setInterval(interval);
		groupDefn[0].setIntervalRange(intervalRange);

		String[] bindingNameRow = new String[3];
		bindingNameRow[0] = "ROW_ID";
		bindingNameRow[1] = "ROW_AMOUT1";
		bindingNameRow[2] = "ROW_AMOUT2";
		IBaseExpression[] bindingExprRow = new IBaseExpression[3];
		bindingExprRow[0] = new ScriptExpression("dataSetRow.ID");
		bindingExprRow[1] = new ScriptExpression("dataSetRow.AMOUNT1");
		bindingExprRow[2] = new ScriptExpression("dataSetRow.AMOUNT2");

		try {
			return this.createQuery(bindingNameGroup, bindingExprGroup, groupDefn, null, null, null, null, null, null,
					bindingNameRow, bindingExprRow);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Test feature of group on hour
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGroupOnHour() throws Exception {
		calendar.set(2005, 0, 1);
		groupOnHour(calendar.getTime());
	}

	/**
	 * Test feature of group on hour
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGroupOnHour1() throws Exception {
		groupOnHour(null);
	}

	private void groupOnHour(Object startValue) throws Exception, IOException {
		String[] bindingNameGroup = new String[1];
		bindingNameGroup[0] = "GROUP_DATE";
		IBaseExpression[] bindingExprGroup = new IBaseExpression[1];
		bindingExprGroup[0] = new ScriptExpression("dataSetRow.DATE_FOR_GROUP");
		GroupDefinition[] groupDefn = new GroupDefinition[] { new GroupDefinition("group1") };
		groupDefn[0].setKeyExpression("row.GROUP_DATE");
		groupDefn[0].setInterval(IGroupDefinition.HOUR_INTERVAL);
		if (startValue != null) {
			groupDefn[0].setIntervalStart(startValue);
		}
		groupDefn[0].setIntervalRange(48);

		String[] bindingNameRow = new String[4];
		bindingNameRow[0] = "ROW_DATE_FOR_GROUP";
		bindingNameRow[1] = "ROW_ID";
		bindingNameRow[2] = "ROW_AMOUT1";
		bindingNameRow[3] = "ROW_AMOUT2";
		IBaseExpression[] bindingExprRow = new IBaseExpression[4];
		bindingExprRow[0] = new ScriptExpression("dataSetRow.DATE_FOR_GROUP");
		bindingExprRow[1] = new ScriptExpression("dataSetRow.ID");
		bindingExprRow[2] = new ScriptExpression("dataSetRow.AMOUNT1");
		bindingExprRow[3] = new ScriptExpression("dataSetRow.AMOUNT2");

		String[] columnStr = new String[] { "date_for_group", "id", "amount1", "amount2" };

		QueryDefinition qd = this.createQuery(bindingNameGroup, bindingExprGroup, groupDefn, null, null, null, null,
				null, null, bindingNameRow, bindingExprRow);

		String outputStr = getOutputStrForGroupTest(30, qd, groupDefn.length, bindingNameRow, columnStr);
		testPrint(outputStr);

		this.checkOutputFile();
	}

	/**
	 * Test feature of group on minute
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGroupOnMinute() throws Exception {
		groupOnMinute("2005-1-1");
	}

	/**
	 * Test feature of group on minute
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGroupOnMinute1() throws Exception {
		groupOnMinute(null);
	}

	private void groupOnMinute(Object startValue) throws Exception, IOException {
		String[] bindingNameGroup = new String[1];
		bindingNameGroup[0] = "GROUP_DATE";
		IBaseExpression[] bindingExprGroup = new IBaseExpression[1];
		bindingExprGroup[0] = new ScriptExpression("dataSetRow.DATE_FOR_GROUP");
		GroupDefinition[] groupDefn = new GroupDefinition[] { new GroupDefinition("group1") };
		groupDefn[0].setKeyExpression("row.GROUP_DATE");
		groupDefn[0].setInterval(IGroupDefinition.MINUTE_INTERVAL);
		if (startValue != null)
			groupDefn[0].setIntervalStart(startValue);
		groupDefn[0].setIntervalRange(60 * 24 * 3);

		String[] bindingNameRow = new String[4];
		bindingNameRow[0] = "ROW_DATE_FOR_GROUP";
		bindingNameRow[1] = "ROW_ID";
		bindingNameRow[2] = "ROW_AMOUT1";
		bindingNameRow[3] = "ROW_AMOUT2";
		IBaseExpression[] bindingExprRow = new IBaseExpression[4];
		bindingExprRow[0] = new ScriptExpression("dataSetRow.DATE_FOR_GROUP");
		bindingExprRow[1] = new ScriptExpression("dataSetRow.ID");
		bindingExprRow[2] = new ScriptExpression("dataSetRow.AMOUNT1");
		bindingExprRow[3] = new ScriptExpression("dataSetRow.AMOUNT2");

		String[] columnStr = new String[] { "date_for_group", "id", "amount1", "amount2" };

		QueryDefinition qd = this.createQuery(bindingNameGroup, bindingExprGroup, groupDefn, null, null, null, null,
				null, null, bindingNameRow, bindingExprRow);

		String outputStr = getOutputStrForGroupTest(30, qd, groupDefn.length, bindingNameRow, columnStr);
		testPrint(outputStr);

		this.checkOutputFile();
	}

	/**
	 * Test feature of group on second
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGroupOnSecond() throws Exception {
		groupOnSecond("2005-1-1");

	}

	/**
	 * Test feature of group on second
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGroupOnSecond1() throws Exception {
		groupOnSecond(null);
	}

	private void groupOnSecond(Object startValue) throws Exception, IOException {
		String[] bindingNameGroup = new String[1];
		bindingNameGroup[0] = "GROUP_DATE";
		IBaseExpression[] bindingExprGroup = new IBaseExpression[1];
		bindingExprGroup[0] = new ScriptExpression("dataSetRow.DATE_FOR_GROUP");
		GroupDefinition[] groupDefn = new GroupDefinition[] { new GroupDefinition("group1") };
		groupDefn[0].setKeyExpression("row.GROUP_DATE");
		groupDefn[0].setInterval(IGroupDefinition.SECOND_INTERVAL);
		if (startValue != null)
			groupDefn[0].setIntervalStart(startValue);
		groupDefn[0].setIntervalRange(60 * 60 * 24 * 3);

		String[] bindingNameRow = new String[4];
		bindingNameRow[0] = "ROW_DATE_FOR_GROUP";
		bindingNameRow[1] = "ROW_ID";
		bindingNameRow[2] = "ROW_AMOUT1";
		bindingNameRow[3] = "ROW_AMOUT2";
		IBaseExpression[] bindingExprRow = new IBaseExpression[4];
		bindingExprRow[0] = new ScriptExpression("dataSetRow.DATE_FOR_GROUP");
		bindingExprRow[1] = new ScriptExpression("dataSetRow.ID");
		bindingExprRow[2] = new ScriptExpression("dataSetRow.AMOUNT1");
		bindingExprRow[3] = new ScriptExpression("dataSetRow.AMOUNT2");

		String[] columnStr = new String[] { "date_for_group", "id", "amount1", "amount2" };

		QueryDefinition qd = this.createQuery(bindingNameGroup, bindingExprGroup, groupDefn, null, null, null, null,
				null, null, bindingNameRow, bindingExprRow);

		String outputStr = getOutputStrForGroupTest(30, qd, groupDefn.length, bindingNameRow, columnStr);
		testPrint(outputStr);

		this.checkOutputFile();
	}

	/**
	 * Test another function, that row[colIndex] JS expression should be supported
	 * defineed in sort.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSortOnKeyIndex() throws Exception {
		// add expression based on group defintion

		String[] bindingNameSort = new String[1];
		bindingNameSort[0] = "SORT_AMOUNT2";
		IBaseExpression[] bindingExprSort = new IBaseExpression[1];
		bindingExprSort[0] = new ScriptExpression("dataSetRow.AMOUNT2");
		SortDefinition[] sortDefn = new SortDefinition[] { new SortDefinition() };
		sortDefn[0].setColumn("SORT_AMOUNT2");
		sortDefn[0].setSortDirection(ISortDefinition.SORT_DESC);

		String[] bindingNameRow = new String[4];
		bindingNameRow[0] = "ROW_ID";
		bindingNameRow[1] = "ROW_rowPosition";
		bindingNameRow[2] = "ROW_AMOUT1";
		bindingNameRow[3] = "ROW_AMOUT2";
		IBaseExpression[] bindingExprRow = new IBaseExpression[4];
		bindingExprRow[0] = new ScriptExpression("dataSetRow.ID");
		bindingExprRow[1] = new ScriptExpression("dataSetRow._rowPosition");
		bindingExprRow[2] = new ScriptExpression("dataSetRow.AMOUNT1");
		bindingExprRow[3] = new ScriptExpression("dataSetRow.AMOUNT2");

		// execute query
		IResultIterator ri = this.executeQuery(createQuery(null, null, null, bindingNameSort, bindingExprSort, sortDefn,
				null, null, null, bindingNameRow, bindingExprRow));

		// output query result
		final int expectedLen = 15;

		String metaData = "";
		metaData += formatStr("ID", expectedLen);
		metaData += formatStr("_rowPosition", expectedLen);
		metaData += formatStr("AMOUNT1", expectedLen);
		metaData += formatStr("AMOUNT2", expectedLen);
		this.testPrintln(metaData);

		while (ri.next()) {
			String rowData = "";

			for (int j = 0; j < bindingNameRow.length; j++) {
				String value = ri.getValue(bindingNameRow[j]).toString();
				rowData += formatStr(value, expectedLen);
			}

			this.testPrintln(rowData);
		}

		this.checkOutputFile();
	}

	/**
	 * Test feature of group on row position
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGroupOnRowPosition() throws Exception {
		String[] bindingNameGroup = new String[3];
		bindingNameGroup[0] = "GROUP_ID";
		bindingNameGroup[1] = "GROUP_0";
		bindingNameGroup[2] = "GROUP_AMOUNT1";
		IBaseExpression[] bindingExprGroup = new IBaseExpression[3];
		bindingExprGroup[0] = new ScriptExpression("dataSetRow.ID");
		bindingExprGroup[1] = new ScriptExpression("dataSetRow._rowPosition");
		bindingExprGroup[2] = new ScriptExpression("dataSetRow.AMOUNT1");
		GroupDefinition[] groupDefn = new GroupDefinition[] { new GroupDefinition("group1"),
				new GroupDefinition("group2"), new GroupDefinition("group3") };
		groupDefn[0].setKeyExpression("row.GROUP_ID");
		groupDefn[0].setInterval(IGroupDefinition.NUMERIC_INTERVAL);
		groupDefn[0].setIntervalRange(5);
		groupDefn[0].setIntervalStart(new Integer(8));
		groupDefn[1].setKeyExpression("row.GROUP_0");
		groupDefn[1].setInterval(IGroupDefinition.NUMERIC_INTERVAL);
		groupDefn[1].setIntervalRange(3);
		groupDefn[1].setIntervalStart(new Integer(0));
		groupDefn[2].setKeyExpression("row.GROUP_AMOUNT1");
		groupDefn[2].setInterval(IGroupDefinition.NUMERIC_INTERVAL);
		groupDefn[2].setIntervalRange(11);
		groupDefn[2].setIntervalStart(new Integer(80));

		String[] bindingNameRow = new String[4];
		bindingNameRow[0] = "ROW_ID";
		bindingNameRow[1] = "ROW_rowPosition";
		bindingNameRow[2] = "ROW_AMOUT1";
		bindingNameRow[3] = "ROW_AMOUT2";
		IBaseExpression[] bindingExprRow = new IBaseExpression[4];
		bindingExprRow[0] = new ScriptExpression("dataSetRow.ID");
		bindingExprRow[1] = new ScriptExpression("dataSetRow._rowPosition");
		bindingExprRow[2] = new ScriptExpression("dataSetRow.AMOUNT1");
		bindingExprRow[3] = new ScriptExpression("dataSetRow.AMOUNT2");

		String[] columnStr = new String[] { "id", "_rowPosition", "amount1", "amount2" };

		QueryDefinition qd = this.createQuery(bindingNameGroup, bindingExprGroup, groupDefn, null, null, null, null,
				null, null, bindingNameRow, bindingExprRow);
		String outputStr = getOutputStrForGroupTest(15, qd, groupDefn.length, bindingNameRow, columnStr);
		testPrint(outputStr);

		this.checkOutputFile();
	}

	/**
	 * Test feature of group on row position
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGroupOnRowPosition2() throws Exception {
		String[] bindingNameGroup = new String[3];
		bindingNameGroup[0] = "GROUP_ID";
		bindingNameGroup[1] = "GROUP_0";
		bindingNameGroup[2] = "GROUP_AMOUNT1";
		IBaseExpression[] bindingExprGroup = new IBaseExpression[3];
		bindingExprGroup[0] = new ScriptExpression("dataSetRow.ID");
		bindingExprGroup[1] = new ScriptExpression("row[\"__rownum\"]");
		bindingExprGroup[2] = new ScriptExpression("dataSetRow.AMOUNT1");
		GroupDefinition[] groupDefn = new GroupDefinition[] { new GroupDefinition("group1"),
				new GroupDefinition("group2"), new GroupDefinition("group3") };
		groupDefn[0].setKeyExpression("row.GROUP_ID");
		groupDefn[0].setInterval(IGroupDefinition.NUMERIC_INTERVAL);
		groupDefn[0].setIntervalRange(5);
		groupDefn[0].setIntervalStart(new Integer(8));
		groupDefn[1].setKeyExpression("row.GROUP_0");
		groupDefn[1].setInterval(IGroupDefinition.NUMERIC_INTERVAL);
		groupDefn[1].setIntervalRange(3);
		groupDefn[1].setIntervalStart(new Integer(0));
		groupDefn[2].setKeyExpression("row.GROUP_AMOUNT1");
		groupDefn[2].setInterval(IGroupDefinition.NUMERIC_INTERVAL);
		groupDefn[2].setIntervalRange(11);
		groupDefn[2].setIntervalStart(new Integer(80));

		String[] bindingNameRow = new String[4];
		bindingNameRow[0] = "ROW_ID";
		bindingNameRow[1] = "ROW_rowPosition";
		bindingNameRow[2] = "ROW_AMOUT1";
		bindingNameRow[3] = "ROW_AMOUT2";
		IBaseExpression[] bindingExprRow = new IBaseExpression[4];
		bindingExprRow[0] = new ScriptExpression("dataSetRow.ID");
		bindingExprRow[1] = new ScriptExpression("dataSetRow._rowPosition");
		bindingExprRow[2] = new ScriptExpression("dataSetRow.AMOUNT1");
		bindingExprRow[3] = new ScriptExpression("dataSetRow.AMOUNT2");

		String[] columnStr = new String[] { "id", "_rowPosition", "amount1", "amount2" };

		QueryDefinition qd = this.createQuery(bindingNameGroup, bindingExprGroup, groupDefn, null, null, null, null,
				null, null, bindingNameRow, bindingExprRow);
		String outputStr = getOutputStrForGroupTest(15, qd, groupDefn.length, bindingNameRow, columnStr);
		testPrint(outputStr);

		this.checkOutputFile();
	}

	/**
	 * Test feature of group on row position
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGroupOnRowPosition3() throws Exception {
		String[] bindingNameGroup = new String[2];
		bindingNameGroup[0] = "GROUP_ID";
		bindingNameGroup[1] = "GROUP_AMOUNT1";
		IBaseExpression[] bindingExprGroup = new IBaseExpression[2];
		bindingExprGroup[0] = new ScriptExpression("dataSetRow.ID");
		bindingExprGroup[1] = new ScriptExpression("dataSetRow.AMOUNT1");
		GroupDefinition[] groupDefn = new GroupDefinition[] { new GroupDefinition("group1"),
				new GroupDefinition("group2"), new GroupDefinition("group3") };
		groupDefn[0].setKeyExpression("row.GROUP_ID");
		groupDefn[0].setInterval(IGroupDefinition.NUMERIC_INTERVAL);
		groupDefn[0].setIntervalRange(5);
		groupDefn[0].setIntervalStart(new Integer(8));
		groupDefn[1].setKeyExpression("row[\"__rownum\"]");
		groupDefn[1].setInterval(IGroupDefinition.NUMERIC_INTERVAL);
		groupDefn[1].setIntervalRange(3);
		groupDefn[1].setIntervalStart(new Integer(0));
		groupDefn[2].setKeyExpression("row.GROUP_AMOUNT1");
		groupDefn[2].setInterval(IGroupDefinition.NUMERIC_INTERVAL);
		groupDefn[2].setIntervalRange(11);
		groupDefn[2].setIntervalStart(new Integer(80));

		String[] bindingNameRow = new String[4];
		bindingNameRow[0] = "ROW_ID";
		bindingNameRow[1] = "ROW_rowPosition";
		bindingNameRow[2] = "ROW_AMOUT1";
		bindingNameRow[3] = "ROW_AMOUT2";
		IBaseExpression[] bindingExprRow = new IBaseExpression[4];
		bindingExprRow[0] = new ScriptExpression("dataSetRow.ID");
		bindingExprRow[1] = new ScriptExpression("dataSetRow._rowPosition");
		bindingExprRow[2] = new ScriptExpression("dataSetRow.AMOUNT1");
		bindingExprRow[3] = new ScriptExpression("dataSetRow.AMOUNT2");

		String[] columnStr = new String[] { "id", "_rowPosition", "amount1", "amount2" };

		QueryDefinition qd = this.createQuery(bindingNameGroup, bindingExprGroup, groupDefn, null, null, null, null,
				null, null, bindingNameRow, bindingExprRow);
		String outputStr = getOutputStrForGroupTest(15, qd, groupDefn.length, bindingNameRow, columnStr);
		testPrint(outputStr);

		this.checkOutputFile();
	}

	/**
	 * Test feature of group on row position
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGroupOnRowPosition4() throws Exception {
		String[] bindingNameGroup = new String[3];
		bindingNameGroup[0] = "GROUP_ID";
		bindingNameGroup[1] = "GROUP_0";
		bindingNameGroup[2] = "GROUP_AMOUNT1";
		IBaseExpression[] bindingExprGroup = new IBaseExpression[3];
		bindingExprGroup[0] = new ScriptExpression("dataSetRow.ID");
		bindingExprGroup[1] = new ScriptExpression("row[\"__rownum\"]");
		bindingExprGroup[2] = new ScriptExpression("dataSetRow.AMOUNT1");
		GroupDefinition[] groupDefn = new GroupDefinition[] { new GroupDefinition("group1"),
				new GroupDefinition("group2"), new GroupDefinition("group3") };
		groupDefn[0].setKeyExpression("row.GROUP_ID");
		groupDefn[0].setInterval(IGroupDefinition.NUMERIC_INTERVAL);
		groupDefn[0].setIntervalRange(5);
		groupDefn[0].setIntervalStart(new Integer(8));
		groupDefn[1].setKeyExpression("row[\"__rownum\"]");
		groupDefn[1].setInterval(IGroupDefinition.HOUR_INTERVAL);
		groupDefn[1].setIntervalRange(3);
		groupDefn[1].setIntervalStart(new Integer(0));
		groupDefn[2].setKeyExpression("row.GROUP_AMOUNT1");
		groupDefn[2].setInterval(IGroupDefinition.NUMERIC_INTERVAL);
		groupDefn[2].setIntervalRange(11);
		groupDefn[2].setIntervalStart(new Integer(80));

		String[] bindingNameRow = new String[4];
		bindingNameRow[0] = "ROW_ID";
		bindingNameRow[1] = "ROW_rowPosition";
		bindingNameRow[2] = "ROW_AMOUT1";
		bindingNameRow[3] = "ROW_AMOUT2";
		IBaseExpression[] bindingExprRow = new IBaseExpression[4];
		bindingExprRow[0] = new ScriptExpression("dataSetRow.ID");
		bindingExprRow[1] = new ScriptExpression("dataSetRow._rowPosition");
		bindingExprRow[2] = new ScriptExpression("dataSetRow.AMOUNT1");
		bindingExprRow[3] = new ScriptExpression("dataSetRow.AMOUNT2");

		String[] columnStr = new String[] { "id", "_rowPosition", "amount1", "amount2" };
		try {
			QueryDefinition qd = this.createQuery(bindingNameGroup, bindingExprGroup, groupDefn, null, null, null, null,
					null, null, bindingNameRow, bindingExprRow);
			String outputStr = getOutputStrForGroupTest(15, qd, groupDefn.length, bindingNameRow, columnStr);
			testPrint(outputStr);

			this.checkOutputFile();
			fail("exception expected!");
		} catch (BirtException be) {
			assertEquals(be.getErrorCode(), "data.engine.group.interval");
		}
	}

	/**
	 * Test feature of group on row position
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGroupOnRowPosition5() throws Exception {
		String[] bindingNameGroup = new String[1];
		bindingNameGroup[0] = "GROUP_ID";
		IBaseExpression[] bindingExprGroup = new IBaseExpression[1];
		bindingExprGroup[0] = new ScriptExpression("dataSetRow.ID");

		GroupDefinition[] groupDefn = new GroupDefinition[] { new GroupDefinition("group1"), };
		groupDefn[0].setKeyExpression("row.__rownum");
		groupDefn[0].setInterval(IGroupDefinition.NUMERIC_INTERVAL);
		groupDefn[0].setIntervalRange(5);

		String[] bindingNameRow = new String[4];
		bindingNameRow[0] = "ROW_ID";
		bindingNameRow[1] = "ROW_rowPosition";
		bindingNameRow[2] = "ROW_AMOUT1";
		bindingNameRow[3] = "ROW_AMOUT2";
		IBaseExpression[] bindingExprRow = new IBaseExpression[4];
		bindingExprRow[0] = new ScriptExpression("dataSetRow.ID");
		bindingExprRow[1] = new ScriptExpression("dataSetRow._rowPosition");
		bindingExprRow[2] = new ScriptExpression("dataSetRow.AMOUNT1");
		bindingExprRow[3] = new ScriptExpression("dataSetRow.AMOUNT2");

		String[] columnStr = new String[] { "id", "_rowPosition", "amount1", "amount2" };
		try {
			QueryDefinition qd = this.createQuery(bindingNameGroup, bindingExprGroup, groupDefn, null, null, null, null,
					null, null, bindingNameRow, bindingExprRow);
			String outputStr = getOutputStrForGroupTest(15, qd, groupDefn.length, bindingNameRow, columnStr);
			testPrint(outputStr);

			this.checkOutputFile();

		} catch (BirtException be) {
			assertEquals(be.getErrorCode(), "data.engine.group.interval");
		}
	}

	/**
	 * Test feature of group on week
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGroupOnWeek() throws Exception {
		/* Calendar calendar = Calendar.getInstance( ); */
		calendar.set(2005, 0, 1);
		groupOnWeek(calendar.getTime());
	}

	/**
	 * Test feature of group on week
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGroupOnWeek1() throws Exception {
		groupOnWeek(null);
	}

	@Test
	public void testGroupOnWeek2() throws Exception {
		groupOnWeek2(null);
	}

	private void groupOnWeek(Object startValue) throws Exception, IOException {
		String[] bindingNameGroup = new String[1];
		bindingNameGroup[0] = "GROUP_DATE";
		IBaseExpression[] bindingExprGroup = new IBaseExpression[1];
		bindingExprGroup[0] = new ScriptExpression("dataSetRow.DATE_FOR_GROUP");
		GroupDefinition[] groupDefn = new GroupDefinition[] { new GroupDefinition("group1") };
		groupDefn[0].setKeyExpression("row.GROUP_DATE");
		groupDefn[0].setInterval(IGroupDefinition.WEEK_INTERVAL);
		if (startValue != null)
			groupDefn[0].setIntervalStart(startValue);
		groupDefn[0].setIntervalRange(2);

		String[] bindingNameRow = new String[4];
		bindingNameRow[0] = "ROW_DATE_FOR_GROUP";
		bindingNameRow[1] = "ROW_ID";
		bindingNameRow[2] = "ROW_AMOUT1";
		bindingNameRow[3] = "ROW_AMOUT2";
		IBaseExpression[] bindingExprRow = new IBaseExpression[4];
		bindingExprRow[0] = new ScriptExpression("dataSetRow.DATE_FOR_GROUP");
		bindingExprRow[1] = new ScriptExpression("dataSetRow.ID");
		bindingExprRow[2] = new ScriptExpression("dataSetRow.AMOUNT1");
		bindingExprRow[3] = new ScriptExpression("dataSetRow.AMOUNT2");

		String[] columnStr = new String[] { "date_for_group", "id", "amount1", "amount2" };

		QueryDefinition qd = this.createQuery(bindingNameGroup, bindingExprGroup, groupDefn, null, null, null, null,
				null, null, bindingNameRow, bindingExprRow);

		String outputStr = getOutputStrForGroupTest(30, qd, groupDefn.length, bindingNameRow, columnStr);
		testPrint(outputStr);

		this.checkOutputFile();
	}

	private void groupOnWeek2(Object startValue) throws Exception, IOException {
		String[] bindingNameGroup = new String[1];
		bindingNameGroup[0] = "GROUP_DATE";
		IBaseExpression[] bindingExprGroup = new IBaseExpression[1];
		bindingExprGroup[0] = new ScriptExpression("dataSetRow.DATE_FOR_GROUP");
		GroupDefinition[] groupDefn = new GroupDefinition[] { new GroupDefinition("group1") };
		groupDefn[0].setKeyExpression("row.GROUP_DATE");
		groupDefn[0].setInterval(IGroupDefinition.WEEK_INTERVAL);
		if (startValue != null)
			groupDefn[0].setIntervalStart(startValue);
		groupDefn[0].setIntervalRange(2);
		SortDefinition sortDefn = new SortDefinition();
		sortDefn.setExpression("row.ROW_DATE_FOR_GROUP");
		sortDefn.setSortDirection(ISortDefinition.SORT_DESC);
		groupDefn[0].addSort(sortDefn);

		String[] bindingNameRow = new String[4];
		bindingNameRow[0] = "ROW_DATE_FOR_GROUP";
		bindingNameRow[1] = "ROW_ID";
		bindingNameRow[2] = "ROW_AMOUT1";
		bindingNameRow[3] = "ROW_AMOUT2";
		IBaseExpression[] bindingExprRow = new IBaseExpression[4];
		bindingExprRow[0] = new ScriptExpression("dataSetRow.DATE_FOR_GROUP");
		bindingExprRow[1] = new ScriptExpression("dataSetRow.ID");
		bindingExprRow[2] = new ScriptExpression("dataSetRow.AMOUNT1");
		bindingExprRow[3] = new ScriptExpression("dataSetRow.AMOUNT2");

		String[] columnStr = new String[] { "date_for_group", "id", "amount1", "amount2" };

		QueryDefinition qd = this.createQuery(bindingNameGroup, bindingExprGroup, groupDefn, null, null, null, null,
				null, null, bindingNameRow, bindingExprRow);

		String outputStr = getOutputStrForGroupTest(30, qd, groupDefn.length, bindingNameRow, columnStr);
		testPrint(outputStr);

		this.checkOutputFile();
	}

	/**
	 * Test feature of group on year
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGroupOnYear() throws Exception {
		calendar.set(2005, 0, 1);
		groupOnYear(calendar.getTime());
	}

	/**
	 * Test feature of group on year
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGroupOnYear1() throws Exception {
		groupOnYear(null);
	}

	private void groupOnYear(Object startValue) throws Exception, IOException {
		String[] bindingNameGroup = new String[1];
		bindingNameGroup[0] = "GROUP_DATE";
		IBaseExpression[] bindingExprGroup = new IBaseExpression[1];
		bindingExprGroup[0] = new ScriptExpression("dataSetRow.DATE_FOR_GROUP");
		GroupDefinition[] groupDefn = new GroupDefinition[] { new GroupDefinition("group1") };
		groupDefn[0].setKeyExpression("row.GROUP_DATE");
		groupDefn[0].setInterval(IGroupDefinition.YEAR_INTERVAL);
		if (startValue != null)
			groupDefn[0].setIntervalStart(startValue);
		groupDefn[0].setIntervalRange(1);

		String[] bindingNameRow = new String[4];
		bindingNameRow[0] = "ROW_DATE_FOR_GROUP";
		bindingNameRow[1] = "ROW_ID";
		bindingNameRow[2] = "ROW_AMOUT1";
		bindingNameRow[3] = "ROW_AMOUT2";
		IBaseExpression[] bindingExprRow = new IBaseExpression[4];
		bindingExprRow[0] = new ScriptExpression("dataSetRow.DATE_FOR_GROUP");
		bindingExprRow[1] = new ScriptExpression("dataSetRow.ID");
		bindingExprRow[2] = new ScriptExpression("dataSetRow.AMOUNT1");
		bindingExprRow[3] = new ScriptExpression("dataSetRow.AMOUNT2");

		String[] columnStr = new String[] { "date_for_group", "id", "amount1", "amount2" };

		QueryDefinition qd = this.createQuery(bindingNameGroup, bindingExprGroup, groupDefn, null, null, null, null,
				null, null, bindingNameRow, bindingExprRow);

		String outputStr = getOutputStrForGroupTest(30, qd, groupDefn.length, bindingNameRow, columnStr);
		testPrint(outputStr);

		this.checkOutputFile();
	}

	/**
	 * Test feature of group on month
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGroupOnMonth() throws Exception {
		/* Calendar calendar = Calendar.getInstance( ); */
		calendar.set(2005, 0, 1);
		groupOnMonth(calendar.getTime());
	}

	/**
	 * Test feature of group on month
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGroupOnMonthWithCache() throws Exception {
		/* Calendar calendar = Calendar.getInstance( ); */
		calendar.set(2005, 0, 1);
		groupOnMonthWithCache(calendar.getTime());
	}

	/**
	 * Test feature of group on month
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGroupOnMonth1() throws Exception {
		groupOnMonth(null);
	}

	private void groupOnMonth(Object startValue) throws Exception, IOException {
		String[] bindingNameGroup = new String[1];
		bindingNameGroup[0] = "GROUP_DATE";
		IBaseExpression[] bindingExprGroup = new IBaseExpression[1];
		bindingExprGroup[0] = new ScriptExpression("dataSetRow.DATE_FOR_GROUP");
		GroupDefinition[] groupDefn = new GroupDefinition[] { new GroupDefinition("group1") };
		groupDefn[0].setKeyExpression("row.GROUP_DATE");
		groupDefn[0].setInterval(IGroupDefinition.MONTH_INTERVAL);
		if (startValue != null)
			groupDefn[0].setIntervalStart(startValue);
		groupDefn[0].setIntervalRange(1);

		String[] bindingNameRow = new String[4];
		bindingNameRow[0] = "ROW_DATE_FOR_GROUP";
		bindingNameRow[1] = "ROW_ID";
		bindingNameRow[2] = "ROW_AMOUT1";
		bindingNameRow[3] = "ROW_AMOUT2";
		IBaseExpression[] bindingExprRow = new IBaseExpression[4];
		bindingExprRow[0] = new ScriptExpression("dataSetRow.DATE_FOR_GROUP");
		bindingExprRow[1] = new ScriptExpression("dataSetRow.ID");
		bindingExprRow[2] = new ScriptExpression("dataSetRow.AMOUNT1");
		bindingExprRow[3] = new ScriptExpression("dataSetRow.AMOUNT2");

		String[] columnStr = new String[] { "date_for_group", "id", "amount1", "amount2" };

		QueryDefinition qd = this.createQuery(bindingNameGroup, bindingExprGroup, groupDefn, null, null, null, null,
				null, null, bindingNameRow, bindingExprRow);

		String outputStr = getOutputStrForGroupTest(30, qd, groupDefn.length, bindingNameRow, columnStr);
		testPrint(outputStr);

		this.checkOutputFile();
	}

	/**
	 * 
	 * @param startValue
	 * @throws Exception
	 * @throws IOException
	 */
	private void groupOnMonthWithCache(Object startValue) throws Exception, IOException {
		String[] bindingNameGroup = new String[1];
		bindingNameGroup[0] = "GROUP_DATE";
		IBaseExpression[] bindingExprGroup = new IBaseExpression[1];
		bindingExprGroup[0] = new ScriptExpression("dataSetRow.DATE_FOR_GROUP");
		GroupDefinition[] groupDefn = new GroupDefinition[] { new GroupDefinition("group1") };
		groupDefn[0].setKeyExpression("row.GROUP_DATE");
		groupDefn[0].setInterval(IGroupDefinition.MONTH_INTERVAL);
		if (startValue != null)
			groupDefn[0].setIntervalStart(startValue);
		groupDefn[0].setIntervalRange(1);

		String[] bindingNameRow = new String[4];
		bindingNameRow[0] = "ROW_DATE_FOR_GROUP";
		bindingNameRow[1] = "ROW_ID";
		bindingNameRow[2] = "ROW_AMOUT1";
		bindingNameRow[3] = "ROW_AMOUT2";
		IBaseExpression[] bindingExprRow = new IBaseExpression[4];
		bindingExprRow[0] = new ScriptExpression("dataSetRow.DATE_FOR_GROUP");
		bindingExprRow[1] = new ScriptExpression("dataSetRow.ID");
		bindingExprRow[2] = new ScriptExpression("dataSetRow.AMOUNT1");
		bindingExprRow[3] = new ScriptExpression("dataSetRow.AMOUNT2");

		String[] columnStr = new String[] { "date_for_group", "id", "amount1", "amount2" };

		QueryDefinition qd = this.createQuery(bindingNameGroup, bindingExprGroup, groupDefn, null, null, null, null,
				null, null, bindingNameRow, bindingExprRow);
		qd.setCacheQueryResults(true);
		String outputStr = getOutputStrForGroupTestWithCache(30, qd, groupDefn.length, bindingNameRow, columnStr);
		testPrint(outputStr);

		this.checkOutputFile();
	}

	/**
	 * Test feature of group on quarter
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGroupOnQuarter() throws Exception {
		/* Calendar calendar = Calendar.getInstance( ); */
		calendar.set(2005, 2, 1);
		groupOnQuarter(calendar.getTime());
	}

	/**
	 * Test feature of group on quarter
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGroupOnQuarter1() throws Exception {
		groupOnQuarter(null);
	}

	private void groupOnQuarter(Object startValue) throws Exception, IOException {
		String[] bindingNameGroup = new String[1];
		bindingNameGroup[0] = "GROUP_DATE";
		IBaseExpression[] bindingExprGroup = new IBaseExpression[1];
		bindingExprGroup[0] = new ScriptExpression("dataSetRow.DATE_FOR_QUARTER");
		GroupDefinition[] groupDefn = new GroupDefinition[] { new GroupDefinition("group1") };
		groupDefn[0].setKeyExpression("row.GROUP_DATE");
		groupDefn[0].setInterval(IGroupDefinition.QUARTER_INTERVAL);
		groupDefn[0].setIntervalRange(2);
		if (startValue != null)
			groupDefn[0].setIntervalStart(startValue);

		String[] bindingNameRow = new String[4];
		bindingNameRow[0] = "ROW_DATE_FOR_QUARTER";
		bindingNameRow[1] = "ROW_ID";
		bindingNameRow[2] = "ROW_AMOUT1";
		bindingNameRow[3] = "ROW_AMOUT2";
		IBaseExpression[] bindingExprRow = new IBaseExpression[4];
		bindingExprRow[0] = new ScriptExpression("dataSetRow.DATE_FOR_QUARTER");
		bindingExprRow[1] = new ScriptExpression("dataSetRow.ID");
		bindingExprRow[2] = new ScriptExpression("dataSetRow.AMOUNT1");
		bindingExprRow[3] = new ScriptExpression("dataSetRow.AMOUNT2");

		String[] columnStr = new String[] { "date_for_quarter", "id", "amount1", "amount2" };
		QueryDefinition qd = this.createQuery(bindingNameGroup, bindingExprGroup, groupDefn, null, null, null, null,
				null, null, bindingNameRow, bindingExprRow);
		String outputStr = getOutputStrForGroupTest(40, qd, groupDefn.length, bindingNameRow, columnStr);
		testPrint(outputStr);

		this.checkOutputFile();
	}

	/**
	 * Return query result
	 * 
	 * @param expectedLen
	 * @param qd
	 * @param gdArray
	 * @param beArray
	 * @param columStr
	 * @return query string output
	 * @throws Exception
	 */
	private String getOutputStrForGroupTestWithCache(int expectedLen, QueryDefinition qd, int groupDefCount,
			String[] beArray, String[] columStr) throws Exception {
		StringBuffer sBuffer = new StringBuffer();

		// execute query
		IPreparedQuery preparedQuery = dataEngine.prepare(qd, this.getAppContext());
		IQueryResults queryResults = preparedQuery.execute(null);
		IResultIterator ri = queryResults.getResultIterator();
		String queryResultID = queryResults.getID();
		ri.close();
		qd.setQueryResultsID(queryResultID);
		preparedQuery = dataEngine.prepare(qd, this.getAppContext());

		queryResults = preparedQuery.execute(null);
		ri = queryResults.getResultIterator();
		String metaData = "";
		for (int i = 0; i < columStr.length; i++) {
			metaData += formatStr(columStr[i], expectedLen);
		}
		sBuffer.append(metaData);
		sBuffer.append("\n");

		int groupCount = groupDefCount;
		while (ri.next()) {
			String rowData = "";

			int startLevel = ri.getStartingGroupLevel();
			if (startLevel <= groupCount) {
				if (startLevel == 0)
					startLevel = 1;

				for (int j = 0; j < startLevel - 1; j++) {
					rowData += formatStr("", expectedLen);
				}
				for (int j = startLevel - 1; j < beArray.length; j++) {
					String value;
					if (ri.getValue(beArray[j]) != null)
						value = ri.getValue(beArray[j]).toString();
					else
						value = "null";

					rowData += formatStr(value, expectedLen);
				}
			} else {
				for (int j = 0; j < groupCount; j++) {
					rowData += formatStr("", expectedLen);
					;
				}
				for (int j = groupCount; j < beArray.length; j++) {
					String value = ri.getValue(beArray[j]).toString();
					rowData += formatStr(value, expectedLen);
				}
			}
			sBuffer.append(rowData);
			sBuffer.append("\n");
		}
		ri.close();
		return new String(sBuffer);
	}

	/**
	 * Return query result
	 * 
	 * @param expectedLen
	 * @param qd
	 * @param gdArray
	 * @param beArray
	 * @param columStr
	 * @return query string output
	 * @throws Exception
	 */
	private String getOutputStrForGroupTest(int expectedLen, QueryDefinition qd, int groupDefCount, String[] beArray,
			String[] columStr) throws Exception {
		StringBuffer sBuffer = new StringBuffer();

		// execute query
		IResultIterator ri = this.executeQuery(qd);

		String metaData = "";
		for (int i = 0; i < columStr.length; i++) {
			metaData += formatStr(columStr[i], expectedLen);
		}
		sBuffer.append(metaData);
		sBuffer.append("\n");

		int groupCount = groupDefCount;
		while (ri.next()) {
			String rowData = "";

			int startLevel = ri.getStartingGroupLevel();
			if (startLevel <= groupCount) {
				if (startLevel == 0)
					startLevel = 1;

				for (int j = 0; j < startLevel - 1; j++) {
					rowData += formatStr("", expectedLen);
				}
				for (int j = startLevel - 1; j < beArray.length; j++) {
					String value;
					if (ri.getValue(beArray[j]) != null)
						value = ri.getValue(beArray[j]).toString();
					else
						value = "null";

					rowData += formatStr(value, expectedLen);
				}
			} else {
				for (int j = 0; j < groupCount; j++) {
					rowData += formatStr("", expectedLen);
					;
				}
				for (int j = groupCount; j < beArray.length; j++) {
					String value = ri.getValue(beArray[j]).toString();
					rowData += formatStr(value, expectedLen);
				}
			}
			sBuffer.append(rowData);
			sBuffer.append("\n");
		}

		return new String(sBuffer);
	}

	/**
	 * Test feature of group on day
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGroupOnDay() throws Exception {
		calendar.set(2005, 0, 1, 10, 0, 0);
		groupOnDay(calendar.getTime());
	}

	/**
	 * Test feature of group on day
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGroupOnDay1() throws Exception {
		groupOnDay(null);
	}

	/**
	 * Test feature of group on day
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGroupOnDayWithCache() throws Exception {
		groupOnDayWithCache(null);
	}

	private void groupOnDay(Object startValue) throws Exception, IOException {
		String[] bindingNameGroup = new String[1];
		bindingNameGroup[0] = "GROUP_DATE";
		IBaseExpression[] bindingExprGroup = new IBaseExpression[1];
		bindingExprGroup[0] = new ScriptExpression("dataSetRow.DATE_FOR_GROUP");
		GroupDefinition[] groupDefn = new GroupDefinition[] { new GroupDefinition("group1") };
		groupDefn[0].setKeyExpression("row.GROUP_DATE");
		groupDefn[0].setInterval(IGroupDefinition.DAY_INTERVAL);
		if (startValue != null)
			groupDefn[0].setIntervalStart(startValue);
		groupDefn[0].setIntervalRange(3);

		String[] bindingNameRow = new String[4];
		bindingNameRow[0] = "ROW_DATE_FOR_GROUP";
		bindingNameRow[1] = "ROW_ID";
		bindingNameRow[2] = "ROW_AMOUT1";
		bindingNameRow[3] = "ROW_AMOUT2";
		IBaseExpression[] bindingExprRow = new IBaseExpression[4];
		bindingExprRow[0] = new ScriptExpression("dataSetRow.DATE_FOR_GROUP");
		bindingExprRow[1] = new ScriptExpression("dataSetRow.ID");
		bindingExprRow[2] = new ScriptExpression("dataSetRow.AMOUNT1");
		bindingExprRow[3] = new ScriptExpression("dataSetRow.AMOUNT2");

		String[] columnStr = new String[] { "date_for_group", "id", "amount1", "amount2" };

		QueryDefinition qd = this.createQuery(bindingNameGroup, bindingExprGroup, groupDefn, null, null, null, null,
				null, null, bindingNameRow, bindingExprRow);

		String outputStr = getOutputStrForGroupTest(30, qd, groupDefn.length, bindingNameRow, columnStr);
		testPrint(outputStr);

		this.checkOutputFile();
	}

	private void groupOnDayWithCache(Object startValue) throws Exception, IOException {
		String[] bindingNameGroup = new String[1];
		bindingNameGroup[0] = "GROUP_DATE";
		IBaseExpression[] bindingExprGroup = new IBaseExpression[1];
		bindingExprGroup[0] = new ScriptExpression("dataSetRow.DATE_FOR_GROUP");
		GroupDefinition[] groupDefn = new GroupDefinition[] { new GroupDefinition("group1") };
		groupDefn[0].setKeyExpression("row.GROUP_DATE");
		groupDefn[0].setInterval(IGroupDefinition.DAY_INTERVAL);
		if (startValue != null)
			groupDefn[0].setIntervalStart(startValue);
		groupDefn[0].setIntervalRange(3);

		String[] bindingNameRow = new String[4];
		bindingNameRow[0] = "ROW_DATE_FOR_GROUP";
		bindingNameRow[1] = "ROW_ID";
		bindingNameRow[2] = "ROW_AMOUT1";
		bindingNameRow[3] = "ROW_AMOUT2";
		IBaseExpression[] bindingExprRow = new IBaseExpression[4];
		bindingExprRow[0] = new ScriptExpression("dataSetRow.DATE_FOR_GROUP");
		bindingExprRow[1] = new ScriptExpression("dataSetRow.ID");
		bindingExprRow[2] = new ScriptExpression("dataSetRow.AMOUNT1");
		bindingExprRow[3] = new ScriptExpression("dataSetRow.AMOUNT2");

		String[] columnStr = new String[] { "date_for_group", "id", "amount1", "amount2" };

		QueryDefinition qd = this.createQuery(bindingNameGroup, bindingExprGroup, groupDefn, null, null, null, null,
				null, null, bindingNameRow, bindingExprRow);
		qd.setCacheQueryResults(true);
		String outputStr = getOutputStrForGroupTestWithCache(30, qd, groupDefn.length, bindingNameRow, columnStr);
		testPrint(outputStr);

		this.checkOutputFile();
	}

	/**
	 * Test feature of group on number
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGroupOnNumber() throws Exception {
		groupOnNumber(new Integer(10), 3);

	}

	/**
	 * Test feature of group on number
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGroupOnNumber0() throws Exception {
		groupOnNumber(null, 0);
	}

	/**
	 * Test feature of group on number
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGroupOnNumber1() throws Exception {
		groupOnNumber(null, 3);
	}

	private void groupOnNumber(Object startValue, double intervalRange) throws Exception, IOException {
		String[] bindingNameGroup = new String[1];
		bindingNameGroup[0] = "GROUP_NUMBER";
		IBaseExpression[] bindingExprGroup = new IBaseExpression[1];
		bindingExprGroup[0] = new ScriptExpression("dataSetRow.ID");
		GroupDefinition[] groupDefn = new GroupDefinition[] { new GroupDefinition("group1") };
		groupDefn[0].setKeyExpression("row.GROUP_NUMBER");
		groupDefn[0].setInterval(IGroupDefinition.NUMERIC_INTERVAL);
		if (startValue != null)
			groupDefn[0].setIntervalStart(startValue);
		groupDefn[0].setIntervalRange(intervalRange);

		String[] bindingNameRow = new String[3];
		bindingNameRow[0] = "ROW_ID";
		bindingNameRow[1] = "ROW_AMOUT1";
		bindingNameRow[2] = "ROW_AMOUT2";
		IBaseExpression[] bindingExprRow = new IBaseExpression[3];
		bindingExprRow[0] = new ScriptExpression("dataSetRow.ID");
		bindingExprRow[1] = new ScriptExpression("dataSetRow.AMOUNT1");
		bindingExprRow[2] = new ScriptExpression("dataSetRow.AMOUNT2");

		String[] columnStr = new String[] { "id", "amount1", "amount2" };

		QueryDefinition qd = this.createQuery(bindingNameGroup, bindingExprGroup, groupDefn, null, null, null, null,
				null, null, bindingNameRow, bindingExprRow);

		String outputStr = getOutputStrForGroupTest(30, qd, groupDefn.length, bindingNameRow, columnStr);
		testPrint(outputStr);

		this.checkOutputFile();
	}

	/**
	 * Test feature of group on string
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGroupOnString() throws Exception {
		groupOnString("13");
	}

	/**
	 * Test feature of group on string
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGroupOnString0() throws Exception {
		groupOnString(null, 0);
	}

	/**
	 * Test feature of group on string
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGroupOnString1() throws Exception {
		groupOnString(null);
	}

	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGroupOnString2() throws Exception {
		groupOnString(null, 3);
	}

	/**
	 * 
	 * @param startValue
	 * @throws Exception
	 * @throws IOException
	 */
	private void groupOnString(Object startValue) throws IOException, Exception {
		groupOnString(startValue, 1);
	}

	/**
	 * 
	 * @param startValue
	 * @param intervalRange
	 * @throws Exception
	 * @throws IOException
	 */
	private void groupOnString(Object startValue, int intervalRange) throws Exception, IOException {
		ComputedColumn computedColumn = new ComputedColumn("STR",
				"(row.ID==9)?\"\":((row.ID==8)?null:row.ID.toString())", DataType.STRING_TYPE);
		((BaseDataSetDesign) this.dataSet).addComputedColumn(computedColumn);

		String[] bindingNameGroup = new String[1];
		bindingNameGroup[0] = "GROUP_STR";
		IBaseExpression[] bindingExprGroup = new IBaseExpression[1];
		bindingExprGroup[0] = new ScriptExpression("dataSetRow.STR");
		GroupDefinition[] groupDefn = new GroupDefinition[] { new GroupDefinition("group1") };
		groupDefn[0].setKeyExpression("row.GROUP_STR");
		groupDefn[0].setInterval(IGroupDefinition.STRING_PREFIX_INTERVAL);
		if (startValue != null)
			groupDefn[0].setIntervalStart(startValue);
		groupDefn[0].setIntervalRange(intervalRange);

		String[] bindingNameRow = new String[4];
		bindingNameRow[0] = "ROW_STR_FOR_GROUP";
		bindingNameRow[1] = "ROW_ID";
		bindingNameRow[2] = "ROW_AMOUT1";
		bindingNameRow[3] = "ROW_AMOUT2";
		IBaseExpression[] bindingExprRow = new IBaseExpression[4];
		bindingExprRow[0] = new ScriptExpression("dataSetRow.STR");
		bindingExprRow[1] = new ScriptExpression("dataSetRow.ID");
		bindingExprRow[2] = new ScriptExpression("dataSetRow.AMOUNT1");
		bindingExprRow[3] = new ScriptExpression("dataSetRow.AMOUNT2");

		String[] columnStr = new String[] { "Str", "ID", "amount1", "amount2" };

		QueryDefinition qd = this.createQuery(bindingNameGroup, bindingExprGroup, groupDefn, null, null, null, null,
				null, null, bindingNameRow, bindingExprRow);

		String outputStr = getOutputStrForGroupTest(30, qd, groupDefn.length, bindingNameRow, columnStr);
		testPrint(outputStr);

		this.checkOutputFile();
	}

	/**
	 * Format string to specified length, if the length of input string is larger
	 * than expected length, then input string will be directly return without any
	 * format.
	 * 
	 * @param str    needs to be formatted string
	 * @param length expected length
	 * @return formatted string
	 */
	private static String formatStr(String inputStr, int length) {
		if (inputStr == null)
			return null;

		int inputLen = inputStr.length();
		if (inputLen >= length)
			return inputStr;

		int appendLen = length - inputLen;
		char[] appendChar = new char[appendLen];
		for (int i = 0; i < appendLen; i++) {
			appendChar[i] = ' ';
		}
		String result = inputStr + new String(appendChar);

		return result;
	}

}

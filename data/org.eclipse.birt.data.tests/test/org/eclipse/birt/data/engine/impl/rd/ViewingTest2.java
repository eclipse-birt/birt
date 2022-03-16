/*******************************************************************************
* Copyright (c) 2004 Actuate Corporation.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v2.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-2.0.html
*
* Contributors:
*  Actuate Corporation  - initial API and implementation
*******************************************************************************/

package org.eclipse.birt.data.engine.impl.rd;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IFilterDefinition.FilterTarget;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.api.querydefn.BaseExpression;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.FilterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.SortDefinition;
import org.eclipse.birt.data.engine.api.querydefn.SubqueryDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ibm.icu.util.TimeZone;

import testutil.ConfigText;

/**
 * Test whehter the query running on report document can be saved into another
 * report document.
 */
public class ViewingTest2 extends RDTestCase {
	private String GEN_queryResultID;
	private String UPDATE_queryResultID;
	private boolean notIncludeAggr;

	private String[] rowExprName;
	private String[] totalExprName;
	private String updateNewBindingName;
	private IBaseExpression updateNewBindingExpr;

	private boolean add_subquery_on_query;

	private boolean USE_ROW_IN_AGGREGATION;
	private boolean USE_DATE_IN_COLUMNBINDING;
	private boolean GEN_add_filter;
	private boolean GEN_add_topN_filter;
	private boolean GEN_add_group;
	private boolean GEN_add_group1 = false;
	private boolean GEN_add_subquery;
	private boolean GEN_add_sort;
	private boolean GEN_print;
	private boolean GEN_use_invalid_column;
	private boolean GEN_USE_RUNNING_AGGR;

	private int UPDATE_add_filter;
	private boolean UPDATE_add_sort;
	private boolean UPDATE_add_same_group;
	private boolean UPDATE_add_diff_group;
	private int UPDATE_add_subquery;

	private int PRE_add_filter;
	private boolean PRE_add_sort;
	private boolean PRE_execute_query;
	private boolean PRE_print_groupinfo;
	private boolean PRE_use_skipto;
	private int PRE_use_skipto_num;

	private boolean PRE_basedon_genfilter;

	private List GEN_filterDefn;
	private List UPDATE_filterDefn;

	private boolean TEST_ISEMPTY;

	private final static int UPDATE = 1;
	private final static int PRESENTATION = 2;

	private final static String subQueryName1 = "IAMTEST1";
	private String[] subRowExprName1;

	private final static String subQueryName2 = "IAMTEST2";

	private String[] subRowExprName2;

	private int PRE_add_group;

	private boolean usesDetails = true;

	private TimeZone currentTimeZone = TimeZone.getDefault();
	private boolean USE_DATE_IN_SUBQUERY;

	/*
	 * @see org.eclipse.birt.data.engine.api.APITestCase#getDataSourceInfo()
	 */
	@Override
	protected DataSourceInfo getDataSourceInfo() {
		return new DataSourceInfo(ConfigText.getString("Api.TestData.TableName"),
				ConfigText.getString("Api.TestData.TableSQL"), ConfigText.getString("Api.TestData.TestDataFileName"));
	}

	/*
	 * @see org.eclipse.birt.data.engine.impl.rd.RDTestCase#useFolderArchive()
	 */
	@Override
	protected boolean useFolderArchive() {
		return true;
	}

	/*
	 * @see org.eclipse.birt.data.engine.impl.rd.RDTestCase#setUp()
	 */
	@Before
	public void viewing2SetUp() throws Exception {

		this.notIncludeAggr = false;

		this.add_subquery_on_query = false;
		this.USE_ROW_IN_AGGREGATION = false;

		this.GEN_queryResultID = null;
		this.UPDATE_queryResultID = null;
		this.USE_DATE_IN_COLUMNBINDING = true;

		this.GEN_add_subquery = false;
		this.GEN_add_sort = true;
		// dataSetRow.AMOUNT>50
		this.GEN_add_filter = false;
		// dataSetRow.COUNTRY
		this.GEN_add_group = false;
		// print information
		this.GEN_print = false;
		this.GEN_USE_RUNNING_AGGR = false;
		this.GEN_use_invalid_column = false;

		// row.AMOUNT_1>200 -> dataSetRow.AMOUNT>200
		this.UPDATE_add_filter = -1;
		// row.AMOUNT_1 ASC
		this.UPDATE_add_sort = false;
		// group on row.AMOUNT_1
		this.UPDATE_add_diff_group = false;
		// dataSetRow.COUNTRY
		this.UPDATE_add_same_group = false;
		// dataSetRow.COUNTRY
		this.UPDATE_add_subquery = 0;

		this.USE_DATE_IN_SUBQUERY = false;
		// 0: row.AMOUNT_1>200
		// 1: row.AMOUNT_1>50 && row.AMOUNT_1<7000
		// 2: row.AMOUNT_1>50 && row.AMOUNT_1<700
		this.PRE_add_filter = -1;
		// row.AMOUNT_1 ASC
		this.PRE_add_sort = false;

		this.PRE_execute_query = false;
		this.PRE_print_groupinfo = false;
		this.PRE_use_skipto = false;
		this.PRE_use_skipto_num = -1;
		this.PRE_add_group = -1;
		this.PRE_basedon_genfilter = false;

		this.GEN_filterDefn = new ArrayList();
		this.UPDATE_filterDefn = new ArrayList();

		this.TEST_ISEMPTY = false;

		TimeZone.setDefault(TimeZone.getTimeZone("GMT+0"));

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.impl.rd.RDTestCase#tearDown()
	 */
	@After
	public void viewing2TearDown() throws Exception {
		if (myPreDataEngine != null) {
			myPreDataEngine.shutdown();
			myPreDataEngine.clearCache(dataSource, dataSet);
			myPreDataEngine = null;
		}
		if (myPreDataEngine2 != null) {
			myPreDataEngine2.shutdown();
			myPreDataEngine2.clearCache(dataSource, dataSet);
			myPreDataEngine2 = null;
		}
		TimeZone.setDefault(this.currentTimeZone);
	}

	/**
	 * With filter
	 *
	 * @throws BirtException
	 */
	@Test
	public void testBasic() throws Exception {
		this.GEN_add_filter = true;
		this.GEN_print = true;
		this.genBasicIV();
		this.closeArchiveWriter();

		DataEngineContext deContext2 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		deContext2.setTmpdir(this.getTempDir());
		myPreDataEngine = DataEngine.newDataEngine(deContext2);

		this.UPDATE_add_sort = true;
		this.updatePreBasicIV();
		this.closeArchiveReader();
		this.closeArchiveWriter();

		DataEngineContext deContext3 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext3);

		this.PRE_add_filter = 0;
		this.PRE_add_sort = true;
		this.PRE_execute_query = true;
		this.preBasicIV();

		this.checkOutputFile();
	}

	/**
	 * Without filter
	 *
	 * @throws BirtException
	 */
	@Test
	public void testBasic1() throws Exception {
		this.GEN_add_filter = true;
		this.genBasicIV();
		this.closeArchiveWriter();

		DataEngineContext deContext2 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext2);

		this.UPDATE_add_sort = true;
		this.updatePreBasicIV();
		this.closeArchiveReader();
		this.closeArchiveWriter();

		DataEngineContext deContext3 = newContext(DataEngineContext.MODE_PRESENTATION, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext3);
		this.preBasicIV();

		this.checkOutputFile();
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testNestedQuery() throws Exception {
		List ids = this.genBasicNestedQuery();

		this.preBasicNestedQuery(ids);

		this.checkOutputFile();
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testComplexNestedQuery() throws Exception {
		List ids = this.genComplexNestedQuery();

		this.preComplexNestedQuery(ids);

		this.checkOutputFile();
	}

	/**
	 * With filter
	 *
	 * @throws BirtException
	 */
	@Test
	public void testBasic2() throws Exception {
		this.GEN_add_filter = true;
		this.genBasicIV();
		this.closeArchiveWriter();

		DataEngineContext deContext2 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		deContext2.setTmpdir(this.getTempDir());
		myPreDataEngine = DataEngine.newDataEngine(deContext2);

		this.UPDATE_add_filter = 0;
		this.UPDATE_add_sort = true;
		this.updatePreBasicIV();
		this.closeArchiveReader();
		this.closeArchiveWriter();

		DataEngineContext deContext3 = newContext(DataEngineContext.MODE_PRESENTATION, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext3);
		this.PRE_use_skipto = true;
		this.preBasicIV();

		this.checkOutputFile();
	}

	/**
	 * Without execute query.
	 *
	 * @throws BirtException
	 */
	@Test
	public void testBasic3() throws Exception {
		this.GEN_add_group = true;
		this.genBasicIV();
		this.closeArchiveWriter();

		DataEngineContext deContext2 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		deContext2.setTmpdir(this.getTempDir());
		myPreDataEngine = DataEngine.newDataEngine(deContext2);

		this.UPDATE_add_filter = 0;
		this.UPDATE_add_same_group = true;
		this.updatePreBasicIV();
		this.closeArchiveReader();
		this.closeArchiveWriter();

		DataEngineContext deContext3 = newContext(DataEngineContext.MODE_PRESENTATION, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext3);

		this.PRE_execute_query = false;
		this.PRE_print_groupinfo = true;
		this.preBasicIV();

		this.checkOutputFile();
	}

	@Test
	public void testBasic4() throws Exception {
		QueryDefinition qd = this.newReportQuery();
		// generation
		IQueryResults qr = myGenDataEngine.prepare(qd).execute(scope);

		// important step
		GEN_queryResultID = qr.getID();

		IResultIterator ri = qr.getResultIterator();
		ri.moveTo(3);
		ri.close();
		myGenDataEngine.shutdown();
		this.closeArchiveWriter();

		// First IV
		DataEngineContext deContext2 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		deContext2.setTmpdir(this.getTempDir());
		myPreDataEngine = DataEngine.newDataEngine(deContext2);

		qd.setQueryResultsID(this.GEN_queryResultID);

		qr = myPreDataEngine.prepare(qd).execute(null);
		this.UPDATE_queryResultID = qr.getID();

		ri = qr.getResultIterator();
		ri.moveTo(3);
		ri.close();
		myPreDataEngine.shutdown();
		this.closeArchiveReader();
		this.closeArchiveWriter();

		// Second IV
		DataEngineContext deContext3 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext3);

		qd.setQueryResultsID(this.UPDATE_queryResultID);

		qr = myPreDataEngine.prepare(qd).execute(null);
		this.UPDATE_queryResultID = qr.getID();

		ri = qr.getResultIterator();
		ri.moveTo(3);
		ri.close();
		myPreDataEngine.shutdown();
		this.closeArchiveReader();
		this.closeArchiveWriter();

	}

	/**
	 * With filter
	 *
	 * @throws BirtException
	 */
	@Test
	public void testBasic5() throws Exception {
		this.GEN_add_filter = false;
		this.GEN_print = true;
		this.GEN_add_sort = false;
		this.genBasicIV();
		this.closeArchiveWriter();

		DataEngineContext deContext2 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		deContext2.setTmpdir(this.getTempDir());
		myPreDataEngine = DataEngine.newDataEngine(deContext2);

		this.UPDATE_add_sort = true;
		this.updatePreBasicIV();
		this.closeArchiveReader();
		this.closeArchiveWriter();

		DataEngineContext deContext3 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext3);

		this.PRE_execute_query = true;
		this.PRE_basedon_genfilter = true;
		this.PRE_add_filter = -1;
		this.PRE_add_sort = false;
		this.preBasicIV();
		this.closeArchiveReader();
		this.closeArchiveWriter();

		this.checkOutputFile();
	}

	/**
	 * Test add a filter in presentation mode.
	 *
	 * @throws Exception
	 */
	@Test
	public void testBasic6() throws Exception {
		this.GEN_print = true;
		this.GEN_add_filter = false;
		this.genBasicIV();
		this.closeArchiveWriter();

		DataEngineContext deContext2 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		deContext2.setTmpdir(this.getTempDir());
		myPreDataEngine = DataEngine.newDataEngine(deContext2);

		this.UPDATE_add_filter = 0;
		this.updatePreBasicIV();
		this.closeArchiveReader();

		this.checkOutputFile();
	}

	/**
	 * Without filter
	 *
	 * @throws BirtException
	 */
	@Test
	public void testBasic7() throws Exception {
		this.GEN_add_group = true;
		this.GEN_add_group1 = true;
		this.genBasicIV();
		this.closeArchiveWriter();

		DataEngineContext deContext2 = newContext(DataEngineContext.MODE_PRESENTATION, fileName, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext2);

		this.updatePreBasicIV();
		this.closeArchiveReader();
		this.closeArchiveWriter();

		this.checkOutputFile();
	}

	/**
	 * Without filter
	 *
	 * @throws BirtException
	 */
	@Test
	public void testBasic8() throws Exception {
		this.GEN_add_group = true;
		this.GEN_add_group1 = true;
		this.GEN_USE_RUNNING_AGGR = true;
		this.genBasicIV();
		this.closeArchiveWriter();

		DataEngineContext deContext2 = newContext(DataEngineContext.MODE_PRESENTATION, fileName, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext2);

		this.updatePreBasicIV();
		this.closeArchiveReader();
		this.closeArchiveWriter();

		this.checkOutputFile();
	}

	/**
	 * same initial position between run and render task
	 *
	 * @throws BirtException
	 */
	@Test
	public void testBasic9() throws Exception {
		this.GEN_add_group = true;
		this.GEN_add_group1 = true;
		this.GEN_USE_RUNNING_AGGR = true;

		int initialPostion1;

		QueryDefinition qd = newGenIVReportQuery();
		qd.setUsesDetails(true);
		// generation
		IQueryResults qr = myGenDataEngine.prepare(qd).execute(scope);

		// important step
		GEN_queryResultID = qr.getID();

		IResultIterator ri = qr.getResultIterator();
		initialPostion1 = ri.getRowIndex();
		ri.close();
		qr.close();
		myGenDataEngine.shutdown();
		myGenDataEngine.clearCache(dataSource, dataSet);
		this.closeArchiveWriter();

		DataEngineContext deContext2 = newContext(DataEngineContext.MODE_PRESENTATION, fileName, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext2);

		// here queryResultID needs to set as the data set
		qd = newPreIVReportQuery(this.UPDATE_add_filter, this.UPDATE_add_sort, -1, UPDATE);
		qd.setUsesDetails(true);
		qd.setQueryResultsID(this.GEN_queryResultID);

		qr = myPreDataEngine.prepare(qd).execute(null);
		this.UPDATE_queryResultID = qr.getID();
		ri = qr.getResultIterator();
		int initialPostion2 = ri.getRowIndex();
		this.closeArchiveReader();
		this.closeArchiveWriter();

		assertTrue(initialPostion1 == initialPostion2 && initialPostion2 == 0);
	}

	/**
	 * Test the feature of Skip to
	 *
	 * @throws Exception
	 */
	@Test
	public void testBasic31() throws Exception {
		this.GEN_add_group = true;
		this.genBasicIV();
		this.closeArchiveWriter();

		DataEngineContext deContext2 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		deContext2.setTmpdir(this.getTempDir());
		myPreDataEngine = DataEngine.newDataEngine(deContext2);

		this.UPDATE_add_filter = 0;
		this.UPDATE_add_same_group = true;
		this.updatePreBasicIV();
		this.closeArchiveReader();
		this.closeArchiveWriter();

		DataEngineContext deContext3 = newContext(DataEngineContext.MODE_PRESENTATION, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext3);

		this.PRE_execute_query = false;
		this.PRE_print_groupinfo = true;
		this.PRE_use_skipto = true;
		this.PRE_use_skipto_num = 2;
		this.preBasicIV();

		this.checkOutputFile();
	}

	/**
	 * Not use detail. With filter
	 *
	 * @throws BirtException
	 */
	@Test
	public void testBasicNotDetail() throws Exception {
		this.usesDetails = false;
		this.GEN_add_filter = true;
		this.genBasicIV();
		this.closeArchiveWriter();

		DataEngineContext deContext2 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext2);

		this.UPDATE_add_filter = 0;
		this.UPDATE_add_sort = true;
		this.updatePreBasicIV();
		this.closeArchiveReader();
		this.closeArchiveWriter();

		DataEngineContext deContext3 = newContext(DataEngineContext.MODE_PRESENTATION, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext3);
		this.PRE_use_skipto = true;
		this.preBasicIV();

		this.checkOutputFile();
	}

	/**
	 * Not use detail. With filter
	 *
	 * @throws BirtException
	 */
	@Test
	public void testBasicNotDetail2() throws Exception {
		this.usesDetails = false;
		this.GEN_add_group = true;
		this.genBasicIV();
		this.closeArchiveWriter();

		DataEngineContext deContext2 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext2);

		this.UPDATE_add_filter = 0;
		this.UPDATE_add_same_group = true;
		this.updatePreBasicIV();
		this.closeArchiveReader();
		this.closeArchiveWriter();

		DataEngineContext deContext3 = newContext(DataEngineContext.MODE_PRESENTATION, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext3);

		this.PRE_print_groupinfo = true;
		this.preBasicIV();

		this.checkOutputFile();
	}

	/**
	 * Not use detail. With filter Gen: Test TopN filter
	 *
	 * @throws BirtException
	 */
	@Test
	public void testBasicNotDetail3() throws Exception {
		this.usesDetails = false;
		this.genBasicIV();
		this.closeArchiveWriter();

		DataEngineContext deContext2 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext2);

		this.UPDATE_add_filter = 5;
		this.updatePreBasicIV();
		this.closeArchiveReader();
		this.closeArchiveWriter();

		DataEngineContext deContext3 = newContext(DataEngineContext.MODE_PRESENTATION, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext3);

		this.PRE_execute_query = false;
		this.preBasicIV();

		this.checkOutputFile();
	}

	/**
	 * Not use detail. Sort when update, then sort+group in presentation.
	 *
	 * @throws Exception
	 */
	@Test
	public void testBasicNotDetail4() throws Exception {
		this.GEN_add_filter = false;
		this.GEN_add_group = false;
		this.genBasicIV();
		this.closeArchiveWriter();

		DataEngineContext deContext2 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext2);

		this.UPDATE_add_filter = -1;
		this.UPDATE_add_sort = true;
		this.UPDATE_add_diff_group = false;
		this.updatePreBasicIV();
		this.closeArchiveReader();
		this.closeArchiveWriter();

		DataEngineContext deContext3 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext3);

		// this.PRE_add_filter = 0;
		this.PRE_execute_query = true;
		this.PRE_add_sort = true;
		this.PRE_add_group = 0;
		this.preBasicIV();

		this.checkOutputFile();
	}

	/**
	 * Without execute query.
	 *
	 * @throws BirtException
	 */
	public void atestUpdateGroup() throws Exception {
		this.GEN_add_group = true;
		this.GEN_add_subquery = true;
		this.genBasicIV();
		this.closeArchiveWriter();

		DataEngineContext deContext2 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		deContext2.setTmpdir(this.getTempDir());
		myPreDataEngine = DataEngine.newDataEngine(deContext2);

		this.UPDATE_add_filter = 0;
		this.UPDATE_add_diff_group = true;
		this.UPDATE_add_subquery = 1;
		this.updatePreBasicIV();
		this.closeArchiveReader();
		this.closeArchiveWriter();

		DataEngineContext deContext3 = newContext(DataEngineContext.MODE_PRESENTATION, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext3);

		this.PRE_execute_query = false;
		this.PRE_print_groupinfo = true;
		this.PRE_use_skipto = true;
		this.preBasicIV();

		this.checkOutputFile();
	}

	/**
	 * With filter
	 *
	 * @throws BirtException
	 */
	@Test
	public void testAggregationOnRow() throws Exception {
		this.USE_ROW_IN_AGGREGATION = true;
		this.GEN_add_filter = false;
		this.genBasicIV();
		this.closeArchiveWriter();

		DataEngineContext deContext2 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		deContext2.setTmpdir(this.getTempDir());
		myPreDataEngine = DataEngine.newDataEngine(deContext2);

		this.UPDATE_add_sort = false;
		this.updatePreBasicIV();
		this.closeArchiveReader();
		this.closeArchiveWriter();

		DataEngineContext deContext3 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext3);

		this.PRE_add_filter = 0;
		this.PRE_execute_query = true;
		this.preBasicIV();

		this.checkOutputFile();
	}

	/**
	 * With filter
	 *
	 * @throws BirtException
	 */
	@Test
	public void testIsEmptyTrue() throws Exception {
		this.TEST_ISEMPTY = true;
		this.GEN_add_filter = true;
		this.genBasicIV();
		this.closeArchiveWriter();

		DataEngineContext deContext2 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext2);

		this.UPDATE_add_filter = 0;
		this.UPDATE_add_sort = true;
		this.updatePreBasicIV();
		this.closeArchiveReader();
		this.closeArchiveWriter();
	}

	/**
	 * Test the feature of update new column binding
	 *
	 * @throws Exception
	 */
	@Test
	public void testUpdateNewColumnBinding() throws Exception {
		this.GEN_add_group = true;
		this.genBasicIV();
		this.closeArchiveWriter();

		DataEngineContext deContext2 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext2);

		this.updateNewBindingExpr = new ScriptExpression("row.COUNTRY_1");
		this.updateNewBindingName = "COUNTRY_2";

		this.UPDATE_add_filter = 0;
		this.UPDATE_add_same_group = true;
		this.updatePreBasicIV();
		this.closeArchiveReader();
		this.closeArchiveWriter();

		DataEngineContext deContext3 = newContext(DataEngineContext.MODE_PRESENTATION, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext3);

		this.PRE_execute_query = false;
		this.PRE_print_groupinfo = true;
		this.PRE_use_skipto = true;
		this.preBasicIV();

		this.checkOutputFile();
	}

	/**
	 * Test the feature of update new aggregate column binding
	 *
	 * @throws Exception
	 */
	@Test
	public void testUpdateNewAggrBinding() throws Exception {
		this.GEN_add_group = true;
		this.genBasicIV();
		this.closeArchiveWriter();

		DataEngineContext deContext2 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext2);

		this.updateNewBindingExpr = new ScriptExpression("Total.sum( row.AMOUNT_1 )");
		this.updateNewBindingName = "TOTAL_AMOUT_BINDING";

		this.UPDATE_add_filter = 0;
		this.UPDATE_add_same_group = true;
		this.updatePreBasicIV();
		this.closeArchiveReader();
		this.closeArchiveWriter();

		DataEngineContext deContext3 = newContext(DataEngineContext.MODE_PRESENTATION, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext3);

		this.PRE_execute_query = false;
		this.PRE_print_groupinfo = true;
		this.PRE_use_skipto = true;
		this.preBasicIV();

		this.checkOutputFile();
	}

	/**
	 * Test the feature of adding multi update.
	 *
	 */
	@Test
	public void testMultiUpdate() throws Exception {
		this.GEN_add_group = true;
		this.genBasicIV();
		this.closeArchiveWriter();

		DataEngineContext deContext2 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext2);

		this.UPDATE_add_filter = 0;
		this.UPDATE_add_same_group = true;
		this.updatePreBasicIV();
		this.closeArchiveReader();
		this.closeArchiveWriter();

		DataEngineContext deContext3 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext3);

		this.UPDATE_add_filter = 1;
		this.UPDATE_add_same_group = true;
		this.updatePreBasicIV();
		this.closeArchiveReader();
		this.closeArchiveWriter();

		DataEngineContext deContext4 = newContext(DataEngineContext.MODE_PRESENTATION, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext4);

		this.PRE_execute_query = false;
		this.preBasicIV();

		this.checkOutputFile();
	}

	/**
	 * Test the feature of adding multi update.
	 *
	 */
	@Test
	public void testMultiUpdate2() throws Exception {
		this.genBasicIV();
		this.closeArchiveWriter();

		// add filter
		DataEngineContext deContext2 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext2);

		this.UPDATE_add_filter = 1;
		this.updatePreBasicIV();
		this.closeArchiveReader();
		this.closeArchiveWriter();

		// add filter+group
		DataEngineContext deContext3 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext3);

		this.UPDATE_add_filter = 1;
		this.UPDATE_add_same_group = true;
		this.updatePreBasicIV();
		this.closeArchiveReader();
		this.closeArchiveWriter();

		// add filter
		DataEngineContext deContext4 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext4);
		this.UPDATE_add_filter = 1;
		this.UPDATE_add_same_group = false;
		this.updatePreBasicIV();
		this.closeArchiveReader();
		this.closeArchiveWriter();

		// add nothing
		DataEngineContext deContext5 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext5);
		this.UPDATE_add_filter = -1;
		this.updatePreBasicIV();
		this.closeArchiveReader();
		this.closeArchiveWriter();

		// presentation
		DataEngineContext deContext6 = newContext(DataEngineContext.MODE_PRESENTATION, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext6);

		this.PRE_execute_query = false;
		this.preBasicIV();

		this.checkOutputFile();
	}

	/**
	 * With filter Gen: filter A Update: filter A, filter B
	 *
	 * @throws BirtException
	 */
	@Test
	public void testFilters1() throws Exception {
		this.GEN_add_filter = true;
		this.genBasicIV();
		this.closeArchiveWriter();

		DataEngineContext deContext2 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext2);

		this.UPDATE_add_filter = 1;
		this.UPDATE_add_sort = true;
		this.updatePreBasicIV();
		this.closeArchiveReader();
		this.closeArchiveWriter();

		DataEngineContext deContext3 = newContext(DataEngineContext.MODE_PRESENTATION, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext3);
		this.PRE_execute_query = true;
		this.PRE_add_sort = true;
		this.preBasicIV();

		this.checkOutputFile();
	}

	/**
	 * With filter Gen: filter A Update: filter B
	 *
	 * @throws BirtException
	 */
	@Test
	public void testFilters2() throws Exception {
		this.GEN_add_filter = true;
		this.genBasicIV();
		this.closeArchiveWriter();

		DataEngineContext deContext2 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext2);

		this.UPDATE_add_filter = 0;
		this.UPDATE_add_sort = true;
		this.updatePreBasicIV();
		this.closeArchiveReader();
		this.closeArchiveWriter();

		DataEngineContext deContext3 = newContext(DataEngineContext.MODE_PRESENTATION, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext3);
		this.PRE_execute_query = true;
		this.PRE_add_sort = true;
		this.preBasicIV();

		this.checkOutputFile();
	}

	/**
	 * With filter Gen: filter A Update: filter A, filter B Pre: filter A, filter C
	 *
	 * @throws BirtException
	 */
	@Test
	public void testFilters3() throws Exception {
		this.GEN_add_filter = true;
		this.genBasicIV();
		this.closeArchiveWriter();

		DataEngineContext deContext2 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext2);

		this.UPDATE_add_filter = 1;
		this.UPDATE_add_sort = true;
		this.updatePreBasicIV();
		this.closeArchiveReader();
		this.closeArchiveWriter();

		DataEngineContext deContext3 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext3);
		this.PRE_add_filter = 2;
		this.PRE_execute_query = true;
		this.PRE_add_sort = true;
		this.preBasicIV();

		this.checkOutputFile();
	}

	/**
	 * With filter Gen: filter A, Update: filter A, filter B, Presentation: filter
	 * A, filter B, filter C
	 *
	 * @throws BirtException
	 */
	@Test
	public void testFilters4() throws Exception {
		this.GEN_add_filter = true;
		this.genBasicIV();
		this.closeArchiveWriter();

		DataEngineContext deContext2 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext2);

		this.UPDATE_add_filter = 0;
		this.UPDATE_add_sort = true;
		this.updatePreBasicIV();
		this.closeArchiveReader();
		this.closeArchiveWriter();

		DataEngineContext deContext3 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext3);
		this.PRE_add_filter = 2;
		this.PRE_execute_query = true;
		this.PRE_basedon_genfilter = true;
		this.PRE_add_sort = true;
		this.preBasicIV();

		this.checkOutputFile();
	}

	/**
	 * With filter Gen: Test filter on Date type column
	 *
	 * @throws BirtException
	 */
	@Test
	public void testFilters5() throws Exception {
		this.genBasicIV();
		this.closeArchiveWriter();

		DataEngineContext deContext2 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext2);

		this.UPDATE_add_filter = 4;
		this.updatePreBasicIV();
		this.closeArchiveReader();
		this.closeArchiveWriter();

		DataEngineContext deContext3 = newContext(DataEngineContext.MODE_PRESENTATION, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext3);

		this.PRE_execute_query = false;
		this.PRE_add_sort = true;
		this.preBasicIV();

		this.checkOutputFile();
	}

	/**
	 * With filter Gen: Test TopN filter
	 *
	 * @throws BirtException
	 */
	@Test
	public void testFilters6() throws Exception {
		this.genBasicIV();
		this.closeArchiveWriter();

		DataEngineContext deContext2 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext2);

		this.UPDATE_add_filter = 5;
		this.updatePreBasicIV();
		this.closeArchiveReader();
		this.closeArchiveWriter();

		DataEngineContext deContext3 = newContext(DataEngineContext.MODE_PRESENTATION, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext3);

		this.PRE_execute_query = false;
		this.preBasicIV();

		this.checkOutputFile();
	}

	/**
	 * Test filter target usage
	 *
	 * @throws BirtException
	 */
	@Test
	public void testFilters7() throws Exception {
		this.GEN_add_topN_filter = true;
		this.GEN_print = true;
		this.genBasicIV();
		this.closeArchiveWriter();

		DataEngineContext deContext2 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext2);

		this.UPDATE_add_filter = 7;
		this.updatePreBasicIV();
		this.closeArchiveReader();
		this.closeArchiveWriter();

		DataEngineContext deContext3 = newContext(DataEngineContext.MODE_PRESENTATION, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext3);

		this.PRE_execute_query = false;
		this.preBasicIV();

		this.checkOutputFile();
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testGroup() throws Exception {
		this.GEN_add_filter = true;
		this.GEN_add_group = true;
		this.genBasicIV();
		this.closeArchiveWriter();

		DataEngineContext deContext2 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext2);

		this.UPDATE_add_filter = 0;
		this.UPDATE_add_sort = true;
		this.UPDATE_add_diff_group = true;
		this.updatePreBasicIV();
		this.closeArchiveReader();
		this.closeArchiveWriter();

		DataEngineContext deContext3 = newContext(DataEngineContext.MODE_PRESENTATION, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext3);

		// this.PRE_add_filter = 0;
		this.PRE_execute_query = false;
		this.preBasicIV();

		this.checkOutputFile();
	}

	/**
	 * Sort when update, then sort+group in presentation.
	 *
	 * @throws Exception
	 */
	@Test
	public void testSortGroup() throws Exception {
		this.GEN_add_filter = false;
		this.GEN_add_group = false;
		this.genBasicIV();
		this.closeArchiveWriter();

		DataEngineContext deContext2 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext2);

		this.UPDATE_add_filter = -1;
		this.UPDATE_add_sort = true;
		this.UPDATE_add_diff_group = false;
		this.updatePreBasicIV();
		this.closeArchiveReader();
		this.closeArchiveWriter();

		DataEngineContext deContext3 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext3);

		// this.PRE_add_filter = 0;
		this.PRE_execute_query = true;
		this.PRE_add_sort = true;
		this.PRE_add_group = 0;
		this.preBasicIV();

		this.checkOutputFile();
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testSubQuery() throws Exception {
		this.GEN_add_filter = true;
		this.GEN_add_group = true;
		this.GEN_add_subquery = true;
		this.genBasicIV();
		this.closeArchiveWriter();

		DataEngineContext deContext2 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext2);

		this.UPDATE_add_filter = 0;
		this.UPDATE_add_sort = true;
		this.UPDATE_add_same_group = true;
		this.UPDATE_add_subquery = 1;
		this.updatePreBasicIV();
		this.closeArchiveReader();
		this.closeArchiveWriter();

		DataEngineContext deContext3 = newContext(DataEngineContext.MODE_PRESENTATION, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext3);

		this.PRE_execute_query = false;
		this.preBasicIV();

		this.checkOutputFile();
	}

	/**
	 * The sub query index of updated report document is not the same as that of
	 * original one.
	 *
	 * @throws Exception
	 */
	@Test
	public void testSubQuery2() throws Exception {
		this.GEN_add_filter = true;
		this.GEN_add_group = true;
		this.GEN_add_subquery = true;
		this.GEN_print = true;
		this.genBasicIV();
		this.closeArchiveWriter();

		DataEngineContext deContext2 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext2);

		this.UPDATE_add_filter = 3;
		this.UPDATE_add_sort = true;
		this.UPDATE_add_same_group = true;
		this.UPDATE_add_subquery = 1;
		this.updatePreBasicIV();
		this.closeArchiveReader();
		this.closeArchiveWriter();

		DataEngineContext deContext3 = newContext(DataEngineContext.MODE_PRESENTATION, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext3);

		this.PRE_execute_query = false;
		this.preBasicIV();

		this.checkOutputFile();
	}

	/**
	 * Test the presentation result that based on original filter not updated
	 * filter.
	 *
	 * @throws Exception
	 */
	@Test
	public void testSubQuery3() throws Exception {
		this.GEN_add_filter = true;
		this.GEN_add_group = true;
		this.GEN_add_subquery = true;
		this.genBasicIV();
		this.closeArchiveWriter();

		DataEngineContext deContext2 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext2);

		this.UPDATE_add_filter = 0;
		this.UPDATE_add_same_group = true;
		this.UPDATE_add_subquery = 1;
		this.updatePreBasicIV();
		this.closeArchiveReader();
		this.closeArchiveWriter();

		DataEngineContext deContext3 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext3);

		this.PRE_execute_query = true;
		this.PRE_basedon_genfilter = true;
		this.PRE_add_group = 0;
		this.UPDATE_add_subquery = 1;
		this.preBasicIV();

		this.checkOutputFile();
	}

	/**
	 * Test the presentation result that based on original filter not updated
	 * filter.
	 *
	 * @throws Exception
	 */
	@Test
	public void testSubQuery4() throws Exception {
		this.GEN_add_filter = true;
		this.GEN_add_group = true;
		this.GEN_add_subquery = true;
		this.genBasicIV();
		this.closeArchiveWriter();

		DataEngineContext deContext2 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext2);

		this.UPDATE_add_filter = 0;
		this.UPDATE_add_same_group = true;
		this.UPDATE_add_subquery = 2;
		this.updatePreBasicIV();
		this.closeArchiveReader();
		this.closeArchiveWriter();

		DataEngineContext deContext3 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext3);

		this.PRE_execute_query = true;
		this.PRE_basedon_genfilter = true;
		this.PRE_add_group = 0;
		this.UPDATE_add_subquery = 2;
		this.preBasicIV();

		this.checkOutputFile();
	}

	/**
	 * Test the presentation result that based on original filter not updated
	 * filter.
	 *
	 * @throws Exception
	 */
	@Test
	public void testSubQuery5() throws Exception {
		this.GEN_add_filter = true;
		this.GEN_add_group = true;
		this.GEN_add_subquery = true;
		this.genBasicIV();
		this.closeArchiveWriter();

		DataEngineContext deContext2 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext2);

		this.UPDATE_add_filter = 0;
		this.UPDATE_add_same_group = true;
		this.UPDATE_add_subquery = 1;
		this.updatePreBasicIV();
		this.closeArchiveReader();
		this.closeArchiveWriter();

		DataEngineContext deContext3 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext3);

		this.PRE_execute_query = true;
		this.PRE_basedon_genfilter = true;
		this.PRE_add_group = 0;
		this.UPDATE_add_subquery = 2;
		this.preBasicIV();

		this.checkOutputFile();
	}

	/**
	 * Test the presentation result that based on original filter not updated
	 * filter.
	 *
	 * @throws Exception
	 */
	@Test
	public void testSubQuery6() throws Exception {
		this.GEN_add_filter = true;
		this.GEN_add_group = true;
		this.GEN_add_subquery = false;
		this.genBasicIV();
		this.closeArchiveWriter();

		DataEngineContext deContext2 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext2);

		this.UPDATE_add_filter = 0;
		this.UPDATE_add_same_group = true;
		this.UPDATE_add_subquery = 1;
		this.updatePreBasicIV();
		this.closeArchiveReader();
		this.closeArchiveWriter();

		DataEngineContext deContext3 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext3);

		this.PRE_execute_query = true;
		this.PRE_basedon_genfilter = true;
		this.PRE_add_group = 0;
		this.UPDATE_add_subquery = 2;
		this.preBasicIV();

		this.checkOutputFile();
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testSubQuery7() throws Exception {
		this.GEN_add_filter = true;
		this.GEN_add_group = true;
		this.GEN_add_subquery = true;
		this.USE_DATE_IN_COLUMNBINDING = false;
		this.USE_DATE_IN_SUBQUERY = true;
		this.genBasicIV();
		this.closeArchiveWriter();

		DataEngineContext deContext2 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext2);

		this.UPDATE_add_filter = 0;
		this.UPDATE_add_sort = true;
		this.UPDATE_add_same_group = true;
		this.UPDATE_add_subquery = 1;
		this.updatePreBasicIV();
		this.closeArchiveReader();
		this.closeArchiveWriter();

		DataEngineContext deContext3 = newContext(DataEngineContext.MODE_PRESENTATION, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext3);

		this.PRE_execute_query = false;
		this.preBasicIV();

		this.checkOutputFile();
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testSubQuery8() throws Exception {
		this.add_subquery_on_query = true;
		this.notIncludeAggr = true;
		this.GEN_add_sort = false;
		this.TEST_ISEMPTY = true;
		this.genBasicIV();
		this.closeArchiveWriter();

		DataEngineContext deContext2 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext2);

		this.UPDATE_add_filter = 6;

		this.updatePreBasicIV();
		this.closeArchiveReader();
		this.closeArchiveWriter();

		DataEngineContext deContext3 = newContext(DataEngineContext.MODE_PRESENTATION, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext3);

		this.PRE_execute_query = false;
		this.preBasicIV();

		this.checkOutputFile();
	}

	/**
	 * With invalid column
	 *
	 * @throws BirtException
	 */
	@Test
	public void testInvalidColumn() throws Exception {
		try {
			this.GEN_use_invalid_column = true;
			this.genBasicIV();

			fail("Should not arrive here");
		} catch (DataException e) {
			this.closeArchiveWriter();
		}

	}

	/**
	 * Test disable some column binding on diskCache.
	 *
	 * @throws Exception
	 */
	@Test
	public void testDiskCacheColumnBinding() throws Exception {
		String initialStr = System.getProperty("birt.data.engine.test.memcachesize");
		System.setProperty("birt.data.engine.test.memcachesize", "2");
		incomprehensiveColumnBinding();

		if (initialStr == null) {
			System.getProperties().remove("birt.data.engine.test.memcachesize");
		} else {
			System.setProperty("birt.data.engine.test.memcachesize", initialStr);
		}
	}

	/**
	 * Test disable some column binding on MemoryCache.
	 *
	 * @throws Exception
	 */
	@Test
	public void testMemoryCacheColumnBinding() throws Exception {
		incomprehensiveColumnBinding();
	}

	/**
	 *
	 * @throws BirtException
	 * @throws DataException
	 * @throws IOException
	 */
	private void incomprehensiveColumnBinding() throws BirtException, DataException, IOException {
		File file = new File(fileName);
		if (file.exists()) {
			file.delete();
		}
		this.GEN_add_group = true;
		this.GEN_add_subquery = true;
		this.GEN_print = true;
		this.USE_DATE_IN_COLUMNBINDING = false;
		this.genBasicIV();
		this.closeArchiveReader();
		this.closeArchiveWriter();

		DataEngineContext deContext2 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		if (myPreDataEngine != null) {
			myPreDataEngine.shutdown();
			myPreDataEngine.clearCache(dataSource, dataSet);
		}
		deContext2.setTmpdir(this.getTempDir());
		myPreDataEngine = DataEngine.newDataEngine(deContext2);
		this.UPDATE_add_same_group = true;
		this.UPDATE_add_subquery = 1;
		this.updatePreBasicIV();
		this.closeArchiveReader();
		this.closeArchiveWriter();
		myPreDataEngine.shutdown();
		DataEngineContext deContext3 = newContext(DataEngineContext.MODE_PRESENTATION, fileName, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext3);

		this.PRE_execute_query = false;
		this.preBasicIV();

		this.checkOutputFile();
		myPreDataEngine.shutdown();
	}

	@Test
	public void testEmptyResult() throws BirtException {
		try {
			QueryDefinition qd1 = newReportQuery();

			// add basic column binding
			IBaseExpression[] rowBeArray = getRowExpr();
			IBinding[] totalBeArray = getAggrExpr();
			populateColumnBinding(qd1, rowBeArray, totalBeArray);

			qd1.addFilter(new FilterDefinition(new ScriptExpression("false")));
			// generation
			IQueryResults qr1 = myGenDataEngine.prepare(qd1).execute(scope);

			// important step
			GEN_queryResultID = qr1.getID();

			IResultIterator ri1 = qr1.getResultIterator();
			while (ri1.next()) {
				String abc = "";
				for (int i = 0; i < rowExprName.length; i++) {
					abc += ri1.getValue(this.rowExprName[i]) + "  ";
				}

				for (int i = 0; i < totalExprName.length; i++) {
					abc += ri1.getValue(this.totalExprName[i]) + "  ";
				}
			}
			ri1.close();
			myGenDataEngine.shutdown();
			this.closeArchiveWriter();

			DataEngineContext deContext2 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
			myPreDataEngine = DataEngine.newDataEngine(deContext2);

			QueryDefinition qd2 = newReportQuery();

			populateColumnBinding(qd2, rowBeArray, totalBeArray);

			qd2.addFilter(new FilterDefinition(new ScriptExpression("false")));

			SortDefinition sortDefn = new SortDefinition();
			sortDefn.setColumn("CITY_1");
			qd2.addSort(sortDefn);

			qd2.setQueryResultsID(GEN_queryResultID);
			// generation
			IQueryResults qr2 = myPreDataEngine.prepare(qd2).execute(scope);

			IResultIterator ri2 = qr2.getResultIterator();
			while (ri2.next()) {
				String abc = "";
				for (int i = 0; i < rowExprName.length; i++) {
					abc += ri2.getValue(this.rowExprName[i]) + "  ";
				}

				for (int i = 0; i < totalExprName.length; i++) {
					abc += ri2.getValue(this.totalExprName[i]) + "  ";
				}
			}

			ri2.close();
			myPreDataEngine.shutdown();

			this.closeArchiveReader();
			this.closeArchiveWriter();
		} catch (Exception e) {
			fail("should not arrive here");
		}
	}

	/**
	 *
	 * @param qd1
	 * @param rowBeArray
	 * @param totalBeArray
	 * @throws DataException
	 */
	private void populateColumnBinding(QueryDefinition qd1, IBaseExpression[] rowBeArray, IBinding[] totalBeArray)
			throws DataException {
		for (int i = 0; i < rowBeArray.length; i++) {
			qd1.addResultSetExpression(this.rowExprName[i], rowBeArray[i]);
		}

		for (int i = 0; i < totalBeArray.length; i++) {
			qd1.addBinding(totalBeArray[i]);
		}
	}

	/**
	 *
	 * @return
	 * @throws BirtException
	 */
	private List genBasicNestedQuery() throws BirtException {
		QueryDefinition qd1 = newReportQuery();

		// add basic column binding
		IBaseExpression[] rowBeArray = getRowExpr();
		IBinding[] totalBeArray = getAggrExpr();
		populateColumnBinding(qd1, rowBeArray, totalBeArray);

		// generation
		IQueryResults qr1 = myGenDataEngine.prepare(qd1).execute(scope);

		QueryDefinition qd2 = newReportQuery();
		populateColumnBinding(qd2, rowBeArray, totalBeArray);

		ConditionalExpression filterExpr = new ConditionalExpression("row._outer[\"CITY_1\"]",
				IConditionalExpression.OP_EQ, "\"Shanghai\"");
		qd2.addFilter(new FilterDefinition(filterExpr));
		// generation
		IPreparedQuery preparedQuery = myGenDataEngine.prepare(qd2);

		// important step
		GEN_queryResultID = qr1.getID();
		List nestedQueryResultId = new ArrayList();
		IResultIterator ri1 = qr1.getResultIterator();
		while (ri1.next()) {
			String abc = "";
			for (int i = 0; i < rowExprName.length; i++) {
				abc += ri1.getValue(this.rowExprName[i]) + "  ";
			}

			for (int i = 0; i < totalExprName.length; i++) {
				abc += ri1.getValue(this.totalExprName[i]) + "  ";
			}

			this.testPrintln(abc);
			IQueryResults qr = preparedQuery.execute(qr1, scope);
			nestedQueryResultId.add(qr.getID());
			IResultIterator subRi = qr.getResultIterator();

			while (subRi.next()) {
				abc = "      ";
				for (int i = 0; i < rowExprName.length; i++) {
					abc += subRi.getValue(rowExprName[i]) + "  ";
				}
				this.testPrintln(abc);
			}
			subRi.close();
		}

		this.testPrintln("");

		ri1.close();
		qr1.close();
		myGenDataEngine.shutdown();
		this.closeArchiveWriter();
		return nestedQueryResultId;
	}

	/**
	 *
	 * @param ids
	 * @throws BirtException
	 * @throws DataException
	 */
	private void preBasicNestedQuery(List ids) throws BirtException, DataException {
		DataEngineContext deContext2 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext2);

		// add basic column binding
		IBaseExpression[] rowBeArray = getRowExpr();
		IBinding[] totalBeArray = getAggrExpr();
		QueryDefinition qd = new QueryDefinition();
		populateColumnBinding(qd, rowBeArray, totalBeArray);

		qd.setQueryResultsID(this.GEN_queryResultID);

		QueryDefinition qd2 = newReportQuery();

		populateColumnBinding(qd2, rowBeArray, totalBeArray);

		ConditionalExpression filterExpr = new ConditionalExpression("row._outer[\"CITY_1\"]",
				IConditionalExpression.OP_EQ, "\"Shanghai\"");
		qd2.addFilter(new FilterDefinition(filterExpr));

		GroupDefinition gd = new GroupDefinition();
		gd.setKeyColumn("COUNTRY_1");
		qd2.addGroup(gd);

		IQueryResults qr = myPreDataEngine.prepare(qd).execute(null);
		this.UPDATE_queryResultID = qr.getID();

		assertTrue(qr.getResultMetaData() != null);

		IResultIterator ri = qr.getResultIterator();
		ri.next();

		do {
			String abc = "";
			for (int i = 0; i < rowExprName.length; i++) {
				abc += ri.getValue(rowExprName[i]) + "  ";
			}
			for (int i = 0; i < totalExprName.length; i++) {
				abc += ri.getValue(totalExprName[i]) + "  ";
			}

			this.testPrintln(abc + ri.getRowId());
			qd2.setQueryResultsID(ids.get(0).toString());
			ids.remove(0);

			IResultIterator subRi = myPreDataEngine.prepare(qd2).execute(qr, null).getResultIterator();
			while (subRi.next()) {
				abc = "      ";
				for (int i = 0; i < rowExprName.length; i++) {
					abc += subRi.getValue(rowExprName[i]) + "  ";
				}
				this.testPrintln(abc);
			}

		} while (ri.next());

		this.testPrintln("");

		ri.close();
		myPreDataEngine.shutdown();

		this.closeArchiveReader();
		this.closeArchiveWriter();
	}

	/**
	 * With filter
	 *
	 * @throws BirtException
	 */
	@Test
	public void testDummyQuery() throws Exception {
		QueryDefinition qd = new QueryDefinition();
		qd.addResultSetExpression("abc", new ScriptExpression("1"));
		IQueryResults qr = myGenDataEngine.prepare(qd).execute(scope);

		// important step
		GEN_queryResultID = qr.getID();

		IResultIterator ri = qr.getResultIterator();
		while (ri.next()) {
			String abc = "";
			abc += ri.getValue("abc") + "  ";

			this.testPrintln(abc);
		}

		ri.close();
		qr.close();
		myGenDataEngine.shutdown();

		this.closeArchiveWriter();

		DataEngineContext deContext2 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext2);

		// here queryResultID needs to set as the data set

		qd = new QueryDefinition();
		qd.addResultSetExpression("abc", new ScriptExpression("1"));
		qd.addResultSetExpression("def", new ScriptExpression("2"));
		qd.setQueryResultsID(this.GEN_queryResultID);

		qr = myPreDataEngine.prepare(qd).execute(null);
		this.UPDATE_queryResultID = qr.getID();
		ri = qr.getResultIterator();

		while (ri.next()) {
			String abc = "";
			abc += ri.getValue("abc") + "  ";
			abc += ri.getValue("def") + "  ";
			this.testPrintln(abc);
		}

		ri.close();
		this.closeArchiveReader();
		this.closeArchiveWriter();

		DataEngineContext deContext3 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext3);

		qd = new QueryDefinition();
		qd.addResultSetExpression("abc", new ScriptExpression("1"));
		qd.addResultSetExpression("def", new ScriptExpression("2"));
		qd.setQueryResultsID(this.GEN_queryResultID);
		qd.setQueryResultsID(this.UPDATE_queryResultID);

		qr = myPreDataEngine.prepare(qd).execute(null);
		ri = qr.getResultIterator();
		this.UPDATE_queryResultID = qr.getID();
		while (ri.next()) {
			String abc = "";
			abc += ri.getValue("abc") + "  ";
			abc += ri.getValue("def") + "  ";
			this.testPrintln(abc);
		}
		ri.close();
		this.checkOutputFile();
	}

	/**
	 *
	 * @throws BirtException
	 */
	@Test
	public void testDummyQueryWithSubQuery1() throws Exception {
		QueryDefinition qd = new QueryDefinition();
		qd.addResultSetExpression("abc", new ScriptExpression("1"));
		SubqueryDefinition sub = new SubqueryDefinition("sub", qd);
		sub.addResultSetExpression("abc1", new ScriptExpression("1.1"));
		qd.addSubquery(sub);
		IQueryResults qr = myGenDataEngine.prepare(qd).execute(scope);

		// important step
		GEN_queryResultID = qr.getID();

		IResultIterator ri = qr.getResultIterator();
		while (ri.next()) {
			String abc = "";
			abc += ri.getValue("abc") + "  ";
			this.testPrintln(abc);
			IResultIterator subRi = ri.getSecondaryIterator("sub", scope);
			abc = "                   ";
			while (subRi.next()) {
				abc += subRi.getValue("abc1");
				this.testPrintln(abc);
			}
			subRi.close();

		}

		ri.close();
		qr.close();
		myGenDataEngine.shutdown();

		this.closeArchiveWriter();

		DataEngineContext deContext2 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext2);

		// here queryResultID needs to set as the data set

		qd = new QueryDefinition();
		qd.addResultSetExpression("abc", new ScriptExpression("1"));
		qd.addResultSetExpression("def", new ScriptExpression("2"));
		qd.setQueryResultsID(this.GEN_queryResultID);

		sub = new SubqueryDefinition("sub", qd);
		sub.addResultSetExpression("abc1", new ScriptExpression("1.1"));
		sub.addResultSetExpression("def1", new ScriptExpression("2.1"));
		qd.addSubquery(sub);
		qr = myPreDataEngine.prepare(qd).execute(null);
		this.UPDATE_queryResultID = qr.getID();
		ri = qr.getResultIterator();

		while (ri.next()) {
			String abc = "";
			abc += ri.getValue("abc") + "  ";
			abc += ri.getValue("def") + "  ";
			this.testPrintln(abc);
			IResultIterator subRi = ri.getSecondaryIterator("sub", scope);
			abc = "                   ";
			while (subRi.next()) {
				abc += subRi.getValue("abc1");
				abc += subRi.getValue("def1");
				this.testPrintln(abc);
			}
			subRi.close();
		}
		ri.close();
		this.closeArchiveReader();
		this.closeArchiveWriter();

		DataEngineContext deContext3 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext3);

		qd = new QueryDefinition();
		qd.addResultSetExpression("abc", new ScriptExpression("1"));
		qd.addResultSetExpression("def", new ScriptExpression("2"));
		qd.setQueryResultsID(this.GEN_queryResultID);
		qd.setQueryResultsID(this.UPDATE_queryResultID);

		sub = new SubqueryDefinition("sub", qd);
		sub.addResultSetExpression("abc1", new ScriptExpression("1.1"));
		sub.addResultSetExpression("def1", new ScriptExpression("2.1"));

		qd.addSubquery(sub);
		qr = myPreDataEngine.prepare(qd).execute(null);
		ri = qr.getResultIterator();
		this.UPDATE_queryResultID = qr.getID();
		while (ri.next()) {
			String abc = "";
			abc += ri.getValue("abc") + "  ";
			abc += ri.getValue("def") + "  ";
			this.testPrintln(abc);
			IResultIterator subRi = ri.getSecondaryIterator("sub", scope);
			abc = "                   ";
			while (subRi.next()) {
				abc += subRi.getValue("abc1");
				abc += subRi.getValue("def1");
				this.testPrintln(abc);
			}
			subRi.close();
		}
		ri.close();
		this.checkOutputFile();
	}

	/**
	 *
	 * @throws BirtException
	 */
	@Test
	public void testDummyQueryWithSubQuery2() throws Exception {
		QueryDefinition qd = new QueryDefinition();
		qd.addResultSetExpression("abc", new ScriptExpression("1"));
		SubqueryDefinition sub = new SubqueryDefinition("sub", qd);
		sub.addResultSetExpression("abc1", new ScriptExpression("1.1"));
		qd.addSubquery(sub);
		IQueryResults qr = myGenDataEngine.prepare(qd).execute(scope);

		// important step
		GEN_queryResultID = qr.getID();

		IResultIterator ri = qr.getResultIterator();
		while (ri.next()) {
			String abc = "";
			abc += ri.getValue("abc") + "  ";
			this.testPrintln(abc);
			IResultIterator subRi = ri.getSecondaryIterator("sub", scope);
			abc = "                   ";
			while (subRi.next()) {
				abc += subRi.getValue("abc1");
				this.testPrintln(abc);
			}
			subRi.close();

		}

		ri.close();
		qr.close();
		myGenDataEngine.shutdown();

		this.closeArchiveWriter();

		DataEngineContext deContext2 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext2);

		// here queryResultID needs to set as the data set

		qd = new QueryDefinition();
		qd.addResultSetExpression("abc", new ScriptExpression("1"));
		qd.addResultSetExpression("def", new ScriptExpression("2"));
		qd.setQueryResultsID(this.GEN_queryResultID);

		sub = new SubqueryDefinition("sub", qd);
		sub.addResultSetExpression("abc1", new ScriptExpression("1.1"));

		qd.addSubquery(sub);
		qr = myPreDataEngine.prepare(qd).execute(null);
		this.UPDATE_queryResultID = qr.getID();
		ri = qr.getResultIterator();

		while (ri.next()) {
			String abc = "";
			abc += ri.getValue("abc") + "  ";
			abc += ri.getValue("def") + "  ";
			this.testPrintln(abc);
			IResultIterator subRi = ri.getSecondaryIterator("sub", scope);
			abc = "                   ";
			while (subRi.next()) {
				abc += subRi.getValue("abc1");

				this.testPrintln(abc);
			}
			subRi.close();
		}
		ri.close();
		this.closeArchiveReader();
		this.closeArchiveWriter();

		DataEngineContext deContext3 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext3);

		qd = new QueryDefinition();
		qd.addResultSetExpression("abc", new ScriptExpression("1"));
		qd.addResultSetExpression("def", new ScriptExpression("2"));
		qd.setQueryResultsID(this.GEN_queryResultID);
		qd.setQueryResultsID(this.UPDATE_queryResultID);

		sub = new SubqueryDefinition("sub", qd);
		sub.addResultSetExpression("abc1", new ScriptExpression("1.1"));

		qd.addSubquery(sub);
		qr = myPreDataEngine.prepare(qd).execute(null);
		ri = qr.getResultIterator();
		this.UPDATE_queryResultID = qr.getID();
		while (ri.next()) {
			String abc = "";
			abc += ri.getValue("abc") + "  ";
			abc += ri.getValue("def") + "  ";
			this.testPrintln(abc);
			IResultIterator subRi = ri.getSecondaryIterator("sub", scope);
			abc = "                   ";
			while (subRi.next()) {
				abc += subRi.getValue("abc1");

				this.testPrintln(abc);
			}
			subRi.close();
		}
		ri.close();
		this.checkOutputFile();
	}

	/**
	 *
	 * @throws BirtException
	 */
	@Test
	public void testDummyQueryWithSubQuery3() throws Exception {
		QueryDefinition qd = new QueryDefinition();
		qd.addResultSetExpression("abc", new ScriptExpression("1"));
		SubqueryDefinition sub = new SubqueryDefinition("sub", qd);
		sub.addResultSetExpression("abc1", new ScriptExpression("1.1"));
		qd.addSubquery(sub);
		IQueryResults qr = myGenDataEngine.prepare(qd).execute(scope);

		// important step
		GEN_queryResultID = qr.getID();

		IResultIterator ri = qr.getResultIterator();
		while (ri.next()) {
			String abc = "";
			abc += ri.getValue("abc") + "  ";
			this.testPrintln(abc);
			IResultIterator subRi = ri.getSecondaryIterator("sub", scope);
			abc = "                   ";
			while (subRi.next()) {
				abc += subRi.getValue("abc1");
				this.testPrintln(abc);
			}
			subRi.close();
		}

		ri.close();
		qr.close();
		myGenDataEngine.shutdown();

		this.closeArchiveWriter();

		DataEngineContext deContext2 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext2);

		// here queryResultID needs to set as the data set

		qd = new QueryDefinition();
		qd.addResultSetExpression("abc", new ScriptExpression("1"));

		qd.setQueryResultsID(this.GEN_queryResultID);

		sub = new SubqueryDefinition("sub", qd);
		sub.addResultSetExpression("abc1", new ScriptExpression("1.1"));
		sub.addResultSetExpression("def1", new ScriptExpression("2.1"));
		qd.addSubquery(sub);
		qr = myPreDataEngine.prepare(qd).execute(null);
		this.UPDATE_queryResultID = qr.getID();
		ri = qr.getResultIterator();

		while (ri.next()) {
			String abc = "";
			abc += ri.getValue("abc") + "  ";

			this.testPrintln(abc);
			IResultIterator subRi = ri.getSecondaryIterator("sub", scope);
			abc = "                   ";
			while (subRi.next()) {
				abc += subRi.getValue("abc1");
				abc += subRi.getValue("def1");
				this.testPrintln(abc);
			}
			subRi.close();
		}
		ri.close();
		this.closeArchiveReader();
		this.closeArchiveWriter();

		DataEngineContext deContext3 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext3);

		qd = new QueryDefinition();
		qd.addResultSetExpression("abc", new ScriptExpression("1"));

		qd.setQueryResultsID(this.GEN_queryResultID);
		qd.setQueryResultsID(this.UPDATE_queryResultID);

		sub = new SubqueryDefinition("sub", qd);
		sub.addResultSetExpression("abc1", new ScriptExpression("1.1"));
		sub.addResultSetExpression("def1", new ScriptExpression("2.1"));

		qd.addSubquery(sub);
		qr = myPreDataEngine.prepare(qd).execute(null);
		ri = qr.getResultIterator();
		this.UPDATE_queryResultID = qr.getID();
		while (ri.next()) {
			String abc = "";
			abc += ri.getValue("abc") + "  ";

			this.testPrintln(abc);
			IResultIterator subRi = ri.getSecondaryIterator("sub", scope);
			abc = "                   ";
			while (subRi.next()) {
				abc += subRi.getValue("abc1");
				abc += subRi.getValue("def1");
				this.testPrintln(abc);
			}
			subRi.close();
		}
		ri.close();
		this.checkOutputFile();
	}

	/**
	 *
	 * @throws BirtException
	 */
	@Test
	public void testDummyQueryWithSubQuery4() throws Exception {
		QueryDefinition qd = new QueryDefinition();
		qd.addResultSetExpression("abc", new ScriptExpression("1"));
		SubqueryDefinition sub = new SubqueryDefinition("sub", qd);
		sub.addResultSetExpression("abc1", new ScriptExpression("1.1"));
		qd.addSubquery(sub);
		IQueryResults qr = myGenDataEngine.prepare(qd).execute(scope);

		// important step
		GEN_queryResultID = qr.getID();

		IResultIterator ri = qr.getResultIterator();
		while (ri.next()) {
			String abc = "";
			abc += ri.getValue("abc") + "  ";
			this.testPrintln(abc);
			IResultIterator subRi = ri.getSecondaryIterator("sub", scope);
			abc = "                   ";
			while (subRi.next()) {
				abc += subRi.getValue("abc1");
				this.testPrintln(abc);
			}
			subRi.close();
		}

		ri.close();
		qr.close();
		myGenDataEngine.shutdown();

		this.closeArchiveWriter();

		DataEngineContext deContext2 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext2);

		// here queryResultID needs to set as the data set

		qd = new QueryDefinition();
		qd.addResultSetExpression("abc", new ScriptExpression("1"));

		qd.setQueryResultsID(this.GEN_queryResultID);

		sub = new SubqueryDefinition("sub", qd);
		sub.addResultSetExpression("abc1", new ScriptExpression("1.1"));
		sub.addResultSetExpression("def1", new ScriptExpression("2.1"));
		qd.addSubquery(sub);
		qr = myPreDataEngine.prepare(qd).execute(null);
		this.UPDATE_queryResultID = qr.getID();
		ri = qr.getResultIterator();

		while (ri.next()) {
			String abc = "";
			abc += ri.getValue("abc") + "  ";

			this.testPrintln(abc);
			IResultIterator subRi = ri.getSecondaryIterator("sub", scope);
			abc = "                   ";
			while (subRi.next()) {
				abc += subRi.getValue("abc1");
				abc += subRi.getValue("def1");
				this.testPrintln(abc);
			}
			subRi.close();
		}
		ri.close();
		this.closeArchiveReader();
		this.closeArchiveWriter();

		DataEngineContext deContext3 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext3);

		// here queryResultID needs to set as the data set

		qd = new QueryDefinition();
		qd.addResultSetExpression("abc", new ScriptExpression("1"));

		qd.setQueryResultsID(this.GEN_queryResultID);

		sub = new SubqueryDefinition("sub", qd);
		sub.addResultSetExpression("abc1", new ScriptExpression("1.1"));
		sub.addResultSetExpression("def1", new ScriptExpression("2.1"));
		sub.addResultSetExpression("ghi1", new ScriptExpression("3.1"));
		qd.addSubquery(sub);
		qr = myPreDataEngine.prepare(qd).execute(null);
		this.UPDATE_queryResultID = qr.getID();
		ri = qr.getResultIterator();

		while (ri.next()) {
			String abc = "";
			abc += ri.getValue("abc") + "  ";

			this.testPrintln(abc);
			IResultIterator subRi = ri.getSecondaryIterator("sub", scope);
			abc = "                   ";
			while (subRi.next()) {
				abc += subRi.getValue("abc1");
				abc += subRi.getValue("def1");
				abc += subRi.getValue("ghi1");
				this.testPrintln(abc);
			}
			subRi.close();
		}
		ri.close();
		this.closeArchiveReader();
		this.closeArchiveWriter();

		DataEngineContext deContext4 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext4);

		qd = new QueryDefinition();
		qd.addResultSetExpression("abc", new ScriptExpression("1"));

		qd.setQueryResultsID(this.GEN_queryResultID);
		qd.setQueryResultsID(this.UPDATE_queryResultID);

		sub = new SubqueryDefinition("sub", qd);
		sub.addResultSetExpression("abc1", new ScriptExpression("1.1"));
		sub.addResultSetExpression("def1", new ScriptExpression("2.1"));
		sub.addResultSetExpression("ghi1", new ScriptExpression("3.1"));

		qd.addSubquery(sub);
		qr = myPreDataEngine.prepare(qd).execute(null);
		ri = qr.getResultIterator();
		this.UPDATE_queryResultID = qr.getID();
		while (ri.next()) {
			String abc = "";
			abc += ri.getValue("abc") + "  ";

			this.testPrintln(abc);
			IResultIterator subRi = ri.getSecondaryIterator("sub", scope);
			abc = "                   ";
			while (subRi.next()) {
				abc += subRi.getValue("abc1");
				abc += subRi.getValue("def1");
				abc += subRi.getValue("ghi1");
				this.testPrintln(abc);
			}
			subRi.close();
		}
		ri.close();
		this.checkOutputFile();
	}

	/**
	 *
	 * @return
	 * @throws BirtException
	 */
	private List genComplexNestedQuery() throws BirtException {
		QueryDefinition qd1 = newReportQuery();

		// add basic column binding
		IBaseExpression[] rowBeArray = getRowExpr();
		IBinding[] totalBeArray = getAggrExpr();
		populateColumnBinding(qd1, rowBeArray, totalBeArray);

		// generation
		IQueryResults qr1 = myGenDataEngine.prepare(qd1).execute(scope);

		QueryDefinition qd2 = newReportQuery();

		populateColumnBinding(qd2, rowBeArray, totalBeArray);

		ConditionalExpression filterExpr = new ConditionalExpression("row._outer[\"CITY_1\"]",
				IConditionalExpression.OP_EQ, "\"Shanghai\"");
		qd2.addFilter(new FilterDefinition(filterExpr));
		// generation
		IPreparedQuery preparedQuery2 = myGenDataEngine.prepare(qd2);

		QueryDefinition qd3 = newReportQuery();

		populateColumnBinding(qd3, rowBeArray, totalBeArray);

		ConditionalExpression filterExpr1 = new ConditionalExpression("row._outer[\"CITY_1\"]",
				IConditionalExpression.OP_EQ, "\"Beijing\"");
		qd3.addFilter(new FilterDefinition(filterExpr1));
		// generation
		IPreparedQuery preparedQuery3 = myGenDataEngine.prepare(qd3);

		// important step
		GEN_queryResultID = qr1.getID();
		List nestedQueryResultId = new ArrayList();
		IResultIterator ri1 = qr1.getResultIterator();
		while (ri1.next()) {
			String abc = "";
			for (int i = 0; i < rowExprName.length; i++) {
				abc += ri1.getValue(this.rowExprName[i]) + "  ";
			}

			for (int i = 0; i < totalExprName.length; i++) {
				abc += ri1.getValue(this.totalExprName[i]) + "  ";
			}

			this.testPrintln(abc);
			IQueryResults qr2 = preparedQuery2.execute(qr1, scope);
			nestedQueryResultId.add(qr2.getID());
			IResultIterator subRi1 = qr2.getResultIterator();

			while (subRi1.next()) {
				abc = "      ";
				for (int i = 0; i < rowExprName.length; i++) {
					abc += subRi1.getValue(rowExprName[i]) + "  ";
				}
				this.testPrintln(abc);

				IQueryResults qr3 = preparedQuery3.execute(qr2, scope);
				nestedQueryResultId.add(qr3.getID());
				IResultIterator subRi2 = qr3.getResultIterator();

				while (subRi2.next()) {
					abc = "      		";
					for (int i = 0; i < rowExprName.length; i++) {
						abc += subRi2.getValue(rowExprName[i]) + "  ";
					}
					this.testPrintln(abc);

				}
				subRi2.close();
			}
			subRi1.close();
		}

		this.testPrintln("");

		ri1.close();
		qr1.close();
		myGenDataEngine.shutdown();
		this.closeArchiveWriter();
		return nestedQueryResultId;
	}

	/**
	 *
	 * @param ids
	 * @throws BirtException
	 * @throws DataException
	 */
	private void preComplexNestedQuery(List ids) throws BirtException, DataException {
		DataEngineContext deContext2 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName);
		deContext2.setTmpdir(this.getTempDir());
		myPreDataEngine = DataEngine.newDataEngine(deContext2);

		// add basic column binding
		IBaseExpression[] rowBeArray = getRowExpr();
		IBinding[] totalBeArray = getAggrExpr();
		QueryDefinition qd = new QueryDefinition();

		populateColumnBinding(qd, rowBeArray, totalBeArray);

		qd.setQueryResultsID(this.GEN_queryResultID);

		QueryDefinition qd2 = newReportQuery();

		populateColumnBinding(qd2, rowBeArray, totalBeArray);

		ConditionalExpression filterExpr = new ConditionalExpression("row._outer[\"CITY_1\"]",
				IConditionalExpression.OP_EQ, "\"Shanghai\"");
		qd2.addFilter(new FilterDefinition(filterExpr));

		// generation
		QueryDefinition qd3 = newReportQuery();

		populateColumnBinding(qd3, rowBeArray, totalBeArray);

		ConditionalExpression filterExpr1 = new ConditionalExpression("row._outer[\"CITY_1\"]",
				IConditionalExpression.OP_EQ, "\"Beijing\"");
		qd3.addFilter(new FilterDefinition(filterExpr1));

		GroupDefinition gd = new GroupDefinition();
		gd.setKeyColumn("COUNTRY_1");
		qd3.addGroup(gd);

		IQueryResults qr = myPreDataEngine.prepare(qd).execute(null);
		this.UPDATE_queryResultID = qr.getID();

		assertTrue(qr.getResultMetaData() != null);

		IResultIterator ri = qr.getResultIterator();
		ri.next();

		do {
			String abc = "";
			for (int i = 0; i < rowExprName.length; i++) {
				abc += ri.getValue(rowExprName[i]) + "  ";
			}
			for (int i = 0; i < totalExprName.length; i++) {
				abc += ri.getValue(totalExprName[i]) + "  ";
			}

			this.testPrintln(abc + ri.getRowId());

			qd2.setQueryResultsID(ids.get(0).toString());
			ids.remove(0);

			IQueryResults qr2 = myPreDataEngine.prepare(qd2).execute(qr, null);
			IResultIterator subRi1 = qr2.getResultIterator();
			while (subRi1.next()) {
				abc = "      ";
				for (int i = 0; i < rowExprName.length; i++) {
					abc += subRi1.getValue(rowExprName[i]) + "  ";
				}
				this.testPrintln(abc);

				qd3.setQueryResultsID(ids.get(0).toString());
				ids.remove(0);

				IResultIterator subRi2 = myPreDataEngine.prepare(qd3).execute(qr2, null).getResultIterator();
				while (subRi2.next()) {
					abc = "      		";
					for (int i = 0; i < rowExprName.length; i++) {
						abc += subRi2.getValue(rowExprName[i]) + "  ";
					}
					this.testPrintln(abc);
				}
			}

		} while (ri.next());

		this.testPrintln("");

		ri.close();
		qr.close();

		myPreDataEngine.shutdown();

		this.closeArchiveReader();
		this.closeArchiveWriter();
	}

	/**
	 * @throws BirtException
	 */
	private void genBasicIV() throws BirtException {

		QueryDefinition qd = newGenIVReportQuery();
		if (!this.usesDetails) {
			qd.setUsesDetails(false);
		}
		// generation
		IQueryResults qr = myGenDataEngine.prepare(qd).execute(scope);

		// important step
		GEN_queryResultID = qr.getID();

		IResultIterator ri = qr.getResultIterator();
		while (ri.next()) {
			String abc = "";
			for (int i = 0; i < rowExprName.length; i++) {
				abc += ri.getValue(this.rowExprName[i]) + "  ";
			}

			if (!this.notIncludeAggr) {
				for (int i = 0; i < totalExprName.length; i++) {
					abc += ri.getValue(this.totalExprName[i]) + "  ";
				}
			}
			if (this.GEN_print) {
				this.testPrintln(abc);
			}

			if (this.GEN_add_subquery) {
				IResultIterator subRi = ri.getSecondaryIterator(subQueryName1, scope);
				while (subRi.next()) {
					abc = "      ";
					for (int i = 0; i < subRowExprName1.length; i++) {
						abc += subRi.getValue(subRowExprName1[i]) + "  ";
					}

					if (this.USE_DATE_IN_SUBQUERY) {
						abc += subRi.getValue("sub4");
					}
				}
				subRi.close();
			}
		}

		if (this.add_subquery_on_query) {
			IResultIterator subRi = ri.getSecondaryIterator(subQueryName1, scope);
			String abc = "      ";
			for (int i = 0; i < subRowExprName1.length; i++) {
				abc += subRi.getValue(subRowExprName1[i]) + "  ";
			}
			if (this.GEN_print) {
				this.testPrintln(abc);
			}
			while (subRi.next()) {
				abc = "      ";
				for (int i = 0; i < subRowExprName1.length; i++) {
					abc += subRi.getValue(subRowExprName1[i]) + "  ";
				}
				this.testPrintln(abc);
			}
		}

		if (this.GEN_print) {
			this.testPrintln("");
		}

		ri.close();
		qr.close();
		myGenDataEngine.shutdown();
		myGenDataEngine.clearCache(dataSource, dataSet);
	}

	/**
	 * @return
	 * @throws DataException
	 */
	private QueryDefinition newGenIVReportQuery() throws DataException {
		QueryDefinition qd = newReportQuery();

		// add basic column binding
		IBaseExpression[] rowBeArray = getRowExpr();
		IBinding[] totalBeArray = getAggrExpr();
		for (int i = 0; i < rowBeArray.length; i++) {
			qd.addResultSetExpression(this.rowExprName[i], rowBeArray[i]);
		}
		if (this.GEN_use_invalid_column) {
			qd.addResultSetExpression("abc", new ScriptExpression("dataSetRow.def"));
		}
		if (!this.notIncludeAggr) {
			for (int i = 0; i < totalBeArray.length; i++) {
				qd.addBinding(totalBeArray[i]);
			}
		}
		// add filter
		if (this.GEN_add_filter) {
			ConditionalExpression filterExpr = new ConditionalExpression("row.AMOUNT_1", IConditionalExpression.OP_GT,
					"50");
			FilterDefinition filterDefn = new FilterDefinition(filterExpr);
			qd.addFilter(filterDefn);

			this.GEN_filterDefn.add(filterDefn);
		}

		// add TopN filter
		if (this.GEN_add_topN_filter) {
			ConditionalExpression filterExpr = new ConditionalExpression("row.AMOUNT_1",
					IConditionalExpression.OP_TOP_N, "5");
			FilterDefinition filterDefn = new FilterDefinition(filterExpr);
			qd.addFilter(filterDefn);
			filterDefn.setFilterTarget(FilterTarget.DATASET);

			this.GEN_filterDefn.add(filterDefn);
		}

		if (this.TEST_ISEMPTY) {
			ConditionalExpression filterExpr = new ConditionalExpression("row.AMOUNT_1 - row.AMOUNT_1",
					IConditionalExpression.OP_GT, "50");
			FilterDefinition filterDefn = new FilterDefinition(filterExpr);
			qd.addFilter(filterDefn);
			this.GEN_filterDefn.add(filterDefn);
		}

		if (this.GEN_add_sort)
		// add sorting
		{
			SortDefinition sortDefn = new SortDefinition();
			sortDefn.setColumn("CITY_1");
			qd.addSort(sortDefn);
		}

		// add group
		if (this.GEN_add_group) {
			// add grouping on column1
			GroupDefinition gd = new GroupDefinition();
			gd.setKeyColumn("COUNTRY_1");
			qd.addGroup(gd);

			if (this.GEN_add_subquery) {
				SubqueryDefinition subqueryDefn = getSubQueryDefn(qd);

				gd.addSubquery(subqueryDefn);
			}
		}

		if (this.GEN_add_group1) {
			// add grouping on column1
			GroupDefinition gd = new GroupDefinition();
			gd.setKeyColumn("CITY_1");
			qd.addGroup(gd);

			gd = new GroupDefinition();
			gd.setKeyColumn("AMOUNT_1");
			qd.addGroup(gd);
		}
		if (add_subquery_on_query) {
			qd.addSubquery(getSubQueryDefn(qd));
		}

		return qd;
	}

	private SubqueryDefinition getSubQueryDefn(QueryDefinition qd) {
		SubqueryDefinition subqueryDefn = new SubqueryDefinition(subQueryName1, qd);

		subRowExprName1 = new String[3];
		subRowExprName1[0] = "sub1";
		subRowExprName1[1] = "sub2";
		subRowExprName1[2] = "sub3";
		ScriptExpression[] exprs = new ScriptExpression[3];
		exprs[0] = new ScriptExpression("dataSetRow.COUNTRY");
		exprs[1] = new ScriptExpression("dataSetRow.CITY");
		exprs[2] = new ScriptExpression("dataSetRow.AMOUNT");
		for (int i = 0; i < subRowExprName1.length; i++) {
			subqueryDefn.addResultSetExpression(subRowExprName1[i], exprs[i]);
		}
		if (this.USE_DATE_IN_SUBQUERY) {
			subqueryDefn.addResultSetExpression("sub4", new ScriptExpression("dataSetRow.SALE_DATE"));
		}
		subqueryDefn.setApplyOnGroupFlag(true);
		return subqueryDefn;
	}

	/**
	 * @throws BirtException
	 */
	private void updatePreBasicIV() throws BirtException {
		// here queryResultID needs to set as the data set
		int groupNeeded = -1;
		if (UPDATE_add_diff_group) {
			groupNeeded = 1;
		} else if (this.UPDATE_add_same_group) {
			groupNeeded = 0;
		}

		QueryDefinition qd = newPreIVReportQuery(this.UPDATE_add_filter, this.UPDATE_add_sort, groupNeeded, UPDATE);
		if (!this.usesDetails) {
			qd.setUsesDetails(false);
		}
		qd.setQueryResultsID(this.GEN_queryResultID);

		IQueryResults qr = myPreDataEngine.prepare(qd).execute(null);
		this.UPDATE_queryResultID = qr.getID();

		displayPreResult(qr, false, false);
	}

	/**
	 * @throws BirtException
	 */
	private void preBasicIV() throws BirtException {
		IQueryResults qr = null;

		if (this.PRE_execute_query) {
			// here queryResultID needs to set as the data set
			QueryDefinition qd = newPreIVReportQuery(this.PRE_add_filter, this.PRE_add_sort, this.PRE_add_group,
					PRESENTATION);
			if (!this.usesDetails) {
				qd.setUsesDetails(false);
			}
			qd.setQueryResultsID(this.UPDATE_queryResultID);

			qr = myPreDataEngine.prepare(qd).execute(null);
			this.UPDATE_queryResultID = qr.getID();
		} else {
			qr = myPreDataEngine.getQueryResults(this.UPDATE_queryResultID);
		}

		displayPreResult(qr, this.PRE_print_groupinfo, this.PRE_use_skipto);
	}

	/**
	 * @throws BirtException
	 */
	private void displayPreResult(IQueryResults qr, boolean printGroupInfo, boolean useSkipTo) throws BirtException {
		assertTrue(qr.getResultMetaData() != null);

		IResultIterator ri = qr.getResultIterator();
		if (useSkipTo) {
			if (this.PRE_use_skipto_num == -1) {
				ri.moveTo(0);
			} else {
				ri.moveTo(this.PRE_use_skipto_num);
			}
		} else {
			ri.next();
		}

		do {
			String abc = "";
			for (int i = 0; i < rowExprName.length; i++) {
				abc += ri.getValue(rowExprName[i]) + "  ";
			}
			if (!this.notIncludeAggr) {
				for (int i = 0; i < totalExprName.length; i++) {
					abc += ri.getValue(totalExprName[i]) + "  ";
				}
			}
			if (this.updateNewBindingName != null && this.updateNewBindingExpr != null) {
				abc += ri.getValue(this.updateNewBindingName) + " ";
			}
			if (printGroupInfo) {
				abc += ri.getStartingGroupLevel() + " ";
			}
			this.testPrintln(abc + ri.getRowId()
					+ (this.GEN_add_group1 ? (" " + ri.getStartingGroupLevel() + ":" + ri.getEndingGroupLevel()) : ""));

			if (this.UPDATE_add_subquery == 1) {
				IResultIterator subRi = ri.getSecondaryIterator(subQueryName1, scope);
				while (subRi.next()) {
					abc = "      ";
					for (int i = 0; i < subRowExprName1.length; i++) {
						abc += subRi.getValue(subRowExprName1[i]) + "  ";
					}

					if (this.USE_DATE_IN_SUBQUERY) {
						abc += subRi.getValue("sub4");
					}

					this.testPrintln(abc);
				}
			}

			if (this.UPDATE_add_subquery == 2) {
				IResultIterator subRi = ri.getSecondaryIterator(subQueryName2, scope);
				while (subRi.next()) {
					abc = "      ";
					for (int i = 0; i < subRowExprName2.length; i++) {
						abc += subRi.getValue(subRowExprName2[i]) + "  ";
					}
					this.testPrintln(abc);
				}
			}

			if (this.add_subquery_on_query) {
				IResultIterator subRi = ri.getSecondaryIterator(subQueryName1, scope);
				abc = "      ";
				for (int i = 0; i < subRowExprName1.length; i++) {
					abc += subRi.getValue(subRowExprName1[i]) + "  ";
				}
				this.testPrintln(abc);
				while (subRi.next()) {
					abc = "      ";
					for (int i = 0; i < subRowExprName1.length; i++) {
						abc += subRi.getValue(subRowExprName1[i]) + "  ";
					}
					this.testPrintln(abc);
				}
			}
		} while (ri.next());

		// Test implementation of API IResultIterator.isEmpty().
		if (this.TEST_ISEMPTY) {
			assertTrue(ri.isEmpty());
		} else {
			assertFalse(ri.isEmpty());
		}

		this.testPrintln("");

		ri.close();
		myPreDataEngine.shutdown();
	}

	/**
	 * IV request is applied here
	 *
	 * @return query definition for interactive viewing
	 * @throws DataException
	 */
	private QueryDefinition newPreIVReportQuery(int filterNeeded, boolean sortNeeded, int groupNeeded, int mode)
			throws DataException {
		QueryDefinition qd = new QueryDefinition();
		qd.setDataSetName("Dummy");
		// add basic column binding
		IBaseExpression[] rowBeArray = getRowExpr();
		IBinding[] totalBeArray = getAggrExpr();
		for (int i = 0; i < rowBeArray.length; i++) {
			qd.addResultSetExpression(this.rowExprName[i], rowBeArray[i]);
		}

		for (int i = 0; i < totalBeArray.length; i++) {
			qd.addBinding(totalBeArray[i]);
		}

		if (this.updateNewBindingName != null && this.updateNewBindingName.trim().length() > 0) {
			qd.addResultSetExpression(this.updateNewBindingName, this.updateNewBindingExpr);
		}

		if (mode == UPDATE) {
			qd.getFilters().addAll(this.GEN_filterDefn);
		} else if (mode == PRESENTATION) {
			if (!PRE_basedon_genfilter) {
				qd.getFilters().addAll(UPDATE_filterDefn);
			} else {
				qd.getFilters().addAll(GEN_filterDefn);
			}
		}

		if (filterNeeded == 0) {
			// do filtering on column 4
			ScriptExpression filterExpr = new ScriptExpression("row.AMOUNT_1>200");
			FilterDefinition fd = new FilterDefinition(filterExpr);
			qd.addFilter(fd);
		}

		if (filterNeeded == 1) {
			ScriptExpression filterExpr = new ScriptExpression("row.AMOUNT_1>50");
			FilterDefinition fd = new FilterDefinition(filterExpr);
			qd.addFilter(fd);

			filterExpr = new ScriptExpression("row.AMOUNT_1<7000");
			fd = new FilterDefinition(filterExpr);
			qd.addFilter(fd);
		}

		if (filterNeeded == 2) {
			ScriptExpression filterExpr = new ScriptExpression("row.AMOUNT_1>50");
			FilterDefinition fd = new FilterDefinition(filterExpr);
			qd.addFilter(fd);

			filterExpr = new ScriptExpression("row.AMOUNT_1<700");
			fd = new FilterDefinition(filterExpr);
			qd.addFilter(fd);
		}

		if (filterNeeded == 3) {
			ScriptExpression filterExpr = new ScriptExpression("row.AMOUNT_1>200");
			FilterDefinition fd = new FilterDefinition(filterExpr);
			qd.addFilter(fd);

			filterExpr = new ScriptExpression("row.COUNTRY_1==\"US\"");
			fd = new FilterDefinition(filterExpr);
			qd.addFilter(fd);
		}
		// filter on date
		if (filterNeeded == 4) {
			BaseExpression filterExpr = new ConditionalExpression("row.SALE_NAME_1", IConditionalExpression.OP_EQ,
					"\"2004-01-01 00:00:00+" + TimeZone.getDefault().getRawOffset() / 3600000 + "\"");
			FilterDefinition fd = new FilterDefinition(filterExpr);
			qd.addFilter(fd);
		}

		// TopN filter
		if (filterNeeded == 5) {
			ConditionalExpression filterExpr = new ConditionalExpression("row.AMOUNT_1",
					IConditionalExpression.OP_TOP_N, "3");
			FilterDefinition fd = new FilterDefinition(filterExpr);
			qd.addFilter(fd);
		}

		if (filterNeeded == 6) {
			// do filtering on column 4
			ScriptExpression filterExpr = new ScriptExpression("row.COUNTRY_1 == \"ABC\"");
			FilterDefinition fd = new FilterDefinition(filterExpr);
			qd.addFilter(fd);
		}

		// bottom N filter
		if (filterNeeded == 7) {
			ConditionalExpression filterExpr = new ConditionalExpression("row.AMOUNT_1",
					IConditionalExpression.OP_BOTTOM_N, "1");
			FilterDefinition fd = new FilterDefinition(filterExpr);
			fd.setFilterTarget(FilterTarget.RESULTSET);
			qd.addFilter(fd);
		}

		UPDATE_filterDefn = qd.getFilters();

		if (sortNeeded) {
			// do sorting on column 4
			SortDefinition sd = new SortDefinition();
			sd.setExpression("row.AMOUNT_1");
			sd.setSortDirection(ISortDefinition.SORT_ASC);
			qd.addSort(sd);
		}

		if (groupNeeded == 1 || groupNeeded == 0) {
			GroupDefinition gd = null;
			if (groupNeeded == 1) {
				gd = new GroupDefinition("Agroup");
				gd.setKeyColumn("AMOUNT_1");
				qd.addGroup(gd);
			} else if (groupNeeded == 0) {
				// add grouping on column1
				gd = new GroupDefinition();
				gd.setKeyColumn("COUNTRY_1");
				qd.addGroup(gd);
			}
			if (this.UPDATE_add_subquery == 1) {
				SubqueryDefinition subqueryDefn = getSubQueryDefn(qd);

				gd.addSubquery(subqueryDefn);
			}

			if (this.UPDATE_add_subquery == 2) {
				SubqueryDefinition subqueryDefn = new SubqueryDefinition(subQueryName2, qd);

				subRowExprName2 = new String[2];
				subRowExprName2[0] = "sub1";
				subRowExprName2[1] = "sub2";
				ScriptExpression[] exprs = new ScriptExpression[2];
				exprs[0] = new ScriptExpression("dataSetRow.COUNTRY");
				exprs[1] = new ScriptExpression("dataSetRow.CITY");
				for (int i = 0; i < subRowExprName2.length; i++) {
					subqueryDefn.addResultSetExpression(subRowExprName2[i], exprs[i]);
				}
				subqueryDefn.setApplyOnGroupFlag(true);

				gd.addSubquery(subqueryDefn);
			}
		}

		if (add_subquery_on_query) {
			qd.addSubquery(getSubQueryDefn(qd));
		}

		return qd;
	}

	/**
	 * @return row expression array
	 */
	private IBaseExpression[] getRowExpr() {
		// row test
		int num;
		IBaseExpression[] rowBeArray;
		if (this.USE_DATE_IN_COLUMNBINDING) {
			num = 4;
			rowBeArray = new IBaseExpression[num];
			rowBeArray[0] = new ScriptExpression("dataSetRow.COUNTRY");
			rowBeArray[1] = new ScriptExpression("dataSetRow.CITY");
			rowBeArray[2] = new ScriptExpression("dataSetRow.SALE_DATE");
			rowBeArray[3] = new ScriptExpression("dataSetRow.AMOUNT");

			this.rowExprName = new String[rowBeArray.length];
			this.rowExprName[0] = "COUNTRY_1";
			this.rowExprName[1] = "CITY_1";
			this.rowExprName[2] = "SALE_NAME_1";
			this.rowExprName[3] = "AMOUNT_1";
		} else {
			num = 3;
			rowBeArray = new IBaseExpression[num];
			rowBeArray[0] = new ScriptExpression("dataSetRow.COUNTRY");
			rowBeArray[1] = new ScriptExpression("dataSetRow.CITY");
			rowBeArray[2] = new ScriptExpression("dataSetRow.AMOUNT");

			this.rowExprName = new String[rowBeArray.length];
			this.rowExprName[0] = "COUNTRY_1";
			this.rowExprName[1] = "CITY_1";
			this.rowExprName[2] = "AMOUNT_1";
		}
		return rowBeArray;
	}

	/**
	 * @return aggregation expression array
	 * @throws DataException
	 */
	private IBinding[] getAggrExpr() throws DataException {
		if (this.notIncludeAggr) {
			return new IBinding[0];
		}

		totalExprName = new String[2];
		this.totalExprName[0] = "TOTAL_COUNT_1";
		this.totalExprName[1] = "TOTAL_AMOUNT_1";

		int num2 = 2;
		IBinding[] totalBeArray = new IBinding[num2];
		totalBeArray[0] = new Binding(this.totalExprName[0], new ScriptExpression(null));
		if (this.GEN_USE_RUNNING_AGGR) {
			totalBeArray[0].setAggrFunction("runningcount");
		} else {
			totalBeArray[0].setAggrFunction("count");
		}
		if (this.USE_ROW_IN_AGGREGATION) {
			totalBeArray[1] = new Binding(this.totalExprName[1], new ScriptExpression("row.AMOUNT_1"));
		} else {
			totalBeArray[1] = new Binding(this.totalExprName[1], new ScriptExpression("dataSetRow.AMOUNT"));
		}
		if (this.GEN_USE_RUNNING_AGGR) {
			totalBeArray[1].setAggrFunction("runningsum");
		} else {
			totalBeArray[1].setAggrFunction("sum");
		}

		return totalBeArray;

	}

}

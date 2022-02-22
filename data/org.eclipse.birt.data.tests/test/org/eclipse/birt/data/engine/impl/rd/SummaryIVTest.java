/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
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
package org.eclipse.birt.data.engine.impl.rd;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.aggregation.api.IBuildInAggregation;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IGroupDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.FilterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.SortDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.junit.Test;

import testutil.ConfigText;

public class SummaryIVTest extends RDTestCase {
	private String[] bindingName;

	private String GEN_queryResultID;
	private String UPDATE_queryResultID;
	private boolean useDateGroup = false;

	@Override
	protected DataSourceInfo getDataSourceInfo() {
		return new DataSourceInfo(ConfigText.getString("Api.TestData.TableName"),
				ConfigText.getString("Api.TestData.TableSQL"), ConfigText.getString("Api.TestData.TestDataFileName"));
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.APITestCase#setUp()
	 */
	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.impl.rd.RDTestCase#tearDown()
	 */
	/**
	 * With filter
	 *
	 * @throws BirtException
	 */
	@Test
	public void testBasicFilter() throws Exception {
		this.genBasicIV();
		this.closeArchiveWriter();

		DataEngineContext deContext2 = newContext(DataEngineContext.MODE_PRESENTATION, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext2);
		this.preBasicIV();
		this.closeArchiveReader();

		DataEngineContext deContext3 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName2);
		myPreDataEngine = DataEngine.newDataEngine(deContext3);

		this.updateBasicIVOnFilter();
		this.closeArchiveReader();
		this.closeArchiveWriter();

		this.checkOutputFile();
	}

	/**
	 * With filter test case for 63315
	 *
	 * @throws BirtException
	 */
	@Test
	public void testBasicFilter1() throws Exception {
		useDateGroup = true;
		this.genBasicIV();
		this.closeArchiveWriter();

		DataEngineContext deContext2 = newContext(DataEngineContext.MODE_PRESENTATION, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext2);
		this.preBasicIV();
		this.closeArchiveReader();

		DataEngineContext deContext3 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName2);
		myPreDataEngine = DataEngine.newDataEngine(deContext3);

		this.updateBasicIVOnFilter2();
		this.closeArchiveReader();
		this.closeArchiveWriter();

		this.checkOutputFile();
	}

	/**
	 * With filter
	 *
	 * @throws BirtException
	 */
	@Test
	public void testBasicSort() throws Exception {
		this.genBasicIV();
		this.closeArchiveWriter();

		DataEngineContext deContext2 = newContext(DataEngineContext.MODE_PRESENTATION, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext2);
		this.preBasicIV();
		this.closeArchiveReader();

		DataEngineContext deContext3 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName2);
		myPreDataEngine = DataEngine.newDataEngine(deContext3);

		this.updateBasicIVOnSort();
		this.closeArchiveReader();
		this.closeArchiveWriter();

		this.checkOutputFile();
	}

	/**
	 * With filter
	 *
	 * @throws BirtException
	 */
	@Test
	public void testBasicAggregation() throws Exception {
		this.genBasicIV();
		this.closeArchiveWriter();

		DataEngineContext deContext2 = newContext(DataEngineContext.MODE_PRESENTATION, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext2);
		this.preBasicIV();
		this.closeArchiveReader();

		DataEngineContext deContext3 = newContext(DataEngineContext.MODE_UPDATE, fileName, fileName2);
		myPreDataEngine = DataEngine.newDataEngine(deContext3);

		this.updateBasicIVOnAggregation();
		this.closeArchiveReader();
		this.closeArchiveWriter();

		this.checkOutputFile();
	}

	private void updateBasicIVOnFilter() throws BirtException {
		IQueryResults qr;

		QueryDefinition qd = newSummaryQuery();
		qd.setQueryResultsID(this.GEN_queryResultID);

		ConditionalExpression condition = new ConditionalExpression("row[\"SALES\"]", IConditionalExpression.OP_GT,
				"1000");
		FilterDefinition filter = new FilterDefinition(condition);
		((GroupDefinition) qd.getGroups().get(1)).addFilter(filter);

		qr = myPreDataEngine.prepare(qd).execute(null);
		this.UPDATE_queryResultID = qr.getID();

		IResultIterator ri = qr.getResultIterator();
		while (ri.next()) {
			StringBuilder abc = new StringBuilder();
			for (int i = 0; i < bindingName.length; i++) {
				abc.append(ri.getValue(this.bindingName[i])).append("  ");
			}

			this.testPrintln(abc.toString());
		}
		this.testPrintln("\n");

		ri.close();
		qr.close();
		myPreDataEngine.shutdown();
		myPreDataEngine.clearCache(dataSource, dataSet);
		myPreDataEngine = null;

	}

	private void updateBasicIVOnFilter2() throws BirtException {
		IQueryResults qr;

		QueryDefinition qd = newSummaryQuery2();
		qd.setQueryResultsID(this.GEN_queryResultID);

		ConditionalExpression condition = new ConditionalExpression("row[\"SALES\"]", IConditionalExpression.OP_EQ,
				"7100.0");
		FilterDefinition filter = new FilterDefinition(condition);
		((GroupDefinition) qd.getGroups().get(0)).addFilter(filter);

		qr = myPreDataEngine.prepare(qd).execute(null);
		this.UPDATE_queryResultID = qr.getID();

		IResultIterator ri = qr.getResultIterator();
		while (ri.next()) {
			StringBuilder abc = new StringBuilder();
			for (int i = 0; i < bindingName.length; i++) {
				abc.append(ri.getValue(this.bindingName[i])).append("  ");
			}

			this.testPrintln(abc.toString());
		}
		this.testPrintln("\n");

		ri.close();
		qr.close();
		myPreDataEngine.shutdown();
		myPreDataEngine.clearCache(dataSource, dataSet);
		myPreDataEngine = null;

	}

	private void updateBasicIVOnAggregation() throws BirtException {
		IQueryResults qr;

		QueryDefinition qd = newSummaryQuery();
		qd.setQueryResultsID(this.GEN_queryResultID);

		IBinding binding = new Binding("SUM_ON_COUNTRY");
		binding.setExpression(new ScriptExpression("row[\"SALES\"]"));
		binding.setAggrFunction(IBuildInAggregation.TOTAL_SUM_FUNC);
		binding.addAggregateOn("countryGroup");

		qd.addBinding(binding);

		qr = myPreDataEngine.prepare(qd).execute(null);
		this.UPDATE_queryResultID = qr.getID();

		IResultIterator ri = qr.getResultIterator();
		while (ri.next()) {
			StringBuilder abc = new StringBuilder();
			for (int i = 0; i < bindingName.length; i++) {
				abc.append(ri.getValue(this.bindingName[i])).append("  ");
			}

			abc.append(ri.getValue("SUM_ON_COUNTRY")).append("  ");

			this.testPrintln(abc.toString());
		}
		this.testPrintln("\n");

		ri.close();
		qr.close();
		myPreDataEngine.shutdown();
		myPreDataEngine.clearCache(dataSource, dataSet);
		myPreDataEngine = null;
	}

	private void updateBasicIVOnSort() throws BirtException {
		IQueryResults qr;

		QueryDefinition qd = newSummaryQuery();
		qd.setQueryResultsID(this.GEN_queryResultID);

		SortDefinition sort = new SortDefinition();
		sort.setExpression("row[\"SALES\"]");
		sort.setSortDirection(ISortDefinition.SORT_ASC);
		((GroupDefinition) qd.getGroups().get(1)).addSort(sort);

		qr = myPreDataEngine.prepare(qd).execute(null);
		this.UPDATE_queryResultID = qr.getID();

		IResultIterator ri = qr.getResultIterator();
		while (ri.next()) {
			StringBuilder abc = new StringBuilder();
			for (int i = 0; i < bindingName.length; i++) {
				abc.append(ri.getValue(this.bindingName[i])).append("  ");
			}

			this.testPrintln(abc.toString());
		}
		this.testPrintln("\n");

		ri.close();
		qr.close();
		myPreDataEngine.shutdown();
		myPreDataEngine.clearCache(dataSource, dataSet);
		myPreDataEngine = null;
	}

	private void preBasicIV() throws BirtException {
		IQueryResults qr;

		// here queryResultID needs to set as the data set
		QueryDefinition qd;

		if (useDateGroup) {
			qd = newSummaryQuery1();
		} else {
			qd = newSummaryQuery();
		}
		qd.setQueryResultsID(this.GEN_queryResultID);

		qr = myPreDataEngine.prepare(qd).execute(null);
		this.UPDATE_queryResultID = qr.getID();

		qr = myPreDataEngine.getQueryResults(this.UPDATE_queryResultID);

		IResultIterator ri = qr.getResultIterator();
		while (ri.next()) {
			StringBuilder abc = new StringBuilder();
			for (int i = 0; i < bindingName.length; i++) {
				abc.append(ri.getValue(this.bindingName[i])).append("  ");
			}

			this.testPrintln(abc.toString());
		}
		this.testPrintln("\n");

		ri.close();
		qr.close();
		myPreDataEngine.shutdown();
		myPreDataEngine.clearCache(dataSource, dataSet);
		myPreDataEngine = null;
	}

	/**
	 * @throws BirtException
	 */
	private void genBasicIV() throws BirtException {
		QueryDefinition qd;

		if (useDateGroup) {
			qd = newSummaryQuery1();
		} else {
			qd = newSummaryQuery();
		}
		// generation
		IQueryResults qr = myGenDataEngine.prepare(qd).execute(scope);

		// important step
		GEN_queryResultID = qr.getID();

		IResultIterator ri = qr.getResultIterator();
		while (ri.next()) {
			StringBuilder abc = new StringBuilder();
			for (int i = 0; i < bindingName.length; i++) {
				abc.append(ri.getValue(this.bindingName[i])).append("  ");
			}

			this.testPrintln(abc.toString());
		}
		this.testPrintln("\n");

		ri.close();
		qr.close();
		myGenDataEngine.shutdown();
		myGenDataEngine.clearCache(dataSource, dataSet);
	}

	/**
	 * @return
	 * @throws DataException
	 */
	private QueryDefinition newSummaryQuery() throws DataException {
		QueryDefinition qd = newReportQuery();
		qd.setIsSummaryQuery(true);
		qd.setUsesDetails(false);

		// add grouping on column1
		GroupDefinition gd = new GroupDefinition("countryGroup");
		gd.setKeyColumn("COUNTRY");
		qd.addGroup(gd);

		// add grouping on column1
		GroupDefinition gd2 = new GroupDefinition("cityGroup");
		gd2.setKeyColumn("CITY");
		qd.addGroup(gd2);

		this.bindingName = new String[3];
		this.bindingName[0] = "COUNTRY";
		this.bindingName[1] = "CITY";
		this.bindingName[2] = "SALES";

		IBinding[] binding;
		binding = new Binding[3];
		binding[0] = new Binding(this.bindingName[0], new ScriptExpression("dataSetRow.COUNTRY"));
		binding[1] = new Binding(this.bindingName[1], new ScriptExpression("dataSetRow.CITY"));
		binding[2] = new Binding(this.bindingName[2]);
		binding[2].setAggrFunction(IBuildInAggregation.TOTAL_SUM_FUNC);
		binding[2].setExpression(new ScriptExpression("dataSetRow.AMOUNT"));
		binding[2].addAggregateOn("cityGroup");
		binding[2].setDataType(DataType.DOUBLE_TYPE);
		qd.addBinding(binding[0]);
		qd.addBinding(binding[1]);
		qd.addBinding(binding[2]);

		return qd;
	}

	/**
	 * @return summary on Year and Country
	 * @throws DataException
	 */
	private QueryDefinition newSummaryQuery1() throws DataException {
		QueryDefinition qd = newReportQuery();
		qd.setIsSummaryQuery(true);
		qd.setUsesDetails(false);

		// add grouping on column1
		GroupDefinition gd = new GroupDefinition("yearGroup");
		gd.setKeyColumn("SALE_DATE");
		gd.setIntervalRange(1);
		gd.setInterval(IGroupDefinition.MONTH_INTERVAL);
		qd.addGroup(gd);

		// add grouping on column1
		GroupDefinition gd2 = new GroupDefinition("countryGroup");
		gd2.setKeyColumn("COUNTRY");
		qd.addGroup(gd2);

		this.bindingName = new String[3];
		this.bindingName[0] = "SALE_DATE";
		this.bindingName[1] = "COUNTRY";
		this.bindingName[2] = "SALES";

		IBinding[] binding;
		binding = new Binding[3];
		binding[0] = new Binding(this.bindingName[0], new ScriptExpression("dataSetRow.SALE_DATE"));
		binding[1] = new Binding(this.bindingName[1], new ScriptExpression("dataSetRow.COUNTRY"));
		binding[2] = new Binding(this.bindingName[2]);
		binding[2].setAggrFunction(IBuildInAggregation.TOTAL_SUM_FUNC);
		binding[2].setExpression(new ScriptExpression("dataSetRow.AMOUNT"));
		binding[2].addAggregateOn("countryGroup");
		binding[2].setDataType(DataType.DOUBLE_TYPE);
		qd.addBinding(binding[0]);
		qd.addBinding(binding[1]);
		qd.addBinding(binding[2]);

		return qd;
	}

	/**
	 * @return summary Country
	 * @throws DataException
	 */
	private QueryDefinition newSummaryQuery2() throws DataException {
		QueryDefinition qd = newReportQuery();
		qd.setIsSummaryQuery(true);
		qd.setUsesDetails(false);

		// add grouping on column1
		GroupDefinition gd2 = new GroupDefinition("countryGroup");
		gd2.setKeyColumn("COUNTRY");
		qd.addGroup(gd2);

		this.bindingName = new String[2];
		this.bindingName[0] = "COUNTRY";
		this.bindingName[1] = "SALES";

		IBinding[] binding;
		binding = new Binding[2];
		binding[0] = new Binding(this.bindingName[0], new ScriptExpression("dataSetRow.COUNTRY"));
		binding[1] = new Binding(this.bindingName[1]);
		binding[1].setAggrFunction(IBuildInAggregation.TOTAL_SUM_FUNC);
		binding[1].setExpression(new ScriptExpression("dataSetRow.AMOUNT"));
		binding[1].addAggregateOn("countryGroup");
		binding[1].setDataType(DataType.DOUBLE_TYPE);
		qd.addBinding(binding[0]);
		qd.addBinding(binding[1]);

		return qd;
	}

}

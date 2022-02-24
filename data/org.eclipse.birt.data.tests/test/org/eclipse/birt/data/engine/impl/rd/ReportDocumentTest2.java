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
package org.eclipse.birt.data.engine.impl.rd;

import java.io.IOException;

import org.eclipse.birt.core.archive.FileArchiveReader;
import org.eclipse.birt.core.archive.FileArchiveWriter;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.APITestCase;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.querydefn.BaseQueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import testutil.ConfigText;

import org.junit.Test;

/**
 * This is a simple test case for report document. It mainly tests that these
 * unused bound columns will be saved into report document as well.
 */
public class ReportDocumentTest2 extends APITestCase {
	private FileArchiveWriter archiveWriter;
	private FileArchiveReader archiveReader;

	private String[] rowExprName;
	private String[] totalExprName;

	private String queryResultID;

	private DataEngine myGenDataEngine;
	private DataEngine myPreDataEngine;

	/*
	 * @see org.eclipse.birt.data.engine.api.APITestCase#getDataSourceInfo()
	 */
	protected DataSourceInfo getDataSourceInfo() {
		return new DataSourceInfo(ConfigText.getString("Api.TestData.TableName"),
				ConfigText.getString("Api.TestData.TableSQL"), ConfigText.getString("Api.TestData.TestDataFileName"));
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testBasic() throws Exception {
		String fileName = getOutputFolder() + "testData";
		DataEngineContext deContext1 = newContext(DataEngineContext.MODE_GENERATION, fileName);
		deContext1.setTmpdir(this.getTempDir());
		myGenDataEngine = DataEngine.newDataEngine(deContext1);

		myGenDataEngine.defineDataSource(this.dataSource);
		myGenDataEngine.defineDataSet(this.dataSet);

		this.genBasic();
		this.closeArchiveWriter();

		DataEngineContext deContext2 = newContext(DataEngineContext.MODE_PRESENTATION, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext2);

		this.preBasic();
		this.closeArchiveReader();
	}

	/**
	 * Inspite of without accessing all resultset iterator. All resultset iterator
	 * should be kept in document.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testHasGapRowInResultSet() throws Exception {
		String fileName = getOutputFolder() + "testData";
		DataEngineContext deContext1 = newContext(DataEngineContext.MODE_GENERATION, fileName);
		myGenDataEngine = DataEngine.newDataEngine(deContext1);

		myGenDataEngine.defineDataSource(this.dataSource);
		myGenDataEngine.defineDataSet(this.dataSet);

		genHasGapRs();
		this.closeArchiveWriter();

		DataEngineContext deContext2 = newContext(DataEngineContext.MODE_PRESENTATION, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext2);

		this.preBasic();
	}

	private void genHasGapRs() throws BirtException {
		Context context = Context.enter();
		Scriptable scope = context.initStandardObjects();
		Context.exit();

		// ------------generation----------------
		QueryDefinition qd = newReportQuery();

		prepareExprNameAndQuery(qd);

		// generation
		IQueryResults qr = myGenDataEngine.prepare(qd).execute(scope);

		// important step
		queryResultID = qr.getID();

		IResultIterator ri = qr.getResultIterator();

		// only access 0 and 3 resultIterator
		ri.next();
		ri.moveTo(3);

		ri.close();
		qr.close();
		myGenDataEngine.shutdown();
	}

	/**
	 * Call skipToEnd() when using detail. Expected: all resultset should be saved
	 * in report document.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSkipToEndWhenUsingDetail() throws Exception {
		String fileName = getOutputFolder() + "testData";
		DataEngineContext deContext1 = newContext(DataEngineContext.MODE_GENERATION, fileName);
		myGenDataEngine = DataEngine.newDataEngine(deContext1);

		myGenDataEngine.defineDataSource(this.dataSource);
		myGenDataEngine.defineDataSet(this.dataSet);

		genSkipToEnd(true);
		this.closeArchiveWriter();

		DataEngineContext deContext2 = newContext(DataEngineContext.MODE_PRESENTATION, fileName);
		myPreDataEngine = DataEngine.newDataEngine(deContext2);

		this.preBasic();
	}

	private void genSkipToEnd(boolean useDetails) throws BirtException {
		Context context = Context.enter();
		Scriptable scope = context.initStandardObjects();
		Context.exit();

		// ------------generation----------------
		QueryDefinition qd = newReportQuery();
		qd.setUsesDetails(useDetails);
		GroupDefinition gd = new GroupDefinition();
		String columnBindingNameGroup = "COUNTRY";
		IBaseExpression columnBindingExprGroup = new ScriptExpression("dataSetRow.COUNTRY");
		gd.setKeyColumn("COUNTRY");
		qd.addBinding(new Binding(columnBindingNameGroup, columnBindingExprGroup));
		qd.addGroup(gd);

		prepareExprNameAndQuery(qd);

		// generation
		IQueryResults qr = myGenDataEngine.prepare(qd).execute(scope);

		// important step
		queryResultID = qr.getID();

		IResultIterator ri = qr.getResultIterator();

		// only access 0 and 3 resultIterator
		ri.next();
		ri.moveTo(4);
		ri.skipToEnd(1);

		ri.close();
		qr.close();
		myGenDataEngine.shutdown();
	}

	/**
	 * @throws Exception
	 */
	private void genBasic() throws Exception {
		Context context = Context.enter();
		Scriptable scope = context.initStandardObjects();
		Context.exit();

		// ------------generation----------------
		QueryDefinition qd = newReportQuery();

		prepareExprNameAndQuery(qd);

		// generation
		IQueryResults qr = myGenDataEngine.prepare(qd).execute(scope);

		// important step
		queryResultID = qr.getID();

		IResultIterator ri = qr.getResultIterator();
		while (ri.next()) {
			ri.getValue(this.rowExprName[0]);
			ri.getValue(this.totalExprName[0]);
		}

		ri.close();
		qr.close();
		myGenDataEngine.shutdown();
	}

	/**
	 * @throws Exception
	 */
	private void preBasic() throws Exception {
		IQueryResults qr = myPreDataEngine.getQueryResults(queryResultID);
		assert (qr.getResultMetaData() != null);

		IResultIterator ri = qr.getResultIterator();
		assert (ri.getResultMetaData() != null);

		while (ri.next()) {
			String str = "";
			for (int i = 0; i < rowExprName.length; i++) {
				Object ob2 = ri.getValue(this.rowExprName[i]);
				if (i != 0)
					str += " ";
				str += ob2.toString();
			}

			if (totalExprName != null) {
				for (int i = 0; i < totalExprName.length; i++) {
					Object ob2 = ri.getValue(this.totalExprName[i]);
					str += " " + ob2.toString();
				}
			}
			testPrintln(str);
		}

		ri.close();
		myPreDataEngine.shutdown();

		this.checkOutputFile();
	}

	/**
	 * @return row expression array
	 */
	private IBaseExpression[] getRowExpr() {
		// row test
		int num = 4;
		IBaseExpression[] rowBeArray = new IBaseExpression[num];
		rowBeArray[0] = new ScriptExpression("dataSetRow.COUNTRY");
		rowBeArray[1] = new ScriptExpression("dataSetRow.CITY");
		rowBeArray[2] = new ScriptExpression("dataSetRow.SALE_DATE");
		rowBeArray[3] = new ScriptExpression("dataSetRow.AMOUNT");

		this.rowExprName = new String[rowBeArray.length];
		this.rowExprName[0] = "COUNTRY_1";
		this.rowExprName[1] = "CITY_1";
		this.rowExprName[2] = "SALE_NAME_1";
		this.rowExprName[3] = "AMOUNT_1";

		return rowBeArray;
	}

	/**
	 * Add expression on the row of group
	 * 
	 * @param rowBeArray
	 * @param totalBeArray
	 * @param qd
	 * @throws DataException
	 */
	private void prepareExprNameAndQuery(BaseQueryDefinition qd) throws DataException {
		// prepare
		IBaseExpression[] rowBeArray = getRowExpr();

		IBaseExpression[] totalBeArray = new IBaseExpression[2];

		totalBeArray[0] = new ScriptExpression(null);
		totalBeArray[1] = new ScriptExpression("dataSetRow.AMOUNT");

		totalExprName = new String[totalBeArray.length];
		this.totalExprName[0] = "TOTAL_COUNT_1";
		this.totalExprName[1] = "TOTAL_AMOUNT_1";

		IBinding total1 = new Binding(this.totalExprName[0], totalBeArray[0]);
		total1.setAggrFunction("count");

		IBinding total2 = new Binding(this.totalExprName[1], totalBeArray[1]);
		total2.setAggrFunction("sum");

		qd.addBinding(total1);
		qd.addBinding(total2);
		for (int i = 0; i < rowExprName.length; i++) {

			qd.addBinding(new Binding(this.rowExprName[i], rowBeArray[i]));
		}

	}

	/**
	 * @param type
	 * @param fileName
	 * @return
	 * @throws BirtException
	 */
	private DataEngineContext newContext(int type, String fileName) throws BirtException {

		switch (type) {
		case DataEngineContext.MODE_GENERATION: {
			try {
				archiveWriter = new FileArchiveWriter(fileName);
				archiveWriter.initialize();
			} catch (IOException e) {
				throw new IllegalArgumentException(e.getMessage());
			}
			return DataEngineContext.newInstance(DataEngineContext.MODE_GENERATION, null, null, archiveWriter);
		}
		case DataEngineContext.MODE_PRESENTATION: {
			try {
				archiveReader = new FileArchiveReader(fileName);
				archiveReader.open();
			} catch (IOException e) {
				throw new IllegalArgumentException(e.getMessage());
			}
			return DataEngineContext.newInstance(DataEngineContext.MODE_PRESENTATION, null, archiveReader, null);
		}
		default:
			throw new IllegalArgumentException("" + type);
		}
	}

	/**
	 * @throws DataException
	 */
	private void closeArchiveWriter() throws DataException {
		if (archiveWriter != null)
			try {
				archiveWriter.finish();
			} catch (IOException e) {
				throw new DataException("error", e);
			}
	}

	/**
	 * @throws DataException
	 */
	private void closeArchiveReader() throws DataException {
		if (archiveReader != null)
			try {
				archiveReader.close();
			} catch (IOException e) {
				throw new DataException("error", e);
			}
	}

}

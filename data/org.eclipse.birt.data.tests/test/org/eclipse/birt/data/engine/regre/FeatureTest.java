/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.regre;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.data.engine.api.APITestCase;
import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.api.querydefn.BaseDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.ComputedColumn;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.FilterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.InputParameterBinding;
import org.eclipse.birt.data.engine.api.querydefn.OdaDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.ParameterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.SortDefinition;
import org.mozilla.javascript.Scriptable;

import testutil.ConfigText;

import org.junit.Before;
import org.junit.Test;

/**
 * Test DtE featurs.
 */
public class FeatureTest extends APITestCase {
	/** Custom is parent query */
	private IBaseDataSetDesign datasetCstm;
	private ScriptExpression[] expressionsCstm;
	private String[] exprNameCstm;

	private QueryDefinition queryDefnCstm;

	/** Call is child query */
	private IBaseDataSetDesign datasetCall;
	private ScriptExpression[] expressionsCall;
	private String[] exprNameCall;
	private QueryDefinition queryDefnCall;

	private String callsTableName;

	/*
	 * @see org.eclipse.birt.data.engine.api.APITestCase#setUp()
	 */
	@Before
	public void featureSetUp() throws Exception {
		prepareDataSet(new DataSourceInfo(ConfigText.getString("Api.TestDataCalls.TableName"),
				ConfigText.getString("Api.TestDataCalls.TableSQL"),
				ConfigText.getString("Api.TestDataCalls.TestDataFileName")));

		callsTableName = ConfigText.getString("Api.TestDataCalls.TableName");
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.APITestCase#getDataSourceInfo()
	 */
	protected DataSourceInfo getDataSourceInfo() {
		return new DataSourceInfo(ConfigText.getString("Api.TestDataCustomer.TableName"),
				ConfigText.getString("Api.TestDataCustomer.TableSQL"),
				ConfigText.getString("Api.TestDataCustomer.TestDataFileName"));
	}

	/**
	 * This test will test all currently supported features of DtE.
	 * 
	 * Including: Group Sort Filter Computed Columns Aggregate Nested query
	 * Parameter Binding Conditional Expression
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAllFeatures() throws Exception {
		Collection inputParamDefns = new ArrayList();
		Collection inputParamBindings = new ArrayList();

		datasetCstm = this.dataSet;

		// Create query define for parent query
		queryDefnCstm = createCustomerQueryDefn();

		datasetCall = newDataSet("data set calls", " SELECT * FROM " + callsTableName + " WHERE CustomerID = ?");

		// Create query define for child query
		queryDefnCall = createCallQueryDefn();

		// Add some Computed Columns to datasetCall,child query
		addComputedColumnsToDataSetCall();

		// Add Parameter Bindings
		ParameterDefinition inputParamDefn = new ParameterDefinition("param1", DataType.INTEGER_TYPE, true, false);
		inputParamDefn.setPosition(1);
		inputParamDefn.setDefaultInputValue("0");
		inputParamDefns.add(inputParamDefn);
		InputParameterBinding inputParamBinding = new InputParameterBinding(1, expressionsCstm[4]);// rows[0].CustomerID
		inputParamBindings.add(inputParamBinding);

		// execute queries
		run(inputParamDefns, inputParamBindings);
	}

	/**
	 * Features: Group Sort
	 * 
	 * @return
	 */
	private QueryDefinition createCustomerQueryDefn() {
		GroupDefinition[] groupDefn = new GroupDefinition[] { new GroupDefinition(), new GroupDefinition() };
		groupDefn[0].setKeyExpression("row.CUSTOMERID");
		groupDefn[1].setKeyExpression("row.NAME");

		SortDefinition[] sortDefn = new SortDefinition[] { new SortDefinition() };
		sortDefn[0].setColumn("CUSTOMERID");
		sortDefn[0].setSortDirection(ISortDefinition.SORT_DESC);

		expressionsCstm = new ScriptExpression[] { new ScriptExpression("dataSetRow.CUSTOMERID", 0),
				new ScriptExpression("dataSetRow.NAME", 0), new ScriptExpression("dataSetRow.ADDRESS", 0),
				new ScriptExpression("dataSetRow.CURRENTBALANCE", 0), new ScriptExpression("rows[0].CUSTOMERID", 0) };

		exprNameCstm = new String[] { "CUSTOMERID", "NAME", "ADDRESS", "CURRENTBALANCE", "ROWS0CUSTID" };
		return createQueryDefnUsingGivenArgs(datasetCstm, exprNameCstm, expressionsCstm, groupDefn, sortDefn, null);
	}

	/**
	 * Features: Filter ConditionalExpression
	 * 
	 * @return
	 */
	private QueryDefinition createCallQueryDefn() {
		expressionsCall = new ScriptExpression[] {

				new ScriptExpression("dataSetRow.CUSTOMERID", 0), new ScriptExpression("dataSetRow.CALLTIME", 0),
				new ScriptExpression("dataSetRow.TONUMBER", 0), new ScriptExpression("dataSetRow.DURATION", 0),
				new ScriptExpression("dataSetRow.Charge2", 0),
				new ScriptExpression("Total.sum(dataSetRow.Charge2)", 0) };

		exprNameCall = new String[] { "CUSTOMERID", "CALLTIME", "TONUMBER", "DURATION", "Charge2", "TOTALSUMCHARGE" };
		FilterDefinition[] filters = new FilterDefinition[] { new FilterDefinition(
				new ConditionalExpression("dataSetRow.DURATION", ConditionalExpression.OP_GT, "0")) };
		return createQueryDefnUsingGivenArgs(datasetCall, exprNameCall, expressionsCall, null, null, filters);
	}

	/**
	 * Create a query design by given Arguments
	 */
	private QueryDefinition createQueryDefnUsingGivenArgs(IBaseDataSetDesign dataset, String[] exprNames,
			ScriptExpression[] expressions, GroupDefinition[] groupDefn, SortDefinition[] sortDefn,
			IFilterDefinition[] filters) {

		QueryDefinition queryDefn = newReportQuery(dataset);

		if (groupDefn != null)
			for (int i = 0; i < groupDefn.length; i++)
				queryDefn.addGroup(groupDefn[i]);
		if (sortDefn != null)
			for (int i = 0; i < sortDefn.length; i++)
				queryDefn.addSort(sortDefn[i]);
		if (expressions != null)
			for (int i = 0; i < expressions.length; i++)
				queryDefn.addResultSetExpression(exprNames[i], expressions[i]);
		if (filters != null)
			for (int i = 0; i < filters.length; i++)
				queryDefn.addFilter(filters[i]);
		return queryDefn;
	}

	/**
	 * Add some Computed Columns to datasetCall, child query
	 */
	private void addComputedColumnsToDataSetCall() {
		// test computed columns
		ComputedColumn[] computedColumns = new ComputedColumn[] {
				new ComputedColumn("Charge2", "row.DURATION*0.05", DataType.DOUBLE_TYPE) };
		for (int i = 0; i < computedColumns.length; i++) {
			((BaseDataSetDesign) this.datasetCall).addComputedColumn(computedColumns[i]);
		}
	}

	/**
	 * execute the queries, output result and compare result with golden file.
	 */
	private void run(Collection inputParamDefns, Collection inputParamBindings) throws Exception {
		Iterator iterator = inputParamDefns.iterator();
		while (iterator.hasNext()) {
			ParameterDefinition inputParamDefn = (ParameterDefinition) iterator.next();
			((OdaDataSetDesign) datasetCall).addParameter(inputParamDefn);
		}

		iterator = inputParamBindings.iterator();
		while (iterator.hasNext()) {
			InputParameterBinding inputParamBinding = (InputParameterBinding) iterator.next();
			queryDefnCall.addInputParamBinding(inputParamBinding);
		}

		IPreparedQuery preparedQueryCstm = dataEngine.prepare(queryDefnCstm);
		IPreparedQuery preparedQueryCall = dataEngine.prepare(queryDefnCall);

		IQueryResults queryResultsCstm = preparedQueryCstm.execute(jsScope);
		IResultIterator resultItCustomer = queryResultsCstm.getResultIterator();

		// output result
		testPrintln("*****A new Report Start!*****");
		while (resultItCustomer.next()) {
			testPrint("Customer Name:");
			testPrint(evalAsString(exprNameCstm[1], resultItCustomer));
			testPrint("  Address:");
			testPrint(evalAsString(exprNameCstm[2], resultItCustomer));
			testPrintln("");
			testPrint("Starting Balance: $");
			testPrint(evalAsString(exprNameCstm[3], resultItCustomer));
			testPrintln("");

			Scriptable newScope = jsContext.newObject(jsScope);
			newScope.setParentScope(jsScope);

			IQueryResults queryResultsCalls = preparedQueryCall.execute(queryResultsCstm, newScope);
			IResultIterator resultItCalls = queryResultsCalls.getResultIterator();
			while (resultItCalls.next()) {
				for (int i = 1; i < expressionsCall.length; i++) {
					testPrint(evalAsString(exprNameCall[i], resultItCalls));
					testPrint(" ");
				}
				testPrintln("");
			}
			testPrintln("");
		}
		checkOutputFile();
	}

}

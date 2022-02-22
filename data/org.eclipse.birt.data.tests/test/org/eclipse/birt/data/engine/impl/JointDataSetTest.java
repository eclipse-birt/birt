/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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
package org.eclipse.birt.data.engine.impl;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.api.APITestCase;
import org.eclipse.birt.data.engine.api.IGroupDefinition;
import org.eclipse.birt.data.engine.api.IJoinCondition;
import org.eclipse.birt.data.engine.api.IJointDataSetDesign;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.querydefn.ColumnDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ComputedColumn;
import org.eclipse.birt.data.engine.api.querydefn.FilterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.JoinCondition;
import org.eclipse.birt.data.engine.api.querydefn.JointDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.OdaDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.ResultClass;
import org.eclipse.birt.data.engine.executor.ResultFieldMetadata;
import org.eclipse.birt.data.engine.impl.jointdataset.JoinConditionMatcher;
import org.eclipse.birt.data.engine.impl.jointdataset.JointDataSetPopulatorFactory;
import org.eclipse.birt.data.engine.impl.jointdataset.JointResultMetadata;
import org.eclipse.birt.data.engine.odi.IDataSetPopulator;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.eclipse.birt.data.engine.odi.IResultObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import testutil.ConfigText;

/**
 *
 */
public class JointDataSetTest extends APITestCase {
	//
	private static int CARTESIAN_POPULATOR = 0;
	private static int BINARY_TREE_POPULATOR = 1;
	private static boolean ADD_FETCH_LIMIT = false;
	private ScriptContext cx;

	/*
	 * @see org.eclipse.birt.data.engine.api.APITestCase#getDataSourceInfo()
	 */
	@Override
	protected DataSourceInfo getDataSourceInfo() {
		return new DataSourceInfo(ConfigText.getString("Impl.TestJointDataSet.TableName"),
				ConfigText.getString("Impl.TestJointDataSet.TableSQL"),
				ConfigText.getString("Impl.TestJointDataSet.TestDataFileName"));
	}

	@Before
	public void jointDataSetSetUp() throws Exception {

		cx = new ScriptContext();
	}

	@After
	public void jointDataSetTearDown() throws Exception {
		cx.close();
	}

	/**
	 * Basic test to get MD for all columns
	 *
	 * @throws Exception
	 */
	@Test
	public void testInnerJoin_DEFAULT() throws Exception {
		String s = basicJoinTest(IJointDataSetDesign.INNER_JOIN, CARTESIAN_POPULATOR);
		this.testPrint(s);
		checkOutputFile();
	}

	/**
	 *
	 * @throws Exception
	 */
	@Test
	public void testLeftOuterJoin_DEFAULT() throws Exception {

		String s = basicJoinTest(IJointDataSetDesign.LEFT_OUTER_JOIN, CARTESIAN_POPULATOR);
		this.testPrint(s);
		checkOutputFile();
	}

	/**
	 *
	 * @throws Exception
	 */
	@Test
	public void testRightOuterJoin_DEFAULT() throws Exception {

		String s = basicJoinTest(IJointDataSetDesign.RIGHT_OUTER_JOIN, CARTESIAN_POPULATOR);
		this.testPrint(s);
		checkOutputFile();
	}

	/**
	 *
	 * @throws Exception
	 */
	@Test
	public void testFullOuterJoin_DEFAULT() throws Exception {

		String s = basicJoinTest(IJointDataSetDesign.FULL_OUTER_JOIN, CARTESIAN_POPULATOR);
		this.testPrint(s);
		checkOutputFile();
	}

	/**
	 * Basic test to get MD for all columns
	 */
	@Test
	public void testInnerJoin_BINARY() throws Exception {

		String s = basicJoinTest(IJointDataSetDesign.INNER_JOIN, BINARY_TREE_POPULATOR);
		this.testPrint(s);
		checkOutputFile();
	}

	/**
	 *
	 * @throws Exception
	 */
	@Test
	public void testLeftOuterJoin_BINARY() throws Exception {

		String s = basicJoinTest(IJointDataSetDesign.LEFT_OUTER_JOIN, BINARY_TREE_POPULATOR);
		this.testPrint(s);
		checkOutputFile();
	}

	/**
	 *
	 * @throws Exception
	 */
	@Test
	public void testRightOuterJoin_BINARY() throws Exception {

		String s = basicJoinTest(IJointDataSetDesign.RIGHT_OUTER_JOIN, BINARY_TREE_POPULATOR);
		this.testPrint(s);
		checkOutputFile();
	}

	/**
	 *
	 * @throws Exception
	 */
	@Test
	public void testComplexInnerJoin() throws Exception {
		String s = complexJoinTest(IJointDataSetDesign.INNER_JOIN);
		this.testPrint(s);
		checkOutputFile();
	}

	/**
	 *
	 * @throws Exception
	 */
	@Test
	public void testComplexLeftOuterJoin() throws Exception {
		String s = complexJoinTest(IJointDataSetDesign.LEFT_OUTER_JOIN);
		this.testPrint(s);
		checkOutputFile();
	}

	/**
	 *
	 * @throws Exception
	 */
	@Test
	public void testComplexRightOuterJoin() throws Exception {
		String s = complexJoinTest(IJointDataSetDesign.RIGHT_OUTER_JOIN);
		this.testPrint(s);
		checkOutputFile();
	}

	/**
	 *
	 * @throws Exception
	 */
	@Test
	public void testComplexFullOuterJoin() throws Exception {
		String s = complexJoinTest(IJointDataSetDesign.FULL_OUTER_JOIN);
		this.testPrint(s);
		checkOutputFile();
	}

	/**
	 *
	 * @throws Exception
	 */
	@Test
	public void testRowFetchLimit() throws Exception {
		ADD_FETCH_LIMIT = true;
		this.testPrint(this.basicJoinTest(IJointDataSetDesign.INNER_JOIN, BINARY_TREE_POPULATOR));
		ADD_FETCH_LIMIT = false;
		checkOutputFile();
	}

	/**
	 *
	 * @param joinType
	 * @return
	 * @throws Exception
	 */
	private String complexJoinTest(int joinType) throws Exception {
		OdaDataSetDesign dset1 = newDataSet("dset1", "Select ID, CITY, STORE FROM " + this.getTestTableName()
				+ " where ID > 4 and ID <> 7 and ID <> 9 order by ID asc");

		OdaDataSetDesign dset2 = newDataSet("dset2", "Select ID, SKU, CATEGORY, PRICE FROM " + this.getTestTableName()
				+ " where ID < 20 and ID <> 10 and ID <> 13 order by ID asc");

		/*
		 * dset1.addComputedColumn( new ComputedColumn( "CP1", "row.ID + 10") );
		 * dset2.addComputedColumn( new ComputedColumn( "CP2", "row.ID + 20") );
		 */
		List a = new ArrayList();
		a.add(new JoinCondition(new ScriptExpression("dataSetRow.ID"), new ScriptExpression("dataSetRow.ID"),
				IJoinCondition.OP_EQ));

		JointDataSetDesign dset3 = new JointDataSetDesign("dset3", dset1.getName(), dset2.getName(), joinType, a);
		dset3.addComputedColumn(new ComputedColumn("group1.sum", "Total.sum(row[\"dset2::PRICE\"],null,0)"));
		dset3.addComputedColumn(new ComputedColumn("group2.sum", "Total.sum(row[\"dset2::PRICE\"],null,0)"));
		dset3.addComputedColumn(new ComputedColumn("cp3", "row[\"dset2::PRICE\"]+100"));
		dset3.addFilter(new FilterDefinition(new ScriptExpression("row[\"dset1::ID\"]!=15")));
		dset3.addFilter(new FilterDefinition(new ScriptExpression("row[\"dset1::ID\"]!=16")));
		dataEngine.defineDataSet(dset3);

		List b = new ArrayList();
		b.add(new JoinCondition(new ScriptExpression("dataSetRow[\"dset1::ID\"]"),
				new ScriptExpression("dataSetRow[\"ID\"]"), IJoinCondition.OP_EQ));

		JointDataSetDesign dset4 = new JointDataSetDesign("dset4", dset3.getName(), dset1.getName(), joinType, b);
		dset4.addComputedColumn(new ComputedColumn("CC", "\"I am grand Dset\""));
		dataEngine.defineDataSet(dset4);

		QueryDefinition query = this.newReportQuery(dset4);

		GroupDefinition group1 = new GroupDefinition();
		group1.setInterval(IGroupDefinition.NUMERIC_INTERVAL);
		group1.setIntervalRange(10);
		group1.setIntervalStart(new Integer(5));
		group1.setKeyExpression("row.G1");
		String G1 = "G1";
		ScriptExpression be1 = new ScriptExpression("dataSetRow[\"dset1::ID\"]");
		GroupDefinition group2 = new GroupDefinition();
		group2.setKeyExpression("row.G2");
		String G2 = "G2";
		ScriptExpression be2 = new ScriptExpression("dataSetRow[\"dset1::CITY\"]");

		query.addGroup(group1);
		query.addGroup(group2);
		query.addResultSetExpression(G1, be1);
		query.addResultSetExpression(G2, be2);
		IPreparedQuery preparedQuery = this.dataEngine.prepare(query);
		IQueryResults qr = preparedQuery.execute(null);
		IResultIterator ri = ((ResultIterator) qr.getResultIterator()).getOdiResult();

		IResultObject ro;
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < ri.getResultClass().getFieldCount(); i++) {
			s.append(ri.getResultClass().getFieldName(i + 1)).append("\t\t\t");
		}
		s.append("\n");
		long start = System.currentTimeMillis();
		int count = 0;
		do {
			ro = ri.getCurrentResult();
			count++;
			if (count == 49) {
				System.out.print("ar");
			}
			for (int i = 0; i < ri.getResultClass().getFieldCount(); i++) {
				s.append(ro.getFieldValue(i + 1)).append("\t\t\t");
			}
			s.append("\n");
		} while (ri.next());
		System.out.println(count + ":" + (System.currentTimeMillis() - start));
		return s.toString();
	}

	@Test
	public void testSelfInnerJoin() throws Exception {
		String s = selfJoinTest(IJointDataSetDesign.INNER_JOIN);
		this.testPrint(s);
		checkOutputFile();
	}

	@Test
	public void testSelfLeftOuterJoin() throws Exception {
		String s = selfJoinTest(IJointDataSetDesign.LEFT_OUTER_JOIN);
		this.testPrint(s);
		checkOutputFile();
	}

	@Test
	public void testSelfRightOuterJoin() throws Exception {
		String s = selfJoinTest(IJointDataSetDesign.RIGHT_OUTER_JOIN);
		this.testPrint(s);
		checkOutputFile();
	}

	private String selfJoinTest(int joinType) throws Exception {
		OdaDataSetDesign dset = newDataSet("dset",
				"Select ID, CITY, STORE FROM " + this.getTestTableName() + " order by ID asc");

		/*
		 * dset1.addComputedColumn( new ComputedColumn( "CP1", "row.ID + 10") );
		 * dset2.addComputedColumn( new ComputedColumn( "CP2", "row.ID + 20") );
		 */
		List a = new ArrayList();
		a.add(new JoinCondition(new ScriptExpression("dataSetRow.ID"), new ScriptExpression("dataSetRow.ID"),
				IJoinCondition.OP_EQ));

		JointDataSetDesign dset2 = new JointDataSetDesign("dset2", dset.getName(), dset.getName(), joinType, a);
		dataEngine.defineDataSet(dset2);

		QueryDefinition query = this.newReportQuery(dset2);

		IPreparedQuery preparedQuery = this.dataEngine.prepare(query);
		IQueryResults qr = preparedQuery.execute(null);
		IResultIterator ri = ((ResultIterator) qr.getResultIterator()).getOdiResult();

		IResultObject ro;
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < ri.getResultClass().getFieldCount(); i++) {
			s.append(ri.getResultClass().getFieldName(i + 1)).append("\t\t\t");
		}
		s.append("\n");
		long start = System.currentTimeMillis();
		int count = 0;
		do {
			ro = ri.getCurrentResult();
			count++;
			if (count == 49) {
				System.out.print("ar");
			}
			for (int i = 0; i < ri.getResultClass().getFieldCount(); i++) {
				s.append(ro.getFieldValue(i + 1)).append("\t\t\t");
			}
			s.append("\n");
		} while (ri.next());
		System.out.println(count + ":" + (System.currentTimeMillis() - start));
		return s.toString();
	}

	/**
	 * @return
	 * @throws Exception
	 * @throws BirtException
	 * @throws DataException
	 */
	private String basicJoinTest(int joinType, int populateType) throws Exception, BirtException, DataException {
		OdaDataSetDesign dset1 = newDataSet("dset1", "Select ID, CITY, STORE FROM " + this.getTestTableName()
				+ " where ID > 4 and ID <> 7 and ID <> 9 order by ID asc");

		if (ADD_FETCH_LIMIT) {
			dset1.setRowFetchLimit(6);
		}
		OdaDataSetDesign dset2 = newDataSet("dset2", "Select ID, SKU, CATEGORY, PRICE FROM " + this.getTestTableName()
				+ " where ID < 20 and ID <> 10 and ID <> 13 order by ID asc");
		if (ADD_FETCH_LIMIT) {
			dset2.setRowFetchLimit(6);
		}

		QueryDefinition query1 = this.newReportQuery(dset1);
		QueryResults qr1 = (QueryResults) this.dataEngine.prepare(query1).execute(null);

		QueryDefinition query2 = this.newReportQuery(dset2);
		QueryResults qr2 = (QueryResults) this.dataEngine.prepare(query2).execute(null);

		IResultIterator it1 = ((ResultIterator) qr1.getResultIterator()).getOdiResult();
		IResultIterator it2 = ((ResultIterator) qr2.getResultIterator()).getOdiResult();

		int[] isFromLeft = { 1, 1, 1, 2, 2, 2, 2 };
		int[] index = { 1, 2, 3, 1, 2, 3, 4 };
		List projectedColumns = new ArrayList();
		projectedColumns.add(new ResultFieldMetadata(1, "dset1::" + it1.getResultClass().getFieldName(1),
				it1.getResultClass().getFieldName(1), it1.getResultClass().getFieldValueClass(1),
				it1.getResultClass().getFieldNativeTypeName(1), false));
		projectedColumns.add(new ResultFieldMetadata(3, "dset1::" + it1.getResultClass().getFieldName(2),
				it1.getResultClass().getFieldName(2), it1.getResultClass().getFieldValueClass(2),
				it1.getResultClass().getFieldNativeTypeName(2), false));
		projectedColumns.add(new ResultFieldMetadata(4, "dset1::" + it1.getResultClass().getFieldName(3),
				it1.getResultClass().getFieldName(3), it1.getResultClass().getFieldValueClass(3),
				it1.getResultClass().getFieldNativeTypeName(3), false));

		projectedColumns.add(new ResultFieldMetadata(2, "dset2::" + it2.getResultClass().getFieldName(1),
				it2.getResultClass().getFieldName(1), it2.getResultClass().getFieldValueClass(1),
				it2.getResultClass().getFieldNativeTypeName(1), false));
		projectedColumns.add(new ResultFieldMetadata(5, "dset2::" + it2.getResultClass().getFieldName(2),
				it2.getResultClass().getFieldName(2), it2.getResultClass().getFieldValueClass(2),
				it2.getResultClass().getFieldNativeTypeName(2), false));
		projectedColumns.add(new ResultFieldMetadata(6, "dset2::" + it2.getResultClass().getFieldName(3),
				it2.getResultClass().getFieldName(3), it2.getResultClass().getFieldValueClass(3),
				it2.getResultClass().getFieldNativeTypeName(3), false));
		projectedColumns.add(new ResultFieldMetadata(7, "dset2::" + it2.getResultClass().getFieldName(4),
				it2.getResultClass().getFieldName(4), it2.getResultClass().getFieldValueClass(4),
				it2.getResultClass().getFieldNativeTypeName(4), false));

		ResultClass resultClass = new ResultClass(projectedColumns);
		JointResultMetadata meta = new JointResultMetadata(resultClass, isFromLeft, index);
		// JoinConditionMatcher matcher = new JoinConditionMatcher(
		// qr1.getResultIterator( ).getScope( ), qr2.getResultIterator( ).getScope( ),
		// new JoinConditionExpression(new ScriptExpression("row.ID"),new
		// ScriptExpression("row.ID"),0));
		List a = new ArrayList();
		a.add(new JoinCondition(new ScriptExpression("dataSetRow.ID"), new ScriptExpression("dataSetRow.ID"),
				IJoinCondition.OP_EQ));
		JoinConditionMatcher matcher = new JoinConditionMatcher(
				((ResultIterator) qr1.getResultIterator()).getOdiResult(),
				((ResultIterator) qr2.getResultIterator()).getOdiResult(), qr1.getQueryScope(), qr2.getQueryScope(), cx,
				a);
		IDataSetPopulator populator = null;

		int fetchRowLimit = 0;
		if (ADD_FETCH_LIMIT) {
			fetchRowLimit = 4;
		}

		if (populateType == BINARY_TREE_POPULATOR) {
			populator = JointDataSetPopulatorFactory.getBinaryTreeDataSetPopulator(it1, it2, meta, matcher, joinType,
					((DataEngineImpl) this.dataEngine).getSession(), fetchRowLimit);
		} else {
			populator = JointDataSetPopulatorFactory.getCartesianJointDataSetPopulator(it1, it2, meta, matcher,
					joinType, ((DataEngineImpl) this.dataEngine).getSession(), fetchRowLimit);
		}

		IResultObject ro;
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < resultClass.getFieldCount(); i++) {
			s.append(resultClass.getFieldName(i + 1)).append("\t\t\t");
		}
		s.append("\n");
		long start = System.currentTimeMillis();
		int count = 0;
		while ((ro = populator.next()) != null) {
			count++;
			if (count == 49) {
				System.out.print("ar");
			}
			for (int i = 0; i < resultClass.getFieldCount(); i++) {
				s.append(ro.getFieldValue(i + 1)).append("\t\t\t");
			}
			s.append("\n");
		}
		System.out.println(count + ":" + (System.currentTimeMillis() - start));
		return s.toString();
	}

	@Test
	public void testAlias() throws Exception {
		String fieldName = "dset2::CITY";
		OdaDataSetDesign dset = newDataSet("dset",
				"Select ID, CITY, STORE FROM " + this.getTestTableName() + " order by ID asc");

		List a = new ArrayList();
		a.add(new JoinCondition(new ScriptExpression("dataSetRow.ID"), new ScriptExpression("dataSetRow.ID"),
				IJoinCondition.OP_EQ));

		JointDataSetDesign dset2 = new JointDataSetDesign("dset2", dset.getName(), dset.getName(),
				IJointDataSetDesign.INNER_JOIN, a);
		ColumnDefinition columnDefinition = new ColumnDefinition(fieldName);
		columnDefinition.setAlias("Alias");
		dset2.addResultSetHint(columnDefinition);
		dataEngine.defineDataSet(dset2);

		QueryDefinition query = this.newReportQuery(dset2);
		IPreparedQuery preparedQuery = this.dataEngine.prepare(query);
		IQueryResults qr = preparedQuery.execute(null);
		IResultClass resultClass = ((ResultIterator) qr.getResultIterator()).getOdiResult().getResultClass();

		assertEquals(resultClass.getFieldAlias(resultClass.getFieldIndex(fieldName)), columnDefinition.getAlias());
	}

}

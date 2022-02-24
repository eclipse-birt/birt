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
package org.eclipse.birt.data.engine.regre;

import org.eclipse.birt.data.engine.api.APITestCase;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ColumnDefinition;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.SortDefinition;

import testutil.ConfigText;

import org.junit.Test;
import static org.junit.Assert.*;

public class SortHintTest extends APITestCase {
	public static final String[] COLS = new String[] { "CUSTOMERID", "DURATION", "CHARGE", "TONUMBER" };

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.APITestCase#getDataSourceInfo()
	 */
	protected DataSourceInfo getDataSourceInfo() {
		return new DataSourceInfo(ConfigText.getString("Api.TestSortHint.TableName"),
				ConfigText.getString("Api.TestSortHint.TableSQL"), "testSortHint.txt");
	}

	private QueryDefinition getQueryDefn() {
		QueryDefinition queryDefn = newReportQuery();

		IBaseExpression[] exprArray = new IBaseExpression[COLS.length];

		for (int i = 0; i < COLS.length; i++) {
			String exprName = COLS[i];
			IBaseExpression expr = new ScriptExpression("dataSetRow[\"" + COLS[i] + "\"]");
			queryDefn.addResultSetExpression(exprName, expr);
		}

		return queryDefn;
	}

	/**
	 * Test sort hint optimize.
	 * <P>
	 * Expected: Optimize sorting
	 */
	@Test
	public void testSortHintTableSort() {
		QueryDefinition queryDefn = getQueryDefn();

		// Add group
		// Group sorting:
		// col1: asc
		GroupDefinition[] gdArray = new GroupDefinition[1];
		GroupDefinition gd = new GroupDefinition();
		gd.setKeyExpression("row[\"" + COLS[0] + "\"]");
		gd.setSortDirection(ISortDefinition.SORT_ASC);
		gdArray[0] = gd;

		for (int i = 0; i < gdArray.length; i++)
			queryDefn.addGroup(gdArray[i]);

		// Add table sort
		// Table sorting:
		// col1: asc
		// col2: asc
		// col3: asc
		SortDefinition[] sdArray = new SortDefinition[3];
		SortDefinition sd = new SortDefinition();
		sd.setExpression("row[\"" + COLS[0] + "\"]");
		sd.setSortDirection(ISortDefinition.SORT_ASC);
		sdArray[0] = sd;

		sd = new SortDefinition();
		sd.setExpression("row[\"" + COLS[1] + "\"]");
		sd.setSortDirection(ISortDefinition.SORT_ASC);
		sdArray[1] = sd;

		sd = new SortDefinition();
		sd.setExpression("row[\"" + COLS[2] + "\"]");
		sd.setSortDirection(ISortDefinition.SORT_ASC);
		sdArray[2] = sd;

		for (int i = 0; i < sdArray.length; i++)
			queryDefn.addSort(sdArray[i]);

		// Add sort hints
		// col1: asc
		// col2: asc
		// ocl3: asc
		for (int i = 0; i < COLS.length - 1; i++) {
			sd = new SortDefinition();
			sd.setColumn("dataSetRow[\"" + COLS[i] + "\"]");
			sd.setSortDirection(ISortDefinition.SORT_ASC);
			dataSet.addSortHint(sd);
		}

		// No sorting
		executeAndCheck(queryDefn);

	}

	/**
	 * Expected: Optimize sorting
	 */
	@Test
	public void testSortHintTableSort2() {
		QueryDefinition queryDefn = getQueryDefn();

		// Add table sort
		// Table sorting:
		// col1: asc
		// col2: asc
		SortDefinition[] sdArray = new SortDefinition[2];
		SortDefinition sd = new SortDefinition();
		sd.setExpression("row[\"" + COLS[0] + "\"]");
		sd.setSortDirection(ISortDefinition.SORT_ASC);
		sdArray[0] = sd;

		sd = new SortDefinition();
		sd.setExpression("row[\"" + COLS[1] + "\"]");
		sd.setSortDirection(ISortDefinition.SORT_ASC);
		sdArray[1] = sd;

		for (int i = 0; i < sdArray.length; i++)
			queryDefn.addSort(sdArray[i]);

		// Add sort hints
		// col1: asc
		// col2: asc
		// ocl3: asc
		for (int i = 0; i < COLS.length - 1; i++) {
			sd = new SortDefinition();
			sd.setColumn("dataSetRow[\"" + COLS[i] + "\"]");
			sd.setSortDirection(ISortDefinition.SORT_ASC);
			dataSet.addSortHint(sd);
		}

		// No sorting
		executeAndCheck(queryDefn);

	}

	/**
	 * Expected: No optimize
	 */
	@Test
	public void testSortHintTableSort3() {
		QueryDefinition queryDefn = getQueryDefn();

		// Add table sort
		// Table sorting:
		// col1: asc
		// col3: asc
		SortDefinition[] sdArray = new SortDefinition[2];
		SortDefinition sd = new SortDefinition();
		sd.setExpression("row[\"" + COLS[0] + "\"]");
		sd.setSortDirection(ISortDefinition.SORT_ASC);
		sdArray[0] = sd;

		sd = new SortDefinition();
		sd.setExpression("row[\"" + COLS[2] + "\"]");
		sd.setSortDirection(ISortDefinition.SORT_ASC);
		sdArray[1] = sd;

		for (int i = 0; i < sdArray.length; i++)
			queryDefn.addSort(sdArray[i]);

		// Add sort hints
		// col1: asc
		// col2: asc
		// ocl3: asc
		for (int i = 0; i < COLS.length - 1; i++) {
			sd = new SortDefinition();
			sd.setColumn("dataSetRow[\"" + COLS[i] + "\"]");
			sd.setSortDirection(ISortDefinition.SORT_ASC);
			dataSet.addSortHint(sd);
		}

		executeAndCheck(queryDefn);

	}

	/**
	 * <P>
	 * Expected: No optimize
	 */
	@Test
	public void testSortHintTableSort4() {
		QueryDefinition queryDefn = getQueryDefn();

		// Add table sort
		// Table sorting:
		// col2: asc
		// col3: desc
		SortDefinition[] sdArray = new SortDefinition[2];
		SortDefinition sd = new SortDefinition();
		sd.setExpression("row[\"" + COLS[1] + "\"]");
		sd.setSortDirection(ISortDefinition.SORT_ASC);
		sdArray[0] = sd;

		sd = new SortDefinition();
		sd.setExpression("row[\"" + COLS[3] + "\"]");
		sd.setSortDirection(ISortDefinition.SORT_DESC);
		sdArray[1] = sd;

		for (int i = 0; i < sdArray.length; i++)
			queryDefn.addSort(sdArray[i]);

		// Add sort hints
		// col1: asc
		// col2: asc
		for (int i = 0; i < 2; i++) {
			sd = new SortDefinition();
			sd.setColumn("dataSetRow[\"" + COLS[i] + "\"]");
			sd.setSortDirection(ISortDefinition.SORT_ASC);
			dataSet.addSortHint(sd);
		}

		executeAndCheck(queryDefn);

	}

	/**
	 * Expected: Optimize sorting
	 */
	@Test
	public void testSortHintTableSort5() {
		QueryDefinition queryDefn = getQueryDefn();

		// Add group
		// Group sorting:
		// col1: asc
		// col2: asc
		GroupDefinition[] gdArray = new GroupDefinition[2];
		GroupDefinition gd = new GroupDefinition();
		gd.setKeyExpression("row[\"" + COLS[0] + "\"]");
		gd.setSortDirection(ISortDefinition.SORT_ASC);
		gdArray[0] = gd;

		gd = new GroupDefinition();
		gd.setKeyExpression("row[\"" + COLS[1] + "\"]");
		gd.setSortDirection(ISortDefinition.SORT_ASC);
		gdArray[1] = gd;

		for (int i = 0; i < gdArray.length; i++)
			queryDefn.addGroup(gdArray[i]);

		// Add table sort
		// Table sorting:
		// col1: asc
		SortDefinition[] sdArray = new SortDefinition[2];
		SortDefinition sd = new SortDefinition();
		sd.setExpression("row[\"" + COLS[0] + "\"]");
		sd.setSortDirection(ISortDefinition.SORT_ASC);
		sdArray[0] = sd;

		sd = new SortDefinition();
		sd.setExpression("row[\"" + COLS[1] + "\"]");
		sd.setSortDirection(ISortDefinition.SORT_ASC);
		sdArray[1] = sd;

		for (int i = 0; i < sdArray.length; i++)
			queryDefn.addSort(sdArray[i]);

		// Add sort hints
		// col1: asc
		// col2: asc
		// ocl3: asc
		for (int i = 0; i < COLS.length - 1; i++) {
			sd = new SortDefinition();
			sd.setColumn("dataSetRow[\"" + COLS[i] + "\"]");
			sd.setSortDirection(ISortDefinition.SORT_ASC);
			dataSet.addSortHint(sd);
		}

		// No sorting
		executeAndCheck(queryDefn);
	}

	/**
	 * Expected: No optimize
	 */
	@Test
	public void testSortHintTableSort6() {
		QueryDefinition queryDefn = getQueryDefn();

		// Add group
		// Group sorting:
		// col1: asc
		// col2: asc
		// col3: asc
		GroupDefinition[] gdArray = new GroupDefinition[3];
		GroupDefinition gd = new GroupDefinition();
		gd.setKeyExpression("row[\"" + COLS[0] + "\"]");
		gd.setSortDirection(ISortDefinition.SORT_ASC);
		gdArray[0] = gd;

		gd = new GroupDefinition();
		gd.setKeyExpression("row[\"" + COLS[1] + "\"]");
		gd.setSortDirection(ISortDefinition.SORT_ASC);
		gdArray[1] = gd;

		gd = new GroupDefinition();
		gd.setKeyExpression("row[\"" + COLS[2] + "\"]");
		gd.setSortDirection(ISortDefinition.SORT_ASC);
		gdArray[2] = gd;

		for (int i = 0; i < gdArray.length; i++)
			queryDefn.addGroup(gdArray[i]);

		// Add table sort
		// Table sorting:
		// col2: asc
		// col4: asc
		SortDefinition[] sdArray = new SortDefinition[2];
		SortDefinition sd = new SortDefinition();
		sd.setExpression("row[\"" + COLS[1] + "\"]");
		sd.setSortDirection(ISortDefinition.SORT_ASC);
		sdArray[0] = sd;

		sd = new SortDefinition();
		sd.setExpression("row[\"" + COLS[3] + "\"]");
		sd.setSortDirection(ISortDefinition.SORT_ASC);
		sdArray[1] = sd;

		for (int i = 0; i < sdArray.length; i++)
			queryDefn.addSort(sdArray[i]);

		// Add sort hints
		// col1: asc
		// col2: asc
		// ocl3: asc
		for (int i = 0; i < COLS.length - 1; i++) {
			sd = new SortDefinition();
			sd.setColumn("dataSetRow[\"" + COLS[i] + "\"]");
			sd.setSortDirection(ISortDefinition.SORT_ASC);
			dataSet.addSortHint(sd);
		}

		executeAndCheck(queryDefn);
	}

	/**
	 * Expected: Optimize sorting
	 */
	@Test
	public void testSortHintTableSort7() {
		QueryDefinition queryDefn = getQueryDefn();

		// Group sorting:
		// col1: asc
		// col2: asc
		// col3: asc
		GroupDefinition[] gdArray = new GroupDefinition[3];
		GroupDefinition gd = new GroupDefinition();
		gd.setKeyExpression("row[\"" + COLS[0] + "\"]");
		gd.setSortDirection(ISortDefinition.SORT_ASC);
		gdArray[0] = gd;

		gd = new GroupDefinition();
		gd.setKeyExpression("row[\"" + COLS[1] + "\"]");
		gd.setSortDirection(ISortDefinition.SORT_ASC);
		gdArray[1] = gd;

		gd = new GroupDefinition();
		gd.setKeyExpression("row[\"" + COLS[2] + "\"]");
		gd.setSortDirection(ISortDefinition.SORT_ASC);
		gdArray[2] = gd;

		for (int i = 0; i < gdArray.length; i++)
			queryDefn.addGroup(gdArray[i]);

		// Table sorting:
		// col4: asc
		SortDefinition[] sdArray = new SortDefinition[1];
		SortDefinition sd = new SortDefinition();
		sd.setExpression("row[\"" + COLS[3] + "\"]");
		sd.setSortDirection(ISortDefinition.SORT_ASC);
		sdArray[0] = sd;

		for (int i = 0; i < sdArray.length; i++)
			queryDefn.addSort(sdArray[i]);

		// Add sort hints
		// col1: asc
		// col2: asc
		// col3: asc
		// col4: asc
		for (int i = 0; i < COLS.length; i++) {
			sd = new SortDefinition();
			sd.setColumn("dataSetRow[\"" + COLS[i] + "\"]");
			sd.setSortDirection(ISortDefinition.SORT_ASC);
			dataSet.addSortHint(sd);
		}

		// No sorting
		executeAndCheck(queryDefn);
	}

	/**
	 * Expected: Optimize sorting
	 */
	@Test
	public void testSortHintTableSort8() {
		QueryDefinition queryDefn = getQueryDefn();

		// Add group
		// Group sorting:
		// col1: asc
		// col2: asc
		// col3: asc
		GroupDefinition[] gdArray = new GroupDefinition[3];
		GroupDefinition gd = new GroupDefinition();
		gd.setKeyExpression("row[\"" + COLS[0] + "\"]");
		gd.setSortDirection(ISortDefinition.SORT_ASC);
		gdArray[0] = gd;

		gd = new GroupDefinition();
		gd.setKeyExpression("row[\"" + COLS[1] + "\"]");
		gd.setSortDirection(ISortDefinition.SORT_ASC);
		gdArray[1] = gd;

		gd = new GroupDefinition();
		gd.setKeyExpression("row[\"" + COLS[2] + "\"]");
		gd.setSortDirection(ISortDefinition.SORT_ASC);
		gdArray[2] = gd;

		for (int i = 0; i < gdArray.length; i++)
			queryDefn.addGroup(gdArray[i]);

		// Add table sort
		// Table sorting:
		// col2: asc
		// col4: asc
		SortDefinition[] sdArray = new SortDefinition[2];
		SortDefinition sd = new SortDefinition();
		sd.setExpression("row[\"" + COLS[1] + "\"]");
		sd.setSortDirection(ISortDefinition.SORT_ASC);
		sdArray[0] = sd;

		sd = new SortDefinition();
		sd.setExpression("row[\"" + COLS[3] + "\"]");
		sd.setSortDirection(ISortDefinition.SORT_ASC);
		sdArray[1] = sd;

		for (int i = 0; i < sdArray.length; i++)
			queryDefn.addSort(sdArray[i]);

		// Add sort hints
		// col1: asc
		// col2: asc
		// col3: asc
		// col4: asc
		for (int i = 0; i < COLS.length; i++) {
			sd = new SortDefinition();
			sd.setColumn("dataSetRow[\"" + COLS[i] + "\"]");
			sd.setSortDirection(ISortDefinition.SORT_ASC);
			dataSet.addSortHint(sd);
		}

		// No sorting
		executeAndCheck(queryDefn);
	}

	/**
	 * Test sort hints no optimize because sorting does not match sort hints.
	 * <P>
	 * Expected: No optimize
	 */
	@Test
	public void testSortHintNoEffect() {
		QueryDefinition queryDefn = getQueryDefn();

		// Add group
		// col1: desc
		GroupDefinition[] gdArray = new GroupDefinition[1];
		GroupDefinition gd = new GroupDefinition();
		gd.setKeyExpression("row[\"" + COLS[0] + "\"]");
		gd.setSortDirection(ISortDefinition.SORT_DESC);
		gdArray[0] = gd;

		for (int i = 0; i < gdArray.length; i++)
			queryDefn.addGroup(gdArray[i]);

		// Add table sort
		// col1: desc
		// col2: desc
		// ocl3: asc
		SortDefinition[] sdArray = new SortDefinition[3];
		SortDefinition sd = new SortDefinition();
		sd.setExpression("row[\"" + COLS[0] + "\"]");
		sd.setSortDirection(ISortDefinition.SORT_DESC);
		sdArray[0] = sd;

		sd = new SortDefinition();
		sd.setExpression("row[\"" + COLS[1] + "\"]");
		sd.setSortDirection(ISortDefinition.SORT_DESC);
		sdArray[1] = sd;

		sd = new SortDefinition();
		sd.setExpression("row[\"" + COLS[2] + "\"]");
		sd.setSortDirection(ISortDefinition.SORT_ASC);
		sdArray[2] = sd;

		for (int i = 0; i < sdArray.length; i++)
			queryDefn.addSort(sdArray[i]);

		// Add sort hints
		// col1: asc
		// col2: asc
		// ocl3: asc
		for (int i = 0; i < COLS.length - 1; i++) {
			sd = new SortDefinition();
			sd.setColumn("dataSetRow[\"" + COLS[i] + "\"]");
			sd.setSortDirection(ISortDefinition.SORT_ASC);
			dataSet.addSortHint(sd);
		}

		executeAndCheck(queryDefn);
	}

	/**
	 * Test binding expression resolving on table sorting. Bind "ID" to
	 * dataSetRow["CUSTOMERID"]; Add table sort on row["ID"] and sort hint on
	 * dataSetRow["CUSTOMERID"]; Sort hint must resolve matching between row["ID"]
	 * and dataSetRow["CUSTOMERID"].
	 * <p>
	 * Expected: Optimize sorting
	 */
	@Test
	public void testSortHintResolve() {
		QueryDefinition queryDefn = getQueryDefn();
		queryDefn.addResultSetExpression("REFERCOL", new ScriptExpression("row[\"ID\"]"));
		queryDefn.addResultSetExpression("ID", new ScriptExpression("dataSetRow[\"" + COLS[0] + "\"]"));

		// Add table sort
		// col1: desc
		// col2: desc
		// ocl3: desc
		SortDefinition[] sdArray = new SortDefinition[3];
		SortDefinition sd = new SortDefinition();
		sd.setExpression("row[\"ID\"]");
		sd.setSortDirection(ISortDefinition.SORT_DESC);
		sdArray[0] = sd;

		sd = new SortDefinition();
		sd.setExpression("row[\"" + COLS[1] + "\"]");
		sd.setSortDirection(ISortDefinition.SORT_DESC);
		sdArray[1] = sd;

		sd = new SortDefinition();
		sd.setExpression("row[\"" + COLS[2] + "\"]");
		sd.setSortDirection(ISortDefinition.SORT_DESC);
		sdArray[2] = sd;

		for (int i = 0; i < sdArray.length; i++)
			queryDefn.addSort(sdArray[i]);

		// Add sort hint
		// col1: desc
		// col2: desc
		// col3: desc
		for (int i = 0; i < COLS.length - 1; i++) {
			sd = new SortDefinition();
			sd.setColumn("dataSetRow[\"" + COLS[i] + "\"]");
			sd.setSortDirection(ISortDefinition.SORT_DESC);
			dataSet.addSortHint(sd);
		}

		executeAndCheck(queryDefn);
	}

	/**
	 * Test binding expression resolving on table sorting. Define column Alias for
	 * the first three columns. eg. "CUSTOMERID_ALIAS" for "CUSTOMERID".
	 * <p>
	 * Group sorting and table sorting using desc order and alias name for sorting
	 * expression. eg. dataSetRow["CUSTOMERID_ALIAS"].
	 * <p>
	 * Sort hint must resolve column alias when matching row["CUSTOMERID_ALIAS"] and
	 * dataSetRow["CUSTOMERID"].
	 * <p>
	 * Expected: Optimize sorting; If the output is sorted by desc order, test
	 * failed.
	 */
	@Test
	public void testSortHintResolveColumnAlias() {
		QueryDefinition queryDefn = newReportQuery();

		IBaseExpression[] exprArray = new IBaseExpression[COLS.length];

		java.util.Map exprs = queryDefn.getResultSetExpressions();
		for (int i = 0; i < COLS.length; i++) {
			String exprName = COLS[i];
			IBaseExpression expr = new ScriptExpression("dataSetRow[\"" + COLS[i] + "_ALIAS\"]");
			queryDefn.addResultSetExpression(exprName, expr);
			exprs.put(exprName + "_ALIAS", expr);
		}

		// Group sorting
		// col1: desc
		// col2: desc
		// col3: desc
		GroupDefinition[] gdArray = new GroupDefinition[3];
		GroupDefinition gd = new GroupDefinition();
		gd.setKeyExpression("row[\"" + COLS[0] + "_ALIAS\"]");
		gd.setSortDirection(ISortDefinition.SORT_DESC);
		gdArray[0] = gd;

		gd = new GroupDefinition();
		gd.setKeyExpression("row[\"" + COLS[1] + "_ALIAS\"]");
		gd.setSortDirection(ISortDefinition.SORT_DESC);
		gdArray[1] = gd;

		gd = new GroupDefinition();
		gd.setKeyExpression("row[\"" + COLS[2] + "_ALIAS\"]");
		gd.setSortDirection(ISortDefinition.SORT_DESC);
		gdArray[2] = gd;

		for (int i = 0; i < gdArray.length; i++)
			queryDefn.addGroup(gdArray[i]);

		// Add table sort
		// col1: desc
		// col2: desc
		// ocl3: desc
		SortDefinition[] sdArray = new SortDefinition[3];
		SortDefinition sd = new SortDefinition();
		sd.setExpression("row[\"" + COLS[0] + "_ALIAS\"]");
		sd.setSortDirection(ISortDefinition.SORT_DESC);
		sdArray[0] = sd;

		sd = new SortDefinition();
		sd.setExpression("row[\"" + COLS[1] + "_ALIAS\"]");
		sd.setSortDirection(ISortDefinition.SORT_DESC);
		sdArray[1] = sd;

		sd = new SortDefinition();
		sd.setExpression("row[\"" + COLS[2] + "_ALIAS\"]");
		sd.setSortDirection(ISortDefinition.SORT_DESC);
		sdArray[2] = sd;

		for (int i = 0; i < sdArray.length; i++)
			queryDefn.addSort(sdArray[i]);

		// Add sort hint
		// col1: desc
		// col2: desc
		// col3: desc
		for (int i = 0; i < COLS.length - 1; i++) {
			sd = new SortDefinition();
			sd.setColumn("dataSetRow[\"" + COLS[i] + "\"]");
			sd.setSortDirection(ISortDefinition.SORT_DESC);
			dataSet.addSortHint(sd);
		}

		java.util.List resultSetHints = dataSet.getResultSetHints();
		for (int j = 0; j < COLS.length; j++) {
			ColumnDefinition cdefn = new ColumnDefinition(COLS[j]);
			cdefn.setAlias(COLS[j] + "_ALIAS");
			resultSetHints.add(cdefn);
		}

		executeAndCheck(queryDefn);
	}

	private void executeAndCheck(QueryDefinition queryDefn) {
		try {
			IResultIterator ri = executeQuery(queryDefn);
			while (ri.next()) {
				for (int i = 0; i < COLS.length; i++) {
					Object ob = ri.getValue(COLS[i]);
					if (ob == null)
						testPrint("null");
					else
						testPrint(ob.toString());

					if (i < COLS.length - 1)
						testPrint(",");
					else
						testPrintln("");
				}
			}
			ri.close();

			checkOutputFile();
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

}

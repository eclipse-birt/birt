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
package org.eclipse.birt.data.engine.olap.cursor;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.olap.OLAPException;
import javax.olap.cursor.CubeCursor;
import javax.olap.cursor.DimensionCursor;
import javax.olap.cursor.EdgeCursor;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.aggregation.api.IBuildInAggregation;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.DataEngineImpl;
import org.eclipse.birt.data.engine.impl.StopSign;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.data.api.cube.ICube;
import org.eclipse.birt.data.engine.olap.impl.query.CubeQueryExecutor;
import org.eclipse.birt.data.engine.olap.query.view.BirtCubeView;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.Scriptable;

import testutil.BaseTestCase;

public class CursorNavigatorTest extends BaseTestCase {
	private Scriptable scope;
	private DataEngineImpl de;
	private CubeUtility creator;
	private ICube cube;

	/*
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before
	public void cursorNavigatorSetUp() throws Exception {
		this.scope = new ImporterTopLevel();
		DataEngineContext context = DataEngineContext.newInstance(DataEngineContext.DIRECT_PRESENTATION, scope, null,
				null);
		context.setTmpdir(this.getTempDir());
		de = (DataEngineImpl) DataEngine.newDataEngine(context);
		creator = new CubeUtility();
		creator.createCube(de);
		cube = creator.getCube(CubeUtility.cubeName, de);
	}

	@After
	public void cursorNavigatorTearDown() throws Exception {
		cube.close();
		if (de != null) {
			de.shutdown();
			de = null;
		}
	}

	/**
	 *
	 * @throws OLAPException
	 * @throws BirtException
	 */
	@Test
	public void testCursorModel1() throws OLAPException, BirtException {
		ICubeQueryDefinition cqd = creator.createQueryDefinition();

		IBinding rowGrandTotal = new Binding("rowGrandTotal");
		rowGrandTotal.setAggrFunction(IBuildInAggregation.TOTAL_SUM_FUNC);
		rowGrandTotal.setExpression(new ScriptExpression("measure[\"measure1\"]"));
		rowGrandTotal.addAggregateOn("dimension[\"dimension5\"][\"level21\"]");
		rowGrandTotal.addAggregateOn("dimension[\"dimension6\"][\"level22\"]");

		IBinding columnGrandTotal = new Binding("columnGrandTotal");
		columnGrandTotal.setAggrFunction(IBuildInAggregation.TOTAL_SUM_FUNC);
		columnGrandTotal.setExpression(new ScriptExpression("measure[\"measure1\"]"));
		columnGrandTotal.addAggregateOn("dimension[\"dimension1\"][\"level11\"]");
		columnGrandTotal.addAggregateOn("dimension[\"dimension2\"][\"level12\"]");
		columnGrandTotal.addAggregateOn("dimension[\"dimension3\"][\"level13\"]");
		columnGrandTotal.addAggregateOn("dimension[\"dimension4\"][\"level14\"]");

		cqd.addBinding(rowGrandTotal);
		cqd.addBinding(columnGrandTotal);

		// Create cube view.
		BirtCubeView cubeView = new BirtCubeView(
				new CubeQueryExecutor(null, cqd, de.getSession(), this.scope, de.getContext()));

		CubeCursor dataCursor = cubeView.getCubeCursor(new StopSign(), cube);

		// retrieve the edge cursors
		// EdgeCursor pageCursor = cubeView.getMeasureEdgeView( );
		EdgeCursor rowCursor = cubeView.getRowEdgeView().getEdgeCursor();
		EdgeCursor columnCursor = cubeView.getColumnEdgeView().getEdgeCursor();

		DimensionCursor countryCursor = (DimensionCursor) columnCursor.getDimensionCursor().get(0);
		DimensionCursor cityCursor = (DimensionCursor) columnCursor.getDimensionCursor().get(1);
		DimensionCursor streetCursor = (DimensionCursor) columnCursor.getDimensionCursor().get(2);
		DimensionCursor timeCursor = (DimensionCursor) columnCursor.getDimensionCursor().get(3);

		// Test edgeCursor navigator
		// -------------------------------edgeCursor beforeFirst()--------------
		columnCursor.beforeFirst();
		assertTrue(countryCursor.isBeforeFirst());
		assertTrue(cityCursor.isBeforeFirst());
		assertTrue(streetCursor.isBeforeFirst());
		assertTrue(timeCursor.isBeforeFirst());

		// -------------------------------edgeCursor afterFirst()--------------
		columnCursor.afterLast();
		assertTrue(countryCursor.isAfterLast());
		assertTrue(cityCursor.isAfterLast());
		assertTrue(streetCursor.isAfterLast());
		assertTrue(timeCursor.isAfterLast());

		// -------------------------------edgeCursor first()--------------
		columnCursor.first();
		assertTrue(columnCursor.isFirst());
		assertTrue(countryCursor.isFirst());
		assertTrue(cityCursor.isFirst());
		assertTrue(streetCursor.isFirst());
		assertTrue(timeCursor.isFirst());

		// -------------------------------edgeCursor last()-------------
		columnCursor.last();
		assertTrue(columnCursor.last());
		assertTrue(countryCursor.last());
		assertTrue(cityCursor.last());
		assertTrue(streetCursor.last());
		assertTrue(timeCursor.last());

		// -------------------------------edgeCursor setPosition()--------------
		columnCursor.setPosition(5);
		assertTrue(countryCursor.getObject("level11").equals("CN"));
		assertTrue(cityCursor.getObject("level12").equals("SH"));
		assertTrue(streetCursor.getObject("level13").equals("A1"));
		assertTrue(timeCursor.getObject("level14").equals("2000"));

		// -------------------------------edgeCursor previous()--------------
		columnCursor.previous();
		assertTrue(countryCursor.getObject("level11").equals("CN"));
		assertTrue(cityCursor.getObject("level12").equals("SH"));
		assertTrue(streetCursor.getObject("level13").equals("A1"));
		assertTrue(timeCursor.getObject("level14").equals("1998"));

		// -------------------------------edgeCursor setPosition()--------------
		columnCursor.setPosition(24);
		try {
			countryCursor.getObject("level11");
			fail("should never get here!!");
		} catch (OLAPException e) {
		}

		// -------------------------------edgeCursor relative()--------------
		columnCursor.beforeFirst();
		columnCursor.relative(6);
		assertTrue(countryCursor.getObject("level11").equals("CN"));
		assertTrue(cityCursor.getObject("level12").equals("SH"));
		assertTrue(streetCursor.getObject("level13").equals("A1"));
		assertTrue(timeCursor.getObject("level14").equals("2000"));

		// -------------------------------edgeCursor
		// beforeFirst(),next(),setPosition()--------------
		columnCursor.beforeFirst();
		columnCursor.next();
		columnCursor.next();
		columnCursor.next();
		columnCursor.setPosition(1);
		assertTrue(countryCursor.getObject("level11").equals("CN"));
		assertTrue(cityCursor.getObject("level12").equals("BJ"));
		assertTrue(streetCursor.getObject("level13").equals("A1"));
		assertTrue(timeCursor.getObject("level14").equals("2001"));

		// ------------------------------dimensionCursor setPosition()--------------
		columnCursor.beforeFirst();
		columnCursor.setPosition(4);
		timeCursor.setPosition(1);
		assertTrue(countryCursor.getObject("level11").equals("CN"));
		assertTrue(cityCursor.getObject("level12").equals("SH"));
		assertTrue(streetCursor.getObject("level13").equals("A1"));
		assertTrue(timeCursor.getObject("level14").equals("2000"));

		// ------------------------------dimensionCursor next()--------------
		columnCursor.beforeFirst();
		columnCursor.next();
		countryCursor.next();
		assertTrue(countryCursor.getObject("level11").equals("JP"));
		assertTrue(cityCursor.getObject("level12").equals("IL"));
		assertTrue(streetCursor.getObject("level13").equals("A4"));
		assertTrue(timeCursor.getObject("level14").equals("1999"));

		columnCursor.afterLast();
		try {
			streetCursor.getObject("level13");
			fail("should not get here");
		} catch (OLAPException e) {
		}

		// ------------------------------dimensionCursor
		// setPosition(),getEdgeStart(),getEdgeEnd()--------------
		columnCursor.beforeFirst();
		columnCursor.setPosition(1);
		assertTrue(countryCursor.getEdgeStart() == 0);
		assertTrue(countryCursor.getEdgeEnd() == 7);

		assertTrue(cityCursor.getEdgeStart() == 0);
		assertTrue(cityCursor.getEdgeEnd() == 2);

		assertTrue(streetCursor.getEdgeStart() == 0);
		assertTrue(streetCursor.getEdgeEnd() == 1);

		assertTrue(timeCursor.getEdgeStart() == 1);
		assertTrue(timeCursor.getEdgeEnd() == 1);

		columnCursor.setPosition(9);
		assertTrue(countryCursor.getEdgeStart() == 8);
		assertTrue(countryCursor.getEdgeEnd() == 11);

		assertTrue(cityCursor.getEdgeStart() == 8);
		assertTrue(cityCursor.getEdgeEnd() == 10);

		assertTrue(streetCursor.getEdgeStart() == 8);
		assertTrue(streetCursor.getEdgeEnd() == 10);

		assertTrue(timeCursor.getEdgeStart() == 9);
		assertTrue(timeCursor.getEdgeEnd() == 9);

		columnCursor.setPosition(23);
		assertTrue(countryCursor.getEdgeStart() == 15);
		assertTrue(countryCursor.getEdgeEnd() == 23);

		assertTrue(cityCursor.getEdgeStart() == 23);
		assertTrue(cityCursor.getEdgeEnd() == 23);

		assertTrue(streetCursor.getEdgeStart() == 23);
		assertTrue(streetCursor.getEdgeEnd() == 23);

		assertTrue(timeCursor.getEdgeStart() == 23);
		assertTrue(timeCursor.getEdgeEnd() == 23);

		columnCursor.setPosition(12);
		assertTrue(countryCursor.getEdgeStart() == 12);
		assertTrue(countryCursor.getEdgeEnd() == 14);

		assertTrue(cityCursor.getEdgeStart() == 12);
		assertTrue(cityCursor.getEdgeEnd() == 13);

		assertTrue(streetCursor.getEdgeStart() == 12);
		assertTrue(streetCursor.getEdgeEnd() == 12);

		assertTrue(timeCursor.getEdgeStart() == 12);
		assertTrue(timeCursor.getEdgeEnd() == 12);

		columnCursor.beforeFirst();
		columnCursor.setPosition(24);
		assertTrue(countryCursor.getEdgeStart() == -1);
		assertTrue(countryCursor.getEdgeEnd() == -1);
		close(dataCursor);
	}

	@Test
	public void testNavigator() throws DataException, OLAPException, IOException {
		ICubeQueryDefinition cqd = creator.createQueryDefinition();

		IBinding rowGrandTotal = new Binding("rowGrandTotal");
		rowGrandTotal.setAggrFunction(IBuildInAggregation.TOTAL_SUM_FUNC);
		rowGrandTotal.setExpression(new ScriptExpression("measure[\"measure1\"]"));
		rowGrandTotal.addAggregateOn("dimension[\"dimension5\"][\"level21\"]");
		rowGrandTotal.addAggregateOn("dimension[\"dimension6\"][\"level22\"]");

		IBinding columnGrandTotal = new Binding("columnGrandTotal");
		columnGrandTotal.setAggrFunction(IBuildInAggregation.TOTAL_SUM_FUNC);
		columnGrandTotal.setExpression(new ScriptExpression("measure[\"measure1\"]"));
		columnGrandTotal.addAggregateOn("dimension[\"dimension1\"][\"level11\"]");
		columnGrandTotal.addAggregateOn("dimension[\"dimension2\"][\"level12\"]");
		columnGrandTotal.addAggregateOn("dimension[\"dimension3\"][\"level13\"]");
		columnGrandTotal.addAggregateOn("dimension[\"dimension4\"][\"level14\"]");

		cqd.addBinding(rowGrandTotal);
		cqd.addBinding(columnGrandTotal);

		// Create cube view.
		BirtCubeView cubeView = new BirtCubeView(
				new CubeQueryExecutor(null, cqd, de.getSession(), this.scope, de.getContext()));

		CubeCursor dataCursor = cubeView.getCubeCursor(new StopSign(), cube);

		// retrieve the edge cursors
		// EdgeCursor pageCursor = cubeView.getMeasureEdgeView( );
		EdgeCursor rowCursor = cubeView.getRowEdgeView().getEdgeCursor();
		EdgeCursor columnCursor = cubeView.getColumnEdgeView().getEdgeCursor();

		DimensionCursor countryCursor = (DimensionCursor) columnCursor.getDimensionCursor().get(0);
		DimensionCursor cityCursor = (DimensionCursor) columnCursor.getDimensionCursor().get(1);
		DimensionCursor streetCursor = (DimensionCursor) columnCursor.getDimensionCursor().get(2);
		DimensionCursor timeCursor = (DimensionCursor) columnCursor.getDimensionCursor().get(3);

		columnCursor.beforeFirst();
		String out = "";
		while (columnCursor.next()) {
			out += "edgePosition is " + columnCursor.getPosition() + "\n";
			out += "country edge start at " + countryCursor.getEdgeStart() + "\n";
			out += "country edge end at " + countryCursor.getEdgeEnd() + "\n";
			out += "city edge start at " + cityCursor.getEdgeStart() + "\n";
			out += "city edge end at " + cityCursor.getEdgeEnd() + "\n";
			out += "street edge start at " + streetCursor.getEdgeStart() + "\n";
			out += "street edge end at " + streetCursor.getEdgeEnd() + "\n";
			out += "time edge start at " + timeCursor.getEdgeStart() + "\n";
			out += "time edge end at " + timeCursor.getEdgeEnd() + "\n";
			out += "\n";
		}
		System.out.print(out);
		testOut.print(out);
		checkOutputFile();
		close(dataCursor);
	}

	@Test
	public void testNavigatorOnSubCursor() throws DataException, OLAPException, IOException {
		ICubeQueryDefinition cqd = creator.createQueryDefinition();

		IBinding rowGrandTotal = new Binding("rowGrandTotal");
		rowGrandTotal.setAggrFunction(IBuildInAggregation.TOTAL_SUM_FUNC);
		rowGrandTotal.setExpression(new ScriptExpression("measure[\"measure1\"]"));
		rowGrandTotal.addAggregateOn("dimension[\"dimension5\"][\"level21\"]");
		rowGrandTotal.addAggregateOn("dimension[\"dimension6\"][\"level22\"]");

		IBinding columnGrandTotal = new Binding("columnGrandTotal");
		columnGrandTotal.setAggrFunction(IBuildInAggregation.TOTAL_SUM_FUNC);
		columnGrandTotal.setExpression(new ScriptExpression("measure[\"measure1\"]"));
		columnGrandTotal.addAggregateOn("dimension[\"dimension1\"][\"level11\"]");
		columnGrandTotal.addAggregateOn("dimension[\"dimension2\"][\"level12\"]");
		columnGrandTotal.addAggregateOn("dimension[\"dimension3\"][\"level13\"]");
		columnGrandTotal.addAggregateOn("dimension[\"dimension4\"][\"level14\"]");

		cqd.addBinding(rowGrandTotal);
		cqd.addBinding(columnGrandTotal);

		// Create cube view.
		BirtCubeView cubeView = new BirtCubeView(
				new CubeQueryExecutor(null, cqd, de.getSession(), this.scope, de.getContext()));
		CubeCursor dataCursor = cubeView.getCubeCursor(new StopSign(), cube);

		// retrieve the edge cursors
		// EdgeCursor pageCursor = cubeView.getMeasureEdgeView( );
		EdgeCursor rowCursor = cubeView.getRowEdgeView().getEdgeCursor();
		EdgeCursor columnCursor = cubeView.getColumnEdgeView().getEdgeCursor();

		BirtCubeView subCubeView = new BirtCubeView(cubeView.getCubeQueryExecutor(), cube, null, null);

		columnCursor.beforeFirst();
		rowCursor.next();
		columnCursor.setPosition(8);
		CubeCursor subCursor = subCubeView.getCubeCursor(new StopSign(), "dimension[\"dimension1\"][\"level11\"]",
				"dimension[\"dimension5\"][\"level21\"]", cubeView);

		EdgeCursor subRowCursor = subCubeView.getRowEdgeView().getEdgeCursor();
		EdgeCursor subColumnCursor = subCubeView.getColumnEdgeView().getEdgeCursor();
		subRowCursor.beforeFirst();
		subColumnCursor.beforeFirst();
		assertTrue(subRowCursor.isBeforeFirst());
		assertTrue(subColumnCursor.isBeforeFirst());
		subRowCursor.first();
		subColumnCursor.first();
		assertTrue(subRowCursor.isFirst());
		assertTrue(subColumnCursor.isFirst());
		assertTrue(subRowCursor.getPosition() == 0);
		assertTrue(subColumnCursor.getPosition() == 0);
		subColumnCursor.setPosition(3);
		assertTrue(subColumnCursor.getPosition() == 3);

		subRowCursor.beforeFirst();
		subRowCursor.next();
		subColumnCursor.setPosition(3);
		DimensionCursor c1 = (DimensionCursor) subColumnCursor.getDimensionCursor().get(0);
		DimensionCursor c2 = (DimensionCursor) subColumnCursor.getDimensionCursor().get(1);
		DimensionCursor c3 = (DimensionCursor) subColumnCursor.getDimensionCursor().get(2);
		DimensionCursor c4 = (DimensionCursor) subColumnCursor.getDimensionCursor().get(3);
		assertTrue(c1.getObject(0).toString().equals("JP"));
		assertTrue(c2.getObject(0).toString().equals("TK"));
		assertTrue(c3.getObject(0).toString().equals("A4"));
		assertTrue(c4.getObject(0).toString().equals("1999"));

		columnCursor.setPosition(5);
		subCursor = subCubeView.getCubeCursor(new StopSign(), "dimension[\"dimension1\"][\"level11\"]",
				"dimension[\"dimension5\"][\"level21\"]", cubeView);

		subRowCursor = subCubeView.getRowEdgeView().getEdgeCursor();
		subColumnCursor = subCubeView.getColumnEdgeView().getEdgeCursor();
		subColumnCursor.setPosition(7);
		c1 = (DimensionCursor) subColumnCursor.getDimensionCursor().get(0);
		c2 = (DimensionCursor) subColumnCursor.getDimensionCursor().get(1);
		c3 = (DimensionCursor) subColumnCursor.getDimensionCursor().get(2);
		c4 = (DimensionCursor) subColumnCursor.getDimensionCursor().get(3);
		assertTrue(c1.getObject(0).toString().equals("CN"));
		assertTrue(c2.getObject(0).toString().equals("SZ"));
		assertTrue(c3.getObject(0).toString().equals("A1"));
		assertTrue(c4.getObject(0).toString().equals("1998"));

		close(dataCursor);
	}

	@Test
	public void testNavigatorOnPage() throws Exception {
		ICubeQueryDefinition cqd = creator.createQueryDefintionWithPage1();

		IBinding rowGrandTotal = new Binding("rowGrandTotal");
		rowGrandTotal.setAggrFunction(IBuildInAggregation.TOTAL_SUM_FUNC);
		rowGrandTotal.setExpression(new ScriptExpression("measure[\"measure1\"]"));
		rowGrandTotal.addAggregateOn("dimension[\"dimension4\"][\"level14\"]");
		rowGrandTotal.addAggregateOn("dimension[\"dimension5\"][\"level21\"]");
		rowGrandTotal.addAggregateOn("dimension[\"dimension6\"][\"level22\"]");

		IBinding columnGrandTotal = new Binding("columnGrandTotal");
		columnGrandTotal.setAggrFunction(IBuildInAggregation.TOTAL_AVE_FUNC);
		columnGrandTotal.setExpression(new ScriptExpression("measure[\"measure1\"]"));
		columnGrandTotal.addAggregateOn("dimension[\"dimension4\"][\"level14\"]");
		columnGrandTotal.addAggregateOn("dimension[\"dimension1\"][\"level11\"]");
		columnGrandTotal.addAggregateOn("dimension[\"dimension2\"][\"level12\"]");
		columnGrandTotal.addAggregateOn("dimension[\"dimension3\"][\"level13\"]");

		IBinding totalGrandTotal = new Binding("totalGrandTotal");
		totalGrandTotal.setAggrFunction(IBuildInAggregation.TOTAL_SUM_FUNC);
		totalGrandTotal.setExpression(new ScriptExpression("measure[\"measure1\"]"));
		totalGrandTotal.addAggregateOn("dimension[\"dimension4\"][\"level14\"]");

		cqd.addBinding(rowGrandTotal);
		cqd.addBinding(columnGrandTotal);
		cqd.addBinding(totalGrandTotal);

		// Create cube view.
		BirtCubeView cubeView = new BirtCubeView(
				new CubeQueryExecutor(null, cqd, de.getSession(), this.scope, de.getContext()), cube, null, null);

		CubeCursor dataCursor = cubeView.getCubeCursor(new StopSign(), cube);
		EdgeCursor pageCursor = (EdgeCursor) dataCursor.getPageEdge().toArray()[0];

		List columnEdgeBindingNames = new ArrayList();
		columnEdgeBindingNames.add("level11");
		columnEdgeBindingNames.add("level12");
		columnEdgeBindingNames.add("level13");

		List rowEdgeBindingNames = new ArrayList();
		rowEdgeBindingNames.add("level21");
		rowEdgeBindingNames.add("level22");

		List measureBindingNames = new ArrayList();
		measureBindingNames.add("measure1");

		List rowGrandTotalNames = new ArrayList();
		rowGrandTotalNames.add("rowGrandTotal");

		pageCursor.setPosition(0);
		dataCursor.synchronizePages();
		String output = "The NO." + pageCursor.getPosition() + " is:";
		for (int i = 0; i < pageCursor.getDimensionCursor().size(); i++) {
			output += ((DimensionCursor) pageCursor.getDimensionCursor().get(i)).getObject(0);
		}
		output += "\n";
		output += this.creator.printCubeAlongEdge(dataCursor, columnEdgeBindingNames, rowEdgeBindingNames,
				measureBindingNames, rowGrandTotalNames, "columnGrandTotal", "totalGrandTotal", null);
		output += "\n";

		pageCursor.setPosition(3);
		dataCursor.synchronizePages();
		output += "The NO." + pageCursor.getPosition() + " is:";
		for (int i = 0; i < pageCursor.getDimensionCursor().size(); i++) {
			output += ((DimensionCursor) pageCursor.getDimensionCursor().get(i)).getObject(0);
		}
		output += "\n";
		output += this.creator.printCubeAlongEdge(dataCursor, columnEdgeBindingNames, rowEdgeBindingNames,
				measureBindingNames, rowGrandTotalNames, "columnGrandTotal", "totalGrandTotal", null);
		output += "\n";

		pageCursor.setPosition(1);
		dataCursor.synchronizePages();
		output += "The NO." + pageCursor.getPosition() + " is:";
		for (int i = 0; i < pageCursor.getDimensionCursor().size(); i++) {
			output += ((DimensionCursor) pageCursor.getDimensionCursor().get(i)).getObject(0);
		}
		output += "\n";
		output += this.creator.printCubeAlongEdge(dataCursor, columnEdgeBindingNames, rowEdgeBindingNames,
				measureBindingNames, rowGrandTotalNames, "columnGrandTotal", "totalGrandTotal", null);
		output += "\n";

		pageCursor.last();
		dataCursor.synchronizePages();
		output += "The NO." + pageCursor.getPosition() + " is:";
		for (int i = 0; i < pageCursor.getDimensionCursor().size(); i++) {
			output += ((DimensionCursor) pageCursor.getDimensionCursor().get(i)).getObject(0);
		}
		output += "\n";
		output += this.creator.printCubeAlongEdge(dataCursor, columnEdgeBindingNames, rowEdgeBindingNames,
				measureBindingNames, rowGrandTotalNames, "columnGrandTotal", "totalGrandTotal", null);
		output += "\n";

		testOut.print(output);
		checkOutputFile();
		close(dataCursor);
	}

	/**
	 *
	 * @param dataCursor
	 * @throws OLAPException
	 */
	private void close(CubeCursor dataCursor) throws OLAPException {
		for (int i = 0; i < dataCursor.getOrdinateEdge().size(); i++) {
			EdgeCursor edge = (EdgeCursor) (dataCursor.getOrdinateEdge().get(i));
			edge.close();
		}
		dataCursor.close();
	}

}

/*******************************************************************************
 * Copyright (c) 2004 ,2005 Actuate Corporation.
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.DataEngineImpl;
import org.eclipse.birt.data.engine.impl.StopSign;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IDimensionDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IHierarchyDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IMeasureDefinition;
import org.eclipse.birt.data.engine.olap.data.api.cube.ICube;
import org.eclipse.birt.data.engine.olap.impl.query.CubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.impl.query.CubeQueryExecutor;
import org.eclipse.birt.data.engine.olap.query.view.BirtCubeView;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.Scriptable;

import testutil.BaseTestCase;

public class CursorModelTest extends BaseTestCase {
	private Scriptable scope;
	private DataEngineImpl de;
	private CubeUtility creator;
	private ICube cube;

	/*
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before
	public void cursorModelSetUp() throws Exception {
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
	public void cursorModelTearDown() throws Exception {
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
	 * @throws IOException
	 */
	@Test
	public void testCursorModel1() throws OLAPException, BirtException, IOException {
		ICubeQueryDefinition cqd = creator.createQueryDefinition();

		IBinding rowGrandTotal = new Binding("rowGrandTotal");
		rowGrandTotal.setAggrFunction(IBuildInAggregation.TOTAL_SUM_FUNC);
		rowGrandTotal.setExpression(new ScriptExpression("measure[\"measure1\"]"));
		rowGrandTotal.addAggregateOn("dimension[\"dimension5\"][\"level21\"]");
		rowGrandTotal.addAggregateOn("dimension[\"dimension6\"][\"level22\"]");

		IBinding columnGrandTotal = new Binding("columnGrandTotal");
		columnGrandTotal.setAggrFunction(IBuildInAggregation.TOTAL_AVE_FUNC);
		columnGrandTotal.setExpression(new ScriptExpression("measure[\"measure1\"]"));
		columnGrandTotal.addAggregateOn("dimension[\"dimension1\"][\"level11\"]");
		columnGrandTotal.addAggregateOn("dimension[\"dimension2\"][\"level12\"]");
		columnGrandTotal.addAggregateOn("dimension[\"dimension3\"][\"level13\"]");
		columnGrandTotal.addAggregateOn("dimension[\"dimension4\"][\"level14\"]");

		IBinding totalGrandTotal = new Binding("totalGrandTotal");
		totalGrandTotal.setAggrFunction(IBuildInAggregation.TOTAL_SUM_FUNC);
		totalGrandTotal.setExpression(new ScriptExpression("measure[\"measure1\"]"));

		cqd.addBinding(rowGrandTotal);
		cqd.addBinding(columnGrandTotal);
		cqd.addBinding(totalGrandTotal);

		// Create cube view.
		BirtCubeView cubeView = new BirtCubeView(
				new CubeQueryExecutor(null, cqd, de.getSession(), this.scope, de.getContext()));

		CubeCursor dataCursor = cubeView.getCubeCursor(new StopSign(), cube);

		List columnEdgeBindingNames = new ArrayList();
		columnEdgeBindingNames.add("level11");
		columnEdgeBindingNames.add("level12");
		columnEdgeBindingNames.add("level13");
		columnEdgeBindingNames.add("level14");

		List rowEdgeBindingNames = new ArrayList();
		rowEdgeBindingNames.add("level21");
		rowEdgeBindingNames.add("level22");

		List measureBindingNames = new ArrayList();
		measureBindingNames.add("measure1");

		List rowGrandTotalNames = new ArrayList();
		rowGrandTotalNames.add("rowGrandTotal");

		try {
			testOut.print(creator.printCubeAlongEdge(dataCursor, columnEdgeBindingNames, rowEdgeBindingNames,
					measureBindingNames, rowGrandTotalNames, "columnGrandTotal", "totalGrandTotal", null));
			this.checkOutputFile();
		} catch (Exception e) {
			fail("fail to get here!");
		}
		CubeUtility.close(dataCursor);
	}

	/**
	 * without row edge
	 *
	 * @throws OLAPException
	 * @throws BirtException
	 */
	@Test
	public void testCursorModel2() throws OLAPException, BirtException {
		ICubeQueryDefinition cqd = new CubeQueryDefinition(CubeUtility.cubeName);

		IMeasureDefinition measure = cqd.createMeasure("measure1");
		measure.setAggrFunction("SUM");

		IEdgeDefinition columnEdge = cqd.createEdge(ICubeQueryDefinition.COLUMN_EDGE);
		IDimensionDefinition dim1 = columnEdge.createDimension("dimension1");
		IHierarchyDefinition hier1 = dim1.createHierarchy("dimension1");
		hier1.createLevel("level11");
		IDimensionDefinition dim2 = columnEdge.createDimension("dimension2");
		IHierarchyDefinition hier2 = dim2.createHierarchy("dimension2");
		hier2.createLevel("level12");
		IDimensionDefinition dim3 = columnEdge.createDimension("dimension3");
		IHierarchyDefinition hier3 = dim3.createHierarchy("dimension3");
		hier3.createLevel("level13");
		IDimensionDefinition dim4 = columnEdge.createDimension("dimension4");
		IHierarchyDefinition hier4 = dim4.createHierarchy("dimension4");
		hier4.createLevel("level14");

		// Create cube view.
		BirtCubeView cubeView = new BirtCubeView(
				new CubeQueryExecutor(null, cqd, de.getSession(), this.scope, de.getContext()));

		CubeCursor dataCursor = cubeView.getCubeCursor(new StopSign(), cube);

		// retrieve the edge cursors
		EdgeCursor columnCursor = cubeView.getColumnEdgeView().getEdgeCursor();

		DimensionCursor countryCursor = (DimensionCursor) columnCursor.getDimensionCursor().get(0);
		DimensionCursor cityCursor = (DimensionCursor) columnCursor.getDimensionCursor().get(1);
		DimensionCursor streetCursor = (DimensionCursor) columnCursor.getDimensionCursor().get(2);
		DimensionCursor timeCursor = (DimensionCursor) columnCursor.getDimensionCursor().get(3);

		testOut.print(creator.printCubeAlongDimension(dataCursor, countryCursor, cityCursor, streetCursor, timeCursor,
				null, null));
		try {
			this.checkOutputFile();
		} catch (Exception e) {
			fail("fail to get here!");
		}
		CubeUtility.close(dataCursor);
	}

	/**
	 * without column edge
	 *
	 * @throws OLAPException
	 * @throws BirtException
	 */
	@Test
	public void testCursorModel3() throws OLAPException, BirtException {
		ICubeQueryDefinition cqd = new CubeQueryDefinition(CubeUtility.cubeName);

		IMeasureDefinition measure = cqd.createMeasure("measure1");
		measure.setAggrFunction("SUM");
		IEdgeDefinition rowEdge = cqd.createEdge(ICubeQueryDefinition.ROW_EDGE);
		IDimensionDefinition dim1 = rowEdge.createDimension("dimension5");
		IHierarchyDefinition hier1 = dim1.createHierarchy("dimension5");
		hier1.createLevel("level21");

		IDimensionDefinition dim2 = rowEdge.createDimension("dimension6");
		IHierarchyDefinition hier2 = dim2.createHierarchy("dimension6");
		hier2.createLevel("level22");

		// Create cube view.
		BirtCubeView cubeView = new BirtCubeView(
				new CubeQueryExecutor(null, cqd, de.getSession(), this.scope, de.getContext()));

		CubeCursor dataCursor = cubeView.getCubeCursor(new StopSign(), cube);

		// retrieve the edge cursors
		// EdgeCursor pageCursor = cubeView.getMeasureEdgeView( );
		EdgeCursor rowCursor = cubeView.getRowEdgeView().getEdgeCursor();

		DimensionCursor productCursor1 = (DimensionCursor) rowCursor.getDimensionCursor().get(0);
		DimensionCursor productCursor2 = (DimensionCursor) rowCursor.getDimensionCursor().get(1);

		testOut.print(
				creator.printCubeAlongDimension(dataCursor, null, null, null, null, productCursor1, productCursor2));
		try {
			this.checkOutputFile();
		} catch (Exception e) {
			fail("fail to get here!");
		}
		CubeUtility.close(dataCursor);
	}

	/**
	 * test populate data along dimension cursor
	 *
	 * @throws DataException
	 * @throws OLAPException
	 */
	@Test
	public void testCursorModel4() throws DataException, OLAPException {

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

		List columnEdgeBindingNames = new ArrayList();
		columnEdgeBindingNames.add("level11");
		columnEdgeBindingNames.add("level12");
		columnEdgeBindingNames.add("level13");
		columnEdgeBindingNames.add("level14");

		List rowEdgeBindingNames = new ArrayList();
		rowEdgeBindingNames.add("level21");
		rowEdgeBindingNames.add("level22");

		List measureBindingNames = new ArrayList();
		measureBindingNames.add("measure1");

		List dimCursorOnColumn = cubeView.getColumnEdgeView().getEdgeCursor().getDimensionCursor();
		List dimCursorOnRow = cubeView.getRowEdgeView().getEdgeCursor().getDimensionCursor();

		testOut.print(creator.printCubeAlongDimension(dataCursor, (DimensionCursor) dimCursorOnColumn.get(0),
				(DimensionCursor) dimCursorOnColumn.get(1), (DimensionCursor) dimCursorOnColumn.get(2),
				(DimensionCursor) dimCursorOnColumn.get(3), (DimensionCursor) dimCursorOnRow.get(0),
				(DimensionCursor) dimCursorOnRow.get(1)));

		try {
			this.checkOutputFile();
		} catch (Exception e) {
			fail("fail to get here!");
		}
		CubeUtility.close(dataCursor);
	}

	/**
	 *
	 *
	 * @throws OLAPException
	 * @throws BirtException
	 */
	@Test
	public void testCursorModel5() throws OLAPException, BirtException {
		ICubeQueryDefinition cqd = this.creator.createQueryDefinition();

		IBinding rowGrandTotal = new Binding("rowGrandTotal");
		rowGrandTotal.setAggrFunction(IBuildInAggregation.TOTAL_SUM_FUNC);
		rowGrandTotal.setExpression(new ScriptExpression("measure[\"measure1\"]"));
		rowGrandTotal.addAggregateOn("dimension[\"dimension5\"][\"level21\"]");
		rowGrandTotal.addAggregateOn("dimension[\"dimension6\"][\"level22\"]");

		IBinding rowGrandAvg = new Binding("rowGrandAvg");
		rowGrandAvg.setAggrFunction(IBuildInAggregation.TOTAL_AVE_FUNC);
		rowGrandAvg.setExpression(new ScriptExpression("measure[\"measure1\"]"));
		rowGrandAvg.addAggregateOn("dimension[\"dimension5\"][\"level21\"]");
		rowGrandAvg.addAggregateOn("dimension[\"dimension6\"][\"level22\"]");

		cqd.addBinding(rowGrandTotal);
		cqd.addBinding(rowGrandAvg);

		// Create cube view.
		BirtCubeView cubeView = new BirtCubeView(
				new CubeQueryExecutor(null, cqd, de.getSession(), this.scope, de.getContext()));

		CubeCursor dataCursor = cubeView.getCubeCursor(new StopSign(), cube);

		List columnEdgeBindingNames = new ArrayList();
		columnEdgeBindingNames.add("level11");
		columnEdgeBindingNames.add("level12");
		columnEdgeBindingNames.add("level13");
		columnEdgeBindingNames.add("level14");

		List rowEdgeBindingNames = new ArrayList();
		rowEdgeBindingNames.add("level21");
		rowEdgeBindingNames.add("level22");

		List measureBindingNames = new ArrayList();
		measureBindingNames.add("measure1");

		List rowGrandTotalNames = new ArrayList();
		rowGrandTotalNames.add("rowGrandTotal");
		rowGrandTotalNames.add("rowGrandAvg");

		try {
			testOut.print(creator.printCubeAlongEdge(dataCursor, columnEdgeBindingNames, rowEdgeBindingNames,
					measureBindingNames, rowGrandTotalNames, null, null, null));
			this.checkOutputFile();
		} catch (Exception e) {
			fail("fail to get here!");
		}
		CubeUtility.close(dataCursor);
	}

	/**
	 * Test aggregation on measure with arguments
	 *
	 * @throws OLAPException
	 * @throws BirtException
	 */
	@Test
	public void testCursorModel7() throws OLAPException, BirtException {
		ICubeQueryDefinition cqd = this.creator.createQueryDefinition();

		IBinding rowGrandAvg = new Binding("rowWightedave");
		rowGrandAvg.setAggrFunction(IBuildInAggregation.TOTAL_WEIGHTEDAVE_FUNC);
		rowGrandAvg.setExpression(new ScriptExpression("measure[\"measure1\"]"));
		rowGrandAvg.addAggregateOn("dimension[\"dimension5\"][\"level21\"]");
		rowGrandAvg.addAggregateOn("dimension[\"dimension6\"][\"level22\"]");
		rowGrandAvg.addArgument(new ScriptExpression("dimension[\"dimension6\"][\"level22\"][\"attributes220\"]"));

		cqd.addBinding(rowGrandAvg);

		// Create cube view.
		BirtCubeView cubeView = new BirtCubeView(
				new CubeQueryExecutor(null, cqd, de.getSession(), this.scope, de.getContext()));

		CubeCursor dataCursor = cubeView.getCubeCursor(new StopSign(), cube);

		List columnEdgeBindingNames = new ArrayList();
		columnEdgeBindingNames.add("level11");
		columnEdgeBindingNames.add("level12");
		columnEdgeBindingNames.add("level13");
		columnEdgeBindingNames.add("level14");

		List rowEdgeBindingNames = new ArrayList();
		rowEdgeBindingNames.add("level21");
		rowEdgeBindingNames.add("level22");

		List measureBindingNames = new ArrayList();
		measureBindingNames.add("measure1");

		List rowGrandTotalNames = new ArrayList();
		rowGrandTotalNames.add("rowWightedave");

		try {
			testOut.print(creator.printCubeAlongEdge(dataCursor, columnEdgeBindingNames, rowEdgeBindingNames,
					measureBindingNames, rowGrandTotalNames, null, null, null));
			this.checkOutputFile();
			CubeUtility.close(dataCursor);
		} catch (Exception e) {
			fail("fail to get here!");
		}
	}

	/**
	 *
	 * @throws OLAPException
	 * @throws BirtException
	 */
	@Test
	public void testCursorOnCountry() throws OLAPException, BirtException {
		ICubeQueryDefinition cqd = this.creator.createQueryDefinition();

		IBinding rowGrandTotal = new Binding("countryGrandTotal");
		rowGrandTotal.setAggrFunction(IBuildInAggregation.TOTAL_SUM_FUNC);
		rowGrandTotal.setExpression(new ScriptExpression("measure[\"measure1\"]"));
		rowGrandTotal.addAggregateOn("dimension[\"dimension1\"][\"level11\"]");
		rowGrandTotal.addAggregateOn("dimension[\"dimension5\"][\"level21\"]");
		rowGrandTotal.addAggregateOn("dimension[\"dimension6\"][\"level22\"]");

		cqd.addBinding(rowGrandTotal);

		// Create cube view.
		BirtCubeView cubeView = new BirtCubeView(
				new CubeQueryExecutor(null, cqd, de.getSession(), this.scope, de.getContext()));

		CubeCursor dataCursor = cubeView.getCubeCursor(new StopSign(), cube);

		List columnEdgeBindingNames = new ArrayList();
		columnEdgeBindingNames.add("level11");
		columnEdgeBindingNames.add("level12");
		columnEdgeBindingNames.add("level13");
		columnEdgeBindingNames.add("level14");

		List rowEdgeBindingNames = new ArrayList();
		rowEdgeBindingNames.add("level21");
		rowEdgeBindingNames.add("level22");

		List measureBindingNames = new ArrayList();
		measureBindingNames.add("measure1");

		List grandBindingNames = new ArrayList();
		grandBindingNames.add("countryGrandTotal");

		try {
			testOut.print(creator.printCubeAlongEdge(dataCursor, columnEdgeBindingNames, rowEdgeBindingNames,
					measureBindingNames, null, null, null, grandBindingNames));
			this.checkOutputFile();
			CubeUtility.close(dataCursor);
		} catch (Exception e) {
			fail("fail to get here!");
		}
	}

	/**
	 * without measure
	 *
	 * @throws Exception
	 */
	@Test
	public void testCursorWithoutMeasure() throws Exception {
		ICubeQueryDefinition cqd = new CubeQueryDefinition(CubeUtility.cubeName);

		IEdgeDefinition rowEdge = cqd.createEdge(ICubeQueryDefinition.ROW_EDGE);
		IDimensionDefinition rowdim1 = rowEdge.createDimension("dimension5");
		IHierarchyDefinition rowhier1 = rowdim1.createHierarchy("dimension5");
		rowhier1.createLevel("level21");

		IDimensionDefinition rowdim2 = rowEdge.createDimension("dimension6");
		IHierarchyDefinition rowhier2 = rowdim2.createHierarchy("dimension6");
		rowhier2.createLevel("level22");

		IEdgeDefinition columnEdge = cqd.createEdge(ICubeQueryDefinition.COLUMN_EDGE);
		IDimensionDefinition dim1 = columnEdge.createDimension("dimension1");
		IHierarchyDefinition hier1 = dim1.createHierarchy("dimension1");
		hier1.createLevel("level11");
		IDimensionDefinition dim2 = columnEdge.createDimension("dimension2");
		IHierarchyDefinition hier2 = dim2.createHierarchy("dimension2");
		hier2.createLevel("level12");
		IDimensionDefinition dim3 = columnEdge.createDimension("dimension3");
		IHierarchyDefinition hier3 = dim3.createHierarchy("dimension3");
		hier3.createLevel("level13");
		IDimensionDefinition dim4 = columnEdge.createDimension("dimension4");
		IHierarchyDefinition hier4 = dim4.createHierarchy("dimension4");
		hier4.createLevel("level14");

		// Create cube view.
		BirtCubeView cubeView = new BirtCubeView(
				new CubeQueryExecutor(null, cqd, de.getSession(), this.scope, de.getContext()));

		CubeCursor dataCursor = cubeView.getCubeCursor(new StopSign(), cube);

		List columnEdgeBindingNames = new ArrayList();
		columnEdgeBindingNames.add("level11");
		columnEdgeBindingNames.add("level12");
		columnEdgeBindingNames.add("level13");
		columnEdgeBindingNames.add("level14");

		List rowEdgeBindingNames = new ArrayList();
		rowEdgeBindingNames.add("level21");
		rowEdgeBindingNames.add("level22");

		testOut.print(creator.printCubeAlongEdge(dataCursor, columnEdgeBindingNames, rowEdgeBindingNames, null, null,
				null, null, null));
		this.checkOutputFile();

		try {

			dataCursor.getObject("measure1");
		} catch (Exception e) {
			assertTrue(e instanceof OLAPException);
		}
		CubeUtility.close(dataCursor);
	}

	/**
	 *
	 * @throws OLAPException
	 * @throws BirtException
	 */
	@Test
	public void testCursorModel6() throws OLAPException, BirtException {
		ICubeQueryDefinition cqd = new CubeQueryDefinition(CubeUtility.cubeName);

		IMeasureDefinition measure = cqd.createMeasure("measure1");
		measure.setAggrFunction("SUM");
		IEdgeDefinition columnEdge = cqd.createEdge(ICubeQueryDefinition.ROW_EDGE);
		IDimensionDefinition dim = columnEdge.createDimension("dimension5");
		IHierarchyDefinition dimHier = dim.createHierarchy("dimension5");
		dimHier.createLevel("level21");

		IEdgeDefinition rowEdge = cqd.createEdge(ICubeQueryDefinition.COLUMN_EDGE);
		IDimensionDefinition geographyDim = rowEdge.createDimension("dimension1");
		IHierarchyDefinition geographyHier = geographyDim.createHierarchy("dimension1");
		geographyHier.createLevel("level11");
		IDimensionDefinition geographyDim3 = rowEdge.createDimension("dimension3");
		IHierarchyDefinition geographyHier3 = geographyDim3.createHierarchy("dimension3");
		geographyHier3.createLevel("level13");

		IBinding rowGrandTotal = new Binding("rowGrandTotal");
		rowGrandTotal.setAggrFunction(IBuildInAggregation.TOTAL_SUM_FUNC);
		rowGrandTotal.setExpression(new ScriptExpression("measure[\"measure1\"]"));
		rowGrandTotal.addAggregateOn("dimension[\"dimension5\"][\"level21\"]");

		IBinding columnGrandTotal = new Binding("columnGrandTotal");
		columnGrandTotal.setAggrFunction(IBuildInAggregation.TOTAL_AVE_FUNC);
		columnGrandTotal.setExpression(new ScriptExpression("measure[\"measure1\"]"));
		columnGrandTotal.addAggregateOn("dimension[\"dimension1\"][\"level11\"]");
		columnGrandTotal.addAggregateOn("dimension[\"dimension3\"][\"level13\"]");

		IBinding totalGrandTotal = new Binding("totalGrandTotal");
		totalGrandTotal.setAggrFunction(IBuildInAggregation.TOTAL_COUNTDISTINCT_FUNC);
		totalGrandTotal.setExpression(new ScriptExpression("measure[\"measure1\"]"));

		cqd.addBinding(rowGrandTotal);
		cqd.addBinding(columnGrandTotal);
		cqd.addBinding(totalGrandTotal);

		// Create cube view.
		BirtCubeView cubeView = new BirtCubeView(
				new CubeQueryExecutor(null, cqd, de.getSession(), this.scope, de.getContext()));

		CubeCursor dataCursor = cubeView.getCubeCursor(new StopSign(), cube);

		List columnEdgeBindingNames = new ArrayList();
		columnEdgeBindingNames.add("level11");
		columnEdgeBindingNames.add("level13");

		List rowEdgeBindingNames = new ArrayList();
		rowEdgeBindingNames.add("level21");

		List measureBindingNames = new ArrayList();
		measureBindingNames.add("measure1");

		List rowGrandTotalNames = new ArrayList();
		rowGrandTotalNames.add("rowGrandTotal");

		try {
			testOut.print(creator.printCubeAlongEdge(dataCursor, columnEdgeBindingNames, rowEdgeBindingNames,
					measureBindingNames, rowGrandTotalNames, "columnGrandTotal", "totalGrandTotal", null));
			this.checkOutputFile();
			CubeUtility.close(dataCursor);
		} catch (Exception e) {
			fail("fail to get here!");
		}
	}

	/**
	 * with measure filter
	 *
	 * @throws OLAPException
	 * @throws BirtException
	 */
	@Test
	public void testCursorModel8() throws OLAPException, BirtException {
		ICubeQueryDefinition cqd = creator.createQueryDefinition();

		IBinding rowGrandTotal = new Binding("rowGrandTotal");
		rowGrandTotal.setAggrFunction(IBuildInAggregation.TOTAL_SUM_FUNC);
		rowGrandTotal.setExpression(new ScriptExpression("measure[\"measure1\"]"));
		rowGrandTotal.addAggregateOn("dimension[\"dimension5\"][\"level21\"]");
		rowGrandTotal.addAggregateOn("dimension[\"dimension6\"][\"level22\"]");
		rowGrandTotal.setFilter(new ScriptExpression("measure[\"measure1\"]>10"));

		IBinding columnGrandTotal = new Binding("columnGrandTotal");
		columnGrandTotal.setAggrFunction(IBuildInAggregation.TOTAL_AVE_FUNC);
		columnGrandTotal.setExpression(new ScriptExpression("measure[\"measure1\"]"));
		columnGrandTotal.addAggregateOn("dimension[\"dimension1\"][\"level11\"]");
		columnGrandTotal.addAggregateOn("dimension[\"dimension2\"][\"level12\"]");
		columnGrandTotal.addAggregateOn("dimension[\"dimension3\"][\"level13\"]");
		columnGrandTotal.addAggregateOn("dimension[\"dimension4\"][\"level14\"]");
		columnGrandTotal.setFilter(new ScriptExpression("measure[\"measure1\"]>10"));

		IBinding totalGrandTotal = new Binding("totalGrandTotal");
		totalGrandTotal.setAggrFunction(IBuildInAggregation.TOTAL_SUM_FUNC);
		totalGrandTotal.setExpression(new ScriptExpression("measure[\"measure1\"]"));

		cqd.addBinding(rowGrandTotal);
		cqd.addBinding(columnGrandTotal);
		cqd.addBinding(totalGrandTotal);

		// Create cube view.
		BirtCubeView cubeView = new BirtCubeView(
				new CubeQueryExecutor(null, cqd, de.getSession(), this.scope, de.getContext()));

		CubeCursor dataCursor = cubeView.getCubeCursor(new StopSign(), cube);

		List columnEdgeBindingNames = new ArrayList();
		columnEdgeBindingNames.add("level11");
		columnEdgeBindingNames.add("level12");
		columnEdgeBindingNames.add("level13");
		columnEdgeBindingNames.add("level14");

		List rowEdgeBindingNames = new ArrayList();
		rowEdgeBindingNames.add("level21");
		rowEdgeBindingNames.add("level22");

		List measureBindingNames = new ArrayList();
		measureBindingNames.add("measure1");

		List rowGrandTotalNames = new ArrayList();
		rowGrandTotalNames.add("rowGrandTotal");

		try {
			testOut.print(creator.printCubeAlongEdge(dataCursor, columnEdgeBindingNames, rowEdgeBindingNames,
					measureBindingNames, rowGrandTotalNames, "columnGrandTotal", "totalGrandTotal", null));
			this.checkOutputFile();
			CubeUtility.close(dataCursor);
		} catch (Exception e) {
			fail("fail to get here!");
		}
	}

	/**
	 * with appContext's fetch limit
	 *
	 * @throws OLAPException
	 * @throws BirtException
	 */
	@Test
	public void testCursorModel9WithFetchLimit() throws OLAPException, BirtException {
		ICubeQueryDefinition cqd = creator.createQueryDefinition();

		IBinding rowGrandTotal = new Binding("rowGrandTotal");
		rowGrandTotal.setAggrFunction(IBuildInAggregation.TOTAL_SUM_FUNC);
		rowGrandTotal.setExpression(new ScriptExpression("measure[\"measure1\"]"));
		rowGrandTotal.addAggregateOn("dimension[\"dimension5\"][\"level21\"]");
		rowGrandTotal.addAggregateOn("dimension[\"dimension6\"][\"level22\"]");

		IBinding columnGrandTotal = new Binding("columnGrandTotal");
		columnGrandTotal.setAggrFunction(IBuildInAggregation.TOTAL_AVE_FUNC);
		columnGrandTotal.setExpression(new ScriptExpression("measure[\"measure1\"]"));
		columnGrandTotal.addAggregateOn("dimension[\"dimension1\"][\"level11\"]");
		columnGrandTotal.addAggregateOn("dimension[\"dimension2\"][\"level12\"]");
		columnGrandTotal.addAggregateOn("dimension[\"dimension3\"][\"level13\"]");
		columnGrandTotal.addAggregateOn("dimension[\"dimension4\"][\"level14\"]");

		IBinding totalGrandTotal = new Binding("totalGrandTotal");
		totalGrandTotal.setAggrFunction(IBuildInAggregation.TOTAL_SUM_FUNC);
		totalGrandTotal.setExpression(new ScriptExpression("measure[\"measure1\"]"));

		cqd.addBinding(rowGrandTotal);
		cqd.addBinding(columnGrandTotal);
		cqd.addBinding(totalGrandTotal);

		Map appContext = new HashMap();
		appContext.put("org.eclipse.birt.data.engine.olap.cursor.onColumn", "10");
		appContext.put("org.eclipse.birt.data.engine.olap.cursor.onRow", "3");

		try {
			// Create cube view.
			BirtCubeView cubeView = new BirtCubeView(
					new CubeQueryExecutor(null, cqd, de.getSession(), this.scope, de.getContext()), cube, appContext,
					null);

			CubeCursor dataCursor = cubeView.getCubeCursor(new StopSign(), cube);
			fail("should not get here");
		} catch (DataException e) {
			assertTrue(e.getErrorCode().equals(ResourceConstants.RESULT_LENGTH_EXCEED_COLUMN_LIMIT));
		}
	}

	@Test
	public void testCursorWithPageEdge1() throws Exception {
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

		List pageEdgeBindingNames = new ArrayList();
		pageEdgeBindingNames.add("level14");

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

		testOut.print(creator.printCubeAlongPageEdge(dataCursor, pageEdgeBindingNames, columnEdgeBindingNames,
				rowEdgeBindingNames, measureBindingNames, rowGrandTotalNames, "columnGrandTotal", "totalGrandTotal",
				null));
		this.checkOutputFile();
		CubeUtility.close(dataCursor);
	}

	@Test
	public void testCursorWithPageEdge2() throws Exception {
		ICubeQueryDefinition cqd = creator.createQueryDefintionWithPage2();

		IBinding rowGrandTotal = new Binding("rowGrandTotal");
		rowGrandTotal.setAggrFunction(IBuildInAggregation.TOTAL_SUM_FUNC);
		rowGrandTotal.setExpression(new ScriptExpression("measure[\"measure1\"]"));
		rowGrandTotal.addAggregateOn("dimension[\"dimension4\"][\"level14\"]");
		rowGrandTotal.addAggregateOn("dimension[\"dimension1\"][\"level11\"]");
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
		totalGrandTotal.addAggregateOn("dimension[\"dimension1\"][\"level11\"]");

		cqd.addBinding(rowGrandTotal);
		cqd.addBinding(columnGrandTotal);
		cqd.addBinding(totalGrandTotal);

		// Create cube view.
		BirtCubeView cubeView = new BirtCubeView(
				new CubeQueryExecutor(null, cqd, de.getSession(), this.scope, de.getContext()), cube, null, null);

		CubeCursor dataCursor = cubeView.getCubeCursor(new StopSign(), cube);

		List pageEdgeBindingNames = new ArrayList();
		pageEdgeBindingNames.add("level14");
		pageEdgeBindingNames.add("level11");

		List columnEdgeBindingNames = new ArrayList();
		columnEdgeBindingNames.add("level12");
		columnEdgeBindingNames.add("level13");

		List rowEdgeBindingNames = new ArrayList();
		rowEdgeBindingNames.add("level21");
		rowEdgeBindingNames.add("level22");

		List measureBindingNames = new ArrayList();
		measureBindingNames.add("measure1");

		List rowGrandTotalNames = new ArrayList();
		rowGrandTotalNames.add("rowGrandTotal");

		testOut.print(creator.printCubeAlongPageEdge(dataCursor, pageEdgeBindingNames, columnEdgeBindingNames,
				rowEdgeBindingNames, measureBindingNames, rowGrandTotalNames, "columnGrandTotal", "totalGrandTotal",
				null));
		this.checkOutputFile();
		CubeUtility.close(dataCursor);
	}

	/**
	 * without one row/column edge
	 *
	 * @throws Exception
	 */
	@Test
	public void testCursorWithPageEdge3() throws Exception {
		ICubeQueryDefinition cqd = new CubeQueryDefinition(CubeUtility.cubeName);

		IMeasureDefinition measure = cqd.createMeasure("measure1");
		measure.setAggrFunction("SUM");
		IEdgeDefinition rowEdge = cqd.createEdge(ICubeQueryDefinition.ROW_EDGE);
		IDimensionDefinition dim1 = rowEdge.createDimension("dimension5");
		IHierarchyDefinition hier1 = dim1.createHierarchy("dimension5");
		hier1.createLevel("level21");

		IDimensionDefinition dim2 = rowEdge.createDimension("dimension6");
		IHierarchyDefinition hier2 = dim2.createHierarchy("dimension6");
		hier2.createLevel("level22");

		IEdgeDefinition pageEdge = cqd.createEdge(ICubeQueryDefinition.PAGE_EDGE);
		IDimensionDefinition dim3 = pageEdge.createDimension("dimension4");
		IHierarchyDefinition hier3 = dim3.createHierarchy("dimension4");
		hier3.createLevel("level14");

		// Create cube view.
		BirtCubeView cubeView = new BirtCubeView(
				new CubeQueryExecutor(null, cqd, de.getSession(), this.scope, de.getContext()));

		CubeCursor dataCursor = cubeView.getCubeCursor(new StopSign(), cube);

		// retrieve the edge cursors
		// EdgeCursor pageCursor = cubeView.getMeasureEdgeView( );
		List pageEdgeBindingNames = new ArrayList();
		pageEdgeBindingNames.add("level14");

		List rowEdgeBindingNames = new ArrayList();
		rowEdgeBindingNames.add("level21");
		rowEdgeBindingNames.add("level22");

		List measureBindingNames = new ArrayList();
		measureBindingNames.add("measure1");

		testOut.print(creator.printCubeAlongPageEdge(dataCursor, pageEdgeBindingNames, new ArrayList(),
				rowEdgeBindingNames, measureBindingNames, null, null, null, null));
		this.checkOutputFile();
		CubeUtility.close(dataCursor);
	}
}

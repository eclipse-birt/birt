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

package org.eclipse.birt.data.engine.olap.data.impl;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.core.archive.compound.ArchiveFile;
import org.eclipse.birt.core.archive.compound.ArchiveReader;
import org.eclipse.birt.core.archive.compound.ArchiveWriter;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.aggregation.api.IBuildInAggregation;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.FilterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.DataEngineImpl;
import org.eclipse.birt.data.engine.impl.StopSign;
import org.eclipse.birt.data.engine.olap.api.query.CubeFilterDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IDimensionDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IHierarchyDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition;
import org.eclipse.birt.data.engine.olap.cursor.CubeUtility;
import org.eclipse.birt.data.engine.olap.data.api.CubeQueryExecutorHelper;
import org.eclipse.birt.data.engine.olap.data.api.DimLevel;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;
import org.eclipse.birt.data.engine.olap.data.api.IComputedMeasureHelper;
import org.eclipse.birt.data.engine.olap.data.api.IDimensionSortDefn;
import org.eclipse.birt.data.engine.olap.data.api.ISelection;
import org.eclipse.birt.data.engine.olap.data.api.MeasureInfo;
import org.eclipse.birt.data.engine.olap.data.api.cube.CubeMaterializer;
import org.eclipse.birt.data.engine.olap.data.api.cube.IDatasetIterator;
import org.eclipse.birt.data.engine.olap.data.api.cube.IHierarchy;
import org.eclipse.birt.data.engine.olap.data.api.cube.ILevelDefn;
import org.eclipse.birt.data.engine.olap.data.document.DocumentManagerFactory;
import org.eclipse.birt.data.engine.olap.data.document.IDocumentManager;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.filter.LevelFilter;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Dimension;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.DimensionFactory;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.DimensionForTest;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.LevelDefinition;
import org.eclipse.birt.data.engine.olap.data.util.DataType;
import org.eclipse.birt.data.engine.olap.impl.query.CubeElementFactory;
import org.eclipse.birt.data.engine.olap.impl.query.CubeQueryExecutor;
import org.eclipse.birt.data.engine.olap.util.filter.BaseDimensionFilterEvalHelper;
import org.eclipse.birt.data.engine.olap.util.filter.DimensionFilterEvalHelper;
import org.eclipse.birt.data.engine.olap.util.filter.IFacttableRow;
import org.eclipse.birt.data.engine.olap.util.filter.IJSFacttableFilterEvalHelper;
import org.eclipse.birt.data.engine.olap.util.filter.IJSFilterHelper;
import org.eclipse.birt.data.engine.olap.util.filter.JSFacttableFilterEvalHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mozilla.javascript.ImporterTopLevel;

import testutil.BaseTestCase;

/**
 *
 */

public class CubeAggregationTest extends BaseTestCase {
	String pathName;
	private ImporterTopLevel baseScope;
	private ICubeQueryDefinition cubeQuery;

	IDocumentManager documentManager;
	private ILevelDefinition level21;
	private ILevelDefinition level31;

	private DimLevel dimLevel21 = new DimLevel("dimension2", "level21");
	private DimLevel dimLevel31 = new DimLevel("dimension3", "level31");
	private DimLevel dimLevel11 = new DimLevel("dimension1", "level11");
	private DimLevel dimLevel12 = new DimLevel("dimension1", "level12");

	private CubeMaterializer materializer;
	private ScriptContext cx = null;
	private DataEngineImpl engine = null;

	public CubeAggregationTest() {
		pathName = System.getProperty("java.io.tmpdir");
		this.baseScope = new ImporterTopLevel();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before
	public void cubeAggregationSetUp() throws Exception {
		DataEngineContext context = DataEngineContext.newInstance(DataEngineContext.DIRECT_PRESENTATION, null, null,
				null);
		context.setTmpdir(this.getTempDir());
		engine = (DataEngineImpl) DataEngine.newDataEngine(context);
		materializer = new CubeMaterializer(engine);
		documentManager = materializer.getDocumentManager();
		createCube1(documentManager);
		cx = new ScriptContext();
		createCube1QueryDefn();

	}

	/*
	 * @see TestCase#tearDown()
	 */
	@After
	public void cubeAggregationTearDown() throws Exception {
		documentManager.close();
		engine.shutdown();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		documentManager.close();
		documentManager = null;
		super.finalize();
	}

	/**
	 * test aggregation using random access document manager.
	 *
	 * @throws IOException
	 * @throws BirtException
	 */
	@Test
	public void testRAAggregation1() throws IOException, BirtException {
		IDocArchiveWriter writer = createRAWriter();
		materializer.saveCubeToReportDocument("cube1", writer, new StopSign());
		writer.flush();
		writer.finish();
		documentManager = createRADocumentManager();
		testCube1Aggregation();
		documentManager.close();
	}

	private IDocumentManager createRADocumentManager() throws IOException, DataException {
		ArchiveFile archiveFile = new ArchiveFile(pathName + File.separator + "docForTest", "rw+");
		ArchiveReader reader = new ArchiveReader(archiveFile);
		IDocumentManager documentManager = DocumentManagerFactory.createRADocumentManager(reader);
		return documentManager;
	}

	private IDocArchiveWriter createRAWriter() throws IOException {
		ArchiveFile archiveFile = new ArchiveFile(pathName + File.separator + "docForTest", "rw+");
		ArchiveWriter writer = new ArchiveWriter(archiveFile);
		return writer;
	}

	/**
	 * create cube query definition for aggregation filtering.
	 *
	 * @return
	 * @throws DataException
	 */
	private void createCube1QueryDefn() throws DataException {
		cubeQuery = new CubeElementFactory().createCubeQuery("cube1");
		IEdgeDefinition rowEdge = cubeQuery.createEdge(ICubeQueryDefinition.ROW_EDGE);
		IEdgeDefinition columnEdge = cubeQuery.createEdge(ICubeQueryDefinition.COLUMN_EDGE);
		IDimensionDefinition dimension1 = rowEdge.createDimension("dimension1");
		IHierarchyDefinition hier1 = dimension1.createHierarchy("hier1");
		hier1.createLevel("level11");
		hier1.createLevel("level12");
		hier1.createLevel("level13");
		IBinding bindin11 = new Binding("edge1_level11");
		bindin11.setExpression(new ScriptExpression("dimension[\"dimension1\"][\"level11\"]"));
		cubeQuery.addBinding(bindin11);

		IBinding bindin12 = new Binding("edge1_level12");
		bindin11.setExpression(new ScriptExpression("dimension[\"dimension1\"][\"level12\"]"));
		cubeQuery.addBinding(bindin12);

		IBinding bindin13 = new Binding("edge1_level13");
		bindin11.setExpression(new ScriptExpression("dimension[\"dimension1\"][\"level13\"]"));
		cubeQuery.addBinding(bindin13);

		IDimensionDefinition dimension2 = columnEdge.createDimension("dimension2");
		IHierarchyDefinition hier2 = dimension2.createHierarchy("hier2");
		level21 = hier2.createLevel("level21");

		IBinding bindin21 = new Binding("edge1_level21");
		bindin11.setExpression(new ScriptExpression("dimension[\"dimension2\"][\"level21\"]"));
		cubeQuery.addBinding(bindin21);

		IDimensionDefinition dimension3 = columnEdge.createDimension("dimension3");
		IHierarchyDefinition hier3 = dimension3.createHierarchy("hier3");
		level31 = hier3.createLevel("level31");

		IBinding bindin31 = new Binding("edge1_level31");
		bindin11.setExpression(new ScriptExpression("dimension[\"dimension3\"][\"level31\"]"));
		cubeQuery.addBinding(bindin31);

		IBinding measure1 = new Binding("measure1");
		measure1.setExpression(new ScriptExpression("measure[\"measure1\"]"));
		cubeQuery.addBinding(measure1);

		IBinding measure2 = new Binding("measure2");
		measure2.setExpression(new ScriptExpression("measure[\"measure2\"]"));
		cubeQuery.addBinding(measure2);
	}

	/**
	 * get distincted string array, in which any object is unique.
	 *
	 * @param iValues
	 * @return
	 */
	private static String[] distinct(String[] iValues) {
		Arrays.sort(iValues);
		List tempList = new ArrayList();
		tempList.add(iValues[0]);
		for (int i = 1; i < iValues.length; i++) {
			if (!iValues[i].equals(iValues[i - 1])) {
				tempList.add(iValues[i]);
			}
		}
		String[] result = new String[tempList.size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = ((String) tempList.get(i));
		}
		return result;
	}

	/**
	 * @throws IOException
	 * @throws BirtException
	 * @throws DataException
	 */
	private void createCube1(IDocumentManager documentManager) throws IOException, BirtException, DataException {
		Dimension[] dimensions = new Dimension[3];

		// dimension1
		String[] colNames = new String[3];
		colNames[0] = "col11";
		colNames[1] = "col12";
		colNames[2] = "col13";
		DimensionForTest iterator = new DimensionForTest(colNames);
		iterator.setLevelMember(0, TestFactTable.L1Col);
		iterator.setLevelMember(1, TestFactTable.L2Col);
		iterator.setLevelMember(2, TestFactTable.L3Col);

		ILevelDefn[] levelDefs = new ILevelDefn[3];
		levelDefs[0] = new LevelDefinition("level11", new String[] { "col11" }, null);
		levelDefs[1] = new LevelDefinition("level12", new String[] { "col12" }, null);
		levelDefs[2] = new LevelDefinition("level13", new String[] { "col13" }, null);
		dimensions[0] = (Dimension) DimensionFactory.createDimension("dimension1", documentManager, iterator, levelDefs,
				false, new StopSign());
		IHierarchy hierarchy = dimensions[0].getHierarchy();
		assertEquals(hierarchy.getName(), "dimension1");
		assertEquals(dimensions[0].length(), TestFactTable.L1Col.length);

		// dimension2
		colNames = new String[1];
		colNames[0] = "level21";
		iterator = new DimensionForTest(colNames);
		iterator.setLevelMember(0, distinct(TestFactTable.L1Col));

		levelDefs = new ILevelDefn[1];
		levelDefs[0] = new LevelDefinition("level21", new String[] { "level21" }, null);
		dimensions[1] = (Dimension) DimensionFactory.createDimension("dimension2", documentManager, iterator, levelDefs,
				false, new StopSign());
		hierarchy = dimensions[1].getHierarchy();
		assertEquals(hierarchy.getName(), "dimension2");
		assertEquals(dimensions[1].length(), 3);

		// dimension3
		colNames = new String[1];
		colNames[0] = "level31";

		iterator = new DimensionForTest(colNames);
		iterator.setLevelMember(0, TestFactTable.L3Col);

		levelDefs = new ILevelDefn[1];
		levelDefs[0] = new LevelDefinition("level31", new String[] { "level31" }, null);
		dimensions[2] = (Dimension) DimensionFactory.createDimension("dimension3", documentManager, iterator, levelDefs,
				false, new StopSign());

		hierarchy = dimensions[2].getHierarchy();
		assertEquals(hierarchy.getName(), "dimension3");
		assertEquals(dimensions[2].length(), 12);

		TestFactTable factTable2 = new TestFactTable();
		String[] measureColumnName = new String[2];
		measureColumnName[0] = "measure1";
		measureColumnName[1] = "measure2";
		Cube cube = new Cube("cube1", documentManager);

		cube.create(CubeUtility.getKeyColNames(dimensions), dimensions, factTable2, measureColumnName, new StopSign());
		documentManager.flush();
	}

	@Test
	public void testCube1Aggregation() throws IOException, DataException, BirtException {
		// query
		CubeQueryExecutorHelper cubeQueryExcutorHelper = new CubeQueryExecutorHelper(
				CubeQueryExecutorHelper.loadCube("cube1", documentManager, new StopSign()), new ComputedMeasureHelper(),
				null);
		ISelection[][] filter = new ISelection[1][1];
		filter[0][0] = SelectionFactory.createRangeSelection(new Object[] { "1" }, new Object[] { "3" }, true, false);
		cubeQueryExcutorHelper.addFilter(new LevelFilter(dimLevel21, filter[0]));

		AggregationDefinition[] aggregations = new AggregationDefinition[4];
		int[] sortType = new int[1];
		sortType[0] = IDimensionSortDefn.SORT_ASC;
		DimLevel[] levelsForFilter = { dimLevel21 };
		AggregationFunctionDefinition[] funcitons = new AggregationFunctionDefinition[2];
		funcitons[0] = new AggregationFunctionDefinition("measure1", IBuildInAggregation.TOTAL_SUM_FUNC);
		funcitons[1] = new AggregationFunctionDefinition("C_Measure1", IBuildInAggregation.TOTAL_SUM_FUNC);
		AggregationFunctionDefinition[] funcitonsWithParameterCol = new AggregationFunctionDefinition[3];
		funcitonsWithParameterCol[0] = new AggregationFunctionDefinition("measure1",
				IBuildInAggregation.TOTAL_SUM_FUNC);
		funcitonsWithParameterCol[1] = new AggregationFunctionDefinition(null, "measure1",
				new DimLevel("dimension1", "level12"), "col12", IBuildInAggregation.TOTAL_WEIGHTEDAVE_FUNC);

		aggregations[0] = new AggregationDefinition(levelsForFilter, sortType, funcitons);
		sortType = new int[2];
		sortType[0] = IDimensionSortDefn.SORT_ASC;
		sortType[1] = IDimensionSortDefn.SORT_ASC;
		levelsForFilter = new DimLevel[] { dimLevel31 };
		aggregations[1] = new AggregationDefinition(levelsForFilter, sortType, funcitons);

		aggregations[2] = new AggregationDefinition(null, null, funcitons);

		aggregations[3] = new AggregationDefinition(levelsForFilter, sortType, null);

		IAggregationResultSet[] resultSet = cubeQueryExcutorHelper.execute(aggregations, new StopSign());
		CubeQueryExecutorHelper.saveAggregationResultSet(pathName, "test2", resultSet);
		resultSet = CubeQueryExecutorHelper.loadAggregationResultSet(pathName, "test2");
		// result set for aggregation 0
		assertEquals(resultSet[0].length(), 2);
		assertEquals(resultSet[0].getAggregationDataType(0), DataType.DOUBLE_TYPE);
		assertEquals(resultSet[0].getLevelIndex(dimLevel21), 0);
		assertEquals(resultSet[0].getLevelKeyDataType(dimLevel21, "level21"), DataType.STRING_TYPE);
		resultSet[0].seek(0);
		assertEquals(resultSet[0].getLevelKeyValue(0)[0], "1");
		assertEquals(resultSet[0].getAggregationValue(0), new Double(6));
		assertEquals(resultSet[0].getAggregationValue(1), new Double(10));
		resultSet[0].seek(1);
		assertEquals(resultSet[0].getLevelKeyValue(0)[0], "2");
		assertEquals(resultSet[0].getAggregationValue(0), new Double(22));
		assertEquals(resultSet[0].getAggregationValue(1), new Double(26));
		// result set for aggregation 1
		assertEquals(resultSet[1].length(), 8);
		assertEquals(resultSet[1].getAggregationDataType(0), DataType.DOUBLE_TYPE);
		assertEquals(resultSet[1].getLevelIndex(dimLevel31), 0);
		assertEquals(resultSet[1].getLevelKeyDataType(dimLevel31, "level31"), DataType.INTEGER_TYPE);
		resultSet[1].seek(0);
		assertEquals(resultSet[1].getLevelKeyValue(0)[0], new Integer(1));
		assertEquals(resultSet[1].getAggregationValue(0), new Double(0));
		resultSet[1].seek(1);
		assertEquals(resultSet[1].getLevelKeyValue(0)[0], new Integer(2));
		assertEquals(resultSet[1].getAggregationValue(0), new Double(1));
		resultSet[1].seek(2);
		assertEquals(resultSet[1].getLevelKeyValue(0)[0], new Integer(3));
		assertEquals(resultSet[1].getAggregationValue(0), new Double(2));
		resultSet[1].seek(3);
		assertEquals(resultSet[1].getLevelKeyValue(0)[0], new Integer(4));
		assertEquals(resultSet[1].getAggregationValue(0), new Double(3));
		resultSet[1].seek(4);
		assertEquals(resultSet[1].getLevelKeyValue(0)[0], new Integer(5));
		assertEquals(resultSet[1].getAggregationValue(0), new Double(4));
		resultSet[1].seek(5);
		assertEquals(resultSet[1].getLevelKeyValue(0)[0], new Integer(6));
		assertEquals(resultSet[1].getAggregationValue(0), new Double(5));
		resultSet[1].seek(6);
		assertEquals(resultSet[1].getLevelKeyValue(0)[0], new Integer(7));
		assertEquals(resultSet[1].getAggregationValue(0), new Double(6));
		resultSet[1].seek(7);
		assertEquals(resultSet[1].getLevelKeyValue(0)[0], new Integer(8));
		assertEquals(resultSet[1].getAggregationValue(0), new Double(7));
		// result set for aggregation 2
		assertEquals(resultSet[2].length(), 1);
		assertEquals(resultSet[2].getAggregationDataType(0), DataType.DOUBLE_TYPE);
		assertEquals(resultSet[2].getLevelIndex(dimLevel31), -1);
		resultSet[2].seek(0);
		assertEquals(resultSet[2].getLevelKeyValue(0), null);
		assertEquals(resultSet[2].getAggregationValue(0), new Double(28));

		// result set for aggregation 3
		assertEquals(resultSet[3].length(), 8);
		assertEquals(resultSet[3].getAggregationDataType(0), DataType.UNKNOWN_TYPE);
		assertEquals(resultSet[3].getLevelIndex(dimLevel31), 0);
		assertEquals(resultSet[3].getLevelKeyDataType(dimLevel31, "level31"), DataType.INTEGER_TYPE);
		resultSet[3].seek(0);
		assertEquals(resultSet[3].getLevelKeyValue(0)[0], new Integer(1));
		assertEquals(resultSet[3].getAggregationValue(0), null);
		resultSet[3].seek(1);
		assertEquals(resultSet[3].getLevelKeyValue(0)[0], new Integer(2));
		resultSet[3].seek(2);
		assertEquals(resultSet[3].getLevelKeyValue(0)[0], new Integer(3));
		assertEquals(resultSet[3].getAggregationValue(0), null);
		resultSet[3].seek(3);
		assertEquals(resultSet[3].getLevelKeyValue(0)[0], new Integer(4));
		resultSet[3].seek(4);
		assertEquals(resultSet[3].getLevelKeyValue(0)[0], new Integer(5));
		assertEquals(resultSet[3].getAggregationValue(0), null);
		resultSet[3].seek(5);
		assertEquals(resultSet[3].getLevelKeyValue(0)[0], new Integer(6));
		resultSet[3].seek(6);
		assertEquals(resultSet[3].getLevelKeyValue(0)[0], new Integer(7));
		resultSet[3].seek(7);
		assertEquals(resultSet[3].getLevelKeyValue(0)[0], new Integer(8));
		for (int i = 0; i < resultSet.length; i++) {
			resultSet[i].close();
		}
	}

	@Test
	public void testCube1AggregationWithFunctionFilter() throws IOException, DataException, BirtException {
		// query
		CubeQueryExecutorHelper cubeQueryExcutorHelper = new CubeQueryExecutorHelper(
				CubeQueryExecutorHelper.loadCube("cube1", documentManager, new StopSign()));

		AggregationDefinition[] aggregations = new AggregationDefinition[1];
		int[] sortType = new int[1];
		sortType[0] = IDimensionSortDefn.SORT_ASC;
		DimLevel[] levelsForFilter = { dimLevel21 };
		AggregationFunctionDefinition[] funcitons = new AggregationFunctionDefinition[1];
		funcitons[0] = new AggregationFunctionDefinition(null, "measure1", null, null,
				IBuildInAggregation.TOTAL_SUM_FUNC, new JSMeasureFilterEvalHelper());

		aggregations[0] = new AggregationDefinition(levelsForFilter, sortType, funcitons);

		IAggregationResultSet[] resultSet = cubeQueryExcutorHelper.execute(aggregations, new StopSign());
		CubeQueryExecutorHelper.saveAggregationResultSet(pathName, "test2", resultSet);
		resultSet = CubeQueryExecutorHelper.loadAggregationResultSet(pathName, "test2");
		// result set for aggregation 0
		assertEquals(resultSet[0].length(), 3);
		assertEquals(resultSet[0].getAggregationDataType(0), DataType.DOUBLE_TYPE);
		assertEquals(resultSet[0].getLevelIndex(dimLevel21), 0);
		assertEquals(resultSet[0].getLevelKeyDataType(dimLevel21, "level21"), DataType.STRING_TYPE);
		resultSet[0].seek(0);
		assertEquals(resultSet[0].getLevelKeyValue(0)[0], "1");
		assertEquals(resultSet[0].getAggregationValue(0), new Double(4));
		resultSet[0].seek(1);
		assertEquals(resultSet[0].getLevelKeyValue(0)[0], "2");
		assertEquals(resultSet[0].getAggregationValue(0), new Double(12));
		resultSet[0].seek(2);
		assertEquals(resultSet[0].getLevelKeyValue(0)[0], "3");
		assertEquals(resultSet[0].getAggregationValue(0), new Double(20));

	}

	@Test
	public void testCube1AggregationWithMeasureFilter() throws IOException, DataException, BirtException {
		// query
		CubeQueryExecutorHelper cubeQueryExcutorHelper = new CubeQueryExecutorHelper(
				CubeQueryExecutorHelper.loadCube("cube1", documentManager, new StopSign()));

		AggregationDefinition[] aggregations = new AggregationDefinition[1];
		int[] sortType = new int[1];
		sortType[0] = IDimensionSortDefn.SORT_ASC;
		DimLevel[] levelsForFilter = { dimLevel21 };
		AggregationFunctionDefinition[] funcitons = new AggregationFunctionDefinition[1];
		funcitons[0] = new AggregationFunctionDefinition(null, "measure1", null, null,
				IBuildInAggregation.TOTAL_SUM_FUNC, null);

		aggregations[0] = new AggregationDefinition(levelsForFilter, sortType, funcitons);
		JSMeasureFilterEvalHelper measureFilter = new JSMeasureFilterEvalHelper();
		List<IJSFacttableFilterEvalHelper> helper = new ArrayList<>();
		helper.add(measureFilter);
		cubeQueryExcutorHelper.addMeasureFilter(helper);
		IAggregationResultSet[] resultSet = cubeQueryExcutorHelper.execute(aggregations, new StopSign());
		CubeQueryExecutorHelper.saveAggregationResultSet(pathName, "test2", resultSet);
		resultSet = CubeQueryExecutorHelper.loadAggregationResultSet(pathName, "test2");
		// result set for aggregation 0
		assertEquals(resultSet[0].length(), 3);
		assertEquals(resultSet[0].getAggregationDataType(0), DataType.DOUBLE_TYPE);
		assertEquals(resultSet[0].getLevelIndex(dimLevel21), 0);
		assertEquals(resultSet[0].getLevelKeyDataType(dimLevel21, "level21"), DataType.STRING_TYPE);
		resultSet[0].seek(0);
		assertEquals(resultSet[0].getLevelKeyValue(0)[0], "1");
		assertEquals(resultSet[0].getAggregationValue(0), new Double(4));
		resultSet[0].seek(1);
		assertEquals(resultSet[0].getLevelKeyValue(0)[0], "2");
		assertEquals(resultSet[0].getAggregationValue(0), new Double(12));
		resultSet[0].seek(2);
		assertEquals(resultSet[0].getLevelKeyValue(0)[0], "3");
		assertEquals(resultSet[0].getAggregationValue(0), new Double(20));

	}

	@Test
	public void testEmptyAggregation() throws IOException, DataException, BirtException {
		// query
		CubeQueryExecutorHelper cubeQueryExcutorHelper = new CubeQueryExecutorHelper(
				CubeQueryExecutorHelper.loadCube("cube1", documentManager, new StopSign()));
		ISelection[][] filter = new ISelection[1][1];
		filter[0][0] = SelectionFactory.createRangeSelection(new Object[] { "3999" }, new Object[] { "1" }, true,
				false);
		cubeQueryExcutorHelper.addFilter(new LevelFilter(dimLevel21, filter[0]));

		AggregationDefinition[] aggregations = new AggregationDefinition[4];
		int[] sortType = new int[1];
		sortType[0] = IDimensionSortDefn.SORT_ASC;
		DimLevel[] levelsForFilter = { dimLevel21 };
		AggregationFunctionDefinition[] funcitons = new AggregationFunctionDefinition[1];
		funcitons[0] = new AggregationFunctionDefinition("measure1", IBuildInAggregation.TOTAL_SUM_FUNC);
		AggregationFunctionDefinition[] funcitonsWithParameterCol = new AggregationFunctionDefinition[2];
		funcitonsWithParameterCol[0] = new AggregationFunctionDefinition("measure1",
				IBuildInAggregation.TOTAL_SUM_FUNC);
		funcitonsWithParameterCol[1] = new AggregationFunctionDefinition(null, "measure1",
				new DimLevel("dimension1", "level12"), "col12", IBuildInAggregation.TOTAL_WEIGHTEDAVE_FUNC);
		aggregations[0] = new AggregationDefinition(levelsForFilter, sortType, funcitons);
		sortType = new int[2];
		sortType[0] = IDimensionSortDefn.SORT_ASC;
		sortType[1] = IDimensionSortDefn.SORT_ASC;
		levelsForFilter = new DimLevel[] { dimLevel31 };
		aggregations[1] = new AggregationDefinition(levelsForFilter, sortType, funcitons);

		aggregations[2] = new AggregationDefinition(null, null, funcitons);

		aggregations[3] = new AggregationDefinition(levelsForFilter, sortType, null);

		IAggregationResultSet[] resultSet = cubeQueryExcutorHelper.execute(aggregations, new StopSign());
		CubeQueryExecutorHelper.saveAggregationResultSet(pathName, "test2", resultSet);
		resultSet = CubeQueryExecutorHelper.loadAggregationResultSet(pathName, "test2");

		for (int i = 0; i < resultSet.length; i++) {
			assertEquals(resultSet[i].length(), 0);
			resultSet[i].close();
		}
	}

	@Test
	public void testCube1AggregationWithColPara() throws IOException, DataException, BirtException {
		// query
		CubeQueryExecutorHelper cubeQueryExcutorHelper = new CubeQueryExecutorHelper(
				CubeQueryExecutorHelper.loadCube("cube1", documentManager, new StopSign()));
		ISelection[][] filter = new ISelection[1][1];
		filter[0][0] = SelectionFactory.createRangeSelection(new Object[] { "1" }, new Object[] { "3" }, true, false);
		cubeQueryExcutorHelper.addFilter(new LevelFilter(dimLevel21, filter[0]));

		AggregationDefinition[] aggregations = new AggregationDefinition[1];
		int[] sortType = new int[1];
		sortType[0] = IDimensionSortDefn.SORT_ASC;
		AggregationFunctionDefinition[] funcitons = new AggregationFunctionDefinition[1];
		funcitons[0] = new AggregationFunctionDefinition("measure1", IBuildInAggregation.TOTAL_SUM_FUNC);
		AggregationFunctionDefinition[] funcitonsWithParameterCol = new AggregationFunctionDefinition[1];
		funcitonsWithParameterCol[0] = new AggregationFunctionDefinition("measure1",
				IBuildInAggregation.TOTAL_SUM_FUNC);
		funcitonsWithParameterCol[0] = new AggregationFunctionDefinition(null, "measure1",
				new DimLevel("dimension1", "level12"), "col12", IBuildInAggregation.TOTAL_WEIGHTEDAVE_FUNC);
		aggregations[0] = new AggregationDefinition(null, null, funcitonsWithParameterCol);

		IAggregationResultSet[] resultSet = cubeQueryExcutorHelper.execute(aggregations, new StopSign());

		// result set for aggregation 0
		assertEquals(resultSet[0].length(), 1);
		assertEquals(resultSet[0].getAggregationDataType(0), DataType.DOUBLE_TYPE);
		assertEquals(resultSet[0].getLevelIndex(dimLevel31), -1);
		resultSet[0].seek(0);
		assertEquals(resultSet[0].getLevelKeyValue(0), null);
		assertEquals(resultSet[0].getAggregationValue(0), new Double(3.8333333333333335));

		for (int i = 0; i < resultSet.length; i++) {
			resultSet[i].close();
		}
	}

	/**
	 * test simiple aggregation with only expression such as data["level21_sum"]>30.
	 * this will filter out the levels that does not qualify this condition.
	 *
	 * @throws IOException
	 * @throws DataException
	 * @throws BirtException
	 */
	@Test
	public void testCube1AggrFilter1() throws IOException, DataException, BirtException {
		// query
		CubeQueryExecutorHelper cubeQueryExcutorHelper = new CubeQueryExecutorHelper(
				CubeQueryExecutorHelper.loadCube("cube1", documentManager, new StopSign()));
		// add aggregation filter on level21
		IBinding level21_sum = new Binding("level21_sum");
		level21_sum.setAggrFunction(IBuildInAggregation.TOTAL_SUM_FUNC);
		level21_sum.setExpression(new ScriptExpression("measure[\"measure1\"]"));
		level21_sum.addAggregateOn("dimension[\"dimension2\"][\"level21\"]");
		cubeQuery.addBinding(level21_sum);

		ScriptExpression expr = new ScriptExpression("data[\"level21_sum\"]>30");
		CubeFilterDefinition cubeFilter = new CubeFilterDefinition(expr);
		cubeFilter.setTargetLevel(level21);
		//
		DimensionFilterEvalHelper filterHelper = new DimensionFilterEvalHelper(null, baseScope, cx, cubeQuery,
				cubeFilter);
		cubeQueryExcutorHelper.addJSFilter(filterHelper);

		AggregationDefinition[] aggregations = new AggregationDefinition[1];
		int[] sortType = new int[1];
		sortType[0] = IDimensionSortDefn.SORT_ASC;
		DimLevel[] levelsForFilter = { dimLevel21 };
		AggregationFunctionDefinition[] funcitons = new AggregationFunctionDefinition[1];
		funcitons[0] = new AggregationFunctionDefinition("level21_sum", "measure1", IBuildInAggregation.TOTAL_SUM_FUNC);
		aggregations[0] = new AggregationDefinition(levelsForFilter, sortType, funcitons);

		IAggregationResultSet[] resultSet = cubeQueryExcutorHelper.execute(aggregations, new StopSign());
		// result set for aggregation 0
		assertEquals(resultSet[0].length(), 1);
		assertEquals(resultSet[0].getAggregationDataType(0), DataType.DOUBLE_TYPE);
		assertEquals(resultSet[0].getLevelIndex(dimLevel21), 0);
		assertEquals(resultSet[0].getLevelKeyDataType(dimLevel21, "level21"), DataType.STRING_TYPE);
		resultSet[0].seek(0);
		assertEquals(resultSet[0].getLevelKeyValue(0)[0], "3");
		assertEquals(resultSet[0].getAggregationValue(0), new Double(38));//

		for (int i = 0; i < resultSet.length; i++) {
			resultSet[i].close();
		}
	}

	/**
	 * test simple aggregation with level filter in muti-level aggregation.
	 *
	 * @throws IOException
	 * @throws DataException
	 * @throws BirtException
	 */
	@Test
	public void testCube1AggrFilter2() throws IOException, DataException, BirtException {
		// query
		CubeQueryExecutorHelper cubeQueryExcutorHelper = new CubeQueryExecutorHelper(
				CubeQueryExecutorHelper.loadCube("cube1", documentManager, new StopSign()));
		// add dimension filter on level21
		ISelection[][] filter = new ISelection[1][1];
		filter[0][0] = SelectionFactory.createRangeSelection(new Object[] { "1" }, new Object[] { "3" }, true, false);
		cubeQueryExcutorHelper.addFilter(new LevelFilter(dimLevel21, filter[0]));
		// add aggregation filter on level21 and level31
		IBinding level21_sum = new Binding("level21_sum");
		level21_sum.setAggrFunction(IBuildInAggregation.TOTAL_SUM_FUNC);
		level21_sum.setExpression(new ScriptExpression("measure[\"measure1\"]"));
		level21_sum.addAggregateOn("dimension[\"dimension2\"][\"level21\"]");
		level21_sum.addAggregateOn("dimension[\"dimension3\"][\"level31\"]");

		cubeQuery.addBinding(level21_sum);

		ScriptExpression expr = new ScriptExpression("data[\"level21_sum\"]>2");
		CubeFilterDefinition cubeFilter = new CubeFilterDefinition(expr);
		cubeFilter.setTargetLevel(level21);

		//
		DimensionFilterEvalHelper dimfilter = new DimensionFilterEvalHelper(null, baseScope, cx, cubeQuery, cubeFilter);
		cubeQueryExcutorHelper.addJSFilter(dimfilter);
		AggregationDefinition[] aggregations = new AggregationDefinition[1];
		int[] sortType = { IDimensionSortDefn.SORT_ASC, IDimensionSortDefn.SORT_ASC };
		DimLevel[] levelsForFilter = { dimLevel21, dimLevel31 };
		AggregationFunctionDefinition[] funcitons = new AggregationFunctionDefinition[1];
		funcitons[0] = new AggregationFunctionDefinition("level21_sum", "measure1", IBuildInAggregation.TOTAL_SUM_FUNC);
		aggregations[0] = new AggregationDefinition(levelsForFilter, sortType, funcitons);

		IAggregationResultSet[] resultSet = cubeQueryExcutorHelper.execute(aggregations, new StopSign());
		// result set for aggregation 0
		assertEquals(resultSet[0].length(), 8);
		assertEquals(resultSet[0].getAggregationDataType(0), DataType.DOUBLE_TYPE);
		assertEquals(resultSet[0].getLevelIndex(dimLevel21), 0);
		assertEquals(resultSet[0].getLevelIndex(dimLevel31), 1);
		assertEquals(resultSet[0].getLevelKeyDataType(dimLevel21, "level21"), DataType.STRING_TYPE);
		/*
		 * aggregation result table index level21 level31 sum(measure1) 0 1 1 0 1 1 2 1
		 * 2 1 3 2 3 1 4 3 4 2 5 4 5 2 6 5 6 2 7 6 7 2 8 7
		 */
		for (int i = 0; i < 8; i++) {
			resultSet[0].seek(i);
			assertEquals(resultSet[0].getLevelKeyValue(0)[0], String.valueOf(i / 4 + 1));
			assertEquals(resultSet[0].getLevelKeyValue(1)[0], new Integer(i + 1));
			assertEquals(resultSet[0].getAggregationValue(0), new Double(i));//
		}

		for (int i = 0; i < resultSet.length; i++) {
			resultSet[i].close();
		}
	}

	/**
	 * test empty aggregation result set.
	 *
	 * @param documentManager
	 * @throws IOException
	 * @throws DataException
	 * @throws BirtException
	 */
	@Test
	public void testCube1AggrFilter3() throws IOException, DataException, BirtException {
		// query
		CubeQueryExecutorHelper cubeQueryExcutorHelper = new CubeQueryExecutorHelper(
				CubeQueryExecutorHelper.loadCube("cube1", documentManager, new StopSign()));
		// add dimension filter on level21
		ISelection[][] filter = new ISelection[1][1];
		filter[0][0] = SelectionFactory.createRangeSelection(new Object[] { "1" }, new Object[] { "3" }, true, false);
		cubeQueryExcutorHelper.addFilter(new LevelFilter(dimLevel21, filter[0]));
		// add aggregation filter on level21 and level31
		IBinding level21_sum = new Binding("level21_sum");
		level21_sum.setAggrFunction(IBuildInAggregation.TOTAL_SUM_FUNC);
		level21_sum.setExpression(new ScriptExpression("measure[\"measure1\"]"));
		level21_sum.addAggregateOn("dimension[\"dimension2\"][\"level21\"]");
		cubeQuery.addBinding(level21_sum);

		ScriptExpression expr = new ScriptExpression("data[\"level21_sum\"]>40");
		CubeFilterDefinition cubeFilter = new CubeFilterDefinition(expr);
		cubeFilter.setTargetLevel(level21);
		//
		DimensionFilterEvalHelper dimfilter = new DimensionFilterEvalHelper(null, baseScope, cx, cubeQuery, cubeFilter);
		cubeQueryExcutorHelper.addJSFilter(dimfilter);
		AggregationDefinition[] aggregations = new AggregationDefinition[1];
		int[] sortType = { IDimensionSortDefn.SORT_ASC };
		DimLevel[] levelsForFilter = { dimLevel21 };
		AggregationFunctionDefinition[] funcitons = new AggregationFunctionDefinition[1];
		funcitons[0] = new AggregationFunctionDefinition("level21_sum", "measure1", IBuildInAggregation.TOTAL_SUM_FUNC);
		aggregations[0] = new AggregationDefinition(levelsForFilter, sortType, funcitons);

		IAggregationResultSet[] resultSet = cubeQueryExcutorHelper.execute(aggregations, new StopSign());
		assertEquals(resultSet[0].length(), 0);

		for (int i = 0; i < resultSet.length; i++) {
			resultSet[i].close();
		}
	}

	/**
	 * test aggregation filter with one axis condition in one level aggregation.
	 *
	 * @throws IOException
	 * @throws DataException
	 * @throws BirtException
	 */
	@Test
	public void testCube1AggrFilter4() throws IOException, DataException, BirtException {
		// query
		CubeQueryExecutorHelper cubeQueryExcutorHelper = new CubeQueryExecutorHelper(
				CubeQueryExecutorHelper.loadCube("cube1", documentManager, new StopSign()));
		IBinding level21_sum = new Binding("level21_sum");
		level21_sum.setAggrFunction(IBuildInAggregation.TOTAL_SUM_FUNC);
		level21_sum.setExpression(new ScriptExpression("measure[\"measure1\"]"));
		level21_sum.addAggregateOn("dimension[\"dimension2\"][\"level21\"]");
		cubeQuery.addBinding(level21_sum);

		ScriptExpression expr = new ScriptExpression("data[\"level21_sum\"]>0");
		CubeFilterDefinition cubeFilter = new CubeFilterDefinition(expr);
		cubeFilter.setTargetLevel(level21);
		cubeFilter.setAxisQualifierLevels(new ILevelDefinition[] { level21 });
		cubeFilter.setAxisQualifierValues(new Object[] { "1" });
		//
		DimensionFilterEvalHelper dimfilter = new DimensionFilterEvalHelper(null, baseScope, cx, cubeQuery, cubeFilter);
		cubeQueryExcutorHelper.addJSFilter(dimfilter);
		AggregationDefinition[] aggregations = new AggregationDefinition[1];
		int[] sortType = { IDimensionSortDefn.SORT_ASC };
		DimLevel[] levelsForFilter = { dimLevel21 };
		AggregationFunctionDefinition[] funcitons = new AggregationFunctionDefinition[1];
		funcitons[0] = new AggregationFunctionDefinition("level21_sum", "measure1", IBuildInAggregation.TOTAL_SUM_FUNC);
		aggregations[0] = new AggregationDefinition(levelsForFilter, sortType, funcitons);

		IAggregationResultSet[] resultSet = cubeQueryExcutorHelper.execute(aggregations, new StopSign());
		// result set for aggregation 0
		assertEquals(resultSet[0].length(), 1);
		assertEquals(resultSet[0].getAggregationDataType(0), DataType.DOUBLE_TYPE);
		assertEquals(resultSet[0].getLevelIndex(dimLevel21), 0);
		assertEquals(resultSet[0].getLevelKeyDataType(dimLevel21, "level21"), DataType.STRING_TYPE);
		resultSet[0].seek(0);
		assertEquals(resultSet[0].getLevelKeyValue(0)[0], "1");
		assertEquals(resultSet[0].getAggregationValue(0), new Double(6));//

		for (int i = 0; i < resultSet.length; i++) {
			resultSet[i].close();
		}
	}

	/**
	 * test aggregation filter with one axis condition in muti-level aggregation.
	 *
	 * @throws IOException
	 * @throws DataException
	 * @throws BirtException
	 */
	@Test
	public void testCube1AggrFilter5() throws IOException, DataException, BirtException {
		// query
		CubeQueryExecutorHelper cubeQueryExcutorHelper = new CubeQueryExecutorHelper(
				CubeQueryExecutorHelper.loadCube("cube1", documentManager, new StopSign()));
		// add aggregation filter on level21 and level31
		IBinding level21_sum = new Binding("level21_sum");
		level21_sum.setAggrFunction(IBuildInAggregation.TOTAL_SUM_FUNC);
		level21_sum.setExpression(new ScriptExpression("measure[\"measure1\"]"));
		level21_sum.addAggregateOn("dimension[\"dimension2\"][\"level21\"]");
		level21_sum.addAggregateOn("dimension[\"dimension3\"][\"level31\"]");
		cubeQuery.addBinding(level21_sum);

		ScriptExpression expr = new ScriptExpression("data[\"level21_sum\"]>0");
		CubeFilterDefinition cubeFilter = new CubeFilterDefinition(expr);
		cubeFilter.setTargetLevel(level21);
		cubeFilter.setAxisQualifierLevels(new ILevelDefinition[] { level31 });
		cubeFilter.setAxisQualifierValues(new Object[] { new Integer(4) });
		//
		DimensionFilterEvalHelper dimfilter = new DimensionFilterEvalHelper(null, baseScope, cx, cubeQuery, cubeFilter);
		cubeQueryExcutorHelper.addJSFilter(dimfilter);
		AggregationDefinition[] aggregations = new AggregationDefinition[1];
		int[] sortType = { IDimensionSortDefn.SORT_ASC, IDimensionSortDefn.SORT_ASC };
		DimLevel[] levelsForFilter = { dimLevel21, dimLevel31 };
		AggregationFunctionDefinition[] funcitons = new AggregationFunctionDefinition[1];
		funcitons[0] = new AggregationFunctionDefinition("level21_sum", "measure1", IBuildInAggregation.TOTAL_SUM_FUNC);
		aggregations[0] = new AggregationDefinition(levelsForFilter, sortType, funcitons);

		IAggregationResultSet[] resultSet = cubeQueryExcutorHelper.execute(aggregations, new StopSign());
		// result set for aggregation 0
		assertEquals(resultSet[0].length(), 4);
		assertEquals(resultSet[0].getAggregationDataType(0), DataType.DOUBLE_TYPE);
		assertEquals(resultSet[0].getLevelIndex(dimLevel21), 0);
		assertEquals(resultSet[0].getLevelIndex(dimLevel31), 1);
		assertEquals(resultSet[0].getLevelKeyDataType(dimLevel21, "level21"), DataType.STRING_TYPE);
		assertEquals(resultSet[0].getLevelKeyDataType(dimLevel31, "level31"), DataType.INTEGER_TYPE);
		int index1 = resultSet[0].getLevelIndex(dimLevel21);
		int index2 = resultSet[0].getLevelIndex(dimLevel31);

		for (int i = 0; i < resultSet[0].length(); i++) {
			resultSet[0].seek(i);
			assertEquals(resultSet[0].getLevelKeyValue(index1)[0], "1");
			assertEquals(resultSet[0].getLevelKeyValue(index2)[0], new Integer(i + 1));
			assertEquals(resultSet[0].getAggregationValue(0), new Double(i));
		}

		for (int i = 0; i < resultSet.length; i++) {
			resultSet[i].close();
		}
	}

	/**
	 * test top/bottom N filter on aggregation result.
	 *
	 * @throws IOException
	 * @throws DataException
	 * @throws BirtException
	 */
	@Test
	public void testCube1AggrFilter6() throws IOException, DataException, BirtException {
		// query
		CubeQueryExecutorHelper cubeQueryExcutorHelper = new CubeQueryExecutorHelper(
				CubeQueryExecutorHelper.loadCube("cube1", documentManager, new StopSign()));
		// add aggregation filter on level21
		IBinding level21_sum = new Binding("level21_sum");
		level21_sum.setAggrFunction(IBuildInAggregation.TOTAL_SUM_FUNC);
		level21_sum.setExpression(new ScriptExpression("measure[\"measure1\"]"));
		level21_sum.addAggregateOn("dimension[\"dimension2\"][\"level21\"]");
		cubeQuery.addBinding(level21_sum);

		// top/bottom filters
		// top 2 filter
		IConditionalExpression expr1 = new ConditionalExpression("data[\"level21_sum\"]",
				IConditionalExpression.OP_TOP_N, "2");
		CubeFilterDefinition cubeFilter = new CubeFilterDefinition(expr1);
		cubeFilter.setTargetLevel(level21);
		IJSFilterHelper filterHelper = BaseDimensionFilterEvalHelper.createFilterHelper(null, baseScope, cubeQuery,
				cubeFilter, cx);
		cubeQueryExcutorHelper.addJSFilter(filterHelper);

		IConditionalExpression expr2 = new ConditionalExpression("data[\"level21_sum\"]",
				IConditionalExpression.OP_BOTTOM_N, "2");
		CubeFilterDefinition cubeFilter2 = new CubeFilterDefinition(expr2);
		cubeFilter2.setTargetLevel(level21);
		// bottom 2 filter
		IJSFilterHelper filterHelper2 = BaseDimensionFilterEvalHelper.createFilterHelper(null, baseScope, cubeQuery,
				cubeFilter2, cx);
		cubeQueryExcutorHelper.addJSFilter(filterHelper2);
		// top 3 filter
		IConditionalExpression expr3 = new ConditionalExpression("data[\"level21_sum\"]",
				IConditionalExpression.OP_BOTTOM_N, "3");
		CubeFilterDefinition cubeFilter3 = new CubeFilterDefinition(expr3);
		cubeFilter3.setTargetLevel(level21);
		IJSFilterHelper filterHelper3 = BaseDimensionFilterEvalHelper.createFilterHelper(null, baseScope, cubeQuery,
				cubeFilter3, cx);
		cubeQueryExcutorHelper.addJSFilter(filterHelper3);

		AggregationDefinition[] aggregations = new AggregationDefinition[1];
		int[] sortType = new int[1];
		sortType[0] = IDimensionSortDefn.SORT_ASC;
		DimLevel[] levelsForFilter = { dimLevel21 };
		AggregationFunctionDefinition[] funcitons = new AggregationFunctionDefinition[1];
		funcitons[0] = new AggregationFunctionDefinition("level21_sum", "measure1", IBuildInAggregation.TOTAL_SUM_FUNC);
		aggregations[0] = new AggregationDefinition(levelsForFilter, sortType, funcitons);

		IAggregationResultSet[] resultSet = cubeQueryExcutorHelper.execute(aggregations, new StopSign());
		// result set for aggregation 0
		assertEquals(resultSet[0].length(), 1);
		assertEquals(resultSet[0].getAggregationDataType(0), DataType.DOUBLE_TYPE);
		assertEquals(resultSet[0].getLevelIndex(dimLevel21), 0);
		assertEquals(resultSet[0].getLevelKeyDataType(dimLevel21, "level21"), DataType.STRING_TYPE);
		resultSet[0].seek(0);
		assertEquals(resultSet[0].getLevelKeyValue(0)[0], "2");
		assertEquals(resultSet[0].getAggregationValue(0), new Double(22));//

		for (int i = 0; i < resultSet.length; i++) {
			resultSet[i].close();
		}
	}

	/**
	 * test top/bottom percentage filter on aggregation result.
	 *
	 * @throws IOException
	 * @throws DataException
	 * @throws BirtException
	 */
	@Test
	public void testCube1AggrFilter7() throws IOException, DataException, BirtException {
		// query
		CubeQueryExecutorHelper cubeQueryExcutorHelper = new CubeQueryExecutorHelper(
				CubeQueryExecutorHelper.loadCube("cube1", documentManager, new StopSign()));
		// add aggregation filter on level21
		IBinding level21_sum = new Binding("level21_sum");
		level21_sum.setAggrFunction(IBuildInAggregation.TOTAL_SUM_FUNC);
		level21_sum.setExpression(new ScriptExpression("measure[\"measure1\"]"));
		level21_sum.addAggregateOn("dimension[\"dimension2\"][\"level21\"]");
		cubeQuery.addBinding(level21_sum);

		// top/bottom filters
		// top 2 filter
		IConditionalExpression expr1 = new ConditionalExpression("data[\"level21_sum\"]",
				IConditionalExpression.OP_TOP_PERCENT, "70");
		CubeFilterDefinition cubeFilter = new CubeFilterDefinition(expr1);
		cubeFilter.setTargetLevel(level21);
		IJSFilterHelper filterHelper = BaseDimensionFilterEvalHelper.createFilterHelper(null, baseScope, cubeQuery,
				cubeFilter, cx);
		cubeQueryExcutorHelper.addJSFilter(filterHelper);

		IConditionalExpression expr2 = new ConditionalExpression("data[\"level21_sum\"]",
				IConditionalExpression.OP_BOTTOM_PERCENT, "70");
		CubeFilterDefinition cubeFilter2 = new CubeFilterDefinition(expr2);
		cubeFilter2.setTargetLevel(level21);
		// bottom 2 filter
		IJSFilterHelper filterHelper2 = BaseDimensionFilterEvalHelper.createFilterHelper(null, baseScope, cubeQuery,
				cubeFilter2, cx);
		cubeQueryExcutorHelper.addJSFilter(filterHelper2);
		// top 3 filter
		IConditionalExpression expr3 = new ConditionalExpression("data[\"level21_sum\"]",
				IConditionalExpression.OP_TOP_PERCENT, "90");
		CubeFilterDefinition cubeFilter3 = new CubeFilterDefinition(expr3);
		cubeFilter3.setTargetLevel(level21);
		IJSFilterHelper filterHelper3 = BaseDimensionFilterEvalHelper.createFilterHelper(null, baseScope, cubeQuery,
				cubeFilter3, cx);
		cubeQueryExcutorHelper.addJSFilter(filterHelper3);

		AggregationDefinition[] aggregations = new AggregationDefinition[1];
		int[] sortType = new int[1];
		sortType[0] = IDimensionSortDefn.SORT_ASC;
		DimLevel[] levelsForFilter = { dimLevel21 };
		AggregationFunctionDefinition[] funcitons = new AggregationFunctionDefinition[1];
		funcitons[0] = new AggregationFunctionDefinition("level21_sum", "measure1", IBuildInAggregation.TOTAL_SUM_FUNC);
		aggregations[0] = new AggregationDefinition(levelsForFilter, sortType, funcitons);

		IAggregationResultSet[] resultSet = cubeQueryExcutorHelper.execute(aggregations, new StopSign());
		// result set for aggregation 0
		assertEquals(resultSet[0].length(), 1);
		assertEquals(resultSet[0].getAggregationDataType(0), DataType.DOUBLE_TYPE);
		assertEquals(resultSet[0].getLevelIndex(dimLevel21), 0);
		assertEquals(resultSet[0].getLevelKeyDataType(dimLevel21, "level21"), DataType.STRING_TYPE);
		resultSet[0].seek(0);
		assertEquals(resultSet[0].getLevelKeyValue(0)[0], "2");
		assertEquals(resultSet[0].getAggregationValue(0), new Double(22));//

		for (int i = 0; i < resultSet.length; i++) {
			resultSet[i].close();
		}
	}

	/**
	 * top/bottom dimension filter.
	 *
	 * @throws IOException
	 * @throws DataException
	 * @throws BirtException
	 */
	@Test
	public void testCube1AggrFilter8() throws IOException, DataException, BirtException {
		// query
		CubeQueryExecutorHelper cubeQueryExcutorHelper = new CubeQueryExecutorHelper(
				CubeQueryExecutorHelper.loadCube("cube1", documentManager, new StopSign()));
		// add aggregation filter on level21
		IBinding level21_sum = new Binding("level21_sum");
		level21_sum.setAggrFunction(IBuildInAggregation.TOTAL_SUM_FUNC);
		level21_sum.setExpression(new ScriptExpression("measure[\"measure1\"]"));
		level21_sum.addAggregateOn("dimension[\"dimension2\"][\"level21\"]");
		cubeQuery.addBinding(level21_sum);

		// top/bottom filters
		// top 2 filter
		IConditionalExpression expr1 = new ConditionalExpression("dimension[\"dimension2\"][\"level21\"]",
				IConditionalExpression.OP_TOP_N, "1");
		CubeFilterDefinition cubeFilter = new CubeFilterDefinition(expr1);
		IJSFilterHelper filterHelper = BaseDimensionFilterEvalHelper.createFilterHelper(null, baseScope, cubeQuery,
				cubeFilter, cx);
		cubeQueryExcutorHelper.addJSFilter(filterHelper);

		AggregationDefinition[] aggregations = new AggregationDefinition[1];
		int[] sortType = new int[1];
		sortType[0] = IDimensionSortDefn.SORT_ASC;
		DimLevel[] levelsForFilter = { dimLevel21 };
		AggregationFunctionDefinition[] funcitons = new AggregationFunctionDefinition[1];
		funcitons[0] = new AggregationFunctionDefinition("level21_sum", "measure1", IBuildInAggregation.TOTAL_SUM_FUNC);
		aggregations[0] = new AggregationDefinition(levelsForFilter, sortType, funcitons);

		IAggregationResultSet[] resultSet = cubeQueryExcutorHelper.execute(aggregations, new StopSign());
		// result set for aggregation 0
		assertEquals(resultSet[0].length(), 1);
		assertEquals(resultSet[0].getAggregationDataType(0), DataType.DOUBLE_TYPE);
		assertEquals(resultSet[0].getLevelIndex(dimLevel21), 0);
		assertEquals(resultSet[0].getLevelKeyDataType(dimLevel21, "level21"), DataType.STRING_TYPE);
		resultSet[0].seek(0);
		assertEquals(resultSet[0].getLevelKeyValue(0)[0], "3");
		assertEquals(resultSet[0].getAggregationValue(0), new Double(38));//

		for (int i = 0; i < resultSet.length; i++) {
			resultSet[i].close();
		}
	}

	/**
	 * in/not in dimension filter.
	 *
	 * @throws IOException
	 * @throws DataException
	 * @throws BirtException
	 */
	@Test
	public void testCube1AggrFilter9() throws IOException, DataException, BirtException {
		// query
		CubeQueryExecutorHelper cubeQueryExcutorHelper = new CubeQueryExecutorHelper(
				CubeQueryExecutorHelper.loadCube("cube1", documentManager, new StopSign()));
		// add aggregation filter on level21
		IBinding level21_sum = new Binding("level21_sum");
		level21_sum.setAggrFunction(IBuildInAggregation.TOTAL_SUM_FUNC);
		level21_sum.setExpression(new ScriptExpression("measure[\"measure1\"]"));
		level21_sum.addAggregateOn("dimension[\"dimension2\"][\"level21\"]");
		cubeQuery.addBinding(level21_sum);

		// top 2 filter
		List valueList = new ArrayList();
		valueList.add("1");
		valueList.add("2");
		IConditionalExpression expr1 = new ConditionalExpression("dimension[\"dimension2\"][\"level21\"]",
				IConditionalExpression.OP_IN, valueList);
		CubeFilterDefinition cubeFilter = new CubeFilterDefinition(expr1);
		IJSFilterHelper filterHelper = BaseDimensionFilterEvalHelper.createFilterHelper(null, baseScope, cubeQuery,
				cubeFilter, cx);
		cubeQueryExcutorHelper.addJSFilter(filterHelper);

		AggregationDefinition[] aggregations = new AggregationDefinition[1];
		int[] sortType = new int[1];
		sortType[0] = IDimensionSortDefn.SORT_ASC;
		DimLevel[] levelsForFilter = { dimLevel21 };
		AggregationFunctionDefinition[] funcitons = new AggregationFunctionDefinition[1];
		funcitons[0] = new AggregationFunctionDefinition("level21_sum", "measure1", IBuildInAggregation.TOTAL_SUM_FUNC);
		aggregations[0] = new AggregationDefinition(levelsForFilter, sortType, funcitons);

		IAggregationResultSet[] resultSet = cubeQueryExcutorHelper.execute(aggregations, new StopSign());
		// result set for aggregation 0
		assertEquals(resultSet[0].length(), 2);
		assertEquals(resultSet[0].getAggregationDataType(0), DataType.DOUBLE_TYPE);
		assertEquals(resultSet[0].getLevelIndex(dimLevel21), 0);
		assertEquals(resultSet[0].getLevelKeyDataType(dimLevel21, "level21"), DataType.STRING_TYPE);
		resultSet[0].seek(0);
		assertEquals(resultSet[0].getLevelKeyValue(0)[0], "1");
		assertEquals(resultSet[0].getAggregationValue(0), new Double(6));//
		resultSet[0].seek(1);
		assertEquals(resultSet[0].getLevelKeyValue(0)[0], "2");
		assertEquals(resultSet[0].getAggregationValue(0), new Double(22));//

		for (int i = 0; i < resultSet.length; i++) {
			resultSet[i].close();
		}
	}

	/**
	 * add this test for ted 65288, filter refered to mutilple dimensions
	 *
	 * @throws IOException
	 * @throws DataException
	 * @throws BirtException
	 */
	@Test
	public void testCube1AggrFilter10() throws IOException, DataException, BirtException {
		// query
		CubeQueryExecutorHelper cubeQueryExcutorHelper = new CubeQueryExecutorHelper(
				CubeQueryExecutorHelper.loadCube("cube1", documentManager, new StopSign()));
		// add aggregation filter on level21
		IBinding level21_sum = new Binding("level21_sum");
		level21_sum.setAggrFunction(IBuildInAggregation.TOTAL_SUM_FUNC);
		level21_sum.setExpression(new ScriptExpression("measure[\"measure1\"]"));
		level21_sum.addAggregateOn("dimension[\"dimension2\"][\"level21\"]");
		cubeQuery.addBinding(level21_sum);

		ScriptExpression expr = new ScriptExpression(
				"dimension[\"dimension2\"][\"level21\"]>1 && dimension[\"dimension3\"][\"level31\"]>1");
		JSFacttableFilterEvalHelper filterHelper = new JSFacttableFilterEvalHelper(baseScope, cx,
				new FilterDefinition(expr), null, null);

		AggregationDefinition[] aggregations = new AggregationDefinition[1];
		int[] sortType = new int[1];
		sortType[0] = IDimensionSortDefn.SORT_ASC;
		DimLevel[] levelsForFilter = { dimLevel21 };
		AggregationFunctionDefinition[] funcitons = new AggregationFunctionDefinition[1];

		funcitons[0] = new AggregationFunctionDefinition("level21_sum", "measure1", null, null,
				IBuildInAggregation.TOTAL_SUM_FUNC, filterHelper);
		aggregations[0] = new AggregationDefinition(levelsForFilter, sortType, funcitons);

		cubeQueryExcutorHelper.setCubeQueryExecutor(new CubeQueryExecutor(null, cubeQuery, engine.getSession(),
				new ImporterTopLevel(), engine.getContext()));

		IAggregationResultSet[] resultSet = cubeQueryExcutorHelper.execute(aggregations, new StopSign());

		for (int i = 0; i < resultSet.length; i++) {
			resultSet[i].close();
		}
	}

	private void createCube2() throws IOException, BirtException {
		Dimension[] dimensions = new Dimension[2];

		// dimension1
		String[] ColNames = new String[3];
		ColNames[0] = "col11";
		ColNames[1] = "col12";
		ColNames[2] = "col13";
		DimensionForTest iterator = new DimensionForTest(ColNames);
		iterator.setLevelMember(0, TestFactTable.L1Col);
		iterator.setLevelMember(1, TestFactTable.L2Col);
		iterator.setLevelMember(2, TestFactTable.L3Col);

		ILevelDefn[] levelDefs = new ILevelDefn[3];
		levelDefs[0] = new LevelDefinition("level11", new String[] { "col11" }, null);
		levelDefs[1] = new LevelDefinition("level12", new String[] { "col12" }, null);
		levelDefs[2] = new LevelDefinition("level13", new String[] { "col13" }, null);
		dimensions[0] = (Dimension) DimensionFactory.createDimension("dimension1", documentManager, iterator, levelDefs,
				false, new StopSign());
		IHierarchy hierarchy = dimensions[0].getHierarchy();
		assertEquals(hierarchy.getName(), "dimension1");
		assertEquals(dimensions[0].length(), TestFactTable.L1Col.length);

		// dimension2
		ColNames = new String[1];
		ColNames[0] = "level21";
		iterator = new DimensionForTest(ColNames);
		iterator.setLevelMember(0, distinct(TestFactTable.L1Col));

		levelDefs = new ILevelDefn[1];
		levelDefs[0] = new LevelDefinition("level21", new String[] { "level21" }, null);
		dimensions[1] = (Dimension) DimensionFactory.createDimension("dimension2", documentManager, iterator, levelDefs,
				false, new StopSign());
		hierarchy = dimensions[1].getHierarchy();
		assertEquals(hierarchy.getName(), "dimension2");
		assertEquals(dimensions[1].length(), 3);

		TestFactTable factTable2 = new TestFactTable();
		String[] measureColumnName = new String[2];
		measureColumnName[0] = "measure1";
		measureColumnName[1] = "measure2";
		Cube cube = new Cube("cube2", documentManager);

		cube.create(CubeUtility.getKeyColNames(dimensions), dimensions, factTable2, measureColumnName, new StopSign());
		documentManager.flush();

	}

	@Test
	public void testCube2Aggregation() throws IOException, BirtException {
		createCube2();
		CubeQueryExecutorHelper cubeQueryExcutorHelper = new CubeQueryExecutorHelper(
				CubeQueryExecutorHelper.loadCube("cube2", documentManager, new StopSign()));
		ISelection[][] filter = new ISelection[1][1];
		filter[0][0] = SelectionFactory.createRangeSelection(new Object[] { "1" }, new Object[] { "3" }, true, false);
		cubeQueryExcutorHelper.addFilter(new LevelFilter(dimLevel21, filter[0]));

		AggregationDefinition[] aggregations = new AggregationDefinition[4];
		int[] sortType = new int[1];
		sortType[0] = IDimensionSortDefn.SORT_ASC;
		DimLevel[] levelsForFilter = { dimLevel21 };
		AggregationFunctionDefinition[] funcitons = new AggregationFunctionDefinition[1];
		funcitons[0] = new AggregationFunctionDefinition("measure1", IBuildInAggregation.TOTAL_SUM_FUNC);
		aggregations[0] = new AggregationDefinition(levelsForFilter, sortType, funcitons);

		sortType = new int[2];
		sortType[0] = IDimensionSortDefn.SORT_ASC;
		sortType[1] = IDimensionSortDefn.SORT_ASC;
		levelsForFilter = new DimLevel[] { dimLevel11, dimLevel12 };
		funcitons = new AggregationFunctionDefinition[1];
		funcitons[0] = new AggregationFunctionDefinition("measure1", IBuildInAggregation.TOTAL_SUM_FUNC);
		aggregations[1] = new AggregationDefinition(levelsForFilter, sortType, funcitons);

		sortType = new int[1];
		sortType[0] = IDimensionSortDefn.SORT_ASC;
		levelsForFilter = new DimLevel[] { dimLevel21 };
		funcitons = new AggregationFunctionDefinition[1];
		funcitons[0] = new AggregationFunctionDefinition("measure1", IBuildInAggregation.TOTAL_SUM_FUNC);
		aggregations[2] = new AggregationDefinition(levelsForFilter, sortType, funcitons);

		sortType = new int[1];
		sortType[0] = IDimensionSortDefn.SORT_ASC;
		levelsForFilter = new DimLevel[] { dimLevel11 };
		funcitons = new AggregationFunctionDefinition[1];
		funcitons[0] = new AggregationFunctionDefinition("measure1", IBuildInAggregation.TOTAL_SUM_FUNC);
		aggregations[3] = new AggregationDefinition(levelsForFilter, sortType, funcitons);

		IAggregationResultSet[] resultSet = cubeQueryExcutorHelper.execute(aggregations, new StopSign());
		CubeQueryExecutorHelper.saveAggregationResultSet(pathName, "test1", resultSet);
		resultSet = CubeQueryExecutorHelper.loadAggregationResultSet(pathName, "test1");

		// result set for aggregation 0
		assertEquals(resultSet[0].length(), 2);

		assertEquals(resultSet[0].getAggregationDataType(0), DataType.DOUBLE_TYPE);
		assertEquals(resultSet[0].getLevelIndex(dimLevel21), 0);
		assertEquals(resultSet[0].getLevelKeyDataType(dimLevel21, "level21"), DataType.STRING_TYPE);
		resultSet[0].seek(0);
		assertEquals(resultSet[0].getLevelKeyValue(0)[0], "1");
		assertEquals(resultSet[0].getAggregationValue(0), new Double(6));
		resultSet[0].seek(1);
		assertEquals(resultSet[0].getLevelKeyValue(0)[0], "2");
		assertEquals(resultSet[0].getAggregationValue(0), new Double(22));

		// result set for aggregation 1
		assertEquals(resultSet[1].length(), 4);

		assertEquals(resultSet[1].getAggregationDataType(0), DataType.DOUBLE_TYPE);
		assertEquals(resultSet[1].getLevelIndex(dimLevel21), -1);
		assertEquals(resultSet[1].getLevelIndex(dimLevel11), 0);
		assertEquals(resultSet[1].getLevelIndex(dimLevel12), 1);
		assertEquals(resultSet[1].getLevelKeyDataType(dimLevel11, "col11"), DataType.STRING_TYPE);
		assertEquals(resultSet[1].getLevelKeyDataType(dimLevel12, "col12"), DataType.INTEGER_TYPE);
		resultSet[1].seek(0);
		assertEquals(resultSet[1].getLevelKeyValue(0)[0], "1");
		assertEquals(resultSet[1].getLevelKeyValue(1)[0], new Integer(1));
		assertEquals(resultSet[1].getAggregationValue(0), new Double(1));
		resultSet[1].seek(1);
		assertEquals(resultSet[1].getLevelKeyValue(0)[0], "1");
		assertEquals(resultSet[1].getLevelKeyValue(1)[0], new Integer(2));
		assertEquals(resultSet[1].getAggregationValue(0), new Double(5));
		resultSet[1].seek(2);
		assertEquals(resultSet[1].getLevelKeyValue(0)[0], "2");
		assertEquals(resultSet[1].getLevelKeyValue(1)[0], new Integer(1));
		assertEquals(resultSet[1].getAggregationValue(0), new Double(9));
		resultSet[1].seek(3);
		assertEquals(resultSet[1].getLevelKeyValue(0)[0], "2");
		assertEquals(resultSet[1].getLevelKeyValue(1)[0], new Integer(2));
		assertEquals(resultSet[1].getAggregationValue(0), new Double(13));

		// result set for aggregation 2
		assertEquals(resultSet[2].length(), 2);

		assertEquals(resultSet[2].getAggregationDataType(0), DataType.DOUBLE_TYPE);
		assertEquals(resultSet[2].getLevelIndex(dimLevel21), 0);
		assertEquals(resultSet[2].getLevelKeyDataType(dimLevel21, "level21"), DataType.STRING_TYPE);
		resultSet[2].seek(0);
		assertEquals(resultSet[2].getLevelKeyValue(0)[0], "1");
		assertEquals(resultSet[2].getAggregationValue(0), new Double(6));
		resultSet[2].seek(1);
		assertEquals(resultSet[2].getLevelKeyValue(0)[0], "2");
		assertEquals(resultSet[2].getAggregationValue(0), new Double(22));

		// result set for aggregation 3
		assertEquals(resultSet[3].length(), 2);

		assertEquals(resultSet[3].getAggregationDataType(0), DataType.DOUBLE_TYPE);
		assertEquals(resultSet[3].getLevelIndex(dimLevel11), 0);
		assertEquals(resultSet[3].getLevelKeyDataType(dimLevel11, "col11"), DataType.STRING_TYPE);
		resultSet[3].seek(0);
		assertEquals(resultSet[3].getLevelKeyValue(0)[0], "1");
		assertEquals(resultSet[3].getAggregationValue(0), new Double(6));
		resultSet[3].seek(1);
		assertEquals(resultSet[3].getLevelKeyValue(0)[0], "2");
		assertEquals(resultSet[3].getAggregationValue(0), new Double(22));
		for (int i = 0; i < resultSet.length; i++) {
			resultSet[i].close();
		}
	}
}

/*
 * col11 col12 col13 level21 level31 measure1 measure2 String Unknown Integer
 * String Integer Integer Double 1 1 1 1 0 0.0 1 2 1 2 1 1.0 1 3 1 3 2 2.0 1 4 1
 * 4 3 3.0 2 5 2 5 4 4.0 2 6 2 6 5 5.0 2 7 2 7 6 6.0 2 8 2 8 7 7.0 3 9 3 9 8 8.0
 * 3 10 3 10 9 9.0 3 11 3 11 10 10.0 3 12 3 12 11 11.0
 */
class TestFactTable implements IDatasetIterator {

	int ptr = -1;
	static String[] L1Col = { "1", "1", "1", "1", "2", "2", "2", "2", "3", "3", "3", "3" };
	static int[] L2Col = { 1, 1, 2, 2, 1, 1, 2, 2, 2, 2, 3, 3 };

	static int[] L3Col = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };

	@Override
	public void close() throws BirtException {
		// TODO Auto-generated method stub

	}

	public void beforeFirst() {
		ptr = -1;

	}

	public Boolean getBoolean(int fieldIndex) throws BirtException {
		// TODO Auto-generated method stub
		return null;
	}

	public Date getDate(int fieldIndex) throws BirtException {
		// TODO Auto-generated method stub
		return null;
	}

	public Double getDouble(int fieldIndex) throws BirtException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getFieldIndex(String name) throws BirtException {
		if (name.equals("col11")) {
			return 0;
		} else if (name.equals("col12")) {
			return -1;
		} else if (name.equals("col13")) {
			return 2;
		} else if (name.equals("level21")) {
			return 3;
		} else if (name.equals("level31")) {
			return 4;
		} else if (name.equals("measure1")) {
			return 5;
		} else if (name.equals("measure2")) {
			return 6;
		}
		return -1;
	}

	@Override
	public int getFieldType(String name) throws BirtException {
		if (name.equals("col11")) {
			return DataType.STRING_TYPE;
		} else if (name.equals("col12")) {
			return DataType.UNKNOWN_TYPE;
		} else if (name.equals("col13")) {
			return DataType.INTEGER_TYPE;
		} else if (name.equals("level21")) {
			return DataType.STRING_TYPE;
		} else if (name.equals("level31")) {
			return DataType.INTEGER_TYPE;
		} else if (name.equals("measure1")) {
			return DataType.INTEGER_TYPE;
		} else if (name.equals("measure2")) {
			return DataType.DOUBLE_TYPE;
		}
		return -1;
	}

	public Integer getInteger(int fieldIndex) throws BirtException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getString(int fieldIndex) throws BirtException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getValue(int fieldIndex) throws BirtException {
		if (fieldIndex == 0) {
			return L1Col[ptr];
		} else if (fieldIndex == 1) {
			return new Integer(L2Col[ptr]);
		} else if (fieldIndex == 2) {
			return new Integer(L3Col[ptr]);
		} else if (fieldIndex == 3) {
			return L1Col[ptr];
		} else if (fieldIndex == 4) {
			return new Integer(L3Col[ptr]);
		} else if (fieldIndex == 5) {
			return new Integer(ptr);
		} else if (fieldIndex == 6) {
			return new Double(ptr);
		}
		return null;
	}

	@Override
	public boolean next() throws BirtException {
		ptr++;
		if (ptr >= L1Col.length) {
			return false;
		}
		return true;
	}
}

class ComputedMeasureHelper implements IComputedMeasureHelper {
	private MeasureInfo[] measureInfos = { new MeasureInfo("C_Measure1", DataType.INTEGER_TYPE) };

	@Override
	public Object[] computeMeasureValues(IFacttableRow factTableRow) throws DataException {
		Object[] result = new Object[1];
		Integer value = new Integer(((Integer) factTableRow.getMeasureValue("measure1")).intValue() + 1);
		result[0] = value;
		return result;
	}

	@Override
	public MeasureInfo[] getAllComputedMeasureInfos() {
		return measureInfos;
	}

	@Override
	public void cleanUp() throws DataException {
	}
}

class JSMeasureFilterEvalHelper implements IJSFacttableFilterEvalHelper {

	@Override
	public boolean evaluateFilter(IFacttableRow facttableRow) throws DataException {
		Integer measureValue = (Integer) facttableRow.getMeasureValue("measure1");
		if (measureValue.intValue() % 2 == 0) {
			return false;
		} else {
			return true;
		}
	}

}

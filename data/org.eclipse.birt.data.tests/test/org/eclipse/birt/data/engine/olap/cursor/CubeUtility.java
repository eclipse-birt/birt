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

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import jakarta.olap.OLAPException;
import jakarta.olap.cursor.CubeCursor;
import jakarta.olap.cursor.DimensionCursor;
import jakarta.olap.cursor.EdgeCursor;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.DataEngineImpl;
import org.eclipse.birt.data.engine.impl.StopSign;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IDimensionDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IHierarchyDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IMeasureDefinition;
import org.eclipse.birt.data.engine.olap.data.api.CubeQueryExecutorHelper;
import org.eclipse.birt.data.engine.olap.data.api.ILevel;
import org.eclipse.birt.data.engine.olap.data.api.cube.DocManagerMap;
import org.eclipse.birt.data.engine.olap.data.api.cube.DocManagerReleaser;
import org.eclipse.birt.data.engine.olap.data.api.cube.ICube;
import org.eclipse.birt.data.engine.olap.data.api.cube.IDatasetIterator;
import org.eclipse.birt.data.engine.olap.data.api.cube.IDimension;
import org.eclipse.birt.data.engine.olap.data.api.cube.IHierarchy;
import org.eclipse.birt.data.engine.olap.data.api.cube.ILevelDefn;
import org.eclipse.birt.data.engine.olap.data.document.DocumentManagerFactory;
import org.eclipse.birt.data.engine.olap.data.document.IDocumentManager;
import org.eclipse.birt.data.engine.olap.data.impl.Cube;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Dimension;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.DimensionFactory;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.DimensionForTest;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.LevelDefinition;
import org.eclipse.birt.data.engine.olap.data.util.DataType;
import org.eclipse.birt.data.engine.olap.data.util.IDiskArray;
import org.eclipse.birt.data.engine.olap.impl.query.CubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.util.OlapExpressionUtil;

/**
 * Define one cube sample
 *
 */
public class CubeUtility {
	public static String cubeName = "cube";
	public static String timeCube = "timeCube";

	CubeUtility() {
	}

	void createCube(DataEngineImpl engine) throws IOException, BirtException, OLAPException {
		IDocumentManager documentManager = DocumentManagerFactory
				.createFileDocumentManager(engine.getSession().getTempDir(), cubeName);
		DocManagerMap.getDocManagerMap().set(String.valueOf(engine.hashCode()),
				engine.getSession().getTempDir() + cubeName, documentManager);
		engine.addShutdownListener(new DocManagerReleaser(engine));
		Dimension[] dimensions = new Dimension[6];

		// dimension0
		String[] levelNames = new String[1];
		levelNames[0] = "level11";
		DimensionForTest iterator = new DimensionForTest(levelNames);
		iterator.setLevelMember(0, TestFactTable1.DIM0_L1Col);

		ILevelDefn[] levelDefs = new ILevelDefn[1];
		levelDefs[0] = new LevelDefinition("level11", new String[] { "level11" }, null);
		dimensions[0] = (Dimension) DimensionFactory.createDimension("dimension1", documentManager, iterator, levelDefs,
				false, new StopSign());
		IHierarchy hierarchy = dimensions[0].getHierarchy();
		IDiskArray allRow = dimensions[0].getAllRows(new StopSign());

		// dimension1
		levelNames = new String[1];
		levelNames[0] = "level12";
		iterator = new DimensionForTest(levelNames);
		iterator.setLevelMember(0, TestFactTable1.DIM0_L2Col);

		levelDefs = new ILevelDefn[1];
		levelDefs[0] = new LevelDefinition("level12", new String[] { "level12" }, null);
		dimensions[1] = (Dimension) DimensionFactory.createDimension("dimension2", documentManager, iterator, levelDefs,
				false, new StopSign());
		hierarchy = dimensions[1].getHierarchy();
		allRow = dimensions[1].getAllRows(new StopSign());

		// dimension2
		levelNames = new String[1];
		levelNames[0] = "level13";
		iterator = new DimensionForTest(levelNames);
		iterator.setLevelMember(0, TestFactTable1.DIM0_L3Col);

		levelDefs = new ILevelDefn[1];
		levelDefs[0] = new LevelDefinition("level13", new String[] { "level13" }, null);
		dimensions[2] = (Dimension) DimensionFactory.createDimension("dimension3", documentManager, iterator, levelDefs,
				false, new StopSign());
		hierarchy = dimensions[2].getHierarchy();
		allRow = dimensions[2].getAllRows(new StopSign());

		// dimension3
		levelNames = new String[1];
		levelNames[0] = "level14";
		iterator = new DimensionForTest(levelNames);
		iterator.setLevelMember(0, TestFactTable1.DIM0_L4Col);

		levelDefs = new ILevelDefn[1];
		levelDefs[0] = new LevelDefinition("level14", new String[] { "level14" }, null);
		dimensions[3] = (Dimension) DimensionFactory.createDimension("dimension4", documentManager, iterator, levelDefs,
				false, new StopSign());
		hierarchy = dimensions[3].getHierarchy();
		allRow = dimensions[3].getAllRows(new StopSign());

		// dimension4
		levelNames = new String[1];
		levelNames[0] = "level21";
		iterator = new DimensionForTest(levelNames);
		iterator.setLevelMember(0, TestFactTable1.DIM1_L1Col);

		levelDefs = new ILevelDefn[1];
		levelDefs[0] = new LevelDefinition("level21", new String[] { "level21" }, null);
		dimensions[4] = (Dimension) DimensionFactory.createDimension("dimension5", documentManager, iterator, levelDefs,
				false, new StopSign());
		hierarchy = dimensions[4].getHierarchy();
		allRow = dimensions[4].getAllRows(new StopSign());

		// dimension5
		levelNames = new String[] { "level22", "attributes220" };
		iterator = new DimensionForTest(levelNames);
		iterator.setLevelMember(0, TestFactTable1.DIM1_L2Col);
		iterator.setLevelMember(1, TestFactTable1.ATTRIBUTE_Col);

		levelDefs = new ILevelDefn[1];
		levelDefs[0] = new LevelDefinition("level22", new String[] { "level22" }, new String[] { "attributes220" });
		dimensions[5] = (Dimension) DimensionFactory.createDimension("dimension6", documentManager, iterator, levelDefs,
				false, new StopSign());
		hierarchy = dimensions[5].getHierarchy();
		allRow = dimensions[5].getAllRows(new StopSign());

		TestFactTable1 factTable2 = new TestFactTable1();
		String[] measureColumnName = new String[1];
		measureColumnName[0] = "measure1";
		Cube cube = new Cube(cubeName, documentManager);

		cube.create(getKeyColNames(dimensions), dimensions, factTable2, measureColumnName, new StopSign());
		cube.close();
		documentManager.flush();
	}

	ICube getCube(String cubeName, DataEngineImpl engine) throws DataException, IOException {
		ICube cube;
		IDocumentManager documentManager = DocumentManagerFactory
				.loadFileDocumentManager(engine.getSession().getTempDir(), cubeName);
		cube = CubeQueryExecutorHelper.loadCube(cubeName, documentManager, engine.getSession().getStopSign());
		return cube;
	}

	void createCube1(DataEngineImpl engine) throws IOException, BirtException, OLAPException {
		IDocumentManager documentManager = DocumentManagerFactory
				.createFileDocumentManager(engine.getSession().getTempDir(), timeCube);
		DocManagerMap.getDocManagerMap().set(String.valueOf(engine.hashCode()),
				engine.getSession().getTempDir() + timeCube, documentManager);
		engine.addShutdownListener(new DocManagerReleaser(engine));
		Dimension[] dimensions = new Dimension[6];

		// dimension0
		String[] levelNames = new String[1];
		levelNames[0] = "level11";
		DimensionForTest iterator = new DimensionForTest(levelNames);
		iterator.setLevelMember(0, TestFactTable2.DIM0_L1Col);

		ILevelDefn[] levelDefs = new ILevelDefn[1];
		levelDefs[0] = new LevelDefinition("level11", new String[] { "level11" }, null);
		dimensions[0] = (Dimension) DimensionFactory.createDimension("dimension1", documentManager, iterator, levelDefs,
				false, new StopSign());
		IHierarchy hierarchy = dimensions[0].getHierarchy();
		IDiskArray allRow = dimensions[0].getAllRows(new StopSign());

		// dimension1
		levelNames = new String[1];
		levelNames[0] = "level12";
		iterator = new DimensionForTest(levelNames);
		iterator.setLevelMember(0, TestFactTable2.DIM0_L2Col);

		levelDefs = new ILevelDefn[1];
		levelDefs[0] = new LevelDefinition("level12", new String[] { "level12" }, null);
		dimensions[1] = (Dimension) DimensionFactory.createDimension("dimension2", documentManager, iterator, levelDefs,
				false, new StopSign());
		hierarchy = dimensions[1].getHierarchy();
		allRow = dimensions[1].getAllRows(new StopSign());

		// dimension2
		levelNames = new String[1];
		levelNames[0] = "level13";
		iterator = new DimensionForTest(levelNames);
		iterator.setLevelMember(0, TestFactTable2.DIM0_L3Col);

		levelDefs = new ILevelDefn[1];
		levelDefs[0] = new LevelDefinition("level13", new String[] { "level13" }, null);
		dimensions[2] = (Dimension) DimensionFactory.createDimension("dimension3", documentManager, iterator, levelDefs,
				false, new StopSign());
		hierarchy = dimensions[2].getHierarchy();
		allRow = dimensions[2].getAllRows(new StopSign());

		// dimension3
		levelNames = new String[1];
		levelNames[0] = "level14";
		iterator = new DimensionForTest(levelNames);
		iterator.setLevelMember(0, TestFactTable2.DIM0_L4Col);

		levelDefs = new ILevelDefn[1];
		levelDefs[0] = new LevelDefinition("level14", new String[] { "level14" }, null);
		dimensions[3] = (Dimension) DimensionFactory.createDimension("dimension4", documentManager, iterator, levelDefs,
				false, new StopSign());
		hierarchy = dimensions[3].getHierarchy();
		allRow = dimensions[3].getAllRows(new StopSign());

		// dimension4
		levelNames = new String[1];
		levelNames[0] = "level21";
		iterator = new DimensionForTest(levelNames);
		iterator.setLevelMember(0, TestFactTable2.DIM1_L1Col);

		levelDefs = new ILevelDefn[1];
		levelDefs[0] = new LevelDefinition("level21", new String[] { "level21" }, null);
		dimensions[4] = (Dimension) DimensionFactory.createDimension("dimension5", documentManager, iterator, levelDefs,
				false, new StopSign());
		hierarchy = dimensions[4].getHierarchy();
		allRow = dimensions[4].getAllRows(new StopSign());

		// dimension5
		levelNames = new String[] { "level22", "attributes220" };
		iterator = new DimensionForTest(levelNames);
		iterator.setLevelMember(0, TestFactTable2.DIM1_L2Col);

		levelDefs = new ILevelDefn[1];
		levelDefs[0] = new LevelDefinition("level22", new String[] { "level22" }, new String[] {});
		dimensions[5] = (Dimension) DimensionFactory.createDimension("dimension6", documentManager, iterator, levelDefs,
				false, new StopSign());
		hierarchy = dimensions[5].getHierarchy();
		allRow = dimensions[5].getAllRows(new StopSign());

		TestFactTable2 factTable2 = new TestFactTable2();
		String[] measureColumnName = new String[1];
		measureColumnName[0] = "measure1";
		Cube cube = new Cube(timeCube, documentManager);

		cube.create(getKeyColNames(dimensions), dimensions, factTable2, measureColumnName, new StopSign());
		cube.close();
		documentManager.flush();
	}

	ICubeQueryDefinition createQueryDefinition() {
		ICubeQueryDefinition cqd = new CubeQueryDefinition(cubeName);

		IMeasureDefinition measure = cqd.createMeasure("measure1");
		measure.setAggrFunction("SUM");

		IEdgeDefinition rowEdge = cqd.createEdge(ICubeQueryDefinition.ROW_EDGE);
		IDimensionDefinition productLineDim1 = rowEdge.createDimension("dimension5");
		IHierarchyDefinition porductLineHie1 = productLineDim1.createHierarchy("dimension5");
		ILevelDefinition columnLevel1 = porductLineHie1.createLevel("level21");

		IDimensionDefinition productLineDim2 = rowEdge.createDimension("dimension6");
		IHierarchyDefinition porductLineHie2 = productLineDim2.createHierarchy("dimension6");
		ILevelDefinition columnLevel2 = porductLineHie2.createLevel("level22");

		IEdgeDefinition columnEdge = cqd.createEdge(ICubeQueryDefinition.COLUMN_EDGE);
		IDimensionDefinition geographyDim1 = columnEdge.createDimension("dimension1");
		IHierarchyDefinition geographyHier1 = geographyDim1.createHierarchy("dimension1");
		geographyHier1.createLevel("level11");

		IDimensionDefinition geographyDim2 = columnEdge.createDimension("dimension2");
		IHierarchyDefinition geographyHier2 = geographyDim2.createHierarchy("dimension2");
		geographyHier2.createLevel("level12");

		IDimensionDefinition geographyDim3 = columnEdge.createDimension("dimension3");
		IHierarchyDefinition geographyHier3 = geographyDim3.createHierarchy("dimension3");
		ILevelDefinition startLevel = geographyHier3.createLevel("level13");

		IDimensionDefinition geographyDim4 = columnEdge.createDimension("dimension4");
		IHierarchyDefinition geographyHier4 = geographyDim4.createHierarchy("dimension4");
		geographyHier4.createLevel("level14");
		return cqd;
	}

	ICubeQueryDefinition createMirroredQueryDefinition(String cubeName, boolean breakHierarchy) {
		ICubeQueryDefinition cqd = new CubeQueryDefinition(cubeName);

		IMeasureDefinition measure = cqd.createMeasure("measure1");
		measure.setAggrFunction("SUM");

		IEdgeDefinition columnEdge = cqd.createEdge(ICubeQueryDefinition.ROW_EDGE);
		IDimensionDefinition productLineDim1 = columnEdge.createDimension("dimension5");
		IHierarchyDefinition porductLineHie1 = productLineDim1.createHierarchy("dimension5");
		ILevelDefinition columnLevel1 = porductLineHie1.createLevel("level21");

		IDimensionDefinition productLineDim2 = columnEdge.createDimension("dimension6");
		IHierarchyDefinition porductLineHie2 = productLineDim2.createHierarchy("dimension6");
		ILevelDefinition columnLevel2 = porductLineHie2.createLevel("level22");

		columnEdge.creatMirrorDefinition(columnLevel2, breakHierarchy);

		IEdgeDefinition rowEdge = cqd.createEdge(ICubeQueryDefinition.COLUMN_EDGE);
		IDimensionDefinition geographyDim1 = rowEdge.createDimension("dimension1");
		IHierarchyDefinition geographyHier1 = geographyDim1.createHierarchy("dimension1");
		geographyHier1.createLevel("level11");

		IDimensionDefinition geographyDim2 = rowEdge.createDimension("dimension2");
		IHierarchyDefinition geographyHier2 = geographyDim2.createHierarchy("dimension2");
		geographyHier2.createLevel("level12");

		IDimensionDefinition geographyDim3 = rowEdge.createDimension("dimension3");
		IHierarchyDefinition geographyHier3 = geographyDim3.createHierarchy("dimension3");
		ILevelDefinition startLevel = geographyHier3.createLevel("level13");

		IDimensionDefinition geographyDim4 = rowEdge.createDimension("dimension4");
		IHierarchyDefinition geographyHier4 = geographyDim4.createHierarchy("dimension4");
		geographyHier4.createLevel("level14");

		rowEdge.creatMirrorDefinition(startLevel, breakHierarchy);
		return cqd;
	}

	ICubeQueryDefinition createMirroredQueryDefinitionWithPage() {
		ICubeQueryDefinition cqd = new CubeQueryDefinition(cubeName);

		IMeasureDefinition measure = cqd.createMeasure("measure1");
		measure.setAggrFunction("SUM");
		IEdgeDefinition pageEdge = cqd.createEdge(ICubeQueryDefinition.PAGE_EDGE);
		IDimensionDefinition pageDim1 = pageEdge.createDimension("dimension5");
		IHierarchyDefinition pagetLineHie1 = pageDim1.createHierarchy("dimension5");
		ILevelDefinition pageLevel1 = pagetLineHie1.createLevel("level21");

		IEdgeDefinition columnEdge = cqd.createEdge(ICubeQueryDefinition.ROW_EDGE);
		IDimensionDefinition productLineDim2 = columnEdge.createDimension("dimension6");
		IHierarchyDefinition porductLineHie2 = productLineDim2.createHierarchy("dimension6");
		ILevelDefinition columnLevel2 = porductLineHie2.createLevel("level22");

		columnEdge.setMirrorStartingLevel(columnLevel2);

		IEdgeDefinition rowEdge = cqd.createEdge(ICubeQueryDefinition.COLUMN_EDGE);
		IDimensionDefinition geographyDim1 = rowEdge.createDimension("dimension1");
		IHierarchyDefinition geographyHier1 = geographyDim1.createHierarchy("dimension1");
		geographyHier1.createLevel("level11");

		IDimensionDefinition geographyDim2 = rowEdge.createDimension("dimension2");
		IHierarchyDefinition geographyHier2 = geographyDim2.createHierarchy("dimension2");
		geographyHier2.createLevel("level12");

		IDimensionDefinition geographyDim3 = rowEdge.createDimension("dimension3");
		IHierarchyDefinition geographyHier3 = geographyDim3.createHierarchy("dimension3");
		geographyHier3.createLevel("level13");

		IDimensionDefinition geographyDim4 = rowEdge.createDimension("dimension4");
		IHierarchyDefinition geographyHier4 = geographyDim4.createHierarchy("dimension4");
		ILevelDefinition startLevel = geographyHier4.createLevel("level14");

		rowEdge.setMirrorStartingLevel(startLevel);
		return cqd;
	}

	/**
	 *
	 * @param dataCursor
	 * @throws OLAPException
	 */
	public static void close(CubeCursor dataCursor) throws OLAPException {
		for (int i = 0; i < dataCursor.getOrdinateEdge().size(); i++) {
			EdgeCursor edge = (EdgeCursor) (dataCursor.getOrdinateEdge().get(i));
			edge.close();
		}
		dataCursor.close();
	}

	ICubeQueryDefinition createQueryDefintionWithPage1() {
		ICubeQueryDefinition cqd = new CubeQueryDefinition(cubeName);

		IMeasureDefinition measure = cqd.createMeasure("measure1");
		measure.setAggrFunction("SUM");
		IEdgeDefinition rowEdge = cqd.createEdge(ICubeQueryDefinition.ROW_EDGE);
		IDimensionDefinition productLineDim1 = rowEdge.createDimension("dimension5");
		IHierarchyDefinition porductLineHie1 = productLineDim1.createHierarchy("dimension5");
		ILevelDefinition columnLevel1 = porductLineHie1.createLevel("level21");

		IDimensionDefinition productLineDim2 = rowEdge.createDimension("dimension6");
		IHierarchyDefinition porductLineHie2 = productLineDim2.createHierarchy("dimension6");
		ILevelDefinition columnLevel2 = porductLineHie2.createLevel("level22");

		IEdgeDefinition columnEdge = cqd.createEdge(ICubeQueryDefinition.COLUMN_EDGE);
		IDimensionDefinition geographyDim1 = columnEdge.createDimension("dimension1");
		IHierarchyDefinition geographyHier1 = geographyDim1.createHierarchy("dimension1");
		geographyHier1.createLevel("level11");

		IDimensionDefinition geographyDim2 = columnEdge.createDimension("dimension2");
		IHierarchyDefinition geographyHier2 = geographyDim2.createHierarchy("dimension2");
		geographyHier2.createLevel("level12");

		IDimensionDefinition geographyDim3 = columnEdge.createDimension("dimension3");
		IHierarchyDefinition geographyHier3 = geographyDim3.createHierarchy("dimension3");
		ILevelDefinition startLevel = geographyHier3.createLevel("level13");

		IEdgeDefinition pageEdge = cqd.createEdge(ICubeQueryDefinition.PAGE_EDGE);
		IDimensionDefinition geographyDim4 = pageEdge.createDimension("dimension4");
		IHierarchyDefinition geographyHier4 = geographyDim4.createHierarchy("dimension4");
		geographyHier4.createLevel("level14");

		return cqd;
	}

	ICubeQueryDefinition createQueryDefintionWithPage2() {
		ICubeQueryDefinition cqd = new CubeQueryDefinition(cubeName);

		IMeasureDefinition measure = cqd.createMeasure("measure1");
		measure.setAggrFunction("SUM");
		IEdgeDefinition rowEdge = cqd.createEdge(ICubeQueryDefinition.ROW_EDGE);
		IDimensionDefinition productLineDim1 = rowEdge.createDimension("dimension5");
		IHierarchyDefinition porductLineHie1 = productLineDim1.createHierarchy("dimension5");
		ILevelDefinition columnLevel1 = porductLineHie1.createLevel("level21");

		IDimensionDefinition productLineDim2 = rowEdge.createDimension("dimension6");
		IHierarchyDefinition porductLineHie2 = productLineDim2.createHierarchy("dimension6");
		ILevelDefinition columnLevel2 = porductLineHie2.createLevel("level22");

		IEdgeDefinition columnEdge = cqd.createEdge(ICubeQueryDefinition.COLUMN_EDGE);
		IDimensionDefinition geographyDim2 = columnEdge.createDimension("dimension2");
		IHierarchyDefinition geographyHier2 = geographyDim2.createHierarchy("dimension2");
		geographyHier2.createLevel("level12");

		IDimensionDefinition geographyDim3 = columnEdge.createDimension("dimension3");
		IHierarchyDefinition geographyHier3 = geographyDim3.createHierarchy("dimension3");
		ILevelDefinition startLevel = geographyHier3.createLevel("level13");

		IEdgeDefinition pageEdge = cqd.createEdge(ICubeQueryDefinition.PAGE_EDGE);
		IDimensionDefinition geographyDim4 = pageEdge.createDimension("dimension4");
		IHierarchyDefinition geographyHier4 = geographyDim4.createHierarchy("dimension4");
		geographyHier4.createLevel("level14");

		IDimensionDefinition geographyDim1 = pageEdge.createDimension("dimension1");
		IHierarchyDefinition geographyHier1 = geographyDim1.createHierarchy("dimension1");
		geographyHier1.createLevel("level11");

		return cqd;
	}

	String printCubeAlongEdge(CubeCursor cursor, List columnEdgeBindingNames, List rowEdgeBindingNames,
			List measureBindingNames, List rowGrandTotal, String columnGrandTotal, String totalGrandTotal,
			List countryGrandTotal) throws Exception {
		EdgeCursor edge1 = null, edge2 = null;
		if (columnEdgeBindingNames.size() > 0) {
			edge1 = (EdgeCursor) cursor.getOrdinateEdge().get(0);
		}
		if (rowEdgeBindingNames.size() > 0) {
			edge2 = (EdgeCursor) cursor.getOrdinateEdge().get(cursor.getOrdinateEdge().size() - 1);
		}
		String[] lines = {};
		if (edge1 != null) {
			lines = new String[edge1.getDimensionCursor().size()];
		}

		Arrays.fill(lines, "		");

		if (edge1 != null) {
			while (edge1.next()) {
				for (int i = 0; i < columnEdgeBindingNames.size(); i++) {
					DimensionCursor dimCursor = (DimensionCursor) edge1.getDimensionCursor().get(i);
					lines[i] += dimCursor.getObject(columnEdgeBindingNames.get(i).toString()) + "   ";
				}
			}
		}

		String output = "";
		for (int i = 0; i < lines.length; i++) {
			output += "\n" + lines[i];
		}
		if (edge2 != null) {
			while (edge2.next()) {
				String line = "";
				for (int k = 0; k < rowEdgeBindingNames.size(); k++) {
					DimensionCursor dimCursor = (DimensionCursor) edge2.getDimensionCursor().get(k);
					line += dimCursor.getObject(rowEdgeBindingNames.get(k).toString()).toString() + "   ";
				}
				if (edge1 != null) {
					edge1.beforeFirst();
					while (edge1.next()) {
						DimensionCursor countryCursor = (DimensionCursor) edge1.getDimensionCursor().get(0);
						if (measureBindingNames != null) {
							for (int j = 0; j < measureBindingNames.size(); j++) {
								line += cursor.getObject(OlapExpressionUtil
										.createMeasureCalculateMemeberName(measureBindingNames.get(j).toString()))
										+ ",";
							}
							if (countryGrandTotal != null) {
								for (int k = 0; k < countryGrandTotal.size(); k++) {
									if (edge1.getPosition() == countryCursor.getEdgeEnd()
											&& countryGrandTotal != null) {
										line += cursor.getObject(countryGrandTotal.get(k).toString());
									}
								}
							}
							line += "  ";
						}
					}
				} else if (measureBindingNames != null) {
					for (int j = 0; j < measureBindingNames.size(); j++) {
						line += cursor
								.getObject(OlapExpressionUtil
										.createMeasureCalculateMemeberName(measureBindingNames.get(j).toString()))
								+ ",";
					}
					line += "  ";
				}
				if (rowGrandTotal != null) {
					for (int j = 0; j < rowGrandTotal.size(); j++) {
						line += cursor.getObject(rowGrandTotal.get(j).toString()) + "&";
					}
				}
				output += "\n" + line;

			}
		}

		if (columnGrandTotal != null) {
			output += "\n" + columnGrandTotal + "  ";
			edge1.beforeFirst();
			while (edge1.next()) {
				output += cursor.getObject(columnGrandTotal) + "   ";
			}
		}

		if (totalGrandTotal != null) {
			output += cursor.getObject(totalGrandTotal);
		}
		System.out.print(output);
		return output;
	}

	String printCubeAlongDimension(CubeCursor dataCursor, DimensionCursor countryCursor, DimensionCursor cityCursor,
			DimensionCursor streetCursor, DimensionCursor timeCursor, DimensionCursor productCursor1,
			DimensionCursor productCursor2) throws OLAPException {
		String[] lines = new String[4];
		Arrays.fill(lines, "		");
		if (countryCursor != null && cityCursor != null && timeCursor != null) {
			countryCursor.beforeFirst();
			while (countryCursor.next()) {
				cityCursor.beforeFirst();
				while (cityCursor.next()) {
					streetCursor.beforeFirst();
					while (streetCursor.next()) {
						timeCursor.beforeFirst();
						while (timeCursor.next()) {
							lines[0] += countryCursor.getObject("level11") + "         ";
							lines[1] += cityCursor.getObject("level12") + "         ";
							lines[2] += streetCursor.getObject("level13") + "         ";
							lines[3] += timeCursor.getObject("level14") + "         ";
						}
					}
				}
			}
		}

		String output = "";
		for (int i = 0; i < lines.length; i++) {
			output += "\n" + lines[i];
		}

		lines = new String[1];
		lines[0] = "";
		if (productCursor1 != null && productCursor2 != null && countryCursor != null && cityCursor != null
				&& timeCursor != null) {
			productCursor1.beforeFirst();
			while (productCursor1.next()) {
				productCursor2.beforeFirst();
				while (productCursor2.next()) {
					lines[0] += productCursor1.getObject("level21") + "   ";
					lines[0] += productCursor2.getObject("level22") + "   ";
					countryCursor.beforeFirst();
					while (countryCursor.next()) {
						cityCursor.beforeFirst();
						while (cityCursor.next()) {

							timeCursor.beforeFirst();
							while (timeCursor.next()) {
								lines[0] += dataCursor.getObject(
										OlapExpressionUtil.createMeasureCalculateMemeberName("measure1")) + "  ";
							}
						}
					}
					lines[0] += "  \n";
				}
				lines[0] += "  \n";
			}
		} else if (countryCursor != null && cityCursor != null && timeCursor != null) {
			countryCursor.beforeFirst();
			lines[0] += "           ";
			while (countryCursor.next()) {
				cityCursor.beforeFirst();
				while (cityCursor.next()) {

					timeCursor.beforeFirst();
					while (timeCursor.next()) {
						lines[0] += dataCursor
								.getObject(OlapExpressionUtil.createMeasureCalculateMemeberName("measure1")) + "  ";
					}
				}
			}
			lines[0] += "  \n";
		} else if (productCursor1 != null && productCursor2 != null) {
			productCursor1.beforeFirst();

			while (productCursor1.next()) {
				productCursor2.beforeFirst();
				while (productCursor2.next()) {
					lines[0] += productCursor1.getObject("level21") + "  ";
					lines[0] += productCursor2.getObject("level22") + "  ";
					lines[0] += dataCursor.getObject(OlapExpressionUtil.createMeasureCalculateMemeberName("measure1"))
							+ "  ";
					lines[0] += "  \n";
				}
			}
			lines[0] += "  \n";
		}
		output += "\n" + lines[0];
		System.out.print(output);
		return output;
	}

	String printCubeAlongPageEdge(CubeCursor cursor, List pageEdgeBindingName, List columnEdgeBindingNames,
			List rowEdgeBindingNames, List measureBindingNames, List rowGrandTotal, String columnGrandTotal,
			String totalGrandTotal, List countryGrandTotal) throws Exception {
		EdgeCursor pageCursor = (EdgeCursor) cursor.getPageEdge().toArray()[0];
		pageCursor.beforeFirst();
		String output = "";
		while (pageCursor.next()) {
			cursor.synchronizePages();
			for (int i = 0; i < pageEdgeBindingName.size(); i++) {
				output += "\n" + ((DimensionCursor) pageCursor.getDimensionCursor().get(i))
						.getObject(pageEdgeBindingName.get(i).toString());
			}
			output += this.printCubeAlongEdge(cursor, columnEdgeBindingNames, rowEdgeBindingNames, measureBindingNames,
					rowGrandTotal, columnGrandTotal, totalGrandTotal, countryGrandTotal);
		}

		return output;
	}

	/**
	 *
	 * @param dimensions
	 * @return
	 */
	public static String[][] getKeyColNames(IDimension[] dimensions) {
		String[][] keyColumnName = new String[dimensions.length][];
		for (int i = 0; i < dimensions.length; i++) {
			ILevel[] levels = dimensions[i].getHierarchy().getLevels();
			ILevel detailLevel = levels[levels.length - 1];
			keyColumnName[i] = detailLevel.getKeyNames();
		}
		return keyColumnName;
	}
}

class TestFactTable1 implements IDatasetIterator {

	int ptr = -1;
	static String[] DIM0_L1Col = { "CN", "CN", "CN", "CN", "CN", "CN", "CN", "CN", "US", "US", "US", "US", "US", "US",
			"US", "US", "US", "UN", "UN", "UN", "JP", "JP", "JP", "JP" };
	static String[] DIM0_L2Col = { "SH", "SH", "SH", "BJ", "BJ", "BJ", "SZ", "HZ", "LA", "LA", "LA", "CS", "CS", "CS",
			"CS", "CS", "NY", "LD", "LD", "LP", "TK", "IL", "IL", "IL" };
	static String[] DIM0_L3Col = { "A1", "A1", "A1", "A1", "A1", "A4", "A1", "A2", "A2", "A2", "A2", "A2", "A3", "A4",
			"A4", "A4", "A1", "A1", "A3", "A3", "A4", "A4", "A4", "A4" };

	static String[] DIM0_L4Col = { "1998", "2000", "2002", "2000", "2001", "2002", "1998", "1998", "1998", "2001",
			"2002", "1998", "1999", "2000", "2001", "2002", "2002", "2001", "2002", "1998", "1999", "1999", "2000",
			"2002", };

	static String[] DIM1_L1Col = { "P1", "P1", "P2", "P2", "P3", "P3", "P2", "P3", "P3", "P3", "P3", "P1", "P1", "P1",
			"P2", "P3", "P1", "P2", "P2", "P2", "P3", "P2", "P2", "P3", };

	static String[] DIM1_L2Col = { "PP1", "PP2", "PP3", "PP1", "PP2", "PP3", "PP1", "PP1", "PP1", "PP2", "PP3", "PP1",
			"PP2", "PP3", "PP1", "PP2", "PP1", "PP2", "PP3", "PP1", "PP2", "PP2", "PP3", "PP1", };
	static int[] MEASURE_Col = { 1, 2, 3, 6, 7, 8, 11, 16, 21, 22, 23, 36, 37, 38, 39, 40, 41, 46, 47, 51, 56, 61, 64,
			65 };
	static int[] ATTRIBUTE_Col = { 1, 2, 3, 1, 2, 3, 1, 1, 1, 2, 3, 1, 2, 3, 1, 2, 1, 2, 3, 1, 2, 2, 3, 1 };

	@Override
	public void close() throws BirtException {
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
		if (name.equals("level11")) {
			return 0;
		} else if (name.equals("level12")) {
			return 1;
		} else if (name.equals("level13")) {
			return 2;
		} else if (name.equals("level14")) {
			return 3;
		} else if (name.equals("level21")) {
			return 4;
		} else if (name.equals("level22")) {
			return 5;
		} else if (name.equals("measure1")) {
			return 6;
		}
		return -1;
	}

	@Override
	public int getFieldType(String name) throws BirtException {
		if (name.equals("level11")) {
			return DataType.STRING_TYPE;
		} else if (name.equals("level12")) {
			return DataType.STRING_TYPE;
		} else if (name.equals("level13")) {
			return DataType.STRING_TYPE;
		} else if (name.equals("level14")) {
			return DataType.STRING_TYPE;
		} else if (name.equals("level21")) {
			return DataType.STRING_TYPE;
		} else if (name.equals("level22")) {
			return DataType.STRING_TYPE;
		} else if (name.equals("measure1")) {
			return DataType.INTEGER_TYPE;
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
			return DIM0_L1Col[ptr];
		} else if (fieldIndex == 1) {
			return DIM0_L2Col[ptr];
		} else if (fieldIndex == 2) {
			return DIM0_L3Col[ptr];
		} else if (fieldIndex == 3) {
			return DIM0_L4Col[ptr];
		} else if (fieldIndex == 4) {
			return DIM1_L1Col[ptr];
		} else if (fieldIndex == 5) {
			return DIM1_L2Col[ptr];
		} else if (fieldIndex == 6) {
			return new Integer(MEASURE_Col[ptr]);
		}
		return null;
	}

	@Override
	public boolean next() throws BirtException {
		ptr++;
		if (ptr >= MEASURE_Col.length) {
			return false;
		}
		return true;
	}
}

class TestFactTable2 implements IDatasetIterator {

	int ptr = -1;
	static String[] DIM0_L1Col = { "CN", "CN", "CN", "CN", "US", "US", "US", "UN", "UN", "JP" };
	static String[] DIM0_L2Col = { "2007", "2007", "2008", "2008", "2007", "2007", "2008", "2007", "2007", "2008" };
	static String[] DIM0_L3Col = { "Q1", "Q1", "Q1", "Q2", "Q2", "Q3", "Q1", "Q1", "Q2", "Q4" };

	static String[] DIM0_L4Col = { "01", "02", "03", "04", "05", "07", "03", "02", "05", "10" };

	static String[] DIM1_L1Col = { "P1", "P1", "P2", "P2", "P3", "P3", "P2", "P3", "P3", "P3" };

	static String[] DIM1_L2Col = { "PP1", "PP2", "PP1", "PP3", "PP1", "PP2", "PP3", "PP1", "PP2", "PP1" };
	static int[] MEASURE_Col = { 1, 2, 3, 6, 7, 8, 11, 16, 21, 22, };

	@Override
	public void close() throws BirtException {
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
		if (name.equals("level11")) {
			return 0;
		} else if (name.equals("level12")) {
			return 1;
		} else if (name.equals("level13")) {
			return 2;
		} else if (name.equals("level14")) {
			return 3;
		} else if (name.equals("level21")) {
			return 4;
		} else if (name.equals("level22")) {
			return 5;
		} else if (name.equals("measure1")) {
			return 6;
		}
		return -1;
	}

	@Override
	public int getFieldType(String name) throws BirtException {
		if (name.equals("level11")) {
			return DataType.STRING_TYPE;
		} else if (name.equals("level12")) {
			return DataType.STRING_TYPE;
		} else if (name.equals("level13")) {
			return DataType.STRING_TYPE;
		} else if (name.equals("level14")) {
			return DataType.STRING_TYPE;
		} else if (name.equals("level21")) {
			return DataType.STRING_TYPE;
		} else if (name.equals("level22")) {
			return DataType.STRING_TYPE;
		} else if (name.equals("measure1")) {
			return DataType.INTEGER_TYPE;
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
			return DIM0_L1Col[ptr];
		} else if (fieldIndex == 1) {
			return DIM0_L2Col[ptr];
		} else if (fieldIndex == 2) {
			return DIM0_L3Col[ptr];
		} else if (fieldIndex == 3) {
			return DIM0_L4Col[ptr];
		} else if (fieldIndex == 4) {
			return DIM1_L1Col[ptr];
		} else if (fieldIndex == 5) {
			return DIM1_L2Col[ptr];
		} else if (fieldIndex == 6) {
			return new Integer(MEASURE_Col[ptr]);
		}
		return null;
	}

	@Override
	public boolean next() throws BirtException {
		ptr++;
		if (ptr >= MEASURE_Col.length) {
			return false;
		}
		return true;
	}
}

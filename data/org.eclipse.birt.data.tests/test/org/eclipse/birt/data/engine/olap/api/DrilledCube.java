/*******************************************************************************
 * Copyright (c) 2004 ,2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.olap.api;

import java.io.IOException;
import java.util.Date;

import javax.olap.OLAPException;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.impl.DataEngineImpl;
import org.eclipse.birt.data.engine.impl.StopSign;
import org.eclipse.birt.data.engine.olap.data.api.cube.DocManagerMap;
import org.eclipse.birt.data.engine.olap.data.api.cube.DocManagerReleaser;
import org.eclipse.birt.data.engine.olap.data.api.cube.IDatasetIterator;
import org.eclipse.birt.data.engine.olap.data.api.cube.IDimension;
import org.eclipse.birt.data.engine.olap.data.api.cube.ILevelDefn;
import org.eclipse.birt.data.engine.olap.data.document.DocumentManagerFactory;
import org.eclipse.birt.data.engine.olap.data.document.IDocumentManager;
import org.eclipse.birt.data.engine.olap.data.impl.Cube;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Dimension;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.DimensionFactory;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.DimensionForTest;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.LevelDefinition;
import org.eclipse.birt.data.engine.olap.data.util.DataType;

class DrilledCube {
	public static final String cubeName = "DrilledCube";

	void createCube(DataEngineImpl engine) throws IOException, BirtException, OLAPException {
		IDocumentManager documentManager = DocumentManagerFactory
				.createFileDocumentManager(engine.getSession().getTempDir(), String.valueOf(engine.hashCode()));
		DocManagerMap.getDocManagerMap().set(String.valueOf(engine.hashCode()),
				engine.getSession().getTempDir() + engine.hashCode(), documentManager);
		engine.addShutdownListener(new DocManagerReleaser(engine));
		Dimension[] dimensions = new Dimension[3];

		// dimension0
		String[] levelNames = new String[3];

		// dimension1
		levelNames[0] = "COUNTRY";
		levelNames[1] = "STATE";
		levelNames[2] = "CITY";

		DimensionForTest iterator = new DimensionForTest(levelNames);

		iterator.setLevelMember(0, DateFactTable.DIM0_COUNTRY_COL);
		iterator.setLevelMember(1, DateFactTable.DIM0_STATE_COL);
		iterator.setLevelMember(2, DateFactTable.DIM0_CITY_COL);

		ILevelDefn[] levelDefs = new ILevelDefn[3];
		levelDefs[0] = new LevelDefinition("COUNTRY", new String[] { "COUNTRY" }, null);

		levelDefs[1] = new LevelDefinition("STATE", new String[] { "STATE" }, null);

		levelDefs[2] = new LevelDefinition("CITY", new String[] { "CITY" }, null);

		dimensions[0] = (Dimension) DimensionFactory.createDimension("dimension1", documentManager, iterator, levelDefs,
				false, new StopSign());

		levelNames = new String[3];
		levelNames[0] = "YEAR";
		levelNames[1] = "QUARTER";
		levelNames[2] = "MONTH";

		iterator = new DimensionForTest(levelNames);
		iterator.setLevelMember(0, DateFactTable.DIM1_YEAR_COL);
		iterator.setLevelMember(1, DateFactTable.DIM1_QUARTER_COL);
		iterator.setLevelMember(2, DateFactTable.DIM1_MONTH_COL);

		levelDefs = new ILevelDefn[3];
		levelDefs[0] = new LevelDefinition("YEAR", new String[] { "YEAR" }, null);
		levelDefs[1] = new LevelDefinition("QUARTER", new String[] { "QUARTER" }, null);
		levelDefs[2] = new LevelDefinition("MONTH", new String[] { "MONTH" }, null);

		dimensions[1] = (Dimension) DimensionFactory.createDimension("dimension2", documentManager, iterator, levelDefs,
				false, new StopSign());

		levelNames = new String[2];
		levelNames[0] = "PRODUCTLINE";
		levelNames[1] = "PRODUCTTYPE";

		iterator = new DimensionForTest(levelNames);
		iterator.setLevelMember(0, DateFactTable.DIM2_PRODUCTLINE_COL1);
		iterator.setLevelMember(1, DateFactTable.DIM2_PRODUCTLINE_COL2);

		levelDefs = new ILevelDefn[2];
		levelDefs[0] = new LevelDefinition("PRODUCTLINE", new String[] { "PRODUCTLINE" }, null);
		levelDefs[1] = new LevelDefinition("PRODUCTTYPE", new String[] { "PRODUCTTYPE" }, null);

		dimensions[2] = (Dimension) DimensionFactory.createDimension("dimension3", documentManager, iterator, levelDefs,
				false, new StopSign());

		DateFactTable factTable2 = new DateFactTable();
		String[] measureColumnName = new String[1];
		measureColumnName[0] = "measure1";
		Cube cube = new Cube(cubeName, documentManager);

		cube.create(getKeyColNames(dimensions), dimensions, factTable2, measureColumnName, new StopSign());
		cube.close();
		documentManager.flush();
	}

	/**
	 * 
	 * @param dimensions
	 * @return
	 */
	public static String[][] getKeyColNames(IDimension[] dimensions) {
		String[][] keyColumnName = new String[3][];

		keyColumnName[0] = new String[] { "COUNTRY", "STATE", "CITY" };
		keyColumnName[1] = new String[] { "YEAR", "QUARTER", "MONTH" };
		keyColumnName[2] = new String[] { "PRODUCTLINE" };

		return keyColumnName;
	}
}

class DateFactTable implements IDatasetIterator {

	int ptr = -1;
	static String[] DIM0_COUNTRY_COL = { "CHINA", "CHINA", "CHINA", "CHINA", "FRANCE", "FRANCE", "FRANCE", "FRANCE",
			"USA", "USA", "USA", "USA", "USA", "USA" };
	static String[] DIM0_STATE_COL = { "STATE1", "STATE1", "STATE2", "STATE2", "STATE3", "STATE3", "STATE3", "STATE4",
			"STATE5", "STATE5", "STATE6", "STATE6", "STATE6", "STATE7" };

	static String[] DIM0_CITY_COL = { "CITY1", "CITY2", "CITY3", "CITY4", "CITY5", "CITY6", "CITY7", "CITY8", "CITY9",
			"CITY10", "CITY11", "CITY12", "CITY13", "CITY14" };

	static Integer[] DIM1_YEAR_COL = { 2003, 2004, 2003, 2004, 2003, 2004, 2003, 2004, 2003, 2004, 2003, 2004, 2003,
			2004 };
	static Integer[] DIM1_QUARTER_COL = { 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2 };

	static Integer[] DIM1_MONTH_COL = { 1, 4, 2, 5, 1, 4, 2, 5, 1, 4, 2, 5, 1, 4 };

	static String[] DIM2_PRODUCTLINE_COL1 = { "CAR", "CAR", "CAR", "CAR", "MOTOR", "MOTOR", "MOTOR", "MOTOR", "PLANE",
			"PLANE", "PLANE", "PLANE", "PLANE", "PLANE" };

	static String[] DIM2_PRODUCTLINE_COL2 = { "001", "002", "003", "004", "005", "006", "007", "008", "009", "010",
			"011", "012", "013", "014" };

	static int[] MEASURE_Col = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14 };

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

	public int getFieldIndex(String name) throws BirtException {
		if (name.equals("COUNTRY")) {
			return 0;
		} else if (name.equals("STATE")) {
			return 1;
		} else if (name.equals("CITY")) {
			return 2;
		} else if (name.equals("YEAR")) {
			return 3;
		} else if (name.equals("QUARTER")) {
			return 4;
		} else if (name.equals("MONTH")) {
			return 5;
		} else if (name.equals("PRODUCTLINE")) {
			return 6;
		} else if (name.equals("PRODUCTTYPE")) {
			return 7;
		} else if (name.equals("measure1")) {
			return 8;
		}
		return -1;
	}

	public int getFieldType(String name) throws BirtException {
		if (name.equals("COUNTRY") || name.equals("STATE") || name.equals("CITY") || name.equals("PRODUCTLINE")
				|| name.equals("PRODUCTNAME")) {
			return DataType.STRING_TYPE;
		} else if (name.equals("YEAR") || name.equals("MONTH") || name.equals("QUARTER")) {
			return DataType.INTEGER_TYPE;
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

	public Object getValue(int fieldIndex) throws BirtException {
		if (fieldIndex == 0) {
			return DIM0_COUNTRY_COL[ptr];
		} else if (fieldIndex == 1) {
			return DIM0_STATE_COL[ptr];
		} else if (fieldIndex == 2) {
			return DIM0_CITY_COL[ptr];
		} else if (fieldIndex == 3) {
			return DIM1_YEAR_COL[ptr];
		} else if (fieldIndex == 4) {
			return DIM1_QUARTER_COL[ptr];
		} else if (fieldIndex == 5) {
			return DIM1_MONTH_COL[ptr];
		} else if (fieldIndex == 6) {
			return DIM2_PRODUCTLINE_COL1[ptr];
		} else if (fieldIndex == 7) {
			return DIM2_PRODUCTLINE_COL2[ptr];
		} else if (fieldIndex == 8) {
			return new Integer(MEASURE_Col[ptr]);
		}
		return null;
	}

	public boolean next() throws BirtException {
		ptr++;
		if (ptr >= MEASURE_Col.length) {
			return false;
		}
		return true;
	}
}

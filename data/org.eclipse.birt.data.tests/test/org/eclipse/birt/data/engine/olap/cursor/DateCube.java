/*******************************************************************************
 * Copyright (c) 2004 ,2009 Actuate Corporation.
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
import java.util.Date;

import javax.olap.OLAPException;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.DataEngineImpl;
import org.eclipse.birt.data.engine.impl.StopSign;
import org.eclipse.birt.data.engine.olap.data.api.CubeQueryExecutorHelper;
import org.eclipse.birt.data.engine.olap.data.api.cube.CubeElementFactory;
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

public class DateCube {
	public static final String cubeName = "DateCube";

	public void createCube(DataEngineImpl engine) throws IOException, BirtException, OLAPException {
		IDocumentManager documentManager = DocumentManagerFactory
				.createFileDocumentManager(engine.getSession().getTempDir(), cubeName);
		DocManagerMap.getDocManagerMap().set(String.valueOf(engine.hashCode()),
				engine.getSession().getTempDir() + cubeName, documentManager);
		engine.addShutdownListener(new DocManagerReleaser(engine));
		Dimension[] dimensions = new Dimension[2];

		// dimension0
		String[] levelNames = new String[8];
//		levelNames[0] = "level11";

		// dimension1
		levelNames[0] = "level11";
		levelNames[1] = "year/DateTime";

		levelNames[2] = "level12";
		levelNames[3] = "quarter/DateTime";

		levelNames[4] = "level13";
		levelNames[5] = "month/DateTime";

		levelNames[6] = "level14";
		levelNames[7] = "day-of-month/DateTime";

		DimensionForTest iterator = new DimensionForTest(levelNames);

		iterator.setLevelMember(0, DateFactTable.DIM1_YEAR_Col);
		iterator.setLevelMember(1, DateFactTable.ATTRIBUTE_Col);
		iterator.setLevelMember(2, DateFactTable.DIM1_QUARTER_Col);
		iterator.setLevelMember(3, DateFactTable.ATTRIBUTE_Col);
		iterator.setLevelMember(4, DateFactTable.DIM1_MONTH_Col);
		iterator.setLevelMember(5, DateFactTable.ATTRIBUTE_Col);
		iterator.setLevelMember(6, DateFactTable.DIM1_DAY_Col);
		iterator.setLevelMember(7, DateFactTable.ATTRIBUTE_Col);

		ILevelDefn[] levelDefs = new ILevelDefn[5];

		levelDefs[0] = new LevelDefinition("level11", new String[] { "level11" }, new String[] { "year/DateTime" });
		levelDefs[0].setTimeType("year");

		levelDefs[1] = new LevelDefinition("level12", new String[] { "level12" }, new String[] { "quarter/DateTime" });
		levelDefs[1].setTimeType("quarter");

		levelDefs[2] = new LevelDefinition("level13", new String[] { "level13" }, new String[] { "month/DateTime" });
		levelDefs[2].setTimeType("month");

		levelDefs[3] = new LevelDefinition("level14", new String[] { "level14" },
				new String[] { "day-of-month/DateTime" });
		levelDefs[3].setTimeType("day-of-month");

		levelDefs[4] = CubeElementFactory.createLevelDefinition("_${INTERNAL_INDEX}$_",
				new String[] { "year/DateTime", "quarter/DateTime", "month/DateTime", "day-of-month/DateTime" },
				new String[0]);

		dimensions[0] = (Dimension) DimensionFactory.createDimension("dimension1", documentManager, iterator, levelDefs,
				false, new StopSign());
		IHierarchy hierarchy = dimensions[0].getHierarchy();
		IDiskArray allRow = dimensions[0].getAllRows(new StopSign());

		levelNames = new String[1];
		levelNames[0] = "level21";
		iterator = new DimensionForTest(levelNames);
		iterator.setLevelMember(0, DateFactTable.DIM2_L2Col);

		levelDefs = new ILevelDefn[1];
		levelDefs[0] = new LevelDefinition("level21", new String[] { "level21" }, null);
		dimensions[1] = (Dimension) DimensionFactory.createDimension("dimension2", documentManager, iterator, levelDefs,
				false, new StopSign());
		hierarchy = dimensions[1].getHierarchy();
		allRow = dimensions[1].getAllRows(new StopSign());

		DateFactTable factTable2 = new DateFactTable();
		String[] measureColumnName = new String[1];
		measureColumnName[0] = "measure1";
		Cube cube = new Cube(cubeName, documentManager);

		cube.create(getKeyColNames(dimensions), dimensions, factTable2, measureColumnName, new StopSign());
		cube.close();
		documentManager.flush();
	}

	public ICube getCube(String cubeName, DataEngineImpl engine) throws DataException, IOException {
		ICube cube = null;
		IDocumentManager documentManager = DocumentManagerFactory
				.loadFileDocumentManager(engine.getSession().getTempDir(), cubeName);
		cube = CubeQueryExecutorHelper.loadCube(cubeName, documentManager, engine.getSession().getStopSign());
		return cube;
	}

	/**
	 * 
	 * @param dimensions
	 * @return
	 */
	public static String[][] getKeyColNames(IDimension[] dimensions) {
		String[][] keyColumnName = new String[2][];

		keyColumnName[0] = new String[] { "level11", "level12", "level13", "level14" };
		keyColumnName[1] = new String[] { "level21" };

		return keyColumnName;
	}
}

class DateFactTable implements IDatasetIterator {

	int ptr = -1;
	static Integer[] DIM1_YEAR_Col = { 1998, 1999, 1999, 1999, 1999, 1999, 1998, 2000 };

	static Integer[] DIM1_QUARTER_Col = { 1, 2, 2, 3, 3, 3, 1, 4 };

	static Integer[] DIM1_MONTH_Col = { 1, 4, 5, 8, 9, 8, 2, 11 };

	static Integer[] DIM1_DAY_Col = { 15, 16, 17, 18, 19, 20, 21, 22 };

	static String[] DIM2_L2Col = { "PP1", "PP2", "PP1", "PP2", "PP1", "PP2", "PP1", "PP2" };
	static int[] MEASURE_Col = { 1, 2, 11, 16, 23, 36, 38, 39, };

	static Date[] ATTRIBUTE_Col = { new Date(98, 0, 1), new Date(98, 4, 1), new Date(99, 0, 1), new Date(99, 4, 1),
			new Date(97, 0, 1), new Date(96, 4, 1), new Date(95, 0, 1), new Date(94, 4, 1) };

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
		}

		else if (name.equals("measure1")) {
			return 5;
		}
		return -1;
	}

	public int getFieldType(String name) throws BirtException {
		if (name.equals("level11")) {
			return DataType.INTEGER_TYPE;
		} else if (name.equals("level12")) {
			return DataType.INTEGER_TYPE;
		} else if (name.equals("level13")) {
			return DataType.INTEGER_TYPE;
		} else if (name.equals("level14")) {
			return DataType.INTEGER_TYPE;
		} else if (name.equals("level21")) {
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

	public Object getValue(int fieldIndex) throws BirtException {
		if (fieldIndex == 0) {
			return DIM1_YEAR_Col[ptr];
		} else if (fieldIndex == 1) {
			return DIM1_QUARTER_Col[ptr];
		} else if (fieldIndex == 2) {
			return DIM1_MONTH_Col[ptr];
		} else if (fieldIndex == 3) {
			return DIM1_DAY_Col[ptr];
		} else if (fieldIndex == 4) {
			return DIM2_L2Col[ptr];
		} else if (fieldIndex == 5) {
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

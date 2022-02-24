
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
package org.eclipse.birt.data.engine.olap.data.impl.dimension;

import java.io.IOException;
import java.util.Date;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.StopSign;
import org.eclipse.birt.data.engine.olap.data.api.DimLevel;
import org.eclipse.birt.data.engine.olap.data.api.ILevel;
import org.eclipse.birt.data.engine.olap.data.api.ISelection;
import org.eclipse.birt.data.engine.olap.data.api.cube.IDatasetIterator;
import org.eclipse.birt.data.engine.olap.data.api.cube.IDimension;
import org.eclipse.birt.data.engine.olap.data.api.cube.IHierarchy;
import org.eclipse.birt.data.engine.olap.data.api.cube.ILevelDefn;
import org.eclipse.birt.data.engine.olap.data.document.DocumentManagerFactory;
import org.eclipse.birt.data.engine.olap.data.document.IDocumentManager;
import org.eclipse.birt.data.engine.olap.data.impl.SelectionFactory;
import org.eclipse.birt.data.engine.olap.data.util.DataType;
import org.eclipse.birt.data.engine.olap.data.util.IDiskArray;
import org.eclipse.birt.data.engine.olap.data.util.IndexKey;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * 
 */

public class DimensionTest2 {

	private DimLevel dimLevel12;
	private DimLevel dimLevel11;

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	/*
	 * @see TestCase#tearDown()
	 */
	/**
	 * 
	 * @throws IOException
	 * @throws BirtException
	 */
	@Test
	public void testDimensionCreateAndFind() throws IOException, BirtException {
		IDocumentManager documentManager = DocumentManagerFactory.createFileDocumentManager();
		testDimensionCreateAndFind(documentManager);
		documentManager.close();
	}

	private void testDimensionCreateAndFind(IDocumentManager documentManager)
			throws IOException, BirtException, DataException {
		Dimension dimension = createDimension(documentManager);
		ILevel[] level = dimension.getHierarchy().getLevels();

		IDiskArray indexKeys = dimension.find((Level) level[0], new Object[] { new Integer(1) });
		assertEquals(indexKeys.size(), 1);
		IndexKey indexKey;
		Member levelMember;
		for (int i = 0; i < indexKeys.size(); i++) {
			indexKey = (IndexKey) indexKeys.get(i);
			assertEquals(indexKey.getKey()[0], new Integer(1));
			assertEquals(indexKey.getDimensionPos()[0], i);
			levelMember = dimension.getRowByPosition(indexKey.getDimensionPos()[0]).getMembers()[0];
			assertEquals(levelMember.getKeyValues()[0], new Integer(1));

			levelMember = dimension.getDimensionRowByOffset(indexKey.getOffset()[0]).getMembers()[0];
			assertEquals(levelMember.getKeyValues()[0], new Integer(1));
		}

		// test load dimension from disk
		dimension = (Dimension) DimensionFactory.loadDimension("student", documentManager);

		indexKeys = dimension.find((Level) level[1], new Object[] { new Integer(1) });
		assertEquals(indexKeys.size(), 1);

		indexKey = (IndexKey) indexKeys.get(0);
		assertEquals(indexKey.getKey()[0], new Integer(1));
		assertEquals(indexKey.getDimensionPos()[0], 0);

		levelMember = dimension.getDimensionRowByOffset(indexKey.getOffset()[0]).getMembers()[1];
		assertEquals(levelMember.getKeyValues()[0], new Integer(1));

		indexKey = (IndexKey) indexKeys.get(0);
		assertEquals(indexKey.getKey()[0], new Integer(1));
		assertEquals(indexKey.getDimensionPos()[1], 1);

		levelMember = dimension.getDimensionRowByOffset(indexKey.getOffset()[0]).getMembers()[1];
		assertEquals(levelMember.getKeyValues()[0], new Integer(1));

		indexKey = (IndexKey) indexKeys.get(0);
		assertEquals(indexKey.getKey()[0], new Integer(1));
		assertEquals(indexKey.getDimensionPos()[2], 4);

		levelMember = dimension.getDimensionRowByOffset(indexKey.getOffset()[0]).getMembers()[1];
		assertEquals(levelMember.getKeyValues()[0], new Integer(1));

		indexKey = (IndexKey) indexKeys.get(0);
		assertEquals(indexKey.getKey()[0], new Integer(1));
		assertEquals(indexKey.getDimensionPos()[3], 5);

		levelMember = dimension.getDimensionRowByOffset(indexKey.getOffset()[3]).getMembers()[1];
		assertEquals(levelMember.getKeyValues()[0], new Integer(1));
	}

	private void testDimensionCreateAndFind2(IDocumentManager documentManager)
			throws IOException, BirtException, DataException {
		Dimension dimension = createDimension2(documentManager);
		ILevel[] level = dimension.getHierarchy().getLevels();

		IDiskArray indexKeys = dimension.find((Level) level[0], new Object[] { new Integer(1) });
		assertEquals(indexKeys.size(), 1);
		IndexKey indexKey;
		Member levelMember;
		for (int i = 0; i < indexKeys.size(); i++) {
			indexKey = (IndexKey) indexKeys.get(i);
			assertEquals(indexKey.getKey()[0], new Integer(1));
			assertEquals(indexKey.getDimensionPos()[0], i);
			levelMember = dimension.getRowByPosition(indexKey.getDimensionPos()[0]).getMembers()[0];
			assertEquals(levelMember.getKeyValues()[0], new Integer(1));

			levelMember = dimension.getDimensionRowByOffset(indexKey.getOffset()[0]).getMembers()[0];
			assertEquals(levelMember.getKeyValues()[0], new Integer(1));

		}

		// test load dimension from disk
		dimension = (Dimension) DimensionFactory.loadDimension("dataset2", documentManager);

		indexKeys = dimension.find((Level) level[1], new Object[] { new java.sql.Date(1) });
		assertEquals(indexKeys.size(), 1);

		indexKey = (IndexKey) indexKeys.get(0);
		assertEquals(indexKey.getKey()[0], new java.sql.Date(1));
		assertEquals(indexKey.getDimensionPos()[0], 0);

		levelMember = dimension.getDimensionRowByOffset(indexKey.getOffset()[0]).getMembers()[1];
		assertEquals(levelMember.getKeyValues()[0], new java.sql.Date(1));

		indexKey = (IndexKey) indexKeys.get(0);
		assertEquals(indexKey.getKey()[0], new java.sql.Date(1));
		assertEquals(indexKey.getDimensionPos()[1], 1);

		levelMember = dimension.getDimensionRowByOffset(indexKey.getOffset()[0]).getMembers()[1];
		assertEquals(levelMember.getKeyValues()[0], new java.sql.Date(1));

		indexKey = (IndexKey) indexKeys.get(0);
		assertEquals(indexKey.getKey()[0], new java.sql.Date(1));
		assertEquals(indexKey.getDimensionPos()[2], 4);

		levelMember = dimension.getDimensionRowByOffset(indexKey.getOffset()[0]).getMembers()[1];
		assertEquals(levelMember.getKeyValues()[0], new java.sql.Date(1));

		indexKey = (IndexKey) indexKeys.get(0);
		assertEquals(indexKey.getKey()[0], new java.sql.Date(1));
		assertEquals(indexKey.getDimensionPos()[3], 5);

		levelMember = dimension.getDimensionRowByOffset(indexKey.getOffset()[3]).getMembers()[1];
		assertEquals(levelMember.getKeyValues()[0], new java.sql.Date(1));
		levelMember = dimension.getDimensionRowByOffset(indexKey.getOffset()[3]).getMembers()[2];
		assertEquals(levelMember.getKeyValues()[0], new java.sql.Time(6));
	}

	/**
	 * 
	 * @throws IOException
	 * @throws BirtException
	 */
	@Test
	public void testDimensionCreateAndFind2() throws IOException, BirtException {
		IDocumentManager documentManager = DocumentManagerFactory.createFileDocumentManager();
		testDimensionCreateAndFind2(documentManager);
		documentManager.close();
	}

	/**
	 * 
	 * @throws IOException
	 * @throws BirtException
	 */
	@Test
	public void testDimensionCreateAndFind3() throws IOException, BirtException {
		IDocumentManager documentManager = DocumentManagerFactory.createFileDocumentManager();
		try {
			testDimensionCreateAndFind3(documentManager);
//			fail( "DataException should be thrown!" );
		} catch (DataException e) {
		}
		documentManager.close();
	}

	private void testDimensionCreateAndFind3(IDocumentManager documentManager)
			throws IOException, BirtException, DataException {
		createDimension3(documentManager);
	}

	/**
	 * 
	 * @throws IOException
	 * @throws BirtException
	 */
	@Test
	public void testDimensionIterator() throws IOException, BirtException {
		IDocumentManager documentManager = DocumentManagerFactory.createFileDocumentManager();
		testDimensionIterator(documentManager);
		documentManager.close();
	}

	private void testDimensionIterator(IDocumentManager documentManager)
			throws IOException, BirtException, DataException {
		Dimension dimension = createDimension(documentManager);
		ILevel[] level = dimension.getHierarchy().getLevels();

		ISelection[][] filter = new ISelection[1][1];
		filter[0][0] = SelectionFactory.createRangeSelection(new Object[] { new Integer(1) },
				new Object[] { new Integer(2) }, true, false);
		Level[] findLevel = new Level[1];
		findLevel[0] = (Level) level[1];

		IDiskArray positionArray = dimension.find(findLevel, filter);
		assertEquals(positionArray.size(), 4);
		DimensionResultIterator dimesionResultSet = new DimensionResultIterator(dimension, positionArray,
				new StopSign());
		dimLevel11 = new DimLevel(dimension.getName(), "l1");
		dimLevel12 = new DimLevel(dimension.getName(), "l2");
		assertEquals(dimesionResultSet.getLevelIndex(dimLevel12.getLevelName()), 1);
		assertEquals(dimesionResultSet.getLevelIndex(dimLevel11.getLevelName()), 0);
		assertEquals(dimesionResultSet.getLevelKeyDataType(dimLevel12.getLevelName())[0], DataType.INTEGER_TYPE);
		assertEquals(dimesionResultSet.getLevelKeyDataType(dimLevel11.getLevelName())[0], DataType.INTEGER_TYPE);
		assertEquals(dimesionResultSet.length(), 4);
		dimesionResultSet.seek(0);
		assertEquals(dimesionResultSet.getLevelKeyValue(0)[0], new Integer(1));
		assertEquals(dimesionResultSet.getLevelKeyValue(1)[0], new Integer(1));
		assertEquals(dimesionResultSet.getLevelKeyValue(2)[0], new Integer(1));
		dimesionResultSet.seek(1);
		assertEquals(dimesionResultSet.getLevelKeyValue(0)[0], new Integer(1));
		assertEquals(dimesionResultSet.getLevelKeyValue(1)[0], new Integer(1));
		assertEquals(dimesionResultSet.getLevelKeyValue(2)[0], new Integer(2));
		dimesionResultSet.seek(2);
		assertEquals(dimesionResultSet.getLevelKeyValue(0)[0], new Integer(2));
		assertEquals(dimesionResultSet.getLevelKeyValue(1)[0], new Integer(1));
		assertEquals(dimesionResultSet.getLevelKeyValue(2)[0], new Integer(5));
		dimesionResultSet.seek(3);
		assertEquals(dimesionResultSet.getLevelKeyValue(0)[0], new Integer(2));
		assertEquals(dimesionResultSet.getLevelKeyValue(1)[0], new Integer(1));
		assertEquals(dimesionResultSet.getLevelKeyValue(2)[0], new Integer(6));
	}

	private Dimension createDimension(IDocumentManager documentManager) throws IOException, BirtException {
		ILevelDefn[] levelDefs = new ILevelDefn[3];
		levelDefs[0] = new LevelDefinition("l1", new String[] { "l1" }, null);
		levelDefs[1] = new LevelDefinition("l2", new String[] { "l2" }, null);
		levelDefs[2] = new LevelDefinition("l3", new String[] { "l3" }, null);

		IDimension dimension = DimensionFactory.createDimension("student", documentManager, new Dataset1(), levelDefs,
				false, new StopSign());
		assertEquals(dimension.isTime(), false);
		IHierarchy hierarchy = dimension.getHierarchy();
		assertEquals(hierarchy.getName(), "student");
		ILevel[] level = hierarchy.getLevels();
		assertEquals(level[0].getName(), "l1");
		assertEquals(level[1].getName(), "l2");
		assertEquals(level[2].getName(), "l3");
		return (Dimension) dimension;
	}

	private Dimension createDimension2(IDocumentManager documentManager) throws IOException, BirtException {
		ILevelDefn[] levelDefs = new ILevelDefn[3];
		levelDefs[0] = new LevelDefinition("l1", new String[] { "l1" }, null);
		levelDefs[1] = new LevelDefinition("l2", new String[] { "l2" }, null);
		levelDefs[2] = new LevelDefinition("l3", new String[] { "l3" }, null);

		IDimension dimension = DimensionFactory.createDimension("dataset2", documentManager, new Dataset2(), levelDefs,
				true, new StopSign());
		assertEquals(dimension.isTime(), true);
		IHierarchy hierarchy = dimension.getHierarchy();
		assertEquals(hierarchy.getName(), "dataset2");
		ILevel[] level = hierarchy.getLevels();
		assertEquals(level[0].getName(), "l1");
		assertEquals(level[1].getName(), "l2");
		assertEquals(level[2].getName(), "l3");
		return (Dimension) dimension;
	}

	private Dimension createDimension3(IDocumentManager documentManager) throws IOException, BirtException {
		ILevelDefn[] levelDefs = new ILevelDefn[3];
		levelDefs[0] = new LevelDefinition("l1", new String[] { "l1" }, null);
		levelDefs[1] = new LevelDefinition("l2", new String[] { "l2" }, null);
		levelDefs[2] = new LevelDefinition("l3", new String[] { "l3" }, null);

		IDimension dimension = DimensionFactory.createDimension("dataset3", documentManager, new Dataset3(), levelDefs,
				false, new StopSign());
		assertEquals(dimension.isTime(), false);
		IHierarchy hierarchy = dimension.getHierarchy();
		assertEquals(hierarchy.getName(), "dataset3");
		ILevel[] level = hierarchy.getLevels();
		assertEquals(level[0].getName(), "l1");
		assertEquals(level[1].getName(), "l2");
		assertEquals(level[2].getName(), "l3");
		return (Dimension) dimension;
	}
}

class Dataset1 implements IDatasetIterator {

	int ptr = -1;
	static int[] L1Col = { 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3 };
	static int[] L2Col = { 1, 1, 2, 2, 1, 1, 2, 2, 2, 2, 3, 3 };

	static int[] L3Col = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };

	public void close() throws BirtException {
		// TODO Auto-generated method stub

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
		if (name.equals("l1")) {
			return 0;
		} else if (name.equals("l2")) {
			return 1;
		} else if (name.equals("l3")) {
			return 2;
		}
		return -1;
	}

	public int getFieldType(String name) throws BirtException {
		if (name.equals("l1")) {
			return DataType.INTEGER_TYPE;
		} else if (name.equals("l2")) {
			return DataType.INTEGER_TYPE;
		} else if (name.equals("l3")) {
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
			return new Integer(L1Col[ptr]);
		} else if (fieldIndex == 1) {
			return new Integer(L2Col[ptr]);
		} else if (fieldIndex == 2) {
			return new Integer(L3Col[ptr]);
		}
		return null;
	}

	public boolean next() throws BirtException {
		ptr++;
		if (ptr >= L1Col.length) {
			return false;
		}
		return true;
	}
}

class Dataset2 implements IDatasetIterator {

	int ptr = -1;
	static int[] L1Col = { 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3 };
	static int[] L2Col = { 1, 1, 2, 2, 1, 1, 2, 2, 2, 2, 3, 3 };

	static int[] L3Col = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };

	public void close() throws BirtException {
		// TODO Auto-generated method stub

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
		if (name.equals("l1")) {
			return 0;
		} else if (name.equals("l2")) {
			return 1;
		} else if (name.equals("l3")) {
			return 2;
		}
		return -1;
	}

	public int getFieldType(String name) throws BirtException {
		if (name.equals("l1")) {
			return DataType.INTEGER_TYPE;
		} else if (name.equals("l2")) {
			return DataType.SQL_DATE_TYPE;
		} else if (name.equals("l3")) {
			return DataType.SQL_TIME_TYPE;
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
			return new Integer(L1Col[ptr]);
		} else if (fieldIndex == 1) {
			return new java.sql.Date(L2Col[ptr]);
		} else if (fieldIndex == 2) {
			return new java.sql.Time(L3Col[ptr]);
		}
		return null;
	}

	public boolean next() throws BirtException {
		ptr++;
		if (ptr >= L1Col.length) {
			return false;
		}
		return true;
	}
}

class Dataset3 implements IDatasetIterator {

	int ptr = -1;
	static int[] L1Col = { 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3 };
	static int[] L2Col = { 1, 1, 2, 2, 1, 1, 2, 2, 2, 2, 3, 3 };

	static int[] L3Col = { 1, 2, 3, 4, 3, 6, 7, 8, 9, 10, 11, 12 };

	public void close() throws BirtException {
		// TODO Auto-generated method stub

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
		if (name.equals("l1")) {
			return 0;
		} else if (name.equals("l2")) {
			return 1;
		} else if (name.equals("l3")) {
			return 2;
		}
		return -1;
	}

	public int getFieldType(String name) throws BirtException {
		if (name.equals("l1")) {
			return DataType.INTEGER_TYPE;
		} else if (name.equals("l2")) {
			return DataType.SQL_DATE_TYPE;
		} else if (name.equals("l3")) {
			return DataType.SQL_TIME_TYPE;
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
			return new Integer(L1Col[ptr]);
		} else if (fieldIndex == 1) {
			return new java.sql.Date(L2Col[ptr]);
		} else if (fieldIndex == 2) {
			return new java.sql.Time(L3Col[ptr]);
		}
		return null;
	}

	public boolean next() throws BirtException {
		ptr++;
		if (ptr >= L1Col.length) {
			return false;
		}
		return true;
	}
}

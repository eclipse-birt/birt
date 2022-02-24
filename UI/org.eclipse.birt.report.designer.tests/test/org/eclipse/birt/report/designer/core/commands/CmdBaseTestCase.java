/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.designer.core.commands;

import java.util.HashMap;

import org.eclipse.birt.report.designer.core.DesignerConstants;
import org.eclipse.birt.report.designer.core.model.schematic.TableHandleAdapter;
import org.eclipse.birt.report.designer.testutil.BaseTestCase;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;

/**
 * 
 * This is base Class for command test provides method for creation of
 * table/row/column/cell
 */
public abstract class CmdBaseTestCase extends BaseTestCase {

	protected TableHandle table;

	protected TableHandleAdapter adapter;

	protected RowHandle firstRow, secondRow;

	protected CellHandle firstCell, secondCell;

	protected DataItemHandle dataItem;

	protected ColumnHandle firstColumn;

	protected DataSourceHandle dataSource;

	protected DataSetHandle dataSet;

	protected static final String TEST_TABLE_NAME = "Table";

	protected static final String DATA_SOURCE_NAME = "Data Source";

	protected static final String DATA_SET_NAME = "Data Set";

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();

		table = getElementFactory().newTableItem(TEST_TABLE_NAME);
		getReportDesignHandle().getBody().add(table);
		adapter = new TableHandleAdapter(table, null);
	}

	protected void tearDown() throws SemanticException {
		clearAll(getReportDesignHandle().getBody());
		clearAll(getReportDesignHandle().getDataSources());
		clearAll(getReportDesignHandle().getDataSets());
		dataItem = null;
		firstColumn = null;
		firstCell = secondCell = null;
		firstRow = secondRow = null;
		adapter = null;
		table = null;
		dataSource = null;
	}

	protected ElementFactory getElementFactory() {
		return getReportDesignHandle().getElementFactory();
	}

	protected void createFirstRow() {
		// create first Row in table Detail
		SlotHandle container = table.getDetail();
		firstRow = getElementFactory().newTableRow();
		HashMap map = new HashMap();
		map.put(DesignerConstants.KEY_NEWOBJECT, firstRow);
		CreateCommand command = new CreateCommand(map);
		command.setParent(container);
		command.execute();
	}

	protected void createSecondRow() {
		// create second Row before the firstRow in the table
		// after was set
		secondRow = getElementFactory().newTableRow();
		HashMap map = new HashMap();
		map.put(DesignerConstants.KEY_NEWOBJECT, secondRow);
		CreateCommand command = new CreateCommand(map);
		command.setParent(table.getDetail());
		command.setAfter(firstRow);
		command.execute();

	}

	protected void createFirstCell() {
		// create cell in firstRow
		// with no after set
		firstCell = getElementFactory().newCell();
		try {
			firstCell.setColumn(1);
		} catch (SemanticException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("setColumn 1");
		}
		HashMap map = new HashMap();
		map.put(DesignerConstants.KEY_NEWOBJECT, firstCell);
		CreateCommand command = new CreateCommand(map);
		command.setParent(firstRow);
		command.execute();
	}

	protected void createSecondCell() {
		// create cell in firstFow after the first cell
		// with after set
		secondCell = getElementFactory().newCell();
		try {
			secondCell.setColumn(2);
		} catch (SemanticException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("set Column 2");
		}
		HashMap map = new HashMap();
		map.put(DesignerConstants.KEY_NEWOBJECT, secondCell);
		CreateCommand command = new CreateCommand(map);
		command.setParent(firstRow);
		command.setAfter(firstCell);
		command.execute();
	}

	protected void addDataItems() {
		// add dataItems to firstCell
		dataItem = getElementFactory().newDataItem("DataItem1");
		HashMap map = new HashMap();
		map.put(DesignerConstants.KEY_NEWOBJECT, dataItem);
		CreateCommand command = new CreateCommand(map);
		command.setParent(firstCell);
		command.execute();
	}

	protected void createColumn() {
		firstColumn = getElementFactory().newTableColumn();
		try {
			table.getColumns().add(firstColumn);
		} catch (ContentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("create Column failed");
		}
	}

	protected void createDataSource() {
		dataSource = getElementFactory().newOdaDataSource(DATA_SOURCE_NAME);
		HashMap map = new HashMap();
		map.put(DesignerConstants.KEY_NEWOBJECT, dataSource);
		CreateCommand command = new CreateCommand(map);
		SlotHandle parent = getReportDesignHandle().getDataSources();
		command.setParent(parent);
		command.execute();

	}

	protected void createDataSet() {
		dataSet = getElementFactory().newOdaDataSet(DATA_SET_NAME);
		HashMap map = new HashMap();
		map.put(DesignerConstants.KEY_NEWOBJECT, dataSet);
		CreateCommand command = new CreateCommand(map);
		SlotHandle parent = getReportDesignHandle().getDataSets();
		command.setParent(parent);
		command.execute();

		if (dataSource == null)
			createDataSource();

		try {
			dataSet.setDataSource(DATA_SOURCE_NAME);
		} catch (SemanticException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("add data source to dataset");
		}

	}

	protected void clearAll(SlotHandle handle) throws SemanticException {
		for (int i = 0; i < handle.getCount(); i++) {
			handle.drop(0);
		}
	}

}

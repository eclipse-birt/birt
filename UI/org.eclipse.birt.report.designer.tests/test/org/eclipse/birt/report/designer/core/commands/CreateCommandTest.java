/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.core.commands;

import java.util.HashMap;

import org.eclipse.birt.report.designer.core.DesignerConstants;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.SlotHandle;

/**
 * @author xzhang
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class CreateCommandTest extends CmdBaseTestCase {

	public void testCreateFirstTableRow() {

		assertEquals(0, table.getDetail().getCount());
		createFirstRow();
		assertEquals(firstRow, table.getDetail().get(0));

	}

	public void testCreateSecondTableRow() {

		assertTrue(adapter.getRows().isEmpty());
		createFirstRow();
		createSecondRow();
		assertEquals(secondRow, table.getDetail().get(0));
		assertEquals(firstRow, table.getDetail().get(1));

	}

	public void testCreateFirstCell() {
		assertTrue(adapter.getRows().isEmpty());
		createFirstRow();
		createFirstCell();
		assertEquals(firstCell, firstRow.getCells().get(0));
	}

	public void testCreateSecondCell() {
		assertTrue(adapter.getRows().isEmpty());
		createFirstRow();
		createFirstCell();
		createSecondCell();
		assertEquals(secondCell, firstRow.getCells().get(0));
		assertEquals(firstCell, firstRow.getCells().get(1));
	}

	public void testAddCell2Table() {
		// It is not allowed to add cell to table.detail
		assertTrue(adapter.getRows().isEmpty());
		CellHandle cell = getElementFactory().newCell();
		HashMap map = new HashMap();
		map.put(DesignerConstants.KEY_NEWOBJECT, cell);
		CreateCommand command = new CreateCommand(map);
		command.setParent(table.getDetail());
		command.execute();
		assertEquals(0, table.getDetail().getCount());
	}

	public void testCreateDataItem() {
		createFirstRow();
		createFirstCell();
		addDataItems();
		assertEquals(1, firstCell.getContent().getCount());
		assertEquals(dataItem, firstCell.getContent().get(0));
	}

	public void testCreateFirstTableColumn() {

		assertNull(adapter.getColumn(1));
		createColumn();
		assertNotNull(adapter.getColumn(1));
		assertEquals(firstColumn, table.getColumns().get(0));

	}

	public void testCreateDataSource() {
		SlotHandle parent = getReportDesignHandle().getDataSources();
		assertEquals(0, parent.getCount());

		createDataSource();

		assertEquals(1, parent.getCount());
		assertEquals(dataSource, parent.get(0));
	}

	public void testCreateDataSourceWithSameName() {

		SlotHandle parent = getReportDesignHandle().getDataSources();
		assertEquals(0, parent.getCount());
		createDataSource();
		assertEquals(1, parent.getCount());

		DataSourceHandle dataSource2 = getElementFactory().newOdaDataSource(DATA_SOURCE_NAME);
		HashMap map = new HashMap();
		map.put(DesignerConstants.KEY_NEWOBJECT, dataSource2);
		CreateCommand command = new CreateCommand(map);
		command.setParent(parent);
		command.execute();

		assertEquals(2, parent.getCount());
		assertEquals(dataSource2, parent.get(1));
		assertTrue(!dataSource2.getName().equals(DATA_SOURCE_NAME));
	}

	public void testCreateDataSet() {
		SlotHandle parent = getReportDesignHandle().getDataSets();
		assertEquals(0, parent.getCount());
		createDataSet();
		assertEquals(1, parent.getCount());
		assertEquals(dataSet, parent.get(0));
	}

}
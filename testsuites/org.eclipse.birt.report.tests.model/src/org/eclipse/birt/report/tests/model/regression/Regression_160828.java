/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.TableRow;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * <b>Regression description:</b>
 * <p>
 * The table will become an error item when set a drop
 * <p>
 * Step: <br>
 * 1.New a report<br>
 * 2.Add a group<br>
 * 3.Select the cell on group and set drop as detail. <br>
 * 4.Close the report and reopen it. <br>
 * Actual result: The table in layout is become an error item <br>
 * Excepted result: The table is still a table
 * <p>
 * <b>Test description:</b>
 * <p>
 * Test as the description
 * <p>
 */
public class Regression_160828 extends BaseTestCase {

	public void test_regression_160828() throws Exception {
		DesignEngine engine = new DesignEngine(new DesignConfig());
		SessionHandle session = engine.newSessionHandle(ULocale.ENGLISH);
		ReportDesignHandle designHandle = session.createDesign();

		// create a table
		ElementFactory factory = designHandle.getElementFactory();
		TableHandle table = factory.newTableItem("table", 2, 1, 1, 1);
		CellHandle cell = getCellInTableSlot(table, TableItem.DETAIL_SLOT, 0, 0);
		assertNotNull(cell);

		// specify a table group on the table
		TableGroupHandle group = factory.newTableGroup();
		table.getGroups().add(group);

		// set the drop on cell
		cell.setDrop(DesignChoiceConstants.DROP_TYPE_DETAIL);
		assertTrue(table.isValidLayoutForCompoundElement());

	}

	private CellHandle getCellInTableSlot(TableHandle table, int slotID, int rowIndex, int cellIndex) {
		RowHandle row = (RowHandle) (table.getSlot(slotID).get(rowIndex));
		CellHandle cell = (CellHandle) (row.getSlot(TableRow.CONTENT_SLOT).get(cellIndex));
		return cell;
	}
}

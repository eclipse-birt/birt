
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
package org.eclipse.birt.report.engine.parser;

import org.eclipse.birt.report.engine.ir.CellDesign;
import org.eclipse.birt.report.engine.ir.DataItemDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.ir.RowDesign;
import org.eclipse.birt.report.engine.ir.TableBandDesign;
import org.eclipse.birt.report.engine.ir.TableItemDesign;

/**
 * Test the suppressDuplicates set in column while it will be set in the
 * relational cell's dataItem
 */

public class SuppressDuplicateDataItemTest extends AbstractDesignTestCase {
	public void testSuppressDuplicate() {
		loadDesign("suppressDuplicate_test.xml");

		TableItemDesign table = (TableItemDesign) report.getContent(0);
		assertTrue(table != null);

		TableBandDesign detail = (TableBandDesign) table.getDetail();
		assertTrue(detail.getRowCount() > 0);

		RowDesign row = detail.getRow(0);
		assertTrue(row.getCellCount() > 0);

		CellDesign cell = row.getCell(0);
		assertTrue(cell.getContentCount() > 0);

		ReportItemDesign item = cell.getContent(0);
		assertTrue(item instanceof DataItemDesign);

		DataItemDesign data = (DataItemDesign) item;
		assertEquals(true, data.getSuppressDuplicate());

	}
}


/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
